package yh.subsys.oa.examManage.data;

import java.io.Serializable;
import java.util.Date;

public class YHExamFlow implements Serializable{
  private int seqId;//  SEQ_ID  number  自增ID
  private int paperId;//  PAPER_ID  number  外键
  private String flowTitle;//  FLOW_TITLE  VARCHAR(200)  考试名称
  private String flowDesc;//  FLOW_DESC CLOB  考试信息描述
  private String flowFlag;//  FLOW_FLAG VARCHAR(20) 无用
  private Date beginDate;//  BEGIN_DATE  DATE  生效日期
  private Date endDate;//  END_DATE  DATE  终止日期
  private Date sendTime;//  SEND_TIME DATE  当前时间
  private String rankman;//  RANKMAN CLOB  无用
  private String participant;//  PARTICIPANT CLOB  参加考试人
  private String anonymity;//  ANONYMITY VARCHAR(20) 无用
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getPaperId() {
    return paperId;
  }
  public void setPaperId(int paperId) {
    this.paperId = paperId;
  }
  public String getFlowTitle() {
    return flowTitle;
  }
  public void setFlowTitle(String flowTitle) {
    this.flowTitle = flowTitle;
  }
  public String getFlowDesc() {
    return flowDesc;
  }
  public void setFlowDesc(String flowDesc) {
    this.flowDesc = flowDesc;
  }
  public String getFlowFlag() {
    return flowFlag;
  }
  public void setFlowFlag(String flowFlag) {
    this.flowFlag = flowFlag;
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
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }
  public String getRankman() {
    return rankman;
  }
  public void setRankman(String rankman) {
    this.rankman = rankman;
  }
  public String getParticipant() {
    return participant;
  }
  public void setParticipant(String participant) {
    this.participant = participant;
  }
  public String getAnonymity() {
    return anonymity;
  }
  public void setAnonymity(String anonymity) {
    this.anonymity = anonymity;
  }
}
