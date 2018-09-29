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

import com.psit.struts.DAO.SupInvoiceDAO;
import com.psit.struts.entity.SupInvoice;

/**
 	* A data access object (DAO) providing persistence and search support for SupInvoice entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .SupInvoice
  * @author MyEclipse Persistence Tools 
 */

public class SupInvoiceDAOImpl extends HibernateDaoSupport implements SupInvoiceDAO  {
    private static final Log log = LogFactory.getLog(SupInvoiceDAOImpl.class);
	//property constants
    public static final String SUI_ID = "suiId";
	public static final String SUI_ORD_TIL = "purOrd.puoCode";
	public static final String SUI_SUP_ID = "supplier.supId";
	public static final String SUI_SUP_NAME = "supplier.supName";
	public static final String SUI_MON = "suiMon";
	public static final String SUI_TYPE_NAME = "typeList.typName";
	public static final String SUI_DATE = "suiDate";
	public static final String SUI_SE_NAME = "salEmp.seName";
	public static final String SUI_CODE = "suiCode";
	public static final String SUI_REMARK = "suiRemark";
	public static final String SUI_CRE_USER = "suiCreUser";
	public static final String SUI_UPD_USER = "suiUpdUser";
	public static final String SUI_SUP = "supplier";
	public static final String SUI_PUR_ORD = "purOrd";
	public static final String SUI_TYPE = "typeList";
	public static final String SUI_SE = "salEmp";


	protected void initDao() {
		//do nothing
	}
    
	public void deleteById(String suiId) {
		Session session = (Session)super.getSession();
		Query query = session.createQuery("delete from SupInvoice where "+SUI_ID+"="+suiId);
		query.executeUpdate();
	}

	public List<SupInvoice> list(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize) {
		Session session = (Session)super.getSession();
		Query query = getQuery(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage -1)*pageSize);
		query.setMaxResults(pageSize);
		List<SupInvoice> list = query.list();
		return list;
	}

	public int listCount(String[] args) {
		Session session = (Session)super.getSession();
		Query query = getQuery(session,args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getQuery(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "si.";//表更名
		String sql = "from SupInvoice si ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select si " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SUI_SUP_ID+"="+args[0]+" ");
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
			String[] items = { "suiCode","suiSup","suiPurOrd","suiMon","typName","suiDate","seName" };
			String[] cols = {  SUI_CODE,SUI_SUP_NAME, SUI_ORD_TIL, SUI_MON, SUI_TYPE_NAME, SUI_DATE,SUI_SE_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 1:
						joinSql += "left join " + tab + SUI_SUP + " ";
						break;
					case 2:
						joinSql += "left join " + tab + SUI_PUR_ORD + " ";
						break;
					case 4:
						joinSql += "left join " + tab + SUI_TYPE + " ";
						break;
					case 6:
						joinSql += "left join " + tab + SUI_SE + " ";
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
			sortSql = "order by "+tab+SUI_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
    public void save(SupInvoice transientInstance) {
        log.debug("saving SupInvoice instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(SupInvoice persistentInstance) {
        log.debug("deleting SupInvoice instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public SupInvoice findById( java.lang.Long id) {
        log.debug("getting SupInvoice instance with id: " + id);
        try {
            SupInvoice instance = (SupInvoice) getHibernateTemplate()
                    .get("com.psit.struts.entity.SupInvoice", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(SupInvoice instance) {
        log.debug("finding SupInvoice instance by example");
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
      log.debug("finding SupInvoice instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from SupInvoice as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}


	public List findBySuiSupId(Object suiSupId
	) {
		return findByProperty(SUI_SUP_ID, suiSupId
		);
	}
	
	public List findBySuiMon(Object suiMon
	) {
		return findByProperty(SUI_MON, suiMon
		);
	}
	
	public List findBySuiType(Object suiType
	) {
		return findByProperty(SUI_TYPE, suiType
		);
	}
	
	public List findBySuiRemark(Object suiRemark
	) {
		return findByProperty(SUI_REMARK, suiRemark
		);
	}
	
	public List findBySuiCreUser(Object suiCreUser
	) {
		return findByProperty(SUI_CRE_USER, suiCreUser
		);
	}
	
	public List findBySuiUpdUser(Object suiUpdUser
	) {
		return findByProperty(SUI_UPD_USER, suiUpdUser
		);
	}
	

	public List findAll() {
		log.debug("finding all SupInvoice instances");
		try {
			String queryString = "from SupInvoice";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public SupInvoice merge(SupInvoice detachedInstance) {
        log.debug("merging SupInvoice instance");
        try {
            SupInvoice result = (SupInvoice) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(SupInvoice instance) {
        log.debug("attaching dirty SupInvoice instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(SupInvoice instance) {
        log.debug("attaching clean SupInvoice instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static SupInvoiceDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (SupInvoiceDAOImpl) ctx.getBean("SupInvoiceDAO");
	}


}