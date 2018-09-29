package yh.subsys.oa.hr.salary.insurancePara.salItem.act;

import java.sql.Connection;
import java.util.ArrayList;
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
import yh.subsys.oa.hr.manage.staffLaborSkills.data.YHHrStaffLaborSkills;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHHrInsurancePara;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.insurancePara.salItem.logic.YHSalaryItemLogic;

public class YHSalaryItemAct {
	YHSalaryItemLogic salaryItem = new YHSalaryItemLogic();
	public String addSalaryItemAct(HttpServletRequest request,
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
			salaryItem.setSalaryItemLogic(dbConn, fileForm, user);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act");
		return null;
	}
  /**
   * 查询薪酬项
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
	public String getSalaryItemJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int num = salaryItem.getSalaryItemCountLogic(dbConn,user);
			List<YHSalItem> findSalItem = salaryItem.getSalaryItemJsonLogic(dbConn,user);
			List list = new ArrayList();
			if(findSalItem!=null){
			   for(int i=0; i<findSalItem.size()&&findSalItem.size()>0; i++){
				  String itemSet="";
				  if(!YHUtility.isNullorEmpty(findSalItem.get(i).getIsreport())&&findSalItem.get(i).getIsreport().equals("1")){
					  itemSet = "部门上报项";
				  }else{
					  if(!YHUtility.isNullorEmpty(findSalItem.get(i).getIscomputer())&&findSalItem.get(i).getIscomputer().equals("1")){
						  itemSet = "计算项";
					  }else{
						  itemSet = "财务录入项";
					  }
				  }
				  list.add(itemSet);
			}
				request.setAttribute("itemSetList", list);
			}
			request.setAttribute("salCount", num);
			request.setAttribute("SalItemList", findSalItem);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/salary/insurancePara/salItem/index.jsp";
	}
	/**
	 * 删除单个薪酬项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delSlaItemInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = salaryItem.delSalItemInfoLogic(dbConn, person, noHiddenId);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act";
	}
	/**
	 * 删除所有薪酬项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delAllSlaItemInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");

			int ok = salaryItem.delAllSalItemInfoLogic(dbConn, person);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act";
	}
	/**
	 * 在修改薪酬项目时，先查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findSlaItemInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String slaItemId = request.getParameter("slaItemId");

			YHSalItem findSlaItem = salaryItem.findSlaItemInfoLogic(dbConn,
					user, slaItemId);
			String seqId ="";
			
			request.setAttribute("findSlaItemList", findSlaItem);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/salary/insurancePara/salItem/edit.jsp";
	}
	/**
	 * 点击公式编辑按钮  先查出编辑公式
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findBJInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String slaItemId = request.getParameter("slaItemId");

			YHSalItem findSlaItem = salaryItem.findSlaItemInfoLogic(dbConn,
					user, slaItemId);	
			request.setAttribute("findSlaItemList", findSlaItem);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/salary/insurancePara/salItem/formulaEdit.jsp";
	}
	/**
	 * 修改薪酬项目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upSalaryItemAct(HttpServletRequest request,
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
			salaryItem.setUpSalaryItemLogic(dbConn, fileForm, user);
			} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act");
		return null;
	}
	/**
	 * 修改公式编辑并且保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upSaveSalaryItemAct(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		String seqId = request.getParameter("seqId");
		String slaItemId = request.getParameter("slaitemId");
		String formulaId = request.getParameter("FormulaID");
		String textFormula = request.getParameter("textFormula");
		
		try {
			dbConn = requestDbConn.getSysDbConn();
			salaryItem.upSaveSalaryItemLogic(dbConn,user,seqId,slaItemId,formulaId,textFormula);
			} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act");
		return null;
	}
	public String getSalItemNameAct(HttpServletRequest request,
		    HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try {
		        String str = "";
		        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		        dbConn = requestDbConn.getSysDbConn();
		        YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		        String officeId = request.getParameter("officeId");
		        str = salaryItem.getSalItemNameLogic(dbConn,user);
		        //str = syslog.getMySysTenLog(dbConn, user);
		        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出办公用品");
		        request.setAttribute(YHActionKeys.RET_DATA, str);
		    }catch(Exception ex) {
		        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		        request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		        throw ex;
		    }
		    return "/core/inc/rtjson.jsp";
		  }
	/**
	 * 查询保险系数设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findInsureBaseSetAct(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			List<YHHrInsurancePara> findInsurance = salaryItem.findInsureBaseSetLogic(dbConn,user);	
			request.setAttribute("findInsuranceList", findInsurance);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		} 
		return "/subsys/oa/hr/salary/insurancePara/insureBaseSet.jsp";
	}
	/**
	 * 查询保险参数的总数 如果为0增加一列保险参数数据
	 * 如果大于 >0 修改保险参数数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findInsureBaseCountAct(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			int seqId = salaryItem.findInsureBaseCountLogic(dbConn,user);
			if(seqId >0){//更新保险参数系数
				salaryItem.upInsureBaseLogic(dbConn, fileForm, user,seqId);
			}else{//增加保险系数
				salaryItem.addInsureBaseLogic(dbConn, fileForm, user);
			}
			//request.setAttribute("SalItemList", findSalItem);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/findInsureBaseSetAct.act");
		return null;
	}
	
	/*public String getSalaryItemJson1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      String data = "";
		      data = this.salaryItem.getSalaryItemJsonLogic(dbConn, request.getParameterMap(), person);
		      PrintWriter pw = response.getWriter();
		      pw.println(data);
		      pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}*/
	
}
