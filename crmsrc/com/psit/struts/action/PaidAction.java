
package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.PaidBIZ;
import com.psit.struts.BIZ.SalTaskBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SalPaidPlan;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.DAO.BatchOperateDAO;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 回款计划回款记录操作 <br>
 */
public class PaidAction extends BaseDispatchAction {
	SalTaskBIZ salTaskBiz = null;
	PaidBIZ paidBiz = null;
	OrderBIZ orderBiz = null;
	UserBIZ userBiz = null;
	ModifyTypeDAO modType = null;
	CustomBIZ customBiz = null;
	TypeListBIZ typeListBiz = null;
	BatchOperateDAO batchOperate = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 判断用户是否有回款计划回款记录操作的权限 <br>
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
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { 
				{"toListPaidPlan", "b036"},
				{ "toNewPaidPlan", "s007" },{ "savePaidPlan", "s007" },//新建回款计划
				{ "toUpdPaidPlan", "s009" },{ "modPaidPlan", "s009" },{ "completePaid", "s009" },//修改回款计划
				{ "deletePaidPlan", "s008" },//删除回款计划
				{"toListPaidPast", "b037"},
				{ "toNewPaidPast", "s010" },{ "savePaidPast", "s010" },//新建回款记录
				{ "deletePaidPast", "s011" },//删除回款记录
				{ "toUpdPaidPast", "s012" },{ "modPaidPast", "s012" },//修改回款记录
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

/*--- 回款计划 ---*/
	/**
	 * 跳转到回款计划列表 <br>
	 * @param request 
	 * 		para > 	range:查询范围,0自己,1全部;
	 * @return ActionForward 跳转到listPaidPlan<br>
	 * 		attr > range:查询范围（0自己，1全部）;
	 *					
	 */
	public ActionForward toListPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		request.setAttribute("range", range);
		return mapping.findForward("listPaidPlan");
	}
	/**
	 * 回款计划列表 <br>
	 * @param request 
	 * 		para > 	range:查询范围,0自己,1全部; 
 * 					p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
					paidPlanSForm:查询条件
	 *					
	 */
	public void listPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm dForm = (DynaActionForm) form;
		//PaidPlanSForm paidPlanSForm = (PaidPlanSForm) dForm.get("paidPlanSForm");
		String planContent = request.getParameter("planContent");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String range = request.getParameter("range");
		String filter = request.getParameter("filter");
		String seId = (String)request.getSession().getAttribute("userId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "spdIsP", "spdCus", "spdOrd", "spdMon", "spdDate", "spdUser" };
		
		String[] args = { "0",range,seId,planContent,startDate, 
				endDate, filter };
		
		if (p == null || p.equals("")) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listPaidPlansCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPaidPlan> list = paidBiz.listPaidPlans(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salOrdCon");
		awareCollect.add("spdResp");

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
	 * 跳转到关联的回款计划<br>
	 * @param request
	 * 			parameter > ordId:订单Id
	 * @param response
	 * @return ActionForward listOrdSpp <br>
	 * 		attribute > ordId:订单Id
	 */
	public ActionForward toListOrdSpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String ordId = request.getParameter("ordId");
		
		request.setAttribute("ordId", ordId);
		return mapping.findForward("listOrdSpp");
	}
	/**
	 * 订单下回款计划 <br>
	 * @param request
	 *      para > 	ordId:订单ID;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 * @param response
	 * @return ActionForward 跳转到listOrdSpp页面<br>
	 * 		attr > 	sppList:符合条件的回款计划列表 ordId:订单ID 
	 * 				page:页码对象，用于分页;  orderCol:排序列号;  isDe:是否逆序; 
	 */
	public void listOrdSpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ordId = request.getParameter("ordId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "spdIsP", "spdMon", "spdDate", "spdUser" };
		String[] args = { "0",ordId };
		
		if (p == null || p.equals("")) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listPaidPlansCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPaidPlan> list = paidBiz.listPaidPlans(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("spdResp");

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
	 * 销售业绩及最近30天回款计划(工作台) <br>
	 * @param request
	 * @param response
	 * @return ActionForward lastPaid页<br>
	 * 		attr > planList1:30天内待执行的计划列表 planList2:过期未执行的回款计划列表
	 */
	public ActionForward getToDoPaid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		SalEmp salEmp = (SalEmp) request.getSession().getAttribute("CUR_EMP");//用户
		//最近30天计划列表
		List<SalPaidPlan> list1 = paidBiz.getPlanByTime(salEmp.getSeNo().toString(), new Date(),
				OperateDate.getDate(new Date(), 30));
		//过期计划列表
		List<SalPaidPlan> list2 = paidBiz.getPastPlan(salEmp.getSeNo().toString(), new Date());
		request.setAttribute("planList1", list1);
		request.setAttribute("planList2", list2);
		return mapping.findForward("lastPaid");//工作台
	}

	/**
	 * 跳转到创建回款计划页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到formPaidPlan页面
	 */
	public ActionForward toNewPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("formPaidPlan");
	}
	/**
	 * 保存回款计划 <br>
	 * @param request
	 *      para > 	seId:负责账号 cusId:对应客户ID  ordId:订单ID 
 *      			isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
 *      			planDate:计划回款日期
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > 	msg:保存成功信息提示 isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 */
	public ActionForward savePaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String curSeName = (String)request.getSession().getAttribute("userName");
		String seId = request.getParameter("seId");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String planDate = request.getParameter("planDate");
		DynaActionForm form1 = (DynaActionForm) form;
		SalPaidPlan paid = (SalPaidPlan) form1.get("salPaidPlan");
		
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
			paid.setCusCorCus(cus);
		} else {
			paid.setCusCorCus(null);
		}
		if (ordId != null && !ordId.equals("")) {
			SalOrdCon order = new SalOrdCon(ordId);
			paid.setSalOrdCon(order);
		} else {
			paid.setSalOrdCon(null);
		}
		//插入日期
		if (planDate!=null && !planDate.equals("")) {
			try {
				paid.setSpdPrmDate(dateFormat.parse(planDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			paid.setSpdPrmDate(null);
		}
		paid.setSpdUserCode(curSeName);
		paid.setSpdCreDate(new Date(new Date().getTime()));
		paid.setSpdAltUser(null);
		paid.setSpdAltDate(null);
		paid.setSpdIsdel("0");
		paid.setSpdResp(new SalEmp(Long.parseLong(seId)));
		paidBiz.savePaidPlan(paid);
		
		//详情下刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建回款计划");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到修改回款计划 <br>
	 * @param request
	 *      para > paidId:回款计划ID
	 * @param response
	 * @return ActionForward 跳转到formPaidPlan页面<br>
	 * 		attr > paid:回款计划实体，如果实体为null返回字符串"null"
	 */
	public ActionForward toUpdPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidId = request.getParameter("paidId");
		
		SalPaidPlan paid = paidBiz.getPaidPlan(paidId);
		if(paid == null){
			request.setAttribute("paid", "null");
		}
		else{
			request.setAttribute("paid", paid);
		}
		return mapping.findForward("formPaidPlan");
	}
	/**
	 * 修改回款计划 <br>
	 * @param request
	 *      para > paidId:回款计划ID planDate:计划回款日期 isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 *      			ordId:订单id cusId:客户ID seId:负责人
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑 
	 */
	public ActionForward modPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalPaidPlan paid = (SalPaidPlan) form1.get("salPaidPlan");
		String id = request.getParameter("paidId");
		String seId = request.getParameter("seId");
		String planDate = request.getParameter("planDate");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String curSeName = (String)request.getSession().getAttribute("userName");
		
		SalPaidPlan paidO = paidBiz.getPaidPlan(id);
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
			paid.setCusCorCus(cus);
		} else {
			paid.setCusCorCus(null);
		}
		//插入日期
		if (planDate != null && !planDate.equals("")) {
			try {
				paid.setSpdPrmDate(dateFormat.parse(planDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			paid.setSpdPrmDate(null);
		}
		if (!ordId.equals("")) {
			SalOrdCon order = new SalOrdCon(ordId);
			paid.setSalOrdCon(order);
		} else {
			paid.setSalOrdCon(null);
		}

		paid.setSpdAltUser(curSeName);
		paid.setSpdAltDate(new Date(new Date().getTime()));
		paid.setSpdId(paidO.getSpdId());
		paid.setSpdUserCode(paidO.getSpdUserCode());
		paid.setSpdCreDate(paidO.getSpdCreDate());
		paid.setSpdIsdel(paidO.getSpdIsdel());
		paid.setSpdResp(new SalEmp(Long.parseLong(seId)));
		paidBiz.updatePaidPlan(paid);
		//详情下刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "修改回款计划");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 回款计划详情(未使用) <br>
	 * @param request
	 *      para > paidId:回款计划ID
	 * @param response
	 * @return ActionForward 跳转到showDescPlan页面<br>
	 * 		attr > paidPlan:回款计划实体
	 */
	public ActionForward getPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("planId");
		SalPaidPlan paidPlan = paidBiz.getPaidPlan(id);
		request.setAttribute("paidPlan", paidPlan);
		return mapping.findForward("showDescPlan");
	}
	/**
	 * 修改回款计划状态（Ajax） <br>
	 * @param request parameter > paidId:回款计划ID
	 * @param response
	 * @return ActionForward null<br>
	 * 		out输出"1"
	 */
	public ActionForward completePaid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidId = request.getParameter("paidId");
		PrintWriter out = null;
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			paidBiz.setPaidPlanCompleted(paidId);//更新实体
			out.print("1");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 删除回款计划<br>
	 * @param request 
	 *      parameter > paidCode:回款计划ID isIfrm:是否在详情页面中删除若为1则是在详情页面中删除 
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功删除的信息提示 isIfrm:是否在详情页面中删除若为1则是在详情页面中删除 
	 */
	public ActionForward deletePaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidCode = request.getParameter("paidCode");
		String isIfrm = request.getParameter("isIfrm");
		paidBiz.invPaidPlan(Long.parseLong(paidCode));
		//详情下刷新iframe
		if ( isIfrm != null
				&& isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除回款计划");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复回款计划 <br>
	 * @param request
	 *      parameter > id:回款计划ID
	 * @param response
	 * @return ActionForward 符合恢复条件的跳转到popDivSuc页面<br>
	 *      attribute > msg:成功恢复信息提示<br>
	 */
	public ActionForward recPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidCode = request.getParameter("id");
		SalPaidPlan paidPlan = paidBiz.getPaidPlan(Long.parseLong(paidCode));
		paidPlan.setSpdIsdel("0");
		paidBiz.updatePaidPlan(paidPlan);
		request.setAttribute("msg", "恢复回款计划");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 物理删除回款计划 <br>
	 * @param request
	 *      parameter > id:回款计划ID 
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功删除的信息提示
	 */
	public ActionForward delPaidPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidCode = request.getParameter("id");
		SalPaidPlan paidPlan = paidBiz.getPaidPlan(Long.parseLong(paidCode));
		paidBiz.deletePaidPlan(paidPlan);
		request.setAttribute("msg", "删除回款计划");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到待删除的回款计划<br>
	 * @param request
	 * @param response
	 * @return ActionForward recPaidPlan<br>
	 */
	public ActionForward toListDelPaidPlan(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		
		return mapping.findForward("recPaidPlan");
	}
	/**
	 * 获得待删除的回款计划 <br>
	 * @param request
	 *      para > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recPaidPlan页面<br>
	 * 		attr > paidPlanList:待删除的回款计划列表 page:分页信息
	 */
	public void  findDelPaidPlan(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(paidBiz.findDelPaidPlanCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = paidBiz.findDelPaidPlan(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("salOrdCon");
		awareCollect.add("spdResp");
		
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
	 * 跳转到回款记录页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward listPaidPast <br>
	 */
	public ActionForward toListPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("listPaidPast");
	}
	
	/**
	 * 回款记录列表 <br>
	 * @param request 
	 * 		para > 	startDate、endDate:回款日期范围  seName:回款人 cusName:对应客户
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序;
	 * @param response
	 * @return ActionForward listPaidPlan<br>
	 * 		attr > 	paidPlanList:查询后的回款计划列表;  startDate、endDate:回款日期范围;
	 * 				page:页码对象，用于分页;  orderCol:排序列号;  isDe:是否逆序;  seName:回款人 cusName:对应客户
	 */
	public void listPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String range = request.getParameter("range");
		String seName = request.getParameter("seName");
		String cusName = request.getParameter("cusName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String seId = (String)request.getSession().getAttribute("userId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String range = "1";
		String[] items = { "spsIsInv", "spsCus", "spsOrd", "spsMon",
				"spsPayType", "spsDate", "spsUser" };
		String[] args = { range, seId, startDate, endDate, cusName, seName };
		
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
	 * 跳转到订单对应的回款记录<br>
	 * @param request
	 * 			parameter > ordId:订单Id
	 * @param response
	 * @return ActionForward listOrdSps<br>
	 * 		attribute > ordId:订单Id
	 */
	public ActionForward toListOrdSps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String ordId = request.getParameter("ordId");
		
		request.setAttribute("ordId", ordId);
		return mapping.findForward("listOrdSps");
	}
	/**
	 * 订单下回款记录 <br>
	 * @param request
	 *      para > 	ordId:订单ID;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 * @param response
	 * @return ActionForward 跳转到listOrdSps页面<br>
	 * 		attr > 	spsList:符合条件的回款记录列表 ordId:订单ID 
	 * 				page:页码对象，用于分页;  orderCol:排序列号;  isDe:是否逆序; 
	 */
	public void listOrdSps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ordId = request.getParameter("ordId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "spsIsInv", "spsMon", "spsPayType", "spsDate", "spsUser" };
		String[] args = { ordId };
		
		if (p == null || p.equals("")) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listPaidPastsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalPaidPast> list = paidBiz.listPaidPasts(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
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
	 * 跳转到创建回款记录页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到formPaidPast
	 */
	public ActionForward toNewPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCus = customBiz.findCus(Long.parseLong(cusId));
			if(cusCorCus !=null){
				request.setAttribute("cusInfo", cusCorCus);
			}
			else{
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的回款记录！");
				return mapping.findForward("error");
			}
		}
		
		return mapping.findForward("formPaidPast");
	}
	/**
	 * 保存回款记录 <br>
	 * @param request
	 *      para > 	seId:负责账号 cusId:对应客户ID  ordId:订单ID 
	 *      		isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 *      		pastDate:实际回款日期
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > 	msg:保存成功信息提示 isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 */
	public ActionForward savePaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String curSeName = (String)request.getSession().getAttribute("userName");
		String seId = request.getParameter("seId");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String pastDate = request.getParameter("pastDate");
		DynaActionForm form1 = (DynaActionForm) form;
		SalPaidPast paid = (SalPaidPast) form1.get("salPaidPast");
		
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
			paid.setCusCorCus(cus);
		} else {
			paid.setCusCorCus(null);
		}
		if (ordId != null && !ordId.equals("")) {
			SalOrdCon order = new SalOrdCon(ordId);
			paid.setSalOrdCon(order);
		} else {
			paid.setSalOrdCon(null);
		}
		//插入日期
		if (pastDate!=null && !pastDate.equals("")) {
			try {
				paid.setSpsFctDate(dateFormat.parse(pastDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			paid.setSpsFctDate(null);
		}
		paid.setSpsUserCode(curSeName);
		paid.setSpsCreDate(new Date(new Date().getTime()));
		paid.setSpsAltUser(null);
		paid.setSpsAltDate(null);
		//paid.setSpsIsdel("0");
		paid.setSalEmp(new SalEmp(Long.parseLong(seId)));
		paidBiz.savePaidPast(paid);
		
		//详情下刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建回款记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到修改回款记录 <br>
	 * @param request
	 *      para > paidId:回款记录ID
	 * @param response
	 * @return ActionForward 跳转到formPaidPast页面<br>
	 * 		attr > paid:回款记录实体，如果实体为null返回字符串"null"
	 */
	public ActionForward toUpdPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidId = request.getParameter("paidId");
		SalPaidPast paid = paidBiz.getPaidPast(paidId);
		request.setAttribute("paid", paid);
		return mapping.findForward("formPaidPast");
	}
	/**
	 * 修改回款记录 <br>
	 * @param request
	 *      para > paidId:回款记录ID pastDate:实际回款日期 isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 *      			ordId:订单id cusId:客户ID seId:负责人
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑 
	 */
	public ActionForward modPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalPaidPast paid = (SalPaidPast) form1.get("salPaidPast");
		String id = request.getParameter("paidId");
		String seId = request.getParameter("seId");
		String pastDate = request.getParameter("pastDate");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String curSeName = (String)request.getSession().getAttribute("userName");
		
		SalPaidPast paidO = paidBiz.getPaidPast(id);
		if(paidO!=null){
			if (cusId != null && !cusId.equals("")) {
				CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
				paid.setCusCorCus(cus);
			} else {
				paid.setCusCorCus(null);
			}
			//插入日期
			if (pastDate != null && !pastDate.equals("")) {
				try {
					paid.setSpsFctDate(dateFormat.parse(pastDate));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				paid.setSpsFctDate(null);
			}
			if (!ordId.equals("")) {
				SalOrdCon order = new SalOrdCon(ordId);
				paid.setSalOrdCon(order);
			} else {
				paid.setSalOrdCon(null);
			}

			paid.setSpsAltUser(curSeName);
			paid.setSpsAltDate(new Date(new Date().getTime()));
			paid.setSpsId(paidO.getSpsId());
			paid.setSpsUserCode(paidO.getSpsUserCode());
			paid.setSpsCreDate(paidO.getSpsCreDate());
			//paid.setSpsIsdel(paidO.getSpsIsdel());
			paid.setSalEmp(new SalEmp(Long.parseLong(seId)));
			paidBiz.updatePaidPast(paid,paidO.getSpsPayMon());
		}
		//详情下刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "修改回款记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 回款记录详情(未使用) <br>
	 * @param request
	 *      parameter > pastId:回款记录ID
	 * @param response
	 * @return ActionForward 跳转到showDescPast页面<br>
	 * 		attribute > paidPast:回款记录实体
	 */
	public ActionForward getPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("pastId");
		SalPaidPast paidPast = paidBiz.getPaidPast(id);
		request.setAttribute("paidPast", paidPast);
		return mapping.findForward("showDescPast");
	}

	/**
	 * 删除回款记录 <br>
	 * @param request
	 *      parameter > paidId:回款记录ID; ordId:订单ID; isIfrm:是否在详情页面中编辑若为1则是在详情页面中删除;
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功删除的信息提示 isIfrm:是否在详情页面中编辑若为1则是在详情页面中删除 
	 */
	public ActionForward deletePaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidId = request.getParameter("paidId");
//		String[] paidIds = paidId.split(",");
		String isIfrm = request.getParameter("isIfrm");
		paidBiz.deletePaidPast(paidId);
		//paidPast.setSpsIsdel(SalPaidPast.D_DELED);
//		paidBiz.updatePaidPast(paidPast);
//		paidBiz.invPaidPast(paidPast);
		//详情下刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除回款记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复回款记录 <br>
	 * @param request
	 *      para > id:回款记录ID;	 relId:订单ID
	 * @param response
	 * @return ActionForward 符合恢复条件的跳转到popDivSuc页面<br>
	 *      attr > msg:成功恢复信息提示
	 */
	/*
	public ActionForward recPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		SalPaidPast paidPast = paidBiz.getPaidPast(Long.parseLong(id));
		paidPast.setSpsIsdel(SalPaidPast.D_UNDEL);
		paidBiz.updatePaidPast(paidPast);
//		paidBiz.recovery(paidPast);
//		SalPaidPast paidPast = paidBiz.getPaidPast(Long.parseLong(paidCode));
		request.setAttribute("msg", "恢复回款记录");
		return mapping.findForward("popDivSuc");
	}
	*/

	/**
	 * 删除回款记录不能再恢复 <br>
	 * @param request
	 *      parameter > id:回款记录ID 
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功删除的信息提示
	 */
	/*public ActionForward delPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String paidCode = request.getParameter("id");
		SalPaidPast paidPast = paidBiz.getPaidPast(Long.parseLong(paidCode));
		paidBiz.deletePaidPast(paidPast);
		request.setAttribute("msg", "删除回款记录");
		return mapping.findForward("popDivSuc");
	}*/

	/**
	 * 跳转到待删除的回款记录<br>
	 * @param request
	 * @param response
	 * @return ActionForward recPaidPast <br>
	 */
	/*public ActionForward toListDelPaidPast(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		
		return mapping.findForward("recPaidPast");
	}*/
	/**
	 * 获得待删除的回款记录 <br>
	 * @param request
	 *      para > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recPaidPast页面<br>
	 * 		attr > paidPastList:待删除的回款记录列表 page:分页信息
	 */
	/*public void findDelPaidPast(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(paidBiz.findDelPaidPastCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = paidBiz.findDelPaidPast(page.getCurrentPageNo(), page
				.getPageSize());
		
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

	}*/

	/**
	 * 批量彻底删除<br>
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

		if(type.equals("plan")){
			batchOperate.recBatchDel("SalPaidPlan","spdId",code);
			request.setAttribute("msg", "批量删除回款计划");
		}else if(type.equals("past")){
			batchOperate.recBatchDel("SalPaidPast","spsId",code);
			request.setAttribute("msg", "批量删除回款记录");
		}
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到对应客户的回款记录 <br>
	 * @param request
	 * 				parameter > cusId:客户Id
	 * @param response
	 * @return ActionForward listCusPaidPast<br>
	 * 		attr > cusId:客户Id
	 */
	public ActionForward toListCusPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusPaidPast");
	}
	
	/**
	 * 客户对应的回款记录列表<br>
	 * @param request
	 * 				parameter > cusId:客户Id
	 * @param response  <br>

	 */
	public void listCusPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String isDe = request.getParameter("isDe");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String [] args = {cusId,cusId};
		String [] items = {"spsIsInv", "spsCus", "spsOrd", "spsMon",
				"spsPayType", "spsDate", "spsUser" };
		if(p == null || p.trim().length()<1){
			p="1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize ="30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(paidBiz.listPaidPastsCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List <SalPaidPast> list = paidBiz.listPaidPasts(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
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
	
	public void setPaidBiz(PaidBIZ paidBiz) {
		this.paidBiz = paidBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}
	public void setSalTaskBiz(SalTaskBIZ salTaskBiz) {
		this.salTaskBiz = salTaskBiz;
	}
	public void setCustomBiz(CustomBIZ customBiz) {
		this.customBiz = customBiz;
	}
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}

}