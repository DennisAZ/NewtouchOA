package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.LimRoleDAO;
import com.psit.struts.entity.LimRole;
/**
 * 职级DAO实现类 <br>
 */
public class LimRoleDAOImpl extends HibernateDaoSupport implements LimRoleDAO{
	private static final Log log = LogFactory.getLog(LimRoleDAO.class);
	// property constants
	public static final String ROL_LEV = "rolLev";
	public static final String ROL_NAME = "rolName";
	protected void initDao() {
		// do nothing
	}
	/**
	 * 添加职级<br>
	 * @param transientInstance 职级实体
	 */
	public void save(LimRole transientInstance) {
		log.debug("saving LimRole instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 删除职级 <br>
	 * @param persistentInstance 职级实体
	 */
	public void delete(LimRole persistentInstance) {
		log.debug("deleting LimRole instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/**
	 * 获得所有职级 <br>
	 * @return List 返回职级列表 
	 */
	public List findAll() {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole order by rolLev";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 获得职级大于upRolLev小于等于curRolLev的职级列表 <br>
	 * @return List 返回职级列表 
	 */
	public List getRoleList(int upRolLev,int curRolLev){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql = "select limRole from LimRole as limRole where rolLev>"
				+ upRolLev + " and rolLev<=" + curRolLev + " order by rolLev";
		query = session.createQuery(sql);
		List list = (List) query.list();
		return list;
	}
	/**
	 * 
	 * 查询下级用户角色 <br>
	 * create_date: Aug 11, 2010,11:03:55 AM <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findRole(int rolLev) {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev > "+rolLev;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 查询不属于顶级的职级 <br>
	 * create_date: Aug 11, 2010,11:16:11 AM <br>
	 * @return List 返回职级列表
	 */
	public List isNotBoss() {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev !=1";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 查询同级别的职级 <br>
	 * @param rolLev 职级
	 * @return List 返回职级列表
	 */
	public List findSameRole(int rolLev) {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev = "+rolLev;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 所有职级数量查询 <br>
	 * @return int 返回职级数量
	 */
	public int getCountRole(){
		Query query;
		Session session;
		session = (Session) super.getSession();
		 String sql1 ="select count(*) from LimRole";
			query = session.createQuery(sql1);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 所有职级列表查询  <br>
	 * @return List 返回职级列表
	 */
	public List roleSerach(){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql1 ="from LimRole order by rolLev";
		query=session.createQuery(sql1);
//		query.setFirstResult((currentPage-1)*pageSize);
//		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 根据Id得到职级实体 <br>
	 * @param roleId 职级id
	 * @return LimRole 返回职级实体
	 */
	public LimRole getRole(Long roleId){
		return (LimRole) super.getHibernateTemplate().get(LimRole.class, roleId);
	}
	/**
	 * 更新职级 <br>
	 * @param limRole 职级实体
	 */
	public void updateRole(LimRole limRole){
		 super.getHibernateTemplate().update(limRole);
	}
	/**
	 * 查询顶级职级 <br>
	 * @return String 返回顶级职级 
	 */
	public String maxRole(){
		Query query;
		Session session;
		session = (Session) super.getSession();
			String sql1 ="select max(rolLev) from LimRole";
			query=session.createQuery(sql1);
			String maxrole="";
			if(query.uniqueResult()!=null)
				maxrole=query.uniqueResult().toString();
			return maxrole;
	}
	/**
	 * 根据职级名称查询所有职级 <br>
	 * @param rolName 职级名称
	 * @return List 返回职级列表
	 */
	public List getRoleByName(String rolName) {
		Session session = (Session) super.getSession();
		String sql ="select rolName from LimRole where rolName=:rName";
		Query query=session.createQuery(sql).setString("rName", rolName);
		List list =(List)query.list();
		return list;
	}
	
}