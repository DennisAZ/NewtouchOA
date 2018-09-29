package com.psit.struts.entity;

import java.util.Date;

import com.psit.struts.util.format.StringFormat;

/**
 * 报告接收人实体 <br>
 */

public class RRepLim implements java.io.Serializable {

	// Fields

	private Long rrlId;
	private SalEmp salEmp;
	private Report report;
	private String rrlContent;
	private String rrlIsappro;
	private String rrlIsdel;
	private String rrlIsView;
	private String rrlIsAllAppro;
	private Integer rrlAppOrder;
	private Date rrlOpproDate;
	private Date rrlDate;
    private String rrlRecUser;
	// Constructors

	/** default constructor */
	public RRepLim() {
	}

	/** full constructor */
	public RRepLim(SalEmp salEmp, Report report,String rrlContent,String rrlIsappro,Integer rrlAppOrder,
			String rrlIsdel,Date rrlOpproDate, Date rrlDate,String rrlIsView,String rrlIsAllAppro,String rrlRecUser) {
		this.salEmp = salEmp;
		this.report = report;
		this.rrlContent=rrlContent;
		this.rrlIsappro=rrlIsappro;
		this.rrlIsdel=rrlIsdel;
		this.rrlOpproDate=rrlOpproDate;
		this.rrlDate = rrlDate;
		this.rrlAppOrder=rrlAppOrder;
		this.rrlIsView=rrlIsView;
		this.rrlIsAllAppro=rrlIsAllAppro;
		this.rrlRecUser=rrlRecUser;
	}

	// Property accessors

	public Long getRrlId() {
		return this.rrlId;
	}

	public void setRrlId(Long rrlId) {
		this.rrlId = rrlId;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public Report getReport() {
		return this.report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Date getRrlDate() {
		return this.rrlDate;
	}

	public void setRrlDate(Date rrlDate) {
		this.rrlDate = rrlDate;
	}

	public String getRrlContent() {
		return StringFormat.toBlank(rrlContent);
	}

	public void setRrlContent(String rrlContent) {
		this.rrlContent = rrlContent;
	}

	public Date getRrlOpproDate() {
		return rrlOpproDate;
	}

	public void setRrlOpproDate(Date rrlOpproDate) {
		this.rrlOpproDate = rrlOpproDate;
	}

	public String getRrlIsappro() {
		return rrlIsappro;
	}

	public void setRrlIsappro(String rrlIsappro) {
		this.rrlIsappro = rrlIsappro;
	}

	public String getRrlIsdel() {
		return rrlIsdel;
	}

	public void setRrlIsdel(String rrlIsdel) {
		this.rrlIsdel = rrlIsdel;
	}

	public String getRrlIsView() {
		return rrlIsView;
	}

	public void setRrlIsView(String rrlIsView) {
		this.rrlIsView = rrlIsView;
	}

	public String getRrlIsAllAppro() {
		return rrlIsAllAppro;
	}

	public void setRrlIsAllAppro(String rrlIsAllAppro) {
		this.rrlIsAllAppro = rrlIsAllAppro;
	}

	public Integer getRrlAppOrder() {
		return rrlAppOrder;
	}

	public void setRrlAppOrder(Integer rrlAppOrder) {
		this.rrlAppOrder = rrlAppOrder;
	}

	public String getRrlRecUser() {
		return rrlRecUser;
	}

	public void setRrlRecUser(String rrlRecUser) {
		this.rrlRecUser = rrlRecUser;
	}

}