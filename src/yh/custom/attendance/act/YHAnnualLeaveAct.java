package yh.custom.attendance.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHAnnualLeave;
import yh.custom.attendance.data.YHPersonAnnualPara;
import yh.custom.attendance.logic.YHAnnualLeaveLogic;
import yh.custom.attendance.logic.YHPersonAnnualParaLogic;
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;

public class YHAnnualLeaveAct {
  /**
   * 
   * 添加年休假记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addAnnualLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      System.out.println("");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAnnualLeave leave = new YHAnnualLeave();
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String smsRemind = request.getParameter("smsRemind");
      String leaveDays = request.getParameter("leaveDays");
      YHFOM fom = new YHFOM();
      leave = (YHAnnualLeave) fom.build(request.getParameterMap());
      leave.setAllow("0");
      leave.setStatus("1");
      leave.setUserId(String.valueOf(userId));
      if(!YHUtility.isNullorEmpty(leaveDate1)){
        leave.setLeaveDate1(dateFormat.parse(leaveDate1));
      }
      if(!YHUtility.isNullorEmpty(leaveDate2)){
        leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
      }
      leave.setApplyTime(curDate);
      if(YHUtility.isInteger(leaveDays)){
        leave.setLeaveDays(Integer.parseInt(leaveDays)); 
      }
     
      logic.addLeave(dbConn, leave);
      //短信smsType, content, remindUrl, toId, fromId
      if(smsRemind!=null){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("提交年休假申请，请批示！");
        sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp");
        sb.setToId(leave.getLeaderId());
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if(moblieSmsRemind!=null){
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn,leave.getLeaderId(), userId, "提交年休假申请，请批示:", new Date());
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
    response.sendRedirect(path+ "/custom/attendance/personal/annualleave/index.jsp");
    return "";
  }
  /**
   * 
   * 更新年休假记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAnnualLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAnnualLeave leave = new YHAnnualLeave();
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String leaveDate1 = request.getParameter("leaveDate1");
      String leaveDate2 = request.getParameter("leaveDate2");
      String smsRemind = request.getParameter("smsRemind");
      String leaveDays = request.getParameter("leaveDays");
      String seqId =  request.getParameter("seqId");
      if(!YHUtility.isNullorEmpty(seqId)){
        YHFOM fom = new YHFOM();
        leave = (YHAnnualLeave) fom.build(request.getParameterMap());
        leave.setAllow("0");
        leave.setStatus("1");
        leave.setSeqId(Integer.parseInt(seqId));
        leave.setUserId(String.valueOf(userId));
        if(!YHUtility.isNullorEmpty(leaveDate1)){
          leave.setLeaveDate1(dateFormat.parse(leaveDate1));
        }
        if(!YHUtility.isNullorEmpty(leaveDate2)){
          leave.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",leaveDate2));
        }
        leave.setApplyTime(curDate);
        if(YHUtility.isInteger(leaveDays)){
          leave.setLeaveDays(Integer.parseInt(leaveDays)); 
        }
       
        logic.updateLeave(dbConn, leave);
        //短信smsType, content, remindUrl, toId, fromId
        if(smsRemind!=null){
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("6");
          sb.setContent("提交年休假申请，请批示！");
          sb.setRemindUrl("/custom/attendance/attendmanage/index.jsp");
          sb.setToId(leave.getLeaderId());
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
        String moblieSmsRemind = request.getParameter("moblieSmsRemind");
        if(moblieSmsRemind!=null){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,leave.getLeaderId(), userId, "提交年休假申请，请批示:" , new Date());
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
    response.sendRedirect(path+ "/custom/attendance/personal/annualleave/index.jsp");
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
  public String selectAnnualLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "(STATUS = '1' and (ALLOW = '0' or ALLOW = '2') )"};
      List<YHAnnualLeave> leaveList = logic.selectLeave(dbConn, str);
      String data = "[";
      YHPersonLogic tpl = new YHPersonLogic();
      for (int i = 0; i < leaveList.size(); i++) {
        YHAnnualLeave leave = leaveList.get(i);
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
  public String selectHistroyAnnualLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String whereStr = "";
      String ymd = year + "-" + month + "-" + "01";
      if(!YHUtility.isNullorEmpty(year) || !YHUtility.isNullorEmpty(month)){
        whereStr += " and " + YHDBUtility.getMonthFilter("LEAVE_DATE1", YHUtility.parseDate(ymd));
      }
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      String[] str = {"USER_ID = '" + userId + "' and ALLOW = '1'" + whereStr};
      List<YHAnnualLeave> leaveList = logic.selectLeave(dbConn, str);
      String data = "[";
      YHPersonLogic tpl = new YHPersonLogic();
      for (int i = 0; i < leaveList.size(); i++) {
        YHAnnualLeave leave = leaveList.get(i);
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
  public String deleteAnnualLeaveById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
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
  public String selectAnnualLeaveById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     
      String seqId = request.getParameter("seqId");
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      String data = "";
      if(!YHUtility.isNullorEmpty(seqId)){
        YHAnnualLeave leave = logic.selectLeaveById(dbConn, seqId);
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
   * 更改出差状态(批准)
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
      int userSeqId = user.getSeqId(); 
      String seqId = request.getParameter("seqId"); 
      String allow = request.getParameter("allow"); 
      String userId = request.getParameter("userId"); 
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic(); 
      if(!YHUtility.isNullorEmpty(seqId)){ 
      logic.updateLeaveAllow(dbConn, seqId, allow, ""); 
      //配合陈晨年休假自动补登记 
      YHAnnualLeave leave = logic.selectLeaveById(dbConn, seqId); 
      YHAttendFillLogic fillLogic = new YHAttendFillLogic(); 
      fillLogic.addAttendScoreYear(dbConn, user, YHUtility.getDateTimeStr(leave.getLeaveDate1()), YHUtility.getDateTimeStr(leave.getLeaveDate2()),userId ,user.getSeqId()+""); 

      //短信smsType, content, remindUrl, toId, fromId 
      YHSmsBack sb = new YHSmsBack(); 
      sb.setSmsType("6"); 
      sb.setContent("您的年休假申请已被批准！"); 
      sb.setRemindUrl("/core/funcs/attendance/manage/index.jsp"); 
      sb.setToId(userId); 
      sb.setFromId(userSeqId); 
      YHSmsUtil.smsBack(dbConn, sb); 
      String checkEvection = request.getParameter("checkEvection"); 
      if(checkEvection!=null&&checkEvection.equals("1")){ 
      YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic(); 
      sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的年休假申请已被批准", new Date()); 
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
  /**
   * 外出不批准(说明理由)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateAllowReason(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      String notReason = request.getParameter("notReason");
      String userId = request.getParameter("userId");
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      if(!YHUtility.isNullorEmpty(seqId)){
        logic.updateLeaveAllow(dbConn, seqId, allow, notReason);
        //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("6");
        sb.setContent("您的年休假申请未被批准！");
        sb.setRemindUrl("/custom/attendance/personal/index.jsp");
        sb.setToId(userId);
        sb.setFromId(userSeqId);
        YHSmsUtil.smsBack(dbConn, sb);
        
        String checkEvection = request.getParameter("checkEvection");
        if(checkEvection!=null&&checkEvection.equals("1")){
          YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
          sms2Logic.remindByMobileSms(dbConn,userId, userSeqId, "您的年休假申请未被批准！内容："+notReason, new Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取年休假剩余天数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAnnualOverplus(HttpServletRequest request,
  HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId(); 
      String userIdStr = request.getParameter("userIdStr");
      if(!YHUtility.isNullorEmpty(userIdStr)){
        userId = Integer.parseInt(userIdStr);
      }
      String yearStr = request.getParameter("year");
      //得到用户的年休假
      YHPersonAnnualParaLogic logic = new YHPersonAnnualParaLogic();
      String[] str = {"USER_ID = '" + userId + "'"};
      List< YHPersonAnnualPara> annualList = logic.selectAnnualLeavePara(dbConn, str);
      int annualDays = 0;
      if(annualList.size()>0){
        YHPersonAnnualPara annualPara = annualList.get(0);
        annualDays = annualPara.getAnnualDays();
      }
      
      //得到今年已请年休假的总天数
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      if(YHUtility.isNullorEmpty(yearStr)){
        yearStr = String.valueOf(year);
      }
      YHAnnualLeaveLogic leaveLogic = new YHAnnualLeaveLogic();
      
      //已请年休假
      String leaveTotal = leaveLogic.selectPersonAnnualDays(dbConn, userId +"", yearStr);
      
      //已请假
//      String leaveSum = leaveLogic.selectPersonLeaveDays(dbConn, String.valueOf(userId), yearStr);
      
      //得到年休假剩余//      double leaveCount = 0;
//      if(!YHUtility.isNullorEmpty(leaveSum)){
//        leaveCount = Double.parseDouble(leaveSum);
//      }
      double OverplusDays = annualDays - Integer.parseInt(leaveTotal) ;//- leaveCount;
      double leaveDays = Integer.parseInt(leaveTotal) ;//+ leaveCount;
      
      String data = "{overplusDays:" + OverplusDays + ",leaveDays:"+leaveDays+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
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
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      String data = "";
      YHAnnualLeaveLogic all = new YHAnnualLeaveLogic();
      data = all.showTimeStr(dbConn, beginDate, endDate);
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
  
  
  public String getAnnualLeaveDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = String.valueOf(person.getSeqId());
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      YHAnnualLeaveLogic logic = new YHAnnualLeaveLogic();
      double data = logic.getAnnualLeaveDayLogic(dbConn, year, month, userId);
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
