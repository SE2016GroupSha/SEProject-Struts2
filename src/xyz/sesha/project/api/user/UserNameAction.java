package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL：api/user/username
 * <br>参数：params={}
 * <br>返回：{"state": "success", "username": "白爷"} 或 {"state": "failed"}
 * <br>说明：获取当前登录用户名，已登录返回success和用户名，未登录返回failed
 * 
 * @author Administrator
 */
public class UserNameAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(UserNameAction.class);
  
  /**
   * 获取当前登陆用户名
   * @return 已登录返回用户名，未登录返回null
   */
  private String getName() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    return JSONObject.fromObject(User.getUserJson(id)).getString("name");
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/username][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/username]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/username][响应]: result=" + result);
      return "success";
    }
    
    //检验登陆 
    String userName = getName();
    if (userName!=null) {
      result.put("state", "success");
      result.put("username", userName);
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/username][响应]: result=" + result);
    return "success";
  }
}
