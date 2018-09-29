package yh.core.funcs.system.data;

public class YHSysFunction {
  private int seqId;
  private String menuId;
  private String icon;
  private String funcName;
  private String funcCode;
  private String openFlag;
  
  public String getOpenFlag() {
    return openFlag;
  }
  public void setOpenFlag(String openFlag) {
    this.openFlag = openFlag;
  }
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }
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
