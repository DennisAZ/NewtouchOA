package com.psit.struts.entity;

/**
 * 
 * 订单明细实体 <br>
 *
 * create_date: Aug 27, 2010,3:30:30 PM<br>
 * @author zjr
 */

public class ROrdPro implements java.io.Serializable {

	// Fields
	private Long ropId;
	private WmsProduct wmsProduct;
	private SalOrdCon salOrdCon;
	private Double ropNum;
	private Double ropRealPrice;
	private String ropRemark;
	private Double ropPrice;
	private String ropZk;
	private Double ropShipNum;
	private Double ropReturnNum;
	private Double ropOutNum;
	private Double ropRealNum;

	// Constructors

	/** default constructor */
	public ROrdPro() {
	}

	/** minimal constructor */
	public ROrdPro(Long ropId) {
		this.ropId = ropId;
	}

	/** full constructor */
	public ROrdPro(Long ropId, WmsProduct wmsProduct, SalOrdCon salOrdCon,
			Double ropNum, Double ropRealPrice,
			String ropRemark, Double rop1, String rop2, Double rop3,
			Double rop4) {
		this.ropId = ropId;
		this.wmsProduct = wmsProduct;
		this.salOrdCon = salOrdCon;
		this.ropNum = ropNum;
		this.ropRealPrice = ropRealPrice;
		this.ropRemark = ropRemark;
		this.ropPrice = rop1;
		this.ropZk = rop2;
		this.ropOutNum = rop3;
		this.ropRealNum = rop4;
	}

	// Property accessors

	public Long getRopId() {
		return this.ropId;
	}

	public void setRopId(Long ropId) {
		this.ropId = ropId;
	}

	public WmsProduct getWmsProduct() {
		return this.wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public Double getRopNum() {
		return ropNum;
	}

	public void setRopNum(Double ropNum) {
		this.ropNum = ropNum;
	}

	public Double getRopRealPrice() {
		return this.ropRealPrice;
	}

	public void setRopRealPrice(Double ropRealPrice) {
		this.ropRealPrice = ropRealPrice;
	}

	public String getRopRemark() {
		return this.ropRemark;
	}

	public void setRopRemark(String ropRemark) {
		this.ropRemark = ropRemark;
	}


	public String getRopZk() {
		return ropZk;
	}

	public void setRopZk(String ropZk) {
		this.ropZk = ropZk;
	}

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}

	public Double getRopPrice() {
		return ropPrice;
	}

	public void setRopPrice(Double ropPrice) {
		this.ropPrice = ropPrice;
	}

	public Double getRopOutNum() {
		return ropOutNum;
	}

	public void setRopOutNum(Double ropOutNum) {
		this.ropOutNum = ropOutNum;
	}

	public Double getRopRealNum() {
		return ropRealNum;
	}

	public void setRopRealNum(Double ropRealNum) {
		this.ropRealNum = ropRealNum;
	}

	public Double getRopShipNum() {
		return ropShipNum;
	}

	public void setRopShipNum(Double ropShipNum) {
		this.ropShipNum = ropShipNum;
	}

	public Double getRopReturnNum() {
		return ropReturnNum;
	}

	public void setRopReturnNum(Double ropReturnNum) {
		this.ropReturnNum = ropReturnNum;
	}

}