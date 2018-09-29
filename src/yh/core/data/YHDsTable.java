package yh.core.data;

import java.util.ArrayList;
import java.util.List;


/**
 * 表描述
 * @author jpt
 * @version 1.0
 * @date 2006-8-9
 */
public class YHDsTable {
  /**  **/
  private int seqId = 0;
  /** 编码5位长 **/
  private String tableNo = null;
  /**  **/
  private String tableName = null;


  /** Java类名 **/
  private String className = null;
  /**  **/
  private String tableDesc = null;
  /** 1=代码表,2=小编码表,3=参数表,4=数据主表,5=数据从表,6=多对多关系表 **/
  private String categoryNo = null;
  /**  **/
  private String dbNo = null;
  /** 字段列表 **/
 private ArrayList<YHDsField> fieldList = null;
  
 public String getClassName() {
   return className;
 }
 public void setClassName(String className) {
   this.className = className;
 }
  /**
   * 取得对象的个数   */
  public int getFieldCnt() {
    return fieldList.size();
  }
  /**
   * 按索引取值   * @param index   索引
   */
  public YHDsField getField(int index) {
    if (index < 0 || index >= fieldList.size()) {
      return null;
    }
    return (YHDsField)fieldList.get(index);
  }
  /**
   * 增加对象
   * @param field   对象
   */
  public void addField(YHDsField field) {
    fieldList.add(field);
  }
  /**
   * 增加对象
   * @param fieldList   对象
   */
  public void addField(ArrayList<YHDsField> fieldList) {
    fieldList.addAll(fieldList);
  }

  /**
   *
   */
  public int getSeqId() {
    return this.seqId;
  }

  /**
   *
   * @param seqId
   */
  public void setSeqId(String seqId) {
    int seq = Integer.parseInt(seqId);
    this.seqId = seq;
  }
  public void setSeqId(int seqId) {
   
    this.seqId = seqId;
  }

  /**
   *
   */
  public String getTableNo() {
    return this.tableNo;
  }

  /**
   *
   * @param tableNo
   */
  public void setTableNo(String tableNo) {
    this.tableNo = tableNo;
  }

  /**
   *
   */
  public String getTableName() {
    return this.tableName;
  }

  /**
   *
   * @param tableName
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   *
   */
  public String getTableDesc() {
    return this.tableDesc;
  }

  /**
   *
   * @param tableDesc
   */
  public void setTableDesc(String tableDesc) {
    this.tableDesc = tableDesc;
  }

  /**
   *
   */
  public String getDbNo() {
    return this.dbNo;
  }

  /**
   *
   * @param dbNo
   */
  public void setDbNo(String dbNo) {
    this.dbNo = dbNo;
  }

  /**
   *
   */
  public String getCategoryNo() {
    return this.categoryNo;
  }

  /**
   *
   * @param categoryNo
   */
  public void setCategoryNo(String categoryNo) {
    this.categoryNo = categoryNo;
  }
  public ArrayList<YHDsField> getFieldList() {
    return fieldList;
  }
  public void setFieldList(ArrayList<YHDsField> fieldList) {
    this.fieldList = fieldList;
  }
/*  @Override
  public String toString() {
    String tos = "categoryNo : " +this.categoryNo + " className : " +this.className+" dbNo : "+this.dbNo+" seqId : "+this.seqId+" tableName : "+this.tableName+" tableNo : "+this.tableNo;
    ArrayList<YHDsField> l = getFieldList();
    if(l!=null)
    for (YHDsField yhDsField : l) {
      yhDsField.toString();
    }
    return tos;
  }*/
  @Override
  public String toString() {
   
    ArrayList<YHDsField> l = getFieldList();
    if(l!=null)
      for (YHDsField yhDsField : l) {
        //System.out.println(yhDsField.toString());
      }
    return "YHDsTable [categoryNo=" + categoryNo + ", className=" + className
        + ", dbNo=" + dbNo + ", fieldList=" + fieldList + ", seqId=" + seqId
        + ", tableDesc=" + tableDesc + ", tableName=" + tableName
        + ", tableNo=" + tableNo + "]";
  }
}
