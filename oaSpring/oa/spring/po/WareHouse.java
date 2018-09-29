package oa.spring.po;

import java.io.Serializable;
import java.util.UUID;

public class WareHouse implements Serializable{

	private static final long serialVersionUID = -2893093219019829315L;
	
	private String id;
	private String name;
	private String address;
	private String type;
	private String remark;
	public WareHouse(){}
	public WareHouse(String name,String address,String type,String remark){
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.address = address;
		this.type = type;
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
