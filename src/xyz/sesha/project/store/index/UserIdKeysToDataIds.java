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
 * 后端数据请求功能类：数据索引功能类
 * 
 * <p>形式1：user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>形式2：user:[id]:search:data:fuzzy:[key]      ->(id1, id2...)
 * <br>说明：通过user的id和关键字,获取全部存在关键字的data的id，一对多映射
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {

  /**
   * 静态初始化，向基本数据类挂载钩子
   * 注意：这部分代码只可以从基本数据类获取数据(get),修改数据可能会无限递归(add,edit,remove)
   */
  static {
    
    //添加data之后，更新索引
    Data.afterAddHook.add(new HookFunction() {

      @Override
      public void func(Collection<String> jsonStrings) {
        //思路：为每个data的values里面的每个字符串的每个子串，都建立一个word->dataIds的索引Set，相等的word对应同一Set
        
        Jedis jedis = JedisUtil.getJedis();

        //word<->dataIdSet的Map(word存储为转换过的库键值)
        TreeMap<String, TreeSet<String>> keysMap = new TreeMap<String, TreeSet<String>>();
        
        //全部word的Set(word存储为裸值)
        TreeSet<String> rawKeySet = new TreeSet<String>();
        try {
          
          //能执行钩子 函数，说明data合法性已经验证，用户单一且与当前相同,data集合不为空,pdo全部合法
          
          //获取这组data数据的user的id
          JSONObject data0 = JSONObject.fromObject(jsonStrings.iterator().next());
          Long pdoId = data0.getLong("pdo");
          String pdoJsonString = jedis.get("pdo:"+String.valueOf(pdoId));
          JSONObject pdoJsonObj = JSONObject.fromObject(pdoJsonString);
          Long userId = pdoJsonObj.getLong("user");
          
          //遍历所有新添加的data
          for (String jsonString : jsonStrings) {
            JSONObject dataJsonObj = JSONObject.fromObject(jsonString);
            String dataId = String.valueOf(dataJsonObj.getLong("id"));
            JSONArray valueArray = dataJsonObj.getJSONArray("values");
            
            //对于data的所有value
            for (int i=0; i<valueArray.size(); i++) {
              String value = valueArray.getString(i);
              
              //对每个value，穷举子串，生成word<->dataIdSet的Map(word存储为转换过的库键值)
              for (int j=0; j<value.length(); j++) {
                for (int k=j; k<=value.length(); k++) {
                  String word = value.substring(j, k);
                  String key = "user:" + String.valueOf(userId) + ":search:data:fuzzy:" + word;
                  rawKeySet.add(word); //附带收集word裸值
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
          
          //添加形式1到Redis
          String[] keys = rawKeySet.toArray(new String[rawKeySet.size()]);
          String allKey = "user:" + String.valueOf(userId) + ":search:data:fuzzy:all";
          jedis.sadd(allKey, keys);
          
          //添加形式2到Redis
          for (Entry<String, TreeSet<String>> entry : keysMap.entrySet()) {
            String key = entry.getKey();
            TreeSet<String> idSet = entry.getValue();
            String[] ids = idSet.toArray(new String[idSet.size()]);
            jedis.sadd(key, ids);
          }
          
        } catch (Exception e) {
          System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdKeysToDataIds] 静态初始化完成");
  }
  
  /**
   * 给定user的id和关键词，模糊搜索，返回data的id的List
   * @param userId user的id
   * @param keys 关键字容器
   * @return data的id的List
   */
  public static List<String> getIds(long userId, Collection<String> keys) {
    
    Set<String> ids = new TreeSet<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      
      //关键字去重，并生成库键值
      TreeSet<String> keySet = new TreeSet<String>();
      for (String key : keys) {
        String dbKey = "user:" + String.valueOf(userId) + ":search:data:fuzzy:" + key;
        keySet.add(dbKey);
      }
      
      //Redis多个Set取交集
      if (!keySet.isEmpty()) {
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //键不存在等情况，Redis应该是返回空集合，而非null
        ids = jedis.sinter(keyArray);
      }

    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
    } finally {
      jedis.close();
    }
    
    return new ArrayList<String>(ids);
  }

}
