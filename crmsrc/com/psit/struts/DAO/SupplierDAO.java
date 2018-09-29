package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.Supplier;


public interface SupplierDAO {
	public void save(Supplier supplier);
	public void delete(Supplier supplier);
	public Supplier merge(Supplier supplier);
	public Supplier findById(Long id);
	
	public void deleteById(String supId);
	
	/**
	 * 列表 <br>
	 */
	public List<Supplier> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCount(String[]args);
}