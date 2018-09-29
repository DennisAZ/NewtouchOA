package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * 商品库存实体类
 */

public class ProdStore  implements java.io.Serializable {


    // Fields    

     private Long pstId;
     private WmsProduct wmsProduct;
     private TypeList storeType;
     private Double pstCount;
     private String pstCreMan;
     private Date pstCreTime;
     private String pstUpdMan;
     private Date pstUpdTime;
     private String pstName;


    // Constructors

    /** default constructor */
    public ProdStore() {
    }

    public ProdStore(Long pstId){
    	this.pstId = pstId;
    }
    
    /** full constructor */
	public ProdStore(WmsProduct wmsProduct, TypeList storeType,
			Double pstCount, String pstCreMan, Date pstCreTime,
			String pstUpdMan, Date pstUpdTime, String pstName) {
		this.wmsProduct = wmsProduct;
		this.storeType = storeType;
		this.pstCount = pstCount;
		this.pstCreMan = pstCreMan;
		this.pstCreTime = pstCreTime;
		this.pstUpdMan = pstUpdMan;
		this.pstUpdTime = pstUpdTime;
		this.pstName = pstName;
	}

   
    // Property accessors

    public String getPstName() {
		return pstName;
	}

	public void setPstName(String pstName) {
		this.pstName = pstName;
	}

	public Long getPstId() {
        return this.pstId;
    }
    
    public void setPstId(Long pstId) {
        this.pstId = pstId;
    }


    public WmsProduct getWmsProduct() {
		return wmsProduct;
	}


	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}


	public TypeList getStoreType() {
		return storeType;
	}


	public void setStoreType(TypeList storeType) {
		this.storeType = storeType;
	}


	public Double getPstCount() {
        return this.pstCount;
    }
    
    public void setPstCount(Double pstCount) {
        this.pstCount = pstCount;
    }

    public String getPstCreMan() {
        return this.pstCreMan;
    }
    
    public void setPstCreMan(String pstCreMan) {
        this.pstCreMan = pstCreMan;
    }

    public Date getPstCreTime() {
        return this.pstCreTime;
    }
    
    public void setPstCreTime(Date pstCreTime) {
        this.pstCreTime = pstCreTime;
    }

    public String getPstUpdMan() {
        return this.pstUpdMan;
    }
    
    public void setPstUpdMan(String pstUpdMan) {
        this.pstUpdMan = pstUpdMan;
    }

    public Date getPstUpdTime() {
        return this.pstUpdTime;
    }
    
    public void setPstUpdTime(Date pstUpdTime) {
        this.pstUpdTime = pstUpdTime;
    }
}