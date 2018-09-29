package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RWoutPro;
/**
 * 
 * 出库明细DAO <br>
 *
 * create_date: Aug 17, 2010,4:58:25 PM<br>
 * @author zjr
 */
public interface RWoutProDAO {
	/**
	 * 
	 * 保存出库明细 <br>
	 * create_date: Aug 11, 2010,5:03:56 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void save(RWoutPro rwoutPro);
	/**
	 * 
	 * 根据出库单号查询出库明细 <br>
	 * create_date: Aug 11, 2010,5:04:23 PM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库明细列表
	 */
	public List getRwoutPro(String wwoCode);
	/**
	 * 
	 * 更新出库明细 <br>
	 * create_date: Aug 11, 2010,5:08:03 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void updateRwp(RWoutPro rwoutPro);
	/**
	 * 
	 * 根据出库明细id查询实体 <br>
	 * create_date: Aug 11, 2010,5:08:28 PM <br>
	 * @param rwoId 出库明细id
	 * @return RWoutPro 返回出库明细实体
	 */
	public RWoutPro getRWoutPro(Long rwoId);
	/**
	 * 
	 * 删除出库明细 <br>
	 * create_date: Aug 11, 2010,5:17:33 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void delete(RWoutPro rwoutPro);
	/**
	 * 
	 * 根据商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:00:58 PM <br>
	 * @param wprId 商品id
	 * @return List 返回出库明细列表
	 */
	public List findByWpr(Long wprId);
	/**
	 * 
	 * 根据出库单id和商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:04:55 PM <br>
	 * @param wprId 商品id
	 * @param wwoId 出库单id
	 * @return List 返回出库明细列表
	 */
	public List findByWwo(Long wprId,Long wwoId);
}