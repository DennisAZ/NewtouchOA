package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SalTask;
/**
 * 销售工作数据库操作接口类 <br>
 */
public interface SalTaskDAO {
	/**
	 * 保存销售工作<br>
	 * create_date: Aug 12, 2010,4:08:07 PM<br>
	 * @param transientInstance 销售工作对象<br>
	 */
	public void save(SalTask transientInstance);
	/**
	 * 销售工作查询数量<br>
	 * create_date: Aug 12, 2010,4:08:44 PM<br>
	 * @param args 0:stTitle 工作主题
	 * 			   1:工作状态
	 * 			   2:发布人Id
	 * @return int 销售工作查询数量<br>
	 */
	public int getCount(String [] args);
	/**
	 * 销售工作查询<br>
	 * create_date: Aug 12, 2010,4:13:40 PM<br>
	 * @param args 0: 工作主题
	 * 			   1:工作状态
	 * 			   2:发布人Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页码
	 * @param pageSize 每页显示数量
	 * @return List 销售工作记录列表<br>
	 */
	public List salTaskSearch(String [] args, String orderItem, String isDe ,int currentPage,int pageSize);
	//查询所有
	//public int getCount();
	//public List salTaskAllSearch(int currentPage,int pageSize);
	
	/**
	 * 查看销售工作详情<br>
	 * create_date: Aug 12, 2010,4:15:14 PM<br>
	 * @param stId 销售工作id
	 * @return SalTask 销售工作对象<br>
	 */
	public SalTask salTaskDesc(Long stId);
	/**
	 * 工作更新<br>
	 * create_date: Aug 13, 2010,9:34:42 AM<br>
	 * @param salTask 销售工作对象<br>
	 */
	public void updateSalTask(SalTask salTask);
	/**
	 * 删除工作<br>
	 * create_date: Aug 13, 2010,9:35:01 AM<br>
	 * @param persistentInstance 销售工作对象<br>
	 */
	public void delete(SalTask persistentInstance);
	/**
	 * 获得待删除的所有工作安排<br>
	 * create_date: Aug 13, 2010,9:35:20 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @return List 销售工作记录列表<br>
	 */
	public List findDelSalTask(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有工作安排数量<br>
	 * create_date: Aug 13, 2010,9:35:57 AM<br>
	 * @return int 销售工作记录数<br>
	 */
	public int findDelSalTaskCount();

	/**
	 * 得到待执行的发布的工作<br>
	 * create_date: Aug 13, 2010,9:40:55 AM<br>
	 * @param userId 发布人id
	 * @return List<SalTask> 销售工作记录列表<br>
	 */
	public List<SalTask> getTodoTask(String userId);
	/**
	 * 得到某人发布的所有工作<br>
	 * create_date: Aug 13, 2010,9:41:17 AM<br>
	 * @param userId 发布人id
	 * @return List<SalTask> 销售工作记录列表<br>
	 */
	public List<SalTask> findTaskByUser(String userId);
}