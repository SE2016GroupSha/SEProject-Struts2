package xyz.sesha.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.sesha.project.store.basic.Data;
import xyz.sesha.project.utils.JedisUtil;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ1(��ΣBug����)��user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>��ʽ2��user:[id]:search:data:fuzzy:[key]      ->(id1, id2...)
 * <br>˵����ͨ��user��id�͹ؼ���,��ȡȫ�����ڹؼ��ֵ�data��id��һ�Զ�ӳ��
 * 
 * @author Si Aoran
 */
public class UserIdKeysToDataIds {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdKeysToDataIds.class);
  
  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    //���data֮�󣬸�������
    Data.afterAddHook.add(new HookFunction() {

      @Override
      public void func(Collection<String> jsonStrings) {
        //˼·��Ϊÿ��data��values�����ÿ���ַ�����ÿ���Ӵ���������һ��word->dataIds������Set����ȵ�word��ӦͬһSet
        //2����չ������data��pdo�����ƣ�data��pdo��fields
        
        Jedis jedis = JedisUtil.getJedis();

        //word<->dataIdSet��Map(word�洢Ϊת�����Ŀ��ֵ)
        TreeMap<String, TreeSet<String>> keysMap = new TreeMap<String, TreeSet<String>>();
        
        //ȫ��word��Set(word�洢Ϊ��ֵ)
        TreeSet<String> rawKeySet = new TreeSet<String>();
        try {
          
          //��ִ�й��� ������˵��data�Ϸ����Ѿ���֤���û���һ���뵱ǰ��ͬ,data���ϲ�Ϊ��,pdoȫ���Ϸ�
          
          //��ȡ����data���ݵ�user��id
          JSONObject data0 = JSONObject.fromObject(jsonStrings.iterator().next());
          String pdoId0 = data0.getString("pdo");
          String pdoJsonString0 = jedis.get("pdo:"+pdoId0);
          JSONObject pdoJsonObj0 = JSONObject.fromObject(pdoJsonString0);
          String userId = pdoJsonObj0.getString("user");
          
          //������������ӵ�data
          for (String jsonString : jsonStrings) {
            JSONObject dataJsonObj = JSONObject.fromObject(jsonString);
            String dataId = dataJsonObj.getString("id");
            JSONArray valueArray = dataJsonObj.getJSONArray("values");
            
            //����data������value
            for (int i=0; i<valueArray.size(); i++) {
              String value = valueArray.getString(i);
              
              //��ÿ��value������Ӵ�������word<->dataIdSet��Map(word�洢Ϊת�����Ŀ��ֵ)
              for (int j=0; j<value.length(); j++) {
                for (int k=j; k<=value.length(); k++) {
                  String word = value.substring(j, k);
                  String key = "user:" + userId + ":search:data:fuzzy:" + word;
                  rawKeySet.add(word); //�����ռ�word��ֵ
                  if (keysMap.containsKey(key)) {
                    TreeSet<String> wordValueSet = keysMap.get(key);
                    wordValueSet.add(dataId);
                    keysMap.replace(key, wordValueSet);
                  } else {
                    TreeSet<String> wordValueSet = new TreeSet<String>();
                    wordValueSet.add(dataId);
                    keysMap.put(key, wordValueSet);
                  }
                }
              }
            }
            
            //-----------������pdo��name��fields(��ʱ��������ȡpdo��Json)------------------------
            String pdoId = dataJsonObj.getString("pdo");
            JSONObject pdoJsonObj = JSONObject.fromObject(jedis.get("pdo:"+pdoId));
            String pdoName = pdoJsonObj.getString("name");
            JSONArray fieldArray = pdoJsonObj.getJSONArray("fields");
            
            //����pdo��name
            for (int j=0; j<pdoName.length(); j++) {
              for (int k=j; k<=pdoName.length(); k++) {
                String word = pdoName.substring(j, k);
                String key = "user:" + userId + ":search:data:fuzzy:" + word;
                rawKeySet.add(word); //�����ռ�word��ֵ
                if (keysMap.containsKey(key)) {
                  TreeSet<String> wordValueSet = keysMap.get(key);
                  wordValueSet.add(dataId);
                  keysMap.replace(key, wordValueSet);
                } else {
                  TreeSet<String> wordValueSet = new TreeSet<String>();
                  wordValueSet.add(dataId);
                  keysMap.put(key, wordValueSet);
                }
              }
            }
            
            //����pdo������field
            for (int i=0; i<fieldArray.size(); i++) {
              String field = fieldArray.getString(i);
              
              //��ÿ��field������Ӵ�������word<->dataIdSet��Map(word�洢Ϊת�����Ŀ��ֵ)
              for (int j=0; j<field.length(); j++) {
                for (int k=j; k<=field.length(); k++) {
                  String word = field.substring(j, k);
                  String key = "user:" + userId + ":search:data:fuzzy:" + word;
                  rawKeySet.add(word); //�����ռ�word��ֵ
                  if (keysMap.containsKey(key)) {
                    TreeSet<String> wordValueSet = keysMap.get(key);
                    wordValueSet.add(dataId);
                    keysMap.replace(key, wordValueSet);
                  } else {
                    TreeSet<String> wordValueSet = new TreeSet<String>();
                    wordValueSet.add(dataId);
                    keysMap.put(key, wordValueSet);
                  }
                }
              }
            }
            
          }
          
          //�����ʽ1��Redis
          String[] keys = rawKeySet.toArray(new String[rawKeySet.size()]);
          String allKey = "user:" + userId + ":search:data:fuzzy:all";
          jedis.sadd(allKey, keys);
          
          //�����ʽ2��Redis
          for (Entry<String, TreeSet<String>> entry : keysMap.entrySet()) {
            String key = entry.getKey();
            TreeSet<String> idSet = entry.getValue();
            String[] ids = idSet.toArray(new String[idSet.size()]);
            jedis.sadd(key, ids);
          }
          
        } catch (Exception e) {
          logger.error("[Error][UserIdKeysToDataIds]: �ڲ�����");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    logger.info("[UserIdKeysToDataIds] ��̬��ʼ�����");
  }
  
  /**
   * �����ؽ�����,����
   */
  public static void rebuildIndex() {
  //public static void main(String[] args) {
    
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      
      Set<String> oldIndexKeys = jedis.keys("user:*:search:data:fuzzy:*");
      Long oldKeysCount = 0L;
      if (oldIndexKeys.size() > 0) {
        String[] oldIndexKeysArray = oldIndexKeys.toArray(new String[oldIndexKeys.size()]);
        oldKeysCount = jedis.del(oldIndexKeysArray);
      }
      
      System.out.println(oldKeysCount);
      System.out.println(Data.afterAddHook.size());
      
      Set<String> allDataKeys = jedis.keys("data:*");
      System.out.println(allDataKeys.size());
      for (String key : allDataKeys) {
        System.out.println(key);
        String json = jedis.get(key);
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(json);
        Data.afterAddHook.get(0).func(tmp);
      }
      
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
  }
  
  /**
   * ����user��id�͹ؼ��ʣ�ģ������������data��id��List
   * @param userId user��id
   * @param keys �ؼ�������
   * @return data��id��List
   */
  public static List<String> getIds(String userId, Collection<String> keys) {
    
    Set<String> ids = new TreeSet<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      
      //�ؼ���ȥ�أ������ɿ��ֵ
      TreeSet<String> keySet = new TreeSet<String>();
      for (String key : keys) {
        String dbKey = "user:" + userId + ":search:data:fuzzy:" + key;
        keySet.add(dbKey);
      }
      
      //Redis���Setȡ����
      if (!keySet.isEmpty()) {
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //�������ڵ������RedisӦ���Ƿ��ؿռ��ϣ�����null
        ids = jedis.sinter(keyArray);
      }

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[Error][UserIdKeysToDataIds]: �ڲ�����");
    } finally {
      jedis.close();
    }
    
    return new ArrayList<String>(ids);
  }

}
