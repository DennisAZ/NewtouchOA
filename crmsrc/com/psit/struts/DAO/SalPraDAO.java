package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.SalPra;
/**
 * 来往记录数据库表接口类<br>
 */
public interface SalPraDAO {
	/**
	  * 获得来往记录列表<br>
	  */
	 public List<SalPra> listSalPras(String [] args,String orderItem,String isDe, int currentPage, int pageSize);
	 public int listSalPrasCount(String [] args);
	 /**
	 * 获得最近来往记录<br>
	 */
	 public List getPraByExeDate(Date startDate,Date endDate,String range,String seNo);
	 public int getPraByExeDateCount(Date startDate,Date endDate,String range,String seNo);
	/**
	 * 获得待删除的所有来往记录<br>
	 */
	 public List findDelPra(int pageCurrentNo, int pageSize);
	 public int findDelPraCount();
	 /**
	  * 获得某客户下的所有来往记录<br>
	  */
	 public List getCusPra(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize);
	 public int getCusPraCount(Long cusCode); 
	/**
	 * 保存来往记录信息<br>
	 */
	public void save(SalPra salPra);
	/**
	 * 根据Id获取来往记录<br>
	 */
	public SalPra showSalPra(Long id);
	/**
	 * 更新来往记录信息<br>
	 */
	public void update(SalPra salPra);
	 /**
	  * 删除来往记录<br>
	  */
	 public void delPra(SalPra salPra);
	 
}