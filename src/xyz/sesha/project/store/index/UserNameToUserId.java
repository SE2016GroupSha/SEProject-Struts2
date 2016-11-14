package xyz.sesha.project.store.index;

import java.util.Collection;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.utils.JedisUtil;


/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ��user:[name]->id
 * <br>˵����ͨ��user��name��ȡuser��id��һ��һӳ��
 * 
 * @author Si Aoran
 */
public class UserNameToUserId {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserNameToUserId.class);

  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    //���user֮�󣬸�������
    User.afterAddHook.add(new HookFunction() {

      @Override
      public void func(Collection<String> jsonStrings) {
        Jedis jedis = JedisUtil.getJedis();
        try {
          for (String jsonString : jsonStrings) {
            JSONObject json = JSONObject.fromObject(jsonString);
            String key = "user:" + json.getString("name");
            String value = json.getString("id");
            jedis.set(key, value);
          }
        } catch (Exception e) {
          logger.error("[Error][UserNameToUserId]: �ڲ�����");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    logger.info("[UserNameToUserId] ��̬��ʼ�����");
  }
  
  /**
   * ����user��name������user��id
   * @param name user��name
   * @return user��id�����������򷵻�null
   */
  public static String getId(String name) {
    
    String id = null;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + name;
      String value = jedis.get(key);
      if (value != null) {
        id = value;
      }
    } catch (Exception e) {
      id = null;
      e.printStackTrace();
      logger.error("[Error][UserNameToUserId]: �ڲ�����");
    } finally {
      jedis.close();
    }
    
    return id;
  }

}
