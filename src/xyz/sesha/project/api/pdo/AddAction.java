package xyz.sesha.project.api.pdo;

import java.util.Collection;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/add
 * <br>������params={"pdos": [pdo1, pdo2...]}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵��������µ�PDO������Ϊpdo���飬�ɹ�����success��ʧ�ܷ���failed��ʧ�ܲ�������κ�PDO
 * 
 * @author Administrator
 */
public class AddAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AddAction.class);

  /**
   * ����µ�pdo
   * @param pdos pdo��json�ַ���������
   * @return ����ִ�н�����ɹ��򷵻�true��ʧ���򷵻�false��������ʧ��ʱ��������κ�Ӱ��
   */
  private boolean add(Collection<String> pdos) {
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
