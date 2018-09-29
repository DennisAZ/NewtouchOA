package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RWmsChangeDAO;
import com.psit.struts.entity.RWmsChange;
/**
 * 
 * 盘点明细DAO实现类 <br>
 *
 * create_date: Aug 17, 2010,4:34:18 PM<br>
 * @author zjr
 */
public class RWmsChangeDAOImpl extends HibernateDaoSupport implements RWmsChangeDAO{
	private static final Log log = LogFactory.getLog(RWmsChangeDAOImpl.class);
	// property constants
	public static final String RWC_DIFFERENT = "rwcDifferent";
	public static final String RMC_TYPE = "rmcType";
	public static final String WMC_REMARK = "wmcRemark";

//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * 保存盘点明细 <br>
	 * create_date: Aug 12, 2010,10:35:49 AM <br>
	 * @param transientInstance 盘点明细实体
	 */
	public void save(RWmsChange transientInstance) {
		log.debug("saving RWmsChange instance");
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
	 * 删除盘点明细<br>
	 * create_date: Aug 12, 2010,10:39:25 AM <br>
	 * @param persistentInstance 盘点明细实体
	 */
	public void delete(RWmsChange persistentInstance) {
		log.debug("deleting RWmsChange instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据Id得到盘点明细实体 <br>
	 * create_date: Aug 12, 2010,10:36:16 AM <br>
	 * @param id 盘点明细id
	 * @return RWmsChange 返回盘点明细实体
	 */
	public RWmsChange findById(java.lang.Long id) {
		log.debug("getting RWmsChange instance with id: " + id);
		try {
			RWmsChange instance = (RWmsChange) getHibernateTemplate().get(
					"com.psit.struts.entity.RWmsChange", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
//	public List findByExample(RWmsChange instance) {
//		log.debug("finding RWmsChange instance by example");
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
//		log.debug("finding RWmsChange instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RWmsChange as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByRwcDifferent(Object rwcDifferent) {
//		return findByProperty(RWC_DIFFERENT, rwcDifferent);
//	}
//
//	public List findByRmcType(Object rmcType) {
//		return findByProperty(RMC_TYPE, rmcType);
//	}
//
//	public List findByWmcRemark(Object wmcRemark) {
//		return findByProperty(WMC_REMARK, wmcRemark);
//	}
//
//	public List findAll() {
//		log.debug("finding all RWmsChange instances");
//		try {
//			String queryString = "from RWmsChange";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public RWmsChange merge(RWmsChange detachedInstance) {
//		log.debug("merging RWmsChange instance");
//		try {
//			RWmsChange result = (RWmsChange) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RWmsChange instance) {
//		log.debug("attaching dirty RWmsChange instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RWmsChange instance) {
//		log.debug("attaching clean RWmsChange instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static RWmsChangeDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RWmsChangeDAOImpl) ctx.getBean("RWmsChangeDAO");
//	}
	/**
	 * 
	 * 更新盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:00 AM <br>
	 * @param instance 盘点明细实体
	 */
	public void updateRwmc(RWmsChange instance){
		log.debug("attaching dirty RWmsChange instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据盘点单id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,10:39:47 AM <br>
	 * @param wmcId 盘点单id
	 * @return List 返回盘点明细列表
	 */
	public List findByWmc(Long wmcId) {
		log.debug("finding all RWmsChange instances");
		try {
			String queryString = "from RWmsChange as rwc where rwc.wmsCheck.wmcId="+wmcId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 *  根据商品id查询盘点明细<br>
	 * create_date: Aug 12, 2010,12:03:40 PM <br>
	 * @param wprId 商品id
	 * @return List 返回盘点明细列表
	 */
	public List findByWpr(Long wprId) {
		log.debug("finding all RWmsChange instances");
		try {
			String queryString = "from RWmsChange where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据盘点记录id和商品id查询盘点明细 <br>
	 * create_date: Aug 12, 2010,12:06:56 PM <br>
	 * @param wprId 商品id
	 * @param wmcId 盘点记录id
	 * @return List 返回出盘点明细列表
	 */
	public List findByWmc(Long wprId,Long wmcId) {
		log.debug("finding all RWmsChange instances");
		try {
			String queryString = "from RWmsChange where wmsProduct.wprId = "+wprId+" and wmsCheck.wmcId ="+wmcId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}