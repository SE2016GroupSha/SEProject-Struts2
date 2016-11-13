package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/checkname
 * <br>参数：params={"name": "坐车"}
 * <br>返回：{"valid": "true"} 或 {"valid": "false"}
 * <br>说明：检查PDO名称是否可用(检查是否存在),可用返回true，不可用返回false
 * 
 * @author Administrator
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * 检查pdo名称是否可用(检查是否存在)
   * @param userId user的id
   * @param pdoName 待检查的pdo名称
   * @return 可用返回true，不可用返回false
   */
  private boolean check(long userId, String pdoName) {
	  return -1L==UserIdPDONameToPDOId.getId(userId, pdoName);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
	  boolean ret = true;
	    
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
	    
	    //检验参数合法性
	    if (!checkParamsJsonFormat()) {
	      System.out.println("[API][api/pdo/checkname]: 非法参数(" + params + ")");
	      result.put("valid", "false");
	      return "success";
	    }
	    
	    JSONObject paramsJson = JSONObject.fromObject(params);
	    String name = paramsJson.getString("name");
	    
	    //判断name是否可用，并生成返回结果
	    if (check(0, name)) {
	      result.put("valid", "true");
	    } else {
	      result.put("valid", "false");
	    }
	    
	    return "success";
  }
}
