package oa.spring.po;

import com.psit.struts.entity.CusCorCus;

//销售订单表
public class SaleOrder {
	private String id;// 订单序列
	private String orderCode;// 订单编号
	private String orderTitle;// 订单标题
	private String orderStatus;// 订单状态
	// private String cusId;//客户id
	private String Salesperson;// 销售人员
	private String SignDate;// 签约日期
	private String contractId;// 合同id
	private String remark;// 备注
	private int personId;
	private CusCorCus cust;// 客户对象

	public SaleOrder() {
	}

	public SaleOrder(String id,String orderCode, String orderTitle, String orderStatus,
			String Salesperson, String SignDate, String contractId,
			String remark, CusCorCus cust,int personId) {
		this.id=id;
		this.orderCode = orderCode;
		this.orderTitle = orderTitle;
		this.orderStatus = orderStatus;
		this.Salesperson = Salesperson;
		this.SignDate = SignDate;
		this.contractId = contractId;
		this.remark = remark;
		this.cust = cust;
		this.personId=personId;

	}



	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSalesperson() {
		return Salesperson;
	}

	public void setSalesperson(String salesperson) {
		Salesperson = salesperson;
	}

	public String getSignDate() {
		return SignDate;
	}

	public void setSignDate(String signDate) {
		SignDate = signDate;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CusCorCus getCust() {
		return cust;
	}

	public void setCust(CusCorCus cust) {
		this.cust = cust;
	}

}
