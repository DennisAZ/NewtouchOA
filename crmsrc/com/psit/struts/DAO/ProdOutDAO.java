package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ProdOut;


public interface ProdOutDAO {
	public void save(ProdOut prodOut);
	public void delete(ProdOut prodOut);
	public ProdOut merge(ProdOut prodOut);
	public ProdOut findById(Long id);
	public void deleteById(String pouId);
	
	public int listCount(String[] args);
	
	public List<ProdOut> list(String[] args,String orderItem,String isDe,int currnetPage,int pageSize);
	

}