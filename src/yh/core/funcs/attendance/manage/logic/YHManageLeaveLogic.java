package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHManageLeaveLogic {
  private static Logger log = Logger.getLogger(YHManageLeaveLogic.class);
  public List<YHAttendLeave> selectLeaveManage(Connection dbConn,int userId) throws Exception {
    List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
    Statement stmt = null;
    ResultSet rs = null;
    //String sql ="select * from oa_attendance_off where LEADER_ID = '" + userId +"' and STATUS = '1' and (ALLOW = '0' or ALLOW = '3')";
    String sql ="select * from oa_attendance_off where LEADER_ID = '" + userId +"' and ALLOW = '0'";
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          YHAttendLeave yhal = new YHAttendLeave();
          yhal.setSeqId(rs.getInt("SEQ_ID"));
          yhal.setUserId(rs.getString("USER_ID"));
          yhal.setLeaderId(rs.getString("LEADER_ID"));
          yhal.setAnnualLeave(rs.getInt("ANNUAL_LEAVE"));
          yhal.setLeaveType(rs.getString("LEAVE_TYPE"));
          yhal.setReason(rs.getString("REASON"));
          yhal.setRegisterIp(rs.getString("REGISTER_IP"));
          yhal.setStatus(rs.getString("STATUS"));
          yhal.setAllow(rs.getString("ALLOW"));
          if(rs.getString("DESTROY_TIME") != null){
            yhal.setDestroyTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss.s", rs.getString("DESTROY_TIME")));
          }
          yhal.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss.s", rs.getString("LEAVE_DATE1")));
          yhal.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss.s", rs.getString("LEAVE_DATE2")));
          leaveList.add(yhal);
        }  
      }catch(Exception ex) {
         throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
    } 
    return leaveList;
  }
  public  List<YHAttendLeave> selectLeave(Connection dbConn,String str[]) throws Exception {
    List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
    YHORM orm = new YHORM();
    leaveList =  orm.loadListSingle(dbConn, YHAttendLeave.class, str);
    return leaveList;
  }
}
