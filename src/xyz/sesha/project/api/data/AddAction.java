package xyz.sesha.project.api.data;

import java.util.Collection;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

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
public class AddAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AddAction.class);
  
  /**
   * ����µ�data
   * @param datas ���data��json�ַ���������
   * @return ����ִ�н�����ɹ��򷵻�true��ʧ���򷵻�false��������ʧ��ʱ��������κ�Ӱ��
   */
  private boolean add(Collection<String> datas) {
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
