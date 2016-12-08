package xyz.sesha.project.api.pdo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.store.index.UserIdToPDOAllIds;
import xyz.sesha.project.utils.UserUtil;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/pdo/all
 * <br>参数：params={}
 * <br>返回：{"pdos": [pdo1, pdo2...]}
 * <br>说明：返回所有的PDO，默认序是时间从大到小，返回值为pdo数组
 * 
 * @author Wan XiaoLong
 */
public class AllAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(AllAction.class);

  /**
   * 返回所有的pdo的json字符串(无序)
   * @param userId user的id
   * @return 返回指定user的所有pdo的json字符串
   */
  private List<String> all(String userId) {
    List<String> ids = UserIdToPDOAllIds.getAllIds(userId);
    return PDO.getPDOJson(ids);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    //检验参数合法性
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/pdo/all]: 非法参数(" + params + ")");
      JSONArray pdoJsonArray = new JSONArray();
      result.put("pdos", pdoJsonArray);
      return "success";
    }
    
    //获取user的id
    String id = UserUtil.getUserId();
    if (id==null) {
      JSONArray pdoJsonArray = new JSONArray();
      result.put("pdos", pdoJsonArray);
      logger.info("[API][api/pdo/all]: 未登陆");
      return "success";
    }
    
    //获取全部pdo
    JSONArray pdoJsonArray = new JSONArray();
    List<String> pdoJsonStrings = all(id);
    
    //对pdo按时间从大到小排序
    Collections.sort(pdoJsonStrings, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(pdoJsonStrings);
          
    //生成返回结果
    for (String jsonString : pdoJsonStrings) {
      JSONObject jsonObj = JSONObject.fromObject(jsonString);
      pdoJsonArray.add(jsonObj);
    }
    
    result.put("pdos", pdoJsonArray);
    
    return "success";
  }
}
