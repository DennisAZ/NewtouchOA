package com.psit.struts.BIZ;

import java.util.List;

import com.psit.struts.entity.ERPOrdProductOutDetail;
import com.psit.struts.entity.ERPPaidPlan;
import com.psit.struts.entity.ERPSalOrder;
import com.psit.struts.entity.SalOrdCon;



public interface OrderCusBIZ {
public List getOrderByCusId(String cusId,String selfId);

public int listOrdersCount(String[]args);
public int listOrdersByCusId(String[]args);
public List<ERPSalOrder> listOrdersByCusId(String[]args, String orderItem,
		String isDe, int currentPage, int pageSize);

public int listOrdersByCusId2(String[]args);
public List<ERPPaidPlan> listOrdersByCusId2(String[]args, String orderItem,
		String isDe, int currentPage, int pageSize);

public int listProdShip(String[]args);
public List<ERPOrdProductOutDetail> listProdShip(String[]args, String orderItem,
		String isDe, int currentPage, int pageSize);
}
