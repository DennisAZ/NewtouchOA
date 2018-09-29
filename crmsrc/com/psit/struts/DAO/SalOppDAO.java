package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.SalOpp;
/**
 * 销售机会数据库表操作接口类<br>
 */
public interface SalOppDAO {
	/**
	 * 销售机会列表 <br>
	 */
	public List<SalOpp> listSalOpps(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSalOppsCount(String[]args);
	 /**
	  * 获得最近销售机会<br>
	  */
	 public List getOppByExeDate(int pageCurrentNo, int pageSize,Date startDate,Date endDate,String range,String seNo);
	 public int getOppByExeDateCount(Date startDate,Date endDate,String range,String seNo);
	 /**
	 * 获得某客户下的所有销售机会<br>
	 */
	 public List getCusOpp(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize);
	 public int getCusOppCount(Long cusCode); 
	 /**
      * 获得待删除的所有销售机会<br>
      */
	 public List findDelOpp(int pageCurrentNo, int pageSize);
	 public int findDelOppCount();
	 /**
	  * 根据客户编号获得销售机会<br>
	  */
	 public List getOppByCusCode(Long cusCode);
	/**
	 * 保存销售机会信息<br>
	 */
	public void save(SalOpp salOpp);
	 /**
     * 根据Id获得销售机会<br>
     */
	 public SalOpp getSalOpp(Long id);
	/**
	 * 根据Id获取销售机会(带删除状态)<br>
	 */
	public SalOpp showSalOpp(Long id);
	/**
	 * 更新销售机会信息<br>
	 */
	public void update(SalOpp salOpp);
	 /**
	  * 删除销售机会<br>
	  */
	 public void delOpp(SalOpp salOpp);
}