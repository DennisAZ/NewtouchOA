package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalTaskDAO;
import com.psit.struts.entity.SalTask;

/**
 * 销售工作数据库操作实现类<br>
 */

public class SalTaskDAOImpl extends HibernateDaoSupport implements SalTaskDAO{
	private static final Log log = LogFactory.getLog(SalTaskDAOImpl.class);
	
	public static final String SAL_ID = "stId";
	public static final String SAL_TITLE = "stTitle";
	public static final String SAL_START_TIME = "stStartDate";
	public static final String SAL_END_TIME = "stFinDate";
	public static final String SAL_STU = "stStu";
	public static final String SAL_ISDEL = "stIsdel";
	public static final String SAL_STNAME = "stName";
	public static final String SAL_STRELDATE ="stRelDate";
	public static final String SAL_EMP_NO ="salEmp.seNo";
	public static final String SAL_TYPE="salTaskType";
	public static final String SAL_TYPE_NAME = "salTaskType.typName";
	protected void initDao() {
		// do nothing
	}

	/**
	 * 保存销售工作<br>
	 * @param transientInstance 销售工作对象<br>
	 */
	public void save(SalTask transientInstance) {
		log.debug("saving SalTask instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * 删除工作<br>
	 * @param persistentInstance 销售工作对象<br>
	 */
	public void delete(SalTask persistentInstance) {
		log.debug("deleting SalTask instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/**
	 * 销售工作查询<br>
	 * create_date: Aug 12, 2010,4:13:29 PM<br>
	 * @param args 0: 工作主题
	 * 			   1:工作状态
	 * 			   2:发布人Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页码
	 * @param pageSize 每页显示数量
	 * @return List 销售工作记录列表<br>
	 */
	public List salTaskSearch(String [] args,String orderItem, String isDe ,int currentPage,int pageSize){
			Query query;
			Session session;
		    session = (Session) super.getSession();
			    query=getSalTaskSql(session,args,orderItem,isDe,false);
				query.setFirstResult((currentPage-1)*pageSize);
				query.setMaxResults(pageSize);
				List list=(List)query.list();
				return list;
	}
	public int getCount(String [] args){
		Query query;
		Session session;
		session = (Session) super.getSession();
			query=getSalTaskSql(session,args,null,null,true);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	private Query getSalTaskSql(Session session, String [] args, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "st.";
		String sql = "from SalTask st ";
		if(isCount){
			sql = "select count(*) "+sql;
		}else{
		    sql = "select st "+sql;	
		}
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				appendSql.append("where ");
				appendSql.append(tab+SAL_TITLE+" like :stTitle ");
			}
			if(args[1]!=null && !args[1].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SAL_STU+" like :stStu ");
			}
			if(args[2]!=null && !args[2].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SAL_EMP_NO+"= '"+args[2]+"' and "+tab+SAL_ISDEL+"= '1' ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"stStu","typName","stTitle","startTime","endTime",null,"stName","stRelDate"};
				String [] cols = {SAL_STU,SAL_TYPE_NAME,SAL_TITLE,SAL_START_TIME,SAL_END_TIME,null,SAL_STNAME,SAL_STRELDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						switch(i){
						 case 1:
							 sql += "left join "+tab+SAL_TYPE+" ";
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
				appendSql.append("order by "+tab+SAL_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				query.setString("stTitle", "%"+args[0]+"%");
			}
			if(args[1]!=null && !args[1].equals("")){
				query.setString("stStu", "%"+args[1]+"%");
			}
		}
		return query;
	}
	/**
	 * 查看销售工作详情<br>
	 * @param stId 销售工作id
	 * @return SalTask 销售工作对象<br>
	 */
	public SalTask salTaskDesc(Long stId){	
		return (SalTask)super.getHibernateTemplate().get(SalTask.class,stId);
		
	}
	/**
	 * 工作更新<br>
	 * @param salTask 销售工作对象<br>
	 */
	public void updateSalTask(SalTask salTask){
		super.getHibernateTemplate().update(salTask);
	}

	/**
	 * 获得待删除的所有工作安排<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @return List 销售工作记录列表<br>
	 */
	public List findDelSalTask(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from SalTask where  stIsdel='0' order by stId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

	/**
	 * 获得待删除的所有工作安排数量<br>
	 * @return int 销售工作记录数<br>
	 */
	public int findDelSalTaskCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from SalTask where  stIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * 得到待执行的发布的工作<br>
	 * @param userId 发布人id
	 * @return List<SalTask> 销售工作记录列表<br>
	 */
	public List<SalTask> getTodoTask(String userId){
		Session session = (Session) super.getSession();
		String hql = "from SalTask where salEmp.seNo="+userId+" and stStu='0' and stIsdel='1' order by stFinDate ";
		Query query=session.createQuery(hql);
		List<SalTask> list=(List)query.list();
		return list;
	}

	/**
	 * 得到某人发布的所有工作<br>
	 * @param userId 发布人id
	 * @return List<SalTask> 销售工作记录列表<br>
	 */
	public List<SalTask> findTaskByUser(String userId) {
		Session session = (Session) super.getSession();
		String hql = "from SalTask where salEmp.seNo="+userId+" and stIsdel='1' order by stFinDate ";
		Query query=session.createQuery(hql);	
		List<SalTask> list=(List)query.list();
		return list;
	}
	
}