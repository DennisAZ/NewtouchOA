package yh.core.funcs.workflow.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHQuickLoadLogic {
  public String getQuickLoad(Connection conn , int flowId , int runId , int itemId , YHPerson user, String selectedItem) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    
    if (!YHWorkFlowUtility.isSave2DataTable()) {
      String query = "SELECT * from oa_fl_run as FLOW_RUN,oa_fl_run_data as FLOW_RUN_DATA where "
        + " FLOW_RUN.RUN_ID = FLOW_RUN_DATA.RUN_ID  "
        + " and FLOW_ID='"+flowId+"'  "
        + " and DEL_FLAG=0  "
        + " and FLOW_RUN.RUN_ID<>'"+runId+"'  "
        + " and ITEM_ID='"+ itemId +"'  "
        + " and ITEM_DATA is not null  "
        + " order by FLOW_RUN.RUN_ID DESC";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        while(rs.next()) {
          int runId1 = rs.getInt("RUN_ID");
          String runName = rs.getString("RUN_NAME");
          int flowId1 = rs.getInt("FLOW_ID");
          Clob cItemData = rs.getClob("ITEM_DATA");
          String itemData = YHWorkFlowUtility.clob2String(cItemData);
          if (count > 100) {
            break;
          }
          //验证是否有权限,并取出权限字符串
          String roleStr = roleUtility.runRole(runId1, flowId1, 0, user , conn);
          if ("".equals(roleStr)) {
            continue; 
          }
          if (!"".equals(itemData) 
              && itemData.contains(selectedItem) ) {
            count++;
            String itemDataAll = "";
            String query2 =
              "SELECT * from oa_fl_run_data where RUN_ID='"+runId1+"' order by ITEM_ID";
            Statement stm2 = null;
            ResultSet rs2 = null;
            try {
              stm2 = conn.createStatement();
              rs2 = stm2.executeQuery(query2);
              while(rs2.next()) {
                Clob cItemDataTmp = rs2.getClob("ITEM_DATA") ;
                String itemDataTmp = YHWorkFlowUtility.clob2String(cItemDataTmp);
                if ("{宏控件}".equals(itemDataTmp)) {
                  itemDataTmp = "";
                }
                itemDataAll += rs2.getInt("ITEM_ID") + ":"+ YHUtility.encodeSpecial(itemDataTmp) + "[@#@]";
              }
            } catch (Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm2, rs2, null);
            }
            sb.append("{")
            .append("runId:'").append(runId1+ "',")
            .append("runName:'").append(runName).append("',")
            .append("itemData:'").append(itemData).append("',")
            .append("itemDataAll:'").append(itemDataAll).append("',")
            .append("flowId:'").append(flowId1).append("'")
            .append("},");
          }
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
    } else {
      YHFormVersionLogic lo = new YHFormVersionLogic();
      YHFlowRunUtility logic = new YHFlowRunUtility();
      
      int versionNo = lo.getVersionNo(conn, runId);
      int formId = logic.getFormId(conn, flowId);
      int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
      
      String field = "DATA_"+itemId;
      String tableName = YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + flowId + "_" + formSeqId;
      String query = "select * from oa_fl_run as flow_run," + tableName + " "
        + " where FLOW_RUN.RUN_ID = " + tableName + ".RUN_ID  "
        + " and FLOW_ID='"+flowId+"'  "
        + " and DEL_FLAG=0  "
        + " and FLOW_RUN.RUN_ID<>'"+runId+"'  "
        + " and " + tableName + "."+ field +" is not null "
        + " order by FLOW_RUN.RUN_ID DESC";
     
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        while(rs.next()) {
          int runId1 = rs.getInt("RUN_ID");
          String runName = rs.getString("RUN_NAME");
          int flowId1 = rs.getInt("FLOW_ID");
          String itemData = rs.getString(field);
          if (count > 100) {
            break;
          }
          //验证是否有权限,并取出权限字符串
          String roleStr = roleUtility.runRole(runId1, flowId1, 0, user , conn);
          if ("".equals(roleStr)) {
            continue; 
          }
          if (!"".equals(itemData) 
              && itemData.contains(selectedItem) ) {
            count++;
            String itemDataAll = "";
            
            ResultSetMetaData rsm = rs.getMetaData();
            int count2 = rsm.getColumnCount();
            for (int i = 1 ;i <= count2 ; i++) {
              String field1 = rsm.getColumnName(i) ;
              if (field1 != null && field1.startsWith("DATA_")) {
                String val = rs.getString(field1);
                if ("{宏控件}".equals(val) || val == null) {
                  val = "";
                }
                itemDataAll += field1.replace("DATA_", "")+ ":"+ YHUtility.encodeSpecial(val)  + "[@#@]";
              }
            }
            sb.append("{")
            .append("runId:'").append(runId1+ "',")
            .append("runName:'").append(runName).append("',")
            .append("itemData:'").append(itemData).append("',")
            .append("itemDataAll:'").append(itemDataAll).append("',")
            .append("flowId:'").append(flowId1).append("'")
            .append("},");
          }
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      
    }
    if (count >  0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    
    return sb.toString(); 
  }
  
}
