package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.form.StatSalMForm;
/**
 * 回款记录数据库操作接口类<br>
 */
public interface SalPaidPastDAO {
	
	/**
	 * 保存<br>
	 */
	public void save(SalPaidPast paid);
	
	/**
	 * 更新<br>
	 */
	public void updatePaid(SalPaidPast paid, double oldPayMon);
	
	/**
	 * 完全删除<br>
	 */
	public void delete(SalPaidPast paid);
	
	/**
	 * 详情<br>
	 */
	public SalPaidPast findById(Long id);
	/**
	 * 根据id获得回款计录（回收站里用）<br>
	 */
	public SalPaidPast getById(Long id);
	/**
	 * 得到订单已回款金额<br>
	 */
	public String getTotalPaid(String code);
	/**
	 * 删除回款记录（进入回收站） <br>
	 */
//	public void invalid(SalPaidPast paid);
	/**
	 * 恢复回款记录，更新订单已回款 <br>
	 */
//	public void recovery(SalPaidPast paid);
	
	/**
	 * 查询回款记录<br>
	 */
//	public List findPaidPast(int pageCurrentNo, int pageSize, String attSql, String isdel);
	/**
	 * 查询回款记录数量<br>
	 */
//	public int findPaidPastCount(String attSql, String isdel);
	/**
	 * 获得待删除的所有回款记录<br>
	 */
//	public List findDelPaidPast(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有回款记录数量<br>
	 */
//	public int findDelPaidPastCount();
	
	/**
	 * 按月查询已回款总额<br>
	 */
	public String getPastMonSum(String userCode, String year, String month);
	/**
	 * 按月查询下级已回款总额<br>
	 */
	public String getPastSumWithUNum(String empId, String year, String month);
	
	/**
	 * 得到已回款总额<br>
	 */
	public String getPastAllSum(String empId);
	
	/**
	 * 回款记录列表 <br>
	 */
	public List<SalPaidPast> listPaidPasts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPaidPastsCount(String[]args);
	
	/**
	 * 统计中回款记录列表 <br>
	 */
	public List<SalPaidPast> listStatPaid(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatPaidCount(String[]args);
	
	/**
	 * 回款记录人员月度统计 <br>
	 */
	public StaTable statPaidEmpMon(String[]monArray,String[]args);
	
	/**
	 * 回款记录月度统计 <br>
	 */
	public List<StatOrd> statOrdMon(String[]args);
	
	/**
	 * 回款记录月度统计(客户详情下) <br>
	 */
	public List<StatOrd> statCusOrdMon(String[]args);
	
	/**
	 * 回款分析统计<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSpsAnalyse(String startDate, String endDate);
}