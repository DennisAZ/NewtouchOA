package yh.rad.velocity.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YHJavaMethod {

  private String methodName;
  private String returnType;
  private List<YHJavaParam> args = new ArrayList<YHJavaParam>();
  private YHJavaMethodBody methodBody;
  
  public YHJavaMethod() {
    // TODO Auto-generated constructor stub
  }
  private YHJavaMethod(String methodName, String returnType) {
    super();
    this.methodName = methodName;
    this.returnType = returnType;
  }
  public String getMethodName() {
    return methodName;
  }
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
  public String getReturnType() {
    return returnType;
  }
  public void setReturnType(String returnType) {
    this.returnType = returnType;
  }
  
  public List<YHJavaParam> getArgs() {
    return args;
  }
  public void setArgs(List<YHJavaParam> args) {
    this.args = args;
  }
  public YHJavaMethod addArgs(YHJavaParam arg) {
    this.args.add(arg);
    return this;
  }
  
  public static YHJavaMethod get(String methodName, String returnType) {
    return new YHJavaMethod(methodName, returnType);
  }
  
  public YHJavaMethodBody getMethodBody() {
    return methodBody;
  }
  
  public YHJavaMethod setMethodBody(YHJavaMethodBody methodBody) {
    this.methodBody = methodBody;
    return this;
  }
  @Override
  public String toString() {
    return "YHJavaMethod [args=" + args + ", methodBody=" + methodBody
        + ", methodName=" + methodName + ", returnType=" + returnType + "]";
  }
  
}
