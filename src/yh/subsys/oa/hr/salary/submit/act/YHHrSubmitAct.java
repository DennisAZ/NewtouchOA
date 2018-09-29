package yh.subsys.oa.hr.salary.submit.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.submit.data.YHSalPerson;
import yh.subsys.oa.hr.salary.submit.logic.YHHrSubmitLogic;

public class YHHrSubmitAct {
	private YHHrSubmitLogic logic = new YHHrSubmitLogic();
	public static final String attachmentFolder = "hr";

	/**
	 * 工资代办流程 通用列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSubmitListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getSubmitJsonLogic(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 * 动态页面后台获取方法（部门列表）
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalItemIdAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		List<YHSalPerson> listPerson = null;
		String deptId = request.getParameter("deptId");
		String flowId = request.getParameter("flowId");
		List<YHSalItem> listSalItem = new ArrayList<YHSalItem>();
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
			String slaItemId = this.logic.getSalItemIdLogic(dbConn, user);
			String[] slaItemIds = slaItemId.split(",");
			int ArrayNum = slaItemIds.length;
			if (slaItemIds[ArrayNum - 1] == "") {
				ArrayNum--;
			}
			if (ArrayNum != 0 && ArrayNum > 0) {
				for (int i = 0; i < ArrayNum; i++) {
					YHSalItem salItem = this.logic.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
					listSalItem.add(salItem);
				}
			}
			if (!YHUtility.isNullorEmpty(deptId)) {
				listPerson = this.logic.getDeptPersonNameLogic(dbConn, user, deptId, flowId);
			}
			request.setAttribute("deptId", deptId);// 部门Id
			request.setAttribute("listSalItem", listSalItem);// 显示多少薪酬项目标题
			request.setAttribute("listPerson", listPerson);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/salary/submit/run/wageList.jsp";
	}

	/**
	 * 批量设置人员财务工资
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setSubmitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			this.logic.setSubmitInfo(dbConn, YHFOM.buildMap(request.getParameterMap()), person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 动态页面后台获取方法（人员列表）
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalItemIdUserAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		YHSalPerson person = null;
		String userId = request.getParameter("userId");
		String flowId = request.getParameter("flowId");
		String history = request.getParameter("history");
		List<YHSalItem> listSalItem = new ArrayList<YHSalItem>();
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
			String slaItemId = this.logic.getSalItemIdLogic2(dbConn, user);
			String[] slaItemIds = slaItemId.split(",");
			int ArrayNum = slaItemIds.length;
			if (slaItemIds[ArrayNum - 1] == "") {
				ArrayNum--;
			}
			if (ArrayNum != 0 && ArrayNum > 0) {
				for (int i = 0; i < ArrayNum; i++) {
					YHSalItem salItem = this.logic.getSalItemIdAndNameLogic2(dbConn, user, Integer.valueOf(slaItemIds[i]));
					listSalItem.add(salItem);
				}
			}
			if (!YHUtility.isNullorEmpty(userId)) {
				person = this.logic.getPersonNameLogic(dbConn, user, userId, flowId, history);
			}
			String yesOtherId = this.logic.getYesOtherLogic(dbConn, user);
			if (!YHUtility.isNullorEmpty(yesOtherId)) {
				request.setAttribute("yesOtherId", Integer.valueOf(yesOtherId));// 查询HR_INSURANCE_PARA中的yesOther
			}else {
			  request.setAttribute("yesOtherId", 0);
			}
			request.setAttribute("userId", userId);// 部门Id
			request.setAttribute("listSalItem", listSalItem);// 显示多少薪酬项目标题
			request.setAttribute("person", person);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
			throw e;
		}
		if (history != null) {
			return "/subsys/oa/hr/salary/submit/history/wageListUser.jsp";
		}
		return "/subsys/oa/hr/salary/submit/run/wageListUser.jsp";
	}

	/**
	 * 获取计算项公式
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFormula(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name = request.getParameter("name");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getFormula(dbConn, name);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 指定设置人员财务工资
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setSubmitUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			this.logic.setSubmitInfoUser(dbConn, YHFOM.buildMap(request.getParameterMap()), person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 工资代办流程 通用列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSubmitHistoryListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getSubmitHistoryListJson(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}
	
	/**
	 * 下载CSV模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String downCSVTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
	  Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String fileName = URLEncoder.encode("工资数据录入.csv", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ArrayList<YHDbRecord> dbL = this.logic.downCSVTempletLogic(dbConn);
			YHCSVUtil.CVSWrite(response.getWriter(), dbL);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}
	/**
	 * 获取总数
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSalDataCountByFlowId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flowIdStr = request.getParameter("flowId");
		int flowId = 0;
		if (YHUtility.isNumber(flowIdStr)) {
			flowId = Integer.parseInt(flowIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int count = this.logic.getSalDataCountByFlowIdLogic(dbConn, flowId);
			if (count > 0) {
				count = 1;
			}
			String data = "{count:\"" + count + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	/**
	 * 导入CSV工资数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String impSubmitInfoByCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			StringBuffer buffer = new StringBuffer("[");
			Map<Object, Object> returnMap = this.logic.impSubmitInfoByCsvLogic(dbConn, fileForm, person, buffer);
			int isCount = (Integer) returnMap.get("isCount");
			int updateCount = (Integer) returnMap.get("updateCount");
			if (buffer.charAt(buffer.length() - 1) == ',') {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
			String data = buffer.toString();
			request.setAttribute("isCount", isCount);
			request.setAttribute("updateCount", updateCount);
			request.setAttribute("contentList", data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "导入数据失败");
			throw e;
		}
		return "/subsys/oa/hr/salary/submit/run/import.jsp";
	}
	

}
