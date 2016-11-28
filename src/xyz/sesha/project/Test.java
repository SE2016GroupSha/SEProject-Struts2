package xyz.sesha.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.Jedis;

import xyz.sesha.project.listener.InitListener;
import xyz.sesha.project.utils.JedisUtil;
import xyz.sesha.project.api.data.RelatedDataAction;
import xyz.sesha.project.api.pdo.AllAction;
import xyz.sesha.project.api.pdo.CheckNameAction;
import xyz.sesha.project.api.search.FuzzyAction;
import xyz.sesha.project.store.basic.Data;
import xyz.sesha.project.store.basic.PDO;
import xyz.sesha.project.store.basic.User;
import xyz.sesha.project.store.index.UserIdKeysToDataIds;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;
import xyz.sesha.project.store.index.UserIdToPDOAllIds;
import xyz.sesha.project.store.index.UserNameToUserId;


/**
 * ǰ�˻���������(���û�!!�ѹ�ʱ����)
 * 
 * <p>��API�����Լ�Store�߼����в���
 *
 * @author Lu Xin
 */
public class Test {
  
  public static void dbInit() {
    
    System.out.println("-------------------------dbInit start-------------------------");
    
    try {
      Class.forName("xyz.sesha.project.utils.JedisUtil");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    try {
      Class.forName("xyz.sesha.project.store.index.UserIdPDONameToPDOId");
      Class.forName("xyz.sesha.project.store.index.UserIdToPDOAllIds");
      Class.forName("xyz.sesha.project.store.index.UserNameToUserId");
      Class.forName("xyz.sesha.project.store.index.UserIdKeysToDataIds");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    
    Jedis jedis = JedisUtil.getJedis();
    try {
      jedis.flushDB();
      jedis.set("dbindex", "0");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    System.out.println("-------------------------dbInit end-------------------------");
  }
  
  @SuppressWarnings("serial")
  public static void userTest() {
    
    boolean ret = false;
    
    System.out.println("-------------------------userTest start-------------------------");
    
    //id��Long(��������value����)
    String userBad1 = "{\"id\":0, \"time\":1477410793369, \"name\":\"��ү\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad1);
    System.out.println("��������1��" + ret);
    
    //ȱ��time(��������key����)
    String userBad2 = "{\"id\":\"0\", \"name\":\"��ү\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad2);
    System.out.println("��������2��" + ret);
    
    //id��null(��������value��null)
    String userBad3 = "{\"id\":null, \"time\":1477410793369, \"name\":\"��ү\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad3);
    System.out.println("��������3��" + ret);
    
    //name�ǿ��ַ���
    String userBad4 = "{\"id\":\"0\", \"time\":1477410793369, \"name\":\"\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad4);
    System.out.println("��������4��" + ret);
    
    //��ȷ��user
    String user = "{\"id\":\"-1\", \"time\":1477410793369, \"name\":\"��ү\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    List<String> users = new ArrayList<String>();
    users.add(user);
    
    //��һ����ӳɹ�
    ret = User.addUser(users);
    System.out.println("��ȷ���룺" + ret);
    
    //�ڶ������ʧ��(name�ظ�)
    ret = User.addUser(user);
    System.out.println("�ظ����֣�" + ret);
    
    //hasUser����
    System.out.println("hasUser���ԣ�");
    System.out.println(User.hasUser("0"));
    System.out.println(User.hasUser("1"));
    System.out.println(User.hasUser("-1"));
    
    //getUserJson����
    System.out.println("getUserJson���ԣ�");
    System.out.println(User.getUserJson("0"));
    System.out.println(User.getUserJson("1"));
    System.out.println(User.getUserJson("-1"));
    System.out.println(User.getUserJson(new ArrayList<String>(){{this.add("0");this.add("1");this.add("-1");}}));
    
    //UserNameToUserId��������
    System.out.println("UserNameToUserId�������ԣ�");
    System.out.println(UserNameToUserId.getId("½��"));
    System.out.println(UserNameToUserId.getId("��ү"));
    
    System.out.println("-------------------------userTest end-------------------------");
  }
  
  
  @SuppressWarnings("serial")
  public static void pdoTest() {
    
    boolean ret = false;
    
    System.out.println("-------------------------pdoTest start-------------------------");
    
    //id��Long(��������value����)
    String pdoBad1 = "{\"id\":1, \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad1);
    System.out.println("��������1��" + ret);
        
    //ȱ��time(��������key����)
    String pdoBad2 = "{\"id\":\"1\", \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad2);
    System.out.println("��������2��" + ret);

    //id��null(��������value��null)
    String pdoBad3 = "{\"id\":null, \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad3);
    System.out.println("��������3��" + ret);

    //name�ǿ��ַ���
    String pdoBad4 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad4);
    System.out.println("��������4��" + ret);

    //fields��Ա�����ַ���
    String pdoBad5 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", 345, \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad5);
    System.out.println("��������5��" + ret);

    //fields��������
    String pdoBad6 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":456}";
    ret = PDO.addPDO(pdoBad6);
    System.out.println("��������6��" + ret);

    //fields����Ϊ��
    String pdoBad7 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[]}";
    ret = PDO.addPDO(pdoBad7);
    System.out.println("��������7��" + ret);

    //user������
    String pdoBad8 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"6\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdoBad8);
    System.out.println("��������8��" + ret);
    
    
    //��ȷ����1
    String pdo1 = "{\"id\":\"-1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}";
    ret = PDO.addPDO(pdo1);
    System.out.println("��ȷ����1��" + ret);
    
    //��ȷ����2,3
    String pdo2 = "{\"id\":\"-1\", \"time\":1477412043598, \"user\":\"0\", \"name\":\"��ѧ\", \"fields\":[\"���\", \"����\"]}";
    String pdo3 = "{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"֧��\", \"fields\":[\"���\"]}";
    List<String> pdos = new ArrayList<String>();
    pdos.add(pdo2);
    pdos.add(pdo3);
    ret = PDO.addPDO(pdos);
    System.out.println("��ȷ����2,3��" + ret);
    
    //�ظ�����ʧ��
    ret = PDO.addPDO(pdos);
    System.out.println("�ظ����룺" + ret);
    
    
    //hasPDO����
    System.out.println("hasPDO���ԣ�");
    System.out.println(PDO.hasPDO("1"));
    System.out.println(PDO.hasPDO("2"));
    System.out.println(PDO.hasPDO("3"));
    System.out.println(PDO.hasPDO("0"));
    System.out.println(PDO.hasPDO("-1"));
    System.out.println(PDO.hasPDO("7"));
    
    //getPDOJson����
    System.out.println("getPDOJson���ԣ�");
    System.out.println(PDO.getPDOJson("1"));
    System.out.println(PDO.getPDOJson("2"));
    System.out.println(PDO.getPDOJson("3"));
    System.out.println(PDO.getPDOJson("0"));
    System.out.println(PDO.getPDOJson("-1"));
    System.out.println(PDO.getPDOJson("7"));
    System.out.println(PDO.getPDOJson(new ArrayList<String>(){{this.add("1");this.add("2");this.add("3");this.add("0");this.add("-1");this.add("7");}}));
    
    //UserIdToPDOAllIds��������
    System.out.println("UserIdToPDOAllIds�������ԣ�");
    System.out.println(UserIdToPDOAllIds.getAllIds("0"));
    System.out.println(UserIdToPDOAllIds.getAllIds("1"));

    //UserIdPDONameToPDOId��������
    System.out.println("UserIdPDONameToPDOId�������ԣ�");
    System.out.println(UserIdPDONameToPDOId.getId("0", "����"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "��ѧ"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "֧��"));
    System.out.println(UserIdPDONameToPDOId.getId("1", "����"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "δ֪"));
    
    System.out.println("-------------------------pdoTest end-------------------------");
  }
  
  @SuppressWarnings("serial")
  public static void dataTest() {
    
    boolean ret = false;
    
    System.out.println("-------------------------dataTest start-------------------------");
    
    //��ȷ����1
    String data1 = "{\"id\":\"-1\", \"time\":1477412545804, \"pdo\": \"1\", \"values\": [\"��\", \"ѧУ\", \"10����\"], \"related_data\": []}";
    ret = Data.addData(data1);
    System.out.println("��ȷ����1��" + ret);

    //��ȷ����2
    String data2 = "{\"id\":\"-1\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"���˺�ѧУͬѧ\", \"2016-10-23\"], \"related_data\": [\"4\"]}";
    ret = Data.addData(data2);
    System.out.println("��ȷ����2��" + ret);
    
    //��ȷ����3
    String data3 = "{\"id\":\"-1\", \"time\":1477412576389, \"pdo\": \"3\", \"values\": [\"32.10\"], \"related_data\": [\"4\", \"5\"]}";
    ret = Data.addData(data3);
    System.out.println("��ȷ����3��" + ret);

    //��ȷ����4,5,6
    String data4 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    String data5 = "{\"id\":\"-1\", \"time\":1477412577654, \"pdo\": \"3\", \"values\": [\"10.23\"], \"related_data\": []}";
    String data6 = "{\"id\":\"-1\", \"time\":1477412666666, \"pdo\": \"1\", \"values\": [\"ѧУ\", \"����\", \"1����\"], \"related_data\": []}";
    List<String> datas = new ArrayList<String>();
    datas.add(data4);
    datas.add(data5);
    datas.add(data6);
    ret = Data.addData(datas);
    System.out.println("��ȷ����4,5,6��" + ret);

    
    //�������ظ������������
    
    
    //id��Long(��������value����)
    String dataBad1 = "{\"id\":-1, \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad1);
    System.out.println("��������1��" + ret);

    //ȱ��time(��������key����)
    String dataBad2 = "{\"id\":\"-1\", \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad2);
    System.out.println("��������2��" + ret);

    //id��null(��������value��null)
    String dataBad3 = "{\"id\":null, \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad3);
    System.out.println("��������3��" + ret);

    //pdo������
    String dataBad4 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"99\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad4);
    System.out.println("��������4��" + ret);

    //pdo��fields��valuesԪ�ظ�����һ��
    String dataBad5 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\", \"ha\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad5);
    System.out.println("��������5��" + ret);

    //values��Ա�����ַ���
    String dataBad6 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [66], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad6);
    System.out.println("��������6��" + ret);

    //values��������
    String dataBad7 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": \"23.10\", \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad7);
    System.out.println("��������7��" + ret);

    //values����Ϊ��
    String dataBad8 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad8);
    System.out.println("��������8��" + ret);

    //related_data��Ա�����ַ���
    String dataBad9 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [66, \"aa\"], \"related_data\": [\"4\", 5, \"6\"]}";
    ret = Data.addData(dataBad9);
    System.out.println("��������9��" + ret);

    //related_data��������
    String dataBad10 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": \"23.10\", \"related_data\": \"6\"}";
    ret = Data.addData(dataBad10);
    System.out.println("��������10��" + ret);

    //related_data�����id������
    String dataBad11 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"17\", \"6\"]}";
    ret = Data.addData(dataBad11);
    System.out.println("��������11��" + ret);
    
    
    //hasData����
    System.out.println("hasData���ԣ�");
    System.out.println(Data.hasData("4"));
    System.out.println(Data.hasData("5"));
    System.out.println(Data.hasData("6"));
    System.out.println(Data.hasData("7"));
    System.out.println(Data.hasData("8"));
    System.out.println(Data.hasData("9"));
    System.out.println(Data.hasData("-1"));
    System.out.println(Data.hasData("0"));
    System.out.println(Data.hasData("1"));
    System.out.println(Data.hasData("3"));
    
    //getDataJson����
    System.out.println("getDataJson���ԣ�");
    System.out.println(Data.getDataJson("4"));
    System.out.println(Data.getDataJson("5"));
    System.out.println(Data.getDataJson("6"));
    System.out.println(Data.getDataJson("7"));
    System.out.println(Data.getDataJson("8"));
    System.out.println(Data.getDataJson("9"));
    System.out.println(Data.getDataJson("-1"));
    System.out.println(Data.getDataJson("0"));
    System.out.println(Data.getDataJson("1"));
    System.out.println(Data.getDataJson("3"));
    System.out.println(Data.getDataJson(new ArrayList<String>(){{this.add("4");this.add("5");this.add("6");this.add("7");this.add("8");this.add("9");this.add("-1");this.add("0");this.add("1");this.add("3");}}));
    
    
    //UserIdKeysToDataIds��������
    System.out.println("UserIdKeysToDataIds�������ԣ�");
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("��")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("��", "ѧУ")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("10", "23")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("ѧУ", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("��", "ѧУ", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList(".10")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("����", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("2016-")));
    
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("����", "��")));
    System.out.println(UserIdKeysToDataIds.getIds("1", Arrays.asList("����")));
    
    
    System.out.println("-------------------------dataTest end-------------------------");
  }
  
  public static void apiTest() {
   
    String params;
  
    System.out.println("-------------------------pdoAddAPI begin-------------------------");
    xyz.sesha.project.api.pdo.AddAction pdoAddAPI = new xyz.sesha.project.api.pdo.AddAction(); 
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"����\", \"fields\":[\"ʼ��\", \"�յ�\", \"��ʱ\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477412043598, \"user\":\"0\", \"name\":\"��ѧ\", \"fields\":[\"���\", \"����\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"֧��\", \"fields\":[\"���\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"haha\":[{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"֧��\", \"fields\":[\"���\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"pdos\":89}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = null;
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    System.out.println("-------------------------pdoAddAPI end-------------------------");
    
    
    System.out.println("-------------------------pdoAllAPI begin-------------------------");
    AllAction pdoAllAPI = new AllAction(); 
    params = "{}";
    pdoAllAPI.setParams(params);
    pdoAllAPI.execute();
    System.out.println(pdoAllAPI.getResult());
    params = "{d}";
    pdoAllAPI.setParams(params);
    pdoAllAPI.execute();
    System.out.println(pdoAllAPI.getResult());
    params = null;
    pdoAllAPI.setParams(params);
    pdoAllAPI.execute();
    System.out.println(pdoAllAPI.getResult());
    System.out.println("-------------------------pdoAllAPI end-------------------------");
    
    
    System.out.println("-------------------------pdoCheckNameAPI begin-------------------------");
    CheckNameAction pdoCheckNameAPI = new CheckNameAction(); 
    params = "{\"name\": \"����\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"֧��\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"��ѧ\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"�Է�\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"ne\": \"�Է�\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": 67}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = null;
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    System.out.println("-------------------------pdoCheckNameAPI end-------------------------");
    
    
    System.out.println("-------------------------dataAddAPI begin-------------------------");
    xyz.sesha.project.api.data.AddAction dataAddAPI = new xyz.sesha.project.api.data.AddAction(); 
    params = "{\"datas\": [{\"id\":\"4\", \"time\":1477412545804, \"pdo\": \"1\", \"values\": [\"��\", \"ѧУ\", \"10����\"], \"related_data\": []}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"5\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"���˺�ѧУͬѧ\", \"2016-10-23\"], \"related_data\": [\"4\"]}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"6\", \"time\":1477412576389, \"pdo\": \"3\", \"values\": [\"32.10\"], \"related_data\": [\"4\", \"5\"]}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"7\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}, {\"id\":\"8\", \"time\":1477412577654, \"pdo\": \"3\", \"values\": [\"10.23\"], \"related_data\": []}, {\"id\":\"9\", \"time\":1477412666666, \"pdo\": \"1\", \"values\": [\"ѧУ\", \"����\", \"1����\"], \"related_data\": []}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": []}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": 99}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"das\": [{\"id\":\"5\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"���˺�ѧУͬѧ\", \"2016-10-23\"], \"related_data\": [\"4\"]}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = null;
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    System.out.println("-------------------------dataAddAPI end-------------------------");
    
    
    System.out.println("-------------------------dataRelatedDataAPI begin-------------------------");
    RelatedDataAction dataRelatedDataAPI = new RelatedDataAction(); 
    params = "{\"ids\": [\"4\"]}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = "{\"ids\": [\"4\", \"5\", \"6\"]}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = "{\"ids\": [\"4\", \"5\", \"6\", \"7\", \"8\"]}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = "{\"ids\": []}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = "{\"ids\": \"99\"}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = "{\"s\": [\"4\", \"5\", \"6\", \"7\", \"8\"]}";
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    params = null;
    dataRelatedDataAPI.setParams(params);
    dataRelatedDataAPI.execute();
    System.out.println(dataRelatedDataAPI.getResult());
    System.out.println("-------------------------dataRelatedDataAPI end-------------------------");
    
    
    System.out.println("-------------------------searchFuzzyAPI begin-------------------------");
    FuzzyAction searchFuzzyAPI = new FuzzyAction(); 
    params = "{\"keys\": [\"\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": [\"��\", \"ѧУ\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": [\"��\", \"ѧУ\", \"1\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": [\"2016-\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": []}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": 766}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"k\": [\"2016-\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = null;
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    System.out.println("-------------------------searchFuzzyAPI end-------------------------");
  
  }

  public static void main(String[] args) {

    //api������ԣ���ȫģ��Struts2����
    {
      //ģ��Servlet������ʼ��Listener
      InitListener init = new InitListener();
      
      //Listener��ɳ�ʼ��
      init.contextInitialized(null);
      
      apiTest();
      
      //Listener����˳�
      init.contextDestroyed(null);
    }
    
    
    
    
    //��ϸ�µ����ݴ����߼����ԣ���ȫ���漰api����
    {
//      dbInit();
//      userTest();
//      pdoTest();
//      dataTest();
    }

    
  }
}