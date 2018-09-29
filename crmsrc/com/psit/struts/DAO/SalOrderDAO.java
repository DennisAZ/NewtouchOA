package com.psit.struts.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.form.StatSalMForm;

/**
 * 订单数据库表操作的接口类<br>
 * create_date: Aug 11, 2010,2:33:32 PM<br>
 */
public interface SalOrderDAO {
	/**
	 * 保存订单<br>
	 */
	public void save(SalOrdCon transientInstance);
	
	/**
	 * 修改订单<br>
	 */
	public void update(SalOrdCon instance);
	
	/**
	 * 删除订单(移至回收站),更新客户成单状态<br>
	 */
	public void invalid(SalOrdCon order);
	/**
	 * 恢复订单，更新客户已成单状态<br>
	 */
	public void recovery(SalOrdCon order);
	/**
	 * 订单合同列表<br>
	 */
	public List<SalOrdCon> listOrders(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listOrdersCount(String[]args);
	/**
	 * 得到订单应收款 <br>
	 */
	public String getLeftMon();
	
	public Double getOrdPaidByCus(String cusId);
	
	/**
	 * 根据订单编号查询<br>
	 */
	public List searchByCode(String code);
	/**
	 * 订单高级查询数量<br>
	 */
	public int getSupCount(String hql,String isAll,String userNum,String sodAppIsok);
	/**
	 * 订单高级查询<br>
	 */
	public List<SalOrdCon> ordSupSearch(String hql,String isAll,String userNum,String sodAppIsok,int pageCurNo,int pageSize);
	
	/**
	 * 得到详情<br>
	 */
	public SalOrdCon getOrdCon(String code);
	
	/**
	 * 相应客户下订单列表<br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]客户Id;  [2]订单主题;  [3]订单号;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOrdCon> 订单合同列表
	 */
	public List<SalOrdCon> listOrdersByCusId(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listOrdersByCusId(String[]args);
	
	/**
	 * 获得待删除的所有订单<br>
	 */
	 public List<SalOrdCon> findDelOrder(String orderItem, String isDe, int pageCurrentNo, int pageSize);
	 public int findDelOrderCount();
     /**
      * 根据Id得到订单<br>
      */
	 public SalOrdCon getSalOrder(Long id);
	 /**
	  * 删除合同订单<br>
	  */
	 public void delete(SalOrdCon persistentInstance);
	 /**
	  * 得到订单总额<br>
	  */
	 public String getOrdMonSum(String userCode);
	 
	/**
	 * 得到当月订单总额<br>
	 */
	public String relOrdMonSum(String empId);
	/**
	 * 得到某人的订单总额或回款总额<br>
	 */
	public String getOrdSumWithUNum(String userCode,String isAll,String type);
	/**
	 * 查询某人当月已成客户数量<br>
	 */
	public int relCusCount(String empId);
	
//---------- 订单统计 ------------
	/**
	 * 统计表格中订单列表 <br>
	 */
	public List<SalOrdCon> listStatOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listStatOrdCount(String[]args);
	
	/**
	 * 销售金额统计 <br>
	 * @param empIds	员工ID集合
	 * @param cusIds	客户ID集合
	 * @param startDate	统计日期
	 * @param endDate
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalM(String empIds,String cusIds,String startDate,String endDate);
	
	/**
	 * 订单类别统计 <br>
	 */
	public List<StatOrd> statOrdType(String[]args);
	/**
	 * 订单来源统计 <br>
	 */
	public List<StatOrd> statOrdSou(String[]args);
	/**
	 * 订单月度分布统计 <br>
	 */
	public StaTable statOrdEmpMon(String[]monArray,String[]args);	
	/**
	 * 订单月度统计 <br>
	 */
	public List<StatOrd> statOrdMon(String[]args);
	
	/**
	 * 订单月度统计（客户详情下）<br>
	 */
	public List<StatOrd> statCusOrdMon(String[]args);
	/**
	 * 订单金额月份统计<br>
	 */
	public List sumMonByDate(String hql);
	/**
	 * 订单合同统计数据挖掘查询数量<br>
	 */
	public int searchByStatCount(String statType,String orderType,String ordConDate,String user,String queryString);
	/**
	 * 订单合同统计数据挖掘查询<br>
	 */
	public List<SalOrdCon> searchByStat(String statType,String orderType,String ordConDate,String user,String queryString,int currentPage, int pageSize);
	
	/**
	 * 年度20大客户排行<br>
	 */
	public List<OrdStatistic>  topSalCus(String type);
	/**
	 * 工作台销售业绩图表hql<br>
	 */
	public String lastSalStat(Long empId,String date1,String date2);

	/**
	 * 实际销售统计<br>
	 */
	public List<OrdStatistic> ordSumByFct(String range,String userSearch,String tarName,String tarYear);
	/**
	 * 实际销售合计（按年人）<br>
	 */
	public List<OrdStatistic> allOrdSumByFct(String uCode,String tarName,String tarYear);
	/**
	 * (用一句话描述这个方法)<br>
	 */
	public List<OrdStatistic> allOrdSumByFct2(String uCode,String tarName,String tarYear);
	
	/**
	 * 工作台发货提醒 <br>
	 */
	public List<SalOrdCon> getProdTipList(String filter);
}