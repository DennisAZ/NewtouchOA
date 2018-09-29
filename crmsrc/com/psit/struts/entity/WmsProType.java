package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 商品类别实体 <br>
 */

public class WmsProType implements java.io.Serializable {

	// Fields

	private Long wptId;
	private String wptName;
	private String wptDesc;
	private String wptIsenabled;
	private Set wmsProducts = new HashSet(0);
	private WmsProType wmsProType;
	private Set wmsProTypes = new HashSet(0);
	private String enabledType = "";
	// Constructors

	public String getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(String enabledType) {
		this.enabledType = enabledType;
	}

	/** default constructor */
	public WmsProType() {
	}

	/** minimal constructor */
	public WmsProType(Long wptId) {
		this.wptId = wptId;
	}

	/** full constructor */
	public WmsProType(Long wptId, String wptName, String wptDesc,
			String wptIsenabled,  Set wmsProducts,Set wmsProTypes) {
		this.wptId = wptId;
		this.wptName = wptName;
		this.wptDesc = wptDesc;
		this.wptIsenabled = wptIsenabled;
		this.wmsProducts = wmsProducts;
		this.wmsProTypes=wmsProTypes;
	}

	// Property accessors

	public Long getWptId() {
		return this.wptId;
	}

	public void setWptId(Long wptId) {
		this.wptId = wptId;
	}

	public String getWptName() {
		return this.wptName;
	}

	public void setWptName(String wptName) {
		this.wptName = wptName;
	}

	public String getWptDesc() {
		return this.wptDesc;
	}

	public void setWptDesc(String wptDesc) {
		this.wptDesc = wptDesc;
	}

	public String getWptIsenabled() {
		return this.wptIsenabled;
	}

	public void setWptIsenabled(String wptIsenabled) {
		this.wptIsenabled = wptIsenabled;
	}

	public Set getWmsProducts() {
		return this.wmsProducts;
	}

	public void setWmsProducts(Set wmsProducts) {
		this.wmsProducts = wmsProducts;
	}

	public WmsProType getWmsProType() {
		return wmsProType;
	}

	public void setWmsProType(WmsProType wmsProType) {
		this.wmsProType = wmsProType;
	}

	public Set getWmsProTypes() {
		return wmsProTypes;
	}

	public void setWmsProTypes(Set wmsProTypes) {
		this.wmsProTypes = wmsProTypes;
	}

}