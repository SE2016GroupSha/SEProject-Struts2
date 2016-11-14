package xyz.luxin.java.se.project.store.index;

import java.util.Collection;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.User;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ��user:[name]->id
 * <br>˵����ͨ��user��name��ȡuser��id��һ��һӳ��
 * 
 * @author Administrator
 */
public class UserNameToUserId {
  
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
          System.out.println("[Error][UserNameToUserId]: ����ԭ������");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserNameToUserId] ��̬��ʼ�����");
  }
  
  
  /**
   * ����user��name������user��id
   * @param name user��name
   * @return user��id�����������򷵻�null
   */
  public static long getId(String name) {
    
    long id = -1L;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + name;
      String value = jedis.get(key);
      if (value != null) {
        id = Long.valueOf(value);
      }
    } catch (NumberFormatException e) {
      System.out.println("[Error][UserNameToUserId]: ����ԭ������");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return id;
  }

}
