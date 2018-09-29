package com.psit.struts.form;

import java.util.Date;

/**
 * 销售金额统计实体 <br>
 */
public class StatSalMForm {
	private Long cusId;//客户ID
	private String cusName;//客户名称
	private String empName;//员工名称
	private Double monSum1;//金额1
	private Double monSum2;//金额2
	private Double monSum3;//金额3
	private Double monSum4;//金额4
	private Double monSum5;//金额5
	private Date date1;//日期1

	public StatSalMForm() {
	}
	public StatSalMForm(Long cusId, String cusName, String empName, Double monSum1, Double monSum2, Double monSum3,
		Double monSum4,Date date1, Double monSum5) {
		this.cusId = cusId;
		this.cusName = cusName;
		this.empName = empName;
		this.monSum1 = monSum1;
		this.monSum2 = monSum2;
		this.monSum3 = monSum3;
		this.monSum4 = monSum4;
		this.date1 = date1;
		this.monSum5 = monSum5;
	}

	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Double getMonSum4() {
		return monSum4;
	}
	public void setMonSum4(Double monSum4) {
		this.monSum4 = monSum4;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public Double getMonSum1() {
		return monSum1;
	}
	public void setMonSum1(Double monSum1) {
		this.monSum1 = monSum1;
	}
	public Double getMonSum2() {
		return monSum2;
	}
	public void setMonSum2(Double monSum2) {
		this.monSum2 = monSum2;
	}
	public Double getMonSum3() {
		return monSum3;
	}
	public void setMonSum3(Double monSum3) {
		this.monSum3 = monSum3;
	}
	public long getCusId() {
		return cusId;
	}
	public void setCusId(Long cusId) {
		this.cusId = cusId;
	}
	public Double getMonSum5() {
		return monSum5;
	}
	public void setMonSum5(Double monSum5) {
		this.monSum5 = monSum5;
	}

}
