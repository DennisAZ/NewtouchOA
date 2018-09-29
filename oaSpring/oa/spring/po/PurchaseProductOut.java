package oa.spring.po;

/**
 * 采购货单
 * 
 * @author Administrator
 * 
 */
public class PurchaseProductOut {
	private String id;
	private String ppoCode;// 采购货单编号
	private String ppoTitle;// 采购货单主题
	private String ppoStatus;// 状态
	private String remark;
	private Purchase purchase;// 采购单对象

	public PurchaseProductOut() {
	}

	public PurchaseProductOut(String ppoCode, String ppoTitle,
			Purchase purchase, String ppoStatus, String remark) {
		this.ppoCode=ppoCode;
		this.ppoTitle=ppoTitle;
		this.purchase=purchase;
		this.ppoStatus=ppoStatus;
		this.remark=remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPpoCode() {
		return ppoCode;
	}

	public void setPpoCode(String ppoCode) {
		this.ppoCode = ppoCode;
	}

	public String getPpoTitle() {
		return ppoTitle;
	}

	public void setPpoTitle(String ppoTitle) {
		this.ppoTitle = ppoTitle;
	}

	public String getPpoStatus() {
		return ppoStatus;
	}

	public void setPpoStatus(String ppoStatus) {
		this.ppoStatus = ppoStatus;
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
