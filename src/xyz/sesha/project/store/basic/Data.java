package xyz.sesha.project.store.basic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import xyz.sesha.project.store.index.HookFunction;


/**
 * ��������������ࣺ�������ݲ�����
 * 
 * <p>�������ͣ�data
 * <br>�����ֶΣ�idȫ�ⲻ�ظ�, time����Ϊdata����ʱ�� �� �û��ֶ���д��ʱ��(����)
 * <br>������ʽ��{"id":"4", "time":1477412545804, "pdo": "1", "values": ["��", "ѧУ", "10����"], "related_data": ["5", "6"]}
 * <br>�洢��ʽ��data:[id]->json�ַ���
 * <br>
 * <br>ע�⣺�����ṩ�Ľӿڣ�ֻ����ȫ��Ψһ��id
 * 
 * @author Administrator
 */
public class Data {
  
  /**
   * ��ȡLog4j���Logger
   */
  private static Logger logger = Logger.getLogger(Data.class);
  
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
   * ����data��json�ַ����ĺϷ���
   * <pr>
   * <br>
   * <br>���飺�����ڣ�ֵ���ͺϷ��ԣ�������(���ּ�)������ĳ�Ա���ͺϷ���
   * <br>���ԣ�ֵ���߼�����
   * @param dataJsons json�ַ����洢������
   * @return ���ؼ��������Ϸ��򷵻�true���Ƿ��򷵻�false
   */
  public static boolean checkDataJsonFormat(Collection<String> dataJsons) {
    return true;
  }
  
  /**
   * ���data����ʵִ�з�����ֱ�Ӷ�дRedis
   * @param dataJsons data��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  private static boolean innerAddData(Collection<String> dataJsons) {
    return true;
  }

  /**
   * ���data�ķ��������ⲿ����
   * @param dataJsons data��json�ַ�������
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addData(Collection<String> dataJsons) {
    return true;
  }
  
  /**
   * ���data�ķ��������ⲿ����
   * @param dataJson data��json�ַ���
   * @return ����ִ�н����true��ɹ���false��ʧ��
   */
  public static boolean addData(String dataJson) {
    return true;
  }
  
  /**
   * ����id���ж�data�Ƿ����
   * @param id data��id
   * @return �����жϽ����true����ڣ�false�򲻴���
   */
  public static boolean hasData(String id) {
    return true;
  }
  
  /**
   * ����id������data��json�ַ���
   * @param id data��id
   * @return ����data��json�ַ�������id�����ڣ��򷵻�null
   */
  public static String getDataJson(String id) {
    return null;
  }
  
  /**
   * ����id������data��json�ַ���
   * @param ids data��id������
   * @return ����data��json�ַ�����List
   */
  public static List<String> getDataJson(Collection<String> ids) {
    return null;
  }
}
