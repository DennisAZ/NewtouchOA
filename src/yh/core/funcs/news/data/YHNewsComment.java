package yh.core.funcs.news.data;

import java.util.Date;

public class YHNewsComment {

  private int seqId = 0; //自增ID
  private int parentId = 0;
  private int newsId = 0;
  private String userId = "";
  private String nickName = "";
  private String content = null;//新闻内容
  private Date reTime = null;

  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public int getParentId() {
    return parentId;
  }
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
  public int getNewsId() {
    return newsId;
  }
  public void setNewsId(int newsId) {
    this.newsId = newsId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getNickName() {
    return nickName;
  }
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  public Date getReTime() {
    return reTime;
  }
  public void setReTime(Date reTime) {
    this.reTime = reTime;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
 
 
 
}
