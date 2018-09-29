package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;

public class YHMeetingApply implements YHIWFHookPlugin {
  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
      String M_ID=(String)arrayHandler.get("KEY");
      if(agree)  {
         String update="update oa_conference set M_STATUS='1' where SEQ_ID='"+ M_ID +"'";
         YHWorkFlowUtility.updateTableBySql(update, conn);
         
//         String query="select * from MEETING where SEQ_ID='"+M_ID+"'";
//         try {
//           stmt=conn.createStatement();
//           rs=stmt.executeQuery(query);
//           if(rs.next()) {
//             String M_PROPOSER=rs.getString("M_PROPOSER");
//             String M_ATTENDEE=rs.getString("M_ATTENDEE");
//             String SMS_REMIND=rs.getString("SMS_REMIND");
//             String SMS2_REMIND=rs.getString("SMS2_REMIND");
//             String M_NAME=rs.getString("M_NAME");
//             String M_ROOM=rs.getString("M_ROOM");
//             String M_START2 =rs.getString("M_START");   
//             String M_END=rs.getString("M_END");   
//             String RESEND_LONG=rs.getString("RESEND_LONG");
//             int RESEND_SEVERAL=rs.getInt("RESEND_SEVERAL"); 
//             String CALENDAR=rs.getString("CALENDAR");
//             
//             if(RESEND_SEVERAL > 4){
//               RESEND_SEVERAL = 4;
//             }
//               
//             query="select MR_NAME from MEETING_ROOM where SEQ_ID='"+M_ROOM+"'";
//             stmt1=conn.createStatement();
//             rs1=stmt1.executeQuery(query);
//             if(rs1.next()){
//                String MR_NAME=rs1.getString("MR_NAME");
//             }
//             query="select USER_NAME from USER where USER_ID='"+M_PROPOSER+"'";
//             rs1=stmt.executeQuery(query);
//             if(rs1.next()){
//                String M_PROPOSER_NAME=rs1.getString("USER_NAME");
//              }
//            }
//         } catch (Exception ex) {
//           throw ex;
//         } finally  {
//           YHDBUtility.close(stmt1, rs1, null);
//           YHDBUtility.close(stmt, rs, null);
//         }
      }  else {   
        String query ="update oa_conference set M_STATUS='3' where SEQ_ID='"+M_ID+"'";
        YHWorkFlowUtility.updateTableBySql(query, conn);
     }
    return null;
  }

}
