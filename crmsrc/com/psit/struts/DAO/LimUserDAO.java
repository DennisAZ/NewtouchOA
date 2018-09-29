package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.LimUser;
/**
 * 账号DAO <br>
 * @author zjr
 */
public interface LimUserDAO {
	/**
	 * 得到所有已启用的账号
	 */
	public List<LimUser> listValidUser();
	
	/**
	 * 根据账号ID查询账号实体 <br>
	 */
	public LimUser findById(String id);
    /**
     * 登陆验证 <br>
     */
	public LimUser getUserByName(String loginName);
	/**
	 * 更新账号 <br>
	 */
	public void updateUser(LimUser limUser);
	/**
	 * 根据用户编号查询用户实体(带状态) <br>
	 */
	public LimUser getUser(String userCode);
	/**
	 * 按部门编号查询账号 <br>
	 */
	public List<LimUser> listByOrgId(String soCode);
	/**
	 * 按员工Id查询账号 <br>
	 */
	public List<LimUser> listByEmpId(String seNo);
	/**
	 * 使用账号数量查询 <br>
	 */
	public int getCountUser();
	
	/**
	 * 查询所有已启用的上级账号(状态为0，2) <br>
	 */
	public List<LimUser> getUpRole(int userLev);
	/**
	 * 检查该账号是否有下级账号<br>
	 */
	public List<LimUser> checkDownCode(String userCode);
	/**
	 * 查询未使用账号数量 <br>
	 */
	public int getCountNotUse();
	/**
	 * 账号总数查询 <br>
	 */
	public int getCountAllUse();
	/**
	 * 检查职级是否被使用 <br>
	 */
	public List<LimUser> checkRole(String rolId);
	/**
	 * 查询顶级职级的账号信息 <br>
	 */
	public List<LimUser> findByBossLev(String userCode);
	/**
	 * 获得所有下级账号 <br>
	 */
	public List<LimUser> getAllLowerUser(String userNum,String userCode);
	 /**
	  * 判断登录名是否重复 <br>
	  */
	public List<LimUser> getLoginName(String loginName);
	/**
	 * 恢复用户的登陆状态，清空IP <br>
	 */
	public void recInit();
	/**
	 * 保存账号 <br>
	 */
	public void save(LimUser limUser);
	/**
	 * 根据用户码查询账号 <br>
	 */
	public List<LimUser> findByUserNum(String userNum);
	/**
	 * 查询顶级账号 <br>
	 */
	public String getMaxCode();
	/**
	 * 或取未启用的userCode最小值 <br>
	 */
	public String getMinCode();
	
	/**
	 * 查询不包括传入的账号及其下级账号的账号列表 <br>
	 */
	public List<LimUser> getUserWithOut(String userNum);
}