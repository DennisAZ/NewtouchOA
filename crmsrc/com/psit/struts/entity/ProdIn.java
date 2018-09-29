package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * 入库记录实体类
 */

public class ProdIn  implements java.io.Serializable {


    // Fields    

     private Long pinId;
     private String pinCode;
     private PurOrd purOrd;
     private ProdStore prodStore;
     private String pinRespMan;
     private Date pinDate;
     private String pinState;
     private String pinRemark;
     private String pinCreMan;
     private String pinUpdMan;
     private Date pinCreTime;
     private Date pinUpdTime;
     private Double pinInNum;

    // Constructors

    /** default constructor */
    public ProdIn() {
    }

    public ProdIn(Long pinId){
    	this.pinId = pinId;
    }
    
    /** full constructor */
    public ProdIn(String pinCode, PurOrd purOrd, ProdStore	prodStore, String pinRespMan, Date pinDate, String pinState, String pinRemark,
    			String pinCreMan, String pinUpdMan, Date pinCreTime, Date pinUpdTime,Double pinInNum) {
        this.pinCode = pinCode;
        this.purOrd = purOrd;
        this.prodStore = prodStore;
        this.pinRespMan = pinRespMan;
        this.pinDate = pinDate;
        this.pinState = pinState;
        this.pinRemark = pinRemark;
        this.pinCreMan = pinCreMan;
        this.pinUpdMan = pinUpdMan;
        this.pinCreTime = pinCreTime;
        this.pinUpdTime = pinUpdTime;
        this.pinInNum = pinInNum;
    }

   
    // Property accessors

    public Double getPinInNum() {
		return pinInNum;
	}

	public void setPinInNum(Double pinInNum) {
		this.pinInNum = pinInNum;
	}

	public Long getPinId() {
        return this.pinId;
    }
    
    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public String getPinCode() {
        return this.pinCode;
    }
    
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }



    public PurOrd getPurOrd() {
		return purOrd;
	}


	public void setPurOrd(PurOrd purOrd) {
		this.purOrd = purOrd;
	}


	public ProdStore getProdStore() {
		return prodStore;
	}


	public void setProdStore(ProdStore prodStore) {
		this.prodStore = prodStore;
	}


	public String getPinRespMan() {
        return this.pinRespMan;
    }
    
    public void setPinRespMan(String pinRespMan) {
        this.pinRespMan = pinRespMan;
    }

    public Date getPinDate() {
        return this.pinDate;
    }
    
    public void setPinDate(Date pinDate) {
        this.pinDate = pinDate;
    }

    public String getPinState() {
        return this.pinState;
    }
    
    public void setPinState(String pinState) {
        this.pinState = pinState;
    }

    public String getPinRemark() {
        return this.pinRemark;
    }
    
    public void setPinRemark(String pinRemark) {
        this.pinRemark = pinRemark;
    }

    public String getPinCreMan() {
        return this.pinCreMan;
    }
    
    public void setPinCreMan(String pinCreMan) {
        this.pinCreMan = pinCreMan;
    }

    public String getPinUpdMan() {
        return this.pinUpdMan;
    }
    
    public void setPinUpdMan(String pinUpdMan) {
        this.pinUpdMan = pinUpdMan;
    }

    public Date getPinCreTime() {
        return this.pinCreTime;
    }
    
    public void setPinCreTime(Date pinCreTime) {
        this.pinCreTime = pinCreTime;
    }

    public Date getPinUpdTime() {
        return this.pinUpdTime;
    }
    
    public void setPinUpdTime(Date pinUpdTime) {
        this.pinUpdTime = pinUpdTime;
    }
}