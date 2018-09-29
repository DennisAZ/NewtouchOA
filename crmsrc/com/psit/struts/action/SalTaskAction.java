package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.SalTaskBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.TaLim;
import com.psit.struts.entity.TypeList;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 工作安排 <br>
 */
public class SalTaskAction extends BaseDispatchAction {
	SalTaskBIZ salTaskBiz = null;
	EmpBIZ empBiz = null;
	ModifyTypeDAO modType = null;
	UserBIZ userBiz = null;
	TypeListBIZ typeListBiz = null;

	/**
	 * 判断用户是否有工作安排操作的权限 <br>
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
			return mapping.findForward("limError");
		}

	}

	/**
	 * 检测用户是否有工作安排的操作的权限码 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "toListMyTaskSearch", "ac022" },//工作安排是否可见
				{ "salTaskSearch", "ac022" },//工作台查看更多的已发工作按钮
				{ "newSalTask", "c009" },{ "newSalTask", "c009" },{"salTask","c009"},//新建工作安排
				{ "delSalTask", "c010" },//删除工作安排
				{ "update", "c011" },{ "updateSalTask", "c011" },//修改工作安排
				{ "salTaskDesc", "c012" },//查看已发的工作安排详情
				{ "showMyTask", "c012" },//查看收到的工作安排详情
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 加载工作安排执行人列表 <br>
	 * create_date: Aug 18, 2010,10:37:55 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > taskId:工作安排ID
	 * @param response 输出工作执行人列表<br>
	 */
	public void getTaskUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskId = request.getParameter("taskId");
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print("<?xml version='1.0' encoding='UTF-8'?><tus>");
			SalTask salTask = salTaskBiz.salTaskDesc(Long.valueOf(taskId));
			Set taLims = salTask.getTaLims();
			Iterator it = taLims.iterator();
			while (it.hasNext()) {
				TaLim talim = (TaLim) it.next();
				if (talim.getTaIsfin().equals("1")) {
					out.print("<taskUsers userName='" + talim.getTaName()
							+ "' taskSta='" + talim.getTaIsdel()
							+ "'></taskUsers>");
				}
			}
			out.print("</tus>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * 建立工作安排 <br>
	 * create_date: Aug 20, 2010,2:54:58 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到newSalTask页面<br>
	 * 		attribute > salTaskType:工作类别列表 orgList:执行人列表
	 */
	public ActionForward salTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = typeListBiz.getEnabledType("22");
		List orgList = empBiz.getAllOrg();

		request.setAttribute("salTaskType", list);
		request.setAttribute("orgList", orgList);//执行人
		return mapping.findForward("newSalTask");
	}

	/**
	 * 保存工作安排 <br>
	 * create_date: Aug 20, 2010,2:57:14 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > taskType:工作类别ID satFinDate:完成期限 nodeIds:执行人id组合字符串
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:工作发布成功信息提示
	 */
	public ActionForward newSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalTask salTask = (SalTask) form1.get("salTask");
		String taskType = request.getParameter("taskType");
		String satStartDate = request.getParameter("satStartDate");
		String satFinDate = request.getParameter("satFinDate");
		String userIds = request.getParameter("nodeIds");//执行人
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		TypeList salTaskType = new TypeList();
		String[] usersArray = null;

		if (taskType != null && !taskType.equals("")) {
			Long sttId = Long.parseLong(taskType);
			salTaskType.setTypId(sttId);
			salTask.setSalTaskType(salTaskType);
		} else {
			salTask.setSalTaskType(null);
		}
		if (satStartDate != null && !satStartDate.equals("")) {
			try {
				Date startDate = dateFormat.parse(satStartDate);
				salTask.setStStartDate(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			salTask.setStStartDate(null);
		}
		if (satFinDate != null && !satFinDate.equals("")) {
			try {
				Date finDate = dateFormat.parse(satFinDate);
				salTask.setStFinDate(finDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			salTask.setStFinDate(null);
		}
		salTask.setSalEmp(new SalEmp(limUser.getSalEmp().getSeNo()));
		salTask.setStName(limUser.getUserSeName());
		salTask.setStRelDate(new java.sql.Date(date.getTime()));
		salTask.setStIsdel("1");
		empBiz.saveTask2(salTask);

		//保存执行人
		usersArray = userIds.split(",");
		for (int i = 0; i < usersArray.length; i++) {
			if (usersArray[i] != null && !usersArray[i].equals("")) {
				TaLim taLim = new TaLim();
				taLim.setSalTask(salTask);
				taLim.setTaIsdel("0");
				taLim.setTaIsfin("1");
				SalEmp emp = empBiz.findById(Long.parseLong(usersArray[i]));
				taLim.setSalEmp(emp);
				taLim.setTaName(emp.getSeName());
				salTaskBiz.saveTal(taLim);
			}
		}

		request.setAttribute("msg", "发布工作安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到工作安排搜索<br>
	 * create_date: Mar 23, 2011,6:47:09 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward salTask <br> 
	 */
	public ActionForward toListSalTaskSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		
		request.setAttribute("range", range);
		return mapping.findForward("salTask");
	}
	/**
	 * 工作安排搜索 <br>
	 * create_date: Aug 20, 2010,3:03:18 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码 stTitle:工作名称 stStu:工作状态 range:标识查询范围1表示我发布的工作安排2表示全部工作安排
	 *      			orederCol:排序字段 isDe:是否逆序  pageSize:每页显示记录数
	 * @param response
	 * @return ActionForward 跳转到salTask页面<br>
	 * 		attribute > salTask:符合条件的工作列表 mark:标识查询范围 page:分页信息 stTitle:工作名称 stStu:工作状态 orederCol:排序字段 isDe:是否逆序
	 *                  lesCount:七天内要完成的工作条数 afCount:七天后要完成的工作条数 noCount:无完成期限的工作条数 oveCount:已过期的工作条数
	 */
	public void  salTaskSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String stTitle = request.getParameter("stTitle");
		String stStu = request.getParameter("stStu");
		String mark = request.getParameter("mark");//没用
		String range = request.getParameter("range");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String code ="";
		String orderItem = "";
		String [] items = {"stStu","typName","stTitle","startTime","endTime",null,"stName","stRelDate"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if(range!=null&&range.equals("1")){
		   code = limUser.getSalEmp().getSeNo().toString();
		}
		String [] args = {stTitle,stStu,code};
		Date finDate = new Date();

		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		Page page = new Page(salTaskBiz.getCount1(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list1 = salTaskBiz.salTaskSearch(args,orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list1);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("salTaskType");

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
	 * 跳转到我的工作<br>
	 * @return ActionForward 跳转到mySalTask<br>
	 */
	public ActionForward toListMyTaskSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String tip = request.getParameter("tip");
		
		request.setAttribute("tip", tip);
		return mapping.findForward("mySalTask");
	}
	/**
	 * 我的工作 <br>
	 * @param request
	 *      parameter > p:当前页码 stTitle:工作名称 stStu:工作状态 tip:工作执行中标识(查询执行中和被退回的工作) mark:值为0表示执行中工作 
	 *                  值为1表示已提交的工作 值为2表示被退回的工作值为3表示已完成的工作
	 */
	public void myTaskSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String stTitle = request.getParameter("stTitle");
		String stStu = request.getParameter("stStu");
		String tip = request.getParameter("tip");
		String mark = request.getParameter("mark");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String code = limUser.getSalEmp().getSeNo().toString();

		String orderItem = "";
		String [] items = {"stStu","typName","stTitle","startTime","endTime",null,"stName","stRelDate"};
		String [] args = {stTitle,stStu,tip,mark,code};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		Page page = new Page(salTaskBiz.myTaskCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = salTaskBiz.myTaskSearch(args, page.getCurrentPageNo(), page.getPageSize(),orderItem, isDe);

		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("salTask");
		awareCollect.add("salTask.salTaskType");

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
	 * 查询发布的待办工作 <br>
	 * create_date: Aug 20, 2010,4:02:40 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到lastTask页面<br>
	 * 		attribute > lastTask:发布的七天内的待办工作列表 laterTask:发布的七天后的待办工作列表
	 *                  timeOutTask:发布的过期的待办工作列表 noTimeTask:发布的无期限的工作列表
	 */
	public ActionForward getTodoTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		Date curDate = new Date();
		Date endDate = OperateDate.getDate(curDate, 7);
		List<SalTask> lastTask = new ArrayList<SalTask>();//七天内
		List<SalTask> laterTask = new ArrayList<SalTask>();//七天后
		List<SalTask> timeOutTask = new ArrayList<SalTask>();//过期
		List<SalTask> noTimeTask = new ArrayList<SalTask>();//无期限

		List<SalTask> todoList = salTaskBiz.getTodoTask(limUser.getSalEmp()
				.getSeNo().toString());
		Iterator<SalTask> it = todoList.iterator();
		while (it.hasNext()) {
			SalTask salTask = it.next();

			if (salTask.getStFinDate() != null) {
				if (curDate.after(salTask.getStFinDate())) {
					timeOutTask.add(salTask);
				} else if (endDate.after(salTask.getStFinDate())) {
					lastTask.add(salTask);
				} else {
					laterTask.add(salTask);
				}
			} else {
				noTimeTask.add(salTask);
			}
		}

		request.setAttribute("lastTask", lastTask);
		request.setAttribute("laterTask", laterTask);
		request.setAttribute("timeOutTask", timeOutTask);
		request.setAttribute("noTimeTask", noTimeTask);
		return mapping.findForward("lastTask");
	}

	/**
	 * 查询收到的待办工作 <br>
	 * create_date: Aug 20, 2010,4:02:40 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到lastMyTask页面<br>
	 * 		attribute > lastTask:七天内待办的工作列表 laterTask:七天后待办的工作列表
	 *                  timeOutTask:过期的待办工作列表 noTimeTask:无期限的工作列表
	 */
	public ActionForward getTodoMyTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String seNo = limUser.getSalEmp().getSeNo().toString();
		Date curDate = new Date();
		Date endDate = OperateDate.getDate(curDate, 7);
		List<TaLim> lastTask = new ArrayList<TaLim>();//七天内
		List<TaLim> laterTask = new ArrayList<TaLim>();//七天后
		List<TaLim> timeOutTask = new ArrayList<TaLim>();//过期
		List<TaLim> noTimeTask = new ArrayList<TaLim>();//无期限

		List<TaLim> todoList = salTaskBiz.getTodoMyTask(seNo);
		Iterator<TaLim> it = todoList.iterator();
		while (it.hasNext()) {
			TaLim taLim = it.next();

			if (taLim.getSalTask().getStFinDate() != null) {
				if (curDate.after(taLim.getSalTask().getStFinDate())) {
					timeOutTask.add(taLim);
				} else if (endDate.after(taLim.getSalTask().getStFinDate())) {
					lastTask.add(taLim);
				} else {
					laterTask.add(taLim);
				}
			} else {
				noTimeTask.add(taLim);
			}
		}

		request.setAttribute("lastTask", lastTask);
		request.setAttribute("laterTask", laterTask);
		request.setAttribute("timeOutTask", timeOutTask);
		request.setAttribute("noTimeTask", noTimeTask);
		return mapping.findForward("lastMyTask");
	}

	/**
	 * 查看工作详情 <br>
	 * create_date: Aug 20, 2010,4:44:38 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作ID
	 * @param response
	 * @return ActionForward 跳转到salTaskDesc页面<br>
	 * 		attribute > salTask:工作实体
	 */
	public ActionForward salTaskDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		LimUser limUser = (LimUser)request.getSession().getAttribute("limUser");
		Long userId = limUser.getSalEmp().getSeNo();
		int isInTask = 0;//判断当前账号是否是发布人或执行人（1：执行人，2：发布人，3：执行人和发布人）
		
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		if(userId.equals(salTask.getSalEmp().getSeNo())){
			isInTask += 2;
		}
		Iterator it = salTask.getTaLims().iterator();
		while(it.hasNext()){
			TaLim talim = (TaLim)it.next();
			if(userId.equals(talim.getSalEmp().getSeNo())){
				isInTask += 1;
				request.setAttribute("taLim", talim);
				break;
			}
		}
		
		request.setAttribute("isInTask", isInTask);
		request.setAttribute("salTask", salTask);
		return mapping.findForward("salTaskDesc");
	}

	/**
	 * 查看工作完成情况<br>
	 * create_date: Aug 20, 2010,4:54:54 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > taLimId:工作执行人(TaLim)表的Id
	 * @param response
	 * @return ActionForward 跳转到showMyTask页面<br>
	 * 		attribute > taLim:工作执行人实体
	 */
	public ActionForward showMyTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long taLimId = Long.parseLong(request.getParameter("taLimId"));
		TaLim taLim = salTaskBiz.showTaLim(taLimId);

		request.setAttribute("taLim", taLim);
		return mapping.findForward("showMyTask");
	}

	/**
	 * 结束工作 <br>
	 * create_date: Aug 17, 2010,4:30:53 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID stTag:工作总结
	 * @param response
	 * @return ActionForward 执行salTaskDesc(mapping,form,request,response)方法<br>
	 */
	public ActionForward finishTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		String stTag = request.getParameter("stTag");
		Date date = new Date();
		
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		salTask.setStStu("1");
		salTask.setStTag(stTag);
		salTask.setStFctDate(new java.sql.Date(date.getTime()));
		salTaskBiz.updateSalTask(salTask);
		return salTaskDesc(mapping, form, request, response);
	}

	/**
	 * 取消工作 <br>
	 * create_date: Aug 17, 2010,4:53:40 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID
	 * @param response
	 * @return ActionForward 执行salTaskDesc(mapping,form,request,response)方法<br>
	 */
	public ActionForward cancelTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		salTask.setStStu("2");
		salTaskBiz.updateSalTask(salTask);
		return salTaskDesc(mapping, form, request, response);
	}

	/**
	 * 重新开始工作 <br>
	 * create_date: Aug 17, 2010,4:49:00 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID
	 * @param response
	 * @return ActionForward 执行salTaskDesc(mapping,form,request,response)方法<br>
	 */
	public ActionForward startTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		salTask.setStStu("0");
		salTaskBiz.updateSalTask(salTask);
		return salTaskDesc(mapping, form, request, response);
	}

	/**
	 * 完成工作 <br>
	 * create_date: Aug 17, 2010,5:14:05 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID taskDesc:工作完成情况 taId:执行人表ID
	 * @param response
	 * @return ActionForward 跳转到salTaskDesc页面<br>
	 *      attribute > taLim:执行人实体
	 */
	public ActionForward optionTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		Long taId = Long.parseLong(request.getParameter("taId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		String log = salTask.getStLog();
		String taskDesc = request.getParameter("taskDesc");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String name = limUser.getUserSeName();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String desc = "";
		String title = "";

		if (taskDesc != null && !taskDesc.equals("")) {
			title = "<strong>>>> " + name + " 于 " + df.format(new Date())
					+ " 提交工作。</strong><br/>(完成情况：" + taskDesc + ")<br/><br/>";
		} else {
			title = "<strong>>>> " + name + " 于 " + df.format(new Date())
					+ " 提交工作。</strong><br/><br/>";
		}
		if (log != null && !log.equals("")) {
			desc = title + log;
		} else {
			desc = title;
		}
		salTask.setStLog(desc);
		salTaskBiz.updateSalTask(salTask);
		TaLim taLim = salTaskBiz.showTaLim(taId);
		if (taLim.getSalEmp().getSeNo().equals(limUser.getSalEmp().getSeNo())) {
			taLim.setTaIsdel("1");
			Date date = new Date();
			Date taFinDate = new java.sql.Date(date.getTime());
			taLim.setTaFinDate(taFinDate);
			//自己的完成记录
			String myLog = taLim.getTaDesc();
			String talog = "";
			String tadesc = "";
			if (taskDesc != null && !taskDesc.equals("")) {
				tadesc = "<strong>>>> " + name + " 于 " + df.format(new Date())
						+ " 提交工作。</strong><br/>(完成情况：" + taskDesc
						+ ")<br/><br/>";
			} else {
				tadesc = "<strong>>>> " + name + " 于 " + df.format(new Date())
						+ " 提交工作。</strong><br/><br/>";
			}
			if (myLog != null && !myLog.equals("")) {
				talog = tadesc + myLog;
			} else {
				talog = tadesc;
			}
			taLim.setTaName(name);
			taLim.setTaDesc(talog);
			salTaskBiz.updateTaskMan(taLim);
		}

		return salTaskDesc(mapping, form, request, response);
	}

	/**
	 * 退回工作 <br>
	 * create_date: Aug 17, 2010,5:21:30 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID taskDesc:退回原因 taName:被退回的人 taLimId:执行人表ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功退回的信息提示
	 */
	public ActionForward backTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		String log = salTask.getStLog();
		String taskDesc = request.getParameter("taskDesc");
		String name1 = request.getParameter("taName");//执行人
		String taLimId = request.getParameter("taLimId");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String name = limUser.getUserSeName();//发布人
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String desc = "";
		String title = "";

		if (taskDesc != null && !taskDesc.equals("")) {
			title = "<strong><<< " + name + " 于 " + df.format(new Date())
					+ " 退回 " + name1 + " 提交的工作。</strong><br/>(原因：" + taskDesc
					+ ")<br/><br/>";
		} else {
			title = "<strong><<< " + name + " 于 " + df.format(new Date())
					+ " 退回 " + name1 + " 提交的工作。</strong><br/><br/>";
		}
		if (log != null && !log.equals("")) {
			desc = title + log;
		} else {
			desc = title;
		}
		salTask.setStLog(desc);
		salTaskBiz.updateSalTask(salTask);
		TaLim taLim = salTaskBiz.showTaLim(Long.parseLong(taLimId));
		taLim.setTaIsdel("2");//退回状态
		//自己的完成记录
		String myLog = taLim.getTaDesc();
		String talog = "";
		String tadesc = "";
		if (taskDesc != null && !taskDesc.equals("")) {
			tadesc = "<strong><<< " + name + " 于 " + df.format(new Date())
					+ " 退回 " + name1 + " 提交的工作。</strong><br/>(原因：" + taskDesc
					+ ")<br/><br/>";
		} else {
			tadesc = "<strong><<< " + name + " 于 " + df.format(new Date())
					+ " 退回 " + name1 + " 提交的工作。</strong><br/><br/>";
		}
		if (myLog != null && !myLog.equals("")) {
			talog = tadesc + myLog;
		} else {
			talog = tadesc;
		}
		taLim.setTaDesc(talog);
		salTaskBiz.updateTaskMan(taLim);

		request.setAttribute("msg", "退回工作提交");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 确认某人完成工作安排<br>
	 * create_date: Aug 17, 2010,5:26:50 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID taskDesc:完成评价 taName:工作提交人 taLimId:执行人表ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:确认完成工作信息提示
	 */
	public ActionForward finSomeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		String log = salTask.getStLog();
		String taskDesc = request.getParameter("taskDesc");
		String name1 = request.getParameter("taName");//执行人
		String taLimId = request.getParameter("taLimId");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		String name = limUser.getUserSeName();//发布人
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String desc = "";
		String title = "";

		if (taskDesc != null && !taskDesc.equals("")) {
			title = "<strong> √ " + name + " 于 " + df.format(new Date())
					+ " 确认了 " + name1 + " 提交的工作。</strong><br/>(情况：" + taskDesc
					+ ")<br/><br/>";
		} else {
			title = "<strong> √ " + name + " 于 " + df.format(new Date())
					+ " 确认了 " + name1 + " 提交的工作。</strong><br/><br/>";
		}
		if (log != null && !log.equals("")) {
			desc = title + log;
		} else {
			desc = title;
		}
		salTask.setStLog(desc);
		salTaskBiz.updateSalTask(salTask);
		TaLim taLim = salTaskBiz.showTaLim(Long.parseLong(taLimId));
		String myLog = taLim.getTaDesc();
		String talog = "";
		String tadesc = "";
		if (taskDesc != null && !taskDesc.equals("")) {
			tadesc = "<strong> √ " + name + " 于 " + df.format(new Date())
					+ " 确认了 " + name1 + " 提交的工作。</strong><br/>(情况：" + taskDesc
					+ ")<br/><br/>";
		} else {
			tadesc = "<strong> √ " + name + " 于 " + df.format(new Date())
					+ " 确认了 " + name1 + " 提交的工作。</strong><br/><br/>";
		}
		if (myLog != null && !myLog.equals("")) {
			talog = tadesc + myLog;
		} else {
			talog = tadesc;
		}
		taLim.setTaDesc(talog);
		taLim.setTaIsdel("3");//结束状态
		salTaskBiz.updateTaskMan(taLim);

		request.setAttribute("msg", "确认工作提交");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到工作安排的修改页面 <br>
	 * create_date: Aug 20, 2010,5:17:28 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID
	 * @param response
	 * @return ActionForward 跳转到updateSalTask页面<br>
	 * 		attribute > orgList:执行人列表 salTaskType:工作类别列表 salTask:工作安排实体
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		List list = typeListBiz.getEnabledType("22");

		request.setAttribute("salTask", salTask);
		request.setAttribute("salTaskType", list);
		return mapping.findForward("updateSalTask");
	}

	/**
	 * 编辑工作安排 <br>
	 * create_date: Aug 20, 2010,5:21:21 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID taskType:工作类别ID satFinDate:开始期限 satFinDate:结束期限 nodeIds:执行人id组合字符串
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:编辑成功信息提示
	 */
	public ActionForward updateSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalTask salTask = (SalTask) form1.get("salTask");
		Long stId = Long.parseLong(request.getParameter("stId"));
		String taskType = request.getParameter("taskType");
		String userIds = request.getParameter("nodeIds");//执行人
		String satStartDate = request.getParameter("stStartDate");
		String satFinDate = request.getParameter("stFinDate");
		LimUser user = (LimUser) request.getSession().getAttribute("limUser");
		TypeList salTaskType = new TypeList();
		Date date = new Date();
		Date inpDate = new java.sql.Date(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SalTask salTask1 = salTaskBiz.salTaskDesc(stId);
		String[] usersArray = null;

		if (taskType != null && !taskType.equals("")) {
			Long sttId = Long.parseLong(taskType);
			salTaskType.setTypId(sttId);
			salTask1.setSalTaskType(salTaskType);
		} else {
			salTask1.setSalTaskType(null);
		}
		salTask1.setStLev(salTask.getStLev());
		salTask1.setStRemark(salTask.getStRemark());
		salTask1.setStStu(salTask.getStStu());
		salTask1.setStTag(salTask.getStTag());
		salTask1.setStTitle(salTask.getStTitle());
		salTask1.setStUpdUser(user.getUserSeName());
		if (!satStartDate.equals("")) {
			try {
				Date startDate = dateFormat.parse(satStartDate);
				salTask1.setStStartDate(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			salTask1.setStStartDate(null);
		}
		if (!satFinDate.equals("")) {
			try {
				Date finDate = dateFormat.parse(satFinDate);
				salTask1.setStFinDate(finDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			salTask1.setStFinDate(null);
		}
		salTask1.setStChangeDate(inpDate);
		salTaskBiz.updateSalTask(salTask1);

		//批量更新该工作的接收人删除状态全部为0
		salTaskBiz.updateByTaskId(stId);
		//更新执行人
		usersArray = userIds.split(",");
		List list = salTaskBiz.findAllTaskMan(stId);//得到该工作所有执行人
		for (int i = 0; i < usersArray.length; i++) {
			if (usersArray[i] != null && !usersArray[i].equals("")) {
				Long userSeNo = Long.parseLong(usersArray[i]);
				boolean flag = false;
				if (list.size() > 0) {
					for (int g = 0; g < list.size(); g++) {
						TaLim taLim1 = (TaLim) list.get(g);
						if (taLim1.getSalEmp().getSeNo().equals(userSeNo)) {//相同的人
							taLim1.setTaIsfin("1");
							salTaskBiz.updateTaskMan(taLim1);
							flag = true;
							break;
						}
					}
					if (!flag) {
						TaLim taLim = new TaLim();
						taLim.setSalTask(salTask1);
						taLim.setTaIsdel("0");
						taLim.setTaIsfin("1");
						SalEmp emp = empBiz.findById(userSeNo);
						taLim.setSalEmp(emp);
						taLim.setTaName(emp.getSeName());
						salTaskBiz.saveTal(taLim);
					}
				}
			}
		}
		request.setAttribute("msg", "修改工作安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除工作 <br>
	 * create_date: Aug 20, 2010,5:26:14 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > stId:工作安排ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:删除成功信息提示
	 */
	public ActionForward delSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long stId = Long.parseLong(request.getParameter("stId"));
		SalTask salTask = salTaskBiz.salTaskDesc(stId);
		salTask.setStIsdel("0");
		salTaskBiz.updateSalTask(salTask);

		request.setAttribute("msg", "删除工作安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 恢复工作安排 <br>
	 * create_date: Aug 20, 2010,5:34:51 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > id:工作安排ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:恢复成功信息提示
	 */
	public ActionForward recSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalTask salTask = salTaskBiz.salTaskDesc(id);
		salTask.setStIsdel("1");
		salTaskBiz.updateSalTask(salTask);

		request.setAttribute("msg", "恢复工作安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除工作安排不能再恢复 <br>
	 * create_date: Aug 20, 2010,5:36:08 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > id:工作安排ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:彻底删除成功信息提示
	 */
	public ActionForward deleteSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalTask salTask = salTaskBiz.salTaskDesc(id);
		Iterator it = salTask.getAttachments().iterator();
		//删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		//删除工作执行人的附件
		Iterator it2 = salTask.getTaLims().iterator();
		while (it2.hasNext()) {
			TaLim talim = (TaLim) it2.next();
			Iterator it3 = talim.getAttachments().iterator();
			while (it3.hasNext()) {
				Attachment att2 = (Attachment) it3.next();
				FileOperator.delFile(att2.getAttPath(), request);
			}
		}

		salTaskBiz.delete(salTask);
		request.setAttribute("msg", "删除工作安排");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到待删除的工作安排列表  <br>
	 * create_date: Mar 25, 2011,9:18:41 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward recSalTask <br>
	 */
	public ActionForward toListDelSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recSalTask");
	}
	
	/**
	 * 获得待删除的工作安排列表 <br>
	 * create_date: Aug 20, 2010,5:37:17 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recSalTask页面<br>
	 * 		attribute > salTask:符合条件的工作安排列表 page:分页信息
	 */
	public void findDelSalTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(salTaskBiz.findDelSalTaskCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = salTaskBiz.findDelSalTask(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("salTaskType");
		
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
		/*request.setAttribute("salTask", list);
		request.setAttribute("page", page);
		return mapping.findForward("recSalTask");*/

	}

	/**
	 * 注入userBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param userBiz 
	 */
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}

	/**
	 * 注入salTaskBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param salTaskBiz 
	 */
	public void setSalTaskBiz(SalTaskBIZ salTaskBiz) {
		this.salTaskBiz = salTaskBiz;
	}

	/**
	 * 注入empBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param empBiz 
	 */
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}

	/**
	 * 注入modType <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param modType 
	 */
	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}

	/**
	 * 注入typeListBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param typeListBiz 
	 */
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
}