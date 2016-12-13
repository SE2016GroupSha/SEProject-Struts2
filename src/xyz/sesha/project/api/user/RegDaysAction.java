package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL：api/user/regdays
 * <br>参数：params={}
 * <br>返回：{"state": "success", "regdays": 10, "regtime":1477412545804} 或 {"state": "failed"}
 * <br>说明：获取当前登录用户注册天数和注册时间，已登录返回success和注册天数，未登录返回failed
 * 
 * @author Administrator
 */
public class RegDaysAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(RegDaysAction.class);
  
  /**
   * 获取当前登陆用户注册天数
   * @return 已登录返回用户注册天数，未登录返回null
   */
  private Integer getRegDays() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    long now = System.currentTimeMillis();
    long reg = JSONObject.fromObject(User.getUserJson(id)).getLong("time");
    double days = ((double)(now-reg))/(1000.0 * 60.0 * 60.0 * 24.0);
    if (days < 0) {
      return null;
    } else {
      return Integer.valueOf(((int)days)+1);
    }
  }
  
  /**
   * 获取当前登陆用户注册时间
   * @return 已登录返回用户注册时间，未登录返回null
   */
  private Long getRegTime() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    return JSONObject.fromObject(User.getUserJson(id)).getLong("time");
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/regdays][请求]: params=" + params);
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/regdays]: 非法参数(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/regdays][响应]: result=" + result);
      return "success";
    }
    
    //检验登陆 
    Integer days = getRegDays();
    Long time = getRegTime();
    if (days!=null && time!=null) {
      result.put("state", "success");
      result.put("regdays", days);
      result.put("regtime", time);
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/regdays][响应]: result=" + result);
    return "success";
  }
}
