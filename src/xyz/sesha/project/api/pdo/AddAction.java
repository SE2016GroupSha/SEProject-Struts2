package xyz.sesha.project.api.pdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.PDO;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/add
 * <br>������params={"pdos": [pdo1, pdo2...]}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵��������µ�PDO������Ϊpdo���飬�ɹ�����success��ʧ�ܷ���failed��ʧ�ܲ�������κ�PDO
 * <br>��ȫ���������û�����
 * 
 * @author Wan XiaoLong
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
    return PDO.addPDO(pdos);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      //�ж�key�Ƿ����
      if (!json.has("pdos")) {
        ret =  false;
      }
      
      //nullֵ�жϣ�������instanceof�ؼ�����
      
      //pdos���ͣ�net.sf.json.JSONArray
      Object pdosObj = json.get("pdos");
      if (!(pdosObj instanceof JSONArray)) {
        ret =  false;
      }
      //pdos���ƣ����鲻Ϊ��
      if (((JSONArray) pdosObj).size() <= 0) {
        ret =  false;
      }

    } catch (JSONException e) {
      ret =  false;
    } catch (Exception e) {
      ret =  false;
    }

    return ret;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/pdo/add]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      return "success";
    }
    
    JSONObject paramsJson = JSONObject.fromObject(params);
    JSONArray pdoJsonArray = paramsJson.getJSONArray("pdos");
    List<String> pdoStrings = new ArrayList<String>();
    
    //����pdo��json�ַ���List
    Iterator<?> it = pdoJsonArray.iterator();
    while (it.hasNext()) {
      pdoStrings.add(it.next().toString());
    }
    
    //���pdo����ȡ���
    if (add(pdoStrings)) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    return "success";
  }
}
