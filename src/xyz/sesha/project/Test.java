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
 * 前端基本测试类(单用户!!已过时！！)
 * 
 * <p>对API请求，以及Store逻辑进行测试
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
    
    //id是Long(代表所有value类型)
    String userBad1 = "{\"id\":0, \"time\":1477410793369, \"name\":\"白爷\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad1);
    System.out.println("错误输入1：" + ret);
    
    //缺少time(代表所有key存在)
    String userBad2 = "{\"id\":\"0\", \"name\":\"白爷\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad2);
    System.out.println("错误输入2：" + ret);
    
    //id是null(代表所有value的null)
    String userBad3 = "{\"id\":null, \"time\":1477410793369, \"name\":\"白爷\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad3);
    System.out.println("错误输入3：" + ret);
    
    //name是空字符串
    String userBad4 = "{\"id\":\"0\", \"time\":1477410793369, \"name\":\"\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    ret = User.addUser(userBad4);
    System.out.println("错误输入4：" + ret);
    
    //正确的user
    String user = "{\"id\":\"-1\", \"time\":1477410793369, \"name\":\"白爷\", \"pwhash\":\"5e007e7046425c92111676b1b0999f12\"}";
    List<String> users = new ArrayList<String>();
    users.add(user);
    
    //第一次添加成功
    ret = User.addUser(users);
    System.out.println("正确输入：" + ret);
    
    //第二次添加失败(name重复)
    ret = User.addUser(user);
    System.out.println("重复名字：" + ret);
    
    //hasUser测试
    System.out.println("hasUser测试：");
    System.out.println(User.hasUser("0"));
    System.out.println(User.hasUser("1"));
    System.out.println(User.hasUser("-1"));
    
    //getUserJson测试
    System.out.println("getUserJson测试：");
    System.out.println(User.getUserJson("0"));
    System.out.println(User.getUserJson("1"));
    System.out.println(User.getUserJson("-1"));
    System.out.println(User.getUserJson(new ArrayList<String>(){{this.add("0");this.add("1");this.add("-1");}}));
    
    //UserNameToUserId索引测试
    System.out.println("UserNameToUserId索引测试：");
    System.out.println(UserNameToUserId.getId("陆鑫"));
    System.out.println(UserNameToUserId.getId("白爷"));
    
    System.out.println("-------------------------userTest end-------------------------");
  }
  
  
  @SuppressWarnings("serial")
  public static void pdoTest() {
    
    boolean ret = false;
    
    System.out.println("-------------------------pdoTest start-------------------------");
    
    //id是Long(代表所有value类型)
    String pdoBad1 = "{\"id\":1, \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdoBad1);
    System.out.println("错误输入1：" + ret);
        
    //缺少time(代表所有key存在)
    String pdoBad2 = "{\"id\":\"1\", \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdoBad2);
    System.out.println("错误输入2：" + ret);

    //id是null(代表所有value的null)
    String pdoBad3 = "{\"id\":null, \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdoBad3);
    System.out.println("错误输入3：" + ret);

    //name是空字符串
    String pdoBad4 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdoBad4);
    System.out.println("错误输入4：" + ret);

    //fields成员不是字符串
    String pdoBad5 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", 345, \"耗时\"]}";
    ret = PDO.addPDO(pdoBad5);
    System.out.println("错误输入5：" + ret);

    //fields不是数组
    String pdoBad6 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":456}";
    ret = PDO.addPDO(pdoBad6);
    System.out.println("错误输入6：" + ret);

    //fields数组为空
    String pdoBad7 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[]}";
    ret = PDO.addPDO(pdoBad7);
    System.out.println("错误输入7：" + ret);

    //user不存在
    String pdoBad8 = "{\"id\":\"1\", \"time\":1477410877415, \"user\":\"6\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdoBad8);
    System.out.println("错误输入8：" + ret);
    
    
    //正确输入1
    String pdo1 = "{\"id\":\"-1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}";
    ret = PDO.addPDO(pdo1);
    System.out.println("正确输入1：" + ret);
    
    //正确输入2,3
    String pdo2 = "{\"id\":\"-1\", \"time\":1477412043598, \"user\":\"0\", \"name\":\"上学\", \"fields\":[\"伙伴\", \"日期\"]}";
    String pdo3 = "{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"支付\", \"fields\":[\"金额\"]}";
    List<String> pdos = new ArrayList<String>();
    pdos.add(pdo2);
    pdos.add(pdo3);
    ret = PDO.addPDO(pdos);
    System.out.println("正确输入2,3：" + ret);
    
    //重复数据失败
    ret = PDO.addPDO(pdos);
    System.out.println("重复输入：" + ret);
    
    
    //hasPDO测试
    System.out.println("hasPDO测试：");
    System.out.println(PDO.hasPDO("1"));
    System.out.println(PDO.hasPDO("2"));
    System.out.println(PDO.hasPDO("3"));
    System.out.println(PDO.hasPDO("0"));
    System.out.println(PDO.hasPDO("-1"));
    System.out.println(PDO.hasPDO("7"));
    
    //getPDOJson测试
    System.out.println("getPDOJson测试：");
    System.out.println(PDO.getPDOJson("1"));
    System.out.println(PDO.getPDOJson("2"));
    System.out.println(PDO.getPDOJson("3"));
    System.out.println(PDO.getPDOJson("0"));
    System.out.println(PDO.getPDOJson("-1"));
    System.out.println(PDO.getPDOJson("7"));
    System.out.println(PDO.getPDOJson(new ArrayList<String>(){{this.add("1");this.add("2");this.add("3");this.add("0");this.add("-1");this.add("7");}}));
    
    //UserIdToPDOAllIds索引测试
    System.out.println("UserIdToPDOAllIds索引测试：");
    System.out.println(UserIdToPDOAllIds.getAllIds("0"));
    System.out.println(UserIdToPDOAllIds.getAllIds("1"));

    //UserIdPDONameToPDOId索引测试
    System.out.println("UserIdPDONameToPDOId索引测试：");
    System.out.println(UserIdPDONameToPDOId.getId("0", "坐车"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "上学"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "支付"));
    System.out.println(UserIdPDONameToPDOId.getId("1", "坐车"));
    System.out.println(UserIdPDONameToPDOId.getId("0", "未知"));
    
    System.out.println("-------------------------pdoTest end-------------------------");
  }
  
  @SuppressWarnings("serial")
  public static void dataTest() {
    
    boolean ret = false;
    
    System.out.println("-------------------------dataTest start-------------------------");
    
    //正确输入1
    String data1 = "{\"id\":\"-1\", \"time\":1477412545804, \"pdo\": \"1\", \"values\": [\"家\", \"学校\", \"10分钟\"], \"related_data\": []}";
    ret = Data.addData(data1);
    System.out.println("正确输入1：" + ret);

    //正确输入2
    String data2 = "{\"id\":\"-1\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"家人和学校同学\", \"2016-10-23\"], \"related_data\": [\"4\"]}";
    ret = Data.addData(data2);
    System.out.println("正确输入2：" + ret);
    
    //正确输入3
    String data3 = "{\"id\":\"-1\", \"time\":1477412576389, \"pdo\": \"3\", \"values\": [\"32.10\"], \"related_data\": [\"4\", \"5\"]}";
    ret = Data.addData(data3);
    System.out.println("正确输入3：" + ret);

    //正确输入4,5,6
    String data4 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    String data5 = "{\"id\":\"-1\", \"time\":1477412577654, \"pdo\": \"3\", \"values\": [\"10.23\"], \"related_data\": []}";
    String data6 = "{\"id\":\"-1\", \"time\":1477412666666, \"pdo\": \"1\", \"values\": [\"学校\", \"网吧\", \"1分钟\"], \"related_data\": []}";
    List<String> datas = new ArrayList<String>();
    datas.add(data4);
    datas.add(data5);
    datas.add(data6);
    ret = Data.addData(datas);
    System.out.println("正确输入4,5,6：" + ret);

    
    //不存在重复数据这个概念
    
    
    //id是Long(代表所有value类型)
    String dataBad1 = "{\"id\":-1, \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad1);
    System.out.println("错误输入1：" + ret);

    //缺少time(代表所有key存在)
    String dataBad2 = "{\"id\":\"-1\", \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad2);
    System.out.println("错误输入2：" + ret);

    //id是null(代表所有value的null)
    String dataBad3 = "{\"id\":null, \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad3);
    System.out.println("错误输入3：" + ret);

    //pdo不存在
    String dataBad4 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"99\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad4);
    System.out.println("错误输入4：" + ret);

    //pdo的fields与values元素个数不一致
    String dataBad5 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\", \"ha\"], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad5);
    System.out.println("错误输入5：" + ret);

    //values成员不是字符串
    String dataBad6 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [66], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad6);
    System.out.println("错误输入6：" + ret);

    //values不是数组
    String dataBad7 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": \"23.10\", \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad7);
    System.out.println("错误输入7：" + ret);

    //values数组为空
    String dataBad8 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [], \"related_data\": [\"4\", \"5\", \"6\"]}";
    ret = Data.addData(dataBad8);
    System.out.println("错误输入8：" + ret);

    //related_data成员不是字符串
    String dataBad9 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [66, \"aa\"], \"related_data\": [\"4\", 5, \"6\"]}";
    ret = Data.addData(dataBad9);
    System.out.println("错误输入9：" + ret);

    //related_data不是数组
    String dataBad10 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": \"23.10\", \"related_data\": \"6\"}";
    ret = Data.addData(dataBad10);
    System.out.println("错误输入10：" + ret);

    //related_data里面的id不存在
    String dataBad11 = "{\"id\":\"-1\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"17\", \"6\"]}";
    ret = Data.addData(dataBad11);
    System.out.println("错误输入11：" + ret);
    
    
    //hasData测试
    System.out.println("hasData测试：");
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
    
    //getDataJson测试
    System.out.println("getDataJson测试：");
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
    
    
    //UserIdKeysToDataIds索引测试
    System.out.println("UserIdKeysToDataIds索引测试：");
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("家")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("家", "学校")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("10", "23")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("学校", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("家", "学校", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList(".10")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("分钟", "1")));
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("2016-")));
    
    System.out.println(UserIdKeysToDataIds.getIds("0", Arrays.asList("分钟", "和")));
    System.out.println(UserIdKeysToDataIds.getIds("1", Arrays.asList("分钟")));
    
    
    System.out.println("-------------------------dataTest end-------------------------");
  }
  
  public static void apiTest() {
   
    String params;
  
    System.out.println("-------------------------pdoAddAPI begin-------------------------");
    xyz.sesha.project.api.pdo.AddAction pdoAddAPI = new xyz.sesha.project.api.pdo.AddAction(); 
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477410877415, \"user\":\"0\", \"name\":\"坐车\", \"fields\":[\"始点\", \"终点\", \"耗时\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477412043598, \"user\":\"0\", \"name\":\"上学\", \"fields\":[\"伙伴\", \"日期\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"pdos\":[{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"支付\", \"fields\":[\"金额\"]}]}";
    pdoAddAPI.setParams(params);
    pdoAddAPI.execute();
    System.out.println(pdoAddAPI.getResult());
    params = "{\"haha\":[{\"id\":\"-1\", \"time\":1477411586548, \"user\":\"0\", \"name\":\"支付\", \"fields\":[\"金额\"]}]}";
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
    params = "{\"name\": \"坐车\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"支付\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"上学\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"name\": \"吃饭\"}";
    pdoCheckNameAPI.setParams(params);
    pdoCheckNameAPI.execute();
    System.out.println(pdoCheckNameAPI.getResult());
    params = "{\"ne\": \"吃饭\"}";
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
    params = "{\"datas\": [{\"id\":\"4\", \"time\":1477412545804, \"pdo\": \"1\", \"values\": [\"家\", \"学校\", \"10分钟\"], \"related_data\": []}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"5\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"家人和学校同学\", \"2016-10-23\"], \"related_data\": [\"4\"]}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"6\", \"time\":1477412576389, \"pdo\": \"3\", \"values\": [\"32.10\"], \"related_data\": [\"4\", \"5\"]}]}";
    dataAddAPI.setParams(params);
    dataAddAPI.execute();
    System.out.println(dataAddAPI.getResult());
    params = "{\"datas\": [{\"id\":\"7\", \"time\":1477412576999, \"pdo\": \"3\", \"values\": [\"23.10\"], \"related_data\": [\"4\", \"5\", \"6\"]}, {\"id\":\"8\", \"time\":1477412577654, \"pdo\": \"3\", \"values\": [\"10.23\"], \"related_data\": []}, {\"id\":\"9\", \"time\":1477412666666, \"pdo\": \"1\", \"values\": [\"学校\", \"网吧\", \"1分钟\"], \"related_data\": []}]}";
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
    params = "{\"das\": [{\"id\":\"5\", \"time\":1477412568543, \"pdo\": \"2\", \"values\": [\"家人和学校同学\", \"2016-10-23\"], \"related_data\": [\"4\"]}]}";
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
    params = "{\"keys\": [\"家\", \"学校\"]}";
    searchFuzzyAPI.setParams(params);
    searchFuzzyAPI.execute();
    System.out.println(searchFuzzyAPI.getResult());
    params = "{\"keys\": [\"家\", \"学校\", \"1\"]}";
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

    //api层面测试，完全模拟Struts2环境
    {
      //模拟Servlet容器初始化Listener
      InitListener init = new InitListener();
      
      //Listener完成初始化
      init.contextInitialized(null);
      
      apiTest();
      
      //Listener完成退出
      init.contextDestroyed(null);
    }
    
    
    
    
    //更细致的数据处理逻辑测试，完全不涉及api部分
    {
//      dbInit();
//      userTest();
//      pdoTest();
//      dataTest();
    }

    
  }
}