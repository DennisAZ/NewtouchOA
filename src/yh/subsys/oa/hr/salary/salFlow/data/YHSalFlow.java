package yh.subsys.oa.hr.salary.salFlow.data;

import java.util.Date;

public class YHSalFlow {

  private int seqId;
  private Date beginDate;
  private Date endDate;
  private String content;
  private Date sendTime;
  private String style;
  private String issend;
  private String salYear;
  private String salMonth;
  private String salCreater;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public Date getBeginDate() {
    return beginDate;
  }
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
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
  public String getStyle() {
    return style;
  }
  public void setStyle(String style) {
    this.style = style;
  }
  public String getIssend() {
    return issend;
  }
  public void setIssend(String issend) {
    this.issend = issend;
  }
  public String getSalYear() {
    return salYear;
  }
  public void setSalYear(String salYear) {
    this.salYear = salYear;
  }
  public String getSalMonth() {
    return salMonth;
  }
  public void setSalMonth(String salMonth) {
    this.salMonth = salMonth;
  }
  public String getSalCreater() {
    return salCreater;
  }
  public void setSalCreater(String salCreater) {
    this.salCreater = salCreater;
  }
  
  
}
