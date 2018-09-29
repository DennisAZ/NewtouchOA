package yh.custom.attendance.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHOvertimeRecord;
import yh.custom.attendance.logic.YHOvertimeRecordLogic;
import yh.subsys.oa.addworkfee.logic.YHAddWorkFeeLogic;

public class YHOvertimeRecordAct {
  private YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
  /**
   * 添加加班申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addOvertime (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String overtimeDesc = request.getParameter("overtimeDesc");
      String beginTime = request.getParameter("beginTime");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String endTime = request.getParameter("endTime");
      String leaderId = request.getParameter("leaderId");
      String overtimeType = request.getParameter("overtimeType");
      String hour = request.getParameter("hour");
      String overtimeMoney = request.getParameter("overtimeMoney");
      String normalAdd = request.getParameter("normalAdd");
      String festivalAdd = request.getParameter("festivalAdd");
      String weekAdd = request.getParameter("weekAdd");
      YHOvertimeRecord yhor = new YHOvertimeRecord();
      yhor.setLeaderId(leaderId);
      yhor.setOvertimeDesc(overtimeDesc);
      yhor.setOvertimeTime(new Date());
      if(!YHUtility.isNullorEmpty("beginTime")){
        yhor.setBeginTime(format.parse(beginTime));
      }
      yhor.setUserId(String.valueOf(userId));
      yhor.setStatus("0");
      yhor.setBeginDate(beginDate);
      yhor.setEndDate(endDate);
      yhor.setHour(hour);
      if(YHUtility.isNullorEmpty(normalAdd)){
        normalAdd = "0";
      }
      if(YHUtility.isNullorEmpty(weekAdd)){
        weekAdd = "0";
      }
      if(YHUtility.isNullorEmpty(overtimeMoney)){
        overtimeMoney = "0";
      }
      if(YHUtility.isNullorEmpty(festivalAdd)){
        festivalAdd = "0";
      }
      yhor.setNormalAdd(Double.valueOf(normalAdd));
      yhor.setWeekAdd(Double.valueOf(weekAdd));
      yhor.setOvertimeMoney(Double.valueOf(overtimeMoney));
      yhor.setOvertimeType(overtimeType);
      yhor.setFestivalAdd(Double.valueOf(festivalAdd));
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      logic.addOvertime(dbConn, yhor);
      
      YHFlowHookUtility ut = new YHFlowHookUtility();
      int overTimeId = ut.getMax(dbConn, "select max(SEQ_ID) FROM oa_timeout_record");
      Map dataArray = new HashMap();
      dataArray.put("KEY", overTimeId + "");
      dataArray.put("FIELD", "OVERTIME_ID");
      dataArray.put("USER_ID", yhor.getUserId()+"");
      YHPersonLogic p = new YHPersonLogic();
      String userName = p.getUserNameLogic(dbConn, Integer.parseInt(yhor.getUserId()));
      dataArray.put("USER_NAME", userName);
      String leaderName= p.getUserNameLogic(dbConn, Integer.parseInt(yhor.getLeaderId()));
      dataArray.put("APPROVE_ID",leaderName);
      dataArray.put("RECORD_TIME",beginTime);
      dataArray.put("OVERTIME_CONTENT",overtimeDesc);
      dataArray.put("START_TIME",beginTime+" "+beginDate);
      dataArray.put("END_TIME",beginTime+" "+beginDate);
      String url = ut.runHook(dbConn, user, dataArray, "attendance_overtime");
      if (!"".equals(url)) {
        String path = request.getContextPath();
        response.sendRedirect(path+ url);
      }
      
      
      String smsRemind = request.getParameter("smsRemind");
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交加班申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(leaderId);
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      //手机短信 提醒 
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leaderId, userId, "提交加班申请，请批示:" + overtimeDesc, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path+ "/custom/attendance/personal/overtime/index.jsp");
    return "";
  }
  /**
   * 更新加班申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateOvertime (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String overtimeDesc = request.getParameter("overtimeDesc");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String leaderId = request.getParameter("leaderId");
      String overtimeType = request.getParameter("overtimeType");
      String hour = request.getParameter("hour");
      String overtimeMoney = request.getParameter("overtimeMoney");
      String normalAdd = request.getParameter("normalAdd");
      String festivalAdd = request.getParameter("festivalAdd");
      String weekAdd = request.getParameter("weekAdd");
      if(YHUtility.isInteger(seqId)){
        YHOvertimeRecord yhor = new YHOvertimeRecord();
        yhor.setLeaderId(leaderId);
        yhor.setOvertimeDesc(overtimeDesc);
        yhor.setOvertimeTime(new Date());
        if(!YHUtility.isNullorEmpty("beginTime")){
          yhor.setBeginTime(format.parse(beginTime));
        }
        yhor.setStatus("0");
        yhor.setBeginDate(beginDate);
        yhor.setEndDate(endDate);
        yhor.setHour(hour);
        if(YHUtility.isNullorEmpty(normalAdd)){
          normalAdd = "0";
        }
        if(YHUtility.isNullorEmpty(weekAdd)){
          weekAdd = "0";
        }
        if(YHUtility.isNullorEmpty(overtimeMoney)){
          overtimeMoney = "0";
        }
        if(YHUtility.isNullorEmpty(festivalAdd)){
          festivalAdd = "0";
        }
        yhor.setNormalAdd(Double.valueOf(normalAdd));
        yhor.setWeekAdd(Double.valueOf(weekAdd));
        yhor.setOvertimeMoney(Double.valueOf(overtimeMoney));
        yhor.setOvertimeType(overtimeType);
        yhor.setFestivalAdd(Double.valueOf(festivalAdd));
        yhor.setSeqId(Integer.parseInt(seqId));
        YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
        logic.updateOvertimeById(dbConn, yhor);
        String smsRemind = request.getParameter("smsRemind");
        //短信smsType, content, remindUrl, toId, fromId
        if(smsRemind!=null){
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("6");
          sb.setContent("提交加班申请，请批示！");
          sb.setRemindUrl("/custom/attendance/attendmanage/index.jsp");
          sb.setToId(leaderId);
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }

        String moblieSmsRemind = request.getParameter("moblieSmsRemind");
        if(moblieSmsRemind!=null){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,leaderId, userId, "提交加班申请，请批示:" + overtimeDesc, new Date());
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path+ "/custom/attendance/personal/overtime/index.jsp");
    return "";
  }
  /**
   * 更新加班申请的状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateOvertimeStatus (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String reason = request.getParameter("reason");
      String status = request.getParameter("status");
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      if(!YHUtility.isInteger(seqId)){
        YHOvertimeRecord yhor = logic.selectOvertimeById(dbConn, seqId);
        if(yhor!=null){
          logic.updateOvertimeById(dbConn, seqId, status, reason);
          String leaderId = yhor.getLeaderId();
          String smsRemind = request.getParameter("smsRemind");
          //短信smsType, content, remindUrl, toId, fromId
          if(smsRemind!=null){
            YHSmsBack sb = new YHSmsBack();
            sb.setSmsType("6");
            sb.setContent("提交加班申请，请批示！");
            sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
            sb.setToId(leaderId);
            sb.setFromId(userId);
            YHSmsUtil.smsBack(dbConn, sb);
          }

          String moblieSmsRemind = request.getParameter("moblieSmsRemind");
          if(moblieSmsRemind!=null){
            YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
            sms2Logic.remindByMobileSms(dbConn,leaderId, userId, "提交加班申请，请批示:" , new Date());
          }
        } 
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path+ "/core/funcs/attendance/personal/evection.jsp");
    return "";
  }
  /**
   * 删除加班申请ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delOvertimeById (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      if(YHUtility.isInteger(seqId)){
        logic.delOvertimeById(dbConn, seqId);
      }
      String data = "{}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询加班申请ById
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectOvertimeById (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      YHPersonLogic tpl = new YHPersonLogic();
      String data = "";
      if(!YHUtility.isNullorEmpty(seqId)){
        YHOvertimeRecord overtime  = logic.selectOvertimeById(dbConn, seqId);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"}";
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询加班申请（待批和未批的）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectOvertime (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "(STATUS = '0' or STATUS is null)" };
      overList = logic.selectOvertime(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      String data = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        
        int runId = fu.isRunHook(dbConn, "OVERTIME_ID", overtime.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",leaderName:\"" + leaderName+ "\"},";
      }
      if(overList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询加班申请（已批的）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectHistoryOvertime (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "STATUS = '1'" };
      overList = logic.selectOvertime(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      String data = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        
        int runId = fu.isRunHook(dbConn, "OVERTIME_ID", overtime.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",leaderName:\"" + leaderName+ "\"},";
      }
      if(overList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更改状态同意
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkOut = request.getParameter("checkOut");
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      if (!YHUtility.isNullorEmpty(seqId)) {
        logic.updateOvertimeById(dbConn, seqId, status, "");
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的加班申请已被批准！");
        sb.setRemindUrl("/custom/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        // 发送手机短信
        if (checkOut != null && checkOut.equals("1")) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, userId, userSeqId, "您的加班申请已被批准！",
              new Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更改状态不同意
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatusReason(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkOut = request.getParameter("checkOut");
      String reason = request.getParameter("reason");
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      if (!YHUtility.isNullorEmpty(seqId)) {
        logic.updateOvertimeById(dbConn, seqId, status, reason);
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的加班申请未被批准！");
        sb.setRemindUrl("/custom/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        // 发送手机短信
        if (checkOut != null && checkOut.equals("1")) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, userId, userSeqId, "您的加班申请未被批准！",new Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取个人平时加班时长--cc 20101130
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonalOverTime (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String overtimeType = request.getParameter("overtimeType");
      String curDateStr = YHUtility.getCurDateTimeStr().substring(0, 10);
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      YHPersonLogic tpl = new YHPersonLogic();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String whereStr = "";
      String ymd = year + "-" + month + "-" + "01";
      if(!YHUtility.isNullorEmpty(year) || !YHUtility.isNullorEmpty(month)){
        whereStr += " and " + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
       whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
      }
//      if(YHUtility.isNullorEmpty(beginDate) && YHUtility.isNullorEmpty(endDate)){
//        whereStr += " and "+ YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(curDateStr));
//      }
      String[] str = {"USER_ID = '" + userId + "' and STATUS = '1' and OVERTIME_TYPE = '"+overtimeType+"'"+ whereStr+"" };
      overList = logic.selectOvertime(dbConn, str);
      String data = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"},";
      }
      if(overList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取加班时长--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOverTimeStat (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String overtimeType = request.getParameter("overtimeType");
      String userId = request.getParameter("userId");
      String curDateStr = YHUtility.getCurDateTimeStr().substring(0, 10);
      YHOvertimeRecordLogic logic = new YHOvertimeRecordLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      YHPersonLogic tpl = new YHPersonLogic();
      String whereStr = "";
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
       whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
      }
      if(YHUtility.isNullorEmpty(beginDate) && YHUtility.isNullorEmpty(endDate)){
        whereStr += " and "+ YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(curDateStr));
      }
      String[] str = {"USER_ID = '" + userId + "' and STATUS = '1' and OVERTIME_TYPE = '"+overtimeType+"'"+ whereStr+"" };
      overList = logic.selectOvertime(dbConn, str);
      String data = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(overtime.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",leaderName:\"" + leaderName+ "\"},";
      }
      if(overList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 个人加班总时长  1-平时,2-周末,3-节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTotalAdd (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(user.getSeqId());
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String overtimeType = request.getParameter("overtimeType");
      String curDateStr = YHUtility.getCurDateTimeStr().substring(0, 10);
      String data = "";
      if("1".equals(overtimeType)){
        data = String.valueOf(this.logic.getNormalAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      if("2".equals(overtimeType)){
        data = String.valueOf(this.logic.getWeekAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      if("3".equals(overtimeType)){
        data = String.valueOf(this.logic.getFestivalAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 加班总时长统计  1-平时,2-周末,3-节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTotalAddStat (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String overtimeType = request.getParameter("overtimeType");
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String curDateStr = YHUtility.getCurDateTimeStr().substring(0, 10);
      String data = "";
      if("1".equals(overtimeType)){
        data = String.valueOf(this.logic.getNormalAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      if("2".equals(overtimeType)){
        data = String.valueOf(this.logic.getWeekAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      if("3".equals(overtimeType)){
        data = String.valueOf(this.logic.getFestivalAddLogic(dbConn, beginDate, endDate, userId, overtimeType, curDateStr, year, month));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取加班基本信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOverTimeInfo (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String beginTime = request.getParameter("beginTime");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHAddWorkFeeLogic awfl = new YHAddWorkFeeLogic();
      String begin = beginTime + " " + beginDate;
      String end = beginTime + " " + endDate;
      String data = awfl.accountAddWorkFee(dbConn, beginTime, begin, end, Integer.parseInt(person.getUserPriv()));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data );
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取值班基本信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDutyInfo (HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String beginTime = request.getParameter("beginTime");
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      YHAddWorkFeeLogic awfl = new YHAddWorkFeeLogic();
      String begin = beginTime + " " + beginDate;
      String end = beginTime + " " + endDate;
      String data = awfl.accountAddDutyFee(dbConn, beginTime, begin, end, Integer.parseInt(person.getUserPriv()));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data );
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
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
   * 加班总时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOverTimeHour(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String userIdStr = request.getParameter("userIdStr");
      if(!YHUtility.isNullorEmpty(userIdStr)){
        userId = userIdStr;
      }
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      double data = this.logic.getOverTimeHourLogic(dbConn, year, month, userId);
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
   * 加班工资数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOverTimeMoney(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String userIdStr = request.getParameter("userIdStr");
      if(!YHUtility.isNullorEmpty(userIdStr)){
        userId = userIdStr;
      }
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      double data = this.logic.getOverTimeMoneyLogic(dbConn, year, month, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
