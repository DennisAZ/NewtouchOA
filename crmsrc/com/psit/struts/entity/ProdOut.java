package com.psit.struts.entity;
// default package

import java.util.Date;


/**
 * 出库记录实体类
 */

public class ProdOut  implements java.io.Serializable {


    // Fields    

     private Long pouId;
     private String pouCode;
     private SalOrdCon salOrdCon;
     private ProdStore prodStore;
     private Date pouDate;
     private String pouState;
     private String pouRemark;
     private String pouPickMan;
     private String pouRespMan;
     private String pouCreMan;
     private String pouUpdMan;
     private Date pouCreTime;
     private Date pouUpdTime;
     private Double pouOutNum;


	/** default constructor */
    public ProdOut() {
    }

    public ProdOut(Long pouId){
    	this.pouId = pouId;
    }
    
    /** full constructor */

    public ProdOut(String pouCode, SalOrdCon salOrdCon, ProdStore prodStore,
			Date pouDate, String pouState, String pouRemark, String pouPickMan,
			String pouRespMan, String pouCreMan, String pouUpdMan,
			Date pouCreTime, Date pouUpdTime, Double pouOutNum) {
		this.pouCode = pouCode;
		this.salOrdCon = salOrdCon;
		this.prodStore = prodStore;
		this.pouDate = pouDate;
		this.pouState = pouState;
		this.pouRemark = pouRemark;
		this.pouPickMan = pouPickMan;
		this.pouRespMan = pouRespMan;
		this.pouCreMan = pouCreMan;
		this.pouUpdMan = pouUpdMan;
		this.pouCreTime = pouCreTime;
		this.pouUpdTime = pouUpdTime;
		this.pouOutNum = pouOutNum;
	}

	public Long getPouId() {
		return pouId;
	}

	public void setPouId(Long pouId) {
		this.pouId = pouId;
	}

	public String getPouCode() {
		return pouCode;
	}

	public void setPouCode(String pouCode) {
		this.pouCode = pouCode;
	}

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}

	public ProdStore getProdStore() {
		return prodStore;
	}

	public void setProdStore(ProdStore prodStore) {
		this.prodStore = prodStore;
	}

	public Date getPouDate() {
		return pouDate;
	}

	public void setPouDate(Date pouDate) {
		this.pouDate = pouDate;
	}

	public String getPouState() {
		return pouState;
	}

	public void setPouState(String pouState) {
		this.pouState = pouState;
	}

	public String getPouRemark() {
		return pouRemark;
	}

	public void setPouRemark(String pouRemark) {
		this.pouRemark = pouRemark;
	}

	public String getPouPickMan() {
		return pouPickMan;
	}

	public void setPouPickMan(String pouPickMan) {
		this.pouPickMan = pouPickMan;
	}

	public String getPouRespMan() {
		return pouRespMan;
	}

	public void setPouRespMan(String pouRespMan) {
		this.pouRespMan = pouRespMan;
	}

	public String getPouCreMan() {
		return pouCreMan;
	}

	public void setPouCreMan(String pouCreMan) {
		this.pouCreMan = pouCreMan;
	}

	public String getPouUpdMan() {
		return pouUpdMan;
	}

	public void setPouUpdMan(String pouUpdMan) {
		this.pouUpdMan = pouUpdMan;
	}

	public Date getPouCreTime() {
		return pouCreTime;
	}

	public void setPouCreTime(Date pouCreTime) {
		this.pouCreTime = pouCreTime;
	}

	public Date getPouUpdTime() {
		return pouUpdTime;
	}

	public void setPouUpdTime(Date pouUpdTime) {
		this.pouUpdTime = pouUpdTime;
	}

	public Double getPouOutNum() {
		return pouOutNum;
	}

	public void setPouOutNum(Double pouOutNum) {
		this.pouOutNum = pouOutNum;
	}


}