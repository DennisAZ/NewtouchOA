package yh.subsys.oa.hr.manage.staffWorkExperience.act;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.hr.manage.hrIdtransName.hrPublicIdTransName;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;
import yh.subsys.oa.hr.manage.staffWorkExperience.logic.YHNewWorkExperienceLogic;

public class YHNewWorkExperienceAct {
	YHNewWorkExperienceLogic workExperience = new YHNewWorkExperienceLogic();
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	public static final String attachmentFolder = "hr";

	/**
	 * 增加工作经历信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addWorkExperienceInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			workExperience.setNewWordInfoValueLogic(dbConn, fileForm, user);
			// int ok = licenseLogic.newLicenseInfo(dbConn, user, license);
//			if (ok != 0) {
//				return "/subsys/oa/hr/manage/staff_license/addOK.jsp";
//			}
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staff_work_experience/addOK.jsp");
		return null;
	}
	/**
	 * 修改工作经历信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateWorkExperienceInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		
		try {
			dbConn = requestDbConn.getSysDbConn();
			workExperience.setUpWorkInfoValueLogic(dbConn, fileForm, user);
			/*int ok = licenseLogic.updateLicenseInfo(dbConn, user, license);
			if (ok != 0) { 
				return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
			}*/

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/findWorkExInfo.act");
		return null;
		//return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
	}

	/*
	 * 查询工作经历信息 带分页的
	 */
	public String findWorkExInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
			int total = workExperience.count(dbConn, user);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);
			
			List<YHHrStaffWorkExperience> findWorkEx = workExperience.findWorkExperienceInfo(dbConn,
					user, page);
			/*String seqId ="";
			for(int i=0; i<findWorkEx.size(); i++){
			   seqId = findWorkEx.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}*/
			request.setAttribute("findWorkExs", findWorkEx);
			request.setAttribute("page", page);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/manage/staff_work_experience/index1.jsp";
	}

	/**
	 * 查询工作经历管理的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findWorkXxInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String workSeqId = request.getParameter("workSeqId");

			List<YHHrStaffWorkExperience> findWorks = workExperience.workXxInfo(dbConn,
					user, Integer.parseInt(workSeqId));
			String seqId ="";
			for(int i=0; i<findWorks.size(); i++){
			   seqId = findWorks.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("onefindWork", findWorks);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/manage/staff_work_experience/workXxInfo.jsp";
	}

	/**
	 * 删除工作经历的单条基本信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public String delWorkExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = workExperience.delWorkExInfo(dbConn, person, Integer
					.parseInt(noHiddenId));
			if (ok != 0) {
				return "/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/findWorkExInfo.act";
			}
			// request.setAttribute("booktype", booktype);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/findWorkExInfo.act";
	}

	/**
	 * 修改工作经历的详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upWorkExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String workSeqId = request.getParameter("workSeqId");

			List<YHHrStaffWorkExperience> findWorkExs = workExperience.workXxInfo(dbConn,
					user, Integer.parseInt(workSeqId));
			String seqId ="";
			for(int i=0; i<findWorkExs.size(); i++){
			   seqId = findWorkExs.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("workExsInfoList", findWorkExs);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_work_experience/updateWorkEx.jsp";
	}

	/**
	 * 删除所选择的工作经历信息
	 */
	public String deleteWorkExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String deleteStr = request.getParameter("deleteStr");

			workExperience.deleteWorkExInfo(dbConn, deleteStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);// RET_STATE返回状态
																			// RETURN_OK正确返回
			request.setAttribute(YHActionKeys.RET_MSRG, "工作经历信息删除成功");// RET_MSRG
																	// 返回消息
			// request.setAttribute(YHActionKeys.RET_DATA, str);//RET_DATA 返回数据
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 查询工作经历信息（带查询条件的条件）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryWorkInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		// String seqid = request.getParameter("seqid");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String postOfJob = request.getParameter("POST_OF_JOB");
		String workUnit = request.getParameter("WORK_UNIT");//工作单位
		String mobile = request.getParameter("MOBILE");//行业类别
		String workContent = request.getParameter("WORK_CONTENT");//工作内容
		String keyPerformance = request.getParameter("KEY_PERFORMANCE");//主要业绩
		
		YHHrStaffWorkExperience workEx = new YHHrStaffWorkExperience();
		workEx.setStaffName(userId);
		workEx.setPostOfJob(postOfJob);
		workEx.setWorkUnit(workUnit);
		workEx.setMobile(mobile);
		workEx.setWorkContent(workContent);
		workEx.setKeyPerformance(keyPerformance);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = workExperience.workCount(dbConn, user,workEx);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);

			List<YHHrStaffWorkExperience> findWorkEx = workExperience.queryWorkExInfo(dbConn, user, page, workEx);
			/*String seqId ="";
			for(int i=0; i<findWorkEx.size(); i++){
			   seqId = findWorkEx.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userNames =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userNames);
			}*/
			request.setAttribute("workInfoList", findWorkEx);
			request.setAttribute("page", page);
			request.setAttribute("workExs", workEx);

		} catch (SQLException e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_work_experience/workQueryIndex2.jsp";
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
      boolean updateFlag = this.workExperience.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
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
