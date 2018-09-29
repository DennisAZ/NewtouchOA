package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 新闻公告实体 <br>
 *
 * create_date: Aug 27, 2010,3:25:47 PM<br>
 * @author zjr
 */

public class News implements java.io.Serializable {

	// Fields

	private Long newCode;
	private SalEmp salEmp;
	private String newTitle;
	private String newType;
	private String newContent;
	private String newIstop;
	private String newInsUser;
	private String newUpdUser;
	private Date newUpdDate;
	private Date newDate;
	private Set RNewLims = new HashSet(0);
	private Set attachments = new HashSet(0);
	// Constructors

	public Set getAttachments() {
		return attachments;
	}
	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	/** default constructor */
	public News() {
	}
    public News(Long newCode)
    {
    	this.newCode=newCode;
    }
	/** full constructor */
	public News(SalEmp salEmp, String newTitle, String newType,
			String newContent, String newIstop, Date newDate,
			Set RNewLims,String newInsUser,String newUpdUser,Date newUpdDate) {
		this.salEmp = salEmp;
		this.newTitle = newTitle;
		this.newType = newType;
		this.newContent = newContent;
		this.newIstop = newIstop;
		this.newDate = newDate;
		this.RNewLims = RNewLims;
		this.newInsUser=newInsUser;
		this.newUpdUser=newUpdUser;
		this.newUpdDate=newUpdDate;
	}

	// Property accessors

	public Long getNewCode() {
		return this.newCode;
	}

	public void setNewCode(Long newCode) {
		this.newCode = newCode;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public String getNewTitle() {
		return this.newTitle;
	}

	public void setNewTitle(String newTitle) {
		this.newTitle = newTitle;
	}

	public String getNewType() {
		return this.newType;
	}

	public void setNewType(String newType) {
		this.newType = newType;
	}

	public String getNewContent() {
		return this.newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public String getNewIstop() {
		return this.newIstop;
	}

	public void setNewIstop(String newIstop) {
		this.newIstop = newIstop;
	}

	public Date getNewDate() {
		return this.newDate;
	}

	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}

	public Set getRNewLims() {
		return this.RNewLims;
	}

	public void setRNewLims(Set RNewLims) {
		this.RNewLims = RNewLims;
	}
	public String getNewInsUser() {
		return newInsUser;
	}
	public void setNewInsUser(String newInsUser) {
		this.newInsUser = newInsUser;
	}
	public String getNewUpdUser() {
		return newUpdUser;
	}
	public void setNewUpdUser(String newUpdUser) {
		this.newUpdUser = newUpdUser;
	}
	public Date getNewUpdDate() {
		return newUpdDate;
	}
	public void setNewUpdDate(Date newUpdDate) {
		this.newUpdDate = newUpdDate;
	}

}