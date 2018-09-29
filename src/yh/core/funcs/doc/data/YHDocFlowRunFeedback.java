//
package yh.core.funcs.doc.data;

import java.util.Date;

public class YHDocFlowRunFeedback {
  private int seqId;
  private int runId;
  private int prcsId;
  private int userId;
  private String content;
  private String attachmentId;
  private String attachmentName;
  private Date editTime;
  private int feedFlag;
  private int flowPrcs;
  public int getFlowPrcs() {
    return flowPrcs;
  }
  public void setFlowPrcs(int flowPrcs) {
    this.flowPrcs = flowPrcs;
  }
  public int getFeedFlag() {
    return feedFlag;
  }
  public void setFeedFlag(int feedFlag) {
    this.feedFlag = feedFlag;
  }
  private String signData;
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  
  public int getRunId() {
    return this.runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getPrcsId() {
    return this.prcsId;
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
  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getAttachmentId() {
    if ( this.attachmentId != null) {
      this.attachmentId = this.attachmentId.trim();
    }
    return this.attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    if (this.attachmentName != null) {
      this.attachmentName = this.attachmentName.trim();
    }
    return this.attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
  public Date getEditTime() {
    return this.editTime;
  }
  public void setEditTime(Date editTime) {
    this.editTime = editTime;
  }
  public String  getSignData() {
    if (signData != null) {
      signData = signData.trim();
    }
    return this.signData;
  }
  public void setSignData(String signData) {
    this.signData = signData;
  }
}
