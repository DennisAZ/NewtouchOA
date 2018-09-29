package yh.subsys.oa.hr.score.data;

import java.util.Date;

public class YHScoreFlow {
  private int seqId;//SEQ_ID  int 流水号
  private int groupId;//GROUP_ID  int   考核项目
  private String flowTitle;//FLOW_TITLE  Varchar（200）  考核任务标题
  private String flowDesc;//FLOW_DESC CLOB  描述
  private String flowFlag;//FLOW_FLAG Varchar（20) 按照管理范围
  private Date beginDate;//BEGIN_DATE  DATE  有效期开始日期  private Date endDate;//END_DATE  DATE  有效期结束日期  private Date sendTime;//SEND_TIME DATE  新建时间
  private String rankman;//RANKMAN CLOB  考核人ID字符串  private String participant;//PARTICIPANT CLOB    被考核人ID字符串  private String anonymity;//ANONYMITY varchar(20) 是否允许匿名1：允许 0：不允许
  private String checkFlag;//CHECK_FLAG varchar(20) 年终考核1：月考核0：
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getGroupId() {
    return groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
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
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

}
