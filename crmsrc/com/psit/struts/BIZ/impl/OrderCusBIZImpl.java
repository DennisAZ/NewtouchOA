package com.psit.struts.BIZ.impl;

import java.util.List;

import com.psit.struts.BIZ.OrderCusBIZ;
import com.psit.struts.DAO.OrderCusDAO;
import com.psit.struts.entity.ERPOrdProductOutDetail;
import com.psit.struts.entity.ERPPaidPlan;
import com.psit.struts.entity.ERPSalOrder;
import com.psit.struts.entity.SalOrdCon;

public class OrderCusBIZImpl implements  OrderCusBIZ{
	OrderCusDAO orderCusDAO=null;
	public OrderCusDAO getOrderCusDAO() {
		return orderCusDAO;
	}

	public void setOrderCusDAO(OrderCusDAO orderCusDAO) {
		this.orderCusDAO = orderCusDAO;
	}

	@Override
	public List getOrderByCusId(String cusId, String selfId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int listOrdersCount(String[] args) {
		// TODO Auto-generated method stub
		return orderCusDAO.listOrdersByCusId(args);
	}

	@Override
	public int listOrdersByCusId(String[] args) {
		// TODO Auto-generated method stub
		return orderCusDAO.listOrdersByCusId(args);
	}

	@Override
	public List<ERPSalOrder> listOrdersByCusId(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		return orderCusDAO.listOrdersByCusId(args, orderItem, isDe, currentPage, pageSize);
	}

	@Override
	public int listOrdersByCusId2(String[] args) {
		// TODO Auto-generated method stub
		return orderCusDAO.listOrdersByCusId2(args);
	}

	@Override
	public List<ERPPaidPlan> listOrdersByCusId2(String[] args,
			String orderItem, String isDe, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return orderCusDAO.listOrdersByCusId2(args, orderItem, isDe, currentPage, pageSize);
	}

	@Override
	public int listProdShip(String[] args) {
		// TODO Auto-generated method stub
		return orderCusDAO.listProdShip(args);
	}

	@Override
	public List<ERPOrdProductOutDetail> listProdShip(String[] args,
			String orderItem, String isDe, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return orderCusDAO.listProdShip(args, orderItem, isDe, currentPage, pageSize);
	}

}
