package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusServDAO;
import com.psit.struts.entity.CusServ;
/**
 * 客户服务DAO实现类 <br>
 */
public class CusServDAOImpl extends HibernateDaoSupport implements CusServDAO{
	public static final String SERV_ID = "serCode";
	public static final String SERV_ISDEL = "serIsDel";
	public static final String SERV_TITLE = "serTitle";
	public static final String SERV_STATE = "serState";
	public static final String SERV_METHOD = "serMethod";
	public static final String SERV_EXEDATE = "serExeDate";
	public static final String SERV_CUS = "cusCorCus";
	public static final String SERV_CUS_ID = "cusCorCus.corCode";
	public static final String SERV_CUS_NAME = "cusCorCus.corName";
	public static final String SERV_EMP_NAME = "salEmp.seName";
	public static final String SERV_EMP ="salEmp";
	public static final String SERV_USER_NUM = "limUser.userNum";
	public static final String SERV_CONTENT = "serContent";
	
	/**
	 * 保存客户服务信息 <br>
	 * @param cusServ 客户服务实体
	 */
	public void save(CusServ cusServ) {
		super.getHibernateTemplate().save(cusServ);
	}
	/**
	 * 根据客户服务ID获取客户服务 <br>
	 * @param serCode 客户服务id
	 * @return CusServ  返回客户服务实体 
	 */
	public CusServ showCusServ(Long serCode) {
		return (CusServ) super.getHibernateTemplate().get(CusServ.class, serCode);
	}
	/**
	 * 更新客户服务信息 <br>
	 * @param cusServ 客户服务实体
	 */
	public void update(CusServ cusServ) {
		super.getHibernateTemplate().update(cusServ);
	}
	 /**
	  * 删除客户服务 <br>
	  * @param cusServ 客户服务实体
	  */
	public void delServ(CusServ cusServ) {
		super.getHibernateTemplate().delete(cusServ);
	}
	 /**
	  * 获得待删除的所有客户服务 <br>
	  * @param pageCurrentNo 当前页数
	  * @param pageSize 每页数量
	  * @return List 返回客户服务列表 
	  */
	public List findDelServ(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from CusServ where  serIsDel='0' order by serInsDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	 /**
	  * 获得待删除的所有客户服务数量 <br>
	  * @return int 返回客户服务列表数量
	  */
	public int findDelServCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from CusServ where  serIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * 获得某客户下的所有客户服务<br>
	 * @param cusCode 客户Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage  当前页数
	 * @param pageSize 每页数量
	 * @return list 返回客户服务列表
	 */
	public List getCusServ(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		Session session=super.getSession();
		String sql1= getCusServSql(cusCode,orderItem,isDe,false);
		Query query=session.createQuery(sql1);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * 获得某客户下的所有客户服务数量
	 */
	public int getCusServCount(Long cusCode){
		Session session=super.getSession();
		 String sql1=getCusServSql(cusCode,null,null,true);
		 Query query=session.createQuery(sql1);
		 int count=Integer.parseInt(query.uniqueResult().toString());
		 return count;
	}
	
	private String getCusServSql(Long cusCode, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql = "from CusServ ";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append(" where "+tab+SERV_ISDEL+" = '1' and "+tab+SERV_CUS_ID+" = "+cusCode+" ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"serState","serTitle","cName","serMethod","serContent","seName","exeDate"};
				String [] cols = {SERV_STATE,SERV_TITLE,SERV_CUS_NAME,SERV_METHOD,SERV_CONTENT,SERV_EMP_NAME,SERV_EXEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append(" order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else{
				appendSql.append(" order by "+tab+SERV_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	}
	 /**
	  * 客服列表<br>
	  * @param args
	  *			    args[0]: 是否删除
	  * 			args[1]: 0:待处理 2：已处理
	  * 			args[2]: 主题
	  * 			args[3]: 客服Id
	  * 			args[4]: 状态
	  * 			args[5]: 关怀方式
	  * 			args[6]: 开始执行日期
	  * 			args[7]: 终止执行日期
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	  * @return List<CusServ> 
	  */
	 public List<CusServ> getServ(String[]args, String orderItem,String isDe, int currentPage, int pageSize){
		 Session session = (Session)super.getSession();
		 Query query = getServSql(session, args, orderItem, isDe,false);
		 query.setFirstResult((currentPage - 1) * pageSize);
		 query.setMaxResults(pageSize);
		 List<CusServ> list = query.list();
		 return list;
	 }
	 public int getServCount(String [] args){
		 Session session = (Session) super.getSession();
		 Query query = getServSql(session,args,null,null,true);
		 int count = Integer.valueOf(query.uniqueResult().toString());
		 return count;
	 }
	 private Query getServSql(Session session, String [] args, String orderItem, String isDe
			 ,boolean isCount){
		 StringBuffer appendSql = new StringBuffer();
		 String tab = "cs.";//表更名
		 String sql = "from CusServ cs ";
		 if(isCount){
			 sql ="select count(*) " + sql;
		 }
		 else {
			 sql ="select cs " + sql;
		 }
		 if(args!=null){
			 if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+SERV_ISDEL+" = '"+args[0]+"' ");
			 }
			 if(args.length>2){
				 if(args[1]!=null && !args[1].equals("")){
				    	if(args[1].equals("0")){
				    		appendSql.append(appendSql.length()==0?"where ":"and ");
				    		appendSql.append(tab+SERV_STATE +" = '待处理' ");
				    	}
				    	else {
				    		appendSql.append(appendSql.length()==0?"where ":"and ");
				    		appendSql.append(tab+SERV_STATE +" = '已处理' ");
				    	}
				 }
				 if(args[2]!=null && !args[2].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_EMP_NAME +" like :seName ");
				 }
				 if(args[3]!=null && !args[3].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_CUS_NAME +" like :corName "); 
				 }
				 if(args[4]!=null && !args[4].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_STATE +" = '"+args[4]+"' ");	 
				 }
				 if(args[5]!=null && !args[5].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_METHOD +" = '"+args[5]+"' "); 
				 }
			    if(args[6]!=null){
			    	if (!args[6].equals("") && args[7].equals("")) {
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+SERV_EXEDATE+" >= '"+ args[6] + "' ");
					} else if (!args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + SERV_EXEDATE + " between '"
								+ args[6] + "' and '"+args[7]+ "' ");
					} else if (args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + SERV_EXEDATE + " <= '"+ args[7]+ "' ");
					}
			    }
			    if(args[8]!=null && !args[8].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_TITLE +" like :serTitle "); 
			    }
			 }
			 else{
				 if(args[1]!=null && !args[1].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_CUS_ID+" = "+args[1]+" "); 
				 }
			 }
		 }
		 if(!isCount){
			if (orderItem != null && !orderItem.equals("")) {
				if(args.length>2){
					String[] items = { "title","customer","mothed","content","seName","exeDate"};
					String[] cols = { SERV_TITLE, SERV_CUS_NAME, SERV_METHOD, SERV_CONTENT ,SERV_EMP_NAME, SERV_EXEDATE};
					for (int i = 0; i < items.length; i++) {
						if (orderItem.equals(items[i])) {
							switch(i){
							case 4:
								sql+=" left join "+tab+SERV_EMP +" ";
								break;
							}
							orderItem = cols[i];
							break;
						}
					}
				}
				else{
					String[] items1 = { "state","title","customer","mothed","content","seName","exeDate"};
					String[] cols1 = { SERV_STATE,SERV_TITLE, SERV_CUS_NAME, SERV_METHOD, SERV_CONTENT ,SERV_EMP_NAME, SERV_EXEDATE};
					for (int i = 0; i < items1.length; i++) {
						if (orderItem.equals(items1[i])) {
							switch(i){
							case 5:
								sql+=" left join "+tab+SERV_EMP +" ";
								break;
							}
							orderItem = cols1[i];
							break;
						}
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//默认排序
				appendSql.append("order by "+tab+SERV_ID+" desc ");
			}
		 }
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args.length>2){
				if(args[2]!=null && !args[2].equals("")){
					query.setString("seName", "%"+args[2]+"%");
				}
				if(args[3]!=null && !args[3].equals("")){
					query.setString("corName", "%"+args[3]+"%");
				}
				if(args[8]!=null && !args[8].equals("")){
					query.setString("serTitle", "%"+args[8]+"%");
				}
			}
		}
		return query;
	 }
}