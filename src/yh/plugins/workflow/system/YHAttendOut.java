package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;

public class YHAttendOut implements YHIWFHookPlugin {

  @Override
  public String execute(Connection conn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
    String outId = (String)arrayHandler.get("KEY");
    String reason = (String)arrayHandler.get("REASON");
    if (agree){
      String sql="select * from oa_attendance_out where SEQ_ID='"+outId+"' and ALLOW='0'";
      Statement stmt=null;
      ResultSet rs=null;
      try { 
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next())
      {
         String SUBMIT_TIME=rs.getString("SUBMIT_TIME"); 
         String OUT_TIME1=rs.getString("OUT_TIME1");
         String OUT_TIME2=rs.getString("OUT_TIME2");
         String OUT_TYPE=rs.getString("OUT_TYPE");
         String USER_ID1=rs.getString("USER_ID");
         
         String OUT_DAY =  SUBMIT_TIME.substring(0, 10);
         String CAL_TIME = OUT_DAY+" "+OUT_TIME1;
         String END_TIME = OUT_DAY+" "+OUT_TIME2;  
 
       String update="insert into oa_schedule(USER_ID,CAL_TIME,END_TIME,CAL_TYPE,CAL_LEVEL,CONTENT,OVER_STATUS) values ('"+USER_ID1+"','"+CAL_TIME+"','"+END_TIME+"','1','','"+OUT_TYPE+"','0')";
         YHWorkFlowUtility.updateTableBySql(update, conn);
      }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stmt, rs, null); 
      }
      sql="update oa_attendance_out set ALLOW='1' where SEQ_ID='"+outId+"'"; 
      YHWorkFlowUtility.updateTableBySql(sql, conn);
      
      
      
    }
    else{
      
      String query="update oa_attendance_out set ALLOW='2',REASON='"+reason+"' where SEQ_ID='"+outId+"'";
      YHWorkFlowUtility.updateTableBySql(query, conn);
    }
    
    return null;
  }

}
