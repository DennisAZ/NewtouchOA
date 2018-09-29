package yh.core.esb.client.update.data;

import java.util.Date;


public class YHUpdateClientLog{
	private int seqId;
	private String updateDesc;
	private int toVersion;
	private Date reseiveTime;
	private String updateStatus;
	private String updateUser;
	private Date doneTime;
	private int logSeqId;
	private String guid;
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setLogSeqId(int logSeqId) {
		this.logSeqId = logSeqId;
	}
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public String getUpdateDesc() {
		return updateDesc;
	}
	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}
	public int getToVersion() {
		return toVersion;
	}
	public void setToVersion(int toVersion) {
		this.toVersion = toVersion;
	}
	public Date getReseiveTime() {
		return reseiveTime;
	}
	public void setReseiveTime(Date reseiveTime) {
		this.reseiveTime = reseiveTime;
	}
	public String getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}
	public int getLogSeqId() {
		return logSeqId;
	}
}