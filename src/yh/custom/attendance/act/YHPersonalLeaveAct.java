package yh.custom.attendance.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.logic.YHAttendLeaveLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHPersonalLeave;
import yh.custom.attendance.logic.YHPersonalLeaveLogic;

public class YHPersonalLeaveAct {
  /**
   * 
   * 添加请假记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonalLeave leave = new YHPersonalLeave();
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String smsRemind = request.getParameter("smsRemind");
      String leaveType = request.getParameter("leaveType");
      String userSeqId = request.getParameter("user");
      YHFOM fom = new YHFOM();
      leave = (YHPersonalLeave) fom.build(request.getParameterMap());
      leave.setAllow("0");
      leave.setStatus("1");
      leave.setUserId(String.valueOf(userId));
      if(!YHUtility.isNullorEmpty(leaveDate1)){
        leave.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate1));
      }
      if(!YHUtility.isNullorEmpty(leaveDate2)){
        leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
      }
      leave.setApplyTime(new Date());
      logic.addLeave(dbConn, leave);
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交请假申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(leave.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leave.getLeaderId(), userId, "提交请假申请，请批示:" + leaveType, new Date());
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
    response.sendRedirect(path+ "/custom/attendance/personal/leave/index.jsp");
    return "";
  }
  /**
   * 
   * 更新请假记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonalLeave leave = new YHPersonalLeave();
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String smsRemind = request.getParameter("smsRemind");
      String leaveType = request.getParameter("leaveType");
      String userSeqId = request.getParameter("user");
      String seqId = request.getParameter("seqId");
      YHFOM fom = new YHFOM();
      leave = (YHPersonalLeave) fom.build(request.getParameterMap());
      if(!YHUtility.isNullorEmpty(seqId)){
        leave.setAllow("0");
        leave.setStatus("1");
        leave.setSeqId(Integer.parseInt(seqId));
        leave.setUserId(String.valueOf(userId));
        if(!YHUtility.isNullorEmpty(leaveDate1)){
          leave.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate1));
        }
        if(!YHUtility.isNullorEmpty(leaveDate2)){
          leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
        }
        leave.setApplyTime(new Date());
        logic.updateLeave(dbConn, leave);
        //短信smsType, content, remindUrl, toId, fromId
        if(smsRemind!=null){
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("6");
          sb.setContent("提交请假申请，请批示！");
          sb.setRemindUrl("/custom/attendance/attendmanage/index.jsp");
          sb.setToId(leave.getLeaderId());
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
        String moblieSmsRemind = request.getParameter("moblieSmsRemind");
        if(moblieSmsRemind!=null){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,leave.getLeaderId(), userId, "提交请假申请，请批示:" + leaveType, new Date());
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
    response.sendRedirect(path+ "/custom/attendance/personal/leave/index.jsp");
    return "";
  }
  /**
   * 
   * 查询所有请假记录根据自己的UserID
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "(STATUS = '1' and (ALLOW = '0' or ALLOW = '2') )"};
      List<YHPersonalLeave> leaveList = logic.selectLeave(dbConn, str);
      String data = "[";
      YHPersonLogic tpl = new YHPersonLogic();
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonalLeave leave = leaveList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",leaderName:\"" + leaderName + "\"},";
      }
      if(leaveList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询所有请假记录根据自己的ID(已销假的/历史 记录)
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectHistroyLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "ALLOW = '1'"};
      List<YHPersonalLeave> leaveList = logic.selectLeave(dbConn, str);
      String data = "[";
      YHPersonLogic tpl = new YHPersonLogic();
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonalLeave leave = leaveList.get(i);
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",leaderName:\"" + leaderName + "\"},";
      }
      if(leaveList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 删除请假记录根据ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteLeaveById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      if(!YHUtility.isNullorEmpty(seqId)){
        logic.deleteLeaveById(dbConn, seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询所有请假记录根据自己的ID
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectLeaveById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     
      String seqId = request.getParameter("seqId");
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      String data = "";
      if(!YHUtility.isNullorEmpty(seqId)){
        YHPersonalLeave leave = logic.selectLeaveById(dbConn, seqId);
        if(leave!=null){
          YHPersonLogic tpl = new YHPersonLogic();
          String leaderName = "";
          if(!YHUtility.isNullorEmpty(leave.getLeaderId())){
            leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
          } 
          if(leaderName!=null&&!leaderName.equals("")){
            leaderName = leaderName.substring(0, leaderName.length()-1);
            leaderName = YHUtility.encodeSpecial(leaderName);
          }
          data = data + YHFOM.toJson(leave).toString().substring(0, YHFOM.toJson(leave).toString().length()-1 ) + ",leaderName:\"" + leaderName + "\"}";
        }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
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
  public String updateAllowReason(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkLeave = request.getParameter("checkLeave");
      String reason = request.getParameter("reason");
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      if (!YHUtility.isNullorEmpty(seqId)) {
        logic.updateLeaveAllow(dbConn, seqId, allow, reason);
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的请假申请未被批准！");
        sb.setRemindUrl("/custom/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        // 发送手机短信
        if (checkLeave != null && checkLeave.equals("1")) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, userId, userSeqId, "您的请假申请未被批准！",new Date());
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
   * 更改状态同意
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAllowReasonOn(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String userId = request.getParameter("userId");
      String checkLeave = request.getParameter("checkLeave");
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      if (!YHUtility.isNullorEmpty(seqId)) {
        logic.updateLeaveAllow(dbConn, seqId, allow, "");
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的请假申请已被批准！");
        sb.setRemindUrl("/custom/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        // 发送手机短信
        if (checkLeave != null && checkLeave.equals("1")) {
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn, userId, userSeqId, "您的请假申请已被批准！",new Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/custom/attendance/attendmanage/leadermanage/index.jsp";
  }
  
  /**
   * 
   * 更新请假记录allow,申请销假
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAllow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String checkLeave = request.getParameter("checkLeave");
      String dateStr = YHUtility.getDateTimeStr(new Date());
      YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
      if (!YHUtility.isNullorEmpty(seqId)) {
        YHPersonalLeave personLeave = logic.selectLeaveById(dbConn, seqId);
        if(personLeave!=null){
          personLeave.setAllow(allow);
          personLeave.setDestroyTime(new Date());
          logic.updateLeave(dbConn, personLeave);
        //短信smsType, content, remindUrl, toId, fromId
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("6");
          sb.setContent("提交销假申请，请批示！");
          sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
          sb.setToId(personLeave.getLeaderId());
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
          if(checkLeave!=null&&checkLeave.equals("1")){
            YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
            sms2Logic.remindByMobileSms(dbConn,personLeave.getLeaderId(), userId, "提交请假销假，请批示！", new Date());
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/attendance/personal/leave.jsp";
  }
  /**
   * 
   * 更新请假记录status,审批人员销假
   * @param request
   * @param response
   * @return
   * @throws Exception
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
          YHPersonalLeaveLogic logic = new YHPersonalLeaveLogic();
          if (!YHUtility.isNullorEmpty(seqId)) {
            logic.updateLeaveStatus(dbConn, seqId, status);
            //短信smsType, content, remindUrl, toId, fromId
            YHSmsBack sb = new YHSmsBack();
            sb.setSmsType("6");
            sb.setContent("您的销假申请已被批准，已销假！");
            sb.setRemindUrl("/custom/attendance/personal/index.jsp");
            sb.setToId(userId);
            sb.setFromId(userSeqId);
            YHSmsUtil.smsBack(dbConn, sb);
            
            if(checkLeave!=null&&checkLeave.equals("1")){
              YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
              sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的销假申请已被批准，已销假！", new Date());
            }
          }
         
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
        }catch(Exception ex) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
          throw ex;
        }
        return "/custom/attendance/attendmanage/leadermanage/index.jsp"; 
      }
}
