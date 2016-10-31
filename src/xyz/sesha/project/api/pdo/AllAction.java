package xyz.sesha.project.api.pdo;

import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/all
 * <br>参数：params={}
 * <br>返回：{"pdos": [pdo1, pdo2...]}
 * <br>说明：返回所有的PDO，默认序是时间从大到小，返回值为pdo数组
 * 
 * @author Administrator
 */
public class AllAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(AllAction.class);

  /**
   * 返回所有的pdo的json字符串(无序)
   * @param userId user的id
   * @return 返回指定user的所有pdo的json字符串
   */
  private List<String> all(String userId) {
    return null;
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("参数: " + params);
    logger.info("返回: " + result.toString());
    
    return "success";
  }
}
