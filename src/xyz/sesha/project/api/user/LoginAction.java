package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

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
public class LoginAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * 从前端传来的Http参数params
   */
  private String params;
  
  /**
   * 返回给前端的Json字符串响应
   */
  private JSONObject result;

  /**
   * @return the params
   */
  public String getParams() {
    return params;
  }

  /**
   * @param params the params to set
   */
  public void setParams(String params) {
    this.params = params;
  }
  
  /**
   * @return the result
   */
  public JSONObject getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(JSONObject result) {
    this.result = result;
  }
  
  /**
   * Action请求处理方法
   * @return 返回success字符串
   */
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("参数: " + params);
    logger.info("返回: " + result.toString());
    
    return "success";
  }
}
