package yh.subsys.oa.training.act;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
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
import yh.subsys.oa.training.data.YHHrTrainingPlan;
import yh.subsys.oa.training.logic.YHTrainingApprovalLogic;
public class YHTrainingApprovalAct {
	
	public static final String attachmentFolder = "training";
	private YHTrainingApprovalLogic logic = new YHTrainingApprovalLogic();

	/**
   * 培训计划名称-模糊查找--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String getTrainingApprovalJson(HttpServletRequest request,  HttpServletResponse response) throws Exception, SQLException{    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;   
    try{
      dbConn = requestDbConn.getSysDbConn();
      String search = request.getParameter("condition");
        search = YHUtility.decodeURL(search); //解码
      if(YHUtility.isNullorEmpty(search)){
        search = "";
      }
      String userId = request.getParameter("userId");
      StringBuffer sb = new StringBuffer("[");
      YHPerson user = null;
      if(!YHUtility.isNullorEmpty(userId)){
        user = new YHPerson();
        user.setSeqId(Integer.parseInt(userId));//从页面中传过来的用户信息
      }else{
        user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      }
      List<YHHrTrainingPlan> hrTrainingPlanList = this.logic.findTrainingPlanNo(dbConn, user, search);
      
      for(int i = 0; i < hrTrainingPlanList.size(); i++){
        YHHrTrainingPlan plan = hrTrainingPlanList.get(i);
        sb.append("{");
        sb.append("seqId:\"" +  plan.getSeqId() + "\"");
        sb.append(",tPlanNo:\"" + (plan.getTPlanNo() == null ? "" : YHUtility.encodeSpecial(plan.getTPlanNo()))+ "\"");
        sb.append(",tPlanName:\"" + (plan.getTPlanName() == null ? "" : YHUtility.encodeSpecial(plan.getTPlanName()))+ "\"");
        sb.append(",tInstitutionName:\"" + (plan.getTInstitutionName() == null ? "" : YHUtility.encodeSpecial(plan.getTInstitutionName())) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (hrTrainingPlanList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());    
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取受训人选择框的用户信息--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String getTrainingUserSelectJson(HttpServletRequest request,  HttpServletResponse response) throws Exception, SQLException{    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;   
    try{
      dbConn = requestDbConn.getSysDbConn();
      String search = request.getParameter("condition");
        search = YHUtility.decodeURL(search); //解码
      if(YHUtility.isNullorEmpty(search)){
        search = "";
      }
      String userId = request.getParameter("userId");
      StringBuffer sb = new StringBuffer("[");
      YHPerson user = null;
      if(!YHUtility.isNullorEmpty(userId)){
        user = new YHPerson();
        user.setSeqId(Integer.parseInt(userId));//从页面中传过来的用户信息
      }else{
        user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      }
      List<YHPerson> personList = this.logic.findTrainingUserSelect(dbConn, user, search);
      
      for(int i = 0; i < personList.size(); i++){
        YHPerson plan = personList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + plan.getSeqId() + "\"");
        sb.append(",userId:\"" +plan.getUserId()+ "\"");
        sb.append(",userName:\"" +plan.getUserName()+ "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (personList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());    
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }

  
  /**
   *培训计划审批通用列表 --cc
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTrainingApprovalListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String mStatus = request.getParameter("assessingStatus");
      String data = this.logic.getTrainingApprovalListJson(dbConn, request.getParameterMap(), mStatus, person);
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
   * 培训计划详情 --cc
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPlanDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHHrTrainingPlan meeting = (YHHrTrainingPlan) this.logic.getPlanDetail(dbConn, Integer.parseInt(seqId));
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
   * 审批人名称 --cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String data = this.logic.getUserNameLogic(dbConn, Integer.parseInt(userId));
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
   * 会议审批通用方法 --cc
   * 
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
      orm.updateSingle(dbConn, "hrTrainingPlan", m);
      
      
      //短信提醒
      YHHrTrainingPlan trainingPlan = (YHHrTrainingPlan) orm.loadObjSingle(dbConn, YHHrTrainingPlan.class, seqId);
      YHSmsBack sb = new YHSmsBack();
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      String remindUrl = "";
      String smsContent = "";
      if ("1".equals(assessingStatus)) {
        remindUrl = "/subsys/oa/training/plan/planManage.jsp?openFlag=1&openWidth=860&openHeight=650";
        smsContent = person.getUserName() + " 已审批通过您的培训计划 " + trainingPlan.getTPlanName() + "。";
        this.logic.doSmsBackTime(dbConn, smsContent, person.getSeqId(), trainingPlan.getCreateUserId(), "61", remindUrl, new java.util.Date());
        
      }
      if ("1".equals(assessingStatus)) {
        smsContent = person.getUserName() + " 已审批通过您的培训计划 " + trainingPlan.getTPlanName() + "。";
        sbl.remindByMobileSms(dbConn, trainingPlan.getCreateUserId(), person.getSeqId(), smsContent, new java.util.Date());
      }
      
      if ("2".equals(assessingStatus)) {
        remindUrl = "/subsys/oa/training/plan/planManage.jsp?openFlag=1&openWidth=860&openHeight=650";
        smsContent = person.getUserName() + " 已驳回您的培训计划 " + trainingPlan.getTPlanName() + "。";
        this.logic.doSmsBackTime(dbConn, smsContent, person.getSeqId(), trainingPlan.getCreateUserId(), "61", remindUrl, new java.util.Date());
      }
      if ("2".equals(assessingStatus)) {
        smsContent = person.getUserName() + " 已驳回您的培训计划 " + trainingPlan.getTPlanName() + "。";
        sbl.remindByMobileSms(dbConn, trainingPlan.getCreateUserId(), person.getSeqId(), smsContent, new java.util.Date());
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
   * 培训计划(审批)查询--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTrainingApprovalSearchList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    ArrayList<YHAddress> addressList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String tPlanName = YHDBUtility.escapeLike(request.getParameter("tPlanName"));
      String tChannel = YHDBUtility.escapeLike(request.getParameter("tChannel"));
      String assessingOfficer = YHDBUtility.escapeLike(request.getParameter("assessingOfficer"));
      String assessingStatus = YHDBUtility.escapeLike(request.getParameter("assessingStatus"));
      String beginDate = YHDBUtility.escapeLike(request.getParameter("beginDate"));
      String endDate = YHDBUtility.escapeLike(request.getParameter("endDate"));
      String data = "";
      data = this.logic.getTrainingApprovalSearchList(dbConn, request.getParameterMap(), person, tPlanName, tChannel, assessingOfficer, assessingStatus, beginDate, endDate);
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
