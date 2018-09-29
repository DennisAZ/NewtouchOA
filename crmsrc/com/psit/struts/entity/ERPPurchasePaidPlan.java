package com.psit.struts.entity;

public class ERPPurchasePaidPlan implements java.io.Serializable {
	private String id;
	private String code;
	private String title;
	private String status;
	private String purchaser;
private Double total;
private Double alreadyTotal;
private ERPPurchase erpPurchase;
	private String remark;




	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPurchaser() {
		return purchaser;
	}
	public void setPurchaser(String purchaser) {
		this.purchaser = purchaser;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
//	public ERPPurchase getErpPurchase() {
//		return erpPurchase;
//	}
//	public void setErpPurchase(ERPPurchase erpPurchase) {
//		this.erpPurchase = erpPurchase;
//	}
	public ERPPurchase getErpPurchase() {
		return erpPurchase;
	}
	public void setErpPurchase(ERPPurchase erpPurchase) {
		this.erpPurchase = erpPurchase;
	}




}
