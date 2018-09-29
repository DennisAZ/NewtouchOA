package com.psit.struts.DAO.impl;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ROrdProDAO;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.SalOrdCon;

/**
 * 订单明细DAO实现类 <br>
 */
public class ROrdProDAOImpl extends HibernateDaoSupport implements ROrdProDAO {
	private static final Log log = LogFactory.getLog(ROrdProDAOImpl.class);
	// property constants
	public static final String ROP_ID ="ropId";
	public static final String ROP_NUM = "ropNum";
	public static final String ROP_REAL_PRICE = "ropRealPrice";
	public static final String ROP_PRICE = "ropPrice";
	public static final String ROP_REMARK = "ropRemark";
	public static final String ROP_RETURN_NUM = "ropReturnNum";
	public static final String ROP_ORD = "salOrdCon";
	public static final String ROP_ORD_ID = "salOrdCon.sodCode";
	public static final String ROP_ORD_TIL = "salOrdCon.sodTil";
	public static final String ROP_ORD_CON_DATE = "salOrdCon.sodConDate";
	public static final String ROP_WPR = "wmsProduct";
	public static final String ROP_WPR_ID = "wmsProduct.wprId";
	public static final String ROP_WRP_NAME= "wmsProduct.wprName";
	
	public static final String ORD_ID = "sodCode";
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 保存订单明细 <br>
	 * @param transientInstance 订单明细实体
	 */
	public void save(ROrdPro transientInstance) {
		log.debug("saving ROrdPro instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void batSave(List<ROrdPro> saveList,String sodId){
		Session session = (Session) super.getSession();
		String delSql = "delete ROrdPro where "+ROP_ORD_ID+"="+sodId;
		session.createQuery(delSql).executeUpdate();
		Iterator<ROrdPro> saveIt = saveList.iterator();
		while(saveIt.hasNext()){
			getHibernateTemplate().save(saveIt.next());
		}
	}
	
	/**
	 * 删除订单明细 <br>
	 * @param persistentInstance 订单明细实体
	 */
	public void delete(ROrdPro persistentInstance) {
		log.debug("deleting ROrdPro instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * 订单产品明细列表<br>
	 * @param ordId 订单ID
	 * @return List<ROrdPro> 订单产品明细列表
	 */
	public List<ROrdPro> listOrdPro(String ordId){
		Session session = (Session) super.getSession();
		Query query = getOrdProSql(session, ordId);
		List<ROrdPro> list = query.list();
		return list;
	}
	private Query getOrdProSql(Session session, String ordId) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "rop.";//表更名
		String sql = "from ROrdPro rop ";
		if(ordId!=null && !ordId.equals("")){
	    	appendSql.append("where ");
	    	appendSql.append(tab+ROP_ORD_ID+" = "+ordId+" ");
	    }
		Query query = session.createQuery(sql+appendSql.toString());
		return query;
	}
	
	/**
	 * 根据订单号查询订单明细 <br>
	 * @param sodCode 订单id 
	 * @return List 返回订单明细列表
	 */
	public List<ROrdPro> findByOrd(String sodCode) {
		long id=Long.parseLong(sodCode);
		log.debug("finding all ROrdPro instances");
		try {
			String queryString = "from ROrdPro where salOrdCon.sodCode="+id+" order by ropId desc";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 根据订单id和商品id查询订单明细 <br>
	 * @param sodCode 订单id
	 * @param wprCode 产品id
	 * @return List 返回订单明细列表
	 */
	public List<ROrdPro> findByWpr(String sodCode,Long wprCode) {
		long id=Long.parseLong(sodCode);
		log.debug("finding all ROrdPro instances");
		try {
			String queryString = "from ROrdPro where salOrdCon.sodCode="+id+" and wmsProduct.wprId="+wprCode+" order by ropId desc";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 根据订单明细Id查询订单明细 <br>
	 * @param ropId 订单明细Id
	 * @return ROrdPro 返回订单明细实体
	 */
	public ROrdPro getRop(Long ropId){
		return (ROrdPro) super.getHibernateTemplate().get(ROrdPro.class, ropId);
	}
	/**
	 * 更新订单明细 <br>
	 * @param rordPro 订单明细实体
	 */
	public void updateRop(ROrdPro rordPro){
		super.getHibernateTemplate().update(rordPro);
	}
	/**
	 * 根据订单id查询订单明细仓储信息 <br>
	 * @param sodCode 订单id
	 * @return List 返回订单明细仓储列表
	 */
	public List findByStro(String sodCode) {
		long id=Long.parseLong(sodCode);
		log.debug("finding all RStroPro instances");
		try {
			String queryString = "from RStroPro as rsp where rsp.wmsProduct.wprCode in (select rop.wmsProduct.wprCode from ROrdPro as rop where rop.salOrdCon.sodCode="+id+") order by rsp.wmsProduct.wprCode";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 根据商品id查询订单 <br>
	 * @param prodId 商品ID
	 * @return List<SalOrdCon> 订单ID集合
	 */
	public List<SalOrdCon> getOrdersByProd(String prodId) {
		try {
			String queryString = "from SalOrdCon where "+ORD_ID+" in (select "
					+ ROP_ORD_ID + " from ROrdPro where " + ROP_WPR_ID + "="
					+ prodId + ")";
			return getHibernateTemplate().find(queryString);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据商品id查询订单明细 <br>
	 * @param wprCode 商品id
	 * @return List 返回订单明细列表
	 */
	public List findByWpr(Long wprCode) {
		try {
			String queryString = "from ROrdPro where wmsProduct.wprId="+wprCode;
			return getHibernateTemplate().find(queryString);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 查看某商品销售历史列表数量 <br>
	 * @param wprCode 商品Id
	 * @return int 返回销售历史列表数量
	 */
	public int getCountByWpr(Long wprCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	    String queryString="select count(*) from ROrdPro where salOrdCon.sodIsfail='0' and salOrdCon.sodAppIsok='1' and  wmsProduct.wprId="+wprCode;
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 根据商品id查询订单明细 <br>
	 * @param wprCode 商品id
	 * @return List 返回订单明细列表
	 */
	public List<ROrdPro> findByWpr(Long wprCode,int currentPage,int pageSize){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		String queryString = "from ROrdPro where salOrdCon.sodIsfail='0' and salOrdCon.sodAppIsok='1' and  wmsProduct.wprId="+wprCode+"order by salOrdCon.sodConDate desc";
		query=session.createQuery(queryString);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List<ROrdPro> list=(List)query.list();
		return list;
	}
	
	/**
	 * 通过客户Id查找订单明细<br>
	 * @param cusId 客户Id
	 * @return List<ROrdPro> 订单明细列表<br>
	 */
	public List<ROrdPro> findByCusId(String cusId, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		String sql = getByCusId(cusId,orderItem,isDe,false);
		Query query = session.createQuery(sql);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ROrdPro> list = query.list();
		return list;
	}
	
	/**
	 *  通过客户Id查找订单明细的数量<br>
	 * @param cusId 客户Id
	 * @return 订单明细的数量
	 */
	public int findByCusIdCount(String cusId){
		Session session = (Session) super.getSession();
		String sql = getByCusId(cusId, null, null, true);
		Query query = session.createQuery(sql);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private String getByCusId(String cusId, String orderItem,String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "rod.";
		String sql = "from ROrdPro rod ";
		if(isCount){
			sql ="select count(*) "+sql;
		}
		else{
			sql = "select rod "+sql;
		}
		if(cusId!=null && !cusId.equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+ROP_ORD_ID+" in (select sal.sodCode from SalOrdCon as sal where sal.cusCorCus.corCode="+cusId+") ");
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"order","name","price","num","tolPrice","conDate","remark"};
				String [] cols = {ROP_ORD_TIL,ROP_WRP_NAME,ROP_PRICE, ROP_NUM,ROP_REAL_PRICE,ROP_ORD_CON_DATE,ROP_REMARK};
				for(int i=0;i<items.length;i++){
					if(items[i].equals(orderItem)){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append(" order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append(" desc ");
				}
			}
			else{
				appendSql.append(" order by "+tab+ ROP_ORD_CON_DATE+" desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	
}