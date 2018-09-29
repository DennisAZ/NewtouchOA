package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.psit.struts.BIZ.OrderCusBIZ;
import com.psit.struts.entity.ERPOrdProductOutDetail;
import com.psit.struts.entity.ERPPaidPlan;
import com.psit.struts.entity.ERPSalOrder;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.Page;

public class OrderCusAction extends BaseDispatchAction {
	private OrderCusBIZ orderCusBIZ=null;
	public OrderCusBIZ getOrderCusBIZ() {
		return orderCusBIZ;
	}

	public void setOrderCusBIZ(OrderCusBIZ orderCusBIZ) {
		this.orderCusBIZ = orderCusBIZ;
	}

	/**
	 * 判断用户是否有订单管理操作的权限 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 有权限继续执行原有方法，没有权限跳转到limError页面
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			return super.execute(mapping, form, request, response);
		

	}
	public ActionForward toListOrdersToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		String cusName = request.getParameter("cusName");
		
		request.setAttribute("cusName", cusName);
		return mapping.findForward("selectOrd");
	}

	/**
	 * 选择订单列表 <br>
	 * @param request
	 * 		para >	ordTil:订单主题;  ordNum:订单号; 
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 * @param response
	 * @return ActionForward 跳转到selectOrd页面<br>
	 * 		attr > 	ordList:订单列表; ordTil:订单主题;  ordNum:订单号;
	 * 				page:分页信息 orderCol:排序列号 isDe:是否逆序
	 */
	public void listOrdersToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String cusName = request.getParameter("cusName");
		String ordTil=request.getParameter("ordTil");
		String ordNum=request.getParameter("ordNum");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String isApp = "1";
		String[] items = { "oTil", "oNum","oCus","oConDate","oEmp"};

		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = null;
		List<ERPSalOrder> list  = new ArrayList<ERPSalOrder>();
		if(cusId !=null && !cusId.equals("")){
			String[] args = { "0",cusName,String.valueOf(this.getPersonId(request)),cusId};
		    page = new Page(orderCusBIZ.listOrdersCount(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderCusBIZ.listOrdersByCusId(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}else{
//			String[] args = { "0",isApp,ordTil, ordNum};
//		    page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
//			page.setCurrentPageNo(Integer.parseInt(p));
//			list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
//		awareCollect.add("salEmp");
		awareCollect.add("erpOrderCus");

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm,awareCollect));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	public void listOrders(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String cusName = request.getParameter("cusName");
		String ordTil=request.getParameter("ordTil");
		String ordNum=request.getParameter("ordNum");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String isApp = "1";
		String[] items = { "oTil", "oNum","oCus","oConDate","oEmp"};

		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = null;
		List<ERPPaidPlan> list  = new ArrayList<ERPPaidPlan>();
		if(cusName !=null && !cusName.equals("")){
			String[] args = { "0",cusName,String.valueOf(this.getPersonId(request))};
		    page = new Page(orderCusBIZ.listOrdersByCusId2(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderCusBIZ.listOrdersByCusId2(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}else{
//			String[] args = { "0",isApp,ordTil, ordNum};
//		    page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
//			page.setCurrentPageNo(Integer.parseInt(p));
//			list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
//		awareCollect.add("salEmp");
		awareCollect.add("erpSalOrder");
		awareCollect.add("erpSalOrder.erpOrderCus");

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm,awareCollect));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	/*发货记录*/
	public void listCusProdShipment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String cusName = request.getParameter("cusName");
		String ordTil=request.getParameter("ordTil");
		String ordNum=request.getParameter("ordNum");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String isApp = "1";
		String[] items = { "oTil", "oNum","oCus","oConDate","oEmp"};

		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = null;
		List<ERPOrdProductOutDetail> list  = new ArrayList<ERPOrdProductOutDetail>();
		if(cusId !=null && !cusId.equals("")){
			String[] args = { "0",cusName,String.valueOf(this.getPersonId(request)),cusId};
		    page = new Page(orderCusBIZ.listProdShip(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderCusBIZ.listProdShip(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}else{
//			String[] args = { "0",isApp,ordTil, ordNum};
//		    page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
//			page.setCurrentPageNo(Integer.parseInt(p));
//			list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
//		awareCollect.add("salEmp");
		awareCollect.add("erpSalOrder.erpOrderCus");
		awareCollect.add("erpOrdProductOut");
		awareCollect.add("erpSalOrder");
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm,awareCollect));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
}
