package oa.spring.po;

import java.io.Serializable;

public class POD implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1731618386260420050L;
	private String id;
	private String pod_code;// 仓库发货单编号
	private String pod_status;// 仓库发货单状态
	private String order_id;// 销售订单id
	private String po_id;// 销售出货单id
	private String person;// 收货人
	private String address;// 收货地址
	private String phone;// 联系方式
	private String zip_code;// 邮编
	private String pod_date;// 发货日期
	private String pod_sender;// 发货人名称
	private String pod_sender_id;// 发货人Id标识
	private String pod_send_way;// 物流公司
	private double total;// 总运费
	private String remark;

	public POD() {
	}

	public POD(String pod_code, String pod_status, String order_id,
			String po_id, String person, String address, String phone,
			String zip_code, String pod_date, String pod_sender,String pod_sender_id,
			String pod_send_way, double total, String remark) {
		this.pod_code = pod_code;
		this.pod_status = pod_status;
		this.order_id = order_id;
		this.po_id = po_id;
		this.person = person;
		this.address = address;
		this.phone = phone;
		this.zip_code = zip_code;
		this.pod_date = pod_date;
		this.pod_sender = pod_sender;
		this.pod_sender_id = pod_sender_id;
		this.pod_send_way = pod_send_way;
		this.total = total;
		this.remark = remark;
	}

	public String getPod_sender_id() {
		return pod_sender_id;
	}

	public void setPod_sender_id(String podSenderId) {
		pod_sender_id = podSenderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPod_code() {
		return pod_code;
	}

	public void setPod_code(String podCode) {
		pod_code = podCode;
	}

	public String getPod_status() {
		return pod_status;
	}

	public void setPod_status(String podStatus) {
		pod_status = podStatus;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getPo_id() {
		return po_id;
	}

	public void setPo_id(String poId) {
		po_id = poId;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zipCode) {
		zip_code = zipCode;
	}

	public String getPod_date() {
		return pod_date;
	}

	public void setPod_date(String podDate) {
		pod_date = podDate;
	}

	public String getPod_sender() {
		return pod_sender;
	}

	public void setPod_sender(String podSender) {
		pod_sender = podSender;
	}

	public String getPod_send_way() {
		return pod_send_way;
	}

	public void setPod_send_way(String podSendWay) {
		pod_send_way = podSendWay;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
