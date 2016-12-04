package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;
import xyz.sesha.project.utils.JedisUtil;
import xyz.sesha.project.utils.UserUtil;

/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�pdo
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, user���������û���id, nameͬ�û����ظ�, time����ΪPDO����ʱ��(����)
 * <br>������ʽ��{"id":"1", "time":1477410877415, "user":"0", "name":"����", "fields":["ʼ��", "�յ�", "��ʱ"]}
 * <br>�洢��ʽ��pdo:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Lu Xin
 */
public class PDO {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(PDO.class);
  
  /**
   * ��Ӳ���֮��,ִ�е�ȫ����������
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();
  
  /**
   * ����pdo��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param pdoJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkPDOJsonFormat(Collection<String> pdoJsons) {
    boolean ret = true;
    
    for (String pdoJson : pdoJsons) {
      try {
        JSONObject json = JSONObject.fromObject(pdoJson);
        
        //�ж�key�Ƿ����
        if (!json.has("id") || !json.has("time") || !json.has("user") 
            || !json.has("name") || !json.has("fields")) {
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
        
        //user���ͣ�java.lang.String
        Object userObj = json.get("user");
        if (!(userObj instanceof String)) {
          ret =  false;
        }
        
        //name���ͣ�java.lang.String
        Object nameObj = json.get("name");
        if (!(nameObj instanceof String)) {
          ret =  false;
        }
        
        
        //fields���ͣ�net.sf.json.JSONArray
        Object fieldsObj = json.get("fields");
        if (!(fieldsObj instanceof JSONArray)) {
          ret =  false;
        }
        //fields���ƣ����鲻Ϊ��
        if (((JSONArray) fieldsObj).size() <= 0) {
          ret =  false;
        }
        //fields���ƣ���Ա���ͣ�java.lang.String
        for (int i=0; i<((JSONArray) fieldsObj).size(); i++) {
          if (!(((JSONArray) fieldsObj).get(i) instanceof String)) {
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
   * ���pdo����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param pdoJsons pdo��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddPDO(Collection<String> pdoJsons) {
    
    //�ռ����ж�
    if (pdoJsons.isEmpty()) {
      return false;
    }
    
    //���ݸ�ʽ����
    if (!checkPDOJsonFormat(pdoJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    for (String pdoJson : pdoJsons) {
      JSONObject json = JSONObject.fromObject(pdoJson);
      
      String userId = UserUtil.getUserId();
      //�жϵ�½
      if (userId==null) {
        logger.error("[Error][PDO][Add]��δ��¼");
        return ret;
      }
      
      //��дuser������
      json.put("user", userId);
      jsons.add(json);
      
      
      String name = json.getString("name");
        
      //pdo��name�ǿ�
      if (name.equals("")) {
        logger.error("[Error][PDO][Add]���������ʱname�ǿ��ַ���");
        return ret;
      }
      //��֤pdo��name�Ƿ����
      if (null != UserIdPDONameToPDOId.getId(userId, name)) {
        logger.error("[Error][PDO][Add]���������ʱͬuser�¸�pdo��name�Ѵ���");
        return ret;
      }
    }

    //��ʼ�������
    Jedis jedis = JedisUtil.getJedis();
    try {
      //��������ݣ������²���List�ڵ�id��time
      pdoJsons.clear();
      for (JSONObject json : jsons) {
        //����UUID
        String uuid = UUID.randomUUID().toString();
        String key = "pdo:" + uuid;
        json.put("id", uuid);
        json.put("time", System.currentTimeMillis()); //���ʱ�����PDO����ʱ��
        jedis.set(key, json.toString());
        pdoJsons.add(json.toString());
      }
      ret = true; 
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[Error][PDO][Add]����������");
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * ���pdo�ķ��������ⲿ����
   * @param pdoJsons pdo��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addPDO(Collection<String> pdoJsons) {
    
    boolean ret = false;
    
    //ת����innerAddPDO����
    ret = innerAddPDO(pdoJsons);
    
    //�ɹ���ִ��������������
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(pdoJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * ���pdo�ķ��������ⲿ����
   * @param pdoJson pdo��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  @SuppressWarnings("serial")
  public static boolean addPDO(String pdoJson) {
    return addPDO(new ArrayList<String>(){{this.add(pdoJson);}});
  }
  
  /**
   * ����id���ж�pdo�Ƿ����
   * @param id pdo��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasPDO(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "pdo:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * ����id������pdo��json�ַ���
   * @param id pdo��id
   * @return ����pdo��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getPDOJson(String id) {
    List<String> ret = getPDOJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * ����id������pdo��json�ַ���
   * @param ids pdo��id������
   * @return ����pdo��json�ַ�����List
   */
  public static List<String> getPDOJson(Collection<String> ids) {
    //TODO:����List��null�ж�
    List<String> ret = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "pdo:" + id;
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
