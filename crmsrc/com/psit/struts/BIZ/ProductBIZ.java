package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.ProdSalBack;
import com.psit.struts.entity.ProdTrans;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsProduct;
public interface ProductBIZ {
	/**
	 * 查询所有已使用的商品类别 <br>
	 * @return List 类别列表
	 */
	public List<WmsProType> findAllWptType();
	/**
	 * 保存商品 <br>
	 * @param wmsProduct 商品实体
	 */
	public void saveWP(WmsProduct wmsProduct);
	
	/**
	 * 商品列表<br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]商品名;  [2]商品编号;  [3]商品型号;  
	 * 				[4]商品分类;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<WmsProduct> 商品列表
	 */
	public List<WmsProduct> listProds(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdsCount(String[]args);
	/**
	 * 
	 * 根据商品id查询商品实体 <br>
	 * @param wprId 商品id
	 * @return WmsProduct 返回商品实体
	 */
	public WmsProduct wmsProDesc(Long wprId);
	/**
	 * 
	 * 根据商品的编号查询商品实体 <br>
	 * @param wprCode 商品编号
	 * @return List 返回商品列表
	 */
	public List findbyWprCode(String wprCode);
	/**
	 * 
	 * 修改商品信息 <br>
	 * @param wmsProduct 商品实体
	 */
	public void wmsProUpdate(WmsProduct wmsProduct);
	/**
	 * 
	 * 删除商品 <br>
	 * @param wmsProduct 商品实体
	 */
	public void deletePro(WmsProduct wmsProduct);
	/**
	 * 按商品名称/型号/编号查询 <br>
	 * @param wprName 商品名称/型号/编号
	 * @param agr 是否计算库存标识
	 * @return List 返回商品列表
	 */
	public List proSearch(String wprName,String agr);
	/**
	 * 判断商品名称型号是否重复 <br>
	 * @param wprName 商品名称
	 * @param wprMod 型号
	 * @return List 返回商品列表
	 */
	public List proCheck(String wprName);
	/**
	 * 查询未分类的商品 <br>
	 * @return List 返回商品列表
	 */
	public List<WmsProduct> noTypePro();
	/**
	 * 
	 * 获得待删除的所有商品列表 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回商品列表
	 */
	public List findDelProduct(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有商品数量 <br>
	 * @return int 返回商品列表数量
	 */
	public int findDelProductCount();
	
	/**
	 * 查询所有商品类别 <br>
	 * @return List 返回商品类别列表
	 */
	public List findAllWpt();
	
	/**
	 * 根据客户Id查询商品类别<br>
	 * @param cusId 客户Id
	 * @return List<WmsProType> 商品类别<br>
	 */
	public List<WmsProType> findByCusId(String cusId);
	
	/**
	 * 按商品名称/型号/编号查询 <br>
	 * @param wprName 商品名称/型号/编号
	 * @return List<WmsProduct> 返回商品列表
	 */
	public List<WmsProduct> searchProdByName(String wprName);
	
	/**
	 * 产品运费列表 <br>
	 * @param prodId
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<ProdTrans>
	 */
	public List<ProdTrans> listProdTransByProdId(String prodId,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdTransByProdIdCount(String prodId);
	
	public List<ProdTrans>  listPtrByProdIds(String prodIds, String expId, String provId, String cityId, String districtId);
	
	public void saveProdTrans(ProdTrans prodTrans);
	public void updProdTrans(ProdTrans prodTrans);
	public void deleteProdTrans(String id);
	public ProdTrans findProdTrans(String id);
	
	/**
	 * 提成率列表 <br>
	 * @param prodId
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<ProdSalBack>
	 */
	public List<ProdSalBack> listProdSalBackByProdId(String prodId,String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listProdSalBackByProdIdCount(String prodId);
	
	public List<ProdSalBack> listAllPsbByProdId(String prodId);
	
	public void saveProdSalBack(ProdSalBack prodSalBack);
	public void updProdSalBack(ProdSalBack prodSalBack);
	public void deleteProdSalBack(ProdSalBack prodSalBack);
	public ProdSalBack findProdSalBack(String id);
}