package yh.subsys.oa.hr.manage.staff_contract.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YHHrStaffContract {
	private int seqId;    //流水号
	private String createUserId;  //系统登录人seqId
	private int createDeptId;  //系统登录人部门Id
	private String staffName;   //单位员工（用户的seqId）
	private String staffContractNo; //合同编号
	private String contractType; //合同类型
	private String contractSpecialization; //合同属性 1：有固定期限 2：无固定期限
	private Date makeContract; //合同签订日期
	private Date trailEffectiveTime; //试用生效日期
	private String probationaryPeriod; //试用天数
	private Date trailOverTime; //试用到期日期
	private String passOrNot; //合同是否转正1:是 0：否
	private Date probationEndDate; //合同转正日期
	private String remark; //备注
	private String removeOrNot;  //合同是否解除1:是  0：否
	private Date contractRemoveTime; //合同解除日期

	private String status;  //合同状态
	private String signTimes;  //签约次数
	private String attachmentId;  //附件ID
	private String attachmentName;  //附件名称
	private String addTime;  //系统当前时间
	private Date probationEffectiveDate; //合同生效日期
	private Date contractEndTime;//合同到期日期
	
	public Date getContractEndTime() {
		return contractEndTime;
	}
	public void setContractEndTime(Date contractEndTime) {
		this.contractEndTime = contractEndTime;
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

	public String getStaffContractNo() {
		return staffContractNo;
	}
	public void setStaffContractNo(String staffContractNo) {
		this.staffContractNo = staffContractNo;
	}

	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractSpecialization() {
		return contractSpecialization;
	}
	public void setContractSpecialization(String contractSpecialization) {
		this.contractSpecialization = contractSpecialization;
	}

	public Date getMakeContract() {
		//SimpleDateFormat	df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//return df.parse(makeContract);
		return makeContract;
	}
	public void setMakeContract(Date makeContract) {
		this.makeContract = makeContract;
	}
	
	public Date getTrailEffectiveTime() {
		return trailEffectiveTime;
	}
	public void setTrailEffectiveTime(Date trailEffectiveTime) {
		this.trailEffectiveTime = trailEffectiveTime;
	}

	public String getProbationaryPeriod() {
		return probationaryPeriod;
	}
	public void setProbationaryPeriod(String probationaryPeriod) {
		this.probationaryPeriod = probationaryPeriod;
	}

	public Date getTrailOverTime() {
		return trailOverTime;
	}
	public void setTrailOverTime(Date trailOverTime) {
		this.trailOverTime = trailOverTime;
	}

	public String getPassOrNot() {
		return passOrNot;
	}
	public void setPassOrNot(String passOrNot) {
		this.passOrNot = passOrNot;
	}

	public Date getProbationEndDate() {
		return probationEndDate;
	}
	public void setProbationEndDate(Date probationEndDate) {
		this.probationEndDate = probationEndDate;
	}

	public Date getProbationEffectiveDate() {
		return probationEffectiveDate;
	}
	public void setProbationEffectiveDate(Date probationEffectiveDate) {
		this.probationEffectiveDate = probationEffectiveDate;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemoveOrNot() {
		return removeOrNot;
	}

	public void setRemoveOrNot(String removeOrNot) {
		this.removeOrNot = removeOrNot;
	}

	public Date getContractRemoveTime() {
		return contractRemoveTime;
	}
	public void setContractRemoveTime(Date contractRemoveTime) {
		this.contractRemoveTime = contractRemoveTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSignTimes() {
		return signTimes;
	}
	public void setSignTimes(String signTimes) {
		this.signTimes = signTimes;
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
