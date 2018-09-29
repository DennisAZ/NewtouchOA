package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 人员管辖区域实体类对象
 * 
 * @author shenrm
 *
 */
public class CrmManagementArea implements java.io.Serializable{

	// Fields
	private Long Id; //主键标识
	
	private String person_id; //所属人员Id
	
	private String person; //所属人员名称
	
	private String management_area; //管辖区域名称
	
	private String remark; //备注
	

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String personId) {
		person_id = personId;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getManagement_area() {
		return management_area;
	}

	public void setManagement_area(String managementArea) {
		management_area = managementArea;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
