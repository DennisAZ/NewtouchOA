package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.PurOrdDAO;
import com.psit.struts.entity.ERPPurchase;
import com.psit.struts.entity.PurOrd;
import com.psit.struts.util.format.StringFormat;

public class PurOrdDAOImpl extends HibernateDaoSupport implements PurOrdDAO {
	private static final Log log = LogFactory.getLog(PurOrdDAOImpl.class);
	// property constants
	public static final String PUO_ID = "puoId";
	public static final String PUO_CODE = "puoCode";
	public static final String PUO_SUP = "puoSup";
	public static final String PUO_TYPE = "puoType";
	public static final String PUO_M = "puoM";
	public static final String PUO_PAID_M = "puoPaidM";
	public static final String PUO_DATE = "puoPurDate";
	public static final String PUO_EMP = "puoEmp";
	public static final String PUO_CONTENT = "puoContent";
	public static final String PUO_IS_END = "puoIsEnd";

	protected void initDao() {
		// do nothing
	}
	public List<PurOrd> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Query query = getHql(args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<PurOrd> list = query.list();
		return list;
	}
	public List<ERPPurchase> listERP(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Query query = getHqlERP(args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ERPPurchase> list = query.list();
		return list;
	}
	public int listCount(String[]args) {
		Query query = getHql(args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	public int listCountERP(String[]args) {
		Query query = getHqlERP(args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getHqlERP(String[]args, String orderItem, String isDe,
			boolean isCount) {
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "puo.";
		String hql = "from ERPPurchase puo ";
  		if(isCount){
  			hql = "select count(puo) " + hql;
  		}
  		else{
  			hql = "select puo " + hql;
  		}
		if(args != null){
			if(!StringFormat.isEmpty(args[2])){
		    	appendHql.append(appendHql.length()==0?"where ":"and ");
		    	appendHql.append(tab+"yhperson.seqId=" +args[2]);
			}
//		    if(!StringFormat.isEmpty(args[1])){
//		    	appendHql.append(appendHql.length()==0?"where ":"and ");
//		    	appendHql.append(tab+PUO_SUP+"."+SupplierDAOImpl.SUP_NAME+" like :supName ");
//		    }
		}
//		if (!isCount) {
//			String[] sortHqls = getSortHql(hql,tab,orderItem,isDe);
//			hql+=sortHqls[0];
//			appendHql.append(sortHqls[1]);
//		}
		hql+=appendHql.toString();
		Query query = session.createQuery(hql);
////		System.out.println(hql);
//		if(args!=null){
//			if(!StringFormat.isEmpty(args[0])){
//				query.setString("code", "%"+args[0]+"%");
//			}
//			if(!StringFormat.isEmpty(args[1])){
//				query.setString("supName","%"+args[1]+"%");
//			}
//		}
		return query;
	}
	private Query getHql(String[]args, String orderItem, String isDe,
			boolean isCount) {
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "puo.";
		String hql = "from PurOrd puo ";
  		if(isCount){
  			hql = "select count(puo) " + hql;
  		}
  		else{
  			hql = "select puo " + hql;
  		}
		if(args != null){
			if(!StringFormat.isEmpty(args[0])){
		    	appendHql.append(appendHql.length()==0?"where ":"and ");
		    	appendHql.append(tab+PUO_CODE +" like :code ");
			}
		    if(!StringFormat.isEmpty(args[1])){
		    	appendHql.append(appendHql.length()==0?"where ":"and ");
		    	appendHql.append(tab+PUO_SUP+"."+SupplierDAOImpl.SUP_NAME+" like :supName ");
		    }
		}
		if (!isCount) {
			String[] sortHqls = getSortHql(hql,tab,orderItem,isDe);
			hql+=sortHqls[0];
			appendHql.append(sortHqls[1]);
		}
		hql+=appendHql.toString();
		Query query = session.createQuery(hql);
//		System.out.println(hql);
		if(args!=null){
			if(!StringFormat.isEmpty(args[0])){
				query.setString("code", "%"+args[0]+"%");
			}
			if(!StringFormat.isEmpty(args[1])){
				query.setString("supName","%"+args[1]+"%");
			}
		}
		return query;
	}
	
	private String[] getSortHql(String hql,String tab,String orderItem,String isDe){
		String joinHql = "";
		String sortHql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "code", "type", "supName", "m", "paidM", "date", "emp" };
			String[] cols = { PUO_CODE,PUO_TYPE+"."+TypeListDAOImpl.TYP_NAME,PUO_SUP+"."+SupplierDAOImpl.SUP_NAME,
					PUO_M,PUO_PAID_M,PUO_DATE, PUO_EMP+"."+SalEmpDAOImpl.SE_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch(i){
						case 1:
							joinHql += "left join " + tab + PUO_TYPE + " "; 
							break;
						case 2:
							joinHql += "left join " + tab + PUO_SUP + " "; 
							break;
						case 6:
							joinHql += "left join " + tab + PUO_EMP + " "; 
							break;
					}
					orderItem = cols[i];
				}
			}
			sortHql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortHql += "desc ";
			}
		} else {//Ĭ������
			sortHql = "order by "+tab+PUO_ID+" desc ";
		}
		return new String[]{joinHql,sortHql};
	}
	
	public void deleteById(String puoId) {
		Session session = (Session) super.getSession();
		Query query = session.createQuery("delete from PurOrd where "+PUO_ID+"="+puoId);
		query.executeUpdate();
	}
	
	public void save(PurOrd transientInstance) {
		log.debug("saving PurOrd instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PurOrd persistentInstance) {
		log.debug("deleting PurOrd instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PurOrd findById(java.lang.Long id) {
		log.debug("getting PurOrd instance with id: " + id);
		try {
			PurOrd instance = (PurOrd) getHibernateTemplate().get(
					"com.psit.struts.entity.PurOrd", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PurOrd instance) {
		log.debug("finding PurOrd instance by example");
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
		log.debug("finding PurOrd instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PurOrd as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPuoCode(Object puoCode) {
		return findByProperty(PUO_CODE, puoCode);
	}

	public List findByPuoM(Object puoM) {
		return findByProperty(PUO_M, puoM);
	}

	public List findByPuoPaidM(Object puoPaidM) {
		return findByProperty(PUO_PAID_M, puoPaidM);
	}

	public List findByPuoContent(Object puoContent) {
		return findByProperty(PUO_CONTENT, puoContent);
	}

	public List findByPuoIsEnd(Object puoIsEnd) {
		return findByProperty(PUO_IS_END, puoIsEnd);
	}

	public List findAll() {
		log.debug("finding all PurOrd instances");
		try {
			String queryString = "from PurOrd";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PurOrd merge(PurOrd detachedInstance) {
		log.debug("merging PurOrd instance");
		try {
			PurOrd result = (PurOrd) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PurOrd instance) {
		log.debug("attaching dirty PurOrd instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PurOrd instance) {
		log.debug("attaching clean PurOrd instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PurOrdDAOImpl getFromApplicationContext(ApplicationContext ctx) {
		return (PurOrdDAOImpl) ctx.getBean("PurOrdDAO");
	}

}