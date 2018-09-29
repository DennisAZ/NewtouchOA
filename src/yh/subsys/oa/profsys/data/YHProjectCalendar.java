package yh.subsys.oa.profsys.data;

import java.util.Date;

public class YHProjectCalendar {
  private int seqId;//SEQ_ID  int 流水号
  private int projId;//PROJ_ID int 来访项目ID
  private String userId;//USER_ID VARCHAR(20) 用户ID
  private Date startTime;//START_TIME  Date  起始时间
  private Date endTime;//END_TIME  DATE  结束时间
  private String activeType;//ACTIVE_TYPE varchar(20)  活动类别
  private String activeLeader;//ACTIVE_LEADER  varchar(200)  活动负责人
  private String activePartner;//ACTIVE_PARTNER  clob  活动参与人
  private String activeContent;//ACTIVE_CONTENT  CLOB  活动内容
  private String overStatus;//OVER_STATUS VARCHAR(20) 状态
  private String attachmentId;//ATTACHMENT_ID date  附件ID
  private String attachmentName;//ATTACHMENT_NAME varchar(200)  附件名称
  private String projCalendarType;//PROJ_CALENDAR_TYPE VARCHAR(2)  项目日程类别  0：来访项目1：出访项目2：大型活动项目
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getProjId() {
    return projId;
  }
  public void setProjId(int projId) {
    this.projId = projId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Date getStartTime() {
    return startTime;
  }
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getActiveType() {
    return activeType;
  }
  public void setActiveType(String activeType) {
    this.activeType = activeType;
  }
  public String getActiveLeader() {
    return activeLeader;
  }
  public void setActiveLeader(String activeLeader) {
    this.activeLeader = activeLeader;
  }
  public String getActivePartner() {
    return activePartner;
  }
  public void setActivePartner(String activePartner) {
    this.activePartner = activePartner;
  }
  public String getActiveContent() {
    return activeContent;
  }
  public void setActiveContent(String activeContent) {
    this.activeContent = activeContent;
  }

  public String getOverStatus() {
    return overStatus;
  }
  public void setOverStatus(String overStatus) {
    this.overStatus = overStatus;
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
  public String getProjCalendarType() {
    return projCalendarType;
  }
  public void setProjCalendarType(String projCalendarType) {
    this.projCalendarType = projCalendarType;
  }

}
