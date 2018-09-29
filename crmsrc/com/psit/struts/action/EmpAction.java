package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.LimRole;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.CodeCreator;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.TransStr;

/**
 * 员工资料管理，部门管理 <br>
 */
public class EmpAction extends BaseDispatchAction {
	EmpBIZ empBiz = null;
	UserBIZ userBiz = null;
	private static final String ORGTOPCODE = "O20100000-1";

	
	/**
	 * 判断用户是否有员工资料管理，部门管理的权限 <br>
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
			// request.setAttribute("errorMsg", "对不起，您没有该操作权限");
			return mapping.findForward("limError");
		}
	}

	/**
	 * 检测用户是否有员工资料，部门管理操作的权限码 <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "toListEmps", "ah005" },// 员工档案是否可见
				{ "toNewSalEmp", "h001" },{"newSalEmp","h001"},// 新建员工资料
				{ "delSalEmp", "h002" },// 删除员工资料
				{ "toUpdEmp", "h003" },{"updEmp","h003"},// 修改员工资料
				{ "salEmpDesc", "h004" },// 查看员工资料详情
				{"findForwardOrg","asy011"},//部门设置是否可见
				{ "saveSalOrg", "sy007" },{"newSalOrg","sy007"},// 添加部门
				{ "updateSalOrg", "sy008" },// 修改部门信息
				{ "delSalOrg", "sy009" },// 删除部门
				{ "salOrgDesc", "sy010" },// 查看部门详情
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}
	
	/**
	 * 跳转到员工资料列表 <br>
	 * @param request
	 * 		para > 	workstate:是否离职;
	 * @return ActionForward 跳转到listEmp页面<br>
	 * 		attr > 	workstate:是否离职;
	 */
	public ActionForward toListEmps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workState = request.getParameter("workstate");
		request.setAttribute("workstate", workState);
		return mapping.findForward("listEmp");
	}
	
	/**
	 * 员工资料列表 <br>
	 * @param request
	 * 		para > 	workstate:是否离职; seCode:员工号; seName:姓名; 
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序;
	 */
	public void listEmps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workState = request.getParameter("workstate");
		String p = request.getParameter("p");
		String seCode = request.getParameter("seCode");
		String seName = request.getParameter("seName");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		Page page;
		List list;
		String[] items = {"seCode", "seName", "seSex", "seEdu","seJobDate","seEndDate","rolName","seType", "soName"};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		if (orderCol != null && !orderCol.equals("")) {
			if(workState.equals("0")){
				switch(Integer.parseInt(orderCol)){
				  	case 0:
				  	case 1:
				  	case 2:
				  	case 3:
				  	case 4:
				  		   orderItem = items[Integer.parseInt(orderCol)];
				  		   break;
				  	case 5:
				  	case 6:
				  	case 7:
					  		orderItem = items[Integer.parseInt(orderCol)+1];
					  		break;
				}
			}else{
				orderItem = items[Integer.parseInt(orderCol)];
			}
		}
		page = new Page(empBiz.getCountEmp(new String[]{seCode, seName,workState}), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		list = empBiz.salEmpSerach(new String[]{seCode, seName,workState},orderItem,isDe,
				page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("limRole");
		awareCollect.add("salOrg");
		
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
	 * 得到公司logo路径(ajax) <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward null 输出公司logo路径<br>
	 */
	public ActionForward getTopOrgLogo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		SalOrg comOrg = empBiz.salOrgDesc(ORGTOPCODE);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(comOrg.getSoRemark());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 清除企业logo <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到simplePicUpload页面<br>
	 *         attribute > msg:显示一成功恢复为默认图片 path:旧的图片路径 id:顶级部门ID
	 */
	public ActionForward clearTopOrgLogo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		SalOrg comOrg = empBiz.salOrgDesc(ORGTOPCODE);
		if (comOrg.getSoRemark() != null && !comOrg.getSoRemark().equals("")) {
			FileOperator.delFile(comOrg.getSoRemark(), request);
			comOrg.setSoRemark(null);
			empBiz.updateSalOrg(comOrg);
		}

		request.setAttribute("msg", "clear");
		request.setAttribute("path", null);
		request.setAttribute("id", ORGTOPCODE);
		return mapping.findForward("simplePicUpload");
	}

	/**
	 * 组织列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到showOrg页面<br>
	 */
	public ActionForward findForwardOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showOrg");
	}

	/**
	 * 新建组织 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > upOrg:上级部门主键
	 * @param response
	 * @return ActionForward 跳转到newSalOrg页面<br>
	 *         attribute > upOrg:上级部门主键 orgList:去除顶级部门所有部门列表
	 */
	public ActionForward newSalOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String upOrg = request.getParameter("upOrg");
		// 获得所有部门去除顶级部门
		List list = empBiz.getAllOrg();
		request.setAttribute("orgList", list);
		if (upOrg != null) {
			request.setAttribute("upOrg", upOrg);
		}
		return mapping.findForward("newSalOrg");
	}

	/**
	 * 保存部门 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > addSoCode:上级部门主键
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:保存成功提示信息
	 */
	public ActionForward saveSalOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SalOrg salOrg = (SalOrg) form1.get("salOrg");
		String upSoCode = request.getParameter("addSoCode");
		if (upSoCode != null && !upSoCode.equals(""))
			salOrg.setSalOrg(new SalOrg(upSoCode));
		else
			salOrg.setSalOrg(new SalOrg(ORGTOPCODE));

		salOrg.setSoIsenabled("1");
		CodeCreator codeCreator = new CodeCreator();
		String prefix = "O" + dateFormat.format(date);
		String soCode = codeCreator.createCode(prefix, "sal_org", 0);
		salOrg.setSoCode(soCode);
		List list = empBiz.getAllOrg();
		request.setAttribute("orgList", list);
		empBiz.save(salOrg);
		request.setAttribute("msg", "添加部门");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 判断编号是否重复 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > orgCode:部门编号 divCount:错误提示层序号
	 * @param response
	 * @return ActionForward null 编号重复输出1，不重复输出0(如果有多个提示层会加上层序号)
	 */
	public ActionForward checkOrgCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String orgCode = TransStr.transStr(request.getParameter("orgCode"));
		String divCount = request.getParameter("divCount");//错误提示层序号
		PrintWriter out = null;
		String isRep = "";
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (empBiz.checkSoCode(orgCode)) {
				isRep = "1";
			} else {
				isRep = "0";
			}
			if(divCount!=null&&!divCount.equals("")){
				isRep += "," + divCount;//如果有多个错误提示层，out返回层序号
			}
			out.print(isRep);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 判断重复名称 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > orgName:部门名称 divCount:错误提示层序号
	 * @param response
	 * @return ActionForward null 名称重复输出1，不重复输出0(如果有多个提示层会加上层序号)
	 */
	public ActionForward checkOrgName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String orgName = "";
		orgName = TransStr.transStr(request.getParameter("orgName"));
		String divCount = request.getParameter("divCount");//错误提示层序号
		PrintWriter out = null;
		String isRep = "";
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (empBiz.checkSoName(orgName)) {
				isRep = "1";
			} else {
				isRep = "0";
			}
			if(divCount!=null&&!divCount.equals("")){
				isRep += "," + divCount;//如果有多个错误提示层，out返回层序号
			}
			out.print(isRep);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 判断部门的上级是否为自己的下级 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > oldSoCode:部门主键 modUpSoCode:上级部门主键 divCount:错误提示层序号
	 * @param response
	 * @return ActionForward null 部门的上级是自己的下级输出1，不是输出0(如果有多个提示层会加上层序号)
	 */
	public ActionForward checkUpOrgCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String divCount = request.getParameter("divCount");//错误提示层序号
		String soCode = TransStr.transStr(request.getParameter("oldSoCode"));
		String upSoCode = TransStr
				.transStr(request.getParameter("modUpSoCode"));
		PrintWriter out = null;
		String isErr = "";
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (isLowLevelOrg(soCode, upSoCode)) {
				isErr = "1";
			} else {
				isErr = "0";
			}
			if(divCount!=null&&!divCount.equals("")){
				isErr += "," + divCount;//如果有多个错误提示层，out返回层序号
			}
			out.print(isErr);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 获得所有部门或不含顶级部门的所有部门 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > mark:页面跳转标识
	 * @param response
	 * @return ActionForward 若mark的值为orgTree跳转到orgTree页面 值为orgList跳转到orgList页面
	 *         值为orgStruts跳转到orgStruts页面<br>
	 *         attribute > mark的值为orgTree orgTopCode:顶级部门主键 orgList:所有部门<br>
	 *         attribute > mark的值为orgList orgTopCode:顶级部门主键 comOrg:顶级部门
	 *         orgList:所有部门不含顶级部门<br>
	 *         attribute > mark的值为orgStruts orgTopCode:顶级部门主键 orgList:所有部门<br>
	 */
	public ActionForward getAllOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = empBiz.getAllOrg();// 所有部门不含顶级部门
		List list1 = empBiz.findAllSalOrg();// 所有部门
		String mark = request.getParameter("mark");

		request.setAttribute("orgTopCode", ORGTOPCODE);
		if (mark != null && mark.equals("orgTree")) {
			request.setAttribute("orgList", list1);
			return mapping.findForward("orgTree");
		}
		if (mark != null && mark.equals("orgList")) {
			SalOrg comOrg = empBiz.salOrgDesc(ORGTOPCODE);
			request.setAttribute("comOrg", comOrg);
			request.setAttribute("orgList", list);
			return mapping.findForward("orgList");
		}
		if (mark != null && mark.equals("orgStruts")) {
			request.setAttribute("orgList", list1);
			return mapping.findForward("orgStruts");
		}

		return null;
	}
	/**
	 * 查看部门详细信息 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > soCode:部门主键
	 * @param response
	 * @return ActionForward 跳转到orgList页面<br>
	 *         attribute > salOrg:部门实体 orgList:除当前部门以外的所有部门列表
	 *         showButton:控制公司信息与部门信息层的显示
	 */
	public ActionForward salOrgDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String soCode = request.getParameter("soCode");
		SalOrg salOrg = empBiz.salOrgDesc(soCode);
		request.setAttribute("salOrg", salOrg);
		List list = empBiz.findPartSalOrg(soCode);
		// ArrayList<SalOrg>parentList=new ArrayList<SalOrg>();
		// List list=getParentOrg(salOrg,list1,parentList);
		request.setAttribute("orgList", list);
		request.setAttribute("showButton", "1");
		// return mapping.findForward("salOrgDesc");
		return mapping.findForward("orgList");
	}

	/**
	 * 获得某部门的所有下级部门 <br>
	 * @param salOrg 特定部门
	 * @param lowLevelList 特定部门的所有下级部门
	 * @return List<SalOrg> 特定部门的所有下级部门<br>
	 */
	public List<SalOrg> getLowOrg(SalOrg salOrg, ArrayList<SalOrg> lowLevelList) {
		if (salOrg.getSalOrgs().size() == 0) {
			return lowLevelList;
		} else {
			for (Iterator<SalOrg> it = salOrg.getSalOrgs().iterator(); it
					.hasNext();) {
				SalOrg salOrg1 = (SalOrg) it.next();
				lowLevelList.add(salOrg1);
				getLowOrg(salOrg1, lowLevelList);

			}
		}
		return lowLevelList;
	}

	/**
	 * 判断一个部门是否是另一个部门的下级<br>
	 * @param soCode 部门主键
	 * @param upSoCode 部门主键
	 * @return boolean 若一个部门是另一个部门的下级返回true否则返回false<br>
	 */
	public boolean isLowLevelOrg(String soCode, String upSoCode) {
		SalOrg salOrg = empBiz.salOrgDesc(soCode);
		// List<SalOrg> list = empBiz.findPartSalOrg(soCode);
		ArrayList<SalOrg> arryList = new ArrayList<SalOrg>();
		List<SalOrg> lowLevelList = getLowOrg(salOrg, arryList);
		if (lowLevelList.size() == 0)
			return false;
		else {
			Iterator<SalOrg> it = lowLevelList.iterator();
			while(it.hasNext()) {
				SalOrg salOrg1 = (SalOrg) it.next();
				if (salOrg1.getSoCode().equals(upSoCode)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 修改部门 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > oldSoCode:部门主键 modUpSoCode:上级部门主键
	 * @param response
	 * @return ActionForward 跳转到orgList页面<br>
	 *         attribute > orgList:所有部门列表 orgTopCode:顶级部门主键 comOrg:顶级部门实体
	 */
	public ActionForward updateSalOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalOrg salOrg1 = null;
		String soCode = request.getParameter("oldSoCode");
		SalOrg salOrg = empBiz.salOrgDesc(soCode);
		String upSoCode = request.getParameter("modUpSoCode");
		if (soCode.equals(ORGTOPCODE)) {
			salOrg1 = (SalOrg) form1.get("comOrg");
			salOrg.setSalOrg(null);
			salOrg.setSoName(salOrg1.getSoName());
		} else {
			salOrg1 = (SalOrg) form1.get("salOrg");
			if (upSoCode != null && !upSoCode.equals("")) {
				salOrg.setSalOrg(new SalOrg(upSoCode));
			} else {
				salOrg.setSalOrg(new SalOrg(ORGTOPCODE));
			}
		}

		salOrg.setSoConArea(salOrg1.getSoConArea());
		salOrg.setSoEmpNum(salOrg1.getSoEmpNum());
		salOrg.setSoLoc(salOrg1.getSoLoc());
		salOrg.setSoRemark(salOrg1.getSoRemark());
		salOrg.setSoResp(salOrg1.getSoResp());
		salOrg.setSoUserCode(salOrg1.getSoUserCode());
		salOrg.setSoCostCenter(salOrg1.getSoCostCenter());
		salOrg.setSoOrgNature(salOrg1.getSoOrgNature());
		// if(!soCode.equals(ORGTOPCODE)&&!salOrg.getSoOrgCode().equals(salOrg1.getSoOrgCode()))
		// {
		// if(empBiz.checkSoCode(salOrg1.getSoOrgCode()))
		//			
		// {
		// request.setAttribute("msg", "该部门编号已存在，请重新修改!");
		// salOrg1.setSoCode(soCode);
		// salOrg1.setSalOrg(new SalOrg(upSoCode));
		// request.setAttribute("salOrg", salOrg1);
		// request.setAttribute("isMod", "1");
		//					 
		// SalOrg comOrg=empBiz.salOrgDesc(ORGTOPCODE);//获得公司信息
		// request.setAttribute("comOrg", comOrg);
		// return mapping.findForward("orgList");
		// }
		// else
		// {
		// salOrg.setSoOrgCode(salOrg1.getSoOrgCode());

		// }
		// }
		// if(!soCode.equals(ORGTOPCODE)&&!salOrg.getSoName().equals(salOrg1.getSoName()))
		// {
		// if(empBiz.checkSoName(salOrg1.getSoName()))
		// {
		// request.setAttribute("msg", "该部门名称已存在，请重新修改!");
		// salOrg1.setSoCode(soCode);
		// request.setAttribute("salOrg", salOrg1);
		// salOrg1.setSalOrg(new SalOrg(upSoCode));
		// request.setAttribute("isMod", "1");
		//					 
		// SalOrg comOrg=empBiz.salOrgDesc(ORGTOPCODE);//获得公司信息
		// request.setAttribute("comOrg", comOrg);
		// return mapping.findForward("orgList");
		// }
		// else
		// salOrg.setSoName(salOrg1.getSoName());
		// }
		salOrg.setSoOrgCode(salOrg1.getSoOrgCode());
		salOrg.setSoName(salOrg1.getSoName());
		request.setAttribute("msg", "修改成功！");
		empBiz.updateSalOrg(salOrg);

		List list = empBiz.getAllOrg();// 获得所有部门
		request.setAttribute("orgList", list);
		SalOrg comOrg = empBiz.salOrgDesc(ORGTOPCODE);// 获得公司信息
		request.setAttribute("orgTopCode", ORGTOPCODE);
		request.setAttribute("comOrg", comOrg);
		return mapping.findForward("orgList");
	}

	/**
	 * 删除部门 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > soCode:部门主键
	 * @param response
	 * @return ActionForward 跳转到orgList页面<br>
	 *         attribute > 要删除的部门为顶级部门 isDel:表示删除不成功 msg:顶级部门不让删除的提示信息
	 *         salOrg:顶级部门实体<br>
	 *         attribute > 要删除的部门存在下级部门 isDel:表示删除不成功 msg:存在下级部门不让删除的提示信息
	 *         salOrg:部门实体<br>
	 *         attribute > 要删除的部门存在员工 isDel:表示删除不成功 msg:部门存在员工不让删除的提示信息
	 *         salOrg:部门实体<br>
	 */
	public ActionForward delSalOrg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String soCode = request.getParameter("soCode");
		SalOrg salOrg = empBiz.salOrgDesc(soCode);
		salOrg.setSoCode(soCode);
		List<LimUser> list = userBiz.listByOrgId(soCode);// 判断用户表是否有和该组织关联的员工
		List list1 = empBiz.getSalOrgByCode(soCode);// 判断员工表里是否有和该组织关联的员工
		List list2 = empBiz.getAllOrg();// 获得所有组织
		request.setAttribute("orgList", list2);
		if (soCode != null && soCode.equals(ORGTOPCODE)) {
			request.setAttribute("isDel", "1");
			request.setAttribute("msg", "顶级部门不允许删除！");
			request.setAttribute("salOrg", salOrg);
			return mapping.findForward("orgList");
		}
		if (empBiz.isExistLowOrg(soCode)) {
			request.setAttribute("isDel", "1");
			request.setAttribute("msg", "该部门存在下级部门，先删除它的所有下级部门");
			request.setAttribute("salOrg", salOrg);
			return mapping.findForward("orgList");
		} else {
			if (list1.size() > 0 || list.size() > 0) {
				request.setAttribute("isDel", "1");
				request.setAttribute("msg", "请先转移该部门下的员工（包括移入回收站中的员工）");
				request.setAttribute("salOrg", salOrg);
				return mapping.findForward("orgList");
			} else {
				empBiz.delete(salOrg);
			}
		}
		request.setAttribute("msg", "删除部门成功！");
		return mapping.findForward("orgList");
	}
	/**
	 * 快速保存员工资料 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > soCode:部门主键 seJobLev:职位
	 * @param response
	 * @return ActionForward 跳转到popDivSuc<br>
	 *         attribute > msg:保存成功信息提示
	 */
	public ActionForward quickSaveSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalEmp salEmp = (SalEmp) form1.get("salEmp");
		String wIdPre = request.getParameter("wIdPre");
		String soCode = request.getParameter("soCode");
		String seJobLev = request.getParameter("seJobLev");
		Date date1 = new Date(new java.util.Date().getTime());
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		
		salEmp.setSeIsenabled("1");
		if (soCode != null && !soCode.equals("")) {
			salEmp.setSalOrg(new SalOrg(soCode));
		} else {
			salEmp.setSalOrg(null);
		}
		if (seJobLev != null && !seJobLev.equals("")) {
			salEmp.setLimRole(new LimRole(Long.parseLong(seJobLev)));
		} else {
			salEmp.setLimRole(null);
		}
		salEmp.setSeInserUser(limUser.getUserSeName());
		salEmp.setSeInserDate(date1);
		empBiz.save(salEmp);
		request.setAttribute("wIdPre", wIdPre);
		request.setAttribute("redir", "noRf");
		request.setAttribute("msg", "员工信息保存");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 保存员工资料 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > soCode:部门主键 seJobLev:职位 jobDate:入职日期endDate:离职日期
	 *            bacGroundCode:教育背景
	 * @param response
	 * @return ActionForward 跳转到newSalEmp<br>
	 *         attribute > msg:保存成功信息提示
	 */
	public ActionForward newSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String soCode = request.getParameter("soCode");
		String seJobLev = request.getParameter("seJobLev");
		String jobDate = request.getParameter("jobDate");
		String endDate = request.getParameter("endDate");
//		String[] bacGroundCode = request.getParameterValues("bacGroundCode");// 不包括页面上已有的一条，循环需加1
		DynaActionForm form1 = (DynaActionForm) form;
		SalEmp salEmp = (SalEmp) form1.get("salEmp");
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		Date date1 = new Date(new java.util.Date().getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int backNum = 0;

		salEmp.setSeIsenabled("1");
		if (soCode != null && !soCode.equals("")) {
			salEmp.setSalOrg(new SalOrg(soCode));
		} else {
			salEmp.setSalOrg(null);
		}
		if (seJobLev != null && !seJobLev.equals("")) {
			salEmp.setLimRole(new LimRole(Long.parseLong(seJobLev)));
		} else {
			salEmp.setLimRole(null);
		}
		try {
			if (!jobDate.equals("")) {
				salEmp.setSeJobDate((java.util.Date) dateFormat.parse(jobDate));
			} else {
				salEmp.setSeJobDate(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (!endDate.equals("")) {
				salEmp.setSeEndDate((java.util.Date) dateFormat.parse(endDate));
			} else {
				salEmp.setSeEndDate(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		salEmp.setSeInserUser(limUser.getUserSeName());
		salEmp.setSeInserDate(date1);
		salEmp.setSeAltDate(null);// 修改日期
		salEmp.setSeAltUser(null);
		empBiz.save(salEmp);

		request.setAttribute("msg", "员工信息保存成功！");
		return mapping.findForward("newSalEmp");
	}

	/**
	 * 检查员工号是否已经存在 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > seCode:员工号
	 * @param response
	 * @return ActionForward null 重复输出1 不重复输出0<br>
	 */
	public ActionForward checkSecode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String seCode = TransStr.transStr(request.getParameter("seCode"));
		// String isEdit=request.getParameter("isEdit");
		List salEmp1 = empBiz.getEmpByCode(seCode);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (salEmp1.size() > 0) {
				out.print("1");
			} else {
				out.print("0");
			}
			out.flush();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 判断员工是否已经分配了账号 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > seNo:员工ID divCount:错误提示层序号
	 * @param response
	 * @return ActionForward null 已经分配了账号输出1未分配输出0(如果有多个提示层会加上层序号)
	 */
	public ActionForward checkIsUseEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seNo =request.getParameter("seNo");
		String divCount = request.getParameter("divCount");//错误提示层序号
		PrintWriter out = null;
		String isErr = "";
		
		SalEmp salEmp = empBiz.findById(Long.parseLong(seNo));
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if(salEmp.getSeUserCode()!=null){
				isErr = "1";
			} else {
				isErr = "0";
			}
			if(divCount!=null&&!divCount.equals("")){
				isErr += "," + divCount;//如果有多个错误提示层，out返回层序号
			}
			out.print(isErr);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	/**
	 * 生成员工树 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > type:判断树连接跳转 
	 * @param response
	 * @return ActionForward empTree<br>
	 *         attribute > orgList:部门列表 orgTopCode:部门顶级号（公司编号）
	 */
	public ActionForward salEmpTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");

		List<SalOrg> orgList = empBiz.findAllSalOrg();//不含顶级部门
		List<SalEmp> salEmps = null;
		salEmps = empBiz.getEmpList();

		request.setAttribute("salEmps", salEmps);
		request.setAttribute("orgList", orgList);
		request.setAttribute("type", type);
		request.setAttribute("orgTopCode", ORGTOPCODE);
		return mapping.findForward("empTree");
	}

	public ActionForward toShowAddBook(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("empContacts");
	}
	
	/**
	 * 查看公司通讯录 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > p:当前页码  seName:员工姓名 orderItem:排序字段 isDe：是否逆序 pageSize:每页显示记录数
	 *            			  
	 * @param response
	 * @return ActionForward 跳转到empContacts页面<br>
	 *         attribute > salEmp:符合条件的员工资料 page:分页信息  orderItem:排序字段 isDe：是否逆序
	 */
	public void showAddBook(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seName = request.getParameter("seName");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String [] items = { "seName", "seSex","soName","rolName","sePhone","seTel","qq","msn","email"};
		String [] args = {null,seName,"0"};
		
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";//
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";
		}
		
		Page page = new Page(empBiz.getCountEmp(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list1 = empBiz.salEmpSerach(args,orderItem,isDe, page
				.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list1);
		List awareCollect = new LinkedList();
		awareCollect.add("limRole");
		awareCollect.add("salOrg");
		
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
	 * 进入新建人员页面 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > toQuickAdd:值为1表示新建账号时跳转到快速新建员工档案页面
	 * @param response
	 * @return ActionForward toQuickAdd为1跳转到quickAddEmp页面，否则跳转到newSalEmp页面<br>
	 *         attribute > salOrg:所有部门不含顶级部门 roleList:职位列表
	 */
	public ActionForward toNewSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String mark=request.getParameter("toQuickAdd");
		
		List list = empBiz.getAllOrg();
		List list1 = empBiz.getAllRole();
		
		request.setAttribute("roleList", list1);
		request.setAttribute("salOrg", list);
		if(mark!=null&&mark.equals("1")){
			return mapping.findForward("quickAddEmp");//新建账号时跳转
		}
		else{
			return mapping.findForward("newSalEmp");//正常录入员工档案页面
		}
		
	}

	/**
	 * 查看销售人员详细信息 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > seNo:员工实体主键
	 * @param response
	 * @return ActionForward 跳转到salEmpDesc页面<br>
	 *         attribute > seNo:员工实体主键 salEmp:员工实体
	 */
	public ActionForward salEmpDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seNo = request.getParameter("seNo");
		SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));

		request.setAttribute("seNo", seNo);
		request.setAttribute("salEmp", salEmp);
		return mapping.findForward("salEmpDesc");
	}

	/**
	 * 跳转到修改员工资料信息页面 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > seNo:员工实体主键
	 * @param response
	 * @return ActionForward 跳转到updateSalEmp页面<br>
	 *         attribute > seNo:员工实体主键 roleList:职位列表 salOrg:出去顶级部门的所有部门列表
	 *         salEmp:员工实体
	 */
	public ActionForward toUpdEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seNo = request.getParameter("seNo");
		SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));
		List list = empBiz.getAllOrg();
		List list1 = empBiz.getAllRole();
		request.setAttribute("roleList", list1);
		request.setAttribute("seNo", seNo);
		request.setAttribute("salOrg", list);
		request.setAttribute("salEmp", salEmp);
		return mapping.findForward("updateSalEmp");
	}

	/**
	 * 修改员工信息 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > seNo:员工实体主键 soCode:部门主键 seJobLev:职位 jobDate:入职日期
	 * @param response
	 * @return ActionForward 跳转到updateSalEmp页面<br>
	 *         attribute > salEmp:员工实体 msg:修改成功信息提示
	 */
	public ActionForward updEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalEmp salEmp1 = (SalEmp) form1.get("salEmp");
		String seNo = request.getParameter("seNo");
		SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));
		salEmp1.setSeNo(Long.valueOf(seNo));
		salEmp1.setSeInserDate(salEmp.getSeInserDate());
		salEmp1.setSeInserUser(salEmp.getSeInserUser());
		salEmp1.setSeUserCode(salEmp.getSeUserCode());
		salEmp1.setSePic(salEmp.getSePic());
		salEmp1.setSeLog(salEmp.getSeLog());
		String soCode = request.getParameter("soCode");
		if (soCode != null && !soCode.equals("")) {
			salEmp1.setSalOrg(new SalOrg(soCode));
		} else {
			salEmp1.setSalOrg(null);
		}
		String seJobLev = request.getParameter("seJobLev");
		if (seJobLev != null && !seJobLev.equals("")) {

			salEmp1.setLimRole(new LimRole(Long.parseLong(seJobLev)));
		} else {
			salEmp1.setLimRole(null);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!request.getParameter("jobDate").equals("")) {
				java.util.Date jobDate = dateFormat.parse(request
						.getParameter("jobDate"));
				salEmp1.setSeJobDate(jobDate);
			} else {
				salEmp1.setSeJobDate(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (!request.getParameter("endDate").equals("")) {
				java.util.Date endDate = dateFormat.parse(request
						.getParameter("endDate"));
				salEmp1.setSeEndDate(endDate);
			} else {
				salEmp1.setSeEndDate(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String seName = salEmp1.getSeName();
		String oldSeName=salEmp.getSeName();
		String oldSoCode=salEmp.getSalOrg().getSoCode();
		String oldSeJobLev=salEmp.getLimRole().getRolId().toString();
		if (!seName.equals(oldSeName)||!soCode.equals(oldSoCode)||!seJobLev.equals(oldSeJobLev)) {
			if(salEmp.getSeUserCode()!=null && !salEmp.getSeUserCode().equals("")){
				LimUser user = userBiz.findById(salEmp.getSeUserCode());
				if(user!=null){
					user.setUserSeName(seName);
					user.setSalOrg(new SalOrg(soCode));
					user.setLimRole(salEmp1.getLimRole());
					empBiz.updateUser(user);
				}
			}
		}
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		Date date1 = new Date(new java.util.Date().getTime());
		salEmp1.setSeAltDate(date1);// 修改日期
		salEmp1.setSeAltUser(limUser.getUserSeName());
		salEmp1.setSeIsenabled("1");
		empBiz.updateSalEmp(salEmp1);
		request.setAttribute("salEmp", salEmp1);
		request.setAttribute("msg", "员工资料修改成功！");
		return mapping.findForward("updateSalEmp");
	}

	/**
	 * 删除员工资料 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > seNo:员工实体主键
	 * @param response
	 * @return ActionForward 不符合删除条件跳转到error页面 删除成功跳转到popDivSuc页面<br>
	 *         attribute > 不符合删除条件 errorMsg:不符删除条件的信息提示<br>
	 *         attribute > 删除成功 msg:删除成功的信息提示<br>
	 */
	public ActionForward delSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seNo = request.getParameter("seNo");
		boolean isRel = false;
		SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));
		List<LimUser> list = userBiz.listByEmpId(seNo);
		Iterator cusIt = salEmp.getCusServs().iterator();
		Iterator mesIt = salEmp.getMessages().iterator();
	    Iterator newIt = salEmp.getNews().iterator();
	    Iterator quoIt = salEmp.getQuotes().iterator();
	    Iterator repIt = salEmp.getReports().iterator();
	    Iterator rMesIt = salEmp.getRmessLims().iterator();
	    Iterator rNewIt = salEmp.getRnewLims().iterator();
	    Iterator rrLimIt = salEmp.getRrepLims().iterator();
	    Iterator salOrdIt = salEmp.getSalOrdCons().iterator();
	    Iterator salPaidIt = salEmp.getSalPaidPasts().iterator();
	    Iterator salPraIt = salEmp.getSalPras().iterator();
	    Iterator salTIt = salEmp.getSalTasks().iterator();
	    Iterator schIt = salEmp.getSchedules().iterator();
	    Iterator taLimIt = salEmp.getTaLims().iterator();
	    Iterator cusCorCusIt = salEmp.getCusCorCus().iterator();
	    Iterator cusContactsIt = salEmp.getCusContacts().iterator();
		Iterator salOppsIt = salEmp.getSalOpps().iterator();
	    Iterator salPaidPlansIt = salEmp.getSalPaidPlans().iterator();
		
	    while(!isRel && cusIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && mesIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && newIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && quoIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && repIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && rMesIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && rNewIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && rrLimIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && salOrdIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && salPaidIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && salPraIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && salTIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && schIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && taLimIt.hasNext()){
			isRel = true;
		}
	    while(!isRel && cusCorCusIt.hasNext()){
	    	isRel = true;
	    }
	    while(!isRel && cusContactsIt.hasNext()){
	    	isRel = true;
	    }
	    while(!isRel && salOppsIt.hasNext()){
	    	isRel = true;
	    }
	    while(!isRel && salPaidPlansIt.hasNext()){
	    	isRel = true;
	    }
		if (list.size() > 0 || isRel) {
			request.setAttribute("errorMsg", "不能删除正在使用中的员工");
			return mapping.findForward("error");
		}
		else{
			salEmp.setSeIsenabled("0");
			empBiz.updateSalEmp(salEmp);
			request.setAttribute("msg", "删除员工");
			
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 恢复员工档案 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > id:员工实体主键
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:恢复成功信息提示
	 */
	public ActionForward recSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalEmp salEmp = empBiz.findById(id);
		salEmp.setSeIsenabled("1");
		empBiz.updateSalEmp(salEmp);
		request.setAttribute("msg", "恢复员工档案");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除员工档案不能再恢复 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *            parameter > id:员工实体主键
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:彻底删除成功的信息提示
	 */
	public ActionForward deleteSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		SalEmp salEmp = empBiz.findById(id);
		// 删除关联图片
		if(salEmp.getSePic()!=null && !salEmp.getSePic().equals("")){
			FileOperator.delFile(salEmp.getSePic(), request);
		}
		Iterator it = salEmp.getAttachments().iterator();
		// 删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		empBiz.delete(salEmp);
		
		request.setAttribute("msg", "删除员工档案");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到待删除的员工资料 <br>
	 * create_date: Mar 25, 2011,9:55:36 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward recSalEmp<br>
	 */
	public ActionForward toListDelSalEmp(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recSalEmp");
	}
	/**
	 * 获得待删除的员工资料 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recSalEmp页面<br>
	 *         attribute > salEmp:符合条件的员工列表 page:分页信息
	 */
	public void findDelSalEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(empBiz.findDelSalEmpCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = empBiz.findDelSalEmp(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("limRole");
		awareCollect.add("salOrg");
		
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
		/*request.setAttribute("salEmp", list);
		request.setAttribute("page", page);
		return mapping.findForward("recSalEmp");*/

	}

	/**
	 * 得到顶级部门（公司）编号 <br>
	 * @return String 顶级部门编号<br>
	 */
	public static String getORGTOPCODE() {
		return ORGTOPCODE;
	}

	/**
	 * 注入empBiz <br>
	 */
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}

	/**
	 * 注入userBiz <br>
	 */
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
}