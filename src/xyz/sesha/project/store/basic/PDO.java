package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.store.index.UserIdPDONameToPDOId;
import xyz.sesha.project.utils.JedisUtil;

/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：pdo
 * <br>数据字段：id全库不重复, user代表所属用户的id, name同用户不重复, time意义为PDO创建时间(毫秒)
 * <br>数据形式：{"id":"1", "time":1477410877415, "user":"0", "name":"坐车", "fields":["始点", "终点", "耗时"]}
 * <br>存储形式：pdo:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Lu Xin
 */
public class PDO {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(PDO.class);
  
  /**
   * 添加操作之后,执行的全部索引钩子
   */
  public static List<HookFunction> afterAddHook = new LinkedList<HookFunction>();
  
  /**
   * <pr>保障基本数据的id全库唯一的键，里面存储自增数字
   * <br>注意：这个只用于单用户单连接环境，多用户环境会产生同步问题
   */
  private static final String DB_INDEX_KEY = "dbindex";
  
  /**
   * 检验pdo的json字符串的合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组(部分键)，数组的成员类型合法性
   * <br>忽略：值的逻辑含义
   * @param pdoJsons json字符串存储的容器
   * @return 返回检验结果，合法则返回true，非法则返回false
   */
  public static boolean checkPDOJsonFormat(Collection<String> pdoJsons) {
    boolean ret = true;
    
    for (String pdoJson : pdoJsons) {
      try {
        JSONObject json = JSONObject.fromObject(pdoJson);
        
        //判断key是否存在
        if (!json.has("id") || !json.has("time") || !json.has("user") 
            || !json.has("name") || !json.has("fields")) {
          ret =  false;
        }
        
        //null值判断，包含在instanceof关键字中
        
        //id类型：java.lang.String
        Object idObj = json.get("id");
        if (!(idObj instanceof String)) {
          ret =  false;
        }
        
        //time类型：java.lang.Long/java.lang.Integer
        Object timeObj = json.get("time");
        if (!(timeObj instanceof Long) && !(timeObj instanceof Integer)) {
          ret =  false;
        }
        
        //user类型：java.lang.String
        Object userObj = json.get("user");
        if (!(userObj instanceof String)) {
          ret =  false;
        }
        
        //name类型：java.lang.String
        Object nameObj = json.get("name");
        if (!(nameObj instanceof String)) {
          ret =  false;
        }
        
        
        //fields类型：net.sf.json.JSONArray
        Object fieldsObj = json.get("fields");
        if (!(fieldsObj instanceof JSONArray)) {
          ret =  false;
        }
        //fields限制：数组不为空
        if (((JSONArray) fieldsObj).size() <= 0) {
          ret =  false;
        }
        //fields限制：成员类型：java.lang.String
        for (int i=0; i<((JSONArray) fieldsObj).size(); i++) {
          if (!(((JSONArray) fieldsObj).get(i) instanceof String)) {
            ret =  false;
          }
        }

      } catch (JSONException e) {
        ret =  false;
      } catch (Exception e) {
        ret =  false;
      }
    }
    
    return ret;
  }
  
  /**
   * 添加pdo的真实执行方法，直接读写Redis
   * @param pdoJsons pdo的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  private static boolean innerAddPDO(Collection<String> pdoJsons) {
    
    //空集合判断
    if (pdoJsons.isEmpty()) {
      return false;
    }
    
    //数据格式检验
    if (!checkPDOJsonFormat(pdoJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    for (String pdoJson : pdoJsons) {
      JSONObject json = JSONObject.fromObject(pdoJson);
      jsons.add(json);
      
      String name = json.getString("name");
      String userId = json.getString("user");

      //TODO:存在非法写入其他用户的可能
      
      //判断user是否存在
      if (!User.hasUser(userId)) {
        logger.error("[Error][PDO][Add]：添加数据时user并不存在");
        return ret;
      }
        
      //TODO:判断PDO的user与当前user相同
    
      //pdo的name非空
      if (name.equals("")) {
        logger.error("[Error][PDO][Add]：添加数据时name是空字符串");
        return ret;
      }
      //验证pdo的name是否可用
      if (null != UserIdPDONameToPDOId.getId(userId, name)) {
        logger.error("[Error][PDO][Add]：添加数据时同user下该pdo的name已存在");
        return ret;
      }
    }

    //TODO:DB_INDEX_KEY存在同步问题
    
    //开始添加数据
    Jedis jedis = JedisUtil.getJedis();
    try {
      //获取DBIndex
      long DBIndex = -1L;
      DBIndex = Long.valueOf(jedis.get(DB_INDEX_KEY));
      
      //添加新数据，并更新参数List内的id
      pdoJsons.clear();
      for (JSONObject json : jsons) {
        String key = "pdo:" + String.valueOf(DBIndex);
        json.put("id", String.valueOf(DBIndex));
        jedis.set(key, json.toString());
        pdoJsons.add(json.toString());
        DBIndex++;
      }
      
      //更新DBIndex
      jedis.set(DB_INDEX_KEY, String.valueOf(DBIndex));
      
      ret = true; 
    } catch (NumberFormatException e) {
      e.printStackTrace();
      logger.error("[Error][PDO][Add]：数据库DBIndex异常");
      ret = false;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[Error][PDO][Add]：其他错误");
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * 添加pdo的方法，供外部访问
   * @param pdoJsons pdo的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addPDO(Collection<String> pdoJsons) {
    
    boolean ret = false;
    
    //转交给innerAddData处理
    ret = innerAddPDO(pdoJsons);
    
    //成功则执行所有索引钩子
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(pdoJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * 添加pdo的方法，供外部访问
   * @param pdoJson pdo的json字符串
   * @return 返回执行结果，true则成功，false则失败
   */
  @SuppressWarnings("serial")
  public static boolean addPDO(String pdoJson) {
    return addPDO(new ArrayList<String>(){{this.add(pdoJson);}});
  }
  
  /**
   * 给定id，判断pdo是否存在
   * @param id pdo的id
   * @return 返回判断结果，true则存在，false则不存在
   */
  public static boolean hasPDO(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "pdo:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * 给定id，返回pdo的json字符串
   * @param id pdo的id
   * @return 返回pdo的json字符串，若id不存在，则返回null
   */
  public static String getPDOJson(String id) {
    List<String> ret = getPDOJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * 给定id，返回pdo的json字符串
   * @param ids pdo的id的容器
   * @return 返回pdo的json字符串的List
   */
  public static List<String> getPDOJson(Collection<String> ids) {
    //TODO:返回List中null判断
    List<String> ret = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "pdo:" + id;
        keys.add(key);
      }
      if (keys.size() > 0) {
        ret = jedis.mget(keys.toArray(new String[keys.size()]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
}
