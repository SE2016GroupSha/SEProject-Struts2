package xyz.sesha.project.api.search;

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
 * URL： api/search/fuzzy <br>
 * 参数：params={"keys": ["学校", "10分钟"...]} <br>
 * 返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]} <br>
 * 说明：全数据全字段模糊搜索，默认序是时间从大到小，输入为关键字数组(空串关键字对应全部数据)，返回data数组和pdo数组
 * 
 * @author Wan XiaoLong
 */
public class FuzzyAction extends AbstractApiAction {

  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

  /**
   * 根据给定的关键字列表，模糊搜索得到data的json字符串list
   * 
   * @param userId 用户id
   * @param keys 关键字容器
   * @return 返回包含所有关键字的data的json字符串list
   */
  private List<String> searchDatas(String userId, Collection<String> keys) {
    
    List<String> dataIds = UserIdKeysToDataIds.getIds(userId, keys);
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
      if (!json.has("keys")) {
        ret = false;
      }

      // null值判断，包含在instanceof关键字中

      // keys类型：net.sf.json.JSONArray
      Object keysObj = json.get("keys");
      if (!(keysObj instanceof JSONArray)) {
        ret = false;
      }
      // keys限制：数组不为空
      if (((JSONArray) keysObj).size() <= 0) {
        ret = false;
      }
      // keys限制：成员类型：java.lang.String
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

    // 检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/search/fuzzy]: 非法参数(" + params + ")");
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      return "success";
    }
    
    //获取user的id
    String id = UserUtil.getUserId();
    if (id==null) {
      JSONArray dataJsonArray = new JSONArray();
      JSONArray pdoJsonArray = new JSONArray();
      result.put("datas", dataJsonArray);
      result.put("pdos", pdoJsonArray);
      logger.info("[API][api/search/fuzzy]: 未登陆");
      return "success";
    }

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    JSONArray keyArray = paramsJsonObj.getJSONArray("keys");

    // 返回json中的datas数组和pdos数组
    JSONArray dataJsonArray = new JSONArray();
    JSONArray pdoJsonArray = new JSONArray();

    // 提取并生成关键字Set,这里对关键字进行去重
    Set<String> keys = new TreeSet<String>();
    for (int i = 0; i < keyArray.size(); i++) {
      keys.add(keyArray.getString(i));
    }

    // 查询得到所有满足条件的data的json字符串
    List<String> dataJsonStrings = searchDatas(id, keys);

    // 对data按时间从大到小排序
    Collections.sort(dataJsonStrings, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(dataJsonStrings);

    // 把data添加到返回json中的datas数组中
    for (String jsonString : dataJsonStrings) {
      JSONObject jsonObj = JSONObject.fromObject(jsonString);
      dataJsonArray.add(jsonObj);
    }

    // 提取已获取data的pdo的id，并去重
    Set<String> allPdoIdsSet = new TreeSet<String>();
    for (String dataJsonString : dataJsonStrings) {
      JSONObject dataJson = JSONObject.fromObject(dataJsonString);
      allPdoIdsSet.add(dataJson.getString("pdo"));
    }

    // 获取对应全部pdo的json字符串
    List<String> pdoJsonStrings = getPdos(allPdoIdsSet);

    // 把pdo添加到返回json中的pdos数组中
    for (String pdoJson : pdoJsonStrings) {
      pdoJsonArray.add(JSONObject.fromObject(pdoJson));
    }

    result.put("datas", dataJsonArray);
    result.put("pdos", pdoJsonArray);

    return "success";
  }
}
