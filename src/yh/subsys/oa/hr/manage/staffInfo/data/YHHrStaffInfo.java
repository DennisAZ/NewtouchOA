package yh.subsys.oa.hr.manage.staffInfo.data;

import java.util.Date;

public class YHHrStaffInfo {
	private int seqId;
	private String createUserId; // 系统登录人
	private int createDeptId; // 系统登录人部门
	private String userId; // OA用户名
	private int deptId; // 部门名称
	private String staffNo; // 编号
	private String workNo; // 工号
	private String photoName; // 图片名称
	private String workType; // 工种
	private String staffName;// 姓名

	private String staffEName;// 英文名
	private String staffCardNo;// 身份证号
	private String staffSex;// 性别
	private Date staffBirth;// 生日
	private String staffAge;// 年龄
	private String staffNativePlace;// 籍贯
	private String staffDomicilePlace;// 户口所在地
	private String yesOtherP;// 是否异地户口
	private String staffNationality; // 民族
	private String staffMaritalStatus;// 婚姻状况
	// 20

	private String staffPoliticalStatus;// 政治面貌
	private Date joinPartyTime;// 入党时间
	private String staffPhone;// 联系电话
	private String staffMobile;// 手机号码
	private String staffLittleSmart;// 小灵通
	private String staffEmail;// 电子邮件
	private String staffMsn;// MSN
	private String staffQq;// QQ
	private String homeAddress;// 家庭地址
	private String otherContact;// 他联系方式
	// 30

	private Date jobBeginning;// 参加工作时间
	private String workAge;// 总工龄
	private String staffHealth;// 健康状况
	private String staffHighestSchool;// 学历
	private String staffHighestDegree;// 学位
	private Date graduationDate;// 毕业时间
	private String graduationSchool;// 毕业学校
	private String staffMajor;// 专业
	private String computerLevel;// 计算机水平
	private String foreignLanguage1;// 外语语种1
	// 40

	private String foreignLevel1;// 外语水平1
	private String foreignLanguage2;// 外语语种2
	private String foreignLevel2;// 外语水平2
	private String foreignLanguage3;// 外语语种3
	private String foreignLevel3;// 外语水平3
	private String staffSkills;// 特长
	private String staffOccupation;// 员工类型
	private String administrationLevel;// 行政级别
	private String jobPosition;// 职务
	private String presentPosition;// 职称
	// 50

	private Date datesEmployed;// 入职时间
	private String jobAge;// 本单位工龄
	private Date beginSalsryTime;// 起薪时间
	private String workStatus;// 在职状态
	private Date staffCs;// 合同签订时间
	private Date staffCtr;// 合同到期时间
	private String remark;// 备注
	private String staffCompany;// 所在单位
	private String resume;// 简历
	private String attachmentId;// 附件Id
	// 60

	private String attachmentName;// 附件名称
	private Date addTime;// 新建日期
	private int leaveType;// 年休假天数
	private String staffType;// 户口类别
	private String yesOrNot;// 是否允许登录（1是）
	private String certificate;// 职务情况
	private String surety;// 担保记录
	private String bodyExamim;// 体检记录
	private String insure;// 社保缴纳情况
	private String userdef1;// 自定义字段1
	// 70

	private String userdef2;// 自定义字段2
	private String userdef3;// 自定义字段3
	private String userdef4;// 自定义字段4
	private String userdef5;// 自定义字段5
	private Date recordDate;
	
	private String insureNum;//社保号

	public String getInsureNum() {
    return insureNum;
  }

  public void setInsureNum(String insureNum) {
    this.insureNum = insureNum;
  }

  public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public int getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(int createDeptId) {
		this.createDeptId = createDeptId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffEName() {
		return staffEName;
	}

	public void setStaffEName(String staffEName) {
		this.staffEName = staffEName;
	}

	public String getStaffCardNo() {
		return staffCardNo;
	}

	public void setStaffCardNo(String staffCardNo) {
		this.staffCardNo = staffCardNo;
	}

	public String getStaffSex() {
		return staffSex;
	}

	public void setStaffSex(String staffSex) {
		this.staffSex = staffSex;
	}

	public Date getStaffBirth() {
		return staffBirth;
	}

	public void setStaffBirth(Date staffBirth) {
		this.staffBirth = staffBirth;
	}

	public String getStaffAge() {
		return staffAge;
	}

	public void setStaffAge(String staffAge) {
		this.staffAge = staffAge;
	}

	public String getStaffNativePlace() {
		return staffNativePlace;
	}

	public void setStaffNativePlace(String staffNativePlace) {
		this.staffNativePlace = staffNativePlace;
	}

	public String getStaffDomicilePlace() {
		return staffDomicilePlace;
	}

	public void setStaffDomicilePlace(String staffDomicilePlace) {
		this.staffDomicilePlace = staffDomicilePlace;
	}

	public String getYesOtherP() {
		return yesOtherP;
	}

	public void setYesOtherP(String yesOtherP) {
		this.yesOtherP = yesOtherP;
	}

	public String getStaffNationality() {
		return staffNationality;
	}

	public void setStaffNationality(String staffNationality) {
		this.staffNationality = staffNationality;
	}

	public String getStaffMaritalStatus() {
		return staffMaritalStatus;
	}

	public void setStaffMaritalStatus(String staffMaritalStatus) {
		this.staffMaritalStatus = staffMaritalStatus;
	}

	public String getStaffPoliticalStatus() {
		return staffPoliticalStatus;
	}

	public void setStaffPoliticalStatus(String staffPoliticalStatus) {
		this.staffPoliticalStatus = staffPoliticalStatus;
	}

	public Date getJoinPartyTime() {
		return joinPartyTime;
	}

	public void setJoinPartyTime(Date joinPartyTime) {
		this.joinPartyTime = joinPartyTime;
	}

	public String getStaffPhone() {
		return staffPhone;
	}

	public void setStaffPhone(String staffPhone) {
		this.staffPhone = staffPhone;
	}

	public String getStaffMobile() {
		return staffMobile;
	}

	public void setStaffMobile(String staffMobile) {
		this.staffMobile = staffMobile;
	}

	public String getStaffLittleSmart() {
		return staffLittleSmart;
	}

	public void setStaffLittleSmart(String staffLittleSmart) {
		this.staffLittleSmart = staffLittleSmart;
	}

	public String getStaffEmail() {
		return staffEmail;
	}

	public void setStaffEmail(String staffEmail) {
		this.staffEmail = staffEmail;
	}

	public String getStaffMsn() {
		return staffMsn;
	}

	public void setStaffMsn(String staffMsn) {
		this.staffMsn = staffMsn;
	}

	public String getStaffQq() {
		return staffQq;
	}

	public void setStaffQq(String staffQq) {
		this.staffQq = staffQq;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	public Date getJobBeginning() {
		return jobBeginning;
	}

	public void setJobBeginning(Date jobBeginning) {
		this.jobBeginning = jobBeginning;
	}

	public String getWorkAge() {
		return workAge;
	}

	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}

	public String getStaffHealth() {
		return staffHealth;
	}

	public void setStaffHealth(String staffHealth) {
		this.staffHealth = staffHealth;
	}

	public String getStaffHighestSchool() {
		return staffHighestSchool;
	}

	public void setStaffHighestSchool(String staffHighestSchool) {
		this.staffHighestSchool = staffHighestSchool;
	}

	public String getStaffHighestDegree() {
		return staffHighestDegree;
	}

	public void setStaffHighestDegree(String staffHighestDegree) {
		this.staffHighestDegree = staffHighestDegree;
	}

	public Date getGraduationDate() {
		return graduationDate;
	}

	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}

	public String getGraduationSchool() {
		return graduationSchool;
	}

	public void setGraduationSchool(String graduationSchool) {
		this.graduationSchool = graduationSchool;
	}

	public String getStaffMajor() {
		return staffMajor;
	}

	public void setStaffMajor(String staffMajor) {
		this.staffMajor = staffMajor;
	}

	public String getComputerLevel() {
		return computerLevel;
	}

	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}

	public String getForeignLanguage1() {
		return foreignLanguage1;
	}

	public void setForeignLanguage1(String foreignLanguage1) {
		this.foreignLanguage1 = foreignLanguage1;
	}

	public String getForeignLevel1() {
		return foreignLevel1;
	}

	public void setForeignLevel1(String foreignLevel1) {
		this.foreignLevel1 = foreignLevel1;
	}

	public String getForeignLanguage2() {
		return foreignLanguage2;
	}

	public void setForeignLanguage2(String foreignLanguage2) {
		this.foreignLanguage2 = foreignLanguage2;
	}

	public String getForeignLevel2() {
		return foreignLevel2;
	}

	public void setForeignLevel2(String foreignLevel2) {
		this.foreignLevel2 = foreignLevel2;
	}

	public String getForeignLanguage3() {
		return foreignLanguage3;
	}

	public void setForeignLanguage3(String foreignLanguage3) {
		this.foreignLanguage3 = foreignLanguage3;
	}

	public String getForeignLevel3() {
		return foreignLevel3;
	}

	public void setForeignLevel3(String foreignLevel3) {
		this.foreignLevel3 = foreignLevel3;
	}

	public String getStaffSkills() {
		return staffSkills;
	}

	public void setStaffSkills(String staffSkills) {
		this.staffSkills = staffSkills;
	}

	public String getStaffOccupation() {
		return staffOccupation;
	}

	public void setStaffOccupation(String staffOccupation) {
		this.staffOccupation = staffOccupation;
	}

	public String getAdministrationLevel() {
		return administrationLevel;
	}

	public void setAdministrationLevel(String administrationLevel) {
		this.administrationLevel = administrationLevel;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getPresentPosition() {
		return presentPosition;
	}

	public void setPresentPosition(String presentPosition) {
		this.presentPosition = presentPosition;
	}

	public Date getDatesEmployed() {
		return datesEmployed;
	}

	public void setDatesEmployed(Date datesEmployed) {
		this.datesEmployed = datesEmployed;
	}

	public String getJobAge() {
		return jobAge;
	}

	public void setJobAge(String jobAge) {
		this.jobAge = jobAge;
	}

	public Date getBeginSalsryTime() {
		return beginSalsryTime;
	}

	public void setBeginSalsryTime(Date beginSalsryTime) {
		this.beginSalsryTime = beginSalsryTime;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public Date getStaffCs() {
		return staffCs;
	}

	public void setStaffCs(Date staffCs) {
		this.staffCs = staffCs;
	}

	public Date getStaffCtr() {
		return staffCtr;
	}

	public void setStaffCtr(Date staffCtr) {
		this.staffCtr = staffCtr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStaffCompany() {
		return staffCompany;
	}

	public void setStaffCompany(String staffCompany) {
		this.staffCompany = staffCompany;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
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

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(int leaveType) {
		this.leaveType = leaveType;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public String getYesOrNot() {
		return yesOrNot;
	}

	public void setYesOrNot(String yesOrNot) {
		this.yesOrNot = yesOrNot;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getSurety() {
		return surety;
	}

	public void setSurety(String surety) {
		this.surety = surety;
	}

	public String getBodyExamim() {
		return bodyExamim;
	}

	public void setBodyExamim(String bodyExamim) {
		this.bodyExamim = bodyExamim;
	}

	public String getInsure() {
		return insure;
	}

	public void setInsure(String insure) {
		this.insure = insure;
	}

	public String getUserdef1() {
		return userdef1;
	}

	public void setUserdef1(String userdef1) {
		this.userdef1 = userdef1;
	}

	public String getUserdef2() {
		return userdef2;
	}

	public void setUserdef2(String userdef2) {
		this.userdef2 = userdef2;
	}

	public String getUserdef3() {
		return userdef3;
	}

	public void setUserdef3(String userdef3) {
		this.userdef3 = userdef3;
	}

	public String getUserdef4() {
		return userdef4;
	}

	public void setUserdef4(String userdef4) {
		this.userdef4 = userdef4;
	}

	public String getUserdef5() {
		return userdef5;
	}

	public void setUserdef5(String userdef5) {
		this.userdef5 = userdef5;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

}
