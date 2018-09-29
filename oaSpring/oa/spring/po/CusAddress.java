package oa.spring.po;

public class CusAddress {
private String id;
private SaleOrder saleOrder;
private String person;
private String address;
private String phone;
private String zipCode;
private String remark;
public CusAddress(){}
public CusAddress(String person,String address,String phone,String zipCode,String remark){
	this.person=person;
	this.address=address;
	this.phone=phone;
	this.zipCode=zipCode;
	this.remark=remark;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public SaleOrder getSaleOrder() {
	return saleOrder;
}
public void setSaleOrder(SaleOrder saleOrder) {
	this.saleOrder = saleOrder;
}
public String getPerson() {
	return person;
}
public void setPerson(String person) {
	this.person = person;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getZipCode() {
	return zipCode;
}
public void setZipCode(String zipCode) {
	this.zipCode = zipCode;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}

}
