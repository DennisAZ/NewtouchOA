package yh.plugins.workflow;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.StaticData;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;

public class YHLossApply implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    return null;
  }
  public String before(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      Connection conn = requestDbConn.getSysDbConn();
		      String flowIdStr = request.getParameter("flowId");
		      String runIdStr = request.getParameter("runId");
		    
		      int runId = Integer.parseInt(runIdStr);
		      int flowId = Integer.parseInt(flowIdStr);
		     
		      YHFlowRunUtility wf = new YHFlowRunUtility();
		    
		      String loosId =  wf.getData(conn, flowId, runId, "报损单id");
		      
		      //修改状态为审核中，此时订单不能修改
		      String sql = "update erp_loss set status = '"+StaticData.RUNNING+"' where id='"+loosId+"'";
		      Statement st  = conn.createStatement();
		      st.executeUpdate(sql);
		} catch(Exception ex) {
		      throw ex;
		    }
		    
		return null;
	}
}
