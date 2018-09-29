package yh.core.funcs.mobilesms.data;

import java.util.Date;

public class YHSms2 {
  private int seqId;
  private String fromId;
  private String phone;
  private String content;
  private Date sendTime;
  private String sendFlag;
  
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getFromId() {
    return this.fromId;
  }
  public void setFromId(String fromId) {
    this.fromId = fromId;
  }
  public String getPhone() {
    return this.phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getSendTime() {
    return this.sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
  public String getSendFlag() {
    return this.sendFlag;
  }
  public void setSendFlag(String sendFlag) {
    this.sendFlag = sendFlag;
  }
}
