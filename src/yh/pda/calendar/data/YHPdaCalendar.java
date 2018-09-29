package yh.pda.calendar.data;

import java.util.Date;

public class YHPdaCalendar {

  private int seqId; 
  private Date calTime; 
  private Date endTime;
  private String content;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public Date getCalTime() {
    return calTime;
  }
  public void setCalTime(Date calTime) {
    this.calTime = calTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  
}
