package yh.plugins.workflow.system;

import java.sql.Connection;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHOut;

public class YHVehicleApply implements YHIWFHookPlugin{

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
      String OPERATOR_REASON=(String)arrayHandler.get("OPERATOR_REASON");
      String VU_ID=(String)arrayHandler.get("KEY");
        if(agree)
        { 
           String update="update oa_vehicle_usage set VU_STATUS='1' where SEQ_ID='"+VU_ID+"'";
           YHWorkFlowUtility.updateTableBySql(update, conn);  
        }
        else
        {
           String query="update oa_vehicle_usage set VU_STATUS='3',OPERATOR_REASON='"+OPERATOR_REASON+"' where SEQ_ID='"+VU_ID+"'";
           YHWorkFlowUtility.updateTableBySql(query, conn);
         
       }
    return null;
  }

      

  
}
