package com.psit.struts.entity;

/**
 * 
 * 入库明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:34:08 PM<br>
 * @author zjr
 */

public class RWinPro implements java.io.Serializable {

	// Fields

	private Long rwiId;
	private WmsWarIn wmsWarIn;
	private WmsProduct wmsProduct;
	private Double rwiWinNum;
	private String rwiRemark;

	// Constructors

	/** default constructor */
	public RWinPro() {
	}

	/** full constructor */
	public RWinPro(WmsWarIn wmsWarIn, WmsProduct wmsProduct, Double rwiWinNum,
			String rwi1, String rwi2, String rwi3, String rwi4, String rwi5) {
		this.wmsWarIn = wmsWarIn;
		this.wmsProduct = wmsProduct;
		this.rwiWinNum = rwiWinNum;
	}

	// Property accessors

	public Long getRwiId() {
		return this.rwiId;
	}

	public void setRwiId(Long rwiId) {
		this.rwiId = rwiId;
	}

	public WmsWarIn getWmsWarIn() {
		return this.wmsWarIn;
	}

	public void setWmsWarIn(WmsWarIn wmsWarIn) {
		this.wmsWarIn = wmsWarIn;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public Double getRwiWinNum() {
		return rwiWinNum;
	}

	public void setRwiWinNum(Double rwiWinNum) {
		this.rwiWinNum = rwiWinNum;
	}

	public String getRwiRemark() {
		return rwiRemark;
	}

	public void setRwiRemark(String rwiRemark) {
		this.rwiRemark = rwiRemark;
	}
}