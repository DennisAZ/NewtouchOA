package com.psit.struts.entity;

/**
 * ProdSalBack entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ProdSalBack implements java.io.Serializable {

	// Fields

	private Long psbId;
	private WmsProduct psbProduct;
	private Double psbPrice;
	private Float psbRate;

	// Constructors

	/** default constructor */
	public ProdSalBack() {
	}

	/** full constructor */
	public ProdSalBack(WmsProduct psbProduct, Double psbPrice, Float psbRate) {
		this.psbProduct = psbProduct;
		this.psbPrice = psbPrice;
		this.psbRate = psbRate;
	}

	// Property accessors

	public Long getPsbId() {
		return this.psbId;
	}

	public void setPsbId(Long psbId) {
		this.psbId = psbId;
	}

	public Double getPsbPrice() {
		return this.psbPrice;
	}

	public void setPsbPrice(Double psbPrice) {
		this.psbPrice = psbPrice;
	}

	public Float getPsbRate() {
		return this.psbRate;
	}

	public void setPsbRate(Float psbRate) {
		this.psbRate = psbRate;
	}

	public WmsProduct getPsbProduct() {
		return psbProduct;
	}

	public void setPsbProduct(WmsProduct psbProduct) {
		this.psbProduct = psbProduct;
	}

}