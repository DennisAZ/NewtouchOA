package yh.subsys.oa.saleOrder.act;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHERPApplyAct {
	
	/**
	 * 销售订单申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String saleOrderApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("ORDER_TITLE", ((String[])paraMap.get("ORDER_TITLE"))[0]);
			map.put("ORDER_CODE", ((String[])paraMap.get("ORDER_CODE"))[0]);
			map.put("SALESPERSON", person.getUserName());
			
			String url = ut.runERPHook(dbConn, person, map,"sale_order");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	//采购申请单申请工作流
public String purchaseRequestApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("NAME", ((String[])paraMap.get("NAME"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			map.put("person", person.getUserName());
			
			String url = ut.runERPHook(dbConn, person, map,"purchase_request");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
//采购单申请工作流
public String purchaseApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			map.put("TITLE", ((String[])paraMap.get("TITLE"))[0]);
			map.put("PERSON", ((String[])paraMap.get("PERSON"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"purchase");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
//财务应付单申请工作流
public String financeOutApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			map.put("person", person.getUserName());
			String url = ut.runERPHook(dbConn, person, map,"finance_out");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

//生产通知单申请工作流
public String notifyApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
	
	Connection dbConn = null;
	String result = "";
	try {
		// 数据库的连接
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHPerson person = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		YHFlowHookUtility ut = new YHFlowHookUtility();
		
		Map paraMap = request.getParameterMap();
		Map map = new HashMap();
		map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
		map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
		String url = ut.runERPHook(dbConn, person, map,"notify");
		
		if (!"".equals(url)) {
			String path = request.getContextPath();
			result = path + url;
		}
		
		result = "{url:'" + result + "'}";
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_DATA, result);
	} catch (Exception e) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
		throw e;
	}
	return "/core/inc/rtjson.jsp";
}
//生产计划申请工作流
public String planApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
	
	Connection dbConn = null;
	String result = "";
	try {
		// 数据库的连接
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHPerson person = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		YHFlowHookUtility ut = new YHFlowHookUtility();
		
		Map paraMap = request.getParameterMap();
		Map map = new HashMap();
		map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
		map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
		String url = ut.runERPHook(dbConn, person, map,"plan");
		
		if (!"".equals(url)) {
			String path = request.getContextPath();
			result = path + url;
		}
		
		result = "{url:'" + result + "'}";
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_DATA, result);
	} catch (Exception e) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
		throw e;
	}
	return "/core/inc/rtjson.jsp";
}
//财务应付单明细申请工作流
public String financeOutDetailApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			map.put("FO_ID", ((String[])paraMap.get("FO_ID"))[0]);
			map.put("person", person.getUserName());
			String url = ut.runERPHook(dbConn, person, map,"finance_out_detial");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
/**
 * 仓库收货单申请工作流
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String whInApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
	
	Connection dbConn = null;
	String result = "";
	try {
		// 数据库的连接
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHPerson person = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		YHFlowHookUtility ut = new YHFlowHookUtility();

		Map paraMap = request.getParameterMap();
		Map map = new HashMap();
		map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
		map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
		map.put("PDEID", ((String[])paraMap.get("PDEID"))[0]);
		String url = ut.runERPHook(dbConn, person, map,"purchase_in_detial");

		if (!"".equals(url)) {
			String path = request.getContextPath();
			result = path + url;
		}

		result = "{url:'" + result + "'}";
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_DATA, result);
	} catch (Exception e) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
		throw e;
	}
	return "/core/inc/rtjson.jsp";
}
	/**
	 * 仓库发货单申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String whPODApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();

			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("POD_CODE", ((String[])paraMap.get("POD_CODE"))[0]);
			map.put("ORDER_ID", ((String[])paraMap.get("ORDER_ID"))[0]);
			map.put("PO_ID", ((String[])paraMap.get("PO_ID"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"wh_pod");

			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}

			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 库存报损申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String dbLoss(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();
			
			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"loss");
			
			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}
			
			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 财务回款明细单申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String financePPDApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();
			
			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("PPD_CODE", ((String[])paraMap.get("PPD_CODE"))[0]);
			map.put("ORDER_ID", ((String[])paraMap.get("ORDER_ID"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"finance_ppd");
			
			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}
			
			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 财务应收单申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String financeInApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();
			
			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"finance_in");
			
			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}
			
			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 财务应收明细申请工作流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String financeInDetialApply(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Connection dbConn = null;
		String result = "";
		try {
			// 数据库的连接
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					YHConst.LOGIN_USER);
			YHFlowHookUtility ut = new YHFlowHookUtility();
			
			Map paraMap = request.getParameterMap();
			Map map = new HashMap();
			map.put("KEY", ((String[])paraMap.get("KEY"))[0]);
			map.put("CODE", ((String[])paraMap.get("CODE"))[0]);
			
			String url = ut.runERPHook(dbConn, person, map,"finance_in_detial");
			
			if (!"".equals(url)) {
				String path = request.getContextPath();
				result = path + url;
			}
			
			result = "{url:'" + result + "'}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, result);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
}
