package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.ERPPurchase;
import com.psit.struts.entity.PurOrd;


public interface PurOrdDAO {
	public void save(PurOrd purOrd);
	public void delete(PurOrd purOrd);
	public PurOrd merge(PurOrd purOrd);
	public PurOrd findById(Long id);
	
	public void deleteById(String puoId);
	
	/**
	 * �б� <br>
	 */
	public List<PurOrd> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCount(String[]args);
	public List<ERPPurchase> listERP(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCountERP(String[]args);
}