package yh.core.funcs.sms.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YHSms {
  private int seqId;
  private int toId;
  private String remindFlag;
  private String deleteFlag;
  private int bodySeqId;
  private Date remindTime;
  private ArrayList<YHSmsBody> smsBodyList = new ArrayList<YHSmsBody>();
  public ArrayList<YHSmsBody> getSmsBodyList(){
    return smsBodyList;
  }
  public void addSmsBodyList(YHSmsBody smsBody){
    if(smsBody != null){
      smsBodyList.add(smsBody);
    }
  }
  public void setSmsBodyList(ArrayList<YHSmsBody> smsBodyList){
    this.smsBodyList = smsBodyList;
  }
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
  public String getRemindFlag() {
    return remindFlag;
  }
  public void setRemindFlag(String remindFlag) {
    this.remindFlag = remindFlag;
  }
  public String getDeleteFlag() {
    return deleteFlag;
  }
  public int getBodySeqId() {
    return bodySeqId;
  }
  public void setBodySeqId(int bodySeqId) {
    this.bodySeqId = bodySeqId;
  }
  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }
  public Date getRemindTime() {
    return remindTime;
  }
  public void setRemindTime(Date remindTime) {
    this.remindTime = remindTime;
  }
  @Override
  public String toString(){
    return "YHSms [bodySeqId=" + bodySeqId + ", deleteFlag=" + deleteFlag
        + ", remindFlag=" + remindFlag + ", remindTime=" + remindTime
        + ", seqId=" + seqId + ", smsBodyList=" + smsBodyList + ", toId="
        + toId + "]";
  }
  
}
