package xyz.sesha.project.store.index;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ1��user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>��ʽ2��user:[id]:search:data:fuzzy:[key]      ->(id1, id2...)
 * <br>˵����ͨ��user��id�͹ؼ���,��ȡȫ�����ڹؼ��ֵ�data��id��һ�Զ�ӳ��
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdKeysToDataIds.class);
  
  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
  }
  
  /**
   * ����user��id�͹ؼ��ʣ�ģ������������data��id��List
   * @param userId user��id
   * @param keys �ؼ�������
   * @return data��id��List
   */
  public static List<String> getIds(String userId, Collection<String> keys) {
    return null;
  }

}
