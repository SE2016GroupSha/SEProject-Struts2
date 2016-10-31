package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/reg
 * <br>������params={"user": user}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵����ע����user���ɹ�����success��ʧ�ܷ���failed
 * 
 * @author Administrator
 */
public class RegAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(RegAction.class);

  /**
   * userע��
   * @param userJson user��json�ַ���
   * @return ע��ɹ�����true��ע��ʧ�ܷ���false
   */
  private boolean register(String userJson) {
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
