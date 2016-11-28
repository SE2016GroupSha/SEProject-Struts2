package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.store.index.UserNameToUserId;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/login
 * <br>参数：params={"user": user}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：user登陆，成功返回success，失败返回failed
 * 
 * @author Si Aoran
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
  private boolean login(String userJson) {
    JSONObject user = JSONObject.fromObject(userJson);
    String name = user.getString("name");
    String pwhash = user.getString("pwhash");
    String id = UserNameToUserId.getId(name);
    if (id==null) {
      return false;
    } else {
      String userRealString = User.getUserJson(id);
      JSONObject userReal = JSONObject.fromObject(userRealString);
      if (!pwhash.equals(userReal.getString("pwhash"))) {
        return false;
      }
    }
    
    //user登陆持久化
    UserUtil.addUserId(id);
    return true;
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      //判断key是否存在
      if (!json.has("user")) {
        ret =  false;
      }
      
      //null值判断
      if (json.get("user")==null) {
        ret =  false;
      }
      
    } catch (JSONException e) {
      ret =  false;
    } catch (Exception e) {
      ret =  false;
    }

    return ret;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/login][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/login]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/login][响应]: result=" + result);
      return "success";
    }
    
    JSONObject paramsJson = JSONObject.fromObject(params);
    String userJsonString = paramsJson.getString("user");
    
    //user登陆，获取结果
    if (login(userJsonString)) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/login][响应]: result=" + result);
    return "success";
  }
}
