package xyz.sesha.project.api.search;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/search/fuzzy
 * <br>������params={"keys": ["ѧУ", "10����"...]}
 * <br>���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>˵����ȫ����ȫ�ֶ�ģ������(��һ��ֻ֧����ȫƥ��)��Ĭ������ʱ��Ӵ�С������Ϊ�ؼ�������(�չؼ������鷵��ȫ������)������data�����pdo����
 * 
 * @author Administrator
 */
public class FuzzyAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

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
