package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.UserUtil;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL��api/user/regdays
 * <br>������params={}
 * <br>���أ�{"state": "success", "regdays": 10, "regtime":1477412545804} �� {"state": "failed"}
 * <br>˵������ȡ��ǰ��¼�û�ע��������ע��ʱ�䣬�ѵ�¼����success��ע��������δ��¼����failed
 * 
 * @author Administrator
 */
public class RegDaysAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(RegDaysAction.class);
  
  /**
   * ��ȡ��ǰ��½�û�ע������
   * @return �ѵ�¼�����û�ע��������δ��¼����null
   */
  private Integer getRegDays() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    long now = System.currentTimeMillis();
    long reg = JSONObject.fromObject(User.getUserJson(id)).getLong("time");
    double days = ((double)(now-reg))/(1000.0 * 60.0 * 60.0 * 24.0);
    if (days < 0) {
      return null;
    } else {
      return Integer.valueOf(((int)days)+1);
    }
  }
  
  /**
   * ��ȡ��ǰ��½�û�ע��ʱ��
   * @return �ѵ�¼�����û�ע��ʱ�䣬δ��¼����null
   */
  private Long getRegTime() {
    String id = UserUtil.getUserId();
    if (id==null) {
      return null;
    } 
    return JSONObject.fromObject(User.getUserJson(id)).getLong("time");
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    return true;
  }
  
  @Override
  public String execute() {

    result = new JSONObject();
    
    logger.info("[API][api/user/regdays][����]: params=" + params);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/regdays]: �Ƿ�����(" + params + ")");
      result.put("state", "failed");
      logger.info("[API][api/user/regdays][��Ӧ]: result=" + result);
      return "success";
    }
    
    //�����½ 
    Integer days = getRegDays();
    Long time = getRegTime();
    if (days!=null && time!=null) {
      result.put("state", "success");
      result.put("regdays", days);
      result.put("regtime", time);
    } else {
      result.put("state", "failed");
    }

    logger.info("[API][api/user/regdays][��Ӧ]: result=" + result);
    return "success";
  }
}
