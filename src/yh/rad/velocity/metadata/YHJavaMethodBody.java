package yh.rad.velocity.metadata;

import java.util.HashMap;
import java.util.Map;

public class YHJavaMethodBody {

  private String bodyName ;
  private Map<String, Object> bodyArg = new HashMap<String, Object>();
  public String getBodyName() {
    return bodyName;
  }
  public void setBodyName(String bodyName) {
    this.bodyName = bodyName;
  }
  public Map<String, Object> getBodyArg() {
    return bodyArg;
  }
  public void setBodyArg(Map<String, Object> bodyArg) {
    this.bodyArg = bodyArg;
  }
  @Override
  public String toString() {
    return "YHJavaMethodBody [bodyArg=" + bodyArg + ", bodyName=" + bodyName
        + "]";
  }
  
  private YHJavaMethodBody(String bodyName,String key,Object value) {
    this.bodyName = bodyName;
    this.bodyArg.put(key, value);
  }
  
  public YHJavaMethodBody put(String key, Object value) {
    this.bodyArg.put(key, value);
    return this;
  }
  
  public YHJavaMethodBody() {
    // TODO Auto-generated constructor stub
  }
  
  private YHJavaMethodBody(String bodyName) {
    this.bodyName = bodyName;
  }
  
  public static YHJavaMethodBody get(String bodyName, String key, Object value) {
    return new YHJavaMethodBody(bodyName, key, value);
  } 
  
  public static YHJavaMethodBody get(String bodyName) {
    return new YHJavaMethodBody(bodyName);
  } 
}
