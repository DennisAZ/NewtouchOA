package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.SalPaidPlan;
/**
 * 回款计划数据库操作接口类 <br>
 */
public interface SalPaidPlanDAO {
	/**
	 * 保存回款计划<br>
	 */
	public void save(SalPaidPlan transientInstance);
	
	/**
	 * 更新回款计划<br>
	 */
	public void updatePaid(SalPaidPlan paid);
	
	/**
	 * 删除回款计划（进入回收站）<br>
	 */
	public void invalid(Long paidId);
	
	/**
	 * 完全删除回款计划<br>
	 */
	public void delete(SalPaidPlan persistentInstance);
	
	/**
	 * 回款计划详情(带删除状态)<br>
	 */
	public SalPaidPlan findById(Long id);
	/**
	 * 根据id获得回款计划（回收站里用）<br>
	 */
	public SalPaidPlan getById(Long id);
	/**
	 * 获得待删除的所有回款计划<br>
	 */
	public List findDelPaidPlan(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有回款计划数量<br>
	 */
	public int findDelPaidPlanCount();
	
	/**
	 * 按月查询计划总额<br>
	 */
	public String getPlanMonSum(String userId, String year, String month);
	/**
	 * 按月查询下级计划总额<br>
	 */
	public String getPlanSumWithUNum(String userId, String year, String month);
	
	/**
	 * 按时间查询自己及下级的未回款计划列表<br>
	 */
	public List<SalPaidPlan> getPlanByTime(String userId,Date startDate, Date endDate);
	/**
	 * 查询过期计划
	 */
	public List<SalPaidPlan> getPastPlan(String userId,Date endDate);
	/**
	 * 回款计划列表 <br>
	 */
	public List<SalPaidPlan> listPaidPlans(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPaidPlansCount(String[]args);
}