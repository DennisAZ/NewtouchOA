package yh.core.funcs.doc.receive.data;

import java.util.Date;

import yh.core.util.YHUtility;

/**
 * 收 文管理
 * @author Administrator
 *
 */
public class YHDocReceive{
  private int seq_id;             //主键
  private String docNo;           //外办文号
  private Date resDate;           //已办文号
  private String fromUnits;       //来电单位
  private String oppdocNo;        //对方文号
  private String title;           //标题
  private int copies;             //份数
  private int confLevel;          //保密级别
  private String instruct;        //领导批示
  private String recipient;       //签收人  private int docType;            //收件类型
  private int status;             //收文状态0未签，1已签
  private int sendStauts;         //发送类型0未发送，1已发送
  private String sponsor;         //承办单位
  private int userId;             //创建人的id
  private String fromUserName;
  private String toUserName;
  private String confLevelName;
  private String docTypeName;
  private String attachNames;     //附件名称
  private String attachIds;       //附件id
  private String runId;
  private String sponsorName;  //承办单位名称
  private int sendRunId;
  private String recDocId ;
  private String recDocName;
  
  
  public String getRecDocId() {
    return recDocId;
  }
  public void setRecDocId(String recDocId) {
    this.recDocId = recDocId;
  }
  public String getRecDocName() {
    return recDocName;
  }
  public void setRecDocName(String recDocName) {
    this.recDocName = recDocName;
  }
  public int getSendRunId() {
    return sendRunId;
  }
  public void setSendRunId(int sendRunId) {
    this.sendRunId = sendRunId;
  }
  public String getSponsorName(){
    if(YHUtility.isNullorEmpty(sponsorName)){
      sponsorName = "";
    }
    return sponsorName;
  }
  public void setSponsorName(String sponsorName){
    this.sponsorName = sponsorName;
  }

  private YHDocNext next;
  
  public YHDocNext getNext(){
    return next;
  }
  public void setNext(YHDocNext next){
    this.next = next;
  }
  public String getRunId(){
    if(YHUtility.isNullorEmpty(runId)){
      runId = "";
    }
    return runId;
  }
  public void setRunId(String runId){
    this.runId = runId;
  }
  public String getAttachNames(){
    if(YHUtility.isNullorEmpty(attachNames) || "null".equalsIgnoreCase(attachNames) ||"null*".equalsIgnoreCase(attachNames)){
      attachNames = "";
    }
    return attachNames;
  }
  public void setAttachNames(String attachNames){
    this.attachNames = attachNames;
  }
  public String getAttachIds(){
    if(YHUtility.isNullorEmpty(attachIds) || "null".equalsIgnoreCase(attachIds)){
      attachIds = "";
    }
    return attachIds;
  }
  public void setAttachIds(String attachIds){
    this.attachIds = attachIds;
  }
  public String getConfLevelName(String webroot) throws Exception{
    int level = getConfLevel();
    String grbb =  YHDocConst.getProp(webroot, YHDocConst.SECRET_GRADE);
    String[] tt2 = YHDocConst.parseStr2Arr(grbb);
    confLevelName = tt2[level-1];
    return confLevelName;
  }
  public void setConfLevelName(String confLevelName){
    this.confLevelName = confLevelName;
  }
  public String getDocTypeName(String webroot) throws Exception{
    int  docType = getDocType();
    String gr =  YHDocConst.getProp(webroot, YHDocConst.DOC_TYPE);
    String[] tt = YHDocConst.parseStr2Arr(gr);
    docTypeName = tt[docType - 1];
    return docTypeName;
  }
  public void setDocTypeName(String docTypeName){
    this.docTypeName = docTypeName;
  }
  public String getFromUserName(){
    return fromUserName;
  }
  public void setFromUserName(String fromUserName){
    this.fromUserName = fromUserName;
  }
  public String getToUserName(){
    if(YHUtility.isNullorEmpty(toUserName)){
      toUserName = "";
    }
    return toUserName;
  }
  public void setToUserName(String toUserName){
    this.toUserName = toUserName;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  public String getSponsor(){
    if(YHUtility.isNullorEmpty(sponsor)){
      sponsor = "";
    }
    return sponsor;
  }
  public void setSponsor(String sponsor){
    this.sponsor = sponsor;
  }
  public int getSendStauts(){
    return sendStauts;
  }
  public void setSendStauts(int sendStauts){
    this.sendStauts = sendStauts;
  }
  public int getStatus(){
    return status;
  }
  public void setStatus(int status){
    this.status = status;
  }
  public int getSeq_id(){
    return seq_id;
  }
  public void setSeq_id(int seqId){
    seq_id = seqId;
  }
  public String getDocNo(){
    return docNo;
  }
  public void setDocNo(String docNo){
    this.docNo = docNo;
  }
  public Date getResDate(){
    return resDate;
  }
  public void setResDate(Date resDate){
    this.resDate = resDate;
  }
  public String getFromUnits(){
    return fromUnits;
  }
  public void setFromUnits(String fromUnits){
    this.fromUnits = fromUnits;
  }
  public String getOppdocNo(){
    return oppdocNo;
  }
  public void setOppdocNo(String oppdocNo){
    this.oppdocNo = oppdocNo;
  }
  public String getTitle(){
    return title;
  }
  public void setTitle(String title){
    this.title = title;
  }
  public int getCopies(){
    return copies;
  }
  public void setCopies(int copies){
    this.copies = copies;
  }
  public int getConfLevel(){
    return confLevel;
  }
  public void setConfLevel(int confLevel){
    this.confLevel = confLevel;
  }
  public String getInstruct(){
    if(YHUtility.isNullorEmpty(instruct)){
      return "";
    }
    return instruct;
  }
  public void setInstruct(String instruct){
    this.instruct = instruct;
  }
  public String getRecipient(){
    return recipient;
  }
  public void setRecipient(String recipient){
    this.recipient = recipient;
  }
  public int getDocType(){
    return docType;
  }
  public void setDocType(int docType){
    this.docType = docType;
  }
  public String toJson(String webroot) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:\"").append(seq_id).append("\",");
       sb.append("docNo:\"").append(YHUtility.encodeSpecial(docNo)).append("\",");
       sb.append("resDate:\"").append(resDate).append("\",");
       sb.append("fromUnits:\"").append(YHUtility.encodeSpecial(fromUnits)).append("\",");
       sb.append("oppDocNo:\"").append(YHUtility.encodeSpecial(oppdocNo)).append("\",");
       sb.append("title:\"").append(YHUtility.encodeSpecial(title)).append("\",");
       sb.append("copies:\"").append(copies).append("\",");
       sb.append("confLevel:\"").append(confLevel).append("\",");
       sb.append("instruct:\"").append(YHUtility.encodeSpecial(getInstruct())).append("\",");
       sb.append("recipient:\"").append(YHUtility.encodeSpecial(recipient)).append("\",");
       sb.append("docType:\"").append(docType).append("\",");
       sb.append("status:\"").append(status).append("\",");
       sb.append("sendStauts:\"").append(sendStauts).append("\",");
       sb.append("sponsor:\"").append(YHUtility.encodeSpecial(sponsor)).append("\",");
       sb.append("userId:\"").append(userId).append("\",");
       sb.append("fromUserName:\"").append(YHUtility.encodeSpecial(getFromUserName())).append("\",");
       sb.append("toUserName:\"").append(YHUtility.encodeSpecial(getToUserName())).append("\",");
       sb.append("confLevelName:\"").append(YHUtility.encodeSpecial(getConfLevelName(webroot))).append("\",");
       sb.append("docTypeName:\"").append(YHUtility.encodeSpecial(getDocTypeName(webroot))).append("\",");
       sb.append("attachNames:\"").append(YHUtility.encodeSpecial(this.getAttachNames())).append("\",");
       sb.append("attachIds:\"").append(this.getAttachIds()).append("\",");
       sb.append("runId:\"").append(this.getRunId()).append("\",");
       sb.append("nextDoc:").append((this.getNext().toJson())).append("");
    sb.append("}");
    
    return sb.toString();
  }
}
