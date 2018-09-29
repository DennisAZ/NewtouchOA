package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsChange;
/**
 * 
 * 库存调拨DAO <br>
 *
 * create_date: Aug 18, 2010,2:54:10 PM<br>
 * @author zjr
 */
public interface WmsChangeDAO {
	/**
	 * 
	 * 保存仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:23:16 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void save(WmsChange wmsChange);
	/**
	 * 
	 * 仓库调拨查询列表数量(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:24:24 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回调拨查询列表数量
	 */
	public int getWchCount(String wchTitle,String wmsCode,String wchAppIsok,
			String wchState,String startTime,String endTime);
	/**
	 * 
	 * 仓库调拨查询列表(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:27:35 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨查询列表
	 */
	public List wchSearch(String wchTitle,String wmsCode,String wchAppIsok,String wchState,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 根据id查询仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:33:40 PM <br>
	 * @param wchId 仓库调拨id
	 * @return WmsChange 返回仓库调拨实体
	 */
	public WmsChange WchDesc(Long wchId);
	/**
	 * 
	 * 更新仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:55:07 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void update(WmsChange wmsChange);
	/**
	 * 
	 * 删除仓库调拨单 <br>
	 * create_date: Aug 11, 2010,5:56:11 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void delete(WmsChange wmsChange);
	/**
	 * 
	 * 根据入库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,9:58:18 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch(String wmsCode);
	/**
	 * 
	 * 根据出库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,10:10:01 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch2(String wmsCode);
	/**
	 * 
	 * 根据仓库编号查询需要审核的出库单数量 <br>
	 * create_date: Aug 12, 2010,11:34:23 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回出库单列表数量
	 */
	public int findApp(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询要审核的调拨调入数量 <br>
	 * create_date: Aug 12, 2010,11:40:11 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回调拨单列表数量
	 */
	public int findApp2(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询需要调拨的单据数量 <br>
	 * create_date: Aug 12, 2010,11:41:58 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回调拨单列表数量
	 */
	public int findInWms(String wmsCode);
	/**
	 * 
	 * 获得待删除的所有调拨单列表 <br>
	 * create_date: Aug 12, 2010,11:47:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨单列表
	 */
	 public List findDelWmsChange(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有调拨单数量 <br>
	 * create_date: Aug 12, 2010,11:49:22 AM <br>
	 * @return int 返回调拨单列表数量
	 */
	 public int findDelWmsChange();
	/**
	 * 
	 * 带删除状态查出调拨单实体 <br>
	 * create_date: Aug 12, 2010,11:56:07 AM <br>
	 * @param wchId 调拨单id
	 * @return WmsChange 返回调拨单实体
	 */
	public WmsChange findWch(Long wchId);
	/**
	 * 
	 * 根据调拨单号查询调拨单 <br>
	 * create_date: Aug 12, 2010,11:59:34 AM <br>
	 * @param wchCode 调拨单号
	 * @return List 返回调拨单列表
	 */
	public List findBywchCode(String wchCode);
	
}
