package xyz.sesha.project.api;

import net.sf.json.JSONObject;

/**
 * 前端API请求响应抽象基类
 * 
 * <p>对API请求Action通用的属性，以及共有的方法进行分离
 *
 * @author Lu Xin
 */
public abstract class AbstractApiAction {

  /**
   * 从前端传来的Http参数params
   */
  protected String params;
  
  /**
   * 返回给前端的Json字符串响应
   */
  protected JSONObject result;

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
   * 检验http传递的json参数合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组，由基本数据构成的数组的成员类型合法性
   * <br>忽略：由object构成数组的成员合法性，值的逻辑含义
   * @return 返回检验结果，合法则true，非法则false
   */
  public abstract boolean checkParamsJsonFormat();
  
  /**
   * Action请求处理方法
   * @return 返回success字符串
   */
  public abstract String execute();
}
