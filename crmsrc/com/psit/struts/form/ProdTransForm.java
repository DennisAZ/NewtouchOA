package com.psit.struts.form;

/**
 * 加载运费标准的form <br>
 */
public class ProdTransForm implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String ptrUnit;//计价单位
	private Long ptrProdId;//产品ID
	private Double ptrAmt;//运费金额
	
	public ProdTransForm(String ptrUnit,Long ptrProdId, Double ptrAmt){
		this.ptrUnit = ptrUnit;
		this.ptrProdId = ptrProdId;
		this.ptrAmt = ptrAmt;
	}
	
	public String getPtrUnit() {
		return ptrUnit;
	}
	public void setPtrUnit(String ptrUnit) {
		this.ptrUnit = ptrUnit;
	}
	public Long getPtrProdId() {
		return ptrProdId;
	}
	public void setPtrProdId(Long ptrProdId) {
		this.ptrProdId = ptrProdId;
	}
	public Double getPtrAmt() {
		return ptrAmt;
	}
	public void setPtrAmt(Double ptrAmt) {
		this.ptrAmt = ptrAmt;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
