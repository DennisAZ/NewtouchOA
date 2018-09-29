package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 权限操作表实体 <br>
 *
 * create_date: Aug 27, 2010,3:23:24 PM<br>
 * @author zjr
 */

public class LimOperate implements java.io.Serializable {

	// Fields

	private String opeCode;
	private String opeDesc;
	private String ope1;
	private Set limRights = new HashSet(0);

	// Constructors

	/** default constructor */
	public LimOperate() {
	}

	/** minimal constructor */
	public LimOperate(String opeCode) {
		this.opeCode = opeCode;
	}

	/** full constructor */
	public LimOperate(String opeCode, String opeDesc,Set limRights) {
		this.opeCode = opeCode;
		this.opeDesc = opeDesc;
		this.limRights = limRights;
	}

	// Property accessors

	public String getOpeCode() {
		return this.opeCode;
	}

	public void setOpeCode(String opeCode) {
		this.opeCode = opeCode;
	}

	public String getOpeDesc() {
		return this.opeDesc;
	}

	public void setOpeDesc(String opeDesc) {
		this.opeDesc = opeDesc;
	}

	public Set getLimRights() {
		return this.limRights;
	}

	public void setLimRights(Set limRights) {
		this.limRights = limRights;
	}

}