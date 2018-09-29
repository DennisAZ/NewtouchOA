package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsProType;
/**
 * 
 * 商品类别DAO <br>
 *
 * create_date: Aug 19, 2010,11:36:32 AM<br>
 * @author zjr
 */
public interface WmsProTypeDAO {
	/**
	 * 
	 * 查询所有已使用的商品类别 <br>
	 * create_date: Aug 11, 2010,2:22:52 PM <br>
	 * @return List 类别列表
	 */
	public List findAllWptType();
	/**
	 * 
	 * 查询所有已使用的商品类别 <br>
	 * create_date: Aug 11, 2010,2:22:52 PM <br>
	 * @return List 类别列表
	 */
	public List<WmsProType> findAll();
	/**
	 * 
	 * 添加商品类别 <br>
	 * create_date: Aug 12, 2010,9:40:34 AM <br>
	 * @param wmsProType 商品类别实体
	 */
	public void save(WmsProType wmsProType);
	/**
	 * 
	 * 根据商品类别Id查询类别实体 <br>
	 * create_date: Aug 12, 2010,11:27:56 AM <br>
	 * @param id 商品类别Id
	 * @return WmsProType 返回商品类别实体
	 */
	public WmsProType findById(java.lang.Long id);
	/**
	 * 
	 * 更新商品类别 <br>
	 * create_date: Aug 12, 2010,11:29:59 AM <br>
	 * @param instance 商品类别实体
	 */
	public void updateProType(WmsProType instance);
	/**
	 * 
	 * 删除商品类别 <br>
	 * create_date: Aug 12, 2010,11:30:18 AM <br>
	 * @param instance 商品类别实体
	 */
	public void delete(WmsProType instance);
	/**
	 * 
	 * 查询商品类别的下级类别 <br>
	 * create_date: Aug 12, 2010,11:30:37 AM <br>
	 * @param wptId 商品类别id
	 * @return List 返回商品类别列表
	 */
	public List findByUpId(Long wptId);
	
	/**
	 * 根据客户Id查询商品类别<br>
	 * @param cusId 客户Id
	 * @return List<WmsProType> 商品类别<br>
	 */
	public List<WmsProType> findByCusId(String cusId);
}