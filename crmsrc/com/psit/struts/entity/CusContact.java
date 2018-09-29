package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 联系人实体 <br>
 */

public class CusContact implements java.io.Serializable {

	// Fields

	private Long conId;
	private CusCorCus cusCorCus;
	private YHPerson person;
	public YHPerson getPerson() {
		return person;
	}

	public void setPerson(YHPerson person) {
		this.person = person;
	}

	private String conName;
	private String conSex;
	private String conDep;
	private String conPos;
	private String conLev;
	private String conPhone;
	private String conWorkPho;
	private String conHomePho;
	private String conFex;
	private String conZipCode;
	private String conEmail;
	private String conQq;
	private String conMsn;
	private String conAdd;
	private String conOthLink;
	private String conHob;
	private String conTaboo;
	private String conEdu;
	private String conPhoto;
	private String conRemark;
	private String conType;
	private Date conBir;
	private Date conCreDate;
	private Date conModDate;
	private String conInpUser;
	private String conUpdUser;
    private String conIsCons;
    private Set memDates = new HashSet(0);
    public static final String M_CONS = "1",M_UNCONS="0";//收货人标记
    
	// Constructors

	/** default constructor */
	public CusContact() {
	}

	/** minimal constructor */
	public CusContact(Long conId) {
		this.conId = conId;
	}

	/** full constructor */
	public CusContact(Long conId, CusCorCus cusCorCus,String conIsCons,
			YHPerson person, String conName, String conSex, String conDep,
			String conPos, String conLev, String conPhone,
			String conWorkPho, String conHomePho, String conFex,
			String conZipCode, String conEmail, String conQq, String conMsn,
			String conAdd, String conOthLink,String conType, Date conBir, String conHob,
			String conTaboo, String conEdu, String conPhoto, String conRemark,
			Date conCreDate, Date conModDate, String conInpUser, String conUpdUser) {
		this.conId = conId;
		this.cusCorCus = cusCorCus;
		this.person = person;
		this.conName = conName;
		this.conSex = conSex;
		this.conDep = conDep;
		this.conPos = conPos;
		this.conLev = conLev;
		this.conPhone = conPhone;
		this.conWorkPho = conWorkPho;
		this.conHomePho = conHomePho;
		this.conFex = conFex;
		this.conZipCode = conZipCode;
		this.conEmail = conEmail;
		this.conQq = conQq;
		this.conMsn = conMsn;
		this.conAdd = conAdd;
		this.conOthLink = conOthLink;
		this.conType=conType;
		this.conBir = conBir;
		this.conHob = conHob;
		this.conTaboo = conTaboo;
		this.conEdu = conEdu;
		this.conPhoto = conPhoto;
		this.conRemark = conRemark;
		this.conCreDate = conCreDate;
		this.conModDate = conModDate;
		this.conInpUser = conInpUser;
		this.conUpdUser = conUpdUser;
		this.conIsCons=conIsCons;
	}

	// Property accessors

	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	public CusCorCus getCusCorCus() {
		return this.cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}


	public String getConName() {
		return this.conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public String getConSex() {
		return this.conSex;
	}

	public void setConSex(String conSex) {
		this.conSex = conSex;
	}

	public String getConDep() {
		return this.conDep;
	}

	public void setConDep(String conDep) {
		this.conDep = conDep;
	}

	public String getConPos() {
		return this.conPos;
	}

	public void setConPos(String conPos) {
		this.conPos = conPos;
	}

	public String getConLev() {
		return this.conLev;
	}

	public void setConLev(String conLev) {
		this.conLev = conLev;
	}

	public String getConPhone() {
		return this.conPhone;
	}

	public void setConPhone(String conPhone) {
		this.conPhone = conPhone;
	}

	public String getConWorkPho() {
		return this.conWorkPho;
	}

	public void setConWorkPho(String conWorkPho) {
		this.conWorkPho = conWorkPho;
	}

	public String getConHomePho() {
		return this.conHomePho;
	}

	public void setConHomePho(String conHomePho) {
		this.conHomePho = conHomePho;
	}

	public String getConFex() {
		return this.conFex;
	}

	public void setConFex(String conFex) {
		this.conFex = conFex;
	}

	public String getConZipCode() {
		return this.conZipCode;
	}

	public void setConZipCode(String conZipCode) {
		this.conZipCode = conZipCode;
	}

	public String getConEmail() {
		return this.conEmail;
	}

	public void setConEmail(String conEmail) {
		this.conEmail = conEmail;
	}

	public String getConQq() {
		return this.conQq;
	}

	public void setConQq(String conQq) {
		this.conQq = conQq;
	}

	public String getConMsn() {
		return this.conMsn;
	}

	public void setConMsn(String conMsn) {
		this.conMsn = conMsn;
	}

	public String getConAdd() {
		return this.conAdd;
	}

	public void setConAdd(String conAdd) {
		this.conAdd = conAdd;
	}

	public String getConOthLink() {
		return this.conOthLink;
	}

	public void setConOthLink(String conOthLink) {
		this.conOthLink = conOthLink;
	}


	public String getConHob() {
		return this.conHob;
	}

	public void setConHob(String conHob) {
		this.conHob = conHob;
	}

	public String getConTaboo() {
		return this.conTaboo;
	}

	public void setConTaboo(String conTaboo) {
		this.conTaboo = conTaboo;
	}

	public String getConEdu() {
		return this.conEdu;
	}

	public void setConEdu(String conEdu) {
		this.conEdu = conEdu;
	}

	public String getConPhoto() {
		return this.conPhoto;
	}

	public void setConPhoto(String conPhoto) {
		this.conPhoto = conPhoto;
	}

	public String getConRemark() {
		return StringFormat.toBlank(this.conRemark);
	}

	public void setConRemark(String conRemark) {
		this.conRemark = conRemark;
	}

	public Date getConCreDate() {
		return this.conCreDate;
	}

	public void setConCreDate(Date conCreDate) {
		this.conCreDate = conCreDate;
	}

	public Date getConModDate() {
		return this.conModDate;
	}

	public void setConModDate(Date conModDate) {
		this.conModDate = conModDate;
	}


	public Date getConBir() {
		return conBir;
	}

	public void setConBir(Date conBir) {
		this.conBir = conBir;
	}

	public String getConInpUser() {
		return conInpUser;
	}

	public void setConInpUser(String conInpUser) {
		this.conInpUser = conInpUser;
	}

	public String getConUpdUser() {
		return conUpdUser;
	}

	public void setConUpdUser(String conUpdUser) {
		this.conUpdUser = conUpdUser;
	}

	public String getConType() {
		return conType;
	}

	public void setConType(String conType) {
		this.conType = conType;
	}



	public Set getMemDates() {
		return memDates;
	}

	public void setMemDates(Set memDates) {
		this.memDates = memDates;
	}

	public String getConIsCons() {
		return conIsCons;
	}

	public void setConIsCons(String conIsCons) {
		this.conIsCons = conIsCons;
	}

}