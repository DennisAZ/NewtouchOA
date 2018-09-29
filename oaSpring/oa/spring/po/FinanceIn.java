package oa.spring.po;

import java.io.Serializable;

/**
 * 应收单
 * @author Administrator
 *
 */
public class FinanceIn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6545247952587185560L;
	
	private String id;
	private String code;
	private String status;
	private String type;
	private String type_id;
	private String customer;
	private double total;
	private String paid_way;
	private String paid_date;
	private String person;
	private String invoice_id;
	private String remark;
	
	public FinanceIn(){}
	public FinanceIn(String code,String status,String type,
			String type_id,String customer,double total,String paid_way,
			String paid_date,String person,String invoice_id,String remark){
		this.code = code;
		this.status = status;
		this.type = type;
		this.type_id = type_id;
		this.customer = customer;
		this.total = total;
		this.paid_way = paid_way;
		this.paid_date = paid_date;
		this.person = person;
		this.invoice_id = invoice_id;
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPpd_code() {
		return code;
	}
	public void setPpd_code(String ppdCode) {
		code = ppdCode;
	}
	public String getPpd_status() {
		return status;
	}
	public void setPpd_status(String ppdStatus) {
		status = ppdStatus;
	}
	public String getOrder_id() {
		return type;
	}
	public void setOrder_id(String orderId) {
		type = orderId;
	}
	public String getPp_id() {
		return type_id;
	}
	public void setPp_id(String ppId) {
		type_id = ppId;
	}
	public String getCus_id() {
		return customer;
	}
	public void setCus_id(String cusId) {
		customer = cusId;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getPaid_way() {
		return paid_way;
	}
	public void setPaid_way(String paidWay) {
		paid_way = paidWay;
	}
	public String getPaid_date() {
		return paid_date;
	}
	public void setPaid_date(String paidDate) {
		paid_date = paidDate;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoiceId) {
		invoice_id = invoiceId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
