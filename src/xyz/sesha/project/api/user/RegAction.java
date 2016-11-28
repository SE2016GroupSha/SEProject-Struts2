package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/reg
 * <br>������params={"user": user}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵����ע����user���ɹ�����success��ʧ�ܷ���failed
 * 
 * @author Si Aoran
 */
public class RegAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(RegAction.class);

  /**
   * userע��
   * @param userJson user��json�ַ���
   * @return ע��ɹ�����true��ע��ʧ�ܷ���false
   */
  private boolean register(String userJson) {
    return User.addUser(userJson);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      //�ж�key�Ƿ����
      if (!json.has("user")) {
        ret =  false;
      }
      
      //nullֵ�ж�
      if (json.get("user")==null) {
        ret =  false;
      }
      
    } catch (JSONException e) {
      ret =  false;
    } catch (Exception e) {
      ret =  false;
    }

    return ret;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/reg][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/reg]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/reg][��Ӧ]: result=" + result);
      return "success";
    }
    
    JSONObject paramsJson = JSONObject.fromObject(params);
    String userJsonString = paramsJson.getString("user");
    
    //���user����ȡ���
    if (register(userJsonString)) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/reg][��Ӧ]: result=" + result);
    return "success";
  }
  
}
