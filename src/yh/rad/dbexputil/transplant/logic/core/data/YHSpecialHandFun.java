package yh.rad.dbexputil.transplant.logic.core.data;

import java.util.HashMap;
/**
 * 特殊处理的产生
 * @author Think
 *
 */
public class YHSpecialHandFun{
  private String tabName;
  private String columnName;
  private HashMap<String, String> params;
  private String refersType;
  
  public String getTabName() {
    return tabName;
  }
  public void setTabName(String tabName) {
    this.tabName = tabName;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public HashMap<String, String> getParams() {
    return params;
  }
  public void setParams(HashMap<String, String> params) {
    this.params = params;
  }
  public String getRefersType() {
    return refersType;
  }
  public void setRefersType(String refersType) {
    this.refersType = refersType;
  }
  @Override
  public String toString() {
    return "YHSpecialHandFun [columnName=" + columnName + ", params=" + params
        + ", refersType=" + refersType + ", tabName=" + tabName + "]";
  }
  
}