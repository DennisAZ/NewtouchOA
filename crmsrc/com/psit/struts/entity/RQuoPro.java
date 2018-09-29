package com.psit.struts.entity;

/**
 * 
 * 报价明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:30:42 PM<br>
 * @author zjr
 */

public class RQuoPro implements java.io.Serializable {

	// Fields

	private Long rupId;
	private Quote quote;
	private WmsProduct wmsProduct;
	private Double rupNum;
	private Double rupPrice;
	private Double rupAllPrice;
	private String rupRemark;

	// Constructors

	/** default constructor */
	public RQuoPro() {
	}

	/** full constructor */
	public RQuoPro(Long rupId, Quote quote, WmsProduct wmsProduct,
			Double rupNum, Double rupPrice, Double rupAllPrice, String rupRemark) {
		this.rupId = rupId;
		this.quote = quote;
		this.wmsProduct = wmsProduct;
		this.rupNum = rupNum;
		this.rupPrice = rupPrice;
		this.rupAllPrice = rupAllPrice;
		this.rupRemark = rupRemark;
	}

	// Property accessors

	public Long getRupId() {
		return this.rupId;
	}

	public void setRupId(Long rupId) {
		this.rupId = rupId;
	}

	public Quote getQuote() {
		return this.quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public Double getRupNum() {
		return this.rupNum;
	}

	public void setRupNum(Double rupNum) {
		this.rupNum = rupNum;
	}

	public Double getRupPrice() {
		return this.rupPrice;
	}

	public void setRupPrice(Double rupPrice) {
		this.rupPrice = rupPrice;
	}

	public Double getRupAllPrice() {
		return this.rupAllPrice;
	}

	public void setRupAllPrice(Double rupAllPrice) {
		this.rupAllPrice = rupAllPrice;
	}

	public String getRupRemark() {
		return this.rupRemark;
	}

	public void setRupRemark(String rupRemark) {
		this.rupRemark = rupRemark;
	}


}