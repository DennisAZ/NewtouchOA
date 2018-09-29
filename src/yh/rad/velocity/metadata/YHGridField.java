package yh.rad.velocity.metadata;

public class YHGridField{
  private String header;
  private String name;
  private String realName;
  private String hidden;
  private String with;
  
  private String fkTableName;
  private String fkRelaFieldName;
  private String fkNameFieldName;
  
  public String getHeader(){
    return header;
  }
  public void setHeader(String header){
    this.header = header;
  }
  public String getName(){
    return name;
  }
  public void setName(String name){
    this.name = name;
  }
  public String getHidden(){
    return hidden;
  }
  public void setHidden(String hidden){
    this.hidden = hidden;
  }
  public String getWith(){
    return with;
  }
  public void setWith(String with){
    this.with = with;
  }
  public String getRealName() {
    return realName;
  }
  public void setRealName(String realName) {
    this.realName = realName;
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
  @Override
  public String toString(){
    return "YHGridField [header=" + header + ", hidden=" + hidden + ", name=" + name + ", realName=" + realName + ", with=" + with + ", fkTableName=" + fkTableName + ", fkRelaFieldName=" + fkRelaFieldName + ", fkNameFieldName=" + fkNameFieldName + "]";
  }
  public YHGridField(String header, String name, String realName, String hidden, String with, String fkTableName, String fkRelaFieldName, String fkNameFieldName){
    super();
    this.header = header;
    this.name = name;
    this.realName = realName;
    this.hidden = hidden;
    this.with = with;
    this.fkTableName = fkTableName;
    this.fkRelaFieldName = fkRelaFieldName;
    this.fkNameFieldName = fkNameFieldName;
  }
  
  public YHGridField(String header, String name, String realName, String hidden, String with){
    super();
    this.header = header;
    this.name = name;
    this.realName = realName;
    this.hidden = hidden;
    this.with = with;
  }
}
