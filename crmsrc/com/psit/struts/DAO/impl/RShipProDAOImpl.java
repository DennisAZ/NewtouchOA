package com.psit.struts.DAO.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RShipProDAO;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

public class RShipProDAOImpl extends HibernateDaoSupport implements RShipProDAO {
	private static final Log log = LogFactory.getLog(RShipProDAOImpl.class);
	
	static final String RSHP_ID = "rshpId";
	
	static final String RSHP_PROD_AMT = "rshpProdAmt";
	static final String RSHP_COUNT = "rshpCount";
	static final String PSHP_PRICE= "rshpPrice";
	static final String RSHP_SAL_BACK = "rshpSalBack";
	static final String RSHP_OUT_COUNT = "rshpOutCount";
	static final String RSHP_PSH_ID = "rshpShipment.pshId";
	static final String RSHP_PSH = "rshpShipment";
	static final String RSHP_PSH_DELIVERY_DATE = "rshpShipment.pshDeliveryDate";
	static final String RSHP_PROD_ID = "rshpProd.wprId";
	static final String RSHP_PROD_NAME = "rshpProd.wprName";
	static final String RSHP_PROD = "rshpProd";
	static final String PSHP_EMP_NO = "rshpShipment.pshOrder.salEmp.seNo";
	static final String RSHP_DELIVERY_DATE = "rshpShipment.pshDeliveryDate";
	
	static final String PSH_ID = "pshId";
	static final String PSH_ORD_ID = "pshOrder.sodCode";
	
	private final String tabName = "rshp";// �����
	private final String tab = tabName + ".";
	
	private final String RSHP_ID_DB = "rshp_id";
	private final String RSHP_SAL_BACK_DB = "rshp_sal_back";
	private final String RSHP_PSH_ID_DB = "rshp_psh_id";
	private final String RSHP_PROD_ID_DB = "rshp_prod_id";
	private final String RSHP_COUNT_DB = "rshp_count";
	private final String RSHP_PROD_AMT_DB = "rshp_prod_amt";
	private final String RSHP_OUT_COUNT_DB = "rshp_out_count";
	/**
	 * ��Ʒ���ۼ�¼�б� <br>
	 * @param prodId ��ƷID(=)
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<RShipPro>
	 */
	public List<RShipPro> listSalHistory(String prodId, int currentPage, int pageSize) {
		Session session = (Session) super.getSession();
		Query query = getSalHisQuery(session,false, prodId);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<RShipPro> list = query.list();
		return list;
	}

	public int listSalHistoryCount(String prodId) {
		Session session = (Session) super.getSession();
		Query query = getSalHisQuery(session, true, prodId);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	private Query getSalHisQuery(Session session, boolean isCount, String prodId) {
		StringBuffer appendSql = new StringBuffer();
		if (!StringFormat.isEmpty(prodId)) {
			appendSql.append("where ");
			appendSql.append(tab + RSHP_PROD_ID + " = "+prodId+" ");
		}
		String sql = "from RShipPro " + tabName + " ";
		if (isCount) {
			sql = "select count(*) " + sql;
		} else {
			sql = "select " + tabName + " " + sql;
		}
		if (!isCount) {
//			String joinSql = "";
			String sortSql = "";
			/*if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "pshNum", "pshOrd", "pshCnee", "pshAddr",
						"pshExp", "pshAmt", "pshDate", "pshShipper", "pshPostCode",
						"pshPho", "prodAmt", "salBack" };
				String[] cols = { PSH_NUM, PSH_ORD_NUM, PSH_CONSIGNEE, PSH_ADDR,
						PSH_EXPRESS, PSH_AMT, PSH_DELIVERY_DATE, PSH_SHIPPER,
						PSH_POST_CODE, PSH_PHO, PSH_PROD_AMT, PSH_SAL_BACK };
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch (i) {
						case 1:
							joinSql += "left join " + tab + PSH_ORDER + " ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				sortSql = "order by " + tab + orderItem + " ";
				if (isDe != null && isDe.equals("1")) {
					sortSql += "desc ";
				}
			} else {// Ĭ������
				sortSql = "order by " + tab + PSH_ID + " desc ";
			}*/
//			sql += joinSql;
			sortSql = "order by " + tab + RSHP_ID + " desc ";
			appendSql.append(sortSql);
		}
		sql += appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	/**
	 * ͳ�Ʋ�Ʒ��ɷ����б� <br>
	 * @param args [0]:Ա��Id,[1]:��ƷId,[2][3]:��������
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<RShipPro>
	 */
	public List<RShipPro> listShipPro(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<RShipPro> list = query.list();
		return list;
	}

	public int listShipProCount(String[]args) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	private Query getQuery(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "rshp.";
		String sql = "from RShipPro rshp ";
		if (isCount) {
			sql = "select count(*) " + sql;
		} else {
			sql = "select rshp " + sql;
		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+ PSHP_EMP_NO +" = '"+args[0]+"' ");
			}
			if(args[1]!=null && !args[1].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append(tab+ RSHP_PROD_ID +" = '"+args[1]+"' ");
			}
		    if(args[2]!=null){
		    	if (!args[2].equals("") && args[3].equals("")) {
		    		appendSql.append("and ");
		    		appendSql.append(tab+RSHP_DELIVERY_DATE+" >= '"+ args[2] + "' ");
				} else if (!args[2].equals("") && !args[3].equals("")) {
					appendSql.append("and ");
					appendSql.append(tab + RSHP_DELIVERY_DATE + " between '"
							+ args[2] + "' and '"
							+ args[3]
							+ "' ");
				} else if (args[2].equals("") && !args[3].equals("")) {
					appendSql.append("and ");
					appendSql.append(tab + RSHP_DELIVERY_DATE + " <= '"
							+ args[3]
							+ "' ");
				}
		    }
			
		}
		
		if (!isCount) {
			String joinSql = "";
			String sortSql = "";
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "wprName","deliveryDate","rshpCount","rshpProdAmt","rshpPrice","rshpSalBack","rshpOutCount"};
				String[] cols = { RSHP_PROD_NAME, RSHP_PSH_DELIVERY_DATE, RSHP_COUNT, RSHP_PROD_AMT,
						PSHP_PRICE, RSHP_SAL_BACK, RSHP_OUT_COUNT};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch (i) {
						case 0:
							joinSql += "left join " + tab + RSHP_PROD + " ";
							break;
						case 1:
							joinSql += "left join " + tab + RSHP_PSH + " ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				sortSql = "order by " + tab + orderItem + " ";
				if (isDe != null && isDe.equals("1")) {
					sortSql += "desc ";
				}
			} else {// Ĭ������
				sortSql = "order by " + tab + RSHP_ID + " desc ";
			}
			appendSql.append(sortSql);
		}
		sql += appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	public void save(RShipPro transientInstance) {
		log.debug("saving RShipPro instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public List<RShipPro> list(String pshId){
		Session session = (Session) super.getSession();
		String sql = "from RShipPro where "+RSHP_PSH_ID+"="+pshId;
		Query query = session.createQuery(sql);
		List<RShipPro> list = query.list();
		return list;
	}
	
	public List<RopShipForm> getRopShip(String ordId){
		Session session = (Session) super.getSession();
		String sql = "select new com.psit.struts.form.RopShipForm("+RSHP_PROD_ID+",sum("+RSHP_COUNT+")) " +
				"from RShipPro " +
				"where "+RSHP_PSH_ID+" in (select "+PSH_ID+" from ProdShipment where "+PSH_ORD_ID+"="+ordId+") " +
				"group by "+RSHP_PROD_ID;
		Query query = session.createQuery(sql);
		List<RopShipForm> list = query.list();
		return list;
	}
	
	
	/**
	 * ��Ʒ��ɷ���<br>
	 * @param startDate	��ʼ����
	 * @param endDate	��������
	 * @param wprId ��ƷId
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statPsalBack(String startDate, String endDate, String wprId){
		Session session = (Session) super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "pro.";
		String tab2 = "emp.";
		String tab3 = "ordShip.";
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		
		appendSql.append("select "
				+ tab2 + SalEmpDAOImpl.SE_NO_DB + " as cusId,"
				+ tab2 + SalEmpDAOImpl.SE_NAME_DB + " as empName,"
				+  "case when sum("+tab1+RSHP_SAL_BACK_DB + ") is null then 0 else sum("
				+tab1+RSHP_SAL_BACK_DB + ") end as monSum1 ");
		appendSql.append("from  r_ship_pro as pro inner join (select "
				+ SalOrderDAOImpl.ORD_EMP_DB +","
				+ ProdShipmentDAOImpl.PSH_ID_DB+" from prod_shipment inner join sal_ord_con on "
				+ SalOrderDAOImpl.ORD_ID_DB+" = "+ProdShipmentDAOImpl.PSH_ORD_ID_DB+" "
				+ (hasAppend?"where "+GetDate.getDateHql(startDate,endDate,ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):" ")
				+ ") as ordShip on "+tab1+RSHP_PSH_ID_DB+" = "
				+ tab3+ProdShipmentDAOImpl.PSH_ID_DB 
				+ " left join (select "+SalEmpDAOImpl.SE_NO_DB+","
				+ SalEmpDAOImpl.SE_NAME_DB+" from sal_emp) as emp "
				+ " on "+tab2+SalEmpDAOImpl.SE_NO_DB +" = "
				+ tab3+SalOrderDAOImpl.ORD_EMP_DB +" ");
		if(!StringFormat.isEmpty(wprId)){ 
			appendSql.append("where ");
			appendSql.append(tab1+RSHP_PROD_ID_DB+" = " + wprId +" ");
		}	
		appendSql.append("group by "+tab2+SalEmpDAOImpl.SE_NO_DB);
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("empName", Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
	
	/**
	 * ��Ʒ����<br>
	 * @param startDate,endDate	ǩ������ 
	 * @param startDate1,endDate1	�������� 
	 * @param wprId ��ƷId
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statProAnalyse(String startDate, String endDate, String wprId,String startDate1, String endDate1){
		Session session = (Session)super.getSession();
		StringBuffer appendSql = new StringBuffer();
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		boolean hasAppend1 = (!StringFormat.isEmpty(startDate1)||!StringFormat.isEmpty(endDate1));
		String tab = "cShipPro.";
		String tab1 = "emp.";
		String tab2 = "cusShipPro.";
		String tab3 = "cusShip.";
		String tab4 = "pro.";
		String tab5 = "cus.";
		String tab6 = "ordShip.";
		appendSql.append("select "
				+ tab + CusCorCusDAOImpl.CUS_ID_DB +" as cusId, "
				+ tab + CusCorCusDAOImpl.CUS_NAME_DB + " as cusName, "
				+ tab1 + SalEmpDAOImpl.SE_NAME_DB + " as empName, "
				+ tab + "sumCount as monSum1, "
				+ tab + "sumAmt as monSum2, "
				+ tab + "sumOutCount as monSum3 "
		);
		appendSql.append("from (select "
				+ tab2 + CusCorCusDAOImpl.CUS_ID_DB + ", "
				+ tab2 + CusCorCusDAOImpl.CUS_NAME_DB + ", "
				+ tab2 + SalOrderDAOImpl.ORD_EMP_DB + ", "
				+ "case when sum(" + tab2 + RSHP_COUNT_DB + ") is null then 0 else sum(" + tab2 + RSHP_COUNT_DB + ") end as sumCount, "
				+ "case when sum(" + tab2 + RSHP_PROD_AMT_DB +") is null then 0 else sum(" + tab2 + RSHP_PROD_AMT_DB + ") end as sumAmt, "
				+ "case when sum(" + tab2 + RSHP_OUT_COUNT_DB +") is null then 0 else sum(" + tab2 + RSHP_OUT_COUNT_DB + ") end as sumOutCount, "
				+ tab2 + RSHP_PROD_ID_DB+", "
				+ tab2 + SalOrderDAOImpl.ORD_CON_DATE_DB + ", "
				+ tab2 + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB + " "
				+ "from (select " + tab3 + CusCorCusDAOImpl.CUS_ID_DB + ", "
				+ tab3 + CusCorCusDAOImpl.CUS_NAME_DB + ", "
				+ tab3 + SalOrderDAOImpl.ORD_EMP_DB + ", "
				+ tab3 + ProdShipmentDAOImpl.PSH_ID_DB + ", "
				+ tab4 + RSHP_COUNT_DB + ", " + tab4 + RSHP_PROD_AMT_DB + ", "
				+ tab4 + RSHP_OUT_COUNT_DB + ", " + tab4 + RSHP_PROD_ID_DB + ", "
				+ tab3 + SalOrderDAOImpl.ORD_CON_DATE_DB + ", "
				+ tab3 + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB +" "
				+ "from (select " + tab5 + CusCorCusDAOImpl.CUS_ID_DB +", " + tab5 + CusCorCusDAOImpl.CUS_NAME_DB+", "
				+ tab6 + SalOrderDAOImpl.ORD_EMP_DB + ", " + tab6 + ProdShipmentDAOImpl.PSH_ID_DB + ", "
				+ tab6 + SalOrderDAOImpl.ORD_CON_DATE_DB + ", " + tab6 + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB + " "
				+ "from crm_cus_cor_cus as cus inner join (select " + SalOrderDAOImpl.ORD_CUS_ID_DB+ ", " + SalOrderDAOImpl.ORD_EMP_DB + ", "
				+ ProdShipmentDAOImpl.PSH_ID_DB + ", " + SalOrderDAOImpl.ORD_CON_DATE_DB + ", " + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB +" "
				+ "from sal_ord_con inner join prod_shipment on "+ SalOrderDAOImpl.ORD_ID_DB + "=" + ProdShipmentDAOImpl.PSH_ORD_ID_DB 
				+ ") as ordShip " + "on " + tab5 + CusCorCusDAOImpl.CUS_ID_DB + "=" + tab6 + SalOrderDAOImpl.ORD_CUS_ID_DB + ") as cusShip "
				+ "inner join r_ship_pro pro on " + tab4 + RSHP_PSH_ID_DB + "=" + tab3 +  ProdShipmentDAOImpl.PSH_ID_DB + " "
				+ ") as  cusShipPro group by " + tab2 + RSHP_PROD_ID_DB +") "
				+ "as cShipPro left join (select " + SalEmpDAOImpl.SE_NO_DB + ", " + SalEmpDAOImpl.SE_NAME_DB + " "
				+ "from sal_emp) emp on " + tab1 + SalEmpDAOImpl.SE_NO_DB +"="+ tab +  SalOrderDAOImpl.ORD_EMP_DB+" "
		);
		
		if(wprId != null && !wprId.equals("")){
			appendSql.append("where ");
			appendSql.append(tab + RSHP_PROD_ID_DB + "=" +wprId + " ");
			appendSql.append(
					hasAppend?" and "+GetDate.getDateHql(startDate,endDate,tab + SalOrderDAOImpl.ORD_CON_DATE_DB):" "             
				   + (hasAppend? (hasAppend1?" and  "+GetDate.getDateHql(startDate1,endDate1,tab + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):" "): (hasAppend1?" and  "+GetDate.getDateHql(startDate1,endDate1,ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):" "))
				   );
		}else{
			appendSql.append(
					hasAppend?" where "+GetDate.getDateHql(startDate,endDate,tab + SalOrderDAOImpl.ORD_CON_DATE_DB):" "             
				   + (hasAppend? (hasAppend1?" and  "+GetDate.getDateHql(startDate1,endDate1,tab + ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):" "): (hasAppend1?" where  "+GetDate.getDateHql(startDate1,endDate1,ProdShipmentDAOImpl.PSH_DELIVERY_DATE_DB):" "))
				   );
		}

		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("empName", Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE)
			.addScalar("monSum2",Hibernate.DOUBLE)
			.addScalar("monSum3",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;

	}
	
	public RShipPro findById(Long id) {
		log.debug("getting RShipPro instance with id: " + id);
		try {
			RShipPro instance = (RShipPro) getHibernateTemplate().get(
					"com.psit.struts.entity.RShipPro", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public RShipPro merge(RShipPro detachedInstance) {
		log.debug("merging RShipPro instance");
		try {
			RShipPro result = (RShipPro) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	public void delete(RShipPro persistentInstance) {
		log.debug("deleting RShipPro instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
}