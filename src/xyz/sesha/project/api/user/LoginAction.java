package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.store.index.UserNameToUserId;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/login
 * <br>������params={"user": user}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵����user��½���ɹ�����success��ʧ�ܷ���failed
 * 
 * @author Si Aoran
 */
public class LoginAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * user��½��ֱ��Ӱ��һ��session��user��Ϣ
   * @param userJson user��json�ַ���
   * @return ��½�ɹ�����true����½ʧ�ܷ���false
   */
  private boolean login(String userJson) {
    JSONObject user = JSONObject.fromObject(userJson);
    String name = user.getString("name");
    String pwhash = user.getString("pwhash");
    String id = UserNameToUserId.getId(name);
    if (id==null) {
      return false;
    } else {
      String userRealString = User.getUserJson(id);
      JSONObject userReal = JSONObject.fromObject(userRealString);
      if (!pwhash.equals(userReal.getString("pwhash"))) {
        return false;
      }
    }
    
    //user��½�־û�
    UserUtil.addUserId(id);
    return true;
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
    
    logger.info("[API][api/user/login][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/login]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/login][��Ӧ]: result=" + result);
      return "success";
    }
    
    JSONObject paramsJson = JSONObject.fromObject(params);
    String userJsonString = paramsJson.getString("user");
    
    //user��½����ȡ���
    if (login(userJsonString)) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/login][��Ӧ]: result=" + result);
    return "success";
  }
}
