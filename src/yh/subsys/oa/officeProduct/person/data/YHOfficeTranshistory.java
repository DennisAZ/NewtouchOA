package yh.subsys.oa.officeProduct.person.data;

import java.util.Date;

public class YHOfficeTranshistory {
    private int seqId;//	流水号Y自增
    private int proId;//办公用品			
    private String borrower;//	新建办公用品登记人			
    private String transFlag; //登记类型   1领用  2借用  3归还
    private int transQty; //	申请数量			
    private int price;				
    private String remark;//备注			
    private Date transDate;//系统当前日期			
    private String operator;//	操作者			
    private String transState; //	状态			0新建
    private int factQty;				
    private String reason;			
    private String cycle;			
    private int cycleNo;	//申请标记			
    private String company;		
    private String band;		
    private int deptId;	//登记申领人的部门id			
    private String officeProductName; //办公用品名称
    private String transName;//办公用品类型
    private String transStateName;//状态名称
	public String getTransStateName() {
		return transStateName;
	}

	public void setTransStateName(String transStateName) {
		this.transStateName = transStateName;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getOfficeProductName() {
		return officeProductName;
	}

	public void setOfficeProductName(String officeProductName) {
		this.officeProductName = officeProductName;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public int getTransQty() {
		return transQty;
	}

	public void setTransQty(int transQty) {
		this.transQty = transQty;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTransState() {
		return transState;
	}

	public void setTransState(String transState) {
		this.transState = transState;
	}

	public int getFactQty() {
		return factQty;
	}

	public void setFactQty(int factQty) {
		this.factQty = factQty;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

}
