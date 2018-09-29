package yh.core.funcs.diary.data;

import java.util.Date;
/**
 * 工作日志点评
 * @author TTlang
 *
 */
public class YHDiaryComment{
  private int seqId;
  private int diaId;
  private int userId;
  private Date sendTime;
  private String commentFlag;
  private String content;
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getDiaId(){
    return diaId;
  }
  public void setDiaId(int diaId){
    this.diaId = diaId;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  public Date getSendTime(){
    return sendTime;
  }
  public void setSendTime(Date sendTime){
    this.sendTime = sendTime;
  }
  public String getCommentFlag(){
    return commentFlag;
  }
  public void setCommentFlag(String commentFlag){
    this.commentFlag = commentFlag;
  }
  public String getContent(){
    return content;
  }
  public void setContent(String content){
    this.content = content;
  }
  @Override
  public String toString(){
    return "YHDiaryComment [commentFlag=" + commentFlag + ", content="
        + content + ", diaId=" + diaId + ", sendTime=" + sendTime + ", seqId="
        + seqId + ", userId=" + userId + "]";
  }
  
}
