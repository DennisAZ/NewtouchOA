package yh.core.funcs.attendance.personal.act;

import java.net.InetAddress;
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
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
public class YHAttendLeaveAct {
  
  private YHAttendLeaveLogic logic = new YHAttendLeaveLogic();
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
      YHAttendLeave leave = new YHAttendLeave();
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String leaveDays = request.getParameter("leaveDays");
      String smsRemind = request.getParameter("smsRemind");
      String userSeqId = request.getParameter("user");
      if(userSeqId!=null&&!userSeqId.equals("")){
        userId = Integer.parseInt(userSeqId);
      }
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
      YHFOM fom = new YHFOM();
      leave = (YHAttendLeave) fom.build(request.getParameterMap());
      String leaveType = request.getParameter("leaveType");
      leaveType = leaveType.replaceAll("\\\n","");
      leaveType = leaveType.replaceAll("\\\r","");
   
      leave.setRegisterIp(registerIp);
      leave.setLeaveDays(Double.parseDouble(leaveDays));
      leave.setAllow("0");
      //leave.setStatus("1");
      leave.setUserId(String.valueOf(userId));
      leave.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate1));
      leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
      double hour = this.logic.getHourDiff(leaveDate1, leaveDate2, "yyyy-MM-dd HH:mm:ss");
      if(leave.getLeaveDate2().compareTo(curDate)<=0){
        leaveType = "补假: " + leaveType;
      }
      leave.setLeaveType(leaveType);
      leave.setHour(hour);
      yhall.addLeave(dbConn, leave);
      YHFlowHookUtility ut = new YHFlowHookUtility();
      int attendLeaveId = ut.getMax(dbConn, "select max(SEQ_ID) FROM oa_attendance_off");
      Map dataArray = new HashMap();
      dataArray.put("KEY", attendLeaveId + "");
      dataArray.put("FIELD", "LEAVE_ID");
      dataArray.put("USER_ID", leave.getUserId());
      YHPersonLogic p = new YHPersonLogic();
      String userName = p.getUserNameLogic(dbConn, Integer.parseInt(leave.getUserId()));
      dataArray.put("USER_NAME", userName);
      dataArray.put("LEAVE_TYPE", leaveType);
      dataArray.put("LEAVE_DATE1", leaveDate1);
      dataArray.put("LEAVE_DATE2", leaveDate2);
      dataArray.put("ANNUAL_LEAVE", leave.getAnnualLeave() + "");
      dataArray.put("LEADER_ID", leave.getLeaderId());
      String url = ut.runHook(dbConn, user, dataArray, "attend_leave");
      if (!"".equals(url)) {
        String path = request.getContextPath();
        response.sendRedirect(path+ url);
        return null;
      }
      
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
    response.sendRedirect(path+ "/core/funcs/attendance/personal/leave.jsp");
    return null;
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
  public String selectLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendLeave leave = new YHAttendLeave();
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      List<YHAttendLeave> leaveList = yhall.selectLeave(dbConn, userId);
      String data = "[";
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        leave = leaveList.get(i);
        //System.out.println(leave.getDestroyTime());
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        int runId = fu.isRunHook(dbConn, "LEAVE_ID", leave.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+ flowId +"\",leaderName:\"" + leaderName + "\"},";
      }
      if(leaveList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      //得到今年已请假多少天
      long leaveDaysTotal = yhall.selectLeaveDaysByUserId(dbConn, userId + "");
      String leaveDaysTotalStr = getDateTimeStr(leaveDaysTotal);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,leaveDaysTotalStr );
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据时长后转换为天 时
   * @return
   */
  public String getDateTimeStr(long dateTime){
    String dateStr = "";
    long day = 0;
    day = dateTime/(24*3600);
    if(day>0){
      dateStr = dateStr + day + "天";
    }
    long hour = 0;
    hour = dateTime - (day * 24*3600);
    if(hour>0){
      dateStr = dateStr + hour + "时";
    }
    return dateStr ;
  }
  /**
   * 
   * 查询所有历史请假记录根据自己的ID
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
      YHAttendLeave leave = new YHAttendLeave();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String whereStr = "";
      String ymd = year + "-" + month + "-" + "01";
      if(!YHUtility.isNullorEmpty(year) || !YHUtility.isNullorEmpty(month)){
        whereStr += " and " + YHDBUtility.getMonthFilter("LEAVE_DATE1", YHUtility.parseDate(ymd));
      }
      String data = "[";
      Map map = new HashMap();
      map.put("USER_ID", userId);
      //map.put("STATUS", "2");
      String[] str = {"USER_ID='"+userId + "' and ALLOW = '1'" + whereStr + " order by LEAVE_DATE1 desc"};
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      List<YHAttendLeave> leaveList = yhall.selectHistroyLeave(dbConn, str);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonLogic tpl = new YHPersonLogic();
        leave = leaveList.get(i);
        //System.out.println(leave.getDestroyTime());
        String leaderName = "";
        leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
        if(leaderName!=null&&!leaderName.equals("")){
          leaderName = YHUtility.encodeSpecial(leaderName);
        }
        int runId = fu.isRunHook(dbConn, "LEAVE_ID", leave.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        data = data + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+ flowId +"\",leaderName:\"" + leaderName + "\"},";
      }
      if(leaveList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
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
      YHAttendLeave leave = new YHAttendLeave();
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      yhall.deleteLeaveById(dbConn, seqId);
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
      YHAttendLeave leave = new YHAttendLeave();
      String seqId = request.getParameter("seqId");
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      leave = yhall.selectLeaveById(dbConn, seqId);
      YHPersonLogic tpl = new YHPersonLogic();
      //System.out.println(leave.getDestroyTime());
      String leaderName = "";
      String data = "";
      if(!YHUtility.isNullorEmpty(leave.getLeaderId())){
        leaderName = tpl.getNameBySeqIdStr(leave.getLeaderId() , dbConn);
      } 
      if(leaderName!=null&&!leaderName.equals("")){
        leaderName = leaderName.substring(0, leaderName.length()-1);
        leaderName = YHUtility.encodeSpecial(leaderName);
      }
      data = data + YHFOM.toJson(leave).toString().substring(0, YHFOM.toJson(leave).toString().length()-1 ) + ",leaderName:\"" + leaderName + "\"}";
      //System.out.println(data);
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
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendLeave leave = new YHAttendLeave();
      Date curDate = new Date();
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDays = request.getParameter("leaveDays");
      //System.out.println(leaveDate1);
      String leaveDate2 = request.getParameter("leaveDate2");
      String smsRemind = request.getParameter("smsRemind");
      //得到客户端的IP地址
      YHAttendDutyAct dutyAct = new YHAttendDutyAct();
      String registerIp = dutyAct.getIpAddr(request);
      InetAddress inet = InetAddress.getLocalHost();
      String localIp = inet.getHostAddress();
      if(registerIp!=null&&registerIp.equals("127.0.0.1")){
        registerIp = localIp;
      }
      YHFOM fom = new YHFOM();
      leave = (YHAttendLeave) fom.build(request.getParameterMap());
      String leaveType = request.getParameter("leaveType");
      leaveType = leaveType.replaceAll("\\\n","");
      leaveType = leaveType.replaceAll("\\\r",""); 
      leave.setRegisterIp(registerIp);
      leave.setLeaveDays(Double.parseDouble(leaveDays));
      leave.setAllow("0");
      leave.setStatus("1");
      leave.setUserId(String.valueOf(userId));
      leave.setReason("");
      leave.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate1));
      leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
      double hour = this.logic.getHourDiff(leaveDate1, leaveDate2, "yyyy-MM-dd HH:mm:ss");
      leave.setHour(hour);
      if(leave.getLeaveDate2().compareTo(curDate)<=0){
        if(!leaveType.trim().startsWith("补假")){
          leaveType = "补假: " + leaveType;
        }
      }
      leave.setLeaveType(leaveType);
      yhall.updateLeave(dbConn, leave);
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
    response.sendRedirect(path+ "/core/funcs/attendance/personal/leave.jsp");
    return "";
  }
  /**
   * 
   * 更新请假记录status
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateLeaveStatus(HttpServletRequest request,
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
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      YHAttendLeave leave = new YHAttendLeave();
      leave = yhall.selectLeaveById(dbConn, seqId);
      leave.setAllow(allow);
      leave.setDestroyTime(YHUtility.parseDate(dateStr));
      yhall.updateLeave(dbConn, leave);
   
      //短信smsType, content, remindUrl, toId, fromId
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("6");
      sb.setContent("提交销假申请，请批示！");
      sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
      sb.setToId(leave.getLeaderId());
      sb.setFromId(userId);
      YHSmsUtil.smsBack(dbConn, sb);
      if(checkLeave!=null&&checkLeave.equals("1")){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leave.getLeaderId(), userId, "提交请假销假，请批示！", new Date());
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
   * 展示自动补登记、不需要审核日期--cc 20101126
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showMonth(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String data = "";
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      data = yhall.showTimeStr(dbConn, person, beginDate, endDate);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 请假总时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAttendLeaveHour(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
      YHAttendLeaveLogic all = new YHAttendLeaveLogic();
      double data = this.logic.getAttendLeaveHourLogic(dbConn, year, month, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
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
      YHAttendLeaveLogic all = new YHAttendLeaveLogic();
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
}
