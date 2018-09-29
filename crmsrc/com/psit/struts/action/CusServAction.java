package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import com.psit.struts.BIZ.CusServBIZ;
import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.Quote;
import com.psit.struts.entity.RQuoPro;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOpp;
import com.psit.struts.entity.SalPra;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.CusContact;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.format.TransStr;
import com.psit.struts.util.DAO.BatchOperateDAO;
import com.psit.struts.util.DAO.GetAccDataDAO;

/**
 * 销售机会，行为，客户服务管理 <br>
 */
public class CusServAction extends BaseDispatchAction {
	EmpBIZ empBiz = null;
	CusServBIZ cusServBiz = null;
	CustomBIZ customBIZ = null;
	TypeListBIZ typeListBiz = null;
	UserBIZ userBiz = null;
	BatchOperateDAO batchOperate = null;
	GetAccDataDAO getAccDataDao = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	PrintWriter out;

	/**
	 * 判断用户是否有销售机会，行为，客户服务操作的权限 <br>
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (isLimitAllow(request)) {
			return super.execute(mapping, form, request, response);
		} else {
			return mapping.findForward("limError");
		}
	}
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "toListSalOpps", "a027" },// 销售机会是否可见
				{ "toNewOpp", "r009" },{ "saveSalOpp", "r009" },// 新建销售机会
				{ "toUpdOpp", "r011" },{ "changeSalOpp", "r011" },// 修改销售机会
				{ "delSalOpp", "r010" },// 删除销售机会
				{ "batchDelSalOpp", "r010" },// 批量删除销售机会
				{ "showSalOppInfo", "r012" },// 查看销售机会详情
				{ "toNewQuote", "r021" },{ "saveQuote", "r021" },// 新建机会报价
				{ "toUpdQuo", "r023" },{ "changeQuote", "r023" },// 编辑机会报价
				{ "delQuote", "r022" },// 删除机会报价
				{ "showQuoteInfo", "r024" },// 查看机会报价详情
				{ "toListSalPra", "a028" },// 来往记录是否可见
				{ "toNewPra", "r013" },{ "saveSalPras", "r013" },// 新增来往记录
				{ "toUpdPra", "r015" },{ "changeSalPraInfo", "r015" },// 修改来往记录信息
				{ "delSalPra", "r014" },// 删除来往记录
				{ "batchDelSalPra", "r014" },// 批量删除来往记录
				{ "showSalPraInfo", "r016" },// 查看来往记录详情
				{ "toListServ", "v001" },// 客户服务是否可见
				{ "toNewServ", "v002" },{ "saveCusServs", "v002" },// 新增客户服务
				{ "toUpdServ", "v004" },{ "changeCusServInfo", "v004" },// 修改客户服务信息 
				{ "delServ", "v003" },// 删除客户服务
				{ "batchDelServ", "v003" },// 批量删除客户服务
				{ "showCusServInfo", "v005" },// 查看客户服务详情
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 保存销售机会 <br>
	 * @param request
	 *         parameter > cusId:客户ID stage:机会阶段ID userCode:负责账号
	 *            		   stageName:机会阶段名称 oppFindDate:机会发现日期 oppExeDate:机会关注日期
	 *            		   oppSigDate:机会预计签单日期 isIfrm:是否在详情页面中新建
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:新建成功信息提示
	 */
	public ActionForward saveSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalOpp salOpp = (SalOpp) form1.get("salOpp");
		String cusId = request.getParameter("cusId");
		String seNo=request.getParameter("seNo");
		String stage = request.getParameter("stage");
		String oppFindDate = request.getParameter("oppFindDate");
		String oppExeDate = request.getParameter("oppExeDate");
		String oppSigDate = request.getParameter("oppSigDate");
		String stageName = request.getParameter("stageName");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		if (stage != null && !stage.equals("")) {
			salOpp.setOppStage(new TypeList(Long.valueOf(stage)));
		} else {
			salOpp.setOppStage(null);
		}
		if (cusId != null && !cusId.equals("")) {
			salOpp.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		} else {
			salOpp.setCusCorCus(null);
		}
		try {
			if (oppFindDate!=null&&!oppFindDate.equals("")) {
				salOpp.setOppFindDate(dateFormat.parse(oppFindDate));
			} else {
				salOpp.setOppFindDate(null);
			}
			if (oppExeDate!=null && !oppExeDate.equals("")) {
				salOpp.setOppExeDate(dateFormat.parse(oppExeDate));
			} else {
				salOpp.setOppExeDate(null);
			}
			if (oppSigDate != null && !oppSigDate.equals("")) {
				salOpp.setOppSignDate(dateFormat.parse(oppSigDate));
			} else {
				salOpp.setOppSignDate(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String oppStaLog = "<span class='bold'>日期:" + dateFormat.format(date)
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" + "阶段:" + stageName + "("
				+ salOpp.getOppPossible() + "%)" + "&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "状态:" + salOpp.getOppState() + "&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "推进人:" + limUser.getUserSeName() + "</span><br/>&nbsp;"
				+ "阶段备注:" + salOpp.getOppStaRemark() + "<br/><br/>";
		salOpp.setOppStaLog(oppStaLog);
		salOpp.setOppStaUpdate(date);
		salOpp.setOppInsDate(date1);
		salOpp.setOppStaUpdate(date1);
		salOpp.setOppInpUser(limUser.getUserSeName());
		salOpp.setOppIsDel("1");
		salOpp.setOppIsexe("否");
		salOpp.setSalEmp(limUser.getSalEmp());//发现人
		salOpp.setSalEmp1(new SalEmp(Long.parseLong(seNo)));
		cusServBiz.save(salOpp);

		// 详情下刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建销售机会");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到销售机会列表<br>
	 * @param request
	 * 			parameter > range:0：我的  1：全部
	 * @return ActionForward 跳转到listSalOpp页面<br>
	 * 		attribute > range:0：我的  1：全部
	 */
	public ActionForward toListSalOpps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		
		request.setAttribute("range", range);
		return mapping.findForward("listSalOpp");
	}
	/**
	 * 销售机会列表 <br>
	 * @param request
	 *         	para >  range:0我的1全部  oppTil:销售机会主题  cusCode:客户ID
	 *         			oppLev:机会热度  oppState:机会状态  uCode:负责人  cusName:客户名称 uName:负责人名称
	 *         			p:当前页码 pageSize:每页显示记录数 orderCol:排序列号 isDe:是否逆序 
	 */
	public void listSalOpps(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String oppTil = request.getParameter("oppTil");
//		String code = request.getParameter("cusCode");
		String oppLev = request.getParameter("oppLev");
		String oppState = request.getParameter("oppState");
//		String uCode=request.getParameter("uCode");
		String cusName = request.getParameter("cusName");
		String uName = request.getParameter("uName");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		String seNo = String.valueOf(limUser.getSalEmp().getSeNo());
		String range=request.getParameter("range");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "oppState", "oppTil", "oppLev", "oppCus","oppExeDate",
				"oppDate", "oppStage", "oppPos",null,"seName" };
		String[] args = { "1",range,seNo, oppTil, cusName, oppLev, oppState, uName };
		Date date = new Date();
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(cusServBiz.listSalOppsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalOpp> list = cusServBiz.listSalOpps(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		//计算阶段停留时间
		Iterator<SalOpp> ite = list.iterator();
		while (ite.hasNext()) {
			SalOpp opp = ite.next();
			Date oldDate = opp.getOppStaUpdate();
			if (oldDate != null && date.after(oldDate)) {
				opp.setOppDayCount(String.valueOf(OperateDate.getDayNum(
						oldDate, date)));
			} else {
				opp.setOppDayCount("0");
			}
		}
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("oppStage");
		awareCollect.add("cusCorCus");
		awareCollect.add("salEmp1");
		
		
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
	 * 查询当天的销售机会，未来七天的销售机会 <br>
	 * @param request
	 *         parameter > isDeskTop:是否为工作台上的机会提醒若不为空则为工作台上的提醒
	 *                     range:标识查询范围0表示查询我的销售机会1表示查询我的下属销售机会2表示全部销售机会
	 * @param response
	 * @return ActionForward isDeskTop不为空则跳转到lastOpp页面否则跳转到readyExeOpp页面<br>
	 *         attribute > num:当天销售机会数量 num2:未来七天销售机会数量 curOppList:当天销售机会列表
	 *         			   lastOppList:未来七天销售机会列表 range:标识查询范围0表示查询我的销售机会1表示查询我的下属销售机会2表示全部销售机会<br>
	 */
	public ActionForward getOppByExeDate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String range=request.getParameter("range");
		request.setAttribute("range",range);
		LimUser limUser=(LimUser) request.getSession().getAttribute("limUser");
		String seNo = String.valueOf(limUser.getSalEmp().getSeNo());
		String isDeskTop = request.getParameter("isDeskTop");
		Date startDate = new Date();
		List list = cusServBiz.getOppByExeDate(0, 0, startDate, startDate,
				range,seNo);
		int num = cusServBiz
				.getOppByExeDateCount(startDate, startDate,range,seNo);
		startDate = OperateDate.getDate(startDate, 1);
		Date endDate = OperateDate.getDate(startDate, 6);
		List list2 = cusServBiz.getOppByExeDate(0, 0, startDate, endDate,
				range,seNo);
		int num2 = cusServBiz.getOppByExeDateCount(startDate, endDate,range,seNo);
		request.setAttribute("num", num);
		request.setAttribute("num2", num2);
		request.setAttribute("curOppList", list);
		request.setAttribute("lastOppList", list2);
		if (isDeskTop != null && !isDeskTop.equals("")) {
			return mapping.findForward("lastOpp");
		} else {
			return mapping.findForward("readyExeOpp");
		}
	}
	
	/**
	 * 跳转到来往记录列表<br>
	 * @param request
	 * 		para,attr > range :0:我的,1：全部; 
	 * @return ActionForward 跳转到listSalPra页面<br>
	 */
	public ActionForward toListSalPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		request.setAttribute("range", range);
		return mapping.findForward("listSalPra");
	}
	/**
	 * 列出我和全部来往记录<br>
	 * @param request
	 * 		para >  range: 0:我的来往记录,1:全部来往记录; filter:过滤器（过滤出过期的来往记录）; 
	 * 				cusName:客户名称;  uName:负责人名;  praRemark:联系内容;
	 * 				 praType:方式;  startDate,endDate:执行日期;
	 * 				p:当前页码;  pageSize:每页显示记录数;  orderCol:排序列号;  isDe:是否逆序; 
	 */
	public void listSalPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range=request.getParameter("range"); 
		String filter = request.getParameter("filter");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		String seNo = String.valueOf(limUser.getSalEmp().getSeNo());
		String cusName = request.getParameter("cusName");
		String praRemark = request.getParameter("praRemark");
		String uName = request.getParameter("uName");
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String praType = request.getParameter("praType");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";

		String [] items = {"praType", "praRemark", "customer", "praExeDate", "seName", "praInsDate"};
		String [] args = {"1", range,seNo, cusName, praRemark, praType, startDate, endDate,uName,filter};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(cusServBiz.listSalPrasCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPra> list = cusServBiz.listSalPras(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
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
	 * 查询当天待执行的，七天内待执行的来往记录 <br>
	 * @param request
	 *         parameter > isDeskTop:显示是否是工作台上的行为提醒若不为空则为工作台上的提醒
	 *                     range:标识查询范围0表示查询我的来往记录1表示查询我的下属来往记录2表示全部来往记录 
	 * @return ActionForward isDeskTop不为空则跳转到lastPra页面否则跳转到readyExePra页面<br>
	 *         attribute > num:当天待执行来往记录数量 num2:七天内待执行的来往记录数量 num3:过期未执行的行为数量
	 *         			   curPraList:当天来往记录列表 lastPraList:七天内来往记录列表
	 *                     range:标识查询范围0表示查询我的来往记录1表示查询我的下属来往记录2表示全部来往记录 
	 */
	public ActionForward getPraByExeDate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String range=request.getParameter("range");
		request.setAttribute("range",range);
		LimUser limUser = (LimUser)request.getSession().getAttribute("limUser");
		String seNo = String.valueOf(limUser.getSalEmp().getSeNo());
		String isDeskTop = request.getParameter("isDeskTop");
		Date startDate = new Date();
		Date endDate = new Date();
		List list = cusServBiz.getPraByExeDate(startDate, startDate,range,seNo);
		int num = cusServBiz.getPraByExeDateCount(startDate, startDate,range,seNo);
		startDate = OperateDate.getDate(startDate, 1);
		endDate = OperateDate.getDate(startDate, 6);
		List list2 = cusServBiz.getPraByExeDate(startDate, endDate,range,seNo);
		int num2 = cusServBiz
				.getPraByExeDateCount(startDate, endDate,range,seNo);
		request.setAttribute("num", num);
		request.setAttribute("num2", num2);
		request.setAttribute("curPraList", list);
		request.setAttribute("lastPraList", list2);
		if (isDeskTop != null && !isDeskTop.equals("")) {
			return mapping.findForward("lastPra");
		} else {
			return mapping.findForward("readyExePra");
		}
	}

	/**
	 * 跳转到客服列表<br>
	 * @param request	
	 * 				parameter > range: 0:待处理 1:已处理
	 * @return ActionForward listCusServ <br>
	 * 		attribute > range: 0:待处理 1:已处理
	 */
	public ActionForward toListServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		
		request.setAttribute("range", range);
		return mapping.findForward("listCusServ");
	}
	/**
	 * 列出我的和全部的客户服务 <br>
	 * @param request
	 *         parameter > cusCode:客户ID range:标识查询范围 0:待处理 1:已处理  servTitle:主题 cusName:客户名称 seName:处理人
	 *            		   serState:状态 serMethod:关怀方式  
	 *            		   startTime、endTime:执行日期区间 p:当前页码  userNum:当前登录人用户码 pageSize:每页显示记录数 orderCol:排序列号  isDe:是否逆序 
	 */
	public void listServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String code = request.getParameter("cusCode");
		String range=request.getParameter("range");
		String seName =request.getParameter("seName");
		String servTitle = request.getParameter("servTitle");
		String cusName = request.getParameter("cusName");
		String serMethod = request.getParameter("serMethod");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"title","customer","mothed","content","seName","exeDate"};
		String[] args = { "1",range, seName, cusName, null,serMethod, startTime,endTime,servTitle};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}

		Page page = new Page(cusServBiz.getServCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusServ> list = cusServBiz.getServ(args,orderItem,isDe,page.getCurrentPageNo(), page.getPageSize());

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
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
	 * 保存来往记录 <br>
	 * @param request
	 * 		para >	cusId:客户ID;	empId:联系人id;	praExeDate:行为执行日期;
	 *            	isIfrm:是否在详情页面中新建若为1则是在详情页面中新建;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward saveSalPras(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalPra salPra = (SalPra) form1.get("salPra");
		String cusId = request.getParameter("cusId");
		String empId=request.getParameter("empId");
		String conId=request.getParameter("conId");
		String praExeTime = request.getParameter("praExeDate");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		if (!StringFormat.isEmpty(praExeTime)) {
			salPra.setPraExeDate(GetDate.formatDate(praExeTime));
		} else {
			salPra.setPraExeDate(null);
		}
		if(!StringFormat.isEmpty(empId)){
			salPra.setPerson(new YHPerson(Long.parseLong(empId)));//执行人
		}
		else{
			salPra.setPerson(null);
		}
		
		if(!StringFormat.isEmpty(conId)){
			salPra.setCusContact(new CusContact(Long.parseLong(conId)));//联系人
		}
		else{
			salPra.setCusContact(null);
		}
		if(cusId !=null && !cusId.equals("")){
			if (!StringFormat.isEmpty(cusId)) {
				CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(cusId);
				//更新最近联系时间
				if (cusCorCus.getCorLastDate() == null) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				} else if (cusCorCus.getCorLastDate().before(
						salPra.getPraExeDate())) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				}
				salPra.setCusCorCus(cusCorCus);
			} else {
				salPra.setCusCorCus(null);
			}
		}
		if(conId !=null && !conId.equals("")){
			CusContact cusContact = customBIZ.showContact(Long.parseLong(conId));
			if (cusContact != null) {
				CusCorCus cusCorCus = cusContact.getCusCorCus();//customBIZ.getCusCorCusInfo(cusId);
				//更新最近联系时间
				if (cusCorCus.getCorLastDate() == null) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				} else if (cusCorCus.getCorLastDate().before(
						salPra.getPraExeDate())) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				}
				salPra.setCusCorCus(cusCorCus);
			} else {
				salPra.setCusCorCus(null);
			}
		}
		salPra.setPraInsDate(GetDate.getCurTime());
		salPra.setPraInpUser(limUser.getUserSeName());
		salPra.setPraIsDel("0");
		cusServBiz.save(salPra);
		// 详情下 刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建来往记录");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 跳转到修改来往记录页面 <br>
	 * @param request
	 * 		para >	id:来往记录ID; isIfrm:是否在详情页面中新建若为1则是在详情页面中;
	 * 		attr >	salPra:来玩记录实体;
	 * @return ActionForward showEditSalPras
	 */
	public ActionForward toUpdPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		String type = request.getParameter("type");
//		String cusType = "";
//		Long cusCode = null;
		
		SalPra salPra = cusServBiz.showSalPra(id);
		/*if (salPra.getCusCorCus() != null) {
			cusType = "0";
			cusCode = salPra.getCusCorCus().getCorCode();
		}
		if (!cusType.equals("") && cusCode != null) {
			List list = cusServBiz.getOppByCusCode(cusCode);
			request.setAttribute("oppList", list);
			request.setAttribute("isOpp", "1");
			if (list.size() <= 0){
				request.setAttribute("isOpp", "0");
			}
		} else {
			request.setAttribute("oppList", null);
		}*/
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("salPra", salPra);
		request.setAttribute("type", type);
		return mapping.findForward("showEditSalPras");
	}
	/**
	 * 编辑来往记录 <br>
	 * @param request
	 * 		param >	cusId:客户ID;	empId:员工id;	praExeDate:执行日期;	
	 * 				isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward changeSalPraInfo(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
//		String userCode = request.getParameter("userCode");
		String cusId = request.getParameter("cusId");
//		Long praId = Long.valueOf(request.getParameter("praId"));
		String empId=request.getParameter("empId");
		String conId=request.getParameter("conId");
//		String oppId = request.getParameter("oppId");
		String praExeDate = request.getParameter("praExeDate");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		SalPra salPra = (SalPra) form1.get("salPra");
		if(salPra!=null){
			if (!StringFormat.isEmpty(praExeDate)) {
				salPra.setPraExeDate(GetDate.formatDate(praExeDate));
			}
			SalPra oldSalPra = cusServBiz.showSalPra(salPra.getPraId());
			if(cusId != null && !cusId.equals("")){
				if (!StringFormat.isEmpty(cusId)) {
					CusCorCus cusCorCus = customBIZ.findCus(Long.valueOf(cusId));
					salPra.setCusCorCus(cusCorCus);
					if (cusCorCus.getCorLastDate() == null) {
						cusCorCus.setCorLastDate(salPra.getPraExeDate());
						customBIZ.update(cusCorCus);
					}
					else if (cusCorCus.getCorLastDate().before(salPra.getPraExeDate())) {
						cusCorCus.setCorLastDate(salPra.getPraExeDate());
						customBIZ.update(cusCorCus);
					}
				}
				else{
					salPra.setCusCorCus(null);
				}
			}else{
				CusContact cusContact = customBIZ.showContact(Long.valueOf(conId));
				CusCorCus cusCorCus = cusContact.getCusCorCus();
				salPra.setCusCorCus(cusCorCus);
				if (cusCorCus.getCorLastDate() == null) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				}
				else if (cusCorCus.getCorLastDate().before(salPra.getPraExeDate())) {
					cusCorCus.setCorLastDate(salPra.getPraExeDate());
					customBIZ.update(cusCorCus);
				}
				
			}


			/*if (oppId != null && !oppId.equals("")) {
				salPra.setSalOpp(new SalOpp(Long.valueOf(oppId)));
			} else {
				salPra.setSalOpp(null);
			}*/
			if(!StringFormat.isEmpty(empId)){
				salPra.setPerson(new YHPerson(Long.parseLong(empId)));//执行人
			}
			else {
				salPra.setPerson(null);
			}
			if(!StringFormat.isEmpty(conId)){
				salPra.setCusContact(new CusContact(Long.parseLong(conId)));//联系人
			}
			else {
				salPra.setCusContact(null);
			}
			salPra.setPraUpdUser(limUser.getUserSeName());
			salPra.setPraUpdDate(GetDate.getCurTime());
			salPra.setPraInsDate(oldSalPra.getPraInsDate());
			salPra.setPraInpUser(oldSalPra.getPraInpUser());
			salPra.setPraIsDel(oldSalPra.getPraIsDel());
			cusServBiz.update(salPra);
		}
		// 详情下编辑 刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑来往记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 显示来往记录详情 <br>
	 * @param request
	 * 		para > id:来往记录ID;
	 * @return ActionForward showDescSalPras
	 */
	public ActionForward showSalPraInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalPra salPra = cusServBiz.showSalPra(id);
		request.setAttribute("salPra", salPra);
		return mapping.findForward("showDescSalPras");
	}

	/**
	 * 新建客户服务跳转 <br>
	 * @param request
	 *         parameter > cusId:客户ID
	 * @return ActionForward 跳转到cusServs页面，在详情下新建时如果客户被删除将跳转到error页面<br>
	 *         attribute > cusCorCusInfo:客户实体（客户详情下的新建）
	 *         attribute > errorMsg:错误信息
	 */
	public ActionForward toNewServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCusInfo = customBIZ.getCusCorCusInfo(cusId);
			if(cusCorCusInfo != null){
				request.setAttribute("cusCorCusInfo", cusCorCusInfo);
			}
			else {
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的客户服务！");
				return mapping.findForward("error");
			}
		}
		return mapping.findForward("cusServs");
	}

	/**
	 * 保存客户服务信息 <br>
	 * @param request
	 *         parameter > cusCode:客户ID servsExeDate:服务执行日期 empId:员工id
	 *            		   isIfrm:是否在详情页面中新建若为1则是在详情页面中新建 userCode:负责人账号
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:新建成功提示信息
	 */
	public ActionForward saveCusServs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusServ cusServ = (CusServ) form1.get("cusServ");
		String cusId = request.getParameter("cusId");
		String empId=request.getParameter("empId");
		String exeDate = request.getParameter("servsExeDate");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		if (cusId != null && !cusId.equals("")) {
			cusServ.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		} else {
			cusServ.setCusCorCus(null);
		}
		try {
			if (exeDate != null && !exeDate.equals("")) {
				cusServ.setSerExeDate(dateFormat.parse(exeDate));
			} else {
				cusServ.setSerExeDate(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cusServ.setSerInpUser(limUser.getUserSeName());
		cusServ.setSerInsDate(date1);
		cusServ.setSerIsDel("1");
		if(empId!=null&&!empId.equals("")){
			cusServ.setSalEmp(new SalEmp(Long.parseLong(empId)));
		}
		else{
			cusServ.setSalEmp(null);
		}
		cusServBiz.save(cusServ);

		// 详情下 刷新iframe
		if (isIfrm != null
				&& isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建客户服务");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 跳转到修改销售机会页面 <br>
	 * @param request
	 *         parameter > id:销售机会ID isEdit:标识是否在详情页面点击编辑按钮若值2则是在详情页面
	 * @return ActionForward 跳转到showEditSalOpp页面<br>
	 *         attribute > salOpp:销售机会实体 oppStageList:机会阶段列表
	 */
	public ActionForward toUpdOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		
		SalOpp salOpp = cusServBiz.showSalOpp(id);
		Date oldDate = salOpp.getOppStaUpdate();
		// 计算停留天数
		Date date = new Date();
		if (oldDate != null && date.after(oldDate)){
			salOpp.setOppDayCount(String.valueOf(OperateDate.getDayNum(oldDate,
					date)));
		}
		else{
			salOpp.setOppDayCount("0");
		}
		List<TypeList> oppStageList = typeListBiz.getEnabledType("3");
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("salOpp", salOpp);
		request.setAttribute("oppStageList", oppStageList);
		return mapping.findForward("showEditSalOpp");
	}

	/**
	 * 显示销售机会详情 <br>
	 * @param request
	 *         parameter > id:销售机会ID
	 * @return ActionForward 跳转到showDescSalOpp页面<br>
	 *         attribute > salOpp:销售机会实体
	 */
	public ActionForward showSalOppInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalOpp salOpp = cusServBiz.getSalOpp(id);
		Date oldDate = salOpp.getOppStaUpdate();
		// 计算停留天数
		Date date = new Date();
		if (oldDate != null && date.after(oldDate))
			salOpp.setOppDayCount(String.valueOf(OperateDate.getDayNum(oldDate,
					date)));
		else
			salOpp.setOppDayCount("0");
		request.setAttribute("salOpp", salOpp);
		return mapping.findForward("showDescSalOpp");
	}

	/**
	 * 修改销售机会信息 <br>
	 * @param request
	 *         parameter > cusCode:客户ID salOppId:销售机会ID userCode:负责账号
	 *            		   isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 *            		   stage:机会阶段ID stageName:机会阶段名称
	 *            		   oppFindDate:机会发现日期 oppExeDate:机会关注日期
	 *            		   oppSigDate:机会预计签单日期
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:编辑成功提示信息
	 */
	public ActionForward changeSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalOpp salOpp1 = (SalOpp) form1.get("salOpp");
		String cusId = request.getParameter("cusId");
		String seNo = request.getParameter("seNo");
		Long oppId = Long.valueOf(request.getParameter("salOppId"));
		String newStage = request.getParameter("stage");
		String exeDate = request.getParameter("oppExeDate");
		String findDate = request.getParameter("oppFindDate");
		String signDate = request.getParameter("oppSigDate");
		String stageName = request.getParameter("stageName");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		SalOpp salOpp = cusServBiz.showSalOpp(oppId);
		String oldStage = "";
		if (salOpp.getOppStage() != null)
			oldStage = salOpp.getOppStage().getTypId().toString();
		String oldPossible = salOpp.getOppPossible();
		String oldStaRemark = salOpp.getOppStaRemark();
		
		if (newStage != null && !newStage.equals("")) {
			salOpp.setOppStage(new TypeList(Long.valueOf(newStage)));
		} else {
			salOpp.setOppStage(null);
		}
		if (exeDate!=null && !exeDate.equals("")) {
			salOpp.setOppExeDate(java.sql.Date.valueOf(exeDate.substring(0, 10)));
		} else {
			salOpp.setOppExeDate(null);
		}
		if (findDate!=null && !findDate.equals("")) {
			salOpp.setOppFindDate(java.sql.Date.valueOf(findDate.substring(0, 10)));
		} else {
			salOpp.setOppFindDate(null);
		}
		if (signDate!=null && !signDate.equals("")) {
			salOpp.setOppSignDate(java.sql.Date.valueOf(signDate.substring(0, 10)));
		} else {
			salOpp.setOppSignDate(null);
		}
		if(seNo != null && !seNo.equals("")){
			salOpp.setSalEmp1(new SalEmp(Long.parseLong(seNo)));
		}
		String newPossible = salOpp1.getOppPossible();
		String newStaRemark = salOpp1.getOppStaRemark();
		if (!oldStage.equals(newStage) || !oldPossible.equals(newPossible)
				|| !oldStaRemark.equals(newStaRemark)) {
			String oppStaLog = "<span class='bold'>日期:"
					+ dateFormat.format(date) + "&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "阶段:" + stageName + "(" + newPossible + "%)"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;" + "状态:"
					+ salOpp1.getOppState() + "&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "推进人:" + limUser.getUserSeName() + "<br/></span>&nbsp;"
					+ "阶段备注:" + newStaRemark + "<br/><br/>";
			String oldLog = salOpp.getOppStaLog();
			String newLog = oldLog + oppStaLog;
			salOpp.setOppStaLog(newLog);
			salOpp.setOppStaRemark(newStaRemark);
			salOpp.setOppStaUpdate(date);
		}
		if (cusId != null && !cusId.equals("")) {
			salOpp.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		}
		salOpp.setOppMoney(salOpp1.getOppMoney());
		salOpp.setOppPossible(salOpp1.getOppPossible());
		salOpp.setOppDes(salOpp1.getOppDes());
		salOpp.setOppLev(salOpp1.getOppLev());
		salOpp.setOppRemark(salOpp1.getOppRemark());
		salOpp.setOppState(salOpp1.getOppState());
		salOpp.setOppTitle(salOpp1.getOppTitle());
		salOpp.setOppUpdUser(limUser.getUserSeName());
		salOpp.setOppUpdDate(date1);
		cusServBiz.update(salOpp);
		
		// 详情下编辑 刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑销售机会");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 下拉列表加载客户下的销售机会（无分页）<br>
	 * @param request
	 *         para > cusCode:客户ID
	 */
	public void loadSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		Long cusCode = Long.valueOf(request.getParameter("cusCode"));
		List list = cusServBiz.getOppByCusCode(cusCode);
		try {
			out = response.getWriter();
			out.print("<?xml version='1.0' encoding='UTF-8'?><select>");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					SalOpp salOpp = (SalOpp) list.get(i);
					out.print("<option value='" + salOpp.getOppId() + "'>"
							+ salOpp.getOppTitle() + "</option>");
				}
			}
			out.print("</select>");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * 跳转到客户服务编辑页面 <br>
	 * @param request
	 *         parameter > serCode:客户服务ID isEdit:标识是否在详情页面点击编辑按钮若值2则是在详情页面
	 * @return ActionForward 跳转到showEditCusServs页面<br>
	 *         attribute > isEdit cusServ:客户关怀服务
	 */
	public ActionForward toUpdServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String serCode = request.getParameter("serCode");
		String isIfrm = request.getParameter("isIfrm");
		
		CusServ cusServ = cusServBiz.showCusServ(Long.valueOf(serCode));
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("cusServ", cusServ);
		return mapping.findForward("showEditCusServs");
	}

	/**
	 * 查看客户服务详情 <br>
	 * @param request
	 *         parameter > serCode:客户服务ID
	 * @return ActionForward 跳转到showDescCusServs页面<br>
	 *         attribute > cusServ:客户服务实体
	 */
	public ActionForward showCusServInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String serCode = request.getParameter("serCode");
		CusServ cusServ = cusServBiz.showCusServ(Long.valueOf(serCode));
		request.setAttribute("cusServ", cusServ);
		return mapping.findForward("showDescCusServs");
	}

	/**
	 * 修改客户服务 <br>
	 * @param request
	 *         parameter > code:客户服务ID cusCode:客户ID empId:员工id userCode:负责账号
	 *            		   servsExeDate:客户服务执行日期 isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > isIfrm msg:编辑成功提示信息
	 */
	public ActionForward changeCusServInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusServ cusServ1 = (CusServ) form1.get("cusServ");
		String serCode = request.getParameter("code");
		String cusId = request.getParameter("cusId");
		String userCode = request.getParameter("userCode");
		String empId=request.getParameter("empId");
		String exeDate = request.getParameter("servsExeDate");
		String isIfrm = request.getParameter("isIfrm");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		CusServ cusServ = cusServBiz.showCusServ(Long.valueOf(serCode));
		if (cusId != null && !cusId.equals("")) {
			cusServ.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		}
		if (exeDate!=null&&!exeDate.equals("")) {
			try {
				cusServ.setSerExeDate(dateFormat.parse(exeDate.substring(0, 10)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			cusServ.setSerExeDate(null);
		}
		if(empId!=null&&!empId.equals("")){
			cusServ.setSalEmp(new SalEmp(Long.parseLong(empId)));
		}
		else{
			cusServ.setSalEmp(null);
		}
		if(userCode!=null && !userCode.equals("")){
			cusServ.setLimUser(new LimUser(userCode));
		}
		cusServ.setSerContent(cusServ1.getSerContent());
		cusServ.setSerCosTime(cusServ1.getSerCosTime());
		cusServ.setSerCusLink(cusServ1.getSerCusLink());
		cusServ.setSerFeedback(cusServ1.getSerFeedback());
		cusServ.setSerMethod(cusServ1.getSerMethod());
		cusServ.setSerState(cusServ1.getSerState());
		cusServ.setSerTitle(cusServ1.getSerTitle());
		cusServ.setSerRemark(cusServ1.getSerRemark());
		cusServ.setSerUpdUser(limUser.getUserSeName());
		cusServ.setSerUpdDate(date1);
		cusServBiz.update(cusServ);
		
		// 详情下编辑 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑客户服务");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到新建销售机会 <br>
	 * @param request cusId:客户ID
	 * @return ActionForward 跳转到salOpps页面，在客户详情下新建时如果客户被删除则跳转到error页面<br>
	 * 		attribute > oppStageList:机会阶段 cusInfo:客户实体<br>
	 * 		attribute > errorMsg:错误信息
	 */
	public ActionForward toNewOpp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		List<TypeList> oppStageList = typeListBiz.getEnabledType("3");
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(cusId);
			if(cusCorCus != null){
				request.setAttribute("cusInfo", cusCorCus);
			}
			else {
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的销售机会！");
				return mapping.findForward("error");
			}
		}
		request.setAttribute("oppStageList", oppStageList);
		return mapping.findForward("salOpps");
	}
	
	/**
	 * 跳转到新建来往记录 <br>
	 * @param request
	 * 		para >	cusCode:客户ID;	isDesc:是否在详情页面内新建（是否保存后刷新iframe标识）;
	 * 		attr >	cusInfo:客户实体;
	 * @return ActionForward salPras
	 * 		attribute > isDesc:是否在详情页面内新建 cusInfo:客户实体 oppId:机会ID<br>
	 * 		attribute > errorMsg:错误信息
	 */
	public ActionForward toNewPra(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String cusCode = request.getParameter("cusCode");
//		String oppId = request.getParameter("oppId");
		String conId = request.getParameter("conId");
		String isDesc = request.getParameter("isDesc");

		if (!StringFormat.isEmpty(cusCode)) {
			CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(cusCode);
			if(cusCorCus != null){
				request.setAttribute("cusInfo", cusCorCus);
			}
			else {
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的来往记录！");
				return mapping.findForward("error");
			}
		}
//		if(oppId != null && !oppId.equals("")){
//			request.setAttribute("oppId", oppId);
//		}
		if(!StringFormat.isEmpty(conId)){
			CusContact cusContact = customBIZ.showContact(Long.parseLong(conId));
			if(cusContact !=null){
				request.setAttribute("cusContact", cusContact);
			}else{
				request.setAttribute("errorMsg","该联系人已被删除，无法新建对应此联系人的来往记录！");
				return mapping.findForward("error");
			}
			
		}
		request.setAttribute("date", GetDate.parseStrDate(GetDate.getCurDate()));
		request.setAttribute("isDesc", isDesc);
		return mapping.findForward("salPras");
	}
	
	/**
	 * 跳转至编辑报价明细 <br>
	 * @param request
	 *         parameter > quoId:报价记录ID
	 * @return ActionForward 跳转到newRup页面<br>
	 *         attribute > quote:报价记录实体
	 */
	public ActionForward goRup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("quoId"));
		Quote quote = cusServBiz.showQuote(id);
		request.setAttribute("quote", quote);
		return mapping.findForward("newRup");
	}

	/**
	 * 编辑报价明细 <br>
	 * @param request
	 *         parameter > wprId:报价明细中产品ID quoId:报价记录ID
	 * @return ActionForward 跳转到empty页面<br>
	 *         attribute > noMsg:标识不弹出信息直接返回上一页
	 */
	public ActionForward newRup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String agr[] = request.getParameterValues("wprId");
		String quoId = request.getParameter("quoId");
		cusServBiz.delByQuo(Long.parseLong(quoId));// 批量删除

		for (int i = 1; i < agr.length; i++) {
			RQuoPro rin = new RQuoPro();
			String price = "price" + agr[i];
			String num = "num" + agr[i];
			String allPrice = "allPrice" + agr[i];
			String remark = "remark" + agr[i];

			String rppPrice = request.getParameter(price);
			String num1 = request.getParameter(num);
			String tatalPrice = request.getParameter(allPrice);
			WmsProduct wmsProduct = new WmsProduct();
			wmsProduct.setWprId(agr[i]);
			Quote quote = new Quote(Long.parseLong(quoId));

			if (rppPrice != null && !rppPrice.equals("")) {
				Double rpp = Double.parseDouble(rppPrice);
				rin.setRupPrice(rpp);
			}
			if (num1 != null && !num1.equals("")) {
				Double rppNum = Double.parseDouble(num1);
				rin.setRupNum(rppNum);
			} else {
				rin.setRupNum(0.0);
			}
			if (tatalPrice != null && !tatalPrice.equals("")) {
				Double all = Double.parseDouble(tatalPrice);
				rin.setRupAllPrice(all);
			} else {
				rin.setRupAllPrice(0.0);
			}
			rin.setQuote(quote);
			rin.setWmsProduct(wmsProduct);
			rin.setRupRemark(request.getParameter(remark));
			cusServBiz.saveRup(rin);
		}
		request.setAttribute("noMsg", "不弹出信息！");
		return mapping.findForward("empty");
	}

	/**
	 * 机会下报价记录 <br>
	 * @param request
	 *         parameter > oppTit:机会主题 oppId:机会ID p:当前页码
	 * @return ActionForward 跳转到showOppQuo页面<br>
	 *         attribute > quoteList:符合条件的报价记录列表 page:分页信息 oppId:机会ID oppTit:机会主题
	 */
	public ActionForward getOppQuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String oppTit = TransStr.transStr(request.getParameter("oppTit"));
		String oppId = request.getParameter("oppId");

		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		int count = getAccDataDao.getAccDataCount(Long.parseLong(oppId),
				"Quote", "salOpp.oppId", "quoIsDel", "1");
		Page page = new Page(count, 10);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = getAccDataDao.getAccData(Long.parseLong(oppId), "Quote",
				"salOpp.oppId", "quoIsDel", "1", "quoId", page
						.getCurrentPageNo(), page.getPageSize());
		request.setAttribute("quoteList", list);
		request.setAttribute("page", page);
		request.setAttribute("oppId", oppId);
		request.setAttribute("oppTit", oppTit);
		return mapping.findForward("showOppQuo");
	}

	/**
	 * 跳转到报价新建页面 <br>
	 * @param request
	 *         parameter > pInfo:标识新建页面显示对应机会还是显示对应项目若值为py则显示对应机会若值为pn则显示对应项目
	 *            		   oppId:机会ID或项目ID oppTitle:机会主题或项目主题
	 * @return ActionForward 跳转到createQuote页面<br>
	 *         attribute > 若pInfo值为py oppId:机会ID oppTitle:机会主题 若pInfo值不为py
	 *         			   proId:项目ID proName:项目主题
	 */
	public ActionForward toNewQuote(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String pInfo = request.getParameter("pInfo");
		String oppId = request.getParameter("oppId");
		String oppTitle = TransStr.transStr(request.getParameter("oppTitle"));
		if (pInfo != null && pInfo.equals("py")) {
			request.setAttribute("oppId", oppId);
			request.setAttribute("oppTitle", oppTitle);
		} else {
			request.setAttribute("proId", oppId);
			request.setAttribute("proName", oppTitle);
		}
		request.setAttribute("pInfo", pInfo);
		return mapping.findForward("createQuote");

	}

	/**
	 * 保存报价信息 <br>
	 * @param request
	 *         parameter > quoDate:报价日期 oppId:机会ID proId:项目ID
	 *            		   isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:新建成功信息提示 isIfrm
	 */
	public ActionForward saveQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		Quote quote = (Quote) form1.get("quote");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		String quoDate = request.getParameter("quoDate");
		String empId = request.getParameter("empId");
		String oppId = request.getParameter("oppId");
		String proId = request.getParameter("proId");

		if (quoDate != null && !quoDate.equals("")) {
			try {
				Date qDate = dateFormat.parse(quoDate);
				quote.setQuoDate(qDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			quote.setQuoDate(null);
		}

		if (empId != null && !empId.equals("")) {
			quote.setSalEmp(new SalEmp(Long.parseLong(empId)));
		} else {
			quote.setSalEmp(null);
		}

		if (oppId != null && !oppId.equals("")) {
			quote.setSalOpp(new SalOpp(Long.parseLong(oppId)));
		} else {
			quote.setSalOpp(null);
		}

		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		quote.setQuoInpUser(limUser.getUserSeName());
		quote.setQuoInsDate(date1);
		quote.setQuoIsDel("1");
		cusServBiz.saveQuote(quote);

		// 详情下刷新iframe
		if (request.getParameter("isIfrm") != null
				&& request.getParameter("isIfrm").equals("1")) {
			request.setAttribute("isIfrm", "1");
		}

		request.setAttribute("msg", "新建报价记录");
		return mapping.findForward("popDivSuc");

	}

	/**
	 * 显示报价详情 <br>
	 * @param request
	 *         parameter > id:报价记录ID
	 * @return ActionForward 跳转到showDescQuote页面<br>
	 *         attribute > quoteInfo:报价实体
	 */
	public ActionForward showQuoteInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		Quote quote = cusServBiz.showQuote(id);
		request.setAttribute("quoteInfo", quote);
		return mapping.findForward("showDescQuote");
	}
	
	/**
	 * 跳转到编辑报价页面 <br>
	 * @param request
	 * 		para > id:报价Id
	 * @return ActionForward 跳转到showEditQuote页面<br>
	 * 		attribute > quoteInfo:报价实体
	 */
	public ActionForward toUpdQuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		
		Quote quote = cusServBiz.showQuote(id);
		
		request.setAttribute("quoteInfo", quote);
		return mapping.findForward("showEditQuote");
	}

	/**
	 * 修改报价信息 <br>
	 * @param request
	 *         parameter > quoId:报价记录ID isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:编辑成功提示信息 isIfrm
	 */
	public ActionForward changeQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		DynaActionForm form1 = (DynaActionForm) form;
		Quote quote1 = (Quote) form1.get("quote");
		Long quoId = Long.valueOf(request.getParameter("quoId"));
		Quote quote = cusServBiz.showQuote(quoId);
		String empId = request.getParameter("empId");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");

		if (empId != null && !empId.equals("")) {
			quote.setSalEmp(new SalEmp(Long.parseLong(empId)));
		} else {
			quote.setSalEmp(null);
		}
		quote.setQuoDate(quote1.getQuoDate());
		quote.setQuoDesc(quote1.getQuoDesc());
		quote.setQuoPrice(quote1.getQuoPrice());
		quote.setQuoRemark(quote1.getQuoRemark());
		quote.setQuoTitle(quote1.getQuoTitle());
		quote.setQuoUpdUser(limUser.getUserSeName());
		quote.setQuoUpdDate(date1);
		cusServBiz.updateQuo(quote);

		// 详情下刷新iframe
		if (request.getParameter("isIfrm") != null
				&& request.getParameter("isIfrm").equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑报价记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除机会报价 <br>
	 * @param request
	 *         parameter > quoId:报价记录ID isIfrm:表示在详情页面中删除
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功提示信息 isIfrm
	 */
	public ActionForward delQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long quoId = Long.valueOf(request.getParameter("quoId"));
		Quote quote = cusServBiz.showQuote(quoId);
		quote.setQuoIsDel("0");
		cusServBiz.updateQuo(quote);

		// 详情下刷新iframe
		request.setAttribute("isIfrm", "1");
		request.setAttribute("msg", "删除报价记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复机会报价 <br>
	 * @param request
	 *         parameter > id:机会报价ID
	 * @return ActionForward 若不符合恢复条件跳转到error页面 符合恢复条件跳转到popDivSuc页面<br>
	 *         attribute > 若不符合恢复条件 errorMsg:不符合恢复条件提示<br>
	 *         attribute > 若符合恢复条件 msg:成功恢复提示信息<br>
	 */
	public ActionForward recQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long quoId = Long.valueOf(request.getParameter("id"));
		Quote quote = cusServBiz.showQuote(quoId);
		if (quote.getSalOpp().getOppIsDel().equals("0")) {
			String msg = "该报价记录对应的销售机会(主题为'" + quote.getSalOpp().getOppTitle()
					+ "')已被删除，请先恢复对应的销售机会";
			request.setAttribute("errorMsg", msg);
			return mapping.findForward("error");
		} else {
			quote.setQuoIsDel("1");
			cusServBiz.updateQuo(quote);
			request.setAttribute("msg", "恢复报价记录");
			return mapping.findForward("popDivSuc");
		}

	}

	/**
	 * 删除机会报价不能再恢复 <br>
	 * @param request
	 *         parameter > id:机会报价ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delQuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long quoId = Long.valueOf(request.getParameter("id"));
		Quote quote = cusServBiz.showQuote(quoId);
		Iterator it = quote.getAttachments().iterator();
		// 删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		cusServBiz.delQuote(quote);
		request.setAttribute("msg", "删除报价记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除销售机会 <br>
	 * @param request
	 *         parameter > id:销售机会ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		SalOpp salOpp = cusServBiz.showSalOpp(id);
		boolean isDel = true;
		StringBuffer eMsg = new StringBuffer();
		if(salOpp.getSalPras()!=null){
			int count = 0;
			int countDel = 0;
			Set<SalPra> list = salOpp.getSalPras();
			Iterator<SalPra> it = list.iterator();
			while(it.hasNext()){
				SalPra sp = it.next();
				if(sp.getPraIsDel().equals("1")){
					count++;
				}
				else{
					countDel++;
				}
			}
			if(count > 0 || countDel > 0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg,count,countDel,"来往记录");
			}
		}
		if(isDel){
			salOpp.setOppIsDel("0");
			cusServBiz.update(salOpp);
			// 详情下编辑 刷新iframe
			if ( isIfrm!= null && isIfrm.equals("1")) {
				request.setAttribute("isIfrm", "1");
			}
			
			request.setAttribute("msg", "删除销售机会");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", "请先删除该销售机会下的关联数据："+eMsg.toString());
			return mapping.findForward("error");
		}
	}

	/**
	 * 恢复销售机会 <br>
	 * @param request
	 *         parameter > id:机会ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功恢复提示信息
	 */
	public ActionForward recSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// try{
		Long id = Long.valueOf(request.getParameter("id"));
		SalOpp salOpp = cusServBiz.getSalOpp(id);
		salOpp.setOppIsDel("1");
		cusServBiz.update(salOpp);
		// }catch(Exception e){
		// request.setAttribute("msg", "恢复销售机会");
		// return mapping.findForward("popDivSuc");
		// System.out.println("恢复失败!");
		// }
		request.setAttribute("msg", "恢复销售机会");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除销售机会不能再恢复 <br>
	 * @param request
	 *         parameter > id:机会ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalOpp salOpp = cusServBiz.getSalOpp(id);
		cusServBiz.delOpp(salOpp);
		request.setAttribute("msg", "删除销售机会");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除来往记录 <br>
	 * @param request
	 *         parameter > id:来往记录ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delSalPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		SalPra salPra = cusServBiz.showSalPra(id);
		salPra.setPraIsDel("1");
		cusServBiz.update(salPra);
		// 详情下新建 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除来往记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复来往记录 <br>
	 * @param request
	 *         parameter > id:来往记录ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功恢复提示信息
	 */
	public ActionForward recSalPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalPra salPra = cusServBiz.showSalPra(id);
		salPra.setPraIsDel("0");
		cusServBiz.update(salPra);
		request.setAttribute("msg", "恢复来往记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除来往记录不能再恢复 <br>
	 * @param request
	 *         parameter > id:来往记录ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalPra salPra = cusServBiz.showSalPra(id);
		Iterator it = salPra.getAttachments().iterator();
		// 删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		cusServBiz.delPra(salPra);
		request.setAttribute("msg", "删除来往记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 修改来往记录执行日期 <br>
	 * @param request
	 *         parameter > id:来往记录ID praExeDate:行为执行日期
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:修改成功信息提示
	 */
	public ActionForward changeExeDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalPra salPra = cusServBiz.showSalPra(id);
		if (request.getParameter("praExeDate") != null
				&& !request.getParameter("praExeDate").equals("")) {
			java.sql.Date praExeDate = java.sql.Date.valueOf((String) request
					.getParameter("praExeDate").substring(0, 10));
			salPra.setPraExeDate(praExeDate);
		}
		cusServBiz.update(salPra);
		request.setAttribute("msg", "修改执行时间");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除客户服务 <br>
	 * @param request
	 *         parameter > serCode:客户服务ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String serCode = request.getParameter("serCode");
		String isIfrm = request.getParameter("isIfrm");
		CusServ cusServ = cusServBiz.showCusServ(Long.valueOf(serCode));
		cusServ.setSerIsDel("0");
		cusServBiz.update(cusServ);
		// 详情下新建 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除客户服务");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复客户服务 <br>
	 * @param request
	 *         parameter > id:客户关怀ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功恢复提示信息
	 */
	public ActionForward recServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		CusServ cusServ = cusServBiz.showCusServ(id);
		cusServ.setSerIsDel("1");
		cusServBiz.update(cusServ);
		request.setAttribute("msg", "恢复客户关怀");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除客户服务不能再恢复 <br>
	 * @param request
	 *         parameter > id:客户服务ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:成功删除提示信息
	 */
	public ActionForward delServer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		CusServ cusServ = cusServBiz.showCusServ(id);
		Iterator it = cusServ.getAttachments().iterator();
		// 删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}

		cusServBiz.delServ(cusServ);
		request.setAttribute("msg", "删除客户服务");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 批量删除销售机会 <br>
	 * @param request
	 *         parameter > code:所有待删除的机会ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:批量删除成功信息提示
	 */
	public ActionForward batchDelSalOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String[] priKeyValues = code.split(",");
		batchOperate.batchUpdDelMark("SalOpp", "oppId", priKeyValues,
				"oppIsDel",0);
		request.setAttribute("msg", "批量删除销售机会");
		return mapping.findForward("popDivSuc");

	}

	/**
	 * 批量删除来往记录 <br>
	 * @param request
	 *         parameter > code:所有待删除的行为ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:批量删除成功信息提示
	 */
	public ActionForward batchDelSalPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String[] priKeyValues = code.split(",");
		// for(int i=0;i<priKeyValues.length;i++)
		// {
		// if(priKeyValues[i]!=null)
		// {
		// SalPra salPra = cusServBiz.showSalPra(Long.valueOf(priKeyValues[i]));
		// Iterator it=salPra.getAttachments().iterator();
		// //删除关联附件
		// while(it.hasNext()){
		// Attachment att=(Attachment)it.next();
		// new FileAction().delFile(att.getAttPath(), request);
		// }
		// }
		//			
		// }
		batchOperate.batchUpdDelMark("SalPra", "praId", priKeyValues,
				"praIsDel",0);
		request.setAttribute("msg", "批量删除来往记录");
		return mapping.findForward("popDivSuc");

	}

	/**
	 * 批量删除客户服务 <br>
	 * @param request
	 *         parameter > code:所有待删除的客户服务ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:批量删除成功信息提示
	 */
	public ActionForward batchDelServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String[] priKeyValues = code.split(",");
		// for(int i=0;i<priKeyValues.length;i++)
		// {
		// if(priKeyValues[i]!=null)
		// {
		// CusServ cusServ =
		// cusServBiz.showCusServ(Long.valueOf(priKeyValues[i]));
		// Iterator it=cusServ.getAttachments().iterator();
		// //删除关联附件
		// while(it.hasNext()){
		// Attachment att=(Attachment)it.next();
		// new FileAction().delFile(att.getAttPath(), request);
		// }
		// }
		//			
		// }
		batchOperate.batchUpdDelMark("CusServ", "serCode", priKeyValues,
				"serIsDel",0);
		request.setAttribute("msg", "批量删除客户服务");
		return mapping.findForward("popDivSuc");

	}

	/**
	 * 跳转到待删除的销售机会 <br>
	 * @return ActionForward recOpp <br>
	 */
	public ActionForward toListDelOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("recOpp");
	}
	/**
	 * 获得待删除的销售机会 <br>
	 * @param request
	 *         parameter > p:当前页码; pageSize:每页显示记录数;
	 */
	public void findDelOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(cusServBiz.findDelOppCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.findDelOpp(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salEmp1");
		awareCollect.add("oppStage");
		
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
	 * 获得待删除的报价记录 <br>
	 * @param request
	 *         parameter > p:当前页码
	 * @return ActionForward 跳转到recQuote页面<br>
	 *         attribute > quoteList:符合条件的报价记录列表 page:分页信息
	 */
	public ActionForward findDelQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(cusServBiz.findDelQuoteCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.findDelQuote(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("quoteList", list);
		request.setAttribute("page", page);
		return mapping.findForward("recQuote");

	}

	/**
	 * 跳转到待删除的来往记录<br>
	 * @return ActionForward recPra<br>
	 */
	public ActionForward toListDelPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("recPra");
	}
	/**
	 * 获得待删除的来往记录 <br>
	 * @param request
	 *         parameter > p:当前页码; pageSize:每页显示记录数;
	 */
	public void findDelPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(cusServBiz.findDelPraCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.findDelPra(page.getCurrentPageNo(), page
				.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("person");
		
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
	 * 跳转到待删除的客户服务 <br>
	 * @return ActionForward recServ <br>
	 */
	public ActionForward toListDelServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("recServ");
	}
	/**
	 * 获得待删除的客户服务 <br>
	 * @param request
	 *         parameter > p:当前页码; pageSize:每页显示记录数;
	 */
	public void  findDelServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(cusServBiz.findDelServCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.findDelServ(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
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
	 * 跳转到对应的销售机会<br>
	 * @param request
	 * 		parameter > cusId :客户Id
	 * @return ActionForward 跳转到showCusOpp页面<br>
	 * 		attribute > cusId :客户Id
	 */
	public ActionForward toListCusOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("showCusOpp");
	}
	/**
	 * 获得某客户下的所有销售机会 <br>
	 * @param request
	 *         parameter > cusId:客户ID p:当前页码 pageSize:每页显示条数  orderCol：排序字段  isDe:是否逆序
	 */
	public void getCusOpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		Date date = new Date();
		String [] args = {"1",cusId};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"oppState", "oppTil", "oppLev", "oppCus","oppExeDate","oppDate", "oppStage", "oppPos",null,"seName" };
		if(orderCol!= null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = new Page(cusServBiz.listSalOppsCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.listSalOpps(args, orderItem, isDe, page
				.getCurrentPageNo(), page.getPageSize());
		//计算阶段停留时间
		Iterator<SalOpp> ite = list.iterator();
		while (ite.hasNext()) {
			SalOpp opp = ite.next();
			Date oldDate = opp.getOppStaUpdate();
			if (oldDate != null && date.after(oldDate)) {
				opp.setOppDayCount(String.valueOf(OperateDate.getDayNum(
						oldDate, date)));
			} else {
				opp.setOppDayCount("0");
			}
		}
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("oppStage");
		awareCollect.add("salEmp1");

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
	 * 跳转到客户对应的来往记录<br>
	 * @param request
	 * 			pararmeter > cusId:客户Id
	 * @return ActionForward 跳转到listCusPra页面<br>
	 * 		attribute > cusId:客户Id
	 */
	public ActionForward toListCusPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusPra");
	}
	
	
	/**
	 * 获得某客户下的所有来往记录 <br>
	 * @param request
	 *         parameter > cusId:客户ID p:当前页码  orderCol:排序字段  pageSize:每页显示条数  isDe:是否逆序
	 */
	public void getCusPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem ="";
		String [] args = {"0",cusId};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"praType","praRemark","praExeDate","conName","seName","cost"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(cusServBiz.listSalPrasCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.listSalPras(args,orderItem,isDe, page
				.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("person");
		awareCollect.add("cusContact");
		
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
	 * 跳转到联系人对应的来往记录<br>
	 * @param request
	 * 			pararmeter > conId:联系人Id
	 * @return ActionForward 跳转到listCusPra页面<br>
	 * 		attribute > conId:联系人Id
	 */
	public ActionForward toListContactPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String conId = request.getParameter("conId");
		request.setAttribute("conId", conId);
		return mapping.findForward("listContactPra");
	}
	
	/**
	 * 获得某联系人下的所有来往记录 <br>
	 * @param request
	 *         parameter > cusId:客户ID p:当前页码  orderCol:排序字段  pageSize:每页显示条数  isDe:是否逆序
	 */
	public void getContactPra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem ="";
		String [] args = {"0",conId,conId};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"praType","praRemark","praExeDate","customer","seName","cost"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(cusServBiz.listSalPrasCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.listSalPras(args,orderItem,isDe, page
				.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salEmp");
		awareCollect.add("cusContact");
		
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
	 * 跳转到某客户下的所有客户服务<br>
	 * @param request
	 * 			parameter > cusId:客户ID
	 * @return ActionForward showCusServ <br>
	 * 		attribute > cusId:客户ID
	 */
	public ActionForward toListCusServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("showCusServ");
	}
	/**
	 * 获得某客户下的所有客户服务 <br>
	 * @param request
	 *         parameter > cusId:客户ID p:当前页码  pageSize:每页显示的条数  orderCol:排序字段 isDe:是否逆序
	 */
	public void getCusServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String orderCol = request.getParameter("orderCol");
		String p = request.getParameter("p");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String  orderItem = "";
		String [] args = {"1",cusId};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"serState","serTitle","customer","serMethod","serContent","seName","exeDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(cusServBiz.getServCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = cusServBiz.getServ(args, orderItem, isDe, page
				.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
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
	 * 跳转到执行客服页面<br>
	 * @param request
	 * 				parameter > serId:客服Id
	 * @return ActionForward 跳转到exeServ页面<br>
	 * 		attribute > serId:客服Id
	 */
	public ActionForward toExeServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String serId = request.getParameter("serId");
		String d = GetDate.parseStrDate(GetDate.getCurDate());
		request.setAttribute("date", d);
		request.setAttribute("serId", serId);
		return mapping.findForward("exeServ");
	}
	
	/**
	 * 执行客服处理<br>
	 * @param request
	 * 			parameter > serId:客服Id  serContent:客服内容 empId:处理人Id servsExeDate:处理日期
	 * @return ActionForward 跳转到成功页面<br>
	 * 		attribute > msg:显示的成功信息
	 */
	public ActionForward exeServ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String serId = request.getParameter("serId");
		String serRemark = request.getParameter("serRemark");
		String empId = request.getParameter("empId");
		String serMethod = request.getParameter("serMethod");
		String servsExeDate = request.getParameter("servsExeDate");
		if(serId!=null && !serId.equals("")){
			CusServ cs = cusServBiz.showCusServ(Long.parseLong(serId));
			cs.setSalEmp(new SalEmp(Long.parseLong(empId)));
			cs.setSerRemark(serRemark);
			cs.setSerMethod(serMethod);
			cs.setSerExeDate(GetDate.formatDate(servsExeDate));
			cs.setSerState("已处理");
			cusServBiz.update(cs);
		}
		request.setAttribute("msg", "处理客户服务");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 注入cusServBiz <br>
	 */
	public void setCusServBiz(CusServBIZ cusServBiz) {
		this.cusServBiz = cusServBiz;
	}
	/**
	 * 注入customBIZ <br>
	 */
	public void setCustomBIZ(CustomBIZ customBIZ) {
		this.customBIZ = customBIZ;
	}
	/**
	 * 注入batchOperate <br>
	 */
	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}
	/**
	 * 注入typeListBiz <br>
	 */
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
	/**
	 * 注入getAccDataDao <br>
	 */
	public void setGetAccDataDao(GetAccDataDAO getAccDataDao) {
		this.getAccDataDao = getAccDataDao;
	}
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
}