package com.psit.struts.entity;

/**
 * 
 * 出库明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:34:52 PM<br>
 * @author zjr
 */

public class RWoutPro implements java.io.Serializable {

	// Fields

	private Long rwoId;
	private WmsProduct wmsProduct;
	private WmsWarOut wmsWarOut;
	private Double rwoWoutNum;
	private String rwoRemark;

	// Constructors

	/** default constructor */
	public RWoutPro() {
	}

	/** minimal constructor */
	public RWoutPro(Long rwoId) {
		this.rwoId = rwoId;
	}

	/** full constructor */
	public RWoutPro(Long rwoId, WmsProduct wmsProduct, WmsWarOut wmsWarOut,
			Double rwoWoutNum) {
		this.rwoId = rwoId;
		this.wmsProduct = wmsProduct;
		this.wmsWarOut = wmsWarOut;
		this.rwoWoutNum = rwoWoutNum;
	}

	// Property accessors

	public Long getRwoId() {
		return this.rwoId;
	}

	public void setRwoId(Long rwoId) {
		this.rwoId = rwoId;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public WmsWarOut getWmsWarOut() {
		return this.wmsWarOut;
	}

	public void setWmsWarOut(WmsWarOut wmsWarOut) {
		this.wmsWarOut = wmsWarOut;
	}

	public Double getRwoWoutNum() {
		return rwoWoutNum;
	}

	public void setRwoWoutNum(Double rwoWoutNum) {
		this.rwoWoutNum = rwoWoutNum;
	}

	public String getRwoRemark() {
		return rwoRemark;
	}

	public void setRwoRemark(String rwoRemark) {
		this.rwoRemark = rwoRemark;
	}

}