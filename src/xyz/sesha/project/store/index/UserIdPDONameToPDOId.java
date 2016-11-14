package xyz.sesha.project.store.index;

import org.apache.log4j.Logger;


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
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdPDONameToPDOId.class);

  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    logger.info("[UserIdPDONameToPDOId] ��̬��ʼ�����");
  }
  
  /**
   * ����user��id��pdo��name������pdo��id
   * @param userId user��id
   * @param pdoName pdo��name
   * @return pdo��id�����������򷵻�null
   */
  public static String getId(String userId, String pdoName) {
    return null;
  }
  
}
