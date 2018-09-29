package com.psit.struts.BIZ;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SalPaidPlan;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatSalMon;
import com.psit.struts.form.StatSalMForm;
/**
 * 订单回款BIZ <br>
 */
public interface PaidBIZ {
	/**
	 * 得到订单已回款金额 <br>
	 * @param code 订单id
	 * @return String 返回订单已回款金额
	 */
	public String getTotalPaid(String code);
	/**
	 * 保存回款记录 <br>
	 * @param paid 回款记录实体
	 */
	public void savePaidPast(SalPaidPast paid);
	/**
	 * 更新回款记录 <br>
	 * @param paid 回款记录实体
	 */
	public void updatePaidPast(SalPaidPast paid,double oldPayMon);
	/**
	 * 执行回款 <br>
	 * @param paidCode 回款记录id
	 */
	public void setPaidPlanCompleted(String paidCode);
	/**
	 * 执行开票 <br>
	 * @param paidCode 回款记录id
	 */
//	public void setPaidPastCompleted(String paidCode);
	/**
	 * 彻底删除 <br>
	 * @param salPaid 回款记录实体
	 */
	public void deletePaidPast(String paidId);
	/**
	 * 回款记录详情 <br>
	 * @param code 回款记录id
	 * @return SalPaidPast 返回回款记录实体
	 */
	public SalPaidPast getPaidPast(String code);
	/**
	 * 根据id获得回款计录（回收站里用） <br>
	 * @param id 回款记录id
	 * @return SalPaidPast 返回回款记录实体
	 */
	public SalPaidPast getPaidPast(Long id);
	/**
	 * 删除回款记录（进入回收站） <br>
	 * @param paidId 回款记录id
	 * @param ordId 订单ID
	 */
//	public void invPaidPast(SalPaidPast paid);
	/**
	 * 恢复回款记录，更新订单已回款 <br>
	 * @param paidId 回款记录ID
	 * @param ordId 订单ID
	 */
//	public void recovery(SalPaidPast paid);
	/**
	 * 按月查询已回款总额 <br>
	 * @param userId 用户ID
	 * @param year 年份
	 * @param month 月份
	 * @return String 返回回款总额
	 */
	public String getPastMonSum(String userId, String year, String month);
	/**
	 * 按月查询下级已回款总额 <br>
	 * @param userId 用户ID
	 * @param year 年份
	 * @param month 月份
	 * @return String 返回回款总额
	 */
	public String getPastSumWithUNum(String userId, String year, String month);
	/**
	 * 得到已回款总额 <br>
	 * @param empId 员工ID
	 * @return String 返回回款总额
	 */
	public String getPastAllSum(String empId);
	/**
	 * 保存回款计划 <br>
	 * @param transientInstance 回款计划实体
	 */
	public void savePaidPlan(SalPaidPlan transientInstance);
	/**
	 * 更新回款计划 <br>
	 * @param paid 回款计划实体
	 */
	public void updatePaidPlan(SalPaidPlan paid);
	/**
	 * 彻底删除 <br>
	 * @param persistentInstance 回款计划实体
	 */ 
	public void deletePaidPlan(SalPaidPlan persistentInstance);
	/**
	 * 回款计划详情 <br>
	 * @param code 回款计划id
	 * @return SalPaidPlan 返回回款计划实体
	 */
	public SalPaidPlan getPaidPlan(String code);
	/**
	 * 删除回款计划（进入回收站） <br>
	 * @param paidId 回款计划id
	 */
	public void invPaidPlan(Long paidId);
	/**
	 * 按月查询回款计划总额 <br>
	 * @param userId 用户ID
	 * @param year 年份
	 * @param month 月份
	 * @return String 返回回款计划总额 
	 */
	public String getPlanMonSum(String userId, String year, String month);
	/**
	 * 按月查询下级回款计划总额 <br>
	 * @param userId 用户Id
	 * @param year 年份
	 * @param month 月份
	 * @return String 返回回款计划总额 
	 */
	public String getPlanSumWithUNum(String userId, String year, String month); 
	/**
	 * 按时间查询回款计划列表 <br>
	 * @param userId 用户ID
	 * @param startDate 计划回款日期(开始)
	 * @param endDate 计划回款日期(结束)
	 * @return List 返回回款计划列表
	 */
	public List<SalPaidPlan> getPlanByTime(String userId,Date startDate, Date endDate);
	/**
	 * 过期回款计划 <br>
	 * @param userId 用户ID
	 * @param endDate 日期
	 * @return List 返回回款计划列表
	 */
	public List<SalPaidPlan> getPastPlan(String userId,Date endDate);
	/**
	 * 根据id获得回款计划（回收站里用） <br>
	 * @param id 回款计划id
	 * @return SalPaidPlan 返回回款计划实体
	 */
	public SalPaidPlan getPaidPlan(Long id);
	
	/**
	 * 获得待删除的所有回款计划 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回回款计划列表
	 */
	public List findDelPaidPlan(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有回款计划数量 <br>
	 * @return int 返回回款计划列表数量
	 */
	public int findDelPaidPlanCount();
	/**
	 * 获得待删除的所有回款记录 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回回款记录列表
	 */
//	public List findDelPaidPast(int pageCurrentNo, int pageSize);
	/**
	 * 
	 * 获得待删除的所有回款记录数量 <br>
	 * create_date: Aug 9, 2010,5:12:22 PM <br>
	 * @return int 返回回款记录列表数量
	 */
//	public int findDelPaidPastCount();
	
	/**
	 * 回款计划列表 <br>
	 * @param args 	[0]是否删除(1已删除,0未删除); [1]查询范围(0我的,1全部); [2]用户码;  [3]摘要;  [4]回款日期(开始);  [5]回款日期(结束);  
	 * 				[6]列表筛选类型;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalPaidPlan> 回款计划列表
	 */
	public List<SalPaidPlan> listPaidPlans(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPaidPlansCount(String[]args);
	
	/**
	 * 回款记录列表 <br>
	 * create_date: Jan 31, 2011,11:53:30 AM <br> 
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]查询范围(0我的,1全部)/订单ID;  [2]用户ID;  [3][4]回款日期范围 [5]对应客户 [6]回款人
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalPaidPast> 回款记录列表
	 */
	public List<SalPaidPast> listPaidPasts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPaidPastsCount(String[]args);
	
	/**
	 * 统计中回款记录列表 <br>
	 * create_date: Jan 31, 2011,11:53:30 AM <br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [2]统计类型; [3]统计对象; [4]员工ID集合;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalPaidPast> 回款记录列表
	 */
	public List<SalPaidPast> listStatPaid(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatPaidCount(String[]args);
	
	/**
	 * 回款记录人员月度统计 <br>
	 * create_date: Feb 25, 2011,1:58:01 PM <br>
	 * @param args [0][1]回款月份; [2]员工ID集合
	 * @return StaTable 统计结果
	 */
	public StaTable statPaidEmpMon(String[]args);
	
	/**
	 * 销售月度统计 <br>
	 * @param args [0,1]统计月份区间;
	 * @return List<StatSalMon> 统计结果
	 */
	public List<StatSalMon> statPaidMon(String[]args);
	
	/**
	 * 销售月度统计 <br>
	 * @param args [0,1]统计月份区间; [2]客户ID（客户详情下销售趋势专有参数）
	 * @return List<StatSalMon> 统计结果
	 */
	public List<StatSalMon> statCusPaidMon(String [] args);
	
	/**
	 * 回款分析统计<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSpsAnalyse(String startDate, String endDate);
}