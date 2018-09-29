package oa.spring.po;

import com.psit.struts.entity.Supplier;

/**
 * 采购申请单
 * 
 * @author Administrator
 * 
 */
public class PurchaseRequest {
	private String id;
	private String code;// 编号
	private String person;// 采购员
	private String date;// 时间
	private String status;// 状态
	private String remark;// 备注
	private Supplier supplier;// 关联供货商对象

	public PurchaseRequest() {
	}

	public PurchaseRequest(String code, String person, String date,
			String status, String remark, Supplier supplier) {
		this.code = code;
		this.person = person;
		this.date = date;
		this.status = status;
		this.remark = remark;
		this.supplier = supplier;
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

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

}
