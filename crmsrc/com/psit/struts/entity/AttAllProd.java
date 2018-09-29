package com.psit.struts.entity;

/**
 * 产品附件实体 <br>
 * @author wyb
 */
public class AttAllProd extends Attachment implements java.io.Serializable{
	private WmsProduct wmsProduct;
	
	public AttAllProd(){}

	public AttAllProd(WmsProduct wmsProduct){
		this.wmsProduct = wmsProduct;
	}
	public WmsProduct getWmsProduct() {
		return wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}
}
