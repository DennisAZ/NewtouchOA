package com.psit.struts.entity;

import java.util.Date;

import com.psit.struts.util.format.GetDate;

/**
 * 统计实体 <br>
 * create_date: Feb 24, 2011,1:58:53 PM<br>
 * @author rabbit
 */
public class StatOrd {
	private Long typId;//；类别ID
	private String typName;//类别名称
	private Long count;//数量
	private Double sum;//金额
//	private String statType;//统计类型
	
	public StatOrd() {
	}
	public StatOrd(String typName, Long count, Double sum) {
		this.typName = typName;
		this.count = count;
		this.sum = sum;
	}
	public StatOrd(Date date, Long count, Double sum) {
		this.typName = GetDate.parseStrMon(date);
		this.count = count;
		this.sum = sum;
	}
	public StatOrd(Date date, Double sum) {
		this.typName = GetDate.parseStrMon(date);
		this.sum = sum;
	}
	public Long getTypId() {
		return typId;
	}

	public void setTypId(Long typId) {
		this.typId = typId;
	}

	public String getTypName() {
		if(typName==null){
			typName = "未选择";
		}
		return typName;
	}

	public void setTypName(String typName) {
		this.typName = typName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	/*public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}*/

}
