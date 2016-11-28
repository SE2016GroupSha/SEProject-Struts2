package xyz.sesha.project.utils;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

/**
 * DataBase������
 * <p>������Ҫ�ṩdb��һЩ�ͼ�����������<br>
 * @author Lu Xin
 */
public class DataBaseUtil {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(DataBaseUtil.class);
  
  /**
   * <pr>���ݿ���ձ�ǣ�����Ϊtrue��InitDB����ִ��ʱ��������ݿ�
   * <br>ע�⣺ֻ�ڿ����׶ι�������ʹ��
   */
  private static final boolean DB_CLEAR_FLAG = false;
  
  /**
   * ���ݿ��ʼ����������ϵͳ��ʼ��ʱ������
   * <pr>
   * <br><br>ע�⣺ֻ��������ʹ�ã��û���Ӧ��ֱ��ʹ���������
   * @return ���س�ʼ��������ɹ�����true��ʧ�ܷ���false
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
      logger.error("[DataBaseUtil][InitDB]����������");
    } finally {
      jedis.close();
    }
    
    return ret;
  }
}
