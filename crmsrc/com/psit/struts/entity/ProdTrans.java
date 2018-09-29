package com.psit.struts.entity;



public class ProdTrans  implements java.io.Serializable {


    // Fields    

     private Long ptrId;
     private TypeList ptrExpTypeList;
     private WmsProduct ptrProduct;
     private CusArea ptrProv;
     private CusProvince ptrCity;
     private CusCity ptrDistrict;
     private String ptrUnit;
     private Double ptrAmt;


    // Constructors

    /** default constructor */
    public ProdTrans() {
    }

    
    /** full constructor */
    public ProdTrans(WmsProduct ptrProduct, TypeList ptrExpTypeList, CusArea ptrProv, CusProvince ptrCity, CusCity ptrDistrict, String ptrUnit, Double ptrAmt) {
        this.ptrProduct = ptrProduct;
        this.ptrExpTypeList = ptrExpTypeList;
        this.ptrProv = ptrProv;
        this.ptrCity = ptrCity;
        this.ptrDistrict = ptrDistrict;
        this.ptrUnit = ptrUnit;
        this.ptrAmt = ptrAmt;
    }

   
    // Property accessors

    public Long getPtrId() {
        return this.ptrId;
    }
    
    public void setPtrId(Long ptrId) {
        this.ptrId = ptrId;
    }

    public WmsProduct getPtrProduct() {
        return this.ptrProduct;
    }
    
    public void setPtrProduct(WmsProduct ptrProduct) {
        this.ptrProduct = ptrProduct;
    }

    public CusArea getPtrProv() {
        return this.ptrProv;
    }
    
    public void setPtrProv(CusArea ptrProv) {
        this.ptrProv = ptrProv;
    }

    public CusProvince getPtrCity() {
        return this.ptrCity;
    }
    
    public void setPtrCity(CusProvince ptrCity) {
        this.ptrCity = ptrCity;
    }

    public CusCity getPtrDistrict() {
        return this.ptrDistrict;
    }
    
    public void setPtrDistrict(CusCity ptrDistrict) {
        this.ptrDistrict = ptrDistrict;
    }

    public String getPtrUnit() {
        return this.ptrUnit;
    }
    
    public void setPtrUnit(String ptrUnit) {
        this.ptrUnit = ptrUnit;
    }

    public Double getPtrAmt() {
        return this.ptrAmt;
    }
    
    public void setPtrAmt(Double ptrAmt) {
        this.ptrAmt = ptrAmt;
    }

	public TypeList getPtrExpTypeList() {
		return ptrExpTypeList;
	}


	public void setPtrExpTypeList(TypeList ptrExpTypeList) {
		this.ptrExpTypeList = ptrExpTypeList;
	}
   








}