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
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/all
 * <br>������params={}
 * <br>���أ�{"pdos": [pdo1, pdo2...]}
 * <br>˵�����������е�PDO��Ĭ������ʱ��Ӵ�С������ֵΪpdo����
 * 
 * @author Wan XiaoLong
 */
public class AllAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(AllAction.class);

  /**
   * �������е�pdo��json�ַ���(����)
   * @param userId user��id
   * @return ����ָ��user������pdo��json�ַ���
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
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/pdo/all]: �Ƿ�����(" + params + ")");
      JSONArray pdoJsonArray = new JSONArray();
      result.put("pdos", pdoJsonArray);
      return "success";
    }
    
    //��ȡuser��id
    String id = UserUtil.getUserId();
    if (id==null) {
      JSONArray pdoJsonArray = new JSONArray();
      result.put("pdos", pdoJsonArray);
      logger.info("[API][api/pdo/all]: δ��½");
      return "success";
    }
    
    //��ȡȫ��pdo
    JSONArray pdoJsonArray = new JSONArray();
    List<String> pdoJsonStrings = all(id);
    
    //��pdo��ʱ��Ӵ�С����
    Collections.sort(pdoJsonStrings, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        JSONObject json1 = JSONObject.fromObject(s1);
        JSONObject json2 = JSONObject.fromObject(s2);
        return Long.valueOf(json1.getLong("time")).compareTo(Long.valueOf(json2.getLong("time")));
      }
    });
    Collections.reverse(pdoJsonStrings);
          
    //���ɷ��ؽ��
    for (String jsonString : pdoJsonStrings) {
      JSONObject jsonObj = JSONObject.fromObject(jsonString);
      pdoJsonArray.add(jsonObj);
    }
    
    result.put("pdos", pdoJsonArray);
    
    return "success";
  }
}
