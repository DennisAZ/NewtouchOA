package yh.subsys.oa.vehicle.data;

import java.util.Date;

public class YHVehicleUsage {
  private int seqId;//
  private String vId;//(车 牌 号：)
  private Date vuRequestDate ;//(维护日期)
  private String vuProposer;//（使用计划）
  private String vuUser;//(用车人)
  private Date vuStart;// (开始时间)
  private Date vuEnd;//(结束时间)
  private int vuMileage;//(里程)
  private String vuStatus;//(状态)
  private String vuDept ;//( 用车部门)
  private String vuDestination; //(目的地)
  private String vuDriver;//(司　　机)
  private String vuOperator;//(调 度 员)
  private String deptManager;//(部门审批人)
  private String operatorReason;//(调度理由 )
  private String deptReason;//(部门理由 )
  private String vuReason;//(事　　由)
  private String vuRemark;//( 备　　注)
  private String smsRemind;//(提醒调度员)
  private String sms2Remind;//
  private String showFlag;//
  private String dmerStatus;//
  private int vuMileageTrue;//实际里程
  private int vuParkingFees;//费用
  public String getvId() {
    return vId;
  }
  public void setvId(String vId) {
    this.vId = vId;
  }
  public String getDeptReason() {
    return deptReason;
  }
  public void setDeptReason(String deptReason) {
    this.deptReason = deptReason;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getVId() {
    return vId;
  }
  public void setVId(String VId) {
    this.vId = VId;
  }
  public Date getVuRequestDate() {
    return vuRequestDate;
  }
  public void setVuRequestDate(Date vuRequestDate) {
    this.vuRequestDate = vuRequestDate;
  }
  public String getVuProposer() {
    return vuProposer;
  }
  public void setVuProposer(String vuProposer) {
    this.vuProposer = vuProposer;
  }
  public String getVuUser() {
    return vuUser;
  }
  public void setVuUser(String vuUser) {
    this.vuUser = vuUser;
  }
  public Date getVuStart() {
    return vuStart;
  }
  public void setVuStart(Date vuStart) {
    this.vuStart = vuStart;
  }
  public Date getVuEnd() {
    return vuEnd;
  }
  public void setVuEnd(Date vuEnd) {
    this.vuEnd = vuEnd;
  }
  public int getVuMileage() {
    return vuMileage;
  }
  public void setVuMileage(int vuMileage) {
    this.vuMileage = vuMileage;
  }
  public String getVuStatus() {
    return vuStatus;
  }
  public void setVuStatus(String vuStatus) {
    this.vuStatus = vuStatus;
  }
  public String getVuDept() {
    return vuDept;
  }
  public void setVuDept(String vuDept) {
    this.vuDept = vuDept;
  }
  public String getVuDestination() {
    return vuDestination;
  }
  public void setVuDestination(String vuDestination) {
    this.vuDestination = vuDestination;
  }
  public String getVuDriver() {
    return vuDriver;
  }
  public void setVuDriver(String vuDriver) {
    this.vuDriver = vuDriver;
  }
  public String getVuOperator() {
    return vuOperator;
  }
  public void setVuOperator(String vuOperator) {
    this.vuOperator = vuOperator;
  }
  public String getDeptManager() {
    return deptManager;
  }
  public void setDeptManager(String deptManager) {
    this.deptManager = deptManager;
  }
  public String getOperatorReason() {
    return operatorReason;
  }
  public void setOperatorReason(String operatorReason) {
    this.operatorReason = operatorReason;
  }
  public String getVuReason() {
    return vuReason;
  }
  public void setVuReason(String vuReason) {
    this.vuReason = vuReason;
  }
  public String getVuRemark() {
    return vuRemark;
  }
  public void setVuRemark(String vuRemark) {
    this.vuRemark = vuRemark;
  }
  public String getSmsRemind() {
    return smsRemind;
  }
  public void setSmsRemind(String smsRemind) {
    this.smsRemind = smsRemind;
  }
  public String getSms2Remind() {
    return sms2Remind;
  }
  public void setSms2Remind(String sms2Remind) {
    this.sms2Remind = sms2Remind;
  }
  public String getShowFlag() {
    return showFlag;
  }
  public void setShowFlag(String showFlag) {
    this.showFlag = showFlag;
  }
  public String getDmerStatus() {
    return dmerStatus;
  }
  public void setDmerStatus(String dmerStatus) {
    this.dmerStatus = dmerStatus;
  }
  public int getVuMileageTrue() {
    return vuMileageTrue;
  }
  public void setVuMileageTrue(int vuMileageTrue) {
    this.vuMileageTrue = vuMileageTrue;
  }
  public int getVuParkingFees() {
    return vuParkingFees;
  }
  public void setVuParkingFees(int vuParkingFees) {
    this.vuParkingFees = vuParkingFees;
  }

}
