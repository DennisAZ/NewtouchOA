package com.psit.struts.form;

/**
 * 订单明细发货数list实体 <br>
 */
public class RopShipForm {
	private Long prodId;
	private Double shipNum;
	
	public RopShipForm(Long prodId,Double shipNum){
		this.prodId = prodId;
		this.shipNum = shipNum;
	}
	
	public Long getProdId() {
		return prodId;
	}
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}
	public Double getShipNum() {
		return shipNum;
	}
	public void setShipNum(Double shipNum) {
		this.shipNum = shipNum;
	}

}
