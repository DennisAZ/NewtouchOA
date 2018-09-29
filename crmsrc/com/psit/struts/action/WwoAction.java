package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.OrderBIZ;
import com.psit.struts.BIZ.TypeListBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.BIZ.WmsManageBIZ;
import com.psit.struts.BIZ.WwoBIZ;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RStroPro;
import com.psit.struts.entity.RWmsChange;
import com.psit.struts.entity.RWmsWms;
import com.psit.struts.entity.RWoutPro;
import com.psit.struts.entity.WmsChange;
import com.psit.struts.entity.WmsCheck;
import com.psit.struts.entity.WmsLine;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.WmsStro;
import com.psit.struts.entity.WmsWarOut;
import com.psit.struts.util.CodeCreator;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.TransStr;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 库存管理操作 <br>
 * create_date: Aug 24, 2010,1:44:16 PM<br>
 */
public class WwoAction extends BaseDispatchAction {
	EmpBIZ empBiz = null;
	WwoBIZ wwoBIZ = null;
	WmsManageBIZ wmsManageBiz = null;
	OrderBIZ orderBiz = null;
	ModifyTypeDAO modType = null;
	UserBIZ userBiz = null;
	TypeListBIZ typeListBiz = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");
	DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * 判断用户是否有库存管理操作的权限 <br>
	 * create_date: Aug 24, 2010,1:53:46 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 有权限继续执行原有方法，没有权限跳转到limError页面
	 * @throws Exception <br>
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
	 * 
	 *  检测用户是否有库存管理的操作的权限码 <br>
	 * create_date: Aug 24, 2010,1:57:43 PM <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "wprHistory", "s024" },//查看商品出入库历史
				{ "toNewWwo", "w006" },{ "saveWwo", "w006" },//新建出库单
				{ "toUpdateWwo", "w006" },{ "updateWwo", "w006" },//修改出库单
				{ "delWwo", "w006" },//删除出库单
				{ "outWmsStro","w006" },//确认出库
				{ "appWwo","w007" },//审核出库
				{ "cancelWwoDesc","w008" },//撤销出库
				{ "toNewRWoutPro", "w006" },{ "newWwoPro", "w006" },//新建出库商品
				{ "toUpdWop", "w006" },{ "updateWoutPro", "w006" },//编辑出库商品
				{ "delRwp", "w006" },//删除出库商品
				{ "toNewWmsChange", "w009" },{ "newWmsChange", "w009" },//新建调拨单
				{ "toUpdateWch", "w009" },{ "updateWch", "w009" },//编辑调拨单
				{ "delWch", "w009" },//删除调拨单
				{ "wchCheckOut","w009" },{ "wchCheckIn","w009" },//确认调拨
				{ "appWch","w010" },//审核调拨
				{ "cancelWchDesc","w011" },//撤销调拨
				{ "toNewRww", "w009" },{ "newRww", "w009" },//新建调拨商品
				{ "toUpdateRww", "w009" },{ "updateRww", "w009" },//修改调拨商品
				{ "delRww", "w009" },//删除调拨商品
				{ "toNewWmsCheck", "w012" },{ "newWmsCheck", "w012" },//新建库存盘点
				{ "toUpdateWmc", "w012" },{ "updateWmc", "w012" },//编辑盘点
				{ "delWmc", "w012" },//删除盘点
				{ "wmsCheck","w012" },//确认盘点
				{ "appWmc","w013" },//审核盘点
				{ "cancelWmc","w014" },//撤销盘点
				{ "toInputWmc", "w012" },{ "newRwmcPro", "w012" },//保存盘点商品
				{ "toUpdateRwmc", "w012" },{ "updateRwmc", "w012" },//编辑盘点商品
				{ "delRwmc", "w012" },//删除盘点商品
				{ "addProType", "sy012" },//添加类别（商品类别）
				{ "modifyProType", "sy013" },//修改类别（商品类别）
				{ "delProType", "sy013" },//修改类别（删除商品类别）
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 进入新建出库单页面 <br>
	 * create_date: Aug 26, 2010,4:12:29 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转到newWwo页面<br>
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号
	 */
	public ActionForward toNewWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = wmsManageBiz.findAllWms();
		String wmsCode = request.getParameter("wmsCode");
		
		request.setAttribute("wmsStro", list);
		request.setAttribute("wmsCode", wmsCode);
		return mapping.findForward("newWwo");
	}
	
	/**
	 * 
	 * 保存出库单 <br>
	 * create_date: Aug 24, 2010,2:22:25 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter >	wst:仓库编号
	 * @param response
	 * @return ActionForward 编号重复跳转到error页面，保存成功跳转到popDivSuc页面
	 * 		attribute > msg:保存成功信息提示 errorMsg:编号重复跳转到错误页面
	 */
	public ActionForward saveWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wst");
		java.util.Date currentDate = new java.util.Date();
		CodeCreator codeCreator = new CodeCreator();
		
		DynaActionForm form1 = (DynaActionForm) form;
		WmsWarOut wmsWarOut = (WmsWarOut) form1.get("wmsWarOut");
		String wwoCode = codeCreator.createCode("PO"
				+ dateFormat3.format(currentDate), "wms_war_out", 0);
		List list = wwoBIZ.findByWwoCode(wwoCode);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该出库单号已被使用，请重新录入");
			return mapping.findForward("error");
		}
		else{
			if (wmsCode != "" && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsWarOut.setWmsStro(wmsStro);
			} else {
				wmsWarOut.setWmsStro(null);
			}
			wmsWarOut.setWwoInpDate(currentDate);
			wmsWarOut.setWwoCode(wwoCode);
			wmsWarOut.setWwoIsdel("1");
			wwoBIZ.saveWwo(wmsWarOut);
			request.setAttribute("redir", "wms_out");
			request.setAttribute("eId", wmsWarOut.getWwoId());
			request.setAttribute("msg", "新建出库单");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 
	 * 出库单查询 <br>
	 * create_date: Aug 24, 2010,2:41:01 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter >	p:当前页码 wwoTitle:出库单出题 wmsCode:仓库编号 wwoAppIsok:审核状态 
	 * 					isok:审核标识 wwoState,wwoState0,wwoState1:出库状态 startTime,endTime:创建日期
	 * 					wwoInpDate 创建日期判断后的数组
	 * @param response
	 * @return ActionForward 查询后跳转到wwoManage页面
	 * 		attribute > appCount:需审核的出库单数量 outWmsCount:需要出库的单据数量 wwoAppIsok:审核状态 
	 * 					wmsCode:仓库编号 woState,wwoState1:出库状态 startTime,endTime:创建日期
	 * 					isok:审核标识 wwoTitle:出库单出题 page:分页信息 wmsWarOut:出库单列表
	 */
	public ActionForward wwoSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wwoTitle = "";
		wwoTitle = TransStr.transStr(request.getParameter("wwoTitle"));
		String wmsCode = request.getParameter("wmsCode");
		String wwoAppIsok = request.getParameter("wwoAppIsok");
		String isok = request.getParameter("isok");
		String wwoState = "";
		String wwoState0 = request.getParameter("wwoState");
		String wwoState1 = request.getParameter("wwoState1");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] wwoInpDate = OperateDate.checkDate(startTime, endTime);
		String date = "";
		if (wwoInpDate[1] != null && !wwoInpDate[1].equals("")) {
			try {
				date = dateFormat.format(OperateDate.getDate(dateFormat
						.parse(wwoInpDate[1]), 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (wwoState1 != null && !wwoState1.equals("")) {
			wwoState = wwoState1;
		} else {
			wwoState = wwoState0;
		}
		Page page = new Page(wwoBIZ.getWwoCount(wwoTitle, wmsCode, wwoAppIsok,
				wwoState, wwoInpDate[0], date), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.WwoSearch(wwoTitle, wmsCode, wwoAppIsok, wwoState,
				wwoInpDate[0], date, page.getCurrentPageNo(), page
						.getPageSize());
		if (list.size() <= 0) {
			request.setAttribute("msg", "对不起，没有您想要的出库单！");
		}
		int appCount = 0;
		int outWmsCount = 0;
		if (isok != null && !isok.equals("")) {
			appCount = wwoBIZ.findOutApp(wmsCode, "2");
			outWmsCount = wwoBIZ.findOutApp(wmsCode, "1");
		}
		request.setAttribute("appCount", appCount);//今天要审核的出库单数量
		request.setAttribute("outWmsCount", outWmsCount);//今天要的出库的单据数量
		request.setAttribute("wwoAppIsok", wwoAppIsok);
		request.setAttribute("wmsCode", wmsCode);
		request.setAttribute("wwoState", wwoState0);
		request.setAttribute("wwoState1", wwoState1);
		request.setAttribute("startTime", wwoInpDate[0]);
		request.setAttribute("endTime", wwoInpDate[1]);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}
		request.setAttribute("isok", isok);
		request.setAttribute("wwoTitle", wwoTitle);
		request.setAttribute("page", page);
		request.setAttribute("wmsWarOut", list);
		return mapping.findForward("wwoManage");
	}

	/**
	 * 
	 * 进入商品出库页面 <br>
	 * create_date: Aug 24, 2010,3:38:48 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id
	 * @param response
	 * @return ActionForward 出库单实体存在返回rwoutPro页面，否则返回error页面
	 * 		attribute > wmsWarOut:出库单实体 errorMsg:错误信息页面
	 */
	public ActionForward wwoDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut != null) {
			request.setAttribute("wmsWarOut", wmsWarOut);
			//			String sm=request.getParameter("sm");
			//			if(sm!=null&&sm.equals("sm")){//订单预览
			//				return mapping.findForward("wwoDesc");
			//			}
			return mapping.findForward("rwoutPro");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}

	}

	/**
	 * 
	 * 进入审核出库单页面 <br>
	 * create_date: Aug 24, 2010,3:42:56 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id
	 * @param response
	 * @return ActionForward 出库单实体存在返回appWwo页面，否则返回error页面
	 * 		attribute > userName:用户名称 wmsWarOut:出库单实体 errorMsg:错误信息页面 wwoAppDate:当前日期
	 */
	public ActionForward goAppWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		java.util.Date currentDate = new java.util.Date();//当前日期
		if (wmsWarOut != null) {
			String date = dateFormat2.format(currentDate);
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			request.setAttribute("userName", limUser.getUserSeName());
			request.setAttribute("wmsWarOut", wmsWarOut);
			request.setAttribute("wwoAppDate", date);
			return mapping.findForward("appWwo");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 审核出库单 <br>
	 * create_date: Aug 24, 2010,3:47:26 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id wwoAppDesc:审核内容 wwoAppIsok 审核状态
	 * @param response
	 * @return ActionForward 出库单实体存在重新定向页面，否则返回error页面
	 * 		attribute > errorMsg:错误信息页面
	 */
	public ActionForward appWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		java.util.Date currentDate = new java.util.Date();//当前日期
		if (wmsWarOut != null) {
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			String wwoAppMan = limUser.getUserSeName();
			String wwoAppDesc = request.getParameter("wwoAppDesc");
			String wwoAppIsok = request.getParameter("wwoAppIsok");
			wmsWarOut.setWwoAppDate(currentDate);
			wmsWarOut.setWwoAppMan(wwoAppMan);
			wmsWarOut.setWwoAppDesc(wwoAppDesc);
			if (wwoAppIsok != null && !wwoAppIsok.equals("")) {
				wmsWarOut.setWwoAppIsok(wwoAppIsok);
			} else {
				wmsWarOut.setWwoAppIsok("1");
			}
			wwoBIZ.updateWwo(wmsWarOut);//更新出库单
			//			//详情下刷新iframe
			//			if(request.getParameter("isIfrm")!=null&&request.getParameter("isIfrm").equals("1")){
			//				request.setAttribute("isIfrm", "1");
			//			}
			//			request.setAttribute("msg", "审核成功！");
			//			return mapping.findForward("empty");
			String url = "wwoAction.do?op=wwoDesc&wwoId=" + wwoId;
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 进入新建出库商品页面 <br>
	 * create_date: Aug 24, 2010,3:52:05 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id
	 * @param response
	 * @return ActionForward 出库单实体存在跳转至newRwoutPro页面，否则跳转至error页面
	 * 		attribute > errorMsg:错误信息页面 wwoId:出库单id wmsWarOut:出库单实体
	 */
	public ActionForward toNewRWoutPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut != null) {
			request.setAttribute("wwoId", wwoId);
			request.setAttribute("wmsWarOut", wmsWarOut);
			return mapping.findForward("newRwoutPro");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 新建出库明细 <br>
	 * create_date: Aug 24, 2010,3:54:39 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id wprId:商品id rwoWoutNum:出库数量
	 * @param response
	 * @return ActionForward 库存明细没有这个商品跳或者出库单实体为null转至error页面，出库单实体不为null跳转至popDivSuc页面
	 * 		attribute > errorMsg:错误信息页面	msg:保存成功信息提示	
	 */
	public ActionForward newWwoPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
//		String wprId = request.getParameter("wprId");
//		WmsProduct wmsProduct = new WmsProduct();
//		wmsProduct.setWprId(Long.parseLong(wprId));
//		RWoutPro rwoutPro = new RWoutPro();
//		String wwoId = request.getParameter("wwoId");
//		Double rwoWoutNum = Double.parseDouble(request
//				.getParameter("rwoWoutNum"));
//		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
//		List list = wwoBIZ.findByWwo(Long.parseLong(wprId), Long
//				.parseLong(wwoId));
//		if (list.size() > 0) {
//			request.setAttribute("errorMsg", "该出库单已存在此商品");
//			return mapping.findForward("error");
//		}
//		if (wmsWarOut != null) {
//			String rwoRemark = request.getParameter("rwoRemark");
//			rwoutPro.setWmsProduct(wmsProduct);
//			rwoutPro.setWmsWarOut(wmsWarOut);
//			rwoutPro.setRwoWoutNum(rwoWoutNum);
//			rwoutPro.setRwoRemark(rwoRemark);
//			wwoBIZ.saveRwp(rwoutPro);
//
//			//			WmsLine wmsLine=new WmsLine();
//			//			wmsLine.setWliType("1");//流水类别--出库
//			//			wmsLine.setWliTypeCode(wwoId);
//			//			wmsLine.setWmsProduct(wmsProduct);
//			//			wmsLine.setWmsStro(wmsWarOut.getWmsStro());
//			//			wmsLine.setWliDate(wmsWarOut.getWwoInpDate());
//			//			wmsLine.setWliState("0");//未执行
//			//			wmsLine.setWliOutNum(rwoWoutNum);
//			//			wmsLine.setWliWmsId(rwoutPro.getRwoId());
//			//			wmsLine.setWliIsdel("1");
//			//			wwoBIZ.saveWli(wmsLine);
//
//			wmsWarOut.setWwoAppIsok("2");
//			wwoBIZ.updateWwo(wmsWarOut);//更新出库单
//			request.setAttribute("msg", "新建出库商品");
//			return mapping.findForward("popDivSuc");
//		} else {
//			request.setAttribute("errorMsg", "该单据不存在");
//			return mapping.findForward("error");
//		}
			return null;
	}

	/**
	 * 进入修改出库明细页面 <br>
	 * create_date: Aug 24, 2010,4:19:45 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwoId:出库明细id wprId:商品id wwoId:出库单据id
	 * @param response
	 * @return ActionForward 出库单实体存在跳转至updateRWoutPro页面，否则跳转至error页面
	 * 		attribute > wwoId:出库单据id rstroPro:库存明细实体 wmsWarOut:单据实体 rwoutPro:出库明细实体
	 * 		errorMsg:错误信息页面 wprId:商品id
	 */
	public ActionForward toUpdWop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwoId = Long.parseLong(request.getParameter("rwoId"));
		RWoutPro rwoutPro = wwoBIZ.getRWoutPro(rwoId);
		String wprId = rwoutPro.getWmsProduct().getWprId();
		request.setAttribute("wprId", wprId);
		Long wwoId = rwoutPro.getWmsWarOut().getWwoId();
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(wwoId);
		if (wmsWarOut != null) {
			String wmsCode = wmsWarOut.getWmsStro().getWmsCode();
//			List<RStroPro> list = wwoBIZ.findProNum(wmsCode, wprId);
			List<RStroPro> list =null;
			RStroPro rstroPro = (RStroPro) list.get(0);
			request.setAttribute("wwoId", wwoId);
			request.setAttribute("rstroPro", rstroPro);
			request.setAttribute("wmsWarOut", wmsWarOut);
			request.setAttribute("rwoutPro", rwoutPro);
			return mapping.findForward("updateRWoutPro");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 修改出库明细 <br>
	 * create_date: Aug 24, 2010,5:10:38 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwoId:出库明细id wwoId:出库单据id
	 * @param response
	 * @return ActionForward 出库单实体存在跳转至popDivSuc页面，否则跳转至error页面
	 * 		attribute > wwoId:出库单据id msg:修改成功信息提示 errorMsg:错误信息页面
	 */
	public ActionForward updateWoutPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwoId = Long.parseLong(request.getParameter("rwoId"));
		RWoutPro rwoutPro = new RWoutPro();
		rwoutPro.setRwoId(rwoId);
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut != null) {
			rwoutPro.setWmsWarOut(wmsWarOut);
			String wprId = request.getParameter("wprCode");
			rwoutPro.setWmsProduct(new WmsProduct(wprId));
			Double rwoWoutNum = Double.parseDouble(request
					.getParameter("rwoWoutNum"));
			rwoutPro.setRwoWoutNum(rwoWoutNum);
			rwoutPro.setRwoRemark(request.getParameter("rwoRemark"));
			wwoBIZ.updateRwp(rwoutPro);
			//更新订单
			if (wmsWarOut.getSalOrdCon() != null) {
				String orderCode = wmsWarOut.getSalOrdCon().getSodCode()
						.toString();
//				List orderPro = orderBiz.findByWpr(orderCode, wprId);
				List orderPro =null;
				if (orderPro.size() > 0) {//订单里-----------商品
					ROrdPro rordPro = (ROrdPro) orderPro.get(0);
					rordPro.setRopOutNum(rwoWoutNum);
					orderBiz.updateRop(rordPro);//修改商品
				}
			}
			//更新库存流水
			//			List list=wwoBIZ.findByWmsId("1", rwoId);
			//			WmsLine wmsLine=(WmsLine)list.get(0);
			//			wmsLine.setWliOutNum(rwoWoutNum);
			//			wwoBIZ.updateWli(wmsLine);
			request.setAttribute("wwoId", wwoId);
			request.setAttribute("msg", "修改出库商品");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 删除出库明细 <br>
	 * create_date: Aug 24, 2010,5:22:39 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwoId:出库明细id 
	 * @param response
	 * @return ActionForward 删除成功跳转至popDivSuc页面
	 * 		attribute > msg:修改成功信息提示
	 */
	public ActionForward delRwp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwoId = Long.parseLong(request.getParameter("rwoId"));
		RWoutPro rwoutPro = wwoBIZ.getRWoutPro(rwoId);
		WmsWarOut wmsWarOut = rwoutPro.getWmsWarOut();
		wwoBIZ.deleteRwp(rwoutPro);

//		String wmsId = request.getParameter("wmsId");
//		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wmsId));
		//更新入库单审核信息
		if (wmsWarOut.getRWoutPros().size() == 0) {
			wmsWarOut.setWwoAppIsok(null);
			wwoBIZ.updateWwo(wmsWarOut);//更新出库单
		}
		request.setAttribute("msg", "删除出库商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 查询库存明细列表 <br>
	 * create_date: Aug 24, 2010,5:31:35 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转至wmsStro页面
	 * 		attribute > wmsCode:出库编号 wmsStro:仓库实体 rstroPro:库存明细实体
	 */
	public ActionForward getAllRsp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		List list = wwoBIZ.getAllRsp(wmsCode);
		if (list.size() == 0) {
			request.setAttribute("msg", "该仓库暂无您需要的商品！");
		}
		request.setAttribute("wmsCode", wmsCode);
		WmsStro wmsStro = wmsManageBiz.findWmsStro(wmsCode);
		request.setAttribute("wmsStro", wmsStro);
		request.setAttribute("rstroPro", list);
		return mapping.findForward("wmsStro");
	}

	/**
	 * 
	 * 库存明细列表查询 <br>
	 * create_date: Aug 24, 2010,5:47:09 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页码 wmsCode:仓库编号 wprName:商品名称
	 * @param response
	 * @return ActionForward 查询后跳转至wmsStro页面
	 * 		attribute > page:分页信息 wmsCode:仓库编号 wprName:商品名称 rstroPro:库存明细列表 msg:列表为空提示信息
	 */
	public ActionForward stroSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		String wmsCode = request.getParameter("wmsCode");
		String wprName = TransStr.transStr(request.getParameter("wprName"));
		
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wwoBIZ.getCountByWms(wmsCode, wprName), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findProByWms(wmsCode, wprName, page
				.getCurrentPageNo(), page.getPageSize());
		
		if (list.size() <= 0) {
			request.setAttribute("msg", "该仓库暂无您需要的商品！");
		}
		request.setAttribute("page", page);
		request.setAttribute("wmsCode", wmsCode);
		request.setAttribute("wprName", wprName);
		request.setAttribute("rstroPro", list);
		return mapping.findForward("wmsStro");
	}

	/**
	 * 确认出库 <br>
	 * create_date: Aug 25, 2010,10:10:00 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id wwoUserName:领料人
	 * @param response
	 * @return ActionForward 库存明细为空,库存商品不足,出库成功跳转至empty页面,单据实体不存在跳转至error页面
	 * 		attribute > error:错误信息提示 msg:操作成功提示信息 errorMsg:错误信息提示
	 */
	public ActionForward outWmsStro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		java.util.Date currentDate = new java.util.Date();//当前日期
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		synchronized (this) {
			String wwoUserName = request.getParameter("wwoUserName");
			WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
			if (wmsWarOut != null) {
				if (!wmsWarOut.getWwoState().equals("1")) {
					//获取页面List
					String wmsCode = request.getParameter("wmsCode");//仓库编号		
					List list1 = wwoBIZ.getRwoutPro(wmsWarOut.getWwoCode());
					Iterator it1 = list1.iterator();
					if (list1.size() > 0) {
						while (it1.hasNext()) {
							RWoutPro rwoutPro = (RWoutPro) it1.next();
							String wprCode1 = rwoutPro.getWmsProduct().getWprId();//商品编号
							Double num1 = rwoutPro.getRwoWoutNum();//商品数量
							//获取仓库里的list
//							List list = wwoBIZ.findProNum(wmsCode, wprCode1);
							List list =null;
							if (list.size() == 0) {
								request.setAttribute("error", wmsWarOut
										.getWmsStro().getWmsName()
										+ "中"
										+ rwoutPro.getWmsProduct().getWprName()
										+ "库存为空，不能完成出库功能！");
								return mapping.findForward("empty");
							}
							if (list.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list.get(0);
								Double num = rstroPro1.getRspProNum();// 商品数量
								Long rspId = rstroPro1.getRspId();//仓库ID
								if (num1 <= num) {
									num = num - num1;
									rstroPro1.setRspProNum(num);
									rstroPro1.setRspId(rspId);
									wmsManageBiz.updatePsp(rstroPro1);
								} else {
									request.setAttribute("error", wmsWarOut
											.getWmsStro().getWmsName()
											+ "中"
											+ rwoutPro.getWmsProduct()
													.getWprName()
											+ "库存数量不够，请重新审核库存！");
									return mapping.findForward("empty");
								}
							}
							//查看是否有订单-----有更新订单里实际出库的商品数量
							if (wmsWarOut.getSalOrdCon() != null) {
								String orderCode = wmsWarOut.getSalOrdCon()
										.getSodCode().toString();
								if (orderCode != "" && !orderCode.equals("")) {
//									List orderPro = orderBiz.findByWpr(
//											orderCode, wprCode1);
									List orderPro =null;
									if (orderPro.size() > 0) {
										ROrdPro rordPro = (ROrdPro) orderPro
												.get(0);
										Double n = rordPro.getRopRealNum();
										if (n == 0 || n == null) {
											rordPro.setRopRealNum(num1);
										} else {
											rordPro.setRopRealNum(num1 + n);
										}
										orderBiz.updateRop(rordPro);//修改商品
									}
								}
							}
							//生成流水
							WmsLine wmsLine = new WmsLine();
							wmsLine.setWliType("1");//流水类别--出库
							wmsLine.setWliTypeCode(wwoId);
							wmsLine.setWmsProduct(rwoutPro.getWmsProduct());
							wmsLine.setWmsStro(wmsWarOut.getWmsStro());
							wmsLine.setWliDate(wmsWarOut.getWwoInpDate());
							wmsLine.setWliState("1");//已执行
							wmsLine.setWliOutNum(num1);
							wmsLine.setWliWmsId(rwoutPro.getRwoId());
							wmsLine.setWliIsdel("1");
							wmsLine.setWliDate(currentDate);
//							List list2 = wwoBIZ.findProNum(wmsCode, wprCode1);
							List list2 = null;
							if (list2.size() == 0) {
								wmsLine.setWliNum(0.0);
							} else if (list2.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list2.get(0);
								wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							}
							wmsLine.setWliMan(lim.getUserSeName());
							wwoBIZ.saveWli(wmsLine);
						}
					}
					wmsWarOut.setWwoState("1");
					wmsWarOut.setWwoOutDate(currentDate);//执行时间
					if (wwoUserName != null && !wwoUserName.equals(""))
						wmsWarOut.setWwoUserName(wwoUserName);
					wmsWarOut.setWwoResName(lim.getUserSeName());//执行人
					wwoBIZ.updateWwo(wmsWarOut);

					//				List li=wwoBIZ.findByTypeCode("1", wwoId);
					//				if(li.size()>0){
					//					for(int i=0;i<li.size();i++){
					//						WmsLine wmsLine=(WmsLine)li.get(i);
					//						wmsLine.setWliState("1");
					//						wmsLine.setWliDate(currentDate);
					//						wmsLine.setWliMan(lim.getUserSeName());
					//						wwoBIZ.updateWli(wmsLine);//更新流水
					//					}
					//				}

				}
				request.setAttribute("msg", "商品出库成功！");
				return mapping.findForward("empty");

			} else {
				request.setAttribute("errorMsg", "该单据不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 进入修改出库单页面 <br>
	 * create_date: Aug 25, 2010,10:34:33 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单号 wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 出库单实体存在跳转至updateWwo页面,否则跳转至error页面
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号 limUser:账号列表 wmsWarOut:出库实体
	 * 					errorMsg:错误信息提示
	 */
	public ActionForward toUpdateWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		String wmsCode = request.getParameter("wmsCode");
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut != null) {
			List list = wmsManageBiz.findAllWms();
			request.setAttribute("wmsStro", list);
			request.setAttribute("wmsCode", wmsCode);
			request.setAttribute("wmsWarOut", wmsWarOut);
			return mapping.findForward("updateWwo");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 修改出库单 <br>
	 * create_date: Aug 25, 2010,10:42:17 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsWarOut:出库单实体 wwoId:出库单id isIfrm:是否在详情页面中刷新iframe,1刷新
	 * @param response
	 * @return ActionForward 修改成功跳转至popDivSuc页面 实体不存在跳转至error页面
	 * 		attribute > isIfrm:是否在详情页面中更新若为1则是在详情页面中更新 msg:操作成功提示信息
	 * 					errorMsg:错误信息提示
	 */
	public ActionForward updateWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		WmsWarOut wmsWarOut = (WmsWarOut) form1.get("wmsWarOut");
		String wwoId = request.getParameter("wwoId");
		WmsWarOut wmsWarOut1 = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut1 != null) {
			wmsWarOut1.setWwoTitle(wmsWarOut.getWwoTitle());
			wmsWarOut1.setWwoRemark(wmsWarOut.getWwoRemark());
			wmsWarOut1.setWwoCode(wmsWarOut.getWwoCode());
			wmsWarOut1.setWwoUserName(wmsWarOut.getWwoUserName());//领用人
			String wmsCode = request.getParameter("wst");
			if (wmsCode != null && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsWarOut1.setWmsStro(wmsStro);
			} else {
				wmsWarOut1.setWmsStro(null);
			}
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			//经手人
			/*String userCode1=request.getParameter("userCode");
			
			if(userCode1!=null&&!userCode1.equals("")){
				LimUser limUser1=userBiz.allUser(userCode1);
				wmsWarOut1.setLimUser(limUser1);
				wmsWarOut1.setWwoUserName(limUser1.getUserSeName());
			}else{
				wmsWarOut1.setLimUser(limUser);	
				wmsWarOut1.setWwoUserName(limUser.getUserSeName());
			}*/
			java.util.Date currentDate = new java.util.Date();//当前日期
			wmsWarOut1.setWwoAltDate(currentDate);
			wmsWarOut1.setWwoAltName(limUser.getUserSeName());
			wwoBIZ.updateWwo(wmsWarOut1);
			//详情下刷新iframe
			if (request.getParameter("isIfrm") != null
					&& request.getParameter("isIfrm").equals("1")) {
				request.setAttribute("isIfrm", "1");
			}
			request.setAttribute("msg", "编辑出库单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 删除出库单 <br>
	 * create_date: Aug 25, 2010,10:52:55 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id  isIfrm:是否在详情页面中刷新iframe,1刷新
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面，实体不存在跳转至error页面
	 * 		attribute >  isIfrm:是否在详情页面中更新若为1则是在详情页面中更新  msg:操作成功提示信息 errorMsg:错误信息提示
	 */
	public ActionForward delWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		String isIfrm = request.getParameter("isIfrm");
		
		WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
		if (wmsWarOut != null) {
			List list1 = wwoBIZ.getRwoutPro(wmsWarOut.getWwoCode());//出库单----出库商品
			if (list1.size() > 0) {
				for (int i = 0; i < list1.size(); i++) {
					RWoutPro rwoutPro = (RWoutPro) list1.get(i);
					String wprCode1 = rwoutPro.getWmsProduct().getWprId();
					if (wmsWarOut.getSalOrdCon() != null) {
						String orderCode = wmsWarOut.getSalOrdCon()
								.getSodCode().toString();
						Double num1 = rwoutPro.getRwoWoutNum();//实际出库数量
						if (orderCode != "" && !orderCode.equals("")) {
//							List orderPro = orderBiz.findByWpr(orderCode,
//									wprCode1);//订单里-----------商品	
							List orderPro = null;
							if (orderPro.size() > 0) {
								ROrdPro rordPro = (ROrdPro) orderPro.get(0);
								Double m = rordPro.getRopOutNum();//已安排数量
								if (m != 0 && m != null) {
									if (m - num1 >= 0) {
										rordPro.setRopOutNum(m - num1);
										orderBiz.updateRop(rordPro);//修改商品
									}
								}
							}
						}
					}

				}
			}
			//			wmsWarOut.setWwoIsdel("0");
			//			wwoBIZ.updateWwo(wmsWarOut);
			wwoBIZ.deleteWwo(wmsWarOut);
			//			List li=wwoBIZ.findByTypeCode("1", wwoId);
			//			if(li.size()>0){
			//				for(int i=0;i<li.size();i++){
			//					WmsLine wmsLine=(WmsLine)li.get(i);
			//					wmsLine.setWliIsdel("0");
			//					wwoBIZ.updateWli(wmsLine);//更新流水
			//				}
			//			}
			if(isIfrm != null){
				request.setAttribute("isIfrm", isIfrm);
			}
			request.setAttribute("msg", "删除出库单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 恢复出库单 <br>
	 * create_date: Aug 25, 2010,11:01:47 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:出库单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc
	 * 		attribute >  msg:操作成功提示信息
	 */
	public ActionForward recWarOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsWarOut wmsWarOut = wwoBIZ.WwoSearchByCode(id);
		wmsWarOut.setWwoIsdel("1");
		wwoBIZ.updateWwo(wmsWarOut);
		request.setAttribute("msg", "恢复出库单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 删除出库单不能再恢复 <br>
	 * create_date: Aug 25, 2010,11:23:34 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:出库单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward delWarOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsWarOut wmsWarOut = wwoBIZ.WwoSearchByCode(id);
		wwoBIZ.deleteWwo(wmsWarOut);
		request.setAttribute("msg", "删除出库单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 恢复仓库调拨单 <br>
	 * create_date: Aug 25, 2010,11:44:30 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:调拨单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward recWmsChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsChange wmsChange = wwoBIZ.wchDesc(id);
		wmsChange.setWchIsdel("1");
		wwoBIZ.updateWch(wmsChange);
		request.setAttribute("msg", "恢复仓库调拨");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 删除仓库调拨单不能再恢复 <br>
	 * create_date: Aug 25, 2010,11:48:24 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:调拨单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward delWmsChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsChange wmsChange = wwoBIZ.wchDesc(id);
		wwoBIZ.delWch(wmsChange);
		request.setAttribute("msg", "删除仓库调拨");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 进入新建仓库调拨单页面 <br>
	 * create_date: Aug 25, 2010,11:55:47 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转至newWmsChange页面
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号
	 */
	public ActionForward toNewWmsChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		
		List list = wmsManageBiz.findAllWms();
		
		request.setAttribute("wmsStro", list);
		request.setAttribute("wmsCode", wmsCode);
		return mapping.findForward("newWmsChange");
	}

	/**
	 * 
	 * 新建仓库调拨单 <br>
	 * create_date: Aug 25, 2010,1:42:00 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > outWms:调出仓库编号 inWms:调入仓库编号
	 * 					wchOutDate:调出日期 wchInDate:调入日期
	 * @param response
	 * @return ActionForward 编号重复跳转至error页面，保存成功跳转至popDivSuc
	 * 		attribute > msg:操作成功提示信息 errorMsg:操作失败提示信息
	 */
	public ActionForward newWmsChange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("outWms");
		String wmsCode1 = request.getParameter("inWms");
		String wchOutDate = request.getParameter("wchOutDate");
		String wchInDate = request.getParameter("wchInDate");
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		DynaActionForm form1 = (DynaActionForm) form;
		WmsChange wmsChange = (WmsChange) form1.get("wmsChange");
		java.util.Date currentDate = new java.util.Date();//当前日期
		CodeCreator codeCreator = new CodeCreator();
		
		String wchCode = codeCreator.createCode("PC"
				+ dateFormat3.format(currentDate), "wms_change", 0);
		List list = wwoBIZ.findBywchCode(wchCode);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该调拨单号已被使用，请重新录入");
			return mapping.findForward("error");
		} else {
			if (wmsCode != "" && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsChange.setWmsStroByWchOutWms(wmsStro);
			} else {
				wmsChange.setWmsStroByWchOutWms(null);
			}
			if (wmsCode1 != "" && !wmsCode1.equals("")) {
				WmsStro wmsStro2 = new WmsStro();
				wmsStro2.setWmsCode(wmsCode1);
				wmsChange.setWmsStroByWchInWms(wmsStro2);
			} else {
				wmsChange.setWmsStroByWchInWms(null);
			}
			
			if (wchOutDate != null && !wchOutDate.equals("")) {
				Date wchOutDate1 = Date.valueOf(wchOutDate);
				wmsChange.setWchOutDate(wchOutDate1);
			} else {
				wmsChange.setWchOutDate(null);
			}
			if (wchInDate != null && !wchInDate.equals("")) {
				Date wchInDate1 = Date.valueOf(wchInDate);
				wmsChange.setWchInDate(wchInDate1);
			} else {
				wmsChange.setWchInDate(null);
			}
			wmsChange.setWchInpDate(currentDate);//录入时间
			wmsChange.setWchInpName(lim.getUserSeName());//录入人
			wmsChange.setWchCode(wchCode);
			wmsChange.setWchIsdel("1");
			wwoBIZ.saveWch(wmsChange);
			
			request.setAttribute("redir", "wms_change");
			request.setAttribute("eId", wmsChange.getWchId());
			request.setAttribute("msg", "新建调拨单");
			return mapping.findForward("popDivSuc");
		}

	}

	/**
	 * 
	 * 库存调拨查询 <br>
	 * create_date: Aug 25, 2010,1:54:35 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数 wchTitle:调拨主题 wmsCode:仓库编号 wchAppIsok:审核状态 isok:审核标识
	 * 					wchState0,wchState1:调拨状态 startTime,endTime:查询时间段
	 * @param response
	 * @return ActionForward 查询后跳转至wmsChange页面
	 * 		attribute > msg:列表为空提示信息 appCount:待审核的调拨单数量 wchWmsCount:待调拨调入的单据数量
	 * 					wchWmsCount2:待调拨调出的单据数量 wchAppIsok:审核状态 isok:审核标识
	 * 					wchTitle:调拨主题 wmsCode:仓库编号 wchState0,wchState1:调拨状态 
	 * 					startTime,endTime:查询时间段 wmsName:仓库名称 page:分页信息 wmsChange:调拨列表
	 */
	public ActionForward wchSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wchTitle = TransStr.transStr(request.getParameter("wchTitle"));
		String wmsCode = request.getParameter("wmsCode");
		String wchAppIsok = request.getParameter("wchAppIsok");
		String isok = request.getParameter("isok");
		String wchState = "";
		String wchState0 = request.getParameter("wchState");
		String wchState1 = request.getParameter("wchState1");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] wchInpDate = OperateDate.checkDate(startTime, endTime);
		String date = "";
		if (wchInpDate[1] != null && !wchInpDate[1].equals("")) {
			try {
				date = dateFormat.format(OperateDate.getDate(dateFormat
						.parse(wchInpDate[1]), 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (wchState1 != null && !wchState1.equals("")) {
			wchState = wchState1;
		} else {
			wchState = wchState0;
		}
		Page page = new Page(wwoBIZ.getWchCount(wchTitle, wmsCode, wchAppIsok,
				wchState, wchInpDate[0], date), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.wchSearch(wchTitle, wmsCode, wchAppIsok, wchState,
				wchInpDate[0], date, page.getCurrentPageNo(), page
						.getPageSize());
		if (list.size() <= 0) {
			request.setAttribute("msg", "对不起，没有您想要的库存调拨信息！");
		}
		int appCount = 0;
		int wchWmsCount = 0;
		int wchWmsCount2 = 0;
		if (isok != null && !isok.equals("")) {
			appCount = wwoBIZ.findWchApp(wmsCode, "2");
			wchWmsCount = wwoBIZ.findWchApp(wmsCode, "1");
			wchWmsCount2 = wwoBIZ.findApp2(wmsCode, "1");
		}
		request.setAttribute("appCount", appCount);//待审核的调拨单数量
		request.setAttribute("wchWmsCount", wchWmsCount);//待调拨调入的单据数量
		request.setAttribute("wchWmsCount2", wchWmsCount2);//待调拨调出的单据数量
		request.setAttribute("wchAppIsok", wchAppIsok);
		request.setAttribute("isok", isok);
		request.setAttribute("wchTitle", wchTitle);
		request.setAttribute("wmsCode", wmsCode);
		request.setAttribute("wchState", wchState0);
		request.setAttribute("wchState1", wchState1);
		request.setAttribute("startTime", wchInpDate[0]);
		request.setAttribute("endTime", wchInpDate[1]);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}
		request.setAttribute("page", page);
		request.setAttribute("wmsChange", list);
		return mapping.findForward("wmsChange");
	}

	/**
	 * 
	 * 进入库存调拨明细列表 <br>
	 * create_date: Aug 25, 2010,2:26:53 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 调拨实体存在跳转至rwmsWms页面,否则跳转至error页面
	 * 		attribute > errorMsg:错误提示信息 wmsChange:调拨实体
	 */
	public ActionForward wchDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wchId = Long.parseLong(request.getParameter("wchId"));
		String wmsCode = request.getParameter("wmsCode");
		request.setAttribute("wmsCode", wmsCode);
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		if (wmsChange != null) {
			request.setAttribute("wmsChange", wmsChange);
			return mapping.findForward("rwmsWms");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}

	}

	/**
	 * 
	 * 调拨单审核 <br>
	 * create_date: Aug 25, 2010,2:37:15 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wmsCode:仓库编号 wchAppDesc:审核内容 wchAppIsok:审核状态
	 * @param response
	 * @return ActionForward 审核成功重新定向页面 调拨实体不存在跳转至error页面
	 * 		attribute > errorMsg:错误信息提示
	 */
	public ActionForward appWch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wchId = Long.parseLong(request.getParameter("wchId"));
		String wmsCode = request.getParameter("wmsCode");
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		java.util.Date currentDate = new java.util.Date();//当前日期
		if (wmsChange != null) {
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			String wchAppMan = limUser.getUserSeName();
			String wchAppDesc = request.getParameter("wchAppDesc");
			String wchAppIsok = request.getParameter("wchAppIsok");
			wmsChange.setWchAppDate(currentDate);
			wmsChange.setWchAppMan(wchAppMan);
			wmsChange.setWchAppDesc(wchAppDesc);
			if (wchAppIsok != null && !wchAppIsok.equals("")) {
				wmsChange.setWchAppIsok(wchAppIsok);
			} else {
				wmsChange.setWchAppIsok("1");
			}
			wwoBIZ.updateWch(wmsChange);//更新调拨单
			//			request.setAttribute("msg", "审核成功！");
			//			return mapping.findForward("empty");
			String url = "wwoAction.do?op=wchDesc&wchId=" + wchId + "&wmsCode="
					+ wmsCode;
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 进入新建调拨明细页面 <br>
	 * create_date: Aug 25, 2010,2:46:52 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id 
	 * @param response
	 * @return ActionForward 调拨实体存在跳转至newRww页面，否则跳转至error页面
	 * 		attribute > wchId:调拨单id wmsChange:调拨实体 errorMsg:错误信息提示
	 */
	public ActionForward toNewRww(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wchId = Long.parseLong(request.getParameter("wchId"));
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		if (wmsChange != null) {
			request.setAttribute("wchId", wchId);
			request.setAttribute("wmsChange", wmsChange);
			return mapping.findForward("newRww");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 新建调拨明细 <br>
	 * create_date: Aug 25, 2010,2:52:38 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wprId:商品id rwwNum:调拨数量
	 * @param response
	 * @return ActionForward 保存成功跳转至popDivSuc页面 库存不足或者调拨实体不存在跳转至error页面
	 * 		attribute > errorMsg:错误信息提示 msg:操作成功提示信息
	 */
	public ActionForward newRww(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		String wprId = request.getParameter("wprId");
		Double rwwNum = Double.parseDouble(request.getParameter("rwoWoutNum"));
		WmsChange wmsChange = wwoBIZ.findWch(Long.parseLong(wch));

		List list = wwoBIZ
				.findByWch(Long.parseLong(wprId), Long.parseLong(wch));
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该调拨单已存在此商品");
			return mapping.findForward("error");
		}
		if (wmsChange != null) {
			RWmsWms rwmsWms = new RWmsWms();
			WmsProduct wmsProduct = new WmsProduct();
			wmsProduct.setWprId(wprId);
			rwmsWms.setWmsChange(wmsChange);
			rwmsWms.setWmsProduct(wmsProduct);
			rwmsWms.setRwwNum(rwwNum);
			rwmsWms.setRwwRemark(request.getParameter("rwwRemark"));
			wwoBIZ.saveRww(rwmsWms);

			//			WmsLine wmsLine=new WmsLine();
			//			wmsLine.setWliType("3");//流水类别--调出
			//			wmsLine.setWliTypeCode(wch);
			//			wmsLine.setWmsProduct(wmsProduct);
			//			wmsLine.setWmsStro(wmsChange.getWmsStroByWchOutWms());
			//			java.util.Date currentDate = new java.util.Date();//当前日期
			//			wmsLine.setWliDate(currentDate);
			//			wmsLine.setWliState("0");//未执行
			//			wmsLine.setWliOutNum(rwwNum);//调出量
			//			wmsLine.setWliWmsId(rwmsWms.getRwwId());
			//			wmsLine.setWliIsdel("1");
			//			wwoBIZ.saveWli(wmsLine);

			wmsChange.setWchAppIsok("2");
			wwoBIZ.updateWch(wmsChange);
			request.setAttribute("msg", "新建调拨商品");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 进入修改调拨明细页面 <br>
	 * create_date: Aug 25, 2010,2:57:38 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwwId:调拨明细id 
	 * @param response
	 * @return ActionForward 调拨实体存在跳转至updateRww页面，否则跳转至error页面
	 * 		attribute > wchId:调拨单id rstroPro:库存明细实体 wprCode:商品id rwwId:调拨明细id 
	 * 					errorMsg:错误信息提示
	 */
	public ActionForward toUpdateRww(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwwId = Long.parseLong(request.getParameter("rwwId"));
		
		RWmsWms rwmsWms = wwoBIZ.searchRww(rwwId);
		String wprCode = rwmsWms.getWmsProduct().getWprId();
		Long wchId = rwmsWms.getWmsChange().getWchId();
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		
		if (wchId != null) {
			request.setAttribute("wchId", wchId);
		}
		if (wmsChange != null) {
			String wmsCode = wmsChange.getWmsStroByWchOutWms().getWmsCode();
//			List<RStroPro> list = wwoBIZ.findProNum(wmsCode, wprCode);
			List<RStroPro> list =null;
			if (list.size() > 0) {
				RStroPro rstroPro = (RStroPro) list.get(0);
				request.setAttribute("rstroPro", rstroPro);
			}
			request.setAttribute("wprCode", wprCode);
			request.setAttribute("rwwId", rwwId);
			request.setAttribute("wmsChange", wmsChange);
			request.setAttribute("rwmsWms", rwmsWms);
			return mapping.findForward("updateRww");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 修改调拨明细 <br>
	 * create_date: Aug 25, 2010,3:07:43 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwwId:调拨明细id rwwNum:调拨数量
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward updateRww(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwwId = Long.parseLong(request.getParameter("rwwId"));
		RWmsWms rwmsWms = wwoBIZ.searchRww(rwwId);
		Double rwwNum = Double.parseDouble(request.getParameter("rwwNum"));
		rwmsWms.setRwwNum(rwwNum);
		rwmsWms.setRwwRemark(request.getParameter("rwwRemark"));
		wwoBIZ.updateRww(rwmsWms);

		//更新库存流水
		//		List list=wwoBIZ.findByWmsId("3", rwwId);
		//		WmsLine wmsLine=(WmsLine)list.get(0);
		//		wmsLine.setWliOutNum(rwwNum);
		//		wwoBIZ.updateWli(wmsLine);
		request.setAttribute("msg", "编辑调拨商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 删除调拨明细 <br>
	 * create_date: Aug 25, 2010,3:13:49 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwwId:调拨明细id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward delRww(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwwId = Long.parseLong(request.getParameter("rwwId"));
		RWmsWms rwmsWms = wwoBIZ.searchRww(rwwId);
		WmsChange wmsChange = rwmsWms.getWmsChange();
		wwoBIZ.delRww(rwmsWms);

//		String wmsId = request.getParameter("wmsId");
//		WmsChange wmsChange = wwoBIZ.findWch(Long.parseLong(wmsId));
		//更新调拨单审核信息(如果调拨明细为空)
		if (wmsChange.getRWmsWmses().size() == 0) {
			wmsChange.setWchAppIsok(null);
			wwoBIZ.updateWch(wmsChange);//更新调拨单
		}
		request.setAttribute("msg", "删除调拨商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 进入仓库调拨单修改页面 <br>
	 * create_date: Aug 25, 2010,3:22:50 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 调拨实体存在跳转至updateWch页面，否则跳转至error页面
	 * 		attribute > limUser:账号列表 wmsCode:仓库编号 wmsStro:仓库列表 wmsChange:调拨实体
	 * 					errorMsg:错误信息提示
	 */
	public ActionForward toUpdateWch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wchId = Long.parseLong(request.getParameter("wchId"));
		String wmsCode = request.getParameter("wmsCode");
		
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		if (wmsChange != null) {
			List list = wmsManageBiz.findAllWms();
			List list1 = userBiz.listValidUser();
			request.setAttribute("limUser", list1);
			request.setAttribute("wmsCode", wmsCode);
			request.setAttribute("wmsStro", list);
			request.setAttribute("wmsChange", wmsChange);
			return mapping.findForward("updateWch");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 仓库调拨单修改 <br>
	 * create_date: Aug 25, 2010,3:41:28 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsChange:调拨实体 wchId:调拨单id outWms:调出仓库 inWms:调入仓库
	 * 					wchOutDate:调出日期 wchInDate:调入日期
	 * @param response
	 * @return ActionForward 保存成功跳转至popDivSuc，否则跳转至error页面
	 * 		attribute > msg:操作成功信息 errorMsg:错误信息提示
	 */
	public ActionForward updateWch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		WmsChange wmsChange = (WmsChange) form1.get("wmsChange");
		Long wchId = Long.parseLong(request.getParameter("wchId"));
		WmsChange wmsChange1 = wwoBIZ.findWch(wchId);
		if (wmsChange1 != null) {
			//wmsChange.setWchId(wchId);
			String wmsCode = request.getParameter("outWms");
			if (wmsCode != "" && !wmsCode.equals("")) {
				//WmsStro wmsStro=new WmsStro();
				//wmsStro.setWmsCode(wmsCode);
				wmsChange1.setWmsStroByWchOutWms(new WmsStro(wmsCode));
			} else {
				wmsChange1.setWmsStroByWchOutWms(null);
			}
			String wmsCode1 = request.getParameter("inWms");
			if (wmsCode1 != "" && !wmsCode1.equals("")) {
				//WmsStro wmsStro2=new WmsStro();
				//wmsStro2.setWmsCode(wmsCode1);
				wmsChange1.setWmsStroByWchInWms(new WmsStro(wmsCode1));
			} else {
				wmsChange1.setWmsStroByWchInWms(null);
			}

			String outDate = request.getParameter("wchOutDate");
			if (outDate != null && !outDate.equals("")) {
				Date wchOutDate = Date.valueOf(outDate);
				wmsChange1.setWchOutDate(wchOutDate);
			} else {
				wmsChange1.setWchOutDate(null);
			}
			String inDate = request.getParameter("wchInDate");
			if (outDate != null && !outDate.equals("")) {
				Date wchInDate = Date.valueOf(inDate);
				wmsChange1.setWchInDate(wchInDate);
			} else {
				wmsChange1.setWchInDate(null);
			}
			/***修改**/
			LimUser lim = (LimUser) request.getSession()
					.getAttribute("limUser");
			java.util.Date currentDate = new java.util.Date();//当前日期
			wmsChange1.setWchAltDate(currentDate);//修改时间
			wmsChange1.setWchAltName(lim.getUserSeName());
			wmsChange1.setWchCode(wmsChange.getWchCode());
			wmsChange1.setWchRemark(wmsChange.getWchRemark());
			wmsChange1.setWchTitle(wmsChange.getWchTitle());

			wwoBIZ.updateWch(wmsChange1);
			request.setAttribute("msg", "编辑调拨单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 调入确认 <br>
	 * create_date: Aug 25, 2010,3:55:28 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 调拨明细为空,操作成功跳转至empty 调拨实体不存在跳转至error页面
	 * 		attribute > msg:操作成功提示信息 error,errorMsg:错误信息提示 wmsCode:仓库编号
	 */
	public ActionForward wchCheckIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		String wmsCode = request.getParameter("wmsCode");
		request.setAttribute("wmsCode", wmsCode);//(待考虑)
		Long wchId = Long.parseLong(wch);
		java.util.Date currentDate = new java.util.Date();//当前日期
		synchronized (this) {
			WmsChange wmsChange = wwoBIZ.findWch(wchId);
			if (wmsChange != null) {
				if (!wmsChange.getWchState().equals("3")) {
					LimUser limUser = (LimUser) request.getSession()
							.getAttribute("limUser");
					if (limUser != null && !limUser.equals("")) {
						wmsChange.setCheckInName(limUser.getUserSeName());
					} else {
						wmsChange.setCheckInName(null);
					}
					List list1 = wwoBIZ.getRww(wchId);//调出商品
					if (list1.size() == 0) {
						request.setAttribute("error", "调拨商品不能为空！");
						return mapping.findForward("empty");
					}
					Iterator it1 = list1.iterator();
					if (list1.size() > 0) {
						while (it1.hasNext()) {
							RWmsWms rwmsWms = (RWmsWms) it1.next();
							String wprCode1 = rwmsWms.getWmsProduct().getWprId();//商品编号
							Double num1 = rwmsWms.getRwwNum();//商品数量

							//获取调入仓库里的list
//							List list3 = wwoBIZ.findProNum(wmsChange
//									.getWmsStroByWchInWms().getWmsCode(),
//									wprCode1);
							List list3 =null;
							if (list3.size() == 0) {
								RStroPro rstroPro1 = new RStroPro();
								WmsStro wmsStro = wmsManageBiz
										.findWmsStro(wmsChange
												.getWmsStroByWchInWms()
												.getWmsCode());
								rstroPro1
										.setWmsProduct(rwmsWms.getWmsProduct());
								rstroPro1.setRspProNum(num1);
								rstroPro1.setWmsStro(wmsStro);
								wmsManageBiz.saveRsp(rstroPro1);
							}
							if (list3.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list3.get(0);
								Double num = rstroPro1.getRspProNum();// 商品数量
								Long rspId = rstroPro1.getRspId();//仓库ID
								num = num + num1;
								rstroPro1.setRspProNum(num);
								rstroPro1.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro1);
							}
							//生成流水
							WmsLine wmsLine1 = new WmsLine();
							wmsLine1.setWliType("2");//流水类别--调入
							wmsLine1.setWliTypeCode(wch);
							wmsLine1.setWmsProduct(rwmsWms.getWmsProduct());
							wmsLine1.setWmsStro(wmsChange
									.getWmsStroByWchInWms());
							wmsLine1.setWliDate(currentDate);
							wmsLine1.setWliState("1");//已执行
							wmsLine1.setWliInNum(num1);//调入量
							wmsLine1.setWliWmsId(rwmsWms.getRwwId());
							wmsLine1.setWliIsdel("1");
							wmsLine1.setWliMan(wmsChange.getCheckOutName());
//							List list2 = wwoBIZ.findProNum(wmsChange
//									.getWmsStroByWchInWms().getWmsCode(),
//									wprCode1);
							List list2 =null;
							if (list2.size() == 0) {
								wmsLine1.setWliNum(0.0);
							} else if (list2.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list2.get(0);
								wmsLine1.setWliNum(rstroPro1.getRspProNum());//库存余量
							}
							wwoBIZ.saveWli(wmsLine1);
						}
					}

					wmsChange.setWchInTime(currentDate);//调入库管确认时间
					wmsChange.setWchState("3");//已调入
					wwoBIZ.updateWch(wmsChange);
					//流水
					//			List li2=wwoBIZ.findByTypeCode("2", wch);//调入
					//			if(li2.size()>0){
					//				for(int i=0;i<li2.size();i++){
					//					WmsLine wmsLine=(WmsLine)li2.get(i);
					//					wmsLine.setWliState("1");
					//					//wmsLine.setWliDate(currentDate);
					//					wmsLine.setWliMan(wmsChange.getCheckInName());
					//					wwoBIZ.updateWli(wmsLine);//更新流水
					//				}
					//			}else{
					//				List li=wwoBIZ.findByTypeCode("3", wch);//调出
					//				if(li.size()>0){
					//					for(int i=0;i<li.size();i++){
					//						WmsLine wmsLine=(WmsLine)li.get(i);
					//						
					//						WmsLine wmsLine1=new WmsLine();
					//						wmsLine1.setWliType("2");//流水类别--调入
					//						wmsLine1.setWliTypeCode(wch);
					//						wmsLine1.setWmsProduct(wmsLine.getWmsProduct());
					//						wmsLine1.setWmsStro(wmsChange.getWmsStroByWchInWms());
					//						wmsLine1.setWliDate(currentDate);
					//						wmsLine1.setWliState("1");//已执行
					//						wmsLine1.setWliInNum(wmsLine.getWliOutNum());//调出量
					//						wmsLine1.setWliWmsId(wmsLine.getWliWmsId());
					//						wmsLine1.setWliIsdel("1");
					//						wmsLine1.setWliMan(wmsChange.getCheckOutName());
					//						wwoBIZ.saveWli(wmsLine1);
					//						}
					//					}
					//				  }

				}
				request.setAttribute("msg", "商品调入成功！");
				return mapping.findForward("empty");
			} else {
				request.setAttribute("errorMsg", "该单据不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 
	 * 调出确认 <br>
	 * create_date: Aug 25, 2010,4:14:17 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wmsCode:仓库编号 wchMatName:调出领料人
	 * @param response
	 * @return ActionForward 库存为空,库存不足,操作成功跳转至empty页面 实体不存在跳转至error页面
	 * 		attribute > error,errorMsg:错误信息提示 msg:操作成功信息提示
	 */
	public ActionForward wchCheckOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		String wmsCode = request.getParameter("wmsCode");
		request.setAttribute("wmsCode", wmsCode);
		Long wchId = Long.parseLong(wch);
		java.util.Date currentDate = new java.util.Date();//当前日期
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		synchronized (this) {
			String wchMatName = TransStr.transStr(request
					.getParameter("wchMatName"));
			WmsChange wmsChange = wwoBIZ.findWch(wchId);
			if (wmsChange != null) {
				if (!wmsChange.getWchState().equals("2")) {
					LimUser limUser = (LimUser) request.getSession()
							.getAttribute("limUser");
					if (limUser != null && !limUser.equals("")) {
						wmsChange.setCheckOutName(limUser.getUserSeName());
					} else {
						wmsChange.setCheckOutName(null);
					}
					List list1 = wwoBIZ.getRww(wchId);//调出商品
					if (list1.size() == 0) {
						request.setAttribute("error", "调拨商品不能为空！");
						return mapping.findForward("empty");
					}
					Iterator it1 = list1.iterator();
					if (list1.size() > 0) {
						while (it1.hasNext()) {
							RWmsWms rwmsWms = (RWmsWms) it1.next();
							String wprCode1 = rwmsWms.getWmsProduct().getWprId();//商品编号
							Double num1 = rwmsWms.getRwwNum();//商品数量
							//获取调出仓库里的list
//							List list = wwoBIZ.findProNum(wmsChange
//									.getWmsStroByWchOutWms().getWmsCode(),
//									wprCode1);
							List list =null;
							RStroPro rstroPro = null;
							if (list.size() == 0) {
								request.setAttribute("error", wmsChange
										.getWmsStroByWchOutWms().getWmsName()
										+ "中"
										+ rwmsWms.getWmsProduct().getWprName()
										+ "库存为空，不能完成出库功能！");
								return mapping.findForward("empty");
							}
							if (list.size() > 0) {
								rstroPro = (RStroPro) list.get(0);
								Double num = rstroPro.getRspProNum();//库存商品数量
								Long rspId = rstroPro.getRspId();
								if (num1 <= num) {
									num = num - num1;
									rstroPro.setRspId(rspId);
									rstroPro.setRspProNum(num);
									wmsManageBiz.updatePsp(rstroPro);
								} else {
									request.setAttribute("error", wmsChange
											.getWmsStroByWchOutWms()
											.getWmsName()
											+ "中"
											+ rwmsWms.getWmsProduct()
													.getWprName()
											+ "库存数量不够，请核对您的调拨商品数量！");
									return mapping.findForward("empty");
								}
							}
							//生成流水
							WmsLine wmsLine = new WmsLine();
							wmsLine.setWliType("3");//流水类别--调出
							wmsLine.setWliTypeCode(wch);
							wmsLine.setWmsProduct(rwmsWms.getWmsProduct());
							wmsLine.setWmsStro(wmsChange
									.getWmsStroByWchOutWms());
							wmsLine.setWliDate(currentDate);
							wmsLine.setWliState("1");//已执行
							wmsLine.setWliOutNum(num1);//调出量
							wmsLine.setWliWmsId(rwmsWms.getRwwId());
							wmsLine.setWliMan(lim.getUserSeName());
							wmsLine.setWliIsdel("1");
//							List list2 = wwoBIZ.findProNum(wmsChange
//									.getWmsStroByWchOutWms().getWmsCode(),
//									wprCode1);
							List list2 =null;
							
							if (list2.size() == 0) {
								wmsLine.setWliNum(0.0);
							} else if (list2.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list2.get(0);
								wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							}
							wwoBIZ.saveWli(wmsLine);
						}
					}
					wmsChange.setWchOutTime(currentDate);//调出库管确认时间
					wmsChange.setWchState("2");//已调出
					if (wchMatName != null && !wchMatName.equals(""))
						wmsChange.setWchMatName(wchMatName);
					wwoBIZ.updateWch(wmsChange);
					//			//流水
					//			List li=wwoBIZ.findByTypeCode("3", wch);
					//			if(li.size()>0){
					//				for(int i=0;i<li.size();i++){
					//					WmsLine wmsLine=(WmsLine)li.get(i);
					//					wmsLine.setWliState("1");//已执行
					//					wmsLine.setWliMan(wmsChange.getCheckOutName());
					//					wmsLine.setWliDate(currentDate);
					//					wwoBIZ.updateWli(wmsLine);//更新流水
					//				}
					//			   }
				}
				request.setAttribute("msg", "商品调出成功！");
				return mapping.findForward("empty");
			} else {
				request.setAttribute("errorMsg", "该单据不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 
	 * 删除仓库调拨单 <br>
	 * create_date: Aug 25, 2010,4:37:55 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id
	 * @param response
	 * @return ActionForward 调拨实体存在跳转至popDivSuc页面,否则跳转至error页面
	 * 		attribute > errorMsg:错误信息提示 msg:操作成功信息提示
	 */
	public ActionForward delWch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		Long wchId = Long.parseLong(wch);
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		if (wmsChange != null) {
			//			wmsChange.setWchIsdel("0");
			//			wwoBIZ.updateWch(wmsChange);
			wwoBIZ.delWch(wmsChange);
			//			List li=wwoBIZ.findByTypeCode("3", wch);//调出流水
			//			if(li.size()>0){
			//				for(int i=0;i<li.size();i++){
			//					WmsLine wmsLine=(WmsLine)li.get(i);
			//					wmsLine.setWliIsdel("0");
			//					wwoBIZ.updateWli(wmsLine);//更新流水
			//				}
			//			}
			//			List lin=wwoBIZ.findByTypeCode("2", wch);//调入流水
			//			if(lin.size()>0){
			//				for(int i=0;i<lin.size();i++){
			//					WmsLine wmsLine=(WmsLine)lin.get(i);
			//					wmsLine.setWliIsdel("0");
			//					wwoBIZ.updateWli(wmsLine);//更新流水
			//				}
			//			}
			request.setAttribute("msg", "删除调拨单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该调拨单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 商品类别树 <br>
	 * create_date: Aug 25, 2010,5:58:59 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转至proTypeTree页面
	 * 		attribute > proType 类别列表
	 */
	public ActionForward proTypeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = wwoBIZ.findAllWpt();
		request.setAttribute("proType", list);
		return mapping.findForward("proTypeTree");
	}

	/**
	 * 
	 * 当前类别 <br>
	 * create_date: Aug 26, 2010,9:43:01 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wptId:类别id
	 * @param response
	 * @return ActionForward 跳转至showProType
	 * 		attribute > wmsProType:类别实体
	 */
	public ActionForward getProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wptId = request.getParameter("wptId");
		WmsProType wmsProType = wwoBIZ.wptFindById(Long.parseLong(wptId));
		request.setAttribute("wmsProType", wmsProType);
		return mapping.findForward("showProType");
	}

	/**
	 * 
	 * 添加下级类别 <br>
	 * create_date: Aug 26, 2010,9:54:11 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wptId:类别id wptName:类别名称 newWpt:是否为新类别 wptIsenabled:是否启用
	 * @param response
	 * @return ActionForward 是新类别跳转至popDivSuc类别，否则重新定向跳转
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward addProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		WmsProType wmsProType = new WmsProType();
		String wptId = request.getParameter("wptId");
		String wptName = request.getParameter("wptName");
		String newWpt = request.getParameter("newWpt");
		String wptIsenabled = request.getParameter("wptIsenabled");
		wmsProType.setWptName(wptName);
		if (newWpt != null && newWpt.equals("newWpt")) {
			//wmsProType.setWmsProType(null);
			wmsProType.setEnabledType(wptIsenabled);
			wwoBIZ.saveWpt(wmsProType);
			request.setAttribute("msg", "添加类别");
			return mapping.findForward("popDivSuc");
		}
		wmsProType.setWmsProType(new WmsProType(Long.parseLong(wptId)));
		wmsProType.setWptIsenabled("1");
		wwoBIZ.saveWpt(wmsProType);
		String url = "wwoAction.do?op=getProType&wptId=" + wptId;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 删除商品类别 <br>
	 * create_date: Aug 26, 2010,10:05:49 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wptId:商品id
	 * @param response
	 * @return ActionForward 类别未被使用,或者没有下级重新定向跳转，否则跳转至empty页面
	 * 		attribute > error:错误提示信息 
	 */
	public ActionForward delProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wptId = request.getParameter("wptId");
		WmsProType wmsProType = wwoBIZ.wptFindById(Long.parseLong(wptId));
		List list = wwoBIZ.findByUpId(Long.parseLong(wptId));
		if (list.size() > 0) {
			request.setAttribute("error", "请先删除此类别的下级类别！");
			return mapping.findForward("empty");
		}
		if (!wmsProType.getWmsProducts().isEmpty()) {
			request.setAttribute("error", "请确认该类别未被使用！");
			return mapping.findForward("empty");
		}

		wwoBIZ.deleteProType(wmsProType);
		String url = "typeManage/showProType.jsp";
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * 获得某商品类别的所有下级类别 <br>
	 * create_date: Aug 26, 2010,10:15:59 AM <br>
	 * @param wmsProType 类别实体
	 * @param lowLevelList 下级类别列表
	 * @return List<WmsProType> 类别列表
	 */
	public List<WmsProType> getLowProType(WmsProType wmsProType,
			ArrayList<WmsProType> lowLevelList) {
		if (wmsProType.getWmsProTypes().size() == 0) {
			return lowLevelList;
		} else {
			for (Iterator<WmsProType> it = wmsProType.getWmsProTypes()
					.iterator(); it.hasNext();) {
				WmsProType wmsProType1 = (WmsProType) it.next();
				lowLevelList.add(wmsProType1);
				//getLowProType(wmsProType1,list,lowLevelList);
				getLowProType(wmsProType1, lowLevelList);
			}
		}
		return lowLevelList;
	}

	/**
	 * 
	 * 判断一个类别是否是另一个类别的下级 <br>
	 * create_date: Aug 26, 2010,10:25:15 AM <br>
	 * @param wmsProType 类别实体
	 * @param upWptId 上级类别id
	 * @return boolean 有下级类别返回true,否则返回false
	 */
	public boolean isLowLevelType(WmsProType wmsProType, Long upWptId) {
		//List<WmsProType> list = wwoBIZ.findAllWpt();
		ArrayList<WmsProType> arryList = new ArrayList<WmsProType>();
		List<WmsProType> lowLevelList = getLowProType(wmsProType, arryList);
		if (lowLevelList.size() == 0)
			return false;
		else {
			for (Iterator<WmsProType> it = lowLevelList.iterator(); it
					.hasNext();) {
				WmsProType wmsProType1 = (WmsProType) it.next();
				if (wmsProType1.getWptId().equals(upWptId)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 
	 * 修改商品类别的上级类别 <br>
	 * create_date: Aug 26, 2010,10:29:32 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > currWptId:当前类别id upTypeId1:上级类别id
	 * @param response
	 * @return ActionForward 操作完成后重新定向页面,若上级id是为下级id跳转至error页面
	 * 		attribute > error:错误信息提示
	 */
	public ActionForward modUpProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long currWptId = Long.parseLong(request.getParameter("currWptId"));
		String upTypeId1 = request.getParameter("upTypeId");
		WmsProType wmsProType = wwoBIZ.wptFindById(currWptId);
		Long upTypeId = null;
		if (upTypeId1 != null && !upTypeId1.equals(""))
			upTypeId = Long.parseLong(upTypeId1);
		if (upTypeId == null) {
			wmsProType.setWmsProType(null);
		} else if (isLowLevelType(wmsProType, upTypeId)) {
			request.setAttribute("error", "不能将自己的下级设为自己的上级！");
			return mapping.findForward("empty");
		} else {
			wmsProType.setWmsProType(new WmsProType(upTypeId));
		}
		wwoBIZ.updateProType(wmsProType);
		String url = "wwoAction.do?op=getProType&wptId=" + currWptId;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 修改商品类别 <br>
	 * create_date: Aug 26, 2010,10:41:23 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wptId:类别id wptName:列表名称
	 * @param response
	 * @return ActionForward 操作完成后重新定向页面
	 */
	public ActionForward modifyProType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wptId = request.getParameter("wptId");
		String wptName = request.getParameter("wptName");
		WmsProType wmsProType = wwoBIZ.wptFindById(Long.parseLong(wptId));
		wmsProType.setWptName(wptName);
		wwoBIZ.updateProType(wmsProType);
		String url = "wwoAction.do?op=getProType&wptId=" + wptId;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 进入撤销出库页面 <br>
	 * create_date: Aug 26, 2010,10:46:01 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id
	 * @param response
	 * @return ActionForward 跳转至cancelWwo
	 * 		attribute > wwoId:出库单id
	 */
	public ActionForward cancelWwo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		request.setAttribute("wwoId", wwoId);
		return mapping.findForward("cancelWwo");
	}

	/**
	 * 
	 * 撤销出库操作 <br>
	 * create_date: Aug 26, 2010,10:50:16 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wwoId:出库单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面，否则跳转至error页面
	 * 		attribute > msg:操作成功提示信息 errorMsg:操作错误提示信息
	 */
	public ActionForward cancelWwoDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwoId = request.getParameter("wwoId");
		synchronized (this) {
			WmsWarOut wmsWarOut = wwoBIZ.findWwo(Long.parseLong(wwoId));
			if (wmsWarOut != null) {
				if (!wmsWarOut.getWwoState().equals("3")) {
					String wmsCode = wmsWarOut.getWmsStro().getWmsCode();
					List list = wwoBIZ.getRwoutPro(wmsWarOut.getWwoCode());//(一个出库单对应一个仓库)	
					Iterator it = list.iterator();
					if (list.size() > 0) {
						while (it.hasNext()) {
							RWoutPro rwoutPro = (RWoutPro) it.next();
							String wprCode = rwoutPro.getWmsProduct().getWprId();//商品编号
							Double num1 = rwoutPro.getRwoWoutNum();//商品数量
							//获取仓库里的list
//							List list1 = wwoBIZ.findProNum(wmsCode, wprCode);
							List list1 =null;
							if (list1 == null || list1.size() == 0) {
								RStroPro rstroPro1 = new RStroPro();
								rstroPro1.setWmsProduct(rwoutPro
										.getWmsProduct());
								rstroPro1.setRspProNum(num1);
								rstroPro1.setWmsStro(wmsWarOut.getWmsStro());
								wmsManageBiz.saveRsp(rstroPro1);
							} else {
								RStroPro rstroPro1 = (RStroPro) list1.get(0);
								Double num = rstroPro1.getRspProNum();// 商品数量
								Long rspId = rstroPro1.getRspId();//仓库ID
								num = num + num1;
								rstroPro1.setRspProNum(num);
								rstroPro1.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro1);
							}
							if (wmsWarOut.getSalOrdCon() != null) {
								String orderCode = wmsWarOut.getSalOrdCon()
										.getSodCode().toString();
//								List orderPro = orderBiz.findByWpr(orderCode,
//										wprCode);//订单里-----------商品	
								List orderPro = null;
								if (orderPro.size() > 0) {
									ROrdPro rordPro = (ROrdPro) orderPro.get(0);
									Double n = rordPro.getRopRealNum();//实际出库数量
									Double m = rordPro.getRopOutNum();//已安排出库数量
									if (n != 0 && n != null) {
										if (n - num1 >= 0) {
											rordPro.setRopRealNum(n - num1);
										}
									}
									if (m != 0 && m != null) {
										if (m - num1 >= 0) {
											rordPro.setRopOutNum(m - num1);
										}
									}
									orderBiz.updateRop(rordPro);//修改商品
								}
							}
						}
					}
					wmsWarOut.setWwoState("3");//撤销状态
					java.util.Date currentDate = new java.util.Date();//当前日期
					//			wmsWarOut.setWwoOutDate(currentDate);//执行时间
					wmsWarOut.setWwoCanDate(currentDate);//撤销时间
					LimUser lim = (LimUser) request.getSession().getAttribute(
							"limUser");
					wmsWarOut.setWwoCanMan(lim.getUserSeName());//撤销执行人
					wwoBIZ.updateWwo(wmsWarOut);
					List li = wwoBIZ.findByTypeCode("1", wwoId);
					if (li.size() > 0) {
						for (int i = 0; i < li.size(); i++) {
							WmsLine wmsLine = (WmsLine) li.get(i);
							wmsLine.setWliState("2");
							wmsLine.setWliMan(lim.getUserSeName());
							//					List list2=wwoBIZ.findProNum(wmsCode,wmsLine.getWmsProduct().getWprId());
							//					if(list2.size()==0){
							//						wmsLine.setWliNum(0.0);
							//					}else if(list2.size()>0){
							//						RStroPro rstroPro1=(RStroPro)list2.get(0);
							//						wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							//					}
							//					wmsLine.setWliDate(currentDate);
							wwoBIZ.updateWli(wmsLine);//更新流水
						}
					}
				}

				request.setAttribute("msg", "撤销出库");
				return mapping.findForward("popDivSuc");
			} else {
				request.setAttribute("errorMsg", "该出库单不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 
	 * 进入撤销调拨页面 <br>
	 * create_date: Aug 26, 2010,11:02:01 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id wm:跳转标识
	 * @param response
	 * @return ActionForward 调拨单据实体存在跳转至cancelWwo页面,否则跳转至error页面
	 * 		attribute > wchOut,wchId:调拨单id wmsChange:调拨单实体 errorMsg:错误信息提示
	 */
	public ActionForward cancelWch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		Long wchId = Long.parseLong(wch);
		WmsChange wmsChange = wwoBIZ.findWch(wchId);
		if (wmsChange != null) {
			String wm = request.getParameter("wm");
			if (wm != null && wm.equals("wm")) {
				request.setAttribute("wchOut", wch);
			} else {
				request.setAttribute("wmsChange", wmsChange);
				request.setAttribute("wchId", wch);
			}
			return mapping.findForward("cancelWwo");
		} else {
			request.setAttribute("errorMsg", "该调拨单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 撤销调拨操作 <br>
	 * create_date: Aug 26, 2010,11:09:30 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面，调拨实体不存在跳转至error页面
	 * 		attribute > msg:操作成功提示信息 errorMsg:操作错误提示信息
	 */
	public ActionForward cancelWchDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		Long wchId = Long.parseLong(wch);
		java.util.Date currentDate = new java.util.Date();//当前日期
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		synchronized (this) {
			WmsChange wmsChange = wwoBIZ.findWch(wchId);
			if (wmsChange != null) {
				if (!wmsChange.getWchState().equals("4")) {
					String outWms = wmsChange.getWmsStroByWchOutWms()
							.getWmsCode();//调出仓库编号
					String inWms = wmsChange.getWmsStroByWchInWms()
							.getWmsCode();//调入仓库编号
					List list = wwoBIZ.getRww(wchId);
					Iterator it = list.iterator();
					if (list.size() > 0) {
						while (it.hasNext()) {
							RWmsWms rwmsWms = (RWmsWms) it.next();
							String wprCode = rwmsWms.getWmsProduct().getWprId();//商品编号
							Double num1 = rwmsWms.getRwwNum();//商品数量
							//获取仓库里的list-----执行调出
//							List list1 = wwoBIZ.findProNum(inWms, wprCode);
							List list1 = null;
							if (list1 == null || list1.size() == 0) {
								request.setAttribute("errorMsg", "该仓库已无'"
										+ rwmsWms.getWmsProduct().getWprName()
										+ "'商品,无法完成此操作");
								return mapping.findForward("error");
							} else {
								RStroPro rstroPro1 = (RStroPro) list1.get(0);
								Double num = rstroPro1.getRspProNum();// 商品数量
								Long rspId = rstroPro1.getRspId();//仓库ID
								num = num - num1;
								if (num < 0) {
									request.setAttribute("errorMsg", "该仓库'"
											+ rwmsWms.getWmsProduct()
													.getWprName()
											+ "'数量不够,无法完成撤销操作");
									return mapping.findForward("error");
								}
								rstroPro1.setRspProNum(num);
								rstroPro1.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro1);
							}

							//获取仓库里的list-----执行调入
//							List list2 = wwoBIZ.findProNum(outWms, wprCode)
							List list2 = null;;
							if (list2 == null || list2.size() == 0) {
								//request.setAttribute("errorMsg", "该仓库已无'"+rwmsWms.getWmsProduct().getWprName()+"'商品,无法完成此操作");
								//return mapping.findForward("error");
								RStroPro rstroPro1 = new RStroPro();
								rstroPro1
										.setWmsProduct(rwmsWms.getWmsProduct());
								rstroPro1.setRspProNum(num1);
								rstroPro1.setWmsStro(wmsChange
										.getWmsStroByWchInWms());
								wmsManageBiz.saveRsp(rstroPro1);
							} else {
								RStroPro rstroPro2 = (RStroPro) list2.get(0);
								Double num = rstroPro2.getRspProNum();// 商品数量
								Long rspId = rstroPro2.getRspId();//仓库ID
								num = num + num1;
								rstroPro2.setRspProNum(num);
								rstroPro2.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro2);
							}

						}
					}
					/*String wchState=request.getParameter("wchState");
					if(wchState!=null&&!wchState.equals("")){
						wmsChange.setWchState("0");//调入（撤销）
					}else{*/
					wmsChange.setWchState("4");//整个（撤销）
					//}

					//wmsChange.setLimUserByWchCheckIn(null);
					//wmsChange.setLimUserByWchCheckOut(null);
					//wmsChange.setWchOutTime(null);
					//wmsChange.setWchInTime(null);
					wmsChange.setWchCanDate(currentDate);
					wmsChange.setWchCanMan(lim.getUserSeName());
					wwoBIZ.updateWch(wmsChange);

					List li = wwoBIZ.findByTypeCode("2", wch);//调入库

					if (li.size() > 0) {
						for (int i = 0; i < li.size(); i++) {
							WmsLine wmsLine = (WmsLine) li.get(i);
							wmsLine.setWliState("2");
							//wmsLine.setWliMan(null);
							//					wmsLine.setWliDate(currentDate);
							//					List list2=wwoBIZ.findProNum(wmsLine.getWmsStro().getWmsCode(),wmsLine.getWmsProduct().getWprId());
							//					if(list2.size()==0){
							//						wmsLine.setWliNum(0.0);
							//					}else if(list2.size()>0){
							//						RStroPro rstroPro1=(RStroPro)list2.get(0);
							//						wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							//					}
							wwoBIZ.updateWli(wmsLine);//更新流水
						}
					}
					List li2 = wwoBIZ.findByTypeCode("3", wch);//调出库
					if (li2.size() > 0) {
						for (int i = 0; i < li2.size(); i++) {
							WmsLine wmsLine = (WmsLine) li2.get(i);
							wmsLine.setWliState("2");
							//wmsLine.setWliMan(null);
							//					wmsLine.setWliDate(currentDate);
//							List list2 = wwoBIZ.findProNum(wmsLine.getWmsStro()
//									.getWmsCode(), wmsLine.getWmsProduct()
//									.getWprId());
							List list2 = null;
							if (list2.size() == 0) {
								wmsLine.setWliNum(0.0);
							} else if (list2.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list2.get(0);
								wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							}
							wwoBIZ.updateWli(wmsLine);//更新流水
						}
					}
				}

				request.setAttribute("msg", "撤销调拨");
				return mapping.findForward("popDivSuc");
			} else {
				request.setAttribute("errorMsg", "该调拨单不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 
	 * 撤销调出 <br>
	 * create_date: Aug 26, 2010,11:33:38 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wchId:调拨单id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面，调拨实体不存在跳转至error页面
	 * 		attribute > msg:操作成功提示信息 errorMsg:操作错误提示信息
	 */
	public ActionForward cancelWchOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wch = request.getParameter("wchId");
		Long wchId = Long.parseLong(wch);
		java.util.Date currentDate = new java.util.Date();//当前日期
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		synchronized (this) {
			WmsChange wmsChange = wwoBIZ.findWch(wchId);
			if (wmsChange != null) {
				if (!wmsChange.getWchState().equals("4")) {

					String outWms = wmsChange.getWmsStroByWchOutWms()
							.getWmsCode();//调出仓库编号
					List list = wwoBIZ.getRww(wchId);
					Iterator it = list.iterator();
					if (list.size() > 0) {
						while (it.hasNext()) {
							RWmsWms rwmsWms = (RWmsWms) it.next();
							String wprCode = rwmsWms.getWmsProduct().getWprId();//商品编号
							Double num1 = rwmsWms.getRwwNum();//商品数量
							//获取仓库里的list-----执行调入
//							List list2 = wwoBIZ.findProNum(outWms, wprCode);
							List list2 = null;
							if (list2 == null || list2.size() == 0) {
								//							request.setAttribute("errorMsg", "该仓库已无'"+rwmsWms.getWmsProduct().getWprName()+"'商品,无法完成此操作");
								//							return mapping.findForward("error");
								RStroPro rstroPro1 = new RStroPro();
								rstroPro1
										.setWmsProduct(rwmsWms.getWmsProduct());
								rstroPro1.setRspProNum(num1);
								rstroPro1.setWmsStro(wmsChange
										.getWmsStroByWchInWms());
								wmsManageBiz.saveRsp(rstroPro1);

							} else {
								RStroPro rstroPro2 = (RStroPro) list2.get(0);
								Double num = rstroPro2.getRspProNum();// 商品数量
								Long rspId = rstroPro2.getRspId();//仓库ID
								num = num + num1;
								rstroPro2.setRspProNum(num);
								rstroPro2.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro2);
							}
						}
					}
					wmsChange.setWchState("4");
					wmsChange.setWchCanDate(currentDate);
					wmsChange.setWchCanMan(lim.getUserSeName());
					wwoBIZ.updateWch(wmsChange);

					List li2 = wwoBIZ.findByTypeCode("3", wch);//调出库

					if (li2.size() > 0) {
						for (int i = 0; i < li2.size(); i++) {
							WmsLine wmsLine = (WmsLine) li2.get(i);
							wmsLine.setWliState("2");
							//					wmsLine.setWliDate(currentDate);
							//					List list2=wwoBIZ.findProNum(wmsLine.getWmsStro().getWmsCode(),wmsLine.getWmsProduct().getWprId());
							//					if(list2.size()==0){
							//						wmsLine.setWliNum(0.0);
							//					}else if(list2.size()>0){
							//						RStroPro rstroPro1=(RStroPro)list2.get(0);
							//						wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							//					}
							wwoBIZ.updateWli(wmsLine);//更新流水
						}
					}
				}
				request.setAttribute("msg", "撤销调拨");
				return mapping.findForward("popDivSuc");
			} else {
				request.setAttribute("errorMsg", "该调拨单不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 
	 * 数据整理 <br>
	 * create_date: Aug 26, 2010,11:54:11 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转至popDivSuc页面
	 */
	public ActionForward wmsOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		wwoBIZ.delData(wmsCode);
		request.setAttribute("msg", "数据整理");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 库存盘点搜索 <br>
	 * create_date: Aug 26, 2010,11:59:43 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数 wmcTitle:盘点主题 wmsCode:仓库编号 wmcAppIsok:审核状态 isok:审核标识
	 * 					wmcState,wmcState1:盘点状态 startTime,endTime:查询日期段
	 * @param response
	 * @return ActionForward 跳转至wmsCheck页面
	 * 		attribute > appCount:需审核的盘点单数量 wmcWmsCount:需盘点的单据数量 wmcAppIsok:审核状态
	 * 					isok:审核标识 wmcTitle:盘点主题 wmsCode:仓库编号 wmcState,wmcState1:盘点状态
	 * 					startTime,endTime:查询日期段 wmsName:仓库名称 page:分页信息 wmsCheck:盘点列表
	 * 					msg:列表为空提示信息
	 */
	public ActionForward wmcSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wmcTitle = TransStr.transStr(request.getParameter("wmcTitle"));
		String wmsCode = request.getParameter("wmsCode");
		String wmcAppIsok = request.getParameter("wmcAppIsok");
		String isok = request.getParameter("isok");
		String wmcState = "";
		String wmcState0 = request.getParameter("wmcState");
		String wmcState1 = request.getParameter("wmcState1");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] wmcInpDate = OperateDate.checkDate(startTime, endTime);
		String date = "";
		if (wmcInpDate[1] != null && !wmcInpDate[1].equals("")) {
			try {
				date = dateFormat.format(OperateDate.getDate(dateFormat
						.parse(wmcInpDate[1]), 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (wmcState1 != null && !wmcState1.equals("")) {
			wmcState = wmcState1;
		} else {
			wmcState = wmcState0;
		}
		Page page = new Page(wwoBIZ.getWmcCount(wmcTitle, wmsCode, wmcAppIsok,
				wmcState, wmcInpDate[0], date), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.wmcSearch(wmcTitle, wmsCode, wmcAppIsok, wmcState,
				wmcInpDate[0], date, page.getCurrentPageNo(), page
						.getPageSize());
		if (list.size() <= 0) {
			request.setAttribute("msg", "对不起，没有您想要的库存盘点信息！");
		}
		int appCount = 0;
		int wmcWmsCount = 0;
		if (isok != null && !isok.equals("")) {
			appCount = wwoBIZ.findWmcApp(wmsCode, "2");
			wmcWmsCount = wwoBIZ.findWmcApp(wmsCode, "1");
		}
		request.setAttribute("appCount", appCount);//今天要审核的盘点单数量
		request.setAttribute("wmcWmsCount", wmcWmsCount);//今天要的盘点的单据数量
		request.setAttribute("wmcAppIsok", wmcAppIsok);
		request.setAttribute("isok", isok);
		request.setAttribute("wmcTitle", wmcTitle);
		request.setAttribute("wmsCode", wmsCode);
		request.setAttribute("wmcState", wmcState0);
		request.setAttribute("wmcState1", wmcState1);
		request.setAttribute("startTime", wmcInpDate[0]);
		request.setAttribute("endTime", wmcInpDate[1]);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}
		request.setAttribute("page", page);
		request.setAttribute("wmsCheck", list);
		return mapping.findForward("wmsCheck");
	}

	/**
	 * 进入新建盘点记录 <br>
	 * create_date: Aug 26, 2010,2:07:17 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转至newWmsCheck
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号
	 */
	public ActionForward toNewWmsCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		
		List list = wmsManageBiz.findAllWms();
		
		request.setAttribute("wmsStro", list);
		request.setAttribute("wmsCode", wmsCode);
		return mapping.findForward("newWmsCheck");
	}

	/**
	 * 新建盘点记录 <br>
	 * create_date: Aug 26, 2010,2:16:29 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wst:仓库编号
	 * @param response
	 * @return ActionForward 编号重复跳转至error页面,操作成功跳转至popDivSuc页面
	 * 		attribute > errorMsg:错误信息提示 msg:操作成功提示信息
	 */
	public ActionForward newWmsCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wst");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		DynaActionForm form1 = (DynaActionForm) form;
		WmsCheck wmsCheck = (WmsCheck) form1.get("wmsCheck");
		java.util.Date currentDate = new java.util.Date();//当前日期
		CodeCreator codeCreator = new CodeCreator();
		
		String wmcCode = codeCreator.createCode("PD"
				+ dateFormat3.format(currentDate), "wms_check", 0);
		List list = wwoBIZ.findByWmcCode(wmcCode);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该单号已被使用");
			return mapping.findForward("error");
		}
		else{
			if (wmsCode != "" && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsCheck.setWmsStro(wmsStro);
			} else {
				wmsCheck.setWmsStro(null);
			}
			wmsCheck.setWmcInpDate(currentDate);//录入日期
			wmsCheck.setWmcInpName(limUser.getUserSeName());//录入日期
			wmsCheck.setWmcCode(wmcCode);
			wmsCheck.setWmcIsdel("1");
			wwoBIZ.saveWmc(wmsCheck);
			
			request.setAttribute("redir", "wms_check");
			request.setAttribute("eId", wmsCheck.getWmcId());
			request.setAttribute("msg", "新建仓库盘点");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 盘点审核 <br>
	 * create_date Dec 21, 20093:59:18 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id wmcAppDesc:盘点内容 wmcAppIsok:审核状态
	 * @param response
	 * @return ActionForward 盘点实体存在重新定向页面,否则跳转至error页面
	 * 		attribute > errorMsg:错误信息提示
	 */
	public ActionForward appWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wmcId = Long.parseLong((request.getParameter("wmcId")));
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		java.util.Date currentDate = new java.util.Date();//当前日期
		if (wmsCheck != null) {
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			String wmcAppMan = limUser.getUserSeName();
			String wmcAppDesc = request.getParameter("wmcAppDesc");
			String wmcAppIsok = request.getParameter("wmcAppIsok");
			wmsCheck.setWmcAppDate(currentDate);
			wmsCheck.setWmcAppMan(wmcAppMan);
			wmsCheck.setWmcAppDesc(wmcAppDesc);
			if (wmcAppIsok != null && !wmcAppIsok.equals("")) {
				wmsCheck.setWmcAppIsok(wmcAppIsok);
			} else {
				wmsCheck.setWmcAppIsok("1");
			}
			wwoBIZ.updateWmc(wmsCheck);//更新盘点单
			String url = "wwoAction.do?op=wmcDesc&wmcId=" + wmcId;
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 进入新建盘点差异页面 <br>
	 * create_date: Aug 26, 2010,2:41:03 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点id
	 * @param response
	 * @return ActionForward 跳转至newRwmcPro页面 盘点实体不存在跳转至error页面
	 * 		attribute > wmsCheck:盘点实体 wmcId:盘点id errorMsg:错误信息提示
	 */
	public ActionForward toInputWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wmcId = Long.parseLong((request.getParameter("wmcId")));
		
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		if (wmsCheck != null) {
			request.setAttribute("wmsCheck", wmsCheck);
			request.setAttribute("wmcId", wmcId);
			return mapping.findForward("newRwmcPro");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 盘点详情 <br>
	 * create_date: Aug 26, 2010,2:41:03 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点id
	 * @param response
	 * @return ActionForward 跳转至rwmcPro页面,盘点实体不存在跳转至error页面
	 * 		attribute > wmsCheck:盘点实体 errorMsg:错误信息提示
	 */
	public ActionForward wmcDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wmcId = Long.parseLong((request.getParameter("wmcId")));
		
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		if (wmsCheck != null) {
			request.setAttribute("wmsCheck", wmsCheck);
			return mapping.findForward("rwmcPro");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 新建盘点明细 <br>
	 * create_date: Aug 26, 2010,3:02:57 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwmsChange:盘点明细实体 wprCode:商品id wmcId:盘点单据id
	 * @param response 
	 * @return ActionForward 单据不存在或者明细中已经存在该商品跳转至error页面,操作成功跳转至popDivSuc页面
	 * 		attribute > errorMsg:错误信息提示 msg:操作成功提示信息
	 */
	public ActionForward newRwmcPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		RWmsChange rwmsChange = (RWmsChange) form1.get("rwmsChange");
		String wprCode = request.getParameter("wprCode");
		String wmc = request.getParameter("wmcId");
		Long wmcId = Long.parseLong(wmc);
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);

		List list = wwoBIZ.findByWmc(Long.parseLong(wprCode), wmcId);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该盘点已存在此商品");
			return mapping.findForward("error");
		}
		if (wmsCheck != null) {
			rwmsChange.setWmsCheck(wmsCheck);

			if (wprCode != null && !wprCode.equals("")) {
				WmsProduct wmsProduct = new WmsProduct();
				wmsProduct.setWprId(wprCode);
				rwmsChange.setWmsProduct(wmsProduct);
			} else {
				rwmsChange.setWmsProduct(null);
			}
			String rmcType = rwmsChange.getRmcType();
			if (rmcType != null && !rmcType.equals("")) {
				rwmsChange.setRmcType(rmcType);
			} else {
				rwmsChange.setRmcType("0");
			}
			wwoBIZ.save(rwmsChange);

			//			WmsLine wmsLine=new WmsLine();
			//			wmsLine.setWliType("4");//流水类别--盘点
			//			wmsLine.setWliTypeCode(wmc);
			//			wmsLine.setWmsProduct(rwmsChange.getWmsProduct());
			//			wmsLine.setWmsStro(wmsCheck.getWmsStro());
			//			wmsLine.setWliDate(wmsCheck.getWmcInpDate());
			//			wmsLine.setWliState("0");//未执行
			//			Double num=rwmsChange.getRwcDifferent();
			//			if(num>0.0){
			//				wmsLine.setWliInNum(num);
			//			}
			//			if(num<0.0){
			//				wmsLine.setWliOutNum(-num);//调出量
			//			}
			//			wmsLine.setWliWmsId(rwmsChange.getRwcId());
			//			wmsLine.setWliIsdel("1");
			//			wwoBIZ.saveWli(wmsLine);

			wmsCheck.setWmcAppIsok("2");
			wwoBIZ.updateWmc(wmsCheck);
			request.setAttribute("msg", "新建盘点记录");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 进入编辑盘点明细页面 <br>
	 * create_date: Aug 26, 2010,3:31:01 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwcId:盘点明细id
	 * @param response
	 * @return ActionForward 盘点实体存在跳转至updateRwmc页面,否则跳转至error页面
	 * 		attribute > rwcId:盘点明细id wprCode:商品id rwmsChange:盘点明细实体 wmsCheck:单据实体
	 * 					errorMsg:错误信息提示
	 */
	public ActionForward toUpdateRwmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwcId = Long.parseLong((request.getParameter("rwcId")));
		
		RWmsChange rwmsChange = wwoBIZ.findByIdRmc(rwcId);
		String wprCode = rwmsChange.getWmsProduct().getWprId();
		WmsCheck wmsCheck = wwoBIZ.findWmc(rwmsChange.getWmsCheck().getWmcId());
		if (wmsCheck != null) {
			String wmsCode = wmsCheck.getWmsStro().getWmsCode();
			request.setAttribute("rwcId", rwcId);
			request.setAttribute("wprCode", wprCode);
			request.setAttribute("rwmsChange", rwmsChange);
			request.setAttribute("wmsCheck", wmsCheck);
			return mapping.findForward("updateRwmc");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 编辑盘点明细 <br>
	 * create_date: Aug 26, 2010,3:42:19 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwcId:盘点明细id rwcDifferent:差异数量 rmcWmsCount:库存数量 rmcRealNum:实际库存数量
	 * 					rmcType:模式
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面
	 * 		attribute > msg:操作成功信息提示
	 */
	public ActionForward updateRwmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwcId = Long.parseLong((request.getParameter("rwcId")));
		RWmsChange rwmsChange = wwoBIZ.findByIdRmc(rwcId);
		Double rwcDifferent = Double.parseDouble(request
				.getParameter("rwcDifferent"));
		Double rmcWmsCount = Double.parseDouble(request
				.getParameter("rmcWmsCount"));
		Double rmcRealNum = Double.parseDouble(request
				.getParameter("rmcRealNum"));
		rwmsChange.setRwcDifferent(rwcDifferent);
		rwmsChange.setRmcWmsCount(rmcWmsCount);
		rwmsChange.setRmcRealNum(rmcRealNum);
		String rmcType = request.getParameter("rmcType");
		if (rmcType != null && !rmcType.equals("")) {
			rwmsChange.setRmcType(rmcType);
		} else {
			rwmsChange.setRmcType("0");
		}
		String rmcRemark = request.getParameter("rmcRemark");
		rwmsChange.setRmcRemark(rmcRemark);
		wwoBIZ.updateRwmc(rwmsChange);
		//更新库存流水
		//		List list=wwoBIZ.findByWmsId("4", rwcId);
		//		WmsLine wmsLine=(WmsLine)list.get(0);
		//		if(rwcDifferent>0){
		//			wmsLine.setWliInNum(rwcDifferent);
		//			wmsLine.setWliOutNum(null);
		//		}
		//		if(rwcDifferent<0){
		//			wmsLine.setWliInNum(null);
		//			wmsLine.setWliOutNum(-rwcDifferent);
		//		}
		//		wwoBIZ.updateWli(wmsLine);
		request.setAttribute("msg", "编辑盘点记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除盘点明细 <br>
	 * create_date Nov 19, 20092:20:57 PM<br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > rwcId:盘点明细id
	 * @param response
	 * @return ActionForward 操作成功跳转至popDivSuc页面
	 * 		attribute > msg:操作成功信息提示
	 */
	public ActionForward delRwmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwcId = Long.parseLong((request.getParameter("rwcId")));
		RWmsChange rwmsChange = wwoBIZ.findByIdRmc(rwcId);
		WmsCheck wmsCheck = rwmsChange.getWmsCheck();
		wwoBIZ.deleteRwmc(rwmsChange);

//		String wmsId = request.getParameter("wmsId");
		//更新盘点单审核信息
//		WmsCheck wmsCheck = wwoBIZ.findWmc(Long.parseLong(wmsId));
		if (wmsCheck.getRWmsChanges().size() == 0) {
			wmsCheck.setWmcAppIsok(null);
			wwoBIZ.updateWmc(wmsCheck);//更新盘点
		}
		request.setAttribute("msg", "删除盘点记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 盘点商品 <br>
	 * create_date: Aug 26, 2010,4:11:34 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id
	 * @param response
	 * @return ActionForward 仓库没有该商品或者库存不足,单据不存跳转至error页面,操作成功跳转至empty页面
	 * 		attribute > errorMsg:错误信息提示 msg:操作成功信息提示
	 */
	public ActionForward wmsCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmc = request.getParameter("wmcId");
		Long wmcId = Long.parseLong(wmc);
		java.util.Date currentDate = new java.util.Date();//当前日期
		LimUser limUser = (LimUser) request.getSession()
				.getAttribute("limUser");
		synchronized (this) {
			WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
			if (wmsCheck != null) {
				if (!wmsCheck.getWmcState().equals("1")) {
					String wmsCode = wmsCheck.getWmsStro().getWmsCode();
					List list = wwoBIZ.findByWmc(wmcId);
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							RWmsChange rwmsChange = (RWmsChange) list.get(i);
							Double num1 = rwmsChange.getRwcDifferent();
							String wprCode = rwmsChange.getWmsProduct()
									.getWprId();
//							List list2 = wwoBIZ.findProNum(wmsCode, wprCode);
							List list2= null;
							if (list2.size() == 0) {
								request.setAttribute("errorMsg", "该仓库已无'"
										+ rwmsChange.getWmsProduct()
												.getWprName() + "'商品,无法完成此操作");
								return mapping.findForward("error");
							} else {
								RStroPro rstroPro2 = (RStroPro) list2.get(0);
								Double num = rstroPro2.getRspProNum();// 商品数量
								Long rspId = rstroPro2.getRspId();//仓库ID
								if (num1 < 0 && num < -num1) {
									request.setAttribute("errorMsg", "该仓库'"
											+ rwmsChange.getWmsProduct()
													.getWprName()
											+ "'商品数量不足,无法完成此次操作");
									return mapping.findForward("error");
								}
								num = num + num1;
								rstroPro2.setRspProNum(num);
								rstroPro2.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro2);
							}
							//生成流水
							WmsLine wmsLine = new WmsLine();
							wmsLine.setWliType("4");//流水类别--盘点
							wmsLine.setWliTypeCode(wmc);
							wmsLine.setWmsProduct(rwmsChange.getWmsProduct());
							wmsLine.setWmsStro(wmsCheck.getWmsStro());
							wmsLine.setWliDate(wmsCheck.getWmcInpDate());
							wmsLine.setWliState("1");//已执行
							Double num = rwmsChange.getRwcDifferent();
							if (num > 0.0) {
								wmsLine.setWliInNum(num);
							}
							if (num < 0.0) {
								wmsLine.setWliOutNum(-num);//调出量
							}
							wmsLine.setWliWmsId(rwmsChange.getRwcId());
							wmsLine.setWliIsdel("1");
							wmsLine.setWliDate(currentDate);//日期
							wmsLine.setWliMan(limUser.getUserSeName());
//							List list3 = wwoBIZ.findProNum(wmsCode, wprCode);
							List list3 = null;
							if (list3.size() == 0) {
								wmsLine.setWliNum(0.0);
							} else if (list3.size() > 0) {
								RStroPro rstroPro1 = (RStroPro) list3.get(0);
								wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
							}
							wwoBIZ.saveWli(wmsLine);

						}
					}

					wmsCheck.setWmcOpman(limUser.getUserSeName());//执行人
					wmsCheck.setWmcDate(currentDate);//执行日期
					wmsCheck.setWmcState("1");
					wwoBIZ.updateWmc(wmsCheck);

					//			List li=wwoBIZ.findByTypeCode("4", wmc);
					//			if(li.size()>0){
					//				for(int i=0;i<li.size();i++){
					//					WmsLine wmsLine=(WmsLine)li.get(i);
					//					wmsLine.setWliState("1");
					//					wmsLine.setWliDate(currentDate);//日期
					//					wmsLine.setWliMan(limUser.getUserSeName());
					//					wwoBIZ.updateWli(wmsLine);//更新流水
					//				}
					//			   }
				}

				request.setAttribute("msg", "盘点完成");
				return mapping.findForward("empty");
			} else {
				request.setAttribute("errorMsg", "该盘点单不存在");
				return mapping.findForward("error");
			}
		}
	}

	/**
	 * 进入编辑盘点单据页面 <br>
	 * create_date: Aug 26, 2010,4:29:23 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id
	 * @param response
	 * @return ActionForward 单据实体存在跳转至updateWmc页面,否则跳转至error页面
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号 wmsCheck:盘点实体 errorMsg:错误信息提示
	 */
	public ActionForward toUpdateWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wmcId = Long.parseLong((request.getParameter("wmcId")));
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		if (wmsCheck != null) {
			List list = wmsManageBiz.findAllWms();
			String wmsCode = request.getParameter("wmsCode");
			request.setAttribute("wmsStro", list);
			request.setAttribute("wmsCode", wmsCode);
			request.setAttribute("wmsCheck", wmsCheck);
			return mapping.findForward("updateWmc");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 编辑盘点单据 <br>
	 * create_date: Aug 26, 2010,4:34:18 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id wmcTitle:盘点主题 wmsCode:盘点仓库 wmcRemark:盘点备注
	 * @param response
	 * @return ActionForward 盘点单据实体存在跳转至popDivSuc页面，否则跳转至error页面
	 * 		attribute > msg:操作成功提示信息 errorMsg:错误信息提示
	 */
	public ActionForward updateWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long wmcId = Long.parseLong((request.getParameter("wmcId")));
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		if (wmsCheck != null) {
			wmsCheck.setWmcTitle(request.getParameter("wmcTitle"));
			String wmsCode = request.getParameter("wst");
			if (wmsCode != "" && !wmsCode.equals("")) {
				wmsCheck.setWmsStro(new WmsStro(wmsCode));
			} else {
				wmsCheck.setWmsStro(null);
			}
			/*String wmcInDate=request.getParameter("wmcInDate");
			if(wmcInDate!=""&&!wmcInDate.equals("")){
				Date wmcDate = Date.valueOf(wmcInDate);
				wmsCheck.setWmcDate(wmcDate);
			}else{
				wmsCheck.setWmcDate(null);
			}*/
			//			String userCode1=request.getParameter("userCode");
			LimUser limUser = (LimUser) request.getSession().getAttribute(
					"limUser");
			/*if(userCode1!=""&&!userCode1.equals("")){
				//LimUser limUser1=new LimUser();
				//limUser.setUserCode(userCode1);	
				wmsCheck.setLimUser(new LimUser(userCode1));	
			}else{
				
				wmsCheck.setLimUser(limUser);
			}*/
			wmsCheck.setWmcRemark(request.getParameter("wmcRemark"));
			java.util.Date currentDate = new java.util.Date();//当前日期
			wmsCheck.setWmcAltDate(currentDate);//修改日期
			wmsCheck.setWmcAltName(limUser.getUserSeName());//修改日期

			wwoBIZ.updateWmc(wmsCheck);
			request.setAttribute("msg", "编辑盘点记录");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 
	 * 删除盘点单据 <br>
	 * create_date: Aug 26, 2010,4:39:53 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id
	 * @param response
	 * @return ActionForward 盘点单据实体存在跳转至popDivSuc页面，否则跳转至error页面
	 * 		attribute > msg:操作成功提示信息 errorMsg:错误信息提示
	 */
	public ActionForward delWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmc = request.getParameter("wmcId");
		Long wmcId = Long.parseLong(wmc);
		WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
		if (wmsCheck != null) {
			//			wmsCheck.setWmcIsdel("0");
			//			wwoBIZ.updateWmc(wmsCheck);
			wwoBIZ.delWmsCheck(wmsCheck);
			//			List li=wwoBIZ.findByTypeCode("4", wmc);
			//			if(li.size()>0){
			//				for(int i=0;i<li.size();i++){
			//					WmsLine wmsLine=(WmsLine)li.get(i);
			//					wmsLine.setWliIsdel("0");
			//					wwoBIZ.updateWli(wmsLine);//更新流水
			//				}
			//			}
			request.setAttribute("msg", "删除盘点记录");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该盘点单不存在");
			return mapping.findForward("error");
		}

	}

	/**
	 * 
	 * 恢复盘点记录 <br>
	 * create_date: Aug 26, 2010,4:40:56 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:盘点单据id
	 * @param response
	 * @return ActionForward 跳转至popDivSuc页面
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward recWmsCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsCheck wmsCheck = wwoBIZ.findById(id);
		wmsCheck.setWmcIsdel("1");
		wwoBIZ.updateWmc(wmsCheck);
		request.setAttribute("msg", "恢复盘点记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 删除盘点记录不能再恢复 <br>
	 * create_date: Aug 26, 2010,4:43:16 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > id:盘点单据id
	 * @param response
	 * @return ActionForward 跳转至popDivSuc页面
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward delWmsCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsCheck wmsCheck = wwoBIZ.findById(id);
		wwoBIZ.delWmsCheck(wmsCheck);
		request.setAttribute("msg", "删除盘点记录");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 
	 * 进入撤销盘点页面 <br>
	 * create_date: Aug 26, 2010,4:44:10 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id
	 * @param response
	 * @return ActionForward 跳转至cancelWwo页面
	 * 		attribute > wmcId:盘点单据id
	 */
	public ActionForward goCancelWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmcId = request.getParameter("wmcId");
		request.setAttribute("wmcId", wmcId);
		return mapping.findForward("cancelWwo");
	}

	/**
	 * 
	 * 撤销盘点 <br>
	 * create_date: Aug 26, 2010,4:45:14 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wmcId:盘点单据id
	 * @param response
	 * @return ActionForward 库存没有该商品或者库存不足跳转至error页面，操作成功跳转至popDivSuc页面
	 * 		attribute > msg:操作成功提示信息
	 */
	public ActionForward cancelWmc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmc = request.getParameter("wmcId");
		Long wmcId = Long.parseLong(wmc);
		synchronized (this) {
			WmsCheck wmsCheck = wwoBIZ.findWmc(wmcId);
			//			if(wmsCheck!=null){
			if (!wmsCheck.getWmcState().equals("2")) {

				String wmsCode = wmsCheck.getWmsStro().getWmsCode();
				List list = wwoBIZ.findByWmc(wmcId);
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						RWmsChange rwmsChange = (RWmsChange) list.get(i);
						Double num1 = rwmsChange.getRwcDifferent();
						String wprCode = rwmsChange.getWmsProduct().getWprId();
//						List list2 = wwoBIZ.findProNum(wmsCode, wprCode);
						List list2= null;
						if (list2 == null || list2.size() == 0) {
							request.setAttribute("errorMsg", "该仓库已无'"
									+ rwmsChange.getWmsProduct().getWprName()
									+ "'商品,无法完成此操作");
							return mapping.findForward("error");
						} else {
							RStroPro rstroPro2 = (RStroPro) list2.get(0);
							Double num = rstroPro2.getRspProNum();// 商品数量
							Long rspId = rstroPro2.getRspId();//仓库ID
							if (num1 > 0 && num < num1) {
								request.setAttribute("errorMsg", "该仓库'"
										+ rwmsChange.getWmsProduct()
												.getWprName()
										+ "'商品数量不足,无法完成此次操作");
								return mapping.findForward("error");
							}
							num = num - num1;
							rstroPro2.setRspProNum(num);
							rstroPro2.setRspId(rspId);
							wmsManageBiz.updatePsp(rstroPro2);
						}
					}
				}
				LimUser limUser = (LimUser) request.getSession().getAttribute(
						"limUser");
				wmsCheck.setWmcCanMan(limUser.getUserSeName());//执行人
				java.util.Date currentDate = new java.util.Date();//当前日期
				wmsCheck.setWmcCanDate(currentDate);//执行日期
				wmsCheck.setWmcState("2");
				wwoBIZ.updateWmc(wmsCheck);

				List li = wwoBIZ.findByTypeCode("4", wmc);
				if (li.size() > 0) {
					for (int i = 0; i < li.size(); i++) {
						WmsLine wmsLine = (WmsLine) li.get(i);
						wmsLine.setWliState("2");
						//					wmsLine.setWliDate(currentDate);//日期
						wmsLine.setWliMan(limUser.getUserSeName());
						//					List list2=wwoBIZ.findProNum(wmsLine.getWmsStro().getWmsCode(),wmsLine.getWmsProduct().getWprId());
						//					if(list2.size()==0){
						//						wmsLine.setWliNum(0.0);
						//					}else if(list2.size()>0){
						//						RStroPro rstroPro1=(RStroPro)list2.get(0);
						//						wmsLine.setWliNum(rstroPro1.getRspProNum());//库存余量
						//					}
						wwoBIZ.updateWli(wmsLine);//更新流水
					}
				}
			}
		}
		request.setAttribute("msg", "撤销盘点");
		return mapping.findForward("popDivSuc");
		//		}else{
		//			request.setAttribute("errorMsg", "该盘点单不存在");
		//			return mapping.findForward("error");
		//		}
	}

	/**
	 * 
	 * 库存流水查询 <br>
	 * create_date: Aug 26, 2010,4:47:06 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数 wliType:流水类型 wmsCode:仓库编号 wprName:商品名称
	 * 					startTime,endTime:查询时间段
	 * @param response
	 * @return ActionForward 符合查询条件的列表跳转至wliLine页面
	 * 		attribute > msg:列表为空的提示信息 startTime,endTime:查询时间段 page:分页信息
	 * 					wliType:流水类型 wmsCode:仓库编号 wmsName:仓库名称 wmsLine:流水列表
	 */
	public ActionForward wliSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wliType = request.getParameter("wliType");
		String wmsCode = request.getParameter("wmsCode");
		//		String wprId=request.getParameter("wprId");
		String wprName = TransStr.transStr(request.getParameter("wprName"));
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] wliDate = OperateDate.checkDate(startTime, endTime);
		String date = "";
		if (wliDate[1] != null && !wliDate[1].equals("")) {
			try {
				date = dateFormat.format(OperateDate.getDate(dateFormat
						.parse(wliDate[1]), 1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (wprName != null && !wprName.equals("")) {
			request.setAttribute("wprName", wprName);
		}
		Page page = new Page(wwoBIZ.getWliCount(wliType, wmsCode, wprName,
				wliDate[0], date), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.WliSearch(wliType, wmsCode, wprName, wliDate[0],
				date, page.getCurrentPageNo(), page.getPageSize());
		if (list.size() <= 0) {
			request.setAttribute("msg", "对不起，没有您想要的库存流水信息！");
		}
		request.setAttribute("startTime", wliDate[0]);
		request.setAttribute("endTime", wliDate[1]);
		//		request.setAttribute("wprId", wprId);
		request.setAttribute("page", page);
		request.setAttribute("wliType", wliType);
		request.setAttribute("wmsCode", wmsCode);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}
		request.setAttribute("wmsLine", list);
		return mapping.findForward("wliLine");
	}

	/**
	 * 
	 * 商品的出入库历史 <br>
	 * create_date: Aug 26, 2010,5:01:55 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数 wprId:商品id
	 * @param response
	 * @return ActionForward 跳转至wprHistory页面
	 * 		attribute > wmsProduct:商品实体 page:分页信息 wmsLine:流水列表
	 */
	public ActionForward wprHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wprId = request.getParameter("wprId");
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		//int num=wmsManageBiz.getCountPro(wprCode);//库存总量
		WmsProduct wmsProduct = wmsManageBiz.wmsProDesc(Long.parseLong(wprId));
		Page page = new Page(
				wwoBIZ.getWliCountByWpr(Long.parseLong(wprId), ""), 40);
		page.setCurrentPageNo(Integer.parseInt(p));

		List list = wwoBIZ.findByWpr(Long.parseLong(wprId), "", page
				.getCurrentPageNo(), page.getPageSize());
		//List list2=wwoBIZ.findByWpr(Long.parseLong(wprId),"isAll",page.getCurrentPageNo(),page.getPageSize());
		request.setAttribute("wmsProduct", wmsProduct);
		request.setAttribute("page", page);
		request.setAttribute("wmsLine", list);
		//request.setAttribute("wmsLine2", list2);
		return mapping.findForward("wprHistory");
	}

	/**
	 * 
	 * 获得待删除的出库单 <br>
	 * create_date: Aug 26, 2010,5:03:51 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数
	 * @param response
	 * @return ActionForward 跳转至recWarOut页面
	 * 		attribute > wmsWarOut:出库单列表 page:分页信息 
	 */
	public ActionForward findDelWarOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wwoBIZ.findDelWarOutCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findDelWarOut(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("wmsWarOut", list);
		request.setAttribute("page", page);
		return mapping.findForward("recWarOut");
	}

	/**
	 * 
	 * 获得待删除的仓库调拨 <br>
	 * create_date: Aug 26, 2010,5:05:12 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数
	 * @param response
	 * @return ActionForward 跳转至recWmsChange页面
	 * 		attribute > wmsChange:调拨单列表 page:分页信息 
	 */
	public ActionForward findDelWmsChange(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wwoBIZ.findDelWmsChange(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findDelWmsChange(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("wmsChange", list);
		request.setAttribute("page", page);
		return mapping.findForward("recWmsChange");

	}

	/**
	 * 
	 * 获得待删除的盘点记录 <br>
	 * create_date: Aug 26, 2010,5:10:25 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数
	 * @param response
	 * @return ActionForward 跳转至recWmsCheck页面
	 * 		attribute > wmsCheck:盘点单列表 page:分页信息
	 */
	public ActionForward findDelWmsCheck(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wwoBIZ.findDelWmsCheckCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findDelWmsCheck(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("wmsCheck", list);
		request.setAttribute("page", page);
		return mapping.findForward("recWmsCheck");

	}

	/**
	 * 
	 * 获得待删除的库存流水 <br>
	 * create_date: Aug 26, 2010,5:11:31 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > p:当前页数
	 * @param response
	 * @return ActionForward 跳转至recWmsLine页面
	 * 		attribute > wmsLine:库存流水列表 page:分页信息
	 */
	public ActionForward findDelWmsLine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wwoBIZ.findDelWmsLineCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findDelWmsLine(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("wmsLine", list);
		request.setAttribute("page", page);
		return mapping.findForward("recWmsLine");

	}

	/**
	 * 
	 * 商品库存情况统计 <br>
	 * create_date: Aug 26, 2010,5:12:28 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 		parameter > wprId:商品id type:图标类型
	 * @param response
	 * @return ActionForward 由输入流强制输出，不进行任何跳转
	 */
	public ActionForward getWprHisXML(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//定义BOM标记 

		String wprId = request.getParameter("wprId");
		String xmlType = request.getParameter("type");
		List list = wwoBIZ.findByWpr(Long.parseLong(wprId), "");
		String data = "";
		String xmlName = "";
		if (xmlType != null && xmlType.equals("1")) {
			xmlName = "库存变化区域图";
		} else if (xmlType.equals("2")) {
			xmlName = "库存变化折线图";
		}
		Double number = null;
		if (list.size() > 0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				WmsLine wli = (WmsLine) list.get(i);
				Double num = null;
				if (wli.getWliInNum() != null) {
					num = wli.getWliInNum();
				}
				if (wli.getWliOutNum() != null) {
					num = -wli.getWliOutNum();
				}
				if (i == list.size() - 1) {
					number = num;
				} else {
					number = number + num;
				}

				data += "<set name='" + dateFormat.format(wli.getWliDate())
						+ "' value='" + number + "' color='B1D1DC'/>";
			}
		}
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ xmlName
				+ "' baseFont='宋体' baseFontSize='12' xAxisName='日期' yAxisName='数值'"
				+ " decimalPrecision='0' chartRightMargin='30' baseFontColor='1c254e' areaBorderColor='7B9D9D' areaAlpha='60' areaBorderThickness='1' rotateNames='1' showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
				+ data + "</graph>";

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

	public void setWwoBIZ(WwoBIZ wwoBIZ) {
		this.wwoBIZ = wwoBIZ;
	}

	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}

	public void setWmsManageBiz(WmsManageBIZ wmsManageBiz) {
		this.wmsManageBiz = wmsManageBiz;
	}

	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
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
	/*public ActionForward recWmsLine(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
	Long id = Long.valueOf(request.getParameter("id"));
	WmsLine wmsLine=wwoBIZ.findById(id);
	wwoBIZ.delWmsCheck(wmsCheck);
	request.setAttribute("msg", "删除盘点记录");
	return mapping.findForward("popDivSuc");
	}
	public ActionForward delWmsLine(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
	Long id = Long.valueOf(request.getParameter("id"));
	WmsCheck wmsCheck=wwoBIZ.findById(id);
	wwoBIZ.delWmsCheck(wmsCheck);
	request.setAttribute("msg", "删除盘点记录");
	return mapping.findForward("popDivSuc");
	}*/
}