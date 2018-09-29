package com.psit.struts.DAO.impl;
// default package

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WhRecDAO;
import com.psit.struts.entity.WhRec;
import com.psit.struts.util.format.StringFormat;

/**
 	* A data access object (DAO) providing persistence and search support for WhRec entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .WhRec
  * @author MyEclipse Persistence Tools 
 */

public class WhRecDAOImpl extends HibernateDaoSupport implements WhRecDAO {
    private static final Log log = LogFactory.getLog(WhRecDAOImpl.class);
	//property constants
    public static final String WRE_ID = "wreId";
	public static final String WRE_PROD_STORE = "prodStore";
	public static final String WRE_COUNT = "wreCount";
	public static final String WRE_TYPE = "wreType";
	public static final String WRE_MAN = "wreMan";
	public static final String WRE_LEFT_COUNT = "wreLeftCount";
	public static final String WRE_TIME = "wreTime";
	

	protected void initDao() {
		//do nothing
	}
    
	public int listCount(String[] args){
		Query query = getHql(args, null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public List<WhRec> list(String[] args, String orderItem, String isDe,int currentPage, int pageSize){
		Query query = getHql(args,orderItem,isDe,false);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List<WhRec> list = query.list();
		return list;
	}
	
	private Query getHql(String[] args, String orderItem, String isDe, boolean isCount){
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "whRec.";//±íÃû
		String hql = "from WhRec whRec ";
		if(isCount){
			hql = "select count(whRec) " + hql;
		}else{
			hql = "select whRec " + hql;
		}
		if(args!=null){
			if(!StringFormat.isEmpty(args[0])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+WRE_PROD_STORE+"."+ProdStoreDAOImpl.PST_ID+"="+args[0]+" ");
			}
		}
		if(!isCount){
			String[] sortHqls = getSortHql(tab,orderItem,isDe);
			hql += sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql +=appendHql.toString();
		Query query = session.createQuery(hql);
		return query;
	}
	
	private String[] getSortHql(String tab, String orderItem, String isDe) {
		String joinHql = "";
		String sortHql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "wreCount", "wreType", "wreTime", "wreMan",
					"wreLeftCount" };
			String[] cols = { WRE_COUNT, WRE_TYPE, WRE_TIME, WRE_MAN,
					WRE_LEFT_COUNT };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					orderItem = cols[i];
				}
			}
			sortHql = "order by " + tab + orderItem + " ";
			if (isDe != null && isDe.equals("1")) {
				sortHql += "desc ";
			}
		} else {// Ä¬ÈÏÅÅÐò
			sortHql = "order by " + tab + WRE_ID + " desc ";
		}
		return new String[] { joinHql, sortHql };
	}
	
    public void save(WhRec transientInstance) {
        log.debug("saving WhRec instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(WhRec persistentInstance) {
        log.debug("deleting WhRec instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public WhRec findById( java.lang.Long id) {
        log.debug("getting WhRec instance with id: " + id);
        try {
            WhRec instance = (WhRec) getHibernateTemplate()
                    .get("com.psit.struts.entity.WhRec", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(WhRec instance) {
        log.debug("finding WhRec instance by example");
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
      log.debug("finding WhRec instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from WhRec as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	
	public List findByWreCount(Object wreCount
	) {
		return findByProperty(WRE_COUNT, wreCount
		);
	}
	
	public List findByWreType(Object wreType
	) {
		return findByProperty(WRE_TYPE, wreType
		);
	}
	
	public List findByWreMan(Object wreMan
	) {
		return findByProperty(WRE_MAN, wreMan
		);
	}
	
	public List findByWreLeftCount(Object wreLeftCount
	) {
		return findByProperty(WRE_LEFT_COUNT, wreLeftCount
		);
	}
	

	public List findAll() {
		log.debug("finding all WhRec instances");
		try {
			String queryString = "from WhRec";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public WhRec merge(WhRec detachedInstance) {
        log.debug("merging WhRec instance");
        try {
            WhRec result = (WhRec) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(WhRec instance) {
        log.debug("attaching dirty WhRec instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(WhRec instance) {
        log.debug("attaching clean WhRec instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static WhRecDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (WhRecDAOImpl) ctx.getBean("WhRecDAO");
	}
}