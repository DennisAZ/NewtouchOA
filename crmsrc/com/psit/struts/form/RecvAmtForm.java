package com.psit.struts.form;

/**
 * 应收款显示实体 <br>
 */
public class RecvAmtForm {
	private Long cusId;//客户ID
	private String cusName;//客户名称
	private String empName;//员工名称
	private Double recvNum;//到期金额
	private Double ticketNum;//开票余额
	
	public RecvAmtForm(){
		
	}
	
	public RecvAmtForm(Long cusId, String cusName, String empName,
			Double recvNum, Double ticketNum) {
		super();
		this.cusId = cusId;
		this.cusName = cusName;
		this.empName = empName;
		this.recvNum = recvNum;
		this.ticketNum = ticketNum;
	}

	public Long getCusId() {
		return cusId;
	}

	public void setCusId(Long cusId) {
		this.cusId = cusId;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Double getRecvNum() {
		return recvNum;
	}

	public void setRecvNum(Double recvNum) {
		this.recvNum = recvNum;
	}

	public Double getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(Double ticketNum) {
		this.ticketNum = ticketNum;
	}
}
