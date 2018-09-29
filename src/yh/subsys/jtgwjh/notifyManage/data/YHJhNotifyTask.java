 package yh.subsys.jtgwjh.notifyManage.data;

import yh.core.util.YHUtility;
 
 public class YHJhNotifyTask {
	 private int seqId;
	 private int  jhNotifyId;
	 private String reciveDept;
	 private String reciveDeptName;
	 private String guid;
	 private String status;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getJhNotifyId() {
		return jhNotifyId;
	}
	public void setJhNotifyId(int jhNotifyId) {
		this.jhNotifyId = jhNotifyId;
	}
	public String getReciveDept() {
		return reciveDept;
	}
	public void setReciveDept(String reciveDept) {
		this.reciveDept = reciveDept;
	}
	public String getReciveDeptName() {
		return reciveDeptName;
	}
	public void setReciveDeptName(String reciveDeptName) {
		this.reciveDeptName = reciveDeptName;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String toXML() {
		 String str = "<seqId>"+seqId+"</seqId>"
	               + "<jhNotifyId>"+jhNotifyId+"</jhNotifyId>"
	               + "<reciveDept>"+reciveDept+"</reciveDept>"
	               + "<reciveDeptName>"+reciveDeptName+"</reciveDeptName>"
	               + "<status>"+status+"</status>"
	               + "<guid>"+YHUtility.null2Empty(guid)+"</guid>";
		return str;
	}
 }