package com.psit.struts.entity;

public class ERPOrdProductOut implements java.io.Serializable {

private String id;
private String poCode;
private String poTitle;
private String poStatus;
private String remark;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getPoCode() {
	return poCode;
}
public void setPoCode(String poCode) {
	this.poCode = poCode;
}
public String getPoTitle() {
	return poTitle;
}
public void setPoTitle(String poTitle) {
	this.poTitle = poTitle;
}
public String getPoStatus() {
	return poStatus;
}
public void setPoStatus(String poStatus) {
	this.poStatus = poStatus;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}


}
