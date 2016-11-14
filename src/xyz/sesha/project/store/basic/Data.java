package xyz.sesha.project.store.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import xyz.sesha.project.store.index.HookFunction;
import xyz.sesha.project.utils.JedisUtil;


/**
 * 后端数据请求功能类：基本数据操作类
 * 
 * <p>数据类型：data
 * <br>数据字段：id全库不重复, time意义为data创建时间 或 用户手动填写的时间(毫秒)
 * <br>数据形式：{"id":"4", "time":1477412545804, "pdo": "1", "values": ["家", "学校", "10分钟"], "related_data": ["5", "6"]}
 * <br>存储形式：data:[id]->json字符串
 * <br>
 * <br>注意：对外提供的接口，只依赖全库唯一的id
 * 
 * @author Lu Xin
 */
public class Data {
  
  /**
   * 获取Log4j相关Logger
   */
  private static Logger logger = Logger.getLogger(Data.class);
  
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
   * 检验data的json字符串的合法性
   * <pr>
   * <br>
   * <br>检验：键存在，值类型合法性，空数组(部分键)，数组的成员类型合法性
   * <br>忽略：值的逻辑含义
   * @param dataJsons json字符串存储的容器
   * @return 返回检验结果，合法则返回true，非法则返回false
   */
  public static boolean checkDataJsonFormat(Collection<String> dataJsons) {
    boolean ret = true;
    
    for (String dataJson : dataJsons) {
      try {
        JSONObject json = JSONObject.fromObject(dataJson);
        
        //判断key是否存在
        if (!json.has("id") || !json.has("time") || !json.has("pdo") 
            || !json.has("values") || !json.has("related_data")) {
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
        
        //pdo类型：java.lang.String
        Object pdoObj = json.get("pdo");
        if (!(pdoObj instanceof String)) {
          ret =  false;
        }
        
        //values类型：net.sf.json.JSONArray
        Object valuesObj = json.get("values");
        if (!(valuesObj instanceof JSONArray)) {
          ret =  false;
        }
        //values限制：数组不为空
        if (((JSONArray) valuesObj).size() <= 0) {
          ret =  false;
        }
        //values限制：成员类型：java.lang.String
        for (int i=0; i<((JSONArray) valuesObj).size(); i++) {
          if (!(((JSONArray) valuesObj).get(i) instanceof String)) {
            ret =  false;
          }
        }
        
        //related_data类型：net.sf.json.JSONArray
        Object relatedDataObj = json.get("related_data");
        if (!(relatedDataObj instanceof JSONArray)) {
          ret =  false;
        }
        //related_data限制：成员类型：java.lang.String
        for (int i=0; i<((JSONArray) relatedDataObj).size(); i++) {
          if (!(((JSONArray) relatedDataObj).get(i) instanceof String)) {
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
   * 添加data的真实执行方法，直接读写Redis
   * @param dataJsons data的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  private static boolean innerAddData(Collection<String> dataJsons) {
    
    //空集合判断
    if (dataJsons.isEmpty()) {
      return false;
    }
    
    //数据格式检验
    if (!checkDataJsonFormat(dataJsons)) {
      return false;
    }
    
    boolean ret = false;
    
    ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
    
    for (String dataJson : dataJsons) {
      JSONObject json = JSONObject.fromObject(dataJson);
      jsons.add(json);

      String pdoId = json.getString("pdo");
      
      //TODO:存在非法写入其他用户的可能
      
      //验证pdo合法性
      if (!PDO.hasPDO(pdoId)) {
        logger.error("[Error][Data][Add]：添加数据时pdo并不存在");
        return ret;
      }

      //TODO:判断所有PDO的user与当前user相同

      //pdo的fields与data的values个数应该相同
      JSONObject pdoJsonObj = JSONObject.fromObject(PDO.getPDOJson(pdoId));
      JSONArray pdoFields = pdoJsonObj.getJSONArray("fields");
      JSONArray dataValues = json.getJSONArray("values");
      if (pdoFields.size() != dataValues.size()) {
        logger.error("[Error][Data][Add]：添加数据时data字段数目与pdo字段数目不匹配");
        return ret;
      }
      
      //验证关联数据合法性
      JSONArray relatedData = json.getJSONArray("related_data");
      if (relatedData.size() > 0) {
        String[] relatedIds = new String[relatedData.size()];
        for (int i=0; i<relatedData.size(); i++) {
          relatedIds[i] = relatedData.getString(i);
        }
        for (String id : relatedIds) {
          if (!Data.hasData(id)) {
            logger.error("[Error][Data][Add]：添加数据时data关联数据不存在");
            return ret;
          }
        }
      }

    }
    
    //TODO:DB_INDEX_KEY存在同步问题
    
    //开始添加数据
    Jedis jedis = JedisUtil.getJedis();
    try {
      //获取DBIndex
      long DBIndex = -1L;
      DBIndex = Long.valueOf(jedis.get(DB_INDEX_KEY));
      
      //生成键值对，并更新参数List内的id
      dataJsons.clear();
      List<String> keyValues = new ArrayList<String>();
      for (JSONObject json : jsons) {
        String key = "data:" + String.valueOf(DBIndex);
        json.put("id", String.valueOf(DBIndex));
        dataJsons.add(json.toString());
        keyValues.add(key);
        keyValues.add(json.toString());
        DBIndex++;
      }
      
      //更新DBIndex
      jedis.set(DB_INDEX_KEY, String.valueOf(DBIndex));
      
      //添加新数据
      jedis.mset(keyValues.toArray(new String[keyValues.size()]));
      
      //更新关联数据
      
      //收集所有关联数据的key，去重复
      Set<String> relatedAllIds = new TreeSet<String>();
      for (JSONObject json : jsons) {
        JSONArray relatedData = json.getJSONArray("related_data");
        if (relatedData.size() > 0) {
          String[] relatedIds = new String[relatedData.size()];
          for (int i=0; i<relatedData.size(); i++) {
            relatedIds[i] = relatedData.getString(i);
          }
          for (String id : relatedIds) {
            String key = "data:" + id;
            relatedAllIds.add(key);
          }
        }
      }
      
      //关联数据个数不为0
      if (relatedAllIds.size() > 0) {
        
        //关联数据的key的Set转数组
        String[] relatedAllArray = relatedAllIds.toArray(new String[relatedAllIds.size()]);
        
        //redis获取全部关联数据，无重复
        List<String> relatedAllString = jedis.mget(relatedAllArray);
        
        //为全部关联数据建立id->json的Map，id唯一无歧义
        Map<String, JSONObject> relatedMap = new TreeMap<String, JSONObject>();
        for (String relatedString : relatedAllString) {
          JSONObject relatedJson = JSONObject.fromObject(relatedString);
          relatedMap.put(relatedJson.getString("id"), relatedJson);
        }
        
        //对每个新data的每个关联数据id，在Map中找到它并更新它的related_data
        for (JSONObject json : jsons) {
          String dataId = json.getString("id");
          JSONArray relatedData = json.getJSONArray("related_data");
          if (relatedData.size() > 0) {
            String[] relatedIds = new String[relatedData.size()];
            for (int i=0; i<relatedData.size(); i++) {
              relatedIds[i] = relatedData.getString(i);
            }
            for (String id : relatedIds) {
              JSONObject relatedJson = relatedMap.get(id);
              JSONArray relatedArray = relatedJson.getJSONArray("related_data");
              if (!relatedArray.contains(dataId)) {
                relatedArray.add(dataId);
              }
              relatedJson.put("related_data", relatedArray);
              relatedMap.replace(id, relatedJson);
            }
          }
        }
        
        //redis更新全部关联数据
        keyValues.clear();
        for (Entry<String, JSONObject> entry : relatedMap.entrySet()) {
          String key = "data:" + entry.getKey();
          keyValues.add(key);
          keyValues.add(entry.getValue().toString());
        }
        jedis.mset(keyValues.toArray(new String[keyValues.size()]));
      }
      
      ret = true; 
    } catch (NumberFormatException e) {
      logger.error("[Error][Data][Add]：数据库DBIndex异常");
      e.printStackTrace();
      ret = false;
    } catch (Exception e) {
      logger.error("[Error][Data][Add]：其他错误");
      e.printStackTrace();
      ret = false;
    } finally {
      jedis.close();
    }

    return ret;
  }

  /**
   * 添加data的方法，供外部访问
   * @param dataJsons data的json字符串容器
   * @return 返回执行结果，true则成功，false则失败
   */
  public static boolean addData(Collection<String> dataJsons) {
    
    boolean ret = false;
    
    //转交给innerAddData处理
    ret = innerAddData(dataJsons);
    
    //成功则执行所有索引钩子
    if (ret) {
      for (HookFunction hook : afterAddHook) {
        hook.func(dataJsons);
      }
    }
   
    return ret;
  }
  
  /**
   * 添加data的方法，供外部访问
   * @param dataJson data的json字符串
   * @return 返回执行结果，true则成功，false则失败
   */
  @SuppressWarnings("serial")
  public static boolean addData(String dataJson) {
    return addData(new ArrayList<String>(){{this.add(dataJson);}});
  }
  
  /**
   * 给定id，判断data是否存在
   * @param id data的id
   * @return 返回判断结果，true则存在，false则不存在
   */
  public static boolean hasData(String id) {
    
    boolean ret = false;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      String key = "data:" + id;
      ret = jedis.exists(key);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      jedis.close();
    }
    
    return ret;
  }
  
  /**
   * 给定id，返回data的json字符串
   * @param id data的id
   * @return 返回data的json字符串，若id不存在，则返回null
   */
  public static String getDataJson(String id) {
    List<String> ret = getDataJson(Arrays.asList(id));
    if (ret.size() <= 0) {
      return null;
    }
    return ret.get(0);
  }
  
  /**
   * 给定id，返回data的json字符串
   * @param ids data的id的容器
   * @return 返回data的json字符串的List
   */
  public static List<String> getDataJson(Collection<String> ids) {
    //TODO:返回List中null判断
    List<String> ret = new ArrayList<String>();;
    Jedis jedis = JedisUtil.getJedis();
    
    try {
      List<String> keys = new ArrayList<String>();
      for (String id : ids) {
        String key = "data:" + id;
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
