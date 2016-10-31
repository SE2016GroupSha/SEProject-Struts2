package xyz.sesha.project.api.search;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/search/fuzzy
 * <br>������params={"keys": ["ѧУ", "10����"...]}
 * <br>���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>˵����ȫ����ȫ�ֶ�ģ��������Ĭ������ʱ��Ӵ�С������Ϊ�ؼ�������(�մ��ؼ��ֶ�Ӧȫ������)������data�����pdo����
 * 
 * @author Administrator
 */
public class FuzzyAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

  /**
   * ���ݸ����Ĺؼ����б�ģ�������õ�data��json�ַ���list
   * @param keys �ؼ�������
   * @return ���ذ������йؼ��ֵ�data��json�ַ���list
   */
  public static List<String> searchDatas(Collection<String> keys) {
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
