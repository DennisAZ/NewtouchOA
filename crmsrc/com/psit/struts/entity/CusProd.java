package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CusProd entity.
 */

public class CusProd implements java.io.Serializable {

	// Fields

	private Long cpId;
	private CusCorCus cusCorCus;
	private WmsProduct wmsProduct;
	private String cpHasTax;
	private Double cpPrice;
	private String cpRemark;
	private String cpCreUser;
	private Date cpCreDate;
	private String cpUpdUser;
	private Date cpUpdDate;
	private String cpOtherName;
	private Double cpPreNumber;
	private Long cpWarnDay;
	private Date cpSentDate;
	private Set attachments = new HashSet(0);
	public Set getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	// Constructors

	/** default constructor */
	public CusProd() {
	}
	/** default constructor */
	public CusProd(Long id) {
		this.cpId=id;
	}
	/** full constructor */
	public CusProd(CusCorCus cusCorCus, WmsProduct wmsProduct, String cpHasTax, Double cpPrice,
			String cpRemark, String cpCreUser, Date cpCreDate,String cpUpdUser, Date cpUpdDate,String cpOtherName,
			Double cpPreNumber,Long cpWarnDay,Date cpSentDate) {
		this.cusCorCus = cusCorCus;
		this.wmsProduct = wmsProduct;
		this.cpPrice = cpPrice;
		this.cpRemark = cpRemark;
		this.cpCreUser = cpCreUser;
		this.cpCreDate = cpCreDate;
		this.cpUpdUser = cpUpdUser;
		this.cpUpdDate = cpUpdDate;
		this.cpOtherName = cpOtherName;
		this.cpPreNumber = cpPreNumber;
		this.cpWarnDay = cpWarnDay;
		this.cpSentDate = cpSentDate;
	}

	// Property accessors

	public Date getCpSentDate() {
		return cpSentDate;
	}

	public void setCpSentDate(Date cpSentDate) {
		this.cpSentDate = cpSentDate;
	}

	public Double getCpPreNumber() {
		return cpPreNumber;
	}

	public void setCpPreNumber(Double cpPreNumber) {
		this.cpPreNumber = cpPreNumber;
	}

	public Long getCpWarnDay() {
		return cpWarnDay;
	}

	public void setCpWarnDay(Long cpWarnDay) {
		this.cpWarnDay = cpWarnDay;
	}

	public String getCpOtherName() {
		return cpOtherName;
	}

	public void setCpOtherName(String cpOtherName) {
		this.cpOtherName = cpOtherName;
	}

	public Long getCpId() {
		return this.cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public CusCorCus getCusCorCus() {
		return cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}

	public WmsProduct getWmsProduct() {
		return wmsProduct;
	}

	public void setWmsProduct(WmsProduct wmsProduct) {
		this.wmsProduct = wmsProduct;
	}

	public Double getCpPrice() {
		return this.cpPrice;
	}

	public void setCpPrice(Double cpPrice) {
		this.cpPrice = cpPrice;
	}

	public String getCpRemark() {
		return this.cpRemark;
	}

	public void setCpRemark(String cpRemark) {
		this.cpRemark = cpRemark;
	}

	public String getCpCreUser() {
		return this.cpCreUser;
	}

	public void setCpCreUser(String cpCreUser) {
		this.cpCreUser = cpCreUser;
	}

	public Date getCpCreDate() {
		return this.cpCreDate;
	}

	public void setCpCreDate(Date cpCreDate) {
		this.cpCreDate = cpCreDate;
	}

	public String getCpUpdUser() {
		return cpUpdUser;
	}

	public void setCpUpdUser(String cpUpdUser) {
		this.cpUpdUser = cpUpdUser;
	}

	public Date getCpUpdDate() {
		return cpUpdDate;
	}

	public void setCpUpdDate(Date cpUpdDate) {
		this.cpUpdDate = cpUpdDate;
	}

	public String getCpHasTax() {
		return cpHasTax;
	}

	public void setCpHasTax(String cpHasTax) {
		this.cpHasTax = cpHasTax;
	}

}