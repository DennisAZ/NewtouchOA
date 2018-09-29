package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalPaidPastDAO;
import com.psit.struts.entity.SalInvoice;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.OperateList;
import com.psit.struts.util.format.StringFormat;

/**
 * �ؿ��¼��ݿ����ʵ����<br>
 */
public class SalPaidPastDAOImpl extends HibernateDaoSupport implements SalPaidPastDAO {
	//property constants
	static final String SPS_ID = "spsId";
	static final String SPS_ISINV = "spsIsinv";
	static final String SPS_MON = "spsPayMon";
	static final String SPS_PAY_TYPE = "spsPayType";
	static final String SPS_FCT_DATE = "spsFctDate";
//	static final String SPS_CUS = "cusCorCus";
	static final String SPS_CUS_ID = "cusCorCus.corCode";
	static final String SPS_CUS_NAME = "cusCorCus.corName";
	static final String SPS_ORD = "salOrdCon";
	static final String SPS_ORD_ID = "salOrdCon.sodCode";
	static final String SPS_ORD_TIL = "salOrdCon.sodTil";
	static final String SPS_USER_NAME = "salEmp.seName";
	static final String SPS_USER_ID = "salEmp.seNo";
	
	static final String SPS_ISDEL_DB = "sps_isdel";
	static final String SPS_FCT_DATE_DB = "sps_fct_date";
	static final String SPS_MON_DB = "sps_pay_mon";
	static final String SPS_USER_DB = "sps_se_no";
	static final String SPS_CUS_ID_DB = "sps_cus_id";
	
	static final String EMP_ID_DB = "se_no";
	static final String EMP_NAME_DB = "se_name";
	static final String EMP_CODE_DB = "se_code";
	
	protected void initDao() {
		// do nothing
	}
	
	/**
	 * ���¶����ؿ��ֶ� <br>
	 * @param paid		�����¼
	 * @param session  
	 * @param isUpd		�Ƿ�Ϊ�������
	 */
	private void updatePaidAmt(SalPaidPast paid, double oldPayMon, Session session,int opType){
		if(paid!=null){
			double paidSum = (paid.getSpsPayMon()!=null)?paid.getSpsPayMon():0;
			switch(opType){
			case 0:break;//�½�
			case 1://����
				if(oldPayMon>0){
					paidSum = paidSum - oldPayMon; 
				}
				break;
			case 2://ɾ��
				paidSum = -paidSum;
				break;
			}
			if(paid.getSalOrdCon()!=null){
				/*String monSum;
				String hql = "select sum("+SPS_MON+") from SalPaidPast where "
						+ SPS_ORD_ID+"=" + paid.getSalOrdCon().getSodCode()
						+ " and "+SPS_ISDEL+"='0'";
				Query query = session.createQuery(hql);
				List list = (List) query.list();
				if(list.get(0)!=null){
					monSum=list.get(0).toString();
				}
				else{
					monSum="0.0";
				}*/
		        String hql = "update SalOrdCon set "+SalOrderDAOImpl.ORD_PAID+" = "+SalOrderDAOImpl.ORD_PAID+"+(" + paidSum + ") where "+SalOrderDAOImpl.ORD_ID+" = " + paid.getSalOrdCon().getSodCode();
				session.createQuery(hql).executeUpdate();
			}
			if(paid.getCusCorCus()!=null){
				String hql = "update CusCorCus set "+CusCorCusDAOImpl.CUS_RECV_AMT+" = "+CusCorCusDAOImpl.CUS_RECV_AMT+"-(" + paidSum + ") where "+CusCorCusDAOImpl.CUS_ID+" = " + paid.getCusCorCus().getCorCode();
				session.createQuery(hql).executeUpdate();
				String rectAmt = "";
				String hql1 = "select "+CusCorCusDAOImpl.CUS_RECV_AMT+" from CusCorCus where "
						+ CusCorCusDAOImpl.CUS_ID+" = " + paid.getCusCorCus().getCorCode()
						+ " and "+CusCorCusDAOImpl.CUS_ISDEL+"='1'";
				Query query = session.createQuery(hql1);
				List list = (List) query.list();
				if(list.get(0)!=null){
					rectAmt=list.get(0).toString();
				}
				if(rectAmt.equals("0.0")){
					String hql2 = "update CusCorCus set "+CusCorCusDAOImpl.CUS_REC_DATE +" = null where "+CusCorCusDAOImpl.CUS_ID+" = " + paid.getCusCorCus().getCorCode();
					session.createQuery(hql2).executeUpdate();
				}
			}
		}
	}
	
	
		/**
	 * ���¿�Ʊ��� <br>
	 * @param SalPaidPast paid �ؿ��¼
	 * @param session  
	 */
	private void updateTicketNum(SalPaidPast paid,double oldSin,Session session,int type){
		if(paid!=null){
			double paidSum = (paid.getSpsPayMon()!=null)?paid.getSpsPayMon():0;
			switch(type){
			case 0:break;//�½�
			case 1:
				if(oldSin>0){
					paidSum = paidSum - oldSin; 
				}
				break;//����
			case 2:
				paidSum = -paidSum;
				break;//ɾ��
			}
			if(paid.getCusCorCus()!=null){
				String hql = "update CusCorCus set "+CusCorCusDAOImpl.COR_TICKET_NUM+" = "+CusCorCusDAOImpl.COR_TICKET_NUM+"-(" + paidSum + ") where "+CusCorCusDAOImpl.CUS_ID+" = " + paid.getCusCorCus().getCorCode();
				session.createQuery(hql).executeUpdate();
			}
		}
	}
	
	
	/**
	 * ����ؿ��¼�����¶����ѻؿ�<br>
	 * @param paid �ؿ��¼����<br>
	 */
	public void save(SalPaidPast paid) {
		Session session=super.getSession();
		updatePaidAmt(paid,0.0,session,0);
		updateTicketNum(paid,0.0,session,0);
		session.save(paid);
	}
	
	/**
	 * ���»ؿ��¼�����¶����ѻؿ�<br>
	 * @param paid �ؿ�ƻ���¼
	 */
	public void updatePaid(SalPaidPast paid, double oldPayMon){
		Session session=super.getSession();
		updatePaidAmt(paid,oldPayMon,session,1);
		updateTicketNum(paid,oldPayMon,session,1);
		session.merge(paid);
	}
	
	/**
	 * ��������վ�����¶����ѻؿ�<br>
	 * @param paidId �ؿ��¼id
	 * @param ordId ����ID
	 */
//	public void invalid(SalPaidPast paid){
//		if(paid!=null){
//			Session session=super.getSession();
//			String sql = "update SalPaidPast set "+SPS_ISDEL+" = '1' where "+SPS_ID+" = " + paid.getSpsId() + " ";
//			session.createQuery(sql).executeUpdate();
//			if(paid.getSalOrdCon()!=null){
//				updateOrdPaid(paid,session);
//			}
//		}
//	}
	/**
	 * �ָ��ؿ��¼�����¶����ѻؿ� <br>
	 * @param paidId �ؿ��¼ID
	 * @param ordId ����ID
	 */
//	public void recovery(SalPaidPast paid){
//		Session session=super.getSession();
//		if(paid!=null){
//			String sql = "update SalPaidPast set "+SPS_ISDEL+" = '0' where "+SPS_ID+" = " + paid.getSpsId() + " ";
//			session.createQuery(sql).executeUpdate();
//			if(paid.getSalOrdCon()!=null){
//				updateOrdPaid(paid,session);
//			}
//		}
//	}
	
	/**
	 * ����ɾ��<br>
	 * @param paid �ؿ��¼����<br>
	 */
	public void delete(SalPaidPast paid) {
		Session session=super.getSession();
		updatePaidAmt(paid,0.0,session,2);
		updateTicketNum(paid,0.0,session,2);
		getHibernateTemplate().delete(paid);
	}
	
	/**
	 * �õ�������ͬ�ѻؿ���<br>
	 * @param code ����id
	 * @return String �ؿ���<br>
	 */
	public String getTotalPaid(String code){
		long id=Long.parseLong(code);
		Query query;
		Session session;
		
		session=super.getSession();
		
		String result="";
		String sql="select sum(spsPayMon) from SalPaidPast where salOrdCon.sodCode="+id;
		query=session.createQuery(sql);
		if(query.list().get(0)==null){
			result="0.00";
		}
		else{
			result=query.list().get(0).toString();
		}
		return result;
	}
	
	/**
	 * ���²�ѯ�ѻؿ��ܶ�<br>
	 * @param userCode �û�id
	 * @param year �ؿ����ڣ��꣩
	 * @param month �ؿ����ڣ��£�
	 * @return String �ؿ���<br>
	 */
	public String getPastMonSum(String userCode, String year, String month) {
		String monSum;
		Session session = super.getSession();
		 
		String hql = "select sum(spsPayMon) from SalPaidPast where salEmp.seNo="
				+ userCode
				+ " and year(spsFctDate) = '"
				+ year
				+ "' and month(spsFctDate) ='" + month 
				+" and salOrdCon.sodIsfail = '0' and salOrdCon.sodAppIsok='1'";
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
	 * ���²�ѯ�¼��ѻؿ��ܶ�<br>
	 * @param empId Ա��id
	 * @param year �ؿ����ڣ��꣩
	 * @param month �ؿ����ڣ��£�
	 * @return String �ؿ���<br>
	 */
	public String getPastSumWithUNum(String empId, String year, String month) {
		String monSum;
		Session session = super.getSession();
		 
		String hql = "select sum(spsPayMon) from SalPaidPast where salEmp.seNo="
				+ empId
				+ " and year(spsFctDate) = '"
				+ year
				+ "' and month(spsFctDate) ='" + month + "' and spsIsdel='0'"
				+" and salOrdCon.sodIsfail = '0' and salOrdCon.sodAppIsok='1'";
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
	 * �õ��ѻؿ��ܶ�<br>
	 * @param empId Ա��id
	 * @return String �ؿ���<br>
	 */
	public String getPastAllSum(String empId){
		String monSum;
		Session session = super.getSession();
		String hql = "select sum(spsPayMon) from SalPaidPast where salEmp.seNo="
				+ empId
				+" and salOrdCon.sodIsfail = '0' and salOrdCon.sodAppIsok='1'";
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
	 * ���id��ûؿ��¼<br>
	 * @param id �ؿ��¼id
	 * @return SalPaidPast �ؿ��¼����<br>
	 */
	public SalPaidPast findById(Long id) {
		String sql="from SalPaidPast where spsId="+id;
		List list=super.getHibernateTemplate().find(sql);
		if(list.size()>0)
			return (SalPaidPast) list.get(0);
		else
			return null;
	}
	
	/**
	 * ���id��ûؿ��¼������վ���ã�<br>
	 * @param id �ؿ��¼id
	 * @return SalPaidPast �ؿ��¼����<br>
	 */
	public SalPaidPast getById(Long id)
	{
		return (SalPaidPast) super.getHibernateTemplate().get(SalPaidPast.class, id);
	}
	
	/**
	 * �ؿ��¼�б� <br>
	 * @param args 	[0]�Ƿ�ɾ��(1��ɾ��,0δɾ��);  [1]��ѯ��Χ(0�ҵ�,1ȫ��)/����ID;  [2]�û�ID;  [3][4]�ؿ����ڷ�Χ [5]��Ӧ�ͻ� [6]�ؿ���
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalPaidPast> �ؿ��¼�б�
	 */
	public List<SalPaidPast> listPaidPasts(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getPastSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalPaidPast> list = query.list();
		return list;
	}
	public int listPaidPastsCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getPastSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getPastSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "past.";//�����
		String sql = "from SalPaidPast past ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select past " + sql;
  		}
		if(args != null){
			if(args.length>3){
				if(args[0]!=null && !args[0].equals("")){
			    	if(args[0].equals("0")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+SPS_USER_ID +" = '"+args[1]+"' ");
			    	}
				}
			    if(args[2]!=null){
			    	if (!args[2].equals("") && args[3].equals("")) {
			    		appendSql.append("and ");
			    		appendSql.append(tab+SPS_FCT_DATE+" >= '"+ args[2] + "' ");
					} else if (!args[2].equals("") && !args[3].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPS_FCT_DATE + " between '"
								+ args[2] + "' and '"
								+ args[3]
								+ "' ");
					} else if (args[2].equals("") && !args[3].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPS_FCT_DATE + " <= '"
								+ args[3]
								+ "' ");
					}
			    }
			    if(args[4]!=null && !args[4].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+SPS_CUS_NAME+" like :cusName ");
			    }
			    if(args[5]!=null && !args[5].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+SPS_USER_NAME+" like :seName ");
			    }
			}
			else if(args.length==2){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+SPS_CUS_ID+"="+args[0]+" ");
			}
			else if(args.length == 3){
			    if(args[0]!=null && !args[0].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+SPS_CUS_ID+"="+args[0]+" ");
			    }
			    if(args[1]!=null){
			    	if (!args[1].equals("") && args[2].equals("")) {
			    		appendSql.append("and ");
			    		appendSql.append(tab+SPS_FCT_DATE+" >= '"+ args[1] + "' ");
					} else if (!args[1].equals("") && !args[2].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPS_FCT_DATE + " between '"
								+ args[1] + "' and '"
								+ args[2]
								+ "' ");
					} else if (args[1].equals("") && !args[2].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPS_FCT_DATE + " <= '"
								+ args[2]
								+ "' ");
					}
			    }
			}
		    else{
		    	if(args[0]!=null && !args[0].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SPS_ORD_ID +" = "+args[0]+" ");
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
			if(args.length>3){
				if(args[4]!=null && !args[4].equals("")){
					query.setString("cusName", "%"+args[4]+"%");
				}
				if(args[5]!=null && !args[5].equals("")){
					query.setString("seName", "%"+args[5]+"%");
				}
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
			String[] items = { "spsIsInv", "spsMon", "spsCus", "spsOrd", "spsPayType", "spsDate", "spsUser" };
			String[] cols = {  SPS_ISINV, SPS_MON, SPS_CUS_NAME, SPS_ORD_TIL,  SPS_PAY_TYPE, SPS_FCT_DATE, SPS_USER_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 3:
						joinSql += "left join " + tab + SPS_ORD + " ";
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
			sortSql = "order by "+tab+SPS_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	
	/**
	 * ��ѯ�ؿ��¼<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @param attSql sql���
	 * @param isdel �Ƿ���0�������ˣ�1����ɾ��2��δ���ˣ� 3���ѳ���
	 * @return List �ؿ��¼�б�<br>
	 */
	/*public List findPaidPast(int pageCurrentNo, int pageSize, String attSql, String isdel) {
		Session session = super.getSession();
		String hql ="from SalPaidPast ";
		if(isdel!=null&&!isdel.equals("")){
			hql += "where spsIsdel='" + isdel + "' ";
		}
		else {//ȫ��
			hql += "where spsIsdel!='1' ";
		}
		hql+= attSql + "order by spsFctDate desc, spsId desc ";
		Query query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		
		return list;
	}*/
	/**
	 * ��ѯ�ؿ��¼����<br>
	 * @param attSql sql���
	 * @param isdel �Ƿ���0�������ˣ�1����ɾ��2��δ���ˣ� 3���ѳ���
	 * @return int �ؿ��¼����<br>
	 */
	/*public int findPaidPastCount(String attSql, String isdel) {
		Session session = super.getSession();
		String hql ="select count(spsId) from SalPaidPast";
		if(isdel!=null&&!isdel.equals("")){
			hql += " where spsIsdel='" 
					+ isdel 
					+ "'" 
					+ attSql;
		}
		else{
			hql += " where spsIsdel!='1' " 
				+ attSql ;
		}
		Query query=session.createQuery(hql);	
		
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	} */
	
	/**
	 * ��ѯ��ɾ��Ļؿ��¼<br>
	 * create_date: Aug 12, 2010,3:26:48 PM<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List �ؿ��¼�б�<br>
	 */
	/*public List findDelPaidPast(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		
		session = super.getSession();
		
		String hql ="from SalPaidPast where spsIsdel='1' order by spsId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		
		
		return list;
	}*/

	/**
	 * ��ô�ɾ������лؿ��¼����<br>
	 * @return  �ؿ��¼����
	 */
	/*public int findDelPaidPastCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from SalPaidPast where  spsIsdel='1'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	} */
	
	/**
	 * ͳ���лؿ��¼�б� <br>
	 * create_date: Jan 31, 2011,11:53:30 AM <br>
	 * @param args 	[0]�Ƿ�ɾ��(1��ɾ��,0δɾ��);  [2]ͳ������; [3]ͳ�ƶ���; [4]Ա��ID����;  
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalPaidPast> �ؿ��¼�б�
	 */
	public List<SalPaidPast> listStatPaid(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getStatPaidSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalPaidPast> list = query.list();
		return list;
	}
	public int listStatPaidCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getStatPaidSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getStatPaidSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String[] statTypes = {"paidEmpMon","ordPaidMon" };
    	String[][] statCols = {
    			{ SPS_FCT_DATE, SPS_FCT_DATE },
    			{ SPS_FCT_DATE, SPS_FCT_DATE },
    		};
		StringBuffer appendSql = new StringBuffer();
		String tab = "past.";//�����
		String sql = "from SalPaidPast past ";
  		if(isCount){
  			sql = "select count(past) " + sql;
  		}
  		else{
  			sql = "select past " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
	    		appendSql.append("and ");
	    		appendSql.append(tab+SPS_USER_ID +" = '"+args[0]+"' ");
			}
			if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	for(int i = 0;i < statTypes.length; i++){
		    		if(args[1].equals(statTypes[i])){
		    			switch(i){
		    			case 0:
		    			case 1:
		    				appendSql.append("date_format("+tab+statCols[i][1]+",'%Y-%m') = '"+args[2]+"' ");
		    				break;
		    			}
		    			break;
		    		}
		    	}
		    }
			if(args.length>3){
				if(args[3]!=null && !args[3].equals("")){
					appendSql.append("and ");
			    	appendSql.append(tab+SPS_USER_ID+" in ("+args[3]+") ");
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
		return query;
	}
	
	/**
	 * �ؿ��¼��Ա�¶�ͳ�� <br>
	 * create_date: Feb 25, 2011,1:58:01 PM <br>
	 * @param monArray �·ݼ���
	 * @param args [0]Ա��ID����
	 * @return StaTable ͳ�ƽ��
	 */
	public StaTable statPaidEmpMon(String[]monArray,String[]args){
		StringBuffer appendSql = new StringBuffer("");
		appendSql.append("select "+EMP_ID_DB+",max("+EMP_CODE_DB+"),max("+EMP_NAME_DB+") as 'head',");
		for(int i=0; i<monArray.length; i++){
			appendSql.append("sum(case when date_format("
					+ SPS_FCT_DATE_DB + ",'%Y-%m') = '" + monArray[i]
					+ "' then "+SPS_MON_DB+" else 0 end ) as '" + monArray[i]
					+ "',");
		}
		String sql = appendSql.toString().substring(0,appendSql.length()-1) + " "
//					+"sum("+SPS_MON_DB+") as '�ϼ�' "
					+"from sal_paid_past inner join sal_emp on "+SPS_USER_DB+"="+EMP_ID_DB+" "
					+"where "+SPS_ISDEL_DB+"='0' ";
		if(args[0] != null && !args[0].equals("")){
			sql+="and "+EMP_ID_DB+" in ("+args[0]+") ";
		}
		sql+=" group by "+EMP_ID_DB+" with rollup";
		return OperateList.getStaTab(sql,true);
	}
	
	/**
	 * �ؿ��¼�¶�ͳ�� <br>
	 * @param args [0,1]�ؿ������·����; [2]�ͻ�ID 
	 * @return List<StatOrd> ͳ�ƽ��
	 */
	public List<StatOrd> statOrdMon(String[]args){
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer("");
		String tab = "sps.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ "max("+tab + SPS_FCT_DATE + "),"
				+ "sum(" + tab + SPS_MON + ")) "
				+ "from SalPaidPast as sps ");
		appendSql.append("where ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
//	    		appendSql.append("and ");
	    		appendSql.append("date_format("+tab+SPS_FCT_DATE+",'%Y-%m') >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
//				appendSql.append("and ");
				appendSql.append("date_format("+tab + SPS_FCT_DATE + ",'%Y-%m') between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
//				appendSql.append("and ");
				appendSql.append("date_format("+tab + SPS_FCT_DATE + ",'%Y-%m') <= '"
						+ args[1] + "' ");
			}
	    }
		
		appendSql.append("group by date_format("+ tab + SPS_FCT_DATE + ",'%Y-%m') ");
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}

	public List<StatOrd> statCusOrdMon(String[] args) {
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer("");
		String tab = "sps.";
		appendSql.append("select new com.psit.struts.entity.StatOrd("
				+ "max("+tab + SPS_FCT_DATE + "),"
				+ "sum(" + tab + SPS_MON + ")) "
				+ "from SalPaidPast as sps ");
		appendSql.append("where ");
		if(args[0]!=null){
	    	if (!args[0].equals("") && args[1].equals("")) {
//	    		appendSql.append("and ");
	    		appendSql.append("date_format("+tab+SPS_FCT_DATE+",'%Y-%m') >= '"+ args[0] + "' ");
			} else if (!args[0].equals("") && !args[1].equals("")) {
//				appendSql.append("and ");
				appendSql.append("date_format("+tab + SPS_FCT_DATE + ",'%Y-%m') between '"
						+ args[0] + "' and '" + args[1] + "' ");
			} else if (args[0].equals("") && !args[1].equals("")) {
//				appendSql.append("and ");
				appendSql.append("date_format("+tab + SPS_FCT_DATE + ",'%Y-%m') <= '"
						+ args[1] + "' ");
			}
	    }
		if(args.length>2){
			if(args[2]!=null && !args[2].equals("")){
				appendSql.append("and ");
				appendSql.append(tab+SPS_CUS_ID +" = "+args[2]+" ");
			}
		}
		
		appendSql.append("group by date_format("+ tab + SPS_FCT_DATE + ",'%Y-%m') ");
		Query query = session.createQuery(appendSql.toString());	
		List<StatOrd> list = query.list();
		return list;
	}
	
	/**
	 * �ؿ����ͳ��<br>
	 * @param startDate	��ʼ����
	 * @param endDate	��������
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSpsAnalyse(String startDate, String endDate){
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "cus.";
		String tab2 = "sps.";
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		
		appendSql.append("select "
				+ tab1 + CusCorCusDAOImpl.CUS_ID_DB + " as cusId,"
				+ tab1 + CusCorCusDAOImpl.CUS_NAME_DB + " as cusName,"
				+ "case when sum("+tab2+SPS_MON_DB + ") is null then 0 else sum("+tab2+SPS_MON_DB + ") end as monSum1,"
				+ "case when "+tab1 + CusCorCusDAOImpl.COR_RECV_AMT_DB+ " is null then 0 else "+tab1 + CusCorCusDAOImpl.COR_RECV_AMT_DB+" end  as monSum2,"
				+ "case when "+tab1 + CusCorCusDAOImpl.COR_RECV_MAX_DB + " is null then 0 else "+tab1 + CusCorCusDAOImpl.COR_RECV_MAX_DB+" end as monSum3,"
				+ "case when "+tab1 + CusCorCusDAOImpl.COR_BEGIN_BAL_DB + " is null then 0 else "+tab1 + CusCorCusDAOImpl.COR_BEGIN_BAL_DB+" end as monSum4, "
				+ "case when "+tab1 + CusCorCusDAOImpl.COR_TICKET_NUM_DB + " is null then 0 else "+tab1 + CusCorCusDAOImpl.COR_TICKET_NUM_DB+" end as monSum5 ");
		appendSql.append("from  sal_paid_past as sps left join crm_cus_cor_cus cus on "
				+ CusCorCusDAOImpl.CUS_ID_DB + "=" + SPS_CUS_ID_DB
				+ (hasAppend?" where "+GetDate.getDateHql(startDate,endDate,SPS_FCT_DATE_DB):" "));
		appendSql.append("group by "+tab1+CusCorCusDAOImpl.CUS_ID_DB );
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE)
			.addScalar("monSum2",Hibernate.DOUBLE)
			.addScalar("monSum3",Hibernate.DOUBLE)
			.addScalar("monSum4",Hibernate.DOUBLE)
			.addScalar("monSum5",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
}