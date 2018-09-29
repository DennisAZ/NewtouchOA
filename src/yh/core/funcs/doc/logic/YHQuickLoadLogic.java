package yh.core.funcs.doc.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;

public class YHQuickLoadLogic {
  public String getQuickLoad(Connection conn , int flowId , int runId , int itemId , YHPerson user, String selectedItem) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "SELECT * from "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN,"+ YHWorkFlowConst.FLOW_RUN_DATA +" FLOW_RUN_DATA where "
      + " FLOW_RUN.RUN_ID = FLOW_RUN_DATA.RUN_ID  "
      + " and FLOW_ID='"+flowId+"'  "
      + " and DEL_FLAG=0  "
      + " and FLOW_RUN.RUN_ID<>'"+runId+"'  "
      + " and ITEM_ID='"+ itemId +"'  "
      + " and ITEM_DATA is not null  "
      + " order by FLOW_RUN.RUN_ID DESC";
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    int count = 0 ;
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
            "SELECT * from "+ YHWorkFlowConst.FLOW_RUN_DATA +" where RUN_ID='"+runId1+"' order by ITEM_ID";
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
              itemDataAll += rs2.getInt("ITEM_ID") + ":"+ itemDataTmp + "[@#@]";
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
    if (count >  0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString(); 
  }
}
