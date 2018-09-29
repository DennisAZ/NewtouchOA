package com.psit.struts.entity;

import java.util.Date;

import com.psit.struts.util.format.StringFormat;

/**
 * 
 * 回款计划实体 <br>
 *
 * create_date: Aug 27, 2010,3:37:35 PM<br>
 * @author zjr
 */

public class SalPaidPlan implements java.io.Serializable {

	// Fields

	private Long spdId;
	private Date spdPrmDate;
	private Double spdPayMon;
	private SalEmp spdResp;
	private String spdIsp;
	private SalOrdCon salOrdCon;
	private CusCorCus cusCorCus;
	private String spdUserCode;
	private String spdAltUser;
	private Date spdCreDate;
	private Date spdAltDate;
	private String spdIsdel;
	private String spdContent;

	// Constructors

	/** default constructor */
	public SalPaidPlan() {
	}
	
	/** mini constructor */
	public SalPaidPlan(Long id) {
		spdId=id;
	}
	
	/** full constructor */
	public SalPaidPlan(LimUser limUser, Date spdPrmDate, String spdContent,
			Double spdPayMon, SalEmp spdResp, String spdIsp,
			CusCorCus cusCorCus, SalOrdCon salOrdCon, String spdUserCode,
			String spdAltUser, Date spdCreDate, Date spdAltDate, String spdIsdel) {
		this.spdPrmDate = spdPrmDate;
		this.spdPayMon = spdPayMon;
		this.spdResp = spdResp;
		this.spdIsp = spdIsp;
		this.salOrdCon = salOrdCon;
		this.cusCorCus = cusCorCus;
		this.spdUserCode = spdUserCode;
		this.spdAltUser = spdAltUser;
		this.spdCreDate = spdCreDate;
		this.spdAltDate = spdAltDate;
		this.spdIsdel = spdIsdel;
		this.spdContent=spdContent;
	}

	// Property accessors

	public Long getSpdId() {
		return this.spdId;
	}

	public void setSpdId(Long spdId) {
		this.spdId = spdId;
	}

	public Date getSpdPrmDate() {
		return this.spdPrmDate;
	}

	public void setSpdPrmDate(Date spdPrmDate) {
		this.spdPrmDate = spdPrmDate;
	}

	public Double getSpdPayMon() {
		return this.spdPayMon;
	}

	public void setSpdPayMon(Double spdPayMon) {
		this.spdPayMon = spdPayMon;
	}

	public String getSpdIsp() {
		return this.spdIsp;
	}

	public void setSpdIsp(String spdIsp) {
		this.spdIsp = spdIsp;
	}

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}

	public SalEmp getSpdResp() {
		return spdResp;
	}

	public void setSpdResp(SalEmp spdResp) {
		this.spdResp = spdResp;
	}

	public String getSpdUserCode() {
		return spdUserCode;
	}

	public void setSpdUserCode(String spdUserCode) {
		this.spdUserCode = spdUserCode;
	}

	public String getSpdAltUser() {
		return spdAltUser;
	}

	public void setSpdAltUser(String spdAltUser) {
		this.spdAltUser = spdAltUser;
	}

	public Date getSpdCreDate() {
		return spdCreDate;
	}

	public void setSpdCreDate(Date spdCreDate) {
		this.spdCreDate = spdCreDate;
	}

	public Date getSpdAltDate() {
		return spdAltDate;
	}

	public void setSpdAltDate(Date spdAltDate) {
		this.spdAltDate = spdAltDate;
	}

	public String getSpdIsdel() {
		return spdIsdel;
	}

	public void setSpdIsdel(String spdIsdel) {
		this.spdIsdel = spdIsdel;
	}

	public String getSpdContent() {
		return StringFormat.toBlank(spdContent);
	}

	public void setSpdContent(String spdContent) {
		this.spdContent = spdContent;
	}

	public CusCorCus getCusCorCus() {
		return cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}

}