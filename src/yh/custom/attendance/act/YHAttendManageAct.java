package yh.custom.attendance.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.custom.attendance.data.YHAnnualLeave;
import yh.custom.attendance.data.YHDuty;
import yh.custom.attendance.data.YHOvertimeRecord;
import yh.custom.attendance.data.YHPersonAnnualPara;
import yh.custom.attendance.data.YHPersonalLeave;
import yh.custom.attendance.logic.YHAnnualLeaveLogic;
import yh.custom.attendance.logic.YHDutyLogic;
import yh.custom.attendance.logic.YHOvertimeRecordLogic;
import yh.custom.attendance.logic.YHPersonAnnualParaLogic;
import yh.custom.attendance.logic.YHPersonalLeaveLogic;

public class YHAttendManageAct {
  /**
   * 
   * 查询所有是自己审批的加班审批 + 请假审批 + 年休假审批
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectAttendLeader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat formatter2 = new SimpleDateFormat("E"); 
      int userId = user.getSeqId();
      YHPersonLogic tpl = new YHPersonLogic();
      //加班审批
      String[] overtimeStr = {"LEADER_ID = '" + userId + "'" , "STATUS = '0'"};
      YHOvertimeRecordLogic overtimeLogic = new YHOvertimeRecordLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      overList = overtimeLogic.selectOvertime(dbConn, overtimeStr);
      YHFlowHookUtility fu = new YHFlowHookUtility();
      YHFlowRunUtility ru = new YHFlowRunUtility();
      String overtimeJson = "[";
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        String applyName = "";
        applyName = tpl.getNameBySeqIdStr(overtime.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        long overtimeChar = 0;
        if(overtime.getBeginTime()!= null){
          String beginDate = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " + overtime.getBeginDate();
          String endDate = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " + overtime.getEndDate();
          overtimeChar = getDateTime(YHUtility.parseDate(beginDate),YHUtility.parseDate(endDate));
        }
        String deptName = YHUtility.encodeSpecial(yhaol.selectByUserIdDept(dbConn, overtime.getUserId()));
        int runId = fu.isRunHook(dbConn, "OVERTIME_ID", overtime.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        
        overtimeJson = overtimeJson + YHFOM.toJson(overtime).toString().substring(0, YHFOM.toJson(overtime).toString().length()-1 ) + ",isHookRun:\""+runId+"\",flowId:\""+flowId+"\",applyName:\"" + applyName+ "\",deptName:\"" + deptName + "\",overtimeChar:\"" + overtimeChar + "\"},";
      }
      if(overList.size()>0){
        overtimeJson = overtimeJson.substring(0, overtimeJson.length()-1);
      }
      overtimeJson = overtimeJson + "]" ;
      
    //值班审批
      String[] dutyStr = {"LEADER_ID = '" + userId + "'" , "STATUS = '0'"};
      YHDutyLogic dutyLogic = new YHDutyLogic();
      List<YHDuty> dutyList = new ArrayList<YHDuty>();
      dutyList = dutyLogic.getDutyList(dbConn, dutyStr);
      String dutyJson = "[";
      for (int i = 0; i < dutyList.size(); i++) {
        YHDuty duty = dutyList.get(i);
        String applyName = "";
        applyName = tpl.getNameBySeqIdStr(duty.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        long overtimeChar = 0;
        if(duty.getDutyTime()!= null){
          String beginDate = String.valueOf(duty.getDutyTime()).substring(0, 10) + " " + duty.getBeginDate();
          String endDate = String.valueOf(duty.getDutyTime()).substring(0, 10) + " " + duty.getEndDate();
          overtimeChar = getDateTime(YHUtility.parseDate(beginDate),YHUtility.parseDate(endDate));
        }
        String deptName = YHUtility.encodeSpecial(yhaol.selectByUserIdDept(dbConn, duty.getUserId()));
        dutyJson = dutyJson + YHFOM.toJson(duty).toString().substring(0, YHFOM.toJson(duty).toString().length()-1 ) + ",applyName:\"" + applyName+ "\",deptName:\"" + deptName + "\",overtimeChar:\"" + overtimeChar + "\"},";
      }
      if(dutyList.size()>0){
        dutyJson = dutyJson.substring(0, dutyJson.length()-1);
      }
      dutyJson = dutyJson + "]" ;
      
      //请假审批
      YHPersonalLeaveLogic leaveLogic = new YHPersonalLeaveLogic();
      String[] str = {"LEADER_ID = '" + userId + "'" , "(STATUS = '1' and (ALLOW = '0' or ALLOW = '3'))"};
      List<YHPersonalLeave> leaveList = leaveLogic.selectLeave(dbConn, str);
      String leaveJson = "[";
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonalLeave leave = leaveList.get(i);
        String applyName = "";
        applyName = tpl.getNameBySeqIdStr(leave.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        long leaveChar = 0;
        if(leave.getLeaveDate1()!=null&&leave.getLeaveDate2()!=null){
          leaveChar = getDateTime(leave.getLeaveDate1(),leave.getLeaveDate2());
        }
        String deptName = YHUtility.encodeSpecial(yhaol.selectByUserIdDept(dbConn, leave.getUserId()));
        leaveJson = leaveJson + YHFOM.toJson(leaveList.get(i)).toString().substring(0, YHFOM.toJson(leaveList.get(i)).toString().length()-1 ) + ",applyName:\"" + applyName + "\",deptName:\"" + deptName + "\",leaveChar:\"" + leaveChar +"\"},";
      }
      if(leaveList.size()>0){
        leaveJson = leaveJson.substring(0, leaveJson.length()-1);
      }
      leaveJson = leaveJson + "]";
      //年休假审批
      YHAnnualLeaveLogic annuallogic = new YHAnnualLeaveLogic();
      String[] annualStr = {"LEADER_ID = '" + userId + "'" , "(STATUS = '1' and (ALLOW = '0' or ALLOW = '3'))"};
      List<YHAnnualLeave> annualList = annuallogic.selectLeave(dbConn, annualStr);
      String annualJson = "[";
      for (int i = 0; i < annualList.size(); i++) {
        YHAnnualLeave leave = annualList.get(i);
        String applyName = "";
        applyName = tpl.getNameBySeqIdStr(leave.getUserId() , dbConn);
        if(applyName!=null&&!applyName.equals("")){
          applyName = YHUtility.encodeSpecial(applyName);
        }
        String deptName = YHUtility.encodeSpecial(yhaol.selectByUserIdDept(dbConn, leave.getUserId()));
        //得到用户的年休假
        YHPersonAnnualParaLogic logic = new YHPersonAnnualParaLogic();
        String[] annualParaStr = {"USER_ID = '" + leave.getUserId() + "'"};
        List< YHPersonAnnualPara> annualParaList = logic.selectAnnualLeavePara(dbConn, annualParaStr);
        int annualDays = 0;
        if(annualParaList.size()>0){
          YHPersonAnnualPara annualPara = annualParaList.get(0);
          annualDays = annualPara.getAnnualDays();
        }
        //得到今年已请年休假的总天数
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String leaveTotal = annuallogic.selectPersonAnnualDays(dbConn, leave.getUserId(), year+"");
        //得到年休假剩余
        int overPlusDays = annualDays - Integer.parseInt(leaveTotal);
        annualJson = annualJson + YHFOM.toJson(leave).toString().substring(0, YHFOM.toJson(leave).toString().length()-1 ) + ",applyName:\"" + applyName +  "\",deptName:\"" + deptName +  "\",annualDays:" + annualDays + ",leaveTotal:"+leaveTotal + ",overPlusDays:" + overPlusDays+ "},";
      }
      if(annualList.size()>0){
        annualJson = annualJson.substring(0, annualJson.length()-1);
      }
      annualJson = annualJson + "]";
      String data = "{overtimeJson:" + overtimeJson + ",leaveJson:" + leaveJson + ",annualJson:" + annualJson + ",dutyJson:" + dutyJson + "}";
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
   * 得到本用户在本月 的加班记录和请假记录，进行统计时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectPersonAttend(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat formatter2 = new SimpleDateFormat("E"); 
      int userId = user.getSeqId();
      YHPersonLogic tpl = new YHPersonLogic();
      //本月加班记录
      String[] overtimeStr = {"USER_ID = '" + userId + "'" , "STATUS = '1'",YHDBUtility.getMonthFilter("BEGIN_TIME", new Date())};
      YHOvertimeRecordLogic overtimeLogic = new YHOvertimeRecordLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
      overList = overtimeLogic.selectOvertime(dbConn, overtimeStr);
      long overtimeTotal = 0; 
      for (int i = 0; i < overList.size(); i++) {
        YHOvertimeRecord overtime = overList.get(i);
        if(overtime.getBeginTime() != null){
          String beginDate = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " +overtime.getBeginDate();
          String endDate = String.valueOf(overtime.getBeginTime() ).substring(0, 10) + " " +overtime.getEndDate();
          overtimeTotal = overtimeTotal + getDateTime(YHUtility.parseDate(beginDate) ,YHUtility.parseDate(endDate));
        }
      }
      
      //请假审批
      YHPersonalLeaveLogic leaveLogic = new YHPersonalLeaveLogic();
      String[] str = {"USER_ID = '" + userId + "'" , "(STATUS = '1' and (ALLOW = '1'))",YHDBUtility.getMonthFilter("LEAVE_DATE2", new Date())};
      List<YHPersonalLeave> leaveList = leaveLogic.selectLeave(dbConn, str);
      long leaveTotal = 0;;
      for (int i = 0; i < leaveList.size(); i++) {
        YHPersonalLeave leave = leaveList.get(i);
        if(leave.getLeaveDate1()!=null&&leave.getLeaveDate2()!=null){
          leaveTotal = leaveTotal + getDateTime(leave.getLeaveDate1(), leave.getLeaveDate2());
        }
      }
      //本月加班时长-本月请假时长
      long overtime_leave = overtimeTotal - leaveTotal;
      String data = "{overtimeTotal:" + overtimeTotal + ",leaveTotal:" + leaveTotal + ",overtime_leave:\"" + overtime_leave + "\",userName:\"" + YHUtility.encodeSpecial(user.getUserName()) + "\"}";
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
   * 根据两个日期得到相隔多长时长精确到分
   * @param date1
   * @param date2
   * @return
   */
  public long getDateTime(Date date1, Date date2){
    long  dateTime1 = date1.getTime(); 
    long  dateTime2 = date2.getTime(); 
    long  dateTime = (dateTime2 - dateTime1)/(1000*60);//精确到分
    return dateTime;
  }
  /**
   * 根据时长后转换为时分
   * @return
   */
  public String getDateTimeStr(long dateTime){
    String dateStr = "";
    long hour = 0;
    hour = dateTime/60;
    if(hour>0){
      dateStr = dateStr + hour + "时";
    }
    long minute = 0;
    minute = dateTime - (hour * 60);
    if(minute>0){
      dateStr = dateStr + minute + "分";
    }
    return dateStr ;
  }
  /**
   * 根据两个日期得到相隔多长时长精确到 dd->天 HH->时 mm->分
   * @param date1
   * @param date2
   * @return
   */
  public long getDateTime(Date date1, Date date2,String type){
    long  dateTime1 = date1.getTime(); 
    long  dateTime2 = date2.getTime(); 
    long  dateTime = 0 ;
    if(type.equals("mm")){
      dateTime = (dateTime2 - dateTime1)/(1000*60);//精确到分
    }else if(type.equals("HH")){
      dateTime = (dateTime2 - dateTime1)/(1000*3600);//精确到时
    }else if(type.equals("dd")){
      dateTime = (dateTime2 - dateTime1)/(1000*3600*24);//精确到天
    }else{
      dateTime = dateTime2 - dateTime1;//精确到秒
    }
      

    return dateTime;
  }
  /**
   * 根据时长后转换为 dd->天 HH->时 mm->分
   * @return
   */
  public String getDateTimeStr(long dateTime,String type){
    String dateStr = "";
    long hour = 0;
    hour = dateTime/60;
    if(hour>0){
      dateStr = dateStr + hour + "时";
    }
    long minute = 0;
    minute = dateTime - (hour * 60);
    if(minute>0){
      dateStr = dateStr + minute + "分";
    }
    return dateStr ;
  } 
  /**
   * 
   * 根据日期，用户得到加班记录和请假记录，进行统计时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectAttendByUserDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userIds = request.getParameter("userIds");//得到指定部门的所有的ID
      String beginDate = request.getParameter("beginDate");
      String endDate = request.getParameter("endDate");
      endDate = endDate + " 23:59:59";
      String[] userIdArray = {};
      if(!userIds.equals("")){
        userIdArray = userIds.split(",");
      }
      
      YHPersonLogic tpl = new YHPersonLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      YHOvertimeRecordLogic overtimeLogic = new YHOvertimeRecordLogic();
      YHPersonalLeaveLogic leaveLogic = new YHPersonalLeaveLogic();
      String data = "[";
      //对所有用户循环
      for (int i = 0; i < userIdArray.length; i++) {
        String deptName = yhaol.selectByUserIdDept(dbConn, userIdArray[i]);//得到用户的 部门 
        String userName = tpl.getNameBySeqIdStr(userIdArray[i],dbConn);
        //得到 加班记录时长
        String[] overtimeStr = {"USER_ID = '" +  userIdArray[i] + "'" , "STATUS = '1'",YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">="),YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=")};

        List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
        overList = overtimeLogic.selectOvertime(dbConn, overtimeStr);
        long overtimeTotal = 0; 
        for (int j = 0; j < overList.size(); j++) {
          YHOvertimeRecord overtime = overList.get(j);
          if(overtime.getBeginTime() != null){
            String beginDates = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " +overtime.getBeginDate();
            String endDates = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " +overtime.getEndDate();
            overtimeTotal = overtimeTotal + getDateTime(YHUtility.parseDate(beginDates), YHUtility.parseDate(endDates));
          }
        }
        

        //得到 请假时长 

        String[] str = {"USER_ID = '" + userIdArray[i] + "'" , "(STATUS = '1' and (ALLOW = '1'))",YHDBUtility.getDateFilter("LEAVE_DATE1", beginDate, ">="),YHDBUtility.getDateFilter("LEAVE_DATE2", endDate, "<=")};
        List<YHPersonalLeave> leaveList = leaveLogic.selectLeave(dbConn, str);
        long leaveTotal = 0;;
        for (int j  = 0; j < leaveList.size(); j++) {
          YHPersonalLeave leave = leaveList.get(j);
          if(leave.getLeaveDate1()!=null&&leave.getLeaveDate2()!=null){
            leaveTotal = leaveTotal + getDateTime(leave.getLeaveDate1(), leave.getLeaveDate2());
          }
        }
        
        //本月加班时长-本月请假时长
        long overtime_leave = overtimeTotal - leaveTotal;
        data =  data + "{overtimeTotal:" + overtimeTotal + ",leaveTotal:" + leaveTotal + ",overtime_leave:\"" + overtime_leave + "\",userName:\"" + YHUtility.encodeSpecial(userName) + "\",deptName:\"" + YHUtility.encodeSpecial(deptName)+ "\"},";
        
      }
      if(userIdArray.length>0){
        data =  data.substring(0, data.length()-1);
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
   * exl导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exprotAttendExl(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");
      String beginDate = request.getParameter("beginTime");
      String endDate = request.getParameter("endTime");
      String days = request.getParameter("days");
      endDate = endDate + " 23:59:59";
      String fileName = "考勤统计数据.xls";

      fileName = URLEncoder.encode(fileName, "UTF-8");//fileName.getBytes("GBK"), "iso8859-1")
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"" );
      ops = response.getOutputStream();

      
      String[] userIdArray = {};
      if(!userId.equals("")){
        userIdArray = userId.split(",");
      }
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHOvertimeRecordLogic overtimeLogic = new YHOvertimeRecordLogic();
      YHPersonalLeaveLogic leaveLogic = new YHPersonalLeaveLogic();
      for (int i = 0; i < userIdArray.length; i++) {
        String deptName = yhaol.selectByUserIdDept(dbConn, userIdArray[i]);
        YHPerson person = personLogic.getPerson(dbConn, userIdArray[i]);
        YHDbRecord rc = new YHDbRecord();
        //得到 加班记录时长
        String[] overtimeStr = {"USER_ID = '" +  userIdArray[i] + "'" , "STATUS = '1'",YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">="),YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=")};

        List<YHOvertimeRecord> overList = new ArrayList<YHOvertimeRecord>();
        overList = overtimeLogic.selectOvertime(dbConn, overtimeStr);
        long overtimeTotal = 0; 
        for (int j = 0; j < overList.size(); j++) {
          YHOvertimeRecord overtime = overList.get(j);
          if(overtime.getBeginTime() != null){
            String beginDates = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " +overtime.getBeginDate();
            String endDates = String.valueOf(overtime.getBeginTime()).substring(0, 10) + " " +overtime.getEndDate();
            overtimeTotal = overtimeTotal + getDateTime(YHUtility.parseDate(beginDates), YHUtility.parseDate(endDates));
          }
        }
        //得到 请假时长 

        String[] str = {"USER_ID = '" + userIdArray[i] + "'" , "(STATUS = '1' and (ALLOW = '1'))",YHDBUtility.getDateFilter("LEAVE_DATE1", beginDate, ">="),YHDBUtility.getDateFilter("LEAVE_DATE2", endDate, "<=")};
        List<YHPersonalLeave> leaveList = leaveLogic.selectLeave(dbConn, str);
        long leaveTotal = 0;;
        for (int j  = 0; j < leaveList.size(); j++) {
          YHPersonalLeave leave = leaveList.get(j);
          if(leave.getLeaveDate1()!=null&&leave.getLeaveDate2()!=null){
            leaveTotal = leaveTotal + getDateTime(leave.getLeaveDate1(), leave.getLeaveDate2());
          }
        }     
        //本月加班时长-本月请假时长
        long overtime_leave = overtimeTotal - leaveTotal;  
        String overtimeTotalStr = getDateTimeStr(overtimeTotal);
        String leaveTotalStr = getDateTimeStr(leaveTotal);
        String overtime_leave_opt = "";
        if(overtime_leave < 0){
          overtime_leave_opt = "-";
        }
        overtime_leave = Math.abs(overtime_leave);
        String overtime_leave_str = overtime_leave_opt + getDateTimeStr(overtime_leave);
        rc.addField("部门", deptName);
        rc.addField("姓名", person.getUserName());
        rc.addField("加班时长 ", overtimeTotalStr);
        rc.addField("请假时长", leaveTotalStr);
        rc.addField("加班时长-请假时长", overtime_leave_str);
        dbL.add(rc);
      }
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
}
