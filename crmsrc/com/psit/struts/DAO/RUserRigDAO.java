package com.psit.struts.DAO;

import java.util.List;
/**
 * 
 * 用户权限DAO <br>
 *
 * create_date: Aug 17, 2010,3:49:30 PM<br>
 * @author zjr
 */
public interface RUserRigDAO {
	/**
	 * 
	 * 根据账号获取该账号的所有权限 <br>
	 * create_date: Aug 11, 2010,11:52:51 AM <br>
	 * @param userCode 账号
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode);
	/**
	 * 
	 * 根据账号获取该账号在某个功能类型下的所有权限 <br>
	 * create_date: Aug 11, 2010,11:54:33 AM <br>
	 * @param userCode 账号
	 * @param rurType 功能类型
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode,String rurType);
	/**
	 * 
	 * 删除特定账号的权限 <br>
	 * create_date: Aug 11, 2010,11:56:58 AM <br>
	 * @param userCode 账号
	 * @param funType 功能类型
	 */
	public void delRUserRig(String userCode,String funType);
	/**
	 * 
	 * 为某账号添加权限 <br>
	 * create_date: Aug 11, 2010,11:57:23 AM <br>
	 * @param userLims 权限数组
	 * @param userCode 账号
	 * @param rurType 功能类型
	 */
	public void addRUserRig(String[] userLims,String userCode,String rurType);
}