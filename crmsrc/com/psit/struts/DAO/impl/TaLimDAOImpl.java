package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.TaLimDAO;
import com.psit.struts.entity.TaLim;
/**
 * 工作执行人DAO实现类 <br>
 */
public class TaLimDAOImpl extends HibernateDaoSupport implements TaLimDAO{
	private static final Log log = LogFactory.getLog(TaLimDAOImpl.class);
	// property constants
	public static final String TA_LIM_ID = "taLimId";
	public static final String TA_ISDEL = "taIsdel";
	public static final String TA_ISFIN = "taIsfin";
	public static final String TA_TITLE = "salTask.stTitle";
	public static final String TA_STATE = "salTask.stStu";
	public static final String TA_EMP_NO = "salEmp.seNo";
	public static final String TA_SALTASK_ISDEL ="salTask.stIsdel";
	public static final String TA_SALTASK_TYPE_NAME ="salTask.salTaskType.typName";
	public static final String TA_SALTASK_TYPE = "salTask.salTaskType";
	public static final String TA_STNAME = "salTask.stName";
	public static final String TA_STRELDATE = "salTask.stRelDate";
	public static final String TA_START_DATE = "salTask.stStartDate";
	public static final String TA_END_DATE  ="salTask.stFinDate";
	/**
	 * 保存工作安排执行人 <br>
	 * @param transientInstance 工作安排执行人实体
	 */
	public void save(TaLim transientInstance) {
		log.debug("saving TaLim instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 删除执行人 <br>
	 * @param persistentInstance 工作执行人实体
	 */
	public void delete(TaLim persistentInstance) {
		log.debug("deleting TaLim instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * 根据工作执行人ID查询实体 <br>
	 * @param id 工作执行人id
	 * @return TaLim 返回工作执行人实体
	 */
	public TaLim findById(java.lang.Long id) {
		log.debug("getting TaLim instance with id: " + id);
		try {
			TaLim instance = (TaLim) getHibernateTemplate().get(
					"com.psit.struts.entity.TaLim", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * 根据工作Id删除执行人 <br>
	 * @param stId 工作安排id
	 */
	public void delByTaskId(Long stId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		log.debug("finding all TaLim instances");
		try {
			String queryString = "delete TaLim  where taIsfin='1' and salTask.stId="+stId;
			 int i=session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 某人的工作安排查询列表（按主题,状态，完成日期） <br>
	 * @param args 0:工作主题
	 * 			   1:工作状态
	 *    		   2:工作执行中标识
	 *    		   3:日期标识
	 *    		   4:员工Id
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回工作安排列表
	 */
	public List myTaskSearch(String [] args, int currentPage, int pageSize, String orderItem, String isDe) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		query = getMyTaskSql(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int myTaskCount(String [] args) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		query = getMyTaskSql(session,args,null,null,true);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private Query getMyTaskSql(Session session, String [] args, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "taLim.";
		String sql = "from TaLim  taLim ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql = "select taLim "+sql;
		}
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+TA_TITLE+" like :title ");
			}
			if(args[1]!=null && !args[1].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append(tab+TA_STATE +" like :state ");
			}
			if(args[2]!=null && args[2].equals("tip")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append("("+tab+TA_ISDEL+"='0' or "+tab+TA_ISDEL+"='2') "); 
			}
			if(args[3]!=null && !args[3].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append(tab+TA_ISDEL +"= '"+args[3]+"' ");
			}
			if(args[4]!=null && !args[4].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
    			appendSql.append(tab+TA_EMP_NO +"= '"+args[4]+"' and "+tab+TA_SALTASK_ISDEL+"= '1' and "+tab+TA_ISFIN+"= '1' ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items =  {"stStu","typName","stTitle","startTime","endTime",null,"stName","stRelDate"};
				String [] cols = {TA_STATE,TA_SALTASK_TYPE_NAME,TA_TITLE,TA_START_DATE,TA_END_DATE,null,TA_STNAME,TA_STRELDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						switch(i){
							case 1:
								sql +="left join "+tab+TA_SALTASK_TYPE+" ";
								break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+TA_LIM_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				query.setString("title", "%"+args[0]+"%");
			}
			if(args[1]!=null && !args[1].equals("")){
				query.setString("state", "%"+args[1]+"%");
			}
		}
		return query;
	}
	/**
	 * 根据工作安排Id查询执行人 <br>
	 * @param stId 工作id
	 * @return List 返回工作执行人列表
	 */
	public List findByTaskId(Long stId){
		log.debug("finding all TaLim instances");
		try {
			String queryString = "from TaLim where  taIsfin='1' and salTask.stId="+stId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	//根据工作Id查询执行人
	public List findAllTaskMan(Long stId){
		log.debug("finding all TaLim instances");
		try {
			String queryString = "from TaLim where salTask.stId="+stId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 更新工作执行人 <br>
	 * @param instance 工作执行人实体
	 */
	public void updateTaskMan(TaLim instance) {
		log.debug("attaching dirty TaLim instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * 根据完成时限查询收到的工作 <br>
	 * @param userId 员工Id
	 * @param startDate 完成日期(开始)
	 * @param endDate 完成日期(结束)
	 * @param stu 状态
	 * @return List 返回工作执行人列表
	 */
	public List getMyTaskByDate(String userId,Date startDate,Date endDate,String stu){
		Session session = (Session) super.getSession();
		String hql = "from TaLim where salEmp.seNo="+userId+" and taIsfin='0' and salTask.stIsdel='1'";
		if(startDate!=null&&endDate!=null){
			hql += " and salTask.stFinDate between "+startDate+" and "+endDate; 
		}
		else if(endDate==null){
			hql += " and salTask.stFinDate > "+startDate; 
		}
		else{
			hql += " and salTask.stFinDate < "+endDate; 
		}
		if(stu!=null&&!stu.equals("")){
			hql += " and taIsdel='"+stu+"'";
		}
		hql+=" order by salTask.stFinDate";
		Query query=session.createQuery(hql);	
		List list=(List)query.list();
		return list;
	}
	/**
	 * 得到未设定完成时限的收到的工作 <br>
	 * @param userCode 账号
	 * @param stu 工作状态
	 * @return List 返回工作列表
	 */
	public List getMyTaskOfNoDate(String userCode,String stu){
		Session session = (Session) super.getSession();
		String hql;
		if(stu!=null&&!stu.equals("")){
			hql ="from TaLim where salTask.stFinDate=null and salTask.stIsdel='0' and taIsfin='1' and taIsdel='"+stu+"' order by taLimId desc ";
		}
		else{
			hql ="from TaLim where salTask.stFinDate=null and salTask.stIsdel='0' and taIsfin='1' order by taLimId desc ";
		}
		Query query=session.createQuery(hql);	
		List list=(List)query.list();
		return list;
	}
	/**
	 * 得到待执行的收到的工作 <br>
	 * @param userId 员工Id
	 * @return List<TaLim> 返回工作执行人列表
	 */
	public List<TaLim> getTodoMyTask(String userId){
		Session session = (Session) super.getSession();
		String hql = "from TaLim where salEmp.seNo='"+userId+"' and salTask.stStu='0' and salTask.stIsdel='1' and taIsfin='1' order by salTask.stFinDate";
		Query query=session.createQuery(hql);	
		List<TaLim> list=(List)query.list();
		return list;
	}
	/**
	 * 根据工作Id查询批量删除执行人 <br>
	 * @param stId 工作Id
	 */
	public void updateByTaskId(Long stId){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		log.debug("finding all TaLim instances");
		try {
			String queryString = "update TaLim set taIsfin='0' where  salTask.stId="+stId;
			session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}