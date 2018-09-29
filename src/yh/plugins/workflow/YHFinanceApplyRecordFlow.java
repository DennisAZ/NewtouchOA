package yh.plugins.workflow;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.finance.data.YHFinanceApplyRecord;
import yh.subsys.oa.finance.logic.YHFinanceApplyRecordLogic;

public class YHFinanceApplyRecordFlow implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    return null;
  }
  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String mage = "";
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      String flowIdStr = request.getParameter("flowId");
      String runIdStr = request.getParameter("runId");
      YHFlowRunUtility wf = new YHFlowRunUtility();
      int flowId = Integer.parseInt(flowIdStr);

      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//系统时间
      String time = sf.format(new java.util.Date());
      String operator = String.valueOf(person.getSeqId());//创建人
      Date operateDate = Date.valueOf(time);//创建时间
      int runId = Integer.parseInt(runIdStr);//工作流ID

      YHFlowRunData det =  wf.getFlowRunData(dbConn, flowId, runId,"领用部门ID");
      String deptId = det.getItemData() ;//登录人的部门ID，领用部门ID

      YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId,"领用人ID");
      String applyClaimer = rd.getItemData();

      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId,"领用项目");
      String applyProject = rd2.getItemData();

      YHFlowRunData rd3 =  wf.getFlowRunData(dbConn, flowId, runId,"领用日期");
      Date applyDate = Date.valueOf(rd3.getItemData());

      int applyYear = Integer.parseInt(time.substring(0,4));//年份

      //YHFlowRunData rd4 =  wf.getFlowRunData(dbConn, flowId, runId,"领用金额");
      //YHFlowRunData rd5 =  wf.getFlowRunData(dbConn, flowId, runId,"备注");

      YHFlowRunData rd6 =  wf.getFlowRunData(dbConn, flowId, runId,"部门主管ID");
      String deptDirector = rd6.getItemData();

      YHFlowRunData rd7 =  wf.getFlowRunData(dbConn, flowId, runId,"部门主管审批时间");
      Date deptDirectorDate = Date.valueOf(rd7.getItemData());

      YHFlowRunData rd8 =  wf.getFlowRunData(dbConn, flowId, runId,"部门主管审批内容");
      String deptDirectorContent = rd8.getItemData();

      YHFlowRunData rd9 =  wf.getFlowRunData(dbConn, flowId, runId,"财务签发人ID");
      String financeSignatory = rd9.getItemData();

      YHFlowRunData rd10 =  wf.getFlowRunData(dbConn, flowId, runId,"签发时间");
      Date signDate = Date.valueOf(rd10.getItemData());

      YHFlowRunData getId =  wf.getFlowRunData(dbConn, flowId, runId,"预算ID");
      String budgetId = getId.getItemData();

      YHFlowRunData cheque = wf.getFlowRunData(dbConn, flowId, runId,"支票领用详细");
      String chequeAccount = "";
      String applyItem = "";  
      String applyMemo = "";
      String money = "";
      if(!YHUtility.isNullorEmpty(cheque.getItemData())){
        String detailContent = cheque.getItemData();
        String dcs[] = detailContent.split("\n");
        for(int i = 0; i< dcs.length;i++){
          String dc= dcs[i];
          if (!YHUtility.isNullorEmpty(dc)&&dc.endsWith("`")) {
            dc = dc.substring(0, dc.length()-1);
          }
          String[] temp = dc.split("`");
          chequeAccount = "";
          applyItem = "";
          applyMemo = "";
          money = "";
          for (int j = 0; j< temp.length;j++) {
            if(j == 0){
              chequeAccount = temp[0];
            }
            if(j == 1){
              applyMemo = temp[1];
            }
            if(j == 2){
              applyItem = temp[2];
            }
            if(j == 3){ 
              money = temp[3];
            }
          }
          boolean flage = true;
          double applyMoney =0;
          try {
            applyMoney = Double.parseDouble(money);
          } catch (Exception ex) {
            flage = false;
          }
          if (flage) {
            YHFinanceApplyRecord record = new YHFinanceApplyRecord();
            record.setApplyClaimer(applyClaimer);
            record.setApplyDate(applyDate);
            record.setRunId(runId);
            record.setApplyMemo(applyMemo);
            record.setApplyItem(applyItem);
            record.setApplyMoney(applyMoney);
            record.setApplyProject(applyProject);
            record.setBudgetId(budgetId);
            record.setApplyYear(applyYear);
            record.setChequeAccount(chequeAccount);
            record.setDeptDirector(deptDirector);
            record.setSignDate(signDate);
            record.setOperator(operator);
            record.setOperateDate(operateDate);
            record.setDeptDirector(deptDirector);
            record.setDeptDirectorContent(deptDirectorContent);
            record.setDeptDirectorDate(deptDirectorDate);
            record.setDeptId(deptId);
            record.setFinanceDirector(financeSignatory);  

            YHFinanceApplyRecordLogic rLogic = new YHFinanceApplyRecordLogic();
            rLogic.addFlow(dbConn, record);
            mage = null;
          }else {
            mage = "金额只能为数字!";
          }

        }
      }else {
        mage = null;
      }
    } catch(Exception ex) {
      throw ex;
    }
    return mage;
  }

}
