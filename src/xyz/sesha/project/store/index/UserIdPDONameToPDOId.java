package xyz.luxin.java.se.project.store.index;

import java.util.Collection;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.PDO;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ��user:[id]:pdo:[name]->id
 * <br>˵����ͨ��user��id��pdo��name��ȡpdo��id��һ��һӳ��
 * 
 * @author Administrator
 */
public class UserIdPDONameToPDOId {
  
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
          System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdPDONameToPDOId] ��̬��ʼ�����");
  }
  
  
  /**
   * ����user��id��pdo��name������pdo��id
   * @param userId user��id
   * @param pdoName pdo��name
   * @return data��id�����������򷵻�null
   */
  public static long getId(long userId, String pdoName) {
    
    long id = -1L;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + String.valueOf(userId) + ":pdo:" + pdoName;
      String value = jedis.get(key);
      if (value != null) {
        id = Long.valueOf(value);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } finally {
      jedis.close();
    }
    
    return id;
  }
}
