package xyz.sesha.project.utils;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis������
 * <p>������Ҫ�ṩJedis���ӳط���<br>
 * @author Lu Xin
 */
public class JedisUtil {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(JedisUtil.class);

  public static final String REDIS_IP = "127.0.0.1";     //IP
  public static final int REDIS_PORT = 6379;             //�˿�
  public static final String REDIS_PASSWORD = "";        //����
  public static final int CONN_TIMEOUT = 10000;          //���ӳ�ʱ
  public static final int MAX_TOTAL = 50;                //���������
  public static final int MAX_IDLE = 20;                 //������������
  public static final int MIN_IDLE = 10;                 //��С����������
  public static final long MAX_WAIT_TIME = 10000;        //���ӳ�ʱ
  public static final long MIN_IDLE_TIME = 180000;       //���л���
  public static final boolean TEST_ON_BORROW = false;    //ȡ����֤
  public static final boolean TEST_ON_RETURN = false;    //�黹��֤
  public static final boolean TEST_WHILE_IDLE = false;   //������֤

  
  /**
   * Jedis���ӳض���
   */
  private static JedisPool jedisPool = null;

  
  /**
   * ��̬��ʼ�����ӳ�
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
    
    logger.info("[JedisUtil] ��̬��ʼ�����");
  }
  
  
  /**
   * ��ȡJedis���ӳ�
   * @return �������ӳض���
   */
  public static JedisPool getPool() {
      return jedisPool;
  }


  /**
   * �����ӳ��л�ȡJedis����
   * @return ����Jedis����
   */
  public static Jedis getJedis() {
      return jedisPool.getResource();
  }

  
  /**
   * �����ӳع黹Jedis����
   * <p>ע�⣺��ǰ�汾�Ѿ����ã���ֱ��ʹ�����ӵ�close����<br>
   * @param jedis jedis���Ӷ���
   * @return �޷���ֵ
   */
  @SuppressWarnings("deprecation")
  public static void returnJedis(Jedis jedis) {
      if(jedis!=null) {
          jedisPool.returnResource(jedis);
      }
  }
  
}
