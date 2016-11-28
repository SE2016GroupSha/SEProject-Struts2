package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/logout
 * <br>������params={}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵����user�˳����ɹ�����success��ʧ�ܷ���failed
 * 
 * @author Si Aoran
 */
public class LogoutAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(LoginAction.class);

  /**
   * user�˳���ֱ��Ӱ��һ��session��user��Ϣ
   * @return �˳��ɹ�����true���˳�ʧ�ܷ���false
   */
  private boolean logout() {
    return UserUtil.delUserId();
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/logout][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][ api/user/logout]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/logout][��Ӧ]: result=" + result);
      return "success";
    }
    
    //userע������ȡ���
    if (logout()) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }
    
    logger.info("[API][api/user/logout][��Ӧ]: result=" + result);
    return "success";

  }
}
