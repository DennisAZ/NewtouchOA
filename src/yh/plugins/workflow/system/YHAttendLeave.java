package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHAttendLeave implements YHIWFHookPlugin {

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData , boolean agree) throws Exception {
    // TODO Auto-generated method stub
    String leaveId = (String)arrayHandler.get("KEY");
    String peason =  (String)arrayHandler.get("REASON");
    if (agree) {
      String query = "SELECT LEAVE_TYPE from oa_attendance_off where SEQ_ID='"+leaveId+"'";
      Statement stm3 = null; 
      ResultSet rs3 = null; 
      String LEAVE_TYPE = "";
      try { 
        stm3 = conn.createStatement(); 
        rs3 = stm3.executeQuery(query); 
        if (rs3.next()) {
          LEAVE_TYPE =YHUtility.null2Empty(rs3.getString("LEAVE_TYPE"));
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      String update = "update oa_attendance_off set ALLOW='1' where SEQ_ID='"+leaveId+"'";
      if (LEAVE_TYPE.trim().startsWith("补假：")) {
        update = "update oa_attendance_off set ALLOW='3',STATUS='2' where SEQ_ID='"+leaveId+"'";
      }
      YHWorkFlowUtility.updateTableBySql(update, conn);
    } else {
      String update = "update oa_attendance_off set ALLOW='2'";
      if (peason != null || "null".equals(peason)) {
        update += ",REASON='"+peason+"' ";
      }
      update += " where SEQ_ID='"+leaveId+"'";
      YHWorkFlowUtility.updateTableBySql(update, conn);
    }
    return null;
  }

}
