package yh.core.oaknow.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;

/**
 * OA知道的问题
 * @author qwx110
 * 
 */
public class YHOAAsk implements Serializable{
  private String creator;      // 问题提出者的user_id
  private Date   createDate;   // 问题提出的时间
  private String askComment;   // 提问的内容
  private String ask;          // 提问的标题
  private int    askReplyCount; // 问题回复次数
  private String replyKeyWord; // 标签
  private int   status  = 0;       // 问题解决状态，1：解决，0：未解决
  private int   commend;      // 推荐的标志，0：未推荐，1：推荐
  private Date   resolveTime;  // 问题解决的时间
  private int    rank;         // 点击的次数
  private int typeId = -1;         //问题输入那个分类id
  private String categoryName; //问题输入那个分类名称
  private int askCount;        //回答次数
  private int seqId;           //主键
  private String createDateStr;
  private String resolveTimeStr;
  private String creatorName;  //提问者的名字
  private int parentId=0;        //问题分类的父id
  private int creatorId =0;
  private String answer;        //（搜索时使用的 ）问题答案
  private int commendCount;   //评论的多少
  public int getCommendCount(){
    return commendCount;
  }

  public void setCommendCount(int commendCount){
    this.commendCount = commendCount;
  }

  public String getAnswer(){
    return answer;
  }

  public void setAnswer(String answer){
    this.answer = answer;
  }

  public int getCreatorId(){
    int k = Integer.parseInt(getCreator());
    return k;
  }

  public void setCreatorId(int creatorId){
    this.creatorId = creatorId;
  }

  public int getParentId(){
    return parentId;
  }

  public void setParentId(int parentId){
    this.parentId = parentId;
  }

  public String getCreatorName(){
    return creatorName;
  }

  public void setCreatorName(String creatorName){
    this.creatorName = creatorName;
  }

  public String getCreateDateStr(){
    return dateFormat(getCreateDate());
  }

  public void setCreateDateStr(String createDateStr){
    this.createDateStr = createDateStr;
  }

  public String getResolveTimeStr(){
    return dateFormat(getResolveTime());
  }

  public void setResolveTimeStr(String resolveTimeStr){
    this.resolveTimeStr = resolveTimeStr;
  }

  public int getSeqId(){
    return seqId;
  }

  public void setSeqId(int seqId){
    this.seqId = seqId;
  }

  public int getAskCount(){
    return askCount;
  }

  public void setAskCount(int askCount){
    this.askCount = askCount;
  }

  public String getCategoryName(){
    return categoryName;
  }

  public void setCategoryName(String categoryName){
    this.categoryName = categoryName;
  }


  public int getTypeId(){
    return typeId;
  }

  public void setTypeId(int typeId){
    this.typeId = typeId;
  }

  public String getCreator(){
    return creator;
  }

  public void setCreator(String creator){
    this.creator = creator;
  }

  public Date getCreateDate(){
    return createDate;
  }

  public void setCreateDate(Date createDate){
    this.createDate = createDate;
  }

  public String getAskComment(){
    return askComment;
  }

  public void setAskComment(String askComment){
    this.askComment = askComment;
  }

  public String getAsk(){
    return ask;
  }

  public void setAsk(String ask){
    this.ask = ask;
  }

  public int getAskReplyCount(){
    return askReplyCount;
  }

  public void setAskReplyCount(int askReplyCount){
    this.askReplyCount = askReplyCount;
  }

  public String getReplyKeyWord(){
    return replyKeyWord;
  }

  public void setReplyKeyWord(String replyKeyWord){
    this.replyKeyWord = replyKeyWord;
  }

 

  public int getStatus(){
    return status;
  }

  public void setStatus(int status){
    this.status = status;
  }

  public int getCommend(){
    return commend;
  }

  public void setCommend(int commend){
    this.commend = commend;
  }

  public Date getResolveTime(){
    return resolveTime;
  }

  public void setResolveTime(Date resolveTime){
    this.resolveTime = resolveTime;
  }

  public int getRank(){
    return rank;
  }

  public void setRank(int rank){
    this.rank = rank;
  }
  
  public String dateFormat(Date date){
    if(date != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String ds = sdf.format(date);
      //YHOut.println(ds.toString());
      return ds.toString();
    }else{
      return "";
    }    
  }
  
  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("creator:").append("'").append(this.getCreator()).append("'").append(",");
       sb.append("ask:").append("'").append(YHUtility.encodeSpecial(this.getAsk())).append("'").append(",");
       sb.append("askComment:").append("'").append(YHUtility.encodeSpecial(YHStringUtil.replaceN(getAskComment()))).append("'").append(",");
       //sb.append("askReplyCount:").append("'").append(this.getAskReplyCount()).append("'").append(",");
       sb.append("replyKeyWord:").append("'").append(YHUtility.encodeSpecial(this.getReplyKeyWord())).append("'").append(",");
       sb.append("status:").append("'").append(this.getStatus()).append("'").append(",");
      // sb.append("commend:").append("'").append(this.getCommend()).append("'").append(",");
      // sb.append("rank:").append("'").append(this.getRank()).append("'").append(",");       
       sb.append("typeId:").append("'").append(this.getTypeId()).append("'").append(",");
       sb.append("categoryName:").append("'").append(YHUtility.encodeSpecial(this.getCategoryName())).append("'").append(",");
      // sb.append("askCount:").append("'").append(this.getAskCount()).append("'").append(",");
      // sb.append("categoryName:").append("'").append(this.getCategoryName()).append("'").append(",");
       sb.append("seqId:").append("'").append(this.getSeqId()).append("'").append(",");
       sb.append("userName:").append("'").append(this.getCreatorName()).append("'").append(",");
       sb.append("pid:").append(this.getParentId()).append(",");
       sb.append("createDateStr:").append("'").append(this.getCreateDateStr()).append("'").append(",");
       sb.append("resolveTimeStr:").append("'").append(this.getResolveTimeStr()).append("'");
    sb.append("}");
    //YHOut.println(sb.toString()+"&&&&&&&&&");
    return sb.toString();
  }
}
