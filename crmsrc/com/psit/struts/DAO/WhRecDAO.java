package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ProdIn;
import com.psit.struts.entity.WhRec;


public interface WhRecDAO {
	public void save(WhRec whRec);
	public void delete(WhRec whRec);
	public WhRec merge(WhRec whRec);
	public WhRec findById(Long id);

	public int listCount(String[] args);
	
	public List<WhRec> list(String[] args, String orderItem, String isDe,int currentPage, int pageSize);
}