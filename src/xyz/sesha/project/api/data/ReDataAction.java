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
 * 前端API请求响应类 <br>
 * <br>
 * URL： api/data/redata <br>
 * 参数：params={"pdo": "5"} <br>
 * 返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * 说明：获取给定PDO的推荐关联数据，参数pdo为PDO的id，返回data数组和pdo数组<br>
 * 安全：已做多用户处理
 * 
 * @author Administrator
 */
public class ReDataAction extends AbstractApiAction {

  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(ReDataAction.class);

  /**
   * 获取当前user当前pdo的推荐数据
   * 
   * @return 返回data的json字符串List
   */
  @SuppressWarnings("serial")
  private List<String> getRecommendDatas(String pdoId) {
    String userId = UserUtil.getUserId();
    if (userId == null) {
      return new ArrayList<String>();
    }

    String pdoJsonString = PDO.getPDOJson(pdoId);
    JSONObject pdoJsonObject = JSONObject.fromObject(pdoJsonString);
    
    //当前PDO的name和fields
    String pdoName = pdoJsonObject.getString("name");
    List<String> pdoFields = new ArrayList<String>();
    
    JSONArray pdoFieldsArray = pdoJsonObject.getJSONArray("fields");
    for (int i=0; i<pdoFieldsArray.size(); i++) {
      pdoFields.add(pdoFieldsArray.getString(i));
    }
    
    //全部数据
    List<String> dataIds = UserIdKeysToDataIds.getIds(userId, new ArrayList<String>(){{add("");}});
    List<String> allDatas = Data.getDataJson(dataIds);
    
    //过滤同类型数据  & 过滤未来数据
    List<String> filterDatas = new ArrayList<String>();
    for (String dataJsonString : allDatas) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      Long dataTime = dataJson.getLong("time");
      String dataPdoId = dataJson.getString("pdo");
      if (!dataPdoId.equals(pdoId) && dataTime<System.currentTimeMillis()) {
        filterDatas.add(dataJsonString);
      }
    }
    
    // 对data按时间从大到小排序
    Collections.sort(filterDatas, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(filterDatas);
    
    //返回数据
    List<String> recommendDatas = new ArrayList<String>();
    
    //遍历数据
    datalabel:
    for (String dataJsonString : filterDatas) {
      if (recommendDatas.size() >= 5) {
        break;
      }
      JSONObject dataJsonObject = JSONObject.fromObject(dataJsonString);
      String dataPdoId = dataJsonObject.getString("pdo");
      String dataPdoJsonString = PDO.getPDOJson(dataPdoId);
      JSONObject dataPdoJsonObject = JSONObject.fromObject(dataPdoJsonString);
      
      //当前数据的pdo的name，pdo的fields，当前数据的values
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
      
      //算法开始

      //1.data方包含pdo方
      
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
      
      //2.pdo方包含data方
      
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
      
      //不满足任何包含，该数据被跳过
    }
    
    //不足5个，从时间最近开始补位
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
   * 由pdo的id获取pdo的json字符串
   * 
   * @param ids pdo的id的容器
   * @return 返回pdo的json字符串List
   */
  private List<String> getPdos(Collection<String> ids) {
    return PDO.getPDOJson(ids);
  }

  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;

    try {
      JSONObject json = JSONObject.fromObject(params);

      // 判断key是否存在
      if (!json.has("pdo")) {
        ret = false;
      }

      // null值判断，包含在instanceof关键字中

      // pdo类型：java.lang.String
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

    // 检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/data/redata]: 非法参数(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }

    //获取参数中的PDO的id
    JSONObject paramsJson = JSONObject.fromObject(params);
    String pdoId = paramsJson.getString("pdo");
    
    //推荐策略：各种名称、各种字段名、各种字段值，相互包含即视为推荐数据
    List<String> allDataJsons = getRecommendDatas(pdoId);
    
    
    //最多返回5个，这段重复逻辑暂时不删了
    //获取pdo过滤后的data的List，同时提取data的pdo的id并去重
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
    
    // 对data按时间从大到小排序
    Collections.sort(filterDataJsons, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(filterDataJsons);

    // 获取对应过滤data的全部pdo的json字符串
    List<String> filtePdoJsons = getPdos(filtePdoIdsSet);

    // 构建返回json字符串
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
