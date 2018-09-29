package yh.core.autorun;

/**
 * 文件提取配置信息类
 * @author jpt
 *
 */
public class YHModuleFileConf {
  //模块编码
  private String moduleNo = null;
  //表名
  private String tableName = null;
  //ID字段名
  private String idField = null;
  //附件ID字段名
  private String attachIdField = null;
  //附件名称字段名
  private String attachNameField = null;

  public String getModuleNo() {
    return moduleNo;
  }
  public void setModuleNo(String moduleNo) {
    this.moduleNo = moduleNo;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getAttachIdField() {
    return attachIdField;
  }
  public void setAttachIdField(String attachIdField) {
    this.attachIdField = attachIdField;
  }
  public String getAttachNameField() {
    return attachNameField;
  }
  public void setAttachNameField(String attachNameField) {
    this.attachNameField = attachNameField;
  }
  public String getIdField() {
    return idField;
  }
  public void setIdField(String idField) {
    this.idField = idField;
  }
}
