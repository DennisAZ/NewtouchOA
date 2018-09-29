package yh.core.funcs.email.data;

import java.util.Date;

public class YHEmailBody{
  private int seqId;
  private int fromId;
  private String toId;
  private String secretToId;
  private String copyToId;
  private String subject;
  private String content;
  private Date sendTime;
  private String attachmentId;
  private String attachmentName;
  private String sendFlag;
  private String smsRemind;
  private String important;
  private long ensize;
  private String fromWebmail;
  private String toWebmail;
  private String toWebmailCopy;
  public String getToWebmailCopy() {
    return toWebmailCopy;
  }
  public void setToWebmailCopy(String toWebmailCopy) {
    this.toWebmailCopy = toWebmailCopy;
  }
  public String getToWebmailSecret() {
    return toWebmailSecret;
  }
  public void setToWebmailSecret(String toWebmailSecret) {
    this.toWebmailSecret = toWebmailSecret;
  }
  private String toWebmailSecret;
  private String compressContent;
  private String fromWebmailId;
  private String webmailContent;
  private String webmailFlag;
  private String recvFromName;
  private String recvFrom;
  private String recvToId;
  private String recvTo;
  private String isWebmail;
  
  
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }

  public String getCopyToId(){
    return copyToId;
  }
  public void setCopyToId(String copyToId){
    this.copyToId = copyToId;
  }
  public int getFromId(){
    return fromId;
  }
  public void setFromId(int fromId){
    this.fromId = fromId;
  }
  public String getToId(){
    return toId;
  }
  public void setToId(String toId){
    this.toId = toId;
  }
  public String getSecretToId(){
    return secretToId;
  }
  public void setSecretToId(String secretToId){
    this.secretToId = secretToId;
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
    if(content != null && !"".equals(content)){
      content = content.replaceAll("\"", "\\\"").replaceAll("\r", "").replaceAll("\n", "");
    }
    this.content = content;
  }
  public Date getSendTime(){
    return sendTime;
  }
  public void setSendTime(Date sendTime){
    this.sendTime = sendTime;
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
  public String getSendFlag(){
    return sendFlag;
  }
  public void setSendFlag(String sendFlag){
    this.sendFlag = sendFlag;
  }
  public String getSmsRemind(){
    return smsRemind;
  }
  public void setSmsRemind(String smsRemind){
    this.smsRemind = smsRemind;
  }
  public String getImportant(){
    return important;
  }
  public void setImportant(String important){
    this.important = important;
  }
  public long getEnsize(){
    return ensize;
  }
  public void setEnsize(long ensize){
    this.ensize = ensize;
  }
  public void setEnsize(int ensize){
    this.ensize = ensize;
  }
  public String getFromWebmail(){
    return fromWebmail;
  }
  public void setFromWebmail(String fromWebmail){
    this.fromWebmail = fromWebmail;
  }
  public String getToWebmail(){
    return toWebmail;
  }
  public void setToWebmail(String toWebmail){
    this.toWebmail = toWebmail;
  }
  public String getCompressContent(){
    return compressContent;
  }
  public void setCompressContent(String compressContent){
    this.compressContent = compressContent;
  }
  
  public String getFromWebmailId() {
    return fromWebmailId;
  }
  public void setFromWebmailId(String fromWebmailId) {
    this.fromWebmailId = fromWebmailId;
  }
  public String getWebmailContent() {
    return webmailContent;
  }
  public void setWebmailContent(String webmailContent) {
    this.webmailContent = webmailContent;
  }
  public String getWebmailFlag() {
    return webmailFlag;
  }
  public void setWebmailFlag(String webmailFlag) {
    this.webmailFlag = webmailFlag;
  }
  public String getRecvFromName() {
    return recvFromName;
  }
  public void setRecvFromName(String recvFromName) {
    this.recvFromName = recvFromName;
  }
  public String getRecvFrom() {
    return recvFrom;
  }
  public void setRecvFrom(String recvFrom) {
    this.recvFrom = recvFrom;
  }
  public String getRecvToId() {
    return recvToId;
  }
  public void setRecvToId(String recvToId) {
    this.recvToId = recvToId;
  }
  public String getRecvTo() {
    return recvTo;
  }
  public void setRecvTo(String recvTo) {
    this.recvTo = recvTo;
  }
  public String getIsWebmail() {
    return isWebmail;
  }
  public void setIsWebmail(String isWebmail) {
    this.isWebmail = isWebmail;
  }
  @Override
  public String toString() {
    return "YHEmailBody [attachmentId=" + attachmentId + ", attachmentName="
        + attachmentName + ", compressContent=" + compressContent
        + ", content=" + content + ", copyToId=" + copyToId + ", ensize="
        + ensize + ", fromId=" + fromId + ", fromWebmail=" + fromWebmail
        + ", fromWebmailId=" + fromWebmailId + ", important=" + important
        + ", isWebmail=" + isWebmail + ", recvFrom=" + recvFrom
        + ", recvFromName=" + recvFromName + ", recvTo=" + recvTo
        + ", recvToId=" + recvToId + ", secretToId=" + secretToId
        + ", sendFlag=" + sendFlag + ", sendTime=" + sendTime + ", seqId="
        + seqId + ", smsRemind=" + smsRemind + ", subject=" + subject
        + ", toId=" + toId + ", toWebmail=" + toWebmail + ", webmailContent="
        + webmailContent + ", webmailFlag=" + webmailFlag + "]";
  }
  
}
