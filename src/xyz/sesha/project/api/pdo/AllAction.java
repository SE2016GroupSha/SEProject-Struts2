package xyz.sesha.project.api.pdo;

import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.store.index.UserIdToPDOAllIds;
import xyz.sesha.project.api.AbstractApiAction;

/**
 * ǰ��API������Ӧ�� <br>
 * <br>
 * URL�� api/pdo/all <br>
 * ������params={} <br>
 * ���أ�{"pdos": [pdo1, pdo2...]} <br>
 * ˵�����������е�PDO��Ĭ������ʱ��Ӵ�С������ֵΪpdo����
 * 
 * @author Administrator
 */
public class AllAction extends AbstractApiAction {

	/**
	 * ��ȡLog4j���Logger
	 */
	private static Logger logger = Logger.getLogger(AllAction.class);

	/**
	 * �������е�pdo��json�ַ���(����)
	 * 
	 * @param userId
	 *            user��id
	 * @return ����ָ��user������pdo��json�ַ���
	 */
	private List<String> all(long userId) {
		List<Long> ids = UserIdToPDOAllIds.getAllIds(userId);
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
	      System.out.println("[API][api/pdo/all]: �Ƿ�����(" + params + ")");
	      JSONArray pdoJsonArray = new JSONArray();
	      result.put("pdos", pdoJsonArray);
	      return "success";
	    }
	    
	    //��ȡȫ��pdo
	    JSONArray pdoJsonArray = new JSONArray();
	    List<String> pdoJsonStrings = all(0);
	    
	    //��pdo��ʱ��Ӵ�С����
	    TreeMap<Long, JSONObject> sortTree = new TreeMap<Long, JSONObject>((n1, n2)->n2.compareTo(n1));
	    for (String pdoJsonString : pdoJsonStrings) {
	      JSONObject jsonObj = JSONObject.fromObject(pdoJsonString);
	      sortTree.put(jsonObj.getLong("time"), jsonObj);
	    }
	    
	    //���ɷ��ؽ��
	    for (JSONObject jsonObj : sortTree.values()) {
	      pdoJsonArray.add(jsonObj);
	    }
	    
	    result.put("pdos", pdoJsonArray);
	    
	    return "success";
	}
}
