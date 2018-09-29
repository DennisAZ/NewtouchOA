package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RPuoPro;


public interface RPuoProDAO {
	public void save(RPuoPro rPuoPro);
	public void delete(RPuoPro rPuoPro);
	public RPuoPro merge(RPuoPro rPuoPro);
	public RPuoPro findById(Long id);
	
	public int listCount(String []args);
	public List<RPuoPro> list(String[] args, String orderItem,String isDe,
			int currentPage, int pageSize );
	/**
	 * 根据采购单号查询采购明细 <br>
	 * @param puoId 采购id 
	 * @return List 返回采购明细列表
	 */
	public List<RPuoPro> findByPurOrd(String puoId);
	public void batSave(List<RPuoPro> saveList,String puoId);
	/**
	 * 采购单产品明细列表<br>
	 * @param puoId 采购ID
	 * @return List<RPuoPro> 采购单产品明细列表
	 */
	public List<RPuoPro> listPuoPro(String puoId);
}