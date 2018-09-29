package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 城市实体 <br>
 *
 * create_date: Aug 27, 2010,3:21:16 PM<br>
 * @author zjr
 */

public class CusCity implements java.io.Serializable {

	// Fields

	private Long cityId;
	private CusProvince cusProvince;
	private String cityName;
	private String cityIsenabled;
	private Set cusCorCuses = new HashSet(0);
	private String enabledType=" ";
	// Constructors

	/** default constructor */
	public CusCity() {
	}

	/** minimal constructor */
	public CusCity(Long cityId) {
		this.cityId = cityId;
	}

	/** full constructor */
	public CusCity(Long cityId, CusProvince cusProvince, String cityName,
			String cityIsenabled,Set cusCorCuses) {
		this.cityId = cityId;
		this.cusProvince = cusProvince;
		this.cityName = cityName;
		this.cityIsenabled = cityIsenabled;
		this.cusCorCuses = cusCorCuses;
	}

	// Property accessors

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public CusProvince getCusProvince() {
		return this.cusProvince;
	}

	public void setCusProvince(CusProvince cusProvince) {
		this.cusProvince = cusProvince;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityIsenabled() {
		return this.cityIsenabled;
	}

	public void setCityIsenabled(String cityIsenabled) {
		this.cityIsenabled = cityIsenabled;
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