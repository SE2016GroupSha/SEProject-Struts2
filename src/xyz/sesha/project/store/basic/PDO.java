package xyz.sesha.project.store.basic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;

/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�pdo
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, user���������û���id, nameͬ�û����ظ�, time����ΪPDO����ʱ��(����)
 * <br>������ʽ��{"id":"1", "time":1477410877415, "user":"0", "name":"����", "fields":["ʼ��", "�յ�", "��ʱ"]}
 * <br>�洢��ʽ��pdo:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Administrator
 */
public class PDO {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(PDO.class);
  
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
   * ����pdo��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param pdoJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkPDOJsonFormat(Collection<String> pdoJsons) {
    return true;
  }
  
  /**
   * ���pdo����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param pdoJsons pdo��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddPDO(Collection<String> pdoJsons) {
    return true;
  }

  /**
   * ���pdo�ķ��������ⲿ����
   * @param pdoJsons pdo��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addPDO(Collection<String> pdoJsons) {
    return true;
  }
  
  /**
   * ���pdo�ķ��������ⲿ����
   * @param pdoJson pdo��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addPDO(String pdoJson) {
    return true;
  }
  
  /**
   * ����id���ж�pdo�Ƿ����
   * @param id pdo��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasPDO(String id) {
    return true;
  }
  
  /**
   * ����id������pdo��json�ַ���
   * @param id pdo��id
   * @return ����pdo��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getPDOJson(String id) {
    return null;
  }
  
  /**
   * ����id������pdo��json�ַ���
   * @param ids pdo��id������
   * @return ����pdo��json�ַ�����List
   */
  public static List<String> getPDOJson(Collection<String> ids) {
    return null;
  }
}
