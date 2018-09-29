package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.NewsDAO;
import com.psit.struts.entity.News;
/**
 * 新闻公告DAO实现类 <br>
 */
public class NewsDAOImpl extends HibernateDaoSupport implements NewsDAO{
	public static final String TOTOP = "newIstop";
	public static final String NEWTYPE = "newType";
	public static final String NEWTITLE = "newTitle";
	public static final String NEW_INS_USER = "newInsUser";
//	public static final String SONAME = "news1.salEmp.salOrg.soName";
	public static final String NEWDATE = "newDate";
	/**
	 * 保存新闻公告 <br>
	 * @param news 新闻公告实体
	 */
	public void saveNews(News news) {
		super.getHibernateTemplate().save(news);
	}
	/**
	 * 获得最近已收新闻公告（无分页） <br>
	 * @param empId 员工id
	 * @param type 类型
	 * @return List 返回新闻公告列表 
	 */
	public List getAllNews(Long empId,String type) {
		Session session=super.getSession();
		String sql = "select news1 from News as news1 where newType in (:types) and newCode in(select news.newCode from RNewLim where salEmp.seNo="
				+ empId + ") order by newIstop,newDate desc";
		Query query = session.createQuery(sql).setString("types", type);
		query.setFirstResult(0);
		query.setMaxResults(15);
		List list=query.list();
		return list;
	}
	
	/**
	 * 获得所有已收新闻公告（有分页） <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回新闻公告列表 
	 */
	public List getAllNews(int pageCurrentNo, int pageSize,String orderItem,String isDe,Long empId) {
		Session session = (Session) super.getSession();
		String sql = getHql(empId, orderItem, isDe, false);
		Query query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int getAllNewsCount(Long empId) {
		Session session = (Session) super.getSession();
		String sql = getHql(empId, null, null, true);
		Query query = session.createQuery(sql);
        int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	public String getHql (Long empId,String orderItem,String isDe,boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from News ";
  		if(isCount){
  			sql = "select count(*) " + sql+" ";
  		}
		if(empId != null  && !empId.equals("")){
		   sql +=  "where newCode in(select news.newCode from RNewLim where salEmp.seNo="+empId+") ";
		
		}
		if(!isCount){
			appendSql.append("order by " + TOTOP+" , ");
			if(orderItem != null && !orderItem.equals("")){
				String[] items = {"newType","newTitle","newInsUser","newDate"};
				String cols[] = {NEWTYPE,NEWTITLE,NEW_INS_USER,NEWDATE};
				for(int i = 0; i < items.length; i++){
					if(orderItem.equals(items[i])){
						  orderItem = cols[i];
						}
				}
				appendSql.append(orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else {//默认排序
				appendSql.append("newCode desc ");
			}
		}
		return sql+appendSql.toString();
	}
	/**
	 * 根据新闻编号获得新闻公告 <br>
	 * @param newCode 新闻编号
	 * @return News 返回新闻实体 
	 */
	public News showNewsInfo(Long newCode) {
		return (News) super.getHibernateTemplate().get(News.class, newCode);
	}
	/**
	 * 获得所有已发的新闻公告 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @param empId 员工id
	 * @return List 返回新闻公告列表 
	 */
	public List getHaveSenNews(String orderItem, String isDe,int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = "from News ";
		StringBuffer appendSql = new StringBuffer("");
		appendSql.append("order by "+TOTOP+" , ");
		if(orderItem != null && !orderItem.equals("")){
			String[] items = {"newType","newTitle","newInsUser","newDate"};
			String cols[] = {NEWTYPE,NEWTITLE,NEW_INS_USER,NEWDATE};
			for(int i = 0; i < items.length; i++){
				if(orderItem.equals(items[i])){
					  orderItem = cols[i];
					}
			}
			appendSql.append(orderItem + " ");
			if(isDe!=null && isDe.equals("1")){
				appendSql.append("desc ");
			}
		}
		else {//默认排序
			appendSql.append("newCode desc ");
		}
		sql = sql + appendSql.toString();
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	/**
	 * 获得特定账号已发的新闻公告的数量 <br>
	 * @param empId 账号
	 * @return int 返回新闻公告列表数量 
	 */
	public int getHaveSenNewsCount() {
		Query query;
		Session session;
		session = super.getSession();
		query = session.createQuery("select count(*) from News ");
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 更新新闻公告信息 <br>
	 * @param news 新闻公告实体
	 */
	public void update(News news) {
		super.getHibernateTemplate().update(news);
	}
	/**
	 * 删除新闻公告 <br>
	 * @param news 新闻公告实体
	 */
	public void delNews(News news) {
		super.getHibernateTemplate().delete(news);
	}
	
	/**
	 * 根据新闻Id查找所有接收人 <br>
	 * @param newId 新闻Id
	 * @return List 返回接收人列表<br>
	 */
	public List findAllMsgMan(Long newId){
		try {
			String queryString = "from RNewLim where news.newCode="+newId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 清空新闻的接收人 <br>
	 * @param newId void 新闻Id<br>
	 */
	public void delRnewLim(Long newId){
		Session session = (Session) super.getSession();
		String sql = "delete r_new_lim where rnl_new_code = "+newId;
		session.createSQLQuery(sql).executeUpdate();
	}
}