package com.psit.struts.entity;

import java.util.Date;

/**
 * 消息接收人实体 <br>
 */

public class RMessLim implements java.io.Serializable {

	// Fields

	private Long rmlId;
	private Message message;
	private SalEmp salEmp;
	private Date rmlDate;
	private String rmlIsdel;
    private String rmlRecUser;
    private String rmlState;
    
	// Constructors

	/** default constructor */
	public RMessLim() {
	}

	/** full constructor */
	public RMessLim(Message message, SalEmp salEmp, Date rmlDate,
			String rmlIsdel,String rmlRecUser, String rmlState) {
		this.message = message;
		this.salEmp = salEmp;
		this.rmlDate = rmlDate;
		this.rmlIsdel = rmlIsdel;
		this.rmlRecUser=rmlRecUser;
		this.rmlState = rmlState;
	}

	// Property accessors

	public Long getRmlId() {
		return this.rmlId;
	}

	public void setRmlId(Long rmlId) {
		this.rmlId = rmlId;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}


	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public Date getRmlDate() {
		return this.rmlDate;
	}

	public void setRmlDate(Date rmlDate) {
		this.rmlDate = rmlDate;
	}

	public String getRmlIsdel() {
		return rmlIsdel;
	}

	public void setRmlIsdel(String rmlIsdel) {
		this.rmlIsdel = rmlIsdel;
	}

	public String getRmlRecUser() {
		return rmlRecUser;
	}

	public void setRmlRecUser(String rmlRecUser) {
		this.rmlRecUser = rmlRecUser;
	}

	public String getRmlState() {
		return rmlState;
	}

	public void setRmlState(String rmlState) {
		this.rmlState = rmlState;
	}

}