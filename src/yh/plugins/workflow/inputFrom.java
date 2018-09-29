package yh.plugins.workflow;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.subsys.oa.finance.logic.YHBudgetApplyLogic;
public class inputFrom implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    // TODO Auto-generated method stub
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
      int runId = Integer.parseInt(runIdStr);

      YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId,"预算ID");
      String seqId = rd.getItemData();
      if (YHUtility.isNullorEmpty(seqId)) {
        mage = "预算ID为空,不能结算!";
      }else {
        mage = null;
      }
    } catch(Exception ex) {
      throw ex;
    }
    return mage;
  }
}
