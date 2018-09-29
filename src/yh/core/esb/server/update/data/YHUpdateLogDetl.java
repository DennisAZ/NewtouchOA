package yh.core.esb.server.update.data;

import java.util.Date;

public class YHUpdateLogDetl{
	private int seqId;
	private int  logSeqId;
	private String clientGuid;
	private String clientName;
	private String updateStatus;
	private Date doneTime;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getLogSeqId() {
		return logSeqId;
	}
	public void setLogSeqId(int logSeqId) {
		this.logSeqId = logSeqId;
	}
	public String getClientGuid() {
		return clientGuid;
	}
	public void setClientGuid(String clientGuid) {
		this.clientGuid = clientGuid;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	public Date getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}
}