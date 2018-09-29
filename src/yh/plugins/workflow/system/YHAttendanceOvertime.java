package yh.plugins.workflow.system;

import java.sql.Connection;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;

public class YHAttendanceOvertime implements YHIWFHookPlugin{

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {


       String OVERTIME_ID=(String)arrayHandler.get("KEY");
        String REASON=(String)arrayHandler.get("REASON");

        if(agree)
        {
           String update="update oa_timeout_record set status='1' where SEQ_ID='"+OVERTIME_ID+"'";   
           YHWorkFlowUtility.updateTableBySql(update, conn);
        }
        else
        {
           String update="update oa_timeout_record set status='2',REASON='"+REASON+"' where SEQ_ID='"+OVERTIME_ID+"'";
           YHWorkFlowUtility.updateTableBySql(update, conn);
        }

    
    return null;
  }
  
  
  
}
