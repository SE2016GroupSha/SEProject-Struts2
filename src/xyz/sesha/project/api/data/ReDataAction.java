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
   * 获取当前user的全部数据
   * 
   * @return 返回data的json字符串List
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
    
    //暂未实现，目前只返回该user全部数据与该pdo类型数据的差集
    List<String> allDataJsons = getAllDatas();
    
    
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
