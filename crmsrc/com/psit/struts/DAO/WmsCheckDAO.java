package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsCheck;
/**
 * 
 * 库存盘点DAO <br>
 *
 * create_date: Aug 18, 2010,3:43:34 PM<br>
 * @author zjr
 */
public interface WmsCheckDAO {
	/**
	 * 
	 * 库存盘点查询列表数量(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:20:27 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回库存盘点列表数量
	 */
	public int getWmcCount(String wmcTitle,String wmsCode,String wmcAppIsok,
			String wmcState,String startTime,String endTime);
	/**
	 * 
	 * 库存盘点查询列表(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:29:10 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存盘点列表
	 */
	public List wmcSearch(String wmcTitle,String wmsCode,String wmcAppIsok,String wmcState,
			String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 保存库存盘点 <br>
	 * create_date: Aug 12, 2010,10:30:44 AM <br>
	 * @param wmsCheck 库存盘点实体
	 */
	public void save(WmsCheck wmsCheck);
	/**
	 * 
	 * 根据Id查看盘点单据实体 <br>
	 * create_date: Aug 12, 2010,10:32:14 AM <br>
	 * @param id 盘点单id
	 * @return WmsCheck 返回盘点单据实体
	 */
	public WmsCheck findById(java.lang.Long id);
	/**
	 * 
	 * 更新盘点单据 <br>
	 * create_date: Aug 12, 2010,10:41:52 AM <br>
	 * @param wmsCheck 盘点单id
	 */
	public void updateWmc(WmsCheck wmsCheck);
	/**
	 * 
	 * 查询指定仓库的盘点 <br>
	 * create_date: Aug 12, 2010,11:13:49 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回盘点列表
	 */
	public List wmcSearch(String wmsCode);
	/**
	 * 
	 * 根据仓库编号查询需要审核的盘点单数量 <br>
	 * create_date: Aug 12, 2010,11:43:50 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回盘点单列表数量
	 */
	public int findApp(String wmsCode,String isok);
	/**
	 * 
	 * 根据仓库编号查询雪要盘点的单据数量 <br>
	 * create_date: Aug 12, 2010,11:44:39 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回盘点单列表数量
	 */
	public int findInWms(String wmsCode);
	/**
	 * 
	 * 获得待删除的所有盘点记录列表 <br>
	 * create_date: Aug 12, 2010,11:49:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回盘点记录列表
	 */
	public List findDelWmsCheck(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有盘点记录数量 <br>
	 * create_date: Aug 12, 2010,11:50:28 AM <br>
	 * @return int 返回盘点记录列表数量
	 */
	public int findDelWmsCheckCount();
	/**
	 * 
	 * 删除盘点单据 <br>
	 * create_date: Aug 12, 2010,10:42:39 AM <br>
	 * @param persistentInstance 盘点单据实体
	 */
	public void delete(WmsCheck persistentInstance);
	/**
	 * 
	 * 带删除状态查出盘点实体 <br>
	 * create_date: Aug 12, 2010,11:57:48 AM <br>
	 * @param wmcId 盘点记录id
	 * @return WmsCheck 返回盘点记录实体
	 */
	public WmsCheck findWmc(Long wmcId);
	/**
	 * 
	 * 根据盘点单号查询 <br>
	 * create_date: Aug 12, 2010,12:00:15 PM <br>
	 * @param wmcCode 盘点单号
	 * @return List 返回盘点记录列表
	 */
	public List findByWmcCode(String wmcCode);
}
