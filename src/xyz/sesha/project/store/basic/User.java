package xyz.sesha.project.store.basic;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;

/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：user
 * <br>数据字段：id全库不重复, name全库不重复, pwhash为密码32位md5值(小写), time意义为user创建时间(毫秒)
 * <br>数据形式：{"id":"0", "time":1477410793369, "name":"白爷", "pwhash":"5e007e7046425c92111676b1b0999f12"}
 * <br>存储形式：user:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Administrator
 */
public class User {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(User.class);
  
  /**
   * 添加操作之后,执行的全部索引钩子
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();

}
