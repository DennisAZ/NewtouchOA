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

import com.psit.struts.entity.ProdStore;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.DAO.ProdStoreDAO;

/**
 	* A data access object (DAO) providing persistence and search support for ProdStore entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .ProdStore
  * @author MyEclipse Persistence Tools 
 */

public class ProdStoreDAOImpl extends HibernateDaoSupport implements ProdStoreDAO {
    private static final Log log = LogFactory.getLog(ProdStoreDAOImpl.class);
	//property constants
    public static final String PST_ID = "pstId";
	public static final String PST_WMS_PRODUCT = "wmsProduct";
	public static final String PST_TYPE = "storeType";
	public static final String PST_PST_COUNT = "pstCount";
	public static final String PST_CRE_MAN = "pstCreMan";
	public static final String PST_UPD_MAN = "pstUpdMan";
	public static final String PST_NAME = "pstName";

	protected void initDao() {
		//do nothing
	}
    
	public int listCount(String[] args){
		Query query = getHql(args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public List<ProdStore> list(String[] args, String orderItem,String isDe, int currentPage,int pageSize){
		Query query = getHql(args,orderItem,isDe,false);
		query.setFirstResult((currentPage -1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdStore> list = query.list();
		return list;
	}
	
	private Query getHql(String[] args,String orderItem, String isDe, boolean isCount){
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "prodStore.";
		String hql = "from ProdStore prodStore ";
		if(isCount){
			hql = "select count(prodStore) " +hql;
		}else{
			hql = "select prodStore " +hql;
		}
		if(args !=null){
			if(args.length >1){
				if(!StringFormat.isEmpty(args[0])){
					appendHql.append(appendHql.length()==0?"where ":"and ");
					appendHql.append(tab+PST_WMS_PRODUCT+"."+WmsProductDAOImpl.WPR_NAME +" like :wprName ");
				}
				if(!StringFormat.isEmpty(args[1])){
					appendHql.append(appendHql.length()==0?"where ":"and ");
					appendHql.append(tab+PST_TYPE+"."+TypeListDAOImpl.TYP_ID+"="+args[1]+" ");
				}
			}else{
				if(!StringFormat.isEmpty(args[0])){
					appendHql.append(appendHql.length()==0?"where ":"and ");
					appendHql.append(tab+PST_WMS_PRODUCT+"."+WmsProductDAOImpl.WPR_ID +"="+args[0]+" ");
				}
			}
		}
		if(!isCount){
			String[] sortHqls = getSortHql(hql,tab,orderItem,isDe);
			hql += sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql += appendHql.toString();
		Query query = session.createQuery(hql);
		if(args!=null && args.length >1){
			if(!StringFormat.isEmpty(args[0])){
				query.setString("wprName", "%"+args[0]+"%");
			}
		}
		return query;
	}
	
	private String[] getSortHql(String hql,String tab, String orderItem,String isDe){
		String joinHql = "";
		String sortHql = "";
		if(orderItem !=null && !orderItem.equals("")){
			String[] items = {"pstName","wprName","type","pstCount"};
			String[] cols = {PST_NAME,PST_WMS_PRODUCT+"."+WmsProductDAOImpl.WPR_NAME,PST_TYPE+"."+TypeListDAOImpl.TYP_NAME,PST_PST_COUNT};
			for(int i=0;i<items.length;i++){
				if(orderItem.equals(items[i])){
					switch(i){
						case 1:
							joinHql += "left join " + tab + PST_WMS_PRODUCT +" ";
							break;
						case 2:
							joinHql += "left join " + tab + PST_TYPE +" ";
							break;
					}
					orderItem = cols[i];
				}
			}
			sortHql = "order by "+tab+orderItem+" ";
			if(isDe !=null && isDe.equals("1")){
				sortHql += "desc ";
			}
		}else{
			sortHql = "order by "+tab+PST_ID+" desc ";
		}
		return new String[]{joinHql,sortHql};
	}
	
	public void delete(String id){
		Session session = (Session) super.getSession();
		String hql = "delete from ProdStore where "+PST_ID+"="+id;
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
    public void save(ProdStore transientInstance) {
        log.debug("saving ProdStore instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ProdStore persistentInstance) {
        log.debug("deleting ProdStore instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ProdStore findById( java.lang.Long id) {
        log.debug("getting ProdStore instance with id: " + id);
        try {
            ProdStore instance = (ProdStore) getHibernateTemplate()
                    .get("com.psit.struts.entity.ProdStore", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ProdStore instance) {
        log.debug("finding ProdStore instance by example");
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
      log.debug("finding ProdStore instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ProdStore as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
	
	public List findByPstPstCount(Object pstPstCount
	) {
		return findByProperty(PST_PST_COUNT, pstPstCount
		);
	}
	
	public List findByPstCreMan(Object pstCreMan
	) {
		return findByProperty(PST_CRE_MAN, pstCreMan
		);
	}
	
	public List findByPstUpdMan(Object pstUpdMan
	) {
		return findByProperty(PST_UPD_MAN, pstUpdMan
		);
	}
	

	public List findAll() {
		log.debug("finding all ProdStore instances");
		try {
			String queryString = "from ProdStore";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public ProdStore merge(ProdStore detachedInstance) {
        log.debug("merging ProdStore instance");
        try {
            ProdStore result = (ProdStore) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ProdStore instance) {
        log.debug("attaching dirty ProdStore instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ProdStore instance) {
        log.debug("attaching clean ProdStore instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ProdStoreDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (ProdStoreDAOImpl) ctx.getBean("ProdStoreDAO");
	}
}