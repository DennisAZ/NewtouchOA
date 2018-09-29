package com.psit.struts.entity;

import java.util.List;

/**
 * 头部动态列表的表格实体 <br>
 * create_date: Aug 27, 2010,3:22:03 PM<br>
 * @author csg
 */
public class StaTable {
	private int colCount;//列数
	private int rowCount;//行数
	private String[] topList;//头部列表
	private List dataList;//内容列表
	private String dataXML;//XML内容 
	public StaTable(){
		
	}
	public StaTable(int colCount,int rowCount,String[] topList,List dataList)
	{
		this.colCount=colCount;
		this.rowCount=rowCount;
		this.topList=topList;
		this.dataList=dataList;
	}
	public int getColCount() {
		return colCount;
	}
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String[] getTopList() {
		return topList;
	}
	public void setTopList(String[] topList) {
		this.topList = topList;
	}
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public String getDataXML() {
		return dataXML;
	}
	public void setDataXML(String dataXML) {
		this.dataXML = dataXML;
	}
	
	
}
