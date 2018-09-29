package yh.subsys.oa.hr.manage.staffInfo.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;
import yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoQueryLogic;

public class YHHrStaffInfoQueryAct {
	private YHHrStaffInfoQueryLogic logic = new YHHrStaffInfoQueryLogic();
	
	/**
	 * 档案详情信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getStaffInfoByPersonId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String personIdStr = request.getParameter("seqId");
		int personId = 0;
		if (YHUtility.isNumber(personIdStr)) {
			personId = Integer.parseInt(personIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHHrStaffInfo staffInfo = this.logic.getStaffInfoByPersonId(dbConn, personId);
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
			map.put("staffDept", YHUtility.null2Empty(request.getParameter("staffDept")));
			map.put("staffName", YHUtility.null2Empty(request.getParameter("staffName")));
			map.put("staffEName", YHUtility.null2Empty(request.getParameter("staffEName")));
			map.put("workStatus", YHUtility.null2Empty(request.getParameter("workStatus")));
			map.put("staffNo", YHUtility.null2Empty(request.getParameter("staffNo")));
			map.put("workNo", YHUtility.null2Empty(request.getParameter("workNo")));
			map.put("staffSex", YHUtility.null2Empty(request.getParameter("staffSex")));
			map.put("staffCardNo", YHUtility.null2Empty(request.getParameter("staffCardNo")));
			map.put("birthdayMin", YHUtility.null2Empty(request.getParameter("birthdayMin")));
			map.put("birthdayMax", YHUtility.null2Empty(request.getParameter("birthdayMax")));
			
			
			map.put("ageMin", YHUtility.null2Empty(request.getParameter("ageMin")));
			map.put("ageMax", YHUtility.null2Empty(request.getParameter("ageMax")));
			map.put("staffNationality", YHUtility.null2Empty(request.getParameter("staffNationality")));
			map.put("staffNativePlace", YHUtility.null2Empty(request.getParameter("staffNativePlace")));
			map.put("workType", YHUtility.null2Empty(request.getParameter("workType")));
			map.put("staffDomicilePlace", YHUtility.null2Empty(request.getParameter("staffDomicilePlace")));
			map.put("staffMaritalStatus", YHUtility.null2Empty(request.getParameter("staffMaritalStatus")));
			map.put("staffHealth", YHUtility.null2Empty(request.getParameter("staffHealth")));
			map.put("staffPoliticalStatus", YHUtility.null2Empty(request.getParameter("staffPoliticalStatus")));
			map.put("administrationLevel", YHUtility.null2Empty(request.getParameter("administrationLevel")));
			
			map.put("staffOccupation", YHUtility.null2Empty(request.getParameter("staffOccupation")));
			map.put("computerLevel", YHUtility.null2Empty(request.getParameter("computerLevel")));
			map.put("staffHighestSchool", YHUtility.null2Empty(request.getParameter("staffHighestSchool")));
			map.put("staffHighestDegree", YHUtility.null2Empty(request.getParameter("staffHighestDegree")));
			map.put("staffMajor", YHUtility.null2Empty(request.getParameter("staffMajor")));
			map.put("graduationSchool", YHUtility.null2Empty(request.getParameter("graduationSchool")));
			map.put("jobPosition", YHUtility.null2Empty(request.getParameter("jobPosition")));
			map.put("presentPosition", YHUtility.null2Empty(request.getParameter("presentPosition")));
			map.put("graduationMin", YHUtility.null2Empty(request.getParameter("graduationMin")));
			map.put("graduationMax", YHUtility.null2Empty(request.getParameter("graduationMax")));
			
			map.put("joinPartyMin", YHUtility.null2Empty(request.getParameter("joinPartyMin")));
			map.put("joinPartyMax", YHUtility.null2Empty(request.getParameter("joinPartyMax")));
			map.put("beginningMin", YHUtility.null2Empty(request.getParameter("beginningMin")));
			map.put("beginningMax", YHUtility.null2Empty(request.getParameter("beginningMax")));
			map.put("employedMin", YHUtility.null2Empty(request.getParameter("employedMin")));
			map.put("employedMax", YHUtility.null2Empty(request.getParameter("employedMax")));
			map.put("workAgeMin", YHUtility.null2Empty(request.getParameter("workAgeMin")));
			map.put("workAgeMax", YHUtility.null2Empty(request.getParameter("workAgeMax")));
			map.put("jobAgeMin", YHUtility.null2Empty(request.getParameter("jobAgeMin")));
			map.put("jobAgeMax", YHUtility.null2Empty(request.getParameter("jobAgeMax")));
			
			map.put("leaveTypeMin", YHUtility.null2Empty(request.getParameter("leaveTypeMin")));
			map.put("leaveTypeMax", YHUtility.null2Empty(request.getParameter("leaveTypeMax")));
			map.put("foreignLanguage1", YHUtility.null2Empty(request.getParameter("foreignLanguage1")));
			map.put("foreignLanguage2", YHUtility.null2Empty(request.getParameter("foreignLanguage2")));
			map.put("foreignLanguage3", YHUtility.null2Empty(request.getParameter("foreignLanguage3")));
			map.put("foreignLevel1", YHUtility.null2Empty(request.getParameter("foreignLevel1")));
			map.put("foreignLevel2", YHUtility.null2Empty(request.getParameter("foreignLevel2")));
			map.put("foreignLevel3", YHUtility.null2Empty(request.getParameter("foreignLevel3")));
			
			String data = this.logic.queryStaffInfoListLogic(dbConn, request.getParameterMap(), person,map);
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
	 * 根据查询条件导出数据到CSV文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportToCSV(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("staffDept", YHUtility.null2Empty(request.getParameter("staffDept")));
		map.put("staffName", YHUtility.null2Empty(request.getParameter("staffName")));
		map.put("staffEName", YHUtility.null2Empty(request.getParameter("staffEName")));
		map.put("workStatus", YHUtility.null2Empty(request.getParameter("workStatus")));
		map.put("staffNo", YHUtility.null2Empty(request.getParameter("staffNo")));
		map.put("workNo", YHUtility.null2Empty(request.getParameter("workNo")));
		map.put("staffSex", YHUtility.null2Empty(request.getParameter("staffSex")));
		map.put("staffCardNo", YHUtility.null2Empty(request.getParameter("staffCardNo")));
		map.put("birthdayMin", YHUtility.null2Empty(request.getParameter("birthdayMin")));
		map.put("birthdayMax", YHUtility.null2Empty(request.getParameter("birthdayMax")));
		
		
		map.put("ageMin", YHUtility.null2Empty(request.getParameter("ageMin")));
		map.put("ageMax", YHUtility.null2Empty(request.getParameter("ageMax")));
		map.put("staffNationality", YHUtility.null2Empty(request.getParameter("staffNationality")));
		map.put("staffNativePlace", YHUtility.null2Empty(request.getParameter("staffNativePlace")));
		map.put("workType", YHUtility.null2Empty(request.getParameter("workType")));
		map.put("staffDomicilePlace", YHUtility.null2Empty(request.getParameter("staffDomicilePlace")));
		map.put("staffMaritalStatus", YHUtility.null2Empty(request.getParameter("staffMaritalStatus")));
		map.put("staffHealth", YHUtility.null2Empty(request.getParameter("staffHealth")));
		map.put("staffPoliticalStatus", YHUtility.null2Empty(request.getParameter("staffPoliticalStatus")));
		map.put("administrationLevel", YHUtility.null2Empty(request.getParameter("administrationLevel")));
		
		map.put("staffOccupation", YHUtility.null2Empty(request.getParameter("staffOccupation")));
		map.put("computerLevel", YHUtility.null2Empty(request.getParameter("computerLevel")));
		map.put("staffHighestSchool", YHUtility.null2Empty(request.getParameter("staffHighestSchool")));
		map.put("staffHighestDegree", YHUtility.null2Empty(request.getParameter("staffHighestDegree")));
		map.put("staffMajor", YHUtility.null2Empty(request.getParameter("staffMajor")));
		map.put("graduationSchool", YHUtility.null2Empty(request.getParameter("graduationSchool")));
		map.put("jobPosition", YHUtility.null2Empty(request.getParameter("jobPosition")));
		map.put("presentPosition", YHUtility.null2Empty(request.getParameter("presentPosition")));
		map.put("graduationMin", YHUtility.null2Empty(request.getParameter("graduationMin")));
		map.put("graduationMax", YHUtility.null2Empty(request.getParameter("graduationMax")));
		
		map.put("joinPartyMin", YHUtility.null2Empty(request.getParameter("joinPartyMin")));
		map.put("joinPartyMax", YHUtility.null2Empty(request.getParameter("joinPartyMax")));
		map.put("beginningMin", YHUtility.null2Empty(request.getParameter("beginningMin")));
		map.put("beginningMax", YHUtility.null2Empty(request.getParameter("beginningMax")));
		map.put("employedMin", YHUtility.null2Empty(request.getParameter("employedMin")));
		map.put("employedMax", YHUtility.null2Empty(request.getParameter("employedMax")));
		map.put("workAgeMin", YHUtility.null2Empty(request.getParameter("workAgeMin")));
		map.put("workAgeMax", YHUtility.null2Empty(request.getParameter("workAgeMax")));
		map.put("jobAgeMin", YHUtility.null2Empty(request.getParameter("jobAgeMin")));
		map.put("jobAgeMax", YHUtility.null2Empty(request.getParameter("jobAgeMax")));
		
		map.put("leaveTypeMin", YHUtility.null2Empty(request.getParameter("leaveTypeMin")));
		map.put("leaveTypeMax", YHUtility.null2Empty(request.getParameter("leaveTypeMax")));
		map.put("foreignLanguage1", YHUtility.null2Empty(request.getParameter("foreignLanguage1")));
		map.put("foreignLanguage2", YHUtility.null2Empty(request.getParameter("foreignLanguage2")));
		map.put("foreignLanguage3", YHUtility.null2Empty(request.getParameter("foreignLanguage3")));
		map.put("foreignLevel1", YHUtility.null2Empty(request.getParameter("foreignLevel1")));
		map.put("foreignLevel2", YHUtility.null2Empty(request.getParameter("foreignLevel2")));
		map.put("foreignLevel3", YHUtility.null2Empty(request.getParameter("foreignLevel3")));
		map.put("selectValue", YHUtility.null2Empty(request.getParameter("selectValue")));
		response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String fileName = URLEncoder.encode("HRMS.csv", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ArrayList<YHDbRecord> dbL = this.logic.exportToCSVLogic(dbConn,map);
			YHCSVUtil.CVSWrite(response.getWriter(), dbL);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}
	
	
	/**
	 *离职人员信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRetireeListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getRetireeListLogic(dbConn, request.getParameterMap(), person);
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
	
	
	
	
	
	
	
	
	

}
