package oa.spring.po;

public class ProductType {
private String id;
private String name;//产品类别名称
private String parentId;//上级节点
private String treeCode;//上级节点加当前节点
private String treeName;
private String parentName;
private String remark;
public ProductType(){}
public ProductType(String name,String parentId,String treeCode,String treeName,String remark,String parentName){
	this.name = name;
	this.parentId=parentId;
	this.treeCode=treeCode;
	this.treeName=treeName;
	this.remark = remark;
	this.parentName=parentName;
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
public String getParentId() {
	return parentId;
}
public void setParentId(String parentId) {
	this.parentId = parentId;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getTreeCode() {
	return treeCode;
}
public void setTreeCode(String treeCode) {
	this.treeCode = treeCode;
}
public String getTreeName() {
	return treeName;
}
public void setTreeName(String treeName) {
	this.treeName = treeName;
}
public String getParentName() {
	return parentName;
}
public void setParentName(String parentName) {
	this.parentName = parentName;
}



}
