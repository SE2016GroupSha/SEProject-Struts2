package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.index.UserNameToUserId;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/user/checkname
 * <br>参数：params={"name": "白爷"} (兼容参数：username=白爷)
 * <br>返回：{"valid": "true"} 或 {"valid": "false"}
 * <br>说明：检查user名称是否可用(检查是否存在),可用返回true，不可用返回false
 * 
 * @author Si Aoran
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);
  
  /**
   * 为前端适配的兼容参数
   */
  protected String username;

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * 检查user名称是否可用(检查是否存在)
   * @param name 待检查的user名称
   * @return 可用返回true，不可用返回false
   */
  private boolean check(String name) {
    return null==UserNameToUserId.getId(name);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    //兼容参数最高优先级
    if (username != null) {
      return ret;
    }
    
    //正常定义参数优先级次之
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      //判断key是否存在
      if (!json.has("name")) {
        ret =  false;
      }
      
      //null值判断，包含在instanceof关键字中
      
      //name类型：java.lang.String
      Object nameObj = json.get("name");
      if (!(nameObj instanceof String)) {
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
    
    logger.info("[API][api/user/checkname][请求]: user=白爷, params=" + params + ", username=" + username);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/checkname]: 非法参数(" + params + ")");
      result.put("valid", "false");
      logger.info("[API][api/user/checkname][响应]: result=" + result);
      return "success";
    }
    
    //兼容参数最高优先级
    if (username != null) {
      //判断name是否可用，并生成返回结果
      if (check(username)) {
        result.put("valid", "true");
      } else {
        result.put("valid", "false");
      }
    }
    //正常定义参数优先级次之
    else {
      JSONObject paramsJson = JSONObject.fromObject(params);
      String name = paramsJson.getString("name");
      
      //判断name是否可用，并生成返回结果
      if (check(name)) {
        result.put("valid", "true");
      } else {
        result.put("valid", "false");
      }
    }

    logger.info("[API][api/user/checkname][响应]: result=" + result);
    return "success";
  }
}
