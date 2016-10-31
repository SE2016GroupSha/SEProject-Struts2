package xyz.sesha.project.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import xyz.sesha.project.utils.DataBaseUtil;


/**
 * Servlet容器初始化Listener
 * <p>该类主要做一些初始化工作，如对一些类进行静态初始化<br>
 * @author Lu Xin
 */
public class InitListener implements ServletContextListener {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(InitListener.class);

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("[InitListener] 系统退出");
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("[InitListener] 系统初始化");
    
    try {
      Class.forName("xyz.sesha.project.utils.JedisUtil");
      logger.info("[InitListener] Jedis连接池静态初始化成功");
    } catch (ClassNotFoundException e) {
      logger.error("[InitListener] Jedis连接池静态初始化失败");
      e.printStackTrace();
    }
    
    if (DataBaseUtil.InitDB()) {
      logger.info("[InitListener] DB初始化成功");
    } else {
      logger.error("[InitListener] DB初始化失败");
    }
    
    try {
      Class.forName("xyz.sesha.project.store.index.UserIdPDONameToPDOId");
      Class.forName("xyz.sesha.project.store.index.UserIdToPDOAllIds");
      Class.forName("xyz.sesha.project.store.index.UserNameToUserId");
      Class.forName("xyz.sesha.project.store.index.UserIdKeysToDataIds");
      logger.info("[InitListener] 索引静态初始化成功");
    } catch (ClassNotFoundException e) {
      logger.error("[InitListener] 索引静态初始化失败");
      e.printStackTrace();
    }
    
  }
}
