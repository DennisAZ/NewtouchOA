package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsProduct;
/**
 * 
 * 商品DAO <br>
 *
 * create_date: Aug 19, 2010,10:13:28 AM<br>
 * @author zjr
 */
public interface WmsProductDAO {
	/**
	 * 保存商品 <br>
	 */
	public void save(WmsProduct wmsProduct);
	/**
	 * 根据商品id查询商品实体 <br>
	 */
	public WmsProduct wmsProDesc(Long wprId);
	/**
	 * 修改商品信息 <br>
	 */
	public void wmsProUpdate(WmsProduct wmsProduct);
	/**
	 * 删除商品 <br>
	 */
	public void delete(WmsProduct wmsProduct);
	/**
	 * 按商品名称/型号/编号查询 <br>
	 */
	public List proSearch(String wprName,String agr);
	/**
	 * 判断商品名称型号是否重复 <br>
	 */
	public List proCheck(String wprName);
	/**
	 * 查询未分类的商品 <br>
	 */
	public List<WmsProduct> noTypePro();
	/**
	 * 根据商品的编号查询商品实体 <br>
	 */
	public List findbyWprCode(String wprCode);
	/**
	 * 获得待删除的所有商品列表 <br>
	 */
	public List findDelProduct(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有商品数量 <br>
	 */
	public int findDelProductCount();
	/**
	 * 商品列表<br>
	 */
	public List<WmsProduct> listProds(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdsCount(String[]args);
	
	/**
	 * 按商品名称/型号/编号查询 <br>
	 */
	public List<WmsProduct> searchProdByName(String wprName);
}