package yh.core.menumodel.data;

public class YHSysFunction {
private int seqId;
private String menuId;
private String funcName;
private String funcCode;
public int getSeqId() {
  return seqId;
}
public void setSeqId(int seqId) {
  this.seqId = seqId;
}
public String getMenuId() {
  return menuId;
}
public void setMenuId(String menuId) {
  this.menuId = menuId;
}
public String getFuncName() {
  return funcName;
}
public void setFuncName(String funcName) {
  this.funcName = funcName;
}
public String getFuncCode() {
  return funcCode;
}
public void setFuncCode(String funcCode) {
  this.funcCode = funcCode;
}
public String toString() {
  return menuId;
}
}
