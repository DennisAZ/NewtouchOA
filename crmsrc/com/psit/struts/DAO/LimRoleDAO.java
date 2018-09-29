package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.LimRole;
/**
 * 
 * 职级DAO <br>
 *
 * create_date: Aug 16, 2010,10:32:00 AM<br>
 * @author zjr
 */
public interface LimRoleDAO {
	/**
	 * 
	 * 获得所有职级 <br>
	 * create_date: Aug 6, 2010,3:46:00 PM <br>
	 * @return List 返回职级列表 
	 */
	public List findAll();
	/**
	 * 
	 * 获得职级大于upRolLev小于等于curRolLev的职级列表 <br>
	 * create_date: Aug 6, 2010,3:46:00 PM <br>
	 * @return List 返回职级列表 
	 */
	public List getRoleList(int upRolLev,int curRolLev);
	/**
	 * 
	 * 查询下级用户角色 <br>
	 * create_date: Aug 11, 2010,11:03:55 AM <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findRole(int rolLev);
	/**
	 * 
	 * 查询不属于顶级的职级 <br>
	 * create_date: Aug 11, 2010,11:16:11 AM <br>
	 * @return List 返回职级列表
	 */
	public List isNotBoss();
	/**
	 * 
	 * 查询同级别的职级 <br>
	 * create_date: Aug 11, 2010,11:27:01 AM <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findSameRole(int rolLev);
	/**
	 * 
	 * 所有职级数量查询 <br>
	 * create_date: Aug 11, 2010,11:28:38 AM <br>
	 * @return int 返回职级数量
	 */
	public int getCountRole();
	/**
	 * 
	 * 所有职级列表查询  <br>
	 * @return List 返回职级列表
	 */
	public List roleSerach();
	/**
	 * 
	 * 添加职级<br>
	 * create_date: Aug 11, 2010,11:32:16 AM <br>
	 * @param limRole 职级实体
	 */
	public void save(LimRole limRole);
	/**
	 * 
	 * 根据Id得到职级实体 <br>
	 * create_date: Aug 11, 2010,11:32:47 AM <br>
	 * @param roleId 职级id
	 * @return LimRole 返回职级实体
	 */
	public LimRole getRole(Long roleId);
	/**
	 * 
	 * 更新职级 <br>
	 * create_date: Aug 11, 2010,11:33:16 AM <br>
	 * @param limRole 职级实体
	 */
	public void updateRole(LimRole limRole);
	/**
	 * 
	 * 删除职级 <br>
	 * create_date: Aug 11, 2010,11:34:48 AM <br>
	 * @param limRole 职级实体
	 */
	public void delete(LimRole limRole);
	/**
	 * 
	 * 查询顶级职级 <br>
	 * create_date: Aug 11, 2010,11:47:41 AM <br>
	 * @return String 返回顶级职级 
	 */
	public String maxRole();
	/**
	 * 
	 * 根据职级名称查询所有职级 <br>
	 * create_date: Aug 11, 2010,12:04:53 PM <br>
	 * @param rolName 职级名称
	 * @return List 返回职级列表
	 */
	public List getRoleByName(String rolName);

}