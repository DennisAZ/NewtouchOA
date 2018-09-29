package yh.core.dto;

/**
 * 下拉框代码加载参数类
 * @author jpt
 *
 */
public class YHCodeLoadParam {
  private String cntrlId = null;
  private String tableName = null;
  private String codeField = null;
  private String nameField = null;
  private String value = null;
  private String isMustFill = null;
  private String filterField = null;
  private String filterValue = null;
  private String order = null;
  private String reloadBy = null;
  private String extFilter = null;

  public String getExtFilter() {
    return extFilter;
  }
  public void setExtFilter(String extFilter) {
    this.extFilter = extFilter;
  }
  public String getCntrlId() {
    return cntrlId;
  }
  public void setCntrlId(String cntrlId) {
    this.cntrlId = cntrlId;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getCodeField() {
    return codeField;
  }
  public void setCodeField(String codeField) {
    this.codeField = codeField;
  }
  public String getNameField() {
    return nameField;
  }
  public void setNameField(String nameField) {
    this.nameField = nameField;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getIsMustFill() {
    return isMustFill;
  }
  public void setIsMustFill(String isMustFill) {
    this.isMustFill = isMustFill;
  }
  public String getFilterField() {
    return filterField;
  }
  public void setFilterField(String filterField) {
    this.filterField = filterField;
  }
  public String getFilterValue() {
    return filterValue;
  }
  public void setFilterValue(String filterValue) {
    this.filterValue = filterValue;
  }
  public String getOrder() {
    return order;
  }
  public void setOrder(String order) {
    this.order = order;
  }
  public String getReloadBy() {
    return reloadBy;
  }
  public void setReloadBy(String reloadBy) {
    this.reloadBy = reloadBy;
  }
  
  public String toString() {
    StringBuffer rtBuf = new StringBuffer();
    rtBuf.append("{");
    rtBuf.append("cntrlId:" + cntrlId);
    rtBuf.append(",tableName:" + tableName);
    rtBuf.append(",codeField:" + codeField);
    rtBuf.append(",nameField:" + nameField);
    rtBuf.append(",value:" + value);
    rtBuf.append(",isMustFill:" + isMustFill);
    rtBuf.append(",filterField:" + filterField);
    rtBuf.append(",filterValue:" + filterValue);
    rtBuf.append(",order:" + order);
    rtBuf.append(",reloadBy:" + reloadBy);
    rtBuf.append("}");
    return rtBuf.toString();
  }
}
