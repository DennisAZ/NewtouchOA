package yh.core.funcs.system.wordmoudel.data;

import java.util.Date;

public class YHWordModel {
  private int seqId;
  private String modelName;
  private String sortId;
  private String privStr;
  private String privModule;
  private Date createTime;
  private int userId;
  private String attachmentId;
  private String attachmentName;
  private String modelDesc;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getModelName() {
    return modelName;
  }
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }
  public String getSortId() {
    return sortId;
  }
  public void setSortId(String sortId) {
    this.sortId = sortId;
  }
  public String getPrivStr() {
    return privStr;
  }
  public void setPrivStr(String privStr) {
    this.privStr = privStr;
  }
  public String getPrivModule() {
    return privModule;
  }
  public void setPrivModule(String privModule) {
    this.privModule = privModule;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
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
  public String getModelDesc() {
    return modelDesc;
  }
  public void setModelDesc(String modelDesc) {
    this.modelDesc = modelDesc;
  }
  @Override
  public String toString() {
    return "YHWordModel [attachmentId=" + attachmentId + ", attachmentName="
        + attachmentName + ", createTime=" + createTime + ", modelName="
        + modelName + ", moudelDesc=" + modelDesc + ", privModule="
        + privModule + ", privStr=" + privStr + ", seqId=" + seqId
        + ", sortId=" + sortId + ", userId=" + userId + "]";
  }
  

}
