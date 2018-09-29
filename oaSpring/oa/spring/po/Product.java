package oa.spring.po;

public class Product {
	private String id;
	private String proCode;//产品编号
	private String proName;//产品名字
	private Double proPrice;//单价
	private String proType;//规格型号
	private String remark;//备注
	private ProductUnit pUnit;//计量单位对象
	private ProductStyle pStyle;//所属大类对象
	private ProductType pType;//产品类别对象
	private String shortName;
	public Product() {
	}


	public Product(String proCode, String proName, String proType,
			Double proPrice, ProductUnit pUnit,ProductType pType,ProductStyle pStyle,String remark,String shortName) {
		this.proCode=proCode;
		this.proName=proName;
		this.proPrice=proPrice;
		this.pStyle=pStyle;
		this.pType=pType;
		this.remark=remark;
		this.pUnit=pUnit;
		this.proType=proType;
		this.shortName=shortName;
	}

	public String getShortName() {
		return shortName;
	}


	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}


	

	public Double getProPrice() {
		return proPrice;
	}


	public void setProPrice(Double proPrice) {
		this.proPrice = proPrice;
	}


	

	public String getProType() {
		return proType;
	}


	public void setProType(String proType) {
		this.proType = proType;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public ProductUnit getpUnit() {
		return pUnit;
	}


	public void setpUnit(ProductUnit pUnit) {
		this.pUnit = pUnit;
	}


	public ProductStyle getpStyle() {
		return pStyle;
	}


	public void setpStyle(ProductStyle pStyle) {
		this.pStyle = pStyle;
	}


	public ProductType getpType() {
		return pType;
	}


	public void setpType(ProductType pType) {
		this.pType = pType;
	}
	

}
