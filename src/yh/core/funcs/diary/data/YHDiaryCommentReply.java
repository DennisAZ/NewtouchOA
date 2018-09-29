package yh.core.funcs.diary.data;

import java.util.Date;
/**
 * 工作日志点评回复
 * @author TTlang
 *
 */
public class YHDiaryCommentReply{
  private int seqId;
  private int commentId;
  private String replyComment;
  private String replyer;
  private Date replyTime;
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getCommentId(){
    return commentId;
  }
  public void setCommentId(int commentId){
    this.commentId = commentId;
  }
  public String getReplyComment(){
    return replyComment;
  }
  public void setReplyComment(String replyComment){
    this.replyComment = replyComment;
  }
  public String getReplyer(){
    return replyer;
  }
  public void setReplyer(String replyer){
    this.replyer = replyer;
  }
  public Date getReplyTime(){
    return replyTime;
  }
  public void setReplyTime(Date replyTime){
    this.replyTime = replyTime;
  }
  @Override
  public String toString(){
    return "YHDiaryCommentReply [commentId=" + commentId + ", replyComment="
        + replyComment + ", replyTime=" + replyTime + ", replyer=" + replyer
        + ", seqId=" + seqId + "]";
  }
  
}
