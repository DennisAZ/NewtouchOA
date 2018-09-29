package yh.subsys.oa.hr.recruit.recruitment.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoLogic;
import yh.subsys.oa.hr.recruit.filter.data.YHHrRecruitFilter;
import yh.subsys.oa.hr.recruit.recruitment.data.YHHrRecruitRecruitment;
import yh.subsys.oa.hr.recruit.recruitment.logic.YHHrRecruitRecruitmentLogic;

public class YHHrRecruitRecruitmentAct {
	private YHHrRecruitRecruitmentLogic logic = new YHHrRecruitRecruitmentLogic();
	public static final String attachmentFolder = "hr";
	public static final String headPicFolder = "hrms_pic";// 头像图片>>D:\MYOA\webroot\attachment\hrms_pic
	/**
	 *招聘录用信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrRecruitListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getHrRecruitJsonLogic(dbConn, request.getParameterMap(), person);
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
	 * 所属部门下拉框
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
			YHDeptLogic deptLogic = new YHDeptLogic();
			String data = "";
			data = deptLogic.getDeptTreeJson(0, dbConn);
			if (YHUtility.isNullorEmpty(data)) {
				data = "[]";
			}
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
	 * 新建招聘录用信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addHrRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planName = request.getParameter("planName");
		String planNo = request.getParameter("planNo");
		String applyerName = request.getParameter("applyerName");
		String expertIdStr = request.getParameter("expertId");
		String jobStatus = request.getParameter("jobStatus");
		String oaName = request.getParameter("oaName");
		String assessingOfficer = request.getParameter("assessingOfficer");
		String assPassTime = request.getParameter("assPassTime");
		String department = request.getParameter("department");
		String type = request.getParameter("type");
		String administrationLevel = request.getParameter("administrationLevel");
		String jobPosition = request.getParameter("jobPosition");
		String presentPosition = request.getParameter("presentPosition");
		String onBoardingTime = request.getParameter("onBoardingTime");
		String startingSalaryTime = request.getParameter("startingSalaryTime");
		String remark = request.getParameter("remark");

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("planName", planName);
		map.put("planNo", planNo);
		map.put("applyerName", applyerName);
		map.put("expertId", expertIdStr);
		map.put("jobStatus", jobStatus);
		map.put("oaName", oaName);
		map.put("assessingOfficer", assessingOfficer);
		map.put("assPassTime", assPassTime);
		map.put("department", department);
		map.put("type", type);
		map.put("administrationLevel", administrationLevel);
		map.put("jobPosition", jobPosition);
		map.put("presentPosition", presentPosition);
		map.put("onBoardingTime", onBoardingTime);
		map.put("startingSalaryTime", startingSalaryTime);
		map.put("remark", remark);

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			int count = this.logic.addHrRecruitInfoLogic(dbConn, person, map);
			if (count > 0) {
				count = 1;
			}
			String data = "{isHave:\"" + count + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "新建成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 编辑招聘录用信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateHrRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		String planNo = request.getParameter("planNo");
		String applyerName = request.getParameter("applyerName");
		String jobStatus = request.getParameter("jobStatus");
		String oaName = request.getParameter("oaName");
		String assessingOfficer = request.getParameter("assessingOfficer");
		String assPassTime = request.getParameter("assPassTime");
		String department = request.getParameter("department");
		String type = request.getParameter("type");
		String administrationLevel = request.getParameter("administrationLevel");
		String jobPosition = request.getParameter("jobPosition");
		String presentPosition = request.getParameter("presentPosition");
		String onBoardingTime = request.getParameter("onBoardingTime");
		String startingSalaryTime = request.getParameter("startingSalaryTime");
		String remark = request.getParameter("remark");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("seqId", seqId);
		map.put("planNo", planNo);
		map.put("applyerName", applyerName);
		map.put("jobStatus", jobStatus);
		map.put("oaName", oaName);
		map.put("assessingOfficer", assessingOfficer);
		map.put("assPassTime", assPassTime);
		map.put("department", department);
		map.put("type", type);
		map.put("administrationLevel", administrationLevel);
		map.put("jobPosition", jobPosition);
		map.put("presentPosition", presentPosition);
		map.put("onBoardingTime", onBoardingTime);
		map.put("startingSalaryTime", startingSalaryTime);
		map.put("remark", remark);
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			this.logic.updateHrRecruitInfoLogic(dbConn, person, map);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "新建成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取人员名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String userName = this.logic.getUserNameLogic(dbConn, seqIdStr);
			String data = "{userName:\"" + YHUtility.encodeSpecial(userName) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功获取数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除招聘录用信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteHrRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deleteHrRecruitLogic(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 根据userId获取staffInfo的数量
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrStaffInfoCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oaName = request.getParameter("oaName");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int count = this.logic.getHrStaffInfoCount(dbConn, oaName);
			if (count>0) {
				count=1;
			}
			String data = "{counter:\"" + count + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功获取数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 聘录用信息详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrRecruitDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		YHORM orm = new YHORM();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHHrRecruitRecruitment recruitment = (YHHrRecruitRecruitment) orm.loadObjSingle(dbConn, YHHrRecruitRecruitment.class, seqId);
			if (recruitment == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(recruitment);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 根据人才库seqId获取聘录用信息详情	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrRecruitInfoByExpertId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String expertIdStr = request.getParameter("expertId");
		int expertId = 0;
		if (YHUtility.isNumber(expertIdStr)) {
			expertId = Integer.parseInt(expertIdStr);
		}
		Connection dbConn = null;
		YHORM orm = new YHORM();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			map.put("EXPERT_ID", expertId);
			YHHrRecruitRecruitment recruitment = (YHHrRecruitRecruitment) orm.loadObjSingle(dbConn, YHHrRecruitRecruitment.class, map);
			if (recruitment == null) {
//				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//				request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(recruitment);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
   *招聘录用查询列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryRecruitmentListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("PLAN_NO", YHUtility.null2Empty(request.getParameter("PLAN_NO")));
    map.put("APPLYER_NAME", YHUtility.null2Empty(request.getParameter("APPLYER_NAME")));
    map.put("OA_NAME", YHUtility.null2Empty(request.getParameter("OA_NAME")));
    map.put("JOB_STATUS", YHUtility.null2Empty(request.getParameter("JOB_STATUS")));
    map.put("ASSESSING_OFFICER", YHUtility.null2Empty(request.getParameter("ASSESSING_OFFICER")));
    map.put("ASS_PASS_TIME_START", YHUtility.null2Empty(request.getParameter("ASS_PASS_TIME_START")));
    map.put("ASS_PASS_TIME_END", YHUtility.null2Empty(request.getParameter("ASS_PASS_TIME_END")));
    map.put("department", YHUtility.null2Empty(request.getParameter("department")));
    map.put("TYPE", YHUtility.null2Empty(request.getParameter("TYPE")));
    map.put("ADMINISTRATION_LEVEL", YHUtility.null2Empty(request.getParameter("ADMINISTRATION_LEVEL")));
    
    map.put("JOB_POSITION", YHUtility.null2Empty(request.getParameter("JOB_POSITION")));
    map.put("PRESENT_POSITION", YHUtility.null2Empty(request.getParameter("PRESENT_POSITION")));
    map.put("REMARK", YHUtility.null2Empty(request.getParameter("REMARK")));
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.queryRecruitmentListJsonLogic(dbConn, request.getParameterMap(), person,map);
    
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
	 * 获取是否允许人力资源管理员设置OA登录权限值
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
			YHHrStaffInfoLogic staffInfoLogic = new YHHrStaffInfoLogic();
			maxSeqId = staffInfoLogic.setNewHrStaffInfoValueLogic(dbConn, fileForm, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/recruit/recruitment/newStaffWarn.jsp");
		return null;
	}
	
	
	/**
	 * 招聘筛选-模糊查找
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getHrFilterList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String keyWord = request.getParameter("condition");
		keyWord = YHUtility.decodeURL(keyWord); //解码
	if(YHUtility.isNullorEmpty(keyWord)){
		keyWord = "";
	}
	String userId = request.getParameter("userId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			StringBuffer sb = new StringBuffer("[");
			YHPerson person = null;
			if(!YHUtility.isNullorEmpty(userId)){
				person = new YHPerson();
				person.setSeqId(Integer.parseInt(userId));//从页面中传过来的用户信息
			}else{
				person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
			}
			List<YHHrRecruitFilter> filters = this.logic.getHrFilterListLogic(dbConn,person,keyWord);
			boolean isHave = false;
			if (filters!=null && filters.size()>0) {
				for(YHHrRecruitFilter filter:filters){
					sb.append("{");
					sb.append("expertId:\"" +  filter.getSeqId() + "\"");
					sb.append(",employeeName:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(filter.getEmployeeName()))+ "\"");
					sb.append("},");
					isHave = true;
				}
				if (isHave) {
					sb.deleteCharAt(sb.length()-1);
				}
				sb.append("]");
			}else {
				sb.append("]");
			}
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
}
