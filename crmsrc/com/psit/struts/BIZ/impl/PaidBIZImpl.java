package com.psit.struts.BIZ.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.psit.struts.BIZ.PaidBIZ;
import com.psit.struts.DAO.SalOrderDAO;
import com.psit.struts.DAO.SalPaidPastDAO;
import com.psit.struts.DAO.SalPaidPlanDAO;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SalPaidPlan;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.entity.StatSalMon;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;
/**
 * 订单回款BIZ实现类 <br>
 */
public class PaidBIZImpl implements PaidBIZ {
	SalOrderDAO orderDao = null;
	SalPaidPastDAO paidPastDao = null;
	SalPaidPlanDAO paidPlanDao = null;
	/**
	 * 保存回款记录 <br>
	 */
	public void savePaidPast(SalPaidPast paid){
		paidPastDao.save(paid);
	}
	/**
	 * 更新回款记录 <br>
	 */
	public void updatePaidPast(SalPaidPast paid,double oldPayMon){
		paidPastDao.updatePaid(paid,oldPayMon);
	}
	/**
	 * 得到订单已回款金额 <br>
	 */
	public String getTotalPaid(String code){
		return paidPastDao.getTotalPaid(code);
	}
	/**
	 * 执行回款 <br>
	 */
	public void setPaidPlanCompleted(String paidCode){
		SalPaidPlan salPaid=paidPlanDao.findById(Long.parseLong(paidCode));
		salPaid.setSpdIsp("1");
		paidPlanDao.updatePaid(salPaid);
	}
	/**
	 * 执行开票 <br>
	 */
	public void setPaidPastCompleted(String paidCode){
		SalPaidPast salPaid=paidPastDao.findById(Long.parseLong(paidCode));
		double oldPayMon = 0;
		if(salPaid!=null){
			oldPayMon = salPaid.getSpsPayMon();
			salPaid.setSpsIsinv("1");
			paidPastDao.updatePaid(salPaid,oldPayMon);
		}
	}
	/**
	 * 彻底删除 <br>
	 */
	public void deletePaidPast(String paidId){
		if(!StringFormat.isEmpty(paidId)){
			SalPaidPast salPaid=paidPastDao.findById(Long.parseLong(paidId));
			paidPastDao.delete(salPaid);
		}
	}
	/**
	 * 回款记录详情 <br>
	 */
	public SalPaidPast getPaidPast(String code){
		return paidPastDao.findById(Long.parseLong(code));
	}
	/**
	 * 回款记录详情 <br>
	 */
	public SalPaidPast getPaidPast(Long id)
	{
		return paidPastDao.getById(id);
	}
	/**
	 * 删除回款记录（进入回收站） <br>
	 */
//	public void invPaidPast(SalPaidPast paid){
//		paidPastDao.invalid(paid);
//	}
	/**
	 * 恢复回款记录，更新订单已回款 <br>
	 */
//	public void recovery(SalPaidPast paid){
//		paidPastDao.recovery(paid);
//	}
	/**
	 * 按月查询已回款总额 <br>
	 */
	public String getPastMonSum(String userCode, String year, String month){
		return paidPastDao.getPastMonSum(userCode, year, month);
	}
	/**
	 * 按月查询下级已回款总额 <br>
	 */
	public String getPastSumWithUNum(String userNum, String year, String month){
		return paidPastDao.getPastSumWithUNum(userNum, year, month);
	}
	/**
	 * 得到已回款总额 <br>
	 */
	public String getPastAllSum(String empId){
		return paidPastDao.getPastAllSum(empId);
	}
	/**
	 * 保存回款计划 <br>
	 */
	public void savePaidPlan(SalPaidPlan transientInstance){
		paidPlanDao.save(transientInstance);
	}
	/**
	 * 更新回款计划 <br>
	 */
	public void updatePaidPlan(SalPaidPlan paid){
		paidPlanDao.updatePaid(paid);
	}
	/**
	 * 彻底删除 <br>
	 */ 
	public void deletePaidPlan(SalPaidPlan persistentInstance){
		paidPlanDao.delete(persistentInstance);
	}
	/**
	 * 回款计划详情 <br>
	 */
	public SalPaidPlan getPaidPlan(String code){
		return paidPlanDao.findById(Long.parseLong(code));
	}
	
	/**
	 * 根据id获得回款计划（回收站里用） <br>
	 */
	public SalPaidPlan getPaidPlan(Long id)
	{
		return paidPlanDao.getById(id);
	}
	/**
	 * 删除回款计划（进入回收站） <br>
	 */
	public void invPaidPlan(Long paidId){
		paidPlanDao.invalid(paidId);
	}
	/**
	 * 按月查询回款计划总额 <br>
	 */
	public String getPlanMonSum(String userCode, String year, String month){
		return paidPlanDao.getPlanMonSum(userCode, year, month);
	}
	/**
	 * 按月查询下级回款计划总额 <br>
	 */
	public String getPlanSumWithUNum(String userCode, String year, String month){
		return paidPlanDao.getPlanSumWithUNum(userCode, year, month); 
	}
	/**
	 * 按时间查询回款计划列表 <br>
	 */
	public List<SalPaidPlan> getPlanByTime(String userCode,Date startDate, Date endDate){
		return paidPlanDao.getPlanByTime(userCode, startDate, endDate);
	}
	/**
	 * 过期回款计划 <br>
	 */
	public List<SalPaidPlan> getPastPlan(String userCode,Date endDate){
		return paidPlanDao.getPastPlan(userCode, endDate);
	}
	/**
	 * 获得待删除的所有回款记录 <br>
	 */
	/*public List findDelPaidPast(int pageCurrentNo, int pageSize) {
		return paidPastDao.findDelPaidPast(pageCurrentNo, pageSize);
	}*/
	/**
	 * 获得待删除的所有回款记录数量 <br>
	 */
	/*public int findDelPaidPastCount() {
		return paidPastDao.findDelPaidPastCount();
	}*/
	/**
	 * 获得待删除的所有回款计划 <br>
	 */
	public List findDelPaidPlan(int pageCurrentNo, int pageSize) {
		return paidPlanDao.findDelPaidPlan(pageCurrentNo, pageSize);
	}
	/**
	 * 获得待删除的所有回款计划数量 <br>
	 */
	public int findDelPaidPlanCount() {
		return paidPlanDao.findDelPaidPlanCount();
	}
	/**
	 * 回款计划列表 <br>
	 */
	public List<SalPaidPlan> listPaidPlans(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return paidPlanDao.listPaidPlans(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listPaidPlansCount(String[]args){
		return paidPlanDao.listPaidPlansCount(args);
	}
	/**
	 * 回款记录列表 <br>
	 */
	public List<SalPaidPast> listPaidPasts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return paidPastDao.listPaidPasts(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listPaidPastsCount(String[]args){
		return paidPastDao.listPaidPastsCount(args);
	}
	
	/**
	 * 统计中回款记录列表 <br>
	 */
	public List<SalPaidPast> listStatPaid(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return paidPastDao.listStatPaid(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listStatPaidCount(String[]args){
		return paidPastDao.listStatPaidCount(args);
	}
	
	/**
	 * 回款记录人员月度统计 <br>
	 */
	public StaTable statPaidEmpMon(String[]args){
		return paidPastDao.statPaidEmpMon(GetDate.getMonths(args[0], args[1]),new String[]{args[2]});
	}
	
	/**
	 * 销售月度统计 <br>
	 */
	public List<StatSalMon> statPaidMon(String[]args){
		List<StatSalMon> statResult = new ArrayList<StatSalMon>() ;
		String[] months = GetDate.getMonths(args[0], args[1]);
		List<StatOrd> paidList = paidPastDao.statOrdMon(args);
		List<StatOrd> ordList = orderDao.statOrdMon(args);
		Iterator<StatOrd> paidIt = paidList.iterator();
		Iterator<StatOrd> ordIt = ordList.iterator(); 
		StatOrd paidStatObj = null;
		StatOrd ordStatObj = null;
		if(paidIt.hasNext()){
			paidStatObj = paidIt.next();
		}
		if(ordIt.hasNext()){
			ordStatObj = ordIt.next();
		}
		for(int i = 0; i<months.length; i++){
			String month = months[i];
			Double paidSum = 0.0;
			Double ordSum = 0.0;
			if(paidStatObj!=null&&paidStatObj.getTypName().equals(month)){
				paidSum = paidStatObj.getSum();
				if(paidIt.hasNext()){
					paidStatObj = paidIt.next();
				}
				else{
					paidStatObj = null;
				}
			}
			if(ordStatObj!=null&&ordStatObj.getTypName().equals(month)){
				ordSum = ordStatObj.getSum();
				if(ordIt.hasNext()){
					ordStatObj = ordIt.next();
				}
				else{
					ordStatObj = null;
				}
			}
			statResult.add(new StatSalMon(month,paidSum,ordSum));
		}
		return statResult;
	}
	
	
	/**
	 * 回款分析统计<br>
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSpsAnalyse(String startDate, String endDate){
		return paidPastDao.statSpsAnalyse(startDate, endDate);
	}
	
	/**
	 * 销售月度统计(客户详情下) <br>
	 */
	public List<StatSalMon> statCusPaidMon(String[] args) {
		List<StatSalMon> statResult = new ArrayList<StatSalMon>() ;
		String[] months = GetDate.getMonths(args[0], args[1]);
		List<StatOrd> paidList = paidPastDao.statCusOrdMon(args);
		List<StatOrd> ordList = orderDao.statCusOrdMon(args);
		Iterator<StatOrd> paidIt = paidList.iterator();
		Iterator<StatOrd> ordIt = ordList.iterator(); 
		StatOrd paidStatObj = null;
		StatOrd ordStatObj = null;
		if(paidIt.hasNext()){
			paidStatObj = paidIt.next();
		}
		if(ordIt.hasNext()){
			ordStatObj = ordIt.next();
		}
		for(int i = 0; i<months.length; i++){
			String month = months[i];
			Double paidSum = 0.0;
			Double ordSum = 0.0;
			if(paidStatObj!=null&&paidStatObj.getTypName().equals(month)){
				paidSum = paidStatObj.getSum();
				if(paidIt.hasNext()){
					paidStatObj = paidIt.next();
				}
				else{
					paidStatObj = null;
				}
			}
			if(ordStatObj!=null&&ordStatObj.getTypName().equals(month)){
				ordSum = ordStatObj.getSum();
				if(ordIt.hasNext()){
					ordStatObj = ordIt.next();
				}
				else{
					ordStatObj = null;
				}
			}
			statResult.add(new StatSalMon(month,paidSum,ordSum));
		}
		return statResult;
	}
	public void setPaidPastDao(SalPaidPastDAO paidPastDao) {
		this.paidPastDao = paidPastDao;
	}
	public void setPaidPlanDao(SalPaidPlanDAO paidPlanDao) {
		this.paidPlanDao = paidPlanDao;
	}
	public void setOrderDao(SalOrderDAO orderDao) {
		this.orderDao = orderDao;
	}

}