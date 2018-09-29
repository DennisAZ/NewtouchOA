package com.psit.struts.DAO.impl;
// default package

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SupProdDAO;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.SupProd;

/**
 	* A data access object (DAO) providing persistence and search support for SupProd entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .SupProd
  * @author MyEclipse Persistence Tools 
 */

public class SupProdDAOImpl extends HibernateDaoSupport implements SupProdDAO  {
    private static final Log log = LogFactory.getLog(SupProdDAO.class);
	//property constants
    public static final String SP_ID = "spId";
	public static final String SP_SUP_ID = "supplier.supId";
	public static final String SP_WPR = "wmsProduct";
	public static final String WPRD_NAME = "wprName";
	public static final String SP_WPRD_NAME = "wmsProduct.wprName";
	public static final String SP_OTHER_NAME = "spOtherName";
	public static final String SP_WPRD_TYPE_NAME = "wmsProduct.typeList.typName";
	public static final String SP_WPRD_SAL_PRICE = "wmsProduct.wprSalePrc";
	public static final String SP_HAS_TAX = "spHasTax";
	public static final String SP_PRICE = "spPrice";
	public static final String SP_REMARK = "spRemark";
	public static final String SP_CRE_USER = "spCreUser";
	public static final String SP_UPD_USER = "spUpdUser";
	public static final String SP_WPRD_TYPE = "wmsProduct.typeList";


	protected void initDao() {
		//do nothing
	}
    
	public void deleteById(String spId) {
		Session session = (Session) super.getSession();
		Query query = session.createQuery("delete from SupProd where "+SP_ID+"="+spId);
		query.executeUpdate();
	}

	/**
	 * 获得供应商产品列表<br>
	 * @param args > 0:供应商Id;1:产品名称
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return 供应商产品策略 list
	 */
	public List<SupProd> list(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize) {
		Session  session = (Session)super.getSession();
		Query query = getQuery(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage -1)*pageSize);
		query.setMaxResults(pageSize);
		List<SupProd> list = query.list();
		return list;
	}

	/**
	 * 获得供应商产品数量<br>
	 * @param args
	 * @return 
	 */
	public int listCount(String[] args) {
		Session session = (Session)super.getSession();
		Query query = getQuery(session,args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getQuery(Session session,String[] args,String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "sp.";
		String tab1 = "wpr.";
		String sql = "from SupProd sp left join "+tab+SP_WPR+" wpr ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql ="select sp "+sql;
		}
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SP_SUP_ID+"= "+args[0]+" ");
			}
			if(args[1]!=null && !args[1].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab1+WPRD_NAME+" like :wprName ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"product","otherName","type","salPrice","spPrice","spRemark"};
				String [] cols = {SP_WPRD_NAME,SP_OTHER_NAME,SP_WPRD_TYPE_NAME,SP_WPRD_SAL_PRICE,SP_PRICE,SP_REMARK};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch(i){
						case 2:
							sql += "left join "+tab+SP_WPRD_TYPE+" ";
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
				appendSql.append("order by "+tab+SP_ID+" desc ");
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
	 * 根据供应商Id查找供应商产品<br>
	 * @param supId 供应商Id
	 * @return 供应商产品列表
	 */
	public List<SupProd> listBySupId(String supId){
		Session session = (Session)super.getSession();
		String sql = "from SupProd where "+SP_SUP_ID+"= "+supId;
		Query query = session.createQuery(sql);
		List<SupProd> list = query.list();
		return list;
	}
	
	
    public void save(SupProd transientInstance) {
        log.debug("saving SupProd instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(SupProd persistentInstance) {
        log.debug("deleting SupProd instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public SupProd findById( java.lang.Long id) {
        log.debug("getting SupProd instance with id: " + id);
        try {
            SupProd instance = (SupProd) getHibernateTemplate()
                    .get("com.psit.struts.entity.SupProd", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(SupProd instance) {
        log.debug("finding SupProd instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding SupProd instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from SupProd as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findBySpSupId(Object spSupId
	) {
		return findByProperty(SP_SUP_ID, spSupId
		);
	}
	
	public List findBySpOtherName(Object spOtherName
	) {
		return findByProperty(SP_OTHER_NAME, spOtherName
		);
	}
	
	public List findBySpHasTax(Object spHasTax
	) {
		return findByProperty(SP_HAS_TAX, spHasTax
		);
	}
	
	public List findBySpPrice(Object spPrice
	) {
		return findByProperty(SP_PRICE, spPrice
		);
	}
	
	public List findBySpRemark(Object spRemark
	) {
		return findByProperty(SP_REMARK, spRemark
		);
	}
	
	public List findBySpCreUser(Object spCreUser
	) {
		return findByProperty(SP_CRE_USER, spCreUser
		);
	}
	
	public List findBySpUpdUser(Object spUpdUser
	) {
		return findByProperty(SP_UPD_USER, spUpdUser
		);
	}
	

	public List findAll() {
		log.debug("finding all SupProd instances");
		try {
			String queryString = "from SupProd";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public SupProd merge(SupProd detachedInstance) {
        log.debug("merging SupProd instance");
        try {
            SupProd result = (SupProd) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SupProd instance) {
        log.debug("attaching dirty SupProd instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(SupProd instance) {
        log.debug("attaching clean SupProd instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static SupProdDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (SupProdDAOImpl) ctx.getBean("SupProdDAO");
	}


}