package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ProdStore;


public interface ProdStoreDAO {
	public void save(ProdStore prodStore);
	public void delete(ProdStore prodStore);
	public void delete(String id);
	public ProdStore merge(ProdStore prodStore);
	public ProdStore findById(Long id);
	
	public int listCount(String[] args);
	
	public List<ProdStore> list(String[] args, String orderItem,String isDe, int currentPage,int pageSize);
	
}