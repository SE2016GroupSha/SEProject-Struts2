package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;

/**
 * ��������������ࣺ��������������
 * 
 * <p>��ʽ1��user:[id]:search:data:fuzzy:all         ->(key1, key2...)
 * <br>��ʽ2��user:[id]:search:data:fuzzy:[key]:     ->(id1, id2...)
 * <br>˵����ͨ��user��id�͹ؼ���,��ȡȫ�����ڹؼ��ֵ�data��id��һ�Զ�ӳ��
 * 
 * @author Administrator
 */
public class UserIdKeysToDataIds {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdKeysToDataIds.class);

}
