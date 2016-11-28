package xyz.sesha.project.api.user;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.api.AbstractApiAction;
import xyz.sesha.project.store.index.UserNameToUserId;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/user/checkname
 * <br>������params={"name": "��ү"} (���ݲ�����username=��ү)
 * <br>���أ�{"valid": "true"} �� {"valid": "false"}
 * <br>˵�������user�����Ƿ����(����Ƿ����),���÷���true�������÷���false
 * 
 * @author Si Aoran
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);
  
  /**
   * Ϊǰ������ļ��ݲ���
   */
  protected String username;

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * ���user�����Ƿ����(����Ƿ����)
   * @param name ������user����
   * @return ���÷���true�������÷���false
   */
  private boolean check(String name) {
    return null==UserNameToUserId.getId(name);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
    boolean ret = true;
    
    //���ݲ���������ȼ�
    if (username != null) {
      return ret;
    }
    
    //��������������ȼ���֮
    try {
      JSONObject json = JSONObject.fromObject(params);
      
      //�ж�key�Ƿ����
      if (!json.has("name")) {
        ret =  false;
      }
      
      //nullֵ�жϣ�������instanceof�ؼ�����
      
      //name���ͣ�java.lang.String
      Object nameObj = json.get("name");
      if (!(nameObj instanceof String)) {
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
    
    logger.info("[API][api/user/checkname][����]: user=��ү, params=" + params + ", username=" + username);
    
    //��������Ϸ���
    if (!checkParamsJsonFormat()) {
      logger.error("[API][api/user/checkname]: �Ƿ�����(" + params + ")");
      result.put("valid", "false");
      logger.info("[API][api/user/checkname][��Ӧ]: result=" + result);
      return "success";
    }
    
    //���ݲ���������ȼ�
    if (username != null) {
      //�ж�name�Ƿ���ã������ɷ��ؽ��
      if (check(username)) {
        result.put("valid", "true");
      } else {
        result.put("valid", "false");
      }
    }
    //��������������ȼ���֮
    else {
      JSONObject paramsJson = JSONObject.fromObject(params);
      String name = paramsJson.getString("name");
      
      //�ж�name�Ƿ���ã������ɷ��ؽ��
      if (check(name)) {
        result.put("valid", "true");
      } else {
        result.put("valid", "false");
      }
    }

    logger.info("[API][api/user/checkname][��Ӧ]: result=" + result);
    return "success";
  }
}
