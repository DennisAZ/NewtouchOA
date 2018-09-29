package com.psit.struts.util;

import java.util.List;

import com.psit.struts.util.Page;

/**
 * 公共form <br>
 */
public class ListForm {
	private List list;
	private Page page;
	private String dataXML;

	public String getDataXML() {
		return dataXML;
	}

	public void setDataXML(String dataXML) {
		this.dataXML = dataXML;
	}

	public ListForm(){
	}
	public ListForm(List list,Page page){
		this.list = list;
		this.page = page;
	}
	
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
}
