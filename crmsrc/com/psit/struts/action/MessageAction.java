package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.AttBIZ;
import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.MessageBIZ;
import com.psit.struts.BIZ.SalTaskBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;

import com.psit.struts.entity.AttRep;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.Message;
import com.psit.struts.entity.News;
import com.psit.struts.entity.RMessLim;
import com.psit.struts.entity.RNewLim;
import com.psit.struts.entity.RRepLim;
import com.psit.struts.entity.Report;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.Schedule;
import com.psit.struts.entity.AttMes;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.DAO.BatchOperateDAO;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 协同办公 <br>
 */
public class MessageAction extends BaseDispatchAction {
	MessageBIZ messageBiz = null;
	ModifyTypeDAO modType = null;
	AttBIZ attBiz = null;
	UserBIZ userBiz = null;
	SalTaskBIZ salTaskBiz = null;
	BatchOperateDAO batchOperate = null;
	TypeListBIZ typeListBiz = null;
	EmpBIZ empBiz = null;
	PrintWriter out;
	CustomBIZ customBiz = null;
	

	/**
	 * 判断用户是否有协同办公相关操作的权限 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * 
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
			request.setAttribute("errorMsg", "对不起，您没有该操作权限");
			return mapping.findForward("limError");
		}

	}

	/**
	 * 检测用户是否有协同办公相关操作的权限码 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * 
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "toListAllNews", "ac020" },// 新闻公告是否可见
				{ "forwardNews", "c001" },{ "saveNews", "c001" },// 发布新闻公告
				{ "goUpdNewsInfo", "c001" },{ "modifyNewsInfo", "c001" },// 修改新闻公告
				{ "delNews", "c001" },// 删除新闻公告
				{ "showNewsInfo", "c004" },// 查看新闻公告详情
				{ "toShowSchList", "ac021" },// 日程是否可见
				{ "fowardSch", "c005" },{ "saveSchedule", "c005" },// 新建日程
				{ "goUpdSchInfo", "c007" },{ "changeSchedule", "c007" },// 修改日程
				{ "delSchedule", "c006" },// 删除日程
				{ "showScheduleInfo", "c008" },// 查看日程详情
				{ "toListHaveSenRep", "ac023" },// 报告管理是否可见
				{ "toListApproRep", "ac023" },// 工作台上的查看更多待阅报告
				{ "findFoward", "c013" },{ "saveReport", "c013" },// 新建报告
				{ "delReport", "c014" },// 删除报告
				{ "showRepInfo", "c015" },// 审批报告发送待发报告（修改报告）
				{ "showRepDesc", "c016" },// 查看报告详情
				{ "toListAllMess", "ac024" },// 内部消息是否可见
				{ "fowardMess", "c017" },{ "saveMessage", "c017" },// 新建消息
				{ "delMess", "c018" },// 删除消息
				{"batchDelMess","c018"},//批量删除消息
				{ "showMessInfo", "c019" },// 查看消息详情
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 跳转到写报告页面，并加载报告类型 <br>
	 * create_date: Aug 12, 2010,9:23:58 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到report页面<br>
	 *         attribute > repCode:报告主键 repTypeList:报告类别列表 orgList:除去顶级部门的所有部门列表
	 */
	public ActionForward findFoward(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");

		/*
		 * 删除上次未保存的报告上传的附件
		 */
		List templist = messageBiz.getNoUseRep(limUser.getSalEmp().getSeNo());
		Iterator repIt = templist.iterator();
		while (repIt.hasNext()) {
			Report rep = (Report) repIt.next();
			if (rep != null) {
				Set atts = rep.getAttachments();
				Iterator attIt = atts.iterator();
				while (attIt.hasNext()) {
					AttRep attRep = (AttRep) attIt.next();
					FileOperator.delFile(attRep.getAttPath(), request);// 删除物理文件
					// attBiz.delete(attRep);//删除数据库记录(级联删除后无需进行此操作)
				}
			}
			messageBiz.delReport(rep);
		}
		/*
		 * 新建一条报告，上传附件用
		 */
		Report report = new Report();
		report.setSalEmp(limUser.getSalEmp());
		report.setRepIsdel("2");// 状态为未启用 ***自动删除用***
		report.setRepDate(new java.sql.Date(new Date().getTime()));
		messageBiz.saveReport(report);

		request.setAttribute("repCode", report.getRepCode());
		List list = typeListBiz.getEnabledType("23");
		request.setAttribute("repTypeList", list);
		List orgList = empBiz.getAllOrg();
		request.setAttribute("orgList", orgList);
		return mapping.findForward("report");
	}

	/**
	 * 跳转到写消息页面 <br>
	 * create_date: Aug 12, 2010,9:59:17 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > mark:标识是创建新消息还是发送保存的草稿值为send表示发送草稿 meCode:消息ID
	 * @param response
	 * @return ActionForward 跳转到message页面<br>
	 *         attribute > meCode:消息ID orgList:除去顶级部门的所有部门列表
	 */
	public ActionForward fowardMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String mark = request.getParameter("mark");

		// 发送草稿
		if (mark != null && mark.equals("send")) {
			String meCode = request.getParameter("meCode");
			request.setAttribute("meCode", meCode);
		} else {
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");

			/*
			 * 删除上次未保存的消息上传的附件
			 */
			List list = messageBiz.getNoUseMess(limUser.getSalEmp().getSeNo());
			Iterator mesIt = list.iterator();
			while (mesIt.hasNext()) {
				Message mes = (Message) mesIt.next();
				if (mes != null) {
					Set atts = mes.getAttachments();
					Iterator attIt = atts.iterator();
					while (attIt.hasNext()) {
						AttMes attMes = (AttMes) attIt.next();
						FileOperator.delFile(attMes.getAttPath(), request);// 删除物理文件
						// attBiz.delete(attMes);//删除数据库记录(级联删除后无需进行此操作)
					}
				}
				messageBiz.delMess(mes);
			}

			/*
			 * 新建一条消息，上传附件用
			 */
			Message message = new Message();
			message.setMeIssend("0");
			// message.setMeCode(meCode);
			message.setSalEmp(limUser.getSalEmp());
			message.setMeIsdel("2");// 状态为未启用 ***自动删除用***
			message.setMeDate(new java.sql.Date(new Date().getTime()));
			messageBiz.save(message);
			request.setAttribute("meCode", message.getMeCode());
		}

		List orgList = empBiz.getAllOrg();
		request.setAttribute("orgList", orgList);
		return mapping.findForward("message");

	}

	/**
	 * 保存报告 <br>
	 * create_date: Aug 12, 2010,10:12:53 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > repCode:报告ID repFromCode:报告发送人 repType:报告类别ID 
	 *            		   isSend:标识是保存报告还是发送报告0表示保存报告1表示发送报告 nodeIds:勾选的员工id(isend=1时才获得)
	 *            		   nodeNamesInput:勾选的员工name(isend=1时才获得)
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > isSend值为0 msg:报告保存成功信息提示<br>
	 *         attribute > isSend值为1 msg:报告发送成功信息提示<br>
	 */
	public ActionForward saveReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		Report report = (Report) form1.get("report");
		String saveType = request.getParameter("saveType");
		String repCode = request.getParameter("repCode");
		String repType = request.getParameter("repType");
		String isSend = request.getParameter("isSend");
		String nodeIds=request.getParameter("nodeIds");
		String nodeNamesInput=request.getParameter("nodeNamesInput");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		if (repType != null && !repType.equals("")) {
			report.setRepType(new TypeList(Long.parseLong(repType)));
		} else {
			report.setRepType(null);
		}
		report.setRepCode(Long.valueOf(repCode));
		report.setSalEmp(user.getSalEmp());
		report.setRepIsappro("0");
		report.setRepIsdel("1");
		report.setRepDate(date1);
		report.setRepInsUser(user.getUserSeName());
		report.setRepRecName(nodeIds+"|"+nodeNamesInput);
		if (saveType != null && saveType.equals("report")) {
			if (isSend.equals("0")) {
				report.setRepIsSend("0");
				messageBiz.update(report);
				request.setAttribute("msg", "报告保存");
			}
			if (isSend.equals("1")) {
				report.setRepIsSend("1");
				messageBiz.update(report);
				String[] empId = null;
				empId = nodeIds.split(",");
					for (int i = 0; i < empId.length; i++) {
						if (empId[i] != null) {
							RRepLim rrepLim = new RRepLim();
							rrepLim.setRrlDate(date1);
							rrepLim.setRrlIsappro("0");
							rrepLim.setRrlIsView("1");
							rrepLim.setReport(report);
							SalEmp emp = empBiz.findById(Long.parseLong(empId[i]));
							rrepLim.setSalEmp(emp);
							rrepLim.setRrlRecUser(emp.getSeName());
							rrepLim.setRrlIsdel("1");
							messageBiz.save(rrepLim);
					}
				}
				request.setAttribute("msg", "报告发送");
			}
		}

		return mapping.findForward("popDivSuc");

	}

	/**
	 * 更新报告，并发送 <br>
	 * create_date: Aug 12, 2010,10:29:09 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > repCode:报告ID repType:报告类别ID
	 *            		   isSend:标识是保存报告还是发送报告0表示保存报告1表示发送报告
	 *           		   nodeIds:勾选的员工id
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > isSend值为0 msg:报告保存成功信息提示<br>
	 *         attribute > isSend值为1 msg:报告发送成功信息提示<br>
	 */
	public ActionForward updateSendReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		Report report1 = (Report) form1.get("report");
		String repCode = request.getParameter("repCode");
		String isSend = request.getParameter("isSend");
		String repType = request.getParameter("repType");
		String nodeIds=request.getParameter("nodeIds");
		String nodeNamesInput=request.getParameter("nodeNamesInput");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		Report report = messageBiz.showReportInfo(Long.valueOf(repCode));
		report.setRepDate(date1);
		report.setRepRecName(nodeIds+"|"+nodeNamesInput);
		if (isSend.equals("0")) {
			report.setRepContent(report1.getRepContent());
			report.setRepTitle(report1.getRepTitle());
			if (repType != null && !repType.equals("")) {
				report.setRepType(new TypeList(Long.parseLong(repType)));
			}
			messageBiz.update(report);
			request.setAttribute("msg", "报告保存");
		} else if (isSend.equals("1")) {
			report.setRepContent(report1.getRepContent());
			report.setRepSendTitle(report1.getRepSendTitle());
			report.setRepTitle(report1.getRepTitle());
			if (repType != null && !repType.equals("")) {
				report.setRepType(new TypeList(Long.parseLong(repType)));
			}
			report.setRepIsSend("1");
			messageBiz.update(report);
			
			String[] empId = null;
			empId = nodeIds.split(",");
				for (int i = 0; i < empId.length; i++) {
					if (empId[i] != null) {
						RRepLim rrepLim = new RRepLim();
						rrepLim.setRrlDate(date1);
						rrepLim.setRrlIsappro("0");
						rrepLim.setRrlIsView("1");
						rrepLim.setReport(report);
						SalEmp emp = empBiz.findById(Long.parseLong(empId[i]));
						rrepLim.setSalEmp(emp);
						rrepLim.setRrlRecUser(emp.getSeName());
						rrepLim.setRrlIsdel("1");
						messageBiz.save(rrepLim);
					}
				}
			request.setAttribute("msg", "报告发送");
		}
		return mapping.findForward("popDivSuc");

	}

	/**
	 * 保存消息 <br>
	 * create_date: Aug 12, 2010,10:34:20 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > meCode:消息ID nodeIds:消息接受人id nodeNamesInput:消息接受人name
	 *            		   isSend:0表示保存草稿，1表示发送消息，2表示回复消息 count:所有部门总数(isend=1时才获得)
	 *            		   count1:所有负责账号总数(isend=1时才获得) rmlId:已收消息的ID(isend=2时才获得)
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > isSend值为1,2 msg:消息发送成功信息提示<br>
	 *         attribute > isSend值不等于1，2 msg:消息保存成功信息提示<br>
	 */
	public ActionForward saveMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		Message message = (Message) form1.get("message");
		String meCode = request.getParameter("meCode");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String isSend = request.getParameter("isSend");
		String nodeIds=request.getParameter("nodeIds");
		String nodeNamesInput=request.getParameter("nodeNamesInput");
		String recName=nodeIds+"|"+nodeNamesInput;
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		if (isSend.equals("1") || isSend.equals("2")) {
			message.setMeIssend("1");
		} else {
			message.setMeIssend("0");
		}
		message.setMeCode(Long.valueOf(meCode));
		message.setSalEmp(user.getSalEmp());
		message.setMeInsUser(user.getUserSeName());
		message.setMeIsdel("1");
		message.setMeDate(date1);
		message.setMeRecName(recName);
		messageBiz.update(message);// .save(message);更改为更新
		if (isSend.equals("1")) {
			String []userCode=nodeIds.split(",");
				for (int i = 0; i < userCode.length; i++) {
					if (userCode[i] != null) {
						RMessLim rmessLim = new RMessLim();
						rmessLim.setRmlDate(date1);
						rmessLim.setMessage(message);
						rmessLim.setRmlState("0");
						SalEmp emp = empBiz.findById(Long.parseLong(userCode[i]));
						rmessLim.setSalEmp(emp);
						rmessLim.setRmlRecUser(emp.getSeName());
						rmessLim.setRmlIsdel("1");
						messageBiz.save(rmessLim);
					}
				}
			request.setAttribute("msg", "消息发送");
		}
		// 回复消息
		else if (isSend.equals("2")) {
			String rmlId = request.getParameter("rmlId");
			String accUserId=request.getParameter("accUserId");
			RMessLim rmessLim = new RMessLim();
			rmessLim.setRmlDate(date1);
			rmessLim.setMessage(message);
			rmessLim.setRmlState("0");
			SalEmp emp = empBiz.findById(Long.parseLong(accUserId));
			rmessLim.setSalEmp(emp);
			rmessLim.setRmlRecUser(emp.getSeName());
			rmessLim.setRmlIsdel("1");
			messageBiz.save(rmessLim);
			request.setAttribute("msg", "消息发送");
			// 标记消息为已回复
			if (rmlId != null) {
				RMessLim rml = messageBiz.getRMessLim(Long.parseLong(rmlId));
				rml.setRmlState("2");
				messageBiz.update(rml);
			}

		} else {
			request.setAttribute("msg", "消息保存");
		}

		return mapping.findForward("popDivSuc");

	}

	/**
	 * 保存审批内容 <br>
	 * create_date: Aug 12, 2010,11:41:58 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > rrlId:已收报告的ID repApproContent:审批内容
	 * @param response
	 * @return ActionForward 执行showRepInfo方法<br>
	 */
	public ActionForward changeReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// DynaActionForm form1=(DynaActionForm) form;
		// Report report1=(Report) form1.get("report");
		// String repCode = request.getParameter("repCode");
		Long rrlId = null;
		if (request.getParameter("rrlId") != null
				&& !request.getParameter("rrlId").equals(""))
			rrlId = Long.valueOf(request.getParameter("rrlId"));
		RRepLim rrepLim = messageBiz.getRRepLim(rrlId);
		String repApproContent = request.getParameter("repApproContent");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		rrepLim.setRrlIsappro("1");
		rrepLim.setRrlOpproDate(date1);
		if (repApproContent != null && !repApproContent.equals("")) {
			rrepLim.setRrlContent(repApproContent);
		} else {
			rrepLim.setRrlContent(null);
		}
		messageBiz.update(rrepLim);
		return showRepInfo(mapping, form, request, response);
	}

	/**
	 * 跳转到特定账号已发的报告<br>
	 * create_date: Mar 23, 2011,3:49:05 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward haveSenRep<br>
	 */
	public ActionForward toListHaveSenRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("haveSenRep");
	}
	
	/**
	 * 获得特定账号已发的报告 <br>
	 * create_date: Aug 12, 2010,11:44:06 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到haveSenRep页面<br>
	 *         attribute > haveRepList:特定账号已发报告列表 page:分页信息
	 */
	public void getHaveSenRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] items = {"typName","repTitle","repDate"};
		
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}

		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		
		Page page = new Page(messageBiz.getHaveSenRepCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getHaveSenRep(page.getCurrentPageNo(), page
				.getPageSize(), user.getSalEmp().getSeNo(),orderItem,isDe);
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("repType");

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
		
		/*if (list.size() <= 0) {
			request.setAttribute("searchmsg", "您还没有提交报告！");
		}
		request.setAttribute("haveRepList", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		return mapping.findForward("haveSenRep");*/
	}

	/**
	 * 删除报告 <br>
	 * create_date: Aug 12, 2010,11:46:18 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > repCode:报告ID
	 *            		   delRmark:值为fdel表示已发报告值为tdel表示待发报告值为sfdel表示已收报告
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String repCode = request.getParameter("repCode");
		String delRmark = request.getParameter("delRmark");
		// String url = "";
		if (delRmark != null && delRmark.equals("fdel")) {
			Report report = messageBiz.showReportInfo(Long.valueOf(repCode));
			report.setRepIsdel("0");
			messageBiz.update(report);// 删除已发报告
			// url = "messageAction.do?op=getHaveSenRep";

		}
		if (delRmark != null && delRmark.equals("tdel")) {
			Report report = messageBiz.showReportInfo(Long.valueOf(repCode));
			messageBiz.delReport(report);// 删除待发报告
			// url = "messageAction.do?op=getReadySenRep";
		}
		if (delRmark != null && delRmark.equals("sfdel")) {
			Long rrlId = null;
			if (request.getParameter("rrlId") != null
					&& !request.getParameter("rrlId").equals(""))
				rrlId = Long.valueOf(request.getParameter("rrlId"));
			RRepLim rrepLim = messageBiz.getRRepLim(rrlId);
			rrepLim.setRrlIsdel("0");
			messageBiz.update(rrepLim);// 删除已收报告
			// url = "messageAction.do?op=getApproRep";
		}
		request.setAttribute("msg", "报告删除");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复已发报告 <br>
	 * create_date: Aug 12, 2010,11:51:18 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:报告ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:恢复成功信息提示
	 */
	public ActionForward recRrepLim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		Report report = messageBiz.showReportInfo(id);
		report.setRepIsdel("1");
		messageBiz.update(report);
		request.setAttribute("msg", "恢复报告");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除已发报告不能再恢复 <br>
	 * create_date: Aug 12, 2010,11:52:49 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:报告ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delRrepLim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		Report report = messageBiz.showReportInfo(id);
		messageBiz.delReport(report);
		request.setAttribute("msg", "删除报告");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到特定账号待发的报告 <br>
	 * create_date: Mar 23, 2011,4:47:59 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward readySenRep <br>
	 */
	public ActionForward toListReadySenRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("readySenRep");
	}
	/**
	 * 获得特定账号待发的报告 <br>
	 * create_date: Aug 12, 2010,11:54:22 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到readySenRep页面<br>
	 *         attribute > readyRepList:特定账号待发报告列表 page:分页信息
	 */
	public void getReadySenRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] items = {"typName","repTitle","repDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(messageBiz.getReadySenRepCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getReadySenRep(page.getCurrentPageNo(), page
				.getPageSize(), user.getSalEmp().getSeNo(),orderItem, isDe);
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("repType");

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
		/*if (list.size() <= 0) {
			request.setAttribute("searchmsg", "您还没有待发的报告！");
		}
		request.setAttribute("readyRepList", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		return mapping.findForward("readySenRep");*/
	}

	/**
	 * 跳转到已发的信息 <br>
	 * create_date: Mar 23, 2011,9:35:50 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward haveSenMess<br>
	 */
	public ActionForward toListHaveSenMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("haveSenMess");
	}
	/**
	 * 获得特定账号已发的信息 <br>
	 * create_date: Aug 12, 2010,11:56:36 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到haveSenMess页面<br>
	 *         attribute > haveMessList:特定账号已发的信息列表 page:分页信息
	 */
	public void getHaveSenMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		
		String [] items = {"title",null,"meDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		Page page = new Page(messageBiz.getHaveSenMessCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getHaveSenMess(page.getCurrentPageNo(), page
				.getPageSize(), user.getSalEmp().getSeNo(),orderItem, isDe);
		
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
			String d = new JSONConvert().model2JSON(listForm,awareCollect).toString();
			out.print(new JSONConvert().model2JSON(listForm,awareCollect));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		/*if (list.size() <= 0) {
			request.setAttribute("searchmsg", "您还没有发过消息！");
		}
		request.setAttribute("haveMessList", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		return mapping.findForward("haveSenMess");*/
	}

	/**
	 * 跳转到待发的所有信息<br>
	 * create_date: Mar 23, 2011,10:46:42 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward readySenMess<br>
	 */
	public ActionForward toListReadySenMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("readySenMess");
	}
	/**
	 * 获得待发的所有信息 <br>
	 * create_date: Aug 12, 2010,11:59:38 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到readySenMess页面<br>
	 *         attribute > readyMessList:特定账号待发的信息列表 page:分页信息
	 */
	public void getReadySenMess(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		
		String [] items = {"meTitle","meDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(messageBiz.getReadySenMessCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getReadySenMess(page.getCurrentPageNo(), page
				.getPageSize(), user.getSalEmp().getSeNo(),orderItem,isDe);
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
			String d = new JSONConvert().model2JSON(listForm,awareCollect).toString();
			out.print(new JSONConvert().model2JSON(listForm,awareCollect));
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		
		/*request.setAttribute("readyMessList", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		return mapping.findForward("readySenMess");*/
	}

	/**
	 * 跳转到已收到的报告 <br>
	 * create_date: Mar 23, 2011,4:19:01 PM <br>
	 * @param mapping
	 * @return ActionForward approvalRep<br>
	 */
	public ActionForward toListApproRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String isApped = request.getParameter("isApped");
		
		request.setAttribute("isApped", isApped);
		return mapping.findForward("approvalRep");
	}
	/**
	 * 获得已收到的报告 <br>
	 * create_date: Aug 12, 2010,12:02:19 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码 isDeskTop:标识是否为工作台不为空跳转到工作台
	 *            		   isApped:0代表未批报告1代表已审批报告为空时代表所有已收到的报告
	 * @param response
	 * @return ActionForward isDeskTop不为空时跳转到lastRep页面为空时跳转到approvalRep页面<br>
	 *         attribute > appRepList:符合条件的报告列表 isApped:标识报告类型 page:分页信息
	 */
	public void getApproRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] items = {"rrlIsappro","typName","repTitle","rrlDate","repInsUser"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}
		String isDeskTop = request.getParameter("isDeskTop");
		// 判断查询是否审批的报告
		String isApped = request.getParameter("isApped");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (isApped == null) {
			isApped = "a";// 查询所有报告
		}
		Page page = null;
		List list = null;
		if (isApped.equals("1")) {
			page = new Page(messageBiz.getAppReportCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getAppReport(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo());
		} else if (isApped.equals("0")) {
			page = new Page(messageBiz.getNoAppReportCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			// 工作台待阅报告（无分页）
			if (isDeskTop != null && !isDeskTop.equals("")) {
				list = messageBiz.getNoAppReport(-1, -1, user.getSalEmp().getSeNo());
			} else {
				list = messageBiz.getNoAppReport(page.getCurrentPageNo(), page
						.getPageSize(), user.getSalEmp().getSeNo());
			}
		} else {
			page = new Page(messageBiz.getAllReportCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getAllReport(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(),orderItem,isDe);
		}

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("report");
		awareCollect.add("report.repType");

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
		// if (list.size() <= 0) {
		// request.setAttribute("searchmsg", "您还没有要批复的报告！");
		// }
		/*request.setAttribute("appRepList", list);
		request.setAttribute("page", page);
		request.setAttribute("isApped", isApped);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		if (isDeskTop != null && !isDeskTop.equals("")) {
			return mapping.findForward("lastRep");
		} else {
			return mapping.findForward("approvalRep");
		}*/

	}

	/**
	 * 获得已收到的报告(工作台) <br>
	 * create_date: Aug 12, 2010,1:59:29 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到lastRep页面<br>
	 *         attribute > appRepList:符合条件的已收报告列表
	 */
	public ActionForward getLastRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		List list = null;
		list = messageBiz.getNoAppReport(-1, -1, user.getSalEmp().getSeNo());
		// if (list.size() <= 0) {
		// request.setAttribute("searchmsg", "您还没有要批复的报告！");
		// }
		request.setAttribute("appRepList", list);
		return mapping.findForward("lastRep");
	}

	/**
	 * 查看报告详情 <br>
	 * create_date: Aug 12, 2010,2:01:23 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > repCode:报告ID
	 * @param response
	 * @return ActionForward 跳转到showAppRepInfo页面<br>
	 *         attribute > reportInfo:报告实体 notAppInf:查看已发报告时隐藏批复
	 */
	public ActionForward showRepDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// String userNum = (String)
		// request.getSession().getAttribute("userNum");
		String repCode = request.getParameter("repCode");
		Report report = messageBiz.showReportInfo(Long.valueOf(repCode));
		request.setAttribute("reportInfo", report);
		request.setAttribute("notAppInf", "1");
		return mapping.findForward("showAppRepInfo");

	}

	/**
	 * 审批报告，发送待发报告 <br>
	 * create_date: Aug 12, 2010,2:29:37 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > repCode:报告ID
	 *            		   mark:值为appRepInfo表示审批报告值为sendInfo表示待发报告
	 * @param response
	 * @return ActionForward mark:值为appRepInfo跳转到showAppRepInfo页面
	 *         					  值为sendInfo跳转到report页面 <br>
	 *         attribute > mark:值为appRepInfo userCount:报告接收人数 reportInfo:报告实体<br>
	 *         attribute > mark:值为sendInfo repTypeList:报告类别列表
	 *         			   orgList:除去顶级部门的所有部门 isReady:值为空表示保存新建报告值不为空表示发送待发报告
	 *         			   reportInfo:报告实体<br>
	 */
	public ActionForward showRepInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// String userNum = (String)
		// request.getSession().getAttribute("userNum");
		String repCode = request.getParameter("repCode");
		String mark = request.getParameter("mark");
		Report report = messageBiz.showReportInfo(Long.valueOf(repCode));

		request.setAttribute("reportInfo", report);
		if (mark.equals("appRepInfo")) {
			request.setAttribute("userCount", report.getRRepLims().size());
			return mapping.findForward("showAppRepInfo");
		} else if (mark.equals("sendInfo")) {
			String userCode = (String) request.getSession().getAttribute(
					"userCode");
			List list = typeListBiz.getEnabledType("23");
			request.setAttribute("repTypeList", list);
			List orgList = empBiz.getAllOrg();
			request.setAttribute("orgList", orgList);
			request.setAttribute("curUser", userCode);
			request.setAttribute("isReady", "1");

			return mapping.findForward("report");
		}
		return null;
	}

	/**
	 * 获得消息详情 <br>
	 * create_date: Aug 12, 2010,2:58:35 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > meCode:消息ID mark:表示不同类型的消息查看详情值为default表示查看已收消息详情
	 *            		   值为haveSend表示查看已发消息详情 值为send表示查看草稿箱里的消息详情
	 *            		   rmlId:已收消息ID(mark值为default时才获得)
	 * @param response
	 * @return ActionForward mark值为default或haveSend跳转到showMessageInfo页面值为send执行fowardMess方法<br>
	 *         attribute > mark值为default rmlId:已收消息ID MessInfo:消息实体 <br>
	 *         attribute > mark值为haveSend haveSend:表示已发消息 MessInfo:消息实体 <br>
	 */
	public ActionForward showMessInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = (String) request.getSession()
				.getAttribute("userCode");
		String meCode = request.getParameter("meCode");
		String mark = request.getParameter("mark");
		Message message = messageBiz.showMessInfo(Long.valueOf(meCode));
		request.setAttribute("MessInfo", message);
		// if (mark.equals("hsmInfo")) {
		// // request.setAttribute("showInfo", "s");// 详情中显示接收人
		// // request.setAttribute("backInfo", "0");//详情中返回按钮返回已发信息列表
		//			
		// return mapping.findForward("showMessageInfo");
		// }
		if (mark.equals("default")) {
			String rmlId = request.getParameter("rmlId");
			RMessLim rml = messageBiz.getRMessLim(Long.parseLong(rmlId));
			// request.setAttribute("showInfo", "f");
			// 状态改为已读
			if (rml.getRmlState().equals("0")) {
				rml.setRmlState("1");
				messageBiz.update(rml);
			}
			// request.setAttribute("backInfo", "1");//详情中返回按钮返回已收信息列表
			request.setAttribute("curUser", userCode);
			request.setAttribute("rmlId", rmlId);
			return mapping.findForward("showMessageInfo");
		}
		// 已发送详情
		else if (mark.equals("haveSend")) {
			request.setAttribute("curUser", userCode);
			request.setAttribute("haveSend", "1");
			return mapping.findForward("showMessageInfo");
		}
		// 草稿箱详情
		else if (mark.equals("send")) {
			return fowardMess(mapping, form, request, response);
		} else
			return null;

	}

	/**
	 * 回复，转发消息,跳转到回复，转发消息页面 <br>
	 * create_date: Aug 12, 2010,3:39:24 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > meCode:消息ID tz:值为reply表示回复消息 值为forward表示转发消息
	 * @param response
	 * @return ActionForward 执行fowardMess方法<br>
	 *         attribute > tz:值为reply rmlId:已收消息ID MessInfo:消息实体
	 *         			   sendType:标识是回复还是转发消息<br>
	 *         attribute > tz:值为forward MessInfo:消息实体 sendType:标识是回复还是转发消息<br>
	 */
	public ActionForward reMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tz = request.getParameter("tz");
		String meCode1 = request.getParameter("meCode");
		Message message = messageBiz.showMessInfo(Long.valueOf(meCode1));
		request.setAttribute("MessInfo", message);
		request.setAttribute("sendType", tz);

		if (tz != null && tz.equals("reply")) {
			request.setAttribute("rmlId", request.getParameter("rmlId"));
			return fowardMess(mapping, form, request, response);
		}
		if (tz != null && tz.equals("forward")) {
			return fowardMess(mapping, form, request, response);
		}
		return null;
	}

	/**
	 * 跳转到所有已收到的信息页面 <br>
	 * create_date: Mar 22, 2011,4:47:57 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward haveAllMess<br>
	 */
	public ActionForward toListAllMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String mesType = request.getParameter("mesType");
		
		request.setAttribute("mesType", mesType);
		return mapping.findForward("haveAllMess");
	}
	/**
	 * 获得所有已收到的信息 <br>
	 * create_date: Aug 12, 2010,3:54:43 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > mesType:值为read表示已读消息值为noRead表示未读消息 值为reply表示已回消息
	 *            		   值为noReply表示未回消息 值为all表示所有已收消息 p:当前页码
	 * @param response
	 * @return ActionForward 跳转到haveAllMess页面<br>
	 *         attribute > haveAllMess:符合条件的消息列表 page:分页信息
	 *         			   mesType:消息类型(已读，未读，已回，未回)
	 */
	public void getAllMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String mesType = request.getParameter("mesType");
		String pageSize = request.getParameter("pageSize");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem ="";
		
		String [] items = {"state","title","insUser","meDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = null;
		List list = null;
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (mesType == null || mesType.trim().length() < 1) {
			mesType = "all";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";
		}

		if (mesType.equals("read")) {
			page = new Page(messageBiz.getIsReadMessCount(user.getSalEmp().getSeNo(), "1"), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getIsReadMess(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(), "1");
		} else if (mesType.equals("noRead")) {
			page = new Page(messageBiz.getIsReadMessCount(user.getSalEmp().getSeNo(), "0"), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getIsReadMess(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(), "0");
		} else if (mesType.equals("reply")) {
			page = new Page(messageBiz.getIsReplyCount(user.getSalEmp().getSeNo(), "1"), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getIsReplyMes(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(), "1");
		} else if (mesType.equals("noReply")) {
			page = new Page(messageBiz.getIsReplyCount(user.getSalEmp().getSeNo(), "0"), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getIsReplyMes(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(), "0");
		} else {
			// mesType为all
			page = new Page(messageBiz.getAllMessCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getAllMess(page.getCurrentPageNo(), page
					.getPageSize(), user.getSalEmp().getSeNo(),orderItem, isDe);
		}
		// if (list.size() <= 0)
		// request.setAttribute("searchmsg", "您尚未收到消息");
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("message");
		
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
		/*request.setAttribute("haveAllMess", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		request.setAttribute("mesType", mesType);
		return mapping.findForward("haveAllMess");*/
	}

	/**
	 * 删除消息 <br>
	 * create_date: Aug 12, 2010,4:02:42 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > meCode:消息ID
	 *            		   delRmark:值为fdel表示已发消息值为tdel表示待发消息值为hcfdel表示已收消息
	 *            		   rmlId:已收消息ID(delRmark:值为hcfdel获得)
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String meCode = request.getParameter("meCode");
		String delRmark = request.getParameter("delRmark");
		// String url = "";
		if (delRmark != null && delRmark.equals("fdel")) {
			Message message = messageBiz.showMessInfo(Long.valueOf(meCode));
			message.setMeIsdel("0");
			messageBiz.update(message);// 删除已发消息
			// url="messageAction.do?op=getHaveSenMess";

		}
		if (delRmark != null && delRmark.equals("tdel")) {
			Message message = messageBiz.showMessInfo(Long.valueOf(meCode));
			messageBiz.delMess(message);// 删除待发消息
			// url="messageAction.do?op=getReadySenMess";
		}
		// if (delRmark != null && delRmark.equals("rfdel")) {
		// Message message = messageBiz.showMessInfo(meCode);
		// message.setMeIsdel("0");
		// messageBiz.update(message);//删除已读消息
		// // url="messageAction.do?op=getIsReadMess&readInfo=havRead";
		// }
		if (delRmark != null && delRmark.equals("hcfdel")) {
			Long rmlId = null;
			if (request.getParameter("rmlId") != null
					&& !request.getParameter("rmlId").equals(""))
				rmlId = Long.valueOf(request.getParameter("rmlId"));
			RMessLim rmessLim = messageBiz.getRMessLim(rmlId);
			//rmessLim.setRmlIsdel("0");
			messageBiz.delRMessLim(rmessLim);// 彻底删除已收的信息
			// url="messageAction.do?op=getAllMess";
		}
		request.setAttribute("msg", "消息删除");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复已收消息 <br>
	 * create_date: Aug 12, 2010,4:10:36 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:消息ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:恢复成功信息提示
	 */
	public ActionForward recEmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		RMessLim rmessLim = messageBiz.getRMessLim(id);
		rmessLim.setRmlIsdel("1");
		messageBiz.update(rmessLim);
		request.setAttribute("msg", "恢复消息");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除已收消息不能再恢复 <br>
	 * create_date: Aug 12, 2010,4:13:39 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:消息ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:彻底删除成功信息提示
	 */
	public ActionForward delEmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		RMessLim rmessLim = messageBiz.getRMessLim(id);
		messageBiz.delRMessLim(rmessLim);
		request.setAttribute("msg", "删除消息");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 批量删除消息 <br>
	 * create_date: Aug 12, 2010,4:15:48 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > code:所有待删除的消息ID
	 *            		   type:标识已发还是已收消息值为ac表示已收值为send表示已发消息
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:批量删除成功信息提示
	 */
	public ActionForward batchDelMess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		String[] priKeyValues = code.split(",");
		if (type != null && type.equals("ac"))
			batchOperate.batchUpdDelMark("RMessLim", "rmlId", priKeyValues,
					"rmlIsdel",0);//彻底删除
		if (type != null && type.equals("send")) {
			batchOperate.batchUpdDelMark("Message", "meCode", priKeyValues,
					"meIsdel",0);
		}
		request.setAttribute("msg", "批量删除消息");
		return mapping.findForward("popDivSuc");

	}

	/**
	 * ajax得到未读消息，当天日程，工作安排，未批报告<br>
	 * create_date: Aug 12, 2010,4:22:11 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward null 输出未读消息，当天日程，工作安排，未批报告数量<br>
	 */
	public ActionForward getNoReadMesCount(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		Long userId = user.getSalEmp().getSeNo();
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			out = response.getWriter();
			int count = messageBiz.getIsReadMessCount(userId, "0");
			int count1 = messageBiz.getSchByDateCount(startDate, endDate,
					userId);// 当天日程
			int count2 = salTaskBiz.myTaskCount(new String []{"", "", "tip", "",userId.toString()});// 工作安排
			int count3 = messageBiz.getNoAppReportCount(userId);// 未批报告
			out.print(count + "," + count1 + "," + count2 + "," + count3
					+ "," + "");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 加载日程类型跳转到新建日程页面 <br>
	 * create_date: Aug 12, 2010,4:28:50 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到schedule页面<br>
	 *         attribute > schTypeList:日程类别列表
	 */
	public ActionForward fowardSch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = typeListBiz.getEnabledType("21");
		String cusId = request.getParameter("cusId");
		String isIfrm = request.getParameter("isIfrm");
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCusInfo = customBiz.getCusCorCusInfo(cusId);
			if(cusCorCusInfo != null){
				request.setAttribute("cusInfo", cusCorCusInfo);
			}
			else {
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的日程安排！");
				return mapping.findForward("error");
			}
		}
		request.setAttribute("schTypeList", list);
		request.setAttribute("isIfrm", isIfrm);
		return mapping.findForward("schedule");
	}

	/**
	 * 保存日程安排信息 <br>
	 * create_date: Aug 12, 2010,4:31:05 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > schStartDate:执行日期 scheduleType:日程类别ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:新建成功信息提示
	 */
	public ActionForward saveSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		String isIfrm = request.getParameter("isIfrm");
		Schedule schedule = (Schedule) form1.get("schedule");
		String cusId = request.getParameter("cusId");
		String schStartDate = request.getParameter("schStartDate");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (schStartDate != null && !schStartDate.equals("")) {
			try {
				Date startDate = dateFormat.parse(schStartDate);
				schedule.setSchStartDate(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			schedule.setSchStartDate(null);
		}
		if (request.getParameter("scheduleType") != null
				&& !request.getParameter("scheduleType").equals("")) {
			schedule.setSchType(new TypeList(Long.valueOf(request
					.getParameter("scheduleType"))));
		} else {
			schedule.setSchType(null);
		}
		if(cusId!=null && !cusId.equals("")){
			schedule.setCusCorCus(new CusCorCus(Long.parseLong(cusId)));
		}else{
			schedule.setCusCorCus(null);
		}
		YHPerson person=new YHPerson();
		person.setSeqId(this.getPersonId(request));
		schedule.setPerson(person);
		schedule.setSchDate(date1);
		schedule.setSchState("未完成");
		schedule.setSchInsUser(this.getPersonName(request));
		messageBiz.save(schedule);
		// 详情下 刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建日程");
		return mapping.findForward("popDivSuc");
	}

	/*public ActionForward getSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		Date startDate = new Date();
		Date endDate = new Date();
		List list = messageBiz.getSchByDate(startDate, endDate, user.getSalEmp().getSeNo());
		int num = messageBiz.getSchByDateCount(startDate, endDate, user.getSalEmp().getSeNo());
		startDate = OperateDate.getDate(startDate, 1);
		endDate = OperateDate.getDate(startDate, 6);
		List list2 = messageBiz.getSchByDate(startDate, endDate, user.getSalEmp().getSeNo());
		int num2 = messageBiz.getSchByDateCount(startDate, endDate, user.getSalEmp().getSeNo());
		request.setAttribute("num", num);
		request.setAttribute("num2", num2);
		request.setAttribute("curSchList", list);
		request.setAttribute("lastSchList", list2);
		return mapping.findForward("readyExeSch");
	}*/
	
	/**
	 * 工作台日程 <br>
	 * @param request
	 * 		attr >	todayList:今天日程列表;	todayListCount:今天日程数;	
	 * 				lastList:七天内日程列表;	lastListCount:其他内日程数;
	 * @return ActionForward lastSch
	 */
	public ActionForward listSchOfDeskTop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("CUR_USER");
		Date startDate = new Date();
		Date endDate = new Date();
		List todayList = messageBiz.getSchByDate(startDate, endDate, user.getSalEmp().getSeNo());
//		int todayListCount = messageBiz.getSchByDateCount(startDate, endDate, user.getSalEmp().getSeNo());
		startDate = OperateDate.getDate(startDate, 1);
		endDate = OperateDate.getDate(startDate, 6);
		List lastList = messageBiz.getSchByDate(startDate, endDate, user.getSalEmp().getSeNo());
//		int lastListCount = messageBiz.getSchByDateCount(startDate, endDate, user.getSalEmp().getSeNo());
//		request.setAttribute("todayListCount", todayListCount);
//		request.setAttribute("lastListCount", lastListCount);
		request.setAttribute("curSchList", todayList);
		request.setAttribute("lastSchList", lastList);
		return mapping.findForward("lastSch");
	}

	/**
	 * 跳转到查看日程 <br>
	 * @return ActionForward 跳转到showAllSchedule<br>
	 */
	public ActionForward toShowSchList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("showAllSchedule");
	}
	/**
	 * 查看日程列表根据执行日期搜索日程 <br>
	 * @param request
	 *         parameter > isAll:标识是待办还是全部日程0表示待办日程1表示全部日程; filter:过滤器;
	 *            		   searDate1、searDate2:日程执行日期时间段; p:当前页码;
	 */
	public void showSchList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String isAll = "1";// 0:待办 1:全部
		String filter = request.getParameter("filter"); // 2:过期
		String searDate1 = request.getParameter("searDate1");
		String searDate2 = request.getParameter("searDate2");
		String pageSize = request.getParameter("pageSize");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = {"schState","schType","schTitle","schDate"};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		String startDate = "";
		String endDate = "";
		Date curDay = new java.sql.Date(new Date().getTime());
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		// 默认显示全部
//		if (isAll == null) {
//			isAll = "1";
//		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}

		if (filter != null && filter.equals("expired")) {
			Date end = new java.sql.Date(OperateDate.getDate(curDay, -1).getTime());
			endDate = end.toString();
			isAll = "0";
		} else if ( (searDate1 == null || searDate1.equals(""))
				&& (searDate2 == null || searDate2.equals(""))) {
			startDate = curDay.toString();
		} else {
			startDate = searDate1;
			endDate = searDate2;
		}
		Page page ;
		List list;
		if(filter!=null && filter.endsWith("expired")){
			String [] args = {startDate, endDate,user.getSalEmp().getSeNo().toString(), isAll};
			page = new Page(messageBiz.getSchListCount(args), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getSchList(args, orderItem, isDe,page.getCurrentPageNo(), page.getPageSize());
		}
		else{
			String [] args1 = {searDate1, searDate2,user.getSalEmp().getSeNo().toString(), isAll};
			page = new Page(messageBiz.getSchListCount(args1), Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			list = messageBiz.getSchList(args1, orderItem, isDe,page.getCurrentPageNo(), page.getPageSize());
		}
		Date end = new java.sql.Date(OperateDate.getDate(curDay, -1).getTime());
		endDate = end.toString();
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("schType");

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
	 * 跳转到客户对应的日程<br>
	 * @param mapping
	 * @param form
	 * @param request	
	 * 				parameter > cusId:客户Id
	 * @param response
	 * @return ActionForward listCusSch<br>
	 * 		attr > 客户Id
	 */
	public ActionForward toListCusSch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusSch");
	}
	
	public void listCusSch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] args = {null,null,String.valueOf(this.getPersonId(request)),null,cusId};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String[] items = {"schState","schType","schTitle","schDate"};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page ;
		List list;
		page = new Page(messageBiz.getSchListCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		list = messageBiz.getSchList(args, orderItem, isDe,page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("schType");

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
	 * 查看日程列表根据执行日期搜索日程 <br>
	 * create_date: Aug 12, 2010,5:01:32 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > isAll:标识是待办还是全部日程0表示待办日程1表示全部日程 listType:值为2表示过期日程
	 *            		   searDate1、searDate2:日程执行日期时间段 p:当前页码
	 * @param response
	 * @return ActionForward 跳转到showAllSchedule页面<br>
	 *         attribute > isAll:标识是待办还是全部日程0表示待办日程1表示全部日程 listType:值为2表示过期日程
	 *         		   	   searDate1、searDate2:日程执行日期时间段 allSchedule:符合条件的日程列表 page:分页信息
	 */
	public ActionForward showSchListCount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String isAll = "1";// 0:待办 1:全部
		String listType = request.getParameter("listType"); // 2:过期
		String searDate1 = request.getParameter("searDate1");
		String searDate2 = request.getParameter("searDate2");
		String pageSize = request.getParameter("pageSize");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = {"schState","schType","schTitle","schDate"};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		String startDate = "";
		String endDate = "";
		Date curDay = new java.sql.Date(new Date().getTime());
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		// 默认显示全部
//		if (isAll == null) {
//			isAll = "1";
//		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}

		if (listType != null && listType.equals("2")) {
			Date end = new java.sql.Date(OperateDate.getDate(curDay, -1).getTime());
			endDate = end.toString();
			isAll = "0";
		} else if ( (searDate1 == null || searDate1.equals(""))
				&& (searDate2 == null || searDate2.equals(""))) {
			startDate = curDay.toString();
		} else {
			startDate = searDate1;
			endDate = searDate2;
		}
		
		Page page = new Page(messageBiz.getSchListCount(new String []{startDate, endDate,
				user.getSalEmp().getSeNo().toString(), isAll}), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getSchList(new String [] {startDate, endDate, user.getSalEmp().getSeNo().toString(), isAll}, orderItem, isDe,page.getCurrentPageNo(), page
				.getPageSize());

		// 过期日程数
//		if (isAll.equals("0")) {
		Date end = new java.sql.Date(OperateDate.getDate(curDay, -1).getTime());
		endDate = end.toString();
		int count = messageBiz.getSchListCount(new String[]{"", endDate, user.getSalEmp().getSeNo().toString(), "0"});
		request.setAttribute("count", count);
//		}
//		request.setAttribute("isAll", isAll);
		request.setAttribute("listType", listType);
		request.setAttribute("orderCol", orderCol);
		request.setAttribute("isDe", isDe);
		request.setAttribute("listType", listType);
		request.setAttribute("searDate1", searDate1);
		request.setAttribute("searDate2", searDate2);
		request.setAttribute("allSchedule", list);
		request.setAttribute("page", page);
		return mapping.findForward("showAllSchedule");
	}
	
	/**
	 * 修改日程安排 <br>
	 * create_date: Aug 12, 2010,5:32:49 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > schId:日程ID schStartDate:日程执行日期 scheduleType:日程类别 cusId：客户Id
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:编辑日程成功信息提示
	 */
	public ActionForward changeSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		Schedule schedule1 = (Schedule) form1.get("schedule");
		String cusId = request.getParameter("cusId");
		String isIfrm = request.getParameter("isIfrm");
		Long schId = Long.valueOf(request.getParameter("schId"));
		String schStartDate = request.getParameter("schStartDate");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Schedule schedule = messageBiz.showSchedule(schId);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = dateFormat.parse(schStartDate);
			schedule.setSchStartDate(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long schType = null;
		if (request.getParameter("scheduleType") != null
				&& !request.getParameter("scheduleType").equals("")) {
			schType = Long.valueOf(request.getParameter("scheduleType"));
			schedule.setSchType(new TypeList(schType));
		} else {
			schedule.setSchType(null);
		}
		if(cusId!=null && !cusId.equals("")){
			schedule.setCusCorCus(new CusCorCus(Long.parseLong(cusId)));
		}else{
			schedule.setCusCorCus(null);
		}
		schedule.setSchTitle(schedule1.getSchTitle());
		schedule.setSchStartTime(schedule1.getSchStartTime());
		schedule.setSchEndTime(schedule1.getSchEndTime());
		schedule.setSchUpdUser(user.getUserSeName());
		schedule.setSchUpdDate(date);
		messageBiz.update(schedule);
		// 详情下编辑 刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑日程安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 修改日程安排状态 <br>
	 * create_date: Aug 13, 2010,9:49:36 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > schId:日程ID
	 * @param response
	 * @return ActionForward null 输出1(表示日程状态一改为已完成)+id(日程id表示页面修改日程状态按钮的层ID)+yid(表示页面显示日程状态的层的ID)<br>
	 */
	public ActionForward modifySchState(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long id = Long.valueOf(request.getParameter("schId"));
		Schedule schedule = messageBiz.showSchedule(id);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			schedule.setSchState("已完成");
			messageBiz.update(schedule);
			String id1 = "y" + id;
			out.print("1" + "," + id + "," + id1 + "," + "");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 跳转到编辑日程页面 <br>
	 * create_date: Aug 13, 2010,10:24:54 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:日程ID
	 * @param response
	 * @return ActionForward 跳转到showEditSchedule页面<br>
	 *         attribute > schedule:日程实体 schTypeList:日程类别列表
	 */
	public ActionForward goUpdSchInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		Schedule schedule = messageBiz.showSchedule(id);
		
		request.setAttribute("schedule", schedule);
		List list = typeListBiz.getEnabledType("21");
		request.setAttribute("schTypeList", list);
		request.setAttribute("isIfrm", isIfrm);
		return mapping.findForward("showEditSchedule");
	}

	/**
	 * 显示日程安排详情 <br>
	 * create_date: Aug 13, 2010,10:27:29 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:日程ID
	 * @param response
	 * @return ActionForward 跳转到showDescSchedule页面<br>
	 *         attribute > schedule:日程实体
	 */
	public ActionForward showScheduleInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		Schedule schedule = messageBiz.showSchedule(id);
		request.setAttribute("schedule", schedule);
		return mapping.findForward("showDescSchedule");
	}

	/**
	 * 删除日程安排 <br>
	 * create_date: Aug 13, 2010,10:28:38 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > schId:日程ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long schId = Long.valueOf(request.getParameter("schId"));
		String isIfrm = request.getParameter("isIfrm");
		Schedule schedule = messageBiz.showSchedule(schId);
		// String type = request.getParameter("type");
		messageBiz.delSchedule(schedule);
		
		if(isIfrm!=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", isIfrm);
		}
		request.setAttribute("msg", "删除日程安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 生成新闻编号，跳转到新建页面 <br>
	 * create_date: Aug 12, 2010,5:35:42 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到news页面<br>
	 *         attribute > orgList:除去顶级部门的所有部门列表
	 */
	public ActionForward forwardNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List orgList = empBiz.getAllOrg();
		request.setAttribute("orgList", orgList);
		return mapping.findForward("news");
	}

	/**
	 * 保存新闻公告 <br>
	 * create_date: Aug 12, 2010,5:37:56 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > content:新闻公告内容 nodeIds:勾选接收人id
	 *            		   newFromCode:新闻公告发布人
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:发布成功信息提示
	 */
	public ActionForward saveNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String content = request.getParameter("content");
		String nodeIds = request.getParameter("nodeIds");
		DynaActionForm form1 = (DynaActionForm) form;
		News news = (News) form1.get("news");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		news.setSalEmp(user.getSalEmp());
		news.setNewDate(date1);
		news.setNewInsUser(user.getUserSeName());
		news.setNewContent(content);
		messageBiz.saveNews(news);
		String []empId=nodeIds.split(",");
		for (int i = 0; i < empId.length; i++) {
			if (empId[i] != null) {
					RNewLim rnewLim = new RNewLim();
					rnewLim.setRnlDate(date1);
					rnewLim.setNews(news);
					rnewLim.setSalEmp(new SalEmp(Long.parseLong(empId[i])));
					messageBiz.save(rnewLim);
				}

			}
		request.setAttribute("msg", "发布新闻公告");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 获得最近发布的新闻公告 <br>
	 * create_date: Aug 12, 2010,5:44:03 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > type1:新闻 type2:公告
	 * @param response
	 * @return ActionForward 跳转到lastNews页面<br>
	 *         attribute > today:当天日期 lastNews1:新闻列表 lastNews2:公告列表
	 */
	public ActionForward getLastNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");

		List list1 = messageBiz.getAllNews(user.getSalEmp().getSeNo(), "'新闻','业内动态'");
		List list2 = messageBiz.getAllNews(user.getSalEmp().getSeNo(), "'通知','公告'");

		// 得到当天时间
		Date date = new Date();
		request.setAttribute("today", new java.sql.Date(date.getTime())
				.toString());
		// if(list.size()<=0)
		// request.setAttribute("searchmsg", "暂无新闻公告！");
		request.setAttribute("lastNews1", list1);
		request.setAttribute("lastNews2", list2);

		return mapping.findForward("lastNews");
	}

	/**
	 * 跳转到新闻公告 <br>
	 * create_date: Mar 23, 2011,1:30:19 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > category:是我的还是所有 0为我的  1为所有
	 * @param response
	 * @return ActionForward showAllNews<br>
	 * 		attribute > category:是我的还是所有 0为我的  1为所有
	 */
	public ActionForward toListAllNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String category = request.getParameter("category");
		
		request.setAttribute("category", category);
		return mapping.findForward("showAllNews");
	}
	/**
	 * 获得所有新闻公告 <br>
	 * create_date: Aug 12, 2010,5:52:15 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > isDeskTop:标识是否跳转到工作台 p:当前页码 category:是我的还是所有 0为我的  1为所有
	 * @param response
	 * @return ActionForward isDeskTop不为空跳转到lastNewsNotice页面否则跳转到showAllNews页面<br>
	 *         attribute > isDeskTop不为空 today:当天日期 allNews:符合条件的新闻公告列表 page:分页信息<br>
	 *         attribute > isDeskTop为空 allNews:符合条件的新闻公告列表 page:分页信息<br>
	 */
	public void getAllNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		String isDeskTop = request.getParameter("isDeskTop");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String p = request.getParameter("p");
		String orderItem = "";
		String[] items = {"newType","newTitle","newInsUser","newDate"};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(messageBiz.getAllNewsCount(user.getSalEmp().getSeNo()), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getAllNews(page.getCurrentPageNo(), page
				.getPageSize(),orderItem,isDe, user.getSalEmp().getSeNo());
		
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
		
		/*request.setAttribute("allNews", list);
		request.setAttribute("page", page);
		request.setAttribute("orderCol",orderCol);
		request.setAttribute("isDe",isDe);
		if (isDeskTop != null && !isDeskTop.equals("")) {
			// 得到当天时间
			Date date = new Date();
			request.setAttribute("today", new java.sql.Date(date.getTime())
					.toString());
			return mapping.findForward("lastNewsNotice");
		} else {
			return mapping.findForward("showAllNews");
		}*/
	}

	/**
	 * 工作台新闻公告 <br>
	 * create_date: Aug 13, 2010,10:49:18 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到lastNewsNotice页面<br>
	 *         attribute > today:当天日期 allNews:符合条件的新闻公告列表
	 */
	public ActionForward getNewsReps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser user = (LimUser) request.getSession()
				.getAttribute("limUser");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		// String isDeskTop=request.getParameter("isDeskTop");
		// String p = request.getParameter("p");
		// if (p == null || p.trim().length() < 1) {
		// p = "1";
		// }
		// Page page = new Page(messageBiz.getAllNewsCount(userCode), 20);
		// page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getAllNews(1, 20,orderItem,isDe, user.getSalEmp().getSeNo());
		// if (list.size() <= 0)
		// request.setAttribute("searchmsg", "暂无新闻公告！");
		request.setAttribute("allNews", list);
		// request.setAttribute("page", page);
		// 得到当天时间
		Date date = new Date();
		request.setAttribute("today", new java.sql.Date(date.getTime())
				.toString());
		return mapping.findForward("lastNewsNotice");

	}

	/**
	 * 跳转到新闻公告编辑页面 <br>
	 * create_date: Aug 13, 2010,10:50:29 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > newCode:新闻公告ID
	 * @param response
	 * @return ActionForward 跳转到showEditNews页面<br>
	 *         attribute > newsInfo:新闻公告实体
	 */
	public ActionForward goUpdNewsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String newCode = request.getParameter("newCode");
		News news = messageBiz.showNewsInfo(Long.valueOf(newCode));
		request.setAttribute("newsInfo", news);
		return mapping.findForward("showEditNews");
	}

	/**
	 * 显示新闻公告详情 <br>
	 * create_date: Aug 13, 2010,10:58:30 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > newCode:新闻公告ID isEdit:标识是查看已收的新闻公告详情还是查看已发的新闻公告详情
	 *            		   0表示查看已收的新闻公告详情1表示查看已发的新闻公告详情
	 * @param response
	 * @return ActionForward 跳转到showDescNews页面<br>
	 *         attribute > newsInfo:新闻公告实体 isEdit:标识是查看已收的新闻公告详情还是查看已发的新闻公告详情
	 */
	public ActionForward showNewsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String newCode = request.getParameter("newCode");
		String isEdit = request.getParameter("isEdit");
		News news = messageBiz.showNewsInfo(Long.valueOf(newCode));
		request.setAttribute("newsInfo", news);
		request.setAttribute("isEdit", isEdit);
		return mapping.findForward("showDescNews");
	}

	/**
	 * 跳转到所有已发布的新闻公告<br>
	 * create_date: Mar 23, 2011,2:17:56 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward showAllSendNews<br>
	 */
	public ActionForward toListAllSendNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("showAllSendNews");
	}
	/**
	 * 所有已发布的新闻公告 <br>
	 * create_date: Aug 13, 2010,11:09:21 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到showAllSendNews页面<br>
	 *         attribute > allSenNews:所有已发的新闻公告列表 page:分页信息
	 */
	public void getAllSendNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String p = request.getParameter("p");
		String[] items = {"newType","newTitle","newInsUser","newDate"};
		String orderItem = "";
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		Page page = new Page(messageBiz.getHaveSenNewsCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.getHaveSenNews(orderItem,isDe,page.getCurrentPageNo(), page.getPageSize());
		
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
		/*request.setAttribute("allSenNews", list);
		request.setAttribute("orderCol",orderCol);
		request.setAttribute("isDe",isDe);
		request.setAttribute("page", page);
		return mapping.findForward("showAllSendNews");*/
	}

	/**
	 * 修改新闻公告信息 <br>
	 * create_date: Aug 13, 2010,11:13:37 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > newCode:新闻公告ID content:新闻公告内容
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:编辑成功信息提示
	 */
	public ActionForward modifyNewsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		News news1 = (News) form1.get("news");
		String newCode = request.getParameter("newCode");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		String userIds = request.getParameter("nodeIds");//接收人
		String [] usersArray = null;
		Date date = new Date();
		String content = request.getParameter("content");
		News news = messageBiz.showNewsInfo(Long.valueOf(newCode));
		news.setNewTitle(news1.getNewTitle());
		news.setNewUpdUser(user.getUserSeName());
		news.setNewUpdDate(date);
		news.setNewContent(content);
		news.setNewType(news1.getNewType());
		news.setNewIstop(news1.getNewIstop());
		messageBiz.update(news);
		
		//清空新该新闻的接收人
		messageBiz.delRnewLim(Long.valueOf(newCode));
		//更新接收人
		usersArray = userIds.split(",");
		List list = messageBiz.findAllMsgMan(Long.parseLong(newCode));
		for(int i=0;i<usersArray.length;i++){
			if(usersArray[i]!=null && !usersArray[i].equals("")){
				Long userSeNo = Long.parseLong(usersArray[i]);
						RNewLim rnewLim = new RNewLim();
						rnewLim.setNews(news);
						rnewLim.setSalEmp(new SalEmp(userSeNo));
						rnewLim.setRnlDate(date);
						messageBiz.save(rnewLim);
					}
				}
		request.setAttribute("msg", "编辑新闻公告");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除新闻公告 <br>
	 * create_date: Aug 13, 2010,11:16:31 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > newCode:新闻公告ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String newCode = request.getParameter("newCode");
		News news = messageBiz.showNewsInfo(Long.valueOf(newCode));
		messageBiz.delNews(news);
		request.setAttribute("msg", "删除新闻公告");
		//删除关联附件
		Iterator it = news.getAttachments().iterator();
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到待删除的已发报告<br>
	 * create_date: Mar 24, 2011,8:00:19 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward recReport<br>
	 */
	public ActionForward toListDelReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recReport");
	}
	/**
	 * 获得待删除的已发报告 <br>
	 * create_date: Aug 13, 2010,11:17:39 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recReport页面<br>
	 *         attribute > appRepList:待删除的已发报告列表 page:分页信息
	 */
	public void findDelReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(messageBiz.findDelReportCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.findDelReport(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("repType");
		
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
		/*request.setAttribute("appRepList", list);
		request.setAttribute("page", page);
		return mapping.findForward("recReport");*/

	}

	/**
	 * 获得待删除的已收消息 <br>
	 * create_date: Aug 13, 2010,11:19:51 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到recMail页面<br>
	 *         attribute > haveAllMess:待删除的已收消息列表 page:分页信息
	 */
	public ActionForward findDelMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(messageBiz.findDelMailCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = messageBiz.findDelMail(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("haveAllMess", list);
		request.setAttribute("page", page);
		return mapping.findForward("recMail");

	}

	
	/**
	 * 注入messageBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param messageBiz
	 */
	public void setMessageBiz(MessageBIZ messageBiz) {
		this.messageBiz = messageBiz;
	}

	/**
	 * 注入modType <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param modType
	 */
	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}

	/**
	 * 注入attBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param attBiz
	 */
	public void setAttBiz(AttBIZ attBiz) {
		this.attBiz = attBiz;
	}

	/**
	 * 注入userBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param userBiz
	 */
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}

	/**
	 * 注入batchOperate <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param batchOperate
	 */
	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}

	/**
	 * 注入salTaskBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param salTaskBiz
	 */
	public void setSalTaskBiz(SalTaskBIZ salTaskBiz) {
		this.salTaskBiz = salTaskBiz;
	}

	/**
	 * 注入typeListBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param typeListBiz
	 */
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}

	/**
	 * 注入empBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * 
	 * @param empBiz
	 */
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	
	/**
	 * 注入customBiz<br>
	 * @param customBiz <br>
	 */
	public void setCustomBiz(CustomBIZ customBiz) {
		this.customBiz = customBiz;
	}
}