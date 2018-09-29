package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 消息实体 <br>
 *
 * create_date: Aug 27, 2010,3:24:18 PM<br>
 * @author zjr
 */
public class Message implements java.io.Serializable {

	// Fields

	private Long meCode;
	private SalEmp salEmp;
	private String meTitle;
	private String meContent;
	private String meIssend;
	private String meIsdel;
	private String meInsUser;
	private Date meDate;
	private String meRecName;
	private Set RMessLims = new HashSet(0);
	private Set attachments = new HashSet(0);

	// Constructors

	/** default constructor */
	public Message() {
	}
	public Message(Long meCode) 
	{
		this.meCode=meCode;
	}
	/** full constructor */
	public Message(SalEmp salEmp, String meTitle, String meContent,
			String meIssend, String meIsdel, String meInsUser, Date meDate,String meRecName,
			Set RMessLims, Set attachments) {
		this.salEmp = salEmp;
		this.meTitle = meTitle;
		this.meContent = meContent;
		this.meIssend=meIssend;
		this.meIsdel=meIsdel;
		this.meInsUser=meInsUser;
		this.meDate = meDate;
		this.meRecName = meRecName;
		this.RMessLims = RMessLims;
		this.attachments = attachments;
	}

	// Property accessors

	public Long getMeCode() {
		return this.meCode;
	}

	public void setMeCode(Long meCode) {
		this.meCode = meCode;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public String getMeTitle() {
		return this.meTitle;
	}

	public void setMeTitle(String meTitle) {
		this.meTitle = meTitle;
	}

	public String getMeContent() {
		return this.meContent;
	}

	public void setMeContent(String meContent) {
		this.meContent = meContent;
	}

	public Date getMeDate() {
		return this.meDate;
	}

	public void setMeDate(Date meDate) {
		this.meDate = meDate;
	}

	public Set getRMessLims() {
		return this.RMessLims;
	}

	public void setRMessLims(Set RMessLims) {
		this.RMessLims = RMessLims;
	}

	public Set getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	public String getMeIssend() {
		return meIssend;
	}
	public void setMeIssend(String meIssend) {
		this.meIssend = meIssend;
	}
	public String getMeIsdel() {
		return meIsdel;
	}
	public void setMeIsdel(String meIsdel) {
		this.meIsdel = meIsdel;
	}
	public String getMeInsUser() {
		return meInsUser;
	}
	public void setMeInsUser(String meInsUser) {
		this.meInsUser = meInsUser;
	}
	public String getMeRecName() {
		return meRecName;
	}
	public void setMeRecName(String meRecName) {
		this.meRecName = meRecName;
	}

}