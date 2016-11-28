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
 * 前端API请求响应类 <br>
 * <br>
 * URL： api/data/relateddata <br>
 * 参数：params={"ids": ["5", "4"...]} <br>
 * 返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * 说明：获取已有数据的全部关联数据(无重复无序)以及对应PDO(无重复)，参数ids为id数组(数据的id)，返回data数组和pdo数组<br>
 * 安全：无需做多用户处理
 * 
 * @author Wan XiaoLong
 */
public class RelatedDataAction extends AbstractApiAction {

  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(RelatedDataAction.class);

  /**
   * 由data的id获取data的json字符串
   * 
   * @param ids data的id的容器
   * @return 返回data的json字符串List
   */
  private List<String> getDatas(Collection<String> ids) {
    return Data.getDataJson(ids);
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
      if (!json.has("ids")) {
        ret = false;
      }

      // null值判断，包含在instanceof关键字中

      // ids类型：net.sf.json.JSONArray
      Object idsObj = json.get("ids");
      if (!(idsObj instanceof JSONArray)) {
        ret = false;
      }
      // ids限制：数组不为空
      if (((JSONArray) idsObj).size() <= 0) {
        ret = false;
      }
      // ids限制：成员类型：java.lang.String
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

    // 检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/data/relateddata]: 非法参数(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }

    JSONObject paramsJson = JSONObject.fromObject(params);
    JSONArray dataIdJsonArray = paramsJson.getJSONArray("ids");

    // 根据id获取全部的data的json字符串，id在这里去重
    Set<String> dataIds = new TreeSet<String>();
    for (int i = 0; i < dataIdJsonArray.size(); i++) {
      dataIds.add(dataIdJsonArray.getString(i));
    }
    List<String> dataJsonTempStrings = getDatas(dataIds);


    // 提取已获取所有data的所有关联data的id，并去重
    Set<String> allDataIdsSet = new TreeSet<String>();
    for (String dataJsonTempString : dataJsonTempStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonTempString);
      JSONArray relatedDataIdArray = dataJson.getJSONArray("related_data");
      for (int i = 0; i < relatedDataIdArray.size(); i++) {
        allDataIdsSet.add(relatedDataIdArray.getString(i));
      }
    }

    // 获取全部关联data的json字符串
    List<String> dataJsonStrings = getDatas(allDataIdsSet);

    // 提取已获取data的pdo的id，并去重
    Set<String> allPdoIdsSet = new TreeSet<String>();
    for (String dataJsonString : dataJsonStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      allPdoIdsSet.add(dataJson.getString("pdo"));
    }

    // 获取对应全部pdo的json字符串
    List<String> pdoJsonStrings = getPdos(allPdoIdsSet);

    // 构建返回json字符串
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
