package com.psit.struts.entity;
/**
 * 
 * 销售指标数组实体 <br>
 *
 * create_date: Aug 27, 2010,3:38:35 PM<br>
 * @author zjr
 */
public class SalTar {
	private Long name;
	private String array[];
	//构造函数
	public SalTar(Long name,String array[]){
		this.name=name;
		this.array=array;
	}
	
	public Long getName() {
		return name;
	}
	public void setName(Long name) {
		this.name = name;
	}
	public String[] getArray() {
		return array;
	}
	public void setArray(String[] array) {
		this.array = array;
	}
	
}
