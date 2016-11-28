package xyz.sesha.project.api.data;

import java.util.Collection;
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

/**
 * ǰ��API������Ӧ�� <br>
 * <br>
 * URL�� api/data/relateddata <br>
 * ������params={"ids": ["5", "4"...]} <br>
 * ���أ�{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * ˵������ȡ�������ݵ�ȫ����������(���ظ�����)�Լ���ӦPDO(���ظ�)������idsΪid����(���ݵ�id)������data�����pdo����<br>
 * ��ȫ�����������û�����
 * 
 * @author Wan XiaoLong
 */
public class RelatedDataAction extends AbstractApiAction {

  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(RelatedDataAction.class);

  /**
   * ��data��id��ȡdata��json�ַ���
   * 
   * @param ids data��id������
   * @return ����data��json�ַ���List
   */
  private List<String> getDatas(Collection<String> ids) {
    return Data.getDataJson(ids);
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
      if (!json.has("ids")) {
        ret = false;
      }

      // nullֵ�жϣ�������instanceof�ؼ�����

      // ids���ͣ�net.sf.json.JSONArray
      Object idsObj = json.get("ids");
      if (!(idsObj instanceof JSONArray)) {
        ret = false;
      }
      // ids���ƣ����鲻Ϊ��
      if (((JSONArray) idsObj).size() <= 0) {
        ret = false;
      }
      // ids���ƣ���Ա���ͣ�java.lang.String
      for (int i = 0; i < ((JSONArray) idsObj).size(); i++) {
        if (!(((JSONArray) idsObj).get(i) instanceof String)) {
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
      logger.error("[API][api/data/relateddata]: �Ƿ�����(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }

    JSONObject paramsJson = JSONObject.fromObject(params);
    JSONArray dataIdJsonArray = paramsJson.getJSONArray("ids");

    // ����id��ȡȫ����data��json�ַ�����id������ȥ��
    Set<String> dataIds = new TreeSet<String>();
    for (int i = 0; i < dataIdJsonArray.size(); i++) {
      dataIds.add(dataIdJsonArray.getString(i));
    }
    List<String> dataJsonTempStrings = getDatas(dataIds);


    // ��ȡ�ѻ�ȡ����data�����й���data��id����ȥ��
    Set<String> allDataIdsSet = new TreeSet<String>();
    for (String dataJsonTempString : dataJsonTempStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonTempString);
      JSONArray relatedDataIdArray = dataJson.getJSONArray("related_data");
      for (int i = 0; i < relatedDataIdArray.size(); i++) {
        allDataIdsSet.add(relatedDataIdArray.getString(i));
      }
    }

    // ��ȡȫ������data��json�ַ���
    List<String> dataJsonStrings = getDatas(allDataIdsSet);

    // ��ȡ�ѻ�ȡdata��pdo��id����ȥ��
    Set<String> allPdoIdsSet = new TreeSet<String>();
    for (String dataJsonString : dataJsonStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      allPdoIdsSet.add(dataJson.getString("pdo"));
    }

    // ��ȡ��Ӧȫ��pdo��json�ַ���
    List<String> pdoJsonStrings = getPdos(allPdoIdsSet);

    // ��������json�ַ���
    JSONArray dataJsonArray = new JSONArray();
    JSONArray pdoJsonArray = new JSONArray();
    for (String dataJson : dataJsonStrings) {
      dataJsonArray.add(JSONObject.fromObject(dataJson));
    }
    for (String pdoJson : pdoJsonStrings) {
      pdoJsonArray.add(JSONObject.fromObject(pdoJson));
    }
    result.put("datas", dataJsonArray);
    result.put("pdos", pdoJsonArray);

    return "success";
  }
}
