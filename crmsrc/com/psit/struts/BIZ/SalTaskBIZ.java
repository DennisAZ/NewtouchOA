package com.psit.struts.BIZ;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.TaLim;
/**
 * 工作安排BIZ <br>
 */
public interface SalTaskBIZ {
	
	/**
	 * 工作安排查询列表数量（按主题，状态,完成日期） <br>
	 * @param args 0:stTitle 工作主题
	 * 			   1:工作状态
	 * 			   2:发布人Id
	 * @return int 返回工作安排列表数量
	 */
	public int getCount1(String [] args);
	/**
	 * 工作安排查询列表（按主题，状态,完成日期） <br>
	 * @param args 0: 工作主题
	 * 			   1:工作状态
	 * 			   2:发布人Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回工作安排列表
	 */
	public List salTaskSearch(String [] args,String orderItem, String isDe,int currentPage,int pageSize);
	/**
	 * 查看工作安排详情 <br>
	 * @param stId 工作安排id
	 * @return SalTask 返回工作安排实体
	 */
	public SalTask salTaskDesc(Long stId);
	/**
	 * 更新工作安排 <br>
	 * @param salTask 工作安排实体
	 */
	public void updateSalTask(SalTask salTask);
	/**
	 * 删除工作安排 <br>
	 * @param salTask 工作安排实体
	 */
	public void delete(SalTask salTask);
	/**
	 * 保存工作安排执行人 <br>
	 * @param taLim 工作安排执行人实体
	 */
	public void saveTal(TaLim taLim);
	/**
	 * 根据工作Id删除执行人 <br>
	 * @param stId 工作安排id
	 */
	public void delByTaskId(Long stId);
	/**
	 * 某人的工作安排查询列表数量（按主题,状态，完成日期） <br>
	 * @param args 0:工作主题
	 * 			   1:工作状态
	 *    		   2:工作执行中标识
	 *    		   3:日期标识
	 *    		   4:员工Id
	 * @return int  返回工作安排列表数量
	 */
	public int myTaskCount(String [] args);
	/**
	 * 某人的工作安排查询列表（按主题,状态，完成日期） <br>
	 * @param args 0:工作主题
	 * 			   1:工作状态
	 *    		   2:工作执行中标识
	 *    		   3:日期标识
	 *    		   4:员工Id
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回工作安排列表
	 */
	public List myTaskSearch(String [] args,int currentPage,int pageSize,String orderItem, String isDe);
	/**
	 * 根据工作安排Id查询执行人 <br>
	 * @param stId 工作id
	 * @return List 返回工作执行人列表
	 */
	public List findByTaskId(Long stId);
	/**
	 * 更新工作执行人 <br>
	 * @param taLim 工作执行人实体
	 */
	public void updateTaskMan(TaLim taLim);
	/**
	 * 删除执行人 <br>
	 * @param taLim 工作执行人实体
	 */
	public void delete(TaLim taLim);
	/**
	 * 根据工作Id查询批量删除执行人 <br>
	 * @param stId 工作Id
	 */
	public void updateByTaskId(Long stId);
	/**
	 * 根据工作Id查询所有执行人 <br>
	 * @param stId 工作Id
	 * @return List 返回工作执行人列表
	 */
	public List findAllTaskMan(Long stId);
	/**
	 * 根据工作执行人ID查询实体 <br>
	 * @param id 工作执行人id
	 * @return TaLim 返回工作执行人实体
	 */
	public TaLim showTaLim(Long id);
	/**
	 * 得到未设定完成时限的收到的工作 <br>
	 * @param userId 员工Id
	 * @param stu 工作状态
	 * @return List 返回工作列表
	 */
	public List getMyTaskOfNoDate(String userId,String stu);
	/**
	 * 根据完成时限查询收到的工作 <br>
	 * @param userId 员工Id
	 * @param startDate 完成日期(开始)
	 * @param endDate 完成日期(结束)
	 * @param stu 状态
	 * @return List 返回工作执行人列表
	 */
	public List getMyTaskByDate(String userId,Date startDate,Date endDate,String stu);
	/**
	 * 得到待执行的发布的工作 <br>
	 * @param userId 发布人Id
	 * @return List<SalTask> 返回工作列表
	 */
	public List<SalTask> getTodoTask(String userId);
	/**
	 * 得到待执行的收到的工作 <br>
	 * @param userId 员工Id
	 * @return List<TaLim> 返回工作执行人列表
	 */
	public List<TaLim> getTodoMyTask(String userId);
	/**
	 * 获得待删除的所有工作安排 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回工作安排列表
	 */
	public List findDelSalTask(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有工作安排数量 <br>
	 * @return int 返回工作安排列表数量
	 */
	public int findDelSalTaskCount();
	/**
	 * 得到某人发布的所有工作 <br>
	 * @param userId 发布人Id
	 * @return List<SalTask> 返回工作安排列表
	 */
	public List<SalTask> findTaskByUser(String userId);
}