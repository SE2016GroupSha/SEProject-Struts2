package xyz.sesha.project.store.index;

import java.util.List;

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

  /**
   * 静态初始化，向基本数据类挂载钩子
   * 注意：这部分代码只可以从基本数据类获取数据(get),修改数据可能会无限递归(add,edit,remove)
   */
  static {
    
    logger.info("[UserIdToPDOAllIds] 静态初始化完成");
  }
  
  /**
   * 给定user的id，返回该user所有pdo的id的List
   * @param userId user的id
   * @return 所有pdo的id的List
   */
  public static List<Long> getAllIds(long userId) {
    return null;
  }
  
}
