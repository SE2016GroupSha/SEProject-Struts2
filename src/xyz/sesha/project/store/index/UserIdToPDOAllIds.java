package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;

/**
 * 后端数据请求功能类：数据索引功能类
 *
 * <p>形式：user:[id]:pdo:all->(id1, id2...)
 * <br>说明：通过user的id获取该用户所有pdo的id，一对多映射
 * 
 * @author Administrator
 */
public class UserIdToPDOAllIds {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserIdToPDOAllIds.class);

}
