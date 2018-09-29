package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 入库单实体 <br>
 */

public class WmsWarIn implements java.io.Serializable {

	// Fields
	private Long wwiId;
	private String wwiCode;
	private WmsStro wmsStro;
	private String wwiOpman;
	private String wwiTitle;
	private Date wwiAppDate;
	private Date wwiInDate;
	private String wwiState;
	private String wwiRemark;
	private String wwiIsdel;
	private String wwiInpName;
	private String wwiAltName;
	private Date wwiInpTime;
	private Date wwiAltTime;
	private String wwiAppMan;
	private String wwiAppDesc;
	private String wwiAppIsok;
	private Date wwiCanDate;
	private String wwiCanMan;
	private Set RWinPros = new HashSet(0);

	// Constructors

	/** default constructor */
	public WmsWarIn() {
	}

	/** full constructor */
	public WmsWarIn(WmsStro wmsStro, String wwiOpman, String wwiTitle,
			Date wwiInpDate, Date wwiInDate, String wwiState, String wwiRemark,Date wwiCanDate,
			String wwi1,String wwiCanMan,Set RWinPros ) {
		this.wmsStro = wmsStro;
		this.wwiOpman = wwiOpman;
		this.wwiTitle = wwiTitle;
		this.wwiAppDate = wwiInpDate;
		this.wwiInDate = wwiInDate;
		this.wwiState = wwiState;
		this.wwiRemark = wwiRemark;
		this.wwiIsdel = wwi1;
		this.RWinPros = RWinPros;
		this.wwiCanDate=wwiCanDate;
		this.wwiCanMan=wwiCanMan;
	}

	// Property accessors

	public String getWwiCanMan() {
		return wwiCanMan;
	}

	public void setWwiCanMan(String wwiCanMan) {
		this.wwiCanMan = wwiCanMan;
	}

	public String getWwiCode() {
		return this.wwiCode;
	}

	public void setWwiCode(String wwiCode) {
		this.wwiCode = wwiCode;
	}

	public WmsStro getWmsStro() {
		return this.wmsStro;
	}

	public void setWmsStro(WmsStro wmsStro) {
		this.wmsStro = wmsStro;
	}

	public String getWwiTitle() {
		return this.wwiTitle;
	}

	public void setWwiTitle(String wwiTitle) {
		this.wwiTitle = wwiTitle;
	}


	public Date getWwiInDate() {
		return this.wwiInDate;
	}

	public void setWwiInDate(Date wwiInDate) {
		this.wwiInDate = wwiInDate;
	}

	public String getWwiState() {
		return this.wwiState;
	}

	public void setWwiState(String wwiState) {
		this.wwiState = wwiState;
	}

	public String getWwiRemark() {
		return this.wwiRemark;
	}

	public String getWwiIsdel() {
		return wwiIsdel;
	}

	public void setWwiIsdel(String wwiIsdel) {
		this.wwiIsdel = wwiIsdel;
	}

	public void setWwiRemark(String wwiRemark) {
		this.wwiRemark = wwiRemark;
	}

	public Set getRWinPros() {
		return this.RWinPros;
	}

	public void setRWinPros(Set RWinPros) {
		this.RWinPros = RWinPros;
	}

	public Long getWwiId() {
		return wwiId;
	}

	public void setWwiId(Long wwiId) {
		this.wwiId = wwiId;
	}

	public String getWwiInpName() {
		return wwiInpName;
	}

	public void setWwiInpName(String wwiInpName) {
		this.wwiInpName = wwiInpName;
	}

	public String getWwiAltName() {
		return wwiAltName;
	}

	public void setWwiAltName(String wwiAltName) {
		this.wwiAltName = wwiAltName;
	}

	public Date getWwiInpTime() {
		return wwiInpTime;
	}

	public void setWwiInpTime(Date wwiInpTime) {
		this.wwiInpTime = wwiInpTime;
	}

	public Date getWwiAltTime() {
		return wwiAltTime;
	}

	public void setWwiAltTime(Date wwiAltTime) {
		this.wwiAltTime = wwiAltTime;
	}

	public Date getWwiAppDate() {
		return wwiAppDate;
	}

	public void setWwiAppDate(Date wwiAppDate) {
		this.wwiAppDate = wwiAppDate;
	}

	public String getWwiAppMan() {
		return wwiAppMan;
	}

	public void setWwiAppMan(String wwiAppMan) {
		this.wwiAppMan = wwiAppMan;
	}

	public String getWwiAppDesc() {
		return wwiAppDesc;
	}

	public void setWwiAppDesc(String wwiAppDesc) {
		this.wwiAppDesc = wwiAppDesc;
	}

	public String getWwiAppIsok() {
		return wwiAppIsok;
	}

	public void setWwiAppIsok(String wwiAppIsok) {
		this.wwiAppIsok = wwiAppIsok;
	}

	public String getWwiOpman() {
		return wwiOpman;
	}

	public void setWwiOpman(String wwiOpman) {
		this.wwiOpman = wwiOpman;
	}

	public Date getWwiCanDate() {
		return wwiCanDate;
	}

	public void setWwiCanDate(Date wwiCanDate) {
		this.wwiCanDate = wwiCanDate;
	}

}