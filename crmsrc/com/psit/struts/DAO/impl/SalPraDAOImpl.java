package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalPraDAO;
import com.psit.struts.entity.SalPra;

/**
 * 来往记录数据库表实现类 <br>
 */
public class SalPraDAOImpl extends HibernateDaoSupport implements SalPraDAO {
	public static final String PRA_ID = "praId";
	public static final String PRA_ISDEL = "praIsDel";
	public static final String PRA_TITLE = "praTitle";
	public static final String PRA_TYPE = "praType";
	public static final String PRA_CONT_TYPE = "praContType";
	public static final String PRA_EXE_DATE = "praExeDate";
	public static final String PRA_INSDATE = "praInsDate";
	public static final String PRA_EMP_NO = "salEmp.seNo";
	public static final String PRA_REMARK = "praRemark";
	public static final String PRA_COST = "praCost";
	public static final String PRA_CUS_ID = "cusCorCus.corCode";
	public static final String PRA_CUS_COR_CUS = "cusCorCus";
	public static final String PRA_CUS_NAME = "cusCorCus.corName";
	public static final String PRA_EMP_NAME = "salEmp.seName";
	public static final String PRA_CON_NAME = "cusContact.conName";
	public static final String PRA_CUS_CONTACT = "cusContact";
	public static final String PRA_CON_ID = "cusContact.conId";
	
	/**
	 * 来往记录列表<br>
	 * @param args
	  *            0：是否删除
	  *            1：0:我的客户 2：全部客户
	  *            2：负责人Id
	  *            3：客户Id
	  *            4：摘要
	  *            5：状态
	  *            6：方式
	  *            7：执行日期（起始）
	  *            8：执行日期（终止）
	  *            9：负责账号
	  *            10：过滤器
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<SalOpp> 机会列表
	 */
	public List<SalPra> listSalPras(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getPraSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalPra> list = query.list();
		return list;
	}
	public int listSalPrasCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getPraSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getPraSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "sal.";//表更名
		String sql = "from SalPra sal ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select sal "+sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+PRA_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args.length>3){
				if(args[1]!=null && !args[1].equals("")){
					if(args[1].equals("0")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+PRA_EMP_NO +" = '"+args[2]+"' ");
					}
				}
			    if(args[3]!=null && !args[3].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+PRA_CUS_NAME+" like :corName ");
			    }
			    if(args[4]!=null && !args[4].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ PRA_REMARK+" like :praRemark ");
			    }
			    if(args[5]!=null && !args[5].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ PRA_TYPE+" = '"+args[5]+"' ");
			    }
			    if(args[6]!=null){
			    	if (!args[6].equals("") && args[7].equals("")) {
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+PRA_EXE_DATE+" >= '"+ args[6] + "' ");
					} else if (!args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + PRA_EXE_DATE + " between '"
								+ args[6] + "' and '"+args[7]+ "' ");
					} else if (args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + PRA_EXE_DATE + " <= '"+ args[7]+ "' ");
					}
			    }
			    if(args[8]!=null && !args[8].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+PRA_EMP_NAME +" like :uName ");
			    }
			}else if(args.length ==3){
				if(args[1]!=null && !args[1].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+PRA_CON_ID+" = "+args[1]+" ");
				}
			}
			else{
				if(args[1]!=null && !args[1].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+PRA_CUS_ID+" = "+args[1]+" ");
				}
			}
	    }
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = {"praType", "praRemark", "customer", "praExeDate", "conName","seName", "praInsDate","praContType","cost"};
				String[] cols = {PRA_TYPE,PRA_REMARK,PRA_CUS_NAME,PRA_EXE_DATE,PRA_CON_NAME,PRA_EMP_NAME,PRA_INSDATE,PRA_CONT_TYPE,PRA_COST};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch(i){
						case 2:
							sql +="left join "+tab +PRA_CUS_COR_CUS+" ";
							break;
						case 4:
							sql +="left join "+tab+PRA_CUS_CONTACT+" ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//默认排序
				appendSql.append("order by "+tab+PRA_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args.length>3){
				if(args[3]!=null && !args[3].equals("")){
					query.setString("corName", "%"+args[3]+"%");
				}
				if(args[4]!=null && !args[4].equals("")){
					query.setString("praRemark", "%"+args[4]+"%");
				}
				if(args[8]!=null && !args[8].equals("")){
					query.setString("uName", "%"+args[9]+"%");
				}
			}
		}
		return query;
	}
	/**
	 * 获得最近来往记录<br>
	 * @param startDate 执行日期开始
	 * @param endDate 执行日期结束
	 * @param range 标识查询范围0表示查询我的来往记录1表示查询我的下属来往记录2表示查询全部来往记录
	 * @param userCode 账号
	 * @param userNum 用户id及其下属
	 * @return List 来往记录列表<br>
	 */
	public List getPraByExeDate(Date startDate, Date endDate,String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  salPra from SalPra as salPra where " +
			"salEmp.seNo ='"+seNo+"'"
			+ " and praIsDel='1' and praExeDate between :startDate and :endDate and praState='待联系' order by praExeDate asc";
	    }/*
	    else if(range!=null&&range.equals("1")){
	    	sql = "select  salPra from SalPra as salPra where limUser.userCode!='" +
			userCode+"' and limUser.userNum like '%"+userNum+"'"
			+ " and praIsDel='1' and praExeDate between :startDate and :endDate and praState='未执行' order by praExeDate asc";
	    }*/
	    else{
	    	sql = "select  salPra from SalPra as salPra where " 
			+ "praIsDel='1' and praExeDate between :startDate and :endDate and praState='待联系' order by praExeDate asc";
	    }
		query = session.createQuery(sql).setDate("startDate", startDate)
				.setDate("endDate", endDate);
		// this.query.setFirstResult((pageCurrentNo - 1) * pageSize);
		// this.query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	public int getPraByExeDateCount(Date startDate, Date endDate,String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  count(*) from SalPra as salPra where " +
			"salEmp.seNo='"+seNo+"'"
			+ " and praIsDel='1' and praExeDate between :startDate and :endDate and praState='待联系'";
	    }/*
	    else if(range!=null&&range.equals("1")){
	    	sql = "select  count(*) from SalPra as salPra where limUser.userCode!='" +
			userCode+"' and limUser.userNum like '%"+userNum+"'"
			+ " and praIsDel='1' and praExeDate between :startDate and :endDate and praState='未执行'";
	    }*/
	    else{
	    	sql = "select  count(*) from SalPra as salPra where " 
			+ "praIsDel='1' and praExeDate between :startDate and :endDate and praState='待联系'";
	    }
		query = session.createQuery(sql)
				.setDate("startDate", startDate).setDate("endDate", endDate);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 获得待删除的所有来往记录<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页记录数
	 * @return List 来往记录列表<br>
	 */
	public List findDelPra(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from SalPra where  praIsDel='1' order by praId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int findDelPraCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from SalPra where  praIsDel='1'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	 /**
	  * 获得某客户下的所有来往记录<br>
	  * @param cusCode 客户id
	  * @param orderItem 排序字段
	  * @param isDe 是否逆序
	  * @param currentPage 当前页码
	  * @param pageSize 每页显示的记录数
	  * @return List 来往记录列表<br>
	  */
	 public List getCusPra(Long cusCode,String orderItem, String isDe,int currentPage,int pageSize){
		 Session session=super.getSession();
		 String sql1=getCusPraSql(cusCode,orderItem,isDe,false);
		 Query query=session.createQuery(sql1);
		 query.setFirstResult((currentPage-1)*pageSize);
		 query.setMaxResults(pageSize);
		 List list=query.list();
		 return list;
	 }
	 /**
	  * 获得某客户下的所有来往记录数量<br>
	  * @param cusCode 客户id
	  * @return int 来往记录数量<br>
	  */
	 public int getCusPraCount(Long cusCode){
		 Session session=super.getSession();
		 String sql1=getCusPraSql(cusCode,null,null,true);
		 Query query=session.createQuery(sql1);
		 int count=Integer.parseInt(query.uniqueResult().toString());
		 return count;
	 }
	 private String getCusPraSql(Long cusCode, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql = "from SalPra ";
		if(isCount){
			sql = "select count(*) "+sql;
		} 
		appendSql.append(" where "+tab+PRA_ISDEL+" = '1' and "+tab+PRA_CUS_ID+" = "+cusCode+" ");
		if(!isCount){
			if(orderItem!=null&& !orderItem.equals("")){
				String [] items = {"praType","praRemark","customer","praExeDate","seName","praInsDate"};
				String [] cols = {PRA_TYPE,PRA_REMARK,PRA_EMP_NAME,PRA_EXE_DATE,PRA_EMP_NAME,PRA_INSDATE};
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
				appendSql.append(" order by "+tab+PRA_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	 }
	
	/**
	 * 保存来往记录信息<br>
	 * @param salPra 来往记录对象<br>
	 */
	public void save(SalPra salPra) {
		super.getHibernateTemplate().save(salPra);

	}

	/**
	 * 根据Id获取来往记录<br>
	 * @param id 来往记录id
	 * @return SalPra 来往记录对象<br>
	 */
	public SalPra showSalPra(Long id) {
		return (SalPra) super.getHibernateTemplate().get(SalPra.class, id);
	}

	/**
	 * 更新来往记录信息<br>
	 * @param salPra 来往记录对象<br>
	 */
	
	public void update(SalPra salPra) {
		super.getHibernateTemplate().merge(salPra);

	}

	/**
	 * 删除来往记录<br>
	 * @param salPra 来往记录对象<br>
	 */
	public void delPra(SalPra salPra) {
		super.getHibernateTemplate().delete(salPra);

	}
}