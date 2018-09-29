package yh.core.funcs.notify.data;

import java.util.Date;

import yh.core.util.YHUtility;

public class YHNotify {

  private int seqId = 0; //自增ID
  private int fromDept = 0;//发布部门ID
  private String fromId = null;//发布用户ID
  private String toId = null;//按部门发布(部门ID串)
  private String subject = null;//公告标题
  private String content = null;//公告通知内容
  private Date sendTime = null;//发布时间
  private Date beginDate = null;//开始日期(有效期)
  private Date endDate = null;//结束日期(有效期)
  private String  attachmentId = null;//附件ID串(逗号分隔)
  private String  attachmentName = null;//附件名称串
  private String  readers = null;//阅读人员用户ID串
  private String  print = null;//是否允许打印office附件
  private String privId = null;//按角色发布
  private String userId = null;//按人员发布
  private String typeId = null;//公告类型
  private String top = null;//是否置顶
  private int topDays = 0;//置顶天数
  private String format = null;//公告通知格式
  private String publish = null;//发布标识
  private String auditer = null;//审核人用户ID
  private String reason = null;//审核人不同意的原因
  private Date auditDate = null;//通过/不通过的日期
  private byte[] compressContent = null;//压缩后的公告通知内容
  private String download = null;//是否允许下载office附件 
  
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
  public int getFromDept() {
    return fromDept;
  }
  public void setFromDept(int fromDept) {
    this.fromDept = fromDept;
  }
  public String getFromId() {
    return fromId;
  }
  public void setFromId(String fromId) {
    this.fromId = fromId;
  }
  public String getToId() {
    return toId;
  }
  public void setToId(String toId) {
    this.toId = toId;
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
  public Date getSendTime() {
    return sendTime;
  }
  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
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
  public String getPrint() {
    return print;
  }
  public void setPrint(String print) {
    this.print = print;
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
  public int getTopDays() {
    return topDays;
  }
  public void setTopDays(int topDays) {
    this.topDays = topDays;
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
  public String getAuditer() {
    return auditer;
  }
  public void setAuditer(String auditer) {
    this.auditer = auditer;
  }
  public String getReason() {
    return reason;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
  public Date getAuditDate() {
    return auditDate;
  }
  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }
  public byte[] getCompressContent() {
    return compressContent;
  }
  public void setCompressContent(byte[] compressContent) {
    this.compressContent = compressContent;
  }
  public String getDownload() {
    return download;
  }
  public void setDownload(String download) {
    this.download = download;
  }
  public String toJSON(){
    if("-1".equals(this.toId)){
      this.toId = "";
    }
    StringBuffer sb = new StringBuffer("{");
    sb.append("seqId:" + this.seqId);
    sb.append(",fromDept:" + this.fromDept);
    if(this.fromId != null){
      sb.append(",fromId:'" + this.fromId + "'");
    }else{
      sb.append(",fromId:''");
    }
    if(this.toId != null){
      sb.append(",toId:'" + this.toId + "'");
    }else{
      sb.append(",toId:''");
    }
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
    if(this.sendTime != null){
      sb.append(",sendTime:'" + this.sendTime + "'");
    }else{
      sb.append(",sendTime:''");
    }
    if(this.beginDate != null){
      sb.append(",beginDate:'" + this.beginDate + "'");
    }else{
      sb.append(",beginDate:''");
    }
    if(this.endDate != null){
      sb.append(",endDate:'" + this.endDate + "'");
    }else{
      sb.append(",endDate:''");
    }
    
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
    if(this.readers != null){
      sb.append(",readers:'" + this.readers + "'");
    }else{
      sb.append(",readers:''");
    }
    
    if(this.print != null){
      sb.append(",print:'" + this.print + "'");
    }else{
      sb.append(",print:''");
    }
    
    if(this.privId != null){
      sb.append(",privId:'" + this.privId + "'");
    }else{
      sb.append(",privId:''");
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
    
    if(this.typeId != null){
      sb.append(",typeId:'" + this.typeId + "'");
    }else{
      sb.append(",typeId:''");
    }
    if(this.top != null){
      sb.append(",top:'" + this.top + "'");
    }else{
      sb.append(",top:''");
    }
    
    sb.append(",topDays:'" + this.topDays + "'");
  
    if(this.format != null){
      sb.append(",format:'" + this.format + "'");
    }else{
      sb.append(",format:''");
    }
    if(this.publish != null){
      sb.append(",publish:'" + this.publish + "'");
    }else{
      sb.append(",publish:''");
    }
    if(this.auditer != null){
      sb.append(",auditer:'" + this.auditer + "'");
    }else{
      sb.append(",auditer:''");
    }
    
    if(this.reason != null){
      sb.append(",reason:'" + this.reason + "'");
    }else{
      sb.append(",reason:''");
    }
    if(this.auditDate != null){
      sb.append(",auditDate:'" + this.auditDate + "'");
    }else{
      sb.append(",auditDate:''");
    }
    if(this.download != null){
      sb.append(",download:'" + this.download + "'");
    }else{
      sb.append(",download:''");
    }
    if(this.subjectFont != null){
      sb.append(",subjectFont:'" + this.subjectFont + "'");
    }else{
      sb.append(",subjectFont:''");
    }
    sb.append("}");
    return sb.toString();
  }
}
