package yh.subsys.oa.profsys.data;

import java.util.Date;

public class YHProject {
  private int seqId;// 流水号
  private String projNum;//PROJ_NUM  varchar(20)   项目编号
  private String projGroupName;//PROJ_GROUP_NAME varchar(20) 团组名称
  private String projVisitType;//PROJ_VISIT_TYPE varchar(20) 来访类别
  private String projActiveType;//PROJ_ACTIVE_TYPE  varchar(20)   项目类别
  private String projLeader;//PROJ_LEADER varchar(20) 负责人ID
  private String projManager;//PROJ_MANAGER  varchar(20)   执行负责人
  private Date projArriveTime;//PROJ_ARRIVE_TIME  DATE  到京时间
  private Date projLeaveTime;//PROJ_LEAVE_TIME DATE  离京时间
  private Date projStartTime;//PROJ_START_TIME DATE  开始时间
  private Date projEndTime;//PROJ_END_TIME DATE  结束时间
  private String projTitle;//PROJ_LIST CLOB  
  private String projList;//PROJ_LIST CLOB  活动人员构成  
  private String projOrganizer;// PROJ_ORGANIZER  BLOB  主办单位
  private String projOperator;//PROJ_OPERATOR CLOB  承办单位
  private String projSponsor;//PROJ_SPONSOR  CLOB  支持单位
  private String projDept;//PROJ_DEPT clob  参与部门
  private String projViwer;//PROJ_VIWER  clob    项目审批人
  private String projLeaderDescription;//PROJ_LEADER_DESCRIPTION CLOB  主要领导情况
  private String projUnitDescription;//PROJ_UNIT_DESCRIPTION CLOB  组团单位情况
  private String projNote;//PROJ_NOTE CLOB  备注
  private String projStatus;//PROJ_STATUS varchar(20) 接待状态  注：根据时间自动判断0：正常（根据时间判断 已接待、准备中、接待中）1:结束状态
  private String saveFlag;//SAVE_FLAG Varchar(10) 归档状态0：未归档1：已归档
  private String attachmentId;//ATTACHMENT_ID CLOB  附件ID
  private String attachmentName;//ATTACHMENT_NAME CLOB  附件文档
  private String projCreator;//PROJ_CREATOR  VARCHAR(20) 项目申请人
  private String projUser;//PROJ_USER VARCHAR(20) 暂没用上
  private int pTotal;//P_TOTAL INT 参与总人数
  private int pYx;//P_YX  INT 参与友协人员
  private int pCouncil;//P_COUNCIL INT 参与理事人数
  private int pGuest;//P_GUEST INT 参与理事人数
  private String purposeCountry;//PURPOSE_COUNTRY CLOB  来访国家
  private int countryTotal;//COUNTRY_TOTAL INT 来访国家总数
  private int deptId;//DEPT_ID INT 登录用户部门ID
  private Date projDate;//PROJ_DATE DATE  申请日期 
  private String printStatus;//PRINT_STATUS  VARCHAR(20) 打印状态
  private int budgetId;//BUDGET_ID INT 财务：SEQ_ID
  private String projType;//PROJ_TYPE VARCHAR(2)  项目类别0：来访项目1：出访项目2：大型活动项目
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getProjNum() {
    return projNum;
  }
  public void setProjNum(String projNum) {
    this.projNum = projNum;
  }
  public String getProjGroupName() {
    return projGroupName;
  }
  public void setProjGroupName(String projGroupName) {
    this.projGroupName = projGroupName;
  }
  public String getProjVisitType() {
    return projVisitType;
  }
  public void setProjVisitType(String projVisitType) {
    this.projVisitType = projVisitType;
  }
  public String getProjActiveType() {
    return projActiveType;
  }
  public void setProjActiveType(String projActiveType) {
    this.projActiveType = projActiveType;
  }
  public String getProjLeader() {
    return projLeader;
  }
  public void setProjLeader(String projLeader) {
    this.projLeader = projLeader;
  }
  public String getProjManager() {
    return projManager;
  }
  public void setProjManager(String projManager) {
    this.projManager = projManager;
  }
  public Date getProjArriveTime() {
    return projArriveTime;
  }
  public void setProjArriveTime(Date projArriveTime) {
    this.projArriveTime = projArriveTime;
  }
  public Date getProjLeaveTime() {
    return projLeaveTime;
  }
  public void setProjLeaveTime(Date projLeaveTime) {
    this.projLeaveTime = projLeaveTime;
  }
  public Date getProjStartTime() {
    return projStartTime;
  }
  public void setProjStartTime(Date projStartTime) {
    this.projStartTime = projStartTime;
  }
  public Date getProjEndTime() {
    return projEndTime;
  }
  public void setProjEndTime(Date projEndTime) {
    this.projEndTime = projEndTime;
  }

  public String getProjTitle() {
    return projTitle;
  }
  public void setProjTitle(String projTitle) {
    this.projTitle = projTitle;
  }
  public String getProjList() {
    return projList;
  }
  public void setProjList(String projList) {
    this.projList = projList;
  }
  public String getProjOrganizer() {
    return projOrganizer;
  }
  public void setProjOrganizer(String projOrganizer) {
    this.projOrganizer = projOrganizer;
  }
  public String getProjOperator() {
    return projOperator;
  }
  public void setProjOperator(String projOperator) {
    this.projOperator = projOperator;
  }
  public String getProjSponsor() {
    return projSponsor;
  }
  public void setProjSponsor(String projSponsor) {
    this.projSponsor = projSponsor;
  }
  public String getProjDept() {
    return projDept;
  }
  public void setProjDept(String projDept) {
    this.projDept = projDept;
  }
  public String getProjViwer() {
    return projViwer;
  }
  public void setProjViwer(String projViwer) {
    this.projViwer = projViwer;
  }
  public String getProjLeaderDescription() {
    return projLeaderDescription;
  }
  public void setProjLeaderDescription(String projLeaderDescription) {
    this.projLeaderDescription = projLeaderDescription;
  }
  public String getProjUnitDescription() {
    return projUnitDescription;
  }
  public void setProjUnitDescription(String projUnitDescription) {
    this.projUnitDescription = projUnitDescription;
  }
  public String getProjNote() {
    return projNote;
  }
  public void setProjNote(String projNote) {
    this.projNote = projNote;
  }
  public String getProjStatus() {
    return projStatus;
  }
  public void setProjStatus(String projStatus) {
    this.projStatus = projStatus;
  }
  public String getSaveFlag() {
    return saveFlag;
  }
  public void setSaveFlag(String saveFlag) {
    this.saveFlag = saveFlag;
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
  public String getProjCreator() {
    return projCreator;
  }
  public void setProjCreator(String projCreator) {
    this.projCreator = projCreator;
  }
  public String getProjUser() {
    return projUser;
  }
  public void setProjUser(String projUser) {
    this.projUser = projUser;
  }
  public int getPTotal() {
    return pTotal;
  }
  public void setPTotal(int pTotal) {
    this.pTotal = pTotal;
  }
  public int getPYx() {
    return pYx;
  }
  public void setPYx(int pYx) {
    this.pYx = pYx;
  }
  public int getPCouncil() {
    return pCouncil;
  }
  public void setPCouncil(int pCouncil) {
    this.pCouncil = pCouncil;
  }
  public int getPGuest() {
    return pGuest;
  }
  public void setPGuest(int pGuest) {
    this.pGuest = pGuest;
  }
  public String getPurposeCountry() {
    return purposeCountry;
  }
  public void setPurposeCountry(String purposeCountry) {
    this.purposeCountry = purposeCountry;
  }
  public int getCountryTotal() {
    return countryTotal;
  }
  public void setCountryTotal(int countryTotal) {
    this.countryTotal = countryTotal;
  }
  public int getDeptId() {
    return deptId;
  }
  public void setDeptId(int deptId) {
    this.deptId = deptId;
  }
  public Date getProjDate() {
    return projDate;
  }
  public void setProjDate(Date projDate) {
    this.projDate = projDate;
  }
  public String getPrintStatus() {
    return printStatus;
  }
  public void setPrintStatus(String printStatus) {
    this.printStatus = printStatus;
  }
  public int getBudgetId() {
    return budgetId;
  }
  public void setBudgetId(int budgetId) {
    this.budgetId = budgetId;
  }
  public String getProjType() {
    return projType;
  }
  public void setProjType(String projType) {
    this.projType = projType;
  }

}
