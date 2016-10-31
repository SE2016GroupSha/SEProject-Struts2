package xyz.sesha.project.api.data;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * 前端API请求响应类
 * <br>
 * <br>URL： api/data/relateddata
 * <br>参数：params={"ids": ["5", "4"...]}
 * <br>返回：{"datas": [data1, data2...], "pdos": [pdo1, pdo2...]}
 * <br>说明：获取已有数据的全部关联数据(无重复无序)以及对应PDO(无重复)，参数ids为id数组(数据的id)，返回data数组和pdo数组
 * 
 * @author Administrator
 */
public class RelatedDataAction extends AbstractApiAction {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(RelatedDataAction.class);

  /**
   * 由data的id获取data的json字符串
   * @param ids data的id的容器
   * @return 返回data的json字符串List
   */
  public static List<String> getDatas(Collection<String> ids) {
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
