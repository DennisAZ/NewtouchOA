package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 库存盘点实体 <br>
 * create_date: Aug 27, 2010,3:30:29 PM<br>
 * @author csg
 */

public class WmsCheck implements java.io.Serializable {

	// Fields

	private Long wmcId;
	private String wmcCode;
	private WmsStro wmsStro;
	private String wmcOpman;
	private String wmcTitle;
	private Date wmcDate;
	private String wmcState;
	private String wmcRemark;
	private String wmcIsdel;
	private String wmcInpName;
	private String wmcAltName;
	private Date wmcInpDate;
	private Date wmcAltDate;
	private Date wmcAppDate;
	private String wmcAppMan;
	private String wmcAppDesc;
	private String wmcAppIsok;
	private Date wmcCanDate;
	private String wmcCanMan;
	private Set RWmsChanges = new HashSet(0);

	// Constructors

	/** default constructor */
	public WmsCheck() {
	}

	/** full constructor */
	public WmsCheck(WmsStro wmsStro, String wmcOpman, String wmcTitle,String wmcCode,
			Date wmcDate, String wmcState, String wmcRemark, String wmcIsdel,Date wmcCanDate,
			String wmcCanMan,Set RWmsChanges) {
		this.wmsStro = wmsStro;
		this.wmcOpman = wmcOpman;
		this.wmcTitle = wmcTitle;
		this.wmcDate = wmcDate;
		this.wmcState = wmcState;
		this.wmcRemark = wmcRemark;
		this.wmcIsdel = wmcIsdel;
		this.RWmsChanges = RWmsChanges;
		this.wmcCode = wmcCode;
		this.wmcCanDate=wmcCanDate;
		this.wmcCanMan=wmcCanMan;
	}

	// Property accessors

	public Long getWmcId() {
		return this.wmcId;
	}

	public void setWmcId(Long wmcId) {
		this.wmcId = wmcId;
	}

	public WmsStro getWmsStro() {
		return this.wmsStro;
	}

	public void setWmsStro(WmsStro wmsStro) {
		this.wmsStro = wmsStro;
	}

	public String getWmcOpman() {
		return wmcOpman;
	}

	public void setWmcOpman(String wmcOpman) {
		this.wmcOpman = wmcOpman;
	}

	public String getWmcTitle() {
		return this.wmcTitle;
	}

	public void setWmcTitle(String wmcTitle) {
		this.wmcTitle = wmcTitle;
	}

	public Date getWmcDate() {
		return this.wmcDate;
	}

	public void setWmcDate(Date wmcDate) {
		this.wmcDate = wmcDate;
	}

	public String getWmcState() {
		return this.wmcState;
	}

	public void setWmcState(String wmcState) {
		this.wmcState = wmcState;
	}

	public String getWmcRemark() {
		return this.wmcRemark;
	}

	public void setWmcRemark(String wmcRemark) {
		this.wmcRemark = wmcRemark;
	}

	public String getWmcIsdel() {
		return this.wmcIsdel;
	}

	public void setWmcIsdel(String wmcIsdel) {
		this.wmcIsdel = wmcIsdel;
	}

	public Set getRWmsChanges() {
		return this.RWmsChanges;
	}

	public void setRWmsChanges(Set RWmsChanges) {
		this.RWmsChanges = RWmsChanges;
	}

	public String getWmcCode() {
		return wmcCode;
	}

	public void setWmcCode(String wmcCode) {
		this.wmcCode = wmcCode;
	}

	public String getWmcInpName() {
		return wmcInpName;
	}

	public void setWmcInpName(String wmcInpName) {
		this.wmcInpName = wmcInpName;
	}

	public String getWmcAltName() {
		return wmcAltName;
	}

	public void setWmcAltName(String wmcAltName) {
		this.wmcAltName = wmcAltName;
	}

	public Date getWmcInpDate() {
		return wmcInpDate;
	}

	public void setWmcInpDate(Date wmcInpDate) {
		this.wmcInpDate = wmcInpDate;
	}

	public Date getWmcAltDate() {
		return wmcAltDate;
	}

	public void setWmcAltDate(Date wmcAltDate) {
		this.wmcAltDate = wmcAltDate;
	}

	public Date getWmcAppDate() {
		return wmcAppDate;
	}

	public void setWmcAppDate(Date wmcAppDate) {
		this.wmcAppDate = wmcAppDate;
	}

	public String getWmcAppMan() {
		return wmcAppMan;
	}

	public void setWmcAppMan(String wmcAppMan) {
		this.wmcAppMan = wmcAppMan;
	}

	public String getWmcAppDesc() {
		return wmcAppDesc;
	}

	public void setWmcAppDesc(String wmcAppDesc) {
		this.wmcAppDesc = wmcAppDesc;
	}

	public String getWmcAppIsok() {
		return wmcAppIsok;
	}

	public void setWmcAppIsok(String wmcAppIsok) {
		this.wmcAppIsok = wmcAppIsok;
	}

	public Date getWmcCanDate() {
		return wmcCanDate;
	}

	public void setWmcCanDate(Date wmcCanDate) {
		this.wmcCanDate = wmcCanDate;
	}

	public String getWmcCanMan() {
		return wmcCanMan;
	}

	public void setWmcCanMan(String wmcCanMan) {
		this.wmcCanMan = wmcCanMan;
	}

}