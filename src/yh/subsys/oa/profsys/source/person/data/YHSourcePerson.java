package yh.subsys.oa.profsys.source.person.data;

import java.util.Date;

public class YHSourcePerson {
  private int seqId;//SEQ_ID  int 流水号
  private String perNum;//PER_NUM varchar(40) 人员编号
  private String perName;//PER_NAME  varchar(40) 姓名
  private String perSex;//PER_SEX varchar(10) 性别
  private Date perBirthday;//PER_BIRTHDAY  DATE  出生年月
  private String perNation;//PER_NATION  varchar(50) 国籍
  private String perVocation;//PER_VOCATION  varchar(50) 职业
  private String perPosition;//PER_POSITION  CLOB  职务（角色ID字符串）
  private String perResume;//PER_RESUME  CLOB  简介
  private String perExperience;//PER_EXPERIENCE  CLOB  来华经历
  private String perNote;//PER_NOTE  CLOB  备注
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID字符串
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件名称字符串
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getPerNum() {
    return perNum;
  }
  public void setPerNum(String perNum) {
    this.perNum = perNum;
  }
  public String getPerName() {
    return perName;
  }
  public void setPerName(String perName) {
    this.perName = perName;
  }
  public String getPerSex() {
    return perSex;
  }
  public void setPerSex(String perSex) {
    this.perSex = perSex;
  }
  public Date getPerBirthday() {
    return perBirthday;
  }
  public void setPerBirthday(Date perBirthday) {
    this.perBirthday = perBirthday;
  }
  public String getPerNation() {
    return perNation;
  }
  public void setPerNation(String perNation) {
    this.perNation = perNation;
  }
  public String getPerVocation() {
    return perVocation;
  }
  public void setPerVocation(String perVocation) {
    this.perVocation = perVocation;
  }
  public String getPerPosition() {
    return perPosition;
  }
  public void setPerPosition(String perPosition) {
    this.perPosition = perPosition;
  }
  public String getPerResume() {
    return perResume;
  }
  public void setPerResume(String perResume) {
    this.perResume = perResume;
  }
  public String getPerExperience() {
    return perExperience;
  }
  public void setPerExperience(String perExperience) {
    this.perExperience = perExperience;
  }
  public String getPerNote() {
    return perNote;
  }
  public void setPerNote(String perNote) {
    this.perNote = perNote;
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
