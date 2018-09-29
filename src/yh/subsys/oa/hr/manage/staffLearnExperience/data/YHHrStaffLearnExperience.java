package yh.subsys.oa.hr.manage.staffLearnExperience.data;

import java.util.Date;

public class YHHrStaffLearnExperience {
	private int seqId;//流水号
	private String createUserId; 	//系统登录人seqId
	private int createDeptId;	//系统登录人部门Id
	private String staffName;	//单位员工
	private Date startDate;	//开始日期
	private Date endDate;	//结束日期
	private String school;	//所在院校
	private String schoolAddress;	//院校所在地
	private String major;	//所学专业
	private String academyDegree;	//所获学历
	private String expirationPeriod;	
	private String  degree; //所获学位
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	private String position;	//曾任班干
	private String awarding;	//获奖情况
	private String certificates;	//所获证书
	private String witness;	//证明人
	private String attachmentId;	//附件ID
	private String attachmentName;	//附件名称
	private String remark;	//备注
	private String addTime;//系统当前时间
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
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
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getSchoolAddress() {
		return schoolAddress;
	}
	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getAcademyDegree() {
		return academyDegree;
	}
	public void setAcademyDegree(String academyDegree) {
		this.academyDegree = academyDegree;
	}
	public String getExpirationPeriod() {
		return expirationPeriod;
	}
	public void setExpirationPeriod(String expirationPeriod) {
		this.expirationPeriod = expirationPeriod;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getAwarding() {
		return awarding;
	}
	public void setAwarding(String awarding) {
		this.awarding = awarding;
	}
	public String getCertificates() {
		return certificates;
	}
	public void setCertificates(String certificates) {
		this.certificates = certificates;
	}
	public String getWitness() {
		return witness;
	}
	public void setWitness(String witness) {
		this.witness = witness;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
