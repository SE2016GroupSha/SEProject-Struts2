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
   * ��ȡ��ǰuser��ǰpdo���Ƽ�����
   * 
   * @return ����data��json�ַ���List
   */
  @SuppressWarnings("serial")
  private List<String> getRecommendDatas(String pdoId) {
    String userId = UserUtil.getUserId();
    if (userId == null) {
      return new ArrayList<String>();
    }

    String pdoJsonString = PDO.getPDOJson(pdoId);
    JSONObject pdoJsonObject = JSONObject.fromObject(pdoJsonString);
    
    //��ǰPDO��name��fields
    String pdoName = pdoJsonObject.getString("name");
    List<String> pdoFields = new ArrayList<String>();
    
    JSONArray pdoFieldsArray = pdoJsonObject.getJSONArray("fields");
    for (int i=0; i<pdoFieldsArray.size(); i++) {
      pdoFields.add(pdoFieldsArray.getString(i));
    }
    
    //ȫ������
    List<String> dataIds = UserIdKeysToDataIds.getIds(userId, new ArrayList<String>(){{add("");}});
    List<String> allDatas = Data.getDataJson(dataIds);
    
    //����ͬ��������  & ����δ������
    List<String> filterDatas = new ArrayList<String>();
    for (String dataJsonString : allDatas) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      Long dataTime = dataJson.getLong("time");
      String dataPdoId = dataJson.getString("pdo");
      if (!dataPdoId.equals(pdoId) && dataTime<System.currentTimeMillis()) {
        filterDatas.add(dataJsonString);
      }
    }
    
    // ��data��ʱ��Ӵ�С����
    Collections.sort(filterDatas, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(filterDatas);
    
    //��������
    List<String> recommendDatas = new ArrayList<String>();
    
    //��������
    datalabel:
    for (String dataJsonString : filterDatas) {
      if (recommendDatas.size() >= 5) {
        break;
      }
      JSONObject dataJsonObject = JSONObject.fromObject(dataJsonString);
      String dataPdoId = dataJsonObject.getString("pdo");
      String dataPdoJsonString = PDO.getPDOJson(dataPdoId);
      JSONObject dataPdoJsonObject = JSONObject.fromObject(dataPdoJsonString);
      
      //��ǰ���ݵ�pdo��name��pdo��fields����ǰ���ݵ�values
      String dataPdoName = dataPdoJsonObject.getString("name");
      List<String> dataPdoFields = new ArrayList<String>();
      List<String> dataValues = new ArrayList<String>();
      
      JSONArray dataPdoFieldsArray = dataPdoJsonObject.getJSONArray("fields");
      for (int i=0; i<dataPdoFieldsArray.size(); i++) {
        dataPdoFields.add(dataPdoFieldsArray.getString(i));
      }
      JSONArray dataValuesArray = dataJsonObject.getJSONArray("values");
      for (int i=0; i<dataValuesArray.size(); i++) {
        dataValues.add(dataValuesArray.getString(i));
      }
      
      //�㷨��ʼ

      //1.data������pdo��
      
      //dataPdoName               pdoName
      //dataPdoFields             pdoFields
      //dataValues
      
      //1)dataPdoName
      if (dataPdoName.contains(pdoName)) {
        recommendDatas.add(dataJsonString);
        continue datalabel;
      }
      for (String pdoField : pdoFields) {
        if (dataPdoName.contains(pdoField)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
      }
      //2)dataPdoFields
      for (String dataPdoField : dataPdoFields) {
        if (dataPdoField.contains(pdoName)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
        for (String pdoField : pdoFields) {
          if (dataPdoField.contains(pdoField)) {
            recommendDatas.add(dataJsonString);
            continue datalabel;
          }
        }
      }
      //3)dataValues
      for (String dataValue : dataValues) {
        if (dataValue.contains(pdoName)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
        for (String pdoField : pdoFields) {
          if (dataValue.contains(pdoField)) {
            recommendDatas.add(dataJsonString);
            continue datalabel;
          }
        }
      }
      
      //2.pdo������data��
      
      //pdoName               dataPdoName
      //pdoFields             dataPdoFields
      //                      dataValues
      
      //1)pdoName
      if (pdoName.contains(dataPdoName)) {
        recommendDatas.add(dataJsonString);
        continue datalabel;
      }
      for (String dataPdoField : dataPdoFields) {
        if (pdoName.contains(dataPdoField)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
      }
      for (String dataValue : dataValues) {
        if (pdoName.contains(dataValue)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
      }
      //2)pdoFields
      for (String pdoField : pdoFields) {
        if (pdoField.contains(dataPdoName)) {
          recommendDatas.add(dataJsonString);
          continue datalabel;
        }
        for (String dataPdoField : dataPdoFields) {
          if (pdoField.contains(dataPdoField)) {
            recommendDatas.add(dataJsonString);
            continue datalabel;
          }
        }
        for (String dataValue : dataValues) {
          if (pdoField.contains(dataValue)) {
            recommendDatas.add(dataJsonString);
            continue datalabel;
          }
        }
      }
      
      //�������κΰ����������ݱ�����
    }
    
    //����5������ʱ�������ʼ��λ
    if (recommendDatas.size() < 5) {
      for (String dataJsonString : filterDatas) {
        if (recommendDatas.size() >= 5) {
          break;
        }
        if (!recommendDatas.contains(dataJsonString)) {
          recommendDatas.add(dataJsonString);
        }
      }
    }

    return recommendDatas;
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
    
    //�Ƽ����ԣ��������ơ������ֶ����������ֶ�ֵ���໥��������Ϊ�Ƽ�����
    List<String> allDataJsons = getRecommendDatas(pdoId);
    
    
    //��෵��5��������ظ��߼���ʱ��ɾ��
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
