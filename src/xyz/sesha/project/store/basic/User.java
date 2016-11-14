package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.store.index.UserNameToUserId;
import xyz.sesha.project.utils.JedisUtil;

/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：user
 * <br>数据字段：id全库不重复, name全库不重复, pwhash为密码32位md5值(小写), time意义为user创建时间(毫秒)
 * <br>数据形式：{"id":"0", "time":1477410793369, "name":"白爷", "pwhash":"5e007e7046425c92111676b1b0999f12"}
 * <br>存储形式：user:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Lu Xin
 */
public class User {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(User.class);
  
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
   * 检验user的json字符串的合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组(部分键)，数组的成员类型合法性
   * <br>忽略：值的逻辑含义
   * @param userJsons json字符串存储的容器
   * @return 返回检验结果，合法则返回true，非法则返回false
   */
  public static boolean checkUserJsonFormat(Collection<String> userJsons) {
    boolean ret = true;
    
    for (String userJson : userJsons) {
      try {
        JSONObject json = JSONObject.fromObject(userJson);
        
        //判断key是否存在
        if (!json.has("id") || !json.has("time") || !json.has("name") || !json.has("pwhash")) {
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

        //name类型：java.lang.String
        Object nameObj = json.get("name");
        if (!(nameObj instanceof String)) {
          ret =  false;
        }
        
        //pwhash类型：java.lang.String
        Object pwhashObj = json.get("pwhash");
        if (!(pwhashObj instanceof String)) {
          ret =  false;
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
   * 添加user的真实执行方法，直接读写Redis
   * @param userJsons user的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  private static boolean innerAddUser(Collection<String> userJsons) {
    
    //空集合判断
    if (userJsons.isEmpty()) {
      return false;
    }
    
    //数据格式检验
    if (!checkUserJsonFormat(userJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    for (String userJson : userJsons) {
      JSONObject json = JSONObject.fromObject(userJson);
      jsons.add(json);

      String name = json.getString("name");
      //用户名非空
      if (name.equals("")) {
        logger.error("[Error][User][Add]：添加数据时name是空字符串");
        return ret;
      }
      //用户名不重复
      if (null != UserNameToUserId.getId(name)) {
        logger.error("[Error][User][Add]：添加数据时name已存在");
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
      userJsons.clear();
      for (JSONObject json : jsons) {
        String key = "user:" + String.valueOf(DBIndex);
        json.put("id", String.valueOf(DBIndex));
        jedis.set(key, json.toString());
        userJsons.add(json.toString());
        DBIndex++;
      }
      
      //更新DBIndex
      jedis.set(DB_INDEX_KEY, String.valueOf(DBIndex));
      
      ret = true; 
    } catch (NumberFormatException e) {
      logger.error("[Error][User][Add]：数据库DBIndex异常");
      e.printStackTrace();
      ret = false;
    } catch (Exception e) {
      logger.error("[Error][User][Add]：其他错误");
      e.printStackTrace();
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * 添加user的方法，供外部访问
   * @param userJsons user的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addUser(Collection<String> userJsons) {
    
    boolean ret = false;
    
    //转交给innerAddData处理
    ret = innerAddUser(userJsons);
    
    //成功则执行所有索引钩子
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(userJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * 添加user的方法，供外部访问
   * @param userJson user的json字符串
   * @return 返回执行结果，true则成功，false则失败
   */
  @SuppressWarnings("serial")
  public static boolean addUser(String userJson) {
    return addUser(new ArrayList<String>(){{this.add(userJson);}});
  }
  
  /**
   * 给定id，判断user是否存在
   * @param id user的id
   * @return 返回判断结果，true则存在，false则不存在
   */
  public static boolean hasUser(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "user:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * 给定id，返回user的json字符串
   * @param id user的id
   * @return 返回user的json字符串，若id不存在，则返回null
   */
  public static String getUserJson(String id) {
    List<String> ret = getUserJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * 给定id，返回user的json字符串
   * @param ids user的id的容器
   * @return 返回user的json字符串的List
   */
  public static List<String> getUserJson(Collection<String> ids) {
    //TODO:返回List中null判断
    List<String> ret = new ArrayList<String>();
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "user:" + id;
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
