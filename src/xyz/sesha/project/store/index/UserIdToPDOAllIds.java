package xyz.sesha.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.utils.JedisUtil;


/**
 * 后端数据请求功能类：数据索引功能类
 *
 * <p>形式：user:[id]:pdo:all->(id1, id2...)
 * <br>说明：通过user的id获取该用户所有pdo的id，一对多映射
 * 
 * @author Si Aoran
 */
public class UserIdToPDOAllIds {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserIdToPDOAllIds.class);

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
          logger.error("[Error][UserIdToPDOAllIds]: 内部错误");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    logger.info("[UserIdToPDOAllIds] 静态初始化完成");
  }
  
  /**
   * 给定user的id，返回该user所有pdo的id的List
   * @param id user的id
   * @return 所有pdo的id的List
   */
  public static List<String> getAllIds(String id) {
    
    List<String> ids = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + id + ":pdo:all";
      Set<String> idStrings = jedis.smembers(key);
      if (idStrings != null) {
        for (String idString : idStrings) {
          ids.add(idString);
        }
      }
    } catch (Exception e) {
      ids.clear();
      e.printStackTrace();
      logger.error("[Error][UserIdToPDOAllIds]: 内部错误");
    } finally {
      jedis.close();
    }
    
    return ids;
  }
  
}
