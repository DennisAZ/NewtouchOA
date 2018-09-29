package yh.subsys.oa.hr.manage.staffInfo.act;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHBatchUpdateStaffInfoLogic;

public class YHBatchUpdateStaffInfoAct {
	private YHBatchUpdateStaffInfoLogic logic = new YHBatchUpdateStaffInfoLogic();

	public String batchUpdateInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("staffName", YHUtility.null2Empty(request.getParameter("staffName")));
		map.put("leaveType", YHUtility.null2Empty(request.getParameter("leaveType")));
		map.put("workStatus", YHUtility.null2Empty(request.getParameter("workStatus")));
		map.put("mode", YHUtility.null2Empty(request.getParameter("mode")));
		map.put("selectitem", YHUtility.null2Empty(request.getParameter("selectitem")));
		map.put("tContext", YHUtility.null2Empty(request.getParameter("tContext")));
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.batchUpdateInfoLogic(dbConn, map);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

}
