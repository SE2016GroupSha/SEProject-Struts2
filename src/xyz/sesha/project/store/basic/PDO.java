package xyz.sesha.project.store.basic;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;

/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：pdo
 * <br>数据字段：id全库不重复, user代表所属用户的id, name同用户不重复, time意义为PDO创建时间(毫秒)
 * <br>数据形式：{"id":"1", "time":1477410877415, "user":"0", "name":"坐车", "fields":["始点", "终点", "耗时"]}
 * <br>存储形式：pdo:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Administrator
 */
public class PDO {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(PDO.class);
  
  /**
   * 添加操作之后,执行的全部索引钩子
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();
  
}
