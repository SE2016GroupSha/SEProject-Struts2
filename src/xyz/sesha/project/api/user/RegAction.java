package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/reg
 * <br>参数：params={"user": user}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：注册新user，成功返回success，失败返回failed
 * 
 * @author Si Aoran
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
    return User.addUser(userJson);
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
    
    logger.info("[API][api/user/reg][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/reg]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/reg][响应]: result=" + result);
      return "success";
    }
    
    JSONObject paramsJson = JSONObject.fromObject(params);
    String userJsonString = paramsJson.getString("user");
    
    //添加user，获取结果
    if (register(userJsonString)) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/reg][响应]: result=" + result);
    return "success";
  }
  
}
