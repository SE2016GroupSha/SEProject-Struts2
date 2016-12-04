package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL��api/user/checklogin
 * <br>������params={}
 * <br>���أ�{"state": "success"} �� {"state": "failed"}
 * <br>˵������֤�Ƿ��Ѿ���¼���ѵ�¼����success��δ��¼����failed
 * 
 * @author Administrator
 */
public class CheckLoginAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckLoginAction.class);
  
  /**
   * ��鵱ǰ�����Ƿ��¼
   * @return �ѵ�¼����true��δ��¼����false
   */
  private boolean check() {
    return null!=UserUtil.getUserId();
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/checklogin][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/checklogin]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/checklogin][��Ӧ]: result=" + result);
      return "success";
    }
    
    //�����½ 
    if (check()) {
      result.put("state", "success");
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/checklogin][��Ӧ]: result=" + result);
    return "success";
  }
}
