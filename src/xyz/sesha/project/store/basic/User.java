package xyz.sesha.project.store.basic;

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

}
