package xyz.luxin.java.se.project.store.index;

import java.util.Collection;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.PDO;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * 后端数据请求功能类：数据索引功能类
 * 
 * <p>形式：user:[id]:pdo:[name]->id
 * <br>说明：通过user的id和pdo的name获取pdo的id，一对一映射
 * 
 * @author Administrator
 */
public class UserIdPDONameToPDOId {
  
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
            String key = "user:" + json.getString("user") + ":pdo:" + json.getString("name");
            String value = json.getString("id");
            jedis.set(key, value);
          }
        } catch (Exception e) {
          System.out.println("[Error][UserIdPDONameToPDOId]: 错误原因描述");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdPDONameToPDOId] 静态初始化完成");
  }
  
  
  /**
   * 给定user的id和pdo的name，返回pdo的id
   * @param userId user的id
   * @param pdoName pdo的name
   * @return data的id，若不存在则返回null
   */
  public static long getId(long userId, String pdoName) {
    
    long id = -1L;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + String.valueOf(userId) + ":pdo:" + pdoName;
      String value = jedis.get(key);
      if (value != null) {
        id = Long.valueOf(value);
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
    
    return id;
  }
}
