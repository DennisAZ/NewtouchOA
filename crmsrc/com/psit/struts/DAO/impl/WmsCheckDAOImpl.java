package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsCheckDAO;
import com.psit.struts.entity.WmsCheck;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.util.OperateDate;
/**
 * 
 * 库存盘点DAO实现类 <br>
 *
 * create_date: Aug 18, 2010,3:57:00 PM<br>
 * @author zjr
 */
public class WmsCheckDAOImpl extends HibernateDaoSupport implements WmsCheckDAO{
	private static final Log log = LogFactory.getLog(WmsCheckDAOImpl.class);
	// property constants
	public static final String WMC_TITLE = "wmcTitle";
	public static final String WMC_DATE = "wmcDate";
	public static final String WMC_STATE = "wmcState";
	public static final String WMC_REMARK = "wmcRemark";
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
	 * 保存库存盘点 <br>
	 * create_date: Aug 12, 2010,10:30:44 AM <br>
	 * @param transientInstance 库存盘点实体
	 */
	public void save(WmsCheck transientInstance) {
		log.debug("saving WmsCheck instance");
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
	 * 删除盘点单据 <br>
	 * create_date: Aug 12, 2010,10:42:39 AM <br>
	 * @param persistentInstance 盘点单据实体
	 */
	public void delete(WmsCheck persistentInstance) {
		log.debug("deleting WmsCheck instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据Id查看盘点单据实体 <br>
	 * create_date: Aug 12, 2010,10:32:14 AM <br>
	 * @param id 盘点单id
	 * @return WmsCheck 返回盘点单据实体
	 */
	public WmsCheck findById(java.lang.Long id) {
		log.debug("getting WmsCheck instance with id: " + id);
		try {
			WmsCheck instance = (WmsCheck) getHibernateTemplate().get(
					"com.psit.struts.entity.WmsCheck", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

//	public List findByExample(WmsCheck instance) {
//		log.debug("finding WmsCheck instance by example");
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
//		log.debug("finding WmsCheck instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsCheck as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByWmcTitle(Object wmcTitle) {
//		return findByProperty(WMC_TITLE, wmcTitle);
//	}
//
//	public List findByWmcDate(Object wmcDate) {
//		return findByProperty(WMC_DATE, wmcDate);
//	}
//
//	public List findByWmcState(Object wmcState) {
//		return findByProperty(WMC_STATE, wmcState);
//	}
//
//	public List findByWmcRemark(Object wmcRemark) {
//		return findByProperty(WMC_REMARK, wmcRemark);
//	}
//
//	public List findAll() {
//		log.debug("finding all WmsCheck instances");
//		try {
//			String queryString = "from WmsCheck";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public WmsCheck merge(WmsCheck detachedInstance) {
//		log.debug("merging WmsCheck instance");
//		try {
//			WmsCheck result = (WmsCheck) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsCheck instance) {
//		log.debug("attaching dirty WmsCheck instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsCheck instance) {
//		log.debug("attaching clean WmsCheck instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsCheckDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsCheckDAOImpl) ctx.getBean("WmsCheckDAO");
//	}
	/**
	 * 
	 * 库存盘点查询列表数量(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:20:27 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @return int 返回库存盘点列表数量
	 */
	public int getWmcCount(String wmcTitle,String wmsCode,String wmcAppIsok,
			String wmcState,String startTime,String endTime){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="";
	    if(wmsCode!=null&&!wmsCode.equals("")){
	    	if(sql.length()>0)
	    		sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
	    	else
	    		sql=" and wmsStro.wmsCode='"+wmsCode+"'";
	    }
	    if(wmcTitle!=null&&!wmcTitle.equals("")){
	    	if(sql.length()>0)
	    		sql+=" and (wmcTitle like '%"+wmcTitle+"%' or wmcCode like '%"+wmcTitle+"%')";
	    	else
	    		sql=" and (wmcTitle like '%"+wmcTitle+"%' or wmcCode like '%"+wmcTitle+"%')";
	    		 
	    } 
	    if(wmcAppIsok!=null&&!wmcAppIsok.equals("")){
	    	if(sql.length()>0)
	    		if(wmcAppIsok.equals("2"))
	    			sql+=" and (wmcAppIsok!='1' or wmcAppIsok is null) and wmcState='0'";
	    		else
	    			sql+=" and wmcAppIsok='1' and wmcState='0'";
			else
				if(wmcAppIsok.equals("2"))
					sql=" and (wmcAppIsok!='1' or wmcAppIsok is null) and wmcState='0'";
				else
					sql=" and wmcAppIsok='1' and wmcState='0'";
			}
		    if(wmcState!=null&&!wmcState.equals("")){
		   		 if(sql.length()>0)
		   			 sql+=" and wmcState ='"+wmcState+"'";
		   		 else
		   			 sql=" and wmcState ='"+wmcState+"'";
		    }
		    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wmcInpDate <= '"+endTime+"'";
	    		else
	    			sql =" and wmcInpDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wmcInpDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wmcInpDate between '"+startTime+"' and '"+endTime+"'";
			}
	     	String queryString="select count(*) from WmsCheck where wmcIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * 库存盘点查询列表(按主题,仓库编号,审核状态,盘点状态,创建日期) <br>
	 * create_date: Aug 12, 2010,10:29:10 AM <br>
	 * @param wmcTitle 主题
	 * @param wmsCode 仓库编号
	 * @param wmcAppIsok 审核状态
	 * @param wmcState 盘点状态
	 * @param startTime 创建日期(开始)
	 * @param endTime 创建日期(结束)
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回库存盘点列表
	 */
	public List wmcSearch(String wmcTitle,String wmsCode,String wmcAppIsok,String wmcState,
			String startTime,String endTime,int currentPage,int pageSize){
		Query query;
		Session session;
	    session = (Session) super.getSession();
	     String sql="";
	     if(wmsCode!=null&&!wmsCode.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
		    	else
		    		sql=" and wmsStro.wmsCode='"+wmsCode+"'";
		    }
	     	if(wmcTitle!=null&&!wmcTitle.equals("")){
		    	if(sql.length()>0)
		    		sql+=" and (wmcTitle like '%"+wmcTitle+"%' or wmcCode like '%"+wmcTitle+"%')";
		    	else
		    		sql=" and (wmcTitle like '%"+wmcTitle+"%' or wmcCode like '%"+wmcTitle+"%')";
		    		 
		    } 
		    if(wmcAppIsok!=null&&!wmcAppIsok.equals("")){
		    	if(sql.length()>0)
		    		if(wmcAppIsok.equals("2"))
		    			sql+=" and (wmcAppIsok!='1' or wmcAppIsok is null) and wmcState='0'";
		    		else
		    			sql+=" and wmcAppIsok='1' and wmcState='0'";
				else
					if(wmcAppIsok.equals("2"))
						sql=" and (wmcAppIsok!='1' or wmcAppIsok is null) and wmcState='0'";
					else
						sql=" and wmcAppIsok='1' and wmcState='0'";
				}
		    if(wmcState!=null&&!wmcState.equals("")){
		   		 if(sql.length()>0)
		   			 sql+=" and wmcState ='"+wmcState+"'";
		   		 else
		   			 sql=" and wmcState ='"+wmcState+"'";
		    }
		    if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wmcInpDate <= '"+endTime+"'";
	    		else
	    			sql =" and wmcInpDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wmcInpDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wmcInpDate between '"+startTime+"' and '"+endTime+"'";
			}
		    String queryString=" from WmsCheck where wmcIsdel='1'"+sql+"order by wmcId desc";
			query=session.createQuery(queryString);
			query.setFirstResult((currentPage-1)*pageSize);
			query.setMaxResults(pageSize);
			List list=(List)query.list();
			return list;
	}
	/**
	 * 
	 * 带删除状态查出盘点实体 <br>
	 * create_date: Aug 12, 2010,11:57:48 AM <br>
	 * @param wmcId 盘点记录id
	 * @return WmsCheck 返回盘点记录实体
	 */
	public WmsCheck findWmc(Long wmcId) {
		log.debug("finding all WmsCheck instances");
		try {
			String queryString = "from WmsCheck where wmcIsdel='1' and wmcId="+wmcId;
			List list=getHibernateTemplate().find(queryString);
			if(list.size()>0)
				return (WmsCheck) list.get(0);
			else
				return null;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据盘点单号查询 <br>
	 * create_date: Aug 12, 2010,12:00:15 PM <br>
	 * @param wmcCode 盘点单号
	 * @return List 返回盘点记录列表
	 */
	public List findByWmcCode(String wmcCode) {
		log.debug("finding all WmsCheck instances");
		try {
			String queryString = "from WmsCheck where wmcCode='"+wmcCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 更新盘点单据 <br>
	 * create_date: Aug 12, 2010,10:41:52 AM <br>
	 * @param instance 盘点单id
	 */
	public void updateWmc(WmsCheck instance){
		log.debug("attaching dirty WmsCheck instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 查询指定仓库的盘点 <br>
	 * create_date: Aug 12, 2010,11:13:49 AM <br>
	 * @param wmsCode 仓库编号
	 * @return List 返回盘点列表
	 */
	public List wmcSearch(String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="select wmc from WmsCheck as wmc where wmc.wmcIsdel='1' and wmc.wmsStro.wmsCode='"+wmsCode+"' order by wmc.wmcId desc";
	     query=session.createQuery(sql);
		 List list=(List)query.list();
		 return list;
	}
	/**
	 * 
	 * 根据仓库编号查询需要审核的盘点单数量 <br>
	 * create_date: Aug 12, 2010,11:43:50 AM <br>
	 * @param wmsCode 仓库编号
	 * @param isok 审核状态
	 * @return int 返回盘点单列表数量
	 */
	public int findApp(String wmsCode,String isok) {
		Query query;
		Session session;
		 session = (Session) super.getSession();
		 String sql ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			if(isok.equals("2"))
				sql=" and (wmcAppIsok!='1' or wmcAppIsok is null) and wmsStro.wmsCode='"+wmsCode+"'";
			else
				sql=" and wmcAppIsok='1' and wmsStro.wmsCode='"+wmsCode+"'";
		}else{
			if(isok.equals("2"))
				sql=" and (wmcAppIsok!='1' or wmcAppIsok is null)";
			else
				sql=" and wmcAppIsok='1'";
		}
		 String queryString="select count(*) from WmsCheck where wmcIsdel='1' and wmcState='0'"+sql;
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * 根据仓库编号查询雪要盘点的单据数量 <br>
	 * create_date: Aug 12, 2010,11:44:39 AM <br>
	 * @param wmsCode 仓库编号
	 * @return int 返回盘点单列表数量
	 */
	public int findInWms(String wmsCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String queryString ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			queryString="select count(*) from WmsCheck where wmcIsdel='1' and wmcState='0' and wmsStro.wmsCode='"
					+wmsCode+"' and (wmcInpDate between'"+starTime1+"' and '"+endTime1+"' or wmcAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}else{
			queryString="select count(*) from WmsCheck where wmcIsdel='1' and wmcState='0' and (wmcInpDate between'"
					+starTime1+"' and '"+endTime1+"' or wmcAltDate between'"+starTime1+"' and '"+endTime1+"')";
		}
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * 获得待删除的所有盘点记录列表 <br>
	 * create_date: Aug 12, 2010,11:49:50 AM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回盘点记录列表
	 */
	public List findDelWmsCheck(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsCheck where  wmcIsdel='0' order by wmcId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * 获得待删除的所有盘点记录数量 <br>
	 * create_date: Aug 12, 2010,11:50:28 AM <br>
	 * @return int 返回盘点记录列表数量
	 */
	public int findDelWmsCheckCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsCheck where  wmcIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}

}