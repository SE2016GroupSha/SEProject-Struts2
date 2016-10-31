package xyz.sesha.project.store.basic;

import java.util.Collection;
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
  
  /**
   * <pr>保障基本数据的id全库唯一的键，里面存储自增数字
   * <br>注意：这个只用于单用户单连接环境，多用户环境会产生同步问题
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * 检验user的json字符串的合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组(部分键)，数组的成员类型合法性
   * <br>忽略：值的逻辑含义
   * @param userJsons json字符串存储的容器
   * @return 返回检验结果，合法则返回true，非法则返回false
   */
  public static boolean checkUserJsonFormat(Collection<String> userJsons) {
    return true;
  }
  
  /**
   * 添加user的真实执行方法，直接读写Redis
   * @param userJsons user的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  private static boolean innerAddUser(Collection<String> userJsons) {
    return true;
  }

  /**
   * 添加user的方法，供外部访问
   * @param userJsons user的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addUser(Collection<String> userJsons) {
    return true;
  }
  
  /**
   * 添加user的方法，供外部访问
   * @param userJson user的json字符串
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addUser(String userJson) {
    return true;
  }
  
  /**
   * 给定id，判断user是否存在
   * @param id user的id
   * @return 返回判断结果，true则存在，false则不存在
   */
  public static boolean hasUser(String id) {
    return true;
  }
  
  /**
   * 给定id，返回user的json字符串
   * @param id user的id
   * @return 返回user的json字符串，若id不存在，则返回null
   */
  public static String getUserJson(String id) {
    return null;
  }
  
  /**
   * 给定id，返回user的json字符串
   * @param ids user的id的容器
   * @return 返回user的json字符串的List
   */
  public static List<String> getUserJson(Collection<String> ids) {
    return null;
  }
}
