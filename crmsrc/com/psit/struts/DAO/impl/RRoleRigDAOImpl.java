package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RRoleRigDAO;
import com.psit.struts.entity.RRoleRig;

/**
 * 
 * Œ¥ π”√ <br>
 *
 * create_date: Aug 31, 2010,11:26:20 AM<br>
 * @author zjr
 */

public class RRoleRigDAOImpl extends HibernateDaoSupport implements RRoleRigDAO {
	private static final Log log = LogFactory.getLog(RRoleRigDAOImpl.class);

//	protected void initDao() {
//		// do nothing
//	}
//
//	public void save(RRoleRig transientInstance) {
//		log.debug("saving RRoleRig instance");
//		try {
//			getHibernateTemplate().save(transientInstance);
//			log.debug("save successful");
//		} catch (RuntimeException re) {
//			log.error("save failed", re);
//			throw re;
//		}
//	}
//
//	public void delete(RRoleRig persistentInstance) {
//		log.debug("deleting RRoleRig instance");
//		try {
//			getHibernateTemplate().delete(persistentInstance);
//			log.debug("delete successful");
//		} catch (RuntimeException re) {
//			log.error("delete failed", re);
//			throw re;
//		}
//	}
//
//	public RRoleRig findById(java.lang.Long id) {
//		log.debug("getting RRoleRig instance with id: " + id);
//		try {
//			RRoleRig instance = (RRoleRig) getHibernateTemplate().get(
//					"com.psit.struts.entity.RRoleRig", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(RRoleRig instance) {
//		log.debug("finding RRoleRig instance by example");
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
//		log.debug("finding RRoleRig instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RRoleRig as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//
//	public List findAll() {
//		log.debug("finding all RRoleRig instances");
//		try {
//			String queryString = "from RRoleRig";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public RRoleRig merge(RRoleRig detachedInstance) {
//		log.debug("merging RRoleRig instance");
//		try {
//			RRoleRig result = (RRoleRig) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RRoleRig instance) {
//		log.debug("attaching dirty RRoleRig instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RRoleRig instance) {
//		log.debug("attaching clean RRoleRig instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static RRoleRigDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RRoleRigDAOImpl) ctx.getBean("RRoleRigDAO");
//	}
}