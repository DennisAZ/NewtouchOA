package yh.core.funcs.attendance.manage.act;

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
import yh.core.funcs.attendance.manage.logic.YHManageLeaveLogic;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.logic.YHAttendLeaveLogic;
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
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;

public class YHManageLeaveAct {
  /**
   * 
   * 查询所有放假记录根据自己的ID审批人
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String selectLeaveManage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendLeave leave = new YHAttendLeave();
      YHManageLeaveLogic yhall = new YHManageLeaveLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      SimpleDateFormat formatter2 = new SimpleDateFormat("E"); 
      int userId = user.getSeqId();
      List<YHAttendLeave> leaveList = yhall.selectLeaveManage(dbConn,  userId);
      String data = "[";
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        leave = leaveList.get(i);
        String applyName = tpl.getNameBySeqIdStr(leave.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        String week1 = formatter2.format(leave.getLeaveDate1());
        String week2 = formatter2.format(leave.getLeaveDate2());
        String deptName = yhaol.selectByUserIdDept(dbConn, leave.getUserId());
        int runId = fu.isRunHook(dbConn, "LEAVE_ID", leave.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+ flowId +"\",week1:\"" + week1+ "\",week2:\"" + week2+ "\",applyName:\"" +applyName +"\",deptName:\"" + deptName +"\"},";
      }
      if(leaveList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
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
  /*
   * 
   * updateStatus ById
   */
  public String updateStatus(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkLeave = request.getParameter("checkLeave");
      if(YHUtility.isInteger(seqId)){
        Map map = new HashMap();
        map.put("seqId", seqId);
        map.put("allow", allow);
        YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
        YHAttendLeave leave = yhall.selectLeaveById(dbConn, seqId);
        YHAttendFillLogic fillLogic = new YHAttendFillLogic();
        if(leave != null){
          fillLogic.addAttendScoreLeave(dbConn, user, YHUtility.getDateTimeStr(leave.getLeaveDate1()),  YHUtility.getDateTimeStr(leave.getLeaveDate2()), leave.getUserId(), leave.getLeaderId());    
        }
        yhall.updateStatus(dbConn, map);
        //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的请假申请已被批准！");
        sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        
        if(checkLeave!=null&&checkLeave.equals("1")){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的请假申请已被批准！", new Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/attendance/manage/manage.jsp";    
  }
  /*
   * 
   * updateStatus ById
   */
  public String updateReason(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String reason = request.getParameter("reason");
      String userId = request.getParameter("userId");
      reason = reason.replaceAll("\\\n","");
      reason = reason.replaceAll("\\\r","");
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("allow", allow);
      map.put("reason", reason);
      //System.out.println(seqId+allow);
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      yhall.updateStatus(dbConn, map);
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("您的请假申请未被批准！");
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      String checkLeave = request.getParameter("checkLeave");
      if(checkLeave!=null&&checkLeave.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的请假申请未被批准！原因："+reason, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 
   * updateStatus ById
   */
  public String updateDestroyStatus(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String status = request.getParameter("status");
      String userId = request.getParameter("userId");
      String checkLeave = request.getParameter("checkLeave");
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("status", status);
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      yhall.updateStatus(dbConn, map);
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("您的销假申请已被批准，已销假！");
      sb.setRemindUrl("/core/funcs/attendance/personal/index.jsp");
      sb.setToId(userId);
      sb.setFromId(userSeqId);
      YHSmsUtil.smsBack(dbConn, sb);
      
      if(checkLeave!=null&&checkLeave.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的销假申请已被批准，已销假！", new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/attendance/manage/manage.jsp";    
  }
  /*
   * 得到开始日期和结束日期
   * 得到UserId
   */
  public String selectLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
        Connection dbConn = null;
        try {
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
          String userId = request.getParameter("userId");
          String beginTime = request.getParameter("beginTime");
          String endTime = request.getParameter("endTime");
          beginTime = beginTime + " 00:00:00";
          endTime = endTime + " 23:59:59";
          //System.out.println(beginTime+endTime);
          YHManageLeaveLogic leaveLogic = new YHManageLeaveLogic();
          YHManageOutLogic yhaol = new YHManageOutLogic();
          List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
          if(!userId.equals("")){
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            newUserId = newUserId.substring(0, newUserId.length()-1);
            String str[] = {"USER_ID in(" + userId + ")",YHDBUtility.getDateFilter("LEAVE_DATE1", beginTime, ">="),YHDBUtility.getDateFilter("LEAVE_DATE2", endTime, "<="),"ALLOW in('1','3') order by LEAVE_DATE1"};
            leaveList =  leaveLogic.selectLeave(dbConn, str);
          }
       
          String data = "[";
          YHPersonLogic tpl = new YHPersonLogic();
          for (int i = 0; i < leaveList.size(); i++) {
            YHAttendLeave leave = leaveList.get(i);
            String userName = tpl.getNameBySeqIdStr(leave.getUserId(), dbConn);
            if(userName!=null&&!userName.equals("")){
              userName = YHUtility.encodeSpecial(userName);
            }
            String leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
            if(leaderName!=null&&!leaderName.equals("")){
              leaderName = leaderName.substring(0, leaderName.length()-1);
              leaderName = YHUtility.encodeSpecial(leaderName);
            }
            String deptName = yhaol.selectByUserIdDept(dbConn, leave.getUserId());
            data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",userName:\""+userName+"\",leaderName:\"" +leaderName +"\",deptName:\"" + deptName +"\"},";
          }
          if(leaveList.size()>0){
            data = data.substring(0, data.length()-1);
          }
          data = data + "]";
          //System.out.println(data);
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
  /*
   * 更新ById
   */
  public String updateLeaveById(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String leaveType = request.getParameter("leaveType");
      String annualLeave = request.getParameter("annualLeave");
      String destroyTime = request.getParameter("destroyTime");
      leaveType = leaveType.replaceAll("\\\n","");
      leaveType = leaveType.replaceAll("\\\r","");
      YHAttendLeave leave = new YHAttendLeave();
      YHAttendLeaveLogic leaveLogic = new YHAttendLeaveLogic();
      leave = leaveLogic.selectLeaveById(dbConn, seqId);
      leave.setLeaveType(leaveType);
      if(!leaveDate1.equals("")){
        leave.setLeaveDate1(YHUtility.parseDate(leaveDate1));
      }else{
        leave.setLeaveDate1(null);
      }
      if(!leaveDate2.equals("")){
        leave.setLeaveDate2(YHUtility.parseDate(leaveDate2));
      }else{
        leave.setLeaveDate2(null);
      }
      if(!destroyTime.equals("")){
        leave.setDestroyTime(YHUtility.parseDate(destroyTime));
      }else{
        leave.setDestroyTime(null);
      }
      leaveLogic.updateLeave(dbConn, leave);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
