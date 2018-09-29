package yh.pda.news.data;

import java.util.Date;

public class YHPdaNews {

  private int seqId;
  private String provider;
  private String subject;
  private Date newsTime;
  private String format;
  private String typeId;
  private String attachmentId;
  private String attachmentName;
  private String userName;
  private String classDesc;
  private String content;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public Date getNewsTime() {
    return newsTime;
  }
  public void setNewsTime(Date newsTime) {
    this.newsTime = newsTime;
  }
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
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
  public String getClassDesc() {
    return classDesc;
  }
  public void setClassDesc(String classDesc) {
    this.classDesc = classDesc;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  
}
