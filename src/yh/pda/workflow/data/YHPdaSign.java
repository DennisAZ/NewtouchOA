package yh.pda.workflow.data;

import java.util.Date;

public class YHPdaSign {

  private int seqId;
  private int prcsId;
  private int userId;
  private String content;
  private String attachmentId;
  private String attachmentName;
  private Date editTime;
  private String signData;
  private String userName;
  private String deptName;
  private int flowPrcs;
  private String prcsName;
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
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
  public String getSignData() {
    return signData;
  }
  public void setSignData(String signData) {
    this.signData = signData;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getPrcsId() {
    return prcsId;
  }
  public void setPrcsId(int prcsId) {
    this.prcsId = prcsId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public Date getEditTime() {
    return editTime;
  }
  public void setEditTime(Date editTime) {
    this.editTime = editTime;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getDeptName() {
    return deptName;
  }
  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }
  public int getFlowPrcs() {
    return flowPrcs;
  }
  public void setFlowPrcs(int flowPrcs) {
    this.flowPrcs = flowPrcs;
  }
  public String getPrcsName() {
    return prcsName;
  }
  public void setPrcsName(String prcsName) {
    this.prcsName = prcsName;
  }
  
}
