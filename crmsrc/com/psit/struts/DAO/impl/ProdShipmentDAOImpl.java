package com.psit.struts.DAO.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.ProdShipmentDAO;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

public class ProdShipmentDAOImpl extends HibernateDaoSupport implements
		ProdShipmentDAO {
	private static final Log log = LogFactory.getLog(ProdShipmentDAOImpl.class);
	// property constants
	static final String PSH_ID = "pshId";
	static final String PSH_NUM = "pshNum";
	static final String PSH_ORDER = "pshOrder";
	static final String PSH_ORDER_COMITE_DATE = "pshOrder.sodComiteDate";
	static final String PSH_ORD_ID = "pshOrder.sodCode";
	static final String PSH_ORD_NUM = "pshOrder.sodNum";
	static final String PSH_DELIVERY_DATE = "pshDeliveryDate";
	static final String PSH_CONSIGNEE = "pshConsignee";
	static final String PSH_ADDR = "pshAddr";
	static final String PSH_EXPRESS = "pshExpress";
	static final String PSH_AMT = "pshAmt";
	static final String PSH_SHIPPER = "pshShipper";
	static final String PSH_POST_CODE = "pshPostCode";
	static final String PSH_PHO = "pshPho";
	static final String PSH_PROD_AMT = "pshProdAmt";
	static final String PSH_SAL_BACK = "pshSalBack";
	static final String PSH_COR_NAME = "pshOrder.cusCorCus.corName";
	static final String PSH_COR_CODE = "pshOrder.cusCorCus.corCode";
	
	static final String PSH_DELIVERY_DATE_DB = "psh_delivery_date";
	static final String PSH_PROD_AMT_DB = "psh_prod_amt";
	static final String PSH_SAL_BACK_DB = "psh_sal_back";
	static final String PSH_ORD_ID_DB = "psh_sod_id";
	static final String PSH_ID_DB = "psh_id";
	
	private final String tabName = "pShipment";// �����
	private final String tab = tabName + ".";
	
	public Double getShipAmtByCus(String cusId){
		Double shipAmt = 0.0;
		Session session = (Session) super.getSession();
		String queryString = "select sum("+PSH_PROD_AMT+") from ProdShipment where "+PSH_ORD_ID+" in " +
				"(select "+SalOrderDAOImpl.ORD_ID +" from SalOrdCon where "+SalOrderDAOImpl.ORD_CUS_ID+"="+cusId+")";
		Query query = session.createQuery(queryString);
		if(query.uniqueResult()!=null){
			shipAmt = (Double)query.uniqueResult();
		}
		return shipAmt;
	}
	
	/**
	 * ���¿ͻ�Ӧ�տ� <br>
	 * @param paid		�����¼
	 * @param session  
	 * @param isUpd		�Ƿ�Ϊ�������
	 */
	private void updateCusRecv(ProdShipment psh,double oldProdAmt,Session session,int type){
		if(psh!=null&&psh.getPshOrder()!=null&&psh.getPshOrder().getCusCorCus()!=null){
			double pshProdSum = psh.getPshProdAmt()!=null?psh.getPshProdAmt():0;
			switch(type){
			case 0:break;//�½�
			case 1:
				if(oldProdAmt>0){
					pshProdSum = pshProdSum - oldProdAmt; 
				}
				break;//����
			case 2:
				pshProdSum = -pshProdSum;
				break;//ɾ��
			}
			String hql = "update CusCorCus set "+CusCorCusDAOImpl.CUS_RECV_AMT+" = "+CusCorCusDAOImpl.CUS_RECV_AMT+"+(" + pshProdSum + ") where "+CusCorCusDAOImpl.CUS_ID+" = " + psh.getPshOrder().getCusCorCus().getCorCode();
			session.createQuery(hql).executeUpdate();
		}
	}
	
	public void saveRsps(List<RShipPro> rspList, ProdShipment psh){
		Session session = (Session) super.getSession();
		updateCusRecv(psh,0.0,session,0);
		if(rspList!=null){
			Iterator<RShipPro> rspIt = rspList.iterator();
			while(rspIt.hasNext()){
				getHibernateTemplate().save(rspIt.next());
			}
		}
	}
	public void updShipNums(List<RShipPro> toUpdRspList,List<RShipPro> toDelRspList, ProdShipment psh, double oldProdAmt){
		Session session = (Session) super.getSession();
		updateCusRecv(psh,oldProdAmt,session,1);
		if(toUpdRspList!=null){
			Iterator<RShipPro> updIt = toUpdRspList.iterator();
			while(updIt.hasNext()){
				getHibernateTemplate().merge(updIt.next());
			}
		}
		if(toDelRspList!=null){
			Iterator<RShipPro> delIt = toDelRspList.iterator();
			while(delIt.hasNext()){
				getHibernateTemplate().delete(delIt.next());
			}
		}
	}
	
	public void save(ProdShipment transientInstance) {
		log.debug("saving ProdShipment instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void update(ProdShipment transientInstance) {
		super.getHibernateTemplate().merge(transientInstance);
	}

	/*public void deleteById(String id) {
		Session session = (Session) super.getSession();
		String queryString = "delete ProdShipment where pshId=" + id;
		session.createQuery(queryString).executeUpdate();
	}*/

	public void delete(ProdShipment persistentInstance) {
		Session session = (Session) super.getSession();
		updateCusRecv(persistentInstance,0.0,session,2);
		log.debug("deleting ProdShipment instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProdShipment findById(Long id) {
		log.debug("getting ProdShipment instance with id: " + id);
		try {
			ProdShipment instance = (ProdShipment) getHibernateTemplate().get(
					"com.psit.struts.entity.ProdShipment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * ������¼�б� <br>
	 * 
	 * @param expName ������˾���(=)
	 * @param cusName �ͻ����(like)
	 * @param ordNum �������(=)
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<ProdShipment>
	 */
	public List<ProdShipment> listProdShipment(String expName, String cusName,
			String ordNum, String corName,String orderItem, String isDe, int currentPage,
			int pageSize) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, orderItem, isDe, false, expName,
				cusName, ordNum,corName);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdShipment> list = query.list();
		return list;
	}

	public int listProdShipmentCount(String expName, String cusName,
			String ordNum,String corName) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, null, null, true, expName, cusName,
				ordNum,corName);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	/**
	 * �ͻ��µķ�����¼ <br>
	 * @param cusId �ͻ�Id 
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<ProdShipment><br>
	 */
	public List<ProdShipment> listByCusId(String[] args,String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session)super.getSession();
		Query query = getQueryByCusId(session, orderItem, isDe, false, args);
		query.setFirstResult((currentPage -1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdShipment> list = query.list();
		return list;
	}
	
	public int listByCusIdCount(String[] args){
		Session session = (Session)super.getSession();
		Query query = getQueryByCusId(session, null, null, true,args);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private Query getQueryByCusId(Session session, String orderItem,String isDe, 
			boolean isCount,String[] args){
		StringBuffer sb = new StringBuffer();
		if(args != null){
		    if(args[0]!=null && !args[0].equals("")){ 
		    	sb.append(sb.length()==0?"where ":"and ");
				sb.append(tab + PSH_COR_CODE + "="+args[0]+" ");
		    }
		    if(args[1]!=null){
		    	if (!args[1].equals("") && args[2].equals("")) {
		    		sb.append(sb.length()==0?"where ":"and ");
		    		sb.append(tab+PSH_DELIVERY_DATE+" >= '"+ args[1] + "' ");
				} else if (!args[1].equals("") && !args[2].equals("")) {
					sb.append(sb.length()==0?"where ":"and ");
					sb.append(tab + PSH_DELIVERY_DATE + " between '"
							+ args[1] + "' and '"
							+ args[2]
							+ "' ");
				} else if (args[1].equals("") && !args[2].equals("")) {
					sb.append(sb.length()==0?"where ":"and ");
					sb.append(tab + PSH_DELIVERY_DATE + " <= '"
							+ args[2]
							+ "' ");
				}
		    }
		}

		Query query = getBaseQuery(session, sb, orderItem, isDe, isCount);

		return query;
	}
	/**
	 * �����µķ�����¼ <br>
	 * @param ordId ����ID
	 * @param orderItem
	 * @param isDe
	 * @param currentPage
	 * @param pageSize
	 * @return List<ProdShipment>
	 */
	public List<ProdShipment> listByOrdId(String ordId, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, orderItem, isDe, false, ordId);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ProdShipment> list = query.list();
		return list;
	}
	public int listByOrdIdCount(String ordId) {
		Session session = (Session) super.getSession();
		Query query = getQuery(session, null, null, true, ordId);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getQuery(Session session, String orderItem, String isDe,
			boolean isCount, String ordId) {
		StringBuffer appendSql = new StringBuffer();
		if (!StringFormat.isEmpty(ordId)) {
			appendSql.append("where ");
			appendSql.append(tab + PSH_ORD_ID + " = :ordId ");
		}
		Query query = getBaseQuery(session, appendSql, orderItem, isDe, isCount);
		if (!StringFormat.isEmpty(ordId)) {
			query.setString("ordId", ordId);
		}
		return query;
	}
	private Query getQuery(Session session, String orderItem, String isDe,
			boolean isCount, String expName, String cusName, String ordNum,String corName) {
		StringBuffer appendSql = new StringBuffer();
		if (!StringFormat.isEmpty(expName)) {
			appendSql.append(appendSql.length() == 0 ? "where " : "and ");
			appendSql.append(tab + PSH_EXPRESS + " = :expName ");
		}
		if (!StringFormat.isEmpty(cusName)) {
			appendSql.append(appendSql.length() == 0 ? "where " : "and ");
			appendSql.append(tab + PSH_CONSIGNEE + " like :consignee ");
		}
		if (!StringFormat.isEmpty(ordNum)) {
			appendSql.append(appendSql.length() == 0 ? "where " : "and ");
			appendSql.append(tab + PSH_ORD_NUM + " = :ordNum ");
		}
		if(!StringFormat.isEmpty(corName)){
			appendSql.append((appendSql.length() == 0)? "where ":" and ");
			appendSql.append(tab + PSH_COR_NAME +" like :corName ");
		}
		Query query = getBaseQuery(session, appendSql, orderItem, isDe, isCount);
		if (!StringFormat.isEmpty(expName)) {
			query.setString("expName", expName);
		}
		if (!StringFormat.isEmpty(cusName)) {
			query.setString("consignee", "%" + cusName + "%");
		}
		if (!StringFormat.isEmpty(ordNum)) {
			query.setString("ordNum", ordNum);
		}
		if(!StringFormat.isEmpty(corName)){
			query.setString("corName","%" + corName + "%");
		}
		return query;
	}
	private Query getBaseQuery(Session session, StringBuffer appendSql,
			String orderItem, String isDe, boolean isCount) {
		String sql = "from ProdShipment " + tabName + " ";
		if (isCount) {
			sql = "select count(*) " + sql;
		} else {
			sql = "select " + tabName + " " + sql;
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql, orderItem, isDe);
			sql += sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql += appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	private String[] getSortSql(String sql, String orderItem, String isDe) {
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
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
		}
		return new String[] { joinSql, sortSql };
	}
	
	/**
	 * ��ѯ�����·���������� <br>
	 * @param ordId
	 * @return Double[]
	 */
	public Double[] getProdAmtSum(String ordId){
		Session session = (Session) super.getSession();
		String prodAmtSql = "select sum("+PSH_PROD_AMT+") from ProdShipment where "+PSH_ORD_ID+"="+ordId+")";
		String salBackSql = "select sum("+PSH_SAL_BACK+") from ProdShipment where "+PSH_ORD_ID+"="+ordId+")";
		Query query = session.createQuery(prodAmtSql);
		Double prodAmt = (Double)query.uniqueResult();
		query = session.createQuery(salBackSql);
		Double salBack = (Double)query.uniqueResult();
		return new Double[]{prodAmt,salBack};
	}
	
	/**
	 * (��ƽ�����)δ��������۽��ͻ� <br>
	 * @param empIds	Ա��ID����
	 * @param cusIds	�ͻ�ID����
	 * @param statMonth	ͳ����
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statLowestSals(String empIds,String cusIds,String startDate, String endDate){
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "cusShip.";
		String tab2 = "emp.";
		String tab3 = "cusShipSum.";
		String tab4 = "cus.";
		String tab5 = "ordShip.";
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		
		appendSql.append("select "
				+ tab1 + CusCorCusDAOImpl.CUS_ID_DB + " as cusId ,"
				+ tab1 + CusCorCusDAOImpl.CUS_NAME_DB + " as cusName,"
				+ tab2 + SalEmpDAOImpl.SE_NAME_DB + " as empName,"
				+ tab1 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + " as monSum1,"
				+ tab1 + "monAvg as monSum2 from (");
		appendSql.append("select "
				+ tab3 + CusCorCusDAOImpl.CUS_ID_DB + ","
				+ tab3 + CusCorCusDAOImpl.CUS_NAME_DB + ","
				+ tab3 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + "," 
				+ tab3 + CusCorCusDAOImpl.CON_EMP_ID_DB + ","
				+ "case when avg("+tab3+"shipSum) is null then 0 else avg("+tab3+"shipSum) end as monAvg ");
		appendSql.append("from (select "
				+ tab4 + CusCorCusDAOImpl.CUS_ID_DB + ","
				+ tab4 + CusCorCusDAOImpl.CUS_NAME_DB + ","
				+ tab4 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + "," 
				+ tab4 + CusCorCusDAOImpl.CON_EMP_ID_DB + ","
				+ "sum("+tab5+PSH_PROD_AMT_DB+") as shipSum ");
		appendSql.append("from crm_cus_cor_cus as cus inner join ( select "
				+ SalOrderDAOImpl.ORD_CUS_ID_DB + ","
				+ PSH_PROD_AMT_DB + ","
				+ PSH_DELIVERY_DATE_DB + " from sal_ord_con inner join prod_shipment on "+SalOrderDAOImpl.ORD_ID_DB+"="+PSH_ORD_ID_DB+" "
				+ (hasAppend?"where "+GetDate.getDateHql(startDate,endDate,PSH_DELIVERY_DATE_DB):"")+") as ordShip ");
		appendSql.append("on "+tab4+CusCorCusDAOImpl.CUS_ID_DB + "=" + tab5 + SalOrderDAOImpl.ORD_CUS_ID_DB 
				+ " group by "+tab4+CusCorCusDAOImpl.CUS_ID_DB +",YEAR("+tab5+PSH_DELIVERY_DATE_DB+"),MONTH("+tab5+PSH_DELIVERY_DATE_DB+")) as cusShipSum " );
		appendSql.append("group by "+ tab3 + CusCorCusDAOImpl.CUS_ID_DB + ") as cusShip ");
		appendSql.append("left join (select "+ SalEmpDAOImpl.SE_NO_DB+","+SalEmpDAOImpl.SE_NAME_DB+" from sal_emp) as emp on "+tab2 + SalEmpDAOImpl.SE_NO_DB+"="+tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" ");
		appendSql.append("where " + tab1 + "monAvg < "+tab1+CusCorCusDAOImpl.CUS_SALE_NUM_DB);
		if(!StringFormat.isEmpty(empIds)){
			appendSql.append(" and ");
			appendSql.append(tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" in ("+empIds+")" );
		}
		if(!StringFormat.isEmpty(cusIds)){
			appendSql.append(" and ");
			appendSql.append(tab1 + CusCorCusDAOImpl.CUS_ID_DB + " in ("+cusIds+")");
		}
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("empName",Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE)
			.addScalar("monSum2",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
	
	
	/**
	 * ���۶�������ĩδ�ﵽ�涨���ĵ�λ <br>
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> getLTsals(){
		String startDate = "";
		String endDate = "";
		int year;
		int month;
		int day;
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		year = c.get(Calendar.YEAR);
		c.add(Calendar.MONTH, -1);
		day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH)+1;
		endDate = year+"-"+month+"-"+day;
		c.add(Calendar.MONTH, -2);
		month = c.get(Calendar.MONTH)+1;
		startDate = year+"-"+month+"-1";
		
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "cusShip.";
		String tab2 = "emp.";
		String tab3 = "cusShipSum.";
		String tab4 = "cus.";
		String tab5 = "ordShip.";
		
		appendSql.append("select "
				+ tab1 + CusCorCusDAOImpl.CUS_ID_DB + " as cusId ,"
				+ tab1 + CusCorCusDAOImpl.CUS_NAME_DB + " as cusName,"
				+ tab2 + SalEmpDAOImpl.SE_NAME_DB + " as empName,"
				+ tab1 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + " as monSum1,"
				+ tab1 + "monAvg as monSum2 from (");
		appendSql.append("select "
				+ tab3 + CusCorCusDAOImpl.CUS_ID_DB + ","
				+ tab3 + CusCorCusDAOImpl.CUS_NAME_DB + ","
				+ tab3 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + "," 
				+ tab3 + CusCorCusDAOImpl.CON_EMP_ID_DB + ","
				+ "case when avg("+tab3+"shipSum) is null then 0 else avg("+tab3+"shipSum) end as monAvg ");
		appendSql.append("from (select "
				+ tab4 + CusCorCusDAOImpl.CUS_ID_DB + ","
				+ tab4 + CusCorCusDAOImpl.CUS_NAME_DB + ","
				+ tab4 + CusCorCusDAOImpl.CUS_SALE_NUM_DB + "," 
				+ tab4 + CusCorCusDAOImpl.CON_EMP_ID_DB + ","
				+ "sum("+tab5+PSH_PROD_AMT_DB+") as shipSum ");
		appendSql.append("from crm_cus_cor_cus as cus inner join ( select "
				+ SalOrderDAOImpl.ORD_CUS_ID_DB + ","
				+ PSH_PROD_AMT_DB + ","
				+ PSH_DELIVERY_DATE_DB + " from sal_ord_con inner join prod_shipment on "+SalOrderDAOImpl.ORD_ID_DB+"="+PSH_ORD_ID_DB+" "
				+ "where "+GetDate.getDateHql(startDate,endDate,PSH_DELIVERY_DATE_DB)+") as ordShip ");
		appendSql.append("on "+tab4+CusCorCusDAOImpl.CUS_ID_DB + "=" + tab5 + SalOrderDAOImpl.ORD_CUS_ID_DB 
				+ " group by "+tab4+CusCorCusDAOImpl.CUS_ID_DB +",YEAR("+tab5+PSH_DELIVERY_DATE_DB+"),MONTH("+tab5+PSH_DELIVERY_DATE_DB+")) as cusShipSum " );
		appendSql.append("group by "+ tab3 + CusCorCusDAOImpl.CUS_ID_DB + ") as cusShip ");
		appendSql.append("left join (select "+ SalEmpDAOImpl.SE_NO_DB+","+SalEmpDAOImpl.SE_NAME_DB+" from sal_emp) as emp on "+tab2 + SalEmpDAOImpl.SE_NO_DB+"="+tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" ");
		appendSql.append("where " + tab1 + "monAvg < "+tab1+CusCorCusDAOImpl.CUS_SALE_NUM_DB);
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("empName",Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE)
			.addScalar("monSum2",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
	

	/**
	 * ��ɽ��ͳ��<br>
	 * @param empIds	Ա��ID����
	 * @param cusIds	�ͻ�ID����
	 * @param statMonth	ͳ����
	 * @return List<StatSalMForm>
	 */
	public List<StatSalMForm> statSalBack(String empIds,String cusIds,String startDate, String endDate){
		Session session = super.getSession();
		StringBuffer appendSql = new StringBuffer();
		String tab1 = "cus.";
		String tab2 = "emp.";
		String tab3 = "ordShip.";
		boolean hasAppend = (!StringFormat.isEmpty(startDate)||!StringFormat.isEmpty(endDate));
		
		appendSql.append("select "
				+ tab1 + CusCorCusDAOImpl.CUS_ID_DB + " as cusId,"
				+ tab1 + CusCorCusDAOImpl.CUS_NAME_DB + " as cusName,"
				+ tab2 + SalEmpDAOImpl.SE_NAME_DB + " as empName,"
				+ "case when sum("+tab3+PSH_SAL_BACK_DB+") is null then 0 else sum("+tab3+PSH_SAL_BACK_DB+") end as monSum1 ");
		appendSql.append("from crm_cus_cor_cus as cus inner join ( select "
				+ SalOrderDAOImpl.ORD_CUS_ID_DB + ","
				+ PSH_SAL_BACK_DB + " from sal_ord_con inner join prod_shipment on "+SalOrderDAOImpl.ORD_ID_DB+"="+PSH_ORD_ID_DB+" "
				+ (hasAppend?"where "+GetDate.getDateHql(startDate,endDate,PSH_DELIVERY_DATE_DB):"")+") as ordShip ");
		appendSql.append("on "+tab1+CusCorCusDAOImpl.CUS_ID_DB + "=" + tab3 + SalOrderDAOImpl.ORD_CUS_ID_DB +" ");
		appendSql.append("left join (select "+ SalEmpDAOImpl.SE_NO_DB+","+SalEmpDAOImpl.SE_NAME_DB+" from sal_emp) as emp on "+tab2 + SalEmpDAOImpl.SE_NO_DB+"="+tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" ");
		if(!StringFormat.isEmpty(empIds)){
			appendSql.append("where ");
			appendSql.append(tab1 +CusCorCusDAOImpl.CON_EMP_ID_DB+" in ("+empIds+")" );
		}
		if(!StringFormat.isEmpty(cusIds)){
			appendSql.append(!StringFormat.isEmpty(empIds)?"and ":"where ");
			appendSql.append(tab1 + CusCorCusDAOImpl.CUS_ID_DB + " in ("+cusIds+")");
		}
		appendSql.append("group by "+ tab1 + CusCorCusDAOImpl.CUS_ID_DB+" ");
		SQLQuery query = session.createSQLQuery(appendSql.toString());	
		query.addScalar("cusId", Hibernate.LONG)
			.addScalar("cusName", Hibernate.STRING)
			.addScalar("empName",Hibernate.STRING)
			.addScalar("monSum1",Hibernate.DOUBLE);
		query.setResultTransformer(Transformers.aliasToBean(StatSalMForm.class));
		List<StatSalMForm> list = query.list();
		return list;
	}
}