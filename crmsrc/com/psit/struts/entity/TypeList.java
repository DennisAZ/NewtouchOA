package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 类别实体 <br>
 * create_date: Aug 27, 2010,3:27:43 PM<br>
 * @author csg
 */

public class TypeList implements java.io.Serializable {

	// Fields

	private Long typId;
	private String typName;
	private String typDesc;
	private String typType;
	private String typIsenabled;
	private Set wmsProducts = new HashSet(0);
	private String enabledType = "";
	public static String EXP_TYPE = "express";
	// Constructors

	public String getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(String enabledType) {
		this.enabledType = enabledType;
	}

	/** default constructor */
	public TypeList() {
	}
    public TypeList(Long typId)
    {
    	this.typId=typId;
    }
	/** full constructor */
	public TypeList(String typName, String typDesc, String typType,
			String typIsenabled, Set wmsProducts) {
		this.typName = typName;
		this.typDesc = typDesc;
		this.typType = typType;
		this.typIsenabled = typIsenabled;
		this.wmsProducts = wmsProducts;
	}

	// Property accessors

	public Long getTypId() {
		return this.typId;
	}

	public void setTypId(Long typId) {
		this.typId = typId;
	}

	public String getTypName() {
		return this.typName;
	}

	public void setTypName(String typName) {
		this.typName = typName;
	}

	public String getTypDesc() {
		return this.typDesc;
	}

	public void setTypDesc(String typDesc) {
		this.typDesc = typDesc;
	}

	public String getTypType() {
		return this.typType;
	}

	public void setTypType(String typType) {
		this.typType = typType;
	}

	public String getTypIsenabled() {
		return this.typIsenabled;
	}

	public void setTypIsenabled(String typIsenabled) {
		this.typIsenabled = typIsenabled;
	}

	public Set getWmsProducts() {
		return this.wmsProducts;
	}

	public void setWmsProducts(Set wmsProducts) {
		this.wmsProducts = wmsProducts;
	}

}