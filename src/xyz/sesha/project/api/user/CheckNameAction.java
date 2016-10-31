package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/checkname
 * <br>������params={"name": "��ү"}
 * <br>���أ�{"valid": "true"} �� {"valid": "false"}
 * <br>˵�������user�����Ƿ����(����Ƿ����),���÷���true�������÷���false
 * 
 * @author Administrator
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * ���user�����Ƿ����(����Ƿ����)
   * @param name ������user����
   * @return ���÷���true�������÷���false
   */
  public static boolean check(String name) {
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
