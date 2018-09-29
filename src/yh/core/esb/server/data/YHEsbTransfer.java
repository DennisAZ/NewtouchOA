package yh.core.esb.server.data;

import java.util.Date;

public class YHEsbTransfer {

  private int seqId;
  private int fromId;
  private String guid;
  private String filePath;
  private String content;
  private String status;
  private String type;
  private Date createTime;
  private String toId;
  private Date completeTime;
  private String failedMessage;
  private String message ;
  private String optGuid;
  
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getOptGuid() {
    return optGuid;
  }
  public void setOptGuid(String optGuid) {
    this.optGuid = optGuid;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public int getFromId() {
    return fromId;
  }
  public void setFromId(int fromId) {
    this.fromId = fromId;
  }
  public String getFilePath() {
    return filePath;
  }
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
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
