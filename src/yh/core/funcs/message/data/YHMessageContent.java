package yh.core.funcs.message.data;

import java.util.Date;

public class YHMessageContent {
  private int seqId;
  private String fromId;
  private String content;
  private Date sendTime;
  private String toId;
  private String remindFlag;
  private String deleteFlag;
  private int bodySeqId;
  private int remindTime;
  
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  public String getRemindFlag() {
    return remindFlag;
  }
  public void setRemindFlag(String remindFlag) {
    this.remindFlag = remindFlag;
  }
  public String getDeleteFlag() {
    return deleteFlag;
  }
  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }
  public int getBodySeqId() {
    return bodySeqId;
  }
  public void setBodySeqId(int bodySeqId) {
    this.bodySeqId = bodySeqId;
  }
  public int getRemindTime() {
    return remindTime;
  }
  public void setRemindTime(int remindTime) {
    this.remindTime = remindTime;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFromId() {
    return fromId;
  }
  public void setFromId(String fromId) {
    this.fromId = fromId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
}
