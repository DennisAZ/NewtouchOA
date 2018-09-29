package yh.plugins.workflow;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.StaticData;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;

public class YHPurchasePlugin implements YHIWFPlugin{

	@Override
	public String after(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	@Override
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
		    
		      String purId =  wf.getData(conn, flowId, runId, "采购单ID");
		      
		      //修改状态为审核中，此时订单不能修改
		      String sql = "update erp_purchase set status = '"+StaticData.AUDITING+"' where id='"+purId+"'";
		      Statement st  = conn.createStatement();
		      st.executeUpdate(sql);
//		      String sqls = "update erp_finance_out set status = '"+StaticData.NEW_CREATE+"' where type_id='"+purId+"'";
//		      Statement sts  = conn.createStatement();
//		      sts.executeUpdate(sqls);
		} catch(Exception ex) {
		      throw ex;
		    }
		    
		return null;
	}

}
