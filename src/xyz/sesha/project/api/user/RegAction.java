package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/reg
 * <br>参数：params={"user": user}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：注册新user，成功返回success，失败返回failed
 * 
 * @author Administrator
 */
public class RegAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(RegAction.class);

  /**
   * user注册
   * @param userJson user的json字符串
   * @return 注册成功返回true，注册失败返回false
   */
  private boolean register(String userJson) {
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
