package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.MessageDAO;
import com.psit.struts.entity.Message;
/**
 * 消息报告DAO实现类 <br>
 */
public class MessageDAOImpl extends HibernateDaoSupport implements MessageDAO{
	public static final String  LIM_RML_ID = "rmlId";
	public static final String 	LIM_RML_DATE = "rmlDate";
	public static final String 	LIM_RML_ISDEL = "rmlIsdel";
	public static final String  LIM_RML_STATE = "rmlState";
	public static final String 	LIM_EMP_NO = "salEmp.seNo";
	public static final String  LIM_MSG_TITLE = "message.meTitle";
	public static final String  LIM_MSG_INSUSER = "message.meInsUser";
	
	public static final String  MSG_ID = "meCode";
	public static final String  MSG_TITLE = "meTitle";
	public static final String  MSG_MEDATE = "meDate";
	public static final String  MSG_ISSEND = "meIssend";
	public static final String  MSG_ISDEL = "meIsdel";
	public static final String  MSG_EMP_NO = "salEmp.seNo";
	
	/**
	 * 保存消息内容 <br>
	 * @param message 消息实体
	 */
	public void save(Message message) {
		super.getHibernateTemplate().save(message);
	}
	/**
	 * 获得特定账号已提交的报告<br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回报告列表 
	 */
	public List getHaveSenMess(int pageCurrentNo, int pageSize, Long empId, String orderItem, String isDe) {
		 Session session = super.getSession();
		String sql = getHaveSenMessSql(empId, orderItem, isDe, false);
		Query query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int getHaveSenMessCount(Long empId) {
		Session session = super.getSession();
		String sql = getHaveSenMessSql(empId,null,null,true);
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getHaveSenMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from Message ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where " + tab + MSG_EMP_NO + "=" + empId + " and "
				+ tab + MSG_ISDEL + " = '1' and " + tab + MSG_ISSEND
				+ " = '1' ");
		if (!isCount) {
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"title",null,"meDate"};
				String [] cols = {MSG_TITLE,null,MSG_MEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+MSG_ID+" desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	/**
	 * 获得消息详情 <br>
	 * @param meCode 消息id
	 * @return Message 返回消息实体 
	 */
	public Message showMessInfo(Long meCode) {
		return (Message) super.getHibernateTemplate().get(Message.class, meCode);
	}
	/**
	 * 获得所有已收消息 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回消息列表 
	 */
	public List getAllMess(int pageCurrentNo, int pageSize,Long empId,String orderItem, String isDe) {
		Query query;
		Session session;
		session=super.getSession();
		String sql=getAllMessSql(empId,orderItem,isDe,false);
		query=session.createQuery(sql);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * 获得所有已收消息条数 <br>
	 * @param empId 账号
	 * @return int 返回消息数量 
	 */
	public int getAllMessCount(Long empId) {
		Query query;
		Session session;
		session=super.getSession();
		String sql = getAllMessSql(empId,null,null,true);
		query=session.createQuery(sql);
		int count=Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private String getAllMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from RMessLim ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+LIM_EMP_NO+"="+empId+" and "+tab+LIM_RML_ISDEL+"= '1' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"state","title","insUser","meDate"};
				String [] cols = {LIM_RML_STATE,LIM_MSG_TITLE,LIM_MSG_INSUSER,LIM_RML_DATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+LIM_RML_ID+" desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	/**
	 * 更新消息 <br>
	 * @param message 消息实体
	 */
	public void update(Message message) {
		super.getHibernateTemplate().update(message);
	}
	/**
	 * 获得已读或未读消息列表 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param isRead 是否已读
	 * @return List 返回消息列表 
	 */
	public List getIsReadMess(int pageCurrentNo, int pageSize, Long empId,String isRead)
	{
		Session session=super.getSession();
		String sql="select rmessLim from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRead order by rmlDate desc";
		Query query=session.createQuery(sql).setParameter("empId", empId).setParameter("isRead", isRead);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * 获得已读或未读消息的条数 <br>
	 * @param empId 员工id
	 * @param isRead 是否已读
	 * @return int 返回消息列表数量 
	 */
	public int getIsReadMessCount(Long empId,String isRead) {
		Session session=super.getSession();
		Query query=session.createQuery("select count(*) from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRead")
		.setParameter("empId", empId).setParameter("isRead", isRead);
		int count =Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	
	/**
	 * 
	 * 获得特定账号待发的消息 <br>
	 * create_date: Aug 6, 2010,12:05:49 PM <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 返回消息列表 
	 */
	public List getReadySenMess(int pageCurrentNo, int pageSize, Long empId, String orderItem, String isDe) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenMessSql(empId,orderItem,isDe,false);
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	/**
	 * 获得特定账号待发的消息的数量 <br>
	 * @param empId 员工id
	 * @return int 返回消息数量 
	 */
	public int getReadySenMessCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenMessSql(empId,null,null,true);
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getReadySenMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from Message ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+MSG_EMP_NO+"="+empId+" and "+tab+MSG_ISDEL+" = '1' and "+tab+MSG_ISSEND+" = '0' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"meTitle","meDate"};
				String [] cols = {MSG_TITLE,MSG_MEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab +MSG_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	}
	/**
	 * 获得已回或未回消息列表 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @param isRep 是否回复
	 * @return List 返回消息列表 
	 */
	public List getIsReplyMes(int pageCurrentNo, int pageSize, Long empId, String isRep){
		Session session=super.getSession();
		String sql="select rmessLim from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRep order by rmlDate desc";
		Query query=session.createQuery(sql).setParameter("empId", empId).setParameter("isRep", isRep);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * 获得已回或未回消息条数 <br>
	 * @param empId 员工id
	 * @param isRep 是否回复
	 * @return int 返回消息列表数量 
	 */
	public int getIsReplyCount(Long empId,String isRep){
		Session session=super.getSession();
		Query query=session.createQuery("select count(*) from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRep")
		.setParameter("empId", empId).setParameter("isRep", isRep);
		int count =Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	/**
	 * 删除消息 <br>
	 * @param message 消息实体
	 */
	public void delMess(Message message) {
		super.getHibernateTemplate().delete(message);
  	}
	/**
	 * 得到无用的消息 <br>
	 * @param empId 员工id
	 * @return List 返回消息列表 
	 */
	public List getNoUseMess(Long empId){
		Session session=super.getSession();
		String sql="select mes from Message as mes where salEmp.seNo=:empId and meIsdel='2'";
		Query query=session.createQuery(sql).setParameter("empId", empId);
		List list=query.list();
		return list;
	}
}