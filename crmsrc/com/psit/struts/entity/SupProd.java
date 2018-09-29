package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * SupProd entity. @author MyEclipse Persistence Tools
 */

public class SupProd  implements java.io.Serializable {


    // Fields    

     private Long spId;
     private Supplier supplier;
     private WmsProduct wmsProduct;
     private String spOtherName;
     private String spHasTax;
     private Double spPrice;
     private String spRemark;
     private String spCreUser;
     private Date spCreDate;
     private String spUpdUser;
     private Date spUpdDate;


    // Constructors

    /** default constructor */
    public SupProd() {
    }

    public SupProd(Long spId){
    	this.spId = spId;
    	
    }
    
    /** full constructor */
    public SupProd(Supplier supplier, WmsProduct wmsProduct, String spOtherName, String spHasTax, Double spPrice, String spRemark, String spCreUser, Date spCreDate, String spUpdUser, Date spUpdDate) {
        this.supplier = supplier;
        this.wmsProduct = wmsProduct;
        this.spOtherName = spOtherName;
        this.spHasTax = spHasTax;
        this.spPrice = spPrice;
        this.spRemark = spRemark;
        this.spCreUser = spCreUser;
        this.spCreDate = spCreDate;
        this.spUpdUser = spUpdUser;
        this.spUpdDate = spUpdDate;
    }

   
    // Property accessors

    public Long getSpId() {
        return this.spId;
    }
    
    public void setSpId(Long spId) {
        this.spId = spId;
    }

    public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public WmsProduct getWmsProduct() {
		return wmsProduct;
	}


	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}


	public String getSpOtherName() {
        return this.spOtherName;
    }
    
    public void setSpOtherName(String spOtherName) {
        this.spOtherName = spOtherName;
    }

    public String getSpHasTax() {
        return this.spHasTax;
    }
    
    public void setSpHasTax(String spHasTax) {
        this.spHasTax = spHasTax;
    }

    public Double getSpPrice() {
        return this.spPrice;
    }
    
    public void setSpPrice(Double spPrice) {
        this.spPrice = spPrice;
    }

    public String getSpRemark() {
        return this.spRemark;
    }
    
    public void setSpRemark(String spRemark) {
        this.spRemark = spRemark;
    }

    public String getSpCreUser() {
        return this.spCreUser;
    }
    
    public void setSpCreUser(String spCreUser) {
        this.spCreUser = spCreUser;
    }

    public Date getSpCreDate() {
        return this.spCreDate;
    }
    
    public void setSpCreDate(Date spCreDate) {
        this.spCreDate = spCreDate;
    }

    public String getSpUpdUser() {
        return this.spUpdUser;
    }
    
    public void setSpUpdUser(String spUpdUser) {
        this.spUpdUser = spUpdUser;
    }

    public Date getSpUpdDate() {
        return this.spUpdDate;
    }
    
    public void setSpUpdDate(Date spUpdDate) {
        this.spUpdDate = spUpdDate;
    }
   








}