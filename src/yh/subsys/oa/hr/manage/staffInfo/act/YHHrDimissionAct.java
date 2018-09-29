package yh.subsys.oa.hr.manage.staffInfo.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHHrDimissionLogic;

public class YHHrDimissionAct {
private YHHrDimissionLogic logic = new YHHrDimissionLogic();
	
/**
 *离职人员信息列表
 * 
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getDimissionListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Connection dbConn = null;
	try {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		String data = this.logic.getDimissionListLogic(dbConn, request.getParameterMap(), person);
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
 *离职原因
 * 
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getDimissionCause(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String seqId = request.getParameter("userId");
	if (YHUtility.isNullorEmpty(seqId)) {
		seqId = "0";
	}
	Connection dbConn = null;
	try {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		String causeStr = this.logic.getDimissionCauseLogic(dbConn,seqId);
		String data = "{causeStr:\"" + YHUtility.encodeSpecial(causeStr) + "\"}";
		
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		request.setAttribute(YHActionKeys.RET_DATA, data);
	} catch (Exception ex) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		throw ex;
	}
	return "/core/inc/rtjson.jsp";
}
/**
 * 末记录所属部门
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getNotRecordDeptList(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Connection dbConn = null;
	try {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		dbConn = requestDbConn.getSysDbConn();
		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		String data = this.logic.getNotRecordDeptListLogic(dbConn, person);
		
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		request.setAttribute(YHActionKeys.RET_DATA, data);
	} catch (Exception ex) {
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		throw ex;
	}
	return "/core/inc/rtjson.jsp";
}





	
}
