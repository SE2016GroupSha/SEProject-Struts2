package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/login
 * <br>参数：params={"user": user}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：user登陆，成功返回success，失败返回failed
 * 
 * @author Administrator
 */
public class LoginAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * user登陆，直接影响一个session的user信息
   * @param userJson user的json字符串
   * @return 登陆成功返回true，登陆失败返回false
   */
  public static boolean login(String userJson) {
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
