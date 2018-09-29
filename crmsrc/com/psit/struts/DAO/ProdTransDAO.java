package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.ProdTrans;

public interface ProdTransDAO {
	public void save(ProdTrans transientInstance);
	public void update(ProdTrans transientInstance);
	public void delete(ProdTrans persistentInstance);
	public void deleteById(String id);
	public ProdTrans findById(Long id);
	
	public List<ProdTrans> listByProdId(String prodId,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listByProdIdCount(String prodId);
	
	public List<ProdTrans> listByProdIds(String prodIds, String expId, String provId, String cityId, String districtId);
}