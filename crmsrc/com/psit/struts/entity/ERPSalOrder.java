package com.psit.struts.entity;

public class ERPSalOrder implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String orderTitle;
	private String 	orderCode;
	private String signDate;
	private String salesPerson;
	private String orderStatus;
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public ERPSalOrder(){
		super();
	}
	public String getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	ERPOrderCus erpOrderCus;
	private long personId;
	public long getPersonId() {
		return personId;
	}
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public ERPOrderCus getErpOrderCus() {
		return erpOrderCus;
	}
	public void setErpOrderCus(ERPOrderCus erpOrderCus) {
		this.erpOrderCus = erpOrderCus;
	}

}
