package yh.subsys.oa.hr.manage.staffLearnExperience.act;

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
import yh.subsys.oa.hr.manage.staffLearnExperience.data.YHHrStaffLearnExperience;
import yh.subsys.oa.hr.manage.staffLearnExperience.logic.YHNewLearnExperienceLogic;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;

public class YHNewLearnExperienceAct {
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	YHNewLearnExperienceLogic learnExperience = new YHNewLearnExperienceLogic();
	public static final String attachmentFolder = "hr";

	/**
	 * 增加学习经历信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addLearnExperienceInfo(HttpServletRequest request,
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
			learnExperience.setNewLicenseInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staff_learn_experience/addOK.jsp");
		return null;
	}
	/**
	 * 修改学习经历信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateLearnExperienceInfo(HttpServletRequest request,
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
			learnExperience.setUpLearnInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/manage/staffLearnExperience/act/YHNewLearnExperienceAct/findLearnExInfo.act");
		return null;
		//return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
	}

	/*
	 * 查询学习经历信息 带分页的
	 */
	public String findLearnExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = learnExperience.count(dbConn, user);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);
			
			List<YHHrStaffLearnExperience> findLearnEx = learnExperience.findLearnExperienceInfo(dbConn,
					user, page);
			request.setAttribute("findLearnExs", findLearnEx);
			request.setAttribute("page", page);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/manage/staff_learn_experience/index1.jsp";
	}

	/**
	 * 查询学习经历管理的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findLearnXxInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String learnSeqId = request.getParameter("learnSeqId");

			List<YHHrStaffLearnExperience> findLearns = learnExperience.learnXxInfo(dbConn,
					user, Integer.parseInt(learnSeqId));
			String seqId ="";
			for(int i=0; i<findLearns.size(); i++){
			   seqId = findLearns.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("onefindLearn", findLearns);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}  
		return "/subsys/oa/hr/manage/staff_learn_experience/learnXxInfo.jsp";
	}

	/**
	 * 删除学习经历的单条基本信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public String delLearnExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = learnExperience.delLearnExInfo(dbConn, person, Integer
					.parseInt(noHiddenId));
			if (ok != 0) {
				return "/yh/subsys/oa/hr/manage/staffLearnExperience/act/YHNewLearnExperienceAct/findLearnExInfo.act";
			}
			// request.setAttribute("booktype", booktype);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/manage/staffLearnExperience/act/YHNewLearnExperienceAct/findLearnExInfo.act";
	}

	/**
	 * 修改学习经历的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upLearnExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String learnseId = request.getParameter("learnseId");

			List<YHHrStaffLearnExperience> findLearnExs = learnExperience.learnXxInfo(dbConn,
					user, Integer.parseInt(learnseId));
			String seqId ="";
			for(int i=0; i<findLearnExs.size(); i++){
			   seqId = findLearnExs.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			
			request.setAttribute("learnExsInfoList", findLearnExs);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_learn_experience/updateLearnEx.jsp";
	}

	/**
	 * 删除所选择的学习经历信息
	 */
	public String deleteLearnExInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String deleteStr = request.getParameter("deleteStr");

			learnExperience.deleteLearnExInfo(dbConn, deleteStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);// RET_STATE返回状态
																			// RETURN_OK正确返回
			request.setAttribute(YHActionKeys.RET_MSRG, "证照信息删除成功");// RET_MSRG
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
	 * 查询学习经历信息（带查询条件的条件）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryLearnInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		// String seqid = request.getParameter("seqid");userId
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String major = request.getParameter("MAJOR");
		String academyDegree = request.getParameter("ACADEMY_DEGREE");//所获学历
		String school = request.getParameter("SCHOOL");//
		String certificates = request.getParameter("CERTIFICATES");//所获证书

		YHHrStaffLearnExperience learnEx = new YHHrStaffLearnExperience();
		learnEx.setStaffName(userId);
		learnEx.setMajor(major);
		learnEx.setAcademyDegree(academyDegree);
		learnEx.setSchool(school);
		learnEx.setCertificates(certificates);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = learnExperience.learnCount(dbConn, user,learnEx);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);

			List<YHHrStaffLearnExperience> findLearnEx = learnExperience.queryLearnExInfo(dbConn, user, page, learnEx);
			request.setAttribute("learnInfoList", findLearnEx);
			request.setAttribute("page", page);
			request.setAttribute("learnExs", learnEx);

		} catch (SQLException e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_learn_experience/learnQueryIndex2.jsp";
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
      boolean updateFlag = this.learnExperience.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
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
