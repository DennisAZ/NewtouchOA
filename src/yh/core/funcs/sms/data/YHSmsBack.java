package yh.core.funcs.sms.data;

import java.util.Date;

public class YHSmsBack{
  private String smsType;
  private String content;
  private String remindUrl;
  private String toId;
  private int fromId;
  private Date sendDate;
  public Date getSendDate() {
    return sendDate;
  }
  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }
  public String getSmsType(){
    return smsType;
  }
  public void setSmsType(String smsType){
    this.smsType = smsType;
  }
  public String getContent(){
    return content;
  }
  public void setContent(String content){
    this.content = content;
  }
  public String getRemindUrl(){
    return remindUrl;
  }
  public void setRemindUrl(String remindUrl){
    this.remindUrl = remindUrl;
  }
  public String getToId(){
    return toId;
  }
  public void setToId(String toId){
    this.toId = toId;
  }
  public int getFromId(){
    return fromId;
  }
  public void setFromId(int fromId){
    this.fromId = fromId;
  }
  @Override
  public String toString(){
    return "YHSmsBack [content=" + content + ", fromId=" + fromId
        + ", remindUrl=" + remindUrl + ", smsType=" + smsType + ", toId="
        + toId + "]";
  }
  
}
