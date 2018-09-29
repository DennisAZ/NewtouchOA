package yh.subsys.oa.profsys.data;

import java.util.Date;

public class YHProjectComm {
  private int seqId;//SEQ_ID  int 流水号
  private int projId;//PROJ_ID Int 来访项目ID
  private String projCreator;//PROJ_CREATOR  VARCHAR(20) 用户ID
  private Date projDate;//PROJ_DATA CLOB 
  private String commNum;//COMM_NUM  VARCHAR(20) 纪要编号
  private String commName;//COMM_NAME VARCHAR(50) 纪要名称
  private String commMemCn;//COMM_MEM_CN VARCHAR(20) 中方人员
  private String commMemFn;//COMM_MEM_FN varchar(20) 外方人员
  private Date commTime;//COMM_TIME DATE  时间
  private String commPlace;//COMM_PLACE  VARCHAR(50) 地点
  private String commContent;//COMM_CONTENT  CLOB  内容
  private String commNote;//COMM_NOTE CLOB  备注
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID
  private String attachmentName;//ATTACHMENT_NAME varchar(200)  附件名称
  private String projCommType;//PROJ_COMM_TYPE VARCHAR(2)  项目纪要类别0：来访项目1：出访项目2：大型活动项目
  public Date getProjDate() {
    return projDate;
  }
  public void setProjDate(Date projDate) {
    this.projDate = projDate;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getProjId() {
    return projId;
  }
  public void setProjId(int projId) {
    this.projId = projId;
  }
  public String getProjCreator() {
    return projCreator;
  }
  public void setProjCreator(String projCreator) {
    this.projCreator = projCreator;
  }
  public String getCommNum() {
    return commNum;
  }
  public void setCommNum(String commNum) {
    this.commNum = commNum;
  }
  public String getCommName() {
    return commName;
  }
  public void setCommName(String commName) {
    this.commName = commName;
  }
  public String getCommMemCn() {
    return commMemCn;
  }
  public void setCommMemCn(String commMemCn) {
    this.commMemCn = commMemCn;
  }
  public String getCommMemFn() {
    return commMemFn;
  }
  public void setCommMemFn(String commMemFn) {
    this.commMemFn = commMemFn;
  }
  public Date getCommTime() {
    return commTime;
  }
  public void setCommTime(Date commTime) {
    this.commTime = commTime;
  }
  public String getCommPlace() {
    return commPlace;
  }
  public void setCommPlace(String commPlace) {
    this.commPlace = commPlace;
  }
  public String getCommContent() {
    return commContent;
  }
  public void setCommContent(String commContent) {
    this.commContent = commContent;
  }
  public String getCommNote() {
    return commNote;
  }
  public void setCommNote(String commNote) {
    this.commNote = commNote;
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
  public String getProjCommType() {
    return projCommType;
  }
  public void setProjCommType(String projCommType) {
    this.projCommType = projCommType;
  }

}
