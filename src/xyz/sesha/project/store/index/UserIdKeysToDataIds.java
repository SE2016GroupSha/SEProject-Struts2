package xyz.luxin.java.se.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.Data;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ1��user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>��ʽ2��user:[id]:search:data:fuzzy:[key]      ->(id1, id2...)
 * <br>˵����ͨ��user��id�͹ؼ���,��ȡȫ�����ڹؼ��ֵ�data��id��һ�Զ�ӳ��
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {

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
          Long pdoId = data0.getLong("pdo");
          String pdoJsonString = jedis.get("pdo:"+String.valueOf(pdoId));
          JSONObject pdoJsonObj = JSONObject.fromObject(pdoJsonString);
          Long userId = pdoJsonObj.getLong("user");
          
          //������������ӵ�data
          for (String jsonString : jsonStrings) {
            JSONObject dataJsonObj = JSONObject.fromObject(jsonString);
            String dataId = String.valueOf(dataJsonObj.getLong("id"));
            JSONArray valueArray = dataJsonObj.getJSONArray("values");
            
            //����data������value
            for (int i=0; i<valueArray.size(); i++) {
              String value = valueArray.getString(i);
              
              //��ÿ��value������Ӵ�������word<->dataIdSet��Map(word�洢Ϊת�����Ŀ��ֵ)
              for (int j=0; j<value.length(); j++) {
                for (int k=j; k<=value.length(); k++) {
                  String word = value.substring(j, k);
                  String key = "user:" + String.valueOf(userId) + ":search:data:fuzzy:" + word;
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
          String allKey = "user:" + String.valueOf(userId) + ":search:data:fuzzy:all";
          jedis.sadd(allKey, keys);
          
          //�����ʽ2��Redis
          for (Entry<String, TreeSet<String>> entry : keysMap.entrySet()) {
            String key = entry.getKey();
            TreeSet<String> idSet = entry.getValue();
            String[] ids = idSet.toArray(new String[idSet.size()]);
            jedis.sadd(key, ids);
          }
          
        } catch (Exception e) {
          System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdKeysToDataIds] ��̬��ʼ�����");
  }
  
  /**
   * ����user��id�͹ؼ��ʣ�ģ������������data��id��List
   * @param userId user��id
   * @param keys �ؼ�������
   * @return data��id��List
   */
  public static List<String> getIds(long userId, Collection<String> keys) {
    
    Set<String> ids = new TreeSet<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      
      //�ؼ���ȥ�أ������ɿ��ֵ
      TreeSet<String> keySet = new TreeSet<String>();
      for (String key : keys) {
        String dbKey = "user:" + String.valueOf(userId) + ":search:data:fuzzy:" + key;
        keySet.add(dbKey);
      }
      
      //Redis���Setȡ����
      if (!keySet.isEmpty()) {
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //�������ڵ������RedisӦ���Ƿ��ؿռ��ϣ�����null
        ids = jedis.sinter(keyArray);
      }

    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } finally {
      jedis.close();
    }
    
    return new ArrayList<String>(ids);
  }

}
