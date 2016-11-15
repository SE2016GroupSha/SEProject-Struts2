package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/checkname
 * <br>参数：params={"name": "坐车"} (兼容参数：pdoname=坐车)
 * <br>返回：{"valid": "true"} 或 {"valid": "false"}
 * <br>说明：检查PDO名称是否可用(检查是否存在),可用返回true，不可用返回false
 * 
 * @author Wan XiaoLong
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);
  
  /**
   * 为前端适配的兼容参数
   */
  protected String pdoname;

  /**
   * @return the pdoname
   */
  public String getPdoname() {
    return pdoname;
  }

  /**
   * @param pdoname the pdoname to set
   */
  public void setPdoname(String pdoname) {
    this.pdoname = pdoname;
  }

  /**
   * 检查pdo名称是否可用(检查是否存在)
   * @param userId user的id
   * @param pdoName 待检查的pdo名称
   * @return 可用返回true，不可用返回false
   */
  private boolean check(String userId, String pdoName) {
    return null==UserIdPDONameToPDOId.getId(userId, pdoName);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    //兼容参数最高优先级
    if (pdoname != null) {
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
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/pdo/checkname]: 非法参数(" + params + ")");
      result.put("valid", "false");
      return "success";
    }
    
    //兼容参数最高优先级
    if (pdoname != null) {
      //判断name是否可用，并生成返回结果
      if (check("0", pdoname)) {
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
      if (check("0", name)) {
        result.put("valid", "true");
      } else {
        result.put("valid", "false");
      }
    }

    return "success";
  }
}
