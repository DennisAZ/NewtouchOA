package yh.core.funcs.diary.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yh.core.util.YHUtility;
/**
 * 工作日志
 * @author TTlang
 *
 */
public class YHDiary{
  
  private int seqId;
  private int userId;
  private Date diaDate;
  private Date diaTime;
  private String diaType;
  private String  subject ;
  private String content;
  private String attachmentId;
  private String attachmentName;
  private Date lastCommentTime;
  private String toId;
  private String compressContent;
  private String readers;
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  public Date getDiaDate(){
    return diaDate;
  }
  /**
   * 保存时间为YYYY-MM-DD格式去掉时间
   * @param diaDate
   */
  public void setDiaDate(Date diaDate){
    try{
      diaDate =YHUtility.parseDate(new SimpleDateFormat("yyyy-MM-dd").format(diaDate));
    } catch (ParseException e){
      e.printStackTrace();
    }
    this.diaDate = diaDate;
  }
  public Date getDiaTime(){
    return diaTime;
  }
  public void setDiaTime(Date diaTime){
    this.diaTime = diaTime;
  }
  public String getDiaType(){
    return diaType;
  }
  public void setDiaType(String diaType){
    this.diaType = diaType;
  }
  public String getSubject(){
    return subject;
  }
  public void setSubject(String subject){
    this.subject = subject;
  }
  public String getContent(){
    return content;
  }
  public void setContent(String content){
    this.content = content;
  }
  public String getAttachmentId(){
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId){
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName(){
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName){
    this.attachmentName = attachmentName;
  }
  public Date getLastCommentTime(){
    return lastCommentTime;
  }
  public void setLastCommentTime(Date lastCommentTime){
    this.lastCommentTime = lastCommentTime;
  }
  public String getToId(){
    return toId;
  }
  public void setToId(String toId){
    this.toId = toId;
  }
  public String getCompressContent(){
    return content;
  }
  public void setCompressContent(String compressContent){
    this.compressContent = compressContent;
  }
  public String getReaders(){
    return readers;
  }
  public void setReaders(String readers){
    this.readers = readers;
  }
  @Override
  public String toString(){
    return "YHDiary [attachmentId=" + attachmentId + ", attachmentName="
        + attachmentName + ", compressContent=" + compressContent
        + ", content=" + content + ", diaDate=" + diaDate + ", diaTime="
        + diaTime + ", diaType=" + diaType + ", lastCommentTime="
        + lastCommentTime + ", readers=" + readers + ", seqId=" + seqId
        + ", subject=" + subject + ", toId=" + toId + ", userId=" + userId
        + "]";
  }
  
}
