package xyz.sesha.project.api;

import net.sf.json.JSONObject;

/**
 * ǰ��API������Ӧ�������
 * 
 * <p>��API����Actionͨ�õ����ԣ��Լ����еķ������з���
 *
 * @author Lu Xin
 */
public abstract class AbstractApiAction {

  /**
   * ��ǰ�˴�����Http����params
   */
  protected String params;
  
  /**
   * ���ظ�ǰ�˵�Json�ַ�����Ӧ
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
   * ����http���ݵ�json�����Ϸ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ������飬�ɻ������ݹ��ɵ�����ĳ�Ա���ͺϷ���
   * <br>���ԣ���object��������ĳ�Ա�Ϸ��ԣ�ֵ���߼�����
   * @return ���ؼ��������Ϸ���true���Ƿ���false
   */
  public abstract boolean checkParamsJsonFormat();
  
  /**
   * Action��������
   * @return ����success�ַ���
   */
  public abstract String execute();
}
