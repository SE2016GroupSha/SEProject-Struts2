package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/checkname
 * <br>参数：params={"name": "坐车"}
 * <br>返回：{"valid": "true"} 或 {"valid": "false"}
 * <br>说明：检查PDO名称是否可用(检查是否存在),可用返回true，不可用返回false
 * 
 * @author Administrator
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * 检查pdo名称是否可用(检查是否存在)
   * @param userId user的id
   * @param pdoName 待检查的pdo名称
   * @return 可用返回true，不可用返回false
   */
  private boolean check(String userId, String pdoName) {
    return true;
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
