package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/checkname
 * <br>������params={"name": "����"}
 * <br>���أ�{"valid": "true"} �� {"valid": "false"}
 * <br>˵�������PDO�����Ƿ����(����Ƿ����),���÷���true�������÷���false
 * 
 * @author Administrator
 */
public class CheckNameAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * ��ǰ�˴�����Http����params
   */
  private String params;
  
  /**
   * ���ظ�ǰ�˵�Json�ַ�����Ӧ
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
   * Action��������
   * @return ����success�ַ���
   */
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("����: " + params);
    logger.info("����: " + result.toString());
    
    return "success";
  }
}
