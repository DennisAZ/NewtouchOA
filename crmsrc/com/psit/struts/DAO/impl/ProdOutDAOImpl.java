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

import com.psit.struts.entity.ProdOut;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.DAO.ProdOutDAO;;

/**
 	* A data access object (DAO) providing persistence and search support for ProdOut entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .ProdOut
  * @author MyEclipse Persistence Tools 
 */

public class ProdOutDAOImpl extends HibernateDaoSupport implements ProdOutDAO {
    private static final Log log = LogFactory.getLog(ProdOutDAOImpl.class);
	//property constants
    public static final String POU_ID = "pouId";
	public static final String POU_CODE = "pouCode";
	public static final String POU_SOD_CON = "salOrdCon";
	public static final String POU_DATE = "pouDate";
	public static final String POU_PROD_STORE = "prodStore";
	public static final String POU_STATE = "pouState";
	public static final String POU_REMARK = "pouRemark";
	public static final String POU_PICK_MAN = "pouPickMan";
	public static final String POU_RESP_MAN = "pouRespMan";
	public static final String POU_CRE_MAN = "pouCreMan";
	public static final String POU_UPD_MAN = "pouUpdMan";
	public static final String POU_OUT_NUM = "pouOutNum";



	protected void initDao() {
		//do nothing
	}
    
	public int listCount(String[] args){
		Query query = getHql(args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public List<ProdOut> list(String[] args,String orderItem,String isDe,int currnetPage,int pageSize){
		Query query = getHql(args,orderItem,isDe,false);
		query.setFirstResult((currnetPage -1)*pageSize);
		query.setMaxResults(pageSize);
		List<ProdOut> list = query.list();
		return list;
	}
	
	private Query getHql(String[] args, String orderItem, String isDe, boolean isCount){
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "prodOut.";
		String hql = "from ProdOut prodOut ";
		if(isCount){
			hql = "select count(prodOut) " + hql+" ";
		}else{
			hql = "select prodOut " + hql+" ";
		}
		if(args!=null){
			if(!StringFormat.isEmpty(args[0])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+POU_PROD_STORE+"."+ProdStoreDAOImpl.PST_WMS_PRODUCT+"."+WmsProductDAOImpl.WPR_NAME+" like :wprName ");
			}
			if(!StringFormat.isEmpty(args[1])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+POU_PROD_STORE+"."+ProdStoreDAOImpl.PST_TYPE+"."+TypeListDAOImpl.TYP_ID+"="+args[1]+" ");
			}
		}
		if(!isCount){
			String[] sortHqls = getSortHql(tab,orderItem,isDe);
			hql +=sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql += appendHql.toString();
		Query query = session.createQuery(hql);
		if(args !=null){
			if(!StringFormat.isEmpty(args[0])){
				query.setString("wprName", "%"+args[0]+"%");
			}
		}
		return query;
	}
	
	private String[] getSortHql(String tab,String orderItem, String isDe){
		String joinHql = "";
		String sortHql = "";
		if(orderItem !=null && !orderItem.equals("")){
			String[] items = {"pouCode","ordCon","pouDate","pouState","pouOutNum","pouPickMan","pouRespMan"};
			String[] cols = {POU_CODE,POU_SOD_CON+"."+SalOrderDAOImpl.ORD_TIL,POU_DATE,POU_STATE,POU_OUT_NUM,POU_PICK_MAN,POU_RESP_MAN};
			for(int i=0;i<items.length;i++){
				if(orderItem.equals(items[i])){
					switch(i){
						case 1:
							joinHql += "left join "+tab +POU_SOD_CON+" ";
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
			sortHql = "order by "+ tab + POU_ID +" desc ";
		}
		return new String[]{joinHql,sortHql};
	}
	
	public void deleteById(String pouId){
		Session session = (Session) super.getSession();
		Query query = session.createQuery("delete from ProdOut where "+POU_ID+"="+pouId);
		query.executeUpdate();
	}
	
    public void save(ProdOut transientInstance) {
        log.debug("saving ProdOut instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ProdOut persistentInstance) {
        log.debug("deleting ProdOut instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ProdOut findById( java.lang.Long id) {
        log.debug("getting ProdOut instance with id: " + id);
        try {
            ProdOut instance = (ProdOut) getHibernateTemplate()
                    .get("com.psit.struts.entity.ProdOut", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ProdOut instance) {
        log.debug("finding ProdOut instance by example");
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
      log.debug("finding ProdOut instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ProdOut as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByPouCode(Object pouCode
	) {
		return findByProperty(POU_CODE, pouCode
		);
	}
	
	public List findByPuoState(Object pouState
	) {
		return findByProperty(POU_STATE, pouState
		);
	}
	
	public List findByPuoRemark(Object pouRemark
	) {
		return findByProperty(POU_REMARK, pouRemark
		);
	}
	
	public List findByPuoPickMan(Object pouPickMan
	) {
		return findByProperty(POU_PICK_MAN, pouPickMan
		);
	}
	
	public List findByPuoRespMan(Object pouRespMan
	) {
		return findByProperty(POU_RESP_MAN, pouRespMan
		);
	}
	
	public List findByPuoCreMan(Object pouCreMan
	) {
		return findByProperty(POU_CRE_MAN, pouCreMan
		);
	}
	
	public List findByPuoUpdMan(Object pouUpdMan
	) {
		return findByProperty(POU_UPD_MAN, pouUpdMan
		);
	}
	

	public List findAll() {
		log.debug("finding all ProdOut instances");
		try {
			String queryString = "from ProdOut";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public ProdOut merge(ProdOut detachedInstance) {
        log.debug("merging ProdOut instance");
        try {
            ProdOut result = (ProdOut) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ProdOut instance) {
        log.debug("attaching dirty ProdOut instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ProdOut instance) {
        log.debug("attaching clean ProdOut instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ProdOutDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (ProdOutDAOImpl) ctx.getBean("ProdOutDAO");
	}
}