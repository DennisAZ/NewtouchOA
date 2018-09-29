package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ReportDAO;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.Report;

/**
 * 报告表的数据库操作接口类 <br>
 */
public class ReportDAOImpl extends HibernateDaoSupport implements ReportDAO{
	
	public static final String REP_ID ="repCode";
	public static final String REP_TITLE = "repTitle";
	public static final String REP_DATE = "repDate";
	public static final String REP_ISSEND = "repIsSend";
	public static final String REP_ISDEL = "repIsdel";
	public static final String REP_EMP_NO = "salEmp.seNo";
	public static final String REP_TYPE_NAME = "repType.typName";
	
	public static final String RREP_ID = "rrlId";
	public static final String RREP_ISAPPRO = "rrlIsappro";
	public static final String RREP_ISDEL = "rrlIsdel";
	public static final String RREP_RRLDATE = "rrlDate";
	public static final String RREP_ISVIEW = "rrlIsView";
	public static final String RREP_EMP_NO = "salEmp.seNo";
	public static final String RREP_TYPE_NAME = "report.repType.typName";
	public static final String RREP_TITLE = "report.repTitle";
	public static final String RREP_REP_INSUSER = "report.repInsUser";
	/**
	 * 保存报告<br>
	 * @param report 报告表对象
	 */
	public void saveReport(Report report) {
		super.getHibernateTemplate().save(report);
	}
	/**
	 * 获得特定账号已提交的报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br>
	 */
	public List<Report> getHaveSenRep(int pageCurrentNo, int pageSize, Long empId,String orderItem, String isDe) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getHaveSenRepSql(empId,orderItem,isDe,false);
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Report> list = query.list();
		return list;
	}

	/**
	 * 获得特定账号已提交的报告的数量<br>
	 * @param empId 员工id
	 * @return int 报告数量<br>
	 */
	public int getHaveSenRepCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getHaveSenRepSql(empId,null,null,true);
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	private String getHaveSenRepSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql ="from Report ";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+REP_EMP_NO+"="+empId+" and "+tab+REP_ISSEND+"= '1' and "+tab+REP_ISDEL+"= '1' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"typName","repTitle","repDate"};
				String [] cols = {REP_TYPE_NAME,REP_TITLE,REP_DATE};
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
				appendSql.append("order by "+tab+REP_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		return sql;
	}
	/**
	 * 获得特定账号待发的报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br>
	 */
	public List getReadySenRep(int pageCurrentNo, int pageSize, Long empId,String orderItem, String isDe) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenRepSql(empId,orderItem,isDe,false);
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	public int getReadySenRepCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenRepSql(empId,null,null,true);
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getReadySenRepSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql = "from Report ";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+REP_EMP_NO+"="+empId+" and "+tab+REP_ISSEND+"= '0' and "+tab+REP_ISDEL+"= '1' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"typName","repTitle","repDate"};
				String [] cols = {REP_TYPE_NAME,REP_TITLE,REP_DATE};
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
				appendSql.append("order by "+tab+REP_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	}
	/**
	 * 根据报告的编号获得报告内容<br>
	 * @param repCode 报告id
	 * @return report 报告对象<br>
	 */
	public Report showReportInfo(Long repCode)
	{
		return (Report) super.getHibernateTemplate().get(Report.class, repCode);
	}

	/**
	 * 查看所有已收到的报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @return List 报告记录列表<br> 
	 */
	public List getAllReport(int pageCurrentNo, int pageSize, Long empId, String orderItem, String isDe) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getAllReportSql(empId,orderItem,isDe,false);
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int getAllReportCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getAllReportSql(empId,null,null,true);
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getAllReportSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql = "from RRepLim ";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+RREP_EMP_NO+"="+empId+" and "+tab+RREP_ISDEL+" = '1' and "+tab+RREP_ISVIEW+"= '1' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"rrlIsappro","typName","repTitle","rrlDate","repInsUser"};
				String [] cols = {RREP_ISAPPRO,RREP_TYPE_NAME,RREP_TITLE,RREP_RRLDATE,RREP_REP_INSUSER};
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
				appendSql.append("order by "+tab+RREP_ID+" desc ");
			}
		} 
		sql +=appendSql.toString();
		return sql;
	}
	/**
	 * 得到已审批的报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getAppReport(int pageCurrentNo, int pageSize, Long empId) {
		Session session = super.getSession();
		String sql = "select rrepLim  from RRepLim as rrepLim  where salEmp.seNo=:empId and rrlIsdel='1' and rrlIsappro='1' order by rrlDate desc";
		Query query = session.createQuery(sql).setParameter("empId", empId);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int getAppReportCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		query = session
				.createQuery(
						"select count(*) from RRepLim where salEmp.seNo=:empId and rrlIsappro='1' and rrlIsdel='1'")
				.setParameter("empId", empId);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 得到未审批的报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getNoAppReport(int pageCurrentNo, int pageSize, Long empId) {
		Session session = super.getSession();
		String sql = "select rrepLim  from RRepLim as rrepLim  where salEmp.seNo=:empId and rrlIsdel='1' and rrlIsappro='0' and rrlIsView='1' order by rrlDate desc";
		Query query = session.createQuery(sql).setParameter("empId", empId);
		//pageCurrentNo,pageSize<0为无分页列表
		if(pageSize>=0&&pageCurrentNo>=0){
			query.setFirstResult((pageCurrentNo - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		List list = (List) query.list();
		return list;
	}
	public int getNoAppReportCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		query = session
				.createQuery(
						"select count(*) from RRepLim where salEmp.seNo=:empId and rrlIsView='1' and rrlIsappro='0' and rrlIsdel='1'")
				.setParameter("empId", empId);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 得到无用报告<br>
	 * @param empId 员工id
	 * @return List 报告记录列表<br>
	 */
	public List getNoUseRep(Long empId){
		Session session=super.getSession();
		String sql="select rep from Report as rep where salEmp.seNo=:empId and repIsdel='2'";
		Query query=session.createQuery(sql).setParameter("empId", empId);
		List list=query.list();
		return list;
	}
	/**
	 * 更新报告<br>
	 * @param report 报告对象<br>
	 */
	public void update(Report report) {
		super.getHibernateTemplate().update(report);
	}

	/**
	 * 删除报告<br>
	 * @param report 报告对象<br>
	 */
	public void delReport(Report report) {
		super.getHibernateTemplate().delete(report);
	}
	
	/**
	 * 获得待删除的所有已发报告<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @return List 报告记录列表<br>
	 */
	public List findDelReport(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from Report where  repIsdel='0' order by repDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	
	/**
	 * 获得待删除的所有已发报告数量<br>
	 * @return int 报告数量<br>
	 */
	public int findDelReportCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from Report where  repIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
}