package xyz.luxin.java.se.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.PDO;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * 后端数据请求功能类：数据索引功能类
 *
 * <p>形式：user:[id]:pdo:all->(id1, id2...)
 * <br>说明：通过user的id获取该用户所有pdo的id，一对多映射
 * 
 * @author Administrator
 */
public class UserIdToPDOAllIds {
  
  /**
   * 静态初始化，向基本数据类挂载钩子
   * 注意：这部分代码只可以从基本数据类获取数据(get),修改数据可能会无限递归(add,edit,remove)
   */
  static {
    
    //添加pdo之后，更新索引
    PDO.afterAddHook.add(new HookFunction() {

      @Override
      public void func(Collection<String> jsonStrings) {
        Jedis jedis = JedisUtil.getJedis();
        try {
          for (String jsonString : jsonStrings) {
            JSONObject json = JSONObject.fromObject(jsonString);
            String key = "user:" + json.getString("user") + ":pdo:all";
            String value = json.getString("id");
            jedis.sadd(key, value);
          }
        } catch (Exception e) {
          System.out.println("[Error][UserIdToPDOAllIds]: 错误原因描述");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdToPDOAllIds] 静态初始化完成");
  }
  
  
  /**
   * 给定user的id，返回该user所有pdo的id的List
   * @param id user的id
   * @return 所有pdo的id的List
   */
  public static List<Long> getAllIds(long id) {
    
    List<Long> ids = new ArrayList<Long>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + String.valueOf(id) + ":pdo:all";
      Set<String> idStrings = jedis.smembers(key);
      if (idStrings != null) {
        for (String idString : idStrings) {
          ids.add(Long.valueOf(idString));
        }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      ids.clear();
      System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
    } catch (Exception e) {
      e.printStackTrace();
      ids.clear();
      System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
    } finally {
      jedis.close();
    }
    
    return ids;
  }
}
