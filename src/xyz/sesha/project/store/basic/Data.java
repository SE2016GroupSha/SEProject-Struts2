package xyz.sesha.project.store.basic;

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

}
