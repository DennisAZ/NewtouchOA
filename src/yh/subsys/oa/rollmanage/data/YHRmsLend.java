package yh.subsys.oa.rollmanage.data;

import java.util.Date;

public class YHRmsLend {

	private int seqId;
	private int fileId;					//档案文件ID
	private String userId;			//借阅人
	private Date addTime;				//借阅时间
	private Date returnTime;		//归还时间
	private Date allowTime;			//审批时间
	private String approve;			//审批人
	private String allow;				//借阅审核/归还标记 1－批准,	2－不批准
	
	public int getSeqId() {
		return seqId;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public Date getAllowTime() {
		return allowTime;
	}
	public void setAllowTime(Date allowTime) {
		this.allowTime = allowTime;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	
	
}
