package xyz.sesha.project.store.index;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 后端数据请求功能类：数据索引功能类
 * 
 * <p>形式1：user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>形式2：user:[id]:search:data:fuzzy:[key]      ->(id1, id2...)
 * <br>说明：通过user的id和关键字,获取全部存在关键字的data的id，一对多映射
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserIdKeysToDataIds.class);
  
  /**
   * 静态初始化，向基本数据类挂载钩子
   * 注意：这部分代码只可以从基本数据类获取数据(get),修改数据可能会无限递归(add,edit,remove)
   */
  static {
    
  }
  
  /**
   * 给定user的id和关键词，模糊搜索，返回data的id的List
   * @param userId user的id
   * @param keys 关键字容器
   * @return data的id的List
   */
  public static List<String> getIds(String userId, Collection<String> keys) {
    return null;
  }

}
