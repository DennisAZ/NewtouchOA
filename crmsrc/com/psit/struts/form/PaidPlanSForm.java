package com.psit.struts.form;

/**
 * 回款计划查询的form <br>
 * create_date: Aug 16, 2010,2:42:45 PM<br>
 * @author rabbit
 */
public class PaidPlanSForm implements java.io.Serializable{
	/**
	 * @fields serialVersionUID : 序列化Id
	 */
	private static final long serialVersionUID = 1L;
	private String planContent;//摘要
	private String startDate;//开始日期
	private String endDate;//结束日期
	private String filter;//列表筛选条件
	
	public String getPlanContent() {
		return planContent;
	}
	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
