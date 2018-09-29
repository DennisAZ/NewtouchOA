package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.PaidBIZ;
import com.psit.struts.BIZ.SalInvoiceBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.CrmManagementAreaRange;
import com.psit.struts.entity.CusAddr;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusContact;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.CusServ;
import com.psit.struts.entity.ERPSalOrder;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.MemDate;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalInvoice;
import com.psit.struts.entity.SalOpp;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalPra;
import com.psit.struts.entity.StatSalMon;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.GetXml;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.OutToExc;
import com.psit.struts.util.Page;
import com.psit.struts.form.RecvAmtForm;
import com.psit.struts.util.DAO.BatchOperateDAO;
import com.psit.struts.util.DAO.ModifyTypeDAO;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.format.TransStr;

/**
 * 客户，客户联系人管理 <br>
 */
public class CustomAction extends BaseDispatchAction {
	CustomBIZ customBIZ = null;
	ModifyTypeDAO modType = null;
	BatchOperateDAO batchOperate = null;
	UserBIZ userBiz = null;
	EmpBIZ empBiz = null;
	TypeListBIZ typeListBiz = null;
	OrderBIZ orderBiz = null;
	PaidBIZ paidBiz = null;
	SalInvoiceBIZ  salInvoiceBiz = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 判断用户是否有客户管理，客户联系人操作的权限 <br>
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
    public void uploadCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
    	customBIZ.uploadCus();
    }
	/**
	 * 检测用户是否有客户，客户联系人操作的权限码 <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
//		String methodName = request.getParameter("op");
//		String methLim[][] = { { "toListCustomers", "a025" },// 客户资料是否可见
//				{ "toSaveCus", "r001" },{ "saveCus", "r001" },// 新建客户
//				{ "toUpdCus", "r003" },{ "updCus", "r003" },// 修改客户
//				{ "deleteCus", "r002" },// 删除客户
//				{ "showCompanyCusDesc", "r004" },// 查看客户资料详情
//				{ "toListContacts", "a026" },// 客户联系人是否可见
//				{ "toNewContact", "r005" },{ "saveContactInfo", "r005" },// 新增客户联系人
//				{ "toUpdCon","r007" },{ "changeContactInfo", "r007" },// 修改联系人信息
//				{ "delCusContact", "r006" },// 删除联系人
//				{ "batchDelContact", "r006" },// 联系人批量删除
//				{ "showContactInfo", "r008" },// 查看客户联系人详情
//				{ "addCountry", "sy012" },// 添加类别（国家或地区）
//				{ "modifyCountry", "sy013" },// 修改类别（国家或地区）
//				{ "addProvince", "sy012" },// 添加类别（省）
//				{ "modifyProvince", "sy013" },// 修改类别（省）
//				{ "addCity", "sy012" },// 添加类别（市）
//				{ "modifyCity", "sy013" },// 修改类别（市）
//				{ "listDelCus", "asy015" }, // 回收站是否可见
//				{"toUpdCorSalNum","r036"},{"saveSaleNum","r036"},//修改客户月最低销售额
//				{"toUpdCorBeginBal","r034"},{"saveBeginBal","r034"},//修改客户发货应收余额
//				{"toUpdCorTicketBal","r034"},{"saveTicketBal","r034"},//修改客户开票应收余额
//		};
//		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
//		return userBiz.getLimit(limUser, methodName, methLim);
		return true;
	}
	
	/**
	 * 加载客户自定义字段，包括行业、来源、国家、客户类型 <br>
	 */
	private HttpServletRequest getIndustryCountry(HttpServletRequest request) {
		List industryList = typeListBiz.getEnabledType("1");//行业
		List souList = typeListBiz.getEnabledType("4");//来源
		List cusAreaList = customBIZ.getCusArea();//国家
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusAreaList", cusAreaList);
		request.setAttribute("industryList", industryList);
		request.setAttribute("souList", souList);
		return request;
	}
	
	/**
	 * 判断客户名是否重复 <br>
	 * @param request
	 * 		para > cusName:客户名称
	 * @return  out 输出1代表客户名称重复,0代表不重复<br>
	 */
	public void checkIsRepeatName(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String name = null;
		PrintWriter out = null;
		if (request.getParameter("cusName") != "") {
			name = TransStr.transStr(request.getParameter("cusName"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (name != null) {
				if (customBIZ.checkCus(name)) {
					out.print("1");//重复
				} else {
					out.print("0");//不重复
				}
				out.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if(out!=null){ out.close(); }
		}
	}
	
	/**
	 * 跳转到新建客户页面 <br>
	 * @return ActionForward 跳转到companyCus页面<br>
	 * 		attri > cusTypeList:客户类型; cusAreaList:国家; industryList:行业; souList:来源;
	 */
	public ActionForward toSaveCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request = getIndustryCountry(request);
		return mapping.findForward("companyCus");
	}
	
	/**
	 * 跳转到我管辖范围内新建客户页面 <br>
	 * @return ActionForward 跳转到companyCus页面<br>
	 * 		attri > cusTypeList:客户类型; cusAreaList:国家; industryList:行业; souList:来源;
	 */
	public ActionForward toSaveMyManageCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request = getIndustryCountry(request);
		return mapping.findForward("myCompanyCus");
	}
	
	/**
	 * 保存客户信息 <br>
	 * @param request
	 * 		para > 	industryId:行业;		souId:来源ID;	countryId,provinceId,cityId:国家省市ID
	 *            	cusType:客户类型;  		seNo:负责人
	 * @return ActionForward popDivSuc
	 */
	public ActionForward saveCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String industryId = request.getParameter("industryId");
		String souId = request.getParameter("souId");
		String countryId1 = request.getParameter("countryId");
		String provinceId1 = request.getParameter("provinceId");
		String cityId1 = request.getParameter("cityId");
		String cusType = request.getParameter("cusType");
		String seNo = request.getParameter("seNo");
		String onDate = request.getParameter("corOnDate");
//		LimUser user = (LimUser)request.getSession().getAttribute("CUR_USER");
		
		DynaActionForm form1 = (DynaActionForm) form;
		CusCorCus cusCorCus = (CusCorCus) form1.get("cusCorCus");
		
		if (customBIZ.checkCus(cusCorCus.getCorName())) {
			request.setAttribute("errorMsg", "该客户已存在");
			return mapping.findForward("error");
		} else {
//			String prefix = "CU" + dateFormat.format(new Date());// 前缀
//			CodeCreator codeCreator = new CodeCreator();
//			String corNum = codeCreator.createCode(prefix, "cus_cor_cus", 0);// 生成编号
			if (!StringFormat.isEmpty(cusType)) {
				cusCorCus.setCusType(new TypeList(Long.valueOf(cusType)));
			}
			if(!StringFormat.isEmpty(seNo)){
				cusCorCus.setPerson(new YHPerson(Long.parseLong(seNo)));
			}
			if (StringFormat.isEmpty(countryId1)) { countryId1 = "1"; }
			if (StringFormat.isEmpty(provinceId1)) { provinceId1 = "1"; }
			if (StringFormat.isEmpty(cityId1)) { cityId1 = "1"; }
			if (!StringFormat.isEmpty(industryId)) {
				cusCorCus.setCusIndustry(new TypeList(Long.valueOf(industryId)));
			}
			if (!StringFormat.isEmpty(souId)) {
				cusCorCus.setCorSou(new TypeList(Long.valueOf(souId)));
			}
			if(onDate!=null && !onDate.equals("")){
				cusCorCus.setCorOnDate(GetDate.formatDate(onDate));
			}else{
				cusCorCus.setCorOnDate(null);
			}
			cusCorCus.setCorRecvAmt(cusCorCus.getCorBeginBal());
			cusCorCus.setCorTicketNum(cusCorCus.getCorTicketBal());
			cusCorCus.setCorTempTag(CusCorCus.M_UNMARK);
			cusCorCus.setCorIssuc(CusCorCus.IS_UNSUC);
			cusCorCus.setCorIsdelete(CusCorCus.D_UNDEL);
			cusCorCus.setCusArea(new CusArea(Long.valueOf(countryId1)));
			cusCorCus.setCusProvince(new CusProvince(Long.valueOf(provinceId1)));
			cusCorCus.setCusCity(new CusCity(Long.valueOf(cityId1)));
			cusCorCus.setCorInsUser(this.getPersonName(request));
			cusCorCus.setCorCreatDate(GetDate.getCurTime());
			customBIZ.save(cusCorCus);
			request.setAttribute("msg", "新建客户");
			return mapping.findForward("popDivSuc");
		}
	}
	
	/**
	 * 显示客户开发状态<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				param: corState :客户状态  seNo:业务员Id
	 * @param response
	 * @return ActionForward showCusState <br>
	 * 		attr > list：列表数据 state：状态  empList:业务员列表  seNo:当前业务员Id
	 */
	public ActionForward toShowCusState(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String type = request.getParameter("corState");
		String seNo = request.getParameter("seNo");
		List<CusCorCus> list = customBIZ.getOnTopCusList(type,seNo);
		List<SalEmp> emp = empBiz.getEmpList();
		
		
		request.setAttribute("list", list);
		request.setAttribute("state", type);
		request.setAttribute("empList", emp);
		request.setAttribute("seNo", seNo);
		return mapping.findForward("showCusState");
	}
	
	/**
	 * 显示应收款信息 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			param:  recDate :收款日期 
	 * @param response
	 * @return ActionForward showRecvAmt<br>
	 * 		attr > 
	 */
	public ActionForward toShowRecvAmt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String recDate = request.getParameter("recDate");
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy年MM月dd日");
		String time = "";
		if(recDate ==null || recDate.equals("")){
			recDate = GetDate.parseStrDate(new Date());
			time = dateformat1.format(new Date());
		}else{
			time = dateformat1.format(GetDate.formatDate(recDate));
		}
		
		List<RecvAmtForm> list = customBIZ.recvAmtList(recDate);
		Double num = 0.0;
		for(int i=0;i<list.size();i++){
			RecvAmtForm rec = list.get(i);
			if(rec.getRecvNum() !=null){
				num += rec.getRecvNum();
			}else{
				num += 0;
			}
		}
		
		Double totalTicketNum = 0.0;
		Double totalRecvNum = 0.0;
		List<CusCorCus> cusList = customBIZ.listAllCus();
		for(int i=0;i<cusList.size();i++){
			CusCorCus cus = cusList.get(i);
			if(cus.getCorTicketNum() !=null){
				totalTicketNum += cus.getCorTicketNum();
			}else{
				totalTicketNum += 0;
			}
			if(cus.getCorRecvAmt() !=null){
				totalRecvNum += cus.getCorRecvAmt();
			}else{
				totalRecvNum += 0;
			}
		}
		
		request.setAttribute("list", list);
		request.setAttribute("num", num);
		request.setAttribute("recDate",recDate);
		request.setAttribute("time",time);
		request.setAttribute("totalTicketNum", totalTicketNum);
		request.setAttribute("totalRecvNum", totalRecvNum);
		return mapping.findForward("showRecvAmt");
	}
	
	
	public ActionForward toListCustomersToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("selectCus");
	}
	
	/**
	 * 选择客户列表<br>
	 * @param request
	 *         para > 	corName:客户名称;  corNum:客户编号;
	 *         			pageSize:每页显示记录数; p:当前页码; orderCol:排序列号; isDe:是否逆序;
	 */
	public void listCustomersToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String corName = request.getParameter("corName");
		String corNum = request.getParameter("corNum");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] items = {"cNum","cName","seName"};
		String range="1";//全部客户
		String[] args = { "1", range, null, corName, corNum };
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCustomersCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusCorCus> list = customBIZ.listCustomers(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
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

	public ActionForward toListContactsToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("selectContacts");
	}
	
	/**
	 * 选择联系人列表<br>
	 * @param request
	 *         para > 	conName:联系人姓名; cusId：客户ID
	 *         			pageSize:每页显示记录数; p:当前页码; orderCol:排序列号; isDe:是否逆序;
	 */
	public void listContactsToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conName = request.getParameter("conName");
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] items = {"conName", "conSex", "conLev","conDep","conPos"};
		String[] args = { cusId , conName};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "10";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCusContactsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusContact> list = customBIZ.listCusContacts(args, orderItem, isDe, page.getCurrentPageNo(), Integer.parseInt(pageSize));
		
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
	
	/**
	 * 跳转到客户列表<br>
	 * @param request
	 * 		para,attr >	range:0我的,1全部;	cusState:客户状态;	
	 * 		attr > 	cusIndList:行业;  cusTypeList:客户类型;
	 * @return ActionForward listCustomers<br>
	 */
	public ActionForward toListCustomers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String range = request.getParameter("range");
		String cusState = request.getParameter("cusState");
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		List<TypeList> cusIndList = typeListBiz.getEnabledType("1");//行业
		List<SalEmp> empList = empBiz.getEmpList();
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusIndList", cusIndList);
		request.setAttribute("empList", empList);
		request.setAttribute("range", range);
		request.setAttribute("cusState", cusState);
		return mapping.findForward("listCustomers");
	}
	
	/**
	 * 跳转到根据人员负责的客户列表<br>
	 * @param request
	 * 		para,attr >	range:0我的,1全部;	cusState:客户状态;	
	 * 		attr > 	cusIndList:行业;  cusTypeList:客户类型;
	 * @return ActionForward listCustomers<br>
	 */
	public ActionForward toListCustomersByPerson(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String range = request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seqId = request.getParameter("seqId");
		String userName = request.getParameter("userName");
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		List<TypeList> cusIndList = typeListBiz.getEnabledType("1");//行业
		List<SalEmp> empList = empBiz.getEmpList();
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusIndList", cusIndList);
		request.setAttribute("empList", empList);
		request.setAttribute("range", range);
		request.setAttribute("cusState", cusState);
		request.setAttribute("seqId", seqId);
		request.setAttribute("userName", userName);
		return mapping.findForward("listCustomersByPerson");
	}
	
	/**
	 * 跳转到根据人员负责的客户列表<br>
	 * @param request
	 * 		para,attr >	range:0我的,1全部;	cusState:客户状态;	
	 * 		attr > 	cusIndList:行业;  cusTypeList:客户类型;
	 * @return ActionForward listCustomers<br>
	 */
	public ActionForward toListCustomersByArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String range = request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String str = request.getParameter("str");
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		List<TypeList> cusIndList = typeListBiz.getEnabledType("1");//行业
		List<SalEmp> empList = empBiz.getEmpList();
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusIndList", cusIndList);
		request.setAttribute("empList", empList);
		request.setAttribute("range", range);
		request.setAttribute("cusState", cusState);
		request.setAttribute("str", str);
		return mapping.findForward("listCustomersByArea");
	}
	/**
	 * 跳转到根据人员负责的客户列表<br>
	 * @param request
	 * 		para,attr >	range:0我的,1全部;	cusState:客户状态;	
	 * 		attr > 	cusIndList:行业;  cusTypeList:客户类型;
	 * @return ActionForward listCustomers<br>
	 */
	public ActionForward toListCustomersByOnlyArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String range = request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String str = request.getParameter("str");
		String areaName = request.getParameter("areaName");
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		List<TypeList> cusIndList = typeListBiz.getEnabledType("1");//行业
		List<SalEmp> empList = empBiz.getEmpList();
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusIndList", cusIndList);
		request.setAttribute("empList", empList);
		request.setAttribute("range", range);
		request.setAttribute("cusState", cusState);
		request.setAttribute("str", str);
		request.setAttribute("areaName", areaName);
		return mapping.findForward("listCustomersByOnlyArea");
	}
	
	/**
	 * 跳转到当前登录人管辖的客户列表<br>
	 * @param request
	 * 		para,attr >	range:0我的,1全部;	cusState:客户状态;	
	 * 		attr > 	cusIndList:行业;  cusTypeList:客户类型;
	 * @return ActionForward myListCustomers<br>
	 */
	public ActionForward toMyListCustomers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		//获取当前登录人对象信息
		yh.core.funcs.person.data.YHPerson loginPerson = (yh.core.funcs.person.data.YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		//获取当前登录人所有的管辖区域
		List<Map<String,String>> manageAreaList = new ArrayList<Map<String,String>>();
		try {
			dbConn = requestDbConn.getSysDbConn();
			
			manageAreaList = getManageAreasByUserId(dbConn,String.valueOf(loginPerson.getSeqId()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String range = request.getParameter("range");
		String cusState = request.getParameter("cusState");
		List<TypeList> cusTypeList = typeListBiz.getEnabledType("2");//客户类型
		List<TypeList> cusIndList = typeListBiz.getEnabledType("1");//行业
		List<SalEmp> empList = empBiz.getEmpList();
		List cusAreaList = customBIZ.getCusArea();//获取国家对象集合
		
		request.setAttribute("cusTypeList", cusTypeList);
		request.setAttribute("cusIndList", cusIndList);
		request.setAttribute("cusAreaList", cusAreaList);
		request.setAttribute("empList", empList);
		request.setAttribute("range", range);
		request.setAttribute("cusState", cusState);
		return mapping.findForward("myListCustomers");
	}
	/**
	 * 客户列表 <br>
	 * @param request
	 * 		para >	range:0我的客户1全部客户;	cusState:客户状态;	corName:客户名称;		corNum:客户编号;
	 * 				cusType:客户类型;		startTime,endTime:创建时间;	corAdd:地址;		cusInd:行业;
	 * 				filter:列表筛选类型; 		seName:负责人;		color:标色;
	 */
	public void listCustomers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		SalEmp curEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String color = request.getParameter("color");
		String corName=request.getParameter("corName");
		String corNum=request.getParameter("corNum");
		String corAdd = request.getParameter("corAdd");
		String range=request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seName = request.getParameter("seName");
		String cusType=request.getParameter("cusType");
		String cusInd = request.getParameter("cusInd");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String filter=request.getParameter("filter");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "cState", "cName","cHot","cType",
				 "cPho","cCellPho","cAddr", "seName", "cLastDate"};
		if(!StringFormat.isEmpty(cusState)&&cusState.equals("a")){
			cusState=null;
		}
		String[] args = { "1", range, String.valueOf(this.getPersonId(request)), corName,
				corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
				filter, color, cusState };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCustomersCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusCorCus> list = customBIZ.listCustomers(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusType");
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
	 * 根据人员Id查询该人员所负责的客户列表 <br>
	 * @param request
	 * 		para >	range:0我的客户1全部客户;	cusState:客户状态;	corName:客户名称;		corNum:客户编号;
	 * 				cusType:客户类型;		startTime,endTime:创建时间;	corAdd:地址;		cusInd:行业;
	 * 				filter:列表筛选类型; 		seName:负责人;		color:标色;
	 */
	public void listCustomersByPersonId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		SalEmp curEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String seqId = request.getParameter("seqId");
		String color = request.getParameter("color");
		String corName=request.getParameter("corName");
		String corNum=request.getParameter("corNum");
		String corAdd = request.getParameter("corAdd");
		String range=request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seName = request.getParameter("seName");
		String cusType=request.getParameter("cusType");
		String cusInd = request.getParameter("cusInd");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String filter=request.getParameter("filter");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "cState", "cName","cHot","cType",
				"cPho","cCellPho","cAddr", "seName", "cLastDate"};
		if(!StringFormat.isEmpty(cusState)&&cusState.equals("a")){
			cusState=null;
		}
		String[] args = { "1", range, seqId, corName,
				corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
				filter, color, cusState };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCustomersCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusCorCus> list = customBIZ.listCustomers(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusType");
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
	 * 查询自己管辖范围内的客户列表 <br>
	 * @param request
	 * 		para >	range:0我的客户1全部客户;	cusState:客户状态;	corName:客户名称;		corNum:客户编号;
	 * 				cusType:客户类型;		startTime,endTime:创建时间;	corAdd:地址;		cusInd:行业;
	 * 				filter:列表筛选类型; 		seName:负责人;		color:标色;countryId：国家；provinceId：省份；cityId：城市
	 * @throws Exception 
	 */
	public void mylistCustomers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  try {
			dbConn = requestDbConn.getSysDbConn(); //获取数据库链接
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//登录当前人对象
		yh.core.funcs.person.data.YHPerson loginPerson = (yh.core.funcs.person.data.YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		  
		//获取当前登录人所有的管辖区域
		List<Map<String,String>> manageAreaList = new ArrayList<Map<String,String>>();
		
		manageAreaList = getManageAreasByUserId(dbConn,String.valueOf(loginPerson.getSeqId()));
		
		SalEmp curEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String color = request.getParameter("color");
		String corName=request.getParameter("corName");
		String corNum=request.getParameter("corNum");
		String corAdd = request.getParameter("corAdd");
		String range=request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seName = request.getParameter("seName");
		String cusType=request.getParameter("cusType");
		String cusInd = request.getParameter("cusInd");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String filter=request.getParameter("filter");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		//=======================添加负责区域的查询条件
		String countryId = request.getParameter("countryId")==null?"":request.getParameter("countryId"); //获取国家
		String provinceId = request.getParameter("provinceId")==null?"":request.getParameter("provinceId"); //获取省份
		String cityId = request.getParameter("cityId")==null?"":request.getParameter("cityId"); //获取城市
		
		String orderItem = "";
		String[] items = { "cState", "cName","cHot","cType",
				"cPho","cCellPho","cAddr", "seName", "cLastDate"};
		if(!StringFormat.isEmpty(cusState)&&cusState.equals("a")){
			cusState=null;
		}
		
		Set<String> cusProvinceListIds = new HashSet<String>(); //省份标识集合
		List<String> cusCityListIds = new ArrayList<String>(); //城市标识集合
		
		//查询管辖区域下面客户
		List<CusCorCus> cuslist = new ArrayList<CusCorCus>();
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		ListForm listForm = new ListForm();
		Page page = new Page(0, Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		
		if((countryId.equals("1") || countryId.equals("2")) && (provinceId.equals("") || provinceId.equals("1")) && (cityId.equals("") || cityId.equals("1"))){ //说明查询区域没有选择或者只选择了全国
					
				Map<String,String> map = null;
				if(manageAreaList != null && manageAreaList.size() > 0){
					for (int m = 0; m < manageAreaList.size(); m++) {
						map = manageAreaList.get(m);
						countryId = map.get("country_id");
						cusProvinceListIds.add(map.get("province_id"));
						cusCityListIds.add(map.get("city_id"));
					}
				}
				if(cusProvinceListIds != null && cusProvinceListIds.size() > 0){
					Iterator<String> it = cusProvinceListIds.iterator();
					for (;it.hasNext();) {
						 String cusProvinceId= it.next();
							if(cusCityListIds != null && cusCityListIds.size() > 0){
								for (int j = 0; j < cusCityListIds.size(); j++) {
									String cusCity_id = cusCityListIds.get(j);
									
									String[] args = { "1", range, 
											countryId,
											cusProvinceId,
											cusCity_id,
											corName,
											corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
											filter, color};
									
									List<CusCorCus> list = customBIZ.mylistCustomers(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
									cuslist.addAll(list);
								}
							}
					}
				}
		}else if((countryId.equals("1") || countryId.equals("2")) && !"".equals(provinceId) && !"1".equals(provinceId) && cityId.equals("1")){//说明查询区域选择了全国以及选择了省份
			Map<String,String> map = null;
			if(manageAreaList != null && manageAreaList.size() > 0){
				for (int m = 0; m < manageAreaList.size(); m++) {
					map = manageAreaList.get(m);
					if(provinceId.equals(map.get("province_id"))){
						cusCityListIds.add(map.get("city_id"));
					}
				}
			}
			
			if(cusCityListIds != null && cusCityListIds.size() > 0){
				for (int j = 0; j < cusCityListIds.size(); j++) {
					String cusCity_Id= cusCityListIds.get(j);
					
					String[] args = { "1", range, 
							countryId,
							provinceId,
							cusCity_Id,
							corName,
							corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
							filter, color};
					
					List<CusCorCus> list = customBIZ.mylistCustomers(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
					cuslist.addAll(list);
				}
			}
		}else if((countryId.equals("1") || countryId.equals("2")) && !"".equals(provinceId) && !"1".equals(provinceId) && !"".equals(cityId) && !"1".equals(cityId)){//说明查询区域选择了全国以及选择了省份以及城市
			
			String[] args = { "1", range, 
					countryId,
					provinceId,
					cityId,
					corName,
					corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
					filter, color};
			
			List<CusCorCus> list = customBIZ.mylistCustomers(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
			cuslist.addAll(list);
		}	

		if(cuslist.size() > 0){
			page.setRowsCount(cuslist.size());
		}else{
			page.setRowsCount(0);
		}
		listForm.setList(cuslist);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusType");
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
	 * 根据区域查询该区域下的所有客户,带有checkbox
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getCustomersByArea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		SalEmp curEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String color = request.getParameter("color");
		String corName=request.getParameter("corName");
		String corNum=request.getParameter("corNum");
		String corAdd = request.getParameter("corAdd");
		String range=request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seName = request.getParameter("seName");
		String cusType=request.getParameter("cusType");
		String cusInd = request.getParameter("cusInd");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String filter=request.getParameter("filter");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "cState", "cName","cHot","cType",
				"cPho","cCellPho","cAddr", "seName", "cLastDate"};
		if(!StringFormat.isEmpty(cusState)&&cusState.equals("a")){
			cusState=null;
		}
		
		
		//区域数组
		String areaId = request.getParameter("str");
		
		String[] areaIds = areaId.split(",");
		//查询管辖区域下面客户
		List<CusCorCus> cuslist = new ArrayList<CusCorCus>();
        if(areaIds != null && areaIds.length > 0){
			if (p == null || p.trim().length() < 1) {
				p = "1";
			}
			if (pageSize == null || pageSize.equals("")) {
				pageSize = "30";//默认每页条数
			}
			if (orderCol != null && !orderCol.equals("")) {
				orderItem = items[Integer.parseInt(orderCol)];
			}
			ListForm listForm = new ListForm();
			Page page = new Page(0, Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			for (int i = 0; i < areaIds.length; i++) {
				String checkBoxId = areaIds[i];
				String country_id = null;
				String province_id = null;
				String city_id = null;
				if(!"".equals(checkBoxId)){
					int countryIndex = checkBoxId.indexOf("c");
					int provinceIndex = checkBoxId.indexOf("p");
					int cityIndex = checkBoxId.indexOf("t");
					if(countryIndex > -1 && provinceIndex > -1 && cityIndex > -1){
						country_id = checkBoxId.substring(countryIndex+1, provinceIndex);
						province_id = checkBoxId.substring(provinceIndex+1, cityIndex);
						city_id = checkBoxId.substring(cityIndex+1);
						
						String[] args = { "1", range, 
								country_id,
								province_id,
								city_id,
								corName,
								corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
								filter, color,cusState};
						
						List<CusCorCus> list = customBIZ.getCustomersByArea(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
						cuslist.addAll(list);
					}
				}
			}
			if(cuslist.size() > 0){
				page.setRowsCount(cuslist.size());
			}else{
				page.setRowsCount(0);
			}
			listForm.setList(cuslist);
			listForm.setPage(page);
			List awareCollect = new LinkedList();
			awareCollect.add("cusType");
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
	}
	
	
	/**
	 * 根据区域查询该区域下的所有客户，不带有checkBox
	 * 
	 * updateTime  2013-08-26 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getCustomersByOnlyArea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  try {
			dbConn = requestDbConn.getSysDbConn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yh.core.funcs.person.data.YHPerson loginPerson = (yh.core.funcs.person.data.YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		  
		//获取当前登录人所有的管辖区域
		List<Map<String,String>> manageAreaList = new ArrayList<Map<String,String>>();
		
		manageAreaList = getManageAreasByUserId(dbConn,String.valueOf(loginPerson.getSeqId()));
		
		SalEmp curEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String color = request.getParameter("color");
		String corName=request.getParameter("corName");
		String corNum=request.getParameter("corNum");
		String corAdd = request.getParameter("corAdd");
		String range=request.getParameter("range");
		String cusState = request.getParameter("cusState");
		String seName = request.getParameter("seName");
		String cusType=request.getParameter("cusType");
		String cusInd = request.getParameter("cusInd");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String filter=request.getParameter("filter");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "cState", "cName","cHot","cType",
				"cPho","cCellPho","cAddr", "seName", "cLastDate"};
		if(!StringFormat.isEmpty(cusState)&&cusState.equals("a")){
			cusState=null;
		}
		
//		List<CusProvince> cusProvinceList = new ArrayList<CusProvince>(); //省份集合
		Set<String> cusProvinceListIds = new HashSet<String>(); //省份标识集合
//		List<CusCity> cusCityList = new ArrayList<CusCity>(); //城市集合
		List<String> cusCityListIds = new ArrayList<String>(); //城市标识集合
//		YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		//区域数组
		String areaId = request.getParameter("str");
		
		String[] areaIds = areaId.split(",");
		//查询管辖区域下面客户
		List<CusCorCus> cuslist = new ArrayList<CusCorCus>();
		if(areaIds != null && areaIds.length > 0){
			if (p == null || p.trim().length() < 1) {
				p = "1";
			}
			if (pageSize == null || pageSize.equals("")) {
				pageSize = "30";//默认每页条数
			}
			if (orderCol != null && !orderCol.equals("")) {
				orderItem = items[Integer.parseInt(orderCol)];
			}
			ListForm listForm = new ListForm();
			Page page = new Page(0, Integer.parseInt(pageSize));
			page.setCurrentPageNo(Integer.parseInt(p));
			for (int i = 0; i < areaIds.length; i++) {
				String checkBoxId = areaIds[i];
				String country_id = null;
				String province_id = null;
				String city_id = null;
				if(!"".equals(checkBoxId)){
					int countryIndex = checkBoxId.indexOf("c");
					int provinceIndex = checkBoxId.indexOf("p");
					int cityIndex = checkBoxId.indexOf("t");
					
					if(countryIndex > -1 && provinceIndex == -1 && cityIndex == -1){
						country_id = checkBoxId.substring(countryIndex+1, checkBoxId.length());
//						cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,Long.parseLong(country_id));
						Map<String,String> map = null;
						if(manageAreaList != null && manageAreaList.size() > 0){
							for (int m = 0; m < manageAreaList.size(); m++) {
								map = manageAreaList.get(m);
								cusProvinceListIds.add(map.get("province_id"));
								cusCityListIds.add(map.get("city_id"));
							}
						}
//						if(cusProvinceList != null && cusProvinceList.size() > 0){
						if(cusProvinceListIds != null && cusProvinceListIds.size() > 0){
							Iterator<String> it = cusProvinceListIds.iterator();
							for (;it.hasNext();) {
								 String cusProvinceId= it.next();
//								 cusCityList = userPrivLogic.selectUserManageCitys(dbConn,cusProvince.getPrvId());
//									if(cusCityList != null && cusCityList.size() > 0){
									if(cusCityListIds != null && cusCityListIds.size() > 0){
										for (int j = 0; j < cusCityListIds.size(); j++) {
//											CusCity cusCity= cusCityList.get(j);
											String cusCity_id = cusCityListIds.get(j);
											
											String[] args = { "1", range, 
													country_id,
//													String.valueOf(cusProvince.getPrvId()),
													cusProvinceId,
//													String.valueOf(cusCity_id),
													cusCity_id,
													corName,
													corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
													filter, color,cusState};
											
											List<CusCorCus> list = customBIZ.getCustomersByArea(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
											cuslist.addAll(list);
										}
									}
							}
						}
					}
//					if(countryIndex > -1 && provinceIndex > -1 && cityIndex == -1){
//						country_id = checkBoxId.substring(countryIndex+1, provinceIndex);
//						province_id = checkBoxId.substring(provinceIndex+1, checkBoxId.length());
//						
//						cusCityList = userPrivLogic.selectUserManageCitys(dbConn,Long.parseLong(province_id));
//						if(cusCityList != null && cusCityList.size() > 0){
//							for (int j = 0; j < cusCityList.size(); j++) {
//								CusCity cusCity= cusCityList.get(j);
//								
//								String[] args = { "1", range, 
//										country_id,
//										province_id,
//										String.valueOf(cusCity.getCityId()),
//										corName,
//										corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
//										filter, color,cusState};
//								
//								List<CusCorCus> list = customBIZ.getCustomersByArea(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
//								cuslist.addAll(list);
//							}
//						}
//					}
					if(countryIndex > -1 && provinceIndex > -1 && cityIndex == -1){
						country_id = checkBoxId.substring(countryIndex+1, provinceIndex);
						province_id = checkBoxId.substring(provinceIndex+1, checkBoxId.length());
//						cusCityList = userPrivLogic.selectUserManageCitys(dbConn,Long.parseLong(province_id));
						Map<String,String> map = null;
						if(manageAreaList != null && manageAreaList.size() > 0){
							for (int m = 0; m < manageAreaList.size(); m++) {
								map = manageAreaList.get(m);
								if(province_id.equals(map.get("province_id"))){
									cusCityListIds.add(map.get("city_id"));
								}
							}
						}
						
						if(cusCityListIds != null && cusCityListIds.size() > 0){
							for (int j = 0; j < cusCityListIds.size(); j++) {
								String cusCity_Id= cusCityListIds.get(j);
								
								String[] args = { "1", range, 
										country_id,
										province_id,
										cusCity_Id,
										corName,
										corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
										filter, color,cusState};
								
								List<CusCorCus> list = customBIZ.getCustomersByArea(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
								cuslist.addAll(list);
							}
						}
					}
					if(countryIndex > -1 && provinceIndex > -1 && cityIndex > -1){
						country_id = checkBoxId.substring(countryIndex+1, provinceIndex);
						province_id = checkBoxId.substring(provinceIndex+1, cityIndex);
						city_id = checkBoxId.substring(cityIndex+1);
						
						String[] args = { "1", range, 
								country_id,
								province_id,
								city_id,
								corName,
								corNum, cusType, startTime, endTime, seName, corAdd, cusInd,
								filter, color,cusState};
						
						List<CusCorCus> list = customBIZ.getCustomersByArea(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
						cuslist.addAll(list);
					}
				}
			}
			if(cuslist.size() > 0){
				page.setRowsCount(cuslist.size());
			}else{
				page.setRowsCount(0);
			}
			listForm.setList(cuslist);
			listForm.setPage(page);
			List awareCollect = new LinkedList();
			awareCollect.add("cusType");
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
	}
	
	
     private List<Map<String,String>> getManageAreasByUserId(Connection dbConn,String userId)throws Exception{
		  List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		  String sql = "select r.country_id,r.province_id,r.city_id from crm_management_area a,crm_management_area_range r where a.id=r.ma_id and a.person_id='"+userId+"'";
		  Statement st = dbConn.createStatement();
		  ResultSet rs = st.executeQuery(sql);
		  while(rs.next()){
			  Map<String,String> map = new HashMap<String,String>();
			  map.put("country_id", rs.getString("country_id"));
			  map.put("province_id", rs.getString("province_id"));
			  map.put("city_id", rs.getString("city_id"));
			  list.add(map);
		  }
		  return list;
	  }
	  
	/**
	 * 跳转到被删除的客户列表(回收站) <br>
	 * @return ActionForward recCus<br>
	 */
	public ActionForward toListDelCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("recCus");
	}
	/**
	 * 被删除的客户列表(回收站) <br>
	 * @param request
	 *         para > p:当前页码; pageSize:每页显示记录数;
	 */
	public void listDelCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(customBIZ.listDelCusCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusCorCus> list = customBIZ.listDelCus(page.getCurrentPageNo(), page.getPageSize());
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
	 * 跳转到客户高级查询页面 <br>
	 * @param request
	 * 		para > range:查询范围，0为自己，1为全部
	 * @param response
	 * @return ActionForward supSearch<br>
	 * 		attr > 	salAreaList:销售区域列表;		orgTopCode:顶级部门编号;	orgList:部门列表（显示查询条件中的所属账号使用）;
	 *         		cusAreaList:已启用的国家列表;	industryList:行业列表;	souList:来源列表;		range:查询范围;
	 */
	/*public ActionForward toCusSupSearch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String range = request.getParameter("range");
		SalEmp salEmp = (SalEmp) request.getSession().getAttribute("CUR_EMP");
		String seNo = salEmp!=null?salEmp.getSeNo().toString():"";
		List orgList = null;
		if (range!=null && range.equals("0")) {// 查询我的客户
			orgList = empBiz.getMyOrg(seNo);
		} else {// 查询所有的客户
			orgList = empBiz.getAllOrg();
		}
		request = getIndustryCountry(request);
		request.setAttribute("orgTopCode", EmpAction.getORGTOPCODE());
		request.setAttribute("range", range);
		request.setAttribute("orgList", orgList);
		return mapping.findForward("supSearch");
	}*/
	
	
	/**
	 * 跳转到销售趋势统计 <br>
	 * @param request
	 * 		para > 	cusId:客户ID
	 * @return ActionForward 跳转到statOrdDSum<br>
	 * 		attr >	cusId:客户ID
	 */
	public ActionForward toCusSalStat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("showCusSalStat");
	}
	/**
	 * 销售趋势统计（客户详情下模块） <br>
	 * @param request
	 * 		para >	startMon:开始月;  endMon:结束月;  cusId:客户ID
	 */
	public void cusSalStat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startMon = request.getParameter("startMon");
		String endMon = request.getParameter("endMon");
		String cusId = request.getParameter("cusId");
		
		String[] args = {startMon, endMon, cusId};
		List<StatSalMon> statList = paidBiz.statCusPaidMon(args);
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
		String dataXML = GetXml.getXml(datas, new String[] { "回款金额","销售金额" }, "客户销售分析",
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
	 * 获得已启用的省 <br>
	 * @param request
	 * 		para >	couId:国家ID;
	 */
	public void getProvince(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = null;
		PrintWriter out = null;
		if (request.getParameter("couId") != "") {
			id = Long.valueOf(request.getParameter("couId"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (id != null && id != 1) {
				List proList = customBIZ.getCusProvince(id);
				Iterator i = proList.iterator();
				String provalue = "";
				while (i.hasNext()) {
					CusProvince pro = (CusProvince) i.next();
					provalue += pro.getPrvId() + "," + pro.getPrvName() + ",";

				}
				out.print(provalue);
				out.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	
	/**
	 * 获得我自己管辖已启用的省 <br>
	 * @param request
	 * 		para >	couId:国家ID;
	 */
	public void getmyManageProvince(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = null;
		PrintWriter out = null;
		if (request.getParameter("couId") != "") {
			id = Long.valueOf(request.getParameter("couId"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (id != null && id != 1) {
				List<java.lang.Long> carList = customBIZ.getmyManageProvince(id);
				Iterator i = carList.iterator();
				String provalue = "";
				
				while (i.hasNext()) {
					java.lang.Long priv_id = (java.lang.Long) i.next();
					String priv_name =getmyManageProvinceName(priv_id);
					//利用省份的Id去查询省的名称
					provalue += priv_id + "," + priv_name + ",";

				}
				out.print(provalue);
				out.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	/**
	 * 获得我自己管辖已启用的省的名称 <br>
	 * @param request
	 * 		para >	couId:国家ID;
	 */
	private String getmyManageProvinceName(long priv_id) {
			List<CusProvince> proList = customBIZ.getmyManageProvinceName(priv_id);
			String proName = "";
			if(proList != null && proList.size() > 0){
				CusProvince pro = (CusProvince) proList.get(0);
				proName = pro.getPrvName();
			}
			return proName;
	}
	/**
	 * 获得已启用的城市 <br>
	 * @param request
	 * 		para >	proId:省ID
	 */
	public void getCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = null;
		PrintWriter out = null;
		if (request.getParameter("proId") != "") {
			id = Long.valueOf(request.getParameter("proId"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (id != null && id != 1) {
				List cityList = customBIZ.getCusCity(id);
				Iterator i = cityList.iterator();
				String cityalue = "";
				while (i.hasNext()) {
					CusCity city = (CusCity) i.next();
					cityalue += city.getCityId() + "," + city.getCityName()
							+ ",";

				}
				out.print(cityalue);
				out.flush();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	
	/**
	 * 获得已启用的城市 <br>
	 * @param request
	 *  para >	proId:省ID
	 */
	public void getmyManageCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = null;
		PrintWriter out = null;
		if (request.getParameter("proId") != "") {
			id = Long.valueOf(request.getParameter("proId"));
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (id != null && id != 1) {
				List<java.lang.Long> cityList = customBIZ.getmyManageCity(id);
				Iterator i = cityList.iterator();
				String cityvalue = "";
				
				while (i.hasNext()) {
					Object obj = i.next();
					if(obj != null){
						java.lang.Long city_id = (java.lang.Long) obj;
						String city_name =getmyManageCityName(city_id);
						//利用省份的Id去查询省的名称
						cityvalue += city_id + "," + city_name + ",";
					}else{
						continue;
					}
				}
				out.print(cityvalue);
				out.flush();
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 获得我自己管辖已启用的省的名称 <br>
	 * @param request
	 * 		para >	couId:国家ID;
	 */
	private String getmyManageCityName(long city_id) {
			List<CusCity> proList = customBIZ.getmyManageCityName(city_id);
			String cityName = "";
			if(proList != null && proList.size() > 0){
				CusCity ct = (CusCity) proList.get(0);
				cityName = ct.getCityName();
			}
			return cityName;
	}

	/**
	 * 获得需要关注的联系人 <br>
	 * @param request
	 *         parameter > range:标识查询范围0表示查询我的联系人1表示全部联系人
	 * @param response
	 * @return ActionForward 跳转到lastConBirList页面<br>
	 *         attribute > num:当天需要关注的联系人数量 conBirList:当天需要关注的联系人列表
	 *         			   num2:最近十天需要关注的联系人数量 conLastBirList:最近十天需要关注的联系人列表
	 *                     range:标识查询范围0表示查询我的联系人1表示查询我的下属联系人2表示全部联系人
	 */
	public ActionForward getContactByBirth(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String range=request.getParameter("range");
		request.setAttribute("range",range);
//		String userCode=(String) request.getSession().getAttribute("userCode");
//		String userNum = (String) request.getSession().getAttribute("userNum");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		String seNo = String.valueOf(this.getPersonId(request));
		Date startDate = new Date();
		Date endDate = new Date();
		int num = customBIZ.getContactByBirthCount(startDate, startDate,
				range,seNo);
		List list = customBIZ.getContactByBirth(startDate, startDate, range,seNo);

		startDate = OperateDate.getDate(startDate, 1);
		endDate = OperateDate.getDate(startDate, 9);
		int num2 = num
				+ customBIZ.getContactByBirthCount(startDate, endDate,range,seNo);
		List list2 = customBIZ.getContactByBirth(startDate, endDate,range,seNo);

		request.setAttribute("num", num);
		request.setAttribute("conBirList", list);
		request.setAttribute("num2", num2);
		request.setAttribute("conLastBirList", list2);
		return mapping.findForward("lastConBirList");
	}
	
	/**
	 * 跳转到联系人页面<br>
	 * @param request
	 * 			parameter > range :0:我的  1：全部
	 * @return ActionForward 跳转到listContact页面<br>
	 * 		attribute > range :0:我的  1：全部
	 */
	public ActionForward toListContacts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		
		request.setAttribute("range", range);
		return mapping.findForward("listContact");
	}
	/**
	 * 联系人列表 <br>
	 * @param request
	 *         	para >  range:0我的1全部  conName:联系人名称  cusCode:客户ID
	 *         			conLev:联系人分类  uCode:负责人  cusName:客户名称 uName:负责人名称
	 *         			p:当前页码 pageSize:每页显示记录数 orderCol:排序列号 isDe:是否逆序 
	 */
	public void listContacts(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String conName = request.getParameter("conName");
//		String code = request.getParameter("cusCode");
		String conLev = request.getParameter("conLev");
//		String uCode=request.getParameter("uCode");
		String cusName =request.getParameter("cusName");
		String uName = request.getParameter("uName");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		String seNo = String.valueOf(this.getPersonId(request));
		String range=request.getParameter("range");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "conName", "conSex", "conLev", "conCus","conDep",
				"conServ", "conPho", "conWPho", "conEm", "conQQ"};
		String[] args = { range,seNo, conName, cusName, conLev, uName };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listContactsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusContact> list = customBIZ.listContacts(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
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
	 * 保存联系人信息 <br>
	 * @param request
	 *         parameter > cusId:客户ID conBir:联系人关注日期
	 *            		   isIfrm:是否在详情页面中新建若为1则是在详情页面中新建 userCode:负责账号
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:保存成功提示信息 isIfrm:是否在详情页面中新建
	 */
	public ActionForward saveContactInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusContact cusContact = (CusContact) form1.get("cusContact");
		String cusId = request.getParameter("cusId");
		String isIfrm = request.getParameter("isIfrm");
		String seNo=request.getParameter("seNo");
		//String conBir = request.getParameter("conBir");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		/*if (!conBir.equals("")) {
			try {
				cusContact.setConBir(dateFormat.parse(conBir));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			cusContact.setConBir(null);
		}*/
		if (cusId != null && !cusId.equals("")) {
			cusContact.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		} else {
			cusContact.setCusCorCus(null);
		}
		cusContact.setConCreDate(date1);
		cusContact.setConModDate(date1);
		cusContact.setConInpUser(this.getPersonName(request));
		cusContact.setConUpdUser(null);
		cusContact.setConModDate(null);
		cusContact.setPerson(new YHPerson(Long.parseLong(seNo)));
		customBIZ.save(cusContact);
		// 详情下新建 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建联系人");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 修改联系人信息 <br>
	 * @param request
	 *         parameter > contactId:联系人ID cusCode:客户ID conBir:联系人关注日期 isIfrm:是否在详情页面中新建
	 *            		   isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑 userCode:负责账号Id
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:修改成功提示信息 isIfrm:是否在详情页面中新建
	 */
	public ActionForward changeContactInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		Long conId = Long.valueOf(request.getParameter("contactId"));
		//String conBir = request.getParameter("conBir");
		String isIfrm = request.getParameter("isIfrm");
		String seNo = request.getParameter("seNo");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		Date date = new Date();
		Date date1 = new java.sql.Date(date.getTime());
		
		DynaActionForm form1 = (DynaActionForm) form;
		CusContact cusContact = (CusContact) form1.get("cusContact");
		CusContact cusContact1 = customBIZ.showContact(conId);
		/*if ( conBir!= null && !conBir.equals("")) {
			try {
				cusContact.setConBir(dateFormat.parse(conBir));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			cusContact.setConBir(null);
		}*/
		if (cusId != null && !cusId.equals("")) {
			cusContact.setCusCorCus(new CusCorCus(Long.valueOf(cusId)));
		}
		if(seNo != null && !seNo.equals("")){
			cusContact.setPerson(new YHPerson(Long.parseLong(seNo)));
		}
		cusContact.setConUpdUser(this.getPersonName(request));
		cusContact.setConModDate(date1);
		cusContact.setConId(conId);
		cusContact.setConCreDate(cusContact1.getConCreDate());
		cusContact.setConInpUser(cusContact1.getConInpUser());
		customBIZ.updateContact(cusContact);
		// 详情下编辑 刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "修改联系人");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到纪念日列表<br>
	 * @param request
	 * 		para,attr >	conId:联系人ID
	 * @return ActionForward listConMemDate
	 */
	public ActionForward toListConMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String conId = request.getParameter("conId");
		request.setAttribute("conId", conId);
		return mapping.findForward("listConMemDate");
	}
	/**
	 * 纪念日列表 <br>
	 * @param request
	 * 		para >	conId:联系人ID
	 */
	public void listConMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "mdName", "mdDate" };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listMemDateByConCount(conId), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<MemDate> list = customBIZ.listMemDateByCon(conId, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
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
	
	/**
	 * 跳转到新建纪念日 <br>
	 * @param request
	 * 		para,attr >	conId:联系人ID
	 * @return ActionForward formMemDate
	 */
	public ActionForward toSaveMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		request.setAttribute("conId", conId);
		return mapping.findForward("formMemDate");
	}
	
	/**
	 * 保存纪念日 <br>
	 * @param request
	 * 		para > 	conId:联系人ID;	meDate:提醒日期;
	 * @return ActionForward popDivSuc/error
	 */
	public ActionForward saveMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		String mdDate = request.getParameter("mdDate");
		DynaActionForm form1 = (DynaActionForm) form;
		MemDate memDate = (MemDate)form1.get("memDate");
		
		if(!StringFormat.isEmpty(conId)){
			memDate.setMdContact(new CusContact(Long.parseLong(conId)));
			if(!StringFormat.isEmpty(mdDate)){
				memDate.setMdDate(GetDate.formatDate(mdDate));
			}
			customBIZ.saveMemDate(memDate);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "新建纪念日");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", "该联系人不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 跳转到编辑纪念日 <br>
	 * @param request
	 * 		para,attr >	meId:纪念日ID;
	 * @return ActionForward formMemDate
	 */
	public ActionForward toUpdMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String mdId = request.getParameter("mdId");
		MemDate memDate = customBIZ.getMemDate(mdId);
		request.setAttribute("memDate", memDate);
		return mapping.findForward("formMemDate");
	}
	/**
	 * 修改纪念日 <br>
	 * @param request
	 * 		para >	conId:联系人ID;	meDate:提醒日期;
	 * @return ActionForward popDivSuc/error
	 */
	public ActionForward updMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		String mdDate = request.getParameter("mdDate");
		DynaActionForm form1 = (DynaActionForm) form;
		MemDate memDate = (MemDate)form1.get("memDate");
		
		if(!StringFormat.isEmpty(conId)){
			memDate.setMdContact(new CusContact(Long.parseLong(conId)));
			if(!StringFormat.isEmpty(mdDate)){
				memDate.setMdDate(GetDate.formatDate(mdDate));
			}
			customBIZ.updMemDate(memDate);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "修改纪念日");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", "该联系人不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 删除纪念日<br>
	 * @param request
	 * 		para >	meId:纪念日ID;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward delMemDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String mdId = request.getParameter("mdId");
		if(!StringFormat.isEmpty(mdId)){
			customBIZ.delMemDate(mdId);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "删除纪念日");
			return mapping.findForward("popDivSuc");
		}	
		else{
			request.setAttribute("errorMsg", "该纪念日不存在");
			return mapping.findForward("error");
		}
	}

	
	/**
	 * 跳转到编辑客户页面 <br>
	 * @param request
	 * 		para >	corCode:客户ID
	 * @param response
	 * @return ActionForward 跳转到showCompanyCusInfo页面<br>
	 *         attribute > cusTypeList:客户类型列表 cusAreaList:国家列表 industryList:行业列表
	 *         			   souList:来源列表 cusCorCusInfo:客户实体
	 */
	public ActionForward toUpdCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String corCode = request.getParameter("corCode");
		String flag = request.getParameter("flag");
		CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(corCode);
		request = getIndustryCountry(request);
		request.setAttribute("cusCorCusInfo", cusCorCus);
		request.setAttribute("flag", flag);
		return mapping.findForward("showCompanyCusInfo");
	}
	
	
	/**
	 * 跳转到编辑自己管辖范围区域内的客户页面 <br>
	 * @param request
	 * 		para >	corCode:客户ID
	 * @param response
	 * @return ActionForward 跳转到showCompanyCusInfo页面<br>
	 *         attribute > cusTypeList:客户类型列表 cusAreaList:国家列表 industryList:行业列表
	 *         			   souList:来源列表 cusCorCusInfo:客户实体
	 */
	public ActionForward toUpdMyManageCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String corCode = request.getParameter("corCode");
		CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(corCode);
		request = getIndustryCountry(request);
		request.setAttribute("cusCorCusInfo", cusCorCus);
		return mapping.findForward("showMyCompanyCusInfo");
	}
	/**
	 * 修改客户信息 <br>
	 * @param request
	 * 		para >	corCode:客户ID countryId:国家 provinceId:省 cityId:市
	 *            		   industryId:行业 cType:客户类型 souId:来源Id userCode:用户码
	 * @return ActionForward 客户名称重复跳转到error页面 修改成功跳转到popDivSuc页面<br>
	 */
	public ActionForward updCus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		String corCode = request.getParameter("corCode");
		String industryId = request.getParameter("industryId");
		String souId = request.getParameter("souId");
		String cType = request.getParameter("cType");
		String countryId1 = request.getParameter("countryId");
		String provinceId1 = request.getParameter("provinceId");
		String cityId1 = request.getParameter("cityId");
		String seNo = request.getParameter("seNo");
		String onDate = request.getParameter("corOnDate");
		DynaActionForm form1 = (DynaActionForm) form;
		CusCorCus cusCorCus = (CusCorCus) form1.get("cusCorCus");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record = "";
		String recLog = "";
		CusCorCus cusCorCus1 = customBIZ.getCusCorCusInfo(corCode);
		if (!cusCorCus.getCorName().equals(cusCorCus1.getCorName())&&customBIZ.checkCus(cusCorCus.getCorName())) {
			request.setAttribute("errorMsg", "该客户已存在");
			return mapping.findForward("error");
		}else{
			if(!cusCorCus1.getCorState().equals(cusCorCus.getCorState())){
				String stateOld = "";
				String stateNew = "";
				switch(cusCorCus1.getCorState()){
					case 0: stateOld = "待开发";break;
					case 1: stateOld = "开发中";break;
					case 2: stateOld = "已归属";break;
				}
				switch(cusCorCus.getCorState()){
					case 0: stateNew = "待开发";break;
					case 1: stateNew = "开发中";break;
					case 2: stateNew = "已归属";break;
				}
				record += "客户状态由&nbsp;&nbsp;"+stateOld+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+stateNew+"&nbsp;&nbsp;";
			}
			if(cusCorCus1.getPerson()== null && seNo != ""){
				record += "负责人由&nbsp;&nbsp;空&nbsp;&nbsp;改为&nbsp;&nbsp;"+empBiz.findPersonById(Long.parseLong(seNo)).getUserName()+"&nbsp;&nbsp;";
			}else if(cusCorCus1.getPerson()!= null && seNo == ""){
				record += "负责人由&nbsp;&nbsp;"+cusCorCus1.getPerson().getUserName()+"&nbsp;&nbsp;改为&nbsp;&nbsp;空&nbsp;&nbsp;";
			}else if(cusCorCus1.getPerson()!=null && !String.valueOf(cusCorCus1.getPerson().getSeqId()).equals(seNo)){
				//if(!cusCorCus1.getSalEmp().getSeNo().equals(seNo)){
					record += "负责人由&nbsp;&nbsp;"+cusCorCus1.getPerson().getUserName()+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+empBiz.findPersonById(Long.parseLong(seNo)).getUserName()+"&nbsp;&nbsp;";
				//}
			}
			if(cusCorCus1.getCorOnDate() == null && onDate!=""){
				record += "到期日期由&nbsp;&nbsp;空&nbsp;&nbsp;改为&nbsp;&nbsp;"+onDate+"&nbsp;&nbsp;";
			}else if(cusCorCus1.getCorOnDate() != null && onDate == ""){
				record += "到期日期由&nbsp;&nbsp;"+cusCorCus1.getCorOnDate().toString().substring(0, 10)+"&nbsp;&nbsp;改为&nbsp;&nbsp;空&nbsp;&nbsp;";
			}else if(cusCorCus1.getCorOnDate()!=null &&!cusCorCus1.getCorOnDate().toString().substring(0, 10).equals(onDate)){	
				record += "到期日期由&nbsp;&nbsp;"+cusCorCus1.getCorOnDate().toString().substring(0, 10)+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+onDate+"&nbsp;&nbsp;";
			}
			if(!record.equals("")){
				recLog = this.getPersonName(request)+"&nbsp;&nbsp;于&nbsp;&nbsp;"+df.format(new Date())+"&nbsp;&nbsp;将&nbsp;&nbsp;"+record +"</br>";
			}
			if(StringFormat.isEmpty(countryId1)) {
				countryId1 = "1";
			}
			if(StringFormat.isEmpty(provinceId1)) {
				provinceId1 = "1";
			}
			if(StringFormat.isEmpty(cityId1)) {
				cityId1 = "1";
			}
			CusArea cusArea = new CusArea(Long.valueOf(countryId1));
			CusProvince cusProvince = new CusProvince(Long.valueOf(provinceId1));
			CusCity cusCity = new CusCity(Long.valueOf(cityId1));
			if(!StringFormat.isEmpty(cType)) {
				cusCorCus.setCusType(new TypeList(Long.valueOf(cType)));
			}
			if(!StringFormat.isEmpty(industryId)){
				cusCorCus.setCusIndustry(new TypeList(Long.parseLong(industryId)));
			}
			if(!StringFormat.isEmpty(souId)) {
				cusCorCus.setCorSou(new TypeList(Long.valueOf(souId)));
			}
			if(!StringFormat.isEmpty(seNo)){
				cusCorCus.setPerson(new YHPerson(Long.parseLong(seNo)));
			}
//			if(!cusCorCus.getCorBeginBal().equals(cusCorCus1.getCorBeginBal())){
//				cusCorCus.setCorRecvAmt(cusCorCus1.getCorRecvAmt()+(cusCorCus.getCorBeginBal()-cusCorCus1.getCorBeginBal()));
//			}
//			else{
//				cusCorCus.setCorRecvAmt(cusCorCus1.getCorRecvAmt());
//			}
			if(onDate!=null && !onDate.equals("")){
				cusCorCus.setCorOnDate(GetDate.formatDate(onDate));
			}else{
				cusCorCus.setCorOnDate(null);
			}
			cusCorCus.setCorBeginBal(cusCorCus1.getCorBeginBal());
			cusCorCus.setCorRecvAmt(cusCorCus1.getCorRecvAmt());
			cusCorCus.setCorCode(cusCorCus1.getCorCode());
			cusCorCus.setCusArea(cusArea);
			cusCorCus.setCusProvince(cusProvince);
			cusCorCus.setCusCity(cusCity);
			cusCorCus.setCorUpdUser(this.getPersonName(request));
			cusCorCus.setCorUpdDate(GetDate.getCurTime());
			cusCorCus.setCorNum(cusCorCus1.getCorNum());
			cusCorCus.setCorInsUser(cusCorCus1.getCorInsUser());
			cusCorCus.setCorCreatDate(cusCorCus1.getCorCreatDate());
			cusCorCus.setCorIsdelete(cusCorCus1.getCorIsdelete());
			cusCorCus.setCorIssuc(cusCorCus1.getCorIssuc());
			cusCorCus.setCorLastDate(cusCorCus1.getCorLastDate());
			cusCorCus.setCorTempTag(cusCorCus1.getCorTempTag());
			cusCorCus.setCorAssignCont(cusCorCus1.getCorAssignCont());
			cusCorCus.setCorAssignDate(cusCorCus1.getCorAssignDate());
			if(cusCorCus1.getCorStateRecord() != null && !cusCorCus1.getCorStateRecord().equals("")){
				cusCorCus.setCorStateRecord(recLog+cusCorCus1.getCorStateRecord());
			}else{
				cusCorCus.setCorStateRecord(recLog);
			}
			customBIZ.update(cusCorCus);
			request.setAttribute("msg", "修改客户");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 跳转到某客户的订单 <br>
	 * create_date: Mar 22, 2011,3:46:31 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cusId:客户ID
	 * @param response
	 * @return ActionForward showCusOrder<br>
	 * 		attribute > cusId:客户ID
	 */
	public ActionForward toListCusOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String cusName = request.getParameter("cusName");
		request.setAttribute("cusId", cusId);
		request.setAttribute("cusName", cusName);
		return mapping.findForward("showCusOrder");
	}
	/**
	 * 获得某客户的订单 <br>
	 * create_date: Aug 9, 2010,5:56:52 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > cusId:客户ID p:当前页码 pageSize:每页显示的条数  orderCol:排序字段  isDe:是否逆序
	 * @param response
	 * @return ActionForward 跳转到showCusOrder页面<br>
	 *         attribute > count:订单数 sumMon:订单总金额 paidMon:已回款金额  cusId:客户ID
	 *         			   ordList:符合条件的订单列表 page:分页信息  orderCol:排序字段  isDe:是否逆序
	 */
	public void getCusOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String orderCol = request.getParameter("orderCol");
		String p = request.getParameter("p");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize"); 
		String orderItem = "";
		String [] args = {"0",cusId};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30";//默认每页条数
		}
		String[] items = { "appState","ordState","oType", "oTil", "oNum", "oCus","oPaid", "oMon", "oConDate","sodComiteDate","oEmp"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(orderBiz.listOrdersCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<SalOrdCon> list = orderBiz.listOrders(args,orderItem,isDe, page
				.getCurrentPageNo(), page.getPageSize());
		
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

	/**
	 * 跳转到对应联系人页面 <br>
	 * create_date: Mar 21, 2011,3:47:46 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cusId:客户Id
	 * @param response
	 * @return ActionForward 跳转到对应联系人页面<br>
	 * 		attribute >  cusId:客户Id
	 */
	public ActionForward toListCusCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("showCusCon");
	}
	/**
	 * 获得某客户下的所有联系人 <br>
	 * create_date: Aug 10, 2010,9:29:32 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > cusId:客户ID  p:当前页码 pageSize:每页显示的条数  orderCol：排序字段   isDe:是否逆序
	 * @param response
	 * @return ActionForward 跳转到showCusCon页面<br>
	 *         attribute > conList:符合条件的联系人列表 page:分页信息 cusId:客户ID   orderCol：排序字段   isDe:是否逆序
	 */
	public void getCusCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] args = {cusId};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"conName","conSex","conLev","conDep","conPos","conPhone","conWorkPho","conEmail","conQq"};
		if(orderCol!= null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listContactsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));

		List list = customBIZ.listContacts(args, orderItem, isDe, page
				.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
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
	 * 查看客户详情<br>
	 * @param request
	 * 		para >	corCode:客户ID
	 * 		attr > 	cusCorCusInfo:客户实体
	 * @return ActionForward showCompanyCusDesc
	 */
	public ActionForward showCompanyCusDesc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String corCode = request.getParameter("corCode");
		String flag = request.getParameter("flag");
		CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(corCode);
//		Double cusToPaid = customBIZ.getCusToPaid(corCode);
		/*boolean flag = true;
		if (cusCorCus != null && cusCorCus.getSalOrdCons() != null) {
			Set list = cusCorCus.getSalOrdCons();
			Iterator<SalOrdCon> iter = list.iterator();
			while (iter.hasNext()) {
				SalOrdCon ordCon = iter.next();
				if (ordCon.getSodIsfail().equals("0"))
					flag = false;
			}
			if (flag) {
				cusCorCus.setCorIssuc("0");
				customBIZ.update(cusCorCus);
			}
		}*/
		request.setAttribute("cusCorCusInfo", cusCorCus);
		request.setAttribute("flag", flag);
//		request.setAttribute("cusToPaid", cusToPaid);
		return mapping.findForward("showCompanyCusDesc");
	}
	
	
	/**
	 * 查看管辖区域内的客户详情<br>
	 * @param request
	 * 		para >	corCode:客户ID
	 * 		attr > 	cusCorCusInfo:客户实体
	 * @return ActionForward showCompanyCusDesc
	 */
	public ActionForward showMyCompanyCusDesc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String corCode = request.getParameter("corCode");
		CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(corCode);
		request.setAttribute("cusCorCusInfo", cusCorCus);
		return mapping.findForward("showMyCompanyCusDesc");
	}

	/**
	 * 跳转到联系人编辑页面 <br>
	 * @param request
	 *         parameter > isEdit:值为2表示详情页面里的编辑值为1表示在列表页面编辑 id:联系人ID
	 * @param response
	 * @return ActionForward 跳转到showEditContact页面<br>
	 *         attribute > isEdit:标识是否在详情页面编辑 contact:联系人实体
	 */
	public ActionForward toUpdCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		
		CusContact contact = customBIZ.showContact(id);
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("contact", contact);
		return mapping.findForward("showEditContact");
	}

	/**
	 * 显示联系人详细信息跳转 <br>
	 * @param request
	 *         parameter > id:联系人ID
	 * @param response
	 * @return ActionForward 跳转到showDescContact页面<br>
	 *         attribute > contact:联系人实体
	 */
	public ActionForward showContactInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		CusContact contact = customBIZ.showContact(id);
		request.setAttribute("contact", contact);
		return mapping.findForward("showDescContact");
	}
	/**
	 * 显示产品策略详细信息跳转 <br>
	 * @param request
	 *         parameter > id:联系人ID
	 * @param response
	 * @return ActionForward 跳转到showDescContact页面<br>
	 *         attribute > contact:联系人实体
	 */
	public ActionForward showCusProd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		CusProd cusProd = customBIZ.showCusProd(id);
		request.setAttribute("cusProd", cusProd);
		return mapping.findForward("showDescCusProd");
	}
	/**
	 * 跳转到联系人新建页面 <br>
	 * @param request
	 *         parameter > cusId:客户ID
	 * @param response
	 * @return ActionForward 跳转到contact页面，在详情下新建时如果客户被删除将跳转到error页面<br>
	 *         attribute > cusCorCusInfo:客户实体（客户详情下的新建）
	 *         attribute > errorMsg:错误信息
	 */
	public ActionForward toNewContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCusInfo = customBIZ.getCusCorCusInfo(cusId);
			if(cusCorCusInfo != null){
				request.setAttribute("cusCorCusInfo", cusCorCusInfo);
			}
			else {
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的联系人！");
				return mapping.findForward("error");
			}
		}
		return mapping.findForward("contact");
	}

	/**
	 * 跳转到确认删除 <br>
	 * @param request
	 *         parameter > code:要删除的实体ID delType:删除类型
	 * @param response
	 * @return ActionForward 跳转到delConfirm页面<br>
	 *         attribute > code:要删除的实体ID delType:删除类型
	 */
	public ActionForward delConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String delType = request.getParameter("delType");

		request.setAttribute("code", code);
		request.setAttribute("delType", delType);
		return mapping.findForward("delConfirm");
	}

	/**
	 * 跳转到删除，恢复确认页面 <br>
	 * @param request
	 *         parameter > type:标识是删除还是恢复 code:要删除的或恢复的实体ID delType:删除或恢复的类型
	 * @param response
	 * @return ActionForward 跳转到recConfirm页面<br>
	 *         attribute > type:标识是删除还是恢复 code:要删除的或恢复的实体ID delType:删除或恢复的类型
	 */
	public ActionForward recConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String delType = request.getParameter("delType");
		String type = request.getParameter("type");
		
		if ( type!= null && !type.equals("")) {
			request.setAttribute("type", type);
		}
		request.setAttribute("code", code);
		request.setAttribute("delType", delType);
		return mapping.findForward("recConfirm");
	}

	/**
	 * 批量删除 <br>
	 * @param request
	 *         parameter > type:标识删除的是已收消息还是已发消息值为ac表示已收消息值为send表示已发消息
	 *            		   delType:删除类型
	 * @param response
	 * @return ActionForward 跳转到batchDelConfirm页面<br>
	 *         attribute > type:标识删除的是已收消息还是已发消息 delType:删除类型
	 */

	public ActionForward batchDelConfirm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("type") != null) {
			String type = request.getParameter("type");
			request.setAttribute("type", type);
		}
		// String code = request.getParameter("code");
		String delType = request.getParameter("delType");

		// request.setAttribute("code", code);
		request.setAttribute("delType", delType);
		return mapping.findForward("batchDelConfirm");
	}

	/**
	 * 跳转到修改计划执行日期 <br>
	 * @param request
	 *         parameter > praExeDate:行为执行日期 code:行为ID
	 * @param response
	 * @return ActionForward 跳转到modifyExeDate页面<br>
	 *         attribute > code:行为ID
	 */
	public ActionForward modifyExeDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("praExeDate") != null) {
			String praExeDate = request.getParameter("praExeDate");
			request.setAttribute("praExeDate", praExeDate);
		}
		String code = request.getParameter("code");
		request.setAttribute("code", code);
		return mapping.findForward("modifyExeDate");
	}

	/**
	 * 删除客户（新） <br>
	 * create_date: Aug 10, 2010,11:59:37 AM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > cusCode:客户ID
	 * @param response
	 * @return ActionForward 不符合删除条件的跳转到error页面 删除成功跳转到popDivSuc页面<br>
	 *         attribute > 不符合删除条件 errorMsg:不符合删除条件信息提示<br>
	 *         attribute > 删除成功 msg:删除成功信息提示<br>
	 */
	public ActionForward deleteCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusCode = request.getParameter("cusCode");
		CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(cusCode);

		boolean isDel = true;
		StringBuffer eMsg = new StringBuffer();
		
		if(cusCorCus.getSalOrdCons()!=null){
			int count = 0;
			int countDel = 0;
			Set<SalOrdCon> list = cusCorCus.getSalOrdCons();
			Iterator<SalOrdCon> it = list.iterator();
			while(it.hasNext()){
				SalOrdCon salOrdCon = it.next();
				if(salOrdCon.getSodIsfail().equals("0")){
					count++;
				}
				else{
					countDel++;
				}
			}
			if(count > 0 || countDel > 0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg,count,countDel,"订单");
			}
		}
		if(cusCorCus.getCusContacts()!=null){
			int count = 0;
			Set<CusContact> list = cusCorCus.getCusContacts();
			if(list!=null){
				count = list.size();
			}
			if(count > 0 ){
				isDel = false;
				StringFormat.getDelEMsg(eMsg, count, 0, "联系人");
			}
		}
		if(cusCorCus.getSalPras()!=null){
			int count = 0;
			int countDel = 0;
			Set<SalPra> list = cusCorCus.getSalPras();
			Iterator<SalPra> it = list.iterator();
			while(it.hasNext()){
				SalPra salPra = it.next();
				if(salPra.getPraIsDel().equals("1")){
					countDel++;
				}
				else{
					count++;
				}
			}
			if(count > 0 || countDel > 0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg, count, countDel, "来往记录");
			}
		}
		if(cusCorCus.getSalOpps()!=null){
			int count = 0;
			int countDel = 0;
			Set<SalOpp> list = cusCorCus.getSalOpps();
			Iterator<SalOpp> it = list.iterator();
			while(it.hasNext()){
				SalOpp salOpp = it.next();
				if(salOpp.getOppIsDel().equals("1")){
					count++;
				}
				else{
					countDel++;
				}
			}
			if(count > 0 || countDel > 0 ){
				isDel = false;
				StringFormat.getDelEMsg(eMsg, count, countDel, "销售机会");
			}
		}
		if(cusCorCus.getCusServs()!=null){
			int count = 0;
			int countDel = 0;
			Set<CusServ> list = cusCorCus.getCusServs();
			Iterator<CusServ> it = list.iterator();
			while(it.hasNext()){
				CusServ cusServ = it.next();
				if(cusServ.getSerIsDel().equals("1")){
					count++;
				}
				else{
					countDel++;
				}
			}
			if(count > 0 || countDel > 0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg, count, countDel, "客服服务");
			}
		}
		
		if(isDel){
			cusCorCus.setCorIsdelete("0");
			customBIZ.update(cusCorCus);
			request.setAttribute("msg", "删除客户");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg", "请先删除该客户下的关联数据："+eMsg.toString());
			return mapping.findForward("error");
		}
	}

	/**
	 * 恢复客户 <br>
	 * create_date: Aug 10, 2010,1:39:37 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > cusCode:客户ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg;恢复成功信息提示
	 */
	public ActionForward recCorCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long cusCode = Long.valueOf(request.getParameter("cusCode"));
		try {
			CusCorCus cusCorCus = customBIZ.findCus(cusCode);
			cusCorCus.setCorIsdelete("1");
			customBIZ.update(cusCorCus);
		} catch (Exception e) {
			System.out.println("恢复失败!");
		}
		request.setAttribute("msg", "恢复客户");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 彻底删除客户不可恢复 <br>
	 * create_date: Aug 10, 2010,1:41:16 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > cusCode:客户ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示
	 */
	public ActionForward delCorCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long cusCode = Long.valueOf(request.getParameter("cusCode"));
		CusCorCus cusCorCus = customBIZ.findCus(cusCode);
		Iterator it = cusCorCus.getAttachments().iterator();
		// 删除关联附件
		while (it.hasNext()) {
			Attachment att = (Attachment) it.next();
			FileOperator.delFile(att.getAttPath(), request);
		}
		customBIZ.delCusCorCus(cusCorCus);
		request.setAttribute("msg", "删除客户");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除联系人 <br>
	 * create_date: Aug 10, 2010,1:43:11 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:联系人ID  isIfrm:是否在详情页面中删除若为1则是在详情页面中删除 
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:删除成功信息提示  isIfrm:是否在详情页面中删除若为1则是在详情页面中删除 
	 */
	 /*public ActionForward delContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		String isIfrm = request.getParameter("isIfrm");
		
		CusContact contact = customBIZ.showContact(id);
		contact.setConIsDel("0");
		customBIZ.updateContact(contact);
		
		//详情下刷新iframe
		if ( isIfrm != null
				&& isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		
		request.setAttribute("msg", "删除联系人");
		return mapping.findForward("popDivSuc");
	}*/

	/**
	 * 恢复联系人 <br>
	 * create_date: Aug 10, 2010,1:44:32 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:联系人ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:恢复成功信息提示
	 */
	/*public ActionForward recContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		try {
			CusContact contact = customBIZ.showContact(id);
			contact.setConIsDel("1");
			customBIZ.updateContact(contact);
		} catch (Exception e) {
			System.out.println("恢复失败!");
		}
		request.setAttribute("msg", "恢复联系人");
		return mapping.findForward("popDivSuc");
	}*/

	/**
	 * 删除联系人<br>
	 * @param request
	 * 		para >	id:联系人ID;
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 */
	public ActionForward delCusContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		CusContact contact = customBIZ.showContact(id);
		customBIZ.delContact(contact);
		request.setAttribute("msg", "删除联系人");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 贴标签 <br>
	 * @param request
	 *         parameter > code:客户ID
	 * @param response
	 * @return ActionForward null 未贴标签输出redMark+客户ID 已贴标签输出grayMark+客户ID<br>
	 */
	public ActionForward addTempTag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		PrintWriter out = null;
		String tempTag = "";
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (code != null) {
				CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(code);
				tempTag = cusCorCus.getCorTempTag();
				if (tempTag.equals("0")) {
					cusCorCus.setCorTempTag("1");
					customBIZ.update(cusCorCus);
					out.print("redMark" + "," + code + "," + "");
					out.flush();
				}
				if (tempTag.equals("1")) {
					cusCorCus.setCorTempTag("0");
					customBIZ.update(cusCorCus);
					out.print("grayMark" + "," + code + "," + "");
					out.flush();
				}

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	/**
	 * 联系人标记
	 * @param request
	 * 		para >	conId:联系人ID;
	 */
	public void addConMark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String conId = request.getParameter("conId");
		PrintWriter out = null;
		String isCons = "";
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (!StringFormat.isEmpty(conId)) {
				CusContact cusCon = customBIZ.showContact(Long.parseLong(conId));
				isCons = cusCon.getConIsCons();
				if (StringFormat.isEmpty(isCons)||isCons.equals(CusContact.M_UNCONS)) {
					cusCon.setConIsCons(CusContact.M_CONS);
					customBIZ.updateContact(cusCon);
					out.print(CusContact.M_CONS + "," + conId );
					out.flush();
				}
				else if (isCons.equals(CusContact.M_CONS)) {
					cusCon.setConIsCons(CusContact.M_UNCONS);
					customBIZ.updateContact(cusCon);
					out.print(CusContact.M_UNCONS + "," + conId );
					out.flush();
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}

	/**
	 * 添加国家 <br>
	 * create_date: Aug 10, 2010,1:50:56 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:添加成功提示信息
	 */
	public ActionForward addCountry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusArea cusArea = (CusArea) form1.get("cusArea");
		customBIZ.saveCountry(cusArea);
		request.setAttribute("msg", "添加国家");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 获得所有国家 <br>
	 * create_date: Aug 10, 2010,1:52:34 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > msg:标识是否成功修改
	 * @param response
	 * @return ActionForward 跳转到showCountry页面<br>
	 *         attribute > list.size()<=1 isEmpty:标识页面上的保存按钮是否显示
	 *         		   	   typeList:所有国家列表<br>
	 *         attribute > isMsg不为空且值为1 msg:标识是否成功修改 typeList:所有国家列表<br>
	 */
	public ActionForward getAllCountry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String isMsg = request.getParameter("msg");
		List list = customBIZ.getAllCusArea();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CusArea cusArea = (CusArea) it.next();
			if (cusArea.getAreIsenabled().equals("1")) {
				cusArea.setEnabledType("checked");
			}
		}
		if (list.size() <= 1) {
			request.setAttribute("isEmpty", "1");
		}
		if (isMsg != null && isMsg.equals("1")) {
			request.setAttribute("msg", 1);
		}
		request.setAttribute("typeList", list);
		return mapping.findForward("showCountry");
	}

	/**
	 * 修改国家信息 <br>
	 * create_date: Aug 10, 2010,2:05:41 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > typeId:要修改的国家ID数组 typeName:要修改的国家名称数组
	 *            enabledType:被选择启用的国家ID数组
	 * @param response
	 * @return ActionForward null 重定向到customAction.do?op=getAllCountry&msg=1<br>
	 */
	public ActionForward modifyCountry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] typeIds = request.getParameterValues("typeId");
		String[] typeNames = request.getParameterValues("typeName");
		// for (int i = 0; i < typeNames.length; i++) {
		// typeNames[i] = TransStr.transStr(typeNames[i]);
		// }
		String[] enabledIds = request.getParameterValues("enabledType");
		modType.modType("CusArea", "are", typeNames, typeIds, enabledIds);
		String url = "customAction.do?op=getAllCountry&msg=1";
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到添加省份页面 <br>
	 * create_date: Aug 10, 2010,2:15:38 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:国家ID
	 * @param response
	 * @return ActionForward 跳转到addProvince页面<br>
	 *         attribute > 国家ID不为空 area:国家实体<br>
	 *         attribute > 国家ID为空 area:所有国家列表<br>
	 */
	public ActionForward toAddPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = null;
		if (request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		// 添加下级省份
		if (id != null) {
			CusArea area = customBIZ.showCountry(Long.parseLong(id));
			request.setAttribute("area", area);
		}
		// 新添省份
		else {
			List areaList = customBIZ.getCusArea();
			request.setAttribute("areaList", areaList);
		}
		return mapping.findForward("addProvince");
	}

	/**
	 * 添加省份 <br>
	 * create_date: Aug 10, 2010,2:18:19 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:国家ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:添加成功提示信息
	 */
	public ActionForward addProvince(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusProvince cusProvince = (CusProvince) form1.get("cusProvince");
		Long id = Long.valueOf((String) request.getParameter("id"));
		cusProvince.setCusArea(new CusArea(id));
		customBIZ.saveProvince(cusProvince);
		request.setAttribute("msg", "添加省份");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 获得所有省 <br>
	 * create_date: Aug 10, 2010,2:19:52 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > msg:标识是否成功修改
	 * @param response
	 * @return ActionForward 跳转到showProvince页面<br>
	 *         attribute > list.size()<=1 isEmpty:标识页面上的保存按钮是否显示
	 *         		   	   typeList:所有省份列表<br>
	 *         attribute > isMsg不为空且值为1 msg:标识是否成功修改 typeList:所有省份列表<br>
	 */
	public ActionForward getAllProvince(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String isMsg = request.getParameter("msg");
		List list = customBIZ.getAllProvince();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CusProvince cusProvince = (CusProvince) it.next();
			if (cusProvince.getPrvIsenabled().equals("1")) {
				cusProvince.setEnabledType("checked");
			}
		}
		if (list.size() <= 1) {
			request.setAttribute("isEmpty", "1");
		}
		if (isMsg != null && isMsg.equals("1")) {
			request.setAttribute("msg", "1");
		}
		request.setAttribute("typeList", list);
		return mapping.findForward("showProvince");
	}

	/**
	 * 修改省的信息 <br>
	 * create_date: Aug 10, 2010,2:52:58 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > typeId:要修改的省ID数组 typeName:要修改的省名称数组
	 *            		   enabledType:被选择启用的省ID数组
	 * @param response
	 * @return ActionForward null 重定向到customAction.do?op=getAllProvince&msg=1<br>
	 */
	public ActionForward modifyProvince(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] typeIds = request.getParameterValues("typeId");
		String[] typeNames = request.getParameterValues("typeName");
		String[] enabledIds = request.getParameterValues("enabledType");
		modType.modType("CusProvince", "prv", typeNames, typeIds, enabledIds);
		String url = "customAction.do?op=getAllProvince&msg=1";
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到添加城市 <br>
	 * create_date: Aug 10, 2010,2:54:49 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:省ID
	 * @param response
	 * @return ActionForward 跳转到addCity页面<br>
	 *         attribute > 省ID不为空 province:省实体<br>
	 *         attribute > 省ID为空 areaList:所有国家列表<br>
	 */
	public ActionForward toAddCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = null;
		if (request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		// 添加下级城市
		if (id != null) {
			CusProvince province = customBIZ.showProvince(Long.parseLong(id));
			request.setAttribute("province", province);
		}
		// 新添城市
		else {
			List areaList = customBIZ.getCusArea();
			request.setAttribute("areaList", areaList);
		}
		return mapping.findForward("addCity");
	}

	/**
	 * 添加城市 <br>
	 * create_date: Aug 10, 2010,2:18:19 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > id:省ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 *         attribute > msg:添加成功提示信息
	 */
	public ActionForward addCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CusCity cusCity = (CusCity) form1.get("cusCity");
		Long id = Long.valueOf((String) request.getParameter("id"));
		// String areName=request.getParameter("areName");
		// request.setAttribute("areName", areName);
		cusCity.setCusProvince(new CusProvince(id));
		customBIZ.saveCity(cusCity);
		request.setAttribute("msg", "添加城市");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 获得所有城市 <br>
	 * create_date: Aug 10, 2010,2:19:52 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > msg:标识是否成功修改
	 * @param response
	 * @return ActionForward 跳转到showCity页面<br>
	 *         attribute > list.size()<=1 isEmpty:标识页面上的保存按钮是否显示
	 *         			   typeList:所有城市列表<br>
	 *         attribute > isMsg不为空且值为1 msg:标识是否成功修改 typeList:所有城市列表<br>
	 */
	public ActionForward getAllCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String isMsg = request.getParameter("msg");
		List list = customBIZ.getAllCity();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CusCity cusCity = (CusCity) it.next();
			if (cusCity.getCityIsenabled().equals("1")) {
				cusCity.setEnabledType("checked");
			}
		}
		if (list.size() <= 1) {
			request.setAttribute("isEmpty", "1");
		}
		if (isMsg != null && isMsg.equals("1")) {
			request.setAttribute("msg", "1");
		}
		request.setAttribute("typeList", list);
		return mapping.findForward("showCity");
	}

	/**
	 * 修改城市信息 <br>
	 * create_date: Aug 10, 2010,2:52:58 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > typeId:要修改的市ID数组 typeName:要修改的市名称数组
	 *            		   enabledType:被选择启用的市ID数组
	 * @param response
	 * @return ActionForward null 重定向到customAction.do?op=getAllCity&msg=1<br>
	 */
	public ActionForward modifyCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] typeIds = request.getParameterValues("typeId");
		String[] typeNames = request.getParameterValues("typeName");
		// for (int i = 0; i < typeNames.length; i++) {
		// typeNames[i] = TransStr.transStr(typeNames[i]);
		// }
		String[] enabledIds = request.getParameterValues("enabledType");
		modType.modType("CusCity", "city", typeNames, typeIds, enabledIds);
		String url = "customAction.do?op=getAllCity&msg=1";
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 批量删除联系人 <br>
	 * @param request
	 * 		para >	code:所有待删除的联系人ID
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 */
	public ActionForward batchDelContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String code = request.getParameter("code");
		batchOperate.recBatchDel("CusContact","conId", code);
		request.setAttribute("msg", "批量删除联系人");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到获得待删除的联系人 <br>
	 * create_date: Mar 24, 2011,5:17:13 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward recContact <br>
	 */
	public ActionForward toListDelContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recContact");
	}
	/**
	 * 获得待删除的联系人 <br>
	 * create_date: Aug 10, 2010,3:36:41 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         para > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recContact页面<br>
	 *         attr > contactList:符合条件的联系人列表 page:分页信息
	 */
	/*public void findDelContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(customBIZ.findDelContactCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = customBIZ.findDelContact(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
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
		request.setAttribute("contactList", list);
		request.setAttribute("page", page);
		return mapping.findForward("recContact");

	}*/

	/**
	 * 跳转到联系人，销售机会，行为高级查询页面 <br>
	 * create_date: Aug 10, 2010,3:38:04 PM
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 *         parameter > searInfo:页面跳转标识 range:标识查询范围0或1表示查询我的及我的下属联系人2表示全部联系人
	 * @param response
	 * @return ActionForward 若searInfo值为con跳转到conSupSearch页面
	 *         值为opp跳转到oppSupSearch页面 值为pra跳转到praSupSearch页面<br>
	 *         attribute > searInfo值为con orgList:我的及我的下级部门员工列表 range:标识查询范围0或1表示查询我的及我的下属联系人2表示全部联系人<br>
	 *         attribute > searInfo值为opp oppStageList:机会阶段列表
	 *         			   orgList:我的及我的下级部门员工列表<br>
	 *         attribute > searInfo值为pra orgList:我的及我的下级部门员工列表<br>
	 */
	public ActionForward findSupSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String searInfo = request.getParameter("searInfo");
		String userNum = (String) request.getSession().getAttribute("userNum");
		String range=request.getParameter("range");
		request.setAttribute("range",range);
		if(range!=null&&range.equals("2")){
			List orgList = empBiz.getAllOrg();
			request.setAttribute("orgList", orgList);
			request.setAttribute("orgTopCode", EmpAction.getORGTOPCODE());
		}
		else{
			List orgList = empBiz.getMyOrg(userNum);
			request.setAttribute("orgList", orgList);
			request.setAttribute("orgTopCode", EmpAction.getORGTOPCODE());
		}
		if (searInfo.equals("con")) {
			return mapping.findForward("conSupSearch");
		}
		if (searInfo.equals("opp")) {
			List<TypeList> oppStageList = typeListBiz.getEnabledType("3");
			request.setAttribute("oppStageList", oppStageList);
			return mapping.findForward("oppSupSearch");
		}
		return null;
	}


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

		if(type.equals("cus")){
			batchOperate.recBatchDel("CusCorCus","corCode",code);
			request.setAttribute("msg", "批量删除客户");
		}else if(type.equals("cont")){
			batchOperate.recBatchDel("CusContact","conId",code);
			request.setAttribute("msg", "批量删除联系人");
		}else if(type.equals("opp")){
			batchOperate.recBatchDel("SalOpp","oppId",code);
			request.setAttribute("msg", "批量删除销售机会");
		}else if(type.equals("pra")){
			batchOperate.recBatchDel("SalPra","praId",code);
			request.setAttribute("msg", "批量删除来往记录");
		}else if(type.equals("serv")){
			batchOperate.recBatchDel("CusServ","serCode",code);
			request.setAttribute("msg", "批量删除客户服务");
		}else if(type.equals("report")){
			batchOperate.recBatchDel("Report","repCode",code);
			request.setAttribute("msg", "批量删除已发报告");
		}else if(type.equals("task")){
			batchOperate.recBatchDel("SalTask","stId",code);
			request.setAttribute("msg", "批量删除工作安排");
		}else if(type.equals("emp")){
			batchOperate.recBatchDel("SalEmp","seNo",code);
			request.setAttribute("msg", "批量删除员工档案");
		}
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到客户分配页面<br>
	 * @return ActionForward batchAssign
	 */
	public ActionForward toAssignCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("type", "cus");
		return mapping.findForward("batchAssign");
	}
	/**
	 * 跳转到联系人分配页面<br>
	 * @return ActionForward batchAssign
	 */
	public ActionForward toAssignCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("type", "con");
		return mapping.findForward("batchAssign");
	}
	
	/**
	 * 分配客户(联系人) <br>
	 * @param request
	 * 		para >	ids:客户/联系人ID集合;		seNo:负责人Id;	type:更新类型;	corState:客户状态;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward batchAssign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String ids = request.getParameter("ids");
		String seNo = request.getParameter("seNo");
		String type = request.getParameter("type");
		String cusState = request.getParameter("corState");
		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		
		ids = ids.substring(0,ids.length()-1);
	    if(type.equals("cus")){
	    	customBIZ.batchAssignCus(ids, cusState, seNo, limUser);
	    	request.setAttribute("msg", "修改客户状态");
	    }else if(type.equals("cont")){
	    	customBIZ.batchAssignCon(ids, seNo);
	    	request.setAttribute("msg", "分配联系人");
	    }
		return mapping.findForward("popDivSuc"); 
	}
	
	/**
	 * 跳转到批量标色页面 <br>
	 * @return ActionForward batchColor <br>
	 */
	public ActionForward toBatchColor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("batchColor");
	}
	
	/**
	 *  批量标色<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > ids:客户Ids  corColor:标记色
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg:成功信息
	 */
	public ActionForward batchColor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String ids = request.getParameter("ids");
		String color = request.getParameter("corColor");
		
		customBIZ.batchColor(ids, color);
    	request.setAttribute("msg", "批量标色");
    	return mapping.findForward("popDivSuc"); 
	}
	
/* 导出 */
	/**
	 * 导出客户<br>
	 * @param request
	 * 		para >	range:0我的客户,1 全部客户;	corName:客户名称;		corNum:客户编号;
	 * 				cType:客户类型;				corAdd:地址;
	 * 				startTime,endTime:创建日期;	seName:负责人;		cusInd:行业;
	 * 				type：导出类型;				filter:筛选条件;		color:标色; 
	 * @return ActionForward outData<br>
	 */
	public ActionForward toOutCus(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		String corName = TransStr.transStr(request.getParameter("corName"));
		String corNum = TransStr.transStr(request.getParameter("corNum"));
		String cType = request.getParameter("cType");
		String corAdd = TransStr.transStr(request.getParameter("corAdd"));
		String seName = TransStr.transStr(request.getParameter("seName"));
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String cusInd = request.getParameter("cusInd");
		String type = request.getParameter("type");
		String filter = request.getParameter("filter");
		String color = request.getParameter("color");
		
		request.setAttribute("color", color);
		request.setAttribute("filter", filter);
		request.setAttribute("range", range);
		request.setAttribute("corName", corName);
		request.setAttribute("corNum", corNum);
		request.setAttribute("cType", cType);
		request.setAttribute("corAdd", corAdd);
		request.setAttribute("seName", seName);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("cusInd", cusInd);
		request.setAttribute("type", type);
		request.setAttribute("outObj", "cus");//导出实体名
		return mapping.findForward("outData");
	}
	public void outCus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		String corName = request.getParameter("corName");
		String corNum = request.getParameter("corNum");
		String cType = request.getParameter("cType");
		String corAdd = request.getParameter("corAdd");
		String seName = request.getParameter("seName");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String cusInd = request.getParameter("cusInd");
		String type = request.getParameter("type");
		String filter = request.getParameter("filter");
		String color = request.getParameter("color");
		String cusIds = request.getParameter("cusIds");
		String dataCols = request.getParameter("dataCols");
		String colNames = request.getParameter("colNames");
		SalEmp salEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String curUserId = salEmp!=null?salEmp.getSeName():"";
		String cusIsDel = "1";
 		String[] args = {cusIsDel, range, curUserId, corName, corNum, cType, startTime,endTime,seName,corAdd,cusInd,filter, color };
 		String[][][] outData = customBIZ.getOutCus(type,cusIds,dataCols,colNames,args);
		new OutToExc(outData[0][0], outData[1],request).downloadXls(response);
	}
	
	/**
	 * 跳转到导出联系人页面<br>
	 * @param request
	 * 		para > 	range:0我的,1全部;	conName:联系人姓名;	cusName:对应客户名;	conLev:分类;
					seName:负责人;		type:导出类型;
	 * @return ActionForward outData<br>
	 */
	public ActionForward toOutCont(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		String conName = TransStr.transStr(request.getParameter("conName"));
		String cusName = TransStr.transStr(request.getParameter("cusName"));
		String conLev = request.getParameter("conLev");
		String seName = TransStr.transStr(request.getParameter("seName"));
		String type = request.getParameter("type");
		
		request.setAttribute("range", range);
		request.setAttribute("conName", conName);
		request.setAttribute("cusName", cusName);
		request.setAttribute("conLev", conLev);
		request.setAttribute("seName", seName);
		request.setAttribute("type", type);
		request.setAttribute("outObj", "cusCon");//导出实体名
		return mapping.findForward("outData");
	}
	public void outCont(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String range = request.getParameter("range");
		String conName = request.getParameter("conName");
		String cusName = request.getParameter("cusName");
		String conLev = request.getParameter("conLev");
		String seName = request.getParameter("seName");
		String type = request.getParameter("type");
		String conIds = request.getParameter("conIds");
		String dataCols = request.getParameter("dataCols");
		String colNames = request.getParameter("colNames");
		SalEmp salEmp = (SalEmp)request.getSession().getAttribute("CUR_EMP");
		String curUserId = salEmp!=null?salEmp.getSeName():"";
		String[] args = { range, curUserId, conName, cusName, conLev, seName };
 		String[][][] outData = customBIZ.getOutCon(type,conIds,dataCols,colNames,args);
		new OutToExc(outData[0][0], outData[1],request).downloadXls(response);
	}
	
	/**
	 * 跳转到客户产品<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > cusId:客户Id
	 * @param response
	 * @return ActionForward 跳转到listCusProd页面<br>
	 * 		attr >  cusId:客户Id
	 */
	public ActionForward toListCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusProd");
	}
	
	/**
	 * 客户产品列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > cusId:客户ID p:当前页码  orderCol:排序字段  pageSize:每页显示条数  isDe:是否逆序
	 */
	public void listCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String wprName = request.getParameter("wprName");
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] args  ={cusId,wprName};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"product","otherName","type","salPrice","cpPrice","cpPreNumber","cpWarnDay","cpSentDate","cpRemark"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCusProdCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = customBIZ.listCusProd(args, orderItem, isDe,  page
				.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		awareCollect.add("wmsProduct");
		awareCollect.add("wmsProduct.typeList");
		awareCollect.add("wmsProduct.productUnit");
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
	
	
	public ActionForward getDeskCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<CusProd> list = customBIZ.getDeskTip(); 
		
		request.setAttribute("cusProdList", list);
		return mapping.findForward("getDeskCusProd");
	}
	
	/**
	 * 跳转到新建客户产品<br>
	 * @param request
	 * 		para >	cusId:客户Id;	isIfrm:是否刷新iframe;
	 * @return ActionForward formCusProd
	 */
	public ActionForward toNewCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String isIfrm = request.getParameter("isIfrm");
		if (!StringFormat.isEmpty(cusId)) {
			CusCorCus cusCorCus = customBIZ.getCusCorCusInfo(cusId);
			if(cusCorCus != null){
				request.setAttribute("cusInfo", cusCorCus);
			}
			else {
				request.setAttribute("errorMsg","该客户不存在！");
				return mapping.findForward("error");
			}
		}
		request.setAttribute("isIfrm", isIfrm);
		return mapping.findForward("formCusProd");
	}
	
	/**
	 * 保存客户产品<br>
	 * @param request
	 * 		para >	cusId:客户Id;	wprId:产品Id;	isIfrm:是否刷新iframe;
	 * @return ActionForward popDivSuc 
	 */
	public ActionForward saveCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String wprId = request.getParameter("wprId");
		String isIfrm = request.getParameter("isIfrm");
		DynaActionForm form1 = (DynaActionForm) form;
		CusProd cusProd = (CusProd) form1.get("cusProd");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		cusProd.setCpCreDate(GetDate.getCurTime());
		cusProd.setCpCreUser(this.getPersonName(request));
		cusProd.setCusCorCus(new CusCorCus(Long.parseLong(cusId)));
		cusProd.setWmsProduct(new WmsProduct(wprId));
		customBIZ.saveCusProd(cusProd);
		
		// 详情下 刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建客户产品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 客户产品详情<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cpId:客户产品Id
	 * @param response
	 * @return ActionForward showCusProdDesc <br>
	 * 		attr > cusProd :客户产品
	 */
	public ActionForward showCusProdDesc(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cpId = request.getParameter("cpId");
		CusProd cusProd = customBIZ.findCusProdById(Long.parseLong(cpId));
		
		request.setAttribute("cusProd", cusProd);
		return mapping.findForward("showCusProdDesc");
	}
	
	/**
	 * 跳转到编辑客户产品 <br>
	 * @param request
	 * 		para >	cpId:客户产品Id;	isIfrm:是否刷新iframe;
	 * @return ActionForward formCusProd
	 */
	public ActionForward toUpdCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cpId = request.getParameter("cpId");
		String isIfrm = request.getParameter("isIfrm");
		CusProd cusProd = customBIZ.findCusProdById(Long.parseLong(cpId));
		
		request.setAttribute("cusProd", cusProd);
		request.setAttribute("isIfrm", isIfrm);
		return mapping.findForward("formCusProd");
	}
	
	/**
	 * 编辑客户产品<br>
	 * @param request
	 * 		para >	cpId:客户产品Id;	isIfrm:是否刷新Iframe;	cusId:客户Id;	wprId:产品Id;
	 * @return ActionForward popDivSuc<br>
	 */
	public ActionForward updCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String isIfrm = request.getParameter("isIfrm");
		String cusId = request.getParameter("cusId");
		String wprId = request.getParameter("wprId");
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		CusProd cusProd = (CusProd) form1.get("cusProd");
		CusProd oldCusProd = customBIZ.findCusProdById(cusProd.getCpId());
		cusProd.setCpUpdDate(GetDate.getCurTime());
		cusProd.setCpUpdUser(this.getPersonName(request));
		cusProd.setCusCorCus(new CusCorCus(Long.parseLong(cusId)));
		cusProd.setWmsProduct(new WmsProduct(wprId));
		cusProd.setCpCreDate(oldCusProd.getCpCreDate());
		cusProd.setCpCreUser(oldCusProd.getCpCreUser());
		customBIZ.updateCusProd(cusProd);
		// 详情下编辑 刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "编辑客户产品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除客户产品<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > cpId:客户产品Id isIfrm:是否刷新Iframe
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg:成功信息
	 */
	public ActionForward delCusProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cpId = request.getParameter("cpId");
		String isIfrm = request.getParameter("isIfrm");
		CusProd cusProd = customBIZ.findCusProdById(Long.parseLong(cpId));
		
		customBIZ.delete(cusProd);
		
		// 详情下新建 刷新iframe
		if (isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除客户产品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到客户地址<br>
	 * @param request
	 * 		para,attr >	cusId:客户Id
	 * @return ActionForward listCusAddr
	 */
	public ActionForward toListCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusAddr");
	}
	/**
	 * 客户地址列表<br>
	 * @param request
	 * 		para >	cusId:客户ID;	
	 */
	public void listCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"addr","area","postCode","contact","phone"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(customBIZ.listCusAddrCount(cusId),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<CusAddr> list = customBIZ.listCusAddr(cusId, orderItem, isDe,  page
				.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cadProv");
		awareCollect.add("cadCity");
		awareCollect.add("cadDistrict");
		
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
	
	public void listCusAddrAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		List<CusAddr> list = customBIZ.listCusAddr(cusId);
		ListForm listForm = new ListForm();
		listForm.setList(list);
		List awareCollect = new LinkedList();
		awareCollect.add("cadProv");
		awareCollect.add("cadCity");
		awareCollect.add("cadDistrict");
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
	 * 跳转到新建客户地址<br>
	 * @param request
	 * 		para,attr > cusId:客户Id;
	 * 		attr >	cusAreaList:省份列表; 	conList:已标记联系人列表;
	 * @return ActionForward formCusAddr
	 */
	public ActionForward toSaveCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		if (!StringFormat.isEmpty(cusId)) {
			CusCorCus cus = customBIZ.findCus(Long.parseLong(cusId));
			List<CusArea> cusAreaList = customBIZ.getCusArea();
			List<CusContact> conList = customBIZ.getAllConByMark(cusId);
			request.setAttribute("conList", conList);
			request.setAttribute("customer", cus);
			request.setAttribute("cusAreaList", cusAreaList);
			return mapping.findForward("formCusAddr");
		}
		else{
			request.setAttribute("errorMsg","该客户不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 保存客户地址<br>
	 * @param request
	 * 		para >	cusId:客户Id;	provId,cityId,districtId:省市区ID;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward saveCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		DynaActionForm form1 = (DynaActionForm) form;
		CusAddr cusAddr = (CusAddr) form1.get("cusAddr");
		if(!StringFormat.isEmpty(cusId)){
			cusAddr.setCadCus(new CusCorCus(Long.parseLong(cusId)));
			if (!StringFormat.isEmpty(provId)) { cusAddr.setCadProv(new CusArea(Long.parseLong(provId))); }
			if (!StringFormat.isEmpty(cityId)) { cusAddr.setCadCity(new CusProvince(Long.parseLong(cityId))); }
			if (!StringFormat.isEmpty(districtId)) { cusAddr.setCadDistrict(new CusCity(Long.parseLong(districtId))); }
			customBIZ.saveCusAddr(cusAddr);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "新建客户收货地址");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg","该客户不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 跳转到编辑客户地址 <br>
	 * @param request
	 * 		para > 	cadId:客户地址Id;
	 * 		attr >	cusAddr:地址实体;		cusAreaList:省份列表;		conList:已标记联系人列表;
	 * @return ActionForward formCusAddr<br>
	 */
	public ActionForward toUpdCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cadId = request.getParameter("cadId");
		CusAddr cusAddr = customBIZ.findCusAddrById(Long.parseLong(cadId));
		List<CusArea> cusAreaList = customBIZ.getCusArea();
		List<CusContact> conList = customBIZ.getAllConByMark(cusAddr.getCadCus().getCorCode().toString());
		request.setAttribute("conList", conList);
		request.setAttribute("cusAddr", cusAddr);
		request.setAttribute("cusAreaList", cusAreaList);
		return mapping.findForward("formCusAddr");
	}
	
	/**
	 * 编辑客户地址<br>
	 * @param request
	 * 		para >	cadId:客户地址Id;		cusId:客户Id		provId,cityId,districtId:省市区ID;
	 * @return ActionForward popDivSuc<br>
	 */
	public ActionForward updCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		DynaActionForm form1 = (DynaActionForm) form;
		CusAddr cusAddr = (CusAddr) form1.get("cusAddr");
		
		if(!StringFormat.isEmpty(cusId)){
			cusAddr.setCadCus(new CusCorCus(Long.parseLong(cusId)));
			if (!StringFormat.isEmpty(provId)) { cusAddr.setCadProv(new CusArea(Long.parseLong(provId))); }
			if (!StringFormat.isEmpty(cityId)) { cusAddr.setCadCity(new CusProvince(Long.parseLong(cityId))); }
			if (!StringFormat.isEmpty(districtId)) { cusAddr.setCadDistrict(new CusCity(Long.parseLong(districtId))); }
			customBIZ.updateCusAddr(cusAddr);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "编辑客户收货地址");
			return mapping.findForward("popDivSuc");
		}
		else{
			request.setAttribute("errorMsg","该客户不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 删除客户地址<br>
	 * @param request
	 * 		para >	cadId:客户地址Id
	 * @return ActionForward popDivSuc<br>
	 */
	public ActionForward delCusAddr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cadId = request.getParameter("cadId");
		CusAddr cusAddr = customBIZ.findCusAddrById(Long.parseLong(cadId));
		
		customBIZ.deleteCusAddr(cusAddr);
		
		request.setAttribute("isIfrm", "1");
		request.setAttribute("msg", "删除客户地址");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 修改客户月最低销售额<br>
	 * @param request
	 * 		para,attr >	cusId:客户Id
	 * @return ActionForward updCorSalNum<br>
	 */
	public ActionForward toUpdCorSalNum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("updCorSalNum");
	}
	
	/**
	 * 修改客户月最低销售额 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cusId:客户Id saleNum:最低月销售额
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：成功信息
	 */
	public ActionForward saveSaleNum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String saleNum = request.getParameter("saleNum");
		CusCorCus cusCorCus = customBIZ.findCus(Long.parseLong(cusId));
		if(saleNum!=null && !saleNum.equals("")){
			cusCorCus.setCorSaleNum(Double.valueOf(saleNum));
		}
		else{
			cusCorCus.setCorSaleNum(0.0);
		}
		customBIZ.update(cusCorCus);
		
		request.setAttribute("msg", "修改客户最低月销售额");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 修改客户起初余额<br>
	 * @param request
	 * 		para,attr >	cusId:客户Id
	 * @return ActionForward updCorSalNum<br>
	 */
	public ActionForward toUpdCorBeginBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("updCorBeginBal");
	}
	
	/**
	 * 修改客户起初余额 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cusId:客户Id beginBal:客户起初余额 
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：成功信息
	 */
	public ActionForward saveBeginBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String beginBal = request.getParameter("beginBal");
		CusCorCus cusCorCus = customBIZ.findCus(Long.parseLong(cusId));
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record = "";
		String recLog = "";
		double beginBalNum = 0.0;
		if(!StringFormat.isEmpty(beginBal)){
			beginBalNum = Double.parseDouble(beginBal);
		}
		
		if(cusCorCus.getCorBeginBal() == 0 && beginBalNum !=0){
			record = "发货期初余额由&nbsp;&nbsp;0.00&nbsp;&nbsp;改为&nbsp;&nbsp;"+beginBalNum+"&nbsp;&nbsp;";
		}else if(cusCorCus.getCorBeginBal() != 0 && beginBalNum == 0){
			record = "发货期初余额由&nbsp;&nbsp;"+cusCorCus.getCorBeginBal()+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+beginBalNum+"&nbsp;&nbsp;";
		}else if(cusCorCus.getCorBeginBal() != 0 && beginBalNum !=0 && cusCorCus.getCorBeginBal() != beginBalNum){
			record = "发货期初余额由&nbsp;&nbsp;"+cusCorCus.getCorBeginBal()+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+beginBalNum+"&nbsp;&nbsp;";
		}
		
		if(!record.equals("")){
			recLog = this.getPersonName(request)+"&nbsp;&nbsp;于&nbsp;&nbsp;"+df.format(new Date())+"&nbsp;&nbsp;将&nbsp;&nbsp;"+record +"</br>";
		}

		if(cusCorCus.getCorStateRecord() != null && !cusCorCus.getCorStateRecord().equals("")){
			cusCorCus.setCorStateRecord(recLog+cusCorCus.getCorStateRecord());
		}else{
			cusCorCus.setCorStateRecord(recLog);
		}
		
		if(beginBal!=null && !beginBal.equals("")){
			cusCorCus.setCorRecvAmt(Double.parseDouble(beginBal));
			cusCorCus.setCorBeginBal(Double.parseDouble(beginBal));
		}else{
			cusCorCus.setCorRecvAmt(0.0);
			cusCorCus.setCorTicketNum(0.0);
			cusCorCus.setCorBeginBal(0.0);
		}
		String corBeginRecord =this.getPersonName(request)+"&nbsp;&nbsp;于&nbsp;&nbsp;"+GetDate.parseStrSTime(GetDate.getCurTime())+"&nbsp;&nbsp;修改";
		cusCorCus.setCorBeginRecord(corBeginRecord);
		customBIZ.update(cusCorCus);
		
		request.setAttribute("msg", "修改客户发货期初余额");
		return mapping.findForward("popDivSuc");
	}
	
	
	/**
	 * 修改开票期初余额<br>
	 * @param request
	 * 		para,attr >	cusId:客户Id
	 * @return ActionForward updCorSalNum<br>
	 */
	public ActionForward toUpdCorTicketBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		request.setAttribute("cusId", cusId);
		return mapping.findForward("updCorTicketBal");
	}
	
	/**
	 * 修改开票期初余额 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > cusId:客户Id beginBal:客户起初余额 
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：成功信息
	 */
	public ActionForward saveTicketBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String ticketBal = request.getParameter("ticketBal");
		CusCorCus cusCorCus = customBIZ.findCus(Long.parseLong(cusId));
		
//		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record = "";
		String recLog = "";
		double  ticketBalNum = 0.0;
		if(!StringFormat.isEmpty(ticketBal)){
			ticketBalNum = Double.parseDouble(ticketBal);
		}
		if(cusCorCus.getCorTicketBal() == 0 && ticketBalNum !=0){
			record = "开票期初余额由&nbsp;&nbsp;0.00&nbsp;&nbsp;改为&nbsp;&nbsp;"+ticketBalNum+"&nbsp;&nbsp;";
		}else if(cusCorCus.getCorTicketBal() != 0 && ticketBalNum == 0){
			record = "开票期初余额由&nbsp;&nbsp;"+cusCorCus.getCorTicketBal()+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+ticketBalNum+"&nbsp;&nbsp;";
		}else if(cusCorCus.getCorTicketBal() != 0 && ticketBalNum !=0 && cusCorCus.getCorTicketBal() != ticketBalNum){
			record = "开票期初余额由&nbsp;&nbsp;"+cusCorCus.getCorTicketBal()+"&nbsp;&nbsp;改为&nbsp;&nbsp;"+ticketBalNum+"&nbsp;&nbsp;";
		}
		
		if(!record.equals("")){
			recLog = this.getPersonName(request)+"&nbsp;&nbsp;于&nbsp;&nbsp;"+df.format(new Date())+"&nbsp;&nbsp;将&nbsp;&nbsp;"+record +"</br>";
		}

		if(cusCorCus.getCorStateRecord() != null && !cusCorCus.getCorStateRecord().equals("")){
			cusCorCus.setCorStateRecord(recLog+cusCorCus.getCorStateRecord());
		}else{
			cusCorCus.setCorStateRecord(recLog);
		}
		if(ticketBal!=null && !ticketBal.equals("")){
			cusCorCus.setCorTicketNum(Double.parseDouble(ticketBal));
			cusCorCus.setCorTicketBal(Double.parseDouble(ticketBal));
		}else{
			cusCorCus.setCorTicketNum(0.0);
			cusCorCus.setCorTicketBal(0.0);
		}
		
		String corTicketRecord = this.getPersonName(request)+"&nbsp;&nbsp;于&nbsp;&nbsp;"+GetDate.parseStrSTime(GetDate.getCurTime())+"&nbsp;&nbsp;修改";
		cusCorCus.setCorTicketRecord(corTicketRecord);
		customBIZ.update(cusCorCus);
		
		request.setAttribute("msg", "修改客户开票期初余额");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到客户对应的开票记录<br>
	 * @param form
	 * @param mapping
	 * @param request 
	 * 				parameter > cusId:客户Id
	 * @param response
	 * @return ActionForward toListSalInvoice<br>
	 * 		attr > cusId:客户Id
	 */
	public ActionForward toListCusSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		
		request.setAttribute("cusId", cusId);
		return mapping.findForward("listCusSalInvoice");
	}
	
	public void listCusSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cusId = request.getParameter("cusId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String [] args = {cusId};
		String [] items = {"sinIsrecieve","sinCus","sinOrd","sinMon","typName","sinDate","seName"};
		if(p == null || p.trim().length()<1){
			p="1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize ="30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(salInvoiceBiz.listSalInvoicesCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List <SalInvoice> list = salInvoiceBiz.listSalInvoices(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("cusCorCus");
		//awareCollect.add("salEmp");
		awareCollect.add("erpSalOrder");
		awareCollect.add("person");
		awareCollect.add("typeList");
		
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
	 * 跳转到创建开票记录页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到formSalInvoice
	 */
	public ActionForward toNewSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusId = request.getParameter("cusId");
		
		if(cusId!=null && !cusId.equals("")){
			CusCorCus cusCorCus = customBIZ.findCus(Long.parseLong(cusId));
			if(cusCorCus !=null){
				request.setAttribute("cusInfo", cusCorCus);
			}
			else{
				request.setAttribute("errorMsg","该客户已被移至回收站或已被删除，无法新建对应此客户的开票记录！");
				return mapping.findForward("error");
			}
		}
		List<TypeList> typeList = typeListBiz.getEnabledType("13");//票据类型
		request.setAttribute("typList", typeList);
		return mapping.findForward("formSalInvoice");
	}
	
	/**
	 * 保存开票记录 <br>
	 * @param request
	 *      para > 	seId:负责账号 cusId:对应客户ID  ordId:订单ID 
	 *      		isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 *      		sinDate:实际回款日期
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > 	msg:保存成功信息提示 isIfrm:是否在详情页面中新建若为1则是在详情页面中新建
	 */
	public ActionForward saveSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String curSeName = (String)request.getSession().getAttribute("userName");
		String seId = request.getParameter("seId");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String sinDate = request.getParameter("sinDate");
		String typId = request.getParameter("typId");
		DynaActionForm form1 = (DynaActionForm) form;
		SalInvoice sal = (SalInvoice) form1.get("salInvoice");
		
		if (cusId != null && !cusId.equals("")) {
			CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
			sal.setCusCorCus(cus);
		} else {
			sal.setCusCorCus(null);
		}
		if (ordId != null && !ordId.equals("")) {
			ERPSalOrder order = new ERPSalOrder();
			order.setOrderId(ordId);
			sal.setErpSalOrder(order);
		} else {
			sal.setSalOrdCon(null);
		}
		if(typId != null && !typId.equals("")){
			TypeList type = new TypeList(Long.parseLong(typId));
			sal.setTypeList(type);
		}else{
			sal.setTypeList(null);
		}
		//插入日期
		if (sinDate!=null && !sinDate.equals("")) {
			try {
				sal.setSinDate(dateFormat1.parse(sinDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sal.setSinDate(null);
		}
		sal.setSinUserCode(curSeName);
		sal.setSinCreDate(new Date(new Date().getTime()));
		sal.setSinAltUser(null);
		sal.setSinAltDate(null);
		//paid.setSpsIsdel("0");
		sal.setPerson(new YHPerson(Long.parseLong(seId)));
		salInvoiceBiz.save(sal,cusId);
		
		//详情下刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "新建开票记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到修改开票记录 <br>
	 * @param request
	 *      para > paidId:开票记录ID
	 * @param response
	 * @return ActionForward 跳转到formPaidPast页面<br>
	 * 		attr > paid:开票记录实体，如果实体为null返回字符串"null"
	 */
	public ActionForward toUpdSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sinId = request.getParameter("sinId");
		SalInvoice sal = salInvoiceBiz.getById(Long.parseLong(sinId));

		List<TypeList> typeList = typeListBiz.getEnabledType("13");//票据类型
		request.setAttribute("typList", typeList);
		request.setAttribute("sal", sal);
		return mapping.findForward("formSalInvoice");
	}
	
	/**
	 * 修改开票记录 <br>
	 * @param request
	 *      para > paidId:开票记录ID pastDate:实际开票日期 isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 *      			ordId:订单id cusId:客户ID seId:负责人
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑 
	 */
	public ActionForward modSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		SalInvoice sal = (SalInvoice) form1.get("salInvoice");
		String id = request.getParameter("sinId");
		String seId = request.getParameter("seId");
		String sinDate = request.getParameter("sinDate");
		String cusId = request.getParameter("cusId");
		String ordId = request.getParameter("ordId");
		String isIfrm = request.getParameter("isIfrm");
		String typId = request.getParameter("typId");
		String curSeName = (String)request.getSession().getAttribute("userName");
		
		SalInvoice salO = salInvoiceBiz.getById(Long.parseLong(id));
		Double oldSin = 0.0;
		if( salO.getSinMon() !=null || ! salO.getSinMon().equals("")){
			oldSin = salO.getSinMon();
		}
		if(salO!=null){
			if (cusId != null && !cusId.equals("")) {
				CusCorCus cus = new CusCorCus(Long.parseLong(cusId));
				sal.setCusCorCus(cus);
			} else {
				sal.setCusCorCus(null);
			}
			//插入日期
			if (sinDate != null && !sinDate.equals("")) {
				try {
					sal.setSinDate(dateFormat1.parse(sinDate));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sal.setSinDate(null);
			}
			if (!ordId.equals("")) {
				ERPSalOrder order = new ERPSalOrder();
				order.setOrderId(ordId);
				sal.setErpSalOrder(order);
			} else {
				sal.setErpSalOrder(null);
			}
			if(typId != null && !typId.equals("")){
				TypeList type = new TypeList(Long.parseLong(typId));
				sal.setTypeList(type);
			}else{
				sal.setTypeList(null);
			}
			sal.setSinAltUser(curSeName);
			sal.setSinAltDate(new Date(new Date().getTime()));
			sal.setSinId(salO.getSinId());
			sal.setSinUserCode(salO.getSinUserCode());
			sal.setSinCreDate(salO.getSinCreDate());
			//paid.setSpsIsdel(paidO.getSpsIsdel());
			sal.setPerson(new YHPerson(Long.parseLong(seId)));
			salInvoiceBiz.update(sal,oldSin);
		}
		//详情下刷新iframe
		if ( isIfrm != null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "修改开票记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除开票记录 <br>
	 * @param request
	 *      parameter > sinId:开票记录ID; ordId:订单ID; isIfrm:是否在详情页面中编辑若为1则是在详情页面中删除;
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功删除的信息提示 isIfrm:是否在详情页面中编辑若为1则是在详情页面中删除 
	 */
	public ActionForward delCusSalInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sinId = request.getParameter("sinId");
		String isIfrm = request.getParameter("isIfrm");
		salInvoiceBiz.delete(sinId);
		//详情下刷新iframe
		if ( isIfrm!= null && isIfrm.equals("1")) {
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除开票记录");
		return mapping.findForward("popDivSuc");
	}
	
	public void setCustomBIZ(CustomBIZ customBIZ) {
		this.customBIZ = customBIZ;
	}

	public void setModType(ModifyTypeDAO modType) {
		this.modType = modType;
	}

	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}

	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}

	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}

	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}

	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}

	public void setPaidBiz(PaidBIZ paidBiz) {
		this.paidBiz = paidBiz;
	}

	public void setSalInvoiceBiz(SalInvoiceBIZ salInvoiceBiz) {
		this.salInvoiceBiz = salInvoiceBiz;
	}
}