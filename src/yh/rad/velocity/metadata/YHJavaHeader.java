package yh.rad.velocity.metadata;

import java.util.ArrayList;
import java.util.List;

public class YHJavaHeader {

  private String packageName;
  private List<String> importNames = new ArrayList<String>();
  
  public void setImportNames(List<String> importNames) {
    this.importNames = importNames;
  }
  public List<String> getImportNames() {
    return importNames;
  }
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }
  public String getPackageName() {
    return packageName;
  }
  public void addImportNames(String importName) {
    this.importNames.add(importName);
  }
  @Override
  public String toString() {
    return "YHJavaHeader [importNames=" + importNames + ", packageName="
        + packageName + "]";
  }
}
