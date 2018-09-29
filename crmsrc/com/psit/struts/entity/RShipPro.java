package com.psit.struts.entity;

import java.util.Date;

public class RShipPro implements java.io.Serializable {

	// Fields
	private Long rshpId;
	private ProdShipment rshpShipment;
	private WmsProduct rshpProd;
	private Double rshpProdAmt;
	private Double rshpCount;
	private String rshpUnit1;
	private String rshpUnit2;
	private Double rshpPackCount1;
	private Double rshpPackCount2;
	private Double rshpAmt1;
	private Double rshpAmt2;
	private Double rshpPrice;
	private String rshpTax;
	private Double rshpSalBack;
	private Double rshpOutCount;

	// Constructors
	
	/** default constructor */
	public RShipPro() {
	}

	/** full constructor */
	public RShipPro(ProdShipment rshpShipment, WmsProduct rshpProd,
			Double rshpProdAmt, Double rshpCount, Double rshpPackCount1,
			Double rshpPackCount2, String rshpUnit1, String rshpUnit2,
			Double rshpAmt1, Double rshpAmt2, Double rshpPrice, String rshpTax,
			Double rshpSalBack,Double rshpOutCount) {
		this.rshpShipment = rshpShipment;
		this.rshpProd = rshpProd;
		this.rshpProdAmt = rshpProdAmt;
		this.rshpCount = rshpCount;
		this.rshpUnit1 = rshpUnit1;
		this.rshpUnit2 = rshpUnit2;
		this.rshpPackCount1 = rshpPackCount1;
		this.rshpPackCount2 = rshpPackCount2;
		this.rshpAmt1 = rshpAmt1;
		this.rshpAmt2 = rshpAmt2;
		this.rshpPrice = rshpPrice;
		this.rshpTax = rshpTax;
		this.rshpSalBack = rshpSalBack;
		this.rshpOutCount = rshpOutCount;
	}



	// Property accessors
	public Double getRshpPrice() {
		return rshpPrice;
	}

	public void setRshpPrice(Double rshpPrice) {
		this.rshpPrice = rshpPrice;
	}

	public String getRshpTax() {
		return rshpTax;
	}

	public void setRshpTax(String rshpTax) {
		this.rshpTax = rshpTax;
	}

	public Long getRshpId() {
		return rshpId;
	}

	public void setRshpId(Long rshpId) {
		this.rshpId = rshpId;
	}

	public ProdShipment getRshpShipment() {
		return rshpShipment;
	}

	public void setRshpShipment(ProdShipment rshpShipment) {
		this.rshpShipment = rshpShipment;
	}

	public WmsProduct getRshpProd() {
		return rshpProd;
	}

	public void setRshpProd(WmsProduct rshpProd) {
		this.rshpProd = rshpProd;
	}

	public Double getRshpCount() {
		return rshpCount;
	}

	public void setRshpCount(Double rshpCount) {
		this.rshpCount = rshpCount;
	}

	public Double getRshpProdAmt() {
		return rshpProdAmt;
	}

	public void setRshpProdAmt(Double rshpProdAmt) {
		this.rshpProdAmt = rshpProdAmt;
	}

	public Double getRshpSalBack() {
		return rshpSalBack;
	}

	public void setRshpSalBack(Double rshpSalBack) {
		this.rshpSalBack = rshpSalBack;
	}

	public Double getRshpAmt1() {
		return rshpAmt1;
	}

	public void setRshpAmt1(Double rshpAmt1) {
		this.rshpAmt1 = rshpAmt1;
	}

	public Double getRshpAmt2() {
		return rshpAmt2;
	}

	public void setRshpAmt2(Double rshpAmt2) {
		this.rshpAmt2 = rshpAmt2;
	}

	public String getRshpUnit1() {
		return rshpUnit1;
	}

	public void setRshpUnit1(String rshpUnit1) {
		this.rshpUnit1 = rshpUnit1;
	}

	public String getRshpUnit2() {
		return rshpUnit2;
	}

	public void setRshpUnit2(String rshpUnit2) {
		this.rshpUnit2 = rshpUnit2;
	}

	public Double getRshpPackCount1() {
		return rshpPackCount1;
	}

	public void setRshpPackCount1(Double rshpPackCount1) {
		this.rshpPackCount1 = rshpPackCount1;
	}

	public Double getRshpPackCount2() {
		return rshpPackCount2;
	}

	public void setRshpPackCount2(Double rshpPackCount2) {
		this.rshpPackCount2 = rshpPackCount2;
	}

	public Double getRshpOutCount() {
		return rshpOutCount;
	}

	public void setRshpOutCount(Double rshpOutCount) {
		this.rshpOutCount = rshpOutCount;
	}

}