package yh.subsys.oa.fillRegister.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;
import yh.subsys.oa.fillRegister.logic.YHAttendApprovalLogic;


public class YHAttendApprovalAct {
  public static final String attachmentFolder = "fillRegister";
  private YHAttendApprovalLogic logic = new YHAttendApprovalLogic();
  
  public String getRegisterApprovalListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String mStatus = request.getParameter("assessingStatus");
      String data = this.logic.getRegisterApprovalListJson(dbConn, request.getParameterMap(), mStatus, person);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
      getScore(request, response);
      getDirectorScore(request, response);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 考勤数据统计查询－补登记查询(审核通过)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegisterApprovalPassJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String mStatus = request.getParameter("assessingStatus");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String data = this.logic.getRegisterApprovalPassJson(dbConn, request.getParameterMap(), mStatus, person, beginTime, endTime);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
      getScore(request, response);
      getDirectorScore(request, response);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 测试用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDirectorScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = "2";//request.getParameter("userId");
      String year = "2010";//request.getParameter("year");
      String month = "10";//request.getParameter("month");
      int data = this.logic.getDirectorScore(dbConn, year, month, userId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = "1";//request.getParameter("userId");
      String year = "2010";//request.getParameter("year");
      String month = "10";//request.getParameter("month");
      this.logic.getAttendScore(dbConn, year, month, userId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      //request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取用户名称 -cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userIdStr = request.getParameter("userId");
    int userId = 0;
    if (!YHUtility.isNullorEmpty(userIdStr)) {
			userId = Integer.parseInt(userIdStr);
		}
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getUserNameLogic(dbConn, userId);
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
   * 修改补登记审核状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId2 = request.getParameter("seqId");
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
      }
      String assessingStatus = request.getParameter("assessingStatus");
      String assessingView = request.getParameter("assessingView");
      String content = "";
      if ("1".equals(assessingStatus)) {
        content = "审批通过";
        assessingView = "<font color='green'>批准</font> <b>by " + person.getUserName() + " " +YHUtility.getCurDateTimeStr() +"</b><br/>" + assessingView;
        
      } else {
        content = "审批未通过";
        assessingView = "<font color='green'>驳回</font> <b>by " + person.getUserName() + " " +YHUtility.getCurDateTimeStr() +"</b><br/>" + assessingView;
      }
      //this.logic.updateStatus(dbConn, seqId, assessingStatus, assessingView);
      Map m =new HashMap();
      Date time = new Date();
      m.put("seqId", seqId);
      m.put("assessingStatus", assessingStatus);
      m.put("assessingTime", YHUtility.getCurDateTimeStr());//YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",YHUtility.getCurDateTimeStr())
      m.put("assessingView", assessingView);
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn, "attendFill", m);
      
      
      //短信提醒
      YHAttendFill attendFill = (YHAttendFill) orm.loadObjSingle(dbConn, YHAttendFill.class, seqId);
      YHSmsBack sb = new YHSmsBack();
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "";
      String smsContent = "";
      if ("1".equals(assessingStatus)) {
        remindUrl = "/subsys/oa/fillRegister/approval/manage.jsp?openFlag=1&openWidth=860&openHeight=650";
        smsContent = person.getUserName() + " 已审批通过您的补登记申请 。";
        this.logic.doSmsBackTime(dbConn, smsContent, person.getSeqId(), attendFill.getProposer(), "62", remindUrl, new java.util.Date());
        
      }
      if ("1".equals(assessingStatus)) {
        smsContent = person.getUserName() + " 已审批通过您的补登记申请 。";
        sbl.remindByMobileSms(dbConn, attendFill.getProposer(), person.getSeqId(), smsContent, new java.util.Date());
      }
      
      if ("2".equals(assessingStatus)) {
        remindUrl = "/subsys/oa/fillRegister/approval/manage.jsp?openFlag=1&openWidth=860&openHeight=650";
        smsContent = person.getUserName() + " 已驳回您的补登记申请 。";
        this.logic.doSmsBackTime(dbConn, smsContent, person.getSeqId(), attendFill.getProposer(), "62", remindUrl, new java.util.Date());
      }
      if ("2".equals(assessingStatus)) {
        smsContent = person.getUserName() + " 已驳回您的补登记申请 。";
        sbl.remindByMobileSms(dbConn, attendFill.getProposer(), person.getSeqId(), smsContent, new java.util.Date());
      }

      String data = assessingStatus;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取补登记详情
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getApprovalDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendFill meeting = (YHAttendFill) this.logic.getPlanDetail(dbConn, Integer.parseInt(seqId));
      if (meeting == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(meeting);
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
	 * 补登记(审批)查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryApprovalListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("assessingOfficer", YHDBUtility.escapeLike(request.getParameter("assessingOfficer")));
			map.put("assessingStatus", YHDBUtility.escapeLike(request.getParameter("assessingStatus")));
			map.put("beginDate", YHDBUtility.escapeLike(request.getParameter("beginDate")));
			map.put("endDate", YHDBUtility.escapeLike(request.getParameter("endDate")));
			String data =  this.logic.queryApprovalListJsonLogic(dbConn, request.getParameterMap(), map, person);
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
	 * 删除单个补登记申请
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteSingle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
	
	/**
	 * 批量删除
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("sumStrs");
      this.logic.deleteAll(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
