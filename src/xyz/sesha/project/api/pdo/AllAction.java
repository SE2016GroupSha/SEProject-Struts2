package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/all
 * <br>������params={}
 * <br>���أ�{"pdos": [pdo1, pdo2...]}
 * <br>˵�����������е�PDO��Ĭ������ʱ��Ӵ�С������ֵΪpdo����
 * 
 * @author Administrator
 */
public class AllAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AllAction.class);

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
