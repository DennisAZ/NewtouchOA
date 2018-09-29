package yh.core.funcs.attendance.personal.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHAttendOutLogic {
  private static Logger log = Logger.getLogger(YHAttendOutLogic.class);
  public void addOut(Connection dbConn, YHAttendOut out) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, out);  
  }
  public List<YHAttendOut> selectOut(Connection dbConn,Map map) throws Exception {
    List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    YHORM orm = new YHORM();
    outList = orm.loadListSingle(dbConn, YHAttendOut.class, map);
    return outList;
  }
  public List<YHAttendOut> selectOut(Connection dbConn,String[] str) throws Exception {
    List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    YHORM orm = new YHORM();
    outList = orm.loadListSingle(dbConn, YHAttendOut.class, str);
    return outList;
  }
  public YHAttendOut selectOutById(Connection dbConn,String seqId) throws Exception {
    YHAttendOut out = new YHAttendOut();
    YHORM orm = new YHORM();
    out = (YHAttendOut) orm.loadObjSingle(dbConn, YHAttendOut.class, Integer.parseInt(seqId));
    return out;
  }
  
  public void deleteOutById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHAttendOut.class, Integer.parseInt(seqId));
  }
  
  public void updateStatus(Connection dbConn,Map map) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, "attendOut",map);
  }
  
  public void updateOut(Connection dbConn,YHAttendOut out) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, out);
  }
  
  public List<YHAttendOut>  selectHistoryOut(Connection dbConn,String[] map) throws Exception {
    List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
    YHORM orm = new YHORM();
    outList = orm.loadListSingle(dbConn, YHAttendOut.class, map);
    return outList;
  }
  
  public int getAttendOutCountLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    int result = 0;
    String sql = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select count(*) from oa_attendance_out where USER_ID = '" + userId + "' and ALLOW = '1' and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("SUBMIT_TIME", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
}
