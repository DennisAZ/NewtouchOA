package com.psit.struts.entity;


/**
 * 销售月度统计实体 <br>
 * create_date: Feb 27, 2011,9:48:40 PM<br>
 * @author rabbit
 */
public class StatSalMon {
	private String month;//月份
	private Double paidSum;//回款金额
	private Double ordSum;//订单金额
	
	public StatSalMon() {
	}
	public StatSalMon(String month, Double paidSum, Double ordSum) {
		this.month = month;
		this.paidSum = paidSum;
		this.ordSum = ordSum;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Double getPaidSum() {
		return paidSum;
	}
	public void setPaidSum(Double paidSum) {
		this.paidSum = paidSum;
	}
	public Double getOrdSum() {
		return ordSum;
	}
	public void setOrdSum(Double ordSum) {
		this.ordSum = ordSum;
	}
	
}
