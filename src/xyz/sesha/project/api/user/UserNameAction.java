package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL��api/user/username
 * <br>������params={}
 * <br>���أ�{"state": "success", "username": "��ү"} �� {"state": "failed"}
 * <br>˵������ȡ��ǰ��¼�û������ѵ�¼����success���û�����δ��¼����failed
 * 
 * @author Administrator
 */
public class UserNameAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserNameAction.class);
  
  /**
   * ��ȡ��ǰ��½�û���
   * @return �ѵ�¼�����û�����δ��¼����null
   */
  private String getName() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    return JSONObject.fromObject(User.getUserJson(id)).getString("name");
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/username][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/username]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/username][��Ӧ]: result=" + result);
      return "success";
    }
    
    //�����½ 
    String userName = getName();
    if (userName!=null) {
      result.put("state", "success");
      result.put("username", userName);
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/username][��Ӧ]: result=" + result);
    return "success";
  }
}
