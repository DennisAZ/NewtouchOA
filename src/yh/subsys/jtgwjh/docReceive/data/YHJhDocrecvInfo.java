package yh.subsys.jtgwjh.docReceive.data;

import java.util.Date;



public class YHJhDocrecvInfo {
  private int seqId;//SEQ_ID  number(38)  自增ID  是 
  private int docsendId;//DOCSEND_ID  INT(10)     发文ID号，与外键jh_docsend_info.sql_id关联。
  private String sendDept;//SEND_DEPT   VARCHAR(20) Y   发文单位ID
  private String sendDeptName;//SEND_DEPT_NAME  VARCHAR(50) Y   发文单位名称
  private Date sendDatetime;//SEND_DATETIME DATETIME      发文时间
  private String docTitle;//DOC_TITLE VARCHAR(400)  Y   文件标题
  private String docType;// DOC_TYPE  VARCHAR(10)   0 文件类型：0-普通，1-公文
  private String docKind;//DOC_KIND  VARCHAR(10)     文件种类
  private String urgentType;//URGENT_TYPE VARCHAR(10)   0 紧急程度：0-一般，1-紧急，2-特急
  private String securityLevel;//SECURITY_LEVEL  VARCHAR(10)   0 密级。0：非密，1：秘密，2：机密
  private String securityTime;//SECURITY_TIME VARCHAR(10)   0 保密期限
  private int printCount;//PRINT_COUNT INT(11)   0 打印份数
  private String docNo;//DOC_NO  VARCHAR(50)     文号
  private int pageCount;//PAGE_COUNT  INT(11)   0 文件页数
  private int attachCount;//ATTACH_COUNT  INT(11)   0 附件数
  private String remark;//REMARK  TEXT      备注
  private String mainDocId;//MAIN_DOC_ID VARCHAR(50)     正文ID
  private String mainDocName;//MAIN_DOC_NAME VARCHAR(400)      正文名称
  private String attachmentId;// ATTACHMENT_ID TEXT      附件ID字符串，以逗号分隔
  private String attachmentName;//ATTACHMENT_NAME TEXT      附件名称字符串，以逗号分隔
  private String receiveUser;//RECEIVE_USER  VARCHAR(20)     收文人ID
  private String receiveUserName;//RECEIVE_USER_NAME VARCHAR(20)     收文人姓名
  private String receiveDept;//RECEIVE_DEPT  VARCHAR(20)     收文人部门ID
  private String receiveDeptName;//RECEIVE_DEPT_NAME VARCHAR(50)     收文部门名称
  private Date receiveDatetime;//RECEIVE_DATETIME  DATETIME      收文时间
  private String status;//STATUS  VARCHAR(20)     处理状态 0：未接收 1：已接收2：
  private String returnReason;//
  private String printNoStart;//PRINT_NO_START
  private String printNoEnd;//PRINT_NO_END
  private String guid ;//
  private String attachmentSize;
  private String handStatus;
  
  private String isSign ;
  
   public String getIsSign() {
    return isSign;
  }
  public void setIsSign(String isSign) {
    this.isSign = isSign;
  }
  public String getHandStatus() {
    return handStatus;
  }
  public void setHandStatus(String handStatus) {
    this.handStatus = handStatus;
  }



  /*
    * 集团OA
    */
  private String oaMainSend;
  private String oaCopySend;
  private int paperPrintCount;

  private int totalPrintCount;
  private Date sendDatetimeShow;
  public String getOaMainSend() {
    return oaMainSend;
  }
  public void setOaMainSend(String oaMainSend) {
    this.oaMainSend = oaMainSend;
  }
  public String getOaCopySend() {
    return oaCopySend;
  }
  public void setOaCopySend(String oaCopySend) {
    this.oaCopySend = oaCopySend;
  }
  public int getPaperPrintCount() {
    return paperPrintCount;
  }
  public void setPaperPrintCount(int paperPrintCount) {
    this.paperPrintCount = paperPrintCount;
  }
  public int getTotalPrintCount() {
    return totalPrintCount;
  }
  public void setTotalPrintCount(int totalPrintCount) {
    this.totalPrintCount = totalPrintCount;
  }
  public Date getSendDatetimeShow() {
    return sendDatetimeShow;
  }
  public void setSendDatetimeShow(Date sendDatetimeShow) {
    this.sendDatetimeShow = sendDatetimeShow;
  }


  
  private String sessiontoken;//SESSIONTOKEN,person
  public String getSessiontoken() {
    return sessiontoken;
  }
  public void setSessiontoken(String sessiontoken) {
    this.sessiontoken = sessiontoken;
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
  public String getReturnReason() {
    return returnReason;
  }
  public void setReturnReason(String returnReason) {
    this.returnReason = returnReason;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getDocsendId() {
    return docsendId;
  }
  public void setDocsendId(int docsendId) {
    this.docsendId = docsendId;
  }
  public String getSendDept() {
    return sendDept;
  }
  public void setSendDept(String sendDept) {
    this.sendDept = sendDept;
  }
  public String getSendDeptName() {
    return sendDeptName;
  }
  public void setSendDeptName(String sendDeptName) {
    this.sendDeptName = sendDeptName;
  }
  public Date getSendDatetime() {
    return sendDatetime;
  }
  public void setSendDatetime(Date sendDatetime) {
    this.sendDatetime = sendDatetime;
  }
  public String getDocTitle() {
    return docTitle;
  }
  public void setDocTitle(String docTitle) {
    this.docTitle = docTitle;
  }
  public String getDocType() {
    return docType;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  public String getDocKind() {
    return docKind;
  }
  public void setDocKind(String docKind) {
    this.docKind = docKind;
  }
  public String getUrgentType() {
    return urgentType;
  }
  public void setUrgentType(String urgentType) {
    this.urgentType = urgentType;
  }
  public String getSecurityLevel() {
    return securityLevel;
  }
  public void setSecurityLevel(String securityLevel) {
    this.securityLevel = securityLevel;
  }
  public String getSecurityTime() {
    return securityTime;
  }
  public void setSecurityTime(String securityTime) {
    this.securityTime = securityTime;
  }
  public int getPrintCount() {
    return printCount;
  }
  public void setPrintCount(int printCount) {
    this.printCount = printCount;
  }
  public String getDocNo() {
    return docNo;
  }
  public void setDocNo(String docNo) {
    this.docNo = docNo;
  }
  public int getPageCount() {
    return pageCount;
  }
  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }
  public int getAttachCount() {
    return attachCount;
  }
  public void setAttachCount(int attachCount) {
    this.attachCount = attachCount;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
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
  public String getReceiveUser() {
    return receiveUser;
  }
  public void setReceiveUser(String receiveUser) {
    this.receiveUser = receiveUser;
  }
  public String getReceiveUserName() {
    return receiveUserName;
  }
  public void setReceiveUserName(String receiveUserName) {
    this.receiveUserName = receiveUserName;
  }
  public String getReceiveDept() {
    return receiveDept;
  }
  public void setReceiveDept(String receiveDept) {
    this.receiveDept = receiveDept;
  }
  public String getReceiveDeptName() {
    return receiveDeptName;
  }
  public void setReceiveDeptName(String receiveDeptName) {
    this.receiveDeptName = receiveDeptName;
  }
  public Date getReceiveDatetime() {
    return receiveDatetime;
  }
  public void setReceiveDatetime(Date receiveDatetime) {
    this.receiveDatetime = receiveDatetime;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public String toString(){
    return "seqId=" + this.seqId + ";发送单位名称=" + this.sendDeptName + ";文件标题=" + this.docTitle + ";文号=" + this.docNo;
  }
}
