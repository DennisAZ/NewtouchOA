package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.Report;
/**
 * 报告表的数据库操作实现类 <br>
 * create_date: Aug 9, 2010,10:40:37 AM<br>
 * @author 朱皖宁
 */
public interface ReportDAO  
{
	/**
	 * 保存报告<br>
	 * create_date: Aug 9, 2010,10:42:17 AM<br>
	 * @param report 报告表对象<br>
	 */
	public void saveReport(Report report);
	/**
	 * 获得特定账号已提交的报告<br>
	 * create_date: Aug 9, 2010,10:42:57 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br>
	 */
	public List getHaveSenRep(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 获得特定账号已提交的报告的数量<br>
	 * create_date: Aug 9, 2010,10:44:51 AM<br>
	 * @param empId 员工id
	 * @return int 报告数量<br>
	 */
	public int getHaveSenRepCount(Long empId);
	/**
	 * 获得特定账号待发的报告<br>
	 * create_date: Aug 9, 2010,10:45:40 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br>
	 */
	public List<Report> getReadySenRep(int pageCurrentNo, int pageSize,Long empId,String orderItem, String isDe);
	/**
	 * 获得特定账号待发的报告的数量<br>
	 * create_date: Aug 9, 2010,10:46:32 AM<br>
	 * @param empId 员工id
	 * @return int 报告数量<br>
	 */
	public int getReadySenRepCount(Long empId);
//	public List getSummary(int pageCurrentNo, int pageSize,String userCode);//获得特定账号已提交的总结
//	public int getSummaryCount(String userCode);//获得特定账号已提交的总结的数量
	/**
	 * 根据报告的编号获得报告内容<br>
	 * create_date: Aug 9, 2010,10:47:32 AM<br>
	 * @param repCode 报告id
	 * @return report 报告对象<br>
	 */
	public Report showReportInfo(Long repCode);
//	public List getUpSummary(int pageCurrentNo, int pageSize,String userCode);//查看上级总结
//	public int getUpSummaryCount(String userCode);//获得上级总结的数量
	/**
	 * 查看所有已收到的报告<br>
	 * create_date: Aug 9, 2010,10:49:40 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br>
	 */
	public List getAllReport(int pageCurrentNo, int pageSize,Long empId, String orderItem, String isDe);
	/**
	 * 获得所有已收到的报告的数量<br>
	 * create_date: Aug 9, 2010,10:49:30 AM<br>
	 * @param empId 用户表账号id
	 * @return int 报告数量<br>
	 */
	public int getAllReportCount(Long empId);
	/**
	 * 得到已审批的报告<br>
	 * create_date: Aug 9, 2010,10:50:06 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getAppReport(int pageCurrentNo, int pageSize, Long empId);
	/**
	 * 得到已审批报告的数量<br>
	 * create_date: Aug 9, 2010,10:50:42 AM<br>
	 * @param empId 员工id
	 * @return int 报告数量<br>
	 */
	public int getAppReportCount(Long empId);
	
	/**
	 * 得到未审批的报告<br>
	 * create_date: Aug 9, 2010,10:51:24 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getNoAppReport(int pageCurrentNo, int pageSize, Long empId);
	/**
	 * 得未审批报告的数量<br>
	 * create_date: Aug 9, 2010,10:51:43 AM<br>
	 * @param empId 员工id
	 * @return int 报告数量<br>
	 */
	public int getNoAppReportCount(Long empId);
	
	/**
	 * 得到无用报告<br>
	 * create_date: Aug 9, 2010,10:52:27 AM<br>
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getNoUseRep(Long empId);
	/**
	 * 更新报告<br>
	 * create_date: Aug 9, 2010,10:53:03 AM<br>
	 * @param report 报告对象<br>
	 */
	public void update(Report report);
	/**
	 * 删除报告<br>
	 * create_date: Aug 9, 2010,10:53:33 AM<br>
	 * @param report 报告对象<br>
	 */
	public void delReport(Report report);
	/**
	 * 获得待删除的所有已发报告<br>
	 * create_date: Aug 9, 2010,10:53:51 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @return List 报告记录列表<br>
	 */
	public List findDelReport(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有已发报告数量<br>
	 * create_date: Aug 9, 2010,10:54:28 AM<br>
	 * @return int 报告数量<br>
	 */
	public int findDelReportCount();
	
}