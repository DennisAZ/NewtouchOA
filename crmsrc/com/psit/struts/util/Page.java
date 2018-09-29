package com.psit.struts.util;

import com.psit.struts.util.format.StringFormat;

public class Page {
	public static final int DF_PAGE_SIZE = 30;
	private int currentPageNo;
	private int nextPageNo;
	private int prePageNo;
	private int firstPageNo;
	private int lastPageNo;
	private int pageSize ;
	private int rowsCount;
	private int pageCount;
	
	/**
	 * ��ʼ��
	 * @param rowsCount ����
	 * @param pageSize 	ҳ��
	 * @param curPageNo ��ǰҳ��
	 */
	public Page(int rowsCount,String pageSize, String curPageNo){
		this.rowsCount = rowsCount;
		if(StringFormat.isEmpty(pageSize)){
			this.pageSize = Page.DF_PAGE_SIZE;
		}
		else{
			this.pageSize = Integer.parseInt(pageSize);
		}
		setCurrentPageNo(curPageNo);
	}
	/**
	 * ��ʼ��
	 * @param rowsCount ����
	 * @param pageSize ҳ��
	 */
	public Page(int rowsCount,int pageSize)
	{
		this.rowsCount = rowsCount;
		this.pageSize = pageSize;
	}
	/**
	 * �õ���ǰҳ�� <br>
	 * @return int ���ص�ǰҳ�� <br>
	 */
	public int getCurrentPageNo() {
		
		if(this.currentPageNo > this.getLastPageNo())
		{
			this.setCurrentPageNo(this.getLastPageNo());
		}
		if(this.currentPageNo < this.getFirstPageNo())
		{
			this.setCurrentPageNo(this.getFirstPageNo());
		}
		return currentPageNo;
	}
	/**
	 * ���õ�ǰҳ�� <br>
	 * @param currentPageNo ҳ��<br>
	 */
	public void setCurrentPageNo(String currentPageNo){
		if (StringFormat.isEmpty(currentPageNo) || currentPageNo.trim().length() < 1) {
			currentPageNo = "1";
		}
		this.currentPageNo = Integer.parseInt(currentPageNo);
	}
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	/**
	 * �����һҳ�� <br>
	 * @return int ������һҳҳ��<br>
	 */
	public int getNextPageNo() {
		this.nextPageNo = this.currentPageNo + 1;
		
		if(this.nextPageNo>this.getPageCount())
		{
			this.nextPageNo = this.getPageCount();
		}
		return nextPageNo;
	}
	/**
	 * �����ҳ�� <br>
	 * @return int ������ҳ��<br>
	 */
	public int getPageCount() {
		if(this.rowsCount % this.pageSize == 0)
		{
			this.pageCount = this.rowsCount / this.pageSize;
		}
		else
		{
			this.pageCount = (this.rowsCount / this.pageSize) + 1;
		}
		return pageCount;
	}
	/**
	 * ���ǰһҳ�� <br>
	 * @return int ������һҳҳ��<br>
	 */
	public int getPrePageNo() {
		this.prePageNo = this.currentPageNo - 1;
		if(this.prePageNo <=0 )
		{
			this.prePageNo = 1;
		}
		return prePageNo;
	}

	/**
	 * ��õ�һҳ <br>
	 * @return int ���ص�һҳ <br>
	 */
	public int getFirstPageNo() {
		this.firstPageNo = 1;
		return firstPageNo;
	}
	/**
	 * ������һҳ <br>
	 * @return int �������һҳ <br>
	 */
	public int getLastPageNo() {
		this.lastPageNo = this.getPageCount();
		return lastPageNo;
	}
	/**
	 * ���ҳ�� <br>
	 * @return int ����ҳ��<br>
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * ������� <br>
	 * @return int ��������<br>
	 */
	public int getRowsCount() {
		return rowsCount;
	}
	
	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	/**
	 * ����ҳ�� <br>
	 * @param pageCount ����ҳ��<br>
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}
