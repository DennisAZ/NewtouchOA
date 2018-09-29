package yh.core.funcs.filefolder.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.filefolder.logic.YHSignReaderLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSignReaderAct {
	private static Logger log = Logger.getLogger(YHSignReaderAct.class);

	public String getSignReader(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortIdStr = request.getParameter("sortId");
		String contentIdStr = request.getParameter("contentId");
		int sortId = 0;
		if (sortIdStr != null && !"".equals(sortIdStr)) {
			sortId = Integer.parseInt(sortIdStr);
		}
		int contentId = 0;
		if (contentIdStr != null && !"".equals(contentIdStr)) {
			contentId = Integer.parseInt(contentIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHSignReaderLogic logic = new YHSignReaderLogic();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = logic.getSignReader(sortId, contentId,person, dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	public String delSignReader(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentIdStr = request.getParameter("contentId");
		int contentId = 0;
		if (contentIdStr != null && !"".equals(contentIdStr)) {
			contentId = Integer.parseInt(contentIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			YHSignReaderLogic logic = new YHSignReaderLogic();
			logic.delSignReader(dbConn, contentId, user);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "清空成功!");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
}
