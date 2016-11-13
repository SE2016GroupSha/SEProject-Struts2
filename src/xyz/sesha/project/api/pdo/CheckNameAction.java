package xyz.sesha.project.api.pdo;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ��
 * <br>
 * <br>URL�� api/pdo/checkname
 * <br>������params={"name": "����"}
 * <br>���أ�{"valid": "true"} �� {"valid": "false"}
 * <br>˵�������PDO�����Ƿ����(����Ƿ����),���÷���true�������÷���false
 * 
 * @author Administrator
 */
public class CheckNameAction extends AbstractApiAction {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(CheckNameAction.class);

  /**
   * ���pdo�����Ƿ����(����Ƿ����)
   * @param userId user��id
   * @param pdoName ������pdo����
   * @return ���÷���true�������÷���false
   */
  private boolean check(long userId, String pdoName) {
	  return -1L==UserIdPDONameToPDOId.getId(userId, pdoName);
  }
  
  @Override
  public boolean checkParamsJsonFormat() {
	  boolean ret = true;
	    
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
	    
	    //��������Ϸ���
	    if (!checkParamsJsonFormat()) {
	      System.out.println("[API][api/pdo/checkname]: �Ƿ�����(" + params + ")");
	      result.put("valid", "false");
	      return "success";
	    }
	    
	    JSONObject paramsJson = JSONObject.fromObject(params);
	    String name = paramsJson.getString("name");
	    
	    //�ж�name�Ƿ���ã������ɷ��ؽ��
	    if (check(0, name)) {
	      result.put("valid", "true");
	    } else {
	      result.put("valid", "false");
	    }
	    
	    return "success";
  }
}
