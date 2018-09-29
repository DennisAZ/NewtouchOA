package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SupInvoice;



public interface SupInvoiceDAO {
	public void save(SupInvoice supInvoice);
	public void delete(SupInvoice supInvoice);
	public SupInvoice merge(SupInvoice supInvoice);
	public SupInvoice findById(Long id);
	
	public void deleteById(String suiId);
	
	/**
	 * 列表 <br>
	 */
	public List<SupInvoice> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCount(String[]args);
}
