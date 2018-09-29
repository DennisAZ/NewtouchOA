package yh.subsys.oa.hr.manage.staffInfo.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.orgselect.logic.YHDeptSelectLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoLogic;

public class YHHrStaffInfoAct {
	private YHHrStaffInfoLogic logic = new YHHrStaffInfoLogic();
	public static final String attachmentFolder = "hr";
	public static final String headPicFolder = "hrms_pic";// 头像图片>>D:\MYOA\webroot\attachment\hrms_pic

	/**
	 * 新建人事档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addHrStaffInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		int maxSeqId = 0;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			maxSeqId = this.logic.setNewHrStaffInfoValueLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffInfo/newWarn.jsp?maxSeqId=" + maxSeqId);
		return null;
	}

	/**
	 * 获取角色名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getUserPriv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getUserPrivLogic(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 判断名称是否已存在（Staff_Info表）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = YHUtility.null2Empty(request.getParameter("userId"));
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.checkUserLogic(dbConn, userId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 *人事档案信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getStaffInfoListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		String deptIdStr = request.getParameter("deptId");
		int deptId = 0;
		if (YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getStaffInfoListJsonLogic(dbConn, request.getParameterMap(), person, deptId);
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
	 *根据person表的userId获取信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = YHUtility.null2Empty(request.getParameter("seqId"));
		String treeFlag = YHUtility.null2Empty(request.getParameter("treeFlag"));
		int seqId = 0;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if ("1".equals(treeFlag)) {
				YHHrStaffInfo dbHrStaffInfo = this.logic.getStaffInfoByIdLogic(dbConn, seqIdStr);
				if (dbHrStaffInfo != null ) {
					seqId = dbHrStaffInfo.getSeqId();
				}
			}
			String data = "";
			if (YHUtility.isNumber(seqIdStr) && !"1".equals(treeFlag)) {
				seqId = Integer.parseInt(seqIdStr);
				data = this.logic.getPersonInfoLogic(dbConn, seqId);
			} else {
				data = this.logic.getPersonInfoByUserIdLogic(dbConn, seqIdStr);
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 *根据userId获取档案信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getStaffInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		String seqIdStr = YHUtility.null2Empty(request.getParameter("seqId"));
		String treeFlag = YHUtility.null2Empty(request.getParameter("treeFlag"));
		int seqId = 0;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if ("1".equals(treeFlag)) {
				YHHrStaffInfo dbHrStaffInfo = this.logic.getStaffInfoByIdLogic(dbConn, seqIdStr);
				if (dbHrStaffInfo != null) {
					seqId = dbHrStaffInfo.getSeqId();
				}
			}
			if (YHUtility.isNumber(seqIdStr) && !"1".equalsIgnoreCase(treeFlag)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			YHHrStaffInfo staffInfo = this.logic.getObjSingle(dbConn, seqId);
			if (staffInfo == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "0");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(staffInfo);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "1");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 编辑人事档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateHrStaffInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		int updateSeqId = 0;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			dbConn = requestDbConn.getSysDbConn();
			updateSeqId = this.logic.updateHrStaffInfoValueLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staffInfo/newWarn.jsp?updateFlag=1&maxSeqId=" + updateSeqId);
		return null;
	}

	/**
	 * 删除人事档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delHrStaffInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String deptIdStr = request.getParameter("deptId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if (!YHUtility.isNullorEmpty(deptIdStr)) {
				this.logic.delHrStaffInfoByDeptIdLogic(dbConn, deptIdStr);
			} else if (!YHUtility.isNullorEmpty(seqIdStr)) {
				this.logic.delHrStaffInfoLogic(dbConn, seqIdStr);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 档案详情信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDetailHrStaffInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHHrStaffInfo staffInfo = this.logic.getObjSingle(dbConn, seqId);
			if (staffInfo == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "0");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(staffInfo);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 部门名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDeptIdName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deptIdStr = request.getParameter("deptId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String deptName = this.logic.getDeptNameLogic(dbConn, deptIdStr);
			String data = "{deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"}";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取禁止登录信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNotLoginInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String treeFlag = YHUtility.null2Empty(request.getParameter("treeFlag"));
		int seqId = 0;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			if ("1".equals(treeFlag)) {
				YHHrStaffInfo dbHrStaffInfo = this.logic.getStaffInfoByIdLogic(dbConn, seqIdStr);
				if (dbHrStaffInfo != null) {
					seqId = dbHrStaffInfo.getSeqId();
				}
			}
			String notLoginStr = "";
			if (YHUtility.isNumber(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
				notLoginStr = this.logic.getNotLoginInfoLogic(dbConn, seqIdStr);
			} else {
				notLoginStr = this.logic.getNotLoginInfoLogic(dbConn, String.valueOf(seqId));
			}
			// String notLoginStr = this.logic.getNotLoginInfoLogic(dbConn,seqIdStr);
			String data = "{notLogin:\"" + YHUtility.encodeSpecial(notLoginStr) + "\" }";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 人事档案查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryStaffInfoListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("userId", YHUtility.null2Empty(request.getParameter("userId")));
			map.put("staffName", YHUtility.null2Empty(request.getParameter("staffName")));
			map.put("staffEName", YHUtility.null2Empty(request.getParameter("staffEName")));
			map.put("staffNo", YHUtility.null2Empty(request.getParameter("staffNo")));
			map.put("workNo", YHUtility.null2Empty(request.getParameter("workNo")));
			map.put("deptId", YHUtility.null2Empty(request.getParameter("deptId")));
			map.put("staffCardNo", YHUtility.null2Empty(request.getParameter("staffCardNo")));
			map.put("staffBirth", YHUtility.null2Empty(request.getParameter("staffBirth")));
			map.put("staffSex", YHUtility.null2Empty(request.getParameter("staffSex")));
			map.put("staffHighestSchool", YHUtility.null2Empty(request.getParameter("staffHighestSchool")));

			map.put("staffHighestDegree", YHUtility.null2Empty(request.getParameter("staffHighestDegree")));
			map.put("staffMajor", YHUtility.null2Empty(request.getParameter("staffMajor")));
			map.put("staffNationality", YHUtility.null2Empty(request.getParameter("staffNationality")));
			map.put("staffNativePlace", YHUtility.null2Empty(request.getParameter("staffNativePlace")));
			map.put("staffMaritalStatus", YHUtility.null2Empty(request.getParameter("staffMaritalStatus")));
			map.put("staffOccupation", YHUtility.null2Empty(request.getParameter("staffOccupation")));
			map.put("staffPoliticalStatus", YHUtility.null2Empty(request.getParameter("staffPoliticalStatus")));
			map.put("workType", YHUtility.null2Empty(request.getParameter("workType")));
			map.put("datesEmployed", YHUtility.null2Empty(request.getParameter("datesEmployed")));
			map.put("joinPartyTime", YHUtility.null2Empty(request.getParameter("joinPartyTime")));

			map.put("staffPhone", YHUtility.null2Empty(request.getParameter("staffPhone")));
			map.put("administrationLevel", YHUtility.null2Empty(request.getParameter("administrationLevel")));
			map.put("staffMobile", YHUtility.null2Empty(request.getParameter("staffMobile")));
			map.put("staffEmail", YHUtility.null2Empty(request.getParameter("staffEmail")));
			map.put("staffLittleSmart", YHUtility.null2Empty(request.getParameter("staffLittleSmart")));
			map.put("staffMsn", YHUtility.null2Empty(request.getParameter("staffMsn")));
			map.put("staffQq", YHUtility.null2Empty(request.getParameter("staffQq")));
			map.put("homeAddress", YHUtility.null2Empty(request.getParameter("homeAddress")));
			map.put("jobPosition", YHUtility.null2Empty(request.getParameter("jobPosition")));
			map.put("presentPosition", YHUtility.null2Empty(request.getParameter("presentPosition")));

			map.put("jobBeginning", YHUtility.null2Empty(request.getParameter("jobBeginning")));
			map.put("graduationDate", YHUtility.null2Empty(request.getParameter("graduationDate")));
			map.put("jobAge", YHUtility.null2Empty(request.getParameter("jobAge")));
			map.put("workAge", YHUtility.null2Empty(request.getParameter("workAge")));
			map.put("staffHealth", YHUtility.null2Empty(request.getParameter("staffHealth")));
			map.put("staffDomicilePlace", YHUtility.null2Empty(request.getParameter("staffDomicilePlace")));
			map.put("graduationSchool", YHUtility.null2Empty(request.getParameter("graduationSchool")));
			map.put("computerLevel", YHUtility.null2Empty(request.getParameter("computerLevel")));
			map.put("beginSalsryTime", YHUtility.null2Empty(request.getParameter("beginSalsryTime")));
			map.put("foreignLanguage1", YHUtility.null2Empty(request.getParameter("foreignLanguage1")));

			map.put("foreignLanguage2", YHUtility.null2Empty(request.getParameter("foreignLanguage2")));
			map.put("foreignLanguage3", YHUtility.null2Empty(request.getParameter("foreignLanguage3")));
			map.put("foreignLevel1", YHUtility.null2Empty(request.getParameter("foreignLevel1")));
			map.put("foreignLevel2", YHUtility.null2Empty(request.getParameter("foreignLevel2")));
			map.put("foreignLevel3", YHUtility.null2Empty(request.getParameter("foreignLevel3")));
			map.put("staffSkills", YHUtility.null2Empty(request.getParameter("staffSkills")));
			map.put("insureNum", YHUtility.null2Empty(request.getParameter("insureNum")));

			String data = this.logic.queryStaffInfoListLogic(dbConn, request.getParameterMap(), person, map);
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
	 * 获取年龄
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getStaffAge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String staffAge = this.logic.getStaffAgeLogic(dbConn, seqId);
			String data = "{staffAge:\"" + YHUtility.encodeSpecial(staffAge) + "\"}";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除头像图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deletePhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deletePhotoLogic(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取部门下的离职人员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String orgId = "organizationNodeId";
		String moduleId = request.getParameter("MODULE_ID");
		String privNoFlagStr = request.getParameter("privNoFlag");
		int privNoFlag = 2;
		if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
			privNoFlag = Integer.parseInt(privNoFlagStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			StringBuffer record = new StringBuffer();
			StringBuffer sb = new StringBuffer();
			ArrayList<YHOrganization> org = new ArrayList();
			YHDeptLogic dls = new YHDeptLogic();
			org = dls.getOrganization(dbConn);
			if ((idStr == null || "".equals(idStr) || "0".equals(idStr)) && !orgId.equals(idStr)) {
				for (YHOrganization orgs : org) {
					String name = orgs.getUnitName();
					String imgAddress = "/yh/core/styles/style1/img/dtree/system.gif";
					record.append("{");
					record.append("nodeId:\"" + orgId + "\"");
					record.append(",name:\"" + name + "\"");
					record.append(",isHaveChild:" + 1 + "");
					record.append(",imgAddress:\"" + imgAddress + "\"");
					record.append(",title:\"" + name + "\"");
					record.append("},");
				}
			} else {
				if (orgId.equals(idStr)) {
					idStr = "0";
				}
				String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT = " + idStr + " order by DEPT_NO ASC, DEPT_NAME ASC";
				ArrayList<YHDepartment> depts = new ArrayList<YHDepartment>();
				ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
				Statement stm4 = null;
				ResultSet rs4 = null;
				try {
					stm4 = dbConn.createStatement();
					rs4 = stm4.executeQuery(query);
					while (rs4.next()) {
						YHDepartment dept = new YHDepartment();
						dept.setSeqId(rs4.getInt("SEQ_ID"));
						dept.setDeptName(rs4.getString("DEPT_NAME"));
						depts.add(dept);
					}
				} catch (Exception ex) {
					throw ex;
				} finally {
					YHDBUtility.close(stm4, rs4, null);
				}
				YHDeptSelectLogic dsl = new YHDeptSelectLogic();
				boolean hasModule = false;
				if (moduleId != null && !"".equals(moduleId)) {
					hasModule = true;
				}
				YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
				if (Integer.parseInt(idStr) != 0) {
					persons = this.logic.getDeptUser2(dbConn, Integer.parseInt(idStr) );
				}
				String allDef = "";
				YHMyPriv mp = new YHMyPriv();
				mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
				allDef = dsl.getDefUserDept(dbConn, mp, person.getDeptId());
				String contextPath = request.getContextPath();
				for (YHPerson per : persons) {
					int seqId = per.getSeqId();
					if (!YHPrivUtil.isUserPriv(dbConn, seqId, mp, person)) {
						continue;
					}
					int deptId = per.getDeptId();
					String deptName = osl.getDeptName(dbConn, deptId);
					String email = per.getEmail();
					int roleId = Integer.parseInt(per.getUserPriv());
					String roleName = osl.getRoleName(dbConn, roleId);
					String telNoDept = per.getTelNoDept();
					if (YHUtility.isNullorEmpty(email)) {
						email = "";
					}
					if (YHUtility.isNullorEmpty(telNoDept)) {
						telNoDept = "";
					}
					String oicq = per.getOicq();
					if (YHUtility.isNullorEmpty(oicq)) {
						oicq = "";
					}
					if (!"".equals(record.toString())) {
						record.append(",");
					}
					String myStatus = per.getMyStatus();
					String myState = "";
					if (YHUtility.isNullorEmpty(myStatus)) {
						myState = "";
					} else {
						myState = "\\n人员状态:" + myStatus + "";
					}
					String userId = per.getUserId(); // cc 20100617
					record.append("{");
					record.append("nodeId:\"r" + seqId + "\"");
					record.append(",name:\"" + YHUtility.encodeSpecial(per.getUserName()) + "\"");
					record.append(",isHaveChild:" + 0);
					record.append(",extData:\"" + userId + "\"");
					record.append(",imgAddress:\"" + request.getContextPath() + "/core/styles/style1/img/dtree/0-1.gif\"");
					record.append(",title:\"部门:" + YHUtility.encodeSpecial(deptName) + "\\n角色:" + YHUtility.encodeSpecial(roleName) + "\\n工作电话:" + telNoDept
							+ "\\nemail:" + YHUtility.encodeSpecial(email) + "\\nQQ:" + oicq + myState + "\"");
					record.append("}");
				}
				for (YHDepartment d : depts) {
					int nodeId = d.getSeqId();
					String name = d.getDeptName();
					int isHaveChild = isHaveChild(dbConn, d.getSeqId());
					boolean extData = false;
					if (YHPrivUtil.isDeptPriv(dbConn, nodeId, mp, person)) {
						extData = true;
					}
					String imgAddress = contextPath + "/core/styles/style1/img/dtree/node_dept.gif";
					if (!"".equals(record.toString())) {
						record.append(",");
					}
					record.append("{");
					record.append("nodeId:\"" + nodeId + "\"");
					record.append(",name:\"" + YHUtility.encodeSpecial(name) + "\"");
					record.append(",isHaveChild:" + isHaveChild + "");
					record.append(",title:\"" + YHUtility.encodeSpecial(name) + "\"");
					record.append(",extData:" + extData);
					record.append(",imgAddress:\"" + YHUtility.encodeSpecial(imgAddress) + "\"");
					record.append("}");
				}
			}
			sb.append("[").append(record).append("]");
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	 /**
   * 判断是否有子部门
   * 
   * @param dbConn
   * @param response
   * @param id
   * @return
   * @throws Exception
   */
  // 判断是否有子部门和本部门是否有人:1为有子部门或者本部门有人0为都不是
  public int isHaveChild(Connection dbConn, int id) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("DEPT_PARENT", id);
    // 判断是否有子部门
    YHOrgSelectLogic osl = new YHOrgSelectLogic();
    ArrayList<YHDepartment> list = osl.getDepartmentList(dbConn, id);

    // List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class,
    // map);
    // 判断本部门是否有人
    // System.out.println(list.size()+"=FGHJT");
    String[] str = { "DEPT_ID =" + id };
    String whereStr = "DEPT_ID =" + id;
    // List<YHPerson> personList = orm.loadListSingle(dbConn,
    // YHPerson.class,str);
    List<YHPerson> personList = osl.getPersonList(dbConn, whereStr);
    if (list.size() > 0 || personList.size() > 0) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * 浮动菜单文件删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    String attachId = request.getParameter("delAttachId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      boolean updateFlag = this.logic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
      int returnFlag = 0;
      if (updateFlag) {
        returnFlag = 1;
      }
      String data = "{updateFlag:\"" + returnFlag + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
