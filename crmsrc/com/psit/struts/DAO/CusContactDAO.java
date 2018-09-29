package com.psit.struts.DAO;

import java.util.Date;
import java.util.List;

import com.psit.struts.entity.CusContact;
/**
 * 客户联系人DAO <br>
 */
public interface CusContactDAO 
{
	/**
	 * 保存客户联系人信息 <br>
	 */
	public void save(CusContact cusContact);
	/**
	 * 根据联系人Id获取联系人 <br>
	 */
	public CusContact showContact(Long id);
	/**
	 * 更新联系人信息 <br>
	 */
	public void updateContact(CusContact cusContact);
	
	public void batchAssign(String ids, String seNo);
	
	public List<CusContact> getAllConByMark(String cusId);
	/**
	 * 联系人列表 <br>
	 */
	public List<CusContact> listContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listContactsCount(String[]args);
		

	/**
	 * 客户联系人列表 <br>
	 */
	public List<CusContact> listCusContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listCusContactsCount(String[]args);
	
	/**
     * 获得某客户下的所有联系人 <br>
     */
	 public List getCusCon(String cusCode,String orderItem, String isDe, int currentPage,int pageSize);
	 /**
	  * 获得某客户下的所有联系人数量 <br>
	  */
	 public int getCusConCount(String cusCode);
	 /**
	  * 删除联系人 <br>
	  */
	public void delContact(CusContact cusContact);
	/**
	 * 获得最近十天关注的联系人 <br>
	 */
	 public List getContactByBirth(Date startDate,Date endDate,String range,String seNo);
	 /**
	  * 获得最近十天关注的联系人数量 <br>
      */
	 public int getContactByBirthCount(Date startDate,Date endDate,String range,String seNo);
	
	/**
	 * 获得删除状态为0的所有联系人 <br>
	 */
//	 public List findDelContact(int pageCurrentNo, int pageSize);
	/**
	 * 获得删除状态为0的所有联系人 <br>
	 */
//	 public int findDelContactCount();
	
	/**
	 * 获得要导出的联系人列表<br>
	 */
	public List<CusContact> getOutCont(String [] args);
	
	/**
	 * 通过Id获得联系人列表<br>
	 */
	public List<CusContact> getContByIds(String ids);
}