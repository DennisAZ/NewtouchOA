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

import com.psit.struts.DAO.WmsWarOutDAO;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.entity.WmsWarOut;
import com.psit.struts.util.OperateDate;
/**
 * 
 * 出库单DAO实现类 <br>
 *
 * create_date: Aug 19, 2010,2:42:17 PM<br>
 * @author zjr
 */
@SuppressWarnings("unchecked")
public class WmsWarOutDAOImpl extends HibernateDaoSupport implements WmsWarOutDAO{
	private static final Log log = LogFactory.getLog(WmsWarOutDAOImpl.class);
	// property constants
	public static final String WWO_TITLE = "wwoTitle";
	public static final String WWO_STATE = "wwoState";
	public static final String WWO_REMARK = "wwoRemark";
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
	 * 保存出库单 <br>
	 * create_date: Aug 11, 2010,4:45:13 PM <br>
	 * @param transientInstance 出库单实体
	 */
	public void save(WmsWarOut transientInstance) {
		log.debug("saving WmsWarOut instance");
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
	 * 删除出库单 <br>
	 * create_date: Aug 11, 2010,5:22:07 PM <br>
	 * @param persistentInstance 出库单实体
	 */
	public void delete(WmsWarOut persistentInstance) {
		log.debug("deleting WmsWarOut instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

//	public WmsWarOut findById(java.lang.String id) {
//		log.debug("getting WmsWarOut instance with id: " + id);
//		try {
//			WmsWarOut instance = (WmsWarOut) getHibernateTemplate().get(
//					"com.psit.struts.entity.WmsWarOut", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(WmsWarOut instance) {
//		log.debug("finding WmsWarOut instance by example");
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
//		log.debug("finding WmsWarOut instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsWarOut as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByWwoTitle(Object wwoTitle) {
//		return findByProperty(WWO_TITLE, wwoTitle);
//	}
//
//	public List findByWwoState(Object wwoState) {
//		return findByProperty(WWO_STATE, wwoState);
//	}
//
//	public List findByWwoRemark(Object wwoRemark) {
//		return findByProperty(WWO_REMARK, wwoRemark);
//	}
//
//	public List findAll() {
//		log.debug("finding all WmsWarOut instances");
//		try {
//			String queryString = "from WmsWarOut";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public WmsWarOut merge(WmsWarOut detachedInstance) {
//		log.debug("merging WmsWarOut instance");
//		try {
//			WmsWarOut result = (WmsWarOut) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsWarOut instance) {
//		log.debug("attaching dirty WmsWarOut instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsWarOut instance) {
//		log.debug("attaching clean WmsWarOut instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsWarOutDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsWarOutDAOImpl) ctx.getBean("WmsWarOutDAO");
//	}
	/**
	 * 
	 * 出库单查询列表数量(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:50:47 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回出库单列表数量
	 */
	public int getWwoCount(String wwoTitle,String wmsCode,String wwoAppIsok,
			String wwoState,String startTime,String endTime){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="";
	     //1.直接搜索 2.按仓库搜素。3.带状态（审核，出库状态）搜索
		    if(wmsCode!=null&&!wmsCode.equals("")){
		    	if(sql.length()>0)
		    		 sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
		    	else
		    		 sql=" and wmsStro.wmsCode='"+wmsCode+"'";
		    }
		    if(wwoTitle!=null&&!wwoTitle.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wwoTitle like '%"+wwoTitle+"%' or wwoCode like '%"+wwoTitle+"%')";
		    	else
		    		sql=" and (wwoTitle like '%"+wwoTitle+"%' or wwoCode like '%"+wwoTitle+"%')";
		    }
		    if(wwoAppIsok!=null&&!wwoAppIsok.equals("")){
		    	if(sql.length()>0)
		    		if(wwoAppIsok.equals("2"))
		    			sql+=" and (wwoAppIsok!='1' or wwoAppIsok is null) and wwoState='0'";
		    		else
		    			sql+=" and wwoAppIsok='1' and wwoState='0'";
				else
					if(wwoAppIsok.equals("2"))
						sql=" and (wwoAppIsok!='1' or wwoAppIsok is null) and wwoState='0'";
					else
						sql=" and wwoAppIsok='1' and wwoState='0'";
			}
		    if(wwoState!=null&&!wwoState.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wwoState ='"+wwoState+"'";
	    		 else
	    			 sql=" and wwoState ='"+wwoState+"'";
	    	 }
		    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wwoInpDate <= '"+endTime+"'";
	    		else
	    			sql =" and wwoInpDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wwoInpDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wwoInpDate between '"+startTime+"' and '"+endTime+"'";
			}
	    	String queryString="select count(*) from WmsWarOut where wwoIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * 出库单查询列表(按主题,仓库编号,审核状态,出库状态,创建日期) <br>
	 * create_date: Aug 11, 2010,4:55:05 PM <br>
	 * @param wwoTitle 主题
	 * @param wmsCode 出库编号
	 * @param wwoAppIsok 审核状态
	 * @param wwoState 出库状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wwoTitle,String wmsCode,String wwoAppIsok,String wwoState,
			String startTime,String endTime,int currentPage,int pageSize){
			Query query;
			Session session;
		    session = (Session) super.getSession();
		     String sql ="";
		     //1.直接搜索 2.按仓库搜素。3.带状态（审核，出库状态）搜索
		     if(wmsCode!=null&&!wmsCode.equals("")){
			    	if(sql.length()>0)
			    		 sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
			    	else
			    		 sql=" and wmsStro.wmsCode='"+wmsCode+"'";
			    }
		     if(wwoTitle!=null&&!wwoTitle.equals("")){
			    	if(sql.length()>0)
			    		sql+=" and (wwoTitle like '%"+wwoTitle+"%' or wwoCode like '%"+wwoTitle+"%')";
			    	else
			    		sql=" and (wwoTitle like '%"+wwoTitle+"%' or wwoCode like '%"+wwoTitle+"%')";
			    }
			    if(wwoAppIsok!=null&&!wwoAppIsok.equals("")){
			    	if(sql.length()>0)
			    		if(wwoAppIsok.equals("2"))
			    			sql+=" and (wwoAppIsok!='1' or wwoAppIsok is null) and wwoState='0'";
			    		else
			    			sql+=" and wwoAppIsok='1' and wwoState='0'";
					else
						if(wwoAppIsok.equals("2"))
							sql=" and (wwoAppIsok!='1' or wwoAppIsok is null) and wwoState='0'";
						else
							sql=" and wwoAppIsok='1' and wwoState='0'";
				}
			    if(wwoState!=null&&!wwoState.equals("")){
		    		 if(sql.length()>0)
		    			 sql+=" and wwoState ='"+wwoState+"'";
		    		 else
		    			 sql=" and wwoState ='"+wwoState+"'";
		    	 }
			    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
		    		if(sql.length()>0)
		    			sql +=" and wwoInpDate <= '"+endTime+"'";
		    		else
		    			sql =" and wwoInpDate <= '"+endTime+"'";
				}
				else if(startTime!=null&&!startTime.equals("")){
					if(sql.length()>0)
		    			sql +=" and wwoInpDate between '"+startTime+"' and '"+endTime+"'";
		    		else
		    			sql =" and wwoInpDate between '"+startTime+"' and '"+endTime+"'";
				}
			    String queryString="from WmsWarOut where wwoIsdel='1'"+sql+"order by wwoId desc";
				query=session.createQuery(queryString);
				query.setFirstResult((currentPage-1)*pageSize);
				query.setMaxResults(pageSize);
				List list=(List)query.list();
				return list;
	}
	/**
	 * 
	 * 按出库单id查询出库单实体<br>
	 * create_date: Aug 11, 2010,4:59:17 PM <br>
	 * @param wwoId 库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut WwoSearchByCode(Long wwoId){
		return (WmsWarOut) super.getHibernateTemplate().get(WmsWarOut.class, wwoId);
	}
	/**
	 * 
	 * 带删除状态查出出库单实体 <br>
	 * create_date: Aug 12, 2010,11:54:19 AM <br>
	 * @param wwoId 出库单id
	 * @return WmsWarOut 返回出库单实体
	 */
	public WmsWarOut findWwo(Long wwoId) {
		log.debug("finding all WmsWarOut instances");
		try {
			String queryString = "from WmsWarOut where wwoIsdel='1' and wwoId="+wwoId;
			List list=getHibernateTemplate().find(queryString);
			if(list.size()>0)
				return (WmsWarOut)list.get(0);
			else
				return null;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据出库单号查询出库单 <br>
	 * create_date: Aug 12, 2010,11:58:35 AM <br>
	 * @param wwoCode 出库单号
	 * @return List 返回出库单列表
	 */
	public List findByWwoCode(String wwoCode) {
		log.debug("finding all WmsWarOut instances");
		try {
			String queryString = "from WmsWarOut where wwoCode='"+wwoCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 更新出库单 <br>
	 * create_date: Aug 11, 2010,5:21:47 PM <br>
	 * @param wmsWarOut 出库单实体
	 */
	public void update(WmsWarOut wmsWarOut){
		super.getHibernateTemplate().update(wmsWarOut);
	}
	/**
	 * 
	 * 查询指定仓库的出库单 <br>
	 * create_date: Aug 12, 2010,9:51:19 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回出库单列表
	 */
	public List WwoSearch(String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="select wwo from WmsWarOut as wwo where wwo.wwoIsdel='1' and wwo.wmsStro.wmsCode='"+wmsCode+"' order by wwo.wwoId desc";
	     query=session.createQuery(sql);
		 List list=(List)query.list();
		 return list;
	}
	/**
	 * 
	 * 查询订单下的出库单 <br>
	 * create_date: Aug 12, 2010,10:13:32 AM <br>
	 * @param sodCode 订单id
	 * @return List 返回订单列表
	 */
	public List findByOrd(String sodCode) {
		long id=Long.parseLong(sodCode);
		log.debug("finding all WmsWarOut instances");
		try {
			String queryString = "from WmsWarOut where wwoIsdel='1' and salOrdCon.sodCode="+id;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
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
				sql=" and (wwoAppIsok!='1' or wwoAppIsok is null) and wmsStro.wmsCode='"+wmsCode+"'";
			else
				sql=" and wwoAppIsok='1' and wmsStro.wmsCode='"+wmsCode+"'";
		}else{
			if(isok.equals("2"))
				sql=" and (wwoAppIsok!='1' or wwoAppIsok is null)";
			else
				sql=" and wwoAppIsok='1'";
		}
		String queryString="select count(*) from WmsWarOut where wwoIsdel='1' and wwoState='0'"+sql;
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * 根据仓库编号查询需要出库的单据数量 <br>
	 * create_date: Aug 12, 2010,11:37:02 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回出库单列表数量
	 */
	public int findInWms(String wmsCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String queryString ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			queryString="select count(*) from WmsWarOut where wwoIsdel='1' and wwoState='0' and wmsStro.wmsCode='"
						+wmsCode+"' and (wwoInpDate between'"+starTime1+"' and '"+endTime1+"'or wwoAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}else{
			queryString="select count(*) from WmsWarOut where wwoIsdel='1' and wwoState='0' and (wwoInpDate between'"
						+starTime1+"' and '"+endTime1+"'or wwoAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * 获得待删除的所有出库单列表 <br>
	 * create_date: Aug 12, 2010,11:45:40 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回出库单列表
	 */
	public List findDelWarOut(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsWarOut where  wwoIsdel='0' order by wwoId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * 获得待删除的所有出库单列表数量 <br>
	 * create_date: Aug 12, 2010,11:47:23 AM <br>
	 * @return int 返回出库单列表数量
	 */
	public int findDelWarOutCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsWarOut where  wwoIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
}