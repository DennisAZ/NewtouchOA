package yh.subsys.jtgwjh.docSend.data;

import java.util.Date;

import yh.core.util.YHUtility;


public class YHJhDocsendTasks{

  private int seqId;
  private int docsendInfoId;//发文登记表
  private String reciveDept;
  private String reciveDeptDesc;//接收单位
  private String mainDocId;//正文ID
  private String mainDocName;//正文名称
  private String attachmentId;//附件id
  private String attachmentName;//附件名称
  private int printCount;//打印份数
  private String printNoStart;//打印开始编号，三位数字，不足用前导0补足
  private String printNoEnd;//打印结束编号，三位数字，不足用前导0补足
  private String status;//状态：0-草拟，1-待发，2-已发，3-已接收
  private Date processTime;//处理时间
  private String guid;
  private String attachmentSize;//附件size

  public String toXML(){
    String str = "<seqId>"+seqId+"</seqId>"
               + "<docsendInfoId>"+docsendInfoId+"</docsendInfoId>"
               + "<reciveDept>"+reciveDept+"</reciveDept>"
               + "<reciveDeptDesc>"+reciveDeptDesc+"</reciveDeptDesc>"
               + "<mainDocId>"+YHUtility.null2Empty(mainDocId)+"</mainDocId>"
               + "<mainDocName>"+YHUtility.null2Empty(mainDocName)+"</mainDocName>"
               + "<attachmentId>"+attachmentId+"</attachmentId>"
               + "<attachmentName>"+attachmentName+"</attachmentName>"
               + "<printCount>"+printCount+"</printCount>"
               + "<printNoStart>"+printNoStart+"</printNoStart>"
               + "<printNoEnd>"+printNoEnd+"</printNoEnd>"
               + "<status>"+status+"</status>"
               + "<processTime>"+processTime.toString().substring(0, 19)+"</processTime>"
               + "<attachmentSize>"+attachmentSize+"</attachmentSize>"
               + "<guid>"+YHUtility.null2Empty(guid)+"</guid>";
    return str;
  }
  
  public String getReciveDept() {
    return reciveDept;
  }

  public void setReciveDept(String reciveDept) {
    this.reciveDept = reciveDept;
  }

  public String getAttachmentSize() {
    return attachmentSize;
  }
  public void setAttachmentSize(String attachmentSize) {
    this.attachmentSize = attachmentSize;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getDocsendInfoId() {
    return docsendInfoId;
  }
  public void setDocsendInfoId(int docsendInfoId) {
    this.docsendInfoId = docsendInfoId;
  }
  public int getPrintCount() {
    return printCount;
  }
  public void setPrintCount(int printCount) {
    this.printCount = printCount;
  }
  public String getReciveDeptDesc() {
    return reciveDeptDesc;
  }
  public void setReciveDeptDesc(String reciveDeptDesc) {
    this.reciveDeptDesc = reciveDeptDesc;
  }
  public String getPrintNoStart() {
    return printNoStart;
  }
  public void setPrintNoStart(String printNoStart) {
    this.printNoStart = printNoStart;
  }
  public String getPrintNoEnd() {
    return printNoEnd;
  }
  public void setPrintNoEnd(String printNoEnd) {
    this.printNoEnd = printNoEnd;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public Date getProcessTime() {
    return processTime;
  }
  public void setProcessTime(Date processTime) {
    this.processTime = processTime;
  }
  public String getMainDocId() {
    return mainDocId;
  }
  public void setMainDocId(String mainDocId) {
    this.mainDocId = mainDocId;
  }
  public String getMainDocName() {
    return mainDocName;
  }
  public void setMainDocName(String mainDocName) {
    this.mainDocName = mainDocName;
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
  
}
