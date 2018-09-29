package yh.plugins.workflow;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;

public class YHFinanceApply implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    return null;
  }
  public String before(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    String mage = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String flowIdStr = request.getParameter("flowId");
      String runIdStr = request.getParameter("runId");
      YHFlowRunUtility wf = new YHFlowRunUtility();
      int flowId = Integer.parseInt(flowIdStr);
      int runId = Integer.parseInt(runIdStr);//工作流ID

      YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId,"领用金额");
      String money = rd.getItemData();

      boolean flage = true;
      double applyMoney =0;
      try {
        applyMoney = Double.parseDouble(money);
        mage = null;
      } catch (Exception ex) {
        flage = false;
      }
      if (!flage) {
        mage = "金额只能为数字!";
      }
    } catch(Exception ex) {
      throw ex;
    }
    return mage;
  }
}
