package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 订单实体 <br>
 */
public class SalOrdCon implements java.io.Serializable {

	// Fields

	private String sodCode;
	private CusCorCus cusCorCus;
	private TypeList salOrderType;
	private TypeList salOrderSou;
	private SalEmp salEmp;
	private String sodTil;
	private String sodNum;
	private Double sodSumMon;
	private Double sodPaidMon;
	private Date sodInpDate;
	private String sodIsfail;
	private String sodRemark;
	private Date sodChangeDate;
	private String sodPaidMethod;
	private Date sodConDate;
	private String sodChangeUser;
	private String sodInpCode;
	private Date sodAppDate;
	private String sodAppMan;
	private String sodAppDesc;
	private String sodAppIsok;
	private TypeList sodShipState;
	private Date sodDeadline;
	private Date sodEndDate;
	private Date sodOrdDate;
	private Date sodComiteDate;
	private String sodCusCon;
	private String sodContent;
	private Set wmsWarOuts = new HashSet(0);
	private Set ROrdPros = new HashSet(0);
	private Set salPaidPasts = new HashSet(0);
	private Set salPaidPlans = new HashSet(0);
	private Set attachments = new HashSet(0);

	// Constructors

	/** default constructor */
	public SalOrdCon() {

	}

	/** minimal constructor */
	public SalOrdCon(String sodCode) {
		this.sodCode = sodCode;
	}

	/** full constructor */
	public SalOrdCon(String sodCode, CusCorCus cusCorCus, TypeList salOrderType,
			TypeList salOrderSou, String sodTil,
			String sodNum, Double sodSumMon, Double sodPaidMon,
			Date sodInpDate, String sodIsfail, String sodRemark,
			TypeList sodShipState, Date sodDeadline, Date sodEndDate,
			Date sodOrdDate, String sodCusCon, Date sodChangeDate,
			SalEmp salEmp, String sodPaidMethod, Date sodConDate,
			String sodChangeUser, String sodInpCode, String sodContent,
			Set wmsWarOuts, Set ROrdPros, Set salPaidPasts, Set salPaidPlans,
			Set attachments,Date sodComiteDate) {
		this.sodCode = sodCode;
		this.cusCorCus = cusCorCus;
		this.salOrderType = salOrderType;
		this.salOrderSou = salOrderSou;
		this.sodTil = sodTil;
		this.sodNum = sodNum;
		this.sodSumMon = sodSumMon;
		this.sodPaidMon = sodPaidMon;
		this.sodInpDate = sodInpDate;
		this.sodIsfail = sodIsfail;
		this.sodRemark = sodRemark;
		this.sodChangeDate = sodChangeDate;
		this.sodPaidMethod = sodPaidMethod;
		this.sodConDate = sodConDate;
		this.sodChangeUser = sodChangeUser;
		this.sodInpCode = sodInpCode;
		this.sodShipState = sodShipState;
		this.sodDeadline = sodDeadline;
		this.sodEndDate = sodEndDate;
		this.sodOrdDate = sodOrdDate;
		this.sodCusCon = sodCusCon;
		this.wmsWarOuts = wmsWarOuts;
		this.ROrdPros = ROrdPros;
		this.salPaidPasts = salPaidPasts;
		this.salPaidPlans = salPaidPlans;
		this.attachments = attachments;
		this.sodContent = sodContent;
		this.salEmp=salEmp;
		this.sodComiteDate = sodComiteDate;
	}

	// Property accessors

	public String getSodCode() {
		return this.sodCode;
	}

	public void setSodCode(String sodCode) {
		this.sodCode = sodCode;
	}

	public CusCorCus getCusCorCus() {
		return this.cusCorCus;
	}

	public void setCusCorCus(CusCorCus cusCorCus) {
		this.cusCorCus = cusCorCus;
	}
	
	public Date getSodComiteDate() {
		return this.sodComiteDate;
	}

	public void setSodComiteDate(Date sodComiteDate) {
		this.sodComiteDate = sodComiteDate;
	}
	
 
	public TypeList getSalOrderType() {
		return this.salOrderType;
	}

	public void setSalOrderType(TypeList salOrderType) {
		this.salOrderType = salOrderType;
	}

	public String getSodTil() {
		return this.sodTil;
	}

	public void setSodTil(String sodTil) {
		this.sodTil = sodTil;
	}

	public Double getSodSumMon() {
		return this.sodSumMon;
	}

	public void setSodSumMon(Double sodSumMon) {
		this.sodSumMon = sodSumMon;
	}

	public Date getSodInpDate() {
		return this.sodInpDate;
	}

	public void setSodInpDate(Date sodInpDate) {
		this.sodInpDate = sodInpDate;
	}

	public String getSodIsfail() {
		return this.sodIsfail;
	}

	public void setSodIsfail(String sodIsfail) {
		this.sodIsfail = sodIsfail;
	}

	public String getSodRemark() {
		return StringFormat.toBlank(this.sodRemark);
	}

	public void setSodRemark(String sodRemark) {
		this.sodRemark = sodRemark;
	}

	public Date getSodChangeDate() {
		return this.sodChangeDate;
	}

	public void setSodChangeDate(Date sodChangeDate) {
		this.sodChangeDate = sodChangeDate;
	}

	public String getSodPaidMethod() {
		return this.sodPaidMethod;
	}

	public void setSodPaidMethod(String sodPaidMethod) {
		this.sodPaidMethod = sodPaidMethod;
	}
	
	public Date getSodConDate() {
		return this.sodConDate;
	}

	public void setSodConDate(Date sodConDate) {
		this.sodConDate = sodConDate;
	}

	public Set getWmsWarOuts() {
		return this.wmsWarOuts;
	}

	public void setWmsWarOuts(Set wmsWarOuts) {
		this.wmsWarOuts = wmsWarOuts;
	}

	public Set getROrdPros() {
		return this.ROrdPros;
	}

	public void setROrdPros(Set ROrdPros) {
		this.ROrdPros = ROrdPros;
	}

	public Set getAttachments() {
		return attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	public Set getSalPaidPasts() {
		return salPaidPasts;
	}

	public void setSalPaidPasts(Set salPaidPasts) {
		this.salPaidPasts = salPaidPasts;
	}

	public Set getSalPaidPlans() {
		return salPaidPlans;
	}

	public void setSalPaidPlans(Set salPaidPlans) {
		this.salPaidPlans = salPaidPlans;
	}

	public String getSodChangeUser() {
		return sodChangeUser;
	}

	public void setSodChangeUser(String sodChangeUser) {
		this.sodChangeUser = sodChangeUser;
	}

	public String getSodInpCode() {
		return sodInpCode;
	}

	public void setSodInpCode(String sodInpCode) {
		this.sodInpCode = sodInpCode;
	}

	public String getSodNum() {
		return sodNum;
	}

	public void setSodNum(String sodNum) {
		this.sodNum = sodNum;
	}

	public Double getSodPaidMon() {
		return sodPaidMon;
	}

	public void setSodPaidMon(Double sodPaidMon) {
		this.sodPaidMon = sodPaidMon;
	}

	public Date getSodAppDate() {
		return sodAppDate;
	}

	public void setSodAppDate(Date sodAppDate) {
		this.sodAppDate = sodAppDate;
	}

	public String getSodAppMan() {
		return sodAppMan;
	}

	public void setSodAppMan(String sodAppMan) {
		this.sodAppMan = sodAppMan;
	}

	public String getSodAppDesc() {
		return StringFormat.toBlank(sodAppDesc);
	}

	public void setSodAppDesc(String sodAppDesc) {
		this.sodAppDesc = sodAppDesc;
	}

	public String getSodAppIsok() {
		return sodAppIsok;
	}

	public void setSodAppIsok(String sodAppIsok) {
		this.sodAppIsok = sodAppIsok;
	}


	public TypeList getSodShipState() {
		return sodShipState;
	}

	public void setSodShipState(TypeList sodShipState) {
		this.sodShipState = sodShipState;
	}

	public Date getSodDeadline() {
		return sodDeadline;
	}

	public void setSodDeadline(Date sodDeadline) {
		this.sodDeadline = sodDeadline;
	}

	public Date getSodEndDate() {
		return sodEndDate;
	}

	public void setSodEndDate(Date sodEndDate) {
		this.sodEndDate = sodEndDate;
	}

	public Date getSodOrdDate() {
		return sodOrdDate;
	}

	public void setSodOrdDate(Date sodOrdDate) {
		this.sodOrdDate = sodOrdDate;
	}

	public String getSodCusCon() {
		return sodCusCon;
	}

	public void setSodCusCon(String sodCusCon) {
		this.sodCusCon = sodCusCon;
	}

	public String getSodContent() {
		return StringFormat.toBlank(sodContent);
	}

	public void setSodContent(String sodContent) {
		this.sodContent = sodContent;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public TypeList getSalOrderSou() {
		return salOrderSou;
	}

	public void setSalOrderSou(TypeList salOrderSou) {
		this.salOrderSou = salOrderSou;
	}

}