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

import com.psit.struts.entity.ProdIn;
import com.psit.struts.DAO.ProdInDAO;
import com.psit.struts.util.format.StringFormat;

/**
 	* A data access object (DAO) providing persistence and search support for ProdIn entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .ProdIn
  * @author MyEclipse Persistence Tools 
 */

public class ProdInDAOImpl extends HibernateDaoSupport  implements ProdInDAO{
    private static final Log log = LogFactory.getLog(ProdInDAOImpl.class);
	//property constants
    public static final String PIN_ID = "pinId";
	public static final String PIN_CODE = "pinCode";
	public static final String PIN_PUR_ORD = "purOrd";
	public static final String PIN_PRO_STORE = "prodStore";
	public static final String PIN_RESP_MAN = "pinRespMan";
	public static final String PIN_STATE = "pinState";
	public static final String PIN_DATE = "pinDate";
	public static final String PIN_IN_NUM = "pinInNum";
	public static final String PIN_REMARK = "pinRemark";
	public static final String PIN_CRE_MAN = "pinCreMan";
	public static final String PIN_UPD_MAN = "pinUpdMan";



	protected void initDao() {
		//do nothing
	}
    
	
	public int listCount(String[] args){
		Query query = getHql(args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public List<ProdIn> list(String[] args,String orderItem, String isDe,int currentPage, int pageSize){
		Query query = getHql(args,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1)*pageSize);
		query.setMaxResults(pageSize);
		List<ProdIn> list = query.list();
		return list;
	}
	
	private Query getHql(String[] args, String orderItem, String isDe,boolean isCount){
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "prodIn.";//±íÃû
		String hql = "from ProdIn prodIn ";
		if (isCount) {
			hql = "select count(prodIn) " + hql;
		} else {
			hql = "select prodIn " + hql;
		}
		if(args !=null){
			if(!StringFormat.isEmpty(args[0])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+PIN_PRO_STORE+"."+ProdStoreDAOImpl.PST_WMS_PRODUCT+"."+WmsProductDAOImpl.WPR_NAME+" like :wprName ");
			}
			if(!StringFormat.isEmpty(args[1])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+PIN_PRO_STORE+"."+ProdStoreDAOImpl.PST_TYPE+"."+TypeListDAOImpl.TYP_ID+"="+args[1]+" ");
			}
		}
		if(!isCount){
			String[] sortHqls = getSortHql(hql,tab,orderItem,isDe);
			hql +=sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql +=appendHql.toString();
		Query query = session.createQuery(hql);
		if(args !=null){
			if(!StringFormat.isEmpty(args[0])){
				query.setString("wprName", "%"+args[0]+"%");
			}
		}
		return query;
	}
	
	private String[] getSortHql(String hql, String tab, String orderItem, String isDe){
		String joinHql ="";
		String sortHql = "";
		if(orderItem !=null && !orderItem.equals("")){
			String[] items = {"pinCode","purOrd","pinRespMan","pinDate","pinState","pinInNum"};
			String[] cols = {PIN_CODE,PIN_PUR_ORD+"."+PurOrdDAOImpl.PUO_CODE,PIN_RESP_MAN,PIN_DATE,PIN_STATE,PIN_IN_NUM};
			for(int i=0;i<items.length;i++){
				if(orderItem.equals(items[i])){
					switch(i){
					case 1:
						joinHql += "left join " + tab +PIN_PUR_ORD+" ";
						break;
					}
					orderItem = cols[i];
				}
			}
			sortHql ="order by "+tab+orderItem +" ";
			if(isDe !=null && isDe.equals("1")){
				sortHql +="desc ";
			}
		}else{
			sortHql ="order by "+tab + PIN_ID+" desc ";
		}
		return new String[]{joinHql,sortHql};
	}
	
	public void delProdInById(String pinId){
		Session session = (Session) super.getSession();
		String hql = "delete from ProdIn where " + PIN_ID + "=" + pinId;
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
    public void save(ProdIn transientInstance) {
        log.debug("saving ProdIn instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ProdIn persistentInstance) {
        log.debug("deleting ProdIn instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ProdIn findById( java.lang.Long id) {
        log.debug("getting ProdIn instance with id: " + id);
        try {
            ProdIn instance = (ProdIn) getHibernateTemplate()
                    .get("com.psit.struts.entity.ProdIn", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ProdIn instance) {
        log.debug("finding ProdIn instance by example");
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
      log.debug("finding ProdIn instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ProdIn as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByPinCode(Object pinCode
	) {
		return findByProperty(PIN_CODE, pinCode
		);
	}

	
	public List findByPinRespMan(Object pinRespMan
	) {
		return findByProperty(PIN_RESP_MAN, pinRespMan
		);
	}
	
	public List findByPinState(Object pinState
	) {
		return findByProperty(PIN_STATE, pinState
		);
	}
	
	public List findByPinRemark(Object pinRemark
	) {
		return findByProperty(PIN_REMARK, pinRemark
		);
	}
	
	public List findByPinCreMan(Object pinCreMan
	) {
		return findByProperty(PIN_CRE_MAN, pinCreMan
		);
	}
	
	public List findByPinUpdMan(Object pinUpdMan
	) {
		return findByProperty(PIN_UPD_MAN, pinUpdMan
		);
	}
	

	public List findAll() {
		log.debug("finding all ProdIn instances");
		try {
			String queryString = "from ProdIn";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public ProdIn merge(ProdIn detachedInstance) {
        log.debug("merging ProdIn instance");
        try {
            ProdIn result = (ProdIn) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ProdIn instance) {
        log.debug("attaching dirty ProdIn instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ProdIn instance) {
        log.debug("attaching clean ProdIn instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ProdInDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (ProdInDAOImpl) ctx.getBean("ProdInDAO");
	}
}