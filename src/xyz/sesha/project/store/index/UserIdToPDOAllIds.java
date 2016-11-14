package xyz.sesha.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.utils.JedisUtil;


/**
 * ��������������ࣺ��������������
 *
 * <p>��ʽ��user:[id]:pdo:all->(id1, id2...)
 * <br>˵����ͨ��user��id��ȡ���û�����pdo��id��һ�Զ�ӳ��
 * 
 * @author Si Aoran
 */
public class UserIdToPDOAllIds {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdToPDOAllIds.class);

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
            String key = "user:" + json.getString("user") + ":pdo:all";
            String value = json.getString("id");
            jedis.sadd(key, value);
          }
        } catch (Exception e) {
          logger.error("[Error][UserIdToPDOAllIds]: �ڲ�����");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    logger.info("[UserIdToPDOAllIds] ��̬��ʼ�����");
  }
  
  /**
   * ����user��id�����ظ�user����pdo��id��List
   * @param id user��id
   * @return ����pdo��id��List
   */
  public static List<String> getAllIds(String id) {
    
    List<String> ids = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + id + ":pdo:all";
      Set<String> idStrings = jedis.smembers(key);
      if (idStrings != null) {
        for (String idString : idStrings) {
          ids.add(idString);
        }
      }
    } catch (Exception e) {
      ids.clear();
      e.printStackTrace();
      logger.error("[Error][UserIdToPDOAllIds]: �ڲ�����");
    } finally {
      jedis.close();
    }
    
    return ids;
  }
  
}
