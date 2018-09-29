package yh.plugins.workflow;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.subsys.oa.finance.data.YHChargeExpense;
import yh.subsys.oa.finance.logic.YHChargeExpenseLogic;
import yh.subsys.oa.finance.logic.YHFinanceApplyRecordLogic;

public class YHChargeExpenseFlow implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mage = "";
    try {
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//系统时间
      String time = sf.format(new java.util.Date());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String flowIdStr = request.getParameter("flowId");
      String runIdStr = request.getParameter("runId");

      int runId = Integer.parseInt(runIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      YHFlowRunUtility wf = new YHFlowRunUtility();
      YHFlowRunData seq =  wf.getFlowRunData(dbConn, flowId, runId, "支票ID串");
      String seqIdStr = "" ;
      if (seq != null) {
        seq.getItemData();
      }
      String expenseId = "0";
      if(!YHUtility.isNullorEmpty(seqIdStr)) {
        YHFinanceApplyRecordLogic record = new YHFinanceApplyRecordLogic();
        record.updateExpense2(dbConn, seqIdStr);
        //expenseId = "1";
      }

      YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId, "报销人的部门ID");
      String deptId = rd.getItemData();

      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId, "报销申请人ID");
      String chargeUser = rd2.getItemData();

      YHFlowRunData rd3 =  wf.getFlowRunData(dbConn, flowId, runId, "报销日期");
      Date chargeDate = Date.valueOf(rd3.getItemData());

      YHFlowRunData rd4 =  wf.getFlowRunData(dbConn, flowId, runId, "报销金额");
      String money = rd4.getItemData();

      YHFlowRunData rd5 =  wf.getFlowRunData(dbConn, flowId, runId, "备注");
      String chargeMemo = rd5.getItemData();

      YHFlowRunData rd6 =  wf.getFlowRunData(dbConn, flowId, runId, "部门审批人ID");
      String deptAuditUser = rd6.getItemData();

      YHFlowRunData rd7 =  wf.getFlowRunData(dbConn, flowId, runId, "部门审批时间");
      Date deptAuditDate = null;
      if (!YHUtility.isNullorEmpty(rd7.getItemData())) {
        deptAuditDate = Date.valueOf(rd7.getItemData());
      } else {
        deptAuditDate = Date.valueOf(time);
      }
      YHFlowRunData rd8 =  wf.getFlowRunData(dbConn, flowId, runId, "部门审批内容");
      String deptAuditContent = rd8.getItemData();

      YHFlowRunData rd9 =  wf.getFlowRunData(dbConn, flowId, runId, "费用报销信息");
      String chargeItem = rd9.getItemData();

      YHFlowRunData rd10 =  wf.getFlowRunData(dbConn, flowId, runId, "财务审批人ID");
      String financeAuditUser = rd10.getItemData();

      YHFlowRunData rd12 =  wf.getFlowRunData(dbConn, flowId, runId, "预算ID");
      String budgetId = rd12.getItemData();

      int chargeYear = Integer.parseInt(time.substring(0,4));//年份
      boolean flage = true;
      double chargeMoney = 0;
      try {
        chargeMoney = Double.parseDouble(money);
      } catch (Exception ex) {
        flage = false;
      }
      if (flage) {
        YHChargeExpense expense = new YHChargeExpense();
        expense.setBudgetId(budgetId);
        expense.setChargeDate(chargeDate);
        expense.setChargeItem(chargeItem);
        expense.setChargeMemo(chargeMemo);
        expense.setChargeMoney(chargeMoney);
        expense.setChargeUser(chargeUser);
        expense.setChargeYear(chargeYear);
        expense.setCostId(0);
        expense.setDeptAuditContent(deptAuditContent);
        expense.setDeptAuditDate(deptAuditDate);
        expense.setDeptAuditUser(deptAuditUser);
        expense.setRunId(runId);
        expense.setExpense(expenseId);
        expense.setDeptId(deptId);
        expense.setFinanceAuditUser(financeAuditUser);
        YHChargeExpenseLogic exLogic = new YHChargeExpenseLogic();
        exLogic.addFlow(dbConn,expense);
        mage = null;
      }else {
        mage = "金额只能为数字!";
      }
    } catch(Exception ex) {
      throw ex;
    }
    return mage;
  }

}
