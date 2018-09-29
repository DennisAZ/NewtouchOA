package yh.core.oaknow.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OA知道评论
 * 
 * @author qwx110
 * 
 */
public class YHOAComment{
  private int    commentId; //评论id
  private String mamber;   // 评论人的userId
  private Date   dateTime; // 评论时间
  private String comment;  // 品论内容
  private int    askId;    // 问题id
  private String userName; //评论人名字
  private String dateTimeStr;//
  public String getDateTimeStr(){
    return dateFormat(this.getDateTime());
  }

  public void setDateTimeStr(String dateTimeStr){
    this.dateTimeStr = dateTimeStr;
  }

  public String getUserName(){
    return userName;
  }

  public void setUserName(String userName){
    this.userName = userName;
  }

  public int getCommentId(){
    return commentId;
  }

  public void setCommentId(int commentId){
    this.commentId = commentId;
  }

  public String getMamber(){
    return mamber;
  }

  public void setMamber(String mamber){
    this.mamber = mamber;
  }

  public Date getDateTime(){
    return dateTime;
  }

  public void setDateTime(Date dateTime){
    this.dateTime = dateTime;
  }

  public String getComment(){
    return comment;
  }

  public void setComment(String comment){
    this.comment = comment;
  }

  public int getAskId(){
    return askId;
  }

  public void setAskId(int askId){
    this.askId = askId;
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
