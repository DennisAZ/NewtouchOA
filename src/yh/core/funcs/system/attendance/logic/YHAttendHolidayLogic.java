package yh.core.funcs.system.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import yh.core.funcs.system.attendance.data.YHAttendHoliday;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHAttendHolidayLogic {
  private static Logger log = Logger.getLogger(YHAttendHolidayLogic.class);
  public void addHoliday(Connection dbConn, YHAttendHoliday holiday) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, holiday);  
  }
  public List<YHAttendHoliday> selectHoliday(Connection dbConn,String[] str) throws Exception {
    List<YHAttendHoliday> holidayList = new ArrayList<YHAttendHoliday>();
    YHORM orm = new YHORM();
    holidayList = orm.loadListSingle(dbConn, YHAttendHoliday.class, str);
    return holidayList;
  }
  public YHAttendHoliday selectHolidayById(Connection dbConn,String seqIds) throws Exception {
    YHORM orm = new YHORM();
    YHAttendHoliday holiday = new YHAttendHoliday ();
    int seqId = 0;
    if(!seqIds.equals("")){
      seqId = Integer.parseInt(seqIds);
      holiday = (YHAttendHoliday) orm.loadObjSingle(dbConn, YHAttendHoliday.class, seqId);
    }
   
    return holiday;
  }
  public void updateHoliday(Connection dbConn, YHAttendHoliday holiday) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, holiday);
  }
  public void deleteHoliday(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHAttendHoliday.class, Integer.parseInt(seqId));
  }
  public void deleteAllHoliday(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "delete from oa_attendance_festival";
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }   
  }
}
