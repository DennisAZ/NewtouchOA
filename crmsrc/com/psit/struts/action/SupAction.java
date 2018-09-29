package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.psit.struts.util.format.GetDate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.SupBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProd;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.ERPPurchase;
import com.psit.struts.entity.ERPPurchasePaidPlan;
import com.psit.struts.entity.ERPPurchaseSupplier;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ProdIn;
import com.psit.struts.entity.ProdOut;
import com.psit.struts.entity.ProdStore;
import com.psit.struts.entity.PurOrd;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RPuoPro;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalInvoice;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SupInvoice;
import com.psit.struts.entity.SupPaidPast;
import com.psit.struts.entity.SupProd;
import com.psit.struts.entity.Supplier;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WhRec;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.YHPerson;
import com.psit.struts.form.RopShipForm;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.web.AjaxUtils;

public class SupAction extends BaseDispatchAction {
	SupBIZ supBIZ = null;
	UserBIZ userBIZ = null;
	TypeListBIZ typeListBIZ = null;
	CustomBIZ customBIZ = null;
	
	/**
	 *	权限判断 <br>
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
		String methLim[][] = { 
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBIZ.getLimit(limUser, methodName, methLim);
	}
	
	/* Supplier */
	/**
	 * 跳转到供应商列表 <br>
	 * @return ActionForward listSup
	 */
	public ActionForward toListSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("listSup");
	}
	/**
	 * 供应商列表 <br>
	 * @param request 
	 *		para > 	supCode:编号;	supName:名称;		
	 * 				orderCol:排序字段名;	isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String supCode = request.getParameter("supCode");
		String supName = request.getParameter("supName");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String[] items = { "name", "code", "type", "contactMan", "phone", "mob", "fex", "email", "add", "prod" };
		
		String[] args = {supCode, supName};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listSupplierCount(args), pageSize, p);
		List supList = supBIZ.listSupplier(args, orderItem, isDe,
				page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(supList,page);
		List awareCollect = new LinkedList();
//		awareCollect.add("cusArea1");
//		awareCollect.add("cusArea2");
//		awareCollect.add("cusArea3");
		awareCollect.add("supType");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/**
	 * 跳转到保存供应商 <br>
	 * @param request
	 * @return ActionForward saveSup
	 */
	public ActionForward toSaveSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		List<TypeList> supTypeList = typeListBIZ.getEnabledType(Supplier.T_SUP_TYP);
		List<CusArea> area1List = customBIZ.getCusArea();//地区
		request.setAttribute("area1List", area1List);
		request.setAttribute("supType", supTypeList);
		if(!StringFormat.isEmpty(supId)){
			request.setAttribute("supplier", supBIZ.findSupplierById(supId));
		}
		return mapping.findForward("saveSup");
	}
	/**
	 * 保存供应商 <br>
	 * @param request
	 * 	para >	supId:供应商ID（更新供应商时传入）;		
	 * 			supTypeId:供应商类别ID; area1Id,area2Id,area3Id:地区ID;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward saveSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		String supTypeId = request.getParameter("supTypeId");
		String area1Id = request.getParameter("area1Id");
		String area2Id = request.getParameter("area2Id");
		String area3Id = request.getParameter("area3Id");
			
		DynaActionForm form1 = (DynaActionForm) form;
		Supplier supplier = (Supplier) form1.get("supplier");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		if(!StringFormat.isEmpty(supId)){
			supplier.setSupId(Long.parseLong(supId));
		}
		if (StringFormat.isEmpty(supTypeId)) {
			supplier.setSupType(null);
		} else {
			supplier.setSupType(new TypeList(Long.parseLong(supTypeId)));
		}
		if (!StringFormat.isEmpty(area1Id)&&!"1".equals(area1Id)) {
			supplier.setSupArea1(new CusArea(Long.valueOf(area1Id)));
		}
		else{
			supplier.setSupArea1(null);
		}
		if (!StringFormat.isEmpty(area2Id)&&!"1".equals(area2Id)) {
			supplier.setSupArea2(new CusProvince(Long.valueOf(area2Id)));
		}
		else{
			supplier.setSupArea2(null);
		}
		if (!StringFormat.isEmpty(area3Id)&&!"1".equals(area3Id)) {
			supplier.setSupArea3(new CusCity(Long.valueOf(area3Id)));
		}
		else{
			supplier.setSupArea3(null);
		}
		supBIZ.saveSupplier(supplier,curUser);
//		request.setAttribute("redir", "desc");
		request.setAttribute("msg", "保存供应商");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 查看供应商详情 <br>
	 * @param request
	 * 		para,attr >	supId:供应商ID;
	 * @return ActionForward descSup
	 */
	public ActionForward toDescSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String supId = request.getParameter("supId");
		Supplier supplier = supBIZ.findSupplierById(supId);
		request.setAttribute("supplier", supplier);
		return mapping.findForward("descSup");
	}
	
	/**
	 * 删除供应商 <br>
	 * @param request
	 * 		para >	supId:供应商ID;
	 * 		att  >   msg:删除供应
	 * @return ActionForward popDivSuc
	 */
	public ActionForward deleteSup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		supBIZ.deleteSupplier(supId);
		request.setAttribute("msg", "删除供应商");
		return mapping.findForward("popDivSuc");
	}
	
	
	/**
	 * 跳转到选择供应商<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward selectSupplier 页面<br/>
	 */
	public ActionForward toListSupplierToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("selectSupplier");
	}
	
	public void listSupplierToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supCode = request.getParameter("supCode");
		String supName = request.getParameter("supName");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem ="";
		String [] items ={"name", "code", "type", "contactMan"};
		String [] args = {supCode, supName};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize = "30"; //默认每页条数
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = new Page(supBIZ.listSupplierCount(args), pageSize, p);
		List supList = supBIZ.listSupplier(args, orderItem, isDe,
				page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(supList,page);
		List awareCollect = new LinkedList();
//		awareCollect.add("cusArea1");
//		awareCollect.add("cusArea2");
//		awareCollect.add("cusArea3");
		awareCollect.add("supType");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
		
	}
	
	/**
	 * 跳转到选择采购单<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward selectSupplier 页面<br/>
	 */
	public ActionForward toListPurOrdToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("selectPurOrd");
	}
	
	public void listPurOrdToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String puoCode = request.getParameter("puoCode");
		String supName = request.getParameter("supName");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String[] items = { "code", "type","supName","m","date", "emp" };
		String[] args = {puoCode,supName};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(supBIZ.listPurOrdCount(args), pageSize, p);
		List<PurOrd> dataList = supBIZ.listPurOrd(args, orderItem, isDe,
				page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(dataList,page);
		List awareCollect = new LinkedList();
		awareCollect.add("puoType");
		awareCollect.add("puoSup");
		awareCollect.add("puoEmp");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	public void listPurOrdToChooseERP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String puoCode = request.getParameter("puoCode");
		String supName = request.getParameter("supName");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String[] items = { "code", "type","supName","m","date", "emp" };
		String[] args = {puoCode,supName,String.valueOf(this.getPersonId(request))};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(supBIZ.listPurOrdCount(args), pageSize, p);
		List<ERPPurchase> dataList = supBIZ.listPurOrdERP(args, orderItem, isDe,
				page.getCurrentPageNo(), page.getPageSize());
		
//		Iterator it1=dataList.iterator();
//		while(it1.hasNext()){
//			ERPPurchase erpPch=(ERPPurchase)it1.next();
//			erpPch.getPaidPlan();
//			erpPch.setErpPurchasePaidPlan(((ERPPurchasePaidPlan)(erpPch.getPaidPlan().iterator().next())));
//			erpPch.setErpPurchaseSupplier((ERPPurchaseSupplier)erpPch.getSupplier().iterator().next());
//		}
		ListForm listForm = new ListForm(dataList,page);
		List awareCollect = new LinkedList();
		awareCollect.add("puoType");
		awareCollect.add("puoSup");
		awareCollect.add("paidPlan");
		awareCollect.add("supplier");
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/* PurOrd */
	/**
	 * 跳转采购单列表 <br>
	 * @param request
	 * @return ActionForward listPurOrd
	 */
	public ActionForward toListPurOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("listPurOrd");
	}
	/**
	 * 采购单列表 <br>
	 * @param request 
	 *		para > 	puoCode:采购单编号;	supName:供应商名称;	
	 * 				orderCol:排序字段名;	isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listPurOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String puoCode = request.getParameter("puoCode");
		String supName = request.getParameter("supName");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String[] items = { "code", "type", "supName", "m", "paidM", "date", "emp" };
		String[] args = {puoCode,supName};
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(supBIZ.listPurOrdCount(args), pageSize, p);
		List<PurOrd> dataList = supBIZ.listPurOrd(args, orderItem, isDe,
				page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(dataList,page);
		List awareCollect = new LinkedList();
		awareCollect.add("puoType");
		awareCollect.add("puoSup");
		awareCollect.add("puoEmp");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/**
	 * 跳转到保存采购单 <br>
	 * @param request
	 * 	para >	puoId:采购单ID（更新时传入）;
	 *  attr >	purOrd:采购单（更新）;	 puoTypeList:采购单类别;
	 * @return ActionForward savePurOrd
	 */
	public ActionForward toSavePurOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String puoId = request.getParameter("puoId");
		List<TypeList> puoTypeList = typeListBIZ.getEnabledType(PurOrd.T_PUO_TYP);
		request.setAttribute("puoTypeList", puoTypeList);
		
		if(!StringFormat.isEmpty(puoId)){
			request.setAttribute("purOrd", supBIZ.getPurOrdById(puoId));
		}
		return mapping.findForward("savePurOrd");
	}
	/**
	 * 保存采购单 <br>
	 * @param request
	 * 	para >	puoId:采购单记录ID（更新时传入）;	supId:供应商ID; seNo:采购人id
	 * 			puoTypeId:采购类别;	puoPurDate:采购日期;	
	 * 			orderCol:排序字段名;	isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 * 			rfType:刷新类型（list客户列表新建）;
	 * @return ActionForward popDivSuc
	 */
	public ActionForward savePurOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		String puoId = request.getParameter("puoId");
		String supId = request.getParameter("supId");
		String seNo = request.getParameter("empId");
		String puoTypeId = request.getParameter("puoTypeId");
		String puoPurDate = request.getParameter("puoPurDate");
		DynaActionForm form1 = (DynaActionForm) form;
		PurOrd purOrd = (PurOrd) form1.get("purOrd");
		if(!StringFormat.isEmpty(puoId)){
			purOrd.setPuoId(Long.parseLong(puoId));
		}
		if(!StringFormat.isEmpty(supId)){
			purOrd.setPuoSup(new Supplier(Long.parseLong(supId)));
		}
		else{
			purOrd.setPuoSup(null);
		}
		if(!StringFormat.isEmpty(puoTypeId)){
			purOrd.setPuoType(new TypeList(Long.parseLong(puoTypeId)));
		}
		else{
			purOrd.setPuoType(null);
		}
		if(!StringFormat.isEmpty(seNo)){
			purOrd.setPuoEmp(new SalEmp(Long.parseLong(seNo)));
		}else{
			purOrd.setPuoEmp(null);
		}
		if(!StringFormat.isEmpty(puoPurDate)){
			try{
				purOrd.setPuoPurDate(GetDate.formatDate(puoPurDate));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			purOrd.setPuoPurDate(null);
		}
		supBIZ.savePurOrd(purOrd,curUser);
		request.setAttribute("msg", "保存采购单");
		return mapping.findForward("popDivSuc");
	}
	
	
	public ActionForward toDescPurOrd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String puoId = request.getParameter("puoId");
		PurOrd purOrd = supBIZ.getPurOrdById(puoId);
		request.setAttribute("purOrd", purOrd);
		return mapping.findForward("descPurOrd");
	}
	/**
	 * 删除采购单<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parms > puoId:采购单id
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：删除采购单
	 */
	public ActionForward deleteContRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String puoId = request.getParameter("puoId");
		supBIZ.deletePurOrd(puoId);
		request.setAttribute("msg", "删除采购单");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到修改采购单状态 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > puoId:采购单id
	 * @param response
	 * @return ActionForward updState:采购单状态修改页面<br>
	 * 		attr > purOrd：采购单实体
	 */
	public ActionForward toUpdState(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String puoId = request.getParameter("puoId");
		PurOrd purOrd = supBIZ.getPurOrdById(puoId);
		
		request.setAttribute("purOrd", purOrd);
		return mapping.findForward("updState");
	}
	
	/**
	 * 保存采购单状态 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > puoId: 采购单id  puoIsEnd:采购单状态
	 * @param response
	 * @return ActionForward popDivSuc页面<br>
	 * 		attr > msg:显示信息
	 */
	public ActionForward updState(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String puoId = request.getParameter("puoId");
		String puoIsEnd = request.getParameter("puoIsEnd");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		PurOrd purOrd = supBIZ.getPurOrdById(puoId);
		purOrd.setPuoIsEnd(puoIsEnd);
		supBIZ.savePurOrd(purOrd,curUser);
		request.setAttribute("msg", "修改采购单状态");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到采购单明细 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > puoId:采购单id
	 * @param response
	 * @return ActionForward listRPuoPro<br>
	 * 		attr > puoId:采购单id
	 */
	public ActionForward toListRPuoPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprId = request.getParameter("wprId");
		
		request.setAttribute("wprId", wprId);
		return mapping.findForward("listRPuoPro");
	}
	
	/**
	 * 采购单明细列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parms > wprId:商品id; orderCol:排序字段名;	isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 * @param response void (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public void listRPuoPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprId = request.getParameter("wprId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] args = {wprId};
		String [] items = {"purOrd","rppNum","rppPrice","rppSumMon","rppOutNum","rppRealNum","rppRemark"};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		
		Page page = new Page(supBIZ.listRPuoProCount(args),pageSize,p);
		List<RPuoPro> list = supBIZ.listRPuoPro(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProduct");
		awareCollect.add("purOrd");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	
	/*ProdIn*/
	/**
	 *  跳转到入库单<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward listProdIn<br>
	 * 		attr > typList：仓库类别
	 */
	public ActionForward toListProdIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<TypeList> typList = typeListBIZ.getEnabledType("31");//仓库
		
		request.setAttribute("typList", typList);
		return mapping.findForward("listProdIn");
	}
	
	/**
	 * 入库单列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		 params > wprName:商品名 storeType：仓库类别 	orderCol:排序字段名;
	 * 				isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listProdIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprName = request.getParameter("wprName");
		String typId = request.getParameter("storeType");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem ="";
		String[] args = {wprName,typId};
		String[] items = {"pinCode","purOrd","pinRespMan","pinDate","pinState","pinInNum"};
		if (p == null || p.trim().length() < 1) {
			p = "1";// 默认页数
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";// 默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listProdInCount(args),pageSize,p);
		List<ProdIn> list = supBIZ.listProdIn(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("purOrd");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	

	public ActionForward toDescProdIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pinId = request.getParameter("pinId");
		
		request.setAttribute("prodIn", supBIZ.getProdInById(pinId));
		return mapping.findForward("descProdIn");
	}
	/**
	 * 跳转到保存入库单 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pinId:入库单Id(更新时用)
	 * @param response
	 * @return ActionForward saveProdIn<br>
	 * 		attr > prodIn：入库单（更新用）
	 */
	public ActionForward toSaveProdIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pinId = request.getParameter("pinId");
		
		if(!StringFormat.isEmpty(pinId)){
			request.setAttribute("prodIn", supBIZ.getProdInById(pinId));
		}
		return mapping.findForward("saveProdIn");
	}
	
	/**
	 * 保存入库单<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		params > pinId:入库单Id(更新时用);pstId:库存Id;pinDate:入库日期
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg:成功信息
	 */
	public ActionForward saveProdIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pinId = request.getParameter("pinId");
		String pstId = request.getParameter("pstId");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		String pinDate = request.getParameter("pinDate");
		DynaActionForm form1 = (DynaActionForm) form;
		ProdIn prodIn = (ProdIn) form1.get("prodIn");
		if(!StringFormat.isEmpty(pinId)){
			prodIn.setPinId(Long.parseLong(pinId));
		}
		if(!StringFormat.isEmpty(pstId)){
			prodIn.setProdStore(new ProdStore(Long.parseLong(pstId)));
		}else{
			prodIn.setProdStore(null);
		}
		if(!StringFormat.isEmpty(pinDate)){
			try{
				prodIn.setPinDate(GetDate.formatDate(pinDate));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			prodIn.setPinDate(null);
		}
		supBIZ.saveProdIn(prodIn, curUser);
		request.setAttribute("msg", "保存入库单");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除入库单 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pinId:入库单Id
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：成功信息
	 */
	public ActionForward delProdIn(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pinId = request.getParameter("pinId");
		supBIZ.delProdIn(pinId);
		request.setAttribute("msg", "删除入库单");
		return mapping.findForward("popDivSuc");
	}
	
	public ActionForward cancleProdIn(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pinId = request.getParameter("pinId");
		supBIZ.cancleProdIn(pinId);
		request.setAttribute("msg", "撤销入库单");
		return mapping.findForward("popDivSuc");
	}
	
	/*ProdStore*/
	/**
	 * 跳转到保存库存<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward saveProdStore<br>
	 */
	public ActionForward toSaveProdStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		List<TypeList> typList = typeListBIZ.getEnabledType("31");//仓库类别
		
		request.setAttribute("typList", typList);
		if(!StringFormat.isEmpty(pstId)){
			request.setAttribute("prodStore", supBIZ.getProdStoreById(pstId));
		} 
		return mapping.findForward("saveProdStore");
	}
	
	/**
	 * 保存库存<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pstId:库存Id(更新库存时传入);wprId:商品Id;psTypId:仓库类别
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 */
	public ActionForward saveProdStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		String wprId = request.getParameter("wprId");
		String typId = request.getParameter("psTypId");
		LimUser curUser = (LimUser)request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		ProdStore prodStore = (ProdStore) form1.get("prodStore");
		if(!StringFormat.isEmpty(pstId)){
			prodStore.setPstId(Long.parseLong(pstId));
		}
		if(!StringFormat.isEmpty(wprId)){
			prodStore.setWmsProduct(new WmsProduct(wprId));
		}else{
			prodStore.setWmsProduct(null);
		}
		if(!StringFormat.isEmpty(typId)){
			prodStore.setStoreType(new TypeList(Long.parseLong(typId)));
		}else{
			prodStore.setStoreType(null);
		}
		supBIZ.saveProdStore(prodStore, curUser);
		request.setAttribute("msg", "保存库存");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除库存<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		 params > pstId:库存Id
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg:删除库存
	 */
	public ActionForward deleteProdStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		supBIZ.deleteProdStore(pstId);
		request.setAttribute("msg", "删除库存");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 跳转到库存列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward listProdStore<br>
	 * 		attr > typList：仓库类别
	 */
	public ActionForward toListProdStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<TypeList> typList = typeListBIZ.getEnabledType("31");//仓库
		
		request.setAttribute("typList", typList);
		return mapping.findForward("listProdStore");
	}
	
	/**
	 * 库存列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params >wprName:商品名称; storeType:仓库类别
	 * 			orderCol:排序字段名;isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listProdStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprName = request.getParameter("wprName");
		String typId = request.getParameter("storeType");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String[] args = {wprName,typId};
		String[] items = {"pstName","wprName","type","pstCount"};
		String orderItem = "";
		if(p == null || p.trim().length()<1){
			p = "1";//默认页数
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize = "30";//默认每页条数
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listProdStoreCount(args),pageSize,p);
		List<ProdStore> list = supBIZ.listProdStore(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProduct");
		awareCollect.add("storeType");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/**
	 * 跳转到选择库存<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward selectProdStore<br>
	 * 		attr > typList:仓库类别
	 */
	public ActionForward toListProdStoreToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<TypeList> typList = typeListBIZ.getEnabledType("31");//仓库
		
		request.setAttribute("typList", typList);
		return mapping.findForward("selectProdStore");
	}
	
	
	/**
	 * 跳转到商品关联库存列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		params > wprId:商品Id
	 * @param response
	 * @return ActionForward listProductStore<br>
	 * 		attr > wprId:商品Id
	 */
	public ActionForward toListProductStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprId = request.getParameter("wprId");
		
		request.setAttribute("wprId", wprId);
		return mapping.findForward("listProductStore");
	}
	
	/**
	 * 商品关联库存列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > wprId:商品Id;orderCol:排序字段名;isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listProductStore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprId = request.getParameter("wprId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String[] items = {"pstName","type","pstCount"};
		String[] args = {wprId};
		String orderItem = "";
		if(p==null || p.trim().length()<1){
			p = "1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize = "30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listProdStoreCount(args),pageSize,p);
		List<ProdStore> list = supBIZ.listProdStore(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProduct");
		awareCollect.add("storeType");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
		
	}
	
	/**
	 * 查看库存详情 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		params > pstId:库存Id
	 * @param response
	 *@return ActionForward descProdStore<br>
	 */
	public ActionForward toDescProdStore(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		ProdStore prodStore = supBIZ.getProdStoreById(pstId);
		request.setAttribute("prodStore", prodStore);
		
		return mapping.findForward("descProdStore");
	}
	
	/*ProdOut*/
	/**
	 * 跳转到出库列表 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward listProdOut<br>
	 * 		attr > typList：仓库类别
	 */
	public ActionForward toListProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<TypeList> typList = typeListBIZ.getEnabledType("31");
		
		request.setAttribute("typList", typList);
		return mapping.findForward("listProdOut");
	}
	
	/**
	 * 出库列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params >  wprName:商品名称;typId:仓库类别;orderCol:排序字段名;isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String wprName = request.getParameter("wprName");
		String typId = request.getParameter("typId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String[] items ={"pouCode","ordCon","pouDate","pouState","pouOutNum","pouPickMan","pouRespMan"};
		String[] args = {wprName,typId};
		String orderItem = "";
		if(p==null || p.trim().length()<1){
			p = "1";
		}
		if(pageSize == null || pageSize.equals("")){
			pageSize ="30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listProdOutCount(args),pageSize,p);
		List<ProdOut> list = supBIZ.listProdOut(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("prodStore");
		awareCollect.add("salOrdCon");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	public ActionForward toDescProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pouId = request.getParameter("pouId");
		
		request.setAttribute("prodOut", supBIZ.getProdOutById(pouId));
		return mapping.findForward("descProdOut");
	}
	
	/**
	 * 跳转到保存出库单<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pouId:出库单Id(更新用)
	 * @param response
	 * @return ActionForward saveProdOut<br>
	 * 		attr > prodOut:出库单(更新用)
	 */
	public ActionForward toSaveProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pouId = request.getParameter("pouId");
		if(!StringFormat.isEmpty(pouId)){
			request.setAttribute("prodOut", supBIZ.getProdOutById(pouId));
		}
		return mapping.findForward("saveProdOut");
	}
	
	/**
	 * 保存出库单 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pouId:出库单Id(更新用); pstId:库存Id; pouDate:出库日期
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward saveProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pouId = request.getParameter("pouId");
		String pstId = request.getParameter("pstId");
		String pouDate = request.getParameter("pouDate");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		ProdOut prodOut = (ProdOut) form1.get("prodOut");
		if(!StringFormat.isEmpty(pouId)){
			prodOut.setPouId(Long.parseLong(pouId));
		}
		if(!StringFormat.isEmpty(pstId)){
			prodOut.setProdStore(new ProdStore(Long.parseLong(pstId)));
		}else{
			prodOut.setProdStore(null);
		}
		if(!StringFormat.isEmpty(pouDate)){
			try{
				prodOut.setPouDate(GetDate.formatDate(pouDate));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			prodOut.setPouDate(null);
		}
		supBIZ.saveProdOut(prodOut, curUser);
		request.setAttribute("msg", "保存出库单");
		return mapping.findForward("popDivSuc");
	}
	
	public ActionForward cancleProdOut(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pouId = request.getParameter("pouId");
		supBIZ.cancleProdOut(pouId);
		request.setAttribute("msg", "撤销出库单");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除出库单 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pouId:出库单Id
	 * @param response
	 * @return ActionForward popDivSuc<br>
	 * 		attr > msg：成功信息
	 */
	public ActionForward delProdOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pouId = request.getParameter("pouId");
		supBIZ.delProdOut(pouId);
		request.setAttribute("msg", "删除出库单");
		
		return mapping.findForward("popDivSuc");
	}
	
	/*WhRec*/
	/**
	 * 跳转到库存变更记录 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pstId:库存Id
	 * @param response
	 * @return ActionForward listWhRec<br>
	 * 		attr >  pstId:库存Id
	 */
	public ActionForward toListWhRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		
		request.setAttribute("pstId", pstId);
		return mapping.findForward("listWhRec");
	} 
	
	/**
	 * 库存变更记录<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > pstId:库存Id;orderCol:排序字段名;isDe:是否逆序;	p:当前页码;	pageSize:每页数据条数;
	 */
	public void listWhRec(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String pstId = request.getParameter("pstId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String[] items = {"wreCount", "wreType", "wreTime", "wreMan",
				"wreLeftCount"};
		String[] args = {pstId};
		String orderItem = "";
		if(p==null || p.trim().length()<1){
			p = "1";
		}
		if(pageSize==null || pageSize.equals("")){
			pageSize = "30";
		}
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listWhRecCount(args),pageSize,p);
		List<WhRec> list = supBIZ.listWhRec(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
		
	}
	
	/**
	 * 跳转到更新采购单明细 <br>
	 * @param request
	 *      para >	puoId:采购单ID;
	 *      attr >	purOrd:采购单实体;	
	 * @return ActionForward updRpp
	 */
	public ActionForward toUpdRpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String puoId = request.getParameter("puoId");
		PurOrd purOrd = supBIZ.getPurOrdById(puoId);
		List<SupProd> supProdList = null;
		if(purOrd.getPuoSup() !=null){
			supProdList = supBIZ.listBySupId(String.valueOf(purOrd.getPuoSup().getSupId()));
		}
		request.setAttribute("supProdList", supProdList);
		request.setAttribute("purOrd", purOrd);
		return mapping.findForward("updRpp");
	}
	
	
	/**
	 * 更新采购单明细 <br>
	 * @param request
	 *      para > 	wprId:采购单明细产品ID数组;	puoId:采购单ID;
	 *      attr >	noMsg:表示不弹出任何提示直接刷新父页面;
	 * @return ActionForward empty
	 */
	public ActionForward updRpp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodIds[] = request.getParameterValues("wprId");
		String puoId = request.getParameter("puoId");
		List<RPuoPro> rppList = new ArrayList();
		try{
			for (int i = 1; i < prodIds.length; i++) {
				RPuoPro rPuoPro = new RPuoPro();
				String rppPrice = request.getParameter("price" + prodIds[i]);
				String num1 = request.getParameter("num" + prodIds[i]);
				String tatalPrice = request.getParameter("allPrice" + prodIds[i]);
				//String outNum = request.getParameter("outNum" + prodIds[i]);
				//String realNum = request.getParameter("realNum" + prodIds[i]);
				String rmStr = request.getParameter("remark" + prodIds[i]);
				
				if (!StringFormat.isEmpty(rppPrice)) {
					rPuoPro.setRppPrice(Double.parseDouble(rppPrice));
				}
				else{
					rPuoPro.setRppPrice(0.0);
				}
				if (!StringFormat.isEmpty(num1)) {
					rPuoPro.setRppNum(Double.parseDouble(num1));
				} else {
					rPuoPro.setRppNum(0.0);
				}
				if (!StringFormat.isEmpty(tatalPrice)) {
					rPuoPro.setRppSumMon(Double.parseDouble(tatalPrice));
				} else {
					rPuoPro.setRppSumMon(0.0);
				}

				/**if (!StringFormat.isEmpty(outNum)) {
					rPuoPro.setRppOutNum(Double.parseDouble(outNum));
				} else {
					rPuoPro.setRppOutNum(0.0);
				}
				if (!StringFormat.isEmpty(realNum)) {
					rPuoPro.setRppRealNum(Double.parseDouble(realNum));
				} else {
					rPuoPro.setRppRealNum(0.0);
				}*/
				
				rPuoPro.setPurOrd(new PurOrd(Long.parseLong(puoId)));
				rPuoPro.setWmsProduct(new WmsProduct(prodIds[i]));
				rPuoPro.setRppRemark(rmStr);
				rppList.add(rPuoPro);
			}
			supBIZ.saveRpp(rppList,puoId); 
			request.setAttribute("noMsg", "1");//不弹出信息
			return mapping.findForward("empty");
		} catch(Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errorMsg", ex.toString());
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 采购单产品明细列表 <br>
	 * @param request
	 * 		para >	puoId:采购单Id;  orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listPuoPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String puoId = request.getParameter("puoId");
		List<RPuoPro> list = supBIZ.listPuoPro(puoId);
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
	 * 跳转到供应商产品<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > supId:供应商Id
	 * @param response
	 * @return ActionForward 跳转到listSupProd页面<br>
	 * 		attr >  supId:供应商Id
	 */
	public ActionForward toListSupProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		
		request.setAttribute("supId", supId);
		return mapping.findForward("listSupProd");
	}

	/**
	 * 供应商产品列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 				parameter > supId:供应商ID p:当前页码  orderCol:排序字段  pageSize:每页显示条数  isDe:是否逆序
	 */
	public void listSupProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){

		String wprName = request.getParameter("wprName");
		String supId = request.getParameter("supId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String [] args  ={supId,wprName};
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		String [] items = {"product","otherName","type","salPrice","spPrice","spRemark"};
		if(orderCol!=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listSupProdCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = supBIZ.listSupProd(args, orderItem, isDe,  page
				.getCurrentPageNo(), page.getPageSize());
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("supplier");
		awareCollect.add("wmsProduct");
		awareCollect.add("wmsProduct.typeList");
		awareCollect.add("wmsProduct.productUnit");
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));

	}
	
	/**
	 * 跳转到保存供应商产品<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			params > spId:供应商产品Id(更新用)
	 * @param response
	 * @return ActionForward saveProdOut<br>
	 * 		attr > supProd:供应商产品(更新用)
	 */
	public ActionForward toSaveSupProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String spId = request.getParameter("spId");
		String supId = request.getParameter("supId");
		String isIfrm = request.getParameter("isIfrm");
		if(!StringFormat.isEmpty(spId)){
			request.setAttribute("supProd", supBIZ.getSupProdById(spId));
		}
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("supId", supId);
		return mapping.findForward("saveSupProd");
	}
	
	/**
	 * 保存供应商产品<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward saveSupProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		String spId = request.getParameter("spId");
		String isIfrm = request.getParameter("isIfrm");
		String wprId = request.getParameter("wprId");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		SupProd supProd = (SupProd) form1.get("supProd");
		if(!StringFormat.isEmpty(spId)){
			supProd.setSpId(Long.parseLong(spId));
		}
		if(!StringFormat.isEmpty(wprId)){
			supProd.setWmsProduct(new WmsProduct(wprId));
		}else{
			supProd.setWmsProduct(null);
		}
		if(!StringFormat.isEmpty(supId)){
			supProd.setSupplier(new Supplier(Long.parseLong(supId)));
		}else{
			supProd.setSupplier(new Supplier(null));
		}
		supBIZ.saveSupProd(supProd, curUser);
		
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "保存供应商产品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除供应商产品<br>
	 * @param mapping
	 * @param form
	 * @param request 
	 * 			params > spId:供应商Id; isIfrm:iframe刷新
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward delSupProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String spId = request.getParameter("spId");
		String isIfrm = request.getParameter("isIfrm");
		supBIZ.delSupProd(spId);
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", "1");
		}
		request.setAttribute("msg", "删除供应商产品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到供应商付款记录<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward toListSupPaidPast(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		
		request.setAttribute("supId", supId);
		return mapping.findForward("listSupPaidPast");
	}
	
	/**
	 * 供应商付款记录列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response void (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public void listSupPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		String p = request.getParameter("p");
		String isDe = request.getParameter("isDe");
		String orderCol = request.getParameter("orderCol");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String [] args = {supId};
		String [] items = {"sppIsInv", "sppSup", "sppPurOrd", "sppMon",
				"sppPayType", "sppDate", "sppUser" };
		if(p == null || p.trim().length()<1){
			p="1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize ="30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listSupPaidPastCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List <SupPaidPast> list = supBIZ.listSupPaidPast(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("supplier");
		awareCollect.add("salEmp");
		awareCollect.add("purOrd");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/**
	 * 跳转到创建付款记录页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到savePaidPast
	 */
	public ActionForward toSaveSupPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sppId = request.getParameter("sppId");
		String supId = request.getParameter("supId");
		String isIfrm = request.getParameter("isIfrm");
		if(!StringFormat.isEmpty(sppId)){
			request.setAttribute("supPaidPast", supBIZ.getSupPaidPast(sppId));
		}
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("supId", supId);
		return mapping.findForward("saveSupPaidPast");
	}
	
	/**
	 * 保存付款记录 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward saveSupPaidPast(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String sppId = request.getParameter("sppId");
		String supId = request.getParameter("supId");
		String puoId = request.getParameter("puoId");
		String seNo = request.getParameter("seId");
		String isIfrm = request.getParameter("isIfrm");
		String pastDate = request.getParameter("pastDate");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		SupPaidPast supPaidPast = (SupPaidPast) form1.get("supPaidPast");
		if(!StringFormat.isEmpty(sppId)){
			supPaidPast.setSppId(Long.parseLong(sppId));
		}
		if(!StringFormat.isEmpty(supId)){
			supPaidPast.setSupplier(new Supplier(Long.parseLong(supId)));
		}else{
			supPaidPast.setSupplier(null);
		}
		if(!StringFormat.isEmpty(puoId)){
			supPaidPast.setPurOrd(new PurOrd(Long.parseLong(puoId)));
		}else{
			supPaidPast.setPurOrd(null);
		}
		if(!StringFormat.isEmpty(seNo)){
			supPaidPast.setSalEmp(new SalEmp(Long.parseLong(seNo)));
		}else{
			supPaidPast.setSalEmp(null);
		}
		if(!StringFormat.isEmpty(pastDate)){
			try{
				supPaidPast.setSppFctDate(GetDate.formatDate(pastDate));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			supPaidPast.setSppFctDate(null);
		}
		supBIZ.saveSupPaidPast(supPaidPast, curUser);
		
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", isIfrm);
		}
		request.setAttribute("msg", "保存付款记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除付款记录 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward delSupPaidPast(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String sppId = request.getParameter("sppId");
		String isIfrm = request.getParameter("isIfrm");
		
		supBIZ.delSupPaidPast(sppId);
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", isIfrm);
		}
		request.setAttribute("msg", "删除该付款记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 跳转到供应商关联的收票记录列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward toListSupInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		
		request.setAttribute("supId", supId);
		return mapping.findForward("listSupInvoice");
	}
	
	/**
	 * 供应商关联收票记录列表<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response void (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public void listSupInvoice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String supId = request.getParameter("supId");
		String p = request.getParameter("p");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String pageSize = request.getParameter("pageSize");
		String orderItem = "";
		String [] args = {supId};
		String [] items = {"suiCode","suiSup","suiPurOrd","suiMon","typName","suiDate","seName"};
		if(p == null || p.trim().length()<1){
			p="1";
		}
		if(pageSize ==null || pageSize.equals("")){
			pageSize ="30";
		}
		if(orderCol !=null && !orderCol.equals("")){
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(supBIZ.listSupInvoiceCount(args),Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List <SupInvoice> list = supBIZ.listSupInvoice(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm(list,page);
		List awareCollect = new LinkedList();
		awareCollect.add("erpPurchase");
		awareCollect.add("yhPerson");
		awareCollect.add("supplier");
		awareCollect.add("typeList");
		
		AjaxUtils.outPrintJSON(response, new JSONConvert().model2JSON(listForm,awareCollect));
	}
	
	/**
	 * 跳转到新建收票记录 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward toSaveSupInvoice(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String suiId = request.getParameter("suiId");
		String supId = request.getParameter("supId");
		String isIfrm = request.getParameter("isIfrm");
		if(!StringFormat.isEmpty(suiId)){
			request.setAttribute("supInvoice", supBIZ.getSupInvoice(suiId));
		}
		List<TypeList> typeList = typeListBIZ.getEnabledType("13");//票据类型
		request.setAttribute("typList", typeList);
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("supId", supId);
		return mapping.findForward("saveSupInvoice");
		
	}
	
	/**
	 * 保存收票记录<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward saveSupInvoice(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String suiId = request.getParameter("suiId");
		String supId = request.getParameter("supId");
		String puoId = request.getParameter("puoId");
		String seNo = request.getParameter("seId");
		String typId = request.getParameter("typId");
		String isIfrm = request.getParameter("isIfrm");
		String suiDate = request.getParameter("suiDate");
		LimUser curUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		SupInvoice supInvoice = (SupInvoice) form1.get("supInvoice");
		if(!StringFormat.isEmpty(suiId)){
			supInvoice.setSuiId(Long.parseLong(suiId));
		}
		if(!StringFormat.isEmpty(supId)){
			supInvoice.setSupplier(new Supplier(Long.parseLong(supId)));
		}else{
			supInvoice.setSupplier(null);
		}
		if(!StringFormat.isEmpty(puoId)){
			supInvoice.setErpPurchase(new ERPPurchase(puoId));
		}else{
			supInvoice.setPurOrd(null);
		}
		if(!StringFormat.isEmpty(seNo)){
			YHPerson person=new YHPerson();
			person.setSeqId(Long.parseLong(seNo));
			supInvoice.setYhPerson(person);
		}else{
			supInvoice.setYhPerson(null);
		}
		if(!StringFormat.isEmpty(typId)){
			supInvoice.setTypeList(new TypeList(Long.parseLong(typId)));
		}else{
			supInvoice.setTypeList(null);
		}
		if(!StringFormat.isEmpty(suiDate)){
			try{
				supInvoice.setSuiDate(GetDate.formatDate(suiDate));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			supInvoice.setSuiDate(null);
		}
		supBIZ.saveSupInvoice(supInvoice, curUser);
		
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", isIfrm);
		}
		request.setAttribute("msg", "保存收票记录");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 删除开票记录<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward (如果是action方法需注明跳转的一个或多个映射名)<br>
	 * 		attr > (attributeName:注释)（没有setAttribute删除此条注释）
	 */
	public ActionForward delSupInvoice(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String suiId = request.getParameter("suiId");
		String isIfrm = request.getParameter("isIfrm");
		supBIZ.delSupInvoice(suiId);
		if(isIfrm !=null && isIfrm.equals("1")){
			request.setAttribute("isIfrm", isIfrm);
		}
		
		request.setAttribute("msg", "删除收票记录");
		return mapping.findForward("popDivSuc");
	}
	
	public void setSupBIZ(SupBIZ supBIZ) {
		this.supBIZ = supBIZ;
	}
	public void setUserBIZ(UserBIZ userBIZ) {
		this.userBIZ = userBIZ;
	}
	public void setTypeListBIZ(TypeListBIZ typeListBIZ) {
		this.typeListBIZ = typeListBIZ;
	}
	public void setCustomBIZ(CustomBIZ customBIZ) {
		this.customBIZ = customBIZ;
	}
}