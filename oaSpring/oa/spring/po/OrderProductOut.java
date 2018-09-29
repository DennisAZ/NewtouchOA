package oa.spring.po;

public class OrderProductOut {
private String id;
private String poCode;//出货单编号
private String poTitle;//出货单标题
private SaleOrder saleOrder;//销售订单对象
private String poStatus;//出货单状态
private String remark;//备注
public  OrderProductOut(){}
public  OrderProductOut(String id,String poCode,String poTitle,SaleOrder saleOrder,String poStatus,String remark){
	this.id=id;
	this.poCode=poCode;
	this.poTitle=poTitle;
	this.saleOrder=saleOrder;
	this.poStatus=poStatus;
	this.remark=remark;
	
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getPoCode() {
	return poCode;
}
public void setPoCode(String poCode) {
	this.poCode = poCode;
}
public String getPoTitle() {
	return poTitle;
}
public void setPoTitle(String poTitle) {
	this.poTitle = poTitle;
}
public SaleOrder getSaleOrder() {
	return saleOrder;
}
public void setSaleOrder(SaleOrder saleOrder) {
	this.saleOrder = saleOrder;
}
public String getPoStatus() {
	return poStatus;
}
public void setPoStatus(String poStatus) {
	this.poStatus = poStatus;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}

}
