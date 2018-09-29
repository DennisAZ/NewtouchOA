package com.psit.struts.entity;

/**
 * 
 * 调拨明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:34:40 PM<br>
 * @author zjr
 */

public class RWmsWms implements java.io.Serializable {

	// Fields

	private Long rwwId;
	private WmsProduct wmsProduct;
	private WmsChange wmsChange;
	private Double rwwNum;
	private String rwwRemark;

	// Constructors

	/** default constructor */
	public RWmsWms() {
	}

	/** full constructor */
	public RWmsWms(WmsProduct wmsProduct, WmsChange wmsChange, Double rwwNum,
			String rwwRemark) {
		this.wmsProduct = wmsProduct;
		this.wmsChange = wmsChange;
		this.rwwNum = rwwNum;
		this.rwwRemark = rwwRemark;
	}

	// Property accessors

	public Long getRwwId() {
		return this.rwwId;
	}

	public void setRwwId(Long rwwId) {
		this.rwwId = rwwId;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public WmsChange getWmsChange() {
		return this.wmsChange;
	}

	public void setWmsChange(WmsChange wmsChange) {
		this.wmsChange = wmsChange;
	}

	public Double getRwwNum() {
		return rwwNum;
	}

	public void setRwwNum(Double rwwNum) {
		this.rwwNum = rwwNum;
	}

	public String getRwwRemark() {
		return rwwRemark;
	}

	public void setRwwRemark(String rwwRemark) {
		this.rwwRemark = rwwRemark;
	}

}