package com.psit.struts.entity;

public class ERPPaidPlan implements java.io.Serializable {
	private String paidId;
	private String paidTitle;
	private String paidCode;
	private Double total;
	private Double alreadyTotal;
	private Double salePaid;
	private ERPSalOrder erpSalOrder;
	public String getPaidId() {
		return paidId;
	}
	public void setPaidId(String paidId) {
		this.paidId = paidId;
	}
	public String getPaidTitle() {
		return paidTitle;
	}
	public void setPaidTitle(String paidTitle) {
		this.paidTitle = paidTitle;
	}
	public String getPaidCode() {
		return paidCode;
	}
	public void setPaidCode(String paidCode) {
		this.paidCode = paidCode;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getAlreadyTotal() {
		return alreadyTotal;
	}
	public void setAlreadyTotal(Double alreadyTotal) {
		this.alreadyTotal = alreadyTotal;
	}
	public Double getSalePaid() {
		return salePaid;
	}
	public void setSalePaid(Double salePaid) {
		this.salePaid = salePaid;
	}
	public ERPSalOrder getErpSalOrder() {
		return erpSalOrder;
	}
	public void setErpSalOrder(ERPSalOrder erpSalOrder) {
		this.erpSalOrder = erpSalOrder;
	}

	


}
