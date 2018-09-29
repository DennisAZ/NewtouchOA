package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 客户服务实体 <br>
 */

public class CusServ implements java.io.Serializable {

	// Fields

	private Long serCode;
	private CusCorCus cusCorCus;
	private LimUser limUser;
	private SalEmp salEmp;
	private String serTitle;
	private String serCusLink;
	private String serMethod;
	private String serContent;
	private Date serExeDate;
	private String serCosTime;
	private String serState;
	private String serFeedback;
	private String serRemark;
	private Date serInsDate;
	private String serInpUser;
	private String serUpdUser;
	private Date serUpdDate;
	private String serIsDel;
	private Set attachments = new HashSet(0);

	// Constructors

	/** default constructor */
	public CusServ() {
	}
	
	public CusServ(Long serCode) {
		this.serCode=serCode;
	}
	
	/** full constructor */
	public CusServ(LimUser limUser, CusCorCus cusCorCus,
			String serTitle, String serCusLink, String serIsDel,
			String serMethod, String serContent, Date serExeDate,
			String serCosTime, String serState,
			SalEmp salEmp, String serFeedback, String serRemark,
			Date serInsDate, String serInpUser, String serUpdUser,
			Date serUpdDate, Set attachments) {
		this.cusCorCus = cusCorCus;
		this.limUser=limUser;
		this.serTitle = serTitle;
		this.serCusLink = serCusLink;
		this.serMethod = serMethod;
		this.serContent = serContent;
		this.serExeDate = serExeDate;
		this.serCosTime = serCosTime;
		this.serState = serState;
		this.serFeedback = serFeedback;
		this.serRemark = serRemark;
		this.serInsDate = serInsDate;
		this.serInpUser = serInpUser;
		this.serUpdUser = serUpdUser;
		this.serUpdDate = serUpdDate;
		this.serIsDel=serIsDel;
		this.attachments = attachments;
		this.salEmp= salEmp;
	}

	// Property accessors

	public Long getSerCode() {
		return this.serCode;
	}

	public void setSerCode(Long serCode) {
		this.serCode = serCode;
	}

	public CusCorCus getCusCorCus() {
		return this.cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}


	public String getSerTitle() {
		return this.serTitle;
	}

	public void setSerTitle(String serTitle) {
		this.serTitle = serTitle;
	}

	public String getSerCusLink() {
		return this.serCusLink;
	}

	public void setSerCusLink(String serCusLink) {
		this.serCusLink = serCusLink;
	}

	public String getSerMethod() {
		return this.serMethod;
	}

	public void setSerMethod(String serMethod) {
		this.serMethod = serMethod;
	}

	public String getSerContent() {
		return StringFormat.toBlank(this.serContent);
	}

	public void setSerContent(String serContent) {
		this.serContent = serContent;
	}

	public Date getSerExeDate() {
		return this.serExeDate;
	}

	public void setSerExeDate(Date serExeDate) {
		this.serExeDate = serExeDate;
	}

	public String getSerCosTime() {
		return this.serCosTime;
	}

	public void setSerCosTime(String serCosTime) {
		this.serCosTime = serCosTime;
	}

	public String getSerState() {
		return this.serState;
	}

	public void setSerState(String serState) {
		this.serState = serState;
	}


	public String getSerFeedback() {
		return StringFormat.toBlank(this.serFeedback);
	}

	public void setSerFeedback(String serFeedback) {
		this.serFeedback = serFeedback;
	}

	public String getSerRemark() {
		return StringFormat.toBlank(this.serRemark);
	}

	public void setSerRemark(String serRemark) {
		this.serRemark = serRemark;
	}

	public Date getSerInsDate() {
		return this.serInsDate;
	}

	public void setSerInsDate(Date serInsDate) {
		this.serInsDate = serInsDate;
	}


	public Set getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	public String getSerInpUser() {
		return serInpUser;
	}

	public void setSerInpUser(String serInpUser) {
		this.serInpUser = serInpUser;
	}

	public String getSerUpdUser() {
		return serUpdUser;
	}

	public void setSerUpdUser(String serUpdUser) {
		this.serUpdUser = serUpdUser;
	}

	public Date getSerUpdDate() {
		return serUpdDate;
	}

	public void setSerUpdDate(Date serUpdDate) {
		this.serUpdDate = serUpdDate;
	}

	public String getSerIsDel() {
		return serIsDel;
	}

	public void setSerIsDel(String serIsDel) {
		this.serIsDel = serIsDel;
	}

	public LimUser getLimUser() {
		return limUser;
	}

	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

}