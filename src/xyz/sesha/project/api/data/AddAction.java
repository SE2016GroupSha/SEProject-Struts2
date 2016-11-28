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
 * 前端API请求响应类
 * <br>
 * <br>URL： api/data/add
 * <br>参数：params={"datas": [data1, data2...]}
 * <br>返回：{"state": "success"} 或 {"state": "failed"}
 * <br>说明：添加新的数据，参数为data数组，成功返回success，失败返回failed，失败不会添加任何data
 * <br>安全：已做多用户处理
 * 
 * @author Wan XiaoLong
 */
public class AddAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(AddAction.class);
  
  /**
   * 添加新的data
   * @param datas 存放data的json字符串的容器
   * @return 返回执行结果，成功则返回true，失败则返回false，当返回失败时不会产生任何影响
   */
  private boolean add(Collection<String> datas) {
    return Data.addData(datas);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;

    try {
        JSONObject json = JSONObject.fromObject(params);

        // 判断key是否存在
        if (!json.has("datas")) {
            ret = false;
        }

        // null值判断，包含在instanceof关键字中

        // datas类型：net.sf.json.JSONArray
        Object datasObj = json.get("datas");
        if (!(datasObj instanceof JSONArray)) {
            ret = false;
        }
        // datas限制：数组不为空
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

    // 检验参数合法性
    if (!checkParamsJsonFormat()) {
        logger.error("[API][api/data/add]: 非法参数(" + params + ")");
        result.put("state", "failed");
        return "success";
    }

    JSONObject paramsJson = JSONObject.fromObject(params);
    JSONArray pdoJsonArray = paramsJson.getJSONArray("datas");
    List<String> pdoStrings = new ArrayList<String>();

    // 构建data的json字符串List
    Iterator<?> it = pdoJsonArray.iterator();
    while (it.hasNext()) {
        pdoStrings.add(it.next().toString());
    }

    // 添加data，获取结果
    if (add(pdoStrings)) {
        result.put("state", "success");
    } else {
        result.put("state", "failed");
    }

    return "success";
    
  }
}
