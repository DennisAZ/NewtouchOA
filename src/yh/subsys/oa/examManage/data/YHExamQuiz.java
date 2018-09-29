package yh.subsys.oa.examManage.data;

import java.io.Serializable;

public class YHExamQuiz implements Serializable{
  private int seqId;//  SEQ_ID  number  自增ID
  private int roomId;//  ROOM_ID number  外键
  private String questionsType;//  QUESTIONS_TYPE  Char(1) 题型
  private String questionsRank;//  QUESTIONS_RANK  Char(1) 难度
  private String questions;//  QUESTIONS CLOB  题目
  private String answerA;//  ANSWER_A  VARCHAR(200)  备选答案A
  private String answerB;//  ANSWER_B  VARCHAR(200)  备选答案B
  private String answerC;//  ANSWER_C  VARCHAR(200)  备选答案C
  private String answerD;//  ANSWER_D  VARCHAR(200)  备选答案D
  private String answerE;//  ANSWER_E  VARCHAR(200)  备选答案E
  private String answers;//  ANSWERS VARCHAR(200)  正确答案
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
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
  public String getQuestions() {
    return questions;
  }
  public void setQuestions(String questions) {
    this.questions = questions;
  }
  public String getAnswerA() {
    return answerA;
  }
  public void setAnswerA(String answerA) {
    this.answerA = answerA;
  }
  public String getAnswerB() {
    return answerB;
  }
  public void setAnswerB(String answerB) {
    this.answerB = answerB;
  }
  public String getAnswerC() {
    return answerC;
  }
  public void setAnswerC(String answerC) {
    this.answerC = answerC;
  }
  public String getAnswerD() {
    return answerD;
  }
  public void setAnswerD(String answerD) {
    this.answerD = answerD;
  }
  public String getAnswerE() {
    return answerE;
  }
  public void setAnswerE(String answerE) {
    this.answerE = answerE;
  }
  public String getAnswers() {
    return answers;
  }
  public void setAnswers(String answers) {
    this.answers = answers;
  }

}
