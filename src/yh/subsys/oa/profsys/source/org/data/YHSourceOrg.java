package yh.subsys.oa.profsys.source.org.data;

import java.util.Date;

public class YHSourceOrg {
  private int seqId ;//SEQ_ID  int 流水号
  private String orgNum;//ORG_NUM varchar(20) 组织编号
  private String orgName;//ORG_NAME  varchar(100)  组织名称
  private String orgNation;//ORG_NATION  varchar(40) 国别
  private String orgLeader;//ORG_LEADER  varchar(100)  领导人ID
  private String orgScale;//ORG_SCALE Varchar（40) 规模
  private Date orgEstablishTime ; //ORG_ESTABLISH_TIME  Date  成立时间
  private String orgActive ;//ORG_ACTIVE  CLOB  主要从事活动
  private String orgContact ; //ORG_CONTACT CLOB  交往情况
  private String orgPublication ; //ORG_PUBLICATION CLOB  发行刊物
  private String orgNote ;//ORG_NOTE  CLOB  备注
  private String attachmentId ;//ATTACHMENT_ID CLOB  附件ID字符串
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件名称字符串
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getOrgNum() {
    return orgNum;
  }
  public void setOrgNum(String orgNum) {
    this.orgNum = orgNum;
  }
  public String getOrgName() {
    return orgName;
  }
  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
  public String getOrgNation() {
    return orgNation;
  }
  public void setOrgNation(String orgNation) {
    this.orgNation = orgNation;
  }
  public String getOrgLeader() {
    return orgLeader;
  }
  public void setOrgLeader(String orgLeader) {
    this.orgLeader = orgLeader;
  }
  public String getOrgScale() {
    return orgScale;
  }
  public void setOrgScale(String orgScale) {
    this.orgScale = orgScale;
  }
  public Date getOrgEstablishTime() {
    return orgEstablishTime;
  }
  public void setOrgEstablishTime(Date orgEstablishTime) {
    this.orgEstablishTime = orgEstablishTime;
  }
  public String getOrgActive() {
    return orgActive;
  }
  public void setOrgActive(String orgActive) {
    this.orgActive = orgActive;
  }

  public String getOrgContact() {
    return orgContact;
  }
  public void setOrgContact(String orgContact) {
    this.orgContact = orgContact;
  }
  public String getOrgPublication() {
    return orgPublication;
  }
  public void setOrgPublication(String orgPublication) {
    this.orgPublication = orgPublication;
  }
  public String getOrgNote() {
    return orgNote;
  }
  public void setOrgNote(String orgNote) {
    this.orgNote = orgNote;
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
