package xyz.sesha.project.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import xyz.sesha.project.utils.DataBaseUtil;


/**
 * Servlet������ʼ��Listener
 * <p>������Ҫ��һЩ��ʼ�����������һЩ����о�̬��ʼ��<br>
 * @author Lu Xin
 */
public class InitListener implements ServletContextListener {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(InitListener.class);

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("[InitListener] ϵͳ�˳�");
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("[InitListener] ϵͳ��ʼ��");
    
    try {
      Class.forName("xyz.sesha.project.utils.JedisUtil");
      logger.info("[InitListener] Jedis���ӳؾ�̬��ʼ���ɹ�");
    } catch (ClassNotFoundException e) {
      logger.error("[InitListener] Jedis���ӳؾ�̬��ʼ��ʧ��");
      e.printStackTrace();
    }
    
    if (DataBaseUtil.InitDB()) {
      logger.info("[InitListener] DB��ʼ���ɹ�");
    } else {
      logger.error("[InitListener] DB��ʼ��ʧ��");
    }
    
    try {
      Class.forName("xyz.sesha.project.store.index.UserIdPDONameToPDOId");
      Class.forName("xyz.sesha.project.store.index.UserIdToPDOAllIds");
      Class.forName("xyz.sesha.project.store.index.UserNameToUserId");
      Class.forName("xyz.sesha.project.store.index.UserIdKeysToDataIds");
      logger.info("[InitListener] ������̬��ʼ���ɹ�");
    } catch (ClassNotFoundException e) {
      logger.error("[InitListener] ������̬��ʼ��ʧ��");
      e.printStackTrace();
    }
    
  }
}
