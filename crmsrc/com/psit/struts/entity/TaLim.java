package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 销售工作执行人实体 <br>
 * create_date: Aug 27, 2010,3:27:00 PM<br>
 * @author csg
 */

public class TaLim implements java.io.Serializable {

	// Fields

	private Long taLimId;
	private SalTask salTask;
	private SalEmp salEmp;
	private String taIsdel;
	private Date taFinDate;
	private String taIsfin;
	private String taDesc;
	private String taName;
	private Set attachments = new HashSet(0);
	// Constructors


	/** default constructor */
	public TaLim() {
	}
	/** mini constructor */
	public TaLim(Long taLimId) {
		this.taLimId=taLimId;
	}
	/** full constructor */
	public TaLim(SalTask salTask, SalEmp salEmp, String taIsdel,String taDesc,String taName,Set attachments) {
		this.salTask = salTask;
		this.salEmp = salEmp;
		this.taIsdel = taIsdel;
		this.taDesc = taDesc;
		this.taName = taName;
		this.attachments = attachments;
	}

	// Property accessors

	public Long getTaLimId() {
		return this.taLimId;
	}

	public void setTaLimId(Long taLimId) {
		this.taLimId = taLimId;
	}

	public SalTask getSalTask() {
		return this.salTask;
	}

	public void setSalTask(SalTask salTask) {
		this.salTask = salTask;
	}

	public String getTaIsdel() {
		return this.taIsdel;
	}

	public void setTaIsdel(String taIsdel) {
		this.taIsdel = taIsdel;
	}

	public String getTaIsfin() {
		return taIsfin;
	}

	public void setTaIsfin(String taIsfin) {
		this.taIsfin = taIsfin;
	}

	public String getTaDesc() {
		return StringFormat.toBlank(taDesc);
	}

	public void setTaDesc(String taDesc) {
		this.taDesc = taDesc;
	}

	public Set getAttachments() {
		return attachments;
	}

	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	
	public Date getTaFinDate() {
		return taFinDate;
	}

	public void setTaFinDate(Date taFinDate) {
		this.taFinDate = taFinDate;
	}
	public String getTaName() {
		return taName;
	}
	public void setTaName(String taName) {
		this.taName = taName;
	}
	public SalEmp getSalEmp() {
		return salEmp;
	}
	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

}