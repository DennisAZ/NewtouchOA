package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.Iterator;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusCorCusDAO;
import com.psit.struts.entity.CrmManagementAreaRange;
import com.psit.struts.entity.CusContact;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.form.RecvAmtForm;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.OperateList;
import com.psit.struts.util.format.StringFormat;
/**
 */
public class CusCorCusDAOImpl extends HibernateDaoSupport implements CusCorCusDAO{
	//property constants
	static final String CUS_ID = "corCode";
	static final String CUS_ISDEL = "corIsdelete";
	
	static final String CUS_AREID = "cusArea.areId";
	static final String CUS_PRVID = "cusProvince.prvId";
	static final String CUS_CITYID = "cusCity.cityId";
	
	static final String CUS_NUM = "corNum";
	static final String CUS_NAME = "corName";
	static final String CUS_HOT = "corHot";
	static final String CUS_MNE = "corMne";
	static final String CUS_STATE = "corState";
	static final String CUS_COLOR = "corColor";
	static final String CUS_PER_SIZE = "corPerSize";
	static final String CUS_CRE_LIM = "corRelation";
	static final String CUS_RECV_AMT = "corRecvAmt";
	static final String CUS_REC_DATE = "corRecDate";
	static final String CUS_PHO = "corPhone";
	static final String CUS_CELL_PHO = "corCellPhone";
	static final String CUS_COR_FEX = "corFex";
	static final String CUS_ADD = "corAddress";
	static final String CUS_LAST_DATE = "corLastDate";
	static final String CUS_UPD_DATE = "corUpdDate";
	static final String CUS_TAG = "corTempTag";
	static final String CUS_REMARK = "corRemark";
	static final String CUS_CRE_DATE = "corCreatDate";
	static final String CUS_EMP_ID = "person.seqId";
	static final String CUS_EMP_NAME = "person.userName";
	static final String CUS_EMP = "person";
	static final String CUS_IND = "cusIndustry";
	static final String CUS_IND_NAME = "cusIndustry.typName";
	static final String CUS_SOU = "corSou";
	static final String CUS_SOU_NAME = "corSou.typName";
	static final String CUS_AREA_ID = "cusArea.areId";
	static final String CUS_TYPE = "cusType";
	static final String CUS_TYPE_ID = "cusType.typId";
	static final String CUS_TYPE_NAME = "cusType.typName";
	static final String CUS_INDUSTRY_ID = "cusIndustry.typId";
	static final String CUS_SOU_ID = "corSou.typId";
	static final String CUS_PROV_ID = "cusProvince.prvId";
	static final String CUS_CITY_ID  = "cusCity.cityId";
	static final String CON_CUS_ID = "cusCorCus.corCode";
	static final String CON_EMP_ID = "salEmp.seNo";
	static final String COR_ON_DATE = "corOnDate";
	static final String COR_TICKET_NUM = "corTicketNum";
	
	static final String CUS_ID_DB = "cor_code";
	static final String CUS_NAME_DB = "cor_name";
	static final String CUS_SALE_NUM_DB = "cor_sale_num";
	static final String CON_EMP_ID_DB = "cor_se_no";
	static final String COR_RECV_AMT_DB = "cor_recv_amt";
	static final String COR_RECV_MAX_DB = "cor_recv_max";
	static final String COR_TICKET_NUM_DB = "cor_ticket_num";
	static final String COR_BEGIN_BAL_DB = "cor_begin_bal";
	static final String COR_ON_DATE_DB = "cor_on_date";

	
	/**
	 * 获取自己管辖范围的内的区域
	 */
	public List<CrmManagementAreaRange> getmyManageCrmManagementArea(long person_id) {
		String HQL = "select b from CrmManagementArea a,CrmManagementAreaRange b where a.Id=b.ma_id and a.person_id="+person_id+" order by b.Id";
		List<CrmManagementAreaRange> list =  super.getHibernateTemplate().find(HQL);
		return list;
	}
	
	public List<CusCorCus> listCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getCusSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list = query.list();
		return list;
	}
	public int listCustomersCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getCusSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public List<CusCorCus> getCustomersByArea(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getCustomersByAreaSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list = query.list();
		return list;
	}
	
	public List<CusCorCus> mylistCustomers(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getMyManageCusSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list = query.list();
		return list;
	}
	public int mylistCustomersCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getMyManageCusSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getCusSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "cus.";
		String sql = "from CusCorCus cus ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select cus "+sql;
  		}
		if(args != null){
			if(!StringFormat.isEmpty(args[0])){
		    	appendSql.append("where ");
		    	appendSql.append(tab+CUS_ISDEL+" = '"+args[0]+"' ");
		    }
			if(!StringFormat.isEmpty(args[1])){
		    	if(args[1].equals("0")&& args.length>5 && !StringFormat.isEmpty(args[13]) && args[13].equals(Integer.toString(CusCorCus.S_DEVED))){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+CUS_EMP_ID +" = "+args[2]+" ");
		    	}
		    	/*else if(args[0].equals("1")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+CUS_USER_NUM +" like '%"+args[1]+"' ");
		    	}*/
			}
		    if(!StringFormat.isEmpty(args[3])){
		    	appendSql.append(appendSql.length()==0?"where ":"and ");
		    	appendSql.append(tab+CUS_NAME+" like :cName ");
		    }
		    if(!StringFormat.isEmpty(args[4])){
		    	appendSql.append(appendSql.length()==0?"where ":"and ");
		    	appendSql.append(tab+CUS_NUM+" like :cNum ");
		    }
		    if(args.length>5){
			    if(!StringFormat.isEmpty(args[5])){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ CUS_TYPE_ID+" = "+args[5]+" ");
			    }
			    if(args[6]!=null){
			    	if (!args[6].equals("") && args[7].equals("")) {
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+CUS_CRE_DATE+" >= '"+ args[6] + "' ");
					} else if (!args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " between '"
								+ args[6] + "' and '"
								+ GetDate.parseStrDate(GetDate.getDate(args[7], 1))
								+ "' ");
					} else if (args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " <= '"
								+ GetDate.parseStrDate(GetDate.getDate(args[7], 1))
								+ "' ");
					}
			    }
		    	if(!StringFormat.isEmpty(args[8])){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+ CUS_EMP_NAME +" like :seName ");
		    	}
				if(!StringFormat.isEmpty(args[9])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_ADD +" like :corAdd ");
				}
				if(!StringFormat.isEmpty(args[10])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_INDUSTRY_ID+"="+args[10]+" ");
				}
				if(!StringFormat.isEmpty(args[11])){
					String [] filter = {"all","tag1","none","date7","date15","date30"};
					Date lastDateCon = new Date();
					Date startDateCon = new Date();
					Date endDateCon = new Date();
					if(args[11].equals("date7")){
						startDateCon = OperateDate.getDate(new Date(), -14);
						endDateCon = OperateDate.getDate(new Date(), -6);
					}else if(args[11].equals("date15")){
						startDateCon = OperateDate.getDate(new Date(), -29);
						endDateCon = OperateDate.getDate(new Date(), -15);
					}else if(args[11].equals("date30")){
						lastDateCon = OperateDate.getDate(lastDateCon, -30);
					}
					for(int i=0;i<filter.length;i++){
						if(args[11].equals(filter[i])){
							switch(i){
								case 0:
									appendSql.append(appendSql.length()==0?"where ":"and ");
									appendSql.append(tab+CUS_EMP_ID+" is null ");
									break;
								case 1:
									appendSql.append(appendSql.length()==0?"where ":"and ");
									appendSql.append(tab+CUS_TAG+" = '1' ");
									break;
								case 2:
									appendSql.append(appendSql.length()==0?"where ":"and ");
									appendSql.append(tab+CUS_LAST_DATE+" is null ");
									break;
								case 3:
								case 4:
									appendSql.append(appendSql.length()==0?"where ":"and ");
									appendSql.append(tab+CUS_LAST_DATE+" between "+"'"+new java.sql.Date(startDateCon.getTime())+"'"+" and "+"'" +new java.sql.Date(endDateCon.getTime())+"' ");
									break;
								case 5:
									appendSql.append(appendSql.length()==0?"where ":"and ");	
									appendSql.append(tab+CUS_LAST_DATE+" <= "+ "'" + new java.sql.Date(lastDateCon.getTime())+ "' ");
									break;
							}
							break;
						}
					}
				}
				if(!StringFormat.isEmpty(args[12])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_COLOR+" = '"+args[12]+"' ");
				}
				if(!StringFormat.isEmpty(args[13])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_STATE+" = "+args[13]+" ");
				}
		    }
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[3]!=null && !args[3].equals("")){
				query.setString("cName", "%"+args[3]+"%");
			}
			if(args[4]!=null && !args[4].equals("")){
				query.setString("cNum","%"+args[4]+"%");
			}
			if(args.length>5){
				if(args[8]!=null && !args[8].equals("")){
					query.setString("seName", "%"+args[8]+"%");
				}
				if(args[9]!=null && !args[9].equals("")){
					query.setString("corAdd", "%"+args[9]+"%");
				}
			}
		}
		return query;
	}
	
	/**
	 * 
	 * @param session
	 * @param args
	 * @param orderItem
	 * @param isDe
	 * @param isCount
	 * @return
	 */
	private Query getMyManageCusSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "cus.";
		String sql = "from CusCorCus cus ";
		if(isCount){
			sql = "select count(*) " + sql;
		}
		else{
			sql = "select cus "+sql;
		}
		if(args != null){
			if(!StringFormat.isEmpty(args[0])){
				appendSql.append("where ");
				appendSql.append(tab+CUS_ISDEL+" = '"+args[0]+"' ");
			}
			if(!StringFormat.isEmpty(args[1])){
				if(args[1].equals("0")&& args.length>5 && !StringFormat.isEmpty(args[13]) && args[13].equals(Integer.toString(CusCorCus.S_DEVED))){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_EMP_ID +" = "+args[2]+" ");
				}
				/*else if(args[0].equals("1")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+CUS_USER_NUM +" like '%"+args[1]+"' ");
		    	}*/
			}
			
			if(!StringFormat.isEmpty(args[2])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_AREID+"="+args[2]+" ");
			}
			if(!StringFormat.isEmpty(args[3])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_PRVID+"="+args[3]+" ");
			}
			if(!StringFormat.isEmpty(args[4])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_CITYID+"="+args[4]+" ");
			}
			
			if(!StringFormat.isEmpty(args[5])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_NAME+" like :cName ");
			}
			if(!StringFormat.isEmpty(args[6])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_NUM+" like :cNum ");
			}
			if(args.length>5){
				if(!StringFormat.isEmpty(args[7])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_TYPE_ID+" = "+args[7]+" ");
				}
				if(args[8]!=null){
					if (!args[8].equals("") && args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab+CUS_CRE_DATE+" >= '"+ args[8] + "' ");
					} else if (!args[8].equals("") && !args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " between '"
								+ args[8] + "' and '"
								+ GetDate.parseStrDate(GetDate.getDate(args[9], 1))
								+ "' ");
					} else if (args[8].equals("") && !args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " <= '"
								+ GetDate.parseStrDate(GetDate.getDate(args[9], 1))
								+ "' ");
					}
				}
				if(!StringFormat.isEmpty(args[10])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_EMP_NAME +" like :seName ");
				}
				if(!StringFormat.isEmpty(args[11])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_ADD +" like :corAdd ");
				}
				if(!StringFormat.isEmpty(args[12])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_INDUSTRY_ID+"="+args[12]+" ");
				}
				if(!StringFormat.isEmpty(args[13])){
					String [] filter = {"all","tag1","none","date7","date15","date30"};
					Date lastDateCon = new Date();
					Date startDateCon = new Date();
					Date endDateCon = new Date();
					if(args[13].equals("date7")){
						startDateCon = OperateDate.getDate(new Date(), -14);
						endDateCon = OperateDate.getDate(new Date(), -6);
					}else if(args[13].equals("date15")){
						startDateCon = OperateDate.getDate(new Date(), -29);
						endDateCon = OperateDate.getDate(new Date(), -15);
					}else if(args[13].equals("date30")){
						lastDateCon = OperateDate.getDate(lastDateCon, -30);
					}
					for(int i=0;i<filter.length;i++){
						if(args[11].equals(filter[i])){
							switch(i){
							case 0:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_EMP_ID+" is null ");
								break;
							case 1:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_TAG+" = '1' ");
								break;
							case 2:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_LAST_DATE+" is null ");
								break;
							case 3:
							case 4:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_LAST_DATE+" between "+"'"+new java.sql.Date(startDateCon.getTime())+"'"+" and "+"'" +new java.sql.Date(endDateCon.getTime())+"' ");
								break;
							case 5:
								appendSql.append(appendSql.length()==0?"where ":"and ");	
								appendSql.append(tab+CUS_LAST_DATE+" <= "+ "'" + new java.sql.Date(lastDateCon.getTime())+ "' ");
								break;
							}
							break;
						}
					}
				}
				if(!StringFormat.isEmpty(args[14])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_COLOR+" = '"+args[14]+"' ");
				}
//				if(!StringFormat.isEmpty(args[15])){
//					appendSql.append(appendSql.length()==0?"where ":"and ");
//					appendSql.append(tab+CUS_STATE+" = "+args[15]+" ");
//				}
			}
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[5]!=null && !args[5].equals("")){
				query.setString("cName", "%"+args[5]+"%");
			}
			if(args[6]!=null && !args[6].equals("")){
				query.setString("cNum","%"+args[6]+"%");
			}
			if(args.length>5){
				if(args[10]!=null && !args[10].equals("")){
					query.setString("seName", "%"+args[10]+"%");
				}
				if(args[11]!=null && !args[11].equals("")){
					query.setString("corAdd", "%"+args[11]+"%");
				}
			}
		}
		return query;
	}
	/**
	 * 
	 * @param session
	 * @param args
	 * @param orderItem
	 * @param isDe
	 * @param isCount
	 * @return
	 */
	private Query getCustomersByAreaSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "cus.";
		String sql = "from CusCorCus cus ";
		if(isCount){
			sql = "select count(*) " + sql;
		}
		else{
			sql = "select cus "+sql;
		}
		if(args != null){
			if(!StringFormat.isEmpty(args[0])){
				appendSql.append("where ");
				appendSql.append(tab+CUS_ISDEL+" = '"+args[0]+"' ");
			}
			if(!StringFormat.isEmpty(args[1])){
				if(args[1].equals("0")&& args.length>5 && !StringFormat.isEmpty(args[13]) && args[13].equals(Integer.toString(CusCorCus.S_DEVED))){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_EMP_ID +" = "+args[2]+" ");
				}
			}
			
			if(!StringFormat.isEmpty(args[2])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_AREID+"="+args[2]+" ");
			}
			if(!StringFormat.isEmpty(args[3])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_PRVID+"="+args[3]+" ");
			}
			if(!StringFormat.isEmpty(args[4])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_CITYID+"="+args[4]+" ");
			}
			
			if(!StringFormat.isEmpty(args[5])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_NAME+" like :cName ");
			}
			if(!StringFormat.isEmpty(args[6])){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+CUS_NUM+" like :cNum ");
			}
			if(args.length>5){
				if(!StringFormat.isEmpty(args[7])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_TYPE_ID+" = "+args[7]+" ");
				}
				if(args[8]!=null){
					if (!args[8].equals("") && args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab+CUS_CRE_DATE+" >= '"+ args[8] + "' ");
					} else if (!args[8].equals("") && !args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " between '"
								+ args[8] + "' and '"
								+ GetDate.parseStrDate(GetDate.getDate(args[9], 1))
								+ "' ");
					} else if (args[8].equals("") && !args[9].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + CUS_CRE_DATE + " <= '"
								+ GetDate.parseStrDate(GetDate.getDate(args[9], 1))
								+ "' ");
					}
				}
				if(!StringFormat.isEmpty(args[10])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_EMP_NAME +" like :seName ");
				}
				if(!StringFormat.isEmpty(args[11])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+ CUS_ADD +" like :corAdd ");
				}
				if(!StringFormat.isEmpty(args[12])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_INDUSTRY_ID+"="+args[12]+" ");
				}
				if(!StringFormat.isEmpty(args[13])){
					String [] filter = {"all","tag1","none","date7","date15","date30"};
					Date lastDateCon = new Date();
					Date startDateCon = new Date();
					Date endDateCon = new Date();
					if(args[13].equals("date7")){
						startDateCon = OperateDate.getDate(new Date(), -14);
						endDateCon = OperateDate.getDate(new Date(), -6);
					}else if(args[13].equals("date15")){
						startDateCon = OperateDate.getDate(new Date(), -29);
						endDateCon = OperateDate.getDate(new Date(), -15);
					}else if(args[13].equals("date30")){
						lastDateCon = OperateDate.getDate(lastDateCon, -30);
					}
					for(int i=0;i<filter.length;i++){
						if(args[11].equals(filter[i])){
							switch(i){
							case 0:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_EMP_ID+" is null ");
								break;
							case 1:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_TAG+" = '1' ");
								break;
							case 2:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_LAST_DATE+" is null ");
								break;
							case 3:
							case 4:
								appendSql.append(appendSql.length()==0?"where ":"and ");
								appendSql.append(tab+CUS_LAST_DATE+" between "+"'"+new java.sql.Date(startDateCon.getTime())+"'"+" and "+"'" +new java.sql.Date(endDateCon.getTime())+"' ");
								break;
							case 5:
								appendSql.append(appendSql.length()==0?"where ":"and ");	
								appendSql.append(tab+CUS_LAST_DATE+" <= "+ "'" + new java.sql.Date(lastDateCon.getTime())+ "' ");
								break;
							}
							break;
						}
					}
				}
				if(!StringFormat.isEmpty(args[14])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_COLOR+" = '"+args[14]+"' ");
				}
				if(!StringFormat.isEmpty(args[15])){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+CUS_STATE+" = "+args[15]+" ");
				}
			}
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[5]!=null && !args[5].equals("")){
				query.setString("cName", "%"+args[5]+"%");
			}
			if(args[6]!=null && !args[6].equals("")){
				query.setString("cNum","%"+args[6]+"%");
			}
			if(args.length>5){
				if(args[10]!=null && !args[10].equals("")){
					query.setString("seName", "%"+args[10]+"%");
				}
				if(args[11]!=null && !args[11].equals("")){
					query.setString("corAdd", "%"+args[11]+"%");
				}
			}
		}
		return query;
	}
	private String[] getSortSql(String sql,String tab,String orderItem,String isDe){
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "cHot", "cName", "cType", "cPho", "cCellPho",
					"cAddr", "cRem", "seName", "cLastDate", "cMne", "cNum", "cState" };
			String[] cols = { CUS_HOT, CUS_NAME, CUS_TYPE_NAME, CUS_PHO,
					CUS_CELL_PHO, CUS_ADD, CUS_REMARK, CUS_EMP_NAME,
					CUS_LAST_DATE, CUS_MNE, CUS_NUM, CUS_STATE };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch(i){
					case 2:
						joinSql +="left join "+tab+CUS_TYPE+" ";
						break;
					case 7:
						joinSql +="left join "+tab+CUS_EMP+" ";
						break;
					}
					orderItem = cols[i];
					break;
				}
			}
			sortSql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortSql += "desc ";
			}
		} else {
			sortSql = "order by "+tab+CUS_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
	public void save(CusCorCus cusCorCus) {
		super.getHibernateTemplate().save(cusCorCus);
	}

	public CusCorCus getCusCorCusInfo(String corCode) {
		String sql = "from CusCorCus where "+CUS_ID+"=" + corCode + "";
		List<CusCorCus> list = super.getHibernateTemplate().find(sql);
		if (list.size() > 0) {
			return (CusCorCus) list.get(0);
			
		} else {
			return null;
		}
	}
	public CusCorCus findCus(Long id) {
		return (CusCorCus) super.getHibernateTemplate().get(CusCorCus.class, id);
	}
	public void update(CusCorCus cusCorCus) {
		super.getHibernateTemplate().merge(cusCorCus);
	}
	
	public List<String> getCusNames(String cusName) {
		Session session = super.getSession();
		String sql="select "+CUS_NAME+" from CusCorCus where "+CUS_NAME+"=:cusName";
		Query query = session.createQuery(sql).setString("cusName", cusName);
		List<String> list = query.list();
		return list;
	}
	public List<CusCorCus> supSearCus(String range,CusCorCus cusCorCus, Long indId, Date startDateCon,
			Date endDateCon, Date lastDateCon, Date startDateUpd, Date endDateUpd,
			Date lastDateUpd, String mark, String startTime, String endTime,
			String uCode,String orderItem, String isDe,int currentPage, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		query = getSupSearSql(session,range,cusCorCus,indId,startDateCon,endDateCon,lastDateCon,startDateUpd,endDateUpd,
				lastDateUpd,mark,startTime,endTime,uCode,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list=query.list();
		return list;
	}
	public int supSearCusCount(String range,CusCorCus cusCorCus, Long indId, Date startDateCon,
			Date endDateCon, Date lastDateCon, Date startDateUpd, Date endDateUpd,
			Date lastDateUpd, String mark, String startTime, String endTime,
			String uCode) {
		Query query;
		Session session;
		session = super.getSession();
		query = getSupSearSql(session,range,cusCorCus,indId,startDateCon,endDateCon,lastDateCon,startDateUpd,endDateUpd,
				lastDateUpd,mark,startTime,endTime,uCode,null,null,true);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}	
	private Query getSupSearSql(Session session,String range,CusCorCus cusCorCus, Long indId, Date startDateCon,
			Date endDateCon, Date lastDateCon, Date startDateUpd, Date endDateUpd,
			Date lastDateUpd, String mark, String startTime, String endTime,
			String uCode,String orderItem, String isDe,boolean isCount){
		StringBuffer appendSql = new StringBuffer();	
		String tab = "cus.";
		String sql = "from CusCorCus cus " ;
		if(isCount){
			sql = "select count(*) "+sql;
		}
		else{
			sql = "select cus "+sql;
		}
		if(cusCorCus.getCorTempTag()!=null && !cusCorCus.getCorTempTag().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_TAG+" like :corTempTag ");
		}
	    if(cusCorCus.getCorName()!=null && !cusCorCus.getCorName().equals("")){
	    	appendSql.append(appendSql.length()==0?"where ":"and ");
	    	appendSql.append(tab+CUS_NAME+" like :corName ");
		}
	    if(cusCorCus.getCorRemark()!=null && !cusCorCus.getCorRemark().equals("")){
	    	appendSql.append(appendSql.length()==0?"where ":"and ");
	    	appendSql.append(tab+CUS_REMARK+" like :corRemark ");
	    }
		if(cusCorCus.getCorMne()!=null && !cusCorCus.getCorMne().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_MNE+" like :corMne ");
		}
		if(cusCorCus.getCorAddress()!=null && !cusCorCus.getCorAddress().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_ADD+" like :corAddress ");
		}
		if(cusCorCus.getCorRelation()!=null && !cusCorCus.getCorRelation().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_CRE_LIM+" like :corRelation ");
		}
		if(cusCorCus.getCorNum()!=null && !cusCorCus.getCorNum().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_NUM+" like :corNum ");
		}
		if(cusCorCus.getCorHot()!=null && !cusCorCus.getCorHot().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_HOT+" like :corHot ");
		}
		if(indId!=null&&indId!=0){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_INDUSTRY_ID+" = "+indId+" ");
		}
		if( cusCorCus.getCorSou() != null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_SOU_ID+" ="+cusCorCus.getCorSou().getTypId()+" ");
		}
		if(cusCorCus.getCorPerSize()!=null && !cusCorCus.getCorPerSize().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_PER_SIZE+" like :corPerSize ");
		}
		if(cusCorCus.getCusArea()!=null&&cusCorCus.getCusArea().getAreId()!=1){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_AREA_ID+" ="+cusCorCus.getCusArea().getAreId()+" ");
		}
		if(cusCorCus.getCusProvince()!=null&&cusCorCus.getCusProvince().getPrvId()!=1){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_PROV_ID+" ="+cusCorCus.getCusProvince().getPrvId()+" ");
		}
		if(cusCorCus.getCusCity()!=null&&cusCorCus.getCusCity().getCityId()!=1){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_CITY_ID+" = "+cusCorCus.getCusCity().getCityId()+" ");
		}
		if(cusCorCus.getCusType()!=null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_TYPE_ID+" ="+cusCorCus.getCusType().getTypId()+" ");
		}
		if((startTime==null||startTime.equals(""))&&endTime!=null&&!endTime.equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_CRE_DATE+" <='" +java.sql.Date.valueOf(endTime)+"' ");
		}
		else if(startTime!=null&&!startTime.equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_CRE_DATE+" between"+"'"+ java.sql.Date.valueOf(startTime)+"'"+" and "+"'" +java.sql.Date.valueOf(endTime)+"' ");
		}
		if(!mark.equals("")&&mark.equals("noContact")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_LAST_DATE+" is null ");
		}
		if(startDateCon!=null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_LAST_DATE+" between "+"'"+new java.sql.Date(startDateCon.getTime())+"'"+" and "+"'" +new java.sql.Date(endDateCon.getTime())+"' ");
		}
		if(startDateUpd!=null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_UPD_DATE+" between "+"'"+ new java.sql.Date(startDateUpd.getTime())+"'"+" and "+"'" +new java.sql.Date(endDateUpd.getTime())+"' ");
		}	
		if(lastDateCon!=null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_LAST_DATE+" <= "+ "'" + new java.sql.Date(lastDateCon.getTime())+ "' ");
		}
		if(lastDateUpd!=null){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_UPD_DATE+" <= "+ "'" + new java.sql.Timestamp(lastDateUpd.getTime())+ "' ");
		}
		if(cusCorCus.getCorPhone()!=null && !cusCorCus.getCorPhone().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_PHO+" like :corPhone ");
		}
		if(cusCorCus.getCorFex()!=null && !cusCorCus.getCorFex().equals("")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_COR_FEX+" like :corFex ");
		}
		if(range!=null && range.equals("0")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			appendSql.append(tab+CUS_EMP_ID+" = "+uCode+" and "+tab +CUS_ISDEL+"= '1' " );
		}
		else if(range!=null && range.equals("1")){
			appendSql.append(appendSql.length()==0?"where ":"and ");
			if(uCode.equals("")){
				appendSql.append(tab +CUS_ISDEL+"= '1' ");
			}
			else{
				appendSql.append(tab+CUS_EMP_ID+" in("+uCode+") and "+tab +CUS_ISDEL+"= '1' ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = { "cMark", "cHot", "cName","cType","cAddr",
						"cPho","cCellPho", "cRem","seName", "cLastDate"};
				String [] cols = { CUS_TAG, CUS_HOT, CUS_NAME,CUS_TYPE_NAME ,
						CUS_ADD, CUS_PHO, CUS_CELL_PHO, CUS_REMARK,CUS_EMP_NAME, CUS_LAST_DATE};
				for(int i=0;i<items.length;i++){
					if (orderItem.equals(items[i])) {
						switch(i){
						case 3:
							sql +="left join "+tab+CUS_TYPE+" ";
							break;
						case 9:
							sql +="left join "+tab+CUS_EMP+" ";
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
				appendSql.append("order by "+tab+CUS_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		Query query = session.createQuery(sql);
		if(cusCorCus.getCorTempTag()!=null && !cusCorCus.getCorTempTag().equals("")){
			query.setString("corTempTag","%"+cusCorCus.getCorTempTag()+"%" );
		}
		if(cusCorCus.getCorName()!=null && !cusCorCus.getCorName().equals("")){
			query.setString("corName", "%"+cusCorCus.getCorName()+"%");
		}
		if(cusCorCus.getCorRemark()!=null && !cusCorCus.getCorRemark().equals("")){
			query.setString("corRemark", "%"+cusCorCus.getCorRemark()+"%");
		}
		if(cusCorCus.getCorMne()!=null && !cusCorCus.getCorMne().equals("")){
			query.setString("corMne", "%"+cusCorCus.getCorMne()+"%");
		}
		if(cusCorCus.getCorAddress()!=null && !cusCorCus.getCorAddress().equals("")){
			query.setString("corAddress", "%"+cusCorCus.getCorAddress()+"%");
		}
		if(cusCorCus.getCorRelation()!=null && !cusCorCus.getCorRelation().equals("")){
			query.setString("corRelation", "%"+cusCorCus.getCorRelation()+"%");
		}
		if(cusCorCus.getCorNum()!=null && !cusCorCus.getCorNum().equals("")){
			query.setString("corNum", "%"+cusCorCus.getCorNum()+"%");
		}
		if(cusCorCus.getCorIssuc()!=null && !cusCorCus.getCorIssuc().equals("")){
			query.setString("corIssuc", "%"+cusCorCus.getCorIssuc()+"%");
		}
		if(cusCorCus.getCorHot()!=null && !cusCorCus.getCorHot().equals("")){
			query.setString("corHot", "%"+cusCorCus.getCorHot()+"%");
		}
		if(cusCorCus.getCorPerSize()!=null && !cusCorCus.getCorPerSize().equals("")){
			query.setString("corPerSize", "%"+cusCorCus.getCorPerSize()+"%");
		}
		if(cusCorCus.getCorPhone()!=null && !cusCorCus.getCorPhone().equals("")){
			query.setString("corPhone", "%"+cusCorCus.getCorPhone()+"%");
		}
		if(cusCorCus.getCorFex()!=null && !cusCorCus.getCorFex().equals("")){
			query.setString("corFex", "%"+cusCorCus.getCorFex()+"%");
		}
		return query;
	}
	
	public List<CusCorCus> listDelCus(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from CusCorCus where "+CUS_ISDEL+"='0' order by "+CUS_ID+" desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list=query.list();
		return list;
	}
	public int listDelCusCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from CusCorCus where "+CUS_ISDEL+"='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	
	public List<CusCorCus> listAllCus() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from CusCorCus where "+CUS_ISDEL+"='1'  ";
		query=session.createQuery(hql);	
		List<CusCorCus> list=query.list();
		return list;
	}

	public List <CusCorCus> statRecvAnalyse(String[] args, String orderItem, String isDe){
		Session session = super.getSession();
		Query query;
		String tab = "cus.";
		String hql ="from CusCorCus cus  where "+CUS_ISDEL+"='1' ";
		StringBuffer appendSql = new StringBuffer();
		if(args!=null){
			if(args[0] !=null && !args[0].equals("")){
				appendSql.append("and ");
				appendSql.append(tab + CUS_REC_DATE + "= '" + args[0] + "' ");
				
				if(args[1] !=null && !args[1].equals("")){
					appendSql = new StringBuffer();
					String [] filter = {"date7","date15","date30","dateAll"};
					Date recDate = GetDate.formatDate(args[0]);
					Date lastRecDate = new Date();
					if(args[1].equals("date7")){
						lastRecDate = OperateDate.getDate(recDate, 7);
					}else if(args[1].equals("date15")){
						lastRecDate = OperateDate.getDate(recDate, 15);
					}else if(args[1].equals("date30")){
						lastRecDate = OperateDate.getDate(recDate, 30);
					}
					for(int i=0;i<filter.length;i++){
						if(args[1].equals(filter[i])){
							switch(i){
								case 0:
									appendSql.append("and ");
									appendSql.append(tab+CUS_REC_DATE+" between '"+args[0]+"' and '" +new java.sql.Date(lastRecDate.getTime())  + "' ");
									break;
								case 1:
									appendSql.append("and ");
									appendSql.append(tab+CUS_REC_DATE+" between '"+ args[0]  +"' and '" +new java.sql.Date(lastRecDate.getTime())+ "' ");
									break;
								case 2:
									appendSql.append("and ");
									appendSql.append(tab+CUS_REC_DATE+" between '"+ args[0] +"' and '" + new java.sql.Date(lastRecDate.getTime()) + "' ");
									break;
								case 3:
									appendSql.append("and ");
									appendSql.append(tab+CUS_REC_DATE+" < '"+ args[0] +"' ");
									break;
							}
							break;
						}
					}
					
				}
			}else{
				appendSql.append("and ");
				appendSql.append(tab + CUS_REC_DATE + "= '" + GetDate.parseStrDate(new Date()) + "' ");
			}
		}
		if(orderItem!=null && !orderItem.equals("")){
			String [] items = { "recvAmt","recDate" };
			String [] cols = {CUS_RECV_AMT,CUS_REC_DATE};
			for(int i=0;i<items.length;i++){
				if (orderItem.equals(items[i])) {
					orderItem = cols[i];
					break;
				}
			}
			appendSql.append(" order by "+tab+orderItem+" ");
			if(isDe!=null && isDe.equals("1")){
				appendSql.append("desc ");
			}
		}
		hql += appendSql.toString();
		query=session.createQuery(hql);	
		List<CusCorCus> list=query.list();
		return list;
	}
	
	public void delCusCorCus(CusCorCus cusCorCus) {
		super.getHibernateTemplate().delete(cusCorCus);
	}
	public void batchAssign(String ids,String seNo,String seName, String cusState, LimUser curUser){
		Session session = super.getSession();
		String [] idsArr = ids.split(",");
    	CusCorCus cusCorCus = null;
    	String assLog;
    	for(int i=0;i<idsArr.length;i++){
    		cusCorCus = findCus(Long.parseLong(idsArr[i]));
    		if(StringFormat.isEmpty(seNo)){
    			cusCorCus.setPerson(null);
    		}
    		else{
    			cusCorCus.setPerson(new YHPerson(Long.parseLong(seNo)));
    		}
    		cusCorCus.setCorState(Integer.parseInt(cusState));
    		cusCorCus.setCorAssignDate(GetDate.getCurDate());
    		assLog = "<span>负责人修改为["+(StringFormat.isEmpty(seName)?"无":seName)+"]，" +
    				"客户状态修改为["+CusCorCus.getCorStateTxt(Integer.parseInt(cusState))+"]</span>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;<span class='gray'>" +
    				"(操作人:"+curUser.getPerson().getUserName()+"，操作时间:"+GetDate.parseStrDate(GetDate.getCurDate())+")</span><br/><br/>";
    		cusCorCus.setCorAssignCont(assLog+cusCorCus.getCorAssignCont());
    		update(cusCorCus);
    	}
    	String sql="";
    	if(!StringFormat.isEmpty(seNo)){
			sql = "update CusContact set "+CON_EMP_ID+"="+seNo+" where "+CON_CUS_ID+" in ("+ids+")";
		}
		else{
			sql = "update CusContact set "+CON_EMP_ID+" = NULL where "+CON_CUS_ID+" in ("+ids+")";
		}
    	session.createQuery(sql).executeUpdate();
	}
	
	public List<CusCorCus> getOutCus(String [] args){
		Session session = (Session)super.getSession();
		Query query = getCusSql(session,args,null,null,false);
		List<CusCorCus> list = query.list();
		return list;
	}
	
	public List<CusCorCus> getCusByIds(String ids){
		Session session = (Session)super.getSession();
		String id = ids.substring(0, ids.length()-1);
		StringBuffer appendSql = new StringBuffer();
		String tab = "cus.";
		String sql = "select cus from CusCorCus cus where ";
		appendSql.append(tab+CUS_ID+" in ("+id+") and "+tab+CUS_ISDEL+"='1' ");
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		List<CusCorCus> list = query.list();
		return list;
	}
	

	public List<CusCorCus> getSupSearCus(String range,CusCorCus cusCorCus, Long indId,Date startDateCon,Date endDateCon,Date lastDateCon,
            Date startDateUpd,Date endDateUpd,Date lastDateUpd,String mark,
            String startTime,String endTime,String uCode){
		Query query;
		Session session;
		session = super.getSession();
		
		query = getSupSearSql(session,range,cusCorCus,indId,startDateCon,endDateCon,lastDateCon,startDateUpd,endDateUpd,
				lastDateUpd,mark,startTime,endTime,uCode,null,null,false);
		List<CusCorCus> list=query.list();
		return list;
	}
	

	public void importCus(List[] dataList)throws Exception{
		Session session = (Session) super.getSession();
		for(int i=0; i<dataList.length;i++){
			Iterator it = dataList[i].iterator();
			while (it.hasNext()) {
				getHibernateTemplate().saveOrUpdate(it.next());
			}
		}
	}

	public void saveContact(CusContact transientInstance) {
		try {
			getHibernateTemplate().save(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<CusCorCus> listStatCus(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getStatCusSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<CusCorCus> list = query.list();
		return list;
	}
	public int listStatCusCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getStatCusSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getStatCusSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String[] statTypes = {"cusType","cusSou","cusInd"};
    	String[][] statCols = {
    			{ CUS_TYPE, CUS_TYPE_NAME },
    			{ CUS_SOU, CUS_SOU_NAME },
    			{ CUS_IND, CUS_IND_NAME } 
    		};
		StringBuffer appendSql = new StringBuffer();
		String tab = "cus.";//表更名
		String sql = "from CusCorCus cus ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select cus "+sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+CUS_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args[1]==null ){
	    		appendSql.append("and ");
	    		appendSql.append(tab+CUS_EMP_ID +" is null ");
			}
			else if(!args[1].equals("")){
				appendSql.append("and ");
	    		appendSql.append(tab+CUS_EMP_ID +" = "+args[1]+" ");
			}
		    if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append("and ");
		    	for(int i = 0;i < statTypes.length; i++){
		    		if(args[2].equals(statTypes[i])){
		    			if(args[3].equals("未选择")){
				    		appendSql.append(tab+statCols[i][0]+" is null ");
				    	}
				    	else{
				    		appendSql.append(tab+statCols[i][1]+" = :statName ");
				    	}
		    			break;
		    		}
		    	}
		    }
		    if(args[4]!=null && !args[4].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+CUS_EMP_ID+" in ("+args[4]+") ");
		    }
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[3]!=null && !args[3].equals("")&&!args[3].equals("未选择")){
				query.setString("statName", args[3]);
			}
		}
		return query;
	}
	

	public StaTable statCusTypeByEmp(String[] args){
		String params = null;
		StringBuffer appendSql = new StringBuffer();
		appendSql.append(" ");
		if(args[0]!=null&&!args[0].equals("")){
			appendSql.append("and cor_se_no in ("+args[0]+") ");
		}
		params = appendSql.toString();
	    return OperateList.getStaTab("statCusType",params);
	}
	
	/*public StaTable statEmpCusType(String[] args){
		String params = null;
		StringBuffer appendSql = new StringBuffer();
		appendSql.append(" ");//����ղ����쳣
		if(args[0]!=null&&!args[0].equals("")){
			appendSql.append("and cor_se_no in ("+args[0]+") ");
		}
		params = appendSql.toString();
	    return OperateList.getStaTab("empCusType",params);
	}*/

	public StaTable statEmpCusSou(String[] args){
		String params = null;
		StringBuffer appendSql = new StringBuffer();
		appendSql.append(" ");
		if(args[0]!=null&&!args[0].equals("")){
			appendSql.append("and cor_se_no in ("+args[0]+") ");
		}
		params = appendSql.toString();
	    return OperateList.getStaTab("empCusSou",params);
	}

	public StaTable statEmpCusInd(String[] args){
		String params = null;
		StringBuffer appendSql = new StringBuffer();
		appendSql.append(" ");
		if(args[0]!=null&&!args[0].equals("")){
			appendSql.append("and cor_se_no in ("+args[0]+") ");
		}
		params = appendSql.toString();
	    return OperateList.getStaTab("empCusInd",params);
	}
	

	public StaTable statSaleAnalyse(String[] args){
		String params = null;
		StringBuffer appendSql = new StringBuffer();
		 boolean hasAppend = (!StringFormat.isEmpty(args[0])||!StringFormat.isEmpty(args[1]));
		appendSql.append(" ");
		if(hasAppend){
			appendSql.append("and "+GetDate.getDateHql(args[0],args[1],ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB)+" ");
		}
		if(args[2]!=null && !args[2].equals("")){
			appendSql.append("and "+ CUS_ID_DB +"="+args[2]+" ");
		}
		if(args[3]!=null && !args[3].equals("") ){
			appendSql.append("and "+ SalOrderDAOImpl.ORD_EMP_DB +" in ("+StringFormat.removeLastSplitWord(args[3], ",")+")  ");
		}
		params = appendSql.toString();
	    return OperateList.getStaTab("saleAnalyse",params);
	}
	

	public void batchColor(String ids, String color){
		Session session = (Session)super.getSession();
		String id = ids.substring(0, ids.length()-1);
		String sql = "update crm_cus_cor_cus set cor_color = "+color+" where cor_code in ("+id+")";
		session.createSQLQuery(sql).executeUpdate();
	}
	
	
	 public List<CusCorCus> getOnTopCusList(String state,String seNo){
			String sql = "from CusCorCus ";
			StringBuffer sb = new StringBuffer();
			if(state !=null&& !state.equals("")){
				sb.append("where ");
				sb.append(CUS_STATE +"="+ Integer.parseInt(state) +" and "+ CUS_ISDEL+" =1");
			}
			if(seNo!=null && !seNo.equals("")){
				sb.append(sb.length()>0?" and ":" where ");
				sb.append( CUS_EMP_ID + "="+Integer.parseInt(seNo)+" and "+ CUS_ISDEL+" =1");
			}
			if(sb.length()<=0){
				sb.append(" where "+ CUS_ISDEL+" =1");
			}
			sql += sb.toString();
			List<CusCorCus> list = super.getHibernateTemplate().find(sql);
			return list;
	 }
	 
	
		 public List<RecvAmtForm> recvAmtList(String recDate){
				Session session = super.getSession();
				String sql = "select cus.cor_code as cusId,  cus.cor_name  as cusName, cus.cor_recv_amt as recvNum, cus.cor_ticket_num as ticketNum, emp.se_name as empName from crm_cus_cor_cus cus  left join sal_emp emp on cus.cor_se_no = emp.se_no  where cus.cor_rec_date <='"+recDate+"'"; 
				SQLQuery query = session.createSQLQuery(sql);	
				query.addScalar("cusId", Hibernate.LONG)
					.addScalar("cusName", Hibernate.STRING)
					.addScalar("empName",Hibernate.STRING)
					.addScalar("ticketNum", Hibernate.DOUBLE)
					.addScalar("recvNum",Hibernate.DOUBLE);
				query.setResultTransformer(Transformers.aliasToBean(RecvAmtForm.class));
				List<RecvAmtForm> list = query.list();
				return list;
		 }
	 
	
		 public List<StatSalMForm> onDateCusList(String[] args){
			 Session session = (Session)super.getSession();
			 boolean hasAppend = (!StringFormat.isEmpty(args[0])||!StringFormat.isEmpty(args[1]));
			 String sql = "select cus.cor_code as cusId,  cus.cor_name  as cusName, cus.cor_on_date as date1 , emp.se_name as empName from crm_cus_cor_cus  cus left join sal_emp emp on cus.cor_se_no = emp.se_no where cus.cor_isdelete = '1' ";
			 sql += (hasAppend?"and "+GetDate.getDateHql(args[0],args[1],COR_ON_DATE_DB):" ");
				SQLQuery query = session.createSQLQuery(sql);	
				query.addScalar("cusId", Hibernate.LONG)
					.addScalar("cusName", Hibernate.STRING)
					.addScalar("date1",Hibernate.DATE)
					.addScalar("empName",Hibernate.STRING);
				query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
				List<StatSalMForm> list = query.list();
				return list;
		 }
}