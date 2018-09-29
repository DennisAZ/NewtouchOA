package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

public class ERPPurchase implements java.io.Serializable {
	private String id;
	private String code;
	private String title;
	private String status;
	private String purchaser;
	private String signDate;
	private String remark;
	private YHPerson yhperson;
	private ERPPurchasePaidPlan erpPurchasePaidPlan;
	private ERPPurchaseSupplier erpPurchaseSupplier;
	private Set paidPlan = new HashSet(0);//
	private Set supplier = new HashSet(0);//
	public ERPPurchase(){
		super();
		
	}
	public ERPPurchase(String id){
		this.id=id;
		
	}
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
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public YHPerson getYhperson() {
		return yhperson;
	}
	public void setYhperson(YHPerson yhperson) {
		this.yhperson = yhperson;
	}
	public Set getPaidPlan() {
		return paidPlan;
	}
	public void setPaidPlan(Set paidPlan) {
		this.paidPlan = paidPlan;
	}
	public ERPPurchasePaidPlan getErpPurchasePaidPlan() {
		return erpPurchasePaidPlan;
	}
	public void setErpPurchasePaidPlan(ERPPurchasePaidPlan erpPurchasePaidPlan) {
		this.erpPurchasePaidPlan = erpPurchasePaidPlan;
	}
	public ERPPurchaseSupplier getErpPurchaseSupplier() {
		return erpPurchaseSupplier;
	}
	public void setErpPurchaseSupplier(ERPPurchaseSupplier erpPurchaseSupplier) {
		this.erpPurchaseSupplier = erpPurchaseSupplier;
	}
	public Set getSupplier() {
		return supplier;
	}
	public void setSupplier(Set supplier) {
		this.supplier = supplier;
	}

}
