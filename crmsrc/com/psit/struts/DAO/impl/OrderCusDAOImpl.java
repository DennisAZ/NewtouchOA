package com.psit.struts.DAO.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




import com.psit.struts.DAO.OrderCusDAO;
import com.psit.struts.entity.ERPOrdProductOutDetail;
import com.psit.struts.entity.ERPPaidPlan;
import com.psit.struts.entity.ERPSalOrder;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.util.format.GetDate;


public class OrderCusDAOImpl extends HibernateDaoSupport implements OrderCusDAO {
	private static final Log log = LogFactory.getLog(OrderCusDAOImpl.class);

	@Override
	public List getOrderByCusId(String cusId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ERPSalOrder> listOrdersByCusId(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ERPSalOrder> list = query.list();
		return list;
	}

	@Override
	public int listOrdersByCusId(String[] args) {
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getOrdSqlByCusId(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		
		StringBuffer appendSql = new StringBuffer();

		String sql = "from ERPSalOrder ord";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append(" where ");
		    	appendSql.append("ord.erpOrderCus.cusId = '"+args[3]+"' ");
		    }
			if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append("personId = '"+args[2]+"' ");
		    }
		   
		}

		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		
		return query;
	}
	private Query getOrdSqlByCusId2(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		
		StringBuffer appendSql = new StringBuffer();

		String sql = "from ERPPaidPlan ord";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append(" where ");
		    	appendSql.append("ord.erpSalOrder.erpOrderCus.cusName = '"+args[1]+"' ");
		    }
			if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append("ord.erpSalOrder.personId = '"+args[2]+"' ");
		    }
		   
		}

		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		
		return query;
	}
	private Query getShipProduct(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		
		StringBuffer appendSql = new StringBuffer();

		String sql = "from ERPOrdProductOutDetail ord";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select ord " + sql;
  		}
		if(args != null){
			if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append(" where ");
		    	appendSql.append("ord.erpSalOrder.erpOrderCus.cusId = '"+args[3]+"' ");
		    }
			if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append("ord.erpSalOrder.personId = '"+args[2]+"' ");
		    }
		   
		}

		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		
		return query;
	}

	@Override
	public List<ERPPaidPlan> listOrdersByCusId2(String[] args,
			String orderItem, String isDe, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId2(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ERPPaidPlan> list = query.list();
		return list;
	}

	@Override
	public int listOrdersByCusId2(String[] args) {
		// TODO Auto-generated method stub
		Session session = (Session) super.getSession();
		Query query = getOrdSqlByCusId2(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	@Override
	public List<ERPOrdProductOutDetail> listProdShip(String[] args,
			String orderItem, String isDe, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Session session = (Session) super.getSession();
		Query query = getShipProduct(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<ERPOrdProductOutDetail> list = query.list();
		return list;
	}

	@Override
	public int listProdShip(String[] args) {
		// TODO Auto-generated method stub
		Session session = (Session) super.getSession();
		Query query = getShipProduct(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	

}
