package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;


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
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserNameToUserId.class);

  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    logger.info("[UserNameToUserId] ��̬��ʼ�����");
  }
  
  /**
   * ����user��name������user��id
   * @param name user��name
   * @return user��id�����������򷵻�null
   */
  public static String getId(String name) {
    return null;
  }

}
