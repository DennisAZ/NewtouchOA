package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SupProd;


public interface SupProdDAO {
	public void save(SupProd supProd);
	public void delete(SupProd supProd);
	public SupProd merge(SupProd supProd);
	public SupProd findById(Long id);
	
	public void deleteById(String spId);
	
	/**
	 * 列表 <br>
	 */
	public List<SupProd> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCount(String[]args);
	/**
	 * 根据供应商Id查找供应商产品<br>
	 * @param supId 供应商Id
	 * @return 供应商产品列表
	 */
	public List<SupProd> listBySupId(String supId);
}
