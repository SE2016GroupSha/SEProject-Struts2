package xyz.sesha.project.store.basic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;

/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�user
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, nameȫ�ⲻ�ظ�, pwhashΪ����32λmd5ֵ(Сд), time����Ϊuser����ʱ��(����)
 * <br>������ʽ��{"id":"0", "time":1477410793369, "name":"��ү", "pwhash":"5e007e7046425c92111676b1b0999f12"}
 * <br>�洢��ʽ��user:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Administrator
 */
public class User {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(User.class);
  
  /**
   * ��Ӳ���֮��,ִ�е�ȫ����������
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();
  
  /**
   * <pr>���ϻ������ݵ�idȫ��Ψһ�ļ�������洢��������
   * <br>ע�⣺���ֻ���ڵ��û������ӻ��������û����������ͬ������
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * ����user��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param userJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkUserJsonFormat(Collection<String> userJsons) {
    return true;
  }
  
  /**
   * ���user����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param userJsons user��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddUser(Collection<String> userJsons) {
    return true;
  }

  /**
   * ���user�ķ��������ⲿ����
   * @param userJsons user��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addUser(Collection<String> userJsons) {
    return true;
  }
  
  /**
   * ���user�ķ��������ⲿ����
   * @param userJson user��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addUser(String userJson) {
    return true;
  }
  
  /**
   * ����id���ж�user�Ƿ����
   * @param id user��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasUser(String id) {
    return true;
  }
  
  /**
   * ����id������user��json�ַ���
   * @param id user��id
   * @return ����user��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getUserJson(String id) {
    return null;
  }
  
  /**
   * ����id������user��json�ַ���
   * @param ids user��id������
   * @return ����user��json�ַ�����List
   */
  public static List<String> getUserJson(Collection<String> ids) {
    return null;
  }
}
