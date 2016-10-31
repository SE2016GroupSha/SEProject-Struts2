package xyz.sesha.project.api.data;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/data/relateddata
 * <br>������params={"ids": ["5", "4"...]}
 * <br>���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>˵������ȡ�������ݵ�ȫ����������(���ظ�����)�Լ���ӦPDO(���ظ�)������idsΪid����(���ݵ�id)������data�����pdo����
 * 
 * @author Administrator
 */
public class RelatedDataAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(RelatedDataAction.class);

  /**
   * ��data��id��ȡdata��json�ַ���
   * @param ids data��id������
   * @return ����data��json�ַ���List
   */
  public static List<String> getDatas(Collection<String> ids) {
    return null;
  }
  
  /**
   * ��pdo��id��ȡpdo��json�ַ���
   * @param ids pdo��id������
   * @return ����pdo��json�ַ���List
   */
  public static List<String> getPdos(Collection<String> ids) {
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
