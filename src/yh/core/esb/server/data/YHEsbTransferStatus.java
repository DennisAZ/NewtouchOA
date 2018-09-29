package yh.core.esb.server.data;

import java.util.Date;

public class YHEsbTransferStatus {
  private int seqId;
  private int toId;
  private String transId;
  private String status;
  private Date createTime;
  private Date completeTime;  
  private String failedMessage;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getToId() {
    return toId;
  }
  public void setToId(int toId) {
    this.toId = toId;
  }
  public String getTransId() {
    return transId;
  }
  public void setTransId(String transId) {
    this.transId = transId;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public Date getCompleteTime() {
    return completeTime;
  }
  public void setCompleteTime(Date completeTime) {
    this.completeTime = completeTime;
  }
  public String getFailedMessage() {
    return failedMessage;
  }
  public void setFailedMessage(String failedMessage) {
    this.failedMessage = failedMessage;
  }
  
}
