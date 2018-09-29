package yh.rad.velocity.metadata;

public class YHJavaParam {

  private String paramType;
  private String paramName;
  public YHJavaParam() {
    // TODO Auto-generated constructor stub
  }
  private YHJavaParam(String paramType,String paramName) {
    this.paramName = paramName;
    this.paramType = paramType;
  }
  public String getParamType() {
    return paramType;
  }
  public void setParamType(String paramType) {
    this.paramType = paramType;
  }
  public String getParamName() {
    return paramName;
  }
  public void setParamName(String paramName) {
    this.paramName = paramName;
  }
  public static YHJavaParam addParam(String paramType,String paramName) {
    return new YHJavaParam(paramType, paramName);
  }
  @Override
  public String toString() {
    return "YHJavaParam [paramName=" + paramName + ", paramType=" + paramType
        + "]";
  }
  
}
