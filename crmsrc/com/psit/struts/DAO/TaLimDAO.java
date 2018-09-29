package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.TaLim;
/**
 * 
 * 工作执行人DAO <br>
 *
 * create_date: Aug 18, 2010,2:12:28 PM<br>
 * @author zjr
 */
public interface TaLimDAO {
	/**
	 * 
	 * 保存工作安排执行人 <br>
	 * create_date: Aug 10, 2010,5:06:12 PM <br>
	 * @param taLim 工作安排执行人实体
	 */
	public void save(TaLim taLim);
	/**
	 * 
	 * 根据工作Id删除执行人 <br>
	 * create_date: Aug 10, 2010,5:06:31 PM <br>
	 * @param stId 工作安排id
	 */
	public void delByTaskId(Long stId);
	/**
	 * 
	 * 某人的工作安排查询列表数量（按主题,状态，完成日期） <br>
	 * create_date: Aug 10, 2010,5:07:17 PM <br>
	 * @param args 0:工作主题
	 * 			   1:工作状态
	 *    		   2:工作执行中标识
	 *    		   3:日期标识
	 *    		   4:员工Id
	 * @return int  返回工作安排列表数量
	 */
	public int myTaskCount(String [] args);
	/**
	 * 
	 * 某人的工作安排查询列表（按主题,状态，完成日期） <br>
	 * create_date: Aug 10, 2010,5:10:14 PM <br>
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
	 * 
	 * 根据工作安排Id查询执行人 <br>
	 * create_date: Aug 10, 2010,5:11:37 PM <br>
	 * @param stId 工作id
	 * @return List 返回工作执行人列表
	 */
	public List findByTaskId(Long stId);
	/**
	 * 
	 * 更新工作执行人 <br>
	 * create_date: Aug 10, 2010,5:12:26 PM <br>
	 * @param taLim 工作执行人实体
	 */
	public void updateTaskMan(TaLim taLim);
	/**
	 * 
	 * 删除执行人 <br>
	 * create_date: Aug 10, 2010,5:12:55 PM <br>
	 * @param taLim 工作执行人实体
	 */
	public void delete(TaLim taLim);
	/**
	 * 
	 * 根据工作Id查询批量删除执行人 <br>
	 * create_date: Aug 10, 2010,5:13:13 PM <br>
	 * @param stId 工作Id
	 */
	public void updateByTaskId(Long stId);
	/**
	 * 
	 * 根据工作Id查询所有执行人 <br>
	 * create_date: Aug 10, 2010,5:13:28 PM <br>
	 * @param stId 工作Id
	 * @return List 返回工作执行人列表
	 */
	public List findAllTaskMan(Long stId);
	/**
	 * 
	 * 根据工作执行人ID查询实体 <br>
	 * create_date: Aug 10, 2010,5:14:02 PM <br>
	 * @param id 工作执行人id
	 * @return TaLim 返回工作执行人实体
	 */
	public TaLim findById(Long id);
	/**
	 * 
	 * 根据完成时限查询收到的工作 <br>
	 * create_date: Aug 10, 2010,5:15:19 PM <br>
	 * @param userId 员工Id
	 * @param startDate 完成日期(开始)
	 * @param endDate 完成日期(结束)
	 * @param stu 状态
	 * @return List 返回工作执行人列表
	 */
	public List getMyTaskByDate(String userId,Date startDate,Date endDate,String stu);
	/**
	 * 
	 * 得到未设定完成时限的收到的工作 <br>
	 * create_date: Aug 10, 2010,5:14:47 PM <br>
	 * @param userId 员工Id
	 * @param stu 工作状态
	 * @return List 返回工作列表
	 */
	public List getMyTaskOfNoDate(String userId,String stu);
	/**
	 * 
	 * 得到待执行的收到的工作 <br>
	 * create_date: Aug 10, 2010,5:18:01 PM <br>
	 * @param userId 员工Id
	 * @return List<TaLim> 返回工作执行人列表
	 */
	public List<TaLim> getTodoMyTask(String userId);
}
