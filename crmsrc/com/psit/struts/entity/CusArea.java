package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 国家实体 <br>
 *
 * create_date: Aug 27, 2010,3:21:04 PM<br>
 * @author zjr
 */

public class CusArea implements java.io.Serializable {

	// Fields

	private Long areId;
	private String areName;
	private String areIsenabled;
	private Set cusCorCuses = new HashSet(0);
	private Set cusProvinces = new HashSet(0);
	private String enabledType=" ";
	// Constructors

	/** default constructor */
	public CusArea() {
	}

	/** minimal constructor */
	public CusArea(Long areId) {
		this.areId = areId;
	}

	/** full constructor */
	public CusArea(Long areId, String areName, String areIsenabled,
			Set cusCorCuses, Set cusProvinces) {
		this.areId = areId;
		this.areName = areName;
		this.areIsenabled = areIsenabled;
		this.cusCorCuses = cusCorCuses;
		this.cusProvinces = cusProvinces;
	}

	// Property accessors

	public Long getAreId() {
		return this.areId;
	}

	public void setAreId(Long areId) {
		this.areId = areId;
	}

	public String getAreName() {
		return this.areName;
	}

	public void setAreName(String areName) {
		this.areName = areName;
	}

	public String getAreIsenabled() {
		return this.areIsenabled;
	}

	public void setAreIsenabled(String areIsenabled) {
		this.areIsenabled = areIsenabled;
	}


	public Set getCusCorCuses() {
		return this.cusCorCuses;
	}

	public void setCusCorCuses(Set cusCorCuses) {
		this.cusCorCuses = cusCorCuses;
	}
	public Set getCusProvinces() {
		return this.cusProvinces;
	}

	public void setCusProvinces(Set cusProvinces) {
		this.cusProvinces = cusProvinces;
	}

	public String getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(String enabledType) {
		this.enabledType = enabledType;
	}

}