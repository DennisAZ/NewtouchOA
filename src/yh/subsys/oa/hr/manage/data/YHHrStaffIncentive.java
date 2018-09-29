package yh.subsys.oa.hr.manage.data;

import java.util.Date;

public class YHHrStaffIncentive {
	private int seqId;
	private String createUserId;
	private int createDeptId;
	private Date incentiveTime;
	private String salaryMonth;
	private double incentiveAmount;
	private String attachmentId;
	private String attachmentName;
	private String remark;
	private String incentiveDescription;
	private Date addTime;
	private double addScore;
	private double reduceScore; 
	private double yearScore;
	private String incentiveItem;
	private String incentiveType;
	private String staffName;
	

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

	public Date getIncentiveTime() {
		return incentiveTime;
	}

	public void setIncentiveTime(Date incentiveTime) {
		this.incentiveTime = incentiveTime;
	}

	public String getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public double getIncentiveAmount() {
		return incentiveAmount;
	}

	public void setIncentiveAmount(double incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
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

	public String getIncentiveDescription() {
		return incentiveDescription;
	}

	public void setIncentiveDescription(String incentiveDescription) {
		this.incentiveDescription = incentiveDescription;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public double getAddScore() {
		return addScore;
	}

	public void setAddScore(double addScore) {
		this.addScore = addScore;
	}

	public double getReduceScore() {
		return reduceScore;
	}

	public void setReduceScore(double reduceScore) {
		this.reduceScore = reduceScore;
	}

	public double getYearScore() {
		return yearScore;
	}

	public void setYearScore(double yearScore) {
		this.yearScore = yearScore;
	}

	public String getIncentiveItem() {
		return incentiveItem;
	}

	public void setIncentiveItem(String incentiveItem) {
		this.incentiveItem = incentiveItem;
	}

	public String getIncentiveType() {
		return incentiveType;
	}

	public void setIncentiveType(String incentiveType) {
		this.incentiveType = incentiveType;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

}
