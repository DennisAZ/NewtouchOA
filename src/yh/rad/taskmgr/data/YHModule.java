package yh.rad.taskmgr.data;

public class YHModule {
  private String parentPath = null;
  private String entryDir = null;
  private String entryDesc = null;
  private String sortNo = null;
  private String isDetl = null;

  public String getParentPath() {
    return parentPath;
  }
  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }
  public String getEntryDir() {
    return entryDir;
  }
  public void setEntryDir(String entryDir) {
    this.entryDir = entryDir;
  }
  public String getEntryDesc() {
    return entryDesc;
  }
  public void setEntryDesc(String entryDesc) {
    this.entryDesc = entryDesc;
  }
  public String getIsDetl() {
    return isDetl;
  }
  public void setIsDetl(String isDetl) {
    this.isDetl = isDetl;
  }
  public String getSortNo() {
    return sortNo;
  }
  public void setSortNo(String sortNo) {
    this.sortNo = sortNo;
  }
}
