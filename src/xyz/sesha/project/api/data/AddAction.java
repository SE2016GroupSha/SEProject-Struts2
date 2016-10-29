package xyz.sesha.project.api.data;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/data/add
 * <br>������params={"datas": [data1, data2...]}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵��������µ����ݣ�����Ϊdata���飬�ɹ�����success��ʧ�ܷ���failed��ʧ�ܲ�������κ�data
 * 
 * @author Administrator
 */
public class AddAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AddAction.class);

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
