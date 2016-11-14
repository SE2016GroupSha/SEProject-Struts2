package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;


/**
 * 后端数据请求功能类：数据索引功能类
 * 
 * <p>形式：user:[id]:pdo:[name]->id
 * <br>说明：通过user的id和pdo的name获取pdo的id，一对一映射
 * 
 * @author Administrator
 */
public class UserIdPDONameToPDOId {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserIdPDONameToPDOId.class);

  /**
   * 静态初始化，向基本数据类挂载钩子
   * 注意：这部分代码只可以从基本数据类获取数据(get),修改数据可能会无限递归(add,edit,remove)
   */
  static {
    
    logger.info("[UserIdPDONameToPDOId] 静态初始化完成");
  }
  
  /**
   * 给定user的id和pdo的name，返回pdo的id
   * @param userId user的id
   * @param pdoName pdo的name
   * @return pdo的id，若不存在则返回null
   */
  public static String getId(String userId, String pdoName) {
    return null;
  }
  
}
