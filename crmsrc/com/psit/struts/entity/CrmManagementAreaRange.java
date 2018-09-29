package com.psit.struts.entity;

/**
 * @author shenrm
 * 
 * 管理区域范围实体类对象
 */
import java.io.Serializable;

public class CrmManagementAreaRange implements Serializable {


	private Long Id; //主键标识
	
	private Long ma_id; //关联人员管辖区域主键
	
	private Long country_id; //关联国家的主键
	
	private Long province_id; //关联省份主键
	
	private Long city_id; //关联城市主键
	
//	private CrmManagementArea crmManagementArea; //所属管辖区域
//	
//	public CrmManagementArea getCrmManagementArea() {
//		return crmManagementArea;
//	}
//
//	public void setCrmManagementArea(CrmManagementArea crmManagementArea) {
//		this.crmManagementArea = crmManagementArea;
//	}




	public Long getMa_id() {
		return ma_id;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public void setMa_id(Long maId) {
		ma_id = maId;
	}

	public Long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Long countryId) {
		country_id = countryId;
	}

	public Long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Long provinceId) {
		province_id = provinceId;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long cityId) {
		city_id = cityId;
	}
}
