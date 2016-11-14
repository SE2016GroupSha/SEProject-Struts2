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
 * <p>��ʽ1��user:[id]:search:data:fuzzy:all         ->(key1, key2...)
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
        
        Jedis jedis = JedisUtil.getJedis();

        //word<->dataIdSet��Map(word�洢Ϊת�����Ŀ��ֵ)
        TreeMap<String, TreeSet<String>> keysMap = new TreeMap<String, TreeSet<String>>();
        
        //ȫ��word��Set(word�洢Ϊ��ֵ)
        TreeSet<String> rawKeySet = new TreeSet<String>();
        try {
          
          //��ִ�й��� ������˵��data�Ϸ����Ѿ���֤���û���һ���뵱ǰ��ͬ,data���ϲ�Ϊ��,pdoȫ���Ϸ�
          
          //��ȡ����data���ݵ�user��id
          JSONObject data0 = JSONObject.fromObject(jsonStrings.iterator().next());
          String pdoId = data0.getString("pdo");
          String pdoJsonString = jedis.get("pdo:"+pdoId);
          JSONObject pdoJsonObj = JSONObject.fromObject(pdoJsonString);
          String userId = pdoJsonObj.getString("user");
          
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
