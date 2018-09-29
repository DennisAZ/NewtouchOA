package yh.core.funcs.doc.logic;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.data.YHDocFlowRunPrcs;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHPraseData2FormEdit;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
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
    YHDocRun flowRun = run.getFlowRunByRunId(runId , conn);
    YHDocFlowType flowType = flowTypeLogic.getFlowTypeById(flowRun.getFlowId() , conn);
    //查出表单
    YHDocFlowFormType fft = (YHDocFlowFormType) orm.loadObjSingle(conn, YHDocFlowFormType.class, flowType.getFormSeqId());
    //查询表单字段信息
    Map formItemQuery = new HashMap();
    formItemQuery.put("FORM_ID", flowType.getFormSeqId());
    List<YHDocFlowFormItem> list = orm.loadListSingle(conn, YHDocFlowFormItem.class , formItemQuery);
    Map runDataQuery = new HashMap();
    runDataQuery.put("RUN_ID", runId);
    List<YHDocFlowRunData> frdList = orm.loadListSingle(conn, YHDocFlowRunData.class , runDataQuery);
    
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
    String query = "select EDIT_PRIV from "+ YHWorkFlowConst.FLOW_TYPE +" where SEQ_ID=" + flowId;
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
    YHDocFlowType flowType = (YHDocFlowType) orm.loadObjSingle(conn, YHDocFlowType.class, flowId);
    Map queryItem = new HashMap();
    queryItem.put("FORM_ID", flowType.getFormSeqId());
    List<YHDocFlowFormItem> list = orm.loadListSingle(conn, YHDocFlowFormItem.class, queryItem);
    for(YHDocFlowFormItem tmp : list){
      int itemId = tmp.getItemId();
      Map queryMap = new HashMap();
      queryMap.put("RUN_ID", runId);
      queryMap.put("ITEM_ID", itemId);
      YHDocFlowRunData flowRunData = (YHDocFlowRunData) orm.loadObjSingle(conn, YHDocFlowRunData.class, queryMap);
      String itemData = request.getParameter("DATA_" + itemId);
      String clazz = tmp.getClazz();
      if ("MODULE".equals(clazz)) {
        String module = tmp.getValue();
        if (flowRunData != null) {
          itemData = flowRunData.getItemData();
        }
        itemData = YHFlowRunUtility.updateModule(module, request , conn, itemData);
      }
      if (itemData == null) {
        continue;
      }
      if(flowRunData != null){
        flowRunData.setItemData((itemData == null ? "" : itemData));
        orm.updateSingle(conn, flowRunData);
      }else{
        flowRunData =  new YHDocFlowRunData();
        flowRunData.setItemId(itemId);
        flowRunData.setRunId(runId);
        flowRunData.setItemData((itemData == null ? "" : itemData));
        orm.saveSingle(conn, flowRunData);
      }
    }
    return null;
  }
  
}
