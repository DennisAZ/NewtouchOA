package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RWoutProDAO;
import com.psit.struts.entity.RWinPro;
import com.psit.struts.entity.RWoutPro;
/**
 * 
 * 出库明细DAO实现类 <br>
 *
 * create_date: Aug 17, 2010,5:11:07 PM<br>
 * @author zjr
 */
public class RWoutProDAOImpl extends HibernateDaoSupport implements RWoutProDAO{
	private static final Log log = LogFactory.getLog(RWoutProDAOImpl.class);
	// property constants
	public static final String RWO_WOUT_NUM = "rwoWoutNum";
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * 保存出库明细 <br>
	 * create_date: Aug 11, 2010,5:03:56 PM <br>
	 * @param transientInstance 出库明细实体
	 */
	public void save(RWoutPro transientInstance) {
		log.debug("saving RWoutPro instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 删除出库明细 <br>
	 * create_date: Aug 11, 2010,5:17:33 PM <br>
	 * @param persistentInstance 出库明细实体
	 */
	public void delete(RWoutPro persistentInstance) {
		log.debug("deleting RWoutPro instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

//	public RWoutPro findById(java.lang.Long id) {
//		log.debug("getting RWoutPro instance with id: " + id);
//		try {
//			RWoutPro instance = (RWoutPro) getHibernateTemplate().get(
//					"com.psit.struts.entity.RWoutPro", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}

//	public List findByExample(RWoutPro instance) {
//		log.debug("finding RWoutPro instance by example");
//		try {
//			List results = getHibernateTemplate().findByExample(instance);
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
//		} catch (RuntimeException re) {
//			log.error("find by example failed", re);
//			throw re;
//		}
//	}
//
//	public List findByProperty(String propertyName, Object value) {
//		log.debug("finding RWoutPro instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RWoutPro as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByRwoWoutNum(Object rwoWoutNum) {
//		return findByProperty(RWO_WOUT_NUM, rwoWoutNum);
//	}
//
//
//	public List findAll() {
//		log.debug("finding all RWoutPro instances");
//		try {
//			String queryString = "from RWoutPro";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public RWoutPro merge(RWoutPro detachedInstance) {
//		log.debug("merging RWoutPro instance");
//		try {
//			RWoutPro result = (RWoutPro) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RWoutPro instance) {
//		log.debug("attaching dirty RWoutPro instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RWoutPro instance) {
//		log.debug("attaching clean RWoutPro instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static RWoutProDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RWoutProDAOImpl) ctx.getBean("RWoutProDAO");
//	}
	/**
	 * 
	 * 根据出库单号查询出库明细 <br>
	 * create_date: Aug 11, 2010,5:04:23 PM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库明细列表
	 */
	public List getRwoutPro(String wwoCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="select rwoutPro from RWoutPro as rwoutPro where rwoutPro.wmsWarOut.wwoCode = '"+wwoCode+"'order by rwoId desc";
		query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * 更新出库明细 <br>
	 * create_date: Aug 11, 2010,5:08:03 PM <br>
	 * @param rwoutPro 出库明细实体
	 */
	public void updateRwp(RWoutPro rwoutPro){
		super.getHibernateTemplate().update(rwoutPro);
	}
	/**
	 * 
	 * 根据出库明细id查询实体 <br>
	 * create_date: Aug 11, 2010,5:08:28 PM <br>
	 * @param rwoId 出库明细id
	 * @return RWoutPro 返回出库明细实体
	 */
	public RWoutPro getRWoutPro(Long rwoId){
		return (RWoutPro)super.getHibernateTemplate().get(RWoutPro.class, rwoId);
	}
	/**
	 * 
	 * 根据商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:00:58 PM <br>
	 * @param wprId 商品id
	 * @return List 返回出库明细列表
	 */
	public List findByWpr(Long wprId) {
		log.debug("finding all RWoutPro instances");
		try {
			String queryString = "from RWoutPro where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据出库单id和商品id查询出库明细 <br>
	 * create_date: Aug 12, 2010,12:04:55 PM <br>
	 * @param wprId 商品id
	 * @param wwoId 出库单id
	 * @return List 返回出库明细列表
	 */
	public List findByWwo(Long wprId,Long wwoId) {
		log.debug("finding all RWoutPro instances");
		try {
			String queryString = "from RWoutPro where wmsProduct.wprId = "+wprId+" and wmsWarOut.wwoId="+wwoId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}