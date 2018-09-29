package com.psit.struts.BIZ.impl;

import java.util.List;

import com.psit.struts.BIZ.ProductBIZ;
import com.psit.struts.DAO.LimRightDAO;
import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.DAO.ProdSalBackDAO;
import com.psit.struts.DAO.ProdTransDAO;
import com.psit.struts.DAO.WmsProTypeDAO;
import com.psit.struts.DAO.WmsProductDAO;
import com.psit.struts.entity.ProdSalBack;
import com.psit.struts.entity.ProdTrans;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsProduct;

public class ProductBIZImpl implements ProductBIZ{
	LimUserDAO userDao=null;
	WmsProTypeDAO wmsProTypeDao=null;
	WmsProductDAO wmsProductDao=null;
	LimRightDAO limRightDao=null;
	ProdTransDAO prodTransDAO=null;
	ProdSalBackDAO prodSalBackDAO = null; 
	
	/**
	 * 查询所有已使用的商品类别 <br>
	 */
	public List<WmsProType> findAllWptType(){
		return wmsProTypeDao.findAll();
	}
	/**
	 * 保存商品 <br>
	 */
	public void saveWP(WmsProduct wmsProduct){
		wmsProductDao.save(wmsProduct);
	}
	
	/**
	 * 商品列表<br>
	 */
	public List<WmsProduct> listProds(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return wmsProductDao.listProds(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listProdsCount(String[]args){
		return wmsProductDao.listProdsCount(args);
	}
	/**
	 * 根据商品id查询商品实体 <br>
	 */
	public WmsProduct wmsProDesc(Long wprId){
		return wmsProductDao.wmsProDesc(wprId);
	}
	/**
	 * 根据商品的编号查询商品实体 <br>
	 */
	public List findbyWprCode(String wprCode){
		return wmsProductDao.findbyWprCode(wprCode);
	}
	/**
	 * 修改商品信息 <br>
	 */
	public void wmsProUpdate(WmsProduct wmsProduct){
		wmsProductDao.wmsProUpdate(wmsProduct);
	}
	/**
	 * 删除商品 <br>
	 */
	public void deletePro(WmsProduct wmsProduct){
		wmsProductDao.delete(wmsProduct);
	}
	/**
	 * 按商品名称/型号/编号查询 <br>
	 */
	public List proSearch(String wprName,String agr){
		return wmsProductDao.proSearch(wprName,agr);
	}

	/**
	 * 判断商品名称型号是否重复 <br>
	 */
	public List proCheck(String wprName){
		return wmsProductDao.proCheck(wprName);
	}
	/**
	 * 查询未分类的商品 <br>
	 */
	public List<WmsProduct> noTypePro(){
		return wmsProductDao.noTypePro();
	}
	/**
	 * 获得待删除的所有商品列表 <br>
	 */
	public List findDelProduct(int pageCurrentNo, int pageSize) {
		return wmsProductDao.findDelProduct(pageCurrentNo, pageSize);
	}
	/**
	 * 获得待删除的所有商品数量 <br>
	 */
	public int findDelProductCount() {
		return wmsProductDao.findDelProductCount();
	}
	
	/**
	 * 按商品名称/型号/编号查询 <br>
	 * @param wprName 商品名称/型号/编号
	 * @return List 返回商品列表
	 */
	public List<WmsProduct> searchProdByName(String wprName){
		return wmsProductDao.searchProdByName(wprName);
	}
//-------- 商品类别 ------------
	/**
	 * 查询所有商品类别
	 */
	public List findAllWpt(){
		return wmsProTypeDao.findAll();
	}
	
	/**
	 * 根据客户Id查询商品类别<br>
	 * @param cusId 客户Id
	 * @return List<WmsProType> 商品类别<br>
	 */
	public List<WmsProType> findByCusId(String cusId){
		return wmsProTypeDao.findByCusId(cusId);
	}
	
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
			String isDe, int currentPage, int pageSize){
		return prodTransDAO.listByProdId(prodId, orderItem, isDe, currentPage, pageSize);
	}
	public int listProdTransByProdIdCount(String prodId){
		return prodTransDAO.listByProdIdCount(prodId);
	}
	
	public List<ProdTrans>  listPtrByProdIds(String prodIds, String expId, String provId, String cityId, String districtId){
		return prodTransDAO.listByProdIds(prodIds, expId, provId, cityId, districtId);
	}
	
	public void saveProdTrans(ProdTrans prodTrans){
		prodTransDAO.save(prodTrans);
	}
	public void updProdTrans(ProdTrans prodTrans){
		prodTransDAO.update(prodTrans);
	}
	public void deleteProdTrans(String id){
		prodTransDAO.deleteById(id);
	}
	public ProdTrans findProdTrans(String id){
		return prodTransDAO.findById(Long.parseLong(id));
	}
	
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
			String isDe, int currentPage, int pageSize){
		return prodSalBackDAO.list(prodId, orderItem, isDe, currentPage, pageSize);
	}
	public int listProdSalBackByProdIdCount(String prodId){
		return prodSalBackDAO.listCount(prodId);
	}
	
	public List<ProdSalBack>  listAllPsbByProdId(String prodId){
		return prodSalBackDAO.listByProdId(prodId);
	}
	
	public void saveProdSalBack(ProdSalBack prodSalBack){
		prodSalBackDAO.save(prodSalBack);
	}
	public void updProdSalBack(ProdSalBack prodSalBack){
		prodSalBackDAO.merge(prodSalBack);
	}
	public void deleteProdSalBack(ProdSalBack prodSalBack){
		prodSalBackDAO.delete(prodSalBack);
	}
	public ProdSalBack findProdSalBack(String id){
		return prodSalBackDAO.findById(Long.parseLong(id));
	}
	
	public void setLimRightDao(LimRightDAO limRightDao) {
		this.limRightDao = limRightDao;
	}
	public void setUserDao(LimUserDAO userDao) {
		this.userDao = userDao;
	}
	public void setWmsProTypeDao(WmsProTypeDAO wmsProTypeDao) {
		this.wmsProTypeDao = wmsProTypeDao;
	}
	public void setWmsProductDao(WmsProductDAO wmsProductDao) {
		this.wmsProductDao = wmsProductDao;
	}
	public void setProdTransDAO(ProdTransDAO prodTransDAO) {
		this.prodTransDAO = prodTransDAO;
	}
	public void setProdSalBackDAO(ProdSalBackDAO prodSalBackDAO) {
		this.prodSalBackDAO = prodSalBackDAO;
	}
}