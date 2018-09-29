package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ProdIn;


public interface ProdInDAO {
	public void save(ProdIn prodIn);
	public void delete(ProdIn prodIn);
	public ProdIn merge(ProdIn prodIn);
	public ProdIn findById(Long id);
	public void delProdInById(String pinId);
	
	public int listCount(String[] args);
	
	public List<ProdIn> list(String[] args,String orderItem, String isDe,int currentPage, int pageSize);
}