package xyz.sesha.project.utils;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * DataBase工具类
 * <p>该类主要提供db的一些低级操作，慎用<br>
 * @author Lu Xin
 */
public class DataBaseUtil {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(DataBaseUtil.class);

  /**
   * <pr>保障基本数据的id全库唯一的键，里面存储自增数字
   * <br>注意：这个只用于单用户单连接环境，多用户环境会产生同步问题
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * <pr>数据库清空标记，如置为true，InitDB方法执行时会清空数据库
   * <br>注意：只在开发阶段供开发者使用
   */
  private static final boolean DB_CLEAR_FLAG = true;
  
  /**
   * 数据库初始化方法，在系统初始化时被调用
   * <pr>
   * <br><br>注意：只供开发者使用，用户不应该直接使用这个方法
   * @return 返回初始化结果，成功返回true，失败返回false
   */
  public static boolean InitDB() {
    boolean ret = true;
    
    Jedis jedis = JedisUtil.getJedis();
    try {
      if (DB_CLEAR_FLAG) {
        jedis.flushDB();
        jedis.set("user:0", "{\"id\":0, \"time\":0, \"name\":\"0\", \"pwhash\":\"0\"}");
        jedis.set(DB_INDEX_KEY, "1");
        ret = true;
      } else {
        if (!jedis.exists(DB_INDEX_KEY)) {
          logger.error("[DataBaseUtil][InitDB]：dbindex键不存在");
          ret = false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[DataBaseUtil][InitDB]：其他错误");
    } finally {
      jedis.close();
    }
    
    return ret;
  }
}
