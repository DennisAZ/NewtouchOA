package yh.core.esb.server.update.data;

import java.util.Date;


public class YHUpdateServerLog{
	private int seqId;
	private String updateDesc;
	private int toVersion;
	private  String deployUser;
	private Date deployTime;
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
	public String getDeployUser() {
		return deployUser;
	}
	public void setDeployUser(String deployUser) {
		this.deployUser = deployUser;
	}
	public Date getDeployTime() {
		return deployTime;
	}
	public void setDeployTime(Date deployTime) {
		this.deployTime = deployTime;
	}
}