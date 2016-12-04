package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/changepasswd
 * <br>参数：params={"oldpwhash": "5e007e7046425c92111676b1b0999f12", "newpwhash": "2111676b1b0999f125e007e7046425c9"}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：修改密码，成功返回success，未登陆或原密码错误则失败返回failed
 * 
 * @author Administrator
 */
public class ChangePasswdAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(ChangePasswdAction.class);

  /**
   * 验证给定user的密码是否正确
   * @param id user的id
   * @param pwhash user的密码hash
   * @return 密码正确返回true，id不存在或密码错误返回false
   */
  private boolean checkPassword(String id, String pwhash) {
    String userJsonString = User.getUserJson(id);
    if (userJsonString==null) {
      return false;
    }
    JSONObject userJsonObject = JSONObject.fromObject(userJsonString);
    if (pwhash.equals(userJsonObject.getString("pwhash"))) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * 修改给定user的密码
   * @param id user的id
   * @param pwhash user的新密码hash
   * @return 修改成功返回true，id不存在或修改失败返回false
   */
  private boolean changePassword(String id, String pwhash) {
    return null!=User.editUser(id, "pwhash", pwhash);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      // 判断key是否存在
      if (!json.has("oldpwhash") || !json.has("newpwhash")) {
        ret =  false;
      }
      
      // null值判断，包含在instanceof关键字中

      // oldpwhash类型：java.lang.String
      Object oldpwhashObj = json.get("oldpwhash");
      if (!(oldpwhashObj instanceof String)) {
        ret = false;
      }
      
      // newpwhash类型：java.lang.String
      Object newpwhashObj = json.get("newpwhash");
      if (!(newpwhashObj instanceof String)) {
        ret = false;
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
    
    logger.info("[API][api/user/changepasswd][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/changepasswd]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][响应]: result=" + result);
      return "success";
    }
    
    //检查登陆并获取user的id
    String id = UserUtil.getUserId();
    if (id == null) {
      logger.error("[API][api/user/changepasswd]: 未登录");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][响应]: result=" + result);
      return "success";
    }
    
    //获取参数
    JSONObject paramsJson = JSONObject.fromObject(params);
    String oldpwhash = paramsJson.getString("oldpwhash");
    String newpwhash = paramsJson.getString("newpwhash");
    
    //验证原密码
    if (!checkPassword(id, oldpwhash)) {
      logger.error("[API][api/user/changepasswd]: 原密码错误");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][响应]: result=" + result);
      return "success";
    }
    
    //修改新密码
    if (changePassword(id, newpwhash)) {
      result.put("state", "success");
    } else {
      logger.error("[API][api/user/changepasswd]: 修改密码失败");
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/changepasswd][响应]: result=" + result);
    return "success";
  }
  
}
