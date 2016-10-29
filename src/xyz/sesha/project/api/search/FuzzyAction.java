package xyz.sesha.project.api.search;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/search/fuzzy
 * <br>参数：params={"keys": ["学校", "10分钟"...]}
 * <br>返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>说明：全数据全字段模糊搜索(第一版只支持完全匹配)，默认序是时间从大到小，输入为关键字数组(空关键字数组返回全部数据)，返回data数组和pdo数组
 * 
 * @author Administrator
 */
public class FuzzyAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

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
