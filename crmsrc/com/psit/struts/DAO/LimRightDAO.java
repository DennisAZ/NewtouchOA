package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.LimRight;
/**
 * 
 * 权限DAO <br>
 *
 * create_date: Aug 16, 2010,10:19:56 AM<br>
 * @author zjr
 */
public interface LimRightDAO {
	/**
	 * 
	 * 获得某一功能模块的所有操作<br>
	 * create_date: Aug 16, 2010,10:20:45 AM <br>
	 * @param funType 功能类型
	 * @return List 权限列表
	 */
	public List getLimRight(String funType);
	/**
	 * 
	 * 保存仓库的权限码 <br>
	 * create_date: Aug 11, 2010,3:40:09 PM <br>
	 * @param limRight 权限实体
	 */
	public void savLimRight(LimRight limRight);
	/**
	 * 
	 * 删除仓库对应的权限 <br>
	 * create_date: Aug 11, 2010,3:42:42 PM <br>
	 * @param rigCode 权限编号
	 */
	public void delLimRight(String rigCode);
	/**
	 * 
	 * 根据权限码获得仓库 <br>
	 * create_date: Aug 11, 2010,3:45:12 PM <br>
	 * @param rigCode 权限编号
	 * @return LimRight 返回权限实体
	 */
	public LimRight findLimRight(String rigCode);
	/**
	 * 
	 * 修改权限实体 <br>
	 * create_date: Aug 11, 2010,3:47:32 PM <br>
	 * @param limRight 权限实体
	 */
	public void updLimRight(LimRight limRight);
}