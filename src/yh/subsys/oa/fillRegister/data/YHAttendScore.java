package yh.subsys.oa.fillRegister.data;

import java.util.Date;

public class YHAttendScore {
  private int seqId; 
  private Date createTime;
  private String userId;
  private Date dutyTime;
  private String dutyType;
  private String registerType;
  private double score;
  private double addScore;
  private String assessingOfficer;
  private Date assessingTime;
  private String assessingView;
  private String assessingStatus;
  private String attendFlag;
  private String remark;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Date getDutyTime() {
    return dutyTime;
  }
  public void setDutyTime(Date dutyTime) {
    this.dutyTime = dutyTime;
  }
  public String getDutyType() {
    return dutyType;
  }
  public void setDutyType(String dutyType) {
    this.dutyType = dutyType;
  }
  public String getRegisterType() {
    return registerType;
  }
  public void setRegisterType(String registerType) {
    this.registerType = registerType;
  }
  
  public double getScore() {
    return score;
  }
  public void setScore(double score) {
    this.score = score;
  }
  public double getAddScore() {
    return addScore;
  }
  public void setAddScore(double addScore) {
    this.addScore = addScore;
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
  public String getAssessingStatus() {
    return assessingStatus;
  }
  public void setAssessingStatus(String assessingStatus) {
    this.assessingStatus = assessingStatus;
  }
  public String getAttendFlag() {
    return attendFlag;
  }
  public void setAttendFlag(String attendFlag) {
    this.attendFlag = attendFlag;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
