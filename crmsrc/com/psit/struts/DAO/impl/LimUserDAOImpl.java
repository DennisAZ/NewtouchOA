package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.entity.LimUser;

/**
 * 账号DAO实现类 <br>
 */

public class LimUserDAOImpl extends HibernateDaoSupport implements LimUserDAO{
	private static final Log log = LogFactory.getLog(LimUserDAOImpl.class);
	// property constants
	public static final String USER_CODE = "userCode";
	public static final String USER_NUM = "userNum";
	public static final String USER_LOGIN_NAME = "userLoginName";
	public static final String USER_PWD = "userPwd";
	public static final String USER_LEV = "userLev";
	public static final String USER_SE_NAME = "userSeName";
	public static final String USER_DESC = "userDesc";
	public static final String USER_ISENABLED = "userIsenabled";
	public static final String USER_ISLOGIN = "userIsLogin";
	public static final String USER_ORG_ID = "salOrg.soCode";
	public static final String USER_EMP_ID = "salEmp.seNo";
	public static final String USER_ROL_ID = "limRole.rolId";
	public static final String USER_ROL_LEV = "limRole.rolLev";
	public static final String USER_USER_CODE = "limUser.userCode";

	protected void initDao() {
		// do nothing
	}
	/**
	 * 保存账号 <br>
	 * @param transientInstance 账号实体
	 */
	public void save(LimUser transientInstance) {
		log.debug("saving LimUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 更新账号 <br>
	 * @param limUser 账号实体
	 */
	public void updateUser(LimUser limUser){
		super.getHibernateTemplate().update(limUser);
	}
	/**
	 * 根据账号ID查询账号实体 <br>
	 * @param id 账号
	 * @return LimUser 账号实体
	 */
	public LimUser findById(java.lang.String id) {
		log.debug("getting LimUser instance with id: " + id);
		try {
			LimUser instance = (LimUser) getHibernateTemplate().get(
					"com.psit.struts.entity.LimUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * 根据用户编号查询用户实体(带状态) <br>
	 * @param userCode 账号
	 * @return LimUser 返回用户实体 
	 */
	public LimUser getUser(String userCode){
		Session session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED
						+ "='1' or " + USER_ISENABLED + "='2') and " + USER_CODE
						+ "=:userCode";
		Query query = session.createQuery(sql)
				.setString("userCode", userCode);	
		return (LimUser)query.uniqueResult();
	}
	/**
	 * 登陆验证 <br>
	 * @param loginName 登录名
	 * @return List 账号列表
	 */
	public LimUser getUserByName(String loginName) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where " + USER_LOGIN_NAME
				+ "=:loginName and (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='3')";
		Query query = session.createQuery(sql)
				.setString("loginName", loginName);	
		return (LimUser)query.uniqueResult();
	}
	/**
	 * 得到所有已启用的账号
	 * @return List 返回已启用账号列表
	 */
	public List<LimUser> listValidUser() {
		log.debug("finding all LimUser instances");
		try {
			String queryString = "from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2')";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 按部门编号查询账号 <br>
	 * @param soCode 部门编号
	 * @return List 返回账号列表
	 */
	public List<LimUser> listByOrgId(String soCode){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_ORG_ID+"='"+soCode+"'";
		Query query=session.createQuery(sql);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * 按员工Id查询账号 <br>
	 * @param seNo 员工Id
	 * @return List 返回账号列表 
	 */
	public List<LimUser> listByEmpId(String seNo){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_EMP_ID+"="+seNo;
		Query query=session.createQuery(sql);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * 账号总数查询 <br>
	 * @return int 返回账号数量
	 */
	public int getCountAllUse(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where "+USER_ISENABLED+"!='3' or "+USER_ISENABLED+" is null";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 查询未使用账号数量 <br>
	 * @return int 返回账号数量 
	 */
	public int getCountNotUse(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where "+USER_ISENABLED+" is null";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 已启用账号数量查询 <br>
	 * @return int 返回使用账号数量
	 */
	public int getCountUser(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2')";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * 查询所有已启用的上级账号(状态为0，2) <br>
	 * @param userLev 职级
	 * @return List<LimUser> 返回上级账号列表
	 */
	public List<LimUser> getUpRole(int userLev) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2') and "+USER_ROL_LEV+" < "
				+ userLev + " order by "+USER_ROL_LEV;
		Query query = session.createQuery(sql);
		List<LimUser> list = query.list();
		return list;
	}
	
	/**
	 * 检查该账号是否有下级账号<br>
	 * @param userCode 账号
	 * @return List 返回账号列表
	 */
	public List<LimUser> checkDownCode(String userCode){
		Session session = (Session) super.getSession();
		String sql ="from LimUser where "+USER_USER_CODE+" = :userCode";
		Query query=session.createQuery(sql).setString("userCode", userCode);
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * 检查职级是否被使用 <br>
	 * @param rolId 职级id
	 * @return List 返回职级列表
	 */
	public List<LimUser> checkRole(String rolId) {
		Session session = (Session) super.getSession();
		String sql1 = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_ROL_ID + "= " + rolId;
		Query query = session.createQuery(sql1);
		List<LimUser> list = query.list();
		return list;
	}
	/**
	 * 查询顶级职级的账号信息 <br>
	 * @param userCode 账号
	 * @return List 返回账号列表
	 */
	public List<LimUser> findByBossLev(String userCode){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_ROL_LEV+"=1 and "+USER_CODE+" =:userCode";
		Query query=session.createQuery(sql).setString("userCode", userCode);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * 获得所有下级账号 <br>
	 * @param userNum 用户码
	 * @param userCode 账号
	 * @return List 返回用户列表 
	 */
	public List<LimUser> getAllLowerUser(String userNum, String userCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM
				+ " like :userNum and " + USER_CODE + "!=:userCode";
		query = session.createQuery(sql)
				.setString("userNum", "%" + userNum)
				.setString("userCode", userCode);
		List<LimUser> list = query.list();
		return list;
	}
	 /**
	  * 判断登录名是否重复 <br>
	  * @param loginName 登录名
	  * @return List 返回账号列表
	  */
	public List getLoginName(String loginName) {
		Session session = (Session) super.getSession();
		String sql ="select userCode from LimUser where "+USER_LOGIN_NAME+" = :loginName";
		Query query=session.createQuery(sql).setString("loginName",loginName);
		List list=query.list();
		return list;
	}
	/**
	 * 恢复用户的登陆状态，清空IP <br>
	 */
	public void recInit() {
		Session session=(Session) super.getSession();
		String sql="update LimUser set "+USER_ISLOGIN+"='0'";
		session.createQuery(sql).executeUpdate();
	}
	/**
	 * 根据用户码查询账号 <br>
	 * @param userNum 用户码
	 * @return List 返回账号列表
	 */
	public List<LimUser> findByUserNum(String userNum) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM
				+ " like :userNum order by " + USER_ROL_LEV + " asc";
		Query query = session.createQuery(sql)
			.setString("userNum","%" + userNum);
		List<LimUser> list = query.list();
		return list;
	}
	/**
	 * 查询顶级账号 <br>
	 * @return String 返回账号
	 */
	public String getMaxCode(){
		Session session = (Session) super.getSession();
	    String sql1 ="select max("+USER_CODE+")from LimUser";
	    Query query=session.createQuery(sql1);
		List list=query.list();
		String code="";
		if(list!=null){
			code=list.get(0).toString();
		}
		return code;
	}
	/**
	 * 或取未启用的userCode最小值 <br>
	 * @return String 返回账号
	 */
	public String getMinCode(){
		Session session = (Session) super.getSession();
	    Query query=session.createQuery("select min("+USER_CODE+") from LimUser where " + USER_ISENABLED + " is null ");
		List list=query.list();
		String code="";
		if(list.get(0)!=null){
			code=list.get(0).toString();
		}
		return code;
	}
	
	/**
	 * 查询不包括传入的账号及其下级账号的账号列表 <br>
	 * @param userNum 需排除的账号用户码
	 * @return List 账号列表
	 */
	public List<LimUser> getUserWithOut(String userNum){
		Session session = (Session) super.getSession();
		String hql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM + " not like :userNum ";
		Query query = session.createQuery(hql).setString("userNum", "%"+userNum);
		List<LimUser> list = query.list();
		return list;
	}
}