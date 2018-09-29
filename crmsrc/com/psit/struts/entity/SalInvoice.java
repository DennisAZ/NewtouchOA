package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 票据记录实体 <br>
 */

public class SalInvoice implements java.io.Serializable {

	// Fields
	private Long sinId;
	private SalOrdCon salOrdCon;
	private TypeList typeList;
	private CusCorCus cusCorCus;
	private SalEmp salEmp;
	private String sinCon;
	private Double sinMon;
	private String sinCode;
	private Date sinDate;
	private String sinRemark;
	private String sinIsPaid;
	private String sinUserCode;
	private Date sinCreDate;
	private Date sinAltDate;
	private String sinAltUser;
	private String sinIsrecieve;
	private ERPSalOrder erpSalOrder;
	private YHPerson person;
	public YHPerson getPerson() {
		return person;
	}
	public void setPerson(YHPerson person) {
		this.person = person;
	}
	public ERPSalOrder getErpSalOrder() {
		return erpSalOrder;
	}
	public void setErpSalOrder(ERPSalOrder erpSalOrder) {
		this.erpSalOrder = erpSalOrder;
	}
	//构造器
	public SalInvoice(){
		
	}
	public SalInvoice(Long sinId, SalOrdCon salOrdCon, TypeList typeList,
			CusCorCus cusCorCus, SalEmp salEmp, String sinCon, Double sinMon,
			String sinCode, Date sinDate, String sinRemark, String sinIsPaid,
			String sinUserCode, Date sinCreDate, Date sinAltDate,
			String sinAltUser, String sinIsrecieve) {
		super();
		this.sinId = sinId;
		this.salOrdCon = salOrdCon;
		this.typeList = typeList;
		this.cusCorCus = cusCorCus;
		this.salEmp = salEmp;
		this.sinCon = sinCon;
		this.sinMon = sinMon;
		this.sinCode = sinCode;
		this.sinDate = sinDate;
		this.sinRemark = sinRemark;
		this.sinIsPaid = sinIsPaid;
		this.sinUserCode = sinUserCode;
		this.sinCreDate = sinCreDate;
		this.sinAltDate = sinAltDate;
		this.sinAltUser = sinAltUser;
		this.sinIsrecieve = sinIsrecieve;
	}
	public Long getSinId() {
		return sinId;
	}
	public void setSinId(Long sinId) {
		this.sinId = sinId;
	}
	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}
	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}
	public TypeList getTypeList() {
		return typeList;
	}
	public void setTypeList(TypeList typeList) {
		this.typeList = typeList;
	}
	public CusCorCus getCusCorCus() {
		return cusCorCus;
	}
	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}
	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public String getSinCon() {
		return sinCon;
	}
	public void setSinCon(String sinCon) {
		this.sinCon = sinCon;
	}
	public Double getSinMon() {
		return sinMon;
	}
	public void setSinMon(Double sinMon) {
		this.sinMon = sinMon;
	}
	public String getSinCode() {
		return sinCode;
	}
	public void setSinCode(String sinCode) {
		this.sinCode = sinCode;
	}
	public Date getSinDate() {
		return sinDate;
	}
	public void setSinDate(Date sinDate) {
		this.sinDate = sinDate;
	}
	public String getSinRemark() {
		return sinRemark;
	}
	public void setSinRemark(String sinRemark) {
		this.sinRemark = sinRemark;
	}
	public String getSinIsPaid() {
		return sinIsPaid;
	}
	public void setSinIsPaid(String sinIsPaid) {
		this.sinIsPaid = sinIsPaid;
	}
	public String getSinUserCode() {
		return sinUserCode;
	}
	public void setSinUserCode(String sinUserCode) {
		this.sinUserCode = sinUserCode;
	}
	public Date getSinCreDate() {
		return sinCreDate;
	}
	public void setSinCreDate(Date sinCreDate) {
		this.sinCreDate = sinCreDate;
	}
	public Date getSinAltDate() {
		return sinAltDate;
	}
	public void setSinAltDate(Date sinAltDate) {
		this.sinAltDate = sinAltDate;
	}
	public String getSinAltUser() {
		return sinAltUser;
	}
	public void setSinAltUser(String sinAltUser) {
		this.sinAltUser = sinAltUser;
	}
	public String getSinIsrecieve() {
		return sinIsrecieve;
	}
	public void setSinIsrecieve(String sinIsrecieve) {
		this.sinIsrecieve = sinIsrecieve;
	}



}