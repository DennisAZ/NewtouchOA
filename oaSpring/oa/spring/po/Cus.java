package oa.spring.po;

public class Cus {
	private String id;
	private SaleOrder saleOrder;
	private String cusName;
	private String cusCode;
	private String cusCorPoration;
	private String cusInfo;
	private CusContact contact;
	private CusAddress address;

	public Cus() {
	}

	public Cus(SaleOrder saleOrder, String cusName, String cusCode,
			String cusCorPoration, String cusInfo, CusContact contact,
			CusAddress address) {
		this.saleOrder = saleOrder;
		this.cusName = cusName;
		this.cusCode = cusCode;
		this.cusCorPoration = cusCorPoration;
		this.cusInfo = cusInfo;
		this.contact = contact;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getCusCorPoration() {
		return cusCorPoration;
	}

	public void setCusCorPoration(String cusCorPoration) {
		this.cusCorPoration = cusCorPoration;
	}

	public String getCusInfo() {
		return cusInfo;
	}

	public void setCusInfo(String cusInfo) {
		this.cusInfo = cusInfo;
	}

	public CusContact getContact() {
		return contact;
	}

	public void setContact(CusContact contact) {
		this.contact = contact;
	}

	public CusAddress getAddress() {
		return address;
	}

	public void setAddress(CusAddress address) {
		this.address = address;
	}

}
