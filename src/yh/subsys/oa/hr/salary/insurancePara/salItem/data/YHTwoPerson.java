package yh.subsys.oa.hr.salary.insurancePara.salItem.data;

import java.util.List;

public class YHTwoPerson {
	private int seqId;
	private String userId;
    private String userName;
    private List<YHHrSalData> salData;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<YHHrSalData> getSalData() {
		return salData;
	}
	public void setSalData(List<YHHrSalData> salData) {
		this.salData = salData;
	}
}
