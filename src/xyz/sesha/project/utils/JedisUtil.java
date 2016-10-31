package xyz.sesha.project.utils;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis工具类
 * <p>该类主要提供Jedis连接池服务<br>
 * @author Lu Xin
 */
public class JedisUtil {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(JedisUtil.class);

  public static final String REDIS_IP = "127.0.0.1";     //IP
  public static final int REDIS_PORT = 6379;             //端口
  public static final String REDIS_PASSWORD = "";        //密码
  public static final int CONN_TIMEOUT = 10000;          //连接超时
  public static final int MAX_TOTAL = 50;                //最大连接数
  public static final int MAX_IDLE = 20;                 //最大空闲连接数
  public static final int MIN_IDLE = 10;                 //最小空闲连接数
  public static final long MAX_WAIT_TIME = 10000;        //连接超时
  public static final long MIN_IDLE_TIME = 180000;       //空闲回收
  public static final boolean TEST_ON_BORROW = false;    //取出验证
  public static final boolean TEST_ON_RETURN = false;    //归还验证
  public static final boolean TEST_WHILE_IDLE = false;   //空闲验证

  
  /**
   * Jedis连接池对象
   */
  private static JedisPool jedisPool = null;

  
  /**
   * 静态初始化连接池
   */
  static {

    JedisPoolConfig config = new JedisPoolConfig();
    
    config.setMaxTotal(MAX_TOTAL);
    config.setMaxIdle(MAX_IDLE);
    config.setMinIdle(MIN_IDLE);
    config.setMaxWaitMillis(MAX_WAIT_TIME);
    config.setMinEvictableIdleTimeMillis(MIN_IDLE_TIME);
    config.setTestOnBorrow(TEST_ON_BORROW);
    config.setTestOnReturn(TEST_ON_RETURN);
    config.setTestWhileIdle(TEST_WHILE_IDLE);

    jedisPool = new JedisPool(config, REDIS_IP, REDIS_PORT, CONN_TIMEOUT/*, REDIS_PASSWORD*/);
    
    logger.info("[JedisUtil] 静态初始化完成");
  }
  
  
  /**
   * 获取Jedis连接池
   * @return 返回连接池对象
   */
  public static JedisPool getPool() {
      return jedisPool;
  }


  /**
   * 从连接池中获取Jedis连接
   * @return 返回Jedis连接
   */
  public static Jedis getJedis() {
      return jedisPool.getResource();
  }

  
  /**
   * 向连接池归还Jedis连接
   * <p>注意：当前版本已经弃用，可直接使用连接的close方法<br>
   * @param jedis jedis连接对象
   * @return 无返回值
   */
  @SuppressWarnings("deprecation")
  public static void returnJedis(Jedis jedis) {
      if(jedis!=null) {
          jedisPool.returnResource(jedis);
      }
  }
  
}
