package yh.core.funcs.menu.data;

public class YHSysFunction {
  private int seqId;
  private String menuId;
  private String menuSort;
  private String menuAdd;
  private String funcName;
  private String funcCode;
  private String icon;
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
  public String getMenuSort() {
    return menuSort;
  }
  public void setMenuSort(String menuSort) {
    this.menuSort = menuSort;
  }
  public String getMenuAdd() {
    return menuAdd;
  }
  public void setMenuAdd(String menuAdd) {
    this.menuAdd = menuAdd;
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
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }
}
