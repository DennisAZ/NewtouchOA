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
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.setting.data.YHHrManager;
import yh.subsys.oa.hr.setting.logic.YHHrManagerLogic;

public class YHHrManagerAct {
	private YHHrManagerLogic logic = new YHHrManagerLogic();

	public String setBatchValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String operation = request.getParameter("operation");
		String userStr = request.getParameter("user");
		String deptStr = request.getParameter("deptStr");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.setBatchValueLogic(dbConn, YHUtility.null2Empty(operation), YHUtility.null2Empty(userStr), deptStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 根据用户的管理权限得到所有部门
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectDeptToAttendance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String userPriv = user.getUserPriv();// 角色
			String postpriv = user.getPostPriv();// 管理范围
			String postDept = user.getPostDept();// 管理范围指定部门
			int userDeptId = user.getDeptId();
			YHDeptLogic deptLogic = new YHDeptLogic();
			String data = "";
			if (userPriv != null && userPriv.equals("1") && user.getUserId().trim().equals("admin")) {// 假如是系统管理员的都快要看得到.而且是ADMIN用户
				data = deptLogic.getDeptTreeJson(0, dbConn);
			} else {
				if (postpriv.equals("0")) {
					String[] postDeptArray = { String.valueOf(userDeptId) };
					data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray) + "]";
				}
				if (postpriv.equals("1")) {
					data = deptLogic.getDeptTreeJson(0, dbConn);
				}
				if (postpriv.equals("2")) {
					if (postDept == null || postDept.equals("")) {
						data = "[]";
					} else {
						String[] postDeptArray = postDept.split(",");
						data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray) + "]";
					}
				}
			}
			if (data.equals("")) {
				data = "[]";
			}
			data = data.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r\n", "").replace("\n", "").replace("\r", "");
			// System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId) + "," + postpriv);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 获取人力资源管理员名称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrManager(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deptId = request.getParameter("deptId");
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getHrManagerLogic(dbConn,deptId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 获取人力资源管理员Id串
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrManagerIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deptId = request.getParameter("deptId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getHrManagerIdStrLogic(dbConn,deptId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 编辑人力资源管理员Id串
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String editHrManager(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deptId = request.getParameter("deptId");
		String deptHrManager = request.getParameter("deptHrManager");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.editHrManagerLogic(dbConn,deptId,YHUtility.null2Empty(deptHrManager));
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	
	
	

}
