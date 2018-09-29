package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 销售目标实体 <br>
 * create_date: Aug 27, 2010,3:13:53 PM<br>
 * @author csg
 */

public class SalTask implements java.io.Serializable {

	// Fields

	private Long stId;
	private SalEmp salEmp;
	private TypeList salTaskType;
	private String stName ;
	private String stTitle;
	private Date stRelDate;
	private Date stStartDate;
	private Date stFinDate;
	private String stLev;
	private String stStu;
	private String stTag;
	private String stRemark;
	private Date stChangeDate;
	private String stIsdel;
	private String stLog;
	private String stUpdUser;
	private Date stFctDate;
	private Set taLims = new HashSet(0);
	private Set attachments = new HashSet(0);

	// Constructors

	/** default constructor */
	public SalTask() {
	}
	/** mini constructor */
	public SalTask(Long stId) {
		this.stId=stId;
	}
	
	/** full constructor */
	public SalTask(SalEmp salEmp, TypeList salTaskType, String stName,
			String stTitle, Date stRelDate, Date stFctDate, Date stStartDate,
			Date stFinDate, String stLev, String stStu, String stTag,
			String stRemark, Date stChangeDate, String stIsdel, String stLog,
			String stUpdUser, Set taLims, Set attachments) {
		this.salEmp = salEmp;
		this.stName = stName;
		this.stTitle = stTitle;
		this.stTitle = stTitle;
		this.stRelDate = stRelDate;
		this.stStartDate = stStartDate;
		this.stFinDate = stFinDate;
		this.stFctDate = stFctDate;
		this.stLev = stLev;
		this.stStu = stStu;
		this.stTag = stTag;
		this.stRemark = stRemark;
		this.stChangeDate = stChangeDate;
		this.stIsdel = stIsdel;
		this.stLog=stLog;
		this.stUpdUser=stUpdUser;
		this.taLims = taLims;
		this.attachments = attachments;
	}

	// Property accessors

	public Long getStId() {
		return this.stId;
	}

	public void setStId(Long stId) {
		this.stId = stId;
	}

	public TypeList getSalTaskType() {
		return this.salTaskType;
	}

	public void setSalTaskType(TypeList salTaskType) {
		this.salTaskType = salTaskType;
	}

	public String getStName() {
		return stName;
	}
	public void setStName(String stName) {
		this.stName = stName;
	}
	public String getStTitle() {
		return this.stTitle;
	}

	public void setStTitle(String stTitle) {
		this.stTitle = stTitle;
	}

	public Date getStRelDate() {
		return this.stRelDate;
	}

	public void setStRelDate(Date stRelDate) {
		this.stRelDate = stRelDate;
	}

	public Date getStFinDate() {
		return this.stFinDate;
	}

	public void setStFinDate(Date stFinDate) {
		this.stFinDate = stFinDate;
	}

	public String getStLev() {
		return this.stLev;
	}

	public void setStLev(String stLev) {
		this.stLev = stLev;
	}

	public String getStStu() {
		return this.stStu;
	}

	public void setStStu(String stStu) {
		this.stStu = stStu;
	}

	public String getStTag() {
		return this.stTag;
	}

	public void setStTag(String stTag) {
		this.stTag = stTag;
	}

	public String getStRemark() {
		return StringFormat.toBlank(this.stRemark);
	}

	public void setStRemark(String stRemark) {
		this.stRemark = stRemark;
	}

	public Date getStChangeDate() {
		return this.stChangeDate;
	}

	public void setStChangeDate(Date stChangeDate) {
		this.stChangeDate = stChangeDate;
	}

	public String getStIsdel() {
		return this.stIsdel;
	}

	public void setStIsdel(String stIsdel) {
		this.stIsdel = stIsdel;
	}

	public Set getTaLims() {
		return this.taLims;
	}

	public void setTaLims(Set taLims) {
		this.taLims = taLims;
	}

	public String getStLog() {
		return stLog;
	}

	public void setStLog(String stLog) {
		this.stLog = stLog;
	}

	public Set getAttachments() {
		return attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	public Date getStFctDate() {
		return stFctDate;
	}
	public void setStFctDate(Date stFctDate) {
		this.stFctDate = stFctDate;
	}
	public String getStUpdUser() {
		return stUpdUser;
	}
	public void setStUpdUser(String stUpdUser) {
		this.stUpdUser = stUpdUser;
	}
	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public Date getStStartDate() {
		return stStartDate;
	}
	public void setStStartDate(Date stStartDate) {
		this.stStartDate = stStartDate;
	}

}