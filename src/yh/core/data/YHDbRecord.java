package yh.core.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yh.core.util.YHUtility;

public class YHDbRecord {
  //除编码、名称和父级编码以外的字段值
  //键是字段的名称，值是字段的值
  private Map<String, Object> queryFields = new HashMap<String, Object>();
  //字段计数
  private int fieldCnt = 0;
  //字段索引
  private Map<String, String> fieldIndexMap = new HashMap<String, String>();

  /**
   * 连接另外一个记录集
   */
  public void join(YHDbRecord other) {
    if (other == null) {
      return;
    }
    int fieldCnt = other.getFieldCnt();
    for (int i = 0; i < fieldCnt; i++) {
      String fieldName = other.getNameByIndex(i);
      Object fieldValue = other.getValueByIndex(i);
      this.addField(fieldName, fieldValue);
    }
  }
  
  /**
   * 取得字段的个数
   * @return
   */
  public int getFieldCnt() {
    return fieldCnt;
  }
  
  /**
   * 把所有的字段值串接成一个字符串
   * @return
   */
  public StringBuffer toJson() {
    StringBuffer rtStr = new StringBuffer("{");
    for (int i = 0; i < fieldCnt; i++) {
      String fieldName = this.getNameByIndex(i);
      Object fieldValue = queryFields.get(fieldName);
      rtStr.append(fieldName);
      rtStr.append(":");
      
      String value = null;
      if (fieldValue == null) {
        value = "\"\"";
      }else {
        Class fieldType = fieldValue.getClass(); 
        if (Integer.class.equals(fieldType)) {        
          value = String.valueOf(((Integer)fieldValue).intValue());
        }else if (Long.class.equals(fieldType)) {        
          value = String.valueOf(((Long)fieldValue).longValue());
        }else if (Double.class.equals(fieldType)) {        
          value = YHUtility.getFormatedStr(((Double)fieldValue).doubleValue(), YHUtility.WITHOUTGROUP);
        }else if (Date.class.equals(fieldType)) {
          value = "\"" + YHUtility.getDateTimeStr((Date)fieldValue) + "\"";
        }else {
          if (fieldValue == null) {
            value = "\"\"";
          }else {
            String tmpStr = YHUtility.null2Empty(fieldValue.toString());
            tmpStr = tmpStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "").replace("\'", "\\\'");
            value = "\"" + tmpStr + "\"";
          }
        }
      }
      rtStr.append(value);
      if (i < fieldCnt - 1) {
        rtStr.append(",");
      }
    }
    rtStr.append("}");
    return rtStr;
  }
  
  /**
   * 增加字段
   * @param fieldName       字段名称
   * @param fieldValue      字段值
   */
  public void addField(String fieldName, Object fieldValue) {
    queryFields.put(fieldName, fieldValue);
    fieldIndexMap.put(String.valueOf(fieldCnt++), fieldName);
  }
  
  /**
   * 增加字段
   * @param fieldName       字段名称
   * @param fieldValue      字段值
   */
  public void updateField(String fieldName, Object fieldValue) {
    queryFields.put(fieldName, fieldValue);
    if (!fieldIndexMap.containsValue(fieldName)) {
      fieldIndexMap.put(String.valueOf(fieldCnt++), fieldName);
    }
  }
  
  /**
   * 由字段名称取得字段的值
   * @param fieldName    字段名称
   * @return
   */
  public Object getValueByName(String fieldName) {
    return queryFields.get(fieldName);
  }
  
  /**
   * 由字段的索引取得字段的值
   * @param fieldIndex  字段的索引
   * @return
   */
  public Object getValueByIndex(int fieldIndex) {
    String fieldName = (String)fieldIndexMap.get(String.valueOf(fieldIndex));
    if (fieldName != null) {
      return queryFields.get(fieldName);
    }
    return null;
  }
  
  /**
   * 由字段的索引取得字段的值
   * @param fieldIndex  字段的索引
   * @return
   */
  public String getNameByIndex(int fieldIndex) {
    String fieldName = (String)fieldIndexMap.get(String.valueOf(fieldIndex));
    return fieldName;
  }
  
  /**
   * 克隆一个对象
   */
  public Object clone() {
    return this.clone(null);
  }
  
  /**
   * 克隆一个对象
   * @param fieldNamePrefix       字段前缀
   */
  public Object clone(String fieldNamePrefix) {
    YHDbRecord rtObj = new YHDbRecord();
    
    for (int i = 0; i < fieldCnt; i++) {
      String fieldName = getNameByIndex(i);
      Object fieldValue = getValueByIndex(i);
      if (!YHUtility.isNullorEmpty(fieldNamePrefix)) {
        fieldName = fieldNamePrefix + "." + fieldName;
      }
      rtObj.addField(fieldName, fieldValue);
    }
    
    return rtObj;
  }

  @Override
  public String toString() {
    return "YHDbRecord [fieldCnt=" + fieldCnt + ", fieldIndexMap="
        + fieldIndexMap + ", queryFields=" + queryFields + "]";
  }
}
