package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PurOrd implements java.io.Serializable {

	// Fields

	private Long puoId;
	private String puoCode;
	private Date puoPurDate;
	private Supplier puoSup;
	private TypeList puoType;
	private Double puoM;
	private Double puoPaidM;
	private SalEmp puoEmp;
	private String puoContent;
	private String puoIsEnd;
	private String puoRemark;
	private String puoCreMan;
	private Date puoCreTime;
	private Date puoUpdTime;
	private String puoUpdMan;
	private Set RPuoPros = new HashSet();
	
	public static final String T_PUO_TYP="puoType";
	
	// Constructors

	/** default constructor */
	public PurOrd() {
	}

	public PurOrd(Long puoId){
		this.puoId = puoId;
	}
	/** full constructor */
	public PurOrd(String puoCode, Date puoPurDate, Long puoSupId,
			Long puoTypeId, Double puoM, Double puoPaidM, Long puoSeNo,
			String puoContent, String puoIsEnd, String puoRemark,
			String puoCreMan, Date puoCreTime, Date puoUpdTime, String puoUpdMan,Set RPuoPros) {
		this.puoCode = puoCode;
		this.puoPurDate = puoPurDate;
		this.puoM = puoM;
		this.puoPaidM = puoPaidM;
		this.puoContent = puoContent;
		this.puoIsEnd = puoIsEnd;
		this.puoRemark = puoRemark;
		this.puoCreMan = puoCreMan;
		this.puoCreTime = puoCreTime;
		this.puoUpdTime = puoUpdTime;
		this.puoUpdMan = puoUpdMan;
		this.RPuoPros = RPuoPros;
	}

	// Property accessors

	public Long getPuoId() {
		return this.puoId;
	}

	public void setPuoId(Long puoId) {
		this.puoId = puoId;
	}

	public String getPuoCode() {
		return this.puoCode;
	}

	public void setPuoCode(String puoCode) {
		this.puoCode = puoCode;
	}

	public Date getPuoPurDate() {
		return this.puoPurDate;
	}

	public void setPuoPurDate(Date puoPurDate) {
		this.puoPurDate = puoPurDate;
	}

	public Double getPuoM() {
		return this.puoM;
	}

	public void setPuoM(Double puoM) {
		this.puoM = puoM;
	}

	public Double getPuoPaidM() {
		return this.puoPaidM;
	}

	public void setPuoPaidM(Double puoPaidM) {
		this.puoPaidM = puoPaidM;
	}

	public String getPuoContent() {
		return this.puoContent;
	}

	public void setPuoContent(String puoContent) {
		this.puoContent = puoContent;
	}

	public String getPuoIsEnd() {
		return this.puoIsEnd;
	}

	public void setPuoIsEnd(String puoIsEnd) {
		this.puoIsEnd = puoIsEnd;
	}

	public String getPuoRemark() {
		return this.puoRemark;
	}

	public void setPuoRemark(String puoRemark) {
		this.puoRemark = puoRemark;
	}

	public String getPuoCreMan() {
		return this.puoCreMan;
	}

	public void setPuoCreMan(String puoCreMan) {
		this.puoCreMan = puoCreMan;
	}

	public Date getPuoCreTime() {
		return this.puoCreTime;
	}

	public void setPuoCreTime(Date puoCreTime) {
		this.puoCreTime = puoCreTime;
	}

	public Date getPuoUpdTime() {
		return this.puoUpdTime;
	}

	public void setPuoUpdTime(Date puoUpdTime) {
		this.puoUpdTime = puoUpdTime;
	}

	public String getPuoUpdMan() {
		return this.puoUpdMan;
	}

	public void setPuoUpdMan(String puoUpdMan) {
		this.puoUpdMan = puoUpdMan;
	}

	public Supplier getPuoSup() {
		return puoSup;
	}

	public void setPuoSup(Supplier puoSup) {
		this.puoSup = puoSup;
	}

	public TypeList getPuoType() {
		return puoType;
	}

	public void setPuoType(TypeList puoType) {
		this.puoType = puoType;
	}

	public SalEmp getPuoEmp() {
		return puoEmp;
	}

	public void setPuoEmp(SalEmp puoEmp) {
		this.puoEmp = puoEmp;
	}

	public Set getRPuoPros() {
		return RPuoPros;
	}

	public void setRPuoPros(Set puoPros) {
		RPuoPros = puoPros;
	}

}