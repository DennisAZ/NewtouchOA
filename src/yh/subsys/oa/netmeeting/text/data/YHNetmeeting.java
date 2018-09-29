package yh.subsys.oa.netmeeting.text.data;

import java.util.Date;

public class YHNetmeeting {

  private int seqId;//   流水号 y   自增
  private String createUserId;//     系统登录人seqid      
  private String toId;//   参会人员      
  private String subject;//   会议主题      
  private Date beginTime;//    开始时间      
  private String stop;//    当前状态      0：尚未开始  2：会议进行中  3：已结束
  private Date addTime;//  系统当前时间      
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getCreateUserId() {
    return createUserId;
  }
  public void setCreateUserId(String createUserId) {
    this.createUserId = createUserId;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getStop() {
    return stop;
  }
  public void setStop(String stop) {
    this.stop = stop;
  }
  public Date getBeginTime() {
    return beginTime;
  }
  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
  public Date getAddTime() {
    return addTime;
  }
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }
}
