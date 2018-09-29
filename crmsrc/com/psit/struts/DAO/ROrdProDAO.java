package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.SalOrdCon;

public interface ROrdProDAO {
	/**
	 * 保存订单明细 <br>
	 */
	public void save(ROrdPro transientInstance);
	
	/**
	 * 批量保存 <br>
	 * @param saveList	明细列表
	 * @param sodId		订单ID
	 */
	public void batSave(List<ROrdPro> saveList,String sodId);
	
	/**
	 * 根据订单号查询订单明细 <br>
	 */
	public List<ROrdPro> findByOrd(String sodCode);
	/**
	 * 订单产品明细列表 <br>
	 * @param ordId 	订单Id
	 * @param orderItem	排序列
	 * @param isDe		是否逆序
	 * @return List<ROrdPro>
	 */
	public List<ROrdPro> listOrdPro(String ordId);
	
	/**
	 * 根据订单明细Id查询订单明细 <br>
	 */
	public ROrdPro getRop(Long ropId);
	/**
	 * 更新订单明细 <br>
	 */
	public void updateRop(ROrdPro rordPro);
	/**
	 * 删除订单明细 <br>
	 */
	public void delete(ROrdPro rordPro);
	/**
	 * 根据订单id和商品id查询订单明细 <br>
	 */
	public List findByWpr(String sodCode,Long wprCode);
	/**
	 * 根据订单id查询订单明细仓储信息 <br>
	 */
	public List findByStro(String sodCode);
	/**
	 * 根据商品id查询订单明细 <br>
	 */
	public List findByWpr(Long wprCode);
	/**
	 * 查看某商品销售历史列表数量 <br>
	 */
	public int getCountByWpr(Long wprCode);
	/**
	 * 根据商品id查询订单明细 <br>
	 */
	public List<ROrdPro> findByWpr(Long wprCode,int currentPage,int pageSize);
	
	/**
	 * 根据商品id查询订单 <br>
	 */
	public List<SalOrdCon> getOrdersByProd(String prodId);
	
	/**
	 * 通过客户Id查找订单明细<br>
	 * @param cusId 客户Id
	 * @return List<ROrdPro> 订单明细列表<br>
	 */
	public List<ROrdPro> findByCusId(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize);
	
	public int findByCusIdCount(String cusId);
	
}