package com.psit.struts.entity;

/**
 * 
 * 盘点明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:34:20 PM<br>
 * @author zjr
 */

public class RWmsChange implements java.io.Serializable {

	// Fields

	private Long rwcId;
	private WmsProduct wmsProduct;
	private WmsCheck wmsCheck;
	private Double rwcDifferent;
	private String rmcType;
	private String rmcRemark;
	private Double rmcWmsCount;
	private Double rmcRealNum;
	// Constructors

	/** default constructor */
	public RWmsChange() {
	}

	/** full constructor */
	public RWmsChange(WmsProduct wmsProduct, WmsCheck wmsCheck,
			Double rwcDifferent, String rmcType, String rmcRemark,Double rmcWmsCount,Double rmcRealNum) {
		this.wmsProduct = wmsProduct;
		this.wmsCheck = wmsCheck;
		this.rwcDifferent = rwcDifferent;
		this.rmcType = rmcType;
		this.rmcRemark = rmcRemark;
		this.rmcWmsCount=rmcWmsCount;
		this.rmcRealNum=rmcRealNum;
	}

	// Property accessors

	public Long getRwcId() {
		return this.rwcId;
	}

	public void setRwcId(Long rwcId) {
		this.rwcId = rwcId;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public WmsCheck getWmsCheck() {
		return this.wmsCheck;
	}

	public void setWmsCheck(WmsCheck wmsCheck) {
		this.wmsCheck = wmsCheck;
	}


	public Double getRwcDifferent() {
		return rwcDifferent;
	}

	public void setRwcDifferent(Double rwcDifferent) {
		this.rwcDifferent = rwcDifferent;
	}

	public String getRmcType() {
		return this.rmcType;
	}

	public void setRmcType(String rmcType) {
		this.rmcType = rmcType;
	}

	public String getRmcRemark() {
		return rmcRemark;
	}

	public void setRmcRemark(String rmcRemark) {
		this.rmcRemark = rmcRemark;
	}

	public Double getRmcWmsCount() {
		return rmcWmsCount;
	}

	public void setRmcWmsCount(Double rmcWmsCount) {
		this.rmcWmsCount = rmcWmsCount;
	}

	public Double getRmcRealNum() {
		return rmcRealNum;
	}

	public void setRmcRealNum(Double rmcRealNum) {
		this.rmcRealNum = rmcRealNum;
	}

}