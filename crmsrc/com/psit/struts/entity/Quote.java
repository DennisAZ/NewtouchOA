package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 报价实体 <br>
 */

public class Quote implements java.io.Serializable {

	// Fields

	private Long quoId;
	private SalOpp salOpp;
	private String quoTitle;
	private Double quoPrice;
	private SalEmp salEmp;
	private String quoDesc;
	private String quoRemark;
	private String quoInpUser;
	private String quoUpdUser;
	private Date quoUpdDate;
	private Date quoDate;
	private Date quoInsDate;
	private String quoIsDel;
	private Set attachments = new HashSet(0);
	private Set rquoPros = new HashSet(0);

	// Constructors

	/** default constructor */
	public Quote() {
	}

	/** mini constructor */
	public Quote(Long quoId) {
		this.quoId = quoId;
	}

	/** full constructor */
	public Quote(SalOpp salOpp, LimUser limUser, 
			String quoTitle, String quoInpUser, String quoUpdUser,
			Date quoUpdDate, Double quoPrice, SalEmp salEmp, String quoDesc,
			String quoRemark, Date quoDate, Date quoInsDate, String quoIsDel,
			Set attachments, Set rquoPros) {
		this.salOpp = salOpp;
		this.quoTitle = quoTitle;
		this.quoPrice = quoPrice;
		this.quoDesc = quoDesc;
		this.quoRemark = quoRemark;
		this.quoInpUser = quoInpUser;
		this.quoUpdUser = quoUpdUser;
		this.quoUpdDate = quoUpdDate;
		this.quoDate = quoDate;
		this.quoInsDate = quoInsDate;
		this.quoIsDel = quoIsDel;
		this.attachments = attachments;
		this.rquoPros = rquoPros;
		this.salEmp = salEmp;
	}

	// Property accessors

	public Long getQuoId() {
		return this.quoId;
	}

	public void setQuoId(Long quoId) {
		this.quoId = quoId;
	}

	public SalOpp getSalOpp() {
		return this.salOpp;
	}

	public void setSalOpp(SalOpp salOpp) {
		this.salOpp = salOpp;
	}

	public String getQuoTitle() {
		return this.quoTitle;
	}

	public void setQuoTitle(String quoTitle) {
		this.quoTitle = quoTitle;
	}

	public Double getQuoPrice() {
		return this.quoPrice;
	}

	public void setQuoPrice(Double quoPrice) {
		this.quoPrice = quoPrice;
	}

	public String getQuoRemark() {
		return this.quoRemark;
	}

	public void setQuoRemark(String quoRemark) {
		this.quoRemark = quoRemark;
	}

	public Date getQuoDate() {
		return this.quoDate;
	}

	public void setQuoDate(Date quoDate) {
		this.quoDate = quoDate;
	}

	public String getQuoDesc() {
		return quoDesc;
	}

	public void setQuoDesc(String quoDesc) {
		this.quoDesc = quoDesc;
	}

	public Date getQuoInsDate() {
		return quoInsDate;
	}

	public void setQuoInsDate(Date quoInsDate) {
		this.quoInsDate = quoInsDate;
	}

	public String getQuoInpUser() {
		return quoInpUser;
	}

	public void setQuoInpUser(String quoInpUser) {
		this.quoInpUser = quoInpUser;
	}

	public String getQuoUpdUser() {
		return quoUpdUser;
	}

	public void setQuoUpdUser(String quoUpdUser) {
		this.quoUpdUser = quoUpdUser;
	}

	public Date getQuoUpdDate() {
		return quoUpdDate;
	}

	public void setQuoUpdDate(Date quoUpdDate) {
		this.quoUpdDate = quoUpdDate;
	}

	public String getQuoIsDel() {
		return quoIsDel;
	}

	public void setQuoIsDel(String quoIsDel) {
		this.quoIsDel = quoIsDel;
	}

	public Set getAttachments() {
		return attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	public Set getRquoPros() {
		return rquoPros;
	}

	public void setRquoPros(Set rquoPros) {
		this.rquoPros = rquoPros;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

}