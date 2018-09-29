package yh.pda.calendar.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.calendar.data.YHPdaAffair;
import yh.pda.calendar.data.YHPdaCalendar;

public class YHPdaCalendarAct {

  public void doint(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String dateStr1 = YHUtility.getDateTimeStr(null).substring(0, 10) + " 00:00:00";
      String dateStr2 = YHUtility.getDateTimeStr(null).substring(0, 10) + " 23:59:59";
      String calTime2 = YHDBUtility.getDateFilter("CAL_TIME", dateStr2, "<=");
      String endTime2 = YHDBUtility.getDateFilter("END_TIME", dateStr1, ">=");
      
      List<YHPdaCalendar> list1 = new ArrayList<YHPdaCalendar>();
      List<YHPdaAffair> list2 = new ArrayList<YHPdaAffair>();
      
      //查询日程
      String sql = " SELECT SEQ_ID, CAL_TIME, END_TIME, CONTENT " 
      		       + " from oa_schedule where USER_ID='"+person.getSeqId()+"'" 
      		       + " and "+calTime2+ " and " + endTime2 
      		       + " order by CAL_TIME ";
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while(rs.next()){
        YHPdaCalendar calendar = new YHPdaCalendar();
        calendar.setSeqId(rs.getInt("SEQ_ID"));
        calendar.setCalTime(rs.getTimestamp("CAL_TIME"));
        calendar.setEndTime(rs.getTimestamp("END_TIME"));
        calendar.setContent(rs.getString("CONTENT"));
        list1.add(calendar);
      }
      
      String dateEnd = YHUtility.getDateTimeStr(null).substring(0, 10)+" 23:59:59";
      String beginDate = YHDBUtility.getDateFilter("BEGIN_TIME", dateEnd, "<=");
      //查询事务
      sql = " SELECT SEQ_ID, USER_ID, TYPE, REMIND_DATE, REMIND_TIME, CONTENT from oa_affairs where USER_ID='"+person.getSeqId()+"' "
          + " and " + beginDate +" order by REMIND_TIME ";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while(rs.next()){
        YHPdaAffair affair = new YHPdaAffair();
        affair.setSeqId(rs.getInt("SEQ_ID"));
        affair.setUserId(rs.getString("USER_ID"));
        affair.setType(rs.getInt("TYPE"));
        affair.setRemindDate(rs.getString("REMIND_DATE"));
        affair.setRemindTime(rs.getString("REMIND_TIME"));
        affair.setContent(rs.getString("CONTENT"));
        list2.add(affair);
      }
      request.setAttribute("calendars", list1);
      request.setAttribute("affairs", list2);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/calendar/index.jsp").forward(request, response);
    return;
  }
  
  public void newCalendar(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    try{
      String calTime = (String)request.getParameter("calTime");
      String endTime = (String)request.getParameter("endTime");
      String calType = (String)request.getParameter("calType");
      String content = (String)request.getParameter("content");
      int flag = 0;
      String date = YHUtility.getDateTimeStr(null).substring(0, 11);
      
      if(YHUtility.isDayTime(date+calTime+":00") || YHUtility.isDayTime(date+endTime+":59")){
        YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        
        String sql = " insert into oa_schedule(USER_ID,CAL_TIME,END_TIME,CAL_TYPE,CAL_LEVEL,CONTENT,OVER_STATUS) " 
        	         + " values("+person.getSeqId()+",?,?,'"+calType+"','','"+content+"','0')";
        ps = dbConn.prepareStatement(sql);
        
        ps.setTimestamp(1, YHUtility.parseTimeStamp(YHUtility.parseDate(date+calTime+":00").getTime()));
        ps.setTimestamp(2, YHUtility.parseTimeStamp(YHUtility.parseDate(date+endTime+":59").getTime()));
        flag = ps.executeUpdate();
      }
      else{
        flag = 3;
      }
      request.setAttribute("flag", flag);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    request.getRequestDispatcher("/pda/calendar/send.jsp").forward(request, response);
    return;
  }
  
}
