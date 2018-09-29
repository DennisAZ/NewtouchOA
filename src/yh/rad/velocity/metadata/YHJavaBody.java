package yh.rad.velocity.metadata;

import java.util.ArrayList;
import java.util.List;

public class YHJavaBody {

  private String  className;
  private List<YHJavaField>  fields  = new ArrayList<YHJavaField>();
  private List<YHJavaMethod> methods = new ArrayList<YHJavaMethod>();

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public List<YHJavaField> getFields() {
    return fields;
  }

  public void setFields(List<YHJavaField> fields) {
    this.fields = fields;
  }

  public YHJavaBody addFields(YHJavaField field) {
    this.fields.add(field);
    return this;
  }

  public List<YHJavaMethod> getMethods() {
    return methods;
  }

  public void setMethods(List<YHJavaMethod> methods) {
    this.methods = methods;
  }

  public YHJavaBody addMethods(YHJavaMethod methods) {
    this.methods.add(methods);
    return this;
  }

  @Override
  public String toString() {
    return "YHJavaBody [className=" + className + ", fields=" + fields
        + ", methods=" + methods + "]";
  }

}
