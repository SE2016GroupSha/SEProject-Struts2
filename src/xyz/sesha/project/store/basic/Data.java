package xyz.sesha.project.store.basic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;


/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：data
 * <br>数据字段：id全库不重复, time意义为data创建时间 或 用户手动填写的时间(毫秒)
 * <br>数据形式：{"id":"4", "time":1477412545804, "pdo": "1", "values": ["家", "学校", "10分钟"], "related_data": ["5", "6"]}
 * <br>存储形式：data:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Administrator
 */
public class Data {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(Data.class);
  
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
   * 检验data的json字符串的合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组(部分键)，数组的成员类型合法性
   * <br>忽略：值的逻辑含义
   * @param dataJsons json字符串存储的容器
   * @return 返回检验结果，合法则返回true，非法则返回false
   */
  public static boolean checkDataJsonFormat(Collection<String> dataJsons) {
    return true;
  }
  
  /**
   * 添加data的真实执行方法，直接读写Redis
   * @param dataJsons data的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  private static boolean innerAddData(Collection<String> dataJsons) {
    return true;
  }

  /**
   * 添加data的方法，供外部访问
   * @param dataJsons data的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addData(Collection<String> dataJsons) {
    return true;
  }
  
  /**
   * 添加data的方法，供外部访问
   * @param dataJson data的json字符串
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addData(String dataJson) {
    return true;
  }
  
  /**
   * 给定id，判断data是否存在
   * @param id data的id
   * @return 返回判断结果，true则存在，false则不存在
   */
  public static boolean hasData(String id) {
    return true;
  }
  
  /**
   * 给定id，返回data的json字符串
   * @param id data的id
   * @return 返回data的json字符串，若id不存在，则返回null
   */
  public static String getDataJson(String id) {
    return null;
  }
  
  /**
   * 给定id，返回data的json字符串
   * @param ids data的id的容器
   * @return 返回data的json字符串的List
   */
  public static List<String> getDataJson(Collection<String> ids) {
    return null;
  }
}
