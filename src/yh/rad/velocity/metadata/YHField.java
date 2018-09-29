package yh.rad.velocity.metadata;

public class YHField{
  private String  fieldName;
  private boolean isMust;
  private String fieldTitle;
  private String domType;
  private boolean hidden;
  private String fkTableName;
  private String fkRelaFieldName;
  private String fkNameFieldName;
  private String fkTableNo;
  private String fkTableName2;
  private String fkFilterName;
  private String codeClass;
  private String fkNameFieldName2;
  public boolean isHidden() {
    return hidden;
  }
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  public String getDomType(){
    return domType;
  }
  public void setDomType(String domType){
    this.domType = domType;
  }
  public String getFieldTitle(){
    return fieldTitle;
  }
  public void setFieldTitle(String fieldTitle){
    this.fieldTitle = fieldTitle;
  }
  public String getFieldName(){
    return fieldName;
  }
  public void setFieldName(String fieldName){
    this.fieldName = fieldName;
  }
  public boolean isMust(){
    return isMust;
  }
  public void setMust(boolean isMust){
    this.isMust = isMust;
  }
  public String getFkTableName() {
    return fkTableName;
  }
  public void setFkTableName(String fkTableName) {
    this.fkTableName = fkTableName;
  }
  public String getFkRelaFieldName() {
    return fkRelaFieldName;
  }
  public void setFkRelaFieldName(String fkRelaFieldName) {
    this.fkRelaFieldName = fkRelaFieldName;
  }
  public String getFkNameFieldName() {
    return fkNameFieldName;
  }
  public void setFkNameFieldName(String fkNameFieldName) {
    this.fkNameFieldName = fkNameFieldName;
  }
  public String getFkTableNo() {
    return fkTableNo;
  }
  public void setFkTableNo(String fkTableNo) {
    this.fkTableNo = fkTableNo;
  }
  public String getFkTableName2() {
    return fkTableName2;
  }
  public void setFkTableName2(String fkTableName2) {
    this.fkTableName2 = fkTableName2;
  }
  public String getFkFilterName() {
    return fkFilterName;
  }
  public void setFkFilterName(String fkFilterName) {
    this.fkFilterName = fkFilterName;
  }
  public String getCodeClass() {
    return codeClass;
  }
  public void setCodeClass(String codeClass) {
    this.codeClass = codeClass;
  }
  public String getFkNameFieldName2() {
    return fkNameFieldName2;
  }
  public void setFkNameFieldName2(String fkNameFieldName2) {
    this.fkNameFieldName2 = fkNameFieldName2;
  }
  @Override
  public String toString(){
    return "YHField [domType=" + domType + ", fieldName=" + fieldName
        + ", fieldTitle=" + fieldTitle + ", isMust=" + isMust + ", fkTableName=" + fkTableName 
        + ", fkRelaFieldName=" + fkRelaFieldName + ", fkNameFieldName=" + fkNameFieldName + ", fkTableNo=" + fkTableNo + "]";
  }
  
  public YHField(String fieldName, String fieldTitle,boolean isMust,String domType){
    super();
    this.fieldName = fieldName;
    this.isMust = isMust;
    this.fieldTitle = fieldTitle;
    this.domType = domType; 
  }
  
  public YHField(String fieldName, String fieldTitle, boolean isMust, String domType
      , String fkTableName, String fkRelaFieldName, String fkNameFieldName, String fkTableNo
      , String fkTableName2, String fkFilterName, String codeClass, String fkNameFieldName2){
    super();
    this.fieldName = fieldName;
    this.isMust = isMust;
    this.fieldTitle = fieldTitle;
    this.domType = domType; 
    this.fkTableName = fkTableName; 
    this.fkRelaFieldName = fkRelaFieldName; 
    this.fkNameFieldName = fkNameFieldName; 
    this.fkTableNo = fkTableNo; 
    this.fkTableName2 = fkTableName2;
    this.fkFilterName = fkFilterName;
    this.codeClass = codeClass;
    this.fkNameFieldName2 = fkNameFieldName2;
  }
  public YHField(){
    // TODO Auto-generated constructor stub
  }
}
