package yh.core.data;

import yh.core.util.YHUtility;

public class YHPropField {
//索引
  private int index = 0;
  //属性字段名
  private String fieldName = null;
  //属性字段描述
  private String fieldDesc = null;
  //属性字段数据长度
  private int fieldLenth = 15;
  //必须填写
  private boolean isMustFill = false;
  //字段值
  private String fieldValue = null;
  //原值
  private String oldValue = null;
  //控件类型
  private int cntrlType = 0;
  //控件对象ID
  private String cntrlObjId = null;
  
  /**
   * 判断值是否有变化
   * @return
   */
  public boolean hasChanged() {
    return !YHUtility.null2Empty(fieldValue).equals(
        YHUtility.null2Empty(oldValue));
  }

  public String getFieldDesc() {
    return fieldDesc;
  }
  public void setFieldDesc(String fieldDesc) {
    this.fieldDesc = fieldDesc;
  }
  public int getFieldLenth() {
    return fieldLenth;
  }
  public void setFieldLenth(int fieldLenth) {
    this.fieldLenth = fieldLenth;
  }
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public String getFieldValue() {
    return fieldValue;
  }
  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }
  public boolean isMustFill() {
    return isMustFill;
  }
  public void setMustFill(boolean isMustFill) {
    this.isMustFill = isMustFill;
  }
  public int getCntrlType() {
    return cntrlType;
  }
  public void setCntrlType(int cntrlType) {
    this.cntrlType = cntrlType;
  }
  public String getOldValue() {
    return oldValue;
  }
  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }
  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }

  public String getCntrlObjId() {
    return cntrlObjId;
  }

  public void setCntrlObjId(String cntrlObjId) {
    this.cntrlObjId = cntrlObjId;
  } 
}
