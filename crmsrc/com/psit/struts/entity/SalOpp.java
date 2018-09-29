
package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 销售机会实体 <br>
 */

public class SalOpp implements java.io.Serializable {

	// Fields

	private Long oppId;
	private CusCorCus cusCorCus;
	private SalEmp salEmp1;
	private SalEmp salEmp;
	private String oppTitle;
	private String oppLev;
	private Date oppExeDate;
	private String oppDes;
	private String oppRemark;
	private Date oppInsDate;
	private Date oppFindDate;
	private String oppIsexe;
	private String oppState;
	private Set salPras = new HashSet(0);
	private Set quotes = new HashSet(0);
	private String oppInpUser;
	private String oppUpdUser;
	private Date oppUpdDate;
	private Date oppSignDate;
	private Double oppMoney;
	private TypeList oppStage;
	private String oppPossible;
	private String oppStaRemark;
	private Date oppStaUpdate;
	private String oppStaLog;
	private String oppDayCount;
    private String oppIsDel;
	// Constructors

	/** default constructor */
	public SalOpp() {
	}
	public SalOpp(Long oppId) 
	{
		this.oppId=oppId;
	}
	/** full constructor */
	public SalOpp(SalEmp salEmp1,CusCorCus cusCorCus,String oppIsDel,
			String oppTitle, String oppLev, Date oppExeDate,SalEmp salEmp,
			String oppDes, String oppRemark, Date oppInsDate, String oppIsexe,
			String oppState,Set salPras,Set quotes,  String oppInpUser,
			String oppUpdUser, Date oppUpdDate,Date oppSignDate,Date oppFindDate,Double oppMoney,
			TypeList oppStage,String oppPossible,String oppStaRemark,Date oppStaUpdate,
			String oppStaLog) {
		this.cusCorCus = cusCorCus;
		this.salEmp1=salEmp1;
		this.oppTitle = oppTitle;
		this.oppLev = oppLev;
		this.oppExeDate = oppExeDate;
		this.oppDes = oppDes;
		this.oppRemark = oppRemark;
		this.oppInsDate = oppInsDate;
		this.oppIsexe = oppIsexe;
		this.oppState = oppState;
		this.salPras=salPras;
		this.quotes=quotes;
		this.oppInpUser = oppInpUser;
		this.oppUpdUser = oppUpdUser;
		this.oppUpdDate = oppUpdDate;
		this.oppFindDate=oppFindDate;
		this.oppSignDate=oppSignDate;
		this.oppMoney=oppMoney;
		this.oppStage=oppStage;
		this.oppPossible=oppPossible;
		this.oppStaRemark=oppStaRemark;
		this.oppStaUpdate=oppStaUpdate;
		this.oppStaLog=oppStaLog;
		this.oppIsDel=oppIsDel;
		this.salEmp=salEmp;
	}

	// Property accessors

	public Long getOppId() {
		return this.oppId;
	}

	public void setOppId(Long oppId) {
		this.oppId = oppId;
	}

	public CusCorCus getCusCorCus() {
		return this.cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}

	public String getOppTitle() {
		return this.oppTitle;
	}

	public void setOppTitle(String oppTitle) {
		this.oppTitle = oppTitle;
	}

	public String getOppLev() {
		return this.oppLev;
	}

	public void setOppLev(String oppLev) {
		this.oppLev = oppLev;
	}

	public Date getOppExeDate() {
		return this.oppExeDate;
	}

	public void setOppExeDate(Date oppExeDate) {
		this.oppExeDate = oppExeDate;
	}

	public String getOppDes() {
		return StringFormat.toBlank(this.oppDes);
	}

	public void setOppDes(String oppDes) {
		this.oppDes = oppDes;
	}

	public String getOppRemark() {
		return StringFormat.toBlank(this.oppRemark);
	}

	public void setOppRemark(String oppRemark) {
		this.oppRemark = oppRemark;
	}

	public Date getOppInsDate() {
		return this.oppInsDate;
	}

	public void setOppInsDate(Date oppInsDate) {
		this.oppInsDate = oppInsDate;
	}

	public String getOppIsexe() {
		return this.oppIsexe;
	}

	public void setOppIsexe(String oppIsexe) {
		this.oppIsexe = oppIsexe;
	}

	public String getOppState() {
		return this.oppState;
	}

	public void setOppState(String oppState) {
		this.oppState = oppState;
	}

	public Set getSalPras() {
		return salPras;
	}

	public void setSalPras(Set salPras) {
		this.salPras = salPras;
	}
	public Set getQuotes() {
		return quotes;
	}
	public void setQuotes(Set quotes) {
		this.quotes = quotes;
	}
	public String getOppInpUser() {
		return oppInpUser;
	}
	public void setOppInpUser(String oppInpUser) {
		this.oppInpUser = oppInpUser;
	}
	public String getOppUpdUser() {
		return oppUpdUser;
	}
	public void setOppUpdUser(String oppUpdUser) {
		this.oppUpdUser = oppUpdUser;
	}
	public Date getOppUpdDate() {
		return oppUpdDate;
	}
	public void setOppUpdDate(Date oppUpdDate) {
		this.oppUpdDate = oppUpdDate;
	}
	public String getOppIsDel() {
		return oppIsDel;
	}
	public void setOppIsDel(String oppIsDel) {
		this.oppIsDel = oppIsDel;
	}
	public Date getOppSignDate() {
		return oppSignDate;
	}
	public void setOppSignDate(Date oppSignDate) {
		this.oppSignDate = oppSignDate;
	}
	public Double getOppMoney() {
		return oppMoney;
	}
	public void setOppMoney(Double oppMoney) {
		this.oppMoney = oppMoney;
	}
	public String getOppPossible() {
		return oppPossible;
	}
	public void setOppPossible(String oppPossible) {
		this.oppPossible = oppPossible;
	}
	public String getOppStaRemark() {
		return StringFormat.toBlank(oppStaRemark);
	}
	public void setOppStaRemark(String oppStaRemark) {
		this.oppStaRemark = oppStaRemark;
	}
	public Date getOppStaUpdate() {
		return oppStaUpdate;
	}
	public void setOppStaUpdate(Date oppStaUpdate) {
		this.oppStaUpdate = oppStaUpdate;
	}
	public String getOppStaLog() {
		return oppStaLog;
	}
	public void setOppStaLog(String oppStaLog) {
		this.oppStaLog = oppStaLog;
	}
	public String getOppDayCount() {
		return oppDayCount;
	}
	public void setOppDayCount(String oppDayCount) {
		this.oppDayCount = oppDayCount;
	}
	public TypeList getOppStage() {
		return oppStage;
	}
	public void setOppStage(TypeList oppStage) {
		this.oppStage = oppStage;
	}
	public Date getOppFindDate() {
		return oppFindDate;
	}
	public void setOppFindDate(Date oppFindDate) {
		this.oppFindDate = oppFindDate;
	}
	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}
	public SalEmp getSalEmp1() {
		return salEmp1;
	}
	public void setSalEmp1(SalEmp salEmp1) {
		this.salEmp1 = salEmp1;
	}

}