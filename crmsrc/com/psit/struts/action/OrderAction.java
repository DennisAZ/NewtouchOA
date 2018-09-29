package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.PaidBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.BIZ.WmsManageBIZ;
import com.psit.struts.BIZ.WwoBIZ;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.entity.RWoutPro;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.WmsStro;
import com.psit.struts.entity.WmsWarOut;
import com.psit.struts.util.ListForm;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.util.CodeCreator;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.format.TransStr;
import com.psit.struts.util.DAO.BatchOperateDAO;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 订单管理 <br>
 */
public class OrderAction extends BaseDispatchAction {
	PrintWriter out;
	EmpBIZ empBiz = null;
	OrderBIZ orderBiz = null;
	TypeListBIZ typeListBiz = null;
	PaidBIZ paidBiz = null;
	UserBIZ userBiz = null;
	CustomBIZ customBiz = null;
	ModifyTypeDAO modType = null;
	WmsManageBIZ wmsManageBiz = null;
	WwoBIZ wwoBIZ = null;
	BatchOperateDAO batchOperate = null;

	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
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
		if (isLimitAllow(request)) {
			return super.execute(mapping, form, request, response);
		} else {
			//				request.setAttribute("errorMsg", "对不起，您没有该操作权限");
			return mapping.findForward("limError");
		}

	}

	/**
	 * 检测用户是否有订单管理的操作的权限码 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "toListOrders", "b025" },//订单是否可见
				{ "toNewOrder", "s001" },{ "newOrder", "s001" },//新建订单
				{ "toUpdOrder", "s003" },{ "updOrder", "s003" },//修改订单
				{ "deleteOrd", "s002" },//删除订单
				{ "showOrdDesc", "s004" },//查看订单详情

		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 跳转到创建订单 <br>
	 * @param request
	 * 		attr > typeList:订单类别列表;  souList:订单来源列表
	 * @return ActionForward newOrd
	 * 		
	 */
	public ActionForward toNewOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCusInfo = customBiz.getCusCorCusInfo(cusId);
			if(cusCorCusInfo != null){
				request.setAttribute("cusCorCusInfo", cusCorCusInfo);
			}
		}
		List<TypeList> typeList = typeListBiz.getEnabledType("11");//订单分类
		List<TypeList> souList = typeListBiz.getEnabledType("ordSou");//订单来源
		List<TypeList> stateList = typeListBiz.getEnabledType("12");//订单状态
		request.setAttribute("typeList", typeList);
		request.setAttribute("souList", souList);
		request.setAttribute("stateList", stateList);
		return mapping.findForward("newOrd");
	}
	/**
	 * 保存订单 <br>
	 * create_date: Aug 16, 2010,11:58:26 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > cusId:客户ID;  empId:签单人ID;  typeId:订单类别ID;  souId:订单来源ID; stateId：订单状态Id;
	 *                  shipDate:交付日期 conDate:签订日期 startDate:开始日期 endDate:结束日期 
	 *                  isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > redir:标识新建后跳转到详情 sodCode:订单id  isIfrm:是否在详情页面中新建
	 */
	public ActionForward newOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalOrdCon order = (SalOrdCon) form1.get("SalOrdCon");
		String cusId = request.getParameter("cusId");
		String empId = request.getParameter("empId");
		String isIfrm = request.getParameter("isIfrm");
		String typId = request.getParameter("typeId");
		String souId = request.getParameter("souId");
		String stateId = request.getParameter("stateId");
		String deadLine = request.getParameter("deadLine");
		String conDate = request.getParameter("conDate");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String sodComiteDate = request.getParameter("sodComiteDate");
		String curUser = (String) request.getSession().getAttribute("userName");
		
		if (order.getSodNum() != null && !order.getSodNum().equals("")) {
			if (orderBiz.checkOrdCode(order.getSodNum())) {
				request.setAttribute("errorMsg", "该订单号已存在，请核实后再添加");
				return mapping.findForward("error");
			}
		}
		if ( typId!= null && !typId.equals("")){
			order.setSalOrderType(new TypeList(Long.parseLong(typId)));
		}
		if ( souId!= null && !souId.equals("")){
			order.setSalOrderSou(new TypeList(Long.parseLong(souId)));
		}
		if (stateId!= null && !stateId.equals("")){
			order.setSodShipState(new TypeList(Long.parseLong(stateId)));
		}
		//插入客户
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cusCorCus = customBiz.getCusCorCusInfo(cusId);
//			if(!cusCorCus.getCorIssuc().equals("1")){
//				cusCorCus.setCorIssuc("1");
//			}
			order.setCusCorCus(cusCorCus);
		} else {
			order.setCusCorCus(null);
		}
		//插入日期
		if (deadLine!=null&&!deadLine.equals("")) {
			order.setSodDeadline(GetDate.formatDate(deadLine));
		} else {
			order.setSodDeadline(null);
		}
		if (sodComiteDate!=null&&!sodComiteDate.equals("")) {
			order.setSodComiteDate(GetDate.formatDate(sodComiteDate));
		} else {
			order.setSodComiteDate(null);
		}
		if (conDate!=null&&!conDate.equals("")) {
			order.setSodConDate(GetDate.formatDate(conDate));
		} else {
			order.setSodConDate(null);
		}
		if (startDate!=null&&!startDate.equals("")) {
			order.setSodOrdDate(GetDate.formatDate(startDate));
		} else {
			order.setSodOrdDate(null);
		}
		if (endDate!=null&&!endDate.equals("")) {
			order.setSodEndDate(GetDate.formatDate(endDate));
		} else {
			order.setSodEndDate(null);
		}

		order.setSodInpCode(curUser);
		order.setSodInpDate(GetDate.getCurTime());
		order.setSodAppIsok("0");//订单待审核
		order.setSodIsfail("0");// 是否删除标记
		order.setSodPaidMon(0.0);
		order.setSodChangeDate(null);
		order.setSodChangeUser(null);
		order.setSalEmp(new SalEmp(Long.parseLong(empId)));//订单签单人
		orderBiz.saveOrd(order);

		// 详情下新建 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		else{
			request.setAttribute("redir", "ord1");
			request.setAttribute("sodCode", order.getSodCode());
		}
		request.setAttribute("msg", "新建订单");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 跳转到修改订单 <br>
	 * create_date: Aug 13, 2010,2:03:26 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * 		para > ordId:订单ID
	 * @param response
	 * @return ActionForward 跳转到updOrder页面<br>
	 * 		attr > order:订单实体 typeList:订单类别列表
	 */
	public ActionForward toUpdOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
			
		List<TypeList> typeList = typeListBiz.getEnabledType("11");//订单分类
		List<TypeList> souList = typeListBiz.getEnabledType("ordSou");//订单来源
		List<TypeList> stateList = typeListBiz.getEnabledType("12");//订单状态
		SalOrdCon order = orderBiz.getOrdCon(ordId);
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("order", order);
		request.setAttribute("typeList", typeList);
		request.setAttribute("souList", souList);
		request.setAttribute("stateList", stateList);
		return mapping.findForward("updOrder");
	}

	/**
	 * 修改订单 <br>
	 * create_date: Aug 16, 2010,2:25:33 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > code:订单ID cusId:客户ID inpUser:录入人 appDate:审核日期 inpDate:录入日期  
	 *      			sodNum:订单编号 empId:签单人 typeId:订单类别ID  souId:订单来源ID  stateId:订单状态Id 
	 *      			deadLine:交付日期  conDate:签订日期  sodAppIsok:审核是否通过0未通过，1已通过 ordOwnCode:订单负责账号 
	 *      			proId:项目ID  startDate:开始日期 endDate:结束日期  isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 * @param response
	 * @return ActionForward 订单号重复跳转到error页面 成功修改跳转到popDivSuc页面<br>
	 * 		attribute > errorMsg:订单号重复提示<br> 
	 *      attribute > msg:成功修改提示  isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 */
	public ActionForward updOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalOrdCon orderForm = (SalOrdCon) form1.get("SalOrdCon");
		String cusId = request.getParameter("cusId");
//		String inpDate = request.getParameter("inpDate");
//		String appDate = request.getParameter("appDate");
		String isIfrm = request.getParameter("isIfrm");
		String sodNum = request.getParameter("oldSodNum");
		String empId=request.getParameter("empId");
		String typId = request.getParameter("typeId");
		String souId = request.getParameter("souId");
		String stateId = request.getParameter("stateId");
		String deadLine = request.getParameter("deadLine");
		String conDate = request.getParameter("conDate");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String comiteDate = request.getParameter("sodComiteDate");
		String curUser = (String) request.getSession().getAttribute("userName");
//		SalOrdCon order = orderBiz.getSalOrder(orderForm.getSodCode());
		SalOrdCon order = orderBiz.getSalOrder(1l);
		if (sodNum != null && !sodNum.equals(order.getSodNum())
				&& !order.getSodNum().equals("")) {
			if (orderBiz.checkOrdCode(order.getSodNum())) {
				request.setAttribute("errorMsg", "该订单号已存在，请核实后再修改");
				return mapping.findForward("error");
			}
		}
		if (typId != null && !typId.equals("")){
			order.setSalOrderType(new TypeList(Long.parseLong(typId)));
		}
		else{
			order.setSalOrderType(null);
		}
		if (souId != null && !souId.equals("")){
			order.setSalOrderSou(new TypeList(Long.parseLong(souId)));
		}
		else{
			order.setSalOrderSou(null);
		}
		if(stateId !=null && !stateId.equals("")){
			order.setSodShipState(new TypeList(Long.parseLong(stateId)));
		}else{
			order.setSodShipState(null);
		}
		//插入客户
		if (cusId != null && !cusId.equals("")) {
			order.setCusCorCus(new CusCorCus(Long.parseLong(cusId)));
		}
		//插入日期
		try {
			if (deadLine!=null && !deadLine.equals("")) {
				order.setSodDeadline(GetDate.formatDate(deadLine));
			} else {
				order.setSodDeadline(null);
			}
			if (comiteDate!=null && !comiteDate.equals("")) {
				order.setSodComiteDate(GetDate.formatDate(comiteDate));
			} else {
				order.setSodComiteDate(null);
			}
			if (conDate!=null && !conDate.equals("")) {
				order.setSodConDate(GetDate.formatDate(conDate));
			} else {
				order.setSodConDate(null);
			}
			if (startDate!=null && !startDate.equals("")) {
				order.setSodOrdDate(GetDate.formatDate(startDate));
			} else {
				order.setSodOrdDate(null);
			}
			if (endDate!=null&&!endDate.equals("")) {
				order.setSodEndDate(GetDate.formatDate(endDate));
			} else {
				order.setSodEndDate(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(empId!=null&&!empId.equals("")){
			order.setSalEmp(new SalEmp(Long.parseLong(empId)));
		}
		else{
			order.setSalEmp(null);
		}
		order.setSodTil(orderForm.getSodTil());
		order.setSodNum(orderForm.getSodNum());
		order.setSodSumMon(orderForm.getSodSumMon());
		order.setSodContent(orderForm.getSodContent());
		order.setSodRemark(orderForm.getSodRemark());
		order.setSodChangeDate(GetDate.getCurTime());
		order.setSodChangeUser(curUser);
		orderBiz.modifyOrd(order);
		// 详情下编辑 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "修改订单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到订单合同页面<br>
	 * @param request
	 * 			parameter > isApp:审核状态(0待审,1已审)
	 * @return ActionForward 跳转到listOrder页面<br>
	 * 		attribute > isApp:审核状态(0待审,1已审)
	 */
	public ActionForward toListOrders(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String isApp = request.getParameter("range");
		List<TypeList> stateList = typeListBiz.getEnabledType("12");//订单状态
		List<TypeList> list = new ArrayList<TypeList>();
		for(int i=0 ;i<stateList.size();i++){
			if(!stateList.get(i).getTypName().equals("已完成")){
				list.add(stateList.get(i));
			}
		}
		
		request.setAttribute("range", isApp);
		request.setAttribute("stateList", list);
		return mapping.findForward("listOrder");
	}
	/**
	 * 订单合同列表 <br>
	 * @param request
	 * 		para >	isApp:审核状态(0待审,1已审);  ordTil:订单主题;  ordNum:订单号; cusId:客户ID; 
	 * 				seName:签单人; startDate:签订日期(开始);  endDate:签订日期(结束); filter:列表筛选类型;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序;  stateId:订单状态
	 */
	public void listOrders(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String range=request.getParameter("range");
		String ordTil=request.getParameter("ordTil");
		String ordNum=request.getParameter("ordNum");
		String cusId=request.getParameter("cusId");
		String seName = request.getParameter("seName");
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String filter=request.getParameter("filter");
		String stateId = request.getParameter("stateId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "appState","oTil", "oNum","ordState","oType", "oCus",
				"oPaid", "oMon","oConDate","sodComiteDate", "oEmp"};
		String[] args = { "0",range,ordTil, ordNum, cusId, startDate,endDate, filter,seName, stateId };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalOrdCon> list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("sodShipState");
		awareCollect.add("salEmp");
		awareCollect.add("salOrderType");
		
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
	
	public ActionForward toListOrdersToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
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
		List<SalOrdCon> list  = new ArrayList<SalOrdCon>();
		if(cusId !=null && !cusId.equals("")){
			String[] args = { "0",ordTil, ordNum,cusId};
		    page = new Page(orderBiz.listOrdersByCusId(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderBiz.listOrdersByCusId(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}else{
			String[] args = { "0",isApp,ordTil, ordNum};
		    page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("salEmp");
		awareCollect.add("cusCorCus");

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
	public void listOrdersToChoose2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
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
		List<SalOrdCon> list  = new ArrayList<SalOrdCon>();
		if(cusId !=null && !cusId.equals("")){
			String[] args = { "0",ordTil, ordNum,cusId};
		    page = new Page(orderBiz.listOrdersByCusId(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderBiz.listOrdersByCusId(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}else{
			String[] args = { "0",isApp,ordTil, ordNum};
		    page = new Page(orderBiz.listOrdersCount(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderBiz.listOrders(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("salEmp");
		awareCollect.add("cusCorCus");

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
	/**
	 * 订单详情 <br>
	 * @param request
	 *      para >	code:订单ID;
	 *      attr >	order:订单实体;	rsfList:明细发货数列表;	realToPaid:发货应收款;	salBackSum:总提成;
	 * @return ActionForward 跳转到showOrdDesc页面<br>
	 */
	public ActionForward showOrdDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		SalOrdCon order = orderBiz.getSalOrder(Long.parseLong(code));
		List<RopShipForm> rsfList = orderBiz.getRopShip(code) ;
		Double[] prodAmts = orderBiz.getProdAmtSum(code);
		Double realToPaid = (prodAmts[0]!=null?prodAmts[0]:0)-(order.getSodPaidMon()!=null?order.getSodPaidMon():0);
		request.setAttribute("rsfList", rsfList);
		request.setAttribute("realToPaid", realToPaid);
		request.setAttribute("salBackSum", prodAmts[1]);
		request.setAttribute("order", order);
		return mapping.findForward("showOrdDesc");
	}
	
	/**
	 * 订单产品明细列表 <br>
	 * @param request
	 * 		para >	ordId:订单Id;  orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listOrdPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ordId = request.getParameter("ordId");
		List<ROrdPro> list = orderBiz.listOrdPro(ordId);
		ListForm listForm = new ListForm();
		listForm.setList(list);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProduct");
		awareCollect.add("wmsProduct.typeList");

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
	
	/**
	 * 订单下出库(未使用) <br>
	 * create_date: Aug 16, 2010,11:42:58 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码 sodCode:订单ID
	 * @param response
	 * @return ActionForward 跳转到wwoList页面<br>
	 * 		attribute > order:订单实体 page:分页信息 sodCode:订单ID wwoList:符合条件的出库单列表 isNone:订单下没有计算库存的产品时返回
	 */
	/*public ActionForward getWwoList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String sodCode = request.getParameter("sodCode");
		Page page = new Page(gadDao.getAccDataCount(Long.parseLong(sodCode),
				"WmsWarOut", "salOrdCon.sodCode", "wwoIsdel", "1"), 10);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list1 = gadDao.getAccData(Long.parseLong(sodCode), "WmsWarOut",
				"salOrdCon.sodCode", "wwoIsdel", "1", "wwoId", page
						.getCurrentPageNo(), page.getPageSize());

		List list = orderBiz.findByOrd(sodCode);//订单明细
		int isNone = 1;
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				ROrdPro rordPro = (ROrdPro) list.get(i);
				if (rordPro.getWmsProduct().getWprIscount() != null
						&& rordPro.getWmsProduct().getWprIscount().equals("1")) {
					isNone = 0;
				}
			}
			if (isNone == 1) {
				request.setAttribute("isnone", "1");
			}
		}
		SalOrdCon order = orderBiz.getOrdCon(sodCode);
		request.setAttribute("order", order);
		request.setAttribute("page", page);
		request.setAttribute("sodCode", sodCode);
		request.setAttribute("wwoList", list1);
		return mapping.findForward("wwoList");
	}*/

	/**
	 * 订单号重复判断 <br>
	 * create_date: Aug 16, 2010,11:56:34 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > code:订单ID
	 * @param response
	 * @return ActionForward null 订单号重复输出1不重复输出0<br>
	 */
	public ActionForward checkOrdCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = null;
		if (request.getParameter("code") != "") {
			code = TransStr.transStr(request.getParameter("code"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (code != null) {
				if (orderBiz.checkOrdCode(code)) {
					out.print("1");
				} else {
					out.print("0");
				}
				out.flush();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 撤销审核 <br>
	 * @param request
	 *      parameter > code:订单ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功撤销审核信息提示
	 */
	public ActionForward removeSta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String curUser = (String) request.getSession().getAttribute("userName");
		
		SalOrdCon salOrdCon = orderBiz.getOrdCon(code);
		String oldApp = salOrdCon.getSodAppDesc();
		String appLog = "";
		String tadesc = "<strong class='brown'>" + curUser + " 于 "
				+ GetDate.parseStrTime(GetDate.getCurTime()) + " 撤销订单审核</strong><br/><br/>";
		if (oldApp != null && !oldApp.equals(""))
			appLog = tadesc + oldApp;
		else
			appLog = tadesc;
		salOrdCon.setSodAppIsok("0");
		salOrdCon.setSodAppDesc(appLog);
		orderBiz.modifyOrd(salOrdCon);
		request.setAttribute("msg", "撤销审核");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 审核订单 <br>
	 * create_date: Aug 16, 2010,2:47:20 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > code:订单ID sodAppIsok:审核是否通过1表示已通过0表示未通过 sodAppDesc:审核内容
	 * @param response
	 * @return ActionForward 重定向到"orderAction.do?op=showOrdDesc&code="+code<br>
	 */
	public ActionForward appOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String sodAppIsok = request.getParameter("sodAppIsok");
		String sodAppDesc = request.getParameter("sodAppDesc");
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		SalOrdCon salOrdCon = orderBiz.getOrdCon(code);
		java.util.Date currentDate = new java.util.Date();//当前日期
		salOrdCon.setSodAppDate(currentDate);
		salOrdCon.setSodAppIsok(sodAppIsok);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		salOrdCon.setSodAppMan(lim.getUserSeName());
		String appLog = "";
		String isOk = "";
		String oldApp = salOrdCon.getSodAppDesc();
		if (sodAppIsok.equals("1")) {
			isOk = "通过";
		} else {
			isOk = "未通过";
		}
		String tadesc = "";
		if (sodAppDesc != null && !sodAppDesc.equals("")) {
			tadesc = "<span class='bold'>订单" + isOk + "审核&nbsp;[审核人:"
					+ lim.getUserSeName()
					+ "]</span>&nbsp;&nbsp;<span class='gray'>"
					+ df.format(new Date()) + "</span><br/>(内容：" + sodAppDesc
					+ ")<br/><br/>";
		} else {
			tadesc = "<span class='bold'>订单" + isOk + "审核&nbsp;[审核人:"
					+ lim.getUserSeName()
					+ "]</span>&nbsp;&nbsp;<span class='gray'>"
					+ df.format(new Date()) + "</span><br/><br/>";
		}
		if (oldApp != null && !oldApp.equals(""))
			appLog = tadesc + oldApp;
		else
			appLog = tadesc;
		salOrdCon.setSodAppDesc(appLog);
		orderBiz.modifyOrd(salOrdCon);
		String url = "orderAction.do?op=showOrdDesc&code=" + code;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到修改订单交付状态页面 <br>
	 * create_date: Aug 16, 2010,2:50:52 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > code:订单ID
	 * @param response
	 * @return ActionForward 跳转到sodState页面<br>
	 * 		attribute > order:订单实体
	 */
	public ActionForward altSodState(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		SalOrdCon salOrdCon = orderBiz.getSalOrder(Long.parseLong(code));
		List<TypeList> stateList = typeListBiz.getEnabledType("12");//订单状态
		
		request.setAttribute("order", salOrdCon);
		request.setAttribute("stateList", stateList);
		return mapping.findForward("sodState");
	}

	/**
	 * 修改订单交付状态 <br>
	 * create_date: Aug 16, 2010,2:54:45 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > code:订单ID sodShipState:订单交付状态 deadLine:订单交付时间
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功修改订单交付状态提示
	 */
	public ActionForward altSodSta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String stateId = request.getParameter("stateId");
		String deadLine = request.getParameter("deadLine");
		Date deadLineDate = null;
		
		SalOrdCon order = orderBiz.getSalOrder(Long.parseLong(code));
		
		if(stateId!=null && !stateId.equals("")){
			order.setSodShipState(new TypeList(Long.parseLong(stateId)));
		}else{
			order.setSodShipState(null);
		}
		if(deadLine != null && !deadLine.equals("")){
			deadLineDate = GetDate.formatDate(deadLine);
		}
		order.setSodDeadline(deadLineDate);
		orderBiz.modifyOrd(order);
		
		request.setAttribute("msg", "修改订单交付状态");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 刪除订单(更新客户成单状态) <br>
	 * @param request  
	 *      parameter > ordCode:订单ID  isIfrm:是否在详情页面中删除若为1则是在详情页面中删除
	 * @param response
	 * @return ActionForward 不符合删除条件跳转到error页面 成功删除跳转到popDivSuc页面<br>
	 * 		attribute > errorMsg:不符合删除条件的信息提示<br>
	 *      attribute > msg:成功删除的信息提示  isIfrm:是否在详情页面中删除若为1则是在详情页面中删除
	 */
	public ActionForward deleteOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String isIfrm = request.getParameter("isIfrm");
		SalOrdCon salOrdCon = orderBiz.getSalOrder(Long.parseLong(id));
		boolean isDel = true;
		StringBuffer eMsg = new StringBuffer();
		/*if (salOrdCon.getSalPaidPlans() != null) {
			int planCount = 0;
			int planRecCount = 0;
			Set<SalPaidPlan> list = salOrdCon.getSalPaidPlans();
			Iterator<SalPaidPlan> iter = list.iterator();
			while (iter.hasNext()) {
				SalPaidPlan salPaidPlan = iter.next();
				if (salPaidPlan.getSpdIsdel().equals("0")) {
					planCount++;
				}
				else{
					planRecCount++;
				}
			}
			if (planCount > 0 || planRecCount > 0) {
				isDel = false;
				StringFormat.getDelEMsg(eMsg,planCount,planRecCount,"回款计划");
			}
		}*/
		if (salOrdCon.getSalPaidPasts() != null) {
//			int pastCount = 0;
//			int pastRecCount = 0;
			Set<SalPaidPast> list = salOrdCon.getSalPaidPasts();
//			Iterator<SalPaidPast> iter = list.iterator();
//			while (iter.hasNext()) {
//				SalPaidPast salPaidPast = iter.next();
//				if (salPaidPast.getSpsIsdel().equals("0")) {
//					pastCount++;
//				}
//				else{
//					pastRecCount++;
//				}
//			}
//			if (pastCount>0 || pastRecCount>0) {
			if (list!=null && list.size()>0) {
				isDel = false;
				StringFormat.getDelEMsg(eMsg,list.size(),0,"回款记录");
			}
		}
		if (isDel) {
			orderBiz.invalidOrd(salOrdCon);
			// 详情下编辑 刷新iframe
			if (isIfrm != null && isIfrm.equals("1")) {
				request.setAttribute("isIfrm", "1");
			}
			request.setAttribute("msg", "删除订单");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", "删除订单失败，该订单下存在关联数据："+eMsg.toString());
			return mapping.findForward("error");
		}
	}

	/**
	 * 跳转到更新订单明细 <br>
	 * @param request
	 *      para >	sodCode:订单ID;
	 *      attr >	order:订单实体;	cusProdList:客户产品列表;
	 * @return ActionForward updRop
	 */
	public ActionForward toUpdRop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sodCode = request.getParameter("sodCode");
		SalOrdCon order = orderBiz.getOrdCon(sodCode);
		List<CusProd> cusProdList = null;
		if(order.getCusCorCus()!=null){
			cusProdList = customBiz.listCusProd(String.valueOf(order.getCusCorCus().getCorCode()));
		}
		request.setAttribute("cusProdList", cusProdList);
		request.setAttribute("order", order);
		return mapping.findForward("updRop");
	}

	/**
	 * 更新订单明细 <br>
	 * @param request
	 *      para > 	wprId:订单明细产品ID数组;	sodCode:订单ID;
	 *      attr >	noMsg:表示不弹出任何提示直接刷新父页面;
	 * @return ActionForward empty
	 */
	public ActionForward updRop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodIds[] = request.getParameterValues("wprId");
		String sodCode = request.getParameter("sodCode");
		List<ROrdPro> ropList = new ArrayList();
		try{
			for (int i = 1; i < prodIds.length; i++) {
				ROrdPro rordPro = new ROrdPro();
				String ropPrice = request.getParameter("price" + prodIds[i]);
				String num1 = request.getParameter("num" + prodIds[i]);
				String tatalPrice = request.getParameter("allPrice" + prodIds[i]);
				String rmStr = request.getParameter("remark" + prodIds[i]);
				String ropZk = request.getParameter("taxRate" + prodIds[i]);
				if (!StringFormat.isEmpty(ropPrice)) {
					rordPro.setRopPrice(Double.parseDouble(ropPrice));
				}
				else{
					rordPro.setRopPrice(0.0);
				}
				if (!StringFormat.isEmpty(num1)) {
					rordPro.setRopNum(Double.parseDouble(num1));
				} else {
					rordPro.setRopNum(0.0);
				}
				if (!StringFormat.isEmpty(tatalPrice)) {
					rordPro.setRopRealPrice(Double.parseDouble(tatalPrice));
				} else {
					rordPro.setRopRealPrice(0.0);
				}
				rordPro.setSalOrdCon(new SalOrdCon(sodCode));
				rordPro.setWmsProduct(new WmsProduct(prodIds[i]));
				rordPro.setRopZk(ropZk);
				rordPro.setRopRemark(rmStr);
				ropList.add(rordPro);
			}
			orderBiz.saveRop(ropList,sodCode);
			request.setAttribute("noMsg", "1");//不弹出信息
			return mapping.findForward("empty");
		} catch(Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errorMsg", ex.toString());
			return mapping.findForward("error");
		}
	}

	/**
	 * 跳转到生成出库单(未使用) <br>
	 * create_date: Aug 16, 2010,3:52:04 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > sodCode:订单ID
	 * @param response
	 * @return ActionForward 跳转到wmsProOut页面<br>
	 * 		attribute > rordPro:订单明细列表 rordPro1:不含库存为空的订单明细列表  sodCode:订单ID wmsStro:仓库列表 rstroPro:订单明细表中的产品的仓储情况列表
	 */
	public ActionForward wmsProOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sodCode = request.getParameter("sodCode");
		List list = orderBiz.findByOrd(sodCode);
		List<ROrdPro> list3 = new ArrayList();
		List<ROrdPro> list2 = new ArrayList();//不带库存为空的list
		//int r=0;
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				//订单明细中的商品数量
				ROrdPro rordPro = (ROrdPro) list.get(i);
				Double n = rordPro.getRopNum();//需求数量
				//				Double m=rordPro.getRopOutNum();//已安排数量
				String code = rordPro.getWmsProduct().getWprId();
//				Double num = wmsManageBiz.getCountPro(code);//库存总量
				Double num =0.0;
				if (num == 0.0) {
					//r++;
					list3.add(rordPro);
				} else {
					if (n > 0) {
						/*if(m==0){
							list3.add(rordPro);
							list2.add(rordPro);
						}
						if(n>m&&m!=0){
							list3.add(rordPro);
							list2.add(rordPro);
						}
						if(m.equals(n)){
							list3.add(rordPro);
							list2.add(rordPro);
						}*/
						list3.add(rordPro);
						list2.add(rordPro);
					}
				}
			}
			//			if(r==list.size()){
			//				request.setAttribute("none","订单中所有的商品库存为空！");
			//				
			//			}
			request.setAttribute("rordPro", list3);
			request.setAttribute("rordPro1", list2);
		}
		List list4 = wmsManageBiz.findAllWms();
		request.setAttribute("sodCode", sodCode);
		request.setAttribute("wmsStro", list4);//仓库列表
		List list5 = orderBiz.findByStro(sodCode);//订单明细表中的产品的仓储情况列表
		request.setAttribute("rstroPro", list5);
		return mapping.findForward("wmsProOut");
	}

	/**
	 * 生成出库单(未使用) <br>
	 * create_date: Aug 16, 2010,5:27:09 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wms:仓库编号数组 wprId:需要出库的商品ID数组 sodCode:订单ID
	 * @param response
	 * @return ActionForward 跳转到empty页面<br>
	 * 		attribute > error:提示订单中已不存在某种商品 msg:成功生成出库单的信息提示
	 */
	public ActionForward newRwmsPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String m[] = request.getParameterValues("wms");
		String n[] = request.getParameterValues("wprId");
		String sodCode = request.getParameter("sodCode");
		//		String yulan=request.getParameter("yulan");
		//boolean falg=false;
		int rr = 0;
		//List<WmsWarOut> wmsOut=new ArrayList();
		for (int j = 0; j < m.length; j++) {
			ArrayList<String> wmsPro = new ArrayList();
			ArrayList wmsNum = new ArrayList();
			ArrayList<String> proName = new ArrayList();
			int r = 0;
			for (int i = 0; i < n.length; i++) {
				Double num = null;
				String n1 = request.getParameter(n[i] + "n" + m[j]);
				String wprName = request.getParameter("wprName" + n[i]);
				Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
				if (n1 != null && !n1.equals("") && !n1.equals("0")) {
					Matcher isNum = pattern.matcher(n1);
					if (!isNum.matches()) {
						num = 0.0;
					} else {
						num = Double.parseDouble(n1);
					}
					wmsPro.add(n[i]);
					wmsNum.add(num);
					proName.add(wprName);
				} else {
					r++;
				}
			}
			if (r != n.length) {
				//生成出库单
				SalOrdCon order = orderBiz.getOrdCon(sodCode);
				String title = "";
				if (order.getSodTil().length() > (300 - 8)) {
					title = order.getSodTil().substring(0, 300 - 11) + "...";
				} else {
					title = order.getSodTil();
				}
				title = "订单「" + title + "」的出库单";
				WmsWarOut wmsWarOut = new WmsWarOut();
				java.util.Date currentDate = new java.util.Date();
				CodeCreator codeCreator = new CodeCreator();
				String wwoCode = codeCreator.createCode("PO"
						+ dateFormat.format(currentDate), "wms_war_out", 0);
				wmsWarOut.setWwoCode(wwoCode);
				wmsWarOut.setWwoTitle(title);
				wmsWarOut.setSalOrdCon(order);
				wmsWarOut.setWwoState("0");
				wmsWarOut.setWwoAppIsok("2");
				//仓库
				WmsStro wmsStro = wmsManageBiz.findWmsStro(m[j]);
				wmsWarOut.setWmsStro(wmsStro);
				Date twoInpDate = new Date(new java.util.Date().getTime());
				wmsWarOut.setWwoInpDate(twoInpDate);

				LimUser limUser = (LimUser) request.getSession().getAttribute(
						"limUser");
				wmsWarOut.setWwoInpName(limUser.getUserSeName());
				wmsWarOut.setWwoIsdel("1");
				//				if(yulan!=null&&!yulan.equals("")){
				//					//生成出库明细
				//					Set<RWoutPro> RWoutPros = new HashSet(0);
				//					for(int x=0;x<wmsPro.size();x++)
				//					{
				//						RWoutPro rwoutPro=new RWoutPro();
				//						rwoutPro.setWmsWarOut(wmsWarOut);
				//						String wprCode=(String) wmsPro.get(x);
				//						String proName1=(String) proName.get(x);
				//						Double allNum=wmsManageBiz.getCountPro(Long.parseLong(wprCode));//库存总量
				//						if(allNum==0.0){
				//							rwoutPro.setRwoWoutNum(0.0);
				//						}else{
				//							Double num=(Double) wmsNum.get(x);//订单商品数量
				//							rwoutPro.setRwoWoutNum(num);
				//						}
				//						WmsProduct wmsProduct=new WmsProduct();	
				//						if(wprCode!=null&&!wprCode.equals("")){
				//							//wmsProduct.setWprId(Long.parseLong(wprCode));
				//							wmsProduct.setWprName(proName1);
				//							rwoutPro.setWmsProduct(wmsProduct);
				//						}else{
				//							rwoutPro.setWmsProduct(null);
				//						}
				//						RWoutPros.add(rwoutPro);
				//					}
				//					wmsWarOut.setRWoutPros(RWoutPros);
				//					wmsOut.add(wmsWarOut);//预览
				//					falg=true;
				//				}else{
				wwoBIZ.saveWwo(wmsWarOut);//保存至数据库
				//生成出库明细
				for (int x = 0; x < wmsPro.size(); x++) {
					RWoutPro rwoutPro = new RWoutPro();
					rwoutPro.setWmsWarOut(wmsWarOut);
					String wprCode = (String) wmsPro.get(x);
					Double allNum = wmsManageBiz.getCountPro(Long
							.parseLong(wprCode));//库存总量
					if (allNum == 0.0) {
						rwoutPro.setRwoWoutNum(0.0);
					} else {
						Double num = (Double) wmsNum.get(x);//订单商品数量
						rwoutPro.setRwoWoutNum(num);
					}
					WmsProduct wmsProduct = new WmsProduct();
					if (wprCode != null && !wprCode.equals("")) {
						wmsProduct.setWprId(wprCode);
						rwoutPro.setWmsProduct(wmsProduct);
					} else {
						rwoutPro.setWmsProduct(null);
					}
					wwoBIZ.saveRwp(rwoutPro); 
					//库存流水
					//						WmsLine wmsLine=new WmsLine();
					//						wmsLine.setWliType("1");//流水类别--出库
					//						wmsLine.setWliTypeCode(String.valueOf(wmsWarOut.getWwoId()));
					//						wmsLine.setWmsProduct(rwoutPro.getWmsProduct());
					//						wmsLine.setWmsStro(wmsWarOut.getWmsStro());
					//						wmsLine.setWliDate(wmsWarOut.getWwoInpDate());
					//						wmsLine.setWliState("0");//未执行
					//						wmsLine.setWliOutNum(rwoutPro.getRwoWoutNum());
					//						wmsLine.setWliWmsId(rwoutPro.getRwoId());
					//						wmsLine.setWliIsdel("1");
					//						wwoBIZ.saveWli(wmsLine);
					//更新订单
					List list1 = orderBiz.findByWpr(sodCode, Long
							.parseLong(wprCode));//订单明细
					if (list1.size() == 0) {
						request.setAttribute("error", "订单中已不存在该商品！");
						return mapping.findForward("empty");
					}
					if (list1.size() > 0) {
						//订单明细中的商品数量
						ROrdPro rordPro = (ROrdPro) list1.get(0);
						String wprId = rordPro.getWmsProduct().getWprId();
						Double num = rordPro.getRopOutNum();
						if (wprId.equals(rwoutPro.getWmsProduct().getWprId())) {
							if (num == 0 && num == null) {
								rordPro.setRopOutNum(rwoutPro.getRwoWoutNum());
								orderBiz.updateRop(rordPro);
							} else {
								Double yn = rwoutPro.getRwoWoutNum() + num;
								rordPro.setRopOutNum(yn);
								orderBiz.updateRop(rordPro);
							}
						}
					}
				}
				//}
			} else {
				rr++;
			}
		}
		request.setAttribute("msg", "已生成出库单！请等待审核。");
		return mapping.findForward("empty");
	}

	/**
	 * 跳转到待删除的订单  <br>
	 * create_date: Mar 24, 2011,6:16:12 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward recOrder<br>
	 */
	public ActionForward toListDelOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recOrder");
	}
	/**
	 * 获得待删除的订单 <br>
	 * create_date: Aug 16, 2010,5:58:33 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      para > p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 * @param response
	 * @return ActionForward 跳转到recOrder页面<br>
	 * 		attr > ordList:符合条件的订单列表 page:分页信息
	 */
	public void findDelOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "oType", "oTil", "oNum", "oCus",
				"oPaid", "oMon", "oConDate", "oEmp"};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.findDelOrderCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalOrdCon> list = orderBiz.findDelOrder(orderItem, isDe, page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salOrderType");
		awareCollect.add("salEmp");
		
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
		/*request.setAttribute("ordList", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		return mapping.findForward("recOrder");*/

	}

	/**
	 * 恢复订单（更新客户成单状态） <br>
	 * create_date: Aug 16, 2010,6:00:14 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      para > id:订单ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > msg:成功恢复订单的信息提示
	 */
	public ActionForward recOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalOrdCon salOrder = orderBiz.getSalOrder(id);
		/*if (salOrder.getCusCorCus() != null) {
			CusCorCus cusCorCus = salOrder.getCusCorCus();
			if (cusCorCus.getCorIssuc().equals("0")) {
				cusCorCus.setCorIssuc("1");
				customBiz.update(cusCorCus);
			}
		}
		salOrder.setSodIsfail("0");*/
		orderBiz.recovery(salOrder);
		request.setAttribute("msg", "恢复订单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 物理删除订单 <br>
	 * create_date: Aug 16, 2010,6:01:38 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      para > id:订单ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > msg:成功删除的信息提示
	 */
	public ActionForward delOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalOrdCon salOrder = orderBiz.getSalOrder(id);
		Iterator it = salOrder.getAttachments().iterator();
		//删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		orderBiz.delete(salOrder);
		request.setAttribute("msg", "删除订单");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 高级查询 <br>
	 * create_date: Aug 13, 2010,5:01:22 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > isAll:标识查询范围0表示查询我的订单1表示查询我的下属订单2表示全部订单  limCode:勾选的所有负责账号的userCode empCode:勾选的所有签单人的empSeNo
	 *                  cusCode:客户ID  cusName:客户名称 sodTil:订单主题 sodNum:订单号 typeId:订单类别id userCBName:勾选负责账号参数 empCBName:勾选签单人参数
	 *                  sodSumMon:总金额 sodPaidMethod:付款方式 conDate1、conDate2:签订日期段 shipDate1、shipDate2:交付日期时间段 startDate1
	 *                  startDate2:开始日期时间段 endDate1、endDate2:结束日期时间段 sodShipState:交付状态 sodAppIsok:是否审核0表示未审核1表示已审核 
	 *                  isEmpty:值不为空表示第一次进入高级查询页面页面不显示任何数据 p:当前页码
	 * @param response
	 * @return ActionForward 跳转到ordSupSearch页面<br>
	 * 		attribute > OrdTypeList:订单类别 orgList:部门列表 ordList:符合条件的订单列表 page:分页信息
	 *                  isAll:标识查询范围0表示查询我的订单1表示查询我的下属订单2表示全部订单  limCode:勾选的所有负责账号的userCode 
	 *                  cusCode:客户ID  cusName:客户名称 sodTil:订单主题 sodNum:订单号 typeId:订单类别id empCode:勾选的所有签单人的empSeNo
	 *                  sodSumMon:总金额 sodPaidMethod:付款方式 conDate1、conDate2:签订日期段 shipDate1、shipDate2:交付日期时间段 startDate1
	 *                  startDate2:开始日期时间段 endDate1、endDate2:结束日期时间段 sodShipState:交付状态 sodAppIsok:是否审核0表示未审核1表示已审核
	 *                  orgTopCode:组织顶级账号 
	 */
	/*public ActionForward ordSupSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list2 = typeListBiz.getEnabledType("11");//订单类别
		List orgList = null;
		String isAll=request.getParameter("isAll");
		request.setAttribute("isAll",isAll);
		String userNum = (String) request.getSession().getAttribute("userNum");
		String limCode = request.getParameter("limCode");
		if (isAll != null&&isAll.equals("2")) {
			orgList = customBiz.getAllOrg();
		} else {
			orgList = customBiz.getAllOrg(userNum);
		}
		List<SalOrg> orgList2;
		orgList2 = empBiz.findAllSalOrg(); 
		request.setAttribute("orgList2", orgList2);
		request.setAttribute("orgTopCode", EmpAction.getORGTOPCODE());
		request.setAttribute("OrdTypeList", list2);
		request.setAttribute("orgList", orgList);
		request.setAttribute("orgTopCode", EmpAction.getORGTOPCODE());
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		*//**************查询条件***************//*
		String uCode = "";
		String uCode1 = "";//需修改
		String[] users=null;
		String name=request.getParameter("userCBName");
		
		if(name!=null&&!name.equals("")){
			users = request.getParameterValues(name);
		}
		
		if (users != null) {
			for (int j = 0; j < users.length; j++) {
				uCode += "'" + users[j] + "',";
				uCode1+= users[j]+","; 
			}
			if (!uCode.equals(""))
				uCode = uCode.substring(0, uCode.length() - 1);
		}
		else if (limCode != null && limCode != "") {
			users = limCode.split(",");
			int n = users.length;
			for (int i = 0; i < n; i++) {
				uCode += "'" + users[i] + "',";
				uCode1+= users[i]+","; 
			}
			if (!uCode.equals(""))
				uCode = uCode.substring(0, uCode.length() - 1);
		}
		String empCode = request.getParameter("empCode");
		String ucode2 = "";
		String ucode3 = "";
		String[] empCodes=null;
		String name1=request.getParameter("empCBName");
		if(name1!=null&&!name1.equals(""))
			empCodes = request.getParameterValues(name1);
		if (empCodes != null) {
			for (int j = 0; j < empCodes.length; j++) {
				ucode2 += "'" + empCodes[j] + "',";
				ucode3+= empCodes[j]+","; 
			}
			if (!ucode2.equals(""))
				ucode2 = ucode2.substring(0, ucode2.length() - 1);
		}
		else if (empCode != null && empCode != "") {
			empCodes = empCode.split(",");
			int n = empCodes.length;
			for (int i = 0; i < n; i++) {
				ucode2 += "'" + empCodes[i] + "',";
				ucode3+= empCodes[i]+","; 
			}
			if (!ucode2.equals(""))
				ucode2 = ucode2.substring(0, ucode2.length() - 1);
		}
		String cusCode = request.getParameter("cusCode");
		String cusName = TransStr.transStr(request.getParameter("cusName"));
		String sodTil = TransStr.transStr(request.getParameter("sodTil"));
		String sodNum = TransStr.transStr(request.getParameter("sodNum"));
		String typeId = request.getParameter("typeId");
		String sodSumMon = request.getParameter("sodSumMon");
		String sodPaidMethod = TransStr.transStr(request.getParameter("sodPaidMethod"));

		String conDate1 = request.getParameter("conDate1");
		String conDate2 = request.getParameter("conDate2");
		String shipDate1 = request.getParameter("shipDate1");
		String shipDate2 = request.getParameter("shipDate2");
		String startDate1 = request.getParameter("startDate1");
		String startDate2 = request.getParameter("startDate2");
		String endDate1 = request.getParameter("endDate1");
		String endDate2 = request.getParameter("endDate2");
		//日期判断
		String[] conDate = OperateDate.checkDate(conDate1, conDate2);
		String[] shipDate = OperateDate.checkDate(shipDate1, shipDate2);
		String[] startDate = OperateDate.checkDate(startDate1, startDate2);
		String[] endDate = OperateDate.checkDate(endDate1, endDate2);
		//交付状态
		String sodShipState = TransStr.transStr(request
				.getParameter("sodShipState"));

		String queryString = orderBiz.queryString("hql", cusCode,
				sodTil, sodNum, typeId, sodSumMon, sodPaidMethod, conDate[0],
				conDate[1], shipDate[0], shipDate[1], uCode,ucode2, startDate[0],
				startDate[1], endDate[0], endDate[1], sodShipState);

		//添加审核状态查询
		String sodAppIsok = request.getParameter("sodAppIsok");
		String isEmpty = request.getParameter("isEmpty");
		Page page = null;
		List<SalOrdCon> list = null;
		if (isEmpty == null || isEmpty.equals("")) {
			page = new Page(orderBiz.getSupCount(queryString,isAll, userNum,
					sodAppIsok), 20);
			page.setCurrentPageNo(Integer.parseInt(p));
			list = orderBiz.ordSupSearch(queryString,isAll, userNum, sodAppIsok, page
					.getCurrentPageNo(), page.getPageSize());
		}
		request.setAttribute("page", page);
		request.setAttribute("ordList", list);
		request.setAttribute("userCBName", name);
		request.setAttribute("limCode", uCode1);
		request.setAttribute("empCode", ucode3);
		request.setAttribute("cusCode", cusCode);
		request.setAttribute("cusName", cusName);
		request.setAttribute("sodTil", sodTil);
		request.setAttribute("sodNum", sodNum);
		request.setAttribute("typeId", typeId);
		request.setAttribute("sodSumMon", sodSumMon);
		request.setAttribute("sodPaidMethod", sodPaidMethod);
		request.setAttribute("conDate1", conDate[0]);
		request.setAttribute("conDate2", conDate[1]);
		request.setAttribute("shipDate1", shipDate[0]);
		request.setAttribute("shipDate2", shipDate[1]);
		request.setAttribute("startDate1", startDate[0]);
		request.setAttribute("startDate2", startDate[1]);
		request.setAttribute("endDate1", endDate[0]);
		request.setAttribute("endDate2", endDate[1]);
		request.setAttribute("sodShipState", sodShipState);
		request.setAttribute("sodAppIsok", sodAppIsok);
		return mapping.findForward("ordSupSearch");
	}*/

	/**
	 * 批量彻底删除<br>
	 * create_date: Feb 28, 2011,9:46:58 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > delType:删除类型
	 * @param response
	 * @return ActionForward 跳转到recBatchDelConfirm 页面<br>
	 * 		attribute > delType:删除类型
	 */
	public ActionForward recBatchDelConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String delType = request.getParameter("delType");
		String type = request.getParameter("type");
		
		request.setAttribute("delType", delType);
		request.setAttribute("type", type);
		return mapping.findForward("recBatchDelConfirm");
	}
	
	/**
	 * 批量彻底删除<br>
	 * create_date: Feb 28, 2011,10:12:40 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > code：主键ID
	 * @param response
	 * @return ActionForward 跳转到成功页面<br>
	 * 		attribute > msg:显示成功信息
	 */
	public ActionForward recBatchDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String code = request.getParameter("code");
		String type = request.getParameter("type");

		if(type.equals("ord")){
			batchOperate.recBatchDel("SalOrdCon","sodCode",code);
			request.setAttribute("msg", "批量删除订单");
		}
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到打印页面<br>
	 * create_date: Mar 17, 2011,2:19:12 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attribute > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward toPrintOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String sodCode = request.getParameter("sodCode");
		SalOrdCon order = orderBiz.getOrdCon(sodCode);
		request.setAttribute("order", order);
		return mapping.findForward("printOrd");
	}
	
	/**
	 * 跳转到客户详情下的产品历史<br>
	 * @param request
	 * 		para,attr > cusId:客户Id
	 * @return ActionForward listCusPro<br>
	 */
	public ActionForward toListCusPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusPro");
	}
	/**
	 * 客户详情下的产品历史<br>
	 * @param request
	 * 		para > cusId:客户Id
	 */
	public void listCusPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem = ""; 
		if(p ==null || p.trim().length()<1){
			p ="1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		String [] items = {"name","order","price","num","tolPrice","conDate","remark"};
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.findByCusIdCount(cusId),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ROrdPro> list = orderBiz.findByCusId(cusId, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProduct");
		awareCollect.add("salOrdCon");
		
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
	
	/**
	 * 跳转到客户下的发货记录<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		  => param : cusId :客户Id
	 * @param response
	 * @return ActionForward toListProdShipmentByCusId<br>
	 * 		attr > cusId :客户Id
	 */
	public ActionForward toListProdShipmentByCusId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusProdShipment");
	}
	
	public void listCusProdShipment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = {"pshNum","pshOrd", "pshCnee", "pshAddr", "prodAmt",
				"salBack", "pshExp","pshAmt","pshDate","pshShipper"};
		if(p == null || p.trim().length()<1){
			p = "1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize = "30";//默认每页条数
		}
		if(orderCol !=null && !orderCol.equals("")){
				orderItem = items[Integer.parseInt(orderCol)];
		}
		String[] args = {cusId,null,null};
		Page page = new Page(orderBiz.listByCusIdCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ProdShipment> list = orderBiz.listByCusId(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("pshOrder");

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
	
	
	public ActionForward toListProdShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		List<TypeList> expList = typeListBiz.getEnabledType("express");
		request.setAttribute("expList", expList);
		return mapping.findForward("listProdShipment");
	}
	
	public void listProdShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String expName=request.getParameter("expName");
		String cusName=request.getParameter("cusName");
		String corName = request.getParameter("corName");
		String ordNum=request.getParameter("ordNum");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = {"pshNum","pshOrd", "pshCnee", "pshAddr", "prodAmt",
				"salBack", "pshExp","pshAmt","pshDate","pshShipper"};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listProdShipmentCount(expName,cusName,ordNum,corName), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ProdShipment> list = orderBiz.listProdShipment(expName,cusName,ordNum,corName, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("pshOrder");

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
	
	
	
	public ActionForward toListOrdPShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String ordId = request.getParameter("ordId");
		request.setAttribute("ordId", ordId);
		return mapping.findForward("listOrdPShipment");
	}
	public void listOrdPShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String ordId=request.getParameter("ordId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "pshNum", "pshCnee", "pshAddr", "prodAmt",
				"salBack", "pshExp", "pshAmt", "pshDate", "pshShipper" };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listPShipmentByOrdCount(ordId), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ProdShipment> list = orderBiz.listPShipmentByOrd(ordId, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();

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
	
	public ActionForward getProdTip(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String filter = request.getParameter("filter");
		List<SalOrdCon> list = orderBiz.getProdTipList(filter); 
		
		request.setAttribute("orders", list);
		request.setAttribute("filter", filter);
		return mapping.findForward("prodTip");
	}
	
	
	public ActionForward delProdShipment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pshId = request.getParameter("pshId");
		if(StringFormat.isEmpty(pshId)){
			request.setAttribute("errorMsg", "该发货记录不存在");
			return mapping.findForward("error");
		}
		else{
			orderBiz.deleteProdShipment(pshId);
			request.setAttribute("msg", "删除发货记录");
			return mapping.findForward("popDivSuc");
		}
	}
	/**
	 * 保存发货记录 <br>
	 * @param request
	 * 		para >	ordId:订单ID;
	 * 		attr >	expList:物流公司列表;		provList:省份列表;	order:订单实体;
	 * @return ActionForward saveProdShipment
	 */
	public ActionForward toSaveProdShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String ordId = request.getParameter("ordId");
		SalOrdCon order = orderBiz.getOrdCon(ordId);
		List<TypeList> expList = typeListBiz.getEnabledType("express");
		request.setAttribute("expList", expList);
		request.setAttribute("order", order);
		return mapping.findForward("saveProdShipment");
	}
	public void saveProdShipment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String ordId = request.getParameter("ordId");
		String pshId = request.getParameter("pshId");
		String expId = request.getParameter("expId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		String deliveryDate = request.getParameter("deliveryDate");
		String addrId = request.getParameter("pshAddrId");
		DynaActionForm prodShipmentForm = (DynaActionForm) form;
		ProdShipment prodShipment = (ProdShipment) prodShipmentForm.get("prodShipment");
		String addr = "";
		String saveRs = "0";//保存结果,1为成功
		
		try{
			prodShipment.setPshOrder(new SalOrdCon(ordId));
			if(!StringFormat.isEmpty(expId)) { 
				TypeList expType = typeListBiz.findById(Long.parseLong(expId));
				if(expType!=null){
					prodShipment.setPshExpress(expType.getTypName()); 
				}
			}
			if(!StringFormat.isEmpty(provId)) { addr += typeListBiz.findProvince(Long.parseLong(provId)).getAreName(); }
			if(!StringFormat.isEmpty(cityId)) { addr += typeListBiz.findCity(Long.parseLong(cityId)).getPrvName(); }
			if(!StringFormat.isEmpty(districtId)) { addr += typeListBiz.findDistrict(Long.parseLong(districtId)).getCityName(); }
			if(!StringFormat.isEmpty(addrId)) { addr+= customBiz.findCusAddrById(Long.parseLong(addrId)).getCadAddr(); }
			prodShipment.setPshAddr(addr);
			//插入日期
			if (!StringFormat.isEmpty(deliveryDate)) {
				prodShipment.setPshDeliveryDate(GetDate.formatDate(deliveryDate));
			}
			if(StringFormat.isEmpty(pshId)){
				orderBiz.saveProdShipment(prodShipment,ordId);
			}
			else{
				prodShipment.setPshId(Long.parseLong(pshId));
				orderBiz.updProdShipment(prodShipment);
			}
			saveRs = prodShipment.getPshId().toString();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(saveRs);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 保存发货明细 <br>
	 * @param request
	 * 		para >	pshId:发货单ID;		prodIds:产品ID集合;	totalAmt:总运费;	
	 * 				transNum+i:发货数量;	transAmt+i:发货运费;
	 */
	public void savePshProd(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String pshId = request.getParameter("pshId");
		String prodIds = request.getParameter("prodIds");
		String totalAmt = request.getParameter("totalAmt");
		String[] prodIdsArr = prodIds.split(",");
		String[] transNums = new String[prodIdsArr.length];
		String[] outNums = new String[prodIdsArr.length];
		String[] transNum1s = new String[prodIdsArr.length];
		String[] transNum2s = new String[prodIdsArr.length];
		String[] transAmt1s = new String[prodIdsArr.length];
		String[] transAmt2s = new String[prodIdsArr.length];
		String[] transUnit1s = new String[prodIdsArr.length];
		String[] transUnit2s = new String[prodIdsArr.length];
		String[] prodAmts = new String[prodIdsArr.length];
		for(int i=0; i<prodIdsArr.length; i++){
			transNums[i] = request.getParameter("transNum"+prodIdsArr[i]);
			outNums[i] = request.getParameter("outNum"+prodIdsArr[i]);
			prodAmts[i] = request.getParameter("prodAmt"+prodIdsArr[i]);
			transNum1s[i] = request.getParameter("transNumA"+prodIdsArr[i]);
			transNum2s[i] = request.getParameter("transNumB"+prodIdsArr[i]);
			transAmt1s[i] = request.getParameter("transAmtA"+prodIdsArr[i]);
			transAmt2s[i] = request.getParameter("transAmtB"+prodIdsArr[i]);
			transUnit1s[i] = request.getParameter("transUnitA"+prodIdsArr[i]);
			transUnit2s[i] = request.getParameter("transUnitB"+prodIdsArr[i]);
		}
		
		String saveRs = "0";//保存结果,1为成功
		
		try{
			if(!StringFormat.isEmpty(pshId)){
				orderBiz.savePshProd(pshId, prodIdsArr, transNums, transNum1s,
						transNum2s, transAmt1s, transAmt2s, transUnit1s,
						transUnit2s, prodAmts, totalAmt,outNums);
				saveRs = "1";
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(saveRs);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 调整发货数量 <br>
	 * @param request
	 * 		para >	rspIds:发货明细Id;	shipNum**:发货货数量;
	 */
	public void updShipNum(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String rspIds = request.getParameter("rspIds");
		String[] rspIdsArr = rspIds.split(",");
		String[] prodNums = new String[rspIdsArr.length];
		String[] prodOutNums = new String[rspIdsArr.length];
		String[] shipNum1s = new String[rspIdsArr.length];
		String[] shipNum2s = new String[rspIdsArr.length];
		for(int i=0; i<rspIdsArr.length; i++){
			prodNums[i] = request.getParameter("prodNum"+rspIdsArr[i]);
			prodOutNums[i] = request.getParameter("prodOutNum"+rspIdsArr[i]);
			shipNum1s[i] = request.getParameter("shipNumA"+rspIdsArr[i]);
			shipNum2s[i] = request.getParameter("shipNumB"+rspIdsArr[i]);
		}
		
		String saveRs = "0";//保存结果,1为成功
		
		try{
			if(rspIds!=null && rspIdsArr.length>0){
				orderBiz.updShipNums(rspIdsArr,prodNums,prodOutNums,shipNum1s,shipNum2s);
				saveRs = "1";
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(saveRs);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public ActionForward showShipmentDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pshId = request.getParameter("pshId");
		ProdShipment psh = orderBiz.findProdShipment(pshId);
		request.setAttribute("psh", psh);
		return mapping.findForward("showShipmentDesc");
	}
	
	public void listPshProds(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String pshId=request.getParameter("pshId");
		
		List<RShipPro> list = orderBiz.listRShipProByPsh(pshId);
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		List awareCollect = new LinkedList();
		awareCollect.add("rshpProd");

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
	
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}
	public void setCustomBiz(CustomBIZ customBiz) {
		this.customBiz = customBiz;
	}
	public void setPaidBiz(PaidBIZ paidBiz) {
		this.paidBiz = paidBiz;
	}
	public void setWmsManageBiz(WmsManageBIZ wmsManageBiz) {
		this.wmsManageBiz = wmsManageBiz;
	}
	public void setWwoBIZ(WwoBIZ wwoBIZ) {
		this.wwoBIZ = wwoBIZ;
	}
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}
}