package yh.core.codeclass.data;

public class YHCodeClass {
  private int sqlId;
  private String classNo;
  private String sortNo;
  private String classDesc;
  private String classLevel;
  
  public int getSqlId() {
    return sqlId;
  }
  public void setSqlId(int sqlId) {
    this.sqlId = sqlId;
  }
  public String getClassNo() {
    return classNo;
  }
  public void setClassNo(String classNo) {
    this.classNo = classNo;
  }
  public String getSortNo() {
    return sortNo;
  }
  public void setSortNo(String sortNo) {
    this.sortNo = sortNo;
  }
  public String getClassDesc() {
    return classDesc;
  }
  public void setClassDesc(String classDesc) {
    this.classDesc = classDesc;
  }
  public String getClassLevel() {
    return classLevel;
  }
  public void setClassLevel(String classLevel) {
    this.classLevel = classLevel;
  }
  @Override
  public String toString() {
    return "YHCodeClass [classDesc=" + classDesc + ", classLevel=" + classLevel
        + ", classNo=" + classNo + ", sortNo=" + sortNo + ", sqlId=" + sqlId
        + "]";
  }
}
