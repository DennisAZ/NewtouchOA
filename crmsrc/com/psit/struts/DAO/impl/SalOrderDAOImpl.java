package com.psit.struts.DAO.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalOrderDAO;
import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.OperateList;
import com.psit.struts.util.format.StringFormat;
/**
 * ������ݿ�������ʵ���� <br>
 */
@SuppressWarnings("unchecked")
public class SalOrderDAOImpl extends HibernateDaoSupport implements SalOrderDAO{
	private static final Log log = LogFactory.getLog(SalOrderDAOImpl.class);
	//property constants
	static final String ORD_ID = "sodCode";
	static final String ORD_ISDEL = "sodIsfail";
	static final String ORD_NUM = "sodNum";
	static final String ORD_TIL = "sodTil";
	static final String ORD_ISAPP = "sodAppIsok";
	static final String ORD_MON = "sodSumMon";
	static final String ORD_PAID = "sodPaidMon";
//	static final String ORD_PAY_CAT = "sodPaidMethod";
	static final String ORD_STATE = "sodShipState";
	static final String ORD_CON_DATE = "sodConDate";
	static final String ORD_DEADLINE = "sodDeadline";
	static final String ORD_END_DATE = "sodEndDate";
	static final String ORD_ORD_DATE ="sodOrdDate";
	static final String ORD_COMITE_DATE ="sodComiteDate";
//	static final String ORD_APP_DATE = "sodAppDate";
//	static final String ORD_APP_MAN = "sodAppMan";
	static final String ORD_TYPE = "salOrderType";
	static final String ORD_TYPE_ID = "salOrderType.typId";
	static final String ORD_TYPE_NAME = "salOrderType.typName";
	static final String TYPE_NAME = "typName";
	static final String ORD_STATE_NAME = "sodShipState.typName";
	static final String ORD_STATE_ID= "sodShipState.typId";
	static final String ORD_SOU = "salOrderSou";
	static final String ORD_SOU_NAME = "salOrderSou.typName";
	static final String ORD_CUS = "cusCorCus";
	static final String ORD_CUS_ID = "cusCorCus.corCode";
	static final String ORD_CUS_NAME = "cusCorCus.corName";
//	static final String ORD_EMP = "salEmp";
	static final String ORD_EMP_ID = "salEmp.seNo";
	static final String ORD_EMP_NAME = "salEmp.seName";
	
	static final String ORD_ID_DB = "sod_code";
	static final String ORD_ISDEL_DB = "sod_isfail";
	static final String ORD_ISAPP_DB = "sod_app_isok";
	static final String ORD_CON_DATE_DB = "sod_con_date";
	static final String ORD_MON_DB = "sod_sum_mon";
	static final String ORD_EMP_DB = "sod_se_no";
	static final String ORD_CUS_ID_DB = "sod_cus_code";
	static final String EMP_ID_DB = "se_no";
	static final String EMP_NAME_DB = "se_name";
	
	protected void initDao() {
		// do nothing
	}
	
	/**
	 * ������ͬ�б�<br>
	 * @param args 	[0]�Ƿ�ɾ��(1��ɾ��,0δɾ��);  [1]���״̬(0����,1����);  [2]��������;  [3]������;  
	 * 				[4]�ͻ�ID;  [5]ǩ������(��ʼ);  [6]ǩ������(����);  [7]�б�ɸѡ����; [8]ǩ���� [9]����״̬
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalOrdCon> ������ͬ�б�
	 */
	public List<SalOrdCon> listOrders(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getOrdSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalOrdCon> list = query.list();
		return list;
	}
	public int listOrdersCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getOrdSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getOrdSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String curDate = GetDate.parseStrDate(GetDate.getCurDate());
		String halfMonDate = GetDate.parseStrDate(GetDate.getDate(curDate, 15));
		String monthDate = GetDate.parseStrDate(GetDate.getDate(curDate, 30));
		int curMon = GetDate.getCurMon();
		int curYear = GetDate.getCurYear();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";//�����
		String tab1 = "typ.";
		String sql = "from SalOrdCon ord left join "+tab+ORD_STATE+" typ ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+ORD_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args.length>2){
				if(args.length>4){
					if(args[1]!=null && !args[1].equals("")){
						if(args[1].equals("0")){
							appendSql.append("and (");
				    		appendSql.append(tab1+TYPE_NAME +" != '�����' or " + tab1+TYPE_NAME+" is null) ");
						}else{
							appendSql.append("and ");
							appendSql.append(tab1+TYPE_NAME +" = '�����' ");
						}
					}
				}else{
		    		appendSql.append("and ");
		    		appendSql.append(tab+ORD_ISAPP +" = '"+args[1]+"' ");
				}
				if(args[2]!=null && !args[2].equals("")){
			    	appendSql.append("and ");
			    	appendSql.append(tab+ORD_TIL+" like :ordTil ");
			    }
			    if(args[3]!=null && !args[3].equals("")){
			    	appendSql.append("and ");
			    	appendSql.append(tab+ORD_NUM+" like :ordNum ");
			    }
			    if(args.length>4){
			    	if(args[4]!=null && !args[4].equals("")){
				    	appendSql.append("and ");
				    	appendSql.append(tab+ORD_CUS_ID+" = "+args[4]+" ");
				    }
				    if(args[5]!=null){
				    	if (!args[5].equals("") && args[6].equals("")) {
				    		appendSql.append("and ");
				    		appendSql.append(tab+ORD_CON_DATE+" >= '"+ args[5] + "' ");
						} else if (!args[5].equals("") && !args[6].equals("")) {
							appendSql.append("and ");
							appendSql.append(tab + ORD_CON_DATE + " between '"
									+ args[5] + "' and '"
									+ args[6]
									+ "' ");
						} else if (args[5].equals("") && !args[6].equals("")) {
							appendSql.append("and ");
							appendSql.append(tab + ORD_CON_DATE + " <= '"
									+ args[6]
									+ "' ");
						}
				    }
				    if(args[7]!=null && !args[7].equals("")){
				    	String[] filter = {"leftMon","d15","d30","dp","e15","e30","mon","year"};
				    	for (int i = 0; i < filter.length; i++) {
							if (args[7].equals(filter[i])) {
								switch(i){
						    	case 0:
						    		appendSql.append("and ");
							    	appendSql.append(tab+ORD_MON+">"+tab+ORD_PAID+" ");
						    		break;
						    	case 1:
						    		appendSql.append("and ");
						    		appendSql.append(tab + ORD_DEADLINE + " between '" + curDate + "'"
							    			+ " and '" + halfMonDate + "' ");
						    		break;
						    	case 2:
						    		appendSql.append("and ");
						    		appendSql.append(tab + ORD_DEADLINE + " between '" + curDate + "'"
							    			+ " and '" + monthDate + "' ");
						    		break;
						    	case 3:
						    		appendSql.append("and ");
							    	appendSql.append(tab + ORD_DEADLINE + "<'" + curDate + "'"
							    			+ " and " + tab + ORD_STATE_NAME + "!='�ѽ���' ");
						    		break;
						    	case 4:
						    		appendSql.append("and ");
						    		appendSql.append(tab + ORD_END_DATE + " between '" + curDate + "'"
							    			+ " and '" + halfMonDate + "' ");
						    		break;
						    	case 5:
						    		appendSql.append("and ");
						    		appendSql.append(tab + ORD_END_DATE + " between '" + curDate + "'"
							    			+ " and '" + monthDate + "' ");
						    		break;
						    	case 6:
						    		appendSql.append("and ");
						    		appendSql.append("year("+tab + ORD_CON_DATE + ") = '" + curYear + "'"
							    			+ " and month(" + tab + ORD_CON_DATE + ") = '" + curMon + "' ");
						    		break;
						    	case 7:
						    		appendSql.append("and ");
						    		appendSql.append("year("+tab + ORD_CON_DATE + ") = '" + curYear + "' ");
						    		break;
						    	}
								break;
							}
				    	}
				    }
				    if(args[8]!=null && !args[8].equals("")){
				    	appendSql.append("and ");
				    	appendSql.append(tab+ORD_EMP_NAME+" like :seName ");
				    }
				    if(args[9]!=null && !args[9].equals("")){
				    	appendSql.append("and ");
				    	appendSql.append(tab+ORD_STATE_ID+" = "+args[9]+" ");
				    }
			    }
			}
			else{
				if(args[1]!=null && !args[1].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ORD_CUS_ID+" = "+args[1]+" ");
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
			if(args.length>2){
				if(args[2]!=null && !args[2].equals("")){
					query.setString("ordTil", "%"+args[2]+"%");
				}
				if(args[3]!=null && !args[3].equals("")){
					query.setString("ordNum","%"+args[3]+"%");
				}
			}
			if(args.length>4){
				if(args[8]!=null && !args[8].equals("")){
					query.setString("seName", "%"+args[8]+"%");
				}
			}
		}
		return query;
	}
	
	
	/**
	 * ��Ӧ�ͻ��¶����б�<br>
	 * @param args 	[0]�Ƿ�ɾ��(1��ɾ��,0δɾ��);  [1]�ͻ�Id;  [2]��������;  [3]������;  
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalOrdCon> ������ͬ�б�
	 */
	public List<SalOrdCon> listOrdersByCusId(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalOrdCon> list = query.list();
		return list;
	}
	public int listOrdersByCusId(String[]args){
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getOrdSqlByCusId(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String curDate = GetDate.parseStrDate(GetDate.getCurDate());
		String halfMonDate = GetDate.parseStrDate(GetDate.getDate(curDate, 15));
		String monthDate = GetDate.parseStrDate(GetDate.getDate(curDate, 30));
		int curMon = GetDate.getCurMon();
		int curYear = GetDate.getCurYear();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";//�����
		String tab1 = "typ.";
		String sql = "from SalOrdCon ord left join "+tab+ORD_STATE+" typ ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+ORD_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args[1]!=null && !args[1].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+ORD_TIL+" like :ordTil ");
		    }
		    if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+ORD_NUM+" like :ordNum ");
		    }
			if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append(appendSql.length()==0?"where ":"and ");
		    	appendSql.append(tab+ORD_CUS_ID+" = "+args[3]+" ");
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
			if(args[1]!=null && !args[1].equals("")){
				query.setString("ordTil", "%"+args[1]+"%");
			}
			if(args[2]!=null && !args[2].equals("")){
				query.setString("ordNum","%"+args[2]+"%");
			}
		}
		return query;
	}
	private Query getOrdSqlByCusId2(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String curDate = GetDate.parseStrDate(GetDate.getCurDate());
		String halfMonDate = GetDate.parseStrDate(GetDate.getDate(curDate, 15));
		String monthDate = GetDate.parseStrDate(GetDate.getDate(curDate, 30));
		int curMon = GetDate.getCurMon();
		int curYear = GetDate.getCurYear();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";//�����
		String tab1 = "typ.";
		String sql = "from SalOrdCon ord left join "+tab+ORD_STATE+" typ ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+ORD_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args[1]!=null && !args[1].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+ORD_TIL+" like :ordTil ");
		    }
		    if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+ORD_NUM+" like :ordNum ");
		    }
			if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append(appendSql.length()==0?"where ":"and ");
		    	appendSql.append(tab+ORD_CUS_ID+" = "+args[3]+" ");
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
			if(args[1]!=null && !args[1].equals("")){
				query.setString("ordTil", "%"+args[1]+"%");
			}
			if(args[2]!=null && !args[2].equals("")){
				query.setString("ordNum","%"+args[2]+"%");
			}
		}
		return query;
	}
	/**
	 * ���������� <br>
	 * @param sql ԭ������sql
	 * @param tab ����
	 * @param orderItem �����ֶ�
	 * @param isDe	�Ƿ�����
	 * @return String[] �������
	 */
	private String[] getSortSql(String sql,String tab,String orderItem,String isDe){
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "appState","oType", "oTil", "oNum", "oCus",
					"oPaid", "oMon", "oConDate", "oDeadLine","oEmp","ordState","sodComiteDate"};
			String[] cols = { ORD_ISAPP,ORD_TYPE_NAME, ORD_TIL, ORD_NUM, ORD_CUS_NAME,
					ORD_PAID, ORD_MON, ORD_CON_DATE, ORD_DEADLINE,ORD_EMP_NAME,ORD_STATE_NAME,ORD_COMITE_DATE};
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 1:
						joinSql += "left join " + tab + ORD_TYPE + " ";
						break;
					case 10:		
						joinSql += "left join "+ tab + ORD_STATE+" ";
						break;
					}
					orderItem = cols[i];
					break;
				}
			}
			sortSql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortSql+="desc ";
			}
		} else {//Ĭ������
			sortSql="order by "+tab+ORD_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
	
	/**
	 * ����̨��������<br>
	 * @return List<SalOrdCon> (�����action������ע����ת��һ������ӳ����)<br>
	 * 		attr > list
	 */
	public List<SalOrdCon> getProdTipList(String filter){
		Session session = super.getSession();
		String curDate = GetDate.parseStrDate(GetDate.getCurDate());
		String thrDate = GetDate.parseStrDate(GetDate.getDate(curDate, 3));
		String sevenDate = GetDate.parseStrDate(GetDate.getDate(curDate, 7));
		String halfMonDate = GetDate.parseStrDate(GetDate.getDate(curDate, 15));
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";
    	String[] filter1 = {"date3","date7","date15"};
    	for (int i = 0; i < filter1.length; i++) {
			if (filter.equals(filter1[i])) {
				switch(i){
		    	case 0:
			    	appendSql.append(" <= '" + thrDate + "' ");
		    		break;
		    	case 1:
		    		appendSql.append(" <= '" + sevenDate + "' ");
		    		break;
		    	case 2:
		    		appendSql.append(" <='" + halfMonDate + "' ");
		    		break;
		    	}
				break;
			}
    	}
    	String sql = "select ord from SalOrdCon ord left join "+tab+ORD_STATE+
				" where ("+tab+ORD_STATE_NAME+"!='�����' or "+tab+ORD_STATE+" is null) and "+tab+ORD_COMITE_DATE+appendSql.toString()+
				" order by "+tab+ORD_COMITE_DATE;
		Query query = session.createQuery(sql);
		List list = query.list();
		return list;
	}
	
	
	/**
	 * �õ�����Ӧ�տ� <br>
	 * @return String Ӧ�տ�
	 */
	public String getLeftMon(){
		String leftMon;
		Session session = super.getSession();
		Query query = session.createQuery("select sum(case when "+ORD_MON+">"+ORD_PAID+" then ("+ORD_MON+"-"+ORD_PAID+") else 0 end) "
					+"from SalOrdCon where "+ORD_ISDEL+" = '0' and "+ORD_ISAPP+"='1' ");
		if(query.uniqueResult()!=null){
			leftMon = query.uniqueResult().toString();
		}
		else{
			leftMon = "0";
		}
		return leftMon;
	}
	
	public Double getOrdPaidByCus(String cusId){
		Double ordPaid=0.0;
		Session session = super.getSession();
		Query query = session.createQuery("select sum("+ORD_PAID+") "
					+"from SalOrdCon where "+ORD_ISDEL+" = '0' and "+ORD_CUS_ID+"="+cusId+" ");
		if(query.uniqueResult()!=null){
			ordPaid = (Double)query.uniqueResult();
		}
		return ordPaid;
	}
	
	/**
	 * �õ���������<br>
	 * @param code ����id
	 * @return SalOrdCon ��������<br>
	 */
	public SalOrdCon getOrdCon(String code){
		long id=Long.parseLong(code);
		String sql="from SalOrdCon where sodIsfail='0' and sodCode="+id;
		List list=super.getHibernateTemplate().find(sql);
		if(list.size()>0)
			return (SalOrdCon) list.get(0);
		else
			return null;
	}
	
	/**
	 * ��ô�ɾ������ж���<br>
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List<SalOrdCon> ��ɾ�����б�<br>
	 */
	public List<SalOrdCon> findDelOrder(String orderItem, String isDe, int currentPage, int pageSize) {
		Session session = (Session) super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";//�����
		String sql = "select ord from SalOrdCon ord ";
		appendSql.append("where "+tab+ORD_ISDEL+"='1' ");
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "oType", "oTil", "oNum", "oCus",
					"oPaid", "oMon", "oConDate", "oDeadLine","oEmp"};
			String[] cols = { ORD_TYPE_NAME, ORD_TIL, ORD_NUM, ORD_CUS_NAME,
					ORD_PAID, ORD_MON, ORD_CON_DATE, ORD_DEADLINE,ORD_EMP_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 0:
						sql += "left join " + tab + ORD_TYPE + " ";
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
		} else {//Ĭ������
			appendSql.append("order by "+tab+ORD_ID+" desc ");
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalOrdCon> list = query.list();
		return list;
	}
	public int findDelOrderCount() {
		Session session = (Session) super.getSession();
		String sql = "select count(*) from SalOrdCon where "+ORD_ISDEL+"='1' ";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * ���Id�õ�����<br>
	 * @param id ����id
     * @return SalOrdCon ��������<br>
	 */
	public SalOrdCon getSalOrder(Long id) {
		String sql="from SalOrdCon where  sodCode="+id;
		List list=super.getHibernateTemplate().find(sql);
		if(list.size()>0)
			return (SalOrdCon) list.get(0);
		else
			return null;
	}
	
	/**
	 * ��ݱ�Ų�ѯ(�������ظ����)<br>
	 * @param code �������
	 * @return List ������¼�б�<br>
	 */
	public List searchByCode(String code){
		Session session = super.getSession();
		String sql="select sodNum from SalOrdCon";
		sql+=" where sodNum=:code order by sodInpDate desc";
		Query query = session.createQuery(sql).setString("code", code);
		List list=(List)query.list();
		return list;
	}
	
	/**
	 * ���涩��<br>
	 * @param transientInstance ��������<br>
	 */
	public void save(SalOrdCon transientInstance) {
		log.debug("saving SalOrdCon instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * ɾ���ͬ����<br>
	 * @param persistentInstance ��������<br>
	 */
	public void delete(SalOrdCon persistentInstance) {
		log.debug("deleting SalOrdCon instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * ����<br>
	 * @param instance ��������
	 */
	public void update(SalOrdCon instance){
		getHibernateTemplate().update(instance);
	}
	
	/**
	 * ɾ�����������վ�������¿ͻ��ѳɵ�״̬<br>
	 * @param order ����ʵ��
	 */
	public void invalid(SalOrdCon order){
		order.setSodIsfail("1");
		getHibernateTemplate().update(order);
//		if (order.getCusCorCus() != null) {
//			int i = 0;
//			CusCorCus cusCorCus = order.getCusCorCus();
//			Set list = cusCorCus.getSalOrdCons();
//			Iterator<SalOrdCon> iter = list.iterator();
//			while (iter.hasNext()) {
//				SalOrdCon ordCon = iter.next();
//				if (ordCon.getSodIsfail().equals("0")){
//					i++;
//				}
//			}
//			if (i == 1) {
//				cusCorCus.setCorIssuc("0");
//				getHibernateTemplate().update(cusCorCus);
//			}
//		}
	}
	
	/**
	 * �ָ����������¿ͻ��ѳɵ�״̬<br>
	 * @param order ����ʵ��
	 */
	public void recovery(SalOrdCon order){
		order.setSodIsfail("0");
		getHibernateTemplate().update(order);
//		if (order.getCusCorCus() != null) {
//			int i = 0;
//			CusCorCus cusCorCus = order.getCusCorCus();
//			if (cusCorCus.getCorIssuc().equals("0")) {
//				cusCorCus.setCorIssuc("1");
//				getHibernateTemplate().update(cusCorCus);
//			}
//		}
	}

	/**
	 * �õ������ܶ�<br>
	 * @param userCode �û�id
	 * @return String �����ܶ�<br>
	 */
	public String getOrdMonSum(String userCode){
		String monSum;
		Session session = super.getSession();
		
		String hql = "select sum(sodSumMon) from SalOrdCon as s where limUser.userCode = '"+userCode
					+ "' and s.sodIsfail = '0' and s.sodAppIsok='1'";
		Query query = session.createQuery(hql);
		List list = (List) query.list();
		if(list.get(0)!=null){
			monSum=list.get(0).toString();
		}
		else{
			monSum="0";
		}
		return monSum;
	}
	
	/**
	 * �õ�ĳ�˵Ķ����ܶ��ؿ��ܶ�<br>
	 * @param userCode �û�Id���û���
	 * @param isAll �Ƿ���ȫ��
	 * @param type ��ѯ���(0Ϊ��ѯ�����ܶ1Ϊ��ѯ�ؿ��ܶ�)
	 * @return String ������ؿ��ܶ�
	 */
	public String getOrdSumWithUNum(String userCode,String isAll,String type){
		String monSum;
		Session session = super.getSession();
		String hql="";
		if(type!=null&&type.equals("0"))
			hql="select sum("+ORD_MON+") ";
		else
			hql="select sum("+ORD_PAID+") ";
		hql += "from SalOrdCon where "+ORD_ISDEL+" = '0' and "+ORD_ISAPP+"='1' ";
		if(isAll!=null&&isAll.equals("0"))
			hql+="and "+ORD_EMP_ID+" ="+userCode+" ";
		if (isAll!=null&&isAll.equals("1"))
			hql+="and "+ORD_EMP_ID+" !="+userCode+" and "+ORD_EMP_ID+" like %"+userCode+" ";
		Query query = session.createQuery(hql);
		List list = (List) query.list();
		if(list.get(0)!=null){
			monSum=list.get(0).toString();
		}
		else{
			monSum="0";
		}
		return monSum;
	}
	
	/**
	 * �õ�ĳ�˵��¶����ܶ�<br>
	 * @param empId Ա��id
	 * @return String �����ܶ�<br>
	 */
	public String relOrdMonSum(String empId){
		int curMon = GetDate.getCurMon();
		int curYear = GetDate.getCurYear();
		String monSum;
		Session session = super.getSession();
		String sql="and year(s.sodConDate)='"+curYear+"'and  month(s.sodConDate) = '"+curMon+"'";
		String hql = "select sum(sodSumMon) from SalOrdCon as s where "
					+ "salEmp.seNo = "+ empId+ " and s.sodIsfail = '0' and s.sodAppIsok='1'"+sql;
		Query query = session.createQuery(hql);
		List list = (List) query.list();
		if(list.get(0)!=null){
			monSum=list.get(0).toString();
		}
		else{
			monSum="0";
		}
		return monSum;
	}
	/**
	 * ��ѯĳ�˵����ѳ�ǩ������<br>
	 * @param empId Ա��id
	 * @return int ��ɿͻ�����<br>
	 */
	public int relCusCount(String empId){
		int curMon = GetDate.getCurMon();
		int curYear = GetDate.getCurYear();
		Session session = super.getSession();
		String sql="and year(s.sodConDate)='"+curYear+"'and  month(s.sodConDate) = '"+curMon+"'";
		String hql = "select count(s) from SalOrdCon as s where "
					+ "salEmp.seNo =" + empId+ " and s.sodIsfail = '0' and s.sodAppIsok='1'"+sql;
		Query query = session.createQuery(hql);
		int num = Integer.parseInt(query.uniqueResult().toString());
		return num;
	}
	
// --------- ͳ�� -----------
	/**
	 * ͳ�Ʊ���ж����б� <br>
	 * @param args 	[0]�Ƿ�ɾ��(1δɾ��,0��ɾ��);  [2]ͳ������; [3]ͳ�ƶ���; [4]Ա��ID����;  [5][6]ǩ������;
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalOrdCon> �����б��б�
	 */
	public List<SalOrdCon> listStatOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getStatOrdSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalOrdCon> list = query.list();
		return list;
	}
	public int listStatOrdCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getStatOrdSql(session, args, null, null, true);
		String te = query.uniqueResult().toString();
		int count = Integer.parseInt(te);
		return count;
	}
	private Query getStatOrdSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String[] statTypes = {"ordType","ordSou","ordEmpMon","ordPaidMon" };
    	String[][] statCols = {
    			{ ORD_TYPE, ORD_TYPE_NAME },
    			{ ORD_SOU, ORD_SOU_NAME },
    			{ ORD_CON_DATE, ORD_CON_DATE },
    			{ ORD_CON_DATE, ORD_CON_DATE },
    		};
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";//�����
		String sql = "from SalOrdCon ord ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord "+sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+ORD_ISDEL+" = '"+args[0]+"' ");
		    	appendSql.append("and ");
		    	appendSql.append(tab+ORD_ISAPP+" = '1' ");
		    }
			if(args[1]!=null && !args[1].equals("")){
	    		appendSql.append("and ");
	    		appendSql.append(tab+ORD_EMP_ID +" = "+args[1]+" ");
			}
		    if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append("and ");
		    	for(int i = 0;i < statTypes.length; i++){
		    		if(args[2].equals(statTypes[i])){
		    			switch(i){
		    			case 0:
		    			case 1:
		    				if(args[3].equals("δѡ��")){
					    		appendSql.append(tab+statCols[i][0]+" is null ");
					    	}
					    	else{
					    		appendSql.append(tab+statCols[i][1]+" = :statName ");
					    	}
			    			break;
		    			case 2:
		    			case 3:
		    				appendSql.append("date_format("+tab+statCols[i][1]+",'%Y-%m') = :statName ");
		    				break;
		    			}
		    			break;
		    		}
		    	}
		    }
		    if(args.length>4){
			    if(args[4]!=null && !args[4].equals("")){
			    	appendSql.append("and ");
			    	appendSql.append(tab+ORD_EMP_ID+" in ("+args[4]+") ");
			    }
			    if(args[5]!=null){
			    	if (!args[5].equals("") && args[6].equals("")) {
			    		appendSql.append("and ");
			    		appendSql.append(tab+ORD_CON_DATE+" >= '"+ args[5] + "' ");
					} else if (!args[5].equals("") && !args[6].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + ORD_CON_DATE + " between '"
								+ args[5] + "' and '"
								+ args[6]
								+ "' ");
					} else if (args[5].equals("") && !args[6].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + ORD_CON_DATE + " <= '"
								+ args[6]
								+ "' ");
					}
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
			if(args[3]!=null && !args[3].equals("")&&!args[3].equals("δѡ��")){
				query.setString("statName", args[3]);
			}
		}
		return query;
	}
	
	/**
	 * ���۽��ͳ�� <br>
	 * @param empIds	Ա��ID����
	 * @param cusIds	�ͻ�ID����
	 * @param startDate	ͳ������
	 * @param endDate
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalM(String empIds,String cusIds,String startDate,String endDate) {
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "salTabSum.";
		String tab1_1 = "cus.";
		String tab1_2 = "salTab1.";
		String tab1_2_1 = "cus1.";
		String tab1_2_2 = "ord.";
		String tab1_3 = "salTab2.";
		String tab1_3_1 = "cus2.";
		String tab1_3_2 = "ordShip.";
		String tab1_3_2_1 = "sorder.";
		String tab1_3_2_2 = "ship.";
		String tab1_4 = "salTab3.";
		String tab1_4_1 = "cus3.";
		String tab1_4_2 = "spaid.";
		String tab2 = "emp.";
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		appendSql.append("select "+tab1+CusCorCusDAOImpl.CUS_ID_DB+" as cusId ,");
		appendSql.append(tab1+CusCorCusDAOImpl.CUS_NAME_DB + " as cusName,");
		appendSql.append(tab2+SalEmpDAOImpl.SE_NAME_DB + " as empName,");
		appendSql.append(tab1+"monSumA as monSum1,");
		appendSql.append(tab1+"monSumB as monSum2,");
		appendSql.append(tab1+"monSumC as monSum3 ");
		appendSql.append("from (select "+tab1_1+CusCorCusDAOImpl.CUS_ID_DB+",");
		appendSql.append(tab1_1+CusCorCusDAOImpl.CUS_NAME_DB+",");
		appendSql.append(tab1_1+CusCorCusDAOImpl.CON_EMP_ID_DB+",");
		appendSql.append("case when "+tab1_2+"monSum is null then 0 else "+tab1_2+"monSum end as monSumA,");
		appendSql.append("case when "+tab1_3+"monSum is null then 0 else "+tab1_3+"monSum end as monSumB,");
		appendSql.append("case when "+tab1_4+"monSum is null then 0 else "+tab1_4+"monSum end as monSumC ");
		appendSql.append("from crm_cus_cor_cus as cus ");
		appendSql.append("left join(select "+tab1_2_1+CusCorCusDAOImpl.CUS_ID_DB+", sum("+tab1_2_2+ORD_MON_DB+") as monSum ");
		appendSql.append("from crm_cus_cor_cus as cus1 ");
		appendSql.append("inner join (select "+ORD_MON_DB+","+ORD_CUS_ID_DB+" from sal_ord_con ");
		appendSql.append((hasAppend?"where "+GetDate.getDateHql(startDate,endDate,ORD_CON_DATE_DB):""));
		appendSql.append(") as ord on "+tab1_2_1+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_2_2+ORD_CUS_ID_DB+" ");
		appendSql.append("group by "+tab1_2_1+CusCorCusDAOImpl.CUS_ID_DB);
		appendSql.append(") as salTab1 on "+tab1_2+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_1+CusCorCusDAOImpl.CUS_ID_DB+" ");
		appendSql.append("left join(select "+tab1_3_1+CusCorCusDAOImpl.CUS_ID_DB+", sum("+tab1_3_2+ProdShipmentDAOImpl.PSH_PROD_AMT_DB+") as monSum ");
		appendSql.append("from crm_cus_cor_cus as cus2 ");
		appendSql.append("inner join (select "+tab1_3_2_1+ORD_CUS_ID_DB+","+tab1_3_2_2+ProdShipmentDAOImpl.PSH_PROD_AMT_DB+" from sal_ord_con as sorder ");
		appendSql.append("inner join prod_shipment as ship on "+tab1_3_2_1+ORD_ID_DB+" = "+tab1_3_2_2+ProdShipmentDAOImpl.PSH_ORD_ID_DB+" ");
		appendSql.append((hasAppend?"where "+GetDate.getDateHql(startDate,endDate,tab1_3_2_2+ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):""));
		appendSql.append(") as ordShip on "+tab1_3_1+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_3_2+ORD_CUS_ID_DB+" "); 
		appendSql.append("group by "+tab1_3_1+CusCorCusDAOImpl.CUS_ID_DB+" ");
		appendSql.append(")  as salTab2 on "+tab1_3+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_1+CusCorCusDAOImpl.CUS_ID_DB+" ");
		appendSql.append("left join(select "+tab1_4_1+CusCorCusDAOImpl.CUS_ID_DB+",sum("+tab1_4_2+SalPaidPastDAOImpl.SPS_MON_DB+") as monSum ");
		appendSql.append("from crm_cus_cor_cus as cus3 ");
		appendSql.append("inner join (select "+SalPaidPastDAOImpl.SPS_MON_DB+","+SalPaidPastDAOImpl.SPS_CUS_ID_DB+" from sal_paid_past ");
		appendSql.append((hasAppend?"where "+GetDate.getDateHql(startDate,endDate,SalPaidPastDAOImpl.SPS_FCT_DATE_DB):""));
		appendSql.append(") as spaid on "+tab1_4_1+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_4_2+SalPaidPastDAOImpl.SPS_CUS_ID_DB+" "); 
		appendSql.append("group by "+tab1_4_1+CusCorCusDAOImpl.CUS_ID_DB);
		appendSql.append(") as salTab3 on "+tab1_4+CusCorCusDAOImpl.CUS_ID_DB+" = "+tab1_1+CusCorCusDAOImpl.CUS_ID_DB);
		appendSql.append(") as salTabSum ");
		appendSql.append("left join (select "+SalEmpDAOImpl.SE_NO_DB+","+SalEmpDAOImpl.SE_NAME_DB+" from sal_emp) as emp ");
		appendSql.append("on "+tab2+SalEmpDAOImpl.SE_NO_DB+"="+tab1+CusCorCusDAOImpl.CON_EMP_ID_DB+" ");
		appendSql.append("where "+tab1+"monSumA+"+tab1+"monSumB+"+tab1+"monSumC>0 ");
		if(!StringFormat.isEmpty(empIds)){
			appendSql.append("and ");
			appendSql.append(tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" in ("+empIds+")" );
		}
		if(!StringFormat.isEmpty(cusIds)){
			appendSql.append("and ");
			appendSql.append(tab1 + CusCorCusDAOImpl.CUS_ID_DB + " in ("+cusIds+")");
		}
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("empName",Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE)
			.addScalar("monSum2",Hibernate.DOUBLE)
			.addScalar("monSum3",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
	
	/**
	 * �������ͳ�� <br>
	 * @param args [0][1]ǩ������
	 * @return List<StatOrd> ͳ�ƽ��
	 */
	public List<StatOrd> statOrdType(String[]args) {
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ tab + ORD_TYPE_NAME + ","
				+ "count(" + tab + ORD_ID + "),"
				+ "sum(" + tab + ORD_MON + ")) "
				+ "from SalOrdCon as ord left join "+tab+ORD_TYPE+" ");
		appendSql.append("where "+tab+ORD_ISDEL+"='0'");
		appendSql.append("and "+tab+ORD_ISAPP+"='1' ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
	    		appendSql.append("and ");
	    		appendSql.append(tab+ORD_CON_DATE+" >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append(tab + ORD_CON_DATE + " between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append(tab + ORD_CON_DATE + " <= '"
						+ args[1] + "' ");
			}
	    }
		appendSql.append("group by "+ tab + ORD_TYPE_NAME);
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}
	/**
	 * ������Դͳ�� <br>
	 * @param args [0][1]ǩ������
	 * @return List<StatOrd> ͳ�ƽ��
	 */
	public List<StatOrd> statOrdSou(String[]args) {
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab = "ord.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ tab + ORD_SOU_NAME + ","
				+ "count(" + tab + ORD_ID + "),"
				+ "sum(" + tab + ORD_MON + ")) "
				+ "from SalOrdCon as ord left join "+tab+ORD_SOU+" ");
		appendSql.append("where "+tab+ORD_ISDEL+"='0' ");
		appendSql.append("and "+tab+ORD_ISAPP+"='1' ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
	    		appendSql.append("and ");
	    		appendSql.append(tab+ORD_CON_DATE+" >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append(tab + ORD_CON_DATE + " between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append(tab + ORD_CON_DATE + " <= '"
						+ args[1] + "' ");
			}
	    }
		appendSql.append("group by "+ tab + ORD_SOU_NAME);
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}
	
	/**
	 * ������Ա�¶ȷֲ�ͳ�� <br>
	 * @param monArray �·ݼ���
	 * @param args [0]Ա��ID����
	 * @return StaTable ͳ�ƽ��
	 */
	public StaTable statOrdEmpMon(String[]monArray,String[]args){
		StringBuffer appendSql = new StringBuffer("");
		appendSql.append("select "+EMP_ID_DB+",max("+EMP_NAME_DB+") as 'head',");
		for(int i=0; i<monArray.length; i++){
			appendSql.append("sum(case when date_format(" + ORD_CON_DATE_DB + ",'%Y-%m') = '"
					+ monArray[i]+ "' "
					+ "then 1 else 0 end ) as '" + monArray[i] + "',");
			appendSql.append("sum(case when date_format("
					+ ORD_CON_DATE_DB + ",'%Y-%m') = '" + monArray[i]
					+ "' then "+ORD_MON_DB+" else 0 end ) as '" + monArray[i]
					+ "',");
		}
		String sql = appendSql.toString().substring(0,appendSql.length()-1)
					+" from sal_ord_con inner join sal_emp on "+ORD_EMP_DB+"="+EMP_ID_DB+" ";
		sql+="where "+ORD_ISDEL_DB+"='0' and "+ORD_ISAPP_DB+"='1' ";
		if(args[0] != null && !args[0].equals("")){
			sql+="and "+EMP_ID_DB+" in ("+args[0]+") ";
		}
		sql+=" group by "+EMP_ID_DB+" with rollup";
		return OperateList.getStaTab(sql,true);
	}
	/**
	 * �����¶�ͳ�� <br>
	 * @param args [0,1]ǩ�������·����;  [2]Ա��ID;
	 * @return List<StatOrd> ͳ�ƽ��
	 */
	public List<StatOrd> statOrdMon(String[]args){
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer("");
		String tab = "ord.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ "max("+tab + ORD_CON_DATE + "),"
				+ "count(" + tab + ORD_ID + "),"
				+ "sum(" + tab + ORD_MON + ")) "
				+ "from SalOrdCon as ord ");
		appendSql.append("where "+tab+ORD_ISDEL+"='0'");
		appendSql.append("and "+tab+ORD_ISAPP+"='1' ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
	    		appendSql.append("and ");
	    		appendSql.append("date_format("+tab+ORD_CON_DATE+",'%Y-%m') >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append("date_format("+tab + ORD_CON_DATE + ",'%Y-%m') between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append("date_format("+tab + ORD_CON_DATE + ",'%Y-%m') <= '"
						+ args[1] + "' ");
			}
	    }
		if(args.length>2){
			if(args[2]!=null && !args[2].equals("")){
				appendSql.append("and ");
				appendSql.append(tab+ORD_EMP_ID +" in ("+args[2]+") ");
			}
		}
		
		appendSql.append("group by date_format("+ tab + ORD_CON_DATE + ",'%Y-%m') ");
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}
	
	/**
	 * �����¶�ͳ�ƣ��ͻ������£� <br>
	 * @param args [0,1]ǩ�������·����;  [2]�ͻ�ID;
	 * @return List<StatOrd> ͳ�ƽ��
	 */
	public List<StatOrd> statCusOrdMon(String[] args) {
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer("");
		String tab = "ord.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ "max("+tab + ORD_CON_DATE + "),"
				+ "count(" + tab + ORD_ID + "),"
				+ "sum(" + tab + ORD_MON + ")) "
				+ "from SalOrdCon as ord ");
		appendSql.append("where "+tab+ORD_ISDEL+"='0'");
		appendSql.append("and "+tab+ORD_ISAPP+"='1' ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
	    		appendSql.append("and ");
	    		appendSql.append("date_format("+tab+ORD_CON_DATE+",'%Y-%m') >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append("date_format("+tab + ORD_CON_DATE + ",'%Y-%m') between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
				appendSql.append("and ");
				appendSql.append("date_format("+tab + ORD_CON_DATE + ",'%Y-%m') <= '"
						+ args[1] + "' ");
			}
	    }
		if(args.length>2){
			if(args[2]!=null && !args[2].equals("")){
					appendSql.append("and ");
					appendSql.append(tab+ORD_CUS_ID +" = "+args[2]+" ");
			}
		}
		
		appendSql.append("group by date_format("+ tab + ORD_CON_DATE + ",'%Y-%m') ");
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}
	
	/**
	 * ����̨����ҵ��ͼ��hql<br>
	 * @param empId Ա��id
	 * @param date1 ǩ��ʱ����ʼʱ��
	 * @param date2 ǩ��ʱ�����ʱ��
	 * @return String hql���<br>
	 */
	public String lastSalStat(Long empId,String date1,String date2){
		StringBuffer hql=new StringBuffer("");
		if(empId!=null&&!empId.equals("")){
			hql.append(" and s.salEmp.seNo ="+empId);
		}
		if(date1!=null){
			hql.append(" and date_format(s.sodConDate,'%Y-%m') between '"+date1+"' and '"+date2+"'");
		}
		return hql.toString();
	}
	
	/**
	 * ����ھ��ѯ���<br>
	 * @param statType ����
	 * @param orderType ��������
	 * @param ordConDate ǩ��ʱ��
	 * @param user Ա��id
	 * @return String hql���<br>
	 */
	private String hqlByStat(String statType,String orderType,String ordConDate,String user){
		StringBuffer hql=new StringBuffer("from SalOrdCon as s where s.sodIsfail = '0' and s.sodAppIsok='1'");
		if(statType!=null){
			if(statType.equals("ordType")){
				if(orderType.equals(""))
					hql.append("and s.salOrderType.typId is null ");
				else
					hql.append("and s.salOrderType.typId ="+Long.parseLong(orderType));
			}
			if(ordConDate!=null&&!ordConDate.equals("")){
				hql.append("and date_format(s.sodConDate,,'%Y-%m') ='"+ordConDate+"'" );
			}
		}
		if(user!=null&&!user.equals("")){
			hql.append("and s.salEmp.seNo ="+Long.valueOf(user));
		}
		return hql.toString();
	}
	public int searchByStatCount(String statType,String orderType,String ordConDate,String user,String queryString){
		Session session = super.getSession();
			
		String hql="select count(*) "+hqlByStat(statType,orderType,ordConDate,user)+queryString;
		Query query = session.createQuery(hql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		
		
		return count;
	}
	 public List<SalOrdCon> searchByStat(String statType,String orderType,String ordConDate,String user,String queryString,int currentPage, int pageSize) {
		Session session = super.getSession();
			
		String sql=hqlByStat(statType,orderType,ordConDate,user)+queryString;
		String hql=sql+" order by s.sodInpDate desc";
		Query query=session.createQuery(hql);	
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

//----- old ------- 
	/**
	 * �����߼���ѯ<br>
	 * create_date: Aug 12, 2010,1:42:24 PM<br>
	 * @param hql hql���
	 * @param isAll ��ʶ��ѯ��Χ0��ʾ��ѯ�ҵĶ���1��ʾ��ѯ�ҵ���������2��ʾȫ������
	 * @param userNum �û�id��������
	 * @param sodAppIsok �����Ƿ�ͨ��0��δͨ��1��ͨ��
	 * @param pageCurNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ��¼��
	 * @return List<SalOrdCon> ������¼�б�<br>
	 */
	public List<SalOrdCon> ordSupSearch(String hql,String isAll,String userNum,String sodAppIsok,int pageCurNo,int pageSize){
		Session session = super.getSession();
		StringBuffer sql=new StringBuffer("");
		if(isAll!=null&&(isAll.equals("0")||isAll.equals("1"))){
			sql.append("and limUser.userNum like '%" + userNum+"'");
	    }
		if(userNum!=null&&!userNum.equals("")){
			sql.append("and limUser.userNum like '%" + userNum+"'");
		}
		if(sodAppIsok!=null&&!sodAppIsok.equals("")){
			if(sodAppIsok.equals("0"))
				sql.append("and (s.sodAppIsok is null or s.sodAppIsok ='0')");
			else
				sql.append("and s.sodAppIsok ='1'");
		}
		String queryString = "select s from SalOrdCon as s where s.sodIsfail='0' "+hql+sql+" order by s.sodInpDate desc";
		Query query = session.createQuery(queryString);
		query.setFirstResult((pageCurNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = query.list();
		return list;
	}
	public int getSupCount(String hql,String isAll,String userNum,String sodAppIsok){
		Session session = super.getSession();
		StringBuffer sql=new StringBuffer("");
		if(isAll!=null&&(isAll.equals("0")||isAll.equals("1"))){
			sql.append("and limUser.userNum like '%" + userNum+"'");
	    }
		if(sodAppIsok!=null&&!sodAppIsok.equals("")){
			if(sodAppIsok.equals("0"))
				sql.append("and (s.sodAppIsok is null or s.sodAppIsok ='0')");
			else
				sql.append("and s.sodAppIsok ='1'");
		}
		String queryString="select count(*) from SalOrdCon as s where s.sodIsfail = '0'"+hql+sql;
		Query query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * ��������·�ͳ��<br>
	 * create_date: Aug 12, 2010,2:18:48 PM<br>
	 * @param hql hql���
	 * @return List ������¼�б�<br>
	 */
	public List sumMonByDate(String hql) {
		log.debug("finding all SalOrdCon instances");
		List list=new ArrayList();
		try {
			String queryString = "select sum(s.sodSumMon),month(s.sodConDate),year(s.sodConDate)"
				+" from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1'"+hql+" group by month(s.sodConDate),year(s.sodConDate)";
			List list1=getHibernateTemplate().find(queryString);
			for(int i=0;i<list1.size();i++)
			{
				Object[] object=(Object[])list1.get(i);
				OrdStatistic st=new OrdStatistic();
				st.setDnum(Double.parseDouble(object[0].toString()));
				String month="";
				if(Integer.parseInt(object[1].toString())<10){
					month="0"+object[1].toString();
				}else{
					month=object[1].toString();
				}
				st.setName(object[2].toString()+"-"+month);
				st.setType("3");
				list.add(st);
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		return list;
	}
	
	/**
	 * ���20��ͻ�����<br>
	 * create_date: Aug 12, 2010,2:19:21 PM<br>
	 * @param type ������ͣ��ؿ���������ܽ�
	 * @return List<OrdStatistic> OrdStatistic��¼�б�<br>
	 */
	public List<OrdStatistic> topSalCus(String type){
		int curYear = GetDate.getCurYear();
		Session session = super.getSession();
		String mon="";
		if(type!=null&&type.equals("sod")){
			mon="s.sodSumMon";
		}
		if(type!=null&&type.equals("paid")){
			mon="s.sodPaidMon";
		}
		String hql="select new com.psit.struts.entity.OrdStatistic(c.corCode,sum("+mon+"),max(c.corName),'2')" +
				"from SalOrdCon as s join s.cusCorCus as c where s.sodIsfail='0' and s.sodAppIsok='1'" +
				"and year(s.sodConDate) = '"+curYear+"'"+
				"group by c.corCode,c.corName order by sum("+mon+") desc";
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(20);
		List list = query.list();
		return list;
	}
	 
	/**
	 * ʵ������ͳ�ƣ���������--��Ч<br>
	 * create_date: Aug 12, 2010,12:04:05 PM<br>
	 * @param range ��ѯ��Χ��1������ѯ�û���2���û�id����������3���û�id��
	 * @param userSearch �û�id
	 * @param tarName ��ѯ�?1��SalPaidPast��2��SalOrdCon��
	 * @param tarYear ����
	 * @return List<OrdStatistic> ��¼�б�<br>
	 */
	public List<OrdStatistic> ordSumByFct(String range,String userSearch,String tarName,String tarYear) {
			List list=new ArrayList();
			String queryString="";
			String hqlHead="";
			String hql="";
			if(tarName!=null&&tarName.equals("2")){
				if(range!=null&&range.equals("1"))
					hql="";
				if(range!=null&&range.equals("2"))
					hql=" and s.limUser.userNum like  '%"+userSearch+"'";
				if(range!=null&&range.equals("3"))
					hql=" and s.limUser.userCode = '"+userSearch+"'";
				
				hqlHead="select sum(s.sodSumMon),s.limUser.userCode,month(s.sodConDate)";
				queryString=hqlHead+" from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1'"+hql+"and year(s.sodConDate)='"+tarYear+"'"
				+"group by s.limUser.userCode,month(s.sodConDate)";
			}
			if(tarName!=null&&tarName.equals("1")){
				if(range!=null&&range.equals("1"))
					hql="";
				if(range!=null&&range.equals("2"))
					hql=" and spsResp.userNum like  '%"+userSearch+"'";
				if(range!=null&&range.equals("3"))
					hql=" and spsResp.userCode = '"+userSearch+"'";
				hqlHead="select sum(spsPayMon),salOrdCon.limUser.userCode,month(spsFctDate)";
				queryString=hqlHead+" from SalPaidPast where salOrdCon.sodIsfail='0' and salOrdCon.sodAppIsok='1'"+hql+"and year(spsFctDate)='"+tarYear+"'"
				+"group by spsResp.userCode,month(spsFctDate)";
			}
			if(tarName!=null&&tarName.equals("3")){
				if(range!=null&&range.equals("1"))
					hql="";
				if(range!=null&&range.equals("2"))
					hql=" and s.limUser.userNum like  '%"+userSearch+"'";
				if(range!=null&&range.equals("3"))
					hql=" and s.limUser.userCode = '"+userSearch+"'";
				hqlHead="select count(distinct s.cusCorCus.corCode),s.limUser.userCode,month(s.sodConDate)";
				queryString=hqlHead+" from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1'"+hql+"and  year(s.sodConDate)='"+tarYear+"'"
				+"group by s.limUser.userCode,month(s.sodConDate)";
			}
			List list1=getHibernateTemplate().find(queryString);
			for(int i=0;i<list1.size();i++)
			{
				Object[] object=(Object[])list1.get(i);
				OrdStatistic st=new OrdStatistic();
				
				if(tarName!=null){
					if(tarName.equals("3"))
						st.setLnum(Long.parseLong(object[0].toString()));
					else
						st.setDnum(Double.parseDouble(object[0].toString()));
				}
				st.setName(object[1].toString());
				//ƴ������
				Integer month=Integer.parseInt(object[2].toString());
				String date="";
				if(month<10)
					date=tarYear+"-0"+month;
				else
					date=tarYear+"-"+month;
				st.setType(date);
				list.add(st);
			}
			return list;
	}
	
	/**
	 * ʵ�����ۺϼƣ������ˣ�<br>
	 * create_date: Aug 12, 2010,12:04:42 PM<br>
	 * @param uCode Ա��id
	 * @param tarName ��ѯ�?1��SalPaidPast��2��SalOrdCon��
	 * @param tarYear ����
	 * @return List<OrdStatistic> ��¼�б�<br>
	 */
	public List<OrdStatistic> allOrdSumByFct(String uCode,String tarName,String tarYear) {
			List<OrdStatistic> list=new ArrayList();
			String queryString="";
			String hqlHead="";
			String hql="";
			if(tarName!=null&&tarName.equals("2")){
				if(uCode!=null&&!uCode.equals(""))
					hql=" and s.salEmp.seNo in ("+uCode+")";
				hqlHead="select new com.psit.struts.entity.OrdStatistic(sum(s.sodSumMon),s.salEmp.seNo,'')";
				queryString=hqlHead+" from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1' "+hql+"and year(s.sodConDate)='"+tarYear+"'"
				+"group by s.salEmp.seNo";
			}
			if(tarName!=null&&tarName.equals("1")){
				if(uCode!=null&&!uCode.equals(""))
					hql=" and salOrdCon.salEmp.seNo in ("+uCode+")";
				hqlHead="select new com.psit.struts.entity.OrdStatistic(sum(spsPayMon),salOrdCon.salEmp.seNo,'')";
				queryString=hqlHead+" from SalPaidPast where salOrdCon.sodIsfail='0' "+hql+"and year(spsFctDate)='"+tarYear+"'"
				+"group by salOrdCon.salEmp.seNo";
			}
			if(tarName!=null&&tarName.equals("3")){
				if(uCode!=null&&!uCode.equals(""))
				hql=" and s.salEmp.seNo in ("+uCode+")";
				hqlHead="select new com.psit.struts.entity.OrdStatistic(count(s),s.salEmp.seNo,'')";
				queryString=hqlHead+" from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1' "+hql+"and  year(s.sodConDate)='"+tarYear+"'"
				+"group by s.salEmp.seNo";
			}
			 list=getHibernateTemplate().find(queryString);
			return list;
	}
	
	/**
	 * ʵ�����ۺϼƣ����꣩<br>
	 * create_date: Aug 12, 2010,12:05:10 PM<br>
	 * @param uCode Ա��id
	 * @param tarName ��ѯ�?1��SalPaidPast��2��SalOrdCon��
	 * @param tarYear ����
	 * @return List<OrdStatistic> ��¼�б�<br>
	 */
	public List<OrdStatistic> allOrdSumByFct2(String uCode, String tarName,
			String tarYear) {
		List list = new ArrayList();
		String queryString = "";
		String hqlHead = "";
		String hql = "";
		if (tarName != null && tarName.equals("2")) {
			if (uCode != null && !uCode.equals(""))
				hql = " and s.salEmp.seNo in (" + uCode + ")";
			hqlHead = "select sum(s.sodSumMon),month(s.sodConDate)";
			queryString = hqlHead
					+ " from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1' "
					+ hql + " and year(s.sodConDate)='" + tarYear + "'"
					+ "group by month(s.sodConDate)";
		}
		if (tarName != null && tarName.equals("1")) {
			if (uCode != null && !uCode.equals(""))
				hql = " and salOrdCon.salEmp.seNo in (" + uCode + ")";
			hqlHead = "select sum(spsPayMon),month(spsFctDate)";
			queryString = hqlHead
					+ " from SalPaidPast where salOrdCon.sodIsfail='0' "
					+ hql + " and year(spsFctDate)='" + tarYear + "'"
					+ "group by month(spsFctDate)";
		}
		if (tarName != null && tarName.equals("3")) {
			if (uCode != null && !uCode.equals(""))
				hql = " and s.salEmp.seNo in (" + uCode + ")";
			hqlHead = "select count(s),month(s.sodConDate)";
			queryString = hqlHead
					+ " from SalOrdCon as s where s.sodIsfail='0' and s.sodAppIsok='1' "
					+ hql + " and  year(s.sodConDate)='" + tarYear + "'"
					+ "group by month(s.sodConDate)";
		}
		List list1 = getHibernateTemplate().find(queryString);
		for (int i = 0; i < list1.size(); i++) {
			Object[] object = (Object[]) list1.get(i);
			OrdStatistic st = new OrdStatistic();
			if (tarName != null) {
				if (tarName.equals("3"))
					st.setLnum(Long.parseLong(object[0].toString()));
				else
					st.setDnum(Double.parseDouble(object[0].toString()));
			}
			// ƴ������
			Integer month = Integer.parseInt(object[1].toString());
			String date = "";
			if (month < 10)
				date = tarYear + "-0" + month;
			else
				date = tarYear + "-" + month;
			st.setName(date);
			list.add(st);
		}
		return list;
	}
}