package com.psit.struts.entity;

public class ERPOrdProductOutDetail implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String podCode;
	private String podStatus;
	private String podDate;
	private String podSender;
	private String podSendWay;
	private Double total;
	private String remark;
	private String person;
	private String address;
	private String phone;
	private String zipCode;
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	private ERPOrdProductOut erpOrdProductOut;
	private ERPSalOrder erpSalOrder;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPodCode() {
		return podCode;
	}
	public void setPodCode(String podCode) {
		this.podCode = podCode;
	}
	public String getPodStatus() {
		return podStatus;
	}
	public void setPodStatus(String podStatus) {
		this.podStatus = podStatus;
	}
	public String getPodDate() {
		return podDate;
	}
	public void setPodDate(String podDate) {
		this.podDate = podDate;
	}
	public String getPodSender() {
		return podSender;
	}
	public void setPodSender(String podSender) {
		this.podSender = podSender;
	}
	public String getPodSendWay() {
		return podSendWay;
	}
	public void setPodSendWay(String podSendWay) {
		this.podSendWay = podSendWay;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public ERPOrdProductOut getErpOrdProductOut() {
		return erpOrdProductOut;
	}
	public void setErpOrdProductOut(ERPOrdProductOut erpOrdProductOut) {
		this.erpOrdProductOut = erpOrdProductOut;
	}
	public ERPSalOrder getErpSalOrder() {
		return erpSalOrder;
	}
	public void setErpSalOrder(ERPSalOrder erpSalOrder) {
		this.erpSalOrder = erpSalOrder;
	}

   

}
