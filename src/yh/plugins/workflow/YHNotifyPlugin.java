package yh.plugins.workflow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.StaticData;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

/**
 * 工作流插件接口
 * @author jpt
 *
 */
public class YHNotifyPlugin implements YHIWFPlugin{
  /**
   * 节点执行前执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
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
			    
			      String notifyId =  wf.getData(conn, flowId, runId, "生产通知单id");
			      
			      //修改状态为审核中，此时订单不能修改
			      String sql = "update erp_produce_notify set status = '"+StaticData.AUDITING+"' where id='"+notifyId+"'";
			      Statement st  = conn.createStatement();
			      st.executeUpdate(sql);
			} catch(Exception ex) {
			      throw ex;
			    }
			    
			return null;
		}
  /**
   * 节点执行完毕执行
   * @param request
   * @param response
   * @return
   */
  public String after(HttpServletRequest request, HttpServletResponse response) {
    //System.out.println("------------结束啦");
    return null;
  }
}
