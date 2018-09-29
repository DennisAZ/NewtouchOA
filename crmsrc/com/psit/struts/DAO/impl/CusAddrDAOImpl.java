package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusAddrDAO;
import com.psit.struts.entity.CusAddr;
import com.psit.struts.util.format.StringFormat;

public class CusAddrDAOImpl extends HibernateDaoSupport implements CusAddrDAO {
	private static final Log log = LogFactory.getLog(CusAddrDAOImpl.class);
	// property constants
	public static final String CAD_ID = "cadId";
	public static final String CAD_ADDR = "cadAddr";
	public static final String CAD_POST_CODE = "cadPostCode";
	public static final String CAD_CONTACT = "cadContact";
	public static final String CAD_PHO = "cadPho";
	public static final String CAD_COR_CODE = "cadCus.corCode";
	public static final String CAD_PROV = "cadProv";
	public static final String CAD_CITY = "cadCity";
	public static final String CAD_DISTRICT = "cadDistrict";
	public static final String CAD_PROV_NAME = "cadProv.areName";
	public static final String CAD_CITY_NAME = "cadCity.prvName";
	public static final String CAD_DISTRICT_NAME = "cadDistrict.cityName";

	protected void initDao() {
		// do nothing
	}
	
	/**
	 * 根据客户Id查找客户地址<br>
	 * @param cusId 客户Id
	 * @return 客户产品列表
	 */
	public List<CusAddr> listByCusId(String cusId){
		Session session = (Session)super.getSession();
		String sql = "from CusAddr where "+CAD_COR_CODE+"= "+cusId;
		Query query = session.createQuery(sql);
		List<CusAddr> list = query.list();
		return list;
	}
	/**
	 * 客户地址<br>
	 * @param cusId 客户Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return  客户地址列表
	 */
	public List<CusAddr> list(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session)super.getSession();
		Query query = getCusAddrSql(session,cusId,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusAddr> list = query.list();
		return list;
	}
	public int listCount(String cusId) {
		Session session = (Session)super.getSession();
		Query query = getCusAddrSql(session,cusId,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getCusAddrSql(Session session,String cusId, String orderItem,String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "cp.";
		String sql = "from CusAddr cp ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql ="select cp "+sql;
		}
		if(!StringFormat.isEmpty(cusId)){
			appendSql.append("where "+tab+CAD_COR_CODE+"= "+cusId+" ");
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"addr","area","postCode","contact","phone"};
				String cArea = CAD_PROV_NAME + ", " + tab + CAD_CITY_NAME + ", "
						+ tab + CAD_DISTRICT_NAME;
				if(isDe!=null && isDe.equals("1")){
					cArea = CAD_PROV_NAME + " desc, " + tab + CAD_CITY_NAME + " desc, "
					+ tab + CAD_DISTRICT_NAME;
				}
				String [] cols = {CAD_ADDR,cArea,CAD_POST_CODE,CAD_CONTACT,CAD_PHO};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch(i){
						case 1:
							sql += "left join " + tab + CAD_PROV + " left join " + tab
								+ CAD_CITY + " left join " + tab + CAD_DISTRICT + " "; 
							break;
						}
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
				appendSql.append("order by "+tab+CAD_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	public void save(CusAddr transientInstance) {
		log.debug("saving CusAddr instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CusAddr persistentInstance) {
		log.debug("deleting CusAddr instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CusAddr findById(java.lang.Long id) {
		log.debug("getting CusAddr instance with id: " + id);
		try {
			CusAddr instance = (CusAddr) getHibernateTemplate().get(
					"com.psit.struts.entity.CusAddr", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CusAddr instance) {
		log.debug("finding CusAddr instance by example");
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
		log.debug("finding CusAddr instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from CusAddr as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCadCorCode(Object cadCorCode) {
		return findByProperty(CAD_COR_CODE, cadCorCode);
	}

	public List findByCadAddr(Object cadAddr) {
		return findByProperty(CAD_ADDR, cadAddr);
	}

	public List findByCadPostCode(Object cadPostCode) {
		return findByProperty(CAD_POST_CODE, cadPostCode);
	}

	public List findByCadContact(Object cadContact) {
		return findByProperty(CAD_CONTACT, cadContact);
	}

	public List findByCadPho(Object cadPho) {
		return findByProperty(CAD_PHO, cadPho);
	}

	public List<CusAddr> findAll() {
		log.debug("finding all CusAddr instances");
		try {
			String queryString = "from CusAddr";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CusAddr merge(CusAddr detachedInstance) {
		log.debug("merging CusAddr instance");
		try {
			CusAddr result = (CusAddr) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CusAddr instance) {
		log.debug("attaching dirty CusAddr instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CusAddr instance) {
		log.debug("attaching clean CusAddr instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CusAddrDAOImpl getFromApplicationContext(ApplicationContext ctx) {
		return (CusAddrDAOImpl) ctx.getBean("CusAddrDAO");
	}
}