package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SupplierDAO;
import com.psit.struts.entity.Supplier;
import com.psit.struts.util.format.StringFormat;

public class SupplierDAOImpl extends HibernateDaoSupport implements SupplierDAO {
	private static final Log log = LogFactory.getLog(SupplierDAOImpl.class);
	// property constants
	public static final String SUP_ID = "supId";
	public static final String SUP_CODE = "supCode";
	public static final String SUP_TYPE = "supType";
	public static final String SUP_NAME = "supName";
	public static final String SUP_PHONE = "supPhone";
	public static final String SUP_MOB = "supMob";
	public static final String SUP_FEX = "supFex";
	public static final String SUP_EMAIL = "supEmail";
	public static final String SUP_NET = "supNet";
	public static final String SUP_ADD = "supAdd";
	public static final String SUP_ZIP_CODE = "supZipCode";
	public static final String SUP_PROD = "supProd";
	public static final String SUP_AREA1 = "supArea1";
	public static final String SUP_AREA2 = "supArea2";
	public static final String SUP_AREA3 = "supArea3";
	public static final String SUP_CONTACT_MAN = "supContactMan";
	public static final String SUP_BANK = "supBank";
	public static final String SUP_BANK_NAME = "supBankName";
	public static final String SUP_BANK_CODE = "supBankCode";

	protected void initDao() {
		// do nothing
	}
	
	public List<Supplier> list(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Query query = getHql(args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Supplier> list = query.list();
		return list;
	}
	public int listCount(String[]args) {
		Query query = getHql(args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getHql(String[]args, String orderItem, String isDe,
			boolean isCount) {
		Session session = (Session) super.getSession();
		StringBuffer appendHql = new StringBuffer();
		String tab = "sup.";//表更名
		String hql = "from Supplier sup ";
  		if(isCount){
  			hql = "select count(sup) " + hql;
  		}
  		else{
  			hql = "select sup " + hql;
  		}
		if(args != null){
			if(!StringFormat.isEmpty(args[0])){
		    	appendHql.append(appendHql.length()==0?"where ":"and ");
		    	appendHql.append(tab+SUP_CODE +" like :code ");
			}
		    if(!StringFormat.isEmpty(args[1])){
		    	appendHql.append(appendHql.length()==0?"where ":"and ");
		    	appendHql.append(tab+SUP_NAME+" like :supName ");
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
			String[] items = { "name", "code", "type","contactMan", "phone", "mob", "fex", "email", "add", "prod" };
			String[] cols = { SUP_NAME,SUP_CODE,SUP_TYPE+"."+TypeListDAOImpl.TYP_NAME,SUP_CONTACT_MAN,SUP_PHONE,SUP_MOB,SUP_FEX,SUP_EMAIL,
					SUP_ADD,SUP_PROD };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch(i){
					case 2:
						joinHql += "left join "+tab+SUP_TYPE+" ";
					}
					orderItem = cols[i];
				}
			}
			sortHql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortHql += "desc ";
			}
		} else {//默认排序
			sortHql = "order by "+tab+SUP_ID+" desc ";
		}
		return new String[]{joinHql,sortHql};
	}
	
	public void deleteById(String supId) {
		Session session = (Session) super.getSession();
		Query query = session.createQuery("delete from Supplier where "+SUP_ID+"="+supId);
		query.executeUpdate();
	}
	
	public void save(Supplier transientInstance) {
		log.debug("saving Supplier instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Supplier persistentInstance) {
		log.debug("deleting Supplier instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Supplier findById(java.lang.Long id) {
		log.debug("getting Supplier instance with id: " + id);
		try {
			Supplier instance = (Supplier) getHibernateTemplate().get(
					"com.psit.struts.entity.Supplier", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Supplier instance) {
		log.debug("finding Supplier instance by example");
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
		log.debug("finding Supplier instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Supplier as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySupCode(Object supCode) {
		return findByProperty(SUP_CODE, supCode);
	}

	public List findBySupName(Object supName) {
		return findByProperty(SUP_NAME, supName);
	}

	public List findBySupPhone(Object supPhone) {
		return findByProperty(SUP_PHONE, supPhone);
	}

	public List findBySupMob(Object supMob) {
		return findByProperty(SUP_MOB, supMob);
	}

	public List findBySupFex(Object supFex) {
		return findByProperty(SUP_FEX, supFex);
	}

	public List findBySupEmail(Object supEmail) {
		return findByProperty(SUP_EMAIL, supEmail);
	}

	public List findBySupNet(Object supNet) {
		return findByProperty(SUP_NET, supNet);
	}

	public List findBySupAdd(Object supAdd) {
		return findByProperty(SUP_ADD, supAdd);
	}

	public List findBySupZipCode(Object supZipCode) {
		return findByProperty(SUP_ZIP_CODE, supZipCode);
	}

	public List findBySupProd(Object supProd) {
		return findByProperty(SUP_PROD, supProd);
	}

	public List findBySupArea1(Object supArea1) {
		return findByProperty(SUP_AREA1, supArea1);
	}

	public List findBySupArea2(Object supArea2) {
		return findByProperty(SUP_AREA2, supArea2);
	}

	public List findBySupArea3(Object supArea3) {
		return findByProperty(SUP_AREA3, supArea3);
	}

	public List findBySupBank(Object supBank) {
		return findByProperty(SUP_BANK, supBank);
	}

	public List findBySupBankName(Object supBankName) {
		return findByProperty(SUP_BANK_NAME, supBankName);
	}

	public List findBySupBankCode(Object supBankCode) {
		return findByProperty(SUP_BANK_CODE, supBankCode);
	}

	public List findAll() {
		log.debug("finding all Supplier instances");
		try {
			String queryString = "from Supplier";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Supplier merge(Supplier detachedInstance) {
		log.debug("merging Supplier instance");
		try {
			Supplier result = (Supplier) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Supplier instance) {
		log.debug("attaching dirty Supplier instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Supplier instance) {
		log.debug("attaching clean Supplier instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SupplierDAOImpl getFromApplicationContext(ApplicationContext ctx) {
		return (SupplierDAOImpl) ctx.getBean("SupplierDAO");
	}
}