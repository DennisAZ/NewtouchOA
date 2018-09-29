package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * WhRec entity. @author MyEclipse Persistence Tools
 */

public class WhRec  implements java.io.Serializable {


    // Fields    

     private Long wreId;
     private ProdStore prodStore;
     private Double wreCount;
     private String wreType;
     private Date wreTime;
     private String wreMan;
     private Double wreLeftCount;


    // Constructors

    /** default constructor */
    public WhRec() {
    }

    public  WhRec(Long wreId){
    	this.wreId = wreId;
    }
    
    /** full constructor */
    public WhRec(ProdStore prodStore , Double wreCount, String wreType, Date wreTime, String wreMan, Double wreLeftCount) {
        this.prodStore = prodStore;
        this.wreCount = wreCount;
        this.wreType = wreType;
        this.wreTime = wreTime;
        this.wreMan = wreMan;
        this.wreLeftCount = wreLeftCount;
    }

   
    // Property accessors

    public Long getWreId() {
        return this.wreId;
    }
    
    public void setWreId(Long wreId) {
        this.wreId = wreId;
    }


    public ProdStore getProdStore() {
		return prodStore;
	}


	public void setProdStore(ProdStore prodStore) {
		this.prodStore = prodStore;
	}


	public Double getWreCount() {
        return this.wreCount;
    }
    
    public void setWreCount(Double wreCount) {
        this.wreCount = wreCount;
    }

    public String getWreType() {
        return this.wreType;
    }
    
    public void setWreType(String wreType) {
        this.wreType = wreType;
    }

    public Date getWreTime() {
        return this.wreTime;
    }
    
    public void setWreTime(Date wreTime) {
        this.wreTime = wreTime;
    }

    public String getWreMan() {
        return this.wreMan;
    }
    
    public void setWreMan(String wreMan) {
        this.wreMan = wreMan;
    }

    public Double getWreLeftCount() {
        return this.wreLeftCount;
    }
    
    public void setWreLeftCount(Double wreLeftCount) {
        this.wreLeftCount = wreLeftCount;
    }
   
}