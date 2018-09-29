//

package yh.core.funcs.doc.data;

import java.util.Date;

public class YHDocFlowRunPrcs {
  private int runId;
  private int prcsId;
  private int userId;
  private Date prcsTime;
  private Date deliverTime;
  private String prcsFlag;
  private int flowPrcs;
  private String opFlag;
  private String topFlag;
  private String delFlag;
  private String parent;
  private int childRun;
  private String timeOut;
  private String freeItem;
  private Date createTime;
  private int otherUser = 0;
  public int getOtherUser() {
    return otherUser;
  }
  public void setOtherUser(int otherUser) {
    this.otherUser = otherUser;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  private int seqId;
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
  public Date getPrcsTime() {
    return this.prcsTime;
  }
  public void setPrcsTime(Date prcsTime) {
    this.prcsTime = prcsTime;
  }
  public Date getDeliverTime() {
    return this.deliverTime;
  }
  public void setDeliverTime(Date deliverTime) {
    this.deliverTime = deliverTime;
  }
  public String getPrcsFlag() {
    return this.prcsFlag;
  }
  public void setPrcsFlag(String prcsFlag) {
    this.prcsFlag = prcsFlag;
  }
  public int getFlowPrcs() {
    return this.flowPrcs;
  }
  public void setFlowPrcs(int flowPrcs) {
    this.flowPrcs = flowPrcs;
  }
  public String getOpFlag() {
    return this.opFlag;
  }
  public void setOpFlag(String opFlag) {
    this.opFlag = opFlag;
  }
  public String getTopFlag() {
    return this.topFlag;
  }
  public void setTopFlag(String topFlag) {
    this.topFlag = topFlag;
  }
  public String getDelFlag() {
    return this.delFlag;
  }
  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
  public String getParent() {
    return this.parent;
  }
  public void setParent(String parent) {
    this.parent = parent;
  }
  public int getChildRun() {
    return this.childRun;
  }
  public void setChildRun(int childRun) {
    this.childRun = childRun;
  }
  public String getTimeOut() {
    return this.timeOut;
  }
  public void setTimeOut(String timeOut) {
    this.timeOut = timeOut;
  }
  public String getFreeItem() {
    return this.freeItem;
  }
  public void setFreeItem(String freeItem) {
    this.freeItem = freeItem;
  }

  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
}
