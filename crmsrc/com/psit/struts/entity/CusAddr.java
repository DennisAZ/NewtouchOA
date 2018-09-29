package com.psit.struts.entity;

public class CusAddr implements java.io.Serializable {

	// Fields

	private Long cadId;
	private CusCorCus cadCus;
	private String cadAddr;
	private String cadPostCode;
	private String cadContact;
	private String cadPho;
	private CusArea cadProv;
	private CusProvince cadCity;
	private CusCity cadDistrict;

	// Constructors

	/** default constructor */
	public CusAddr() {
	}

	/** full constructor */
	public CusAddr(CusCorCus cadCus, String cadAddr, String cadPostCode,
			String cadContact, String cadPho, CusArea cadProv, CusProvince cadCity,
			CusCity cadDistrict) {
		this.cadCus = cadCus;
		this.cadAddr = cadAddr;
		this.cadPostCode = cadPostCode;
		this.cadContact = cadContact;
		this.cadPho = cadPho;
		this.cadProv = cadProv;
		this.cadCity = cadCity;
		this.cadDistrict = cadDistrict;
	}

	// Property accessors

	public Long getCadId() {
		return this.cadId;
	}

	public void setCadId(Long cadId) {
		this.cadId = cadId;
	}

	public String getCadAddr() {
		return this.cadAddr;
	}

	public void setCadAddr(String cadAddr) {
		this.cadAddr = cadAddr;
	}

	public String getCadPostCode() {
		return this.cadPostCode;
	}

	public void setCadPostCode(String cadPostCode) {
		this.cadPostCode = cadPostCode;
	}

	public String getCadContact() {
		return this.cadContact;
	}

	public void setCadContact(String cadContact) {
		this.cadContact = cadContact;
	}

	public String getCadPho() {
		return this.cadPho;
	}

	public void setCadPho(String cadPho) {
		this.cadPho = cadPho;
	}

	public CusArea getCadProv() {
		return cadProv;
	}

	public void setCadProv(CusArea cadProv) {
		this.cadProv = cadProv;
	}

	public CusProvince getCadCity() {
		return cadCity;
	}

	public void setCadCity(CusProvince cadCity) {
		this.cadCity = cadCity;
	}

	public CusCity getCadDistrict() {
		return cadDistrict;
	}

	public void setCadDistrict(CusCity cadDistrict) {
		this.cadDistrict = cadDistrict;
	}

	public CusCorCus getCadCus() {
		return cadCus;
	}

	public void setCadCus(CusCorCus cadCus) {
		this.cadCus = cadCus;
	}

}