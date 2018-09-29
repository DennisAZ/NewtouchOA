package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.PaidBIZ;
import com.psit.struts.BIZ.SalTaskBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SalPaidPlan;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatOrd;
import com.psit.struts.entity.StatSalMon;
import com.psit.struts.util.ListForm;
import com.psit.struts.form.StatSalMForm;
import com.psit.struts.util.GetXml;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.OperateList;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.TransStr;

/**
 * 统计 <br>
 */
public class StatisticAction extends BaseDispatchAction {
	UserBIZ userBiz = null;
	EmpBIZ empBiz = null;
	OrderBIZ orderBiz = null;
	PaidBIZ paidBiz = null;
	SalTaskBIZ salTaskBiz = null;
	CustomBIZ customBIZ = null;
	TypeListBIZ typeListBiz = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

	/**
	 * 判断用户是否有统计的权限 <br>
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
	 * 检测用户是否有统计报表的操作的权限码 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = {
				{ "showSalTarget", "b034" },//销售指标是否可见
				{ "salTarget", "s033" },//销售目标设置
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

//------- 客户统计 --------
	/**
	 * 跳转到统计客户列表 <br>
	 * @param request
	 * 		para,attr >	statType:统计类型; statItem:统计对象;  empId:员工ID;  empName:员工姓名; nodeIds:员工ID集合;
	 * @return ActionForward 跳转到listStatCus<br>
	 */
	public ActionForward toListStatCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = TransStr.transStr(request.getParameter("statItem"));
		String empId = request.getParameter("empId");
		String empName = TransStr.transStr(request.getParameter("empName"));
		String nodeIds = request.getParameter("nodeIds");
		request.setAttribute("empId",empId);
		request.setAttribute("empName",empName);
		request.setAttribute("statType",statType);
		request.setAttribute("statItem",statItem);
		request.setAttribute("nodeIds",nodeIds);
		return mapping.findForward("listStatCus");
	}
	/**
	 * 统计客户列表 <br>
	 * @param request
	 * 		para >	statType:统计类型; statItem:统计对象;  empId:员工ID; nodeIds:员工ID集合;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listStatCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = request.getParameter("statItem");
		String empId = request.getParameter("empId");
		String nodeIds = request.getParameter("nodeIds");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "cHot", "cName","cType",
				"cAddr", "cPho", "cRem","seName", "cLastDate"};
		String ids = "";
		if(nodeIds!=null&&!nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = { "1",empId,statType,statItem,ids};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listStatCusCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusCorCus> list = customBIZ.listStatCus(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusType");
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
	}
	
	/**
	 * 跳转到客户统计 <br>
	 * @param request
	 * 		para,attr >	statType
	 * @return ActionForward statCus<br>
	 */
	public ActionForward toCusStat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String statType = request.getParameter("statType");
		request.setAttribute("statType", statType);
		return mapping.findForward("statCus");
	}
	/**
	 * 客户类型统计 <br>
	 * @param request
	 * 		param >	nodeIds:员工ID集合;
	 */
	public void statCusType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String nodeIds=request.getParameter("nodeIds");
		
		String ids="";
		if(nodeIds!=null && !nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = {ids};
		StaTable staTab = customBIZ.statCusTypeByEmp(args);
		if(staTab!=null){
			staTab.setDataXML(GetXml.getXml(staTab, "客户类型统计", "负责人", "客户量",true));
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 客户来源统计 <br>
	 * @param request
	 * 		para >	nodeIds:员工ID集合;
	 */
	public void statCusSou(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String nodeIds=request.getParameter("nodeIds");
		
		String ids="";
		if(nodeIds!=null && !nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = {ids};
		StaTable staTab = customBIZ.statEmpCusSou(args);
		if(staTab!=null){
			staTab.setDataXML(GetXml.getXml(staTab, "客户来源统计", "负责人", "客户量",true));
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 客户行业统计 <br>
	 * @param request
	 * 		para >	nodeIds:员工ID集合;
	 */
	public void statCusInd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String nodeIds=request.getParameter("nodeIds");
		
		String ids="";
		if(nodeIds!=null&&!nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = {ids};
		StaTable staTab = customBIZ.statEmpCusInd(args);
		if(staTab!=null){
			staTab.setDataXML(GetXml.getXml(staTab, "客户行业统计", "负责人", "客户量",true));
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 开发到期客户统计<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params : startDate :开始日期     endDate ：结束日期
	 */
	public void statCusOnDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] args = {startDate,endDate};
		List<StatSalMForm> statList = customBIZ.onDateCusList(args);
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		
	}
//------- 销售统计 --------	
	
	/**
	 * 销售提成分析 <br>
	 * @param request
	 * 		para >	nodeIds:员工ID集合;
	 */
	public void statSaleAnalyse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cusId = request.getParameter("cusIds");
		String empIds = request.getParameter("nodeIds");
		String[] args = {startDate,endDate,cusId,empIds};
		StaTable staTab = customBIZ.statSaleAnalyse(args);
//		if(staTab!=null){
//			staTab.setDataXML(GetXml.getXml(staTab, "销售提成分析", "客户", "提成",true));
//		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 跳转到统计发货记录列表 <br>
	 * @param request
	 * 		para,attr >	cusId:客户Id; startDate:开始日期;  endDate:结束日期;  cusName:客户名称; 
	 * @return ActionForward 跳转到listStatSalPast<br>
	 */
	public ActionForward toListStatProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cusName = TransStr.transStr(request.getParameter("cusName"));
		
		request.setAttribute("cusId", cusId);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("cusName", cusName);
		return mapping.findForward("listStatProd");
	}

	/**
	 * 统计发货记录列表 <br>
	 * @param request
	 * 		para >	cusId:客户Id; startDate:开始日期;  endDate:结束日期;  cusName:客户名称; 
	 */
	public void listStatProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] args = {cusId,startDate,endDate};
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
	
	
	/**
	 * 跳转到销售统计 <br>
	 * @param request
	 * 		para,attr >	statType
	 * @return ActionForward statSal<br>
	 */
	public ActionForward toSalStat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String statType = request.getParameter("statType");
		request.setAttribute("statType", statType);
		return mapping.findForward("statSal");
	}
	
	/**
	 * 销售金额统计 <br>
	 * @param request
	 * 		para >	startDate,endDate:统计日期;	nodeIds:员工ID集合; 	cusIds:客户ID集合;
	 */
	public void statSalM(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String empIds = request.getParameter("nodeIds");
		String cusIds = request.getParameter("cusIds");
		List<StatSalMForm> statList = orderBiz.statSalM(empIds,cusIds,startDate,endDate);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getCusName(),
					Double.toString(statSal.getMonSum1()),
					Double.toString(statSal.getMonSum2()),
					Double.toString(statSal.getMonSum3())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "订单额", "发货额", "回款额" }, "销售金额统计",
				"客户", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * (月平均发货额)未达最低销售金额客户 <br>
	 * @param request
	 * 		para >	startDate,endDate:统计日期;	nodeIds:员工ID集合; 	cusIds:客户ID集合;
	 */
	public void statLowestSals(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String empIds = request.getParameter("nodeIds");
		String cusIds = request.getParameter("cusIds");
		List<StatSalMForm> statList = orderBiz.statLowestSals(empIds,cusIds,startDate,endDate);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getCusName(),
					Double.toString(statSal.getMonSum1()),
					Double.toString(statSal.getMonSum2()),
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "最低销售额", "实际发货额" }, "未达最低销售额客户",
				"客户", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 提成金额统计 <br>
	 * @param request
	 * 		para >	startDate,endDate:统计日期;	nodeIds:员工ID集合; 	cusIds:客户ID集合;
	 */
	public void statSalBack(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String empIds = request.getParameter("nodeIds");
		String cusIds = request.getParameter("cusIds");
		List<StatSalMForm> statList = orderBiz.statSalBack(empIds,cusIds,startDate,endDate);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getCusName(),
					Double.toString(statSal.getMonSum1())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "提成金额" }, "业务提成统计",
				"客户", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 回款分析 <br>
	 * @param request
	 * 		para >	startDate1,endDate1:统计日期;
	 */
	public void statSpsAnalyse(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate1");
		String endDate = request.getParameter("endDate1");
		List<StatSalMForm> statList = paidBiz.statSpsAnalyse(startDate,endDate);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getCusName(),
					Double.toString(statSal.getMonSum1()),
					Double.toString(statSal.getMonSum2()),
					Double.toString(statSal.getMonSum3()),
					Double.toString(statSal.getMonSum4())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "回款金额","应收余额"," 最高余额", "期初余额" }, "回款分析",
				"客户", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 跳转到统计回款记录列表 <br>
	 * @param request
	 * 		para,attr >	cusId:客户Id; startDate:开始日期;  endDate:结束日期;  cusName:客户名称; 
	 * @return ActionForward 跳转到listStatSalPast<br>
	 */
	public ActionForward toListStatSalPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cusName = TransStr.transStr(request.getParameter("cusName"));
		
		request.setAttribute("cusId", cusId);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("cusName", cusName);
		return mapping.findForward("listStatSalPast");
	}

	/**
	 * 统计回款记录列表 <br>
	 * @param request
	 * 		para >	cusId:客户Id; startDate:开始日期;  endDate:结束日期;  cusName:客户名称; 
	 */
	public void listStatSalPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "spsIsInv", "spsCus", "spsOrd", "spsMon",
				"spsPayType", "spsDate", "spsUser" };
		String[] args = { cusId, startDate, endDate};
		
		if (p == null || p.equals("")) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listPaidPastsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPaidPast> list = paidBiz.listPaidPasts(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salEmp");
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
	 * 产品提成分析 <br>
	 * @param request
	 * 		para >	startDate1,endDate1:发货日期;  wprId：产品Id
	 */
	public void statPsalBack(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate1");
		String endDate = request.getParameter("endDate1");
		String wprId = request.getParameter("wprId");
		List<StatSalMForm> statList = orderBiz.statPsalBack(startDate,endDate,wprId);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getEmpName(),
					Double.toString(statSal.getMonSum1())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "提成金额" }, "产品提成分析",
				"员工", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 跳转到统计发货明细列表 <br>
	 * @param request
	 * 		para,attr >	empId:员工Id startDate:开始日期;  endDate:结束日期;  wprId:商品Id; 
	 * @return ActionForward 跳转到listStatSalPast<br>
	 */
	public ActionForward toListStatPsalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String empId = request.getParameter("empId");
		String wprId = request.getParameter("wprId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String empName = TransStr.transStr(request.getParameter("empName"));
		
		request.setAttribute("empId", empId);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("wprId", wprId);
		request.setAttribute("empName", empName);
		return mapping.findForward("listStatPsalBack");
	}
	
	/**
	 * 跳转到统计发货明细列表 <br>
	 * @param request
	 * 		para,attr >	empId:员工Id startDate:开始日期;  endDate:结束日期;  wprId:商品Id; 
	 * @return ActionForward 跳转到listStatSalPast<br>
	 */
	public void liststatPsalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String empId = request.getParameter("empId");
		String wprId = request.getParameter("wprId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "wprName","deliveryDate","rshpCount","rshpProdAmt","rshpPrice","rshpSalBack","rshpOutCount"};
		String[] args = { empId,wprId, startDate, endDate};
		
		if (p == null || p.equals("")) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listShipProCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<RShipPro> list = orderBiz.listShipPro(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("rshpProd");
		awareCollect.add("rshpShipment");
		
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
	 * 产品分析 <br>
	 * @param request
	 * 		para >	startDate1,endDate1:发货日期;  wprId：产品Id ; startDate,endDate: 签单日期 
	 */
	public void statProAnalyse(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate1 = request.getParameter("startDate1");
		String endDate1 = request.getParameter("endDate1");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String wprId = request.getParameter("wprId");
		List<StatSalMForm> statList = orderBiz.statProAnalyse(startDate,endDate,wprId,startDate1,endDate1);
		Iterator<StatSalMForm> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMForm statSal = it.next();
			datas[i] = new String[]{
					statSal.getEmpName(),
					Double.toString(statSal.getMonSum2())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "销售价格" }, "产品分析",
				"客户", "金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * 应收款分析<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > cusId:客户ID orderCol:排序字段  pageSize:每页显示条数  isDe:是否逆序
	 */
	public void statRecvAnalyse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String corRecDate = request.getParameter("corRecDate");
		String timeRage = request.getParameter("timeRage");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] args  ={corRecDate,timeRage};
		String [] items = {"recvAmt","recDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		List<CusCorCus> list = customBIZ.statRecvAnalyse(args, orderItem, isDe);
		
		Iterator<CusCorCus> it = list.iterator();
		String[][]datas = new String[list.size()][3];
		int i=0;
		while(it.hasNext()){
			CusCorCus cus = it.next();
			datas[i] = new String[]{
					cus.getCorName(),
					Double.toString(cus.getCorRecvAmt())
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "应收款" }, "应收款分析",
				"客户", "金额");
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setDataXML(dataXML);
		List awareCollect = new LinkedList();
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
	 * 跳转到统计订单列表 <br>
	 * @param request
	 * 		para >	statType:统计类型; statItem:统计对象;  empId:员工ID;  empName:员工姓名; 
	 * 				nodeIds:员工ID集合; startDate,endDate:签订日期 ;
	 * @return ActionForward 跳转到listStatOrd<br>
	 * 		attr >	statType:统计类型; statItem:统计对象;  empId:员工ID;  empName:员工姓名; 
	 * 				nodeIds:员工ID集合; startDate,endDate:签订日期;
	 */
	public ActionForward toListStatOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = TransStr.transStr(request.getParameter("statItem"));
		String empId = request.getParameter("empId");
		String empName = TransStr.transStr(request.getParameter("empName"));
		String nodeIds = request.getParameter("nodeIds");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		request.setAttribute("empId",empId);
		request.setAttribute("empName",empName);
		request.setAttribute("statType",statType);
		request.setAttribute("statItem",statItem);
		request.setAttribute("startDate",startDate);
		request.setAttribute("endDate",endDate);
		request.setAttribute("nodeIds",nodeIds);
		return mapping.findForward("listStatOrd");
	}
	/**
	 * 统计订单列表 <br>
	 * @param request
	 * 		para >	statType:统计类型; statItem:统计对象;  empId:员工ID;
	 * 				nodeIds:员工ID集合; startDate,endDate:签订日期 ;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listStatOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = request.getParameter("statItem");
		String empId = request.getParameter("empId");
		String nodeIds = request.getParameter("nodeIds");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "appState","ordState","oType", "oTil", "oNum", "oCus",
				"oPaid", "oMon", "oConDate", "oEmp"};
		String ids = "";
		if(nodeIds!=null&&!nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = { "0",empId,statType,statItem,ids,startDate,endDate};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listStatOrdCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalOrdCon> list = orderBiz.listStatOrd(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("sodShipState");
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
	}
	
	/**
	 * 跳转到订单类别统计 <br>
	 * @param request
	 * @return ActionForward 跳转到statOrdSimple<br>
	 * 		attr >	statType:统计类别;
	 */
	/*public ActionForward toStatOrdType(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statType", "ordType");
		return mapping.findForward("statOrdSimple");
	}*/
	/**
	 * 订单类别统计 <br>
	 * @param request
	 * 		para > 	startDate,endDate:签订日期;
	 */
	/*public void statOrdType(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] args = {startDate,endDate};
		List<StatOrd> statList = orderBiz.statOrdType(args);
		Iterator<StatOrd> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatOrd statOrd = it.next();
			datas[i] = new String[]{
					statOrd.getTypName(),
					statOrd.getCount().toString(),
					statOrd.getSum().toString()
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "数量,S", "金额,P" }, "订单类别统计",
				"订单类别", "数量,金额");
		
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}*/
	
	/**
	 * 订单来源统计 <br>
	 * @param request
	 * @return ActionForward 跳转到statOrdSimple<br>
	 * 		attr >	statType:统计类别;
	 */
	public ActionForward toStatOrdSou(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statType", "ordSou");
		return mapping.findForward("statOrdSimple");
	}
	/**
	 * 订单来源统计 <br>
	 * @param request
	 * 		para > 	startDate,endDate:签订日期;
	 */
	public void statOrdSou(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String[] args = {startDate,endDate};
		
		if(startDate!=null){
			List<StatOrd> statList = orderBiz.statOrdSou(args);
			Iterator<StatOrd> it = statList.iterator();
			String[][]datas = new String[statList.size()][3];
			int i=0;
			while(it.hasNext()){
				StatOrd statOrd = it.next();
				datas[i] = new String[]{
						statOrd.getTypName(),
						statOrd.getCount().toString(),
						statOrd.getSum().toString()
					};
				i++;
			}
			String dataXML = GetXml.getXml(datas, new String[] { "数量,S", "金额,P" }, "订单来源统计",
					"订单来源", "数量,金额");
			ListForm listForm = new ListForm();
			listForm.setList(statList);
			listForm.setDataXML(dataXML);
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8"); 
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.print(new JSONConvert().model2JSON(listForm));
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				out.close();
			}
		}
	}
	
	/**
	 * 销售额人员月度统计 <br>
	 * @param request
	 * @return ActionForward 跳转到statOrdDyn<br>
	 * 		attr >	statType:统计类型;
	 */
	public ActionForward toStatOrdEmpMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statType", "ordEmpMon");
		return mapping.findForward("statOrdDCol");
	}
	/**
	 * 销售额人员月度统计 <br>
	 * @param request
	 * 		para >	startMon:开始月;  endMon:结束月;  nodeIds:员工ID集合;
	 */
	public void statOrdEmpMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startMon = request.getParameter("startMon");
		String endMon = request.getParameter("endMon");
		String nodeIds=request.getParameter("nodeIds");
		
		String ids="";
		if(nodeIds != null && !nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = {startMon, endMon, ids};
		StaTable staTab = orderBiz.statOrdEmpMon(args);
		List<StatOrd> statList = orderBiz.statOrdMon(args);
		Iterator<StatOrd> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatOrd statOrd = it.next();
			datas[i] = new String[]{
					statOrd.getTypName(),
					statOrd.getCount().toString(),
					statOrd.getSum().toString()
				};
			i++;
		}
		if(staTab!=null){
			staTab.setDataXML(GetXml.getXml(datas, new String[] { "数量,S", "金额,P" }, "销售额人员月度统计",
				"签订月份", "数量,金额"));
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 统计回款记录列表 <br>
	 * @param request
	 * 		para >	statType:统计类型; statItem:统计对象;  empId:员工ID;  empName:员工姓名; 
	 * 				nodeIds:员工ID集合; 
	 * @return ActionForward 跳转到listStatPaid<br>
	 * 		attr >	statType:统计类型; statItem:统计对象;  empId:员工ID;  empName:员工姓名; 
	 * 				nodeIds:员工ID集合;
	 */
	public ActionForward toListStatPaid(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = TransStr.transStr(request.getParameter("statItem"));
		String empId = request.getParameter("empId");
		String empName = TransStr.transStr(request.getParameter("empName"));
		String nodeIds = request.getParameter("nodeIds");
		request.setAttribute("empId",empId);
		request.setAttribute("empName",empName);
		request.setAttribute("statType",statType);
		request.setAttribute("statItem",statItem);
		request.setAttribute("nodeIds",nodeIds);
		return mapping.findForward("listStatPaid");
	}
	/**
	 * 统计回款记录列表 <br>
	 * @param request
	 * 		para >	statType:统计类型; statItem:统计对象;  empId:员工ID; 
	 * 				nodeIds:员工ID集合; 
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listStatPaid(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String statType = request.getParameter("statType");
		String statItem = request.getParameter("statItem");
		String empId = request.getParameter("empId");
		String nodeIds = request.getParameter("nodeIds");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "spsIsInv", "spsCus", "spsOrd", "spsMon",
				"spsPayType", "spsDate", "spsUser" };
		String ids = "";
		if(nodeIds!=null&&!nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = { "0",empId,statType,statItem,ids};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listStatPaidCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPaidPast> list = paidBiz.listStatPaid(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salOrdCon");
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
	}
	
	/**
	 * 跳转到回款记录人员月度统计 <br>
	 * @param request
	 * @return ActionForward 跳转到statOrdDyn<br>
	 * 		attr >	statType:统计类型;
	 */
	public ActionForward toStatPaidEmpMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statType", "paidEmpMon");
		return mapping.findForward("statOrdDyn");
	}
	/**
	 * 回款记录人员月度统计 <br>
	 * @param request
	 * 		para >	startMon:开始月;  endMon:结束月;  nodeIds:员工ID集合;
	 */
	public void statPaidEmpMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startMon = request.getParameter("startMon");
		String endMon = request.getParameter("endMon");
		String nodeIds=request.getParameter("nodeIds");
		String ids="";
		if(nodeIds != null && !nodeIds.equals("")){
			ids = nodeIds.substring(0, nodeIds.length()-1);
		}
		String[] args = {startMon, endMon, ids};
		StaTable staTab = paidBiz.statPaidEmpMon(args);
		if(staTab!=null){
			staTab.setDataXML(GetXml.getXml(staTab, "回款额人员月度统计", "回款月份", "回款金额",false));
		}
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(staTab!=null){
				out.print(new JSONConvert().model2JSON(staTab));
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 跳转到销售月度统计 <br>
	 * @param request
	 * @return ActionForward 跳转到statOrdDSum<br>
	 * 		attr >	statType:统计类型; 
	 */
	public ActionForward toStatOrdPaidMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statType", "ordPaidMon");
		return mapping.findForward("statOrdDSum");
	}
	/**
	 * 销售月度统计 <br>
	 * @param request
	 * 		para >	startMon:开始月;  endMon:结束月;
	 */
	public void statOrdPaidMon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startMon = request.getParameter("startMon");
		String endMon = request.getParameter("endMon");
		
		String[] args = {startMon, endMon};
		List<StatSalMon> statList = paidBiz.statPaidMon(args);
		Iterator<StatSalMon> it = statList.iterator();
		String[][]datas = new String[statList.size()][3];
		int i=0;
		while(it.hasNext()){
			StatSalMon statSalMon = it.next();
			datas[i] = new String[]{
					statSalMon.getMonth(),
					statSalMon.getPaidSum().toString(),
					statSalMon.getOrdSum().toString()
				};
			i++;
		}
		String dataXML = GetXml.getXml(datas, new String[] { "回款金额","订单金额" }, "销售月度统计",
				"统计月份", "金额");
		ListForm listForm = new ListForm();
		listForm.setList(statList);
		listForm.setDataXML(dataXML);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(new JSONConvert().model2JSON(listForm));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	
	/** 
	 * 进入目标页面 <br>
	 * create_date: Aug 23, 2010,10:45:44 AM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到target页面<br>
	 * 		attribute > year:当前的年份 month:当前的月份
	 */
	public ActionForward salTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String usernum = (String) request.getSession().getAttribute("userNum");
		Calendar time = Calendar.getInstance();
		int year = time.get(1);
		int month = time.get(2) + 1;
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		return mapping.findForward("target");
	}

	/**
	 * 我的销售业绩 <br>
	 * create_date: Aug 24, 2010,11:52:52 AM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到lastSalStat页面<br>
	 * 		attribute >countAll:符合条件的销售业绩总数 ordStatistic:符合条件的销售业绩列表
	 */
	public ActionForward getLastSalTable(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute(
		"limUser");
		java.util.Date date = new Date();
		String date1 = dateFormat.format(date);
		String date2 = dateFormat.format(OperateDate.getMonth(date, -6));
		String hql = orderBiz.lastSalStat(user.getSalEmp().getSeNo(), date2, date1);
		List list = orderBiz.sumMonByDate(hql);
		List list2 = new ArrayList();
		for (int k = 5; k >= 0; k--) {
			String date3 = dateFormat.format(OperateDate.getMonth(date, -k));
			OrdStatistic sta = new OrdStatistic();
			Double num = 0.0;
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					OrdStatistic sta1 = (OrdStatistic) list.get(i);
					if (sta1.getName().equals(date3)) {
						num = sta1.getDnum();
					}
				}
			}
			sta.setName(date3);
			sta.setDnum(num);
			list2.add(sta);
		}
		Double count3 = 0.0;
		if (!OperateList.getTotal(list).equals("")) {
			count3 = Double.parseDouble(OperateList.getTotal(list));
		}
		//		request.setAttribute("date", list2);
		request.setAttribute("countAll", count3);
		request.setAttribute("ordStatistic", list2);
		return mapping.findForward("lastSalStat");
	}

	/**
	 * 我的销售业绩统计报表 <br>
	 * create_date: Aug 24, 2010,2:10:51 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *       parameter > type:统计图类型标识 1:2D柱图 2:3D柱图 3:2D条形图 4:折线图 5:2D面积图 6:2D饼图 7:3D饼图 8:圆环图
	 * @param response
	 * @return ActionForward null 输出报表生成的XML<br>
	 */
	public ActionForward getLastSalXML(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		LimUser user = (LimUser) request.getSession().getAttribute(
				"limUser");
		String type = request.getParameter("type");
		java.util.Date date = new Date();
		String date1 = dateFormat.format(date);
		String date2 = dateFormat.format(OperateDate.getMonth(date, -6));
		String hql = orderBiz.lastSalStat(user.getSalEmp().getSeNo(), date2, date1);
		List list = orderBiz.sumMonByDate(hql);

		String dataXML = GetXml.getXml2(list, "", "", "", type);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			out.print(dataXML);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	/**
	 * 工作台显示销售额在上月末未达到规定金额的单位<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward getLTsals(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<StatSalMForm> statList = orderBiz.getLTsals();
		
		request.setAttribute("statSal", statList);
		return mapping.findForward("toListLTsals");
	}
	
	

	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
	public void setSalTaskBiz(SalTaskBIZ salTaskBiz) {
		this.salTaskBiz = salTaskBiz;
	}
	public void setCustomBIZ(CustomBIZ customBIZ) {
		this.customBIZ = customBIZ;
	}
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}
	public void setPaidBiz(PaidBIZ paidBiz) {
		this.paidBiz = paidBiz;
	}
}