package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SupPaidPast;


public interface SupPaidPastDAO {
	public void save(SupPaidPast supPaidPast);
	public void delete(SupPaidPast supPaidPast);
	public SupPaidPast merge(SupPaidPast supPaidPast);
	public SupPaidPast findById(Long id);
	
	public void deleteById(String sppId);
	
	/**
	 * 列表 <br>
	 */
	public List<SupPaidPast> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCount(String[]args);
}
