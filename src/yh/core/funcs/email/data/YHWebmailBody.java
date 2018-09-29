package yh.core.funcs.email.data;

import java.util.Date;

public class YHWebmailBody {
 
  private int seqId;
  private int bodyId;
  private String fromMail;
  private String replyMail;
  private String toMail;
  private String toMailCopy;
  private String toMailSecret;
  public String getToMailCopy() {
    return toMailCopy;
  }

  public void setToMailCopy(String toMailCopy) {
    this.toMailCopy = toMailCopy;
  }

  public String getToMailSecret() {
    return toMailSecret;
  }

  public void setToMailSecret(String toMailSecret) {
    this.toMailSecret = toMailSecret;
  }
  private String ccMail;
  private String subject;
  private String isHtml;
  private String largeAttachment;
  private String attachmentId;
  private String attachmentName;
  private String contentHtml;
  private String webmailUid;
  private Date sendDate;
  
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public YHWebmailBody() {
    // TODO Auto-generated constructor stub
  }
  
  public YHWebmailBody(String fromMail, String replyMail,
      String toMail, String ccMail, String subject, String isHtml,
      String largeAttachment, String attachmentId, String attachmentName,
      String contentHtml, String webmailUid) {
    super();
    this.fromMail = fromMail;
    this.replyMail = replyMail;
    this.toMail = toMail;
    this.ccMail = ccMail;
    this.subject = subject;
    this.isHtml = isHtml;
    this.largeAttachment = largeAttachment;
    this.attachmentId = attachmentId;
    this.attachmentName = attachmentName;
    this.contentHtml = contentHtml;
    this.webmailUid = webmailUid;
  }
  public int getSeqId() {
    return seqId;
  }

  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getBodyId() {
    return bodyId;
  }
  public void setBodyId(int bodyId) {
    this.bodyId = bodyId;
  }
  public String getFromMail() {
    return fromMail;
  }
  public void setFromMail(String fromMail) {
    this.fromMail = fromMail;
  }
  public String getReplyMail() {
    return replyMail;
  }
  public void setReplyMail(String replyMail) {
    this.replyMail = replyMail;
  }
  public String getToMail() {
    return toMail;
  }
  public void setToMail(String toMail) {
    this.toMail = toMail;
  }
  public String getCcMail() {
    return ccMail;
  }
  public void setCcMail(String ccMail) {
    this.ccMail = ccMail;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getIsHtml() {
    return isHtml;
  }
  public void setIsHtml(String isHtml) {
    this.isHtml = isHtml;
  }
  public String getLargeAttachment() {
    return largeAttachment;
  }
  public void setLargeAttachment(String largeAttachment) {
    this.largeAttachment = largeAttachment;
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
  public String getContentHtml() {
    return contentHtml;
  }
  public void setContentHtml(String contentHtml) {
    this.contentHtml = contentHtml;
  }
  public String getWebmailUid() {
    return webmailUid;
  }
  public void setWebmailUid(String webmailUid) {
    this.webmailUid = webmailUid;
  }
  @Override
  public String toString() {
    return "YHWebmailBody [attachmentId=" + attachmentId + ", attachmentName="
        + attachmentName + ", bodyId=" + bodyId + ", ccMail=" + ccMail
        + ", contentHtml=" + contentHtml + ", fromMail=" + fromMail
        + ", isHtml=" + isHtml + ", largeAttachment=" + largeAttachment
        + ", replyMail=" + replyMail + ", subject=" + subject + ", toMail="
        + toMail + ", webmailUid=" + webmailUid + "]";
  }
}
