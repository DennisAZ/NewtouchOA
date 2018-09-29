package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ProdTransDAO;
import com.psit.struts.entity.ProdTrans;
import com.psit.struts.util.format.StringFormat;

public class ProdTransDAOImpl extends HibernateDaoSupport implements ProdTransDAO {
	private static final Log log = LogFactory.getLog(ProdTransDAOImpl.class);
	// property constants
	public static final String PTR_ID = "ptrId";
	public static final String PTR_UNIT = "ptrUnit";
	public static final String PTR_AMT = "ptrAmt";
	public static final String PTR_PROD_ID = "ptrProduct.wprId";
	public static final String PTR_EXP = "ptrExpTypeList";
	public static final String PTR_EXP_ID = "ptrExpTypeList.typId";
	public static final String PTR_EXP_NAME = "ptrExpTypeList.typName";
	public static final String PTR_PROV = "ptrProv";
	public static final String PTR_PROV_ID = "ptrProv.areId";
	public static final String PTR_PROV_NAME = "ptrProv.areName";
	public static final String PTR_CITY = "ptrCity";
	public static final String PTR_CITY_ID = "ptrCity.prvId";
	public static final String PTR_CITY_NAME = "ptrCity.prvName";
	public static final String PTR_DISTRICT = "ptrDistrict";
	public static final String PTR_DISTRICT_ID = "ptrDistrict.cityId";
	public static final String PTR_DISTRICT_NAME = "ptrDistrict.cityName";
	
	public void save(ProdTrans transientInstance) {
		log.debug("saving ProdTrans instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void update(ProdTrans transientInstance) {
		super.getHibernateTemplate().merge(transientInstance);
	}
	public void deleteById(String id){
		Session session = (Session) super.getSession();
		String queryString = "delete ProdTrans where ptrId="+id;
		session.createQuery(queryString).executeUpdate();
	}
	public void delete(ProdTrans persistentInstance) {
		log.debug("deleting ProdTrans instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public ProdTrans findById(Long id) {
		log.debug("getting ProdTrans instance with id: " + id);
		try {
			ProdTrans instance = (ProdTrans) getHibernateTemplate().get(
					"com.psit.struts.entity.ProdTrans", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List<ProdTrans> listByProdId(String prodId,String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getSql(session, prodId, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdTrans> list = query.list();
		return list;
	}
	
	public int listByProdIdCount(String prodId){
		Session session = (Session) super.getSession();
		Query query = getSql(session, prodId, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getSql(Session session, String prodId, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "pTrans.";//表更名
		String sql = "from ProdTrans pTrans ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select pTrans "+sql;
  		}
		if(!StringFormat.isEmpty(prodId)){
			appendSql.append("where ");
	    	appendSql.append(tab+PTR_PROD_ID+" = "+prodId+" ");
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
	private String[] getSortSql(String sql,String tab,String orderItem,String isDe){
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = {"pExp", "pUnit", "pProv","pCity","pDistrict","pAmt"};
			String[] cols = { PTR_EXP_NAME,PTR_UNIT, PTR_PROV_NAME,PTR_CITY_NAME ,PTR_DISTRICT_NAME, PTR_AMT };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch(i){
					case 0:
						joinSql +="left join "+tab+PTR_EXP+" ";
						break;
					case 2:
						joinSql +="left join "+tab+PTR_PROV+" ";
						break;
					case 3:
						joinSql +="left join "+tab+PTR_CITY+" ";
						break;
					case 4:
						joinSql +="left join "+tab+PTR_DISTRICT+" ";
						break;
					}
					orderItem = cols[i];
					break;
				}
			}
			sortSql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortSql += "desc ";
			}
		} else {//默认排序
			sortSql = "order by "+tab+PTR_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
	public List<ProdTrans> listByProdIds(String prodIds, String expId, String provId, String cityId, String districtId){
		Session session = (Session) super.getSession();
		StringBuilder sqlSb = new StringBuilder("select new com.psit.struts.form.ProdTransForm("+PTR_UNIT+","+PTR_PROD_ID+","+PTR_AMT+") from ProdTrans where " +
				PTR_PROD_ID + " in ("+prodIds+") and "+PTR_EXP_ID+"="+expId+" ");
		if(!StringFormat.isEmpty(provId)){
			sqlSb.append("and "+PTR_PROV_ID+"="+provId+" ");
		}
		if(!StringFormat.isEmpty(cityId)){
			sqlSb.append("and "+PTR_CITY_ID+"="+cityId+" ");
		}
		if(!StringFormat.isEmpty(districtId)){
			sqlSb.append("and "+PTR_DISTRICT_ID+"="+districtId+" ");
		}
		Query query = session.createQuery(sqlSb.toString());
		List<ProdTrans> list = query.list();
		return list;
	}
}