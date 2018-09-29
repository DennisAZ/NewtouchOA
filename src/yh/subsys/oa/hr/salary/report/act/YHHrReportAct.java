package yh.subsys.oa.hr.salary.report.act;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.attendance.personal.logic.YHAttendEvectionLogic;
import yh.core.funcs.attendance.personal.logic.YHAttendLeaveLogic;
import yh.core.funcs.attendance.personal.logic.YHAttendOutLogic;
import yh.core.funcs.calendar.info.logic.YHInfoLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHOvertimeRecord;
import yh.custom.attendance.logic.YHOvertimeRecordLogic;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.report.logic.YHHrReportLogic;
import yh.subsys.oa.hr.salary.submit.data.YHSalPerson;
import yh.subsys.oa.hr.score.data.YHScoreFlow;

public class YHHrReportAct {
	private YHHrReportLogic logic = new YHHrReportLogic();
	public static final String attachmentFolder = "hr";

	/**
	 * 工资上报待办流程 通用列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getReportListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getReportJsonLogic(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
			pw.close();
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
		return "/subsys/oa/hr/salary/report/run/wageList.jsp";
	}

	/**
	 * 批量设置人员财务工资
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setReportInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			this.logic.setReportInfo(dbConn, YHFOM.buildMap(request.getParameterMap()), person);
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
			return "/subsys/oa/hr/salary/report/history/wageListUser.jsp";
		}
		return "/subsys/oa/hr/salary/report/run/wageListUser.jsp";
	}

	/**
	 * 指定设置人员财务工资
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setReportUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			this.logic.setReportInfoUser(dbConn, YHFOM.buildMap(request.getParameterMap()), person);
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
	public String getReportHistoryListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getReportHistoryListJson(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
			pw.close();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 * 获取考勤记录
	 * 
	 * @param request
	 * @param response
	 * 
	 * */
	public String getAttendance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userIdStr");
		String flowId = request.getParameter("flowId");
		Connection dbConn = null;

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getAttendanceLogic(dbConn, userId, flowId, person);
			// System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";

	}

	/**
	 * 
	 * 外出历史记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectHistoryOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHAttendOut out = new YHAttendOut();
			YHAttendOutLogic yhaol = new YHAttendOutLogic();
			stmt = dbConn.createStatement();

			SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cDay1 = Calendar.getInstance();
			String flowId = request.getParameter("flowId");
			String sql = "select SAL_YEAR,SAL_MONTH from oa_sal_flow where SEQ_ID='" + flowId + "'";

			rs = stmt.executeQuery(sql);
			String currentdate = "";
			if (rs.next()) {
			  String str = rs.getString(1) + "-" + rs.getString(2) + "-";
				currentdate = str + "01";
			}

			cDay1.setTime(curTime.parse(currentdate));
			int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
			Date lastDate = curTime.parse(currentdate);
			lastDate.setDate(lastDay);

			String date = curTime.format(lastDate);
			String startDate = date.substring(0, 7) + "-01 00:00:00";
			String endDate = date + " 23:59:59";

			String whereStr = "";
			whereStr += " and " + YHDBUtility.getDateFilter("SUBMIT_TIME", startDate, ">=") + " and "
					+ YHDBUtility.getDateFilter("SUBMIT_TIME", endDate, "<=");

			Map map = new HashMap();
			YHFOM fom = new YHFOM();
			String userid = request.getParameter("userId");
			int userId = Integer.parseInt(userid);
			map.put("USER_ID", userId);
			map.put("ALLOW", "1");
			map.put("status", "1");
			String[] str = { "USER_ID='" + userId + "' and ALLOW='1' " + whereStr + " order by SUBMIT_TIME desc" };

			String data = "[";

			List<YHAttendOut> outList = yhaol.selectHistoryOut(dbConn, str);
			for (int i = 0; i < outList.size(); i++) {
				YHPersonLogic tpl = new YHPersonLogic();
				out = outList.get(i);
				String leaderName = "";
				leaderName = tpl.getNameBySeqIdStr(out.getLeaderId(), dbConn);
				if (leaderName != null && !leaderName.equals("")) {
					leaderName = YHUtility.encodeSpecial(leaderName);
				}
				data = data + YHFOM.toJson(outList.get(i)).toString().substring(0, YHFOM.toJson(outList.get(i)).toString().length() - 1) + ",leaderName:\""
						+ leaderName + "\"},";
			}
			if (outList.size() > 0) {
				data = data.substring(0, data.length() - 1);
			}
			data = data + "]";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 
	 * 查询所有历史请假记录根据自己的ID
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectHistroyLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String userid = request.getParameter("userId");
			String flowId = request.getParameter("flowId");
			int userId = Integer.parseInt(userid);

			YHAttendLeave leave = new YHAttendLeave();

			String sql = "";
			stmt = dbConn.createStatement();

			SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cDay1 = Calendar.getInstance();

			sql = "select SAL_YEAR,SAL_MONTH from oa_sal_flow where SEQ_ID='" + flowId + "'";
			rs = stmt.executeQuery(sql);
			String currentdate = "";
			if (rs.next()) {
			  String str = rs.getString(1) + "-" + rs.getString(2) + "-";
				currentdate = str + "01";
			}

			cDay1.setTime(curTime.parse(currentdate));
			int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
			Date lastDate = curTime.parse(currentdate);
			lastDate.setDate(lastDay);

			String date = curTime.format(lastDate);

			String startDate = date.substring(0, 7) + "-01 00:00:00";
			String endDate = date + " 23:59:59";

			String whereStr = "";
			whereStr += " and " + YHDBUtility.getDateFilter("leave_date1", startDate, ">=") + "  and "
					+ YHDBUtility.getDateFilter("leave_date1", endDate, "<=");

			String data = "[";
			Map map = new HashMap();
			map.put("USER_ID", userId);
			// map.put("STATUS", "2");
			String[] str = { "USER_ID='" + userId + "' and ALLOW = '1'" + whereStr + " order by LEAVE_DATE1 desc" };
			YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
			List<YHAttendLeave> leaveList = yhall.selectHistroyLeave(dbConn, str);
			for (int i = 0; i < leaveList.size(); i++) {
				YHPersonLogic tpl = new YHPersonLogic();
				leave = leaveList.get(i);
				// System.out.println(leave.getDestroyTime());
				String leaderName = "";
				leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId(), dbConn);
				if (leaderName != null && !leaderName.equals("")) {
					leaderName = YHUtility.encodeSpecial(leaderName);
				}
				data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length() - 1)
						+ ",leaderName:\"" + leaderName + "\"},";
			}
			if (leaveList.size() > 0) {
				data = data.substring(0, data.length() - 1);
			}
			data = data + "]";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 
	 *查询所有出差记录
	 */
	public String selectHistroyEvection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String userid = request.getParameter("userId");
			String flowId = request.getParameter("flowId");
			int userId = Integer.parseInt(userid);
			YHAttendEvection evection = new YHAttendEvection();
			YHAttendEvectionLogic yhael = new YHAttendEvectionLogic();

			stmt = dbConn.createStatement();

			SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cDay1 = Calendar.getInstance();

			String sql = "select SAL_YEAR,SAL_MONTH from oa_sal_flow where SEQ_ID='" + flowId + "'";
			rs = stmt.executeQuery(sql);
			String currentdate = "";
			if (rs.next()) {
			  String str = rs.getString(1) + "-" + rs.getString(2) + "-";
				currentdate = str + "01";
			}

			cDay1.setTime(curTime.parse(currentdate));
			int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
			Date lastDate = curTime.parse(currentdate);
			lastDate.setDate(lastDay);

			String date = curTime.format(lastDate);

			String startDate = date.substring(0, 7) + "-01 00:00:00";
			String endDate = date + " 23:59:59";

			String whereStr = "";
			whereStr += " and " + YHDBUtility.getDateFilter("EVECTION_DATE1", startDate, ">=") + " and "
					+ YHDBUtility.getDateFilter("EVECTION_DATE1", endDate, "<=");

			String data = "[";
			String[] str = { "USER_ID='" + userId + "' and ALLOW='1' " + whereStr + " order by EVECTION_DATE1 desc" };

			List<YHAttendEvection> evectionList = yhael.selectEvection(dbConn, str);
			for (int i = 0; i < evectionList.size(); i++) {
				YHPersonLogic tpl = new YHPersonLogic();
				evection = evectionList.get(i);
				String leaderName = "";
				leaderName = tpl.getNameBySeqIdStr(evection.getLeaderId(), dbConn);
				if (leaderName != null && !leaderName.equals("")) {
					leaderName = YHUtility.encodeSpecial(leaderName);
				}
				data = data + YHFOM.toJson(evectionList.get(i)).toString().substring(0, YHFOM.toJson(evectionList.get(i)).toString().length() - 1)
						+ ",leaderName:\"" + leaderName + "\"},";
			}
			if (evectionList.size() > 0) {
				data = data.substring(0, data.length() - 1);
			}
			data = data + "]";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 查询加班申请（已批的）
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectHistoryOvertime(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String flowId = request.getParameter("flowId");
			String userid = request.getParameter("userId");
			int userId = Integer.parseInt(userid);

			YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
			List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
			YHPersonLogic tpl = new YHPersonLogic();

			String sql = "";
			stmt = dbConn.createStatement();

			SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cDay1 = Calendar.getInstance();

			sql = "select SAL_YEAR,SAL_MONTH from oa_sal_flow where SEQ_ID='" + flowId + "'";
			rs = stmt.executeQuery(sql);
			String currentdate = "";
			if (rs.next()) {
			  String str = rs.getString(1) + "-" + rs.getString(2) + "-";
				currentdate = str + "01";
			}

			cDay1.setTime(curTime.parse(currentdate));
			int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
			Date lastDate = curTime.parse(currentdate);
			lastDate.setDate(lastDay);

			String date = curTime.format(lastDate);

			String startDate = date.substring(0, 7) + "-01 00:00:00";
			String endDate = date + " 23:59:59";

			String whereStr = "";
			whereStr += " and " + YHDBUtility.getDateFilter("BEGIN_TIME", startDate, ">=") + " and "
					+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");

			String[] str = { "USER_ID='" + userId + "' and STATUS='1' " + whereStr };
			// String[] str = {"USER_ID = '" + userId + "'" , "STATUS = '1'" };
			overList = logic.selectOvertime(dbConn, str);
			String data = "[";
			for (int i = 0; i < overList.size(); i++) {
				YHOvertimeRecord overtime = overList.get(i);
				String leaderName = "";
				leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId(), dbConn);
				if (leaderName != null && !leaderName.equals("")) {
					leaderName = YHUtility.encodeSpecial(leaderName);
				}
				data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length() - 1) + ",leaderName:\"" + leaderName
						+ "\"},";
			}
			if (overList.size() > 0) {
				data = data.substring(0, data.length() - 1);
			}
			data = data + "]";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 *查询所有(分页)通用列表显示数据--查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String flowTitle = request.getParameter("flowTitle");
			String rankman = request.getParameter("rankman");
			String groupId = request.getParameter("groupId");
			String participant = request.getParameter("participant");
			String beginDate = request.getParameter("beginDate");
			String beginDate1 = request.getParameter("beginDate1");
			String endDate = request.getParameter("endDate");
			String endDate1 = request.getParameter("endDate1");
			String cd = request.getParameter("cd");

			YHScoreFlow flow = new YHScoreFlow();
			flow.setFlowTitle(flowTitle);
			flow.setParticipant(participant);
			flow.setRankman(rankman);
			if (!YHUtility.isNullorEmpty(groupId)) {
				flow.setGroupId(Integer.parseInt(groupId));
			}
			if (!YHUtility.isNullorEmpty(beginDate)) {
				flow.setBeginDate(YHUtility.parseDate(beginDate));
			}
			if (!YHUtility.isNullorEmpty(endDate)) {
				flow.setEndDate(YHUtility.parseDate(endDate));
			}
			String data = this.logic.selectList(dbConn, request.getParameterMap(), flow, beginDate1, endDate1, cd, participant);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
			pw.close();
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
	public String impReportInfoByCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			StringBuffer buffer = new StringBuffer("[");
			Map<Object, Object> returnMap = this.logic.impReportInfoByCsvLogic(dbConn, fileForm, person, buffer);
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
		return "/subsys/oa/hr/salary/report/run/import.jsp";
	}
	
	 /**
   * 查询在一段时间内得到考勤登记信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserDutyInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     
      String userId = request.getParameter("userId");//得到指定用户的ID
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String days = request.getParameter("days");//得到指定的所有日期

      String data=this.logic.getUserDutyInfoLogic(dbConn,request,response,userId,days);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询日志
   * */
  public String searchDiarySelf(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
     // YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     // int userId = person.getSeqId();
     // YHDiaryLogic dl = new YHDiaryLogic();
      String data =this.logic.toSearchData(dbConn,request.getParameterMap(),userId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
      pw.close();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}
