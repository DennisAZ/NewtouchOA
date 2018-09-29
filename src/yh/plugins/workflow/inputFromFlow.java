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
public class inputFromFlow implements YHIWFPlugin{

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
      if(!YHUtility.isNullorEmpty(seqId)) {
        YHBudgetApplyLogic budget = new YHBudgetApplyLogic();
        budget.updateBudgetToSettle(dbConn, seqId);
      }
      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId,"报销ID串");
      String seqIdStr = rd2.getItemData();
      //System.out.println(seqIdStr);
      //      String applyDate = rd.getItemData();
      //
      //      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId,"编号");
      //
      //      YHFlowRunData rd3 =  wf.getFlowRunData(dbConn, flowId, runId,"团组名称");    
      //
      //      YHFlowRunData cheque = wf.getFlowRunData(dbConn, flowId, runId,"部门");
      //     
      //      YHFlowRunData rd4 =  wf.getFlowRunData(dbConn, flowId, runId,"申请人姓名");
      //  
      //      YHFlowRunData rd5 =  wf.getFlowRunData(dbConn, flowId, runId,"预收款实际到帐金额");
      //
      //      YHFlowRunData rd6 =  wf.getFlowRunData(dbConn, flowId, runId,"到帐方式");
      //     
      //      YHFlowRunData rd7 =  wf.getFlowRunData(dbConn, flowId, runId,"余款到帐金额");
      //  
      //      YHFlowRunData rd8 =  wf.getFlowRunData(dbConn, flowId, runId,"余款到帐方式");
      //     
      //      YHFlowRunData rd9 =  wf.getFlowRunData(dbConn, flowId, runId,"发票金额开据数");
      //
      //      YHFlowRunData rd10 =  wf.getFlowRunData(dbConn, flowId, runId,"追加");
      //      
      //      YHFlowRunData rd11 =  wf.getFlowRunData(dbConn, flowId, runId,"实际收入金额");
      //      
      //      YHFlowRunData rd12 =  wf.getFlowRunData(dbConn, flowId, runId,"实收金额(大写)");
      //      
      //      YHFlowRunData rd13 =  wf.getFlowRunData(dbConn, flowId, runId,"实际支出金额");
      //      
      //      YHFlowRunData rd14 =  wf.getFlowRunData(dbConn, flowId, runId,"实支金额(大写)");
      //
      //      YHFlowRunData rd15 =  wf.getFlowRunData(dbConn, flowId, runId,"备注");
      //      
      //      YHFlowRunData rd16 =  wf.getFlowRunData(dbConn, flowId, runId,"主管业务会领导");
      //      
      //      YHFlowRunData rd17 =  wf.getFlowRunData(dbConn, flowId, runId,"财务主管");
      //      
      //      YHFlowRunData rd18 =  wf.getFlowRunData(dbConn, flowId, runId,"财务人员");
      //      
      //      YHFlowRunData rd19 =  wf.getFlowRunData(dbConn, flowId, runId,"部门领导");
      //      
      //      YHFlowRunData rd20 =  wf.getFlowRunData(dbConn, flowId, runId,"经办人");
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
