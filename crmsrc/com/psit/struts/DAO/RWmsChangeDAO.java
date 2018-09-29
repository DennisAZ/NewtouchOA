package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RWmsChange;
/**
 * 
 * 盘点明细DAO <br>
 *
 * create_date: Aug 17, 2010,4:25:30 PM<br>
 * @author zjr
 */
public interface RWmsChangeDAO {
	/**
	 * 
	 * 保存盘点明细 <br>
	 * create_date: Aug 12, 2010,10:35:49 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void save(RWmsChange rwmsChange);
	/**
	 * 
	 * 根据Id得到盘点明细实体 <br>
	 * create_date: Aug 12, 2010,10:36:16 AM <br>
	 * @param id 盘点明细id
	 * @return RWmsChange 返回盘点明细实体
	 */
	public RWmsChange findById(Long id);
	/**
	 * 
	 * 更新盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:00 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void updateRwmc(RWmsChange rwmsChange);
	/**
	 * 
	 * 删除盘点明细<br>
	 * create_date: Aug 12, 2010,10:39:25 AM <br>
	 * @param rwmsChange 盘点明细实体
	 */
	public void delete(RWmsChange rwmsChange);
	/**
	 * 
	 * 根据盘点单id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:47 AM <br>
	 * @param wmcId 盘点单id
	 * @return List 返回盘点明细列表
	 */
	public List findByWmc(Long wmcId);
	/**
	 * 
	 *  根据商品id查询盘点明细<br>
	 * create_date: Aug 12, 2010,12:03:40 PM <br>
	 * @param wprId 商品id
	 * @return List 返回盘点明细列表
	 */
	public List findByWpr(Long wprId);
	/**
	 * 
	 * 根据盘点记录id和商品id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,12:06:56 PM <br>
	 * @param wprId 商品id
	 * @param wmcId 盘点记录id
	 * @return List 返回出盘点明细列表
	 */
	public List findByWmc(Long wprId,Long wmcId);
}
