package oa.spring.po;

/**
 * 采购产品关联
 * 
 * @author Administrator
 * 
 */
public class PpoPro {
	private String id;
	private int purchaseNum;// 采购数量
	private int alreadyPurchaseNum;// 已经采购数量
	private String status;// 状态
	private Double purchasePrice;// 采购单价
	private Double total;// 采购总额
	private String date;// 日期
	private String remark;// 备注
	private PurchaseProductOut purchaseProductOut;// 采购出货单关联对象
	private Product product;// 产品关联对象
	private PurchaseRequest purchaseRequest;// 采购申请单关联对象

	public PpoPro() {
	}

	public PpoPro(PurchaseProductOut purchaseProductOut, Product product,
			PurchaseRequest purchaseRequest, int purchaseNum,
			int alreadyPurchaseNum, String status, Double purchasePrice,
			Double total,String date,String remark) {
		this.purchaseProductOut=purchaseProductOut;
		this.product=product;
		this.purchaseRequest=purchaseRequest;
		this.purchaseNum=purchaseNum;
		this.alreadyPurchaseNum=alreadyPurchaseNum;
		this.status=status;
		this.purchasePrice=purchasePrice;
		this.total=total;
		this.date=date;
		this.remark=remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(int purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public int getAlreadyPurchaseNum() {
		return alreadyPurchaseNum;
	}

	public void setAlreadyPurchaseNum(int alreadyPurchaseNum) {
		this.alreadyPurchaseNum = alreadyPurchaseNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
