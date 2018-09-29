package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 报告实体 <br>
 */

public class Report implements java.io.Serializable {

	// Fields

	private Long repCode;
	private SalEmp salEmp;
	private String repTitle;
	private String repSendTitle;
	private String repContent;
	private String repIsSend;
	private String repIsappro;
	private String repIsdel;
	private Date repDate;
	private TypeList repType;
	private String repInsUser;
	private String repRecName;
	private Set attachments = new HashSet(0);
	private Set RRepLims = new HashSet(0);

	// Constructors

	/** default constructor */
	public Report() {
	}
	public Report(Long repCode) 
	{
		this.repCode=repCode;
	}
	/** full constructor */
	public Report(SalEmp salEmp, String repTitle,String repSendTitle, String repContent,
			String repIsSend, String repIsappro,String repIsdel,String repRecName,
			Date repDate, TypeList repType,String repInsUser, Set attachments, Set RRepLims) {
		this.salEmp = salEmp;
		this.repTitle = repTitle;
		this.repSendTitle=repSendTitle;
		this.repContent = repContent;
		this.repIsSend = repIsSend;
		this.repIsappro = repIsappro;
		this.repIsdel=repIsdel;
		this.repDate = repDate;
		this.repType = repType;
		this.repInsUser=repInsUser;
		this.repRecName=repRecName;
		this.attachments = attachments;
		this.RRepLims = RRepLims;
	}

	// Property accessors

	public Long getRepCode() {
		return this.repCode;
	}

	public void setRepCode(Long repCode) {
		this.repCode = repCode;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public String getRepTitle() {
		return this.repTitle;
	}

	public void setRepTitle(String repTitle) {
		this.repTitle = repTitle;
	}

	public String getRepContent() {
		return StringFormat.toBlank(this.repContent);
	}

	public void setRepContent(String repContent) {
		this.repContent = repContent;
	}


	public String getRepIsappro() {
		return this.repIsappro;
	}

	public void setRepIsappro(String repIsappro) {
		this.repIsappro = repIsappro;
	}

	public Date getRepDate() {
		return this.repDate;
	}

	public void setRepDate(Date repDate) {
		this.repDate = repDate;
	}

	public TypeList getRepType() {
		return this.repType;
	}

	public void setRepType(TypeList repType) {
		this.repType = repType;
	}

	public Set getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	public Set getRRepLims() {
		return this.RRepLims;
	}

	public void setRRepLims(Set RRepLims) {
		this.RRepLims = RRepLims;
	}
	public String getRepSendTitle() {
		return repSendTitle;
	}
	public void setRepSendTitle(String repSendTitle) {
		this.repSendTitle = repSendTitle;
	}
	public String getRepIsSend() {
		return repIsSend;
	}
	public void setRepIsSend(String repIsSend) {
		this.repIsSend = repIsSend;
	}
	public String getRepIsdel() {
		return repIsdel;
	}
	public void setRepIsdel(String repIsdel) {
		this.repIsdel = repIsdel;
	}
	public String getRepInsUser() {
		return repInsUser;
	}
	public void setRepInsUser(String repInsUser) {
		this.repInsUser = repInsUser;
	}
	public String getRepRecName() {
		return repRecName;
	}
	public void setRepRecName(String repRecName) {
		this.repRecName = repRecName;
	}
	

}