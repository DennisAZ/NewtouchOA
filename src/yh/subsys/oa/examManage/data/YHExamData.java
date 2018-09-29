package yh.subsys.oa.examManage.data;

import java.io.Serializable;
import java.util.Date;

public class YHExamData implements Serializable {
  private int seqId;//  SEQ_ID  number  自增ID
  private int flowId;
  private String rankman;//  RANKMAN VARCHAR(20) 无用
  private String participant;//  PARTICIPANT VARCHAR(20) 答卷人
  private Date startTime;//  STARTTIME DATA  考试开始时间
  private Date endTime;//  ENDTIME DATA  考试结束时间
  private String score;//  SCORE CLOB  答案正确标识串
  private String answer;//  ANSWER  CLOB  正确答案选项串
  private Date rankDate;//  RANK_DATE CLOB  考试日期
  private String examed;//  EXAMED  CLOB  是否参加考试
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getFlowId() {
    return flowId;
  }
  public void setFlowId(int flowId) {
    this.flowId = flowId;
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
  public Date getStartTime() {
    return startTime;
  }
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getScore() {
    return score;
  }
  public void setScore(String score) {
    this.score = score;
  }
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }
  public Date getRankDate() {
    return rankDate;
  }
  public void setRankDate(Date rankDate) {
    this.rankDate = rankDate;
  }
  public String getExamed() {
    return examed;
  }
  public void setExamed(String examed) {
    this.examed = examed;
  }

}
