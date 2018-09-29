package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ProdSalBackDAO;
import com.psit.struts.entity.CusAddr;
import com.psit.struts.entity.ProdSalBack;
import com.psit.struts.util.format.StringFormat;

public class ProdSalBackDAOImpl extends HibernateDaoSupport implements ProdSalBackDAO {
	private static final Log log = LogFactory.getLog(ProdSalBackDAOImpl.class);
	// property constants
	public static final String PSB_ID = "psbId";
	public static final String PSB_PRICE = "psbPrice";
	public static final String PSB_RATE = "psbRate";
	
	public static final String PSB_PROD = "psbProduct";
	public static final String PSB_PROD_ID = "psbProduct.wprId";

	protected void initDao() {
		// do nothing
	}
	
	/**
	 * 根据产品Id查找提成率<br>
	 * @param prodId 产品Id
	 * @return 提成率列表
	 */
	public List<ProdSalBack> listByProdId(String prodId){
		Session session = (Session)super.getSession();
		String sql = "from ProdSalBack where "+PSB_PROD_ID+"= "+prodId;
		Query query = session.createQuery(sql);
		List<ProdSalBack> list = query.list();
		return list;
	}
	/**
	 * 提成率列表<br>
	 * @param prodId 产品Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return  提成率列表
	 */
	public List<ProdSalBack> list(String prodId, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session)super.getSession();
		Query query = getListSql(session,prodId,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdSalBack> list = query.list();
		return list;
	}
	public int listCount(String prodId) {
		Session session = (Session)super.getSession();
		Query query = getListSql(session,prodId,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getListSql(Session session,String prodId, String orderItem,String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "psb.";
		String sql = "from ProdSalBack psb ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql ="select psb "+sql;
		}
		if(!StringFormat.isEmpty(prodId)){
			appendSql.append("where "+tab+PSB_PROD_ID+"= "+prodId+" ");
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"price","rate"};
				String [] cols = {PSB_PRICE,PSB_RATE};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else{
				appendSql.append("order by "+tab+PSB_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	public void save(ProdSalBack transientInstance) {
		log.debug("saving ProdSalBack instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ProdSalBack persistentInstance) {
		log.debug("deleting ProdSalBack instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProdSalBack findById(java.lang.Long id) {
		log.debug("getting ProdSalBack instance with id: " + id);
		try {
			ProdSalBack instance = (ProdSalBack) getHibernateTemplate().get(
					"com.psit.struts.entity.ProdSalBack", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ProdSalBack instance) {
		log.debug("finding ProdSalBack instance by example");
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
		log.debug("finding ProdSalBack instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ProdSalBack as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPsbPrice(Object psbPrice) {
		return findByProperty(PSB_PRICE, psbPrice);
	}

	public List findByPsbRate(Object psbRate) {
		return findByProperty(PSB_RATE, psbRate);
	}

	public List findAll() {
		log.debug("finding all ProdSalBack instances");
		try {
			String queryString = "from ProdSalBack";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ProdSalBack merge(ProdSalBack detachedInstance) {
		log.debug("merging ProdSalBack instance");
		try {
			ProdSalBack result = (ProdSalBack) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ProdSalBack instance) {
		log.debug("attaching dirty ProdSalBack instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProdSalBack instance) {
		log.debug("attaching clean ProdSalBack instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}