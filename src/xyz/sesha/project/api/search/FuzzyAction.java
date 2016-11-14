package xyz.sesha.project.api.search;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.Data;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.store.index.UserIdKeysToDataIds;

/**
 * ǰ��API������Ӧ�� <br>
 * <br>
 * URL�� api/search/fuzzy <br>
 * ������params={"keys": ["ѧУ", "10����"...]} <br>
 * ���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * ˵����ȫ����ȫ�ֶ�ģ��������Ĭ������ʱ��Ӵ�С������Ϊ�ؼ�������(�մ��ؼ��ֶ�Ӧȫ������)������data�����pdo����
 * 
 * @author Wan XiaoLong
 */
public class FuzzyAction extends AbstractApiAction {

  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

  /**
   * ���ݸ����Ĺؼ����б�ģ�������õ�data��json�ַ���list
   * 
   * @param keys �ؼ�������
   * @return ���ذ������йؼ��ֵ�data��json�ַ���list
   */
  private List<String> searchDatas(Collection<String> keys) {
    List<String> dataIds = UserIdKeysToDataIds.getIds("0", keys);
    return Data.getDataJson(dataIds);
  }

  /**
   * ��pdo��id��ȡpdo��json�ַ���
   * 
   * @param ids pdo��id������
   * @return ����pdo��json�ַ���List
   */
  private List<String> getPdos(Collection<String> ids) {
    return PDO.getPDOJson(ids);
  }

  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;

    try {
      JSONObject json = JSONObject.fromObject(params);

      // �ж�key�Ƿ����
      if (!json.has("keys")) {
        ret = false;
      }

      // nullֵ�жϣ�������instanceof�ؼ�����

      // keys���ͣ�net.sf.json.JSONArray
      Object keysObj = json.get("keys");
      if (!(keysObj instanceof JSONArray)) {
        ret = false;
      }
      // keys���ƣ����鲻Ϊ��
      if (((JSONArray) keysObj).size() <= 0) {
        ret = false;
      }
      // keys���ƣ���Ա���ͣ�java.lang.String
      for (int i = 0; i < ((JSONArray) keysObj).size(); i++) {
        if (!(((JSONArray) keysObj).get(i) instanceof String)) {
          ret = false;
        }
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
      logger.error("[API][api/search/fuzzy]: �Ƿ�����(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    JSONArray keyArray = paramsJsonObj.getJSONArray("keys");

    // ����json�е�datas�����pdos����
    JSONArray dataJsonArray = new JSONArray();
    JSONArray pdoJsonArray = new JSONArray();

    // ��ȡ�����ɹؼ���Set,����Թؼ��ֽ���ȥ��
    Set<String> keys = new TreeSet<String>();
    for (int i = 0; i < keyArray.size(); i++) {
      keys.add(keyArray.getString(i));
    }

    // ��ѯ�õ���������������data��json�ַ���
    List<String> dataJsonStrings = searchDatas(keys);

    // ��data��ʱ��Ӵ�С����
    TreeMap<Long, JSONObject> sortTree =
        new TreeMap<Long, JSONObject>((n1, n2) -> n2.compareTo(n1));
    for (String dataJsonString : dataJsonStrings) {
      JSONObject jsonObj = JSONObject.fromObject(dataJsonString);
      sortTree.put(jsonObj.getLong("time"), jsonObj);
    }

    // ��data��ӵ�����json�е�datas������
    for (JSONObject jsonObj : sortTree.values()) {
      dataJsonArray.add(jsonObj);
    }

    // ��ȡ�ѻ�ȡdata��pdo��id����ȥ��
    Set<String> allPdoIdsSet = new TreeSet<String>();
    for (String dataJsonString : dataJsonStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      allPdoIdsSet.add(dataJson.getString("pdo"));
    }

    // ��ȡ��Ӧȫ��pdo��json�ַ���
    List<String> pdoJsonStrings = getPdos(allPdoIdsSet);

    // ��pdo��ӵ�����json�е�pdos������
    for (String pdoJson : pdoJsonStrings) {
      pdoJsonArray.add(JSONObject.fromObject(pdoJson));
    }

    result.put("datas", dataJsonArray);
    result.put("pdos", pdoJsonArray);

    return "success";
  }
}
