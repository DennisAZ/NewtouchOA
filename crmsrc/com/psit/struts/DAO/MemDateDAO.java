package com.psit.struts.DAO;

import java.util.List;
import com.psit.struts.entity.MemDate;

public interface MemDateDAO {
	public List<MemDate> listByCon(String conId, String orderItem,String isDe, int currentPage, int pageSize);
	public int listByConCount(String conId);
	
	public void save(MemDate memDate);
	public void update(MemDate memDate);
	public void delete(MemDate memDate);
	
	public MemDate findById(Long mdId);
	
}