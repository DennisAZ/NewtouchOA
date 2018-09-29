package com.psit.struts.entity;

public class Statistic {
	private String userCode;
	private Double staHtMon;//合同额
	private Double staPaidMon;//回款额
	private Long staCusNum;//客户数
	public Statistic()
	{
		
	}
	public Statistic(String userCode,Double staHtMon,Double staPaidMon,Long staCusNum)
	{
		this.userCode=userCode;
		this.staHtMon=staHtMon;
		this.staPaidMon=staPaidMon;
		this.staCusNum=staCusNum;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Double getStaHtMon() {
		return staHtMon;
	}
	public void setStaHtMon(Double staHtMon) {
		this.staHtMon = staHtMon;
	}
	public Double getStaPaidMon() {
		return staPaidMon;
	}
	public void setStaPaidMon(Double staPaidMon) {
		this.staPaidMon = staPaidMon;
	}
	public Long getStaCusNum() {
		return staCusNum;
	}
	public void setStaCusNum(Long staCusNum) {
		this.staCusNum = staCusNum;
	}
	
}

