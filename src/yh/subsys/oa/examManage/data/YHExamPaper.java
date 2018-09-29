package yh.subsys.oa.examManage.data;

import java.io.Serializable;
import java.util.Date;

public class YHExamPaper implements Serializable{
  private int seqId;//  SEQ_ID  number  自增ID
  private String userId;//  USER_ID VARCHAR(200)  创建者用户名
  private String paperTitle;//  PAPER_TITLE VARCHAR(200)  试卷标题
  private String paperDesc;//  PAPER_DESC  VARCHAR(254)  试卷说明
  private int paperGrade;//  PAPER_GRADE number  试卷总分
  private int questionsCount;//  QUESTIONS_COUNT number  试题数量
  private String paperTimes;//  PAPER_TIMES VARCHAR(254)  考试时长
  private Date beginDate;//  BEGIN_DATE  DATE  无用
  private Date endDate;//  END_DATE  DATE  无用
  private Date sendDate;//  SEND_DATE DATE  创建和修改的当前时间
  private int roomId;//  ROOM_ID number  所属题库(外键)
  private String questionsType;//  QUESTIONS_TYPE  Char(1) 无用
  private String questionsRank;//  QUESTIONS_RANK  Char(1) 无用
  private String questionsList;//  QUESTIONS_LIST  CLOB  选择题的ID串
  private String questionsScore;//  QUESTIONS_SCORE CLOB  每个选择题的分数（总分数/题数）已逗号分隔的数字串
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getPaperTitle() {
    return paperTitle;
  }
  public void setPaperTitle(String paperTitle) {
    this.paperTitle = paperTitle;
  }
  public String getPaperDesc() {
    return paperDesc;
  }
  public void setPaperDesc(String paperDesc) {
    this.paperDesc = paperDesc;
  }
  public int getPaperGrade() {
    return paperGrade;
  }
  public void setPaperGrade(int paperGrade) {
    this.paperGrade = paperGrade;
  }
  public int getQuestionsCount() {
    return questionsCount;
  }
  public void setQuestionsCount(int questionsCount) {
    this.questionsCount = questionsCount;
  }
  public String getPaperTimes() {
    return paperTimes;
  }
  public void setPaperTimes(String paperTimes) {
    this.paperTimes = paperTimes;
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
  public Date getSendDate() {
    return sendDate;
  }
  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }
  public int getRoomId() {
    return roomId;
  }
  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }
  public String getQuestionsType() {
    return questionsType;
  }
  public void setQuestionsType(String questionsType) {
    this.questionsType = questionsType;
  }
  public String getQuestionsRank() {
    return questionsRank;
  }
  public void setQuestionsRank(String questionsRank) {
    this.questionsRank = questionsRank;
  }
  public String getQuestionsList() {
    return questionsList;
  }
  public void setQuestionsList(String questionsList) {
    this.questionsList = questionsList;
  }
  public String getQuestionsScore() {
    return questionsScore;
  }
  public void setQuestionsScore(String questionsScore) {
    this.questionsScore = questionsScore;
  }
}
