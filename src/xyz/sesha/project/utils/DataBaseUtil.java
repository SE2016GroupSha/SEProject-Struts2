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
   * <pr>���ϻ������ݵ�idȫ��Ψһ�ļ�������洢��������
   * <br>ע�⣺���ֻ���ڵ��û������ӻ��������û����������ͬ������
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * <pr>���ݿ���ձ�ǣ�����Ϊtrue��InitDB����ִ��ʱ��������ݿ�
   * <br>ע�⣺ֻ�ڿ����׶ι�������ʹ��
   */
  private static final boolean DB_CLEAR_FLAG = true;
  
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
        jedis.set("user:0", "{\"id\":0, \"time\":0, \"name\":\"0\", \"pwhash\":\"0\"}");
        jedis.set(DB_INDEX_KEY, "1");
        ret = true;
      } else {
        if (!jedis.exists(DB_INDEX_KEY)) {
          logger.error("[DataBaseUtil][InitDB]��dbindex��������");
          ret = false;
        }
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
