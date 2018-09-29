package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ScheduleDAO;
import com.psit.struts.entity.Schedule;
/**
 * 日程安排数据库表操作的实现类 <br>
 */
public class ScheduleDAOImpl extends HibernateDaoSupport implements ScheduleDAO{
	private static final String SCH_DATE = "schStartDate";
	private static final String SCH_TIL = "schTitle";
	private static final String SCH_STATE = "schState";
	private static final String SCH_TYPE = "schType";
	private static final String SCH_TYPE_NAME = "schType.typName";
	private static final String SCH_SE_NO = "person.seqId";
	private static final String SCH_CUS_CODE = "cusCorCus.corCode";
	
	/**
	 * 保存日程内容<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void save(Schedule schedule) {
		super.getHibernateTemplate().save(schedule);
	}
	/**
	 * 获得当天，七天内的日程安排<br>
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @return List 日程安排列表<br>
	 */
	public List getSchByDate(Date startDate, Date endDate, Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = "select  schedule from Schedule as schedule where person.seqId='"+empId
				+ "' and schStartDate between :startDate and :endDate order by schStartDate,schStartTime asc";
		query = session.createQuery(sql).setDate("startDate", startDate).setDate("endDate", endDate);
		List list = (List)query.list();
		return list;
	}

	/**
	 * 获得当天，七天内,三十天内的日程安排数量<br>
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @return int 日程安排数量<br>
	 */
	public int getSchByDateCount(Date startDate, Date endDate, Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		query = session
				.createQuery(
						"select count(*) from Schedule where person.seqId=:empId "
								+ "and schStartDate between :startDate and :endDate")
				.setParameter("empId",empId).setDate("startDate", startDate).setDate("endDate", endDate);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}

	/**
	 * 根据Id获得日程安排<br>
	 * @param id 日程安排id
	 * @return Schedule 日程安排对象<br>
	 */
	public Schedule showSchedule(Long id) {
		return (Schedule) super.getHibernateTemplate().get(Schedule.class, id);
	}

	/**
	 * 更新日程安排信息<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void update(Schedule schedule) {
		super.getHibernateTemplate().update(schedule);
	}

	/**
	 * 删除日程安排<br>
	 * @param schedule 日程安排对象<br>
	 */
	public void delSchedule(Schedule schedule) {
		super.getHibernateTemplate().delete(schedule);
	}
	
	/**
	 * 查询日程<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示的记录数
	 * @param startDate 日程日期开始
	 * @param endDate 日程日期结束
	 * @param empId 用户id
	 * @param isAll 日程状态（1：未完成）
	 * @return List (如果是action方法需注明跳转的一个或多个映射名)<br>
	 */
	public List getSchList(String [] args, String orderItem, String isDe,int pageCurrentNo, int pageSize) {
		Session session = super.getSession();
		Query query = session.createQuery(getHql(args, orderItem, isDe, false));
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	public int getSchListCount(String [] args) {
		Session session = super.getSession();
		Query query = session.createQuery(getHql(args,null,null,true));
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getHql(String [] args,String orderItem,String isDe,boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "sch.";
		String sql = "from Schedule as sch ";
  		if(isCount){
  			sql = "select count(*) " + sql+" ";
  		}
  		else{
  			sql = "select sch "+sql+" ";
  		}
  		if(args!=null){
			if(args[0]!=null){
  	  			if(!args[0].equals("")&& args[1].equals("")){
  	  				appendSql.append(appendSql.length()==0?"where ":"and ");
  	  				appendSql.append(tab+SCH_DATE+" >= '"+args[0]+"' ");
  	  			}else if(!args[0].equals("") && !args[1].equals("")){
  	  				appendSql.append(appendSql.length()==0?"where ":"and ");
  	  				appendSql.append(tab+SCH_DATE+" between '"+args[0]+"' and '"+args[1]+"' ");
  	  			}else if(args[0].equals("") && !args[1].equals("")){
  	  				appendSql.append(appendSql.length()==0?"where ":"and ");
  	  				appendSql.append(tab+SCH_DATE+"<= '"+args[1]+"' ");
  	  			}
  			}
  			if(args[2]!=null && !args[2].equals("")){
  				appendSql.append(appendSql.length()==0?"where ":"and ");
  				appendSql.append(tab+SCH_SE_NO+" = "+args[2]+" ");
  			}
  			if(args[3]!=null && !args[3].equals("1")){
  				appendSql.append(appendSql.length()==0?"where ":"and ");
  				appendSql.append(tab+SCH_STATE+" ='未完成' ");
  			}
  	  		if(args.length>4&&args[4]!=null && !args.equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SCH_CUS_CODE+"="+args[4]+" ");
			}
  		}
		if(!isCount){
			if(orderItem != null && !orderItem.equals("")){
				String[] items = {"schState","schType","schTitle","schDate"};
				String cols[] = {SCH_STATE,SCH_TYPE_NAME,SCH_TIL,SCH_DATE};
				for(int i = 0; i < items.length; i++){
					if(orderItem.equals(items[i])){
						switch(i){
						    case 1:
								sql += "left join " + tab+SCH_TYPE + " ";
								break;
						}
						orderItem = cols[i];
					}
				}
				appendSql.append("order by "+tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else {//默认排序
				appendSql.append("order by "+tab+"schStartDate desc ");
			}
		}
		return sql+appendSql.toString();
	}
	
}