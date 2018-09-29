package com.psit.struts.DAO;

import java.util.List;
import com.psit.struts.entity.SalOrg;
/**
 * 公司部门数据库表操作接口类 <br>
 * create_date: Aug 12, 2010,2:22:52 PM<br>
 * @author 朱皖宁
 */
public interface SalOrgDAO {
	/**
	 * 保存数据<br>
	 * create_date: Aug 12, 2010,2:23:48 PM<br>
	 * @param transientInstance 公司部门对象<br>
	 */
	public void save(SalOrg transientInstance);
	//查看组织详情
	/**
	 * 查看部门详情<br>
	 * create_date: Aug 12, 2010,2:26:07 PM<br>
	 * @param soCode 公司部门id
	 * @return SalOrg 公司部门对象<br>
	 */
	public SalOrg salOrgDesc(String soCode);
	/**
	 * 修改部门<br>
	 * create_date: Aug 12, 2010,2:26:41 PM<br>
	 * @param salOrg 公司部门对象<br>
	 */
	public void updateSalOrg(SalOrg salOrg);
	/**
	 * 删除部门<br>
	 * create_date: Aug 12, 2010,2:27:20 PM<br>
	 * @param persistentInstance 公司部门对象<br>
	 */
	public void delete(SalOrg persistentInstance);
	/**
	 * 查询所有部门除去顶级部门<br>
	 * create_date: Aug 12, 2010,2:28:00 PM<br>
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> findAll();
	/**
	 * 或得所有部门<br>
	 * create_date: Aug 12, 2010,2:28:24 PM<br>
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> findAllSalOrg();
	/**
	 * 获得除自己和顶级部门以外的所有部门<br>
	 * create_date: Aug 12, 2010,2:28:39 PM<br>
	 * @param soCode1 公司部门id 
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> findPartSalOrg(String soCode1);
	/**
	 * 获得下级部门<br>
	 * create_date: Aug 12, 2010,2:29:29 PM<br>
	 * @param soCode 公司部门id
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> getLowSalOrg(String soCode);
	/**
	 * 判断部门名是否已存在<br>
	 * create_date: Aug 12, 2010,2:29:53 PM<br>
	 * @param soName 公司部门名称
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> searchBySoName(String soName);
	/**
	 * 判断部门编号是否已存在<br>
	 * create_date: Aug 12, 2010,2:30:27 PM<br>
	 * @param soCode 公司部门编号
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> searchBySoCode(String soCode);
	/**
	 * 获得部门下自己及所有下级成员<br>
	 * create_date: Aug 12, 2010,2:31:21 PM<br>
	 * @param userNum 用户id及其下属
	 * @return List<SalOrg> 公司部门对象列表<br>
	 */
	public List<SalOrg> findAll(String userNum);
}