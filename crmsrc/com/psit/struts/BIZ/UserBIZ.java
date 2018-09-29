package com.psit.struts.BIZ;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.psit.struts.entity.LimRole;
import com.psit.struts.entity.LimUser;

/**
 * 账号管理BIZ <br>
 * @author zjr
 */
public interface UserBIZ {
	/**
	 * 根据账号查询账号实体 <br>
	 * @param code 账号
	 * @return LimUser 账号实体
	 */
	public LimUser findById(String code);
	/**
	 * 登陆验证 <br>
	 * @param longinName 登录名
	 * @param pwd 密码
	 * @return boolean 通过返回true,不通过返回false
	 */
	public boolean checkLogin(String longinName,String pwd);
	/**
	 * 根据登陆名获得用户 <br>
	 * @param loginName 登录名
	 * @return List 返回账号列表
	 */
	public LimUser getUserByName(String loginName);
	/**
	 * 查询所有已启用的账号 <br>
	 * @return List 返回账号列表
	 */
	public List<LimUser> listValidUser();
	/**
	 * 按部门编号查询账号 <br>
	 * @param soCode 部门编号
	 * @return List 返回账号列表
	 */
	public List<LimUser> listByOrgId(String soCode);
	/**
	 * 按员工Id查询账号 <br>
	 */
	public List<LimUser> listByEmpId(String seNo);
	/**
	 * 使用账号数量查询 <br>
	 * @return int 返回使用账号数量
	 */
	public int getCountUser();
	
	/**
	 * 更新账号 <br>
	 * @param limUser 账号实体
	 */
	public void updateUser(LimUser limUser);
	/**
	 * 查询所有下级用户角色 <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findAllRole(int rolLev);
	/**
	 * 查询账号详细信息(带状态) <br>
	 * @param userCode 账号
	 * @return LimUser 返回账号实体
	 */
	public LimUser getUser(String userCode);
	/**
	 * 查询不属于顶级的职级 <br>
	 * @return List 返回职级列表
	 */
	public List isNotBoss();

	/**
	 * 检查该账号是否有下级账号<br>
	 * @param userCode 账号
	 * @return List 返回账号列表
	 */
	public List<LimUser> checkDownCode(String userCode);
	/**
	 * 查询未使用账号数量 <br>
	 * @return int 返回账号数量 
	 */
	public int getCountNotUse();
	/**
	 * 账号总数查询 <br>
	 * @return int 返回账号数量
	 */
	public int getCountAllUse();
	/**
	 * 查询同级别的职级 <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findSameRole(int rolLev);
	/**
	 * 所有职级数量查询 <br>
	 * @return int 返回职级数量
	 */
	public int getCountRole();
	/**
	 * 所有职级列表查询  <br>
	 * @return List 返回职级列表
	 */
	public List roleSerach();
	/**
	 * 添加职级<br>
	 * @param limRole 职级实体
	 */
	public void saveRole(LimRole limRole);
	/**
	 * 根据Id得到职级实体 <br>
	 * @param roleId 职级id
	 * @return LimRole 返回职级实体
	 */
	public LimRole getRole(Long roleId);
	/**
	 * 更新职级 <br>
	 * @param limRole 职级实体
	 */
	public void updateRole(LimRole limRole);
	/**
	 * 删除职级 <br>
	 * @param limRole 职级实体
	 */
	public void delRole(LimRole limRole);
	/**
	 * 检查职级是否被使用 <br>
	 * @param rolId 职级id
	 * @return List 返回职级列表
	 */
	public List<LimUser> checkRole(String rolId);
	/**
	 * 查询顶级职级的账号信息 <br>
	 * @param userCode 账号
	 * @return List 返回账号列表
	 */
	public List<LimUser> findByBossLev(String userCode);
	/**
	 * 查询顶级职级 <br>
	 * @return String 返回顶级职级 
	 */
	public String maxRole();
	 /**
	  * 判断登录名是否重复 <br>
	  * @param loginName 登录名
	  * @return List 返回账号列表
	  */
	public List<LimUser> getLoginName(String loginName);
	/**
	 * 获得某一功能模块的所有操作 <br>
	 * @param funType 功能类型
	 * @return List 返回功能列表
	 */
	public List getLimRight(String funType);
	/**
	 * 根据账号获取该账号的所有权限 <br>
	 * @param userCode 账号
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode);
	/**
	 * 根据账号获取该账号在某个功能类型下的所有权限 <br>
	 * @param userCode 账号
	 * @param rurType 功能类型
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode,String rurType);
	/**
	 * 删除特定账号的权限 <br>
	 * @param userCode 账号
	 * @param funType 功能类型
	 */
	public void delRUserRig(String userCode,String funType);
	/**
	 * 为某账号添加权限 <br>
	 * @param userLims 权限数组
	 * @param userCode 账号
	 * @param rurType 功能类型
	 */
	public void addRUserRig(String[] userLims,String userCode,String rurType);
	/**
	 * 恢复用户的登陆状态，清空IP <br>
	 */
	public void recInit();
	/**
	 * 保存账号 <br>
	 * @param limUser 账号实体
	 */
	public void saveLim(LimUser limUser);
	/**
	 * 根据用户码查询账号 <br>
	 * @param userNum 用户码
	 * @return List 返回账号列表
	 */
	public List<LimUser> findByUserNum(String userNum);
	
	/**
	 * 查询所有已启用的上级账号(状态为0，2) <br>
	 * @param userLev 职级
	 * @return List<LimUser> 返回上级账号列表
	 */
	public List<LimUser> getUpRole(int userLev);
	/**
	 * 查询顶级账号 <br>
	 * @return String 返回账号
	 */
	public String getMaxCode();
	/**
	 * 或取未启用的userCode最小值 <br>
	 * @return String 返回账号
	 */
	public String getMinCode();
	/**
	 * 根据职级名称查询所有职级 <br>
	 * @param rolName 职级名称
	 * @return List 返回职级列表
	 */
	public List getRoleByName(String rolName);
	
	/**
	 * 查询不包括传入的账号及其下级账号的账号列表 <br>
	 * @param userNum 需排除的账号用户码
	 * @return List 账号列表
	 */
	public List<LimUser> getUserWithOut(String userNum);
	
	/**
	 * 重新生成用户码 <br>
	 * @param limUser 账号实体
	 * @param userNum 用户码
	 */
	public void UpdateLowUserNum(LimUser limUser, String userNum);
	
	/**
	 * 获取权限操作 <br>
	 * @param limUser 账号
	 * @param methodName 方法名
	 * @param methLim 权限数组
	 * @return boolean 账号没有权限返回false,否则返回true
	 */
	public boolean getLimit(LimUser limUser,String methodName,
			String methLim[][]);
	
	/**
	 * 判断当前账号是否有此权限码 <br>
	 * @param rig 传入的权限码数组
	 * @param limUser 账号实体
	 * @return boolean[] 如果有权限码返回true，否则返回false(返回的boolean数组顺序和权限码数组顺序一致)
	 */
	public boolean[] isInUserLims(String[] rig,LimUser limUser);
}