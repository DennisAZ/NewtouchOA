package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RQuoProDAO;
import com.psit.struts.entity.RQuoPro;
/**
 * 
 * 报价明细DAO实现类 <br>
 *
 * create_date: Aug 17, 2010,2:21:36 PM<br>
 * @author zjr
 */
public class RQuoProDAOImpl extends HibernateDaoSupport implements RQuoProDAO{
	private static final Log log = LogFactory.getLog(RQuoProDAOImpl.class);
	// property constants
//	protected void initDao() {
		// do nothing
//	}
	/**
	 * 
	 * 保存报价明细 <br>
	 * create_date: Aug 9, 2010,11:30:38 AM <br>
	 * @param transientInstance 报价明细实体
	 */
	public void save(RQuoPro transientInstance) {
		log.debug("saving RQuoPro instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

//	public void delete(RQuoPro persistentInstance) {
//		log.debug("deleting RQuoPro instance");
//		try {
//			getHibernateTemplate().delete(persistentInstance);
//			log.debug("delete successful");
//		} catch (RuntimeException re) {
//			log.error("delete failed", re);
//			throw re;
//		}
//	}
//
//	public RQuoPro findById(com.psit.struts.entity.RQuoPro id) {
//		log.debug("getting RQuoPro instance with id: " + id);
//		try {
//			RQuoPro instance = (RQuoPro) getHibernateTemplate().get(
//					"com.psit.struts.DAO.impl.RQuoPro", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(RQuoPro instance) {
//		log.debug("finding RQuoPro instance by example");
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
//		log.debug("finding RQuoPro instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RQuoPro as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findAll() {
//		log.debug("finding all RQuoPro instances");
//		try {
//			String queryString = "from RQuoPro";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public RQuoPro merge(RQuoPro detachedInstance) {
//		log.debug("merging RQuoPro instance");
//		try {
//			RQuoPro result = (RQuoPro) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RQuoPro instance) {
//		log.debug("attaching dirty RQuoPro instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RQuoPro instance) {
//		log.debug("attaching clean RQuoPro instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static RQuoProDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RQuoProDAOImpl) ctx.getBean("RQuoProDAO");
//	}
	/**
	 * 
	 * 根据报价记录删除报价明细 <br>
	 * create_date: Aug 9, 2010,11:30:16 AM <br>
	 * @param quoId 报价id
	 */
	public void delByQuo(Long quoId){
		Session session;
		session = (Session) super.getSession();
		log.debug("finding all RQuoPro instances");
		try {
			String queryString = "delete RQuoPro where quote.quoId="+quoId;
			 int i=session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}