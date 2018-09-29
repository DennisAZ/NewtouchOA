package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFormVersionLogic {
  public void updateFormVersion(Connection conn , int seqId , int formId , int versionNo ) throws Exception {
    String sql = "UPDATE oa_fl_form_type SET VERSION_TIME = ? ,VERSION_NO = "+versionNo +", FORM_ID = "+formId+" WHERE SEQ_ID =  " + seqId;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Timestamp time =  new  Timestamp(new Date().getTime());
    try {
      stmt = conn.prepareStatement(sql);
      stmt.setTimestamp(1, time);
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  public boolean hasFormVersion(Connection conn , int formId ) throws Exception {
    String sql = "select  1  from oa_fl_form_type WHERE form_id =  " + formId;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean result = false;
    try {
      stmt = conn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        result = true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return result;
  }
  public int getVersionNo(Connection conn  , int runId) throws Exception {
    int formId = 1 ;
    String sql2 = "SELECT FORM_VERSION FROM oa_fl_run WHERE RUN_ID=" + runId;
    Statement stmt2= null;
    ResultSet rs2 = null;
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        formId = rs2.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt2, rs2, null);
    }
    return formId;
  }
  public String getVersionNoByForm(Connection conn  , int formId) throws Exception {
    String sql2 = "SELECT VERSION_NO FROM oa_fl_form_type WHERE SEQ_ID=" + formId + " OR FORM_ID=" + formId + " ORDER BY VERSION_NO DESC";
    Statement stmt2= null;
    ResultSet rs2 = null;
    String ss = "";
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      while (rs2.next()) {
        int versionNo = rs2.getInt(1);
        ss += versionNo + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt2, rs2, null);
    }
    ss = YHWorkFlowUtility.getOutOfTail(ss);
    return ss;
  }
  public int getMaxFormId(Connection conn ) throws Exception {
    int formId = 0 ;
    String sql2 = "SELECT max(SEQ_ID) FROM oa_fl_form_type";
    Statement stmt2= null;
    ResultSet rs2 = null;
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        formId = rs2.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt2, rs2, null);
    }
    return formId;
  }
  public int getMaxVersion(Connection conn , int seqId) throws Exception {
    int formId = 1 ;
    String sql2 = "SELECT VERSION_NO FROM oa_fl_form_type WHERE SEQ_ID =" + seqId;
    Statement stmt2= null;
    ResultSet rs2 = null;
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        formId = rs2.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt2, rs2, null);
    }
    return formId;
  }
  public void updateFormId(Connection conn  , int seqId , int formId) throws Exception {
    String sql2 = "update  oa_fl_form_type set FORM_ID = " + formId + " where SEQ_ID=" + seqId;
    YHWorkFlowUtility.updateTableBySql(sql2, conn);
  }
  public void changeTableName(Connection conn  , int oldId , int newId) throws Exception {
    if (YHWorkFlowUtility.isSave2DataTable()) {
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      String flowTypes = logic.getFlowTypeByFormId(conn, oldId + "");
      if (!YHUtility.isNullorEmpty(flowTypes)) {
        String[] flowType = flowTypes.split(",");
        for (String ss : flowType) {
          if (!YHUtility.isNullorEmpty(ss)) {
            String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + ss + "_" + oldId;
            String newTableName = "" + YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + ss + "_" + newId;
            this.changeTableName(conn, tableName, newTableName);
          }
        }
      }
    }
  }
  public void changeTableName(Connection conn  , String tableName , String newTableName) throws Exception {
    String sql = "alter table "+ tableName  +" rename to " + newTableName;
    String type = YHSysProps.getProp("db.jdbc.dbms");
    if ("sqlserver".equals(type)) {
      sql = "EXEC sp_rename '"+ tableName  +"', '"+ newTableName  +"'";
    }
    YHWorkFlowUtility.updateTableBySql(sql, conn);
    if ("oracle".equals(type)) {
      //取得最新的index
      //删除seq
      //删除触发器
      //建立新的
      //设置seq
      sql = "alter SEQUENCE SEQ_" + tableName  +" rename to SEQ_" + newTableName;
      YHWorkFlowUtility.updateTableBySql(sql, conn);
    }
    
  }
  /**
   * 是否存在流转的流程
   * @param formId
   * @param conn
   * @return
   * @throws Exception
   */
  public boolean isExistRunFlowRun444(int formId , Connection conn) throws Exception{
    String query  = "SELECT 1 from oa_fl_run where FORM_VERSIOIN="+ formId + " AND END_TIME is null ";
    Statement pstmt = null;
    ResultSet rs = null;
    try{
      pstmt = conn.createStatement();
      rs = pstmt.executeQuery(query);
      if (rs.next()) {
        return true;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(pstmt, rs, null);
    }
    return false;
  }
  public void updateFormItem(Connection dbConn, int id, int newId) throws Exception {
    // TODO Auto-generated method stub
    String update = "update oa_fl_form_item SET FORM_ID = " + newId + " where FORM_ID=" + id;
    YHWorkFlowUtility.updateTableBySql(update, dbConn);
  }
  public static String getTableName(int flowId ,int formSeqId) {
    return  YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + flowId + "_" + formSeqId;
  }
  public String getTableName(Connection conn , YHFlowRun flowRun) throws Exception {
    int versionNo = flowRun.getFormVersion();
    YHFormVersionLogic lo = new YHFormVersionLogic();
    YHFlowRunUtility logic = new YHFlowRunUtility();
    int formId = logic.getFormId(conn, flowRun.getFlowId());
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    return YHFormVersionLogic.getTableName(flowRun.getFlowId(), formSeqId);
  }
  public String getTableName(Connection conn , int runId , int flowId) throws Exception {
    YHFormVersionLogic lo = new YHFormVersionLogic();
    int versionNo = lo.getVersionNo(conn, runId);
    YHFlowRunUtility logic = new YHFlowRunUtility();
    int formId = logic.getFormId(conn, flowId);
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    return YHFormVersionLogic.getTableName(flowId, formSeqId);
  }
  
  public int getFormSeqId(Connection conn , int versionNo , int formId) throws Exception {
    int formId1 = 0 ;
    String sql2 = "SELECT SEQ_ID FROM oa_fl_form_type WHERE (FORM_ID =" + formId + " or  SEQ_ID = " + formId +") AND VERSION_NO = " + versionNo;
    Statement stmt2= null;
    ResultSet rs2 = null;
    try {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery(sql2);
      if (rs2.next()) {
        formId1 = rs2.getInt(1);
      } else {
        formId1 = formId;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt2, rs2, null);
    }
    return formId1;
  }
}
