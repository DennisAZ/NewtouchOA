package com.psit.struts.entity;

/**
 * 
 * 库存明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:32:10 PM<br>
 * @author zjr
 */

public class RStroPro implements java.io.Serializable {

	// Fields

	private Long rspId;
	private WmsProduct wmsProduct;
	private WmsStro wmsStro;
	private Double rspProNum;

	// Constructors

	/** default constructor */
	public RStroPro() {
	}

	/** minimal constructor */
	public RStroPro(Long rspId) {
		this.rspId = rspId;
	}

	/** full constructor */
	public RStroPro(Long rspId, WmsProduct wmsProduct, WmsStro wmsStro,
			Double rspProNum) {
		this.rspId = rspId;
		this.wmsProduct = wmsProduct;
		this.wmsStro = wmsStro;
		this.rspProNum = rspProNum;
	}

	// Property accessors

	public Long getRspId() {
		return this.rspId;
	}

	public void setRspId(Long rspId) {
		this.rspId = rspId;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public WmsStro getWmsStro() {
		return this.wmsStro;
	}

	public void setWmsStro(WmsStro wmsStro) {
		this.wmsStro = wmsStro;
	}

	public Double getRspProNum() {
		return rspProNum;
	}

	public void setRspProNum(Double rspProNum) {
		this.rspProNum = rspProNum;
	}

}