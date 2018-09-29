package yh.subsys.jtgwjh.docSend.data;

import java.util.Date;
import java.util.List;

import yh.core.util.YHUtility;

public class YHJhDocsendInfo{

  private int seqId;// 自增，索引
  private int runId;//集团公文oa(run_id)
  private int forwordId;// 转发文id号，与外键doc_receive.sql_id关联。如大于0表示此文为转发文件
  private String docTitle;//文件标题
  private String docType;//文件类型：0-普通，1-公文
  private String docKind;//文件种类
  private String urgentType;//紧急程度：0-一般，1-紧急，2-特急
  private String securityLevel;//密级。0：非密，1：秘密，2：机密
  private String securityTime;//保密期限
  private int printCount;//打印份数
  private String docNo;//文号
  private int pageCount;//文件页数
  private int attachCount;//附件数
  private String remark;//备注
  private String reciveDept;//联网接收单位id字符串，以半角逗号分隔
  private String reciveDeptDesc;//联网接受单位名称
  private String handReciveDept;//手工接受单位信息
  private String mainDocId;//正文id
  private String mainDocName;//正文名称
  private String attachmentId;//附件id字符串，以逗号分隔
  private String attachmentName;//附件名称字符串，以逗号分隔
  private int createUser;//创建人id
  private String createUserName;//创建人姓名
  private int createDept;//创建人部门id
  private String createDeptName;//创建部门名称
  private Date createDatetime;//创建时间
  private Date sendDatetime;//发文发送时间
  private String isSign;
  private String isStamp;
  private String stampComplete;
  private String docStyle;
  private String reciveDeptFlag;
  private String oaMainSend;
  private String oaCopySend;
  private int paperPrintCount;
  private int totalPrintCount;
  private Date sendDatetimeShow;//发文发送时间

  public Date getSendDatetimeShow() {
    return sendDatetimeShow;
  }

  public void setSendDatetimeShow(Date sendDatetimeShow) {
    this.sendDatetimeShow = sendDatetimeShow;
  }

  public int getTotalPrintCount() {
    return totalPrintCount;
  }

  public void setTotalPrintCount(int totalPrintCount) {
    this.totalPrintCount = totalPrintCount;
  }

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

  public String getReciveDeptFlag() {
    return reciveDeptFlag;
  }

  public void setReciveDeptFlag(String reciveDeptFlag) {
    this.reciveDeptFlag = reciveDeptFlag;
  }

  public String toString(){
    return "seqId：" + seqId + ",公文标题：" + docTitle;
  }
  
  public String toXML(List<YHJhDocsendTasks> docsendTasksList){
    
    StringBuffer docsendTasksStr = new StringBuffer();
    for(YHJhDocsendTasks docsendTasks : docsendTasksList){
      docsendTasksStr.append("<task>" + docsendTasks.toXML() + "</task>");
    }
    String str = "<?xml version='1.0' encoding='UTF-8'?>"
               + "<body>"
               + "<seqId>"+seqId+"</seqId>"
               + "<runId>"+runId+"</runId>"
               + "<forwordId>"+forwordId+"</forwordId>"
               + "<docTitle>"+docTitle+"</docTitle>"
               + "<docType>"+docType+"</docType>"
               + "<docKind>"+docKind+"</docKind>"
               + "<urgentType>"+urgentType+"</urgentType>"
               + "<securityLevel>"+securityLevel+"</securityLevel>"
               + "<securityTime>"+securityTime+"</securityTime>"
               + "<printCount>"+printCount+"</printCount>"
               + "<docNo>"+docNo+"</docNo>"
               + "<pageCount>"+pageCount+"</pageCount>"
               + "<attachCount>"+attachCount+"</attachCount>"
               + "<remark>"+remark+"</remark>"
               + "<reciveDept>"+reciveDept+"</reciveDept>"
               + "<reciveDeptDesc>"+reciveDeptDesc+"</reciveDeptDesc>"
               + "<handReciveDept>"+YHUtility.null2Empty(handReciveDept)+"</handReciveDept>"
               + "<mainDocId>"+mainDocId+"</mainDocId>"
               + "<mainDocName>"+mainDocName+"</mainDocName>"
               + "<attachmentId>"+attachmentId+"</attachmentId>"
               + "<attachmentName>"+attachmentName+"</attachmentName>"
               + "<createUser>"+createUser+"</createUser>"
               + "<createUserName>"+createUserName+"</createUserName>"
               + "<createDept>"+createDept+"</createDept>"
               + "<createDeptName>"+createDeptName+"</createDeptName>"
               + "<createDatetime>"+createDatetime.toString().substring(0, 19)+"</createDatetime>"
               + "<sendDatetime>"+sendDatetime.toString().substring(0, 19)+"</sendDatetime>"
               + "<isSign>"+isSign+"</isSign>"
               + "<tasks>"
               + docsendTasksStr.toString()
               + "</tasks>"
               + "</body>";
    return str;
  }
  public String getDocStyle() {
    return docStyle;
  }

  public void setDocStyle(String docStyle) {
    this.docStyle = docStyle;
  }
  public String getStampComplete() {
    return stampComplete;
  }
  public void setStampComplete(String stampComplete) {
    this.stampComplete = stampComplete;
  }
  public String getIsStamp() {
    return isStamp;
  }
  public void setIsStamp(String isStamp) {
    this.isStamp = isStamp;
  }
  public String getIsSign() {
    return isSign;
  }
  public void setIsSign(String isSign) {
    this.isSign = isSign;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getRunId() {
    return runId;
  }
  public void setRunId(int runId) {
    this.runId = runId;
  }
  public int getForwordId() {
    return forwordId;
  }
  public void setForwordId(int forwordId) {
    this.forwordId = forwordId;
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
  public String getReciveDept() {
    return reciveDept;
  }
  public void setReciveDept(String reciveDept) {
    this.reciveDept = reciveDept;
  }
  public String getReciveDeptDesc() {
    return reciveDeptDesc;
  }
  public void setReciveDeptDesc(String reciveDeptDesc) {
    this.reciveDeptDesc = reciveDeptDesc;
  }
  public String getHandReciveDept() {
    return handReciveDept;
  }
  public void setHandReciveDept(String handReciveDept) {
    this.handReciveDept = handReciveDept;
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
  public String getCreateUserName() {
    return createUserName;
  }
  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }
  public String getCreateDeptName() {
    return createDeptName;
  }
  public void setCreateDeptName(String createDeptName) {
    this.createDeptName = createDeptName;
  }
  public Date getCreateDatetime() {
    return createDatetime;
  }
  public void setCreateDatetime(Date createDatetime) {
    this.createDatetime = createDatetime;
  }
  public Date getSendDatetime() {
    return sendDatetime;
  }
  public void setSendDatetime(Date sendDatetime) {
    this.sendDatetime = sendDatetime;
  }
  public int getCreateUser() {
    return createUser;
  }
  public void setCreateUser(int createUser) {
    this.createUser = createUser;
  }
  public int getCreateDept() {
    return createDept;
  }
  public void setCreateDept(int createDept) {
    this.createDept = createDept;
  }
}
