package yh.core.oaknow.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YHAskAnswer{
  private int    answerId;
  private int    askId;        // 问题id
  private String answerUserId = "0"; // 回答人的userId
  private Date   answerTime;   // 回答时间
  private String answerComment; // 回答内容
  private int   goodAnswer = 0;   // 是否采纳为答案 0：普通答案，1：采纳答案
  private String userName;     //回答人的名字
  private String answerTimeStr;
  
  public String getAnswerTimeStr(){
    return dateFormat(this.getAnswerTime());
  }

  public void setAnswerTimeStr(String answerTimeStr){
    this.answerTimeStr = answerTimeStr;
  }

  public String getUserName(){
    return userName;
  }

  public void setUserName(String userName){
    this.userName = userName;
  }

  public int getAnswerId(){
    return answerId;
  }

  public void setAnswerId(int answerId){
    this.answerId = answerId;
  }

  public int getAskId(){
    return askId;
  }

  public void setAskId(int askId){
    this.askId = askId;
  }

  public String getAnswerUserId(){
    return answerUserId;
  }

  public void setAnswerUserId(String answerUserId){
    this.answerUserId = answerUserId;
  }

  public Date getAnswerTime(){
    return answerTime;
  }

  public void setAnswerTime(Date answerTime){
    this.answerTime = answerTime;
  }

  public String getAnswerComment(){
    return answerComment;
  }

  public void setAnswerComment(String answerComment){
    this.answerComment = answerComment;
  }  
  
  public int getGoodAnswer(){
    return goodAnswer;
  }

  public void setGoodAnswer(int goodAnswer){
    this.goodAnswer = goodAnswer;
  }

  public String dateFormat(Date date){
    if(date != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String ds = sdf.format(date);
      return ds.toString();
    }else{
      return "";
    }    
  }
}
