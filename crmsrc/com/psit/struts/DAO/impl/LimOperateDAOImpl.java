package com.psit.struts.DAO.impl;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.LimOperateDAO;
import com.psit.struts.entity.LimOperate;

/**
 * 
 * £®Œﬁ”√£© <br>
 *
 * create_date: Aug 31, 2010,11:21:49 AM<br>
 * @author zjr
 */

public class LimOperateDAOImpl extends HibernateDaoSupport implements LimOperateDAO{
	private static final Log log = LogFactory.getLog(LimOperateDAOImpl.class);
	// property constants
	public static final String OPE_DESC = "opeDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(LimOperate transientInstance) {
		log.debug("saving LimOperate instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(LimOperate persistentInstance) {
		log.debug("deleting LimOperate instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LimOperate findById(java.lang.String id) {
		log.debug("getting LimOperate instance with id: " + id);
		try {
			LimOperate instance = (LimOperate) getHibernateTemplate().get(
					"com.psit.struts.entity.LimOperate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(LimOperate instance) {
		log.debug("finding LimOperate instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding LimOperate instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from LimOperate as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOpeDesc(Object opeDesc) {
		return findByProperty(OPE_DESC, opeDesc);
	}


	public List findAll() {
		log.debug("finding all LimOperate instances");
		try {
			String queryString = "from LimOperate";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public LimOperate merge(LimOperate detachedInstance) {
		log.debug("merging LimOperate instance");
		try {
			LimOperate result = (LimOperate) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(LimOperate instance) {
		log.debug("attaching dirty LimOperate instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LimOperate instance) {
		log.debug("attaching clean LimOperate instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static LimOperateDAOImpl getFromApplicationContext(ApplicationContext ctx) {
		return (LimOperateDAOImpl) ctx.getBean("LimOperateDAO");
	}
}