package yh.subsys.oa.hr.manage.staffLaborSkills.data;

import java.util.Date;

public class YHHrStaffLaborSkills {
	private int seqId;	// 流水号
	private String createUserId; 	//系统登录人seqId
	private int createDeptId;	//系统登录人部门Id
	private String staffName;		//单位员工
	private String abilityName;		//技能名称
	private String specialWork;	//特种作业
	private String skillsLevel;		//级别
	private String skillsCertificate;		//技能证1：是 0：否
	private Date issueDate;	//发证日期
	private Date expireDate;	//到期日期
	private String expires;	//有效期
	private String issuingAuthority;		//发证机关/单位
	private String attachmentId;		//附件ID
	private String attachmentName; 		//附件名称
	private String remark;	//备注
	private String addTime;	//系统当前时间
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
	public String getAbilityName() {
		return abilityName;
	}
	public void setAbilityName(String abilityName) {
		this.abilityName = abilityName;
	}
	public String getSpecialWork() {
		return specialWork;
	}
	public void setSpecialWork(String specialWork) {
		this.specialWork = specialWork;
	}
	public String getSkillsLevel() {
		return skillsLevel;
	}
	public void setSkillsLevel(String skillsLevel) {
		this.skillsLevel = skillsLevel;
	}
	public String getSkillsCertificate() {
		return skillsCertificate;
	}
	public void setSkillsCertificate(String skillsCertificate) {
		this.skillsCertificate = skillsCertificate;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getIssuingAuthority() {
		return issuingAuthority;
	}
	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
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
	

}
