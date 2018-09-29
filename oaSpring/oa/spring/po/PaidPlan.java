package oa.spring.po;

public class PaidPlan {
	private String id;
	private String paidCode;// 回款单编号
	private String paidTitle;// 回款单名称
	private SaleOrder saleOrder;//销售订单关联对象
	private String paidStatus;//回款单状态
	private Double total;//应收总金额
	private Double alreadyTotal;//应收金额
	private Double salePaid;//支出金额
	private String remark;

	public PaidPlan() {
	}

	public PaidPlan(String paidCode,String paidTitle,SaleOrder saleOrder, String paidStatus,
			Double total, Double alreadyTotal, Double salePaid, String remark) {
		this.paidCode=paidCode;
		this.saleOrder=saleOrder;
		this.paidStatus=paidStatus;
		this.remark=remark;
		this.alreadyTotal=alreadyTotal;
		this.total=total;
		this.salePaid=salePaid;
		this.paidTitle=paidTitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaidCode() {
		return paidCode;
	}

	public void setPaidCode(String paidCode) {
		this.paidCode = paidCode;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getAlreadyTotal() {
		return alreadyTotal;
	}

	public void setAlreadyTotal(Double alreadyTotal) {
		this.alreadyTotal = alreadyTotal;
	}

	public Double getSalePaid() {
		return salePaid;
	}

	public void setSalePaid(Double salePaid) {
		this.salePaid = salePaid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaidTitle() {
		return paidTitle;
	}

	public void setPaidTitle(String paidTitle) {
		this.paidTitle = paidTitle;
	}


}
