package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/logout
 * <br>参数：params={}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：user退出，成功返回success，失败返回failed
 * 
 * @author Si Aoran
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
  private boolean logout() {
    return UserUtil.delUserId();
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/logout][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/logout]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/logout][响应]: result=" + result);
      return "success";
    }
    
    //user注销，获取结果
    if (logout()) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/logout][响应]: result=" + result);
    return "success";

  }
}
