package com.psit.struts.DAO;

import java.sql.Date;
import java.util.List;

import com.psit.struts.entity.WmsWarIn;
/**
 * 
 * 入库单DAO <br>
 *
 * create_date: Aug 19, 2010,2:01:21 PM<br>
 * @author zjr
 */
public interface WmsWarInDAO {
	/**
	 * 
	 * 保存入库单 <br>
	 * create_date: Aug 11, 2010,2:33:07 PM <br>
	 * @param wmsWarIn 入库单实体
	 */
	public void save(WmsWarIn wmsWarIn);
	/**
	 * 
	 * 入库单查询列表数量(按入库单主题,仓库编号,审核状态,入库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,2:34:09 PM <br>
	 * @param wwiTitle 入库主题
	 * @param wmsCode 仓库主题
	 * @param wwiAppIsok 审核状态
	 * @param wwiState 入库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回入库单列表数量
	 */
	public int getWwiCount(String wwiTitle,String wmsCode,String wwiAppIsok,
			String wwiState,String startTime,String endTime);
	/**
	 * 
	 * 入库单查询列表(按入库单主题,仓库编号,审核状态,入库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,2:39:30 PM <br>
	 * @param wwiTitle 入库主题
	 * @param wmsCode 仓库主题
	 * @param wwiAppIsok 审核状态
	 * @param wwiState 入库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回入库单列表
	 */
	public List WwiSearch(String wwiTitle,String wmsCode,String wwiAppIsok,String wwiState
			,String startTime,String endTime,int currentPage,int pageSize);
	/**
	 * 
	 * 根据入库单Id查询实体 <br>
	 * create_date: Aug 11, 2010,2:40:43 PM <br>
	 * @param wwiId 入库单Id
	 * @return WmsWarIn 返回入库单实体
	 */
	public WmsWarIn findWwi(Long wwiId);
	/**
	 * 
	 * 更新入库单 <br>
	 * create_date: Aug 11, 2010,2:48:41 PM <br>
	 * @param wmsWarIn 入库单实体
	 */
	public void updateWwi(WmsWarIn wmsWarIn);
	/**
	 * 
	 * 删除未入库的入库单 <br>
	 * create_date: Aug 11, 2010,2:59:37 PM <br>
	 * @param wmsWarIn 入库单实体
	 */
	public void delete(WmsWarIn wmsWarIn);
	/**
	 * 
	 * 查询指定仓库的入库单 <br>
	 * create_date: Aug 11, 2010,3:20:52 PM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回入库单列表
	 */
	public List WwiSearch(String wmsCode);
	/**
	 * 
	 * 查询今天要审核的入库单数量 <br>
	 * create_date: Aug 11, 2010,3:27:19 PM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回入库单数量
	 */
	public int findApp(String wmsCode,String isok);
	/**
	 * 
	 * 查询今天要入库的入库单 <br>
	 * create_date: Aug 11, 2010,3:28:36 PM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回入库单数量
	 */
	public int findInWms(String wmsCode);
	/**
	 * 
	 * 获得待删除的所有入库单 <br>
	 * create_date: Aug 11, 2010,3:31:27 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回入库单列表
	 */
	public List findDelWarIn(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有入库单数量 <br>
	 * create_date: Aug 11, 2010,3:34:10 PM <br>
	 * @return int 返回入库单列表数量
	 */
	public int findDelWarInCount();
	/**
	 * 
	 * 带删除状态查出入库单实体 <br>
	 * create_date: Aug 11, 2010,3:34:45 PM <br>
	 * @param wwiId 入库单id
	 * @return WmsWarIn 返回入库单实体
	 */
	public WmsWarIn findWwi2(Long wwiId);
	/**
	 * 
	 * 根据入库单号查询 <br>
	 * create_date: Aug 11, 2010,3:39:43 PM <br>
	 * @param wwiCode 入库单号
	 * @return List 返回入库单列表
	 */
	public List findAll(String wwiCode);
	
}
