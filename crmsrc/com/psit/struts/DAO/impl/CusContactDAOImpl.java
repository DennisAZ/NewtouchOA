package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.psit.struts.DAO.CusContactDAO;
import com.psit.struts.entity.CusContact;
import com.psit.struts.util.format.StringFormat;
/**
 * 客户联系人DAO实现类 <br>
 */
public class CusContactDAOImpl extends HibernateDaoSupport implements CusContactDAO {
	private static final Log log = LogFactory.getLog(CusContactDAOImpl.class);
	//property constants
	public static final String CON_ID = "conId";
	public static final String CON_IS_CONS = "conIsCons";
	public static final String CON_NAME = "conName";
	public static final String CON_SEX = "conSex";
	public static final String CON_LEV = "conLev";
	public static final String CON_DEP = "conDep";
	public static final String CON_SERV = "conPos";
	public static final String CON_PHO = "conPhone";
	public static final String CON_WPHO = "conWorkPho";
	public static final String CON_EM = "conEmail";
	public static final String CON_QQ = "conQq";
	public static final String CON_CUS_ID = "cusCorCus.corCode";
	public static final String CON_CUS_NAME = "cusCorCus.corName";
	public static final String CON_EMP_ID = "salEmp.seNo";
	public static final String CON_EMP_NAME = "salEmp.seName";
	/**
	 * 保存客户联系人信息 <br>
	 * @param cusContact 联系人实体
	 */
	public void save(CusContact cusContact) {
		super.getHibernateTemplate().save(cusContact);
	}
	/**
	 * 根据联系人Id获取联系人 <br>
	 * @param id 联系人Id
	 * @return CusContact 返回联系人实体 
	 */
	public CusContact showContact(Long id) {
		return (CusContact) super.getHibernateTemplate().get(CusContact.class,id);
	}
	/**
	 * 更新联系人信息 <br>
	 * @param cusContact 联系人实体
	 */
	public void updateContact(CusContact cusContact) {
		this.getSession().merge(cusContact);
	}
	
	public void batchAssign(String ids, String seNo){
		Session session=super.getSession();
		String sql="";
    	if(!StringFormat.isEmpty(seNo)){
			sql = "update CusContact set "+CON_EMP_ID+"="+seNo+" where "+CON_ID+" in ("+ids+")";
		}
		else{
			sql = "update CusContact set "+CON_EMP_ID+" = NULL where "+CON_ID+" in ("+ids+")";
		}
    	session.createQuery(sql).executeUpdate();
	}
	
	 /**
	  * 删除联系人 <br>
	  * @param cusContact 联系人实体
	  */
	public void delContact(CusContact cusContact) {
		super.getHibernateTemplate().delete(cusContact);
	}
	
	/**
	 * 标记为收货人的客户联系人 <br>
	 * @param cusId 客户ID
	 * @return List<CusContact>
	 */
	 public List<CusContact> getAllConByMark(String cusId){
		Session session=super.getSession();
		String sql= "from CusContact where "+CON_CUS_ID+"="+cusId+" and "+CON_IS_CONS+"="+CusContact.M_CONS;
		Query query=session.createQuery(sql);
		List<CusContact> list=query.list();
		return list;
	 }
	
	/**
     * 获得某客户下的所有联系人 <br>
     * @param cusCode 客户id
     * @param orderItem 排序字段
     * @param isDe 是否逆序
     * @param currentPage 当前页数
     * @param pageSize 每页数量
     * @return List 返回联系人列表 
     */
	 public List getCusCon(String cusCode, String orderItem, String isDe, int currentPage,int pageSize){
		Session session=super.getSession();
		String sql= getCusCon(cusCode,orderItem,isDe,false);
		Query query=session.createQuery(sql);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	 }
	 /**
	  * 获得某客户下的所有联系人数量 <br>
	  * @param cusCode 客户id
	  * @return int 返回联系人列表数量 
	  */
	 public int getCusConCount(String cusCode){
		 Session session=super.getSession();
		 String sql=getCusCon(cusCode,null,null,true);
		 Query query=session.createQuery(sql);
		 int count=Integer.parseInt(query.uniqueResult().toString());
		 return count;
	 }
	 
	 private String getCusCon(String cusCode, String orderItem, String isDe, boolean isCount){
		 StringBuffer appendSql = new StringBuffer();
		 String sql =" from CusContact ";
		 String tab = "";
		 if(isCount){
			 sql = "select count(*) " + sql;
		 }
		 appendSql.append(" where "+tab+CON_CUS_ID+" = "+cusCode+" ");
		 if(!isCount){
			 if(orderItem!=null && !orderItem.equals("")){
				 String [] items = {"conName","conSex","conLev","conCus","conPos","conPhone","conWorkPho","conEmail","conQq"};
				 String [] cols = {CON_NAME,CON_SEX,CON_LEV,CON_CUS_NAME,CON_SERV,CON_PHO,CON_WPHO,CON_EM,CON_QQ};
				 for(int i=0;i<items.length;i++){
					 if(orderItem.equals(items[i])){
						 orderItem = cols[i];
						 break;
					 }
				 }
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			 }
			 else{
				appendSql.append("order by "+tab+CON_ID+" desc ");
			 }
		 }
		 sql +=appendSql.toString();
		 return sql;
	 }
	/**
	 * 联系人列表 <br>
	 * @param args 	[0]是否删除(1未删除,0已删除);  [1]范围(0自己,1全部);  [2]用户码(userNum);  
	 * 				[3]联系人名称;  [4]客户ID;  [5]联系人分类;  [6]负责人;  
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusContact> 联系人列表
	 */
	public List<CusContact> listContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getConSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusContact> list = query.list();
		return list;
	}
	public int listContactsCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getConSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getConSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "";//表更名
		String sql = "from CusContact ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
		if(args != null){
			if(args.length>1){
				if(args[0]!=null && !args[0].equals("")){
			    	if(args[0].equals("0")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+CON_EMP_ID +" = '"+args[1]+"' ");
			    	}
				}
				
			    if(args[2]!=null && !args[2].equals("")){ 
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+CON_NAME+" like :conName ");
			    }
			    if(args[3]!=null && !args[3].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+CON_CUS_NAME+" like :corName ");
			    }
			    if(args[4]!=null && !args[4].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ CON_LEV+" = '"+args[4]+"' ");
			    }
			    if(args[5]!=null && !args[5].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ CON_EMP_NAME+" like :uName ");
			    }
			}
			else{
				if(args[0]!=null && !args[0].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+CON_CUS_ID+" = "+args[0]+" ");
				}
			}

		}
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "conName", "conSex", "conLev", "conCus",
						"conServ", "conPho", "conWPho", "conEm", "conQQ","conDep"};
				String[] cols = { CON_NAME, CON_SEX, CON_LEV, CON_CUS_NAME,
						CON_SERV, CON_PHO, CON_WPHO, CON_EM, CON_QQ,CON_DEP };
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						orderItem = cols[i];
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//默认排序
				appendSql.append("order by "+tab+CON_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args.length>1){
				if(args[2]!=null && !args[2].equals("")){
					query.setString("conName", "%"+args[2]+"%");
				}
				if(args[3]!=null && !args[3].equals("")){
					query.setString("corName", "%"+args[3]+"%");
				}
				if(args[5]!=null && !args[5].equals("")){
					query.setString("uName", "%"+args[5]+"%");
				}
			}
		}
		return query;
	}
	
	/**
	 * 客户联系人列表 <br>
	 * @param args 	[0]客户Id;  [1]姓名;   
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<CusContact> 联系人列表
	 */
	public List<CusContact> listCusContacts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getCusConSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusContact> list = query.list();
		return list;
	}
	public int listCusContactsCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getCusConSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getCusConSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "";//表更名
		String sql = "from CusContact ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
		if(args != null){
			if(args.length>1){
				if(args[0]!=null && !args[0].equals("")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+CON_CUS_ID+" = "+args[0]+"  ");
				}
				
			    if(args[1]!=null && !args[1].equals("")){ 
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+CON_NAME+" like :conName ");
			    }
			}

		}
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "conName", "conSex", "conLev","conDep","conPos"};
				String[] cols = { CON_NAME, CON_SEX, CON_LEV, CON_DEP, CON_SERV};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						orderItem = cols[i];
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//默认排序
				appendSql.append("order by "+tab+CON_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
				if(args[1]!=null && !args[1].equals("")){
					query.setString("conName", "%"+args[1]+"%");
				}
		}
		return query;
	}
	
	/**
	 * 获得最近十天关注的联系人 <br>
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param range 标识查询范围0表示查询我的客户1表示查询我的下属客户
	 * @param userCode 账号
	 * @param userNum 用户码
	 * @return List 返回联系人列表
	 */
	public List getContactByBirth(Date startDate, Date endDate,String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  cusContact from CusContact as cusContact where " +
			"salEmp.seNo ='"+seNo+"'"
			+ " and conBir between :startDate and :endDate order by conBir asc";
	    }
	    else{
	    	sql = "select  cusContact from CusContact as cusContact where " 
			+ " conBir between :startDate and :endDate order by conBir asc";
	    }
		query = session.createQuery(sql).setDate("startDate", startDate)
				.setDate("endDate", endDate);
		List list = (List)query.list();
		return list;
	}
	public int getContactByBirthCount(Date startDate, Date endDate,
			String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  count(*) from CusContact  where " +
			"salEmp.seNo ='"+seNo+"'"
			+ " and conBir between :startDate and :endDate";
	    }
	    else if(range!=null&&range.equals("1")){
	    	sql = "select  count(*) from CusContact where conBir between :startDate and :endDate";
	    }
	    else{
	    	sql = "select  count(*) from CusContact where conBir between :startDate and :endDate";
	    }
		query = session.createQuery(sql)
				.setDate("startDate", startDate).setDate("endDate", endDate);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 获得删除状态为0的所有联系人 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回联系人列表
	 */
	/*public List findDelContact(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from CusContact where  conIsDel='0' order by conCreDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int findDelContactCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from CusContact where  conIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}*/
	
	/**
	 * 获得要导出的联系人列表<br>
	 * @param args 	[0]范围(0自己,1全部);  [1]用户码(userNum);  
	 * 				[2]联系人名称;  [3]客户;  [4]联系人分类;  [5]负责人;  
	 * @return List<CusContact> 联系人列表<br>
	 */
	public List<CusContact> getOutCont(String [] args){
		Session session = (Session)super.getSession();
		Query query = getConSql(session,args,null,null,false);
		List<CusContact> list = query.list();
		return list;
	}
	/**
	 * 通过Id获得联系人列表<br>
	 * @param ids 联系人Id
	 * @return List<CusContact> 联系人列表<br>
	 */
	public List<CusContact> getContByIds(String ids){
		Session session = (Session)super.getSession();
		String id = ids.substring(0, ids.length()-1);
		StringBuffer appendSql = new StringBuffer();
		String tab = "con.";
		String sql = "select con from CusContact con where ";
		appendSql.append(tab+CON_ID+" in ("+id+") ");
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		List<CusContact> list = query.list();
		return list;
	}
}