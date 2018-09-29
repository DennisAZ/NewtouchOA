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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
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
import com.psit.struts.entity.Attachment;
import com.psit.struts.entity.LimFunction;
import com.psit.struts.entity.LimOperate;
import com.psit.struts.entity.LimRight;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RStroPro;
import com.psit.struts.entity.RUserRig;
import com.psit.struts.entity.RWinPro;
import com.psit.struts.entity.SalOrdCon;
import com.psit.struts.entity.TypeList;
import com.psit.struts.entity.WmsChange;
import com.psit.struts.entity.WmsCheck;
import com.psit.struts.entity.WmsLine;
import com.psit.struts.entity.WmsProType;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.WmsStro;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.entity.WmsWarOut;
import com.psit.struts.util.CodeCreator;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.Page;
import com.psit.struts.util.format.TransStr;
import com.psit.struts.util.DAO.ModifyTypeDAO;

/**
 * 商品管理入库操作 <br>
 * create_date: Aug 25, 2010,2:05:19 PM<br>
 */
@SuppressWarnings("unused")
public class WmsManageAction extends BaseDispatchAction {
	WmsManageBIZ wmsManageBiz = null;
	EmpBIZ empBiz = null;
	WwoBIZ wwoBIZ = null;
	UserBIZ userBiz = null;
	TypeListBIZ typeListBiz = null;
	OrderBIZ orderBiz = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");
	DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * 判断用户是否有商品管理，入库操作的权限 <br>
	 * create_date: Aug 9, 2010,1:59:35 PM <br>
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
	 * 检测用户是否有商品管理，入库操作的权限码 <br>
	 * create_date: Aug 9, 2010,2:01:27 PM
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { 
				{ "toNewWwi", "w003" },{ "saveWarIn", "w003" },//入库(新建)
				{ "toUpdWwi", "w003" },{ "updateWwi", "w003" },//编辑入库单
				{ "delWmi", "w003" },//删除入库单
				{ "saveRwarPro","w003" },//确认入库
				{ "appWwi","w004" },//审核入库
				{ "delWwiDesc","w005" },//撤销入库
				{ "toWwiPro","w003" },{ "wwiPro", "w003" },//编辑入库明细
				{ "toUpdWip", "w003" },{ "updateWinPro", "w003" },//编辑入库产品
				{ "delRwp", "w003" },//删除入库明细
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}

	/**
	 * 查看仓库列表 <br>		
	 * create_date: Aug 25, 2010,2:08:19 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到wmsStroList页面<br>
	 * 		attribute > wmsStro:仓库列表
	 */
	public ActionForward wmsStroList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//String mark=request.getParameter("mark");
		List list = wmsManageBiz.findAllWms();
		request.setAttribute("wmsStro", list);
		//request.setAttribute("mark",mark);
		return mapping.findForward("wmsStroList");
	}

	/**
	 * 跳转到新建仓库页面 <br>
	 * create_date: Aug 25, 2010,2:11:19 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到newWms页面<br>
	 * 		attribute > wmsStroType:仓库类别列表 limUser:用户列表
	 */
	public ActionForward toNewWms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = typeListBiz.getEnabledType("31");
		List list1 = userBiz.listValidUser();
		
		request.setAttribute("wmsStroType", list);
		request.setAttribute("limUser", list1);
		return mapping.findForward("newWms");
	}

	/**
	 * 新建仓库 <br>
	 * create_date: Aug 25, 2010,2:13:37 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsType:仓库类别ID schRuserCode:仓库负责账号
	 * @param response
	 * @return ActionForward 仓库名已存在跳转到error 成功保存跳转到popDivSuc页面<br>
	 * 		attribute > 仓库名已存在 errorMsg:仓库名重复信息提示<br>
	 *      attribute > 成功保存 msg:成功保存信息提示<br>
	 */
	public ActionForward newWmsStro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		CodeCreator codeCreator = new CodeCreator();
		java.util.Date currentDate = new java.util.Date();//当前日期
		WmsStro wmsStro = (WmsStro) form1.get("wmsStro");
		List list = wmsManageBiz.checkWmsName(wmsStro.getWmsName());
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "该仓库名已存在，请核实后再添加");
			return mapping.findForward("error");
		}
		String userCode = (String) request.getSession()
				.getAttribute("userCode");
		String prefix = "ST" + dateFormat3.format(currentDate);
		String wmsCode = codeCreator.createCode(prefix, "wms_stro", 0);
		wmsStro.setWmsCode(wmsCode);
		String wstId = request.getParameter("wmsType");
		if (wstId != null && !wstId.equals("")) {
			Long wstId1 = Long.parseLong(wstId);
			TypeList wmsStroType = new TypeList(wstId1);
			wmsStro.setWmsStroType(wmsStroType);
		} else {
			wmsStro.setWmsStroType(null);
		}

		String userCode1 = request.getParameter("schRuserCode");
		LimUser limUser1 = (LimUser) request.getSession().getAttribute(
				"limUser");
		if (userCode1 != null && userCode1.equals("")) {
			LimUser limUser = new LimUser();
			limUser.setUserCode(userCode1);
			wmsStro.setLimUser(limUser);
		} else {
			wmsStro.setLimUser(limUser1);
		}
		wmsStro.setWmsCreDate(currentDate);
		wmsStro.setWmsIsenabled("1");
		wmsManageBiz.saveWms(wmsStro);
		request.setAttribute("msg", "新建仓库");
		//生成仓库的权限码
		LimRight limRight = new LimRight();
		limRight.setRigCode(wmsCode);
		limRight.setLimFunction(new LimFunction("wms006"));
		limRight.setLimOperate(new LimOperate("fun005"));
		limRight.setRigWmsName(wmsStro.getWmsName());
		wmsManageBiz.savLimRight(limRight);
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 仓库查询 <br>
	 * create_date: Aug 25, 2010,2:19:12 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到wmsManage页面<br>
	 * 		attribute > wmsStro:仓库列表 page:分页信息
	 */
	public ActionForward wmsStroSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		//仓库名设为null，查询所有仓库
		Page page = new Page(wmsManageBiz.getWmsCount(null), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List<WmsStro> list = wmsManageBiz.WmsStroSearch(null,page.getCurrentPageNo(), page.getPageSize());
		request.setAttribute("page", page);
		request.setAttribute("wmsStro", list);
		return mapping.findForward("wmsManage");
	}

	/**
	 * 查看仓库信息<br>
	 * create_date: Aug 25, 2010,2:26:46 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转到wmsStroDesc页面<br>
	 * 		attribute > wmsStro:仓库实体 count:仓库的库存数量
	 */
	public ActionForward wmsStroDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		//		String wmsName=request.getParameter("wmsName");
		//		if(wmsName!=null){
		//			request.setAttribute("wmsName", wmsName);
		//		}
		int count = wmsManageBiz.getCountStro(wmsCode);//x仓库的库存数量
		request.setAttribute("count", count);
		WmsStro wmsStro = wmsManageBiz.findWmsStro(wmsCode);
		wmsStro.setWmsCreDate(Date.valueOf(wmsStro.getWmsCreDate().toString()
				.substring(0, 10)));
		request.setAttribute("wmsStro", wmsStro);
		return mapping.findForward("wmsStroDesc");
	}

	/**
	 * 进入仓库修改页面 <br>
	 * create_date: Aug 25, 2010,2:34:21 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转到wmsStroUpdate页面<br>
	 * 		attribute > wmsStro:仓库实体 wmsStroType:仓库类别列表 limUser:用户列表
	 */
	public ActionForward toUpdWms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		
		WmsStro wmsStro = wmsManageBiz.findWmsStro(wmsCode);
		wmsStro.setWmsCreDate(Date.valueOf(wmsStro.getWmsCreDate().toString()
				.substring(0, 10)));
		List list = typeListBiz.getEnabledType("31");
		List list1 = userBiz.listValidUser();
		
		request.setAttribute("wmsStro", wmsStro);
		request.setAttribute("wmsStroType", list);
		request.setAttribute("limUser", list1);
		return mapping.findForward("wmsStroUpdate");
	}

	/**
	 * 修改仓库资料 <br>
	 * create_date: Aug 25, 2010,2:37:09 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > oldName:原仓库名称 wmsCode:仓库编号 wmsCreDate:仓库创建日期 wmsType:仓库类别ID schRuserCode:仓库负责账号
	 * @param response
	 * @return ActionForward 仓库名已存在跳转到error 成功修改跳转到popDivSuc页面<br>
	 * 		attribute > 仓库名已存在 errorMsg:仓库名重复信息提示<br>
	 *      attribute > 成功保存 msg:成功修改信息提示<br>
	 */
	public ActionForward wmsStroUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		WmsStro wmsStro = (WmsStro) form1.get("wmsStro");
		String oldName = request.getParameter("oldName");
		String wmsCode = request.getParameter("wmsCode");
		String wmsCreDate = request.getParameter("wmsCreDate");
		String wmsType = request.getParameter("wmsType");
		String userCode1 = request.getParameter("schRuserCode");
		LimUser limUser1 = (LimUser) request.getSession().getAttribute("limUser");
		if (!oldName.equals(wmsStro.getWmsName())){
			List list = wmsManageBiz.checkWmsName(wmsStro.getWmsName());
			if(list.size() > 0) {//判断仓库名重复
				request.setAttribute("errorMsg", "该仓库名已存在，请核实后再修改");
				return mapping.findForward("error");
			}
			else{
				// 更改权限表中的仓库名称
				LimRight limRight = wmsManageBiz.findLimRight(wmsCode);
				limRight.setRigWmsName(wmsStro.getWmsName());
				wmsManageBiz.updLimRight(limRight);
			}
		}
		wmsStro.setWmsCode(wmsCode);
		if (wmsCreDate != null && wmsCreDate != "") {
			Date wmsCreDate1 = Date.valueOf(wmsCreDate);
			wmsStro.setWmsCreDate(wmsCreDate1);
		}
		if (wmsType != null && !wmsType.equals("")) {
			TypeList wmsStroType = new TypeList(Long.parseLong(wmsType));
			wmsStro.setWmsStroType(wmsStroType);
		} else {
			wmsStro.setWmsStroType(null);
		}
		if (userCode1 != null && !userCode1.equals("")) {
			LimUser limUser = new LimUser();
			limUser.setUserCode(userCode1);
			wmsStro.setLimUser(limUser);
		} else {
			wmsStro.setLimUser(limUser1);
		}
		wmsManageBiz.updateWmsStro(wmsStro);
		
		request.setAttribute("msg", "编辑仓库");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除仓库 <br>
	 * create_date: Aug 25, 2010,2:49:54 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 仓库中存在商品跳转到error 成功修改跳转到popDivSuc页面<br>
	 * 		attribute > 仓库中存在商品 errorMsg:先清空仓库中的商品信息提示<br>
	 *      attribute > 成功删除 msg:成功删除信息提示<br>
	 */
	public ActionForward delWmsStro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		//		String wmsName=TransStr.transStr(request.getParameter("wmsName"));
		//		if(wmsName!=null){
		//			request.setAttribute("wmsName", wmsName);
		//		}	
		WmsStro wmsStro = wmsManageBiz.findWmsStro(wmsCode);
		wmsStro.setWmsCode(wmsCode);
		List list = wmsManageBiz.findByWmsCode(wmsCode);
		if (list.size() > 0) {
			request.setAttribute("errorMsg", "请先清空仓库内的商品再删除此仓库！");
			return mapping.findForward("error");
		}
		//清空已使用的仓库的相关信息（入库单，出库单，调拨单，盘点，流水）
		//入库单
		List list1 = wmsManageBiz.WwiSearch(wmsCode);
		if (list1.size() > 0) {
			for (int i = 0; i < list1.size(); i++) {
				WmsWarIn wmsWarIn = (WmsWarIn) list1.get(i);
				wmsWarIn.setWmsStro(null);
				wmsManageBiz.updateWwi(wmsWarIn);
			}
		}
		//出库单
		List list2 = wwoBIZ.WwoSearch(wmsCode);
		if (list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				WmsWarOut wmsWarOut = (WmsWarOut) list2.get(i);
				wmsWarOut.setWmsStro(null);
				wwoBIZ.updateWwo(wmsWarOut);
			}
		}
		//调拨单中的入库
		List list3 = wwoBIZ.wchSearch(wmsCode);
		if (list3.size() > 0) {
			for (int i = 0; i < list3.size(); i++) {
				WmsChange wmsChange = (WmsChange) list3.get(i);
				wmsChange.setWmsStroByWchInWms(null);
				wwoBIZ.updateWch(wmsChange);
			}
		}
		//调拨单中的出库
		List list4 = wwoBIZ.wchSearch2(wmsCode);
		if (list4.size() > 0) {
			for (int i = 0; i < list4.size(); i++) {
				WmsChange wmsChange = (WmsChange) list4.get(i);
				wmsChange.setWmsStroByWchOutWms(null);
				wwoBIZ.updateWch(wmsChange);
			}
		}
		List list5 = wwoBIZ.wmcSearch(wmsCode);
		if (list5.size() > 0) {
			for (int i = 0; i < list5.size(); i++) {
				WmsCheck wmsCheck = (WmsCheck) list5.get(i);
				wmsCheck.setWmsStro(null);
				wwoBIZ.updateWmc(wmsCheck);
			}
		}
		List list6 = wwoBIZ.wliSearch(wmsCode);
		if (list6.size() > 0) {
			for (int i = 0; i < list6.size(); i++) {
				WmsLine wmsLine = (WmsLine) list6.get(i);
				wmsLine.setWmsStro(null);
				wwoBIZ.updateWli(wmsLine);
			}
		}
		wmsManageBiz.delete(wmsStro);
		//		wmsStro.setWmsIsenabled("0");
		//		wmsManageBiz.updateWmsStro(wmsStro);
		request.setAttribute("msg", "删除仓库");
		wmsManageBiz.delLimRight(wmsCode);//删除仓库的权限码
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 仓库名称重复判断 <br>
	 * create_date: Aug 25, 2010,3:13:28 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsName:新仓库名称 oldName:原仓库名称
	 * @param response
	 * @return ActionForward 仓库名称重复输出1<br>
	 */
	public ActionForward checkWmsName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsName = "";
		String oldName = "";
		wmsName = TransStr.transStr(request.getParameter("wmsName"));
		if (request.getParameter("oldName") != null) {
			oldName = TransStr.transStr(request.getParameter("oldName"));
		}
		List list = wmsManageBiz.checkWmsName(wmsName);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (oldName != null) {
				if (!wmsName.equals(oldName) && list.size() > 0) {
					out.print("1");
				}
			} else {
				if (list.size() > 0) {
					out.print("1");
				}
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
	 * 进入新建入库单页面 <br>
	 * create_date: Aug 25, 2010,5:04:33 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * 		para > wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转到newWarIn页面<br>
	 * 		attribute > wmsStro:仓库列表 wmsCode:仓库编号
	 */
	public ActionForward toNewWwi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wmsCode");
		
		List list = wmsManageBiz.findAllWms();
		
		request.setAttribute("wmsStro", list);
		request.setAttribute("wmsCode", wmsCode);
		return mapping.findForward("newWarIn");
	}

	/**
	 * 入库单新建 <br>
	 * create_date: Aug 25, 2010,5:19:39 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wst:仓库编号 
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:保存成功信息提示
	 */
	public ActionForward saveWarIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wmsCode = request.getParameter("wst");
		java.util.Date currentDate = new java.util.Date();//当前日期
		CodeCreator codeCreator = new CodeCreator();
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		
		String wwiCode = codeCreator.createCode("PI" + dateFormat3.format(currentDate), "wms_war_in", 0);
		DynaActionForm form1 = (DynaActionForm) form;
		WmsWarIn wmsWarIn = (WmsWarIn) form1.get("wmsWarIn");
		List list=wmsManageBiz.findByWwiCode(wwiCode);
		if(list.size()>0){
			request.setAttribute("errorMsg", "该入库单号已被使用，请重新录入");
			return mapping.findForward("error");
		}
		else{
			if (wmsCode != null && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsWarIn.setWmsStro(wmsStro);
			} else {
				wmsWarIn.setWmsStro(null);
			}
			wmsWarIn.setWwiInpTime(currentDate);//录入时间
			wmsWarIn.setWwiInpName(limUser.getUserSeName());//录入人
			wmsWarIn.setWwiCode(wwiCode);
			wmsWarIn.setWwiIsdel("1");
			wmsWarIn.setWwiCode(wwiCode);
			wmsManageBiz.saveWwi(wmsWarIn);
			
			request.setAttribute("redir", "wms_in");
			request.setAttribute("eId", wmsWarIn.getWwiId());
			request.setAttribute("msg", "新建入库单");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 入库单查询 <br>
	 * create_date: Aug 25, 2010,5:30:59 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码 wwiTitle:入库单主题单据号 wmsCode:仓库编号 wwiAppIsok:审核状态  wwiState:列表筛选中的入库状态 
	 *                  wwiState1:查询条件中的入库状态  startTime、endTime:录入日期时间段
	 * @param response
	 * @return ActionForward 跳转到wmsWarIn页面<br>
	 * 		attribute > wmsWarIn:符合条件的入库单列表 inWmsCount:要入库的单据数量 appCount:要审核的入库单数量 wwiTitle:入库单主题单据号 wmsCode:仓库编号 wwiAppIsok:审核状态  wwiState:列表筛选中的入库状态 
	 *                  wwiState1:查询条件中的入库状态  startTime、endTime:录入日期时间段 wmsName:仓库名称
	 */
	public ActionForward wwiSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wwiTitle = TransStr.transStr(request.getParameter("wwiTitle"));
		String wmsCode = request.getParameter("wmsCode");
		String wwiAppIsok = request.getParameter("wwiAppIsok");
		//		String isok=request.getParameter("isok");
		String wwiState = "";
		String wwiState0 = request.getParameter("wwiState");
		String wwiState1 = request.getParameter("wwiState1");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] wwiInpTime = OperateDate.checkDate(startTime, endTime);
		String date = "";
		if (wwiInpTime[1] != null && !wwiInpTime[1].equals("")) {
			try {
				date = dateFormat.format(OperateDate.getDate(dateFormat
						.parse(wwiInpTime[1]), 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (wwiState1 != null && !wwiState1.equals("")) {
			wwiState = wwiState1;
		} else {
			wwiState = wwiState0;
		}
		Page page = new Page(wmsManageBiz.getWwiCount(wwiTitle, wmsCode,
				wwiAppIsok, wwiState, wwiInpTime[0], date), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wmsManageBiz.WwiSearch(wwiTitle, wmsCode, wwiAppIsok,
				wwiState, wwiInpTime[0], date, page.getCurrentPageNo(), page
						.getPageSize());
		//		if (list.size() <= 0) {
		//			request.setAttribute("msg", "对不起，没有您想要的入库单！");
		//		}
		int appCount = 0;
		int inWmsCount = 0;
		//		if(isok!=null&&!isok.equals("")){
		appCount = wmsManageBiz.findApp(wmsCode, "2");
		inWmsCount = wmsManageBiz.findApp(wmsCode, "1");
		//		}

		request.setAttribute("inWmsCount", inWmsCount);//今天要的入库的单据数量
		request.setAttribute("appCount", appCount);//今天要审核的入库单数量
		request.setAttribute("wwiAppIsok", wwiAppIsok);
		//		request.setAttribute("isok",isok);
		request.setAttribute("wmsCode", wmsCode);
		request.setAttribute("wwiState", wwiState0);
		request.setAttribute("wwiState1", wwiState1);
		request.setAttribute("startTime", wwiInpTime[0]);
		request.setAttribute("endTime", wwiInpTime[1]);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}

		request.setAttribute("wwiTitle", wwiTitle);
		request.setAttribute("page", page);
		request.setAttribute("wmsWarIn", list);
		return mapping.findForward("wmsWarIn");
	}

	/**
	 * 跳转到编辑入库明细页面 <br>
	 * create_date: Aug 26, 2010,10:02:27 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID
	 * @param response
	 * @return ActionForward 跳转到newRip页面 单据不存在跳转到error页面<br>
	 * 		attribute > 单据不存在 errorMsg:单据不存在提示信息
	 */
	public ActionForward toWwiPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		if (wmsWarIn != null) {
			request.setAttribute("wmsWarIn", wmsWarIn);
			return mapping.findForward("newRip");
		} else {
			request.setAttribute("errorMsg", "该入库单已不存在");
			return mapping.findForward("error");
		}
	}
	/**
	 * 查看入库详情 <br>
	 * create_date: Aug 26, 2010,10:02:27 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID 
	 * @param response
	 * @return ActionForward 跳转到RWarPro页面<br>
	 * 		attribute > wmsWarIn:入库单实体
	 */
	public ActionForward wwiDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		request.setAttribute("wmsWarIn", wmsWarIn);
		return mapping.findForward("RWarPro");
	}

	/**
	 * 批量编辑入库商品 <br>
	 * create_date: Aug 26, 2010,10:25:55 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID wprId:入库明细中产品ID数组
	 * @param response
	 * @return ActionForward 入库明细中商品为空跳转到empty页面 单据不存在跳转到error页面 成功编辑跳转到empty页面<br>
	 * 		attribute > 入库明细中商品为空 noMsg:不弹出任何信息直接刷新父页面<br>
	 *      attribute > 单据不存在 errorMsg:单据不存在信息提示<br>
	 *      attribute > 成功编辑 msg:成功编辑信息提示<br>
	 */
	public ActionForward wwiPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		if (wmsWarIn != null) {
			wmsManageBiz.delRwinPro(wmsWarIn.getWwiId());//批量删除入库明细
			wwoBIZ.updateByCode("0", wmsWarIn.getWwiId().toString());//批量删除库存流水--（只有入库有)
			String agr[] = request.getParameterValues("wprId");
			if (agr == null || agr.length <= 0) {
				wmsWarIn.setWwiAppIsok(null);
				wmsManageBiz.updateWwi(wmsWarIn);//更新入库单
				request.setAttribute("msg", "编辑入库明细成功！");
				return mapping.findForward("empty");
			}
			else{
				for (int i = 0; i < agr.length; i++) {
					RWinPro rwinPro = new RWinPro();
					String num1 = request.getParameter(agr[i] + "num");
					Double rwpNum = null;
					if (num1 != null && !num1.equals("")) {
						rwpNum = Double.parseDouble(num1);
					} else {
						rwpNum = 0.0;
					}
					String remark1 = request.getParameter(agr[i] + "remark");
					WmsProduct wmsProduct = new WmsProduct();
					wmsProduct.setWprId(agr[i]);
					rwinPro.setRwiWinNum(rwpNum);
					rwinPro.setRwiRemark(remark1);
					rwinPro.setWmsProduct(wmsProduct);
					rwinPro.setWmsWarIn(wmsWarIn);
					wmsManageBiz.saveRwinPro(rwinPro);
				}
				wmsWarIn.setWwiAppIsok("2");
				wmsManageBiz.updateWwi(wmsWarIn);//更新入库单
				request.setAttribute("msg", "编辑入库明细成功！");
				return mapping.findForward("empty");
			}
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 审核入库单 <br>
	 * create_date: Aug 26, 2010,10:33:23 AM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID wwiAppDesc:审核内容 wwiAppIsok:是否通过
	 * @param response
	 * @return ActionForward 审核成功重定向到入库单详情页面，单据不存在跳转到error页面<br>
	 * 		attribute > 转入error页面时，errorMsg:单据不存在信息提示
	 */
	public ActionForward appWwi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		String wwiAppDesc = request.getParameter("wwiAppDesc");
		String wwiAppIsok = request.getParameter("wwiAppIsok");
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		java.util.Date currentDate = new java.util.Date();//当前日期
		
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		if (wmsWarIn != null) {
			String wwiAppMan = limUser.getUserSeName();
			wmsWarIn.setWwiAppDate(currentDate);
			wmsWarIn.setWwiAppMan(wwiAppMan);
			wmsWarIn.setWwiAppDesc(wwiAppDesc);
			if (wwiAppIsok != null && !wwiAppIsok.equals("")) {
				wmsWarIn.setWwiAppIsok(wwiAppIsok);
			} else {
				wmsWarIn.setWwiAppIsok("1");
			}
			wmsManageBiz.updateWwi(wmsWarIn);//更新入库单
			String url = "wmsManageAction.do?op=wwiDesc&wwiId=" + wwiId;
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			request.setAttribute("errorMsg", "该入库单不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 跳转到修改入库商品页面 <br>
	 * create_date: Aug 26, 2010,2:48:46 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > rwiId:入库明细ID
	 * @param response
	 * @return ActionForward 跳转到updateRWarPro页面<br>
	 * 		attribute > wprId:商品ID wwiId:入库单ID rwinPro:入库明细实体
	 */
	public ActionForward toUpdWip(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwiId = Long.parseLong(request.getParameter("rwiId"));
		RWinPro rwinPro = wmsManageBiz.getRWinPro(rwiId);
		String wprId = rwinPro.getWmsProduct().getWprId();
		request.setAttribute("wprId", wprId);
		Long wwiId = rwinPro.getWmsWarIn().getWwiId();
		request.setAttribute("wwiId", wwiId);
		request.setAttribute("rwinPro", rwinPro);
		return mapping.findForward("updateRWarPro");
	}

	/**
	 * 编辑入库商品 <br>
	 * create_date: Aug 26, 2010,2:58:50 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > rwiId:入库明细ID wprId:商品ID wwiId:入库单ID rwiWinNum:入库数量 rwiRemark:备注
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:编辑成功信息提示
	 */
	public ActionForward updateWinPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		RWinPro rwinPro = (RWinPro) form1.get("rWinPro");

		Long rwiId = Long.parseLong(request.getParameter("rwiId"));
		rwinPro.setRwiId(rwiId);
		//入库单
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi(Long.parseLong(wwiId));
		rwinPro.setWmsWarIn(wmsWarIn);

		String wprId = request.getParameter("wprId");
		WmsProduct wmsProduct = new WmsProduct();
		wmsProduct.setWprId(wprId);
		rwinPro.setWmsProduct(wmsProduct);
		String num1 = request.getParameter("rwiWinNum");
		Double rwiWinNum = Double.parseDouble(num1);
		rwinPro.setRwiWinNum(rwiWinNum);
		rwinPro.setRwiRemark(request.getParameter("rwiRemark"));
		wmsManageBiz.updateRwp(rwinPro);
		
		//		List list1=wwoBIZ.findByWmsId("0", rwiId);
		//		WmsLine wmsLine=(WmsLine)list1.get(0);
		//		wmsLine.setWliInNum(rwiWinNum);
		//		wwoBIZ.updateWli(wmsLine);
		request.setAttribute("msg", "编辑入库商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除入库商品 <br>
	 * create_date: Aug 26, 2010,3:01:54 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > rwiId:入库明细ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:删除成功信息提示
	 */
	public ActionForward delRwp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rwiId = Long.parseLong(request.getParameter("rwiId"));

		RWinPro rwinPro = wmsManageBiz.getRWinPro(rwiId);
		WmsWarIn wmsWarIn = rwinPro.getWmsWarIn();
		wmsManageBiz.delRwp(rwinPro);
		//更新入库单审核信息
		if (wmsWarIn.getRWinPros().size() == 0) {
			wmsWarIn.setWwiAppIsok(null);
			wmsManageBiz.updateWwi(wmsWarIn);//更新入库单
		}
		request.setAttribute("msg", "删除入库商品");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 确认入库 <br>
	 * create_date: Aug 26, 2010,3:05:23 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号 wwiId:入库单ID
	 * @param response
	 * @return ActionForward 跳转到empty页面<br>
	 * 		attribute > msg:商品入库成功信息提示
	 */
	public ActionForward saveRwarPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取页面List
		String wmsCode = request.getParameter("wmsCode");//仓库编号	
		String wwiId = request.getParameter("wwiId");
		LimUser lim = (LimUser) request.getSession().getAttribute("limUser");
		java.util.Date currentDate = new java.util.Date();//当前日期
		synchronized (this) {
			WmsWarIn wmsWarIn = wmsManageBiz.findWwi(Long.parseLong(wwiId));
			if (!wmsWarIn.getWwiState().equals("1")) {
				List<RWinPro> list1 = wmsManageBiz.findAllRwinPro(Long
						.parseLong(wwiId));
				Iterator<RWinPro> it1 = list1.iterator();
				if (list1.size() > 0) {
					while (it1.hasNext()) {
						RWinPro rwinPro = (RWinPro) it1.next();
						String wprId1 = rwinPro.getWmsProduct().getWprId();//商品编号
						Double num1 = rwinPro.getRwiWinNum();//商品数量

						//获取仓库里的商品list
//						List list = wwoBIZ.findProNum(wmsCode, wprId1);
						List list=new ArrayList();
						boolean flag = false;
						if (list.size() == 0) {
							RStroPro rstroPro1 = new RStroPro();
							WmsStro wmsStro = wmsManageBiz.findWmsStro(wmsCode);
							rstroPro1.setWmsProduct(rwinPro.getWmsProduct());
							rstroPro1.setRspProNum(num1);
							rstroPro1.setWmsStro(wmsStro);
							wmsManageBiz.saveRsp(rstroPro1);//仓库为空的情况	
						}
						if (list.size() > 0) {
							RStroPro rstroPro1 = (RStroPro) list.get(0);
							Double num = rstroPro1.getRspProNum();// 商品数量
							Long rspId = rstroPro1.getRspId();//仓库明细ID
							num = num + num1;
							rstroPro1.setRspProNum(num);
							rstroPro1.setRspId(rspId);
							wmsManageBiz.updatePsp(rstroPro1);
						}
						//生成流水
						WmsLine wmsLine = new WmsLine();
						wmsLine.setWliType("0");//流水类别--入库
						wmsLine.setWliTypeCode(wwiId);
						wmsLine.setWmsProduct(rwinPro.getWmsProduct());
						wmsLine.setWmsStro(wmsWarIn.getWmsStro());
						wmsLine.setWliDate(wmsWarIn.getWwiInpTime());
						wmsLine.setWliState("1");//已执行
						wmsLine.setWliInNum(num1);
						wmsLine.setWliWmsId(rwinPro.getRwiId());
						wmsLine.setWliIsdel("1");
						wmsLine.setWliDate(currentDate);
//						List list2 = wwoBIZ.findProNum(wmsCode, wprId1);
						List list2 =new ArrayList();
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

				wmsWarIn.setWwiState("1");
				wmsWarIn.setWwiInDate(currentDate);//入库时间
				wmsWarIn.setWwiOpman(lim.getUserSeName());//执行人
				wmsManageBiz.updateWwi(wmsWarIn);//更新入库单

				//			List li=wwoBIZ.findByTypeCode("0",wwiId);
				//			if(li.size()>0){
				//				for(int i=0;i<li.size();i++){
				//					WmsLine wmsLine=(WmsLine)li.get(i);
				//					wmsLine.setWliState("1");
				//					wmsLine.setWliDate(currentDate);
				//					wmsLine.setWliMan(lim.getUserSeName());
				//					wwoBIZ.updateWli(wmsLine);//更新流水
				//				}
				//			}
			}
		}
		request.setAttribute("msg", "商品入库成功！");
		return mapping.findForward("empty");
	}

	/**
	 * 进入修改入库单页面 <br>
	 * create_date: Aug 26, 2010,3:13:33 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号 wwiId:入库单ID
	 * @param response
	 * @return ActionForward 单据不存在跳转到error页面 入库单存在跳转到updateWwi页面<br>
	 * 		attribute > 单据不存在 errorMsg:提示单据不存在<br>
	 *      attribute > 单据存在 wmsCode:仓库编号 wmsWarIn:入库单实体 wmsStro:仓库列表<br>
	 */
	public ActionForward toUpdWwi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		String wmsCode = request.getParameter("wmsCode");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		if (wmsWarIn != null) {
			List list1 = wmsManageBiz.findAllWms();
			request.setAttribute("wmsWarIn", wmsWarIn);
			request.setAttribute("wmsCode", wmsCode);
			request.setAttribute("wmsStro", list1);
			return mapping.findForward("updateWwi");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 修改入库单信息<br>
	 * create_date: Aug 26, 2010,3:22:54 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wmsCode:仓库编号 wwiId:入库单ID isIfrm:是否在详情页面中编辑若为1则是在详情页面中编辑
	 * @param response
	 * @return ActionForward 成功编辑跳转到popDivSuc页面 单据不存在跳转到error页面<br>
	 * 		attribute > 单据不存在 errorMsg:提示单据不存在<br>
	 *      attribute > 成功编辑 msg:成功编辑信息提示<br>
	 */
	public ActionForward updateWwi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		WmsWarIn wmsWarIn = (WmsWarIn) form1.get("wmsWarIn");
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn1 = wmsManageBiz.findWwi(Long.parseLong(wwiId));
		if (wmsWarIn1 != null) {
			String wmsCode = request.getParameter("wmsCode");
			if (wmsCode != null && !wmsCode.equals("")) {
				WmsStro wmsStro = new WmsStro();
				wmsStro.setWmsCode(wmsCode);
				wmsWarIn1.setWmsStro(wmsStro);
			} else {
				wmsWarIn1.setWmsStro(null);
			}
			java.util.Date currentDate = new java.util.Date();//当前日期
			wmsWarIn1.setWwiAltTime(currentDate);//修改时间
			LimUser lim = (LimUser) request.getSession()
					.getAttribute("limUser");
			wmsWarIn1.setWwiAltName(lim.getUserSeName());//修改人

			wmsWarIn1.setWwiTitle(wmsWarIn.getWwiTitle());
			wmsWarIn1.setWwiRemark(wmsWarIn.getWwiRemark());
			//详情下刷新iframe
			if (request.getParameter("isIfrm") != null
					&& request.getParameter("isIfrm").equals("1")) {
				request.setAttribute("isIfrm", "1");
			}
			wmsManageBiz.updateWwi(wmsWarIn1);
			request.setAttribute("msg", "编辑入库单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}

	}

	/**
	 * 删除未入库的入库单 <br>
	 * create_date: Aug 26, 2010,3:27:13 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID
	 * @param response
	 * @return ActionForward 成功删除跳转到popDivSuc页面 单据不存在跳转到error页面<br>
	 * 		attribute > 单据不存在 errorMsg:提示单据不存在<br>
	 *      attribute > 成功删除 msg:成功删除信息提示<br>
	 */
	public ActionForward delWmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
		if (wmsWarIn != null) {
			String wmsCode = wmsWarIn.getWmsStro().getWmsCode();
			List list = wmsManageBiz.findAllRwinPro(Long.parseLong(wwiId));
			Iterator it = list.iterator();
			if (list.size() > 0) {
				while (it.hasNext()) {
					RWinPro rwinPro = (RWinPro) it.next();
					String wprId = rwinPro.getWmsProduct().getWprId();//商品编号
					Double num1 = rwinPro.getRwiWinNum();//商品数量
				}
			}
			wmsManageBiz.deleteWwi(wmsWarIn);
			request.setAttribute("msg", "删除入库单");
			return mapping.findForward("popDivSuc");
		} else {
			request.setAttribute("errorMsg", "该单据不存在");
			return mapping.findForward("error");
		}
	}

	/**
	 * 恢复入库单 <br>
	 * create_date: Aug 26, 2010,3:35:38 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > id:入库单ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:成功恢复信息提示
	 */
	public ActionForward recWarIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi(id);
		wmsWarIn.setWwiIsdel("1");
		wmsManageBiz.updateWwi(wmsWarIn);
		request.setAttribute("msg", "恢复入库单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除入库单不能再恢复 <br>
	 * create_date: Aug 26, 2010,3:37:17 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > id:入库单ID
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attribute > msg:彻底删除成功信息提示
	 */
	public ActionForward delWarIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("id"));
		WmsWarIn wmsWarIn = wmsManageBiz.findWwi(id);
		wmsManageBiz.deleteWwi(wmsWarIn);
		request.setAttribute("msg", "删除入库单");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 按商品编号查询库存列表 <br>
	 * create_date: Aug 26, 2010,3:38:57 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码  wprName:商品名称 wmsCode:仓库编号
	 * @param response
	 * @return ActionForward 跳转到rwpSearch页面<br>
	 * 		attribute > wprName:商品名称 wmsCode:仓库编号 wmsName:仓库名称 rStroPro:符合条件的库存列表 isAll:标识是否是全部仓库值为1表示全部仓库 page:分页信息
	 */
	public ActionForward rwpSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		String wprId = "";
		//String wm=request.getParameter("wm");
		/*if (wm != null && wm.equals("wm")) {
			wprId="";
		}else{
			wprId=request.getParameter("wprId");
			request.setAttribute("wprId", wprId);
		}*/
		String wprName = TransStr.transStr(request.getParameter("wprName"));
		if (wprName != null && !wprName.equals("")) {
			request.setAttribute("wprName", wprName);
		}
		String wmsCode = request.getParameter("wmsCode");
		Page page = new Page(wwoBIZ.getCountByWms(wmsCode, wprName), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wwoBIZ.findProByWms(wmsCode, wprName, page
				.getCurrentPageNo(), page.getPageSize());
		//		if (list.size() <= 0) {
		//			request.setAttribute("msg", "对不起，没有您想要的库存信息！");
		//		}
		//String n=request.getParameter("n");
		//request.setAttribute("n", n);//css样式参数
		//List list1=wmsManageBiz.findAllWms();
		//request.setAttribute("wmsStro",list1);
		request.setAttribute("wmsCode", wmsCode);
		//仓库名
		if (wmsCode != null && wmsCode != "") {
			request.setAttribute("wmsName", wmsManageBiz.findWmsStro(wmsCode)
					.getWmsName());
		}
		request.setAttribute("isAll", "0");
		request.setAttribute("page", page);
		request.setAttribute("rStroPro", list);
		return mapping.findForward("rwpSearch");
	}

	/**
	 * 查询所有仓库信息<br>
	 * create_date: Aug 26, 2010,3:53:26 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到rwpSearch页面<br>
	 * 		attribute > rStroPro:符合条件的库存列表 isAll:标识是否是全部仓库值为1表示全部仓库 page:分页信息
	 */
	public ActionForward rStroProSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wmsManageBiz.getCountRStroPro(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wmsManageBiz.rStroProSearch(page.getCurrentPageNo(), page
				.getPageSize());
		//		if (list.size() <= 0) {
		//			request.setAttribute("msg", "对不起，没有您想要的库存信息！");
		//		}
		//String n=request.getParameter("n");
		//request.setAttribute("n", n);//css样式参数
		//		List list1=wmsManageBiz.findAllWms();
		//		request.setAttribute("wmsStro",list1);
		request.setAttribute("isAll", "1");
		request.setAttribute("page", page);
		request.setAttribute("rStroPro", list);
		return mapping.findForward("rwpSearch");
	}

	/**
	 * 跳转到撤销入库页面 <br>
	 * create_date: Aug 26, 2010,4:18:37 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID
	 * @param response
	 * @return ActionForward 跳转到delWwi页面<br>
	 * 		attribute > wwiId:入库单ID
	 */
	public ActionForward delWwi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		request.setAttribute("wwiId", wwiId);
		return mapping.findForward("delWwi");
	}

	/**
	 * 撤销入库操作 <br>
	 * create_date: Aug 26, 2010,4:20:06 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > wwiId:入库单ID
	 * @param response
	 * @return ActionForward 单据不存在跳转到error页面 成功撤销跳转到popDivSuc页面<br>
	 * 		attribute > 单据不存在 errorMsg:提示单据不存在<br>
	 *      attribute > 成功撤销 msg:成功撤销信息提示<br>
	 */
	public ActionForward delWwiDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String wwiId = request.getParameter("wwiId");
		synchronized (this) {
			WmsWarIn wmsWarIn = wmsManageBiz.findWwi2(Long.parseLong(wwiId));
			if (wmsWarIn == null) {
				request.setAttribute("errorMsg", "该单据不存在");
				return mapping.findForward("error");
			} else if (!wmsWarIn.getWwiState().equals("2")) {
				String wmsCode = wmsWarIn.getWmsStro().getWmsCode();
				List list = wmsManageBiz.findAllRwinPro(Long.parseLong(wwiId));
				Iterator it = list.iterator();
				if (list.size() > 0) {
					while (it.hasNext()) {
						RWinPro rwinPro = (RWinPro) it.next();
						String wprId = rwinPro.getWmsProduct().getWprId();//商品编号
						Double num1 = rwinPro.getRwiWinNum();//商品数量

						//获取仓库里的list
//						List list1 = wwoBIZ.findProNum(wmsCode, wprId);
						List list1 = new ArrayList();
						if (list1 == null || list1.size() == 0) {
							request.setAttribute("errorMsg", "该仓库已无'"
									+ rwinPro.getWmsProduct().getWprName()
									+ "'商品,无法完成此操作");
							return mapping.findForward("error");
						} else {
							RStroPro rstroPro1 = (RStroPro) list1.get(0);
							Double num = rstroPro1.getRspProNum();// 商品数量
							Long rspId = rstroPro1.getRspId();//仓库ID
							if (num1 <= num) {
								num = num - num1;
								rstroPro1.setRspProNum(num);
								rstroPro1.setRspId(rspId);
								wmsManageBiz.updatePsp(rstroPro1);
							} else {
								request.setAttribute("errorMsg", "该仓库'"
										+ rwinPro.getWmsProduct().getWprName()
										+ "'数量不够,无法完成撤销操作");
								return mapping.findForward("error");
							}
						}
					}
				}
				wmsWarIn.setWwiState("2");
				LimUser lim = (LimUser) request.getSession().getAttribute(
						"limUser");
				java.util.Date currentDate = new java.util.Date();//当前日期
				//			wmsWarIn.setWwiInDate(currentDate);//撤销时间
				wmsWarIn.setWwiCanDate(currentDate);
				wmsWarIn.setWwiCanMan(lim.getUserSeName());//*撤销的执行人----待商量
				wmsManageBiz.updateWwi(wmsWarIn);
				List li = wwoBIZ.findByTypeCode("0", wwiId);
				if (li.size() > 0) {
					for (int i = 0; i < li.size(); i++) {
						WmsLine wmsLine = (WmsLine) li.get(i);
						wmsLine.setWliState("2");
						//					wmsLine.setWliDate(currentDate);
						wmsLine.setWliMan(lim.getUserSeName());
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
			request.setAttribute("msg", "撤销入库");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 获得待删除的入库单 <br>
	 * create_date: Aug 26, 2010,4:28:49 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > p:当前页码
	 * @param response
	 * @return ActionForward 跳转到recWarIn页面<br>
	 * 		attribute > wmsWarIn:符合条件的入库单列表 page:分页信息
	 */
	public ActionForward findDelWarIn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter("p");
		if (p == null || p.trim().length() < 1) {
			p = "1";
		}
		Page page = new Page(wmsManageBiz.findDelWarInCount(), 20);
		page.setCurrentPageNo(Integer.parseInt(p));
		List list = wmsManageBiz.findDelWarIn(page.getCurrentPageNo(), page
				.getPageSize());
		request.setAttribute("wmsWarIn", list);
		request.setAttribute("page", page);
		return mapping.findForward("recWarIn");

	}

	/**
	 * 注入wmsManageBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param wmsManageBiz 
	 */
	public void setWmsManageBiz(WmsManageBIZ wmsManageBiz) {
		this.wmsManageBiz = wmsManageBiz;
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
	 * 注入wwoBIZ <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param wwoBIZ 
	 */
	public void setWwoBIZ(WwoBIZ wwoBIZ) {
		this.wwoBIZ = wwoBIZ;
	}

	/**
	 * 注入typeListBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param typeListBiz 
	 */
	public void setTypeListBiz(TypeListBIZ typeListBiz) {
		this.typeListBiz = typeListBiz;
	}

	/**
	 * 注入orderBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param orderBiz 
	 */
	public void setOrderBiz(OrderBIZ orderBiz) {
		this.orderBiz = orderBiz;
	}

	/**
	 * 注入userBiz <br>
	 * create_date: Aug 13, 2010,11:21:56 AM
	 * @param userBiz 
	 */
	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}

}