package yh.core.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;

public class YHProps {
  private Map<String, String> propsMap = new LinkedHashMap<String, String>();
  
  /**
   * 取得键的迭代子
   * @return
   */
  public Iterator<String> itKeys() {
    return propsMap.keySet().iterator();
  }
  /**
   * 取得值的迭代子
   * @return
   */
  public Iterator<String> itVaues() {
    return propsMap.values().iterator();
  }
  /**
   * 从文件中加载系统属性定义
   * @param filePath
   * @throws Exception
   */
  public void loadProps(String filePath) throws Exception {
    List<String> lineList = new ArrayList<String>();
    YHFileUtility.loadLine2Array(filePath, lineList);
    for (String lineStr : lineList) {
      lineStr = lineStr.trim();
      if (YHUtility.isNullorEmpty(lineStr)) {
        continue;
      }
      if (lineStr.startsWith("#")) {
        continue;
      }
      String[] strArray = lineStr.split("=");
      if (strArray.length < 2) {
        continue;
      }
      propsMap.put(strArray[0].trim(), strArray[1].trim());
    }
  }
  /**
   * 新增加属性定义
   * @param key
   * @param value
   */
  public void addProp(String key, String value) {
    this.propsMap.put(key, value);
  }
  /**
   * 删除属性值
   * @param key
   */
  public void removeProp(String key) {
    this.propsMap.remove(key);
  }
  /**
   * 取得字符串值
   * @param key
   * @return
   */
  public String get(String key) {
    String rtStr = propsMap.get(key);
    if (rtStr == null) {
      return null;
    }
    return rtStr.trim();
  }
  /**
   * 取得整型值
   * @param key
   * @return
   */
  public int getInt(String key) {
    String value = propsMap.get(key);
    if (YHUtility.isNullorEmpty(value)) {
      return 0;
    }
    return Integer.parseInt(key);
  }
  /**
   * 取得长整型值
   * @param key
   * @return
   */
  public long getLong(String key) {
    String value = propsMap.get(key);
    if (YHUtility.isNullorEmpty(value)) {
      return 0;
    }
    return Long.parseLong(key);
  }
}
