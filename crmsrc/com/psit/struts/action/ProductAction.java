package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.ProductBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ProdSalBack;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.ProdTrans;
import com.psit.struts.entity.RShipPro;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.util.ListForm;
import com.psit.struts.util.FileOperator;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.JSONConvert;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.format.TransStr;
import com.psit.struts.util.DAO.BatchOperateDAO;

/**
 * 商品管理 <br>
 */
public class ProductAction extends BaseDispatchAction {
	ProductBIZ prodBiz = null;
	EmpBIZ empBiz = null;
	UserBIZ userBiz = null;
	TypeListBIZ typeListBiz = null;
	OrderBIZ orderBiz = null;
	BatchOperateDAO batchOperate = null;
	CustomBIZ customBiz = null;

	/**
	 * 判断用户是否有商品管理的权限 <br>
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
		String methLim[][] = { { "toListProd", "b027" },//商品管理是否可见
				{ "toNewPro", "s020" },{ "newProduct", "s020" },//新建商品
				{ "toUpdPro", "s022" },{ "UpdProduct", "s022" },//修改商品
				{ "delWmsPro", "s021" },//删除商品
				{ "wmsProDesc", "s023" },//查看商品详情
				{ "findOrdByWpr", "s035" }//查看商品销售历史
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}
	
	/**
	 * 跳转到商品页面 <br>
	 * @param request
	 * 			parameter proType :商品类别
	 * @param response
	 * @return ActionForward listProduct<br>
	 * 		attribute >proType :商品类别
	 */
	public ActionForward toListProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String proType = request.getParameter("proType");
		request.setAttribute("proType", proType);
		return mapping.findForward("listProduct");
	}
	/**
	 * 商品列表 <br>
	 * @param request
	 * 		para >	proName:商品名;  proCode:商品编号; proMod:商品型号; proType:商品类型;
	 * 				p:当前页码; pageSize:每页显示记录数; orderCol:排序列号; isDe:是否逆序; 
	 */
	public void listProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String proName=request.getParameter("proName");
		String proCode=request.getParameter("proCode");
		String proMod=request.getParameter("proMod");
		String proType = request.getParameter("proType");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "proCode","proName","proType","price","normalPer","overPer"};
		String[] args = { "0", proName, proCode, proMod, proType };
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(prodBiz.listProdsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<WmsProduct> list = prodBiz.listProds(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProType");

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
	 * 跳转到选择产品 <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward selectProduct页面<br>
	 */
	public ActionForward toListProductToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return mapping.findForward("selectProduct");
	}
	
	public void listProductToChoose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String proName=request.getParameter("proName");
		String proCode=request.getParameter("proCode");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items = { "proCode","proName","price","proUnit"};
		String[] args = { "0", proName, proCode, null, null};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(prodBiz.listProdsCount(args), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<WmsProduct> list = prodBiz.listProds(args, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
//		awareCollect.add("wmsProType");
//		awareCollect.add("typeList");

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
	 * 选择商品类别 <br>
	 * @param request
	 * 		parameter > pro:跳转标识
	 * @param response
	 * @return ActionForward pro不为空时跳转至wptTree页面,否则跳转至selProType页面
	 * 		attribute > proType:类别列表
	 */
	public ActionForward selProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pro = request.getParameter("pro");
		String type = request.getParameter("type");
		List list = prodBiz.findAllWpt();
		request.setAttribute("proType", list);
		if (pro != null && !pro.equals("")) {
			return mapping.findForward("wptTree");
		}
		request.setAttribute("type", type);
		return mapping.findForward("selProType");
	}

	/**
	 * 进入新建商品页面 <br>
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到newProduct页面<br>
	 * 		attribute > typeList:商品单位列表
	 */
	public ActionForward toNewPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//商品单位
		List list2 = typeListBiz.getEnabledType("15");
		request.setAttribute("typeList", list2);
		return mapping.findForward("newProduct");
	}

	/**
	 * 保存商品 <br>
	 * @param request
	 *      parameter > wptId:商品分类 wprUnit:商品单位
	 * @param response
	 * @return ActionForward 不符合条件跳转到error页面 成功添加跳转到popDivSuc页面<br>
	 * 		attribute > 不符合添加条件 errorMsg:商品名称型号重复，商品编号重复信息提示<br>
	 *      attribute > 成功添加 msg:成功添加信息提示<br>
	 */
	public ActionForward newProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		WmsProduct wmsProduct = (WmsProduct) form1.get("wmsProduct");
		String id = request.getParameter("wptId");
		String wprUnit = request.getParameter("wprUnit");
		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		
		String wprName = wmsProduct.getWprName();
		//String wprModel = wmsProduct.getWprModel();
		List list = prodBiz.proCheck(wprName);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该商品名称已存在，请核实后再添加");
			return mapping.findForward("error");
		}
		if (wmsProduct.getWprCode() != null
				&& !wmsProduct.getWprCode().equals("")) {
			List list1 = prodBiz.findbyWprCode(wmsProduct.getWprCode());
			if (list1.size() > 0) {
				request.setAttribute("errorMsg", "该商品编号已存在，请核实后再添加");
				return mapping.findForward("error");
			}
		}
		if (id != null && !id.equals("")) {
			wmsProduct.setWmsProType(new WmsProType(Long.parseLong(id)));
		} else {
			wmsProduct.setWmsProType(null);
		}
		if (wprUnit != null && !wprUnit.equals("")) {
			wmsProduct.setTypeList(new TypeList(Long.parseLong(wprUnit)));
		} else {
			wmsProduct.setTypeList(null);
		}
		wmsProduct.setWprPic(null);
		wmsProduct.setWprIsdel("0");
		wmsProduct.setWprCuserCode(limUser.getUserSeName());//录入人
		wmsProduct.setWprCreDate(GetDate.getCurTime());//录入时间
		prodBiz.saveWP(wmsProduct);
		request.setAttribute("msg", "添加商品");
		return mapping.findForward("popDivSuc");
	}


	/**
	 * 产品名称型号重复判断<br>
	 * @param request
	 *      parameter > wprMod:产品型号 wprName:产品名称 divCount:错误提示层序号
	 * @param response
	 * @return ActionForward 名称型号重复输出1，不重复输出0(如果有多个提示层会加上层序号)
	 */
	public ActionForward checkPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String divCount = request.getParameter("divCount");//错误提示层序号
		String wprName = TransStr.transStr(request.getParameter("wprName"));
		List list = prodBiz.proCheck(wprName);
		PrintWriter out = null;
		String isErr = "";
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (list.size() > 0) {
				isErr = "1";
			}
			else{
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
	 * 判断商品编号是否重复<br>
	 * @param request
	 *      parameter > wprCode:商品编号
	 * @param response
	 * @return ActionForward 编号重复输出1,2 不重复输出0,2<br>
	 */
	public ActionForward checkWprCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprCode = TransStr.transStr(request.getParameter("wprCode"));
		String divCount = request.getParameter("divCount");//错误提示层序号
		List list = null;
		PrintWriter out = null;
		String isErr = "";
		
		if (!wprCode.equals("")){
			list = prodBiz.findbyWprCode(wprCode);
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (list != null && list.size() > 0) {
				isErr = "1";
			}
			else{
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
	 * 商品详细 <br>
	 * @param request
	 *      parameter > wprId:商品ID
	 * @param response
	 * @return ActionForward 跳转到showProDesc页面<br>
	 * 		attribute > wmsProduct:商品实体 count:商品库存总量
	 */
	public ActionForward wmsProDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("wprId");
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		request.setAttribute("wmsProduct", wmsProduct);
		return mapping.findForward("showProDesc");
	}

	/**
	 * 进入商品编辑页面 <br>
	 * @param request
	 *      parameter > count:该商品在列表中所处的行数 wprId:商品ID
	 * @param response
	 * @return ActionForward 跳转到updProduct页面<br>
	 * 		attribute > typeList:商品单位列表 wmsProduct:商品实体 count:该商品在列表中所处的行数 wprId:商品ID
	 */
	public ActionForward toUpdPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String count = request.getParameter("count");
		String wprId = request.getParameter("wprId");
		
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		List list2 = typeListBiz.getEnabledType("15");
		
		request.setAttribute("wprId", wprId);
		request.setAttribute("typeList", list2);
		request.setAttribute("wmsProduct", wmsProduct);
		request.setAttribute("count", count);
		return mapping.findForward("updProduct");
	}

	/**
	 * 编辑商品信息 <br>
	 * @param request
	 *      parameter > wprId:商品ID wptId:商品分类ID wprUnit:商品单位
	 * @param response
	 * @return ActionForward 商品名称型号已存在或商品编号已存在跳转到error页面 成功修改跳转到popDivSuc页面<br>
	 * 		attribute > 商品名称型号已存在或商品编号已存在 errorMsg:商品名称型号已存在或商品编号已存在信息提示<br>
	 *      attribute > 成功修改 msg:成功修改信息提示<br>
	 */
	public ActionForward updProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("wprId");
		String wpType = request.getParameter("wptId");
		String wprUnit = request.getParameter("wprUnit");
		LimUser limUser = (LimUser) request.getSession().getAttribute("CUR_USER");
		DynaActionForm form1 = (DynaActionForm) form;
		WmsProduct productForm = (WmsProduct) form1.get("wmsProduct");
		
		WmsProduct product = prodBiz.wmsProDesc(Long.parseLong(wprId));
		List list = prodBiz.proCheck(productForm.getWprName());
		if (!product.getWprName().equals(productForm.getWprName())
				&& list.size() > 0) {
			request.setAttribute("errorMsg", "该商品已存在，请核实后再修改");
			return mapping.findForward("error");
		}
		if (productForm.getWprCode() != null
				&& !productForm.getWprCode().equals("")
				&& !productForm.getWprCode().equals(product.getWprCode())) {
			List<WmsProduct> list1 = prodBiz.findbyWprCode(productForm.getWprCode());
			if (list1.size() > 0) {
				request.setAttribute("errorMsg", "该商品编号已存在，请核实后再修改");
				return mapping.findForward("error");
			}
		}
		if (wpType != null && !wpType.equals("")) {
			productForm.setWmsProType(new WmsProType(Long.parseLong(wpType)));
		} else {
			productForm.setWmsProType(null);
		}
		if (wprUnit != null && !wprUnit.equals("")) {
			productForm.setTypeList(new TypeList(Long.parseLong(wprUnit)));
		} else {
			productForm.setTypeList(null);
		}
		productForm.setWprId(product.getWprId());
		productForm.setWprCreDate(product.getWprCreDate());
		productForm.setWprCuserCode(product.getWprCuserCode());
		productForm.setWprPic(product.getWprPic());
		productForm.setWprIsdel(product.getWprIsdel());
		productForm.setWprEuserCode(limUser.getUserSeName());
		productForm.setWprEditDate(GetDate.getCurTime());
		prodBiz.wmsProUpdate(productForm);
		request.setAttribute("msg", "修改商品");
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 产品销售明细（发货明细） <br>
	 * @param request
	 * 		para,attr >	prodId 产品ID
	 * @return ActionForward prodSalHistory
	 */
	public ActionForward toListProdHis(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId = request.getParameter("prodId");
		request.setAttribute("prodId", prodId);
		return mapping.findForward("prodSalHistory");
	}
	public void listProdHis(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		String prodId=request.getParameter("prodId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(orderBiz.listSalHistoryCount(prodId), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<RShipPro> list = orderBiz.listSalHistory(prodId, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("rshpShipment.pshOrder.cusCorCus.salEmp");
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
	 * 查看商品销售历史 <br>
	 * @param request
	 *      parameter > p:当前页码 wprId:商品ID
	 * @param response
	 * @return ActionForward 跳转到wprOrdHistory页面<br>
	 * 		attribute > wmsProduct:商品实体 count:商品销售记录条数 rordPro:产品销售历史列表 page:分页信息
	 */
	public ActionForward findOrdByWpr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("wprId");
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		int count = orderBiz.getCountByWpr(Long.parseLong(wprId));
		Page page = new Page((count), 40);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = orderBiz.findByWpr(Long.parseLong(wprId), page
				.getCurrentPageNo(), page.getPageSize());
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		//		List list=orderBiz.findByWpr(Long.parseLong(wprId));

		request.setAttribute("count", count);
		request.setAttribute("rordPro", list);
		request.setAttribute("page", page);
		request.setAttribute("wmsProduct", wmsProduct);

		return mapping.findForward("wprOrdHistory");
	}

	/**
	 * 删除商品 <br>
	 * @param request
	 *      para >	wprId:商品ID
	 * @param response
	 * @return ActionForward 不符合删除条件跳转到error页面 成功删除跳转到popDivSuc页面<br>
	 * 		attribute > 不符合删除条件 errorMsg:商品存在其他关联数据信息提示<br>
	 *      attribute > 成功删除 msg:成功删除信息提示<br>
	 */
	public ActionForward delWmsPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("wprId");
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		List<SalOrdCon> list = orderBiz.getOrdersByProd(wprId);//订单集合
		Iterator<SalOrdCon> iter = list.iterator();
		int orderCount = 0;
		int orderRecCount = 0;
		while (iter.hasNext()) {
			SalOrdCon salOrdCon = iter.next();
			if (salOrdCon.getSodIsfail().equals("0")) {
				orderCount++;
			}
			else{
				orderRecCount++;
			}
		}
		if (orderCount > 0 || orderRecCount > 0) {
			StringBuffer eMsg = new StringBuffer();
			StringFormat.getDelEMsg(eMsg,orderCount,orderRecCount,"订单");
			request.setAttribute("errorMsg", "商品删除失败，该商品已被其他数据关联："+eMsg.toString()+"");
			return mapping.findForward("error");
		} else {
			wmsProduct.setWprIsdel("1");
			prodBiz.wmsProUpdate(wmsProduct);
			request.setAttribute("msg", "删除商品");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 恢复商品 <br>
	 * @param request
	 *      parameter > id:商品ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:恢复成功信息提示
	 */
	public ActionForward recProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("id");
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		wmsProduct.setWprIsdel("0");
		prodBiz.wmsProUpdate(wmsProduct);
		request.setAttribute("msg", "恢复商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除商品不能再恢复 <br>
	 * @param request
	 *      parameter > id:商品ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:彻底删除成功信息提示
	 */
	public ActionForward delProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("id");
		WmsProduct wmsProduct = prodBiz.wmsProDesc(Long.parseLong(wprId));
		//删除关联图片
		if(wmsProduct.getWprPic()!=null){
			FileOperator.delFile(wmsProduct.getWprPic(), request);
		}
		prodBiz.deletePro(wmsProduct);
		
		request.setAttribute("msg", "删除商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 选择产品 <br>
	 * @param request
	 * 		attr >	prodListByType:按类别分类的产品;	prodListNoType:未分类产品;
	 * @return ActionForward prodTree
	 */
	public ActionForward searchProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		List<WmsProType> prodTypeList = prodBiz.findAllWptType();
		List<WmsProduct> prodListNoType = prodBiz.noTypePro();//未分类的商品
		request.setAttribute("prodTypeList", prodTypeList);
		request.setAttribute("prodListNoType", prodListNoType);
		request.setAttribute("type", type);
		return mapping.findForward("prodTree");
	}


	/**
	 * 产品查询 <br>
	 * @param request
	 *      para,attr >	pName:商品名称;  <br>
	 *      attr >	searProdList:搜索结果;
	 * @return ActionForward prodTree
	 */
	public ActionForward prodSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pName = request.getParameter("pName");
		String type = request.getParameter("type");
		List<WmsProduct> list = prodBiz.searchProdByName(pName);
		request.setAttribute("pName", pName);
		request.setAttribute("type", type);
		request.setAttribute("searProdList", list);
		return searchProType(mapping, form, request, response);
	}

	/**
	 * 跳转到待删除的商品 <br>
	 * @return ActionForward recProduct<br>
	 */
	public ActionForward toListDelProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return mapping.findForward("recProduct");
	}
	/**
	 * 获得待删除的商品 <br>
	 * @param request
	 *      para > p:当前页码; pageSize:每页显示记录数;
	 * @param response
	 * @return ActionForward 跳转到recProduct页面<br>
	 * 		attr > wmsProduct:符合条件的商品列表 page:分页信息
	 */
	public void findDelProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		Page page = new Page(prodBiz.findDelProductCount(), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = prodBiz.findDelProduct(page.getCurrentPageNo(), page
				.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("wmsProType");
		
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

		if(type.equals("prod")){
			batchOperate.recBatchDel("WmsProduct","wprId",code);
			request.setAttribute("msg", "批量删除商品");
		}
		return mapping.findForward("popDivSuc");
	}
	
	/**
	 * 产品运费列表 <br>
	 * @param request
	 * 		para,attr >	prodId:产品ID;
	 */
	public ActionForward toListProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodId=request.getParameter("prodId");
		request.setAttribute("prodId", prodId);
		return mapping.findForward("listProdTrans");
	}
	public void listProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId=request.getParameter("prodId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items =  {"pExp","pUnit", "pProv","pCity","pDistrict","pAmt"};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(prodBiz.listProdTransByProdIdCount(prodId), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ProdTrans> list = prodBiz.listProdTransByProdId(prodId, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
		ListForm listForm = new ListForm();
		listForm.setList(list);
		listForm.setPage(page);
		List awareCollect = new LinkedList();
		awareCollect.add("ptrProv");
		awareCollect.add("ptrCity");
		awareCollect.add("ptrDistrict");
		awareCollect.add("ptrExpTypeList");

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
	 * 保存运费标准 <br>
	 * @param request
	 * 		para >	prodId:产品ID
	 * 		attr >	prodId:产品ID;	expList:物流公司列表;		provList:省份列表;
	 * @param response
	 * @return ActionForward formProdTrans
	 */
	public ActionForward toSaveProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodId = request.getParameter("prodId");
		
		List<TypeList> expList = typeListBiz.getEnabledType("express");
		List provList = typeListBiz.getProv();//省份
		
		request.setAttribute("prodId", prodId);
		request.setAttribute("expList", expList);
		request.setAttribute("provList", provList);
		return mapping.findForward("formProdTrans");
	}
	public ActionForward saveProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId = request.getParameter("prodId");
		String expId = request.getParameter("expId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		DynaActionForm prodTransForm = (DynaActionForm) form;
		ProdTrans prodTrans = (ProdTrans) prodTransForm.get("prodTrans");
		
		if(StringFormat.isEmpty(prodId)){
			request.setAttribute("errorMsg", "产品不存在");
			return mapping.findForward("error");
		}
		else{
			prodTrans.setPtrProduct(new WmsProduct(prodId));
			if (StringFormat.isEmpty(expId)) {
				prodTrans.setPtrExpTypeList(null);
			} else {
				prodTrans.setPtrExpTypeList(new TypeList(Long.valueOf(expId)));
			}
			if (StringFormat.isEmpty(provId)) { provId = "1"; }
			if (StringFormat.isEmpty(cityId)) { cityId = "1"; }
			if (StringFormat.isEmpty(districtId)) { districtId = "1"; }
			prodTrans.setPtrProv(new CusArea(Long.valueOf(provId)));
			prodTrans.setPtrCity(new CusProvince(Long.valueOf(cityId)));
			prodTrans.setPtrDistrict(new CusCity(Long.valueOf(districtId)));
			prodBiz.saveProdTrans(prodTrans);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "新建运费标准");
			return mapping.findForward("popDivSuc");
		}
	}
	
	public void listPtrByProdIds(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodIds = request.getParameter("prodIds");
		String expId = request.getParameter("expId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		
		List<ProdTrans> list = prodBiz.listPtrByProdIds(prodIds, expId, provId, cityId, districtId);
		ListForm listForm = new ListForm();
		listForm.setList(list);
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
	
	public ActionForward toUpdProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String ptrId = request.getParameter("ptrId");
		String prodId = request.getParameter("prodId");
		
		List<TypeList> expList = typeListBiz.getEnabledType("express");
		List provList = typeListBiz.getProv();//省份
		ProdTrans prodTrans = prodBiz.findProdTrans(ptrId);
		
		request.setAttribute("prodId", prodId);
		request.setAttribute("prodTrans", prodTrans);
		request.setAttribute("expList", expList);
		request.setAttribute("provList", provList);
		return mapping.findForward("formProdTrans");
	}
	public ActionForward updProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId = request.getParameter("prodId");
		String expId = request.getParameter("expId");
		String provId = request.getParameter("provId");
		String cityId = request.getParameter("cityId");
		String districtId = request.getParameter("districtId");
		DynaActionForm prodTransForm = (DynaActionForm) form;
		ProdTrans prodTrans = (ProdTrans) prodTransForm.get("prodTrans");
		
		if(StringFormat.isEmpty(prodId)){
			request.setAttribute("errorMsg", "产品不存在");
			return mapping.findForward("error");
		}
		else{
			prodTrans.setPtrProduct(new WmsProduct(prodId));
			if (StringFormat.isEmpty(expId)) {
				prodTrans.setPtrExpTypeList(null);
			} else {
				prodTrans.setPtrExpTypeList(new TypeList(Long.valueOf(expId)));
			}
			if (StringFormat.isEmpty(provId)) { provId = "1"; }
			if (StringFormat.isEmpty(cityId)) { cityId = "1"; }
			if (StringFormat.isEmpty(districtId)) { districtId = "1"; }
			prodTrans.setPtrProv(new CusArea(Long.valueOf(provId)));
			prodTrans.setPtrCity(new CusProvince(Long.valueOf(cityId)));
			prodTrans.setPtrDistrict(new CusCity(Long.valueOf(districtId)));
			prodBiz.updProdTrans(prodTrans);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "修改运费标准");
			return mapping.findForward("popDivSuc");
		}
	}
	
	public ActionForward delProdTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ptrId = request.getParameter("ptrId");
		if(StringFormat.isEmpty(ptrId)){
			request.setAttribute("errorMsg", "该运费标准不存在");
			return mapping.findForward("error");
		}
		else{
			prodBiz.deleteProdTrans(ptrId);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "删除运费标准");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 提成率列表 <br>
	 * @param request
	 * 		para,attr >	prodId:产品ID;
	 */
	public ActionForward toListProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodId=request.getParameter("prodId");
		request.setAttribute("prodId", prodId);
		return mapping.findForward("listProdSalBack");
	}
	public void listProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId=request.getParameter("prodId");
		String p = request.getParameter("p");
		String pageSize = request.getParameter("pageSize");
		String orderCol = request.getParameter("orderCol");
		String isDe = request.getParameter("isDe");
		String orderItem = "";
		String[] items =  {"price","rate"};
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		if (pageSize == null || pageSize.equals("")) {
			pageSize = "30";//默认每页条数
		}
		if (orderCol != null && !orderCol.equals("")) {
			orderItem = items[Integer.parseInt(orderCol)];
		}
		Page page = new Page(prodBiz.listProdSalBackByProdIdCount(prodId), Integer.parseInt(pageSize));
		page.setCurrentPageNo(Integer.parseInt(p));
		List<ProdSalBack> list = prodBiz.listProdSalBackByProdId(prodId, orderItem, isDe, page.getCurrentPageNo(), page.getPageSize());
		
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
	 * 保存提成率 <br>
	 * @param request
	 * 		para,attr >	prodId:产品ID
	 * @return ActionForward formProdSalBack
	 */
	public ActionForward toSaveProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodId = request.getParameter("prodId");
		request.setAttribute("prodId", prodId);
		return mapping.findForward("formProdSalBack");
	}
	public ActionForward saveProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId = request.getParameter("prodId");
		DynaActionForm prodSalBackForm = (DynaActionForm) form;
		ProdSalBack prodSalBack = (ProdSalBack) prodSalBackForm.get("prodSalBack");
		
		if(StringFormat.isEmpty(prodId)){
			request.setAttribute("errorMsg", "产品不存在");
			return mapping.findForward("error");
		}
		else{
			prodSalBack.setPsbProduct(new WmsProduct(prodId));
			prodBiz.saveProdSalBack(prodSalBack);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "新建提成率");
			return mapping.findForward("popDivSuc");
		}
	}
	
	public void listAllPsbByProdId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String prodId = request.getParameter("prodId");
		
		List<ProdSalBack> list = prodBiz.listAllPsbByProdId(prodId);
		ListForm listForm = new ListForm();
		listForm.setList(list);
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
	
	public ActionForward toUpdProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String psbId = request.getParameter("psbId");
		String prodId = request.getParameter("prodId");
		
		ProdSalBack prodSalBack = prodBiz.findProdSalBack(psbId);
		
		request.setAttribute("prodId", prodId);
		request.setAttribute("prodSalBack", prodSalBack);
		return mapping.findForward("formProdSalBack");
	}
	public ActionForward updProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String prodId = request.getParameter("prodId");
		DynaActionForm prodSalBackForm = (DynaActionForm) form;
		ProdSalBack prodSalBack = (ProdSalBack) prodSalBackForm.get("prodSalBack");
		
		if(StringFormat.isEmpty(prodId)){
			request.setAttribute("errorMsg", "产品不存在");
			return mapping.findForward("error");
		}
		else{
			prodSalBack.setPsbProduct(new WmsProduct(prodId));
			prodBiz.updProdSalBack(prodSalBack);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "修改提成率");
			return mapping.findForward("popDivSuc");
		}
	}
	
	public ActionForward delProdSalBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String psbId = request.getParameter("psbId");
		if(StringFormat.isEmpty(psbId)){
			request.setAttribute("errorMsg", "该提成率不存在");
			return mapping.findForward("error");
		}
		else{
			ProdSalBack psb = prodBiz.findProdSalBack(psbId);
			prodBiz.deleteProdSalBack(psb);
			request.setAttribute("isIfrm", "1");
			request.setAttribute("msg", "删除提成率");
			return mapping.findForward("popDivSuc");
		}
	}
	
	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}
	public void setProdBiz(ProductBIZ prodBiz) {
		this.prodBiz = prodBiz;
	}
	public void setBatchOperate(BatchOperateDAO batchOperate) {
		this.batchOperate = batchOperate;
	}
	public void setCustomBiz(CustomBIZ customBiz) {
		this.customBiz = customBiz;
	}

}