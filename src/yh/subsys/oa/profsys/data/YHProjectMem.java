package yh.subsys.oa.profsys.data;

import java.util.Date;

public class YHProjectMem {
  private int seqId;//SEQ_ID  int 流水号
  private int projId;//PROJ_ID INT 
  private String projCreator;//PROJ_CREATOR  VARCHAR(20) 
  private Date projDate;//PROJ_DATE DATE  新建时间
  private String memNum;//MEM_NUM varchar(20) 成员编号
  private String memRole;//IMPORTANT_ROLE  varchar(200)  成员身份
  private String memName;//MEM_NAME  Varchar(20) 姓名（ID）
  private String memSex;//MEM_SEX varchar(20) 性别
  private String memPosition;//MEM_POSITION  varchar(20) 职务(角色)
  private String memNation;//MEM_NATION  varchar(20) 名族
  private String memNativePlace;//MEM_NATIVE_PLACE  varchar(20) 籍贯
  private String memBirthplace;//MEM_BIRTHPLACE  varchar(200)  出生地
  private Date memBirth;//MEM_BIRTH date  出生年月
  private String memIdNum;//MEM_ID_NUM  varchar(20) 证件号码
  private String memPhone;//MEM_PHONE varchar(20) 电话
  private String memMail;// MEM_MAIL  varchar(100)  邮箱
  private String memFax;//MEM_FAX varchar(100)  传真
  private String memAddress;//MEM_ADDRESS varchar(100)  地址
  private String memNote;//MEM_NOTE  Clob  备注
  private String unitNum;//UNIT_NUM  VARCHAR(20) 单位编号
  private String includeFn;//INCLUDE_FN  VARCHAR(10) 判断是否含驻京外国人，友协null和on(YH 0：不包含1：包含)
  private String unitName;//UNIT_NAME VARCHAR(20) 单位名称
  private String unitManNum;//UNIT_MAN_NUM  VARCHAR(20) 人数
  private String attachmentId;//ATTACHMENT_ID Clob  附件ID
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件名称
  private String projMemType;//PROJ_MEM_TYPE  VARCHAR(2)  项目成员类别 0：来访项目 1：出访项目2：大型活动项目

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
  public Date getProjDate() {
    return projDate;
  }
  public void setProjDate(Date projDate) {
    this.projDate = projDate;
  }
  public String getMemNum() {
    return memNum;
  }
  public void setMemNum(String memNum) {
    this.memNum = memNum;
  }
  public String getMemRole() {
    return memRole;
  }
  public void setMemRole(String memRole) {
    this.memRole = memRole;
  }
  public String getMemName() {
    return memName;
  }
  public void setMemName(String memName) {
    this.memName = memName;
  }
  public String getMemSex() {
    return memSex;
  }
  public void setMemSex(String memSex) {
    this.memSex = memSex;
  }
  public String getMemPosition() {
    return memPosition;
  }
  public void setMemPosition(String memPosition) {
    this.memPosition = memPosition;
  }
  public String getMemNation() {
    return memNation;
  }
  public void setMemNation(String memNation) {
    this.memNation = memNation;
  }
  public String getMemNativePlace() {
    return memNativePlace;
  }
  public void setMemNativePlace(String memNativePlace) {
    this.memNativePlace = memNativePlace;
  }
  public String getMemBirthplace() {
    return memBirthplace;
  }
  public void setMemBirthplace(String memBirthplace) {
    this.memBirthplace = memBirthplace;
  }
  public Date getMemBirth() {
    return memBirth;
  }
  public void setMemBirth(Date memBirth) {
    this.memBirth = memBirth;
  }
  public String getMemIdNum() {
    return memIdNum;
  }
  public void setMemIdNum(String memIdNum) {
    this.memIdNum = memIdNum;
  }
  public String getMemPhone() {
    return memPhone;
  }
  public void setMemPhone(String memPhone) {
    this.memPhone = memPhone;
  }
  public String getMemMail() {
    return memMail;
  }
  public void setMemMail(String memMail) {
    this.memMail = memMail;
  }
  public String getMemFax() {
    return memFax;
  }
  public void setMemFax(String memFax) {
    this.memFax = memFax;
  }
  public String getMemAddress() {
    return memAddress;
  }
  public void setMemAddress(String memAddress) {
    this.memAddress = memAddress;
  }
  public String getMemNote() {
    return memNote;
  }
  public void setMemNote(String memNote) {
    this.memNote = memNote;
  }
  public String getUnitNum() {
    return unitNum;
  }
  public void setUnitNum(String unitNum) {
    this.unitNum = unitNum;
  }
  public String getIncludeFn() {
    return includeFn;
  }
  public void setIncludeFn(String includeFn) {
    this.includeFn = includeFn;
  }
  public String getUnitName() {
    return unitName;
  }
  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }
  public String getUnitManNum() {
    return unitManNum;
  }
  public void setUnitManNum(String unitManNum) {
    this.unitManNum = unitManNum;
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
  public String getProjMemType() {
    return projMemType;
  }
  public void setProjMemType(String projMemType) {
    this.projMemType = projMemType;
  }

}
