package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.YHPerson;
/**
 * 员工管理BIZ <br>
 */
public interface EmpBIZ {
	/**
	 * 保存部门 <br>
	 * @param transientInstance 部门实体
	 */
	public void save(SalOrg transientInstance);
	/**
	 * 更新用户表 <br>
	 * @param limUser 用户实体
	 */
	public void updateUser(LimUser limUser);
	/**
	 * 查看部门详情
	 * @param soCode 部门id
	 * @return SalOrg 返回部门实体 
	 */
	public SalOrg salOrgDesc(String soCode);
	/**
	 * 修改部门 <br>
	 * @param salOrg 部门实体
	 */
	public void updateSalOrg(SalOrg salOrg);
	/**
	 * 删除部门 <br>
	 * @param persistentInstance 部门实体
	 */
	public void delete(SalOrg persistentInstance);
	/**
	 * 保存员工信息 <br>
	 * @param transientInstance 员工实体
	 */
	public void save(SalEmp transientInstance);
	/**
	 * 查询所有部门去除顶级部门 <br>
	 * @return List 返回部门列表 
	 */
	public List getAllOrg();
	/**
	 * 获得自己及所有下级部门 <br>
	 * @param userNum 用户码
	 * @return List 返回部门列表
	 */
	public List getMyOrg(String userNum);
	/**
	 * 查询所有部门不含顶级部门 <br>
	 * @return List 返回部门列表 
	 */
	public List findAllSalOrg();
	/**
	 * 获得除自己以外的所有部门 <br>
	 * @param soCode1 部门编号
	 * @return List 返回部门列表 
	 */
	public List findPartSalOrg(String soCode1);
	/**
	 * 判断部门编号是否重复 <br>
	 * @param soCode 部门编号
	 * @return Boolean 重复返回true,不重复返回false   
	 */
	public Boolean checkSoCode(String soCode);
	/**
	 * 判断部门名称是否重复 <br>
	 * @param soName 部门名称
	 * @return 重复返回true,不重复返回false
	 */
	public Boolean checkSoName(String soName);
	/**
	 * 是否存在下级部门 <br>
	 * @param soCode 部门编号
	 * @return 重复返回true,不重复返回false
	 */
	public Boolean isExistLowOrg(String soCode);
	/**
	 * 根据部门编号查询与员工关联的部门 <br>
	 * @param soCode 部门编号
	 * @return List 返回部门列表 
	 */
	public List getSalOrgByCode(String soCode);
	/**
	 * 员工编号名称查询数量 <br>
	 * @param seCode 员工编号
	 * @param seName 员工名称
	 * @return int 返回员工列表数量 
	 */
	public int getCountEmp(String args[]);
	/**
	 * 员工编号名称查询 <br>
	 * @param seCode 员工编号
	 * @param seName 员工名称
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回员工列表 
	 */
	public List salEmpSerach(String args[],String orderItem,String isDe, int currentPage,int pageSize);
	/**
	 * 查看员工详情 <br>
	 * @param seNo 员工id
	 * @return SalEmp 返回员工实体 
	 */
	public SalEmp salEmpDesc(Long seNo);
	/**
	 * 根据编号查询员工 <br>
	 * @param seNo 员工id
	 * @return List 返回值 
	 */
	public List getEmp(String seNo);
	/**
	 * 判断员工工号是否重复 <br>
	 * @param seCode 员工编号
	 * @return List 返回员工列表 
	 */
	public List getEmpByCode(String seCode);
	/**
	 * 获得所有职级 <br>
	 * @return List 返回职级列表 
	 */
	public List getAllRole();
	/**
	 * 获得职级大于upRolLev小于等于curRolLev的职级列表 <br>
	 * @return List 返回职级列表 
	 */
	public List getRoleList(int upRolLev,int curRolLev);
	/**
	  * 修改员工信息 <br>
	  * @param salEmp 员工实体
	  */
	public void updateSalEmp(SalEmp salEmp);
	/**
	 * 删除员工 <br>
	 * @param persistentInstance 员工实体
	 */
	public void delete(SalEmp persistentInstance);
	/**
	 * 获得待删除的所有员工 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回员工列表 
	 */
	public List findDelSalEmp(int pageCurrentNo, int pageSize);
	/**
	 * 获得待删除的所有员工数量 <br>
	 * @return int 返回员工列表数量 
	 */
	public int findDelSalEmpCount();
	/**
	 * 保存工作安排 <br>
	 * @param salTask 工作实体
	 */
	public void saveTask2(SalTask salTask);
	
	/**
	 * 得到比传入职级小的未离职员工（无分页） <br>
	 * @param seLev 职级
	 * @return List<SalEmp> 员工list
	 */
	public List<SalEmp> getEmpsByRole(String seLev);
	
	/**
	 * 查询所有未离职的员工（无分页，根据员工姓名排序） <br>
	 * @return List<SalEmp> 未离职的员工列表
	 */
	public List<SalEmp> getEmpList();
	
	/**
	 * 根据员工id获得员工 <br>
	 * @param id 员工id
	 * @return SalEmp 返回员工实体 
	 */
	public SalEmp findById(Long id);
	
	/**
	 * 根据员工id获得员工 <br>
	 * @param id 员工id
	 * @return SalEmp 返回员工实体 
	 */
	public YHPerson findPersonById(Long id);
}