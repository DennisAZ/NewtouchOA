package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

public class ERPPurchaseSupplier implements java.io.Serializable {
	private String id;
	private String supName;
	private String supCode;
	private String contactName;
	private String phone;
//	private ERPPurchase erpPurchase;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
	}
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
//	public ERPPurchase getErpPurchase() {
//		return erpPurchase;
//	}
//	public void setErpPurchase(ERPPurchase erpPurchase) {
//		this.erpPurchase = erpPurchase;
//	}
	

}
