package xyz.luxin.java.se.project.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import xyz.luxin.java.se.project.store.basic.PDO;
import xyz.luxin.java.se.project.utils.JedisUtil;

/**
 * ��������������ࣺ��������������
 *
 * <p>��ʽ��user:[id]:pdo:all->(id1, id2...)
 * <br>˵����ͨ��user��id��ȡ���û�����pdo��id��һ�Զ�ӳ��
 * 
 * @author Administrator
 */
public class UserIdToPDOAllIds {
  
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
          System.out.println("[Error][UserIdToPDOAllIds]: ����ԭ������");
          e.printStackTrace();
        } finally {
          jedis.close();
        }
      }
      
    });
    
    System.out.println("[UserIdToPDOAllIds] ��̬��ʼ�����");
  }
  
  
  /**
   * ����user��id�����ظ�user����pdo��id��List
   * @param id user��id
   * @return ����pdo��id��List
   */
  public static List<Long> getAllIds(long id) {
    
    List<Long> ids = new ArrayList<Long>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + String.valueOf(id) + ":pdo:all";
      Set<String> idStrings = jedis.smembers(key);
      if (idStrings != null) {
        for (String idString : idStrings) {
          ids.add(Long.valueOf(idString));
        }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      ids.clear();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } catch (Exception e) {
      e.printStackTrace();
      ids.clear();
      System.out.println("[Error][UserIdPDONameToPDOId]: ����ԭ������");
    } finally {
      jedis.close();
    }
    
    return ids;
  }
}
