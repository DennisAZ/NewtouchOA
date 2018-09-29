package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.praser.YHPraseData2FormEdit;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHFormEditLogic {
  /**
   * 取得编辑界面的相关信息
   * @param loginUser
   * @param runId
   * @param remoteAddr
   * @param dbConn
   * @return
   * @throws Exception 
   */
  public String getEditMsg(YHPerson user, int runId, String ip,
      Connection conn , String imgPath) throws Exception {
    YHORM orm = new YHORM();
    YHFlowTypeLogic flowTypeLogic =  new YHFlowTypeLogic();
    YHFlowRunLogic run =  new YHFlowRunLogic();
    YHFlowRun flowRun = run.getFlowRunByRunId(runId , conn);
    YHFlowType flowType = flowTypeLogic.getFlowTypeById(flowRun.getFlowId() , conn);
    //查出表单
    YHFormVersionLogic logic5  = new YHFormVersionLogic();
    int formId = logic5.getFormSeqId(conn, flowRun.getFormVersion(), flowType.getFormSeqId());
    YHFlowFormType fft = (YHFlowFormType) orm.loadObjSingle(conn, YHFlowFormType.class, formId);
    //查询表单字段信息
    Map formItemQuery = new HashMap();
    
    formItemQuery.put("FORM_ID", formId);
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    Map runDataQuery = new HashMap();
    runDataQuery.put("RUN_ID", runId);
    List<YHFlowRunData> frdList = orm.loadListSingle(conn, YHFlowRunData.class , runDataQuery);
    
    //设置宏标记
    String formMsg = "";
    if (list.size() > 0) {
      String modelShort = run.analysisAutoFlag(flowRun, flowType, fft, conn,imgPath);
      YHPraseData2FormEdit pdf = new YHPraseData2FormEdit();
      formMsg  = pdf.parseForm(user
          , modelShort
          , flowType
          , frdList
          , list
          , ip
          , conn
          , runId);
      formMsg = formMsg.replaceAll("\'", "\\\\'");
      formMsg = formMsg.replaceAll("\\\n", "");
    }
    String js = (fft == null || fft.getScript() == null) ? "" : fft.getScript();
    String css = ( fft == null || fft.getCss() == null) ? "" : fft.getCss();
    js = js.replaceAll("\'", "\\\\'");
    js = js.replaceAll("[\n-\r]", "");
    css = css.replaceAll("\'", "\\\\'");
    css = css.replaceAll("[\n-\r]", "");
    
    String ff = flowType.getFlowType();
    String doc = flowType.getFlowDoc();
    StringBuffer sb = new StringBuffer();
    sb.append("{formMsg:'" + formMsg + "'");
    sb.append(",js:'" + js + "'");
    sb.append(",css:'" + css + "'");
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    sb.append(",runName:'" + runName + "'");
    sb.append(",flowType:" + flowType.getFlowType());
    sb.append(",flowDoc:" + flowType.getFlowDoc());
    sb.append("}");
    return sb.toString();
  }

  public boolean hasEditRight(int flowId, YHPerson user , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    if (user.isAdmin()) {
      return true;
    }
    String query = "select EDIT_PRIV from oa_fl_type where SEQ_ID=" + flowId;
    String editPriv = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        editPriv = rs.getString("EDIT_PRIV");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    YHPrcsRoleUtility pr = new YHPrcsRoleUtility();
    boolean flag = pr.checkPriv(user, editPriv);
    return flag;
  }
  /**
   * 保存表单
   * @param user
   * @param flowId
   * @param runId
   * @param prcsId
   * @param flowPrcs
   * @param map
   * @return
   * @throws Exception 
   */
  public String saveFormData(YHPerson user , int flowId 
      , int runId  , HttpServletRequest request, Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowType flowType = (YHFlowType) orm.loadObjSingle(conn, YHFlowType.class, flowId);
    Map queryItem = new HashMap();
    
    YHFormVersionLogic lo = new YHFormVersionLogic();
    int versionNo = lo.getVersionNo(conn, runId);
    int formId = flowType.getFormSeqId();
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    
    queryItem.put("FORM_ID", formSeqId);
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class, queryItem);
    String dataField = "";
    int count = 0;
    Map dataMap = new HashMap();
    for(YHFlowFormItem tmp : list){
      int itemId = tmp.getItemId();
      
      String itemData = request.getParameter("DATA_" + itemId);
      String clazz = tmp.getClazz();
      /*
      if ("MODULE".equals(clazz)) {
        String module = tmp.getValue();
        if (flowRunData != null) {
          itemData = flowRunData.getItemData();
        }
        itemData = YHFlowRunUtility.updateModule(module, request , conn, itemData);
      }*/
      if (itemData == null) {
        continue;
      }
      if (!YHWorkFlowUtility.isSave2DataTable()){
        Map queryMap = new HashMap();
        queryMap.put("RUN_ID", runId);
        queryMap.put("ITEM_ID", itemId);
        YHFlowRunData flowRunData = (YHFlowRunData) orm.loadObjSingle(conn, YHFlowRunData.class, queryMap);
        if(flowRunData != null){
          flowRunData.setItemData((itemData == null ? "" : itemData));
          orm.updateSingle(conn, flowRunData);
        }else{
          flowRunData =  new YHFlowRunData();
          flowRunData.setItemId(itemId);
          flowRunData.setRunId(runId);
          flowRunData.setItemData((itemData == null ? "" : itemData));
          orm.saveSingle(conn, flowRunData);
        }
      }   else {
        String t = "DATA_" + itemId;
        dataField += t + "=?,";
        count++;
        dataMap.put(count, itemData);
      }
    }
    if (YHWorkFlowUtility.isSave2DataTable()){
      dataField = YHWorkFlowUtility.getOutOfTail(dataField);
      if (!YHUtility.isNullorEmpty(dataField)) {
        String tableName = YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE+ flowId  + "_" + formSeqId;
        String update = "update " +tableName + " set "
           + dataField 
           + " where RUN_ID=" + runId;
        PreparedStatement stm4 = null;
        try {
          stm4 = conn.prepareStatement(update);
          Set<Integer> keys = dataMap.keySet();
          for (int b : keys) {
            String itemData = (String)dataMap.get(b);
            stm4.setString(b, itemData);
          }
          stm4.executeUpdate();
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, null, null); 
        }
      }
    }
    return null;
  }
  
}
