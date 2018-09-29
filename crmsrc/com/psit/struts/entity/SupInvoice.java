package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * SupInvoice entity. @author MyEclipse Persistence Tools
 */

public class SupInvoice  implements java.io.Serializable {


    // Fields    

     private Long suiId;
     private PurOrd purOrd;
     private ERPPurchase erpPurchase;
     private Supplier supplier;
     private Double suiMon;
     private TypeList typeList;
     private YHPerson yhPerson;
     private Date suiDate;
     private String suiCode;
     private String suiRemark;
     private String suiCreUser;
     private Date suiCreDate;
     private String suiUpdUser;
     private Date suiUpdDate;


    // Constructors

    /** default constructor */
    public SupInvoice() {
    }

    public SupInvoice(Long suiId){
    	this.suiId = suiId;
    }
    
    /** full constructor */
    public SupInvoice(PurOrd purOrd, Supplier supplier, Double suiMon, TypeList typeList, YHPerson salEmp, Date suiDate, String suiCode, String suiRemark, String suiCreUser, Date suiCreDate, String suiUpdUser, Date suiUpdDate) {
        this.purOrd = purOrd;
        this.supplier = supplier;
        this.suiMon = suiMon;
        this.typeList = typeList;
        this.yhPerson = salEmp;
        this.suiDate = suiDate;
        this.suiCode = suiCode;
        this.suiRemark = suiRemark;
        this.suiCreUser = suiCreUser;
        this.suiCreDate = suiCreDate;
        this.suiUpdUser = suiUpdUser;
        this.suiUpdDate = suiUpdDate;
    }

   
    // Property accessors

    public Long getSuiId() {
        return this.suiId;
    }
    
    public void setSuiId(Long suiId) {
        this.suiId = suiId;
    }

    public Double getSuiMon() {
        return this.suiMon;
    }
    
    public void setSuiMon(Double suiMon) {
        this.suiMon = suiMon;
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


	public TypeList getTypeList() {
		return typeList;
	}


	public void setTypeList(TypeList typeList) {
		this.typeList = typeList;
	}





	public Date getSuiDate() {
        return this.suiDate;
    }
    
    public void setSuiDate(Date suiDate) {
        this.suiDate = suiDate;
    }


    public String getSuiCode() {
		return suiCode;
	}

	public void setSuiCode(String suiCode) {
		this.suiCode = suiCode;
	}

	public String getSuiRemark() {
        return this.suiRemark;
    }
    
    public void setSuiRemark(String suiRemark) {
        this.suiRemark = suiRemark;
    }

    public String getSuiCreUser() {
        return this.suiCreUser;
    }
    
    public void setSuiCreUser(String suiCreUser) {
        this.suiCreUser = suiCreUser;
    }

    public Date getSuiCreDate() {
        return this.suiCreDate;
    }
    
    public void setSuiCreDate(Date suiCreDate) {
        this.suiCreDate = suiCreDate;
    }

    public String getSuiUpdUser() {
        return this.suiUpdUser;
    }
    
    public void setSuiUpdUser(String suiUpdUser) {
        this.suiUpdUser = suiUpdUser;
    }

    public Date getSuiUpdDate() {
        return this.suiUpdDate;
    }
    
    public void setSuiUpdDate(Date suiUpdDate) {
        this.suiUpdDate = suiUpdDate;
    }

	public ERPPurchase getErpPurchase() {
		return erpPurchase;
	}

	public void setErpPurchase(ERPPurchase erpPurchase) {
		this.erpPurchase = erpPurchase;
	}

	public YHPerson getYhPerson() {
		return yhPerson;
	}

	public void setYhPerson(YHPerson yhPerson) {
		this.yhPerson = yhPerson;
	}
   








}