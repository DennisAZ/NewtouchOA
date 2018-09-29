package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * SupPaidPast entity. @author MyEclipse Persistence Tools
 */

public class SupPaidPast  implements java.io.Serializable {


    // Fields    

     private Long sppId;
 	 private PurOrd purOrd;
     private Supplier supplier;
     private String sppContent;
     private Date sppFctDate;
     private String sppPayType;
     private Double sppPayMon;
     private SalEmp salEmp;
     private String sppIsinv;
     private String sppCreUser;
     private Date sppCreDate;
     private String sppUpdUser;
     private Date sppUpdDate;


    // Constructors

    /** default constructor */
    public SupPaidPast() {
    }

    public SupPaidPast(Long sppId){
    	this.sppId = sppId;
    }
    
    /** full constructor */
    public SupPaidPast(PurOrd purOrd, Supplier supplier, String sppContent, Date sppFctDate, String sppPayType, Double sppPayMon, SalEmp salEmp, String sppIsinv, String sppCreUser, Date sppCreDate, String sppUpdUser, Date sppUpdDate) {
        this.purOrd = purOrd;
        this.supplier = supplier;
        this.sppContent = sppContent;
        this.sppFctDate = sppFctDate;
        this.sppPayType = sppPayType;
        this.sppPayMon = sppPayMon;
        this.salEmp = salEmp;
        this.sppIsinv = sppIsinv;
        this.sppCreUser = sppCreUser;
        this.sppCreDate = sppCreDate;
        this.sppUpdUser = sppUpdUser;
        this.sppUpdDate = sppUpdDate;
    }

   
    // Property accessors

    public Long getSppId() {
        return this.sppId;
    }
    
    public void setSppId(Long sppId) {
        this.sppId = sppId;
    }


	public PurOrd getPurOrd() {
		return purOrd;
	}

	public void setPurOrd(PurOrd purOrd) {
		this.purOrd = purOrd;
	}

	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public String getSppContent() {
        return this.sppContent;
    }
    
    public void setSppContent(String sppContent) {
        this.sppContent = sppContent;
    }

    public Date getSppFctDate() {
        return this.sppFctDate;
    }
    
    public void setSppFctDate(Date sppFctDate) {
        this.sppFctDate = sppFctDate;
    }

    public String getSppPayType() {
        return this.sppPayType;
    }
    
    public void setSppPayType(String sppPayType) {
        this.sppPayType = sppPayType;
    }

    public Double getSppPayMon() {
        return this.sppPayMon;
    }
    
    public void setSppPayMon(Double sppPayMon) {
        this.sppPayMon = sppPayMon;
    }

    public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public String getSppIsinv() {
        return this.sppIsinv;
    }
    
    public void setSppIsinv(String sppIsinv) {
        this.sppIsinv = sppIsinv;
    }

    public String getSppCreUser() {
        return this.sppCreUser;
    }
    
    public void setSppCreUser(String sppCreUser) {
        this.sppCreUser = sppCreUser;
    }

    public Date getSppCreDate() {
        return this.sppCreDate;
    }
    
    public void setSppCreDate(Date sppCreDate) {
        this.sppCreDate = sppCreDate;
    }

    public String getSppUpdUser() {
        return this.sppUpdUser;
    }
    
    public void setSppUpdUser(String sppUpdUser) {
        this.sppUpdUser = sppUpdUser;
    }

    public Date getSppUpdDate() {
        return this.sppUpdDate;
    }
    
    public void setSppUpdDate(Date sppUpdDate) {
        this.sppUpdDate = sppUpdDate;
    }
   








}