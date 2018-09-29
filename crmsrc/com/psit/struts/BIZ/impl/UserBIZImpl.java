package com.psit.struts.BIZ.impl;

import java.util.Iterator;
import java.util.List;

import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.DAO.LimFunctionDAO;
import com.psit.struts.DAO.LimRightDAO;
import com.psit.struts.DAO.LimRoleDAO;
import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.DAO.RUserRigDAO;
import com.psit.struts.entity.LimRole;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RUserRig;
/**
 * 账号管理BIZ实现类 <br>
 * @author zjr
 */
public class UserBIZImpl implements UserBIZ{
	LimUserDAO userDao=null;
	LimRoleDAO limRoleDao=null;
	LimFunctionDAO limFunctionDao=null;
	LimRightDAO limRightDao=null;
	RUserRigDAO ruserRigDao=null;
	/**
	 * 登陆验证 <br>
	 */
	public boolean checkLogin(String longinName, String pwd) {
		LimUser limUser = userDao.getUserByName(longinName);
		if (limUser != null) {
			if (limUser.getUserPwd().equals(pwd)) {
				return true;
			}
			return false;
		}
		return false;
	}
	/**
	 * 根据登陆名获得用户 <br>
	 */
	public LimUser getUserByName(String loginName) {
		return userDao.getUserByName(loginName);
	}
	/**
	 * 查询所有已启用的账号 <br>
	 */
	public List<LimUser> listValidUser() { 
		return userDao.listValidUser();
	}
	/**
	 * 按部门编号查询账号 <br>
	 */
	public List<LimUser> listByOrgId(String soCode){
		return userDao.listByOrgId(soCode);
	}
	/**
	 * 按员工Id查询账号 <br>
	 */
	public List<LimUser> listByEmpId(String seNo){
		return userDao.listByEmpId(seNo);
	}
	/**
	 * 使用账号数量查询 <br>
	 */
	public int getCountUser(){
		return userDao.getCountUser();
	}
	/**
	 * 根据账号查询账号实体 <br>
	 */
	public LimUser findById(String code){
		return userDao.findById(code);
	}
	/**
	 * 更新账号 <br>
	 */
	public void updateUser(LimUser limUser){
		userDao.updateUser(limUser);
	}
	/**
	 * 查询所有下级用户角色 <br>
	 */
	public List findAllRole(int rolLev){
		return limRoleDao.findRole(rolLev);
	}
	/**
	 * 查询账号详细信息 <br>
	 */
	public LimUser getUser(String userCode){
		return userDao.getUser(userCode);
	}
	/**
	 * 查询不属于顶级的职级 <br>
	 */
	public List isNotBoss(){
		return limRoleDao.isNotBoss();
	}
	/**
	 * 检查该账号是否有下级账号<br>
	 */
	public List<LimUser> checkDownCode(String userCode){
		return userDao.checkDownCode(userCode);
	}
	/**
	 * 查询未使用账号数量 <br>
	 */
	public int getCountNotUse(){
		return userDao.getCountNotUse();
	}
	/**
	 * 账号总数查询 <br>
	 */
	public int getCountAllUse(){
		return userDao.getCountAllUse();
	}
	/**
	 * 查询同级别的职级 <br>
	 */
	public List findSameRole(int rolLev){
		return limRoleDao.findSameRole(rolLev);
	}
	/**
	 * 所有职级数量查询 <br>
	 */
	public int getCountRole(){
		return limRoleDao.getCountRole();
	}
	/**
	 * 所有职级列表查询  <br>
	 */
	public List roleSerach(){
		return limRoleDao.roleSerach();
	}
	/**
	 * 添加职级<br>
	 */
	public void saveRole(LimRole limRole){
		 limRoleDao.save(limRole);
	}
	/**
	 * 根据Id得到职级实体 <br>
	 */
	public LimRole getRole(Long roleId){
		return limRoleDao.getRole(roleId);
	}
	/**
	 * 更新职级 <br>
	 */
	public void updateRole(LimRole limRole){
		limRoleDao.updateRole(limRole);
	}
	/**
	 * 删除职级 <br>
	 */
	public void delRole(LimRole limRole){
		limRoleDao.delete(limRole);
	}
	/**
	 * 检查职级是否被使用 <br>
	 */
	public List<LimUser> checkRole(String rolId){
		return userDao.checkRole(rolId);
	}
	/**
	 * 查询顶级职级的账号信息 <br>
	 */
	public List<LimUser> findByBossLev(String userCode){
		return userDao.findByBossLev(userCode);
	}
	/**
	 * 查询顶级职级 <br>
	 */
	public String maxRole(){
		return limRoleDao.maxRole();
	}
	 /**
	  * 判断登录名是否重复 <br>
	  */
	public List<LimUser> getLoginName(String loginName){
		return userDao.getLoginName(loginName);
	}
	/**
	 * 保存账号 <br>
	 */
	public void saveLim(LimUser limUser){
		userDao.save(limUser);
	}
	/**
	 * 根据用户码查询账号 <br>
	 */
	public List<LimUser> findByUserNum(String userNum){
		return userDao.findByUserNum(userNum);
	}
	/**
	 * 查询顶级账号 <br>
	 */
	public String getMaxCode(){
		return userDao.getMaxCode();
	}
	/**
	 * 或取未启用的userCode最小值 <br>
	 */
	public String getMinCode(){
		return userDao.getMinCode();
	}
	/**
	 * 根据职级名称查询所有职级 <br>
	 */
	public List getRoleByName(String rolName){
		return limRoleDao.getRoleByName(rolName);
	}
	/**
	 * 根据账号获取该账号的所有权限 <br>
	 */
	public List getRUserRig(String userCode) {
		return ruserRigDao.getRUserRig(userCode);
	}
	/**
	 * 为某账号添加权限 <br>
	 */
	public void addRUserRig(String[] userLims, String userCode,String rurType) {
		ruserRigDao.addRUserRig(userLims, userCode,rurType);
	}
	/**
	 * 删除特定账号的权限 <br>
	 */
	public void delRUserRig(String userCode,String funType) {
		ruserRigDao.delRUserRig(userCode,funType);
	}
	/**
	 * 恢复用户的登陆状态，清空IP <br>
	 */
	public void recInit() {
		userDao.recInit();
	}
	/**
	 * 根据账号获取该账号在某个功能类型下的所有权限 <br>
	 */
	public List getRUserRig(String userCode, String rurType) {
		return ruserRigDao.getRUserRig(userCode, rurType);
	}
	/**
	 * 查询所有已启用的上级账号(状态为0，2) <br>
	 */
	public List<LimUser> getUpRole(int userLev) {
		return userDao.getUpRole(userLev);
	}
	
	/**
	 * 查询不包括传入的账号及其下级账号的账号列表 <br>
	 */
	public List<LimUser> getUserWithOut(String userNum){
		return userDao.getUserWithOut(userNum);
	}
	
	/**
	 * 重新生成用户码 <br>
	 */
	public void UpdateLowUserNum(LimUser limUser, String userNum) {
		if (limUser.getLimUsers().size() == 0) {
			return;
		} else {
			for (Iterator<LimUser> it = limUser.getLimUsers().iterator(); it
					.hasNext();) {
				LimUser limUser1 = (LimUser) it.next();
				String userNum1 = limUser1.getUserCode() + userNum;
				limUser1.setUserNum(userNum1);
				userDao.updateUser(limUser1);
				UpdateLowUserNum(limUser1, userNum1);
			}
		}
		return;
	}
	
	/**
	 * 获得某一功能模块的所有操作<br>
	 */
	public List getLimRight(String funType) {
		return limRightDao.getLimRight(funType);
	}
	/**
	 * 获取权限操作 <br>
	 */
	public boolean getLimit(LimUser limUser, String methodName,
			String methLim[][]) {
		boolean isAllow = true;
		if (limUser != null && methodName != null) {
			for (int i = 0; i < methLim.length; i++) {
				if (methodName.equals(methLim[i][0])) {
					if(!isInUserLims(new String[]{methLim[i][1]},limUser)[0]){
						isAllow = false;
					}
					break;
				}
			}
		}
		return isAllow;
	}
	
	/**
	 * 判断当前账号是否有此权限码 <br>
	 */
	public boolean[] isInUserLims(String[] rig,LimUser limUser){
		boolean[] rs = new boolean[rig.length];
		if (!limUser.getUserIsenabled().equals("3")) {
			List<RUserRig> list = getRUserRig(limUser.getUserCode());
			if(list != null){
				for(int i = 0; i < rig.length ; i++){
					rs[i] = false;
					Iterator<RUserRig> userLim = list.iterator();
					while (userLim.hasNext()) {
						RUserRig ruserRig = (RUserRig) userLim.next();
						if (ruserRig.getLimRight().getRigCode().equals(rig[i])) {
							rs[i] = true;
							break;
						}
					}
				}
			}
			else{//此账号无任何权限
				for(int j = 0; j < rs.length ; j++){
					rs[j] = false;
				}
			}
		}
		else{//超级账号不判断权限
			for(int k = 0; k < rs.length ; k++){
				rs[k] = true;
			}
		}
		return rs;
	}
	
	public void setUserDao(LimUserDAO userDao) {
		this.userDao = userDao;
	}
	public void setLimRoleDao(LimRoleDAO limRoleDao) {
		this.limRoleDao = limRoleDao;
	}
	public void setLimFunctionDao(LimFunctionDAO limFunctionDao) {
		this.limFunctionDao = limFunctionDao;
	}
	public void setLimRightDao(LimRightDAO limRightDao) {
		this.limRightDao = limRightDao;
	}
	public void setRuserRigDao(RUserRigDAO ruserRigDao) {
		this.ruserRigDao = ruserRigDao;
	}
	
//	public List findByUserCode(){
//	return userDao.findByUserCode();
//}

}