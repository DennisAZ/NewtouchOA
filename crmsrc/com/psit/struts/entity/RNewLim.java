package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 新闻公告接收人实体 <br>
 *
 * create_date: Aug 27, 2010,3:30:15 PM<br>
 * @author zjr
 */

public class RNewLim implements java.io.Serializable {

	// Fields

	private Long rnlId;
	private SalEmp salEmp;
	private News news;
	private Date rnlDate;

	// Constructors

	/** default constructor */
	public RNewLim() {
	}

	/** full constructor */
	public RNewLim(SalEmp salEmp, News news, Date rnlDate) {
		this.salEmp = salEmp;
		this.news = news;
		this.rnlDate = rnlDate;
	}

	// Property accessors

	public Long getRnlId() {
		return this.rnlId;
	}

	public void setRnlId(Long rnlId) {
		this.rnlId = rnlId;
	}

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

	public News getNews() {
		return this.news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Date getRnlDate() {
		return this.rnlDate;
	}

	public void setRnlDate(Date rnlDate) {
		this.rnlDate = rnlDate;
	}

}