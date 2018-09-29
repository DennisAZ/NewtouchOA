package com.psit.struts.entity;

import java.util.Date;

import com.psit.struts.util.format.StringFormat;

/**
 * 日程安排实体 <br>
 * create_date: Aug 27, 2010,3:19:53 PM<br>
 * @author csg
 */

public class Schedule implements java.io.Serializable {

	// Fields

	private Long schId;
	private TypeList schType;
	private YHPerson person;
	public YHPerson getPerson() {
		return person;
	}

	public void setPerson(YHPerson person) {
		this.person = person;
	}

	private String schTitle;
	private String schState;
	private Date schStartDate;
	private String schStartTime;
	private String schEndTime;
	//private Date schEndDate;
	private Date schDate;
    private String schInsUser;
    private String schUpdUser;
    private Date schUpdDate;
    private CusCorCus cusCorCus;
	// Constructors

	/** default constructor */
	public Schedule() {
	}

	/** full constructor */
	public Schedule(TypeList schType, YHPerson person, String schTitle,
			Date schStartDate,String schStartTime,String schEndTime,String schState,
			 Date schDate,String schInsUser,String schUpdUser,Date schUpdDate,CusCorCus cusCorCus) {
		this.schType = schType;
		this.person = person;
		this.schTitle = schTitle;
		this.schStartDate = schStartDate;
		this.schStartTime=schStartTime;
		this.schEndTime=schEndTime;
		this.schState=schState;
		this.schDate = schDate;
		this.schInsUser=schInsUser;
		this.schUpdUser=schUpdUser;
		this.schUpdDate=schUpdDate;
		this.cusCorCus = cusCorCus;
	}

	// Property accessors

	public Long getSchId() {
		return this.schId;
	}

	public void setSchId(Long schId) {
		this.schId = schId;
	}

	public TypeList getSchType() {
		return this.schType;
	}

	public void setSchType(TypeList schType) {
		this.schType = schType;
	}
	
	public String getSchTitle() {
		return StringFormat.toBlank(this.schTitle);
	}

	public void setSchTitle(String schTitle) {
		this.schTitle = schTitle;//转换BR
	}

	public Date getSchStartDate() {
		return this.schStartDate;
	}

	public void setSchStartDate(Date schStartDate) {
		this.schStartDate = schStartDate;
	}

	public Date getSchDate() {
		return this.schDate;
	}

	public void setSchDate(Date schDate) {
		this.schDate = schDate;
	}

	public String getSchState() {
		return schState;
	}

	public void setSchState(String schState) {
		this.schState = schState;
	}

	public String getSchStartTime() {
		return schStartTime;
	}

	public void setSchStartTime(String schStartTime) {
		this.schStartTime = schStartTime;
	}

	public String getSchEndTime() {
		return schEndTime;
	}

	public void setSchEndTime(String schEndTime) {
		this.schEndTime = schEndTime;
	}

	public String getSchInsUser() {
		return schInsUser;
	}

	public void setSchInsUser(String schInsUser) {
		this.schInsUser = schInsUser;
	}

	public String getSchUpdUser() {
		return schUpdUser;
	}

	public void setSchUpdUser(String schUpdUser) {
		this.schUpdUser = schUpdUser;
	}

	public Date getSchUpdDate() {
		return schUpdDate;
	}

	public void setSchUpdDate(Date schUpdDate) {
		this.schUpdDate = schUpdDate;
	}

	public CusCorCus getCusCorCus() {
		return cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}

}