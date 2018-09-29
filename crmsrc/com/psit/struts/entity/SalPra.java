package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 来往记录实体 <br>
 */

public class SalPra implements java.io.Serializable {

	// Fields

	private Long praId;
	private CusCorCus cusCorCus;
	private LimUser limUser;
	private YHPerson person;
	private SalOpp salOpp;
	private String praTitle;
	private String praContent;
	private Date praInsDate;
	private String praType;
	private String praState;
	private String praContType;
	private Date praUpdDate;
	private Date praExeDate;
	private String praCostTime;
	private String praCusLink;
	private String praRemark;
	private String praInpUser;
	private String praUpdUser;
	private String praIsDel;
	private Double praCost;
	private CusContact cusContact;
	private Set attachments = new HashSet(0);

	// Constructors

	/** default constructor */
	public SalPra() {
	}
	public SalPra(Long praId) {
		this.praId=praId;
	}

	/** full constructor */
	public SalPra(CusCorCus cusCorCus, LimUser limUser,SalOpp salOpp,
			String praTitle, String praContent, Date praInsDate,String praIsDel,
			String praType, String praState, String praContType,
			Date praExeDate,Date praUpdDate, String praCostTime, String praCusLink,
			YHPerson person,String praRemark, String praInpUser,
			String praUpdUser,Set attachments, CusContact cusContact) {
		this.cusCorCus = cusCorCus;
		this.salOpp=salOpp;
		this.limUser=limUser;
		this.praTitle = praTitle;
		this.praContent = praContent;
		this.praInsDate = praInsDate;
		this.praType = praType;
		this.praState = praState;
		this.praContType = praContType;
		this.praUpdDate=praUpdDate;
		this.praExeDate = praExeDate;
		this.praCostTime = praCostTime;
		this.praCusLink = praCusLink;
		this.praRemark = praRemark;
		this.praInpUser=praInpUser;
		this.praUpdUser=praUpdUser;
		this.praIsDel=praIsDel;
		this.attachments = attachments;
		this.person=person;
		this.cusContact = cusContact;
	}

	// Property accessors

	public Long getPraId() {
		return this.praId;
	}

	public void setPraId(Long praId) {
		this.praId = praId;
	}

	public CusCorCus getCusCorCus() {
		return this.cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}

	public String getPraTitle() {
		return this.praTitle;
	}

	public void setPraTitle(String praTitle) {
		this.praTitle = praTitle;
	}

	public String getPraContent() {
		return this.praContent;
	}

	public void setPraContent(String praContent) {
		this.praContent = praContent;
	}

	public Date getPraInsDate() {
		return this.praInsDate;
	}

	public void setPraInsDate(Date praInsDate) {
		this.praInsDate = praInsDate;
	}

	public String getPraType() {
		return this.praType;
	}

	public void setPraType(String praType) {
		this.praType = praType;
	}

	public String getPraState() {
		return this.praState;
	}

	public void setPraState(String praState) {
		this.praState = praState;
	}

	public String getPraContType() {
		return this.praContType;
	}

	public void setPraContType(String praContType) {
		this.praContType = praContType;
	}

	public Date getPraExeDate() {
		return this.praExeDate;
	}

	public void setPraExeDate(Date praExeDate) {
		this.praExeDate = praExeDate;
	}

	public String getPraCostTime() {
		return this.praCostTime;
	}

	public void setPraCostTime(String praCostTime) {
		this.praCostTime = praCostTime;
	}

	public String getPraCusLink() {
		return this.praCusLink;
	}

	public void setPraCusLink(String praCusLink) {
		this.praCusLink = praCusLink;
	}

	public String getPraRemark() {
		return StringFormat.toBlank(this.praRemark);
	}

	public void setPraRemark(String praRemark) {
		this.praRemark = praRemark;
	}

	public Set getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	public Date getPraUpdDate() {
		return praUpdDate;
	}
	public void setPraUpdDate(Date praUpdDate) {
		this.praUpdDate = praUpdDate;
	}
	public SalOpp getSalOpp() {
		return salOpp;
	}
	public void setSalOpp(SalOpp salOpp) {
		this.salOpp = salOpp;
	}
	public String getPraInpUser() {
		return praInpUser;
	}
	public void setPraInpUser(String praInpUser) {
		this.praInpUser = praInpUser;
	}
	public String getPraUpdUser() {
		return praUpdUser;
	}
	public void setPraUpdUser(String praUpdUser) {
		this.praUpdUser = praUpdUser;
	}
	public String getPraIsDel() {
		return praIsDel;
	}
	public void setPraIsDel(String praIsDel) {
		this.praIsDel = praIsDel;
	}
	public LimUser getLimUser() {
		return limUser;
	}
	public void setLimUser(LimUser limUser) {
		this.limUser = limUser;
	}
	public YHPerson getPerson() {
		return person;
	}
	public void setPerson(YHPerson person) {
		this.person = person;
	}
	public Double getPraCost() {
		return praCost;
	}
	public void setPraCost(Double praCost) {
		this.praCost = praCost;
	}
	public CusContact getCusContact() {
		return cusContact;
	}
	public void setCusContact(CusContact cusContact) {
		this.cusContact = cusContact;
	}
}