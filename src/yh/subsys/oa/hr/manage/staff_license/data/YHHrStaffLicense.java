package yh.subsys.oa.hr.manage.staff_license.data;

import java.util.Date;

public class YHHrStaffLicense{
	private int seqId;	//流水号
	private String createUserId; 	//系统登录人seqId
	private int createDeptId;	//系统登录人部门Id
	private String staffName;	//单位员工
	private String licenseType;	//证照类型
	private String licenseNo;	//证照编号
	private String licenseName;	//证照名称
	private String notifiedBody;	//工资月份
	private Date	getLicenseDate;	//取证日期
	private Date effectiveDate;	//生效日期
	private String expirationPeriod;	//期限限制  1:限制  0：不限制
	private  Date expireDate;	//到期日期
	private String status;	//状态
	private String attachmentId;	//附件ID
	private String attachmentName;//附件名称
	private String remark;	//备注
	private String addTime;	//系统当前时间

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


public String getLicenseType() {
	return licenseType;
}


public void setLicenseType(String licenseType) {
	this.licenseType = licenseType;
}


public String getLicenseNo() {
	return licenseNo;
}


public void setLicenseNo(String licenseNo) {
	this.licenseNo = licenseNo;
}


public String getLicenseName() {
	return licenseName;
}


public void setLicenseName(String licenseName) {
	this.licenseName = licenseName;
}


public String getNotifiedBody() {
	return notifiedBody;
}


public void setNotifiedBody(String notifiedBody) {
	this.notifiedBody = notifiedBody;
}


public Date getGetLicenseDate() {
	return getLicenseDate;
}


public void setGetLicenseDate(Date getLicenseDate) {
	this.getLicenseDate = getLicenseDate;
}


public Date getEffectiveDate() {
	return effectiveDate;
}


public void setEffectiveDate(Date effectiveDate) {
	this.effectiveDate = effectiveDate;
}


public String getExpirationPeriod() {
	return expirationPeriod;
}


public void setExpirationPeriod(String expirationPeriod) {
	this.expirationPeriod = expirationPeriod;
}


public Date getExpireDate() {
	return expireDate;
}


public void setExpireDate(Date expireDate) {
	this.expireDate = expireDate;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
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


public String getAddTime() {
	return addTime;
}


public void setAddTime(String addTime) {
	this.addTime = addTime;
}



	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
