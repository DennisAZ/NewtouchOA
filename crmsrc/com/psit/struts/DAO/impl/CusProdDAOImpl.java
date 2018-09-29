package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusProdDAO;
import com.psit.struts.entity.CusProd;

public class CusProdDAOImpl extends HibernateDaoSupport implements CusProdDAO{
	private static final Log log = LogFactory.getLog(CusProdDAO.class);
	// property constants
	public static final String CP_ID = "cpId";
	public static final String CP_PRICE = "cpPrice";
	public static final String CP_REMARK = "cpRemark";
	public static final String CP_CRE_USER = "cpCreUser";
	public static final String CP_OTHER_NAME = "cpOtherName";
	public static final String CP_PRE_NUMBER = "cpPreNumber";
	public static final String CP_WARN_DAY = "cpWarnDay";
	public static final String CP_SENT_DATE	= "cpSentDate";
	public static final String CP_COR_CODE = "cusCorCus.corCode";
	public static final String CP_CUS_NAME = "cusCorCus.corName";
	public static final String CP_WPR_ID = "wmsProduct.wprId";
	public static final String CP_WPR = "wmsProduct";
	public static final String WPRD_NAME = "wprName";
	public static final String CP_WPRD_NAME = "wmsProduct.wprName";
	public static final String CP_WPRD_MODEL = "wmsProduct.wprModel";
	public static final String CP_WPRD_SAL_PRICE = "wmsProduct.wprSalePrc";
	public static final String CP_WPRD_TYPE_NAME = "wmsProduct.typeList.typName";
	public static final String CP_WPRD_TYPE = "wmsProduct.typeList";
	
	protected void initDao() {
		// do nothing
	}
	
	

	/**
	 * 获得客户产品列表<br>
	 * @param args
	 * 			args[0]:是否删除
	 * 			args[1]:客户Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return  客户产品列表
	 */
	public List<CusProd> listCusProd(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session)super.getSession();
		Query query = getCusProdSql(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusProd> list = query.list();
		return list;
	}

	/**
	 * 获得客户产品数量<br>
	 * @param args
	 * 			args[0]:是否删除
	 * 			args[1]:客户Id
	 * @return count: 户产品数量
	 */
	public int listCusProdCount(String[] args) {
		Session session = (Session)super.getSession();
		Query query = getCusProdSql(session,args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getCusProdSql(Session session,String [] args, String orderItem,String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "cp.";
		String tab1 = "wpr.";
		String sql = "from CusProd cp left join "+tab+CP_WPR+" wpr ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql ="select cp "+sql;
		}
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CP_COR_CODE+"= "+args[0]+" ");
			}
			if(args[1]!=null && !args[1].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab1+WPRD_NAME+" like :wprName ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"customer","product","otherName","type","salPrice","cpPrice","cpPreNumber","cpWarnDay","cpSentDate","cpRemark"};
				String [] cols = {CP_CUS_NAME,CP_WPRD_NAME,CP_OTHER_NAME,CP_WPRD_TYPE_NAME,CP_WPRD_SAL_PRICE,CP_PRICE,CP_PRE_NUMBER,CP_WARN_DAY,CP_SENT_DATE,CP_REMARK};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch(i){
						case 3:
							sql += "left join "+tab+CP_WPRD_TYPE+" ";
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
				appendSql.append("order by "+tab+CP_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args !=null){
			if(args[1] !=null && !args[1].equals("")){
				query.setString("wprName", "%"+args[1]+"%");
			}
			
		}
		return query;
	}
	
	/**
	 * 编辑客户产品<br>
	 * @param cusProd 客户产品
	 */
	public void update(CusProd cusProd) {
		super.getHibernateTemplate().merge(cusProd);
	}
	
	/**
	 * 根据客户Id查找客户产品<br>
	 * @param cusId 客户Id
	 * @return 客户产品列表
	 */
	public List<CusProd> listByCusId(String cusId){
		Session session = (Session)super.getSession();
		String sql = "from CusProd where "+CP_COR_CODE+"= "+cusId;
		Query query = session.createQuery(sql);
		List<CusProd> list = query.list();
		return list;
	}
	
	/**
	 * 根据产品Id查找客户产品 <br>
	 * @param wprId 产品Id
	 * @return List<CusProd> 客户产品列表<br>
	 */
	public List<CusProd> listByWprId(String wprId){
		Session session = (Session)super.getSession();
		String sql = "from CusProd where "+CP_WPR_ID+"= "+wprId;
		Query query = session.createQuery(sql);
		List<CusProd> list = query.list();
		return list;
	}
	
	
	
	
	public void save(CusProd transientInstance) {
		log.debug("saving CusProd instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CusProd persistentInstance) {
		log.debug("deleting CusProd instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CusProd findById(java.lang.Long id) {
		log.debug("getting CusProd instance with id: " + id);
		try {
			CusProd instance = (CusProd) getHibernateTemplate().get(
					"com.psit.struts.entity.CusProd", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CusProd instance) {
		log.debug("finding CusProd instance by example");
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
		log.debug("finding CusProd instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from CusProd as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCpPrice(Object cpPrice) {
		return findByProperty(CP_PRICE, cpPrice);
	}

	public List findByCpRemark(Object cpRemark) {
		return findByProperty(CP_REMARK, cpRemark);
	}

	public List findByCpCreUser(Object cpCreUser) {
		return findByProperty(CP_CRE_USER, cpCreUser);
	}

	public List findAll() {
		log.debug("finding all CusProd instances");
		try {
			String queryString = "from CusProd";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CusProd merge(CusProd detachedInstance) {
		log.debug("merging CusProd instance");
		try {
			CusProd result = (CusProd) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CusProd instance) {
		log.debug("attaching dirty CusProd instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

}
