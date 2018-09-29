package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.YHPerson;

/**
 * 员工个人资料数据库表接口类 <br>
 * create_date: Aug 10, 2010,9:17:59 AM<br>
 * @author 朱皖宁
 */
public interface SalEmpDAO {
	/**
	 * 保存员工信息<br>
	 * create_date: Aug 10, 2010,9:18:22 AM<br>
	 * @param transientInstance 员工表对象<br>
	 */
	public void save(SalEmp transientInstance);
	/**
	 * 
	 * 员工编号名称查询数量 <br>
	 * create_date: Aug 6, 2010,3:36:29 PM <br>
	 * @param seCode 员工编号
	 * @param seName 员工名称
	 * @return int 返回员工列表数量 
	 */
	public int getCountEmp(String[]args);
	/**
	 * 
	 * 员工编号名称查询 <br>
	 * create_date: Aug 6, 2010,3:37:06 PM <br>
	 * @param seCode 员工编号
	 * @param seName 员工名称
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回员工列表 
	 */
	public List salEmpSerach(String[]args,String orderItem,String isDe,int currentPage,int pageSize);
	/**
	 * 查看员工详情<br>
	 * create_date: Aug 10, 2010,9:23:33 AM<br>
	 * @param seNo 员工编号
	 * @return SalEmp 员工对象<br>
	 */
	public SalEmp salEmpDesc(Long seNo);
	 /**
	  * 修改员工信息<br>
	  * create_date: Aug 10, 2010,9:24:24 AM<br>
	  * @param salEmp 员工对象<br>
	  */
	public void updateSalEmp(SalEmp salEmp);
	/**
	 * 根据编号查询<br>
	 * create_date: Aug 10, 2010,9:24:44 AM<br>
	 * @param seNo 员工编号
	 * @return List 员工记录列表<br>
	 */
	public List getEmp(String seNo);
	/**
	 * 按员工工号查询<br>
	 * create_date: Aug 10, 2010,9:25:20 AM<br>
	 * @param seCode 员工工号
	 * @return List 员工记录列表<br>
	 */
	public List getEmpByCode(String seCode);
	/**
	 * 删除员工<br>
	 * create_date: Aug 10, 2010,9:26:34 AM<br>
	 * @param persistentInstance 员工对象<br>
	 */
	public void delete(SalEmp persistentInstance);
	/**
	 * 根据部门编号查询与员工关联的部门<br>
	 * create_date: Aug 10, 2010,9:27:04 AM<br>
	 * @param soCode 员工编号
	 * @return List 员工记录列表<br>
	 */
	public List getSalOrgByCode(String soCode);
	
	/**
	 * 得到比传入职级小的未离职员工（无分页） <br>
	 * create_date: Sep 13, 2010,2:47:48 PM <br>
	 * @param seLev 职级
	 * @return List<SalEmp> 员工list
	 */
	public List<SalEmp> getEmpsByRole(String seLev);
	
	/**
	 * 查询所有未离职的员工（无分页，根据员工姓名排序） <br>
	 * create_date: Aug 11, 2010,10:12:11 AM <br>
	 * @return List<SalEmp> 未离职的员工列表
	 */
	public List<SalEmp> getEmpList();
	
	/**
	 * 获得待删除的所有员工<br>
	 * create_date: Aug 10, 2010,9:32:28 AM<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示的记录数
	 * @return List 员工记录列表<br>
	 */
	public List findDelSalEmp(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有员工记录数量<br>
	 * create_date: Aug 10, 2010,9:32:55 AM<br>
	 * @return int 员工记录数量<br>
	 */
	public int findDelSalEmpCount();
	/**
	 * 根据员工id获得员工<br>
	 * create_date: Aug 10, 2010,9:33:38 AM<br>
	 * @param id 员工id
	 * @return SalEmp 员工对象<br>
	 */
	public SalEmp findById(Long id);
	public YHPerson findPersonById(Long id);
}