package com.psit.struts.entity;

import java.util.HashSet;
import java.util.Set;

public class ERPOrderCus implements java.io.Serializable {
    private Set erpSalOrders = new HashSet(0);
    private ERPSalOrder erpSalOrder =null;
    private String cusId;
    public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public ERPSalOrder getErpSalOrder() {
		return erpSalOrder;
	}

	public void setErpSalOrder(ERPSalOrder erpSalOrder) {
		this.erpSalOrder = erpSalOrder;
	}

	private String orderId;
    private String cusName;
	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Set getErpSalOrders() {
		return erpSalOrders;
	}

	public void setErpSalOrders(Set erpSalOrders) {
		this.erpSalOrders = erpSalOrders;
	}
	
}
