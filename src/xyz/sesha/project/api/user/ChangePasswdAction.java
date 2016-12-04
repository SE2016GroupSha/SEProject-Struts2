package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/changepasswd
 * <br>������params={"oldpwhash": "5e007e7046425c92111676b1b0999f12", "newpwhash": "2111676b1b0999f125e007e7046425c9"}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵�����޸����룬�ɹ�����success��δ��½��ԭ���������ʧ�ܷ���failed
 * 
 * @author Administrator
 */
public class ChangePasswdAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(ChangePasswdAction.class);

  /**
   * ��֤����user�������Ƿ���ȷ
   * @param id user��id
   * @param pwhash user������hash
   * @return ������ȷ����true��id�����ڻ�������󷵻�false
   */
  private boolean checkPassword(String id, String pwhash) {
    String userJsonString = User.getUserJson(id);
    if (userJsonString==null) {
      return false;
    }
    JSONObject userJsonObject = JSONObject.fromObject(userJsonString);
    if (pwhash.equals(userJsonObject.getString("pwhash"))) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * �޸ĸ���user������
   * @param id user��id
   * @param pwhash user��������hash
   * @return �޸ĳɹ�����true��id�����ڻ��޸�ʧ�ܷ���false
   */
  private boolean changePassword(String id, String pwhash) {
    return null!=User.editUser(id, "pwhash", pwhash);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      // �ж�key�Ƿ����
      if (!json.has("oldpwhash") || !json.has("newpwhash")) {
        ret =  false;
      }
      
      // nullֵ�жϣ�������instanceof�ؼ�����

      // oldpwhash���ͣ�java.lang.String
      Object oldpwhashObj = json.get("oldpwhash");
      if (!(oldpwhashObj instanceof String)) {
        ret = false;
      }
      
      // newpwhash���ͣ�java.lang.String
      Object newpwhashObj = json.get("newpwhash");
      if (!(newpwhashObj instanceof String)) {
        ret = false;
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
    
    logger.info("[API][api/user/changepasswd][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/changepasswd]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][��Ӧ]: result=" + result);
      return "success";
    }
    
    //����½����ȡuser��id
    String id = UserUtil.getUserId();
    if (id == null) {
      logger.error("[API][api/user/changepasswd]: δ��¼");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][��Ӧ]: result=" + result);
      return "success";
    }
    
    //��ȡ����
    JSONObject paramsJson = JSONObject.fromObject(params);
    String oldpwhash = paramsJson.getString("oldpwhash");
    String newpwhash = paramsJson.getString("newpwhash");
    
    //��֤ԭ����
    if (!checkPassword(id, oldpwhash)) {
      logger.error("[API][api/user/changepasswd]: ԭ�������");
      result.put("state", "failed");
      logger.info("[API][api/user/changepasswd][��Ӧ]: result=" + result);
      return "success";
    }
    
    //�޸�������
    if (changePassword(id, newpwhash)) {
      result.put("state", "success");
    } else {
      logger.error("[API][api/user/changepasswd]: �޸�����ʧ��");
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/changepasswd][��Ӧ]: result=" + result);
    return "success";
  }
  
}
