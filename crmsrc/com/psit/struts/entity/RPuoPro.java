package com.psit.struts.entity;

/**
 * 采购明细实体 <br>
 * @author wangyb
 */
public class RPuoPro  implements java.io.Serializable {

     private Long rppId;
     private PurOrd purOrd;
     private WmsProduct wmsProduct;
     private Double rppNum;
     private Double rppPrice;
     private Double rppSumMon;
     private String rppRemark;
     private Double rppOutNum;
     private Double rppRealNum;


    // Constructors

    /** default constructor */
    public RPuoPro() {
    }

    public RPuoPro(Long rppId){
    	this.rppId = rppId;
    }
    
    /** full constructor */
    public RPuoPro(Long rppId, PurOrd purOrd,WmsProduct wmsProduct, Double rppNum, Double rppPrice, Double rppSumMon,
    		  String rppRemark, Double rppOutNum, Double rppRealNum) {
    	this.rppId = rppId;
        this.purOrd = purOrd;
        this.wmsProduct = wmsProduct;
        this.rppNum = rppNum;
        this.rppPrice = rppPrice;
        this.rppSumMon = rppSumMon;
        this.rppRemark = rppRemark;
        this.rppOutNum = rppOutNum;
        this.rppRealNum = rppRealNum;
    }

   
    // Property accessors

    public Long getRppId() {
        return this.rppId;
    }
    
    public void setRppId(Long rppId) {
        this.rppId = rppId;
    }

    

    public PurOrd getPurOrd() {
		return purOrd;
	}


	public void setPurOrd(PurOrd purOrd) {
		this.purOrd = purOrd;
	}


	public WmsProduct getWmsProduct() {
		return wmsProduct;
	}


	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}


	public Double getRppNum() {
        return this.rppNum;
    }
    
    public void setRppNum(Double rppNum) {
        this.rppNum = rppNum;
    }

    public Double getRppPrice() {
        return this.rppPrice;
    }
    
    public void setRppPrice(Double rppPrice) {
        this.rppPrice = rppPrice;
    }

    public Double getRppSumMon() {
        return this.rppSumMon;
    }
    
    public void setRppSumMon(Double rppSumMon) {
        this.rppSumMon = rppSumMon;
    }

    public String getRppRemark() {
        return this.rppRemark;
    }
    
    public void setRppRemark(String rppRemark) {
        this.rppRemark = rppRemark;
    }

    public Double getRppOutNum() {
        return this.rppOutNum;
    }
    
    public void setRppOutNum(Double rppOutNum) {
        this.rppOutNum = rppOutNum;
    }

    public Double getRppRealNum() {
        return this.rppRealNum;
    }
    
    public void setRppRealNum(Double rppRealNum) {
        this.rppRealNum = rppRealNum;
    }

}