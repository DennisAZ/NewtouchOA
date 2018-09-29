package com.psit.struts.BIZ.impl;

import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.DAO.LimRoleDAO;
import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.DAO.SalEmpDAO;
import com.psit.struts.DAO.SalOrgDAO;
import com.psit.struts.DAO.SalTaskDAO;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.YHPerson;
/**
 * 员工管理BIZ实现类 <br>
 */
public class EmpBIZImpl implements EmpBIZ {
	
	SalOrgDAO salOrgDao=null;
	LimUserDAO limUserDao=null;
	SalEmpDAO salEmpDao=null;
	SalTaskDAO salTaskDao=null;
	LimRoleDAO limRoleDao=null;
	/**
	 * 保存部门 <br>
	 */
	public void save(SalOrg transientInstance){
		salOrgDao.save(transientInstance);
	}
	/**
	 * 查看部门详情
	 */
	public SalOrg salOrgDesc(String soCode){
		return	salOrgDao.salOrgDesc(soCode);
	}
	/**
	 * 修改部门 <br>
	 */
	public void updateSalOrg(SalOrg salOrg){	
		salOrgDao.updateSalOrg(salOrg);
	}
	/**
	 * 删除部门 <br>
	 */
	public void delete(SalOrg salorg){
		salOrgDao.delete(salorg);
	}
	/**
	 * 保存员工信息 <br>
	 */
	public void save(SalEmp transientInstance){
		salEmpDao.save(transientInstance);
	}
	/**
	 * 根据编号查询员工 <br>
	 */
	public List getEmp(String seNo){
		return salEmpDao.getEmp(seNo);
	}
	/**
	 * 查询所有部门去除顶级部门 <br>
	 */
	public List getAllOrg(){
		return salOrgDao.findAll();
	}
	/**
	 * 获得下级部门 <br>
	 */
	public List getMyOrg(String userNum) {
		return salOrgDao.findAll(userNum);
	}
	/**
	 * 获得除自己以外的所有部门 <br>
	 */
	public List findPartSalOrg(String soCode1) {
		return salOrgDao.findPartSalOrg(soCode1);
	}
	/**
	 * 员工编号名称查询数量 <br>
	 */
	public int getCountEmp(String args[]){
		return salEmpDao.getCountEmp(args);
	}
	/**
	 * 员工编号名称查询 <br>
	 */
	public List salEmpSerach(String args[],String orderItem,String isDe, int currentPage,int pageSize){
		return salEmpDao.salEmpSerach(args, orderItem, isDe, currentPage, pageSize);
	}
	/**
	 * 获得所有职级 <br>
	 */
	public List getAllRole() {
		return limRoleDao.findAll();
	}
	/**
	 * 获得职级大于upRolLev小于等于curRolLev的职级列表 <br>
	 */
	public List getRoleList(int upRolLev,int curRolLev){
		return limRoleDao.getRoleList(upRolLev, curRolLev);
	}
	/**
	 * 查看员工详情 <br>
	 */
	public SalEmp salEmpDesc(Long seNo){
		return salEmpDao.salEmpDesc(seNo);
	}
	/**
	  * 修改员工信息 <br>
	  */
	public void updateSalEmp(SalEmp salEmp){
		 salEmpDao.updateSalEmp(salEmp);
	}
	/**
	 * 删除员工 <br>
	 */
	public void delete(SalEmp persistentInstance){
		salEmpDao.delete(persistentInstance);
	}
	/**
	 * 更新用户表 <br>
	 */
	public void updateUser(LimUser limUser){
		limUserDao.updateUser(limUser);
	}
	
	/**
	 * 得到比传入职级小的未离职员工（无分页） <br>
	 */
	public List<SalEmp> getEmpsByRole(String seLev){
		return salEmpDao.getEmpsByRole(seLev);
	}

	/**
	 * 查询所有未离职的员工（无分页，根据员工姓名排序） <br>
	 */
	public List<SalEmp> getEmpList(){
		return salEmpDao.getEmpList();
	}
	/**
	 * 保存工作安排 <br>
	 */
	public void saveTask2(SalTask salTask){
		salTaskDao.save(salTask);
	}
	/**
	 * 判断部门编号是否重复 <br>
	 */
	public Boolean checkSoCode(String soCode) {
		List list=salOrgDao.searchBySoCode(soCode);
		if(list.size()>0)
		    return true;
		else
			return false;
	}
	/**
	 * 判断部门名称是否重复 <br>
	 */
	public Boolean checkSoName(String soName) {
		List list =salOrgDao.searchBySoName(soName);
		if(list.size()>0)
		    return true;
		else 
			return false;
	}
	/**
	 * 根据部门编号查询与员工关联的部门 <br>
	 */
	public List getSalOrgByCode(String soCode) {
		return salEmpDao.getSalOrgByCode(soCode);
	}
	/**
	 * 判断员工工号是否重复 <br>
	 */
	public List getEmpByCode(String seCode) {
		return salEmpDao.getEmpByCode(seCode);
	}
	/**
	 * 查询所有部门 <br>
	 */
	public List findAllSalOrg() {
		return salOrgDao.findAllSalOrg();
	}
	/**
	 * 是否存在下级部门 <br>
	 */
	public Boolean isExistLowOrg(String soCode) {
		List list=salOrgDao.getLowSalOrg(soCode);
		if(list.size()>0)
		    return true;
		else
			return false;
	}
	/**
	 * 获得待删除的所有员工 <br>
	 */
	public List findDelSalEmp(int pageCurrentNo, int pageSize) {
		return salEmpDao.findDelSalEmp(pageCurrentNo, pageSize);
	}
	/**
	 * 获得待删除的所有员工数量 <br>
	 */
	public int findDelSalEmpCount() {
		return salEmpDao.findDelSalEmpCount();
	}
	/**
	 * 根据员工id获得员工 <br>
	 */
	public SalEmp findById(Long id) {
		return salEmpDao.findById(id);
	}
	
	public void setSalOrgDao(SalOrgDAO salOrgDao) {
		this.salOrgDao = salOrgDao;
	}
	public void setLimUserDao(LimUserDAO limUserDao) {
		this.limUserDao = limUserDao;
	}
	public void setSalEmpDao(SalEmpDAO salEmpDao) {
		this.salEmpDao = salEmpDao;
	}
	public void setSalTaskDao(SalTaskDAO salTaskDao) {
		this.salTaskDao = salTaskDao;
	}
	public void setLimRoleDao(LimRoleDAO limRoleDao) {
		this.limRoleDao = limRoleDao;
	}
	@Override
	public YHPerson findPersonById(Long id) {
		// TODO Auto-generated method stub
		return salEmpDao.findPersonById(id);
	}
}