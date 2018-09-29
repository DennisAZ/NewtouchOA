package yh.subsys.oa.hr.manage.staff_license.act;

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
import yh.subsys.oa.hr.manage.staff_contract.data.YHHrStaffContract;
import yh.subsys.oa.hr.manage.staff_contract.logic.YHNewContractInfoLogic;
import yh.subsys.oa.hr.manage.staff_license.data.YHHrStaffLicense;
import yh.subsys.oa.hr.manage.staff_license.logic.YHNewLicenseInfoLogic;

public class YHNewLicenseInfoAct {
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	YHNewLicenseInfoLogic licenseLogic = new YHNewLicenseInfoLogic();
	public static final String attachmentFolder = "hr";

	/**
	 * 增加证照信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addLicenseInfo(HttpServletRequest request,
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
			 licenseLogic.setNewLicenseInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staff_license/addOK.jsp");
		return null;
	}
	/**
	 * 修改证照信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateLicenseInfo(HttpServletRequest request,
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
			 licenseLogic.setUpLicenseInfoValueLogic(dbConn, fileForm, user);
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
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act");
		return null;
		//return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
	}

	/*
	 * 查询证照信息 带分页的
	 */

	public String findLicenseInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = licenseLogic.count(dbConn, user);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);
			List<YHHrStaffLicense> findlicenses = licenseLogic.findLicenses(dbConn,
					user, page);
			request.setAttribute("licenseInfoList", findlicenses);
			request.setAttribute("page", page);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_license/index1.jsp";
	}

	/**
	 * 查询证照管理的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findLicenseXxInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String LicenseSeqId = request.getParameter("LicenseSeqId");

			List<YHHrStaffLicense> findLicenses = licenseLogic.licenseXxInfo(dbConn,
					user, Integer.parseInt(LicenseSeqId));
			String seqId ="";
			for(int i=0; i<findLicenses.size(); i++){
			   seqId = findLicenses.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			
			request.setAttribute("licenseXxInfoList", findLicenses);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_license/licenseXxInfo.jsp";
	}

	/**
	 * 删除合同的基本信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public String delLicenseInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = licenseLogic.delLicenseInfo(dbConn, person, Integer
					.parseInt(noHiddenId));
			if (ok != 0) {
				return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
			}
			// request.setAttribute("booktype", booktype);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/findLicenseInfo.act";
	}

	/**
	 * 修改证照的详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upLicenseInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String licenseId = request.getParameter("licenseId");

			List<YHHrStaffLicense> findlicenses = licenseLogic.licenseXxInfo(dbConn,
					user, Integer.parseInt(licenseId));
			
			String seqId ="";
			for(int i=0; i<findlicenses.size(); i++){
			   seqId = findlicenses.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}
			request.setAttribute("licensesInfoList", findlicenses);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_license/updateLicense.jsp";
	}

	/**
	 * 删除所选择的证照信息
	 */
	public String deleteLicenseInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String deleteStr = request.getParameter("deleteStr");

			licenseLogic.deleteLicenseInfo(dbConn, deleteStr);

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
	 * 查询证照信息（带时间条件）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryLicenseInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		// String seqid = request.getParameter("seqid");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String licenseType = request.getParameter("licenseType");
		String licenseNo = request.getParameter("LICENSE_NO");
		String licenseName = request.getParameter("LICENSE_NAME");//
		String licenseState = request.getParameter("licenseState");// 证照状态

		String expireDate1 = request.getParameter("EXPIRE_DATE1");// 到期日期
		String expireDate2 = request.getParameter("EXPIRE_DATE2"); // 到期日期
		String notifiedBody = request.getParameter("NOTIFIED_BODY"); // 发证机构
		YHHrStaffLicense license = new YHHrStaffLicense();
		license.setStaffName(userId);
		license.setLicenseType(licenseType);
		license.setLicenseNo(licenseNo);
		license.setLicenseName(licenseName);
		license.setStatus(licenseState);
		license.setNotifiedBody(notifiedBody);

		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int total = licenseLogic.licenseCount(dbConn, user,license,expireDate1, expireDate2);
			String currNo = request.getParameter("currNo");
			int curruntNo = 1;
			if (YHUtility.isNullorEmpty(currNo)) {
				curruntNo = 1;
			} else {
				curruntNo = Integer.parseInt(currNo);
			}
			YHPage page = new YHPage(5, total, curruntNo);

			List<YHHrStaffLicense> findLicenses = licenseLogic.queryLicenseInfo(
					dbConn, user, page, license, expireDate1, expireDate2);
			request.setAttribute("licenseInfoList", findLicenses);
			request.setAttribute("page", page);
            request.setAttribute("licenses", license);
            request.setAttribute("expireDate1", expireDate1);
            request.setAttribute("expireDate2", expireDate2);
            
		} catch (SQLException e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/manage/staff_license/licenseQueryIndex2.jsp";
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
      boolean updateFlag = this.licenseLogic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
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
