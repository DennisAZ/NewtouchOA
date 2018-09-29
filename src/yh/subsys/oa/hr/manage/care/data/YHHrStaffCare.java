package yh.subsys.oa.hr.manage.care.data;

import java.util.Date;

public class YHHrStaffCare {

  private int seqId;//   流水号
  private String createUserId;//    系统登录人seqid
  private int createDeptId;//   系统登录人部门id
  private String byCareStaffs;//    被关怀员工
  private Date careDate;//   关怀日期
  private String careContent;//    关怀内容
  private String participants;//    参与人
  private String careEffects;//    关怀效果
  private double careFees;//   关怀开支费用
  private String careType;//   关怀类型
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
  public String getByCareStaffs() {
    return byCareStaffs;
  }
  public void setByCareStaffs(String byCareStaffs) {
    this.byCareStaffs = byCareStaffs;
  }
  public Date getCareDate() {
    return careDate;
  }
  public void setCareDate(Date careDate) {
    this.careDate = careDate;
  }
  public String getCareContent() {
    return careContent;
  }
  public void setCareContent(String careContent) {
    this.careContent = careContent;
  }
  public String getParticipants() {
    return participants;
  }
  public void setParticipants(String participants) {
    this.participants = participants;
  }
  public String getCareEffects() {
    return careEffects;
  }
  public void setCareEffects(String careEffects) {
    this.careEffects = careEffects;
  }
  public double getCareFees() {
    return careFees;
  }
  public void setCareFees(double careFees) {
    this.careFees = careFees;
  }
  public String getCareType() {
    return careType;
  }
  public void setCareType(String careType) {
    this.careType = careType;
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
