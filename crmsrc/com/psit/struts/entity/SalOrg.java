package com.psit.struts.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 
 * 部门实体 <br>
 *
 * create_date: Aug 27, 2010,3:37:13 PM<br>
 * @author zjr
 */

public class SalOrg implements java.io.Serializable {

	// Fields
	private String soCode;
	private SalOrg salOrg;
	private String soName;
	private String soConArea;
	private String soLoc;
	private String soUserCode;
	private String soEmpNum;
	private String soResp;
	private String soOrgCode;
	private String soRemark;
	private String soIsenabled;
	private String soCostCenter;
	private String soOrgNature;
	private Set salOrgs = new HashSet(0);
	private Set limUsers = new LinkedHashSet(0);
	private Set salEmps = new HashSet(0);

	// Constructors

	/** default constructor */
	public SalOrg() {
	}
    public SalOrg(String soCode)
    {
    	this.soCode=soCode;
    }
	/** full constructor */
	public SalOrg(SalOrg salOrg,String soName, String soConArea, String soLoc,
			String soUserCode, String soEmpNum, String soResp, String soOrgCode,
			String soRemark, String soIsenabled,  String soCostCenter,
			String soOrgNature,Set salOrgs, Set limUsers, Set salEmps) {
	
		this.soName = soName;
		this.soConArea = soConArea;
		this.salOrg=salOrg;
		this.soLoc = soLoc;
		this.soUserCode = soUserCode;
		this.soEmpNum = soEmpNum;
		this.soResp = soResp;
		this.soOrgCode = soOrgCode;
		this.soRemark = soRemark;
		this.soIsenabled = soIsenabled;
		this.salOrgs = salOrgs;
		this.soCostCenter = soCostCenter;
		this.soOrgNature = soOrgNature;
		this.limUsers = limUsers;
		this.salEmps = salEmps;
	}

	// Property accessors

	public String getSoCode() {
		return this.soCode;
	}

	public void setSoCode(String soCode) {
		this.soCode = soCode;
	}

	public String getSoName() {
		return this.soName;
	}

	public void setSoName(String soName) {
		this.soName = soName;
	}

	public String getSoConArea() {
		return this.soConArea;
	}

	public void setSoConArea(String soConArea) {
		this.soConArea = soConArea;
	}

	public String getSoLoc() {
		return this.soLoc;
	}

	public void setSoLoc(String soLoc) {
		this.soLoc = soLoc;
	}

	public String getSoUserCode() {
		return this.soUserCode;
	}

	public void setSoUserCode(String soUserCode) {
		this.soUserCode = soUserCode;
	}

	public String getSoEmpNum() {
		return this.soEmpNum;
	}

	public void setSoEmpNum(String soEmpNum) {
		this.soEmpNum = soEmpNum;
	}

	public String getSoResp() {
		return StringFormat.toBlank(this.soResp);
	}

	public void setSoResp(String soResp) {
		this.soResp = soResp;
	}


	public String getSoRemark() {
		return StringFormat.toBlank(this.soRemark);
	}

	public void setSoRemark(String soRemark) {
		this.soRemark = soRemark;
	}

	public String getSoIsenabled() {
		return this.soIsenabled;
	}

	public void setSoIsenabled(String soIsenabled) {
		this.soIsenabled = soIsenabled;
	}

	public Set getLimUsers() {
		return this.limUsers;
	}

	public void setLimUsers(Set limUsers) {
		this.limUsers = limUsers;
	}

	public Set getSalEmps() {
		return this.salEmps;
	}

	public void setSalEmps(Set salEmps) {
		this.salEmps = salEmps;
	}

	public SalOrg getSalOrg() {
		return salOrg;
	}

	public void setSalOrg(SalOrg salOrg) {
		this.salOrg = salOrg;
	}

	public String getSoCostCenter() {
		return soCostCenter;
	}

	public void setSoCostCenter(String soCostCenter) {
		this.soCostCenter = soCostCenter;
	}

	public String getSoOrgNature() {
		return soOrgNature;
	}

	public void setSoOrgNature(String soOrgNature) {
		this.soOrgNature = soOrgNature;
	}

	public Set getSalOrgs() {
		return salOrgs;
	}

	public void setSalOrgs(Set salOrgs) {
		this.salOrgs = salOrgs;
	}
	public String getSoOrgCode() {
		return soOrgCode;
	}
	public void setSoOrgCode(String soOrgCode) {
		this.soOrgCode = soOrgCode;
	}

}