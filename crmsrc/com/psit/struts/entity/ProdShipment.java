package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProdShipment implements java.io.Serializable {
	private Long pshId;
	private String pshNum;
	private SalOrdCon pshOrder;
	private Date pshDeliveryDate;
	private String pshAddr;
	private String pshConsignee;
	private String pshPho;
	private String pshPostCode;
	private Double pshProdAmt;
	private String pshExpress;
	private Double pshAmt;
	private Double pshSalBack;
	private String pshShipper;
	private String pshRemark;
	private Date pshInpDate;
	private String pshInpUser;
	private Date pshUpdDate;
	private String pshUpdUser;
	private Set rshipPros = new HashSet(0);
	
	public ProdShipment() {
    }
	
	public ProdShipment(Long pshId) {
		this.pshId = pshId;
    }
	
	public ProdShipment(String pshNum, SalOrdCon pshOrder,
			Date pshDeliveryDate, String pshAddr, String pshConsignee,
			String pshPho, String pshPostCode, Double pshProdAmt,
			Double pshSalBack, String pshExpress, Double pshAmt,
			String pshShipper, String pshRemark, Date pshInpDate,
			String pshInpUser, Date pshUpdDate, String pshUpdUser) {
		this.pshNum = pshNum;
		this.pshOrder = pshOrder;
		this.pshDeliveryDate = pshDeliveryDate;
		this.pshAddr = pshAddr;
		this.pshConsignee = pshConsignee;
		this.pshPho = pshPho;
		this.pshPostCode = pshPostCode;
		this.pshProdAmt = pshProdAmt;
		this.pshExpress = pshExpress;
		this.pshAmt = pshAmt;
		this.pshSalBack = pshSalBack;
		this.pshShipper = pshShipper;
		this.pshRemark = pshRemark;
		this.pshInpUser = pshInpUser;
		this.pshInpDate = pshInpDate;
		this.pshUpdDate = pshUpdDate;
		this.pshUpdUser = pshUpdUser;
	}

	public Long getPshId() {
		return pshId;
	}

	public void setPshId(Long pshId) {
		this.pshId = pshId;
	}

	public SalOrdCon getPshOrder() {
		return pshOrder;
	}

	public void setPshOrder(SalOrdCon pshOrder) {
		this.pshOrder = pshOrder;
	}

	public String getPshConsignee() {
		return pshConsignee;
	}

	public void setPshConsignee(String pshConsignee) {
		this.pshConsignee = pshConsignee;
	}

	public String getPshExpress() {
		return pshExpress;
	}

	public void setPshExpress(String pshExpress) {
		this.pshExpress = pshExpress;
	}

	public Double getPshAmt() {
		return pshAmt;
	}

	public void setPshAmt(Double pshAmt) {
		this.pshAmt = pshAmt;
	}

	public String getPshShipper() {
		return pshShipper;
	}

	public void setPshShipper(String pshShipper) {
		this.pshShipper = pshShipper;
	}

	public String getPshRemark() {
		return pshRemark;
	}

	public void setPshRemark(String pshRemark) {
		this.pshRemark = pshRemark;
	}

	public Date getPshInpDate() {
		return pshInpDate;
	}

	public void setPshInpDate(Date pshInpDate) {
		this.pshInpDate = pshInpDate;
	}

	public String getPshInpUser() {
		return pshInpUser;
	}

	public void setPshInpUser(String pshInpUser) {
		this.pshInpUser = pshInpUser;
	}

	public Date getPshUpdDate() {
		return pshUpdDate;
	}

	public void setPshUpdDate(Date pshUpdDate) {
		this.pshUpdDate = pshUpdDate;
	}

	public String getPshUpdUser() {
		return pshUpdUser;
	}

	public void setPshUpdUser(String pshUpdUser) {
		this.pshUpdUser = pshUpdUser;
	}

	public Date getPshDeliveryDate() {
		return pshDeliveryDate;
	}

	public void setPshDeliveryDate(Date pshDeliveryDate) {
		this.pshDeliveryDate = pshDeliveryDate;
	}

	public String getPshAddr() {
		return pshAddr;
	}

	public void setPshAddr(String pshAddr) {
		this.pshAddr = pshAddr;
	}

	public String getPshNum() {
		return pshNum;
	}

	public void setPshNum(String pshNum) {
		this.pshNum = pshNum;
	}

	public Set getRshipPros() {
		return rshipPros;
	}

	public void setRshipPros(Set rshipPros) {
		this.rshipPros = rshipPros;
	}

	public String getPshPho() {
		return pshPho;
	}

	public void setPshPho(String pshPho) {
		this.pshPho = pshPho;
	}

	public String getPshPostCode() {
		return pshPostCode;
	}

	public void setPshPostCode(String pshPostCode) {
		this.pshPostCode = pshPostCode;
	}

	public Double getPshProdAmt() {
		return pshProdAmt;
	}

	public void setPshProdAmt(Double pshProdAmt) {
		this.pshProdAmt = pshProdAmt;
	}

	public Double getPshSalBack() {
		return pshSalBack;
	}

	public void setPshSalBack(Double pshSalBack) {
		this.pshSalBack = pshSalBack;
	}
}
