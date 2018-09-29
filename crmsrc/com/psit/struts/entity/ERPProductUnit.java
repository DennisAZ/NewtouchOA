package com.psit.struts.entity;

public class ERPProductUnit implements java.io.Serializable {
private String unitId;
private String unitName;
private String isDel;
public String getIsDel() {
	return isDel;
}
public void setIsDel(String isDel) {
	this.isDel = isDel;
}
public String getUnitId() {
	return unitId;
}
public void setUnitId(String unitId) {
	this.unitId = unitId;
}
public String getUnitName() {
	return unitName;
}
public void setUnitName(String unitName) {
	this.unitName = unitName;
}


}
