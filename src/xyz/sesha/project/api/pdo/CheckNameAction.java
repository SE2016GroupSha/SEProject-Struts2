package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

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
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * ���pdo�����Ƿ����(����Ƿ����)
   * @param userId user��id
   * @param pdoName ������pdo����
   * @return ���÷���true�������÷���false
   */
  private boolean check(String userId, String pdoName) {
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
