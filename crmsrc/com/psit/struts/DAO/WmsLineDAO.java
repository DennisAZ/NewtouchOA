package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsLine;
/**
 * 
 * 库存流水DAO <br>
 *
 * create_date: Aug 18, 2010,4:16:25 PM<br>
 * @author zjr
 */
public interface WmsLineDAO {
	/**
	 * 
	 * 保存库存流水 <br>
	 * create_date: Aug 12, 2010,10:43:10 AM <br>
	 * @param wmsLine 库存流水实体
	 */
	public void save(WmsLine wmsLine);
	/**
	 * 
	 * 根据单据类型,明细编号查询其流水 <br>
	 * create_date: Aug 12, 2010,10:43:26 AM <br>
	 * @param wliType 流水类型
	 * @param wliWmsId 库存明细id
	 * @return List 返回库存流水列表
	 */
	public List findByWmsId( String wliType,Long wliWmsId);
	/**
	 * 
	 * 库存流水查询列表数量(按单据类型,仓库编号,产品名称,创建日期) <br>
	 * create_date: Aug 12, 2010,10:50:45 AM <br>
	 * @param wliType 单据类型
	 * @param wmsCode 仓库编号
	 * @param wprName 产品名称
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回库存流水列表数量
	 */
	public int getWliCount(String wliType,String wmsCode,String wprName,String startTime,String endTime);
	/**
	 * 
	 * 库存流水查询列表(按单据类型,仓库编号,产品名称,创建日期) <br>
	 * create_date: Aug 12, 2010,10:54:36 AM <br>
	 * @param wliType 单据类型
	 * @param wmsCode 仓库编号
	 * @param wprName 产品名称
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List WliSearch(String wliType,String wmsCode,String wprName,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 更新库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:11 AM <br>
	 * @param instance 库存流水实体
	 */
	public void updateWli(WmsLine instance);
	/**
	 * 
	 * 删除库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:27 AM <br>
	 * @param wmsLine 库存流水实体
	 */
	public void delete(WmsLine wmsLine);
	/**
	 * 
	 * 根据单据编号查询其流水 <br>
	 * create_date: Aug 12, 2010,10:59:39 AM <br>
	 * @param wliType 单据类型
	 * @param wliTypeCode 单据编号
	 * @return List 返回库存流水列表
	 */
	public List findByTypeCode( String wliType,String wliTypeCode);
	/**
	 * 
	 * 查看商品的库存流水列表数量 <br>
	 * create_date: Aug 12, 2010,11:02:06 AM <br>
	 * @param wprCode 商品编号
	 * @param isAll 状态标识
	 * @return int 返回库存流水列表数量
	 */
	public int getWliCountByWpr(Long wprCode,String isAll);
	/**
	 * 
	 * 查看商品的库存流水列表 <br>
	 * create_date: Aug 12, 2010,11:06:50 AM <br>
	 * @param wprCode 商品编号
	 * @param isAll 状态标识
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List findByWpr(Long wprCode,String isAll,int currentPage,int pageSize);
	/**
	 * 
	 * 生成商品的库存流水的XML <br>
	 * create_date: Aug 12, 2010,11:08:37 AM <br>
	 * @param wprCode 商品id
	 * @param isAll 状态标识
	 * @return List 返回库存流水列表
	 */
	public List findByWpr(Long wprCode,String isAll);
	/**
	 * 
	 * 查询某个仓库的流水 <br>
	 * create_date: Aug 12, 2010,11:24:28 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回流水列表
	 */
	public List wliSearch(String wmsCode);
	/**
	 * 
	 * 根据单据类型删除指定的库存流水 <br>
	 * create_date: Aug 12, 2010,11:26:12 AM <br>
	 * @param wliType 单据类型
	 * @param wliTypeCode 单据id
	 */
	public void updateByCode(String wliType,String wliTypeCode);
	/**
	 * 
	 * 获得待删除的所有库存流水 <br>
	 * create_date: Aug 12, 2010,11:52:33 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存流水列表
	 */
	public List findDelWmsLine(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有库存流水数量 <br>
	 * create_date: Aug 12, 2010,11:53:09 AM <br>
	 * @return int 返回库存流水列表数量
	 */
	public int findDelWmsLineCount();
	/**
	 * 
	 * 根据Id获得库存流水 <br>
	 * create_date: Aug 12, 2010,10:56:46 AM <br>
	 * @param id 库存流水id
	 * @return WmsLine 返回库存流水实体
	 */
	public WmsLine findById(Long id);
	/**
	 * 
	 * 根据商品id查询库存流水 <br>
	 * create_date: Aug 12, 2010,12:04:08 PM <br>
	 * @param wprId 商品id
	 * @return List 返回流水列表
	 */
	public List findByWpr(Long wprId);
}
