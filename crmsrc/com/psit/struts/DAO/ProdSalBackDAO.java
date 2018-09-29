package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.ProdSalBack;

public interface ProdSalBackDAO {
	public void save(ProdSalBack transientInstance);

	public void delete(ProdSalBack persistentInstance);

	public ProdSalBack findById(Long id);

	public List<ProdSalBack> findAll() ;

	public ProdSalBack merge(ProdSalBack detachedInstance) ;

	public List<ProdSalBack> list(String prodId, String orderItem, String isDe,
			int currentPage, int pageSize);
	public int listCount(String prodId);

	public List<ProdSalBack> listByProdId(String prodId);
}