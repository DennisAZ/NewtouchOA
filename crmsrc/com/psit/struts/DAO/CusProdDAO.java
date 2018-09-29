package com.psit.struts.DAO;

import java.util.List;
import com.psit.struts.entity.CusProd;

/**
 * 
 * 客户产品DAO <br>
 *
 * create_date: May 20, 2011,14:40:26 PM<br>
 * @author wyb
 */

public interface CusProdDAO {
	
	/**
	 * 客户产品列表<br/>
	 */
	public List<CusProd> listCusProd(String [] args, String orderItem,String isDe, int currentPage, int pageSize);
	public int listCusProdCount(String [] args);
	
	/**
	 * 保存客户产品
	 */
	public void save(CusProd cusProd);
	
	/**
	 * 通过Id查找客户产品<br>
	 */
	public CusProd findById(Long cpId);
	
	/**
	 * 编辑客户产品<br>
	 */
	public void update(CusProd cusProd);
	
	/**
	 * 根据客户Id查找客户产品<br>
	 */
	public List<CusProd> listByCusId(String cusId);
	
	/**
 	 *删除客户产品<br>
	 */
	public void delete(CusProd persistentInstance);
	
	/**
	 * 根据产品Id查找客户产品 <br>
	 * @param wprId 产品Id
	 * @return List<CusProd> 客户产品列表<br>
	 */
	public List<CusProd> listByWprId(String wprId);
	
	public List findAll();

}