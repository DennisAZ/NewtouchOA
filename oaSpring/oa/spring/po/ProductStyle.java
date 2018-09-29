package oa.spring.po;

public class ProductStyle {
private String id;
private String name;//所属大类名称
private String remark;//备注
public ProductStyle(){}
public ProductStyle(String id,String name,String remark){
	this.id = id;
	this.name = name;
	this.remark = remark;
}
public ProductStyle(String name,String remark){
	this.name = name;
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
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}

}
