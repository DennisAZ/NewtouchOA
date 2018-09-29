package yh.core.funcs.system.mobilesms.data;

import java.util.Date;
public class YHSms3 {
  private int seqId;
  private String phone;
  private String content;
  private Date sendTime;
  public int getSeqId() {
    return this.seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
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
}
