package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/logout
 * <br>������params={}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵����user�˳����ɹ�����success��ʧ�ܷ���failed
 * 
 * @author Administrator
 */
public class LogoutAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * user�˳���ֱ��Ӱ��һ��session��user��Ϣ
   * @return �˳��ɹ�����true���˳�ʧ�ܷ���false
   */
  public static boolean logout() {
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
    
    logger.info("����: " + params);
    logger.info("����: " + result.toString());
    
    return "success";
  }
}
