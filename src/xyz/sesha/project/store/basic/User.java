package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.store.index.UserNameToUserId;
import xyz.sesha.project.utils.JedisUtil;

/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�user
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, nameȫ�ⲻ�ظ�, pwhashΪ����32λmd5ֵ(Сд), time����Ϊuser����ʱ��(����)
 * <br>������ʽ��{"id":"0", "time":1477410793369, "name":"��ү", "pwhash":"5e007e7046425c92111676b1b0999f12"}
 * <br>�洢��ʽ��user:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Lu Xin
 */
public class User {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(User.class);
  
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
   * ����user��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param userJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkUserJsonFormat(Collection<String> userJsons) {
    boolean ret = true;
    
    for (String userJson : userJsons) {
      try {
        JSONObject json = JSONObject.fromObject(userJson);
        
        //�ж�key�Ƿ����
        if (!json.has("id") || !json.has("time") || !json.has("name") || !json.has("pwhash")) {
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

        //name���ͣ�java.lang.String
        Object nameObj = json.get("name");
        if (!(nameObj instanceof String)) {
          ret =  false;
        }
        
        //pwhash���ͣ�java.lang.String
        Object pwhashObj = json.get("pwhash");
        if (!(pwhashObj instanceof String)) {
          ret =  false;
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
   * ���user����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param userJsons user��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddUser(Collection<String> userJsons) {
    
    //�ռ����ж�
    if (userJsons.isEmpty()) {
      return false;
    }
    
    //���ݸ�ʽ����
    if (!checkUserJsonFormat(userJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    for (String userJson : userJsons) {
      JSONObject json = JSONObject.fromObject(userJson);
      jsons.add(json);

      String name = json.getString("name");
      //�û����ǿ�
      if (name.equals("")) {
        logger.error("[Error][User][Add]���������ʱname�ǿ��ַ���");
        return ret;
      }
      //�û������ظ�
      if (null != UserNameToUserId.getId(name)) {
        logger.error("[Error][User][Add]���������ʱname�Ѵ���");
        return ret;
      }
    }
    
    //TODO:DB_INDEX_KEY����ͬ������
    
    //��ʼ�������
    Jedis jedis = JedisUtil.getJedis();
    try {
      //��ȡDBIndex
      long DBIndex = -1L;
      DBIndex = Long.valueOf(jedis.get(DB_INDEX_KEY));
      
      //��������ݣ������²���List�ڵ�id
      userJsons.clear();
      for (JSONObject json : jsons) {
        String key = "user:" + String.valueOf(DBIndex);
        json.put("id", String.valueOf(DBIndex));
        jedis.set(key, json.toString());
        userJsons.add(json.toString());
        DBIndex++;
      }
      
      //����DBIndex
      jedis.set(DB_INDEX_KEY, String.valueOf(DBIndex));
      
      ret = true; 
    } catch (NumberFormatException e) {
      logger.error("[Error][User][Add]�����ݿ�DBIndex�쳣");
      e.printStackTrace();
      ret = false;
    } catch (Exception e) {
      logger.error("[Error][User][Add]����������");
      e.printStackTrace();
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * ���user�ķ��������ⲿ����
   * @param userJsons user��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addUser(Collection<String> userJsons) {
    
    boolean ret = false;
    
    //ת����innerAddData����
    ret = innerAddUser(userJsons);
    
    //�ɹ���ִ��������������
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(userJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * ���user�ķ��������ⲿ����
   * @param userJson user��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  @SuppressWarnings("serial")
  public static boolean addUser(String userJson) {
    return addUser(new ArrayList<String>(){{this.add(userJson);}});
  }
  
  /**
   * ����id���ж�user�Ƿ����
   * @param id user��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasUser(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * ����id������user��json�ַ���
   * @param id user��id
   * @return ����user��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getUserJson(String id) {
    List<String> ret = getUserJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * ����id������user��json�ַ���
   * @param ids user��id������
   * @return ����user��json�ַ�����List
   */
  public static List<String> getUserJson(Collection<String> ids) {
    //TODO:����List��null�ж�
    List<String> ret = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "user:" + id;
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
