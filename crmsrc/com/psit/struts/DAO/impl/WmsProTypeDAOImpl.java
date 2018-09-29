package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsProTypeDAO;
import com.psit.struts.entity.WmsProType;

/**
 * 商品类别DAO实现类 <br>
 */
public class WmsProTypeDAOImpl extends HibernateDaoSupport implements WmsProTypeDAO{
	private static final Log log = LogFactory.getLog(WmsProTypeDAOImpl.class);
	// property constants
	public static final String WPT_NAME = "wptName";
	public static final String WPT_DESC = "wptDesc";
	public static final String WPT_ISENABLED = "wptIsenabled";

	protected void initDao() {
		// do nothing
	}
	/**
	 * 添加商品类别 <br>
	 * @param transientInstance 商品类别实体
	 */
	public void save(WmsProType transientInstance) {
		log.debug("saving WmsProType instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 删除商品类别 <br>
	 * @param persistentInstance 商品类别实体
	 */
	public void delete(WmsProType persistentInstance) {
		log.debug("deleting WmsProType instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * 根据商品类别Id查询类别实体 <br>
	 * @param id 商品类别Id
	 * @return WmsProType 返回商品类别实体
	 */
	public WmsProType findById(java.lang.Long id) {
		log.debug("getting WmsProType instance with id: " + id);
		try {
			WmsProType instance = (WmsProType) getHibernateTemplate().get(
					"com.psit.struts.entity.WmsProType", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * 查询所有已使用的商品类别 <br>
	 * @return List 类别列表
	 */
	public List<WmsProType> findAll() {
		log.debug("finding all WmsProType instances");
		try {
			String queryString = "from WmsProType";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 查询所有已使用的商品类别 <br>
	 * @return List 类别列表
	 */
	public List findAllWptType() {
		log.debug("finding all WmsProType instances");
		try {
			String queryString = "from WmsProType where wptIsenabled=1";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 更新商品类别 <br>
	 * @param instance 商品类别实体
	 */
	public void updateProType(WmsProType instance){
		log.debug("attaching dirty WmsProType instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * 查询商品类别的下级类别 <br>
	 * @param wptId 商品类别id
	 * @return List 返回商品类别列表
	 */
	public List findByUpId(Long wptId) {
		log.debug("finding WmsProType instance with property: ");
		try {
			String queryString = "from WmsProType as model where model.wmsProType.wptId="+wptId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 根据客户Id查询商品类别<br>
	 * @param cusId 客户Id
	 * @return List<WmsProType> 商品类别<br>
	 */
	public List<WmsProType> findByCusId(String cusId){
		Session session = (Session) super.getSession();
		String sql = "from WmsProduct as wpd where wpd.wprId in ( select cp.wmsProduct.wprId from CusProd as cp where cp.cusCorCus.corCode ="+cusId+" )";
		Query query = session.createQuery(sql);
		List<WmsProType> list = query.list();
		return list;
	}
}