package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.entity.WmsWarOut;
/**
 * 
 * 出库单DAO <br>
 *
 * create_date: Aug 19, 2010,2:30:17 PM<br>
 * @author zjr
 */
public interface WmsWarOutDAO {
	/**
	 * 
	 * 保存出库单 <br>
	 * create_date: Aug 11, 2010,4:45:13 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void save(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 出库单查询列表数量(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:50:47 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回出库单列表数量
	 */
	public int getWwoCount(String wwoTitle,String wmsCode,String wwoAppIsok,String wwoState,
			String startTime,String endTime);
	/**
	 * 
	 * 出库单查询列表(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:55:05 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wwoTitle,String wmsCode,String wwoAppIsok,String wwoState,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 按出库单id查询出库单实体<br>
	 * create_date: Aug 11, 2010,4:59:17 PM <br>
	 * @param wwoId 库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut WwoSearchByCode(Long wwoId);
	/**
	 * 
	 * 更新出库单 <br>
	 * create_date: Aug 11, 2010,5:21:47 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void update(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 删除出库单 <br>
	 * create_date: Aug 11, 2010,5:22:07 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void delete(WmsWarOut wmsWarOut);
	/**
	 * 
	 * 查询指定仓库的出库单 <br>
	 * create_date: Aug 12, 2010,9:51:19 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wmsCode);
	/**
	 * 
	 * 查询订单下的出库单 <br>
	 * create_date: Aug 12, 2010,10:13:32 AM <br>
	 * @param sodCode 订单id
	 * @return List 返回订单列表
	 */
	public List findByOrd(String sodCode);
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
	 * 根据仓库编号查询需要出库的单据数量 <br>
	 * create_date: Aug 12, 2010,11:37:02 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回出库单列表数量
	 */
	public int findInWms(String wmsCode);
	/**
	 * 
	 * 获得待删除的所有出库单列表 <br>
	 * create_date: Aug 12, 2010,11:45:40 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List findDelWarOut(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有出库单列表数量 <br>
	 * create_date: Aug 12, 2010,11:47:23 AM <br>
	 * @return int 返回出库单列表数量
	 */
	public int findDelWarOutCount();
	/**
	 * 
	 * 带删除状态查出出库单实体 <br>
	 * create_date: Aug 12, 2010,11:54:19 AM <br>
	 * @param wwoId 出库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut findWwo(Long wwoId);
	/**
	 * 
	 * 根据出库单号查询出库单 <br>
	 * create_date: Aug 12, 2010,11:58:35 AM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库单列表
	 */
	public List findByWwoCode(String wwoCode);
	
}