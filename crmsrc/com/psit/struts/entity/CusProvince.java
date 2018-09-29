package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 省份实体 <br>
 *
 * create_date: Aug 27, 2010,3:22:08 PM<br>
 * @author zjr
 */

public class CusProvince implements java.io.Serializable {

	// Fields

	private Long prvId;
	private CusArea cusArea;
	private String prvName;
	private String prvIsenabled;
	private Set cusCities = new HashSet(0);
	private Set cusCorCuses = new HashSet(0);
	private Set salChans = new HashSet(0);
	private String enabledType=" ";
	// Constructors

	/** default constructor */
	public CusProvince() {
	}

	/** minimal constructor */
	public CusProvince(Long prvId) {
		this.prvId = prvId;
	}

	/** full constructor */
	public CusProvince(Long prvId, CusArea cusArea, String prvName,
			String prvIsenabled, Set cusCities, Set cusCorCuses) {
		this.prvId = prvId;
		this.cusArea = cusArea;
		this.prvName = prvName;
		this.prvIsenabled = prvIsenabled;
		this.cusCities = cusCities;
		this.cusCorCuses = cusCorCuses;
	}

	// Property accessors

	public Long getPrvId() {
		return this.prvId;
	}

	public void setPrvId(Long prvId) {
		this.prvId = prvId;
	}

	public CusArea getCusArea() {
		return this.cusArea;
	}

	public void setCusArea(CusArea cusArea) {
		this.cusArea = cusArea;
	}

	public String getPrvName() {
		return this.prvName;
	}

	public void setPrvName(String prvName) {
		this.prvName = prvName;
	}

	public String getPrvIsenabled() {
		return this.prvIsenabled;
	}

	public void setPrvIsenabled(String prvIsenabled) {
		this.prvIsenabled = prvIsenabled;
	}
	public Set getCusCities() {
		return this.cusCities;
	}

	public void setCusCities(Set cusCities) {
		this.cusCities = cusCities;
	}

	public Set getCusCorCuses() {
		return this.cusCorCuses;
	}

	public void setCusCorCuses(Set cusCorCuses) {
		this.cusCorCuses = cusCorCuses;
	}

	public String getEnabledType() {
		return enabledType;
	}

	public void setEnabledType(String enabledType) {
		this.enabledType = enabledType;
	}

}