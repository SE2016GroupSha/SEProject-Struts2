package xyz.sesha.project.store.index;

import java.util.Collection;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.utils.JedisUtil;


/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ��user:[id]:pdo:[name]->id
 * <br>˵����ͨ��user��id��pdo��name��ȡpdo��id��һ��һӳ��
 * 
 * @author Si Aoran
 */
public class UserIdPDONameToPDOId {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdPDONameToPDOId.class);

  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    //���pdo֮�󣬸�������
    PDO.afterAddHook.add(new HookFunction() {

      @Override
      public void func(Collection<String> jsonStrings) {
        Jedis jedis = JedisUtil.getJedis();
        try {
          for (String jsonString : jsonStrings) {
            JSONObject json = JSONObject.fromObject(jsonString);
            String key = "user:" + json.getString("user") + ":pdo:" + json.getString("name");
            String value = json.getString("id");
            jedis.set(key, value);
          }
        } catch (Exception e) {
          logger.error("[Error][UserIdPDONameToPDOId]: �ڲ�����");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    logger.info("[UserIdPDONameToPDOId] ��̬��ʼ�����");
  }
  
  /**
   * ����user��id��pdo��name������pdo��id
   * @param userId user��id
   * @param pdoName pdo��name
   * @return pdo��id�����������򷵻�null
   */
  public static String getId(String userId, String pdoName) {
    
    String id = null;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + userId + ":pdo:" + pdoName;
      String value = jedis.get(key);
      if (value != null) {
        id = value;
      }
    } catch (Exception e) {
      id = null;
      e.printStackTrace();
      logger.error("[Error][UserIdPDONameToPDOId]: �ڲ�����");
    } finally {
      jedis.close();
    }
    
    return id;
  }
  
}
