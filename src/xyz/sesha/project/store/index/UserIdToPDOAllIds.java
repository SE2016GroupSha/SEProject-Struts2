package xyz.sesha.project.store.index;

import java.util.List;

import org.apache.log4j.Logger;


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
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(UserIdToPDOAllIds.class);

  /**
   * ��̬��ʼ�����������������ع���
   * ע�⣺�ⲿ�ִ���ֻ���Դӻ����������ȡ����(get),�޸����ݿ��ܻ����޵ݹ�(add,edit,remove)
   */
  static {
    
    logger.info("[UserIdToPDOAllIds] ��̬��ʼ�����");
  }
  
  /**
   * ����user��id�����ظ�user����pdo��id��List
   * @param userId user��id
   * @return ����pdo��id��List
   */
  public static List<Long> getAllIds(long userId) {
    return null;
  }
  
}
