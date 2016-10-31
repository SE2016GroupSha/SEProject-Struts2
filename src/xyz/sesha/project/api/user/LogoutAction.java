package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/logout
 * <br>参数：params={}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：user退出，成功返回success，失败返回failed
 * 
 * @author Administrator
 */
public class LogoutAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * user退出，直接影响一个session的user信息
   * @return 退出成功返回true，退出失败返回false
   */
  public static boolean logout() {
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
