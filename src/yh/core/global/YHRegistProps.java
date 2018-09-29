package yh.core.global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yh.core.util.YHUtility;

/**
 * 系统注册信息类
 * @author jpt
 *
 */
public class YHRegistProps {
  //系统注册信息
  private static Map<String, String> props = null;
  
  /**
   * 是否是空
   * @return
   */
  public static boolean isEmpty() {
    return props == null || props.size() < 1;
  }
  /**
   * 取得系统配置属性
   * @param key
   * @return
   */
  public static String getProp(String key) {
    if (props == null) {
      return "";
    }
    String prop = props.get(key);
    if (prop != null) {
      prop = prop.trim();
    }else {
      prop = "";
    }
    return prop;
  }
  
  /**
   * 取得字符串类型的值
   * @param key
   * @return
   */
  public static String getString(String key) {
    return getProp(key);
  }
  
  /**
   * 取得整型值
   * @param key
   * @return
   */
  public static int getInt(String key) {
    int rtInt = 0;
    try {
      String strValue = getString(key);
      if (!YHUtility.isNullorEmpty(strValue)) {
        rtInt = Integer.parseInt(strValue);
      }
    }catch(Exception ex) {
    }
    return rtInt;
  }
  
  /**
   * 取得长整型值
   * @param key
   * @return
   */
  public static long getLong(String key) {
    long rtLong = 0;
    try {
      String strValue = getString(key);
      if (!YHUtility.isNullorEmpty(strValue)) {
        rtLong = Long.parseLong(strValue);
      }
    }catch(Exception ex) {      
    }
    return rtLong;
  }
  
  /**
   * 清楚带后缀的属性
   */
  public static void clear(String postFix) {
    if (props != null) {
      YHUtility.clearMapPost(props, postFix);
    }
  }
  /**
   * 清楚属性
   */
  public static void clear() {
    if (props != null) {
      props.clear();
      props = null;
    }
  }
  /**
   * 设置系统配置属性
   * @param props
   */
  public static void setProps(Map aProps) {
    clear();
    props = aProps;
  }
  
  public static void updateProp(String key, String value) {
    props.put(key, value);
  }
  
  /**
   * 设置系统配置属性
   * @param props
   */
  public static void addProps(Map aProps) {
    if (aProps == null) {
      return;
    }
    if (props == null) {
      props = new HashMap<String, String>();
    }
    Iterator iKeys = aProps.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = (String)iKeys.next();
      String value = (String)aProps.get(key);
      props.put(key, value);
    }
  }
}
