package yh.subsys.jtgwjh.certificate.data;

import java.util.Date;



public class YHJhCertificate{
	private int seqId;
	private String orgGuid;
	private String orgName;
	private String cert;
	private Date buildTime;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public String getOrgGuid() {
		return orgGuid;
	}
	public void setOrgGuid(String orgGuid) {
		this.orgGuid = orgGuid;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public Date getBuildTime() {
		return buildTime;
	}
	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}
	public String getStopUse() {
		return stopUse;
	}
	public void setStopUse(String stopUse) {
		this.stopUse = stopUse;
	}
	private String stopUse;
}