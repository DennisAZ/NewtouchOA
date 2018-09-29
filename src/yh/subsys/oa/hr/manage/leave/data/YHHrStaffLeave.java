package yh.subsys.oa.hr.manage.leave.data;

import java.util.Date;

public class YHHrStaffLeave {

  private int seqId;          //   流水号
  private String createUserId;//     系统登录人seqid
  private int createDeptId;//   系统登录人部门id
  private Date quitTimePlan;//    拟离职日期
  private String quitType;//   离职类型
  private String quitReason;//   离职原因
  private Date lastSalaryTime;//    工资截止日期
  private String trace;//   去向
  private String remark;//    备注
  private Date quitTimeFact;//    实际离职日期
  private String leavePerson;//    离职人员
  private String materialsCondition;//   离职手续办理
  private String despacho;//    批示
  private String despachoPerson;//   批示人id
  private String position;//    担任职务
  private Date applicationDate;//    申请日期
  private String leaveDept;//    离职部门
  private String attachmentId;//   附件id
  private String attachmentName;//   附件名称
  private Date addTime;//    系统当前时间
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCreateUserId() {
    return createUserId;
  }
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }
  public int getCreateDeptId() {
    return createDeptId;
  }
  public void setCreateDeptId(int createDeptId) {
    this.createDeptId = createDeptId;
  }
  public Date getQuitTimePlan() {
    return quitTimePlan;
  }
  public void setQuitTimePlan(Date quitTimePlan) {
    this.quitTimePlan = quitTimePlan;
  }
  public String getQuitType() {
    return quitType;
  }
  public void setQuitType(String quitType) {
    this.quitType = quitType;
  }
  public String getQuitReason() {
    return quitReason;
  }
  public void setQuitReason(String quitReason) {
    this.quitReason = quitReason;
  }
  public Date getLastSalaryTime() {
    return lastSalaryTime;
  }
  public void setLastSalaryTime(Date lastSalaryTime) {
    this.lastSalaryTime = lastSalaryTime;
  }
  public String getTrace() {
    return trace;
  }
  public void setTrace(String trace) {
    this.trace = trace;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public Date getQuitTimeFact() {
    return quitTimeFact;
  }
  public void setQuitTimeFact(Date quitTimeFact) {
    this.quitTimeFact = quitTimeFact;
  }
  public String getLeavePerson() {
    return leavePerson;
  }
  public void setLeavePerson(String leavePerson) {
    this.leavePerson = leavePerson;
  }
  public String getMaterialsCondition() {
    return materialsCondition;
  }
  public void setMaterialsCondition(String materialsCondition) {
    this.materialsCondition = materialsCondition;
  }
  public String getDespacho() {
    return despacho;
  }
  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }
  public String getDespachoPerson() {
    return despachoPerson;
  }
  public void setDespachoPerson(String despachoPerson) {
    this.despachoPerson = despachoPerson;
  }
  public String getPosition() {
    return position;
  }
  public void setPosition(String position) {
    this.position = position;
  }
  public Date getApplicationDate() {
    return applicationDate;
  }
  public void setApplicationDate(Date applicationDate) {
    this.applicationDate = applicationDate;
  }
  public String getLeaveDept() {
    return leaveDept;
  }
  public void setLeaveDept(String leaveDept) {
    this.leaveDept = leaveDept;
  }
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }


}
