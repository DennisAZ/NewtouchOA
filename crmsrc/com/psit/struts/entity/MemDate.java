package com.psit.struts.entity;

import java.util.Date;

/**
 * 联系人纪念日 <br>
 */

public class MemDate implements java.io.Serializable {
	private Long mdId;
	private CusContact mdContact;
	private String mdName;
	private Date mdDate;
	private String mdRemark;

	public String getMdRemark() {
		return mdRemark;
	}

	public void setMdRemark(String mdRemark) {
		this.mdRemark = mdRemark;
	}

	/** default constructor */
	public MemDate() {
	}

	/** full constructor */
	public MemDate(CusContact mdContact,String mdName, Date mdDate, String mdRemark ) {
		this.mdContact = mdContact;
		this.mdName = mdName;
		this.mdDate = mdDate;
		this.mdRemark = mdRemark;
	}

	public Long getMdId() {
		return mdId;
	}

	public void setMdId(Long mdId) {
		this.mdId = mdId;
	}

	public CusContact getMdContact() {
		return mdContact;
	}

	public void setMdContact(CusContact mdContact) {
		this.mdContact = mdContact;
	}

	public String getMdName() {
		return mdName;
	}

	public void setMdName(String mdName) {
		this.mdName = mdName;
	}

	public Date getMdDate() {
		return mdDate;
	}

	public void setMdDate(Date mdDate) {
		this.mdDate = mdDate;
	}
}