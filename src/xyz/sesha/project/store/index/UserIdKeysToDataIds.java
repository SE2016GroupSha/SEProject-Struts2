package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;

/**
 * 后端数据请求功能类：数据索引功能类
 * 
 * <p>形式1：user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>形式2：user:[id]:search:data:fuzzy:[key]:     ->(id1, id2...)
 * <br>说明：通过user的id和关键字,获取全部存在关键字的data的id，一对多映射
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserIdKeysToDataIds.class);

}
