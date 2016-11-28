package xyz.sesha.project.api.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.Data;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/data/add
 * <br>������params={"datas": [data1, data2...]}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵��������µ����ݣ�����Ϊdata���飬�ɹ�����success��ʧ�ܷ���failed��ʧ�ܲ�������κ�data
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
   * ����µ�data
   * @param datas ���data��json�ַ���������
   * @return ����ִ�н�����ɹ��򷵻�true��ʧ���򷵻�false��������ʧ��ʱ��������κ�Ӱ��
   */
  private boolean add(Collection<String> datas) {
    return Data.addData(datas);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;

    try {
        JSONObject json = JSONObject.fromObject(params);

        // �ж�key�Ƿ����
        if (!json.has("datas")) {
            ret = false;
        }

        // nullֵ�жϣ�������instanceof�ؼ�����

        // datas���ͣ�net.sf.json.JSONArray
        Object datasObj = json.get("datas");
        if (!(datasObj instanceof JSONArray)) {
            ret = false;
        }
        // datas���ƣ����鲻Ϊ��
        if (((JSONArray) datasObj).size() <= 0) {
            ret = false;
        }

    } catch (JSONException e) {
        ret = false;
    } catch (Exception e) {
        ret = false;
    }

    return ret;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();

    // ��������Ϸ���
    if (!checkParamsJsonFormat()) {
        logger.error("[API][api/data/add]: �Ƿ�����(" + params + ")");
        result.put("state", "failed");
        return "success";
    }

    JSONObject paramsJson = JSONObject.fromObject(params);
    JSONArray pdoJsonArray = paramsJson.getJSONArray("datas");
    List<String> pdoStrings = new ArrayList<String>();

    // ����data��json�ַ���List
    Iterator<?> it = pdoJsonArray.iterator();
    while (it.hasNext()) {
        pdoStrings.add(it.next().toString());
    }

    // ���data����ȡ���
    if (add(pdoStrings)) {
        result.put("state", "success");
    } else {
        result.put("state", "failed");
    }

    return "success";
    
  }
}
