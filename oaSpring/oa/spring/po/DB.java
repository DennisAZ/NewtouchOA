package oa.spring.po;

import java.io.Serializable;

public class DB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5345782644077221305L;
	
	
	private String id;
	private WareHouse warehouse;
	private Product product;
	private double price;
	private int number;
	private double total;
	private String remark;
	
	public DB(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WareHouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WareHouse warehouse) {
		this.warehouse = warehouse;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
