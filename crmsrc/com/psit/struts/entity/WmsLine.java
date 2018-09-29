package com.psit.struts.entity;

import java.util.Date;

/**
 * 库存流水实体 <br>
 * create_date: Aug 27, 2010,3:30:51 PM<br>
 * @author csg
 */

public class WmsLine implements java.io.Serializable {

	// Fields

	private Long wliId;
	private WmsProduct wmsProduct;
	private WmsStro wmsStro;
	private String wliTypeCode;
	private String wliType;
	private Double wliInNum;
	private Double wliOutNum;
	private Date wliDate;
	private String wliState;
	private String wliMan;
	private Long wliWmsId;
	private String wliIsdel;
	private Double wmsAllNum;//总库存（未放入数据库）
	private Double wliNum;//各个仓库的库存
	// Constructors

	/** default constructor */
	public WmsLine() {
	}

	/** full constructor */
	public WmsLine(WmsProduct wmsProduct, WmsStro wmsStro, String wliTypeCode,
			String wliType, Double wliInNum, Double wliOutNum, Date wliDate,
			String wliState, String wliMan,Long wliWmsId,String wliIsdel,Double wmsAllNum,Double wliNum) {
		this.wmsProduct = wmsProduct;
		this.wmsStro = wmsStro;
		this.wliTypeCode = wliTypeCode;
		this.wliType = wliType;
		this.wliInNum = wliInNum;
		this.wliOutNum = wliOutNum;
		this.wliDate = wliDate;
		this.wliState = wliState;
		this.wliMan = wliMan;
		this.wliWmsId=wliWmsId;
		this.wliIsdel=wliIsdel;
		this.wmsAllNum=wmsAllNum;
		this.wliNum=wliNum;
	}

	// Property accessors

	public Long getWliId() {
		return this.wliId;
	}

	public void setWliId(Long wliId) {
		this.wliId = wliId;
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

	public String getWliTypeCode() {
		return this.wliTypeCode;
	}

	public void setWliTypeCode(String wliTypeCode) {
		this.wliTypeCode = wliTypeCode;
	}

	public String getWliType() {
		return this.wliType;
	}

	public void setWliType(String wliType) {
		this.wliType = wliType;
	}

	public Double getWliInNum() {
		return this.wliInNum;
	}

	public void setWliInNum(Double wliInNum) {
		this.wliInNum = wliInNum;
	}

	public Double getWliOutNum() {
		return this.wliOutNum;
	}

	public void setWliOutNum(Double wliOutNum) {
		this.wliOutNum = wliOutNum;
	}

	public Date getWliDate() {
		return this.wliDate;
	}

	public void setWliDate(Date wliDate) {
		this.wliDate = wliDate;
	}

	public String getWliState() {
		return this.wliState;
	}

	public void setWliState(String wliState) {
		this.wliState = wliState;
	}

	public String getWliMan() {
		return this.wliMan;
	}

	public void setWliMan(String wliMan) {
		this.wliMan = wliMan;
	}

	public Long getWliWmsId() {
		return wliWmsId;
	}

	public void setWliWmsId(Long wliWmsId) {
		this.wliWmsId = wliWmsId;
	}

	public String getWliIsdel() {
		return wliIsdel;
	}

	public void setWliIsdel(String wliIsdel) {
		this.wliIsdel = wliIsdel;
	}

	public Double getWmsAllNum() {
		return wmsAllNum;
	}

	public void setWmsAllNum(Double wmsAllNum) {
		this.wmsAllNum = wmsAllNum;
	}

	public Double getWliNum() {
		return wliNum;
	}

	public void setWliNum(Double wliNum) {
		this.wliNum = wliNum;
	}

}