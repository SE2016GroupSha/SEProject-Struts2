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
   * <pr>数据库清空标记，如置为true，InitDB方法执行时会清空数据库
   * <br>注意：只在开发阶段供开发者使用
   */
  private static final boolean DB_CLEAR_FLAG = false;
  
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
        ret = true;
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
