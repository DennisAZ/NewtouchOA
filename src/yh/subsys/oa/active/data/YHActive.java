package yh.subsys.oa.active.data;

import java.util.Date;

public class YHActive {
  private int seqId;
  private String activeContent;
  private String activeUser;
  private Date activeTime;
  private String activeTimeRang;
  private String attachmentId;
  private String attachmentName;
  private String overStatus;
  private String opUserId;
  private Date opDatetime;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getActiveContent() {
    return activeContent;
  }
  public void setActiveContent(String activeContent) {
    this.activeContent = activeContent;
  }
  public String getActiveUser() {
    return activeUser;
  }
  public void setActiveUser(String activeUser) {
    this.activeUser = activeUser;
  }
  public Date getActiveTime() {
    return activeTime;
  }
  public void setActiveTime(Date activeTime) {
    this.activeTime = activeTime;
  }
  public String getActiveTimeRang() {
    return activeTimeRang;
  }
  public void setActiveTimeRang(String activeTimeRang) {
    this.activeTimeRang = activeTimeRang;
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
  public String getOverStatus() {
    return overStatus;
  }
  public void setOverStatus(String overStatus) {
    this.overStatus = overStatus;
  }
  public String getOpUserId() {
    return opUserId;
  }
  public void setOpUserId(String opUserId) {
    this.opUserId = opUserId;
  }
  public Date getOpDatetime() {
    return opDatetime;
  }
  public void setOpDatetime(Date opDatetime) {
    this.opDatetime = opDatetime;
  }
  
}
