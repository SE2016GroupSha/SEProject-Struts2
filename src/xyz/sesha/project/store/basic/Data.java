package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.utils.JedisUtil;


/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�data
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, time����Ϊdata����ʱ�� �� �û��ֶ���д��ʱ��(����)
 * <br>������ʽ��{"id":"4", "time":1477412545804, "pdo": "1", "values": ["��", "ѧУ", "10����"], "related_data": ["5", "6"]}
 * <br>�洢��ʽ��data:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Lu Xin
 */
public class Data {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(Data.class);
  
  /**
   * ��Ӳ���֮��,ִ�е�ȫ����������
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();
  
  /**
   * <pr>���ϻ������ݵ�idȫ��Ψһ�ļ�������洢��������
   * <br>ע�⣺���ֻ���ڵ��û������ӻ��������û����������ͬ������
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * ����data��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param dataJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkDataJsonFormat(Collection<String> dataJsons) {
    boolean ret = true;
    
    for (String dataJson : dataJsons) {
      try {
        JSONObject json = JSONObject.fromObject(dataJson);
        
        //�ж�key�Ƿ����
        if (!json.has("id") || !json.has("time") || !json.has("pdo") 
            || !json.has("values") || !json.has("related_data")) {
          ret =  false;
        }
        
        //nullֵ�жϣ�������instanceof�ؼ�����
        
        //id���ͣ�java.lang.String
        Object idObj = json.get("id");
        if (!(idObj instanceof String)) {
          ret =  false;
        }
        
        //time���ͣ�java.lang.Long/java.lang.Integer
        Object timeObj = json.get("time");
        if (!(timeObj instanceof Long) && !(timeObj instanceof Integer)) {
          ret =  false;
        }
        
        //pdo���ͣ�java.lang.String
        Object pdoObj = json.get("pdo");
        if (!(pdoObj instanceof String)) {
          ret =  false;
        }
        
        //values���ͣ�net.sf.json.JSONArray
        Object valuesObj = json.get("values");
        if (!(valuesObj instanceof JSONArray)) {
          ret =  false;
        }
        //values���ƣ����鲻Ϊ��
        if (((JSONArray) valuesObj).size() <= 0) {
          ret =  false;
        }
        //values���ƣ���Ա���ͣ�java.lang.String
        for (int i=0; i<((JSONArray) valuesObj).size(); i++) {
          if (!(((JSONArray) valuesObj).get(i) instanceof String)) {
            ret =  false;
          }
        }
        
        //related_data���ͣ�net.sf.json.JSONArray
        Object relatedDataObj = json.get("related_data");
        if (!(relatedDataObj instanceof JSONArray)) {
          ret =  false;
        }
        //related_data���ƣ���Ա���ͣ�java.lang.String
        for (int i=0; i<((JSONArray) relatedDataObj).size(); i++) {
          if (!(((JSONArray) relatedDataObj).get(i) instanceof String)) {
            ret =  false;
          }
        }
        
      } catch (JSONException e) {
        ret =  false;
      } catch (Exception e) {
        ret =  false;
      }
    }
    
    return ret;
  }
  
  /**
   * ���data����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param dataJsons data��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddData(Collection<String> dataJsons) {
    
    //�ռ����ж�
    if (dataJsons.isEmpty()) {
      return false;
    }
    
    //���ݸ�ʽ����
    if (!checkDataJsonFormat(dataJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    
    for (String dataJson : dataJsons) {
      JSONObject json = JSONObject.fromObject(dataJson);
      jsons.add(json);

      String pdoId = json.getString("pdo");
      
      //TODO:���ڷǷ�д�������û��Ŀ���
      
      //��֤pdo�Ϸ���
      if (!PDO.hasPDO(pdoId)) {
        logger.error("[Error][Data][Add]���������ʱpdo��������");
        return ret;
      }

      //TODO:�ж�����PDO��user�뵱ǰuser��ͬ

      //pdo��fields��data��values����Ӧ����ͬ
      JSONObject pdoJsonObj = JSONObject.fromObject(PDO.getPDOJson(pdoId));
      JSONArray pdoFields = pdoJsonObj.getJSONArray("fields");
      JSONArray dataValues = json.getJSONArray("values");
      if (pdoFields.size() != dataValues.size()) {
        logger.error("[Error][Data][Add]���������ʱdata�ֶ���Ŀ��pdo�ֶ���Ŀ��ƥ��");
        return ret;
      }
      
      //��֤�������ݺϷ���
      JSONArray relatedData = json.getJSONArray("related_data");
      if (relatedData.size() > 0) {
        String[] relatedIds = new String[relatedData.size()];
        for (int i=0; i<relatedData.size(); i++) {
          relatedIds[i] = relatedData.getString(i);
        }
        for (String id : relatedIds) {
          if (!Data.hasData(id)) {
            logger.error("[Error][Data][Add]���������ʱdata�������ݲ�����");
            return ret;
          }
        }
      }

    }
    
    //TODO:DB_INDEX_KEY����ͬ������
    
    //��ʼ�������
    Jedis jedis = JedisUtil.getJedis();
    try {
      //��ȡDBIndex
      long DBIndex = -1L;
      DBIndex = Long.valueOf(jedis.get(DB_INDEX_KEY));
      
      //���ɼ�ֵ�ԣ������²���List�ڵ�id
      dataJsons.clear();
      List<String> keyValues = new ArrayList<String>();
      for (JSONObject json : jsons) {
        String key = "data:" + String.valueOf(DBIndex);
        json.put("id", String.valueOf(DBIndex));
        dataJsons.add(json.toString());
        keyValues.add(key);
        keyValues.add(json.toString());
        DBIndex++;
      }
      
      //����DBIndex
      jedis.set(DB_INDEX_KEY, String.valueOf(DBIndex));
      
      //���������
      jedis.mset(keyValues.toArray(new String[keyValues.size()]));
      
      //���¹�������
      
      //�ռ����й������ݵ�key��ȥ�ظ�
      Set<String> relatedAllIds = new TreeSet<String>();
      for (JSONObject json : jsons) {
        JSONArray relatedData = json.getJSONArray("related_data");
        if (relatedData.size() > 0) {
          String[] relatedIds = new String[relatedData.size()];
          for (int i=0; i<relatedData.size(); i++) {
            relatedIds[i] = relatedData.getString(i);
          }
          for (String id : relatedIds) {
            String key = "data:" + id;
            relatedAllIds.add(key);
          }
        }
      }
      
      //�������ݸ�����Ϊ0
      if (relatedAllIds.size() > 0) {
        
        //�������ݵ�key��Setת����
        String[] relatedAllArray = relatedAllIds.toArray(new String[relatedAllIds.size()]);
        
        //redis��ȡȫ���������ݣ����ظ�
        List<String> relatedAllString = jedis.mget(relatedAllArray);
        
        //Ϊȫ���������ݽ���id->json��Map��idΨһ������
        Map<String, JSONObject> relatedMap = new TreeMap<String, JSONObject>();
        for (String relatedString : relatedAllString) {
          JSONObject relatedJson = JSONObject.fromObject(relatedString);
          relatedMap.put(relatedJson.getString("id"), relatedJson);
        }
        
        //��ÿ����data��ÿ����������id����Map���ҵ�������������related_data
        for (JSONObject json : jsons) {
          String dataId = json.getString("id");
          JSONArray relatedData = json.getJSONArray("related_data");
          if (relatedData.size() > 0) {
            String[] relatedIds = new String[relatedData.size()];
            for (int i=0; i<relatedData.size(); i++) {
              relatedIds[i] = relatedData.getString(i);
            }
            for (String id : relatedIds) {
              JSONObject relatedJson = relatedMap.get(id);
              JSONArray relatedArray = relatedJson.getJSONArray("related_data");
              if (!relatedArray.contains(dataId)) {
                relatedArray.add(dataId);
              }
              relatedJson.put("related_data", relatedArray);
              relatedMap.replace(id, relatedJson);
            }
          }
        }
        
        //redis����ȫ����������
        keyValues.clear();
        for (Entry<String, JSONObject> entry : relatedMap.entrySet()) {
          String key = "data:" + entry.getKey();
          keyValues.add(key);
          keyValues.add(entry.getValue().toString());
        }
        jedis.mset(keyValues.toArray(new String[keyValues.size()]));
      }
      
      ret = true; 
    } catch (NumberFormatException e) {
      logger.error("[Error][Data][Add]�����ݿ�DBIndex�쳣");
      e.printStackTrace();
      ret = false;
    } catch (Exception e) {
      logger.error("[Error][Data][Add]����������");
      e.printStackTrace();
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * ���data�ķ��������ⲿ����
   * @param dataJsons data��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addData(Collection<String> dataJsons) {
    
    boolean ret = false;
    
    //ת����innerAddData����
    ret = innerAddData(dataJsons);
    
    //�ɹ���ִ��������������
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(dataJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * ���data�ķ��������ⲿ����
   * @param dataJson data��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  @SuppressWarnings("serial")
  public static boolean addData(String dataJson) {
    return addData(new ArrayList<String>(){{this.add(dataJson);}});
  }
  
  /**
   * ����id���ж�data�Ƿ����
   * @param id data��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasData(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "data:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * ����id������data��json�ַ���
   * @param id data��id
   * @return ����data��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getDataJson(String id) {
    List<String> ret = getDataJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * ����id������data��json�ַ���
   * @param ids data��id������
   * @return ����data��json�ַ�����List
   */
  public static List<String> getDataJson(Collection<String> ids) {
    //TODO:����List��null�ж�
    List<String> ret = new ArrayList<String>();;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "data:" + id;
        keys.add(key);
      }
      if (keys.size() > 0) {
        ret = jedis.mget(keys.toArray(new String[keys.size()]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
}
