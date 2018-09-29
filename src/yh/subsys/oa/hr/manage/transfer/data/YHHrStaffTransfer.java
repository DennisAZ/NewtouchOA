package yh.subsys.oa.hr.manage.transfer.data;

import java.util.Date;

public class YHHrStaffTransfer {

  private int seqId;//  流水号
  private String createUserId;//    系统登录人seqid
  private int createDeptId;//   系统登录人部门id
  private String transferPerson;//   调动人员
  private Date transferDate;//   调动日期
  private Date transferEffectiveDate;//   调动生效日期
  private String transferType;//   调动类型
  private String tranCompanyBefore;//   调动前单位
  private String tranDeptBefore;//    调动前部门
  private String tranPositionBefore;//    调动前职务
  private String tranCompanyAfter;//    调动后单位
  private String tranDeptAfter;//   调动后部门
  private String tranPositionAfter;//   调动后职务
  private String tranReason;//   调动原因
  private String responsiblePerson;//    经办人
  private String despacho;//    批示
  private String materialsCondition;//   调动手续办理
  private String attachmentId;//   附件id
  private String attachmentName;//   附件名称
  private String remark;//    备注
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
  public String getTransferPerson() {
    return transferPerson;
  }
  public void setTransferPerson(String transferPerson) {
    this.transferPerson = transferPerson;
  }
  public Date getTransferDate() {
    return transferDate;
  }
  public void setTransferDate(Date transferDate) {
    this.transferDate = transferDate;
  }
  public Date getTransferEffectiveDate() {
    return transferEffectiveDate;
  }
  public void setTransferEffectiveDate(Date transferEffectiveDate) {
    this.transferEffectiveDate = transferEffectiveDate;
  }
  public String getTransferType() {
    return transferType;
  }
  public void setTransferType(String transferType) {
    this.transferType = transferType;
  }
  public String getTranCompanyBefore() {
    return tranCompanyBefore;
  }
  public void setTranCompanyBefore(String tranCompanyBefore) {
    this.tranCompanyBefore = tranCompanyBefore;
  }
  public String getTranDeptBefore() {
    return tranDeptBefore;
  }
  public void setTranDeptBefore(String tranDeptBefore) {
    this.tranDeptBefore = tranDeptBefore;
  }
  public String getTranPositionBefore() {
    return tranPositionBefore;
  }
  public void setTranPositionBefore(String tranPositionBefore) {
    this.tranPositionBefore = tranPositionBefore;
  }
  public String getTranCompanyAfter() {
    return tranCompanyAfter;
  }
  public void setTranCompanyAfter(String tranCompanyAfter) {
    this.tranCompanyAfter = tranCompanyAfter;
  }
  public String getTranDeptAfter() {
    return tranDeptAfter;
  }
  public void setTranDeptAfter(String tranDeptAfter) {
    this.tranDeptAfter = tranDeptAfter;
  }
  public String getTranPositionAfter() {
    return tranPositionAfter;
  }
  public void setTranPositionAfter(String tranPositionAfter) {
    this.tranPositionAfter = tranPositionAfter;
  }
  public String getTranReason() {
    return tranReason;
  }
  public void setTranReason(String tranReason) {
    this.tranReason = tranReason;
  }
  public String getResponsiblePerson() {
    return responsiblePerson;
  }
  public void setResponsiblePerson(String responsiblePerson) {
    this.responsiblePerson = responsiblePerson;
  }
  public String getDespacho() {
    return despacho;
  }
  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }
  public String getMaterialsCondition() {
    return materialsCondition;
  }
  public void setMaterialsCondition(String materialsCondition) {
    this.materialsCondition = materialsCondition;
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
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

}
