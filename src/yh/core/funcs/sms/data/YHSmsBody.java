package yh.core.funcs.sms.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class YHSmsBody {
  private int seqId;
  private int fromId;
  private String smsType;
  private String content;
  private Date sendTime;
  private String remindUrl;
  private ArrayList<YHSms> smslist;
  public ArrayList<YHSms> getSmslist() {
    return smslist;
  }
  public void setSmslist(ArrayList<YHSms> smslist) {
    this.smslist = smslist;
  }
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
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
  public String getRemindUrl() {
    return remindUrl;
  }
  public void setRemindUrl(String remindUrl) {
    this.remindUrl = remindUrl;
  }
  public Iterator itSmsl(){
    if(this.smslist == null){
      this.smslist = new ArrayList<YHSms>();
    }
    return this.smslist.iterator();
  }
  @Override
  public String toString(){
    return "YHSmsBody [content=" + content + ", fromId=" + fromId
        + ", remindUrl=" + remindUrl + ", sendTime=" + sendTime + ", seqId="
        + seqId + ", smsType=" + smsType + ", smslist=" + smslist + "]";
  }
  
}
