package yh.custom.attendance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.custom.attendance.data.YHPersonalLeave;

public class YHPersonalLeaveLogic{
  private static Logger log = Logger.getLogger(YHPersonalLeaveLogic.class);
  public void addLeave(Connection dbConn, YHPersonalLeave leave) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, leave);  
  }
  public void updateLeave(Connection dbConn,YHPersonalLeave leave) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, leave);
  }
  public List<YHPersonalLeave>  selectLeave(Connection dbConn,String[] map) throws Exception {
    List<YHPersonalLeave> leaveList = new ArrayList<YHPersonalLeave>();
    YHORM orm = new YHORM();
    leaveList = orm.loadListSingle(dbConn, YHPersonalLeave.class, map);
    return leaveList;
  }
  public void deleteLeaveById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHPersonalLeave.class, Integer.parseInt(seqId));
  }
  public YHPersonalLeave selectLeaveById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHPersonalLeave leave = (YHPersonalLeave) orm.loadObjSingle(dbConn, YHPersonalLeave.class, Integer.parseInt(seqId));
    return leave;
  }
  
  public void updateLeaveAllow(Connection dbConn,String seqId,String allow ,String reason) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String sql = " update oa_users_leave set ALLOW = '" + allow +  "'" ;
    if(!YHUtility.isNullorEmpty(allow)){
      sql = sql + " , REASON = '" + reason.replace("'", "''")  + "'";
    }
    sql = sql + " where  SEQ_ID = " + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
    }catch(Exception ex) {
         throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, log);
    } 
  }
  public void updateLeaveStatus(Connection dbConn,String seqId,String status)throws Exception{ 
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "update  oa_users_leave set STATUS = '"  + status + "' where SEQ_ID = " + seqId;
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
