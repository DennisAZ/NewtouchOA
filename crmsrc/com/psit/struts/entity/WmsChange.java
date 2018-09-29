package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 库存调拨实体 <br>
 * create_date: Aug 27, 2010,3:28:15 PM<br>
 * @author csg
 */

public class WmsChange implements java.io.Serializable {

	// Fields

	private Long wchId;
	private String checkOutName;
	private String checkInName;
	private WmsStro wmsStroByWchInWms;
	private WmsStro wmsStroByWchOutWms;
	private String wchCode;
	private String wchTitle;
	private String wchState;
	private Date wchInDate;
	private String wchRecMan;
	private String wchRemark;
	private String wchIsdel;
	private Date wchOutDate;
	private Date wchInTime;
	private Date wchOutTime;
	private String wchInpName;
	private String wchAltName;
	private Date wchInpDate;
	private Date wchAltDate;
	private Date wchAppDate;
	private String wchAppMan;
	private String wchAppDesc;
	private String wchAppIsok;
	private String wchMatName;
	private Date wchCanDate;
	private String wchCanMan;
	private Set RWmsWmses = new HashSet(0);

	// Constructors

	/** default constructor */
	public WmsChange() {
	}

	/** full constructor */
	public WmsChange(String checkOutName, String checkInName,Date wchCanDate,String wchCanMan,
			WmsStro wmsStroByWchInWms, WmsStro wmsStroByWchOutWms,
			String wchTitle, String wchState, Date wchInDate, String wchRecMan,
			String wchRemark, String wchIsdel,Date wchOutDate,String wchMatName, Set RWmsWmses) {
		this.checkOutName = checkOutName;
		this.checkInName = checkInName;
		this.wmsStroByWchInWms = wmsStroByWchInWms;
		this.wmsStroByWchOutWms = wmsStroByWchOutWms;
		this.wchTitle = wchTitle;
		this.wchState = wchState;
		this.wchInDate = wchInDate;
		this.wchRecMan = wchRecMan;
		this.wchRemark = wchRemark;
		this.wchIsdel = wchIsdel;
		this.wchOutDate = wchOutDate;
		this.wchMatName = wchMatName;
		this.RWmsWmses = RWmsWmses;
		this.wchCanDate=wchCanDate;
		this.wchCanMan=wchCanMan;
	}

	// Property accessors

	public Long getWchId() {
		return this.wchId;
	}

	public void setWchId(Long wchId) {
		this.wchId = wchId;
	}

	public String getCheckOutName() {
		return checkOutName;
	}

	public void setCheckOutName(String checkOutName) {
		this.checkOutName = checkOutName;
	}

	public String getCheckInName() {
		return checkInName;
	}

	public void setCheckInName(String checkInName) {
		this.checkInName = checkInName;
	}

	public WmsStro getWmsStroByWchInWms() {
		return this.wmsStroByWchInWms;
	}

	public void setWmsStroByWchInWms(WmsStro wmsStroByWchInWms) {
		this.wmsStroByWchInWms = wmsStroByWchInWms;
	}

	public WmsStro getWmsStroByWchOutWms() {
		return this.wmsStroByWchOutWms;
	}

	public void setWmsStroByWchOutWms(WmsStro wmsStroByWchOutWms) {
		this.wmsStroByWchOutWms = wmsStroByWchOutWms;
	}

	public String getWchTitle() {
		return this.wchTitle;
	}

	public void setWchTitle(String wchTitle) {
		this.wchTitle = wchTitle;
	}

	public String getWchState() {
		return this.wchState;
	}

	public void setWchState(String wchState) {
		this.wchState = wchState;
	}

	public Date getWchInDate() {
		return this.wchInDate;
	}

	public void setWchInDate(Date wchInDate) {
		this.wchInDate = wchInDate;
	}

	public String getWchRecMan() {
		return this.wchRecMan;
	}

	public void setWchRecMan(String wchRecMan) {
		this.wchRecMan = wchRecMan;
	}

	public String getWchRemark() {
		return this.wchRemark;
	}

	public void setWchRemark(String wchRemark) {
		this.wchRemark = wchRemark;
	}

	public String getWchIsdel() {
		return wchIsdel;
	}

	public void setWchIsdel(String wchIsdel) {
		this.wchIsdel = wchIsdel;
	}

	public Date getWchOutDate() {
		return this.wchOutDate;
	}

	public void setWchOutDate(Date wchOutDate) {
		this.wchOutDate = wchOutDate;
	}

	public Set getRWmsWmses() {
		return this.RWmsWmses;
	}

	public void setRWmsWmses(Set RWmsWmses) {
		this.RWmsWmses = RWmsWmses;
	}

	public String getWchCode() {
		return wchCode;
	}

	public void setWchCode(String wchCode) {
		this.wchCode = wchCode;
	}

	public Date getWchInTime() {
		return wchInTime;
	}

	public void setWchInTime(Date wchInTime) {
		this.wchInTime = wchInTime;
	}

	public Date getWchOutTime() {
		return wchOutTime;
	}

	public void setWchOutTime(Date wchOutTime) {
		this.wchOutTime = wchOutTime;
	}

	public String getWchInpName() {
		return wchInpName;
	}

	public void setWchInpName(String wchInpName) {
		this.wchInpName = wchInpName;
	}

	public String getWchAltName() {
		return wchAltName;
	}

	public void setWchAltName(String wchAltName) {
		this.wchAltName = wchAltName;
	}

	public Date getWchInpDate() {
		return wchInpDate;
	}

	public void setWchInpDate(Date wchInpDate) {
		this.wchInpDate = wchInpDate;
	}

	public Date getWchAltDate() {
		return wchAltDate;
	}

	public void setWchAltDate(Date wchAltDate) {
		this.wchAltDate = wchAltDate;
	}

	public Date getWchAppDate() {
		return wchAppDate;
	}

	public void setWchAppDate(Date wchAppDate) {
		this.wchAppDate = wchAppDate;
	}

	public String getWchAppMan() {
		return wchAppMan;
	}

	public void setWchAppMan(String wchAppMan) {
		this.wchAppMan = wchAppMan;
	}

	public String getWchAppDesc() {
		return wchAppDesc;
	}

	public void setWchAppDesc(String wchAppDesc) {
		this.wchAppDesc = wchAppDesc;
	}

	public String getWchAppIsok() {
		return wchAppIsok;
	}

	public void setWchAppIsok(String wchAppIsok) {
		this.wchAppIsok = wchAppIsok;
	}

	public String getWchMatName() {
		return wchMatName;
	}

	public void setWchMatName(String wchMatName) {
		this.wchMatName = wchMatName;
	}

	public Date getWchCanDate() {
		return wchCanDate;
	}

	public void setWchCanDate(Date wchCanDate) {
		this.wchCanDate = wchCanDate;
	}

	public String getWchCanMan() {
		return wchCanMan;
	}

	public void setWchCanMan(String wchCanMan) {
		this.wchCanMan = wchCanMan;
	}

}