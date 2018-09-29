package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.RStroPro;
/**
 * 
 * 仓库明细DAO <br>
 *
 * create_date: Aug 17, 2010,2:55:31 PM<br>
 * @author zjr
 */
public interface RStroProDAO {
	/**
	 * 
	 * 保存仓库明细 <br>
	 * create_date: Aug 11, 2010,2:45:31 PM <br>
	 * @param rstroPro 仓库明细实体
	 */
	public void save(RStroPro rstroPro);
	/**
	 * 
	 * 查询仓库所有商品 <br>
	 * create_date: Aug 11, 2010,2:55:12 PM <br>
	 * @return List 返回仓库明细列表
	 */
	public List findAll();
	/**
	 * 
	 * 更新仓库明细表 <br>
	 * create_date: Aug 11, 2010,2:56:10 PM <br>
	 * @param rsp 仓库明细实体
	 */
	public void updatePsp(RStroPro rsp);
	/**
	 * 
	 * 根据仓库编号查询仓库商品 <br>
	 * create_date: Aug 11, 2010,2:56:36 PM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回仓库明细列表
	 */
	public List findByWmsCode(String wmsCode);
	/**
	 * 
	 * 按商品id,仓库编号查询库存明细列表数量 <br>
	 * create_date: Aug 11, 2010,3:01:40 PM <br>
	 * @param wprCode 商品id
	 * @param wmsCode 仓库编号
	 * @return int 返回库存明细列表数量
	 */
	public int getCountRwp(String wprCode,String wmsCode);
	/**
	 * 
	 * 按商品id,仓库编号查询库存明细列表 <br>
	 * create_date: Aug 11, 2010,3:03:58 PM <br>
	 * @param wprCode 商品id
	 * @param wmsCode 仓库编号
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存明细列表
	 */
	public List rwpSearch(String wprCode,String wmsCode,int currentPage,int pageSize);
	/**
	 * 
	 * 查询所有仓库明细列表数量 <br>
	 * create_date: Aug 11, 2010,3:05:19 PM <br>
	 * @return int 返回库存明细列表数量
	 */
	public int getCountRStroPro();
	/**
	 * 
	 * 查询所有仓库明细列表 <br>
	 * create_date: Aug 11, 2010,3:05:38 PM <br>
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存明细列表
	 */
	public List rStroProSearch(int currentPage,int pageSize);
	/**
	 * 
	 * 按商品名称和仓库编号查询仓库明细列表数量 <br>
	 * create_date: Aug 11, 2010,5:44:14 PM <br>
	 * @param wmsCode 仓库编号
	 * @param wprName 商品名称
	 * @return int 返回仓库明细列表数量
	 */
	public int getCount(String wmsCode,String wprName);
	/**
	 * 
	 * 按商品名称和仓库编号查询仓库明细列表 <br>
	 * create_date: Aug 11, 2010,5:49:43 PM <br>
	 * @param wmsCode 仓库编号
	 * @param wprName 商品名称
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回仓库明细列表
	 */
	public List findProByWms(String wmsCode,String wprName,int currentPage,int pageSize);
	/**
	 * 
	 * 查询某仓库商品的库存量 <br>
	 * create_date: Aug 12, 2010,9:49:04 AM <br>
	 * @param wmsCode 仓库编号
	 * @param wprCode 商品id
	 * @return List<RStroPro> 返回库存明细列表
	 */
	public List<RStroPro> findProNum(String wmsCode,Long wprCode);
	/**
	 * 
	 * 根据商品id查询商品的库存量 <br>
	 * create_date: Aug 11, 2010,3:12:57 PM <br>
	 * @param wprId 商品id
	 * @return List<RStroPro> 返回库存明细列表
	 */
	public List<RStroPro> rwpSearch(Long wprId);
	/**
	 * 
	 * 查询商品的库存总量 <br>
	 * create_date: Aug 11, 2010,3:14:12 PM <br>
	 * @param wprId 商品id
	 * @return Double 返回库存总量
	 */
	public Double getCountPro(Long wprId);
	/**
	 * 
	 * 查询仓库的库存总量 <br>
	 * create_date: Aug 11, 2010,3:20:00 PM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回库存总量
	 */
	public int getCountStro(String wmsCode);
	/**
	 * 
	 * 清除库存为0的商品数据 <br>
	 * create_date: Aug 12, 2010,10:20:05 AM <br>
	 * @param wmsCode 仓库编号
	 */
	public void delData(String wmsCode);
}