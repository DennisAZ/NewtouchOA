package yh.subsys.oa.hr.setting.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.hr.setting.logic.YHHrSetOtherLogic;

public class YHHrSetOtherAct {
	public static final String attachmentFolder = "hr";
	private YHHrSetOtherLogic logic = new YHHrSetOtherLogic();

	/**
	 * 获取是否允许人力资源管理员设置OA登录权限值(新建)

	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrSetUserLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getHrSetUserLogin(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取休年龄默认值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrRetireAge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getHrRetireAge(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 设置值
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setOtherValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String yesOther = request.getParameter("yesOther");
		String manAge = request.getParameter("manAge");
		String womenAge = request.getParameter("womenAge");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			 this.logic.setOtherValueLogic(dbConn,YHUtility.null2Empty(yesOther),manAge,womenAge);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 获取是否允许人力资源管理员设置OA登录权限值(编辑)
	 * 2011-4-14
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrSetUserLogin2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = YHUtility.null2Empty(request.getParameter("seqId"));
		String treeFlag = YHUtility.null2Empty(request.getParameter("treeFlag"));
		int seqId = 0;
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int counter = 0;
			if (YHUtility.isNumber(seqIdStr) && !"1".equals(treeFlag)) { //人事档案编辑
				seqId = Integer.parseInt(seqIdStr);
				counter = this.logic.getPersongCountLogic1(dbConn,seqId);
			} else {//如是左侧人员树传来值
				counter = this.logic.getPersongCountLogic2(dbConn,seqIdStr);
			}
			String isLogin = this.logic.getHrSetUserLogin(dbConn);
			String data = "{isLogin:\"" + isLogin + "\",counter:\"" + counter + "\" }";
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data );
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	

}
