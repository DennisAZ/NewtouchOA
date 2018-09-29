package com.psit.struts.DAO.impl;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RPuoPro;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.DAO.RPuoProDAO;


public class RPuoProDAOImpl extends HibernateDaoSupport implements RPuoProDAO {
    private static final Log log = LogFactory.getLog(RPuoProDAOImpl.class);
	//property constants
    public static final String RPP_ID = "rppId";
	public static final String RPP_PURORD = "purOrd";
	public static final String RPP_PURORD_ID = "purOrd.puoId";
	public static final String RPP_WMS_PRODUCT = "wmsProduct";
	public static final String ROP_WPR_ID = "wmsProduct.wprId";
	public static final String ROP_WRP_NAME= "wmsProduct.wprName";
	public static final String RPP_NUM = "rppNum";
	public static final String RPP_PRICE = "rppPrice";
	public static final String RPP_SUM_MON = "rppSumMon";
	public static final String RPP_REMARK = "rppRemark";
	public static final String RPP_OUT_NUM = "rppOutNum";
	public static final String RPP_REAL_NUM = "rppRealNum";



	protected void initDao() {
		//do nothing
	}
	
	
	public int listCount(String []args){
		Query query = getHql(args,null,null,true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
    
	public List<RPuoPro> list(String[] args, String orderItem,String isDe,
			int currentPage, int pageSize ){
		Query query = getHql(args,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1)*pageSize);
		query.setMaxResults(pageSize);
		List<RPuoPro> list = query.list();
		return list;
	}
	
	private Query getHql(String []args,String orderItem,String isDe,boolean isCount){
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "rpuopro.";//表更名
		String hql = "from RPuoPro rpuopro ";
		if(isCount){
			hql ="select count(rpuopro) " + hql;
		}else{
			hql ="select rpuopro " + hql;
		}
		if(args !=null){
			if(!StringFormat.isEmpty(args[0])){
				appendHql.append(appendHql.length()==0?"where ":"and ");
				appendHql.append(tab+ROP_WPR_ID+"="+args[0]+" ");
			}
		}
		if(!isCount){
			String[] sortHqls = getSortHql(hql,tab,orderItem,isDe);
			hql += sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql += appendHql.toString();
		Query query = session.createQuery(hql);
		return query;
	}
	
	private String[] getSortHql(String hql,String tab,String orderItem,String isDe){
		String joinHql = "";
		String sortHql ="";
		if(orderItem !=null && !orderItem.equals("")){
			String[] items = {"purOrd","rppNum","rppPrice","rppSumMon","rppOutNum","rppRealNum","rppRemark"};
			String[] cols = {RPP_PURORD+"."+PurOrdDAOImpl.PUO_CODE,RPP_NUM,RPP_PRICE,RPP_SUM_MON,RPP_OUT_NUM,RPP_REAL_NUM,RPP_REMARK};
			for(int i=0;i<items.length;i++){
				if(orderItem.equals(items[i])){
					switch(i){
					case 0:
						joinHql += "left join " + tab + RPP_PURORD + " ";
						break;
					}
					orderItem = cols[i];
				}
			}
			sortHql = "order by " + tab + orderItem + " ";
			if (isDe != null && isDe.equals("1")) {
				sortHql += "desc ";
			}
		}else{
			sortHql = "order by " + tab + RPP_ID+" desc ";
		}
		return new String[]{joinHql,sortHql};
	} 
	
	/**
	 * 根据采购单号查询采购明细 <br>
	 * @param puoId 采购id 
	 * @return List 返回采购明细列表
	 */
	public List<RPuoPro> findByPurOrd(String puoId) {
		long id=Long.parseLong(puoId);
		log.debug("finding all RPuoPro instances");
		try {
			String queryString = "from RPuoPro where "+RPP_PURORD_ID+"="+puoId+" order by rppId desc";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	
	public void batSave(List<RPuoPro> saveList,String puoId){
		Session session = (Session) super.getSession();
		String delSql = "delete RPuoPro where "+RPP_PURORD_ID+"="+puoId;
		session.createQuery(delSql).executeUpdate();
		Iterator<RPuoPro> saveIt = saveList.iterator();
		while(saveIt.hasNext()){
			getHibernateTemplate().save(saveIt.next());
		}
	}
	
	
	/**
	 * 采购单产品明细列表<br>
	 * @param puoId 采购ID
	 * @return List<RPuoPro> 采购单产品明细列表
	 */
	public List<RPuoPro> listPuoPro(String puoId){
		Session session = (Session) super.getSession();
		Query query = getOrdProSql(session, puoId);
		List<RPuoPro> list = query.list();
		return list;
	}
	private Query getOrdProSql(Session session, String puoId) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "rpp.";//表更名
		String sql = "from RPuoPro rpp ";
		if(puoId!=null && !puoId.equals("")){
	    	appendSql.append("where ");
	    	appendSql.append(tab+RPP_PURORD_ID+" = "+puoId+" ");
	    }
		Query query = session.createQuery(sql+appendSql.toString());
		return query;
	}
	
    public void save(RPuoPro transientInstance) {
        log.debug("saving RPuoProDAOImpl instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(RPuoPro persistentInstance) {
        log.debug("deleting RPuoProDAOImpl instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public RPuoPro findById( java.lang.Long id) {
        log.debug("getting RPuoProDAOImpl instance with id: " + id);
        try {
            RPuoPro instance = (RPuoPro) getHibernateTemplate()
                    .get("RPuoProDAOImpl", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(RPuoPro instance) {
        log.debug("finding RPuoProDAOImpl instance by example");
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
      log.debug("finding RPuoProDAOImpl instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from RPuoProDAOImpl as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
	
	public List findByRppNum(Object rppNum
	) {
		return findByProperty(RPP_NUM, rppNum
		);
	}
	
	public List findByRppPrice(Object rppPrice
	) {
		return findByProperty(RPP_PRICE, rppPrice
		);
	}
	
	public List findByRppSumMon(Object rppSumMon
	) {
		return findByProperty(RPP_SUM_MON, rppSumMon
		);
	}
	
	public List findByRppRemark(Object rppRemark
	) {
		return findByProperty(RPP_REMARK, rppRemark
		);
	}
	
	public List findByRppOutNum(Object rppOutNum
	) {
		return findByProperty(RPP_OUT_NUM, rppOutNum
		);
	}
	
	public List findByRppRealNum(Object rppRealNum
	) {
		return findByProperty(RPP_REAL_NUM, rppRealNum
		);
	}
	

	public List findAll() {
		log.debug("finding all RPuoProDAOImpl instances");
		try {
			String queryString = "from RPuoProDAOImpl";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public RPuoPro merge(RPuoPro detachedInstance) {
        log.debug("merging RPuoProDAOImpl instance");
        try {
            RPuoPro result = (RPuoPro) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(RPuoPro instance) {
        log.debug("attaching dirty RPuoProDAOImpl instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(RPuoPro instance) {
        log.debug("attaching clean RPuoProDAOImpl instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RPuoProDAOImpl getFromApplicationContext(ApplicationContext ctx) {
    	return (RPuoProDAOImpl) ctx.getBean("RPuoProDAO");
	}
}