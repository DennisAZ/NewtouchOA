package oa.spring.po;

public class ProductUnit {
	private String unitId;
	private String unitName;//计量单位名称
	public ProductUnit(){}
	public ProductUnit(String unitId,String unitName){
		this.unitId=unitId;
		this.unitName=unitName;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
}
