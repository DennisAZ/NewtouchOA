package yh.subsys.oa.hr.manage.staffWorkExperience.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YHHrStaffWorkExperience {
	private int seqId;	//流水号
	private String createUserId;	//系统登录人seqId
	private int createDeptId;	//系统登录人部门Id
	private String staffName;	//单位员工
	private Date startDate;		//开始日期
	private Date endDate;	//结束日期
	private String workUnit;	//工作单位
	private String mobile;	//行业类别
	private String workBranch;		//所在部门
	private String postOfJob;		//担任职务
	private String workContent;	//工作内容
	private String keyPerformance;		//主要业绩
	private String reasonForLeaving;//离职原因
	private String witness;		//证明人
	private String attachmentId;		//附件ID
	private String attachmentName;		//附件名称
	private String remark; 		//备注
	private String addTime;		//系统当前时间
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
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getWorkBranch() {
		return workBranch;
	}
	public void setWorkBranch(String workBranch) {
		this.workBranch = workBranch;
	}
	public String getPostOfJob() {
		return postOfJob;
	}
	public void setPostOfJob(String postOfJob) {
		this.postOfJob = postOfJob;
	}
	public String getWorkContent() {
		return workContent;
	}
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}
	public String getKeyPerformance() {
		return keyPerformance;
	}
	public void setKeyPerformance(String keyPerformance) {
		this.keyPerformance = keyPerformance;
	}
	public String getReasonForLeaving() {
		return reasonForLeaving;
	}
	public void setReasonForLeaving(String reasonForLeaving) {
		this.reasonForLeaving = reasonForLeaving;
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
		Date currentTime = new Date();    
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
	    String dateString = formatter.format(currentTime); 
	    System.out.println(dateString);
	    
	    String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	    System.out.println("date::"+date);
	}

}
