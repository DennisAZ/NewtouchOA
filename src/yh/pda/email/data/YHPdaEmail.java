package yh.pda.email.data;

import java.util.Date;

public class YHPdaEmail {

  private int seqId;
  private int fromId;
  private String subject;
  private Date sendTime;
  private String important;
  private String attachmentId;
  private String attachmentName;
  private String userName;
  private String content;
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getImportant() {
    return important;
  }
  public void setImportant(String important) {
    this.important = important;
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
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getFromId() {
    return fromId;
  }
  public void setFromId(int fromId) {
    this.fromId = fromId;
  }
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  
}
