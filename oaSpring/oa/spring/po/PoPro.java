package oa.spring.po;

public class PoPro {
	private String id;
	private OrderProductOut opo;//出货单关联对象
	private Product product;//产品关联对象
	private double orderNum;//订单数量
	private double alreadySendNum;//已经发货数量
	private Double salePrice;//销售单价
	private Double total;//销售合计
	private String sendDate;//发货时间
	private String status;//状态
	private String remark;//备注

	public PoPro() {
	}		


	public PoPro(OrderProductOut opo, Product product, double orderNum,
			double alreadySendNum, String status,Double salePrice, Double total,
			String sendDate, String remark) {
		this.opo = opo;
		this.product = product;
		this.orderNum = orderNum;
		this.alreadySendNum =alreadySendNum;
		this.status=status;
		this.salePrice = salePrice;
		this.total = total;
		this.sendDate = sendDate;
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderProductOut getOpo() {
		return opo;
	}

	public void setOpo(OrderProductOut opo) {
		this.opo = opo;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}









	public double getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(double orderNum) {
		this.orderNum = orderNum;
	}


	public double getAlreadySendNum() {
		return alreadySendNum;
	}


	public void setAlreadySendNum(double alreadySendNum) {
		this.alreadySendNum = alreadySendNum;
	}


	public Double getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}




	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
