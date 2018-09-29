package yh.plugins.workflow;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;

/**
 * 工作流插件接口
 * @author jpt
 *
 */
public class MyPlugin2 implements YHIWFPlugin{
  /**
   * 节点执行前执行
   * @param request
   * @param response
   * @return
   * @throws SQLException 
   */
  public String before(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    //System.out.println("------------开始啦");
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = requestDbConn.getSysDbConn();
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    
    
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
