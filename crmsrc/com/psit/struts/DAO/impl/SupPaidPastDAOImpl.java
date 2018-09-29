package com.psit.struts.DAO.impl;
// default package

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SupPaidPastDAO;
import com.psit.struts.entity.SupPaidPast;

/**
 	* A data access object (DAO) providing persistence and search support for SupPaidPast entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .SupPaidPast
  * @author MyEclipse Persistence Tools 
 */

public class SupPaidPastDAOImpl extends HibernateDaoSupport implements  SupPaidPastDAO{
    private static final Log log = LogFactory.getLog(SupPaidPastDAOImpl.class);
	//property constants
    public static final String SPP_ID = "sppId";
	public static final String SPP_PUR_CODE = "purOrd.puoCode";
	public static final String SPP_PUR = "purOrd";
	public static final String SPP_SUP_ID = "supplier.supId";
	public static final String SPP_SUPPLIER_NAME = "supplier.supName";
	public static final String SPP_SUPPLIER = "supplier";
	public static final String SPP_CONTENT = "sppContent";
	public static final String SPP_PAY_TYPE = "sppPayType";
	public static final String SPP_FCT_DATE = "sppFctDate";
	public static final String SPP_PAY_MON = "sppPayMon";
	public static final String SPP_SE_NO = "sppSeNo";
	public static final String SPP_ISINV = "sppIsinv";
	public static final String SPP_CRE_USER = "sppCreUser";
	public static final String SPP_UPD_USER = "sppUpdUser";
	public static final String SPP_USER_NAME = "salEmp.seName";



	protected void initDao() {
		//do nothing
	}
    
	public void deleteById(String sppId) {
		Session session = super.getSession();
		Query query = session.createQuery("delete from SupPaidPast where "+SPP_ID+"="+sppId);
		query.executeUpdate();
	}

	public List<SupPaidPast> list(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize) {
		Session session = super.getSession();
		Query query = getPastSql(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage -1)*pageSize);
		query.setMaxResults(pageSize);
		List<SupPaidPast> list= query.list();
		return list;
	}

	public int listCount(String[] args) {
		Session session = super.getSession();
		Query query = getPastSql(session,args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	
	private Query getPastSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "past.";//表更名
		String sql = "from SupPaidPast past ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select past " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+SPP_SUP_ID+"="+args[0]+" ");
			}
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	/**
	 * 生成排序语句 <br>
	 * @param sql 原表连接sql
	 * @param tab 表名
	 * @param orderItem 排序字段
	 * @param isDe	是否逆序
	 * @return String[] 排序语句
	 */
	private String[] getSortSql(String sql,String tab,String orderItem,String isDe){
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "sppIsInv","sppMon","sppSup", "sppPurOrd", "sppPayType", "sppDate", "sppUser" };
			String[] cols = {  SPP_ISINV, SPP_PAY_MON, SPP_SUPPLIER_NAME, SPP_PUR_CODE,  SPP_PAY_TYPE, SPP_FCT_DATE, SPP_USER_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 2:
						joinSql += "left join " + tab + SPP_SUPPLIER + " ";
						break;
					case 3:
						joinSql += "left join " + tab + SPP_PUR + " ";
						break;
					}
					orderItem = cols[i];
					break;
				}
			}
			sortSql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortSql+="desc ";
			}
		} else {//默认排序
			sortSql = "order by "+tab+SPP_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
	
    public void save(SupPaidPast transientInstance) {
        log.debug("saving SupPaidPast instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(SupPaidPast persistentInstance) {
        log.debug("deleting SupPaidPast instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public SupPaidPast findById( java.lang.Long id) {
        log.debug("getting SupPaidPast instance with id: " + id);
        try {
            SupPaidPast instance = (SupPaidPast) getHibernateTemplate()
                    .get("com.psit.struts.entity.SupPaidPast", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(SupPaidPast instance) {
        log.debug("finding SupPaidPast instance by example");
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
      log.debug("finding SupPaidPast instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from SupPaidPast as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findBySppSupId(Object sppSupId
	) {
		return findByProperty(SPP_SUP_ID, sppSupId
		);
	}
	
	public List findBySppContent(Object sppContent
	) {
		return findByProperty(SPP_CONTENT, sppContent
		);
	}
	
	public List findBySppPayType(Object sppPayType
	) {
		return findByProperty(SPP_PAY_TYPE, sppPayType
		);
	}
	
	public List findBySppPayMon(Object sppPayMon
	) {
		return findByProperty(SPP_PAY_MON, sppPayMon
		);
	}
	
	public List findBySppSeNo(Object sppSeNo
	) {
		return findByProperty(SPP_SE_NO, sppSeNo
		);
	}
	
	public List findBySppIsinv(Object sppIsinv
	) {
		return findByProperty(SPP_ISINV, sppIsinv
		);
	}
	
	public List findBySppCreUser(Object sppCreUser
	) {
		return findByProperty(SPP_CRE_USER, sppCreUser
		);
	}
	
	public List findBySppUpdUser(Object sppUpdUser
	) {
		return findByProperty(SPP_UPD_USER, sppUpdUser
		);
	}
	

	public List findAll() {
		log.debug("finding all SupPaidPast instances");
		try {
			String queryString = "from SupPaidPast";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public SupPaidPast merge(SupPaidPast detachedInstance) {
        log.debug("merging SupPaidPast instance");
        try {
            SupPaidPast result = (SupPaidPast) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SupPaidPast instance) {
        log.debug("attaching dirty SupPaidPast instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(SupPaidPast instance) {
        log.debug("attaching clean SupPaidPast instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static SupPaidPastDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (SupPaidPastDAOImpl) ctx.getBean("SupPaidPastDAO");
	}


}