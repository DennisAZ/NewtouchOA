package yh.cms.template.data;

import java.util.Date;

public class YHCmsTemplate {

  private int seqId;
  private String templateName;
  private String templateFileName;
  private int templateType;
  private int createId;
  private Date createTime;
  private String attachmentId;
  private String attachmentName;
  private int stationId;
  public int getCreateId() {
    return createId;
  }
  public void setCreateId(int createId) {
    this.createId = createId;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getTemplateName() {
    return templateName;
  }
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }
  public String getTemplateFileName() {
    return templateFileName;
  }
  public void setTemplateFileName(String templateFileName) {
    this.templateFileName = templateFileName;
  }
  public int getTemplateType() {
    return templateType;
  }
  public void setTemplateType(int templateType) {
    this.templateType = templateType;
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
  public int getStationId() {
    return stationId;
  }
  public void setStationId(int stationId) {
    this.stationId = stationId;
  }
}
