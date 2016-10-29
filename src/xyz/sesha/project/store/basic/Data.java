package xyz.sesha.project.store.basic;

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

}
