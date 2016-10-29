package xyz.sesha.project.api.data;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/data/relateddata
 * <br>参数：params={"ids": ["5", "4"...]}
 * <br>返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>说明：获取已有数据的全部关联数据(无重复)以及对应PDO(无重复)，参数ids为id数组(数据的id)，返回data数组和pdo数组
 * 
 * @author Administrator
 */
public class RelatedDataAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(RelatedDataAction.class);

  /**
   * 从前端传来的Http参数params
   */
  private String params;
  
  /**
   * 返回给前端的Json字符串响应
   */
  private JSONObject result;

  /**
   * @return the params
   */
  public String getParams() {
    return params;
  }

  /**
   * @param params the params to set
   */
  public void setParams(String params) {
    this.params = params;
  }
  
  /**
   * @return the result
   */
  public JSONObject getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(JSONObject result) {
    this.result = result;
  }
  
  /**
   * Action请求处理方法
   * @return 返回success字符串
   */
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("参数: " + params);
    logger.info("返回: " + result.toString());
    
    return "success";
  }
}
