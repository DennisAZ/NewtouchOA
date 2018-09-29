package yh.core.funcs.modulepriv.data;

public class YHModulePriv {
  private int seqId;
  private String  deptPriv;//人员范围
  private String rolePriv;//人员角色
  private String deptId;//指定部门1,2,3,
  private String privId;//指定角色
  private int userSeqId;//对应person表中的自增字段
  private String userId;//指定人员a,b,c,
  private int moduleId;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
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
  public int getUserSeqId() {
    return userSeqId;
  }
  public void setUserSeqId(int userSeqId) {
    this.userSeqId = userSeqId;
  }
  public int getModuleId() {
    return moduleId;
  }
  public void setModuleId(int moduleId) {
    this.moduleId = moduleId;
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
  
  public String toJSON(){
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:" + this.seqId);
    sb.append(",userSeqId:" + this.userSeqId);
    sb.append(",moduleId:" + this.moduleId);
    if(this.deptPriv != null){
      sb.append(",deptPriv:'" + this.deptPriv + "'");
    }else{
      sb.append(",deptPriv:''");
    }
    if(this.rolePriv != null){
      sb.append(",rolePriv:'" + this.rolePriv + "'");
    }else{
      sb.append(",rolePriv:''");
    }
    if(this.deptId != null){
      sb.append(",deptId:'" + this.deptId + "'");
    }else{
      sb.append(",deptId:''");
    }
    if(this.privId != null){
      sb.append(",privId:'" + this.privId + "'");
    }else{
      sb.append(",privId:''");
    }
    if(this.userId != null){
      sb.append(",userId:'" + this.userId + "'");
    }else{
      sb.append(",userId:''");
    }
    sb.append("}");
    return sb.toString();
  }
}
