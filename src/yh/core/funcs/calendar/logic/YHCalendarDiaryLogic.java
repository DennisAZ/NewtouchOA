package yh.core.funcs.calendar.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import org.apache.log4j.Logger;

import yh.core.funcs.calendar.data.YHCalendarDiary;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHCalendarDiaryLogic {
  private static Logger log = Logger.getLogger(YHCalendarDiaryLogic.class);
  public int addCalDiary(Connection dbConn,YHCalendarDiary cd) throws Exception {
    YHORM orm = new YHORM();
    if(cd!=null){
      orm.saveSingle(dbConn, cd);
    }
    return 0;
  }
  public String selectDiaryId(Connection dbConn,String str) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String diaryIdStr = "";
    String sql = "select * from oa_schedule_detail ";
    if(str!=null&&!str.equals("")){
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          int seqId = rs.getInt("SEQ_ID");
          String diaryId = rs.getString("diary_Id");
          String calendarId = rs.getString("calendar_Id");
          if(calendarId!=null&&!calendarId.trim().equals("")){
            String[] calendarIdArray = calendarId.split(",");
            for (int i = 0; i < calendarIdArray.length; i++) {
              if(str.equals(calendarIdArray[i])){
                diaryIdStr = diaryIdStr + diaryId + ",";
                break;
              }
            }
          }
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
       YHDBUtility.close(stmt, rs, log);
      }
    }
    if(!diaryIdStr.equals("")){
      diaryIdStr = diaryIdStr.substring(0, diaryIdStr.length()-1);
    }
    return diaryIdStr ;
  }
  
  public String selectDiaryIdByDate(Connection dbConn,String date) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String diaryIdStr = "";

    if(date!=null&&!date.equals("")){
      String beginDate = YHDBUtility.getDateFilter("CAL_DIA_DATE", date, ">=");
      String endDate = YHDBUtility.getDateFilter("CAL_DIA_DATE", date + " 23:59:59", "<=");
      String sql = "select * from oa_schedule_detail where " + beginDate + " and " + endDate ;
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          int seqId = rs.getInt("SEQ_ID");
          String diaryId = rs.getString("diary_Id");
          String calendarId = rs.getString("calendar_Id");
          if(calendarId!=null&&!calendarId.trim().equals("")){
            diaryIdStr = diaryIdStr + diaryId + ",";
         /*   String[] calendarIdArray = calendarId.split(",");
            for (int i = 0; i < calendarIdArray.length; i++) {
              if(str.equals(calendarIdArray[i])){
                diaryIdStr = diaryIdStr + diaryId + ",";
                break;
              }
            }*/
          }
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
       YHDBUtility.close(stmt, rs, log);
      }
    }
    if(!diaryIdStr.equals("")){
      diaryIdStr = diaryIdStr.substring(0, diaryIdStr.length()-1);
    }
    return diaryIdStr ;
  }
  public String selectCalendarId(Connection dbConn,String str) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String calendarStr = "";
    String sql = "select * from oa_schedule_detail where DIARY_ID =(" + str + ")";
    if(str!=null&&!str.equals("")){
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        if(rs.next()){
          calendarStr = rs.getString("CALENDAR_ID");
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
       YHDBUtility.close(stmt, rs, log);
      }
    }
    return calendarStr ;
  }
}
