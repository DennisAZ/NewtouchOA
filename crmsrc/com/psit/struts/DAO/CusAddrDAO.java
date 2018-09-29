package com.psit.struts.DAO;

import java.util.List;
import com.psit.struts.entity.CusAddr;

public interface CusAddrDAO  {

	public void save(CusAddr transientInstance);

	public void delete(CusAddr persistentInstance);

	public CusAddr findById(Long id);

	public List<CusAddr> findAll() ;

	public CusAddr merge(CusAddr detachedInstance) ;

	public List<CusAddr> list(String cusId, String orderItem, String isDe,
			int currentPage, int pageSize);

	public int listCount(String cusId);

	public List<CusAddr> listByCusId(String cusId);
}