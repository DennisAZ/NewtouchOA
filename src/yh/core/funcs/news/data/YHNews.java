package yh.core.funcs.news.data;

import java.util.Date;

import yh.core.util.YHUtility;

public class YHNews {

  private int seqId = 0; //自增ID
  private String subject = null;//新闻标题
  private String content = null;//新闻内容
  private String provider = null;//发布者
  private Date newsTime = null;//发布时间
  private int clickCount = 0;//点击数
  private String  attachmentId = null;//附件ID串(逗号分隔)
  private String  attachmentName = null;//附件名称串
  private String anonymityYn = null;//是否允许匿名评论
  private String format = null;//新闻格式
  private String typeId = null;//新闻类型
  private String publish = null;//发布标识
  private String toId = null;//发布范围
  private String privId = null;//角色ID串
  private String userId = null;//人员ID串
  private String readers = null;//阅读人员ID串
  private byte[] compressContent = null;//压缩后的公告通知内容
  private String top = null;//是否置顶
  
  private String subjectFont;
  
  public String getSubjectFont() {
    return subjectFont;
  }
  public void setSubjectFont(String subjectFont) {
    this.subjectFont = subjectFont;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
 
  public String getAttachmentId() {
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName() {
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }
  public String getReaders() {
    return readers;
  }
  public void setReaders(String readers) {
    this.readers = readers;
  }
 
  public String getPrivId() {
    return privId;
  }
  public void setPrivId(String privId) {
    this.privId = privId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getTypeId() {
    return typeId;
  }
  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }
  public String getTop() {
    return top;
  }
  public void setTop(String top) {
    this.top = top;
  }
 
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }
  public String getPublish() {
    return publish;
  }
  public void setPublish(String publish) {
    this.publish = publish;
  }
 
 
  public byte[] getCompressContent() {
    return compressContent;
  }
  public void setCompressContent(byte[] compressContent) {
    this.compressContent = compressContent;
  }
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }
  public Date getNewsTime() {
    return newsTime;
  }
  public void setNewsTime(Date newsTime) {
    this.newsTime = newsTime;
  }
  public int getClickCount() {
    return clickCount;
  }
  public void setClickCount(int clickCount) {
    this.clickCount = clickCount;
  }
  public String getAnonymityYn() {
    return anonymityYn;
  }
  public void setAnonymityYn(String anonymityYn) {
    this.anonymityYn = anonymityYn;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
  }
  
  public String toJSON(){
    if("-1".equals(this.toId)){
      this.toId = "";
    }
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:" + this.seqId);
    if(this.subject != null){
      sb.append(",subject:'" + YHUtility.encodeSpecial(this.subject) + "'");
    }else{
      sb.append(",subject:''");
    }
    if(this.content != null){
      sb.append(",content:'" + YHUtility.encodeSpecial(this.content) + "'");
    }else{
      sb.append(",content:''");
    }
    if(this.provider != null){
      sb.append(",provider:'" + this.provider + "'");
    }else{
      sb.append(",provider:''");
    }
    if(this.newsTime != null){
      sb.append(",newsTime:'" + this.newsTime + "'");
    }else{
      sb.append(",newsTime:''");
    }
    sb.append(",clickCount:'" + this.clickCount + "'");
    
    if(this.attachmentId != null){
      sb.append(",attachmentId:'" + this.attachmentId + "'");
    }else{
      sb.append(",attachmentId:''");
    }
    if(this.attachmentName != null){
      sb.append(",attachmentName:'" + this.attachmentName + "'");
    }else{
      sb.append(",attachmentName:''");
    }
    if(this.anonymityYn != null){
      sb.append(",anonymityYn:'" + this.anonymityYn + "'");
    }else{
      sb.append(",anonymityYn:''");
    }
    if(this.format != null){
      sb.append(",format:'" + this.format + "'");
    }else{
      sb.append(",format:''");
    }
    if(this.typeId != null){
      sb.append(",typeId:'" + this.typeId + "'");
    }else{
      sb.append(",typeId:''");
    }
    
    if(this.publish != null){
      sb.append(",publish:'" + this.publish + "'");
    }else{
      sb.append(",publish:''");
    }
    
    if(this.toId != null){
      sb.append(",toId:'" + this.toId + "'");
    }else{
      sb.append(",toId:''");
    }
    
    if(this.privId != null){
      sb.append(",privId:'" + this.privId + "'");
    }else{
      sb.append(",privId:''");
    }
    
    if(this.userId != null){
      sb.append(",userId:'" + this.userId + "'");
    }else{
      sb.append(",userId:''");
    }
    
    if(this.top != null){
      sb.append(",top:'" + this.top + "'");
    }else{
      sb.append(",top:''");
    }
    
    if(this.top != null){
      sb.append(",subjectFont:'" + this.subjectFont + "'");
    }else{
      sb.append(",subjectFont:''");
    }
    sb.append("}");
    return sb.toString();
  }
}
