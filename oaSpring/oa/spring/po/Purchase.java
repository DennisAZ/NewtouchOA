package oa.spring.po;

/**
 * 采购订单
 * 
 * @author Administrator
 * 
 */
public class Purchase {
	private String id;
	private String code;// 采购编号
	private String title;// 采购标题
	private String status;// 采购状态
	private String person;// 采购人
	private String signDate;// 采购日期
	private String contractId;// 合同
	private String remark;

	public Purchase() {
	}

	public Purchase(String id,String code, String title, String status, String person,
			String signDate, String contractId, String remark) {
		this.id=id;
		this.code=code;
		this.title=title;
		this.status=status;
		this.person=person;
		this.signDate=signDate;
		this.contractId=contractId;
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

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
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

}
