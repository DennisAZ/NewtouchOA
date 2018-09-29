package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 回款记录实体 <br>
 */

public class SalPaidPast implements java.io.Serializable {

	// Fields

	private Long spsId;
	private SalOrdCon salOrdCon;
	private TypeList salPaidType;
	private CusCorCus cusCorCus;
	private String spsContent;
	private Date spsFctDate;
	private Integer spsCount;
	private String spsPayType;
	private Double spsPayMon;
	private SalEmp salEmp;
	private String spsIsinv;
	private String spsRemark;
	private String spsUserCode;
	private String spsAltUser;
	private Date spsCreDate;
	private Date spsAltDate;
	private String spsIsdel;
	private String spsCode;
	private String spsOutName;
	private TypeList typeList;
	private Date spsUndoDate;
	private String spsUndoUser;

	// Constructors
	/** default constructor */
	public SalPaidPast() {
	}
	
	/** mini constructor */
	public SalPaidPast(Long spsId) {
		this.spsId=spsId;
	}

	/** full constructor */
	public SalPaidPast(SalOrdCon salOrdCon,
			TypeList salPaidType,
			Date spsFctDate, Integer spsCount, String spsPayType,
			Double spsPayMon, SalEmp salEmp,
			String spsIsinv, String spsRemark,String spsUserCode,String spsAltUser,
			Date spsCreDate,Date spsAltDate, String spsIsdel, String spsCode, 
			String spsOutName, String spsContent, TypeList typeList,
			Date spsUndoDate, String spsUndoUser,CusCorCus cusCorCus) {
		this.salOrdCon = salOrdCon;
		this.salPaidType = salPaidType;
		this.spsFctDate = spsFctDate;
		this.spsCount = spsCount;
		this.spsPayType = spsPayType;
		this.spsPayMon = spsPayMon;
		this.salEmp = salEmp;
		this.spsIsinv = spsIsinv;
		this.spsRemark = spsRemark;
		this.spsUserCode = spsUserCode;
		this.spsAltUser = spsAltUser;
		this.spsCreDate = spsCreDate;
		this.spsAltDate = spsAltDate;
		this.spsIsdel = spsIsdel;
		this.spsCode = spsCode;
		this.spsOutName = spsOutName;
		this.spsContent = spsContent;
		this.typeList = typeList;
		this.spsUndoDate = spsUndoDate;
		this.spsUndoUser = spsUndoUser;
		this.cusCorCus = cusCorCus;
	}

	// Property accessors

	public Long getSpsId() {
		return this.spsId;
	}

	public void setSpsId(Long spsId) {
		this.spsId = spsId;
	}

	public TypeList getSalPaidType() {
		return this.salPaidType;
	}

	public void setSalPaidType(TypeList salPaidType) {
		this.salPaidType = salPaidType;
	}

	public Date getSpsFctDate() {
		return this.spsFctDate;
	}

	public void setSpsFctDate(Date spsFctDate) {
		this.spsFctDate = spsFctDate;
	}

	public Integer getSpsCount() {
		return this.spsCount;
	}

	public void setSpsCount(Integer spsCount) {
		this.spsCount = spsCount;
	}

	public String getSpsPayType() {
		return this.spsPayType;
	}

	public void setSpsPayType(String spsPayType) {
		this.spsPayType = spsPayType;
	}

	public Double getSpsPayMon() {
		return this.spsPayMon;
	}

	public void setSpsPayMon(Double spsPayMon) {
		this.spsPayMon = spsPayMon;
	}

	public String getSpsIsinv() {
		return this.spsIsinv;
	}

	public void setSpsIsinv(String spsIsinv) {
		this.spsIsinv = spsIsinv;
	}

	public String getSpsRemark() {
		return StringFormat.toBlank(this.spsRemark);
	}

	public void setSpsRemark(String spsRemark) {
		this.spsRemark = spsRemark;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}

	public String getSpsUserCode() {
		return spsUserCode;
	}

	public void setSpsUserCode(String spsUserCode) {
		this.spsUserCode = spsUserCode;
	}

	public String getSpsAltUser() {
		return spsAltUser;
	}

	public void setSpsAltUser(String spsAltUser) {
		this.spsAltUser = spsAltUser;
	}

	public Date getSpsCreDate() {
		return spsCreDate;
	}

	public void setSpsCreDate(Date spsCreDate) {
		this.spsCreDate = spsCreDate;
	}

	public Date getSpsAltDate() {
		return spsAltDate;
	}

	public void setSpsAltDate(Date spsAltDate) {
		this.spsAltDate = spsAltDate;
	}

	public String getSpsIsdel() {
		return spsIsdel;
	}

	public void setSpsIsdel(String spsIsdel) {
		this.spsIsdel = spsIsdel;
	}

	public String getSpsCode() {
		return spsCode;
	}

	public void setSpsCode(String spsCode) {
		this.spsCode = spsCode;
	}

	public String getSpsOutName() {
		return spsOutName;
	}

	public void setSpsOutName(String spsOutName) {
		this.spsOutName = spsOutName;
	}

	public String getSpsContent() {
		return spsContent;
	}

	public void setSpsContent(String spsContent) {
		this.spsContent = spsContent;
	}

	public Date getSpsUndoDate() {
		return spsUndoDate;
	}

	public void setSpsUndoDate(Date spsUndoDate) {
		this.spsUndoDate = spsUndoDate;
	}

	public String getSpsUndoUser() {
		return spsUndoUser;
	}

	public void setSpsUndoUser(String spsUndoUser) {
		this.spsUndoUser = spsUndoUser;
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
}