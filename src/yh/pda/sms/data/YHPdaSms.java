package yh.pda.sms.data;

import java.util.Date;

public class YHPdaSms {

  private int seqId;
  private int fromId;
  private Date sendTime;
  private String smsType;
  private String content;
  private String userName;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getFromId() {
    return fromId;
  }
  public void setFromId(int fromId) {
    this.fromId = fromId;
  }
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
  public String getSmsType() {
    return smsType;
  }
  public void setSmsType(String smsType) {
    this.smsType = smsType;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
}
