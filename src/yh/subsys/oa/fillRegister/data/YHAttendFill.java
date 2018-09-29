package yh.subsys.oa.fillRegister.data;

import java.util.Date;

public class YHAttendFill {
  private int seqId; 
  private Date fillTime;
  private String registerType;
  private String attendFlag;
  private String assessingStatus;
  private String proposer;
  private String assessingOfficer;
  private Date assessingTime;
  private String assessingView;
  private String remark;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public Date getFillTime() {
    return fillTime;
  }
  public void setFillTime(Date fillTime) {
    this.fillTime = fillTime;
  }
  public String getRegisterType() {
    return registerType;
  }
  public void setRegisterType(String registerType) {
    this.registerType = registerType;
  }
  public String getAttendFlag() {
    return attendFlag;
  }
  public void setAttendFlag(String attendFlag) {
    this.attendFlag = attendFlag;
  }
  public String getAssessingStatus() {
    return assessingStatus;
  }
  public void setAssessingStatus(String assessingStatus) {
    this.assessingStatus = assessingStatus;
  }
  public String getProposer() {
    return proposer;
  }
  public void setProposer(String proposer) {
    this.proposer = proposer;
  }
  public String getAssessingOfficer() {
    return assessingOfficer;
  }
  public void setAssessingOfficer(String assessingOfficer) {
    this.assessingOfficer = assessingOfficer;
  }
  public Date getAssessingTime() {
    return assessingTime;
  }
  public void setAssessingTime(Date assessingTime) {
    this.assessingTime = assessingTime;
  }
  public String getAssessingView() {
    return assessingView;
  }
  public void setAssessingView(String assessingView) {
    this.assessingView = assessingView;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
}
