package xyz.sesha.project.store.basic;

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
  
}
