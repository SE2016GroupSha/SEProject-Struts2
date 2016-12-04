package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL：api/user/checklogin
 * <br>参数：params={}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：验证是否已经登录，已登录返回success，未登录返回failed
 * 
 * @author Administrator
 */
public class CheckLoginAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(CheckLoginAction.class);
  
  /**
   * 检查当前连接是否登录
   * @return 已登录返回true，未登录返回false
   */
  private boolean check() {
    return null!=UserUtil.getUserId();
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/checklogin][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/checklogin]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/checklogin][响应]: result=" + result);
      return "success";
    }
    
    //检验登陆 
    if (check()) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/checklogin][响应]: result=" + result);
    return "success";
  }
}
