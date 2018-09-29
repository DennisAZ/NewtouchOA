package com.psit.struts.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.psit.struts.util.format.StringFormat;

/**
 * 商品实体 <br>
 */

public class WmsProduct implements java.io.Serializable {

	// Fields
	private String wprId;
	public String getWprId() {
		return wprId;
	}
	public void setWprId(String wprId) {
		this.wprId = wprId;
	}
	private String wprCode;
	private String proType;
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	private WmsProType wmsProType;
	private TypeList typeList;
	private String wprName;
	private String wprModel;
	private String wprProvider;
	private Integer wprUpLim;
	private Integer wprLowLim;
	private Double wprCostPrc;
	private Double wprSalePrc;
	private Float wprNormalPer;
	private Float wprOverPer;
	private Float wprLowPer;
	private String wprPic;
	private Date wprCreDate;
	private Date wprEditDate;
	private String wprDesc;
	private String wprRemark;
	private String wprStates;
	private String wprRange;
	private String wprTechnology;
	private String wprProblem;
	private String wprIsdel;
	private String wprCuserCode;
	private String wprEuserCode;
	private String wprIscount;
	private ERPProductUnit productUnit;
	public ERPProductUnit getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(ERPProductUnit productUnit) {
		this.productUnit = productUnit;
	}
	private Set ProdTranss = new HashSet(0);
	private Set RWmsWmses = new HashSet(0);
	private Set RWoutPros = new HashSet(0);
	private Set RWinPros = new HashSet(0);
	private Set RStroPros = new HashSet(0);
	private Set ROrdPros = new HashSet(0);
	private Set RWmsChanges = new HashSet(0);
	private Set WmsLines = new HashSet(0);
	private Set attachments = new HashSet(0);
	// Constructors

	/** default constructor */
	public WmsProduct() {
	}
	public WmsProduct(String wprId) {
		this.wprId=wprId;
	}
	/** full constructor */
	public WmsProduct(WmsProType wmsProType, TypeList typeList, String wprName,
			String wprModel, String wprProvider, Integer wprUpLim,
			Integer wprLowLim, Double wprCostPrc, Double wprSalePrc,
			String wprPic, Date wprCreDate, Date wprEditDate, String wprDesc,
			String wprRemark, String wprStates, String wprRange,
			String wprTechnology, String wprProblem, String wprIsdel,
			String wprCuserCode, String wprEuserCode, Set RWmsWmses,
			Set RWoutPros, Set RWinPros, Set RStroPros,
			Set ROrdPros, Set RWmsChanges, Set WmsLines, Set attachments) {
		this.wmsProType = wmsProType;
		this.typeList = typeList;
		this.wprName = wprName;
		this.wprModel = wprModel;
		this.wprProvider = wprProvider;
		this.wprUpLim = wprUpLim;
		this.wprLowLim = wprLowLim;
		this.wprCostPrc = wprCostPrc;
		this.wprSalePrc = wprSalePrc;
		this.wprPic = wprPic;
		this.wprCreDate = wprCreDate;
		this.wprEditDate = wprEditDate;
		this.wprDesc = wprDesc;
		this.wprRemark = wprRemark;
		this.wprStates = wprStates;
		this.wprRange = wprRange;
		this.wprTechnology = wprTechnology;
		this.wprProblem = wprProblem;
		this.wprIsdel = wprIsdel;
		this.wprCuserCode = wprCuserCode;
		this.wprEuserCode = wprEuserCode;
		this.RWmsWmses = RWmsWmses;
		this.RWoutPros = RWoutPros;
		this.RWinPros = RWinPros;
		this.RStroPros = RStroPros;
		this.ROrdPros = ROrdPros;
		this.RWmsChanges = RWmsChanges;
		this.WmsLines = WmsLines;
		this.attachments = attachments;
	}

	// Property accessors

	public String getWprCode() {
		return this.wprCode;
	}

	public void setWprCode(String wprCode) {
		this.wprCode = wprCode;
	}

	public WmsProType getWmsProType() {
		return this.wmsProType;
	}

	public void setWmsProType(WmsProType wmsProType) {
		this.wmsProType = wmsProType;
	}

	public TypeList getTypeList() {
		return this.typeList;
	}

	public void setTypeList(TypeList typeList) {
		this.typeList = typeList;
	}

	public String getWprName() {
		return this.wprName;
	}

	public void setWprName(String wprName) {
		this.wprName = wprName;
	}

	public String getWprModel() {
		return this.wprModel;
	}

	public void setWprModel(String wprModel) {
		this.wprModel = wprModel;
	}

	public String getWprProvider() {
		return this.wprProvider;
	}

	public void setWprProvider(String wprProvider) {
		this.wprProvider = wprProvider;
	}

	public Integer getWprUpLim() {
		return this.wprUpLim;
	}

	public void setWprUpLim(Integer wprUpLim) {
		this.wprUpLim = wprUpLim;
	}

	public Integer getWprLowLim() {
		return this.wprLowLim;
	}

	public void setWprLowLim(Integer wprLowLim) {
		this.wprLowLim = wprLowLim;
	}

	public Double getWprCostPrc() {
		return this.wprCostPrc;
	}

	public void setWprCostPrc(Double wprCostPrc) {
		this.wprCostPrc = wprCostPrc;
	}

	public Double getWprSalePrc() {
		return this.wprSalePrc;
	}

	public void setWprSalePrc(Double wprSalePrc) {
		this.wprSalePrc = wprSalePrc;
	}

	public String getWprPic() {
		return this.wprPic;
	}

	public void setWprPic(String wprPic) {
		this.wprPic = wprPic;
	}

	public Date getWprCreDate() {
		return this.wprCreDate;
	}

	public void setWprCreDate(Date wprCreDate) {
		this.wprCreDate = wprCreDate;
	}

	public Date getWprEditDate() {
		return this.wprEditDate;
	}

	public void setWprEditDate(Date wprEditDate) {
		this.wprEditDate = wprEditDate;
	}

	public String getWprDesc() {
		return StringFormat.toBlank(this.wprDesc);
	}

	public void setWprDesc(String wprDesc) {
		this.wprDesc = wprDesc;
	}

	public String getWprRemark() {
		return StringFormat.toBlank(this.wprRemark);
	}

	public void setWprRemark(String wprRemark) {
		this.wprRemark = wprRemark;
	}

	public String getWprStates() {
		return this.wprStates;
	}

	public void setWprStates(String wprStates) {
		this.wprStates = wprStates;
	}

	public String getWprRange() {
		return this.wprRange;
	}

	public void setWprRange(String wprRange) {
		this.wprRange = wprRange;
	}

	public String getWprTechnology() {
		return this.wprTechnology;
	}

	public void setWprTechnology(String wprTechnology) {
		this.wprTechnology = wprTechnology;
	}

	public String getWprProblem() {
		return this.wprProblem;
	}

	public void setWprProblem(String wprProblem) {
		this.wprProblem = wprProblem;
	}

	public Set getRWmsWmses() {
		return this.RWmsWmses;
	}

	public void setRWmsWmses(Set RWmsWmses) {
		this.RWmsWmses = RWmsWmses;
	}

	public Set getRWoutPros() {
		return this.RWoutPros;
	}

	public void setRWoutPros(Set RWoutPros) {
		this.RWoutPros = RWoutPros;
	}

	public Set getRWinPros() {
		return this.RWinPros;
	}

	public void setRWinPros(Set RWinPros) {
		this.RWinPros = RWinPros;
	}

	public Set getRStroPros() {
		return this.RStroPros;
	}

	public void setRStroPros(Set RStroPros) {
		this.RStroPros = RStroPros;
	}

	public Set getROrdPros() {
		return this.ROrdPros;
	}

	public void setROrdPros(Set ROrdPros) {
		this.ROrdPros = ROrdPros;
	}

	public String getWprIsdel() {
		return wprIsdel;
	}

	public void setWprIsdel(String wprIsdel) {
		this.wprIsdel = wprIsdel;
	}

	public String getWprCuserCode() {
		return wprCuserCode;
	}

	public void setWprCuserCode(String wprCuserCode) {
		this.wprCuserCode = wprCuserCode;
	}

	public String getWprEuserCode() {
		return wprEuserCode;
	}

	public void setWprEuserCode(String wprEuserCode) {
		this.wprEuserCode = wprEuserCode;
	}


	public String getWprIscount() {
		return wprIscount;
	}

	public void setWprIscount(String wprIscount) {
		this.wprIscount = wprIscount;
	}
	public Set getRWmsChanges() {
		return RWmsChanges;
	}
	public void setRWmsChanges(Set wmsChanges) {
		RWmsChanges = wmsChanges;
	}
	public Set getWmsLines() {
		return WmsLines;
	}
	public void setWmsLines(Set wmsLines) {
		WmsLines = wmsLines;
	}
	public Set getAttachments() {
		return attachments;
	}
	public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
	public Float getWprNormalPer() {
		return wprNormalPer;
	}
	public void setWprNormalPer(Float wprNormalPer) {
		this.wprNormalPer = wprNormalPer;
	}
	public Float getWprOverPer() {
		return wprOverPer;
	}
	public void setWprOverPer(Float wprOverPer) {
		this.wprOverPer = wprOverPer;
	}
	public Float getWprLowPer() {
		return wprLowPer;
	}
	public void setWprLowPer(Float wprLowPer) {
		this.wprLowPer = wprLowPer;
	}
	public Set getProdTranss() {
		return ProdTranss;
	}
	public void setProdTranss(Set prodTranss) {
		ProdTranss = prodTranss;
	}

}