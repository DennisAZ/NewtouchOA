package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsChangeDAO;
import com.psit.struts.entity.WmsChange;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.util.OperateDate;
/**
 * 
 * 库存调拨DAO实现类 <br>
 *
 * create_date: Aug 18, 2010,3:18:08 PM<br>
 * @author zjr
 */
public class WmsChangeDAOImpl extends HibernateDaoSupport implements WmsChangeDAO{
	private static final Log log = LogFactory.getLog(WmsChangeDAOImpl.class);
	// property constants
	public static final String WCH_TITLE = "wchTitle";
	public static final String WCH_STATE = "wchState";
	public static final String WCH_REMARK = "wchRemark";
	java.util.Date starTime=new Date();  
	java.util.Date  endTime=OperateDate.getDate(starTime, 1);
	//当天日期
	java.sql.Date starTime1=new java.sql.Date(starTime.getTime());
	java.sql.Date endTime1=new java.sql.Date(endTime.getTime());
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * 保存仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:23:16 PM <br>
	 * @param transientInstance 仓库调拨实体
	 */
	public void save(WmsChange transientInstance) {
		log.debug("saving WmsChange instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 删除仓库调拨单 <br>
	 * create_date: Aug 11, 2010,5:56:11 PM <br>
	 * @param persistentInstance 仓库调拨实体
	 */
	public void delete(WmsChange persistentInstance) {
		log.debug("deleting WmsChange instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

//	public WmsChange findById(java.lang.Long id) {
//		log.debug("getting WmsChange instance with id: " + id);
//		try {
//			WmsChange instance = (WmsChange) getHibernateTemplate().get(
//					"com.psit.struts.DAO.impl.WmsChange", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(WmsChange instance) {
//		log.debug("finding WmsChange instance by example");
//		try {
//			List results = getHibernateTemplate().findByExample(instance);
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
//		} catch (RuntimeException re) {
//			log.error("find by example failed", re);
//			throw re;
//		}
//	}
//
//	public List findByProperty(String propertyName, Object value) {
//		log.debug("finding WmsChange instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsChange as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByWchTitle(Object wchTitle) {
//		return findByProperty(WCH_TITLE, wchTitle);
//	}
//
//	public List findByWchState(Object wchState) {
//		return findByProperty(WCH_STATE, wchState);
//	}
//
//	public List findByWchRemark(Object wchRemark) {
//		return findByProperty(WCH_REMARK, wchRemark);
//	}
//
//	public List findAll() {
//		log.debug("finding all WmsChange instances");
//		try {
//			String queryString = "from WmsChange";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public WmsChange merge(WmsChange detachedInstance) {
//		log.debug("merging WmsChange instance");
//		try {
//			WmsChange result = (WmsChange) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsChange instance) {
//		log.debug("attaching dirty WmsChange instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsChange instance) {
//		log.debug("attaching clean WmsChange instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsChangeDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsChangeDAOImpl) ctx.getBean("WmsChangeDAO");
//	}
	/**
	 * 
	 * 仓库调拨查询列表数量(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:24:24 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回调拨查询列表数量
	 */
	public int getWchCount(String wchTitle,String wmsCode,String wchAppIsok,
			String wchState,String startTime,String endTime){
		Query query;
		Session session;
		 session = (Session) super.getSession();
	     	String sql ="";
		    if(wmsCode!=null&&!wmsCode.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wmsStroByWchInWms.wmsCode='"+wmsCode+"' or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
		    	else
		    		sql=" and (wmsStroByWchInWms.wmsCode='"+wmsCode+"' or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
		    }
		    if(wchTitle!=null&&!wchTitle.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wchTitle like '%"+wchTitle+"%' or wchCode like '%"+wchTitle+"%')";
		    	else
		    		sql=" and (wchTitle like '%"+wchTitle+"%' or wchCode like '%"+wchTitle+"%')";
		    } 
		    if(wchAppIsok!=null&&!wchAppIsok.equals("")){
				if(sql.length()>0)
					if(wchAppIsok.equals("2"))
						sql+=" and (wchAppIsok!='1' or wchAppIsok is null)";
					else
						sql+=" and wchAppIsok='1' ";
				else
					if(wchAppIsok.equals("2"))
						sql=" and (wchAppIsok!='1' or wchAppIsok is null)";
					else
						sql=" and wchAppIsok='1'";
			}
		    if(wchState!=null&&!wchState.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wchState ='"+wchState+"'";
	    		 else
	    			 sql=" and wchState ='"+wchState+"'";
	    	 }
		    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wchInpDate <= '"+endTime+"'";
	    		else
	    			sql =" and wchInpDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wchInpDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wchInpDate between '"+startTime+"' and '"+endTime+"'";
			}
			String queryString="select count(*) from WmsChange where wchIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * 仓库调拨查询列表(按主题,仓库编号,审核状态,调拨状态,创建日期) <br>
	 * create_date: Aug 11, 2010,5:27:35 PM <br>
	 * @param wchTitle 调拨主题
	 * @param wmsCode 出库编号
	 * @param wchAppIsok 审核状态
	 * @param wchState 调拨状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨查询列表
	 */
	public List wchSearch(String wchTitle,String wmsCode,String wchAppIsok,String wchState,
			String startTime,String endTime,int currentPage,int pageSize){
			Query query;
			Session session;
			session = (Session) super.getSession();
	     	String sql ="";
	     	if(wmsCode!=null&&!wmsCode.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wmsStroByWchInWms.wmsCode='"+wmsCode+"' or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
		    	else
		    		sql=" and (wmsStroByWchInWms.wmsCode='"+wmsCode+"' or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
		    }
		    if(wchTitle!=null&&!wchTitle.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wchTitle like '%"+wchTitle+"%' or wchCode like '%"+wchTitle+"%')";
		    	else
		    		sql=" and (wchTitle like '%"+wchTitle+"%' or wchCode like '%"+wchTitle+"%')";
		    		 
		    } 
		    if(wchAppIsok!=null&&!wchAppIsok.equals("")){
				if(sql.length()>0)
					if(wchAppIsok.equals("2"))
						sql+=" and (wchAppIsok!='1' or wchAppIsok is null)";
					else
						sql+=" and wchAppIsok='1'";
				else
					if(wchAppIsok.equals("2"))
						sql=" and (wchAppIsok!='1' or wchAppIsok is null)";
					else
						sql=" and wchAppIsok='1'";
			}
		    if(wchState!=null&&!wchState.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wchState ='"+wchState+"'";
	    		 else
	    			 sql=" and wchState ='"+wchState+"'";
	    	 }
		    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wchInpDate <= '"+endTime+"'";
	    		else
	    			sql =" and wchInpDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wchInpDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wchInpDate between '"+startTime+"' and '"+endTime+"'";
			}
	     	String queryString=" from WmsChange where wchIsdel='1'"+sql+"order by wchId desc";
			query=session.createQuery(queryString);
			query.setFirstResult((currentPage-1)*pageSize);
			query.setMaxResults(pageSize);
			List list=(List)query.list();
			return list;
	}
	/**
	 * 
	 * 根据id查询仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:33:40 PM <br>
	 * @param wchId 仓库调拨id
	 * @return WmsChange 返回仓库调拨实体
	 */
	public WmsChange WchDesc(Long wchId){
		return (WmsChange) super.getHibernateTemplate().get(WmsChange.class, wchId);
	}
	/**
	 * 
	 * 带删除状态查出调拨单实体 <br>
	 * create_date: Aug 12, 2010,11:56:07 AM <br>
	 * @param wchId 调拨单id
	 * @return WmsChange 返回调拨单实体
	 */
	public WmsChange findWch(Long wchId){
		log.debug("finding all WmsChange instances");
		try {
			String queryString = "from WmsChange where wchIsdel='1' and wchId="+wchId;
			List list=getHibernateTemplate().find(queryString);
			if(list.size()>0)
				return (WmsChange)list.get(0);
			else
				return null;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据调拨单号查询调拨单 <br>
	 * create_date: Aug 12, 2010,11:59:34 AM <br>
	 * @param wchCode 调拨单号
	 * @return List 返回调拨单列表
	 */
	public List findBywchCode(String wchCode) {
		log.debug("finding all WmsChange instances");
		try {
			String queryString = "from WmsChange where wchCode='"+wchCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 更新仓库调拨 <br>
	 * create_date: Aug 11, 2010,5:55:07 PM <br>
	 * @param wmsChange 仓库调拨实体
	 */
	public void update(WmsChange wmsChange){
		super.getHibernateTemplate().update(wmsChange);
	}
	/**
	 * 
	 * 根据入库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,9:58:18 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch(String wmsCode){
		 Query query;
		 Session session;
		 session = (Session) super.getSession();
	     String sql ="from WmsChange where wchIsdel='1' and wmsStroByWchInWms.wmsCode='"+wmsCode+"' order by wchId desc";
	     query=session.createQuery(sql);
		 List list=(List)query.list();
		 return list;
	}
	/**
	 * 
	 * 根据出库仓库查询调拨单 <br>
	 * create_date: Aug 12, 2010,10:10:01 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回调拨单列表
	 */
	public List wchSearch2(String wmsCode){
		 Query query;
		 Session session;
		 session = (Session) super.getSession();
	     String sql ="from WmsChange where wchIsdel='1' and wmsStroByWchOutWms.wmsCode='"+wmsCode+"' order by wchId desc";
	     query=session.createQuery(sql);
		 List list=(List)query.list();
		 return list;
	}
	/**
	 * 
	 * 根据仓库编号查询需要审核的出库单数量 <br>
	 * create_date: Aug 12, 2010,11:34:23 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回出库单列表数量
	 */
	public int findApp(String wmsCode,String isok) {
		Query query;
		Session session;
		 session = (Session) super.getSession();
		 String sql ="";
			if(wmsCode!=null&&!wmsCode.equals("")){
				if(isok.equals("2"))
					sql=" and (wchAppIsok!='1' or wchAppIsok is null) and (wmsStroByWchInWms.wmsCode='"
							+wmsCode+"'or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
				else
					sql=" and wchAppIsok='1' and (wmsStroByWchInWms.wmsCode='"
						+wmsCode+"'or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
			}else{
				if(isok.equals("2"))
					sql=" and (wchAppIsok!='1' or wchAppIsok is null)";
				else
					sql=" and wchAppIsok='1' ";
			}
			String queryString="select count(*) from WmsChange where wchState='0' and wchIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * 根据仓库编号查询要审核的调拨调入数量 <br>
	 * create_date: Aug 12, 2010,11:40:11 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回调拨单列表数量
	 */
	public int findApp2(String wmsCode,String isok) {
		Query query;
		Session session;
		 session = (Session) super.getSession();
		 String sql ="";
			if(wmsCode!=null&&!wmsCode.equals("")){
				if(isok.equals("2"))
					sql=" and (wchAppIsok!='1' or wchAppIsok is null) and (wmsStroByWchInWms.wmsCode='"
							+wmsCode+"'or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
				else
					sql=" and wchAppIsok='1' and (wmsStroByWchInWms.wmsCode='"
						+wmsCode+"'or wmsStroByWchOutWms.wmsCode='"+wmsCode+"')";
			}else{
				if(isok.equals("2"))
					sql=" and (wchAppIsok!='1' or wchAppIsok is null)";
				else
					sql=" and wchAppIsok='1' ";
			}
			String queryString="select count(*) from WmsChange where wchState='2' and wchIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * 根据仓库编号查询需要调拨的单据数量 <br>
	 * create_date: Aug 12, 2010,11:41:58 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回调拨单列表数量
	 */
	public int findInWms(String wmsCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String queryString ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			queryString="select count(*) from WmsChange where wchIsdel='1' and (wchState !='1' and wchState !='4') and (wmsStroByWchInWms.wmsCode='"
						+wmsCode+"'or wmsStroByWchOutWms.wmsCode='"+wmsCode+"') and (wchInpDate between'"+starTime1+
						"' and '"+endTime1+"'or wchAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}else{
			queryString="select count(*) from WmsChange where wchIsdel='1' and (wchState !='1' and wchState !='4') and (wchInpDate between'"
						+starTime1+"' and '"+endTime1+"'or wchAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * 获得待删除的所有调拨单列表 <br>
	 * create_date: Aug 12, 2010,11:47:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回调拨单列表
	 */
	public List findDelWmsChange(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsChange where  wchIsdel='0' order by wchId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * 获得待删除的所有调拨单列表数量 <br>
	 * create_date: Aug 12, 2010,11:47:50 AM <br>
	 * @return List 返回调拨单列表数量
	 */
	public int findDelWmsChange() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsChange where  wchIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
}