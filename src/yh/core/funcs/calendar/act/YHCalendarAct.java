package yh.core.funcs.calendar.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.info.logic.YHInfoLogic;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.calendar.logic.YHCalExpImpLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
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
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHCalendarAct {
  /**
   * 新建日程安排
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String ldwmNewStatus = request.getParameter("ldwmNewStatus");
      YHCalendar calendar = new YHCalendar();
      Date curDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String calTime = request.getParameter("calTime");
      String endTime = request.getParameter("endTime");
      String calType = request.getParameter("calType");
      String calLevel = request.getParameter("calLevel");
      String content = request.getParameter("content");
      String smsRemind = request.getParameter("smsRemind");
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      String beforeDay = request.getParameter("beforeDay");
      String beforeHour = request.getParameter("beforeHour");
      String beforeMin = request.getParameter("beforeMin");
      int beforeDayInt = 0;
      int beforeHourInt = 0;
      int beforeMinInt = 0;
      if (beforeDay != null && !beforeDay.equals("")
          && YHUtility.isInteger(beforeDay)) {
        beforeDayInt = Integer.parseInt(beforeDay);
      }
      if (beforeHour != null && !beforeHour.equals("")
          && YHUtility.isInteger(beforeHour)) {
        beforeHourInt = Integer.parseInt(beforeHour);
      }
      if (beforeMin != null && !beforeMin.equals("")
          && YHUtility.isInteger(beforeMin)) {
        beforeMinInt = Integer.parseInt(beforeMin);
      }

      //content = content.replaceAll("\n", "");
      //content = content.replaceAll("\r", "");
      // System.out.println(content);
      calTime = calTime + ":00";
      endTime = endTime + ":00";
      // System.out.println(calTime);
      calendar.setUserId(String.valueOf(userId));
      calendar.setCalTime(dateFormat.parse(calTime));
      calendar.setEndTime(dateFormat.parse(endTime));
      calendar.setOverStatus("0");
      calendar.setCalType(calType);
      calendar.setCalLevel(calLevel);
      calendar.setContent(content);
      YHCalendarLogic tcl = new YHCalendarLogic();
      int maxSeqId = tcl.addCalendar(dbConn, calendar);

      Calendar c = Calendar.getInstance();
      c.setTime(dateFormat.parse(calTime));
      c.add(Calendar.DATE, -beforeDayInt);
      c.add(Calendar.HOUR, -beforeHourInt);
      c.add(Calendar.MINUTE, -beforeMinInt);
      Date newDate = c.getTime();
      if (smsRemind != null) {
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        if (curDate.compareTo(newDate) < 0) {
          sb.setSendDate(newDate);
        }
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容：" + content);
        sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + maxSeqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      if (moblieSmsRemind != null) {
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn, String.valueOf(userId), userId,
            "日程安排：" + content, new Date());
      }
      // 手机短信/yh/core/funcs/mobilesms/logic/YHMobileSms2Logic 中的
      // remindByMobileSms
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      // request.setAttribute(YHActionKeys.RET_DATA, "data");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 更新日程安排
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String ldwm = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCalendar calendar = new YHCalendar();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String calTime = request.getParameter("calTime");
      String endTime = request.getParameter("endTime");
      String calType = request.getParameter("calType");
      String calLevel = request.getParameter("calLevel");
      String content = request.getParameter("content");
//      content = content.replaceAll("\\\n", "");
//      content = content.replaceAll("\\\r", "");
      String seqId = request.getParameter("seqId");
      ldwm = request.getParameter("ldwm");
      String smsRemind = request.getParameter("smsRemind");
      String beforeDay = request.getParameter("beforeDay");
      String beforeHour = request.getParameter("beforeHour");
      String beforeMin = request.getParameter("beforeMin");
      int beforeDayInt = 0;
      int beforeHourInt = 0;
      int beforeMinInt = 0;
      if (beforeDay != null && !beforeDay.equals("")
          && YHUtility.isInteger(beforeDay)) {
        beforeDayInt = Integer.parseInt(beforeDay);
      }
      if (beforeHour != null && !beforeHour.equals("")
          && YHUtility.isInteger(beforeHour)) {
        beforeHourInt = Integer.parseInt(beforeHour);
      }
      if (beforeMin != null && !beforeMin.equals("")
          && YHUtility.isInteger(beforeMin)) {
        beforeMinInt = Integer.parseInt(beforeMin);
      }

      // System.out.println(ldwm);
      // System.out.println(calTime);
      calTime = calTime + ":00";
      endTime = endTime + ":00";
      // System.out.println(calTime);
      calendar.setSeqId(Integer.parseInt(seqId));
      calendar.setUserId(String.valueOf(userId));
      calendar.setCalTime(dateFormat.parse(calTime));
      calendar.setEndTime(dateFormat.parse(endTime));
      calendar.setOverStatus("0");
      calendar.setCalType(calType);
      calendar.setCalLevel(calLevel);
      calendar.setContent(content);
      YHCalendarLogic tcl = new YHCalendarLogic();
      tcl.updateCalendar(dbConn, calendar);
      // 短信smsType, content, remindUrl, toId, fromId

      if (smsRemind != null) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateFormat.parse(calTime));
        c.add(Calendar.DATE, -beforeDayInt);
        c.add(Calendar.HOUR, -beforeHourInt);
        c.add(Calendar.MINUTE, -beforeMinInt);
        Date newDate = c.getTime();
        Date curDate = new Date();
        YHSmsBack sb = new YHSmsBack();
        if (curDate.compareTo(newDate) < 0) {
          sb.setSendDate(newDate);
        }
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容：" + content);
        sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + seqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if (ldwm.equals("list")) {
      String contextPath = request.getContextPath();
      response.sendRedirect(contextPath + "/core/funcs/calendar/list.jsp");
      // return "/core/funcs/calendar/list.jsp";
    } else if (ldwm.equals("week")) {
      // return "/core/funcs/calendar/week.jsp";
      String contextPath = request.getContextPath();
      response.sendRedirect(contextPath + "/core/funcs/calendar/week.jsp");
    } else if (ldwm.equals("month")) {
      // return "/core/funcs/calendar/month.jsp";
      String contextPath = request.getContextPath();
      response.sendRedirect(contextPath + "/core/funcs/calendar/month.jsp");
    } else if (ldwm.equals("day")) {
      String contextPath = request.getContextPath();
      response.sendRedirect(contextPath + "/core/funcs/calendar/day.jsp");
      // "/core/funcs/calendar/day.jsp";
    } else {
      return "/core/inc/rtjson.jsp";
    }
    return null;
  }

  /**
   * 更新日程安排
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCalendarById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String ldwm = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCalendar calendar = new YHCalendar();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String calTime = request.getParameter("calTime");
      String endTime = request.getParameter("endTime");
      String calType = request.getParameter("calType");
      String calLevel = request.getParameter("calLevel");
      String content = request.getParameter("content");
//      content = content.replaceAll("\\\n", "");
//      content = content.replaceAll("\\\r", "");
      String seqId = request.getParameter("seqId");
      ldwm = request.getParameter("ldwm");
      String smsRemind = request.getParameter("smsRemind");
      String beforeDay = request.getParameter("beforeDay");
      String beforeHour = request.getParameter("beforeHour");
      String beforeMin = request.getParameter("beforeMin");
      int beforeDayInt = 0;
      int beforeHourInt = 0;
      int beforeMinInt = 0;
      if (beforeDay != null && !beforeDay.equals("")
          && YHUtility.isInteger(beforeDay)) {
        beforeDayInt = Integer.parseInt(beforeDay);
      }
      if (beforeHour != null && !beforeHour.equals("")
          && YHUtility.isInteger(beforeHour)) {
        beforeHourInt = Integer.parseInt(beforeHour);
      }
      if (beforeMin != null && !beforeMin.equals("")
          && YHUtility.isInteger(beforeMin)) {
        beforeMinInt = Integer.parseInt(beforeMin);
      }

      // System.out.println(ldwm);
      // System.out.println(calTime);
      calTime = calTime + ":00";
      endTime = endTime + ":00";
      // System.out.println(calTime);
      YHCalendarLogic tcl = new YHCalendarLogic();
      calendar = tcl.selectCalendarById(dbConn, Integer.parseInt(seqId));
      calendar.setUserId(String.valueOf(userId));
      calendar.setCalTime(dateFormat.parse(calTime));
      calendar.setEndTime(dateFormat.parse(endTime));
      calendar.setCalType(calType);
      calendar.setCalLevel(calLevel);
      calendar.setContent(content);

      tcl.updateCalendar(dbConn, calendar);
      // 短信smsType, content, remindUrl, toId, fromId
      if (smsRemind != null) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateFormat.parse(calTime));
        c.add(Calendar.DATE, -beforeDayInt);
        c.add(Calendar.HOUR, -beforeHourInt);
        c.add(Calendar.MINUTE, -beforeMinInt);
        Date newDate = c.getTime();
        Date curDate = new Date();
        YHSmsBack sb = new YHSmsBack();
        if (curDate.compareTo(newDate) < 0) {
          sb.setSendDate(newDate);
        }
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容：" + content);
        sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + seqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      String moblieSmsRemind = request.getParameter("moblieSmsRemind");
      if (moblieSmsRemind != null) {
        YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
        sms2Logic.remindByMobileSms(dbConn, String.valueOf(userId), userId,
            "日程安排：" + content, new Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";

  }

  /**
   * 更新日程安排查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCalendarByUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String ldwm = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCalendar calendar = new YHCalendar();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String calTime = request.getParameter("calTime");
      String endTime = request.getParameter("endTime");
      String calType = request.getParameter("calType");
      String calLevel = request.getParameter("calLevel");
      String content = request.getParameter("content");
      String smsRemind = request.getParameter("smsRemind");
      String beforeDay = request.getParameter("beforeDay");
      String beforeHour = request.getParameter("beforeHour");
      String beforeMin = request.getParameter("beforeMin");
      int beforeDayInt = 0;
      int beforeHourInt = 0;
      int beforeMinInt = 0;
      if (beforeDay != null && !beforeDay.equals("")
          && YHUtility.isInteger(beforeDay)) {
        beforeDayInt = Integer.parseInt(beforeDay);
      }
      if (beforeHour != null && !beforeHour.equals("")
          && YHUtility.isInteger(beforeHour)) {
        beforeHourInt = Integer.parseInt(beforeHour);
      }
      if (beforeMin != null && !beforeMin.equals("")
          && YHUtility.isInteger(beforeMin)) {
        beforeMinInt = Integer.parseInt(beforeMin);
      }
//      content = content.replaceAll("\\\n", "");
//      content = content.replaceAll("\\\r", "");
      String seqId = request.getParameter("seqId");
      YHCalendarLogic tcl = new YHCalendarLogic();
      if (seqId != null && !seqId.equals("")) {
        calendar = tcl.selectCalendarById(dbConn, Integer.parseInt(seqId));
        if (calendar != null) {
          calendar.setCalTime(YHUtility.parseDate(calTime + ":00"));
          calendar.setEndTime(YHUtility.parseDate(endTime + ":00"));
          calendar.setCalType(calType);
          calendar.setCalLevel(calLevel);
          calendar.setContent(content);
          tcl.updateCalendar(dbConn, calendar);
          if (smsRemind != null) {
            // 短信smsType, content, remindUrl, toId, fromId
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(calTime + ":00"));
            c.add(Calendar.DATE, -beforeDayInt);
            c.add(Calendar.HOUR, -beforeHourInt);
            c.add(Calendar.MINUTE, -beforeMinInt);
            Date newDate = c.getTime();
            Date curDate = new Date();
            YHSmsBack sb = new YHSmsBack();
            if (curDate.compareTo(newDate) < 0) {
              sb.setSendDate(newDate);
            }
            sb.setSmsType("5");
            sb.setContent(user.getUserName() + "为您安排新的工作 内容：" + content);
            sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + seqId
                + "&openFlag=1&openWidth=300&openHeight=250");
            sb.setToId(calendar.getUserId());
            sb.setFromId(userSeqId);
            YHSmsUtil.smsBack(dbConn, sb);
          }
          String moblieSmsRemind = request.getParameter("moblieSmsRemind");
          if (moblieSmsRemind != null) {
            YHMobileSms2Logic sms2Logic = new YHMobileSms2Logic();
            sms2Logic.remindByMobileSms(dbConn, calendar.getUserId(),
                userSeqId, "日程安排：" + content, new Date());
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 按日查询显示在新建工作日志上
  public String selectCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String date = request.getParameter("date");
      String dateStr1 = date + " 00:00:00";
      String dateStr2 = date + " 23:59:59";
      String calTime1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr1, ">=");
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime1 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = tcl.selectCalendarByDay(dbConn, String
          .valueOf(userId), calTime1, calTime2, endTime1, endTime2, "0");
      String data = "[";
      for (int i = 0; i < calendarList.size(); i++) {
        data = data + YHFOM.toJson(calendarList.get(i)) + ",";
      }
      if (calendarList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 显示在桌面上
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectCalendarToDisk(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String date = request.getParameter("date");
      String index = request.getParameter("index");
      String dateStr1 = date + " 00:00:00";
      String dateStr2 = date + " 23:59:59";
      String calTime1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr1, ">=");
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime1 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
      if (index.equals("1")) {
        calendarList = tcl.selectCalendarByDay(dbConn, String.valueOf(userId),
            calTime1, calTime2, endTime1, endTime2, "0");
      }
      if (index.equals("2")) {
        String endTimeTemp = format.format(getDateBefore(new Date(), 10));
        calTime2 = YHDBUtility.getDateFilter("CAL_TIME", endTimeTemp
            + " 23:59:59", "<=");
        // endTime2 = YHDBUtility.getDateFilter("END_TIME", endTimeTemp, ">=");
        calendarList = tcl.selectCalendarByDay(dbConn, String.valueOf(userId),
            calTime1, calTime2, endTime1, endTime2, "0");
      }
      String data = "[";
      for (int i = 0; i < calendarList.size(); i++) {
        if (i == 9) {
          break;
        }
        data = data + YHFOM.toJson(calendarList.get(i)) + ",";
      }
      if (calendarList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 按日查询
  public String selectCalendarByDay(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Date dateCur = new Date();
      long dateTime = dateCur.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormatday = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String day = request.getParameter("day");
      String date = year + "-" + month + "-" + day;
      String status1 = request.getParameter("status");
      // System.out.println(status1+"........s");
      String dateStr1 = date + " 00:00:00";
      String dateStr2 = date + " 23:59:59";
      // System.out.println(dateStr1);
      String calTime1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr1, ">=");
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime1 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      // System.out.println(calTime2+status1);
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = tcl.selectCalendarByDay(dbConn, String
          .valueOf(userId), calTime1, calTime2, endTime1, endTime2, status1);
      String data = "[";

      for (int i = 0; i < calendarList.size(); i++) {
        YHCalendar calendar = new YHCalendar();
        calendar = calendarList.get(i);
        long begin = 0;
        long end = 0;
        int status = 0;// 进行中 判断判断状态
        // System.out.println(calendar.getCalTime());
        begin = calendar.getCalTime().getTime();
        end = calendar.getEndTime().getTime();
        if (dateTime < begin) {
          status = 1;// 未开始
        }
        if (dateTime > end) {
          status = 2;// 超时
        }
        calendar = calendarList.get(i);
        // 判断是否跨天,并且判断是哪种跨天
        int dayStatus = 0;// 没跨天
        // System.out.println(calendar.getSeqId());
        if (!dateFormatday.format(calendar.getCalTime()).equals(
            dateFormatday.format(calendar.getEndTime()))) {
          if (date.compareTo(dateFormatday.format(calendar.getCalTime())) > 0
              && date.compareTo(dateFormatday.format(calendar.getEndTime())) == 0) {
            dayStatus = 1;// 过期跨天
          }
          // System.out.println(dateFormatday.format(calendar.getEndTime()).substring(0,
          // 10));
          // System.out.println(date);
          // System.out.println(date.compareTo(dateFormatday.format(calendar.getEndTime()).substring(0,
          // 10))<0);
          if (date.compareTo(dateFormatday.format(calendar.getCalTime())) == 0
              && date.compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 2;// 未过跨天
          }
          if (date.compareTo(dateFormatday.format(calendar.getCalTime())) > 0
              && date.compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 3;// 跨天
          }
        }

        String managerName = YHInfoLogic.getUserName(calendar.getManagerId(),
            dbConn);
        if (managerName != null && !managerName.equals("")) {
          managerName = managerName.replace("\\", "\\\\").replace("\"", "\\\"")
              .replace("\r", "").replace("\n", "");
        }
        data = data
            + YHFOM.toJson(calendar).substring(0,
                YHFOM.toJson(calendar).length() - 1) + ",managerName:\""
            + managerName + "\",dayStatus:" + dayStatus + ",status:" + status
            + "},";
      }
      if (calendarList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      // System.out.println(data);
      // request.setAttribute("calendarList", calendarList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 按周查询
  public String selectCalendarByWeek(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Date dateCur = new Date();
      long dateTime = dateCur.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormatday = new SimpleDateFormat("yyyy-MM-dd");
      String dateCurStr = dateFormatday.format(dateCur);
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String year = request.getParameter("year");
      String weekth = request.getParameter("weekth");
      String status1 = request.getParameter("status");
      // System.out.println(status1+"........s");
      Calendar[] darr = getStartEnd(Integer.parseInt(year), Integer
          .parseInt(weekth));
      String dateStr1 = getFullTimeStr(darr[0]) + " 00:00:00";
      String dateStr2 = getFullTimeStr(darr[1]) + " 23:59:59";
      // System.out.println(dateStr1);
      String calTime1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr1, ">=");
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime1 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      // System.out.println(calTime2+status1);
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = tcl.selectCalendarByDay(dbConn, String
          .valueOf(userId), calTime1, calTime2, endTime1, endTime2, status1);
      String data = "[";

      for (int i = 0; i < calendarList.size(); i++) {
        YHCalendar calendar = new YHCalendar();
        calendar = calendarList.get(i);
        long begin = 0;
        long end = 0;
        int status = 0;// 进行中 判断判断状态
        // System.out.println(calendar.getCalTime());
        begin = calendar.getCalTime().getTime();
        end = calendar.getEndTime().getTime();
        if (dateTime < begin) {
          status = 1;// 未开始
        }
        if (dateTime > end) {
          status = 2;// 超时
        }
        calendar = calendarList.get(i);
        // 判断是否跨天,并且判断是哪种跨天
        int dayStatus = 0;// 没跨天
        // System.out.println(calendar.getSeqId());
        if (!dateFormatday.format(calendar.getCalTime()).equals(
            dateFormatday.format(calendar.getEndTime()))) {
          // System.out.println(calendar.getSeqId()+"------------");
          // System.out.println(getFullTimeStr(darr[0]).compareTo(dateFormatday.format(calendar.getCalTime()))>0);
          // System.out.println(getFullTimeStr(darr[1]).compareTo(dateFormatday.format(calendar.getEndTime()))>=0);
          if (getFullTimeStr(darr[0]).compareTo(
              dateFormatday.format(calendar.getCalTime())) > 0
              && getFullTimeStr(darr[1]).compareTo(
                  dateFormatday.format(calendar.getEndTime())) >= 0) {
            dayStatus = 1;// 过期跨周
          } else if (getFullTimeStr(darr[0]).compareTo(
              dateFormatday.format(calendar.getCalTime())) <= 0
              && getFullTimeStr(darr[1]).compareTo(
                  dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 2;// 未过跨周
          } else if (getFullTimeStr(darr[0]).compareTo(
              dateFormatday.format(calendar.getCalTime())) > 0
              && getFullTimeStr(darr[1]).compareTo(
                  dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 3;// 跨周
          } else {
            dayStatus = 4;// 本周跨天
          }
        }
        String managerName = YHInfoLogic.getUserName(calendar.getManagerId(),
            dbConn);
        if (managerName != null && !managerName.equals("")) {
          managerName = managerName.replace("\\", "\\\\").replace("\"", "\\\"")
              .replace("\r", "").replace("\n", "");
        }
        data = data
            + YHFOM.toJson(calendar).substring(0,
                YHFOM.toJson(calendar).length() - 1) + ",managerName:\""
            + managerName + "\",dayStatus:" + dayStatus + ",status:" + status
            + "},";
      }
      if (calendarList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      // System.out.println(data);
      // request.setAttribute("calendarList", calendarList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 按月查询
  public String selectCalendarByMonth(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Date dateCur = new Date();
      long dateTime = dateCur.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat dateFormatday = new SimpleDateFormat("yyyy-MM-dd");
      String dateCurStr = dateFormatday.format(dateCur);
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String status1 = request.getParameter("status");
      Calendar time = Calendar.getInstance();
      time.clear();
      time.set(Calendar.YEAR, Integer.parseInt(year)); // year 为 int
      time.set(Calendar.MONTH, Integer.parseInt(month) - 1);// 注意,Calendar对象默认一月为0
      int maxDay = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
      // System.out.println(status1+"........s");
      if (String.valueOf(month).length() == 1) {
        month = "0" + month;
      }
      String dateStr1 = year + "-" + month + "-01 00:00:00";
      String dateStr2 = year + "-" + month + "-" + maxDay + " 23:59:59";
      // System.out.println(dateStr1);
      String calTime1 = YHDBUtility.getDateFilter("CAL_TIME", dateStr1, ">=");
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime1 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      // System.out.println(calTime2+status1);
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = tcl.selectCalendarByDay(dbConn, String
          .valueOf(userId), calTime1, calTime2, endTime1, endTime2, status1);
      String data = "[";
      for (int i = 0; i < calendarList.size(); i++) {
        YHCalendar calendar = new YHCalendar();
        calendar = calendarList.get(i);
        long begin = 0;
        long end = 0;
        int status = 0;// 进行中 判断判断状态
        // System.out.println(calendar.getCalTime());
        begin = calendar.getCalTime().getTime();
        end = calendar.getEndTime().getTime();
        if (dateTime < begin) {
          status = 1;// 未开始
        }
        if (dateTime > end) {
          status = 2;// 超时
        }
        calendar = calendarList.get(i);
        // 判断是否跨天,并且判断是哪种跨天
        int dayStatus = 0;// 没跨天
        // System.out.println(calendar.getSeqId());
        dateStr1 = dateStr1.substring(0, 10);
        dateStr2 = dateStr2.substring(0, 10);
        if (!dateFormatday.format(calendar.getCalTime()).equals(
            dateFormatday.format(calendar.getEndTime()))) {
          // System.out.println(dateStr1.compareTo(dateFormatday.format(calendar.getCalTime()))>0);
          if (dateStr1.compareTo(dateFormatday.format(calendar.getCalTime())) > 0
              && dateStr2
                  .compareTo(dateFormatday.format(calendar.getEndTime())) >= 0) {
            dayStatus = 1;// 过期跨月
          } else if (dateStr1.compareTo(dateFormatday.format(calendar
              .getCalTime())) <= 0
              && dateStr2
                  .compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 2;// 未过跨月
          } else if (dateStr1.compareTo(dateFormatday.format(calendar
              .getCalTime())) > 0
              && dateStr2
                  .compareTo(dateFormatday.format(calendar.getEndTime())) < 0) {
            dayStatus = 3;// 跨月
          } else {
            dayStatus = 4;// 本月跨天
          }
        }
        String managerName = YHInfoLogic.getUserName(calendar.getManagerId(),
            dbConn);
        if (managerName != null && !managerName.equals("")) {
          managerName = managerName.replace("\\", "\\\\").replace("\"", "\\\"")
              .replace("\r", "").replace("\n", "");
        }
        data = data
            + YHFOM.toJson(calendar).substring(0,
                YHFOM.toJson(calendar).length() - 1) + ",managerName:\""
            + managerName + "\",dayStatus:" + dayStatus + ",status:" + status
            + "},";
      }
      if (calendarList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      // System.out.println(data);
      // request.setAttribute("calendarList", calendarList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 按状态 ：进行，全部，未开始，以完成，超时
  public String selectCalendarByStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String statusTemp = request.getParameter("statusTemp");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHCalendarLogic dl = new YHCalendarLogic();
      String data = dl.toSearchData(dbConn, request.getParameterMap(), userId,
          statusTemp);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
    /*
     * Connection dbConn = null; try { YHRequestDbConn requestDbConn =
     * (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     * dbConn = requestDbConn.getSysDbConn(); String statusTemp =
     * request.getParameter("statusTemp");
     * //System.out.println(statusTemp+"---------"); YHCalendar calendar = new
     * YHCalendar(); SimpleDateFormat dateFormat = new
     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Date date = new Date(); long
     * dateTime = date.getTime(); YHPerson user = (YHPerson)
     * request.getSession().getAttribute(YHConst.LOGIN_USER); int userId =
     * user.getSeqId(); YHCalendarLogic tcl = new YHCalendarLogic();
     * List<YHCalendar> calendarList= tcl.selectCalendarByStatus(dbConn, userId,
     * statusTemp); String data = "["; long begin = 0; long end = 0;
     * 
     * for (int i = 0; i < calendarList.size(); i++) { calendar =
     * calendarList.get(i); begin = calendar.getCalTime().getTime(); end =
     * calendar.getEndTime().getTime(); int status = 0;//进行中 判断判断状态
     * 
     * if(dateTime<begin){ status = 1;//未开始
     * 
     * } if(dateTime>end){ status = 2;//超时 } data = data +
     * YHFOM.toJson(calendar).toString().substring(0,
     * YHFOM.toJson(calendar).toString().length()-1) + ",status:" + status +
     * "},"; } if(calendarList.size()>0){ data = data.substring(0,
     * data.length()-1); } data = data + "]"; //System.out.println(data);
     * request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     * request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
     * request.setAttribute(YHActionKeys.RET_DATA, data); }catch(Exception ex) {
     * request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     * request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage()); throw ex; }
     * return "/core/inc/rtjson.jsp";
     */
  }

  /*
   * 查询日程安排byId
   */
  public String selectCalendarById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCalendar calendar = new YHCalendar();
      String seqId = request.getParameter("seqId");
      YHCalendarLogic tcl = new YHCalendarLogic();
      calendar = tcl.selectCalendarById(dbConn, Integer.parseInt(seqId));
      Date date = new Date();
      long dateTime = date.getTime();
      long begin = 0;
      long end = 0;
      int status = 0;// 进行中 判断判断状态
      String data = "";
      // System.out.println(calendar.getCalTime());
      if (calendar != null) {
        begin = calendar.getCalTime().getTime();
        end = calendar.getEndTime().getTime();
        if (dateTime < begin) {
          status = 1;// 未开始
        }
        if (dateTime > end) {
          status = 2;// 超时
        }

        YHPersonLogic tpl = new YHPersonLogic();
        String userName = tpl.getNameBySeqIdStr(calendar.getUserId(), dbConn);
        if (userName != null && !userName.equals("")) {
          userName = YHUtility.encodeSpecial(userName);
        }
        String managerName = tpl.getNameBySeqIdStr(calendar.getManagerId(),
            dbConn);
        if (managerName != null && !managerName.equals("")) {
          managerName = YHUtility.encodeSpecial(managerName);
        }
        data = data
            + YHFOM.toJson(calendar).toString().substring(0,
                YHFOM.toJson(calendar).toString().length() - 1)
            + ",userName:\"" + userName + "\",managerName:\"" + managerName
            + "\",status:" + status + "}";

      }
      if (data.equals("")) {
        data = "{}";
      }
      // System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  // 删除ById
  public String deleteCalendarById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String ldwm = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCalendar calendar = new YHCalendar();
      YHCalendarLogic tcl = new YHCalendarLogic();
      String seqId = request.getParameter("seqId");
      tcl.deleteCalendarById(dbConn, Integer.parseInt(seqId));
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String deleteCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String statusTemp = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqIds");
      statusTemp = request.getParameter("statusTemp");
      YHCalendar calendar = new YHCalendar();
      YHCalendarLogic tcl = new YHCalendarLogic();
      tcl.deleteCalendar(dbConn, seqIdStr.substring(0, seqIdStr.length() - 1));
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/calendar/list.jsp?statusTemp=" + statusTemp;
  }

  // 更改完成状态
  public String updateStatusById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String ldwm = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHCalendar calendar = new YHCalendar();
      YHCalendarLogic tcl = new YHCalendarLogic();
      String seqId = request.getParameter("seqId");
      String overStatus = request.getParameter("status");
      ldwm = request.getParameter("ldwm");
      calendar.setSeqId(Integer.parseInt(seqId));
      calendar.setOverStatus(overStatus);
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("overStatus", overStatus);
      tcl.updateStatusById(dbConn, map);
      // 查询出来是否是别人给安排的日程 ,给他短信短信提醒
      if (!seqId.equals("")) {
        calendar = tcl.selectCalendarById(dbConn, Integer.parseInt(seqId));
      }

      // 发送短信
      if (overStatus.equals("1")) {
        // 短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("您安排的日程安排！内容：" + calendar.getContent() + " 已完成");
        sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + seqId
            + "&openFlag=1&openWidth=300&openHeight=250");
        sb.setFromId(userId);
        if (calendar.getManagerId() != null
            && !calendar.getManagerId().equals("")) {
          sb.setToId(calendar.getManagerId());
        } else {
          sb.setToId(String.valueOf(userId));
        }

        YHSmsUtil.smsBack(dbConn, sb);
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String selectCalendarByTerm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String type = request.getParameter("type");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      String sendTimeMin = request.getParameter("sendTimeMin");
      String sendTimeMax = request.getParameter("sendTimeMax");
      String calLevel = request.getParameter("calLevel");
      String calType = request.getParameter("calType");
      // System.out.println(calType);
      String overStatus = request.getParameter("overStatus");
      String content = request.getParameter("content");

      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHCalendarLogic tcl = new YHCalendarLogic();
      List<YHCalendar> calendarList = tcl.selectCalendarByTerm(dbConn, String
          .valueOf(userId), sendTimeMin, sendTimeMax, calLevel, calType,
          overStatus, content);
      List<Map<String, String>> calendarListCopy = new ArrayList<Map<String, String>>();
      Date date = new Date();
      long dateTime = date.getTime();
      long begin = 0;
      long end = 0;
      for (int i = 0; i < calendarList.size(); i++) {
        String status = "0";// 进行中 判断判断状态
        Map<String, String> map = new HashMap<String, String>();
        YHCalendar calendar = calendarList.get(i);
        map.put("seqId", String.valueOf(calendar.getSeqId()));
        map.put("userId", calendar.getUserId());
        map.put("calLevel", calendar.getCalLevel());
        map.put("calType", calendar.getCalType());
        map.put("content", calendar.getContent());
        map.put("managerId", calendar.getManagerId());
        if (calendar.getCalTime() != null) {
          map.put("calTime", dateFormat.format(calendar.getCalTime()));
        } else {
          map.put("calTime", "");
        }
        if (calendar.getEndTime() != null) {
          map.put("endTime", dateFormat.format(calendar.getEndTime()));
        } else {
          map.put("endTime", "");
        }

        map.put("overStatus", calendar.getOverStatus());
        // System.out.println(calendar.getManagerId());
        if (calendar.getManagerId() != null) {
          YHPersonLogic tpl = new YHPersonLogic();
          map.put("managerName", tpl.getNameBySeqIdStr(calendar.getManagerId(),
              dbConn));
        } else {
          map.put("managerName", "");
        }
        String overStatus1 = calendar.getOverStatus();
        if (overStatus1 == null || overStatus1.equals("0")
            || overStatus1.trim().equals("")) {
          begin = calendar.getCalTime().getTime();
          end = calendar.getEndTime().getTime();
          if (dateTime < begin) {
            status = "1";// 未开始
          }
          if (dateTime > end) {
            status = "2";// 超时
          }
        }
        map.put("status", status);
        calendarListCopy.add(map);
      }
      // Map<String,String> map = tcl.selectPersonById(dbConn, userId);
      request.setAttribute("calendarList", calendarListCopy);
      // request.setAttribute("person", map);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if (type != null && !type.equals("")) {
      return "/core/funcs/calendar/calexprot.jsp";
    }
    return "/core/funcs/calendar/showcalendar.jsp";
  }

  /**
   * 查询在当月的所有生日的人
   * 
   * @param year
   * @param weeknum
   * @return
   */
  public String selectBirthday(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
      Calendar time = Calendar.getInstance();
      time.clear();
      time.set(Calendar.YEAR, Integer.parseInt(year)); // year 为 int
      time.set(Calendar.MONTH, Integer.parseInt(month) - 1);// 注意,Calendar对象默认一月为0
      int maxDay = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
      if (month.length() == 1) {
        month = "0" + month;
      }
      String[] str = {
          YHDBUtility.getMDFilter("BIRTHDAY", month + "-01", ">="),
          YHDBUtility.getMDFilter("BIRTHDAY", month + "-" + maxDay, "<=")
              + " order by " + YHDBUtility.getYMOrderby("BIRTHDAY") };
      YHCalendarLogic calendarLogic = new YHCalendarLogic();
      List<YHPerson> personList = calendarLogic.selectPerson(dbConn, str);
      String data = "[";
      for (int i = 0; i < personList.size(); i++) {
        YHPerson personTemp = personList.get(i);
        String userName = "";
        userName = personTemp.getUserName();
        if (userName != null && !userName.equals("")) {
          userName = userName.replace("\\", "\\\\").replace("\"", "\\\"")
              .replace("\r", "").replace("\n", "");
        }
        // data = data + YHFOM.toJson(persionTemp)+",";
        data = data + "{seqId:" + personTemp.getSeqId() + ",userName:\""
            + userName + "\",birthday:\""
            + dateFormat.format(personTemp.getBirthday()) + "\"},";
      }
      if (personList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      // System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询近期所有生日的人（一个月30天）
   * 
   * @param year
   * @param weeknum
   * @return
   */
  public String selectBirthdayToDisk(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
      Date date = new Date();
      Calendar cal = Calendar.getInstance();
      int curMonth = cal.get(Calendar.MONTH);
      String curMonthDay = dateFormat2.format(date);
      String curDateStr = dateFormat.format(date);
      Date newDate = getDateBefore(date, 30);
      cal.setTime(newDate);
      String newMonthDay = dateFormat2.format(newDate);
      int newMonth = cal.get(Calendar.MONTH);
      YHPerson person = new YHPerson();
      YHCalendarLogic calendarLogic = new YHCalendarLogic();
      List<YHPerson> personList = new ArrayList<YHPerson>();
      if (curMonth > newMonth) {
        String[] str = { YHDBUtility.getMDFilter("BIRTHDAY", curMonthDay, ">=")
            + " or " + YHDBUtility.getMDFilter("BIRTHDAY", newMonthDay, "<=")
            + " order by " + YHDBUtility.getYMOrderby("BIRTHDAY") };
        personList = calendarLogic.selectPerson(dbConn, str);
      } else {
        String[] str = {
            YHDBUtility.getMDFilter("BIRTHDAY", curMonthDay, ">="),
            YHDBUtility.getMDFilter("BIRTHDAY", newMonthDay, "<=")
                + " order by " + YHDBUtility.getYMOrderby("BIRTHDAY") };
        personList = calendarLogic.selectPerson(dbConn, str);
      }
      String data = "[";
      for (int i = 0; i < personList.size(); i++) {
        YHPerson personTemp = personList.get(i);
        // data = data + YHFOM.toJson(persionTemp)+",";
        String userName = "";
        userName = personTemp.getUserName();
        if (userName != null && !userName.equals("")) {
          userName = userName.replace("\\", "\\\\").replace("\"", "\\\"")
              .replace("\r", "").replace("\n", "");
        }
        data = data + "{seqId:" + personTemp.getSeqId() + ",userName:\""
            + userName + "\",birthday:\""
            + dateFormat.format(personTemp.getBirthday()) + "\"},";
      }
      if (personList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      // System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询短信显示不显示
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSysParaRemind(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String type = request.getParameter("type");// 5为日程安排
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      String sysRemind = yhadl.getParaValue(dbConn, "SMS_REMIND");// 2 |3 | 1
      String allowRemind = "2";// 1允许短信提醒2为不允许
      String defaultRemind = "2";// 1为默认提醒2为不默认
      String mobileRemind = "2";// 1为默认提醒2为不默认
      if (sysRemind != null) {
        String[] sysRemindArray = sysRemind.split("\\|");
        if (sysRemindArray.length == 1) {
          String temp = sysRemindArray[0];
          String[] tempArray = temp.split(",");
          for (int i = 0; i < tempArray.length; i++) {
            if (tempArray[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }

        }
        if (sysRemindArray.length == 2) {
          String temp1 = sysRemindArray[0];
          String[] tempArray1 = temp1.split(",");
          for (int i = 0; i < tempArray1.length; i++) {
            if (tempArray1[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }
          String temp2 = sysRemindArray[1];
          String[] tempArray2 = temp2.split(",");
          for (int i = 0; i < tempArray2.length; i++) {
            if (tempArray2[i].equals(type)) {
              mobileRemind = "1";
              break;
            }
          }
        }
        if (sysRemindArray.length == 3) {
          String temp1 = sysRemindArray[0];
          String[] tempArray1 = temp1.split(",");
          for (int i = 0; i < tempArray1.length; i++) {
            if (tempArray1[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }
          String temp2 = sysRemindArray[2];
          String[] tempArray2 = temp2.split(",");
          for (int i = 0; i < tempArray2.length; i++) {
            if (tempArray2[i].equals(type)) {
              allowRemind = "1";
              break;
            }
          }
          String temp3 = sysRemindArray[1];
          String[] tempArray3 = temp3.split(",");
          for (int i = 0; i < tempArray3.length; i++) {
            if (tempArray3[i].equals(type)) {
              mobileRemind = "1";
              break;
            }
          }
        }
      }
      String data = "{allowRemind:" + allowRemind + ",defaultRemind:"
          + defaultRemind + ",mobileRemind:" + mobileRemind + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 别人安排的日程安排发送短信
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendNote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String userIds = request.getParameter("user");
      String content = request.getParameter("content");
      if (userIds != null && !userIds.equals("")) {
        String userIdArray[] = userIds.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
          // 短信smsType, content, remindUrl, toId, fromId
          String userIdTemp = userIdArray[i];
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("5");
          sb.setContent("内容：" + content);
          sb.setRemindUrl("/core/funcs/calendar/mynote.jsp?seqId=" + seqId
              + "&openFlag=1&openWidth=300&openHeight=250");
          sb.setToId(String.valueOf(userIdTemp));
          sb.setFromId(userId);
          YHSmsUtil.smsBack(dbConn, sb);
        }
      }

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      // request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public static Calendar[] getStartEnd(int year, int weeknum) {
    /*
     * 参数说明 int year 年分 例如 2005 int weeknum 第几周 例如33 返回一个Calendar数组，长度为2
     * 分别是开始日期和结束日期
     */
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.WEEK_OF_YEAR, weeknum);
    int nw = cal.get(Calendar.DAY_OF_WEEK);
    Calendar start = (Calendar) cal.clone();
    Calendar end = (Calendar) cal.clone();
    start.add(Calendar.DATE, 1 - nw + 1);
    end.add(Calendar.DATE, 7 - nw + 1);
    Calendar[] darr = { start, end };
    return darr;
  }

  public static Calendar[] getStartEnd() {
    /*
     * 参数说明 int year 年分 例如 2005 int weeknum 第几周 例如33 返回一个Calendar数组，长度为2
     * 分别是开始日期和结束日期
     */
    Calendar cal = Calendar.getInstance();
    // 向后推一天（从星期一到周末）
    cal.add(Calendar.DATE, -1);
    int nw = cal.get(Calendar.DAY_OF_WEEK);
    Calendar start = (Calendar) cal.clone();
    Calendar end = (Calendar) cal.clone();
    start.add(Calendar.DATE, 1 - nw + 1);
    end.add(Calendar.DATE, 7 - nw + 1);
    Calendar[] darr = { start, end };
    return darr;
  }

  public static String getFullTimeStr(Calendar d) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(d.getTime());
  }

  // public static void main(String[] args) throws ParseException {
  /*
   * GregorianCalendar d = new GregorianCalendar(); SimpleDateFormat dateFormat
   * = new SimpleDateFormat("yyyy-MM-dd"); Calendar c = Calendar.getInstance();
   * d.setTime(dateFormat.parse("2010-03-06"));
   * 
   * //System.out.println(d.get(c.DAY_OF_WEEK));
   * //System.out.println(dateFormat.format(c.getTime()));
   */
  /*
   * Calendar c = Calendar.getInstance(); c.set(2010,11,30); int endWeekth =
   * c.get(Calendar.WEEK_OF_YEAR); //System.out.println(endWeekth);
   */

  // System.out.println(getMaxWeekNumOfYear(2007));
  /*
   * SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); Date
   * beginDate ; Date endDate ;
   * 
   * 
   * //System.out.println(getFullTimeStr(getStartEnd(2009,1)[0]));
   * //System.out.println(getFullTimeStr(getStartEnd(2007,1)[1]));
   */
  // String s = "1|1|";
  // System.out.println(s.split("\\|").length);
  // System.out.println("23,2".contains("1"));
  // System.out.println(getMaxWeekNumOfYear(2000));
  // }
  /**
   * 
   * @param year
   * @return计算一年的最大周
   */
  public static int getMaxWeekNumOfYear(int year) {
    Calendar c = new GregorianCalendar();
    c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

    return getWeekOfYear(c.getTime());
  }

  public static int getWeekOfYear(Date date) {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setMinimalDaysInFirstWeek(7);
    c.setTime(date);

    return c.get(Calendar.WEEK_OF_YEAR);
  }

  /**
   * 得到多少天前后的时期
   * 
   * @param d
   * @param day
   * @return
   */
  public Date getDateBefore(Date d, int day) {
    Calendar now = Calendar.getInstance();
    now.setTime(d);
    now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
    return now.getTime();
  }

  /**
   * 导出XML
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String export(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String userPriv = user.getUserPriv();
      String calendarContent = request.getParameter("calendarContent");
      String affariContent = request.getParameter("affairContent");
      String taskContent = request.getParameter("taskContent");
      String calType = request.getParameter("calType");
      String minDate = request.getParameter("minDate");
      String maxDate = request.getParameter("maxDate");
      String deptOut = request.getParameter("deptOut");
      String roleOut = request.getParameter("roleOut");
      String userOut = request.getParameter("userOut");

      String xmlStr = "<AFFAIRS>\n";
      String sql = "";
      String fileName = "";
      YHPersonLogic pl = new YHPersonLogic();
      if (calendarContent != null) {
        Set set = new HashSet();
        fileName = fileName + "日程";

        sql = "select c.CAL_TIME as CAL_TIME, c.END_TIME as END_TIME,c.CAL_TYPE as CAL_TYPE,"
            + "c.CAL_LEVEL as CAL_LEVEL,c.CONTENT as CONTENT,c.MANAGER_ID as MANAGER_ID from oa_schedule c left outer join PERSON p on c.user_id = p.seq_id where 1=1 ";
        if (userPriv != null && userPriv.equals("1")) {
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and c.USER_ID in (" + newUserIds + ")";
        } else {
          sql = sql + " and c.USER_ID = " + userSeqId;
        }
        if (calType != null && !calType.equals("")) {
          sql = sql + " and c.CAL_TYPE = '" + calType + "'";
        }
        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("c.END_TIME", maxDate,
              "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("c.CAL_TIME", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }
        xmlStr = xmlStr
            + dataChangeXml(request, response, ops, "AFFAIRS", "AFFAIR", sql,
                "CALENDAR");
      }
      if (affariContent != null) {
        fileName = fileName + "周期性事务";
        sql = "select a.BEGIN_TIME as BEGIN_TIME,a.END_TIME as END_TIME,a.TYPE as TYPE,a.REMIND_DATE as REMIND_DATE"
            + ",a.REMIND_TIME as REMIND_TIME,a.CONTENT as CONTENT,a.LAST_REMIND as LAST_REMIND,a.LAST_SMS2_REMIND as LAST_SMS2_REMIND"
            + ",a.MANAGER_ID as MANAGER_ID from oa_affairs a left outer join PERSON p on a.user_id= p.seq_id where 1=1";

        if (userPriv != null && userPriv.equals("1")) {
          Set set = new HashSet();
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and a.USER_ID in (" + newUserIds + ")";
        } else {
          sql = sql + " and a.USER_ID = " + userSeqId;
        }

        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("a.BEGIN_TIME",
              maxDate, "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("a.END_TIME", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }
        xmlStr = xmlStr
            + dataChangeXml(request, response, ops, "AFFAIRS", "AFFAIR", sql,
                "AFFAIR");

      }
      if (taskContent != null) {
        fileName = fileName + "任务";
        sql = "select t.BEGIN_DATE as BEGIN_DATE, t.END_DATE as END_DATE,t.TASK_TYPE as TASK_TYPE,"
            + "t.TASK_STATUS as TASK_STATUS,t.COLOR as COLOR,t.IMPORTANT as IMPORTANT,t.RATE as RATE,"
            + "t.FINISH_TIME as FINISH_TIME,t.TOTAL_TIME as TOTAL_TIME,t.USE_TIME as USE_TIME ,"
            + "t.SUBJECT as SUBJECT ,t.CONTENT as CONTENT,t.EDIT_TIME as EDIT_TIME,t.CAL_ID as CAL_ID,t.MANAGER_ID as MANAGER_ID"
            + " from TASK t left outer join PERSON p on t.user_id = p.seq_id where 1=1 ";
        if (userPriv != null && userPriv.equals("1")) {
          Set set = new HashSet();
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and t.USER_ID in (" + newUserIds + ")";
        } else {
          sql = sql + " and t.USER_ID = " + userSeqId;
        }
        if (calType != null && !calType.equals("")) {
          sql = sql + " and t.TASK_TYPE = '" + calType + "'";
        }
        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("t.BEGIN_DATE",
              maxDate, "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("t.END_DATE", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }
        xmlStr = xmlStr
            + dataChangeXml(request, response, ops, "AFFAIRS", "AFFAIR", sql,
                "TASK");
      }

      fileName = fileName + ".xml";
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("text/xml");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename="
          + new String(fileName.getBytes("GBK"), "iso8859-1"));
      ops = response.getOutputStream();

      String xmlHead = "<?xml version='1.0' encoding='GB2312'?>\n";
      ops.write(xmlHead.getBytes());
      xmlStr = xmlStr + "</AFFAIRS>";

      ops.write(xmlStr.getBytes("gb2312"));

      // YHExportLogic expl = new YHExportLogic();
      // ArrayList<YHDbRecord > dbL = expl.getDbRecord();
      // YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }

  public String dataChangeXml(HttpServletRequest request,
      HttpServletResponse response, OutputStream ops, String root,
      String childRoot, String sql, String flag) throws Exception {
    Connection dbConn = null;
    String temp = "";
    String url = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      // sql= "select * from AFFAIR where user_id=1";

      if (sql != null && !sql.equals("")) {
        PreparedStatement pstmt = dbConn.prepareStatement(sql);
        ResultSetMetaData rsmd = null;
        ResultSet rs = pstmt.executeQuery();
        rsmd = pstmt.getMetaData();
        rsmd.getColumnCount();// 得到多少列
        /** 查询语句_例句 */

        temp = getXml(rs, url, root, childRoot, rsmd, flag);
      }

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return temp;
  }

  public String getXml(ResultSet rs, String url, String root, String childRoot,
      ResultSetMetaData rsmd, String flag) throws SQLException {
    int count = rsmd.getColumnCount();
    int i = 0;
    String temp = "";
    try {
      while (rs.next()) {
        // 得到字段名称
        temp = temp + "<" + childRoot + ">\n";
        temp = temp + "<FLAG>" + flag + "</FLAG>\n";
        for (int j = 1; j <= count; j++) {
          temp = temp + "<" + rsmd.getColumnName(j) + ">";
          if (rs.getString(rsmd.getColumnName(j)) != null) {
            if (rsmd.getColumnType(j) == java.sql.Types.DATE
                || rsmd.getColumnType(j) == java.sql.Types.TIMESTAMP) {
              temp = temp
                  + YHUtility.getDateTimeStr(rs.getTimestamp(rsmd
                      .getColumnName(j)));
            } else {
              String tempStr = rs.getString(rsmd.getColumnName(j));
              if(!YHUtility.isNullorEmpty(tempStr)){
                tempStr = tempStr.replace("&", "&amp;");
                tempStr = tempStr.replace("<", "&lt;");
                tempStr = tempStr.replace(">", "&gt;");
                tempStr = tempStr.replace("\"", "&quot;");
                tempStr = tempStr.replace("'", "&apos;");
              }else{
                tempStr = "";
              }
              temp = temp + tempStr;
            }

          } else {
            temp = temp + "";
          }
          temp = temp + "</" + rsmd.getColumnName(j) + ">\n";
        }
        temp = temp + "</" + childRoot + ">\n";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return temp;
  }

  /**
   * csv导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportCSV(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String userPriv = user.getUserPriv();
      String calendarContent = request.getParameter("calendarContent");
      String affariContent = request.getParameter("affairContent");
      String taskContent = request.getParameter("taskContent");
      String calType = request.getParameter("calType");
      String minDate = request.getParameter("minDate");
      String maxDate = request.getParameter("maxDate");
      String deptOut = request.getParameter("deptOut");
      String roleOut = request.getParameter("roleOut");
      String userOut = request.getParameter("userOut");
      String fileName = "";
      String sql = "";
      // YHExportLogic expl = new YHExportLogic();
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();// expl.getDbRecord();
      YHCalExpImpLogic el = new YHCalExpImpLogic();
      YHPersonLogic pl = new YHPersonLogic();
      if (calendarContent != null && !calendarContent.equals("null")) {
        Set set = new HashSet();
        fileName = fileName + "日程";
        sql = "select c.CAL_TIME as CAL_TIME, c.END_TIME as END_TIME,c.CAL_TYPE as CAL_TYPE,"
            + "c.CAL_LEVEL as CAL_LEVEL,c.CONTENT as CONTENT,c.MANAGER_ID as MANAGER_ID from oa_schedule c left outer join PERSON p on c.user_id = p.SEQ_ID where 1=1 ";
        if (userPriv != null && userPriv.equals("1")) {
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and c.USER_ID in (" + newUserIds + ")";
          /*
           * if(deptOut!=null&&!deptOut.equals("")&&!deptOut.equals("0")){ sql =
           * sql+ " and p.dept_id in (" + deptOut + ")"; }
           * if(roleOut!=null&&!roleOut.equals("")){ String[] roleOutArray =
           * roleOut.split(","); String newRoleOut = ""; for (int i = 0; i <
           * roleOutArray.length; i++) { newRoleOut = newRoleOut + "'" +
           * roleOutArray[i] + "',"; } if(roleOutArray.length>0){ newRoleOut =
           * newRoleOut.substring(0, newRoleOut.length()-1); } sql = sql +
           * " and p.user_priv in(" + newRoleOut + ")"; }
           * if(userOut!=null&&!userOut.equals("")){ sql = sql +
           * " and p.seq_id in(" + userOut + ")"; }
           */
        } else {
          sql = sql + " and c.USER_ID = " + userSeqId;
        }
        if (calType != null && !calType.equals("")) {
          sql = sql + " and c.CAL_TYPE = '" + calType + "'";
        }
        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("c.END_TIME", maxDate,
              "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("c.CAL_TIME", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }

        dbL = el.getCVS(sql, dbL, conn, "CALENDAR");
      }
      if (affariContent != null && !affariContent.equals("null")) {
        fileName = fileName + "周期性事务";
        sql = "select a.BEGIN_TIME as BEGIN_TIME,a.END_TIME as END_TIME,a.TYPE as TYPE,a.REMIND_DATE as REMIND_DATE"
            + ",a.REMIND_TIME as REMIND_TIME,a.CONTENT as CONTENT,a.LAST_REMIND as LAST_REMIND,a.LAST_SMS2_REMIND as LAST_SMS2_REMIND"
            + ",a.MANAGER_ID as MANAGER_ID from oa_affairs a left outer join PERSON p on a.user_id= p.seq_id where 1=1";
        if (userPriv != null && userPriv.equals("1")) {
          /*
           * if(deptOut!=null&&!deptOut.equals("")&&!deptOut.equals("0")){ sql =
           * sql+ " and p.dept_id in (" + deptOut + ")"; }
           * if(roleOut!=null&&!roleOut.equals("")){ String[] roleOutArray =
           * roleOut.split(","); String newRoleOut = ""; for (int i = 0; i <
           * roleOutArray.length; i++) { newRoleOut = newRoleOut + "'" +
           * roleOutArray[i] + "',"; } if(roleOutArray.length>0){ newRoleOut =
           * newRoleOut.substring(0, newRoleOut.length()-1); } sql = sql +
           * " and p.user_priv in(" + newRoleOut + ")"; }
           * if(userOut!=null&&!userOut.equals("")){ sql = sql +
           * " and p.seq_id in(" + userOut + ")"; }
           */
          Set set = new HashSet();
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and a.USER_ID in (" + newUserIds + ")";
        } else {
          sql = sql + " and a.USER_ID = " + userSeqId;
        }

        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("a.BEGIN_TIME",
              maxDate, "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("a.END_TIME", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }
        dbL = el.getCVS(sql, dbL, conn, "AFFAIR");
      }
      if (taskContent != null && !taskContent.equals("null")) {
        fileName = fileName + "任务";
        sql = "select t.BEGIN_DATE as BEGIN_DATE, t.END_DATE as END_DATE,t.TASK_TYPE as TASK_TYPE,"
            + "t.TASK_STATUS as TASK_STATUS,t.COLOR as COLOR,t.IMPORTANT as IMPORTANT,t.RATE as RATE,"
            + "t.FINISH_TIME as FINISH_TIME,t.TOTAL_TIME as TOTAL_TIME,t.USE_TIME as USE_TIME ,"
            + "t.SUBJECT as SUBJECT ,t.CONTENT as CONTENT,t.EDIT_TIME as EDIT_TIME,t.CAL_ID as CAL_ID,t.MANAGER_ID as MANAGER_ID"
            + " from TASK t left outer join PERSON p on t.user_id = p.seq_id  where 1=1 ";
        if (userPriv != null && userPriv.equals("1")) {
          /*
           * if(deptOut!=null&&!deptOut.equals("")&&!deptOut.equals("0")){ sql =
           * sql+ " and p.dept_id in (" + deptOut + ")"; }
           * 
           * if(roleOut!=null&&!roleOut.equals("")){ String[] roleOutArray =
           * roleOut.split(","); String newRoleOut = ""; for (int i = 0; i <
           * roleOutArray.length; i++) { newRoleOut = newRoleOut + "'" +
           * roleOutArray[i] + "',"; } if(roleOutArray.length>0){ newRoleOut =
           * newRoleOut.substring(0, newRoleOut.length()-1); } sql = sql +
           * " and p.user_priv in(" + newRoleOut + ")"; }
           * if(userOut!=null&&!userOut.equals("")){ sql = sql +
           * " and p.seq_id in(" + userOut + ")"; }
           */
          Set set = new HashSet();
          if (deptOut != null && deptOut.equals("") && roleOut != null
              && roleOut.equals("") && userOut != null && userOut.equals("")) {
            // newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (deptOut != null && deptOut.equals("0")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (deptOut != null && !deptOut.equals("")) {
              set = pl.getPersonByTerm(conn, "DEPT_ID", deptOut, set);
            }
            if (roleOut != null && !roleOut.equals("")) {
              set = pl.getPersonByTerm(conn, "USER_PRIV", roleOut, set);
            }
            if (userOut != null && !userOut.equals("")) {
              String[] personArray = userOut.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
          Iterator it = set.iterator();
          String newUserIds = "";
          while (it.hasNext()) {
            newUserIds = newUserIds + "'" + it.next() + "',";
          }
          if (!newUserIds.equals("")) {
            newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
          }
          sql = sql + " and t.USER_ID in (" + newUserIds + ")";
        } else {
          sql = sql + " and t.USER_ID = " + userSeqId;
        }
        if (calType != null && !calType.equals("")) {
          sql = sql + " and t.TASK_TYPE = '" + calType + "'";
        }
        if (maxDate != null && !maxDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("t.BEGIN_DATE",
              maxDate, "<=");
          sql = sql + " and " + minDateStr;
        }
        if (minDate != null && !minDate.equals("")) {
          String minDateStr = YHDBUtility.getDateFilter("t.END_DATE", minDate,
              ">=");
          sql = sql + " and " + minDateStr;
        }
        dbL = el.getCVS(sql, dbL, conn, "TASK");
      }

      fileName = fileName + ".csv";
      // fileName = URLEncoder.encode(fileName,
      // "UTF-8");//fileName.getBytes("GBK"), "iso8859-1")
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename="
          + new String(fileName.getBytes("GBK"), "iso8859-1"));
      // YHExportLogic expl = new YHExportLogic();
      // ArrayList<YHDbRecord> dbLs = expl.getDbRecord();
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
    }
    return null;
  }

  /**
   * 导入XML/CVS
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String improt(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    InputStream is = null;
    OutputStream ops = null;
    String error = "导入成功！";
    String type = "1";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String userPriv = user.getUserPriv();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);

      String calendarContent = fileForm.getParameter("calendarContentImp");
      String affariContent = fileForm.getParameter("affairContentImp");
      String taskContent = fileForm.getParameter("taskContentImp");
      String dept = fileForm.getParameter("dept");
      String role = fileForm.getParameter("role");
      String person = fileForm.getParameter("user");
      String fileName = fileForm.getParameter("fileName");
      String impFileType = fileForm.getParameter("impType");
      String impType = fileForm.getParameter("calAffTask");
      //System.out.println(impType);

      if (impType != null && impType.equals("1")) {
        impType = "CALENDAR";
      }
      if (impType != null && impType.equals("2")) {
        impType = "AFFAIR";
      }
      if (impType != null && impType.equals("3")) {
        impType = "TASK";
      }
      is = fileForm.getInputStream();

      YHCalExpImpLogic el = new YHCalExpImpLogic();
      YHPersonLogic pl = new YHPersonLogic();

      String newUserIds = "";
      if (impFileType != null) {
        Set set = new HashSet();
        if (userPriv != null && userPriv.equals("1")) {
          if ((dept != null && dept.equals(""))
              && (role != null && role.equals(""))
              && (person != null && person.equals(""))) {
            newUserIds = String.valueOf(userSeqId);
            set.add(String.valueOf(userSeqId));
          } else {
            if (dept != null && dept.equals("0")) {
              set = pl.getPersonByTerm(dbConn, "DEPT_ID", "", set);
              // newUserIds =
            }
            if (dept != null && !dept.equals("")) {
              set = pl.getPersonByTerm(dbConn, "DEPT_ID", dept, set);
            }
            if (role != null && !role.equals("")) {
              set = pl.getPersonByTerm(dbConn, "USER_PRIV", role, set);
            }
            if (person != null && !person.equals("")) {
              String[] personArray = person.split(",");
              for (int i = 0; i < personArray.length; i++) {
                set.add(personArray[i]);
              }
            }
          }
        } else {
          newUserIds = String.valueOf(userSeqId);
          set.add(String.valueOf(userSeqId));
        }
        if (impFileType.equals("1")) {// cvs
          ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is);
          el.addCalAffarTaskCVS(dbConn, set, drl, impType);
        } else {// xml
          List<Map<String, String>> listXml = new ArrayList<Map<String, String>>();
          listXml = parserXml(is);
          el.addCalAffarTask(dbConn, set, listXml);
          //System.out.println(listXml);

        }
      }

    } catch (DocumentException e) {
      type = "2";
    } catch (Exception ex) {
 
      ex.printStackTrace();
      // System.out.println(ex.getMessage());
      // throw ex;
    }
    String path = request.getContextPath();
    response.sendRedirect(path + "/core/funcs/calendar/inout.jsp?type=" + type);
    return "";
  }

  public List<Map<String, String>> parserXml(InputStream is) throws Exception {
    SAXReader saxReader = new SAXReader();
    List<Map<String, String>> listXml = new ArrayList<Map<String, String>>();
    try {
      Document document = saxReader.read(is);
      Element employees = document.getRootElement();
      // System.out.println(employees.getName()+":"+employees.getText());

      for (Iterator i = employees.elementIterator(); i.hasNext();) {
        Element employee = (Element) i.next();
        // System.out.println(employee.getName()+":"+employee.getText());
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator j = employee.elementIterator(); j.hasNext();) {
          Element node = (Element) j.next();
          //System.out.println(node.getName() + ":" + node.getText());

          map.put(node.getName(), node.getText());

        }
        listXml.add(map);
      }
    } catch (DocumentException e) {
      //System.out.println(e.getMessage());
      throw e;
    }
    //System.out.println("dom4j parserXml");
    return listXml;

  }
  public static String getJson(List<Map<String,String>> mapList){
    StringBuffer buffer=new StringBuffer("["); 
    for(Map<String, String> equipmentsMap:mapList){ 
    buffer.append("{"); 
    Set<String>keySet=equipmentsMap.keySet(); 
    for(String mapStr:keySet){ 
      //System.out.println(mapStr + ":>>>>>>>>>>>>" + equipmentsMap.get(mapStr)); 
      String name=equipmentsMap.get(mapStr); 
      if(name!=null){
        name = YHUtility.encodeSpecial(name);
      }
     /* if(mapStr!=null&&mapStr.equals("seqId")){
        
      }*/
      buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 
    } 
    buffer.deleteCharAt(buffer.length()-1); 
    buffer.append("},"); 
    }
    buffer.deleteCharAt(buffer.length()-1); 
    if (mapList.size()>0) { 
      buffer.append("]"); 
    }else { 
      buffer.append("[]"); 
    }
    String data = buffer.toString();
    //System.out.println(data);
    return data;
  }
}
