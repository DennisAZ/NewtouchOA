package yh.core.funcs.diary.logic;

public class YHMyPriv {
  
  private String deptPriv ;
  private String rolePriv;
  private String deptId;
  private String privId;
  private String userId;
  private int privNo;
  public String getDeptPriv() {
    return deptPriv;
  }
  public void setDeptPriv(String deptPriv) {
    this.deptPriv = deptPriv;
  }
  public String getRolePriv() {
    return rolePriv;
  }
  public void setRolePriv(String rolePriv) {
    this.rolePriv = rolePriv;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getPrivId() {
    return privId;
  }
  public void setPrivId(String privId) {
    this.privId = privId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public int getPrivNo() {
    return privNo;
  }
  public void setPrivNo(int privNo) {
    this.privNo = privNo;
  }
  @Override
  public String toString() {
    return "YHMyPriv [deptId=" + deptId + ", deptPriv=" + deptPriv
        + ", privId=" + privId + ", privNo=" + privNo + ", rolePriv="
        + rolePriv + ", userId=" + userId + "]";
  }
  
}
