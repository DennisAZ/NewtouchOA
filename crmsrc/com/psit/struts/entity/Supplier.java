package com.psit.struts.entity;

import java.util.Date;

public class Supplier implements java.io.Serializable {

	// Fields

	private Long supId;
	private String supCode;
	private TypeList supType;
	private String supName;
	private String supPhone;
	private String supMob;
	private String supFex;
	private String supEmail;
	private String supNet;
	private String supQq;
	private String supAdd;
	private String supZipCode;
	private String supProd;
	private CusArea supArea1;
	private CusProvince supArea2;
	private CusCity supArea3;
	private String supContactMan;
	private String supBank;
	private String supBankName;
	private String supBankCode;
	private String supRemark;
	private String supCreMan;
	private Date supCreTime;
	private Date supUpdTime;
	private String supUpdMan;
	
	public static final String T_SUP_TYP="supType";

	// Constructors

	/** default constructor */
	public Supplier() {
	}
	
	public Supplier(Long supId) {
		this.supId = supId;
	}

	/** full constructor */
	public Supplier(String supCode, Long supTypeId, String supName,
			String supPhone, String supMob, String supFex, String supEmail,
			String supNet, String supAdd, String supZipCode, String supProd,
			Long supArea1, Long supArea2, Long supArea3, String supBank,
			String supBankName, String supBankCode, String supRemark,
			String supCreMan, Date supCreTime, Date supUpdTime, String supUpdMan) {
		this.supCode = supCode;
		this.supName = supName;
		this.supPhone = supPhone;
		this.supMob = supMob;
		this.supFex = supFex;
		this.supEmail = supEmail;
		this.supNet = supNet;
		this.supAdd = supAdd;
		this.supZipCode = supZipCode;
		this.supProd = supProd;
		this.supBank = supBank;
		this.supBankName = supBankName;
		this.supBankCode = supBankCode;
		this.supRemark = supRemark;
		this.supCreMan = supCreMan;
		this.supCreTime = supCreTime;
		this.supUpdTime = supUpdTime;
		this.supUpdMan = supUpdMan;
	}

	// Property accessors

	public Long getSupId() {
		return this.supId;
	}

	public void setSupId(Long supId) {
		this.supId = supId;
	}

	public String getSupCode() {
		return this.supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}


	public String getSupName() {
		return this.supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	public String getSupPhone() {
		return this.supPhone;
	}

	public void setSupPhone(String supPhone) {
		this.supPhone = supPhone;
	}

	public String getSupMob() {
		return this.supMob;
	}

	public void setSupMob(String supMob) {
		this.supMob = supMob;
	}

	public String getSupFex() {
		return this.supFex;
	}

	public void setSupFex(String supFex) {
		this.supFex = supFex;
	}

	public String getSupEmail() {
		return this.supEmail;
	}

	public void setSupEmail(String supEmail) {
		this.supEmail = supEmail;
	}

	public String getSupNet() {
		return this.supNet;
	}

	public void setSupNet(String supNet) {
		this.supNet = supNet;
	}

	public String getSupAdd() {
		return this.supAdd;
	}

	public void setSupAdd(String supAdd) {
		this.supAdd = supAdd;
	}

	public String getSupZipCode() {
		return this.supZipCode;
	}

	public void setSupZipCode(String supZipCode) {
		this.supZipCode = supZipCode;
	}

	public String getSupProd() {
		return this.supProd;
	}

	public void setSupProd(String supProd) {
		this.supProd = supProd;
	}

	public TypeList getSupType() {
		return supType;
	}

	public void setSupType(TypeList supType) {
		this.supType = supType;
	}

	public CusArea getSupArea1() {
		return supArea1;
	}

	public void setSupArea1(CusArea supArea1) {
		this.supArea1 = supArea1;
	}

	public CusProvince getSupArea2() {
		return supArea2;
	}

	public void setSupArea2(CusProvince supArea2) {
		this.supArea2 = supArea2;
	}

	public CusCity getSupArea3() {
		return supArea3;
	}

	public void setSupArea3(CusCity supArea3) {
		this.supArea3 = supArea3;
	}

	public String getSupBank() {
		return this.supBank;
	}

	public void setSupBank(String supBank) {
		this.supBank = supBank;
	}

	public String getSupBankName() {
		return this.supBankName;
	}

	public void setSupBankName(String supBankName) {
		this.supBankName = supBankName;
	}

	public String getSupBankCode() {
		return this.supBankCode;
	}

	public void setSupBankCode(String supBankCode) {
		this.supBankCode = supBankCode;
	}

	public String getSupRemark() {
		return this.supRemark;
	}

	public void setSupRemark(String supRemark) {
		this.supRemark = supRemark;
	}

	public String getSupCreMan() {
		return this.supCreMan;
	}

	public void setSupCreMan(String supCreMan) {
		this.supCreMan = supCreMan;
	}

	public Date getSupCreTime() {
		return this.supCreTime;
	}

	public void setSupCreTime(Date supCreTime) {
		this.supCreTime = supCreTime;
	}

	public Date getSupUpdTime() {
		return this.supUpdTime;
	}

	public void setSupUpdTime(Date supUpdTime) {
		this.supUpdTime = supUpdTime;
	}

	public String getSupUpdMan() {
		return this.supUpdMan;
	}

	public void setSupUpdMan(String supUpdMan) {
		this.supUpdMan = supUpdMan;
	}

	public String getSupContactMan() {
		return supContactMan;
	}

	public void setSupContactMan(String supContactMan) {
		this.supContactMan = supContactMan;
	}

	public String getSupQq() {
		return supQq;
	}

	public void setSupQq(String supQq) {
		this.supQq = supQq;
	}

}