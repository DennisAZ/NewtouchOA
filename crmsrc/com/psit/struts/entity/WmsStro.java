package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 仓库实体 <br>
 * create_date: Aug 27, 2010,3:33:15 PM<br>
 * @author csg
 */

public class WmsStro implements java.io.Serializable {

	// Fields

	private String wmsCode;
	private LimUser limUser;
	private TypeList wmsStroType;
	private String wmsName;
	private String wmsLoc;
	private Date wmsCreDate;
	private String wmsRemark;
	private String wmsIsenabled;
	private Set RStroPros = new HashSet(0);
	private Set wmsWarOuts = new HashSet(0);

	// Constructors

	/** default constructor */
	public WmsStro() {
	}

	/** minimal constructor */
	public WmsStro(String wmsCode) {
		this.wmsCode = wmsCode;
	}

	/** full constructor */
	public WmsStro(String wmsCode, LimUser limUser, TypeList wmsStroType,
			String wmsName, String wmsLoc, Date wmsCreDate, String wmsRemark,
			String wmsIsenabled, Set RStroPros, Set wmsWarOuts) {
		this.wmsCode = wmsCode;
		this.limUser = limUser;
		this.wmsStroType = wmsStroType;
		this.wmsName = wmsName;
		this.wmsLoc = wmsLoc;
		this.wmsCreDate = wmsCreDate;
		this.wmsRemark = wmsRemark;
		this.wmsIsenabled = wmsIsenabled;
		this.RStroPros = RStroPros;
		this.wmsWarOuts = wmsWarOuts;
	}

	// Property accessors

	public String getWmsCode() {
		return this.wmsCode;
	}

	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}

	public LimUser getLimUser() {
		return this.limUser;
	}

	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}

	public TypeList getWmsStroType() {
		return this.wmsStroType;
	}

	public void setWmsStroType(TypeList wmsStroType) {
		this.wmsStroType = wmsStroType;
	}

	public String getWmsName() {
		return this.wmsName;
	}

	public void setWmsName(String wmsName) {
		this.wmsName = wmsName;
	}

	public String getWmsLoc() {
		return this.wmsLoc;
	}

	public void setWmsLoc(String wmsLoc) {
		this.wmsLoc = wmsLoc;
	}

	public Date getWmsCreDate() {
		return this.wmsCreDate;
	}

	public void setWmsCreDate(Date wmsCreDate) {
		this.wmsCreDate = wmsCreDate;
	}

	public String getWmsRemark() {
		return this.wmsRemark;
	}

	public void setWmsRemark(String wmsRemark) {
		this.wmsRemark = wmsRemark;
	}

	public String getWmsIsenabled() {
		return wmsIsenabled;
	}

	public void setWmsIsenabled(String wmsIsenabled) {
		this.wmsIsenabled = wmsIsenabled;
	}

	public Set getRStroPros() {
		return this.RStroPros;
	}

	public void setRStroPros(Set RStroPros) {
		this.RStroPros = RStroPros;
	}

	public Set getWmsWarOuts() {
		return this.wmsWarOuts;
	}

	public void setWmsWarOuts(Set wmsWarOuts) {
		this.wmsWarOuts = wmsWarOuts;
	}

}