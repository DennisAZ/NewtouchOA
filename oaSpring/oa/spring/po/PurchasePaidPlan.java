package oa.spring.po;

/**
 * 采购出款单
 * 
 * @author Administrator
 * 
 */
public class PurchasePaidPlan {
	private String id;
	private String code;// 编号
	private String title;// 标题
	private String status;// 状态
	private Double total;// 应付金额
	private Double alreadyTotal;// 已经付金额
	private String remark;// 备注
	private Purchase purchase;// 订单关联对象

	public PurchasePaidPlan() {
	}

	public PurchasePaidPlan(String code, String title, Purchase purchase,
			String status, Double total, Double alreadyTotal, String remark) {
		this.code=code;
		this.title=title;
		this.purchase=purchase;
		this.status=status;
		this.total=total;
		this.alreadyTotal=alreadyTotal;
		this.remark=remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

}
