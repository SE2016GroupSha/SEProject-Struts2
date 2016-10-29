package xyz.sesha.project.store.index;

import java.util.List;

/**
 * 索引钩子方法封装类
 * <p>当对基本数据进行操作后,数据索引类可能需要更新索引,因此数据索引类在类装载初始化时,
 * 会在基本数据类中设置钩子方法,这个类为钩子方法提供了一个通用的封装
 * @author Lu Xin
 */
public class HookFunction {
  
  /**
   * 通用钩子方法
   * <p>不实现任何功能，在匿名内部类中重写使用
   * @param jsonStrings 基本数据的Json字符串List
   * @return 无返回值
   */
  public void func(List<String> jsonStrings) {
    
  }
}
