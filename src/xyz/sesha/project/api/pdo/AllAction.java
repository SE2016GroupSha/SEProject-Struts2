package xyz.sesha.project.api.pdo;

import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

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
public class AllAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AllAction.class);

  /**
   * �������е�pdo��json�ַ���(����)
   * @param userId user��id
   * @return ����ָ��user������pdo��json�ַ���
   */
  private List<String> all(String userId) {
    return null;
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
