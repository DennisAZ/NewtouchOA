package oa.spring.po;

public class CusContact {
	private String id;
	private SaleOrder saleOrder;
	private String name;
	private String sex;
	private String phone;
	private String mobile;
	private String email;
	private String remark;

	public CusContact() {
	}

	public CusContact(SaleOrder saleOrder, String name, String sex,
			String phone, String mobile,String email,String remark) {
		this.saleOrder=saleOrder;
		this.name=name;
		this.sex=sex;
		this.phone=phone;
		this.mobile=mobile;
		this.email=email;
		this.remark=remark;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
