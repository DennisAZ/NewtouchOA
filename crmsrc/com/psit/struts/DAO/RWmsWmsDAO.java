package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RWmsWms;
/**
 * 
 * 调拨明细DAO <br>
 *
 * create_date: Aug 17, 2010,4:38:33 PM<br>
 * @author zjr
 */
public interface RWmsWmsDAO {
	/**
	 * 
	 * 保存调拨明细 <br>
	 * create_date: Aug 11, 2010,5:36:06 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void save(RWmsWms rwmsWms);
	/**
	 * 
	 * 根据仓库调拨单id查询调拨明细 <br>
	 * create_date: Aug 11, 2010,5:38:21 PM <br>
	 * @param wchId 仓库调拨单id
	 * @return List 返回调拨明细列表
	 */
	public List getRww(Long wchId);
	/**
	 * 
	 * 更新仓库调拨商品 <br>
	 * create_date: Aug 11, 2010,5:39:12 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void updateRww(RWmsWms rwmsWms);
	/**
	 * 
	 * 根据Id查询调拨明细 <br>
	 * create_date: Aug 11, 2010,5:39:46 PM <br>
	 * @param rwwId 调拨明细id
	 * @return RWmsWms 返回调拨明细实体
	 */
	public RWmsWms searchRww(Long rwwId);
	/**
	 * 
	 * 删除仓库调拨明细 <br>
	 * create_date: Aug 11, 2010,5:53:46 PM <br>
	 * @param rwmsWms 调拨明细实体
	 */
	public void delete(RWmsWms rwmsWms);
	/**
	 * 
	 * 根据商品id查询调拨明细 <br>
	 * create_date: Aug 12, 2010,12:02:30 PM <br>
	 * @param wprId 商品id
	 * @return List 返回调拨明细列表
	 */
	public List findByWpr(Long wprId);
	/**
	 * 
	 * 根据调拨单id和商品id查询调拨明细 <br>
	 * create_date: Aug 12, 2010,12:05:59 PM <br>
	 * @param wprId 商品id
	 * @param wchId 调拨单id
	 * @return List 返回出调拨明细列表
	 */
	public List findByWch(Long wprId,Long wchId);
}
