package yh.plugins.workflow.system;

import java.sql.Connection;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;

public class YHAttendEvection implements YHIWFHookPlugin {

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
    String evectionId = (String)arrayHandler.get("KEY");
    String not_reason = (String)arrayHandler.get("NOT_REASON");
    if (agree) {
      String query="update oa_attendance_trip set ALLOW='1' where SEQ_ID='"+evectionId+"'";
      YHWorkFlowUtility.updateTableBySql(query, conn);
   }
   else
   {
      String query="update oa_attendance_trip set ALLOW='2',NOT_REASON='"+not_reason+"' where SEQ_ID='"+evectionId+"'";
      YHWorkFlowUtility.updateTableBySql(query, conn);
   }
      
    
    return null;
  }

}
