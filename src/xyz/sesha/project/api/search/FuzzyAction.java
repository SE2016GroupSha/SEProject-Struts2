package xyz.sesha.project.api.search;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/search/fuzzy
 * <br>参数：params={"keys": ["学校", "10分钟"...]}
 * <br>返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>说明：全数据全字段模糊搜索，默认序是时间从大到小，输入为关键字数组(空串关键字对应全部数据)，返回data数组和pdo数组
 * 
 * @author Administrator
 */
public class FuzzyAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(FuzzyAction.class);

  /**
   * 根据给定的关键字列表，模糊搜索得到data的json字符串list
   * @param keys 关键字容器
   * @return 返回包含所有关键字的data的json字符串list
   */
  public static List<String> searchDatas(Collection<String> keys) {
    return null;
  }
  
  /**
   * 由pdo的id获取pdo的json字符串
   * @param ids pdo的id的容器
   * @return 返回pdo的json字符串List
   */
  public static List<String> getPdos(Collection<String> ids) {
    return null;
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    JSONObject paramsJsonObj = JSONObject.fromObject(params);
    result = new JSONObject();
    result.put("receive", paramsJsonObj);
    
    logger.info("参数: " + params);
    logger.info("返回: " + result.toString());
    
    return "success";
  }
}
