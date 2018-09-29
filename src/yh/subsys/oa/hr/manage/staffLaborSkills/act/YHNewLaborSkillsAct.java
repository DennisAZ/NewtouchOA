package yh.subsys.oa.hr.manage.staffLaborSkills.act;

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
import yh.subsys.oa.hr.manage.staffLaborSkills.data.YHHrStaffLaborSkills;
import yh.subsys.oa.hr.manage.staffLaborSkills.logic.YHNewLaborSkillsLogic;
import yh.subsys.oa.hr.manage.staffWorkExperience.data.YHHrStaffWorkExperience;

public class YHNewLaborSkillsAct {
	YHNewLaborSkillsLogic laborSkills = new YHNewLaborSkillsLogic();
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	public static final String attachmentFolder = "hr";

	/**
	 * 增加劳动技能信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addLaborSkillsInfo(HttpServletRequest request,
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
			laborSkills.setNewLaborSkillsInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staff_labor_skills/addOK.jsp");
		return null;
	}
	/**
	 * 修改劳动技能信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateLaborSkillInfo(HttpServletRequest request,
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
			laborSkills.setUpLaborSkillInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/findLaborSkillsInfo.act");
		return null;
		//return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
	}

	/*
	 * 查询劳动技能信息 带分页的
	 */
	public String findLaborSkillsInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = laborSkills.count(dbConn, user);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);
			
			List<YHHrStaffLaborSkills> findLaborSkill = laborSkills.findLaborSkillsInfo(dbConn,
					user, page);
			request.setAttribute("findLaborSkills", findLaborSkill);
			request.setAttribute("page", page);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_labor_skills/index1.jsp";
	}

	/**
	 * 查询劳动技能管理的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findlaborSkillsXxInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String skillId = request.getParameter("skillId");

			List<YHHrStaffLaborSkills> findLaborSkills = laborSkills.laborSkillXxInfo(dbConn,
					user, Integer.parseInt(skillId));
			String seqId ="";
			for(int i=0; i<findLaborSkills.size(); i++){
			   seqId = findLaborSkills.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("onefindLaborSkill", findLaborSkills);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/manage/staff_labor_skills/laborSkillXxInfo.jsp";
	}

	/**
	 * 删除劳动技能的单条基本信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public String delLaborSkillInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = laborSkills.delLaborSkillInfo(dbConn, person, Integer
					.parseInt(noHiddenId));
			if (ok != 0) {
				return "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/findLaborSkillsInfo.act";
			}
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/findLaborSkillsInfo.act";
	}

	/**
	 * 修改劳动技能的详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upLaborSkillInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String laborSkillId = request.getParameter("laborSkillId");

			List<YHHrStaffLaborSkills> findLaborSkill = laborSkills.laborSkillXxInfo(dbConn,
					user, Integer.parseInt(laborSkillId));
			String seqId ="";
			for(int i=0; i<findLaborSkill.size(); i++){
			   seqId = findLaborSkill.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("laborSkillInfoList", findLaborSkill);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_labor_skills/updateLaborSkill.jsp";
	}

	/**
	 * 删除所选择的劳动技能信息
	 */
	public String deleteLaborSkillInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String deleteStr = request.getParameter("deleteStr");

			laborSkills.deleteLaborSkillInfo(dbConn, deleteStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);// RET_STATE返回状态
																			// RETURN_OK正确返回
			request.setAttribute(YHActionKeys.RET_MSRG, "劳动技能信息删除成功");// RET_MSRG
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
	 * 查询劳动技能信息（带查询条件的条件）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryLaborSkillInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		// String seqid = request.getParameter("seqid");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String issueDate1 = request.getParameter("ISSUE_DATE1");
		String issueDate2 = request.getParameter("ISSUE_DATE2");//
		String expireDate1 = request.getParameter("EXPIRE_DATE1");//
		String expireDate2 = request.getParameter("EXPIRE_DATE2");//
		String abilityName = request.getParameter("ABILITY_NAME");//
		String issuingAuthority = request.getParameter("ISSUING_AUTHORITY");//
		
		
		YHHrStaffLaborSkills laborSkill = new YHHrStaffLaborSkills();
		laborSkill.setStaffName(userId);
		laborSkill.setAbilityName(abilityName);
		laborSkill.setIssuingAuthority(issuingAuthority);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = laborSkills.laborSkillCount(dbConn, user,laborSkill,issueDate1,issueDate2,expireDate1,expireDate2);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);

			List<YHHrStaffLaborSkills> findLaborSkills = laborSkills.queryLaborSkillInfo(dbConn, user, page, laborSkill,issueDate1,issueDate2,expireDate1,expireDate2);
			/*String seqId ="";
			for(int i=0; i<findWorkEx.size(); i++){
			   seqId = findWorkEx.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userNames =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userNames);
			}*/
			request.setAttribute("findLaborSkills", findLaborSkills);
			request.setAttribute("page", page);
			request.setAttribute("laborSkill", laborSkill);
			request.setAttribute("issueDate1", issueDate1);
			request.setAttribute("issueDate2", issueDate2);
			request.setAttribute("expireDate1", expireDate1);
			request.setAttribute("expireDate2", expireDate2);
		} catch (SQLException e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_labor_skills/skillQueryIndex2.jsp";
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
      boolean updateFlag = this.laborSkills.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
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
