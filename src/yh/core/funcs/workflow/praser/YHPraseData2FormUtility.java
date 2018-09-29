package yh.core.funcs.workflow.praser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFormVersionLogic;
import yh.core.funcs.workflow.util.YHWorkflowSave2DataTableLogic;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.util.db.YHDBUtility;

public class YHPraseData2FormUtility {

  public List<YHFlowRunData> tableData2FlowRunData(Connection conn, int flowId, int runId,
      List<YHFlowFormItem> itemList) throws Exception {
    // TODO Auto-generated method stub
    List<YHFlowRunData>  frdList = new ArrayList<YHFlowRunData>();
    YHFormVersionLogic lo = new YHFormVersionLogic();
    YHFlowRunUtility logic = new YHFlowRunUtility();
    int versionNo = lo.getVersionNo(conn, runId);
    int formId = logic.getFormId(conn, flowId);
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    String tableName = YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE+ flowId  + "_" + formSeqId;
    
    
    
    String query = "select * from " + tableName  + " where RUN_ID =" + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs =  stm.executeQuery(query);
      if (rs.next()) {
        for (YHFlowFormItem item : itemList) {
          String clazz = item.getClazz();
          if ("DATE".equals(clazz) || "USER".equals(clazz)) {
            continue;
          }
          YHFlowRunData rd = new YHFlowRunData();
          rd.setRunId(runId);
          rd.setItemData(rs.getString("DATA_" + item.getItemId()));
          rd.setItemId(item.getItemId());
          frdList.add(rd);
        }
      } 
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return frdList;
  }
  public static String getRealValue(List<YHFlowRunData> frdList, YHFlowFormItem item){
    String realValue = "";
    for (YHFlowRunData flowRunData : frdList) {
      if (item.getItemId() == flowRunData.getItemId()) {
        //$ITEM_VALUE = str_replace(array('"','<','>'),array("&quot;","&lt;","&gt;"),$ITEM_VALUE);
        //可能会进行以上语句的处理
        if(flowRunData.getItemData() != null){
          realValue = flowRunData.getItemData();
        }
      }
    }
    return realValue;
  }
  /**
   * sql宏控件-替换函数-工作流运行的时候
   * @param conn
   * @param user
   * @param dataStr
   * @param runId
   * @return
   * @throws Exception
   */
  public static String replaceSql(Connection conn , YHPerson user , String dataStr , int runId ,List<YHFlowFormItem>  fiList , List<YHFlowRunData> frdList) throws Exception {
    dataStr = replaceSql(conn , user, dataStr);
    dataStr  = dataStr.replaceAll("\\[SYS_RUN_ID\\]", String.valueOf(runId));
    for (YHFlowFormItem fi : fiList) {
      String name = fi.getTitle();
      String value = getRealValue(frdList , fi);
      dataStr = dataStr.replace("["+ name +"]", value);
    }
    return dataStr; 
  }
  /**
   * sql宏控件-替换函数-表单预览
   * @param conn
   * @param user
   * @param dataStr
   * @return
   * @throws Exception
   */
  public static String replaceSql(Connection conn , YHPerson user , String dataStr   ) throws Exception {
    String  query ="select PRIV_NO from USER_PRIV where SEQ_ID=" + user.getUserPriv();
    Statement stm = null;
    ResultSet rs = null ;
    int loginPrivNo = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        loginPrivNo = rs.getInt("PRIV_NO");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    dataStr  = dataStr.replaceAll("`", "'");
    dataStr  = dataStr.replace("》", ">");
    
    dataStr  = dataStr.replaceAll("&#13;&#10;", " ");
    dataStr  = dataStr.replaceAll("\\[SYS_USER_ID\\]", String.valueOf(user.getSeqId()));
    dataStr  = dataStr.replaceAll("\\[SYS_DEPT_ID\\]", String.valueOf(user.getDeptId()));
    dataStr  = dataStr.replaceAll("\\[SYS_PRIV_ID\\]", String.valueOf(user.getUserPriv()));
    dataStr = dataStr.replaceAll("\\[SYS_PRIV_NO\\]",String.valueOf(loginPrivNo));
    return dataStr; 
  }
}
