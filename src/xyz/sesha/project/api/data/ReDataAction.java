package xyz.sesha.project.api.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.Data;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.store.index.UserIdKeysToDataIds;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ�� <br>
 * <br>
 * URL�� api/data/redata <br>
 * ������params={"pdo": "5"} <br>
 * ���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * ˵������ȡ����PDO���Ƽ��������ݣ�����pdoΪPDO��id������data�����pdo����<br>
 * ��ȫ���������û�����
 * 
 * @author Administrator
 */
public class ReDataAction extends AbstractApiAction {

  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(ReDataAction.class);

  /**
   * ��ȡ��ǰuser��ȫ������
   * 
   * @return ����data��json�ַ���List
   */
  @SuppressWarnings("serial")
  private List<String> getAllDatas() {
    String userId = UserUtil.getUserId();
    if (userId == null) {
      return new ArrayList<String>();
    }
    List<String> dataIds = UserIdKeysToDataIds.getIds(userId, new ArrayList<String>(){{add("");}});
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
      if (!json.has("pdo")) {
        ret = false;
      }

      // nullֵ�жϣ�������instanceof�ؼ�����

      // pdo���ͣ�java.lang.String
      Object pdoObj = json.get("pdo");
      if (!(pdoObj instanceof String)) {
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
      logger.error("[API][api/data/redata]: �Ƿ�����(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }

    //��ȡ�����е�PDO��id
    JSONObject paramsJson = JSONObject.fromObject(params);
    String pdoId = paramsJson.getString("pdo");
    
    //��δʵ�֣�Ŀǰֻ���ظ�userȫ���������pdo�������ݵĲ
    List<String> allDataJsons = getAllDatas();
    
    
    //��ȡpdo���˺��data��List��ͬʱ��ȡdata��pdo��id��ȥ��
    List<String> filterDataJsons = new ArrayList<String>();
    Set<String> filtePdoIdsSet = new TreeSet<String>();
    for (String dataJsonString : allDataJsons) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      String dataPdoId = dataJson.getString("pdo");
      if (!dataPdoId.equals(pdoId)) {
        filterDataJsons.add(dataJsonString);
        filtePdoIdsSet.add(dataPdoId);
      }
    }
    
    // ��data��ʱ��Ӵ�С����
    Collections.sort(filterDataJsons, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(filterDataJsons);

    // ��ȡ��Ӧ����data��ȫ��pdo��json�ַ���
    List<String> filtePdoJsons = getPdos(filtePdoIdsSet);

    // ��������json�ַ���
    JSONArray dataJsonArray = new JSONArray();
    JSONArray pdoJsonArray = new JSONArray();
    for (String dataJson : filterDataJsons) {
      dataJsonArray.add(JSONObject.fromObject(dataJson));
    }
    for (String pdoJson : filtePdoJsons) {
      pdoJsonArray.add(JSONObject.fromObject(pdoJson));
    }
    result.put("datas", dataJsonArray);
    result.put("pdos", pdoJsonArray);

    return "success";
  }
}
