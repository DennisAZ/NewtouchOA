package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 出库单实体 <br>
 * create_date: Aug 27, 2010,3:33:46 PM<br>
 * @author csg
 */

public class WmsWarOut implements java.io.Serializable {

	// Fields
	private Long wwoId;
	private String wwoCode;
	private WmsStro wmsStro;
	private SalOrdCon salOrdCon;
	private LimUser limUser;
	private String wwoTitle;
	private Date wwoInpDate;
	private Date wwoOutDate;
	private String wwoState;
	private String wwoRemark;
	private String wwoIsdel;
	private Date wwoAltDate;
	private String wwoInpName;
	private String wwoAltName;
	private String wwoResName;
	private String wwoUserName;
	private Date wwoAppDate;
	private String wwoAppMan;
	private String wwoAppDesc;
	private String wwoAppIsok;
	private Date wwoCanDate;
	private String wwoCanMan;
	private Set RWoutPros = new HashSet(0);
	

	// Constructors

	/** default constructor */
	public WmsWarOut() {
	}

	/** full constructor */
	public WmsWarOut(WmsStro wmsStro, SalOrdCon salOrdCon, LimUser limUser,
			String wwoTitle, Date wwoInpDate, Date wwoOutDate, String wwoState,Date wwoAltDate,
			String wwoRemark, String wwo1, Date wwoCanDate,String wwoCanMan, Set RWoutPros) {
		this.wmsStro = wmsStro;
		this.salOrdCon = salOrdCon;
		this.limUser = limUser;
		this.wwoTitle = wwoTitle;
		this.wwoInpDate = wwoInpDate;
		this.wwoOutDate = wwoOutDate;
		this.wwoAltDate = wwoAltDate;
		this.wwoState = wwoState;
		this.wwoRemark = wwoRemark;
		this.wwoIsdel = wwo1;
		this.RWoutPros = RWoutPros;
		this.wwoCanDate=wwoCanDate;
		this.wwoCanMan=wwoCanMan;
	}

	// Property accessors

	public String getWwoCode() {
		return this.wwoCode;
	}

	public void setWwoCode(String wwoCode) {
		this.wwoCode = wwoCode;
	}

	public WmsStro getWmsStro() {
		return this.wmsStro;
	}

	public void setWmsStro(WmsStro wmsStro) {
		this.wmsStro = wmsStro;
	}

	public LimUser getLimUser() {
		return this.limUser;
	}

	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}

	public String getWwoTitle() {
		return this.wwoTitle;
	}

	public void setWwoTitle(String wwoTitle) {
		this.wwoTitle = wwoTitle;
	}

	public Date getWwoInpDate() {
		return this.wwoInpDate;
	}

	public void setWwoInpDate(Date wwoInpDate) {
		this.wwoInpDate = wwoInpDate;
	}

	public Date getWwoOutDate() {
		return this.wwoOutDate;
	}

	public void setWwoOutDate(Date wwoOutDate) {
		this.wwoOutDate = wwoOutDate;
	}

	public String getWwoState() {
		return this.wwoState;
	}

	public void setWwoState(String wwoState) {
		this.wwoState = wwoState;
	}

	public String getWwoRemark() {
		return this.wwoRemark;
	}

	public void setWwoRemark(String wwoRemark) {
		this.wwoRemark = wwoRemark;
	}

	public Set getRWoutPros() {
		return this.RWoutPros;
	}

	public void setRWoutPros(Set RWoutPros) {
		this.RWoutPros = RWoutPros;
	}

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}

	public String getWwoIsdel() {
		return wwoIsdel;
	}

	public void setWwoIsdel(String wwoIsdel) {
		this.wwoIsdel = wwoIsdel;
	}

	public Date getWwoAltDate() {
		return wwoAltDate;
	}

	public void setWwoAltDate(Date wwoAltDate) {
		this.wwoAltDate = wwoAltDate;
	}

	public String getWwoInpName() {
		return wwoInpName;
	}

	public void setWwoInpName(String wwoInpName) {
		this.wwoInpName = wwoInpName;
	}

	public String getWwoAltName() {
		return wwoAltName;
	}

	public void setWwoAltName(String wwoAltName) {
		this.wwoAltName = wwoAltName;
	}

	public String getWwoResName() {
		return wwoResName;
	}

	public void setWwoResName(String wwoResName) {
		this.wwoResName = wwoResName;
	}

	public String getWwoUserName() {
		return wwoUserName;
	}

	public void setWwoUserName(String wwoUserName) {
		this.wwoUserName = wwoUserName;
	}

	public Long getWwoId() {
		return wwoId;
	}

	public void setWwoId(Long wwoId) {
		this.wwoId = wwoId;
	}

	public Date getWwoAppDate() {
		return wwoAppDate;
	}

	public void setWwoAppDate(Date wwoAppDate) {
		this.wwoAppDate = wwoAppDate;
	}

	public String getWwoAppMan() {
		return wwoAppMan;
	}

	public void setWwoAppMan(String wwoAppMan) {
		this.wwoAppMan = wwoAppMan;
	}

	public String getWwoAppDesc() {
		return wwoAppDesc;
	}

	public void setWwoAppDesc(String wwoAppDesc) {
		this.wwoAppDesc = wwoAppDesc;
	}

	public String getWwoAppIsok() {
		return wwoAppIsok;
	}

	public void setWwoAppIsok(String wwoAppIsok) {
		this.wwoAppIsok = wwoAppIsok;
	}

	public Date getWwoCanDate() {
		return wwoCanDate;
	}

	public void setWwoCanDate(Date wwoCanDate) {
		this.wwoCanDate = wwoCanDate;
	}

	public String getWwoCanMan() {
		return wwoCanMan;
	}

	public void setWwoCanMan(String wwoCanMan) {
		this.wwoCanMan = wwoCanMan;
	}

}