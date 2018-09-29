package yh.subsys.oa.hr.manage.reinstatement.data;

import java.util.Date;

public class YHHrStaffReinstatement {

  private int seqId;//   流水号
  private String createUserId;//     系统登录人seqid
  private int createDeptId;//   系统登录人部门id
  private Date reappointmentTimeFact;//   实际复职日期
  private String reappointmentType;//    复职类型
  private String reappointmentState;//   复职说明
  private String remark;//    备注
  private String reinstatementPerson;//   复职人员
  private Date reappointmentTimePlan;//   拟复职日期
  private String nowPosition;//    担任职务
  private String despachoPerson;//  批示人
  private String despacho;//    批示
  private Date applicationDate;//    申请日期
  private Date firstSalaryTime;//   工资恢复日期
  private String materialsCondition;//  复职手续办理
  private String reappointmentDept;//    复职部门
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
  public Date getReappointmentTimeFact() {
    return reappointmentTimeFact;
  }
  public void setReappointmentTimeFact(Date reappointmentTimeFact) {
    this.reappointmentTimeFact = reappointmentTimeFact;
  }
  public String getReappointmentType() {
    return reappointmentType;
  }
  public void setReappointmentType(String reappointmentType) {
    this.reappointmentType = reappointmentType;
  }
  public String getReappointmentState() {
    return reappointmentState;
  }
  public void setReappointmentState(String reappointmentState) {
    this.reappointmentState = reappointmentState;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getReinstatementPerson() {
    return reinstatementPerson;
  }
  public void setReinstatementPerson(String reinstatementPerson) {
    this.reinstatementPerson = reinstatementPerson;
  }
  public Date getReappointmentTimePlan() {
    return reappointmentTimePlan;
  }
  public void setReappointmentTimePlan(Date reappointmentTimePlan) {
    this.reappointmentTimePlan = reappointmentTimePlan;
  }
  public String getNowPosition() {
    return nowPosition;
  }
  public void setNowPosition(String nowPosition) {
    this.nowPosition = nowPosition;
  }
  public String getDespachoPerson() {
    return despachoPerson;
  }
  public void setDespachoPerson(String despachoPerson) {
    this.despachoPerson = despachoPerson;
  }
  public String getDespacho() {
    return despacho;
  }
  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }
  public Date getApplicationDate() {
    return applicationDate;
  }
  public void setApplicationDate(Date applicationDate) {
    this.applicationDate = applicationDate;
  }
  public Date getFirstSalaryTime() {
    return firstSalaryTime;
  }
  public void setFirstSalaryTime(Date firstSalaryTime) {
    this.firstSalaryTime = firstSalaryTime;
  }
  public String getMaterialsCondition() {
    return materialsCondition;
  }
  public void setMaterialsCondition(String materialsCondition) {
    this.materialsCondition = materialsCondition;
  }
  public String getReappointmentDept() {
    return reappointmentDept;
  }
  public void setReappointmentDept(String reappointmentDept) {
    this.reappointmentDept = reappointmentDept;
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
