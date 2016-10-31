package xyz.sesha.project.api.pdo;

import java.util.Collection;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/add
 * <br>参数：params={"pdos": [pdo1, pdo2...]}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：添加新的PDO，参数为pdo数组，成功返回success，失败返回failed，失败不会添加任何PDO
 * 
 * @author Administrator
 */
public class AddAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(AddAction.class);

  /**
   * 添加新的pdo
   * @param pdos pdo的json字符串的容器
   * @return 返回执行结果，成功则返回true，失败则返回false，当返回失败时不会产生任何影响
   */
  private boolean add(Collection<String> pdos) {
    return true;
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("参数: " + params);
    logger.info("返回: " + result.toString());
    
    return "success";
  }
}
