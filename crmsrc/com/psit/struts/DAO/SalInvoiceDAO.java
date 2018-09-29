package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SalInvoice;
/**
 * 回款记录数据库操作接口类<br>
 */
public interface SalInvoiceDAO {
	
	/**
	 * 保存<br>
	 */
	public void save(SalInvoice invoice);
	
	/**
	 * 更新<br>
	 */
	public void update(SalInvoice invoice,Double oldSin);
	
	/**
	 * 完全删除<br>
	 */
	public void delete(SalInvoice invoice);
	
	/**
	 * 详情<br>
	 */
	public SalInvoice findById(Long id);
	/**
	 * 根据id获得回款计录（回收站里用）<br>
	 */
	public SalInvoice getById(Long id);

	/**
	 * 票据记录列表 <br>
	 */
	public List<SalInvoice> listSalInvoices(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSalInvoicesCount(String[]args);
}