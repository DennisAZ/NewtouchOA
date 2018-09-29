package yh.rad.velocity.metadata;

public class YHSelectField{
  private String cntrlId;
  private String tableName;
  private String codeField;
  private String nameField;
  private String isMustFill;
  private String filterField;
  private String filterValue;
  private String order;
  private String reloadBy;
  private String actionUrl;
  @Override
  public String toString(){
    return "YHSelectField [actionUrl=" + actionUrl + ", cntrlId=" + cntrlId
        + ", codeField=" + codeField + ", filterField=" + filterField
        + ", filterValue=" + filterValue + ", isMustFill=" + isMustFill
        + ", nameField=" + nameField + ", order=" + order + ", reloadBy="
        + reloadBy + ", tableName=" + tableName + "]";
  }
  public String getCntrlId(){
    return cntrlId;
  }
  public void setCntrlId(String cntrlId){
    this.cntrlId = cntrlId;
  }
  public String getTableName(){
    return tableName;
  }
  public void setTableName(String tableName){
    this.tableName = tableName;
  }
  public String getCodeField(){
    return codeField;
  }
  public void setCodeField(String codeField){
    this.codeField = codeField;
  }
  public String getNameField(){
    return nameField;
  }
  public void setNameField(String nameField){
    this.nameField = nameField;
  }
  public String getIsMustFill(){
    return isMustFill;
  }
  public void setIsMustFill(String isMustFill){
    this.isMustFill = isMustFill;
  }
  public String getFilterField(){
    return filterField;
  }
  public void setFilterField(String filterField){
    this.filterField = filterField;
  }
  public String getFilterValue(){
    return filterValue;
  }
  public void setFilterValue(String filterValue){
    this.filterValue = filterValue;
  }
  public String getOrder(){
    return order;
  }
  public void setOrder(String order){
    this.order = order;
  }
  public String getReloadBy(){
    return reloadBy;
  }
  public void setReloadBy(String reloadBy){
    this.reloadBy = reloadBy;
  }
  public String getActionUrl(){
    return actionUrl;
  }
  public void setActionUrl(String actionUrl){
    this.actionUrl = actionUrl;
  }
}
