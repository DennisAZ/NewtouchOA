package yh.pda.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHFlowProcessLogic;
import yh.core.funcs.workflow.util.YHFlowRunLogLogic;
import yh.core.funcs.workflow.util.YHFlowRunLogic;
import yh.core.funcs.workflow.util.YHFlowTypeLogic;
import yh.core.funcs.workflow.praser.YHPraseData2Form;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHPdaWorkflowLogic {

  
  public Map getHandlerMsg(YHPerson user , int runId, int prcsId, String flowPrcs , String ip , Connection conn  , String imgPath , String isWriteLog) throws Exception {
    // TODO Auto-generated method stub
    YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
    YHFlowTypeLogic flowTypeLogic =  new YHFlowTypeLogic();
    YHFlowRun flowRun = flowRunLogic.getFlowRunByRunId(runId , conn);
    Date date = new Date();
    YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowRun.getFlowId() , conn);
    boolean  attachPrivWrite =  false;
    boolean  filePrivWrite = true;
    YHFlowProcess flowProcess = null;
    String attachPriv = "";
    Map runPrcsQuery = new HashMap();
    runPrcsQuery.put("RUN_ID", runId);
    runPrcsQuery.put("PRCS_ID", prcsId);
    runPrcsQuery.put("USER_ID", user.getSeqId());
    YHORM orm = new YHORM();
    YHFlowRunPrcs runProcess = (YHFlowRunPrcs) orm.loadObjSingle(conn, YHFlowRunPrcs.class, runPrcsQuery);
    if ("1".equals(flowType.getFlowType())) {
      YHFlowProcessLogic flowPrcsLogic = new  YHFlowProcessLogic();
      //查出相关步骤
      flowProcess = flowPrcsLogic.getFlowProcessById(flowRun.getFlowId(), flowPrcs , conn);
      String item = flowProcess.getPrcsItem();
      //附件是否可写
      if ("1".equals(runProcess.getOpFlag())) {
        attachPrivWrite = YHWorkFlowUtility.findId(item, "[A@]");
        attachPriv = flowProcess.getAttachPriv();
        if (attachPriv == null) {
          attachPriv = "";
        }
        //文号是否可写
        filePrivWrite = YHWorkFlowUtility.findId(item, "[B@]");
      }
    } else {
      attachPriv = "1,2,3,4,5";
      attachPrivWrite = true;
    }
    //查出第一步骤
    //查出运行中的步骤
    
    //查出表单
    YHFlowFormType fft = (YHFlowFormType) orm.loadObjSingle(conn, YHFlowFormType.class, flowType.getFormSeqId());
    
    
    //查询表单字段信息
    Map formItemQuery = new HashMap();
    formItemQuery.put("FORM_ID", flowType.getFormSeqId());
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    Map runDataQuery = new HashMap();
    runDataQuery.put("RUN_ID", runId);
    List<YHFlowRunData> frdList = orm.loadListSingle(conn, YHFlowRunData.class , runDataQuery);
    
    if(runProcess.getPrcsFlag().equals("1")){
      runProcess.setPrcsFlag("2");
      runProcess.setPrcsTime(date);
      orm.updateSingle(conn, runProcess);
      if( "1".equals(runProcess.getTopFlag())
          && "1".equals(runProcess.getOpFlag())){
        String query = "update oa_fl_run_prcs set OP_FLAG=0 WHERE "
          + " USER_ID<>'" + user.getSeqId() +"'  "
          + " AND RUN_ID='" + flowRun.getRunId() + "'  "
          + " AND PRCS_ID='" + runProcess.getPrcsId() + "'  "
          + " AND FLOW_PRCS='"+ runProcess.getFlowPrcs() +"'";
        Statement stm = null;
        try {
          stm = conn.createStatement();;
          stm.executeUpdate(query);
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, null, null);
        }
      }
      if(runProcess.getPrcsId()  == 1){
        flowRun.setBeginUser(user.getSeqId());
        flowRun.setBeginTime(date);
        orm.updateSingle(conn, flowRun);
        //父流程先不做
      }
    }
    //修改上一步骤状态为已经办理完毕
    int oldPrcsId = prcsId - 1;
    String query = "update oa_fl_run_prcs set PRCS_FLAG='4' WHERE "
          + " RUN_ID='"+runId+"'  "
          + " AND PRCS_ID='"+oldPrcsId+"'";
    if(!"0".equals(runProcess.getParent()) 
        && !YHUtility.isNullorEmpty(runProcess.getParent()))
       query +=" AND FLOW_PRCS IN ("+ runProcess.getParent() +")";
    Statement stm = null;
    try {
      stm = conn.createStatement();;
      stm.executeUpdate(query);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null);
    }
    String flowTypeStr = flowType.getFlowType();
    boolean hasEnd = false;
    if ("2".equals(flowTypeStr) && "1".equals(runProcess.getOpFlag())) {
       query = "SELECT * from oa_fl_run_prcs where RUN_ID="+runId+" and PRCS_ID>'"+prcsId+"' and PRCS_FLAG='5'";
       Statement stm1 = null;
       ResultSet rs1 = null;
       try {
         stm1 = conn.createStatement();
         rs1 = stm1.executeQuery(query);
         if (!rs1.next()) {
           hasEnd = true;
         }
       } catch(Exception ex) {
         throw ex;
       } finally {
         YHDBUtility.close(stm1, rs1, null); 
       }
    } else if ("2".equals(flowTypeStr) && "2".equals(runProcess.getTopFlag())) {
      query = "select 1 FROM oa_fl_run_prcs WHERE RUN_ID='"+runId+"' AND PRCS_ID='"+prcsId+"' AND FLOW_PRCS='"+flowPrcs+"' AND USER_ID<>'"+user.getSeqId()+"' AND PRCS_FLAG IN('1','2')";
      Statement stm1 = null;
      ResultSet rs1 = null;
      try {
        stm1 = conn.createStatement();
        rs1 = stm1.executeQuery(query);
        if (!rs1.next()) {
          hasEnd = true;
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm1, rs1, null); 
      }
    }
    //设置宏标记
    String formMsg = "";
    if (list.size() > 0) {
      String modelShort = flowRunLogic.analysisAutoFlag(flowRun, flowType, fft, conn , imgPath);
      YHPraseData2Form pdf = new YHPraseData2Form();
      formMsg  = pdf.parseForm(user
          , modelShort
          , flowProcess
          , runProcess
          , flowType
          , frdList
          , list
          , ip
          , conn);
//      formMsg = formMsg.replaceAll("\'", "\\\\'");
      formMsg = formMsg.replaceAll("\n", "");
      formMsg = formMsg.replaceAll("\\\n", "");
    }
    String js = (fft == null || fft.getScript() == null) ? "" : fft.getScript();
    String css = ( fft == null || fft.getCss() == null) ? "" : fft.getCss();
    js = js.replaceAll("\'", "\\\\'");
    js = js.replaceAll("[\n-\r]", "");
    css = css.replaceAll("\'", "\\\\'");
    css = css.replaceAll("[\n-\r]", "");
    
//    StringBuffer sb = new StringBuffer();
    Map map = new HashMap();
    String feedback = "0";
    if(flowProcess!= null && flowProcess.getFeedback() != null){
      feedback = flowProcess.getFeedback();
    }
    String allowBack = "0";//不允许回退
    if(flowProcess != null && flowProcess.getAllowBack() != null){
      allowBack = flowProcess.getAllowBack();
    }
    String focusUser =  flowRun.getFocusUser();
    YHPersonLogic logic = new YHPersonLogic();
    String focusUserName = logic.getNameBySeqIdStr(focusUser, conn);
    map.put("formMsg", formMsg);
    map.put("js", js);
    map.put("css", css);
//    sb.append("{formMsg:'" + formMsg + "'");
//    sb.append(",js:'" + js + "'");
//    sb.append(",css:'" + css + "'");
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    
    map.put("runName", runName);
    if ("1".equals(isWriteLog)) {
      YHFlowRunLogLogic log = new YHFlowRunLogLogic();
      int iFlowPrcs = 0;
      if (YHUtility.isInteger(flowPrcs)) {
        iFlowPrcs = Integer.parseInt(flowPrcs);
      }
      log.runLog(runId, prcsId, iFlowPrcs, user.getSeqId(), 8, "访问了工作流：" + runName + "的办理界面！", ip, conn);
    }
    return map;
  }
}
