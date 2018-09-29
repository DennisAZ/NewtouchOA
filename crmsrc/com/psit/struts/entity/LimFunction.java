package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 权限功能表实体 <br>
 *
 * create_date: Aug 27, 2010,3:23:06 PM<br>
 * @author zjr
 */

public class LimFunction implements java.io.Serializable {

	// Fields

	private String funCode;
	private String funDesc;
	private String funType;
	private Set limRights = new HashSet(0);

	// Constructors

	/** default constructor */
	public LimFunction() {
	}

	/** minimal constructor */
	public LimFunction(String funCode) {
		this.funCode = funCode;
	}

	/** full constructor */
	public LimFunction(String funCode, String funDesc, String funType,
			 Set limRights) {
		this.funCode = funCode;
		this.funDesc = funDesc;
		this.funType = funType;
		this.limRights = limRights;
	}

	// Property accessors

	public String getFunCode() {
		return this.funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}

	public String getFunDesc() {
		return this.funDesc;
	}

	public void setFunDesc(String funDesc) {
		this.funDesc = funDesc;
	}

	public Set getLimRights() {
		return this.limRights;
	}

	public void setLimRights(Set limRights) {
		this.limRights = limRights;
	}

	public String getFunType() {
		return funType;
	}

	public void setFunType(String funType) {
		this.funType = funType;
	}

}