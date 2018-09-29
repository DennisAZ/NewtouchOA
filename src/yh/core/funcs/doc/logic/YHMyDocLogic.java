package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


public class YHMyDocLogic {
  public String getMyWork(Connection conn , YHPerson user , int maxCount  , String sortId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "SELECT FLOW_RUN_PRCS.PRCS_ID " 
      + " ,FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,FLOW_RUN_PRCS.PRCS_FLAG " 
      + " ,FLOW_RUN_PRCS.FLOW_PRCS " 
      + " ,FLOW_TYPE.FLOW_NAME " 
      + " ,FLOW_RUN.RUN_NAME " 
      + " ,FLOW_TYPE.FLOW_TYPE " 
      + " ,FLOW_TYPE.FORM_SEQ_ID  " 
      + " , PERSON.USER_NAME"
      + " from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" FLOW_RUN_PRCS,"+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN,"+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE, PERSON WHERE  " 
      + " FLOW_RUN_PRCS.RUN_ID=FLOW_RUN.RUN_ID " 
      + "  and PERSON.SEQ_ID = FLOW_RUN.BEGIN_USER  " 
      + "  and FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID  " 
      + " and FLOW_RUN_PRCS.USER_ID=" + user.getSeqId()
      + " and FLOW_RUN.DEL_FLAG='0'  " 
      + " and PRCS_FLAG<>'4'  " 
      + " and PRCS_FLAG<>'5'  " ;
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
     
     query += " order by FLOW_RUN_PRCS.PRCS_FLAG,PRCS_TIME desc ";
    int count = 0 ;
   
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      
      while (rs.next()) {
        count ++ ;
        if(count > maxCount )
           break;
        int prcsId = rs.getInt("PRCS_ID");
        int runId = rs.getInt("RUN_ID");
        int flowId = rs.getInt("FLOW_ID");
        String  prcsFlag = rs.getString("PRCS_FLAG");
        int flowPrcs = rs.getInt("FLOW_PRCS");
        String flowName = rs.getString("FLOW_NAME");
        String runName = rs.getString("RUN_NAME");
        String flowType = rs.getString("FLOW_TYPE");
        String userName = rs.getString("USER_NAME");
        int formId = rs.getInt("FORM_SEQ_ID");
        
        Map prcsNameMap = new HashMap();
        String prcsName = "";
        String sFlowPrcsI = String.valueOf(flowPrcs);
        if ("1".equals(flowType)) {
          if (!prcsNameMap.containsKey(sFlowPrcsI)) {
            String query3 = "select PRCS_NAME from "+ YHWorkFlowConst.FLOW_PROCESS +" WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs;
            Statement stm3 = null;
            ResultSet rs3 = null;
            try {
              stm3 = conn.createStatement();
              rs3 = stm3.executeQuery(query3);
              if (rs3.next()) {
                String tmp = rs3.getString("PRCS_NAME");
                prcsNameMap.put(sFlowPrcsI, tmp);
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm3, rs3, null); 
            }
          }
        } else {
          prcsNameMap.put(sFlowPrcsI, "");
        }
        YHFlowRunUtility wf = new YHFlowRunUtility();
        YHDocFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "标题");
        String title = "";
        if (rd6 != null) {
          title = rd6.getItemData();
        }
        sb.append("{");
        sb.append("runId:" + runId);
        sb.append(",prcsId:" + prcsId);
        sb.append(",flowId:" + flowId);
        sb.append(",prcsFlag:'" + prcsFlag + "'");
        sb.append(",flowPrcs:'" + flowPrcs + "'");
        sb.append(",flowName:'" + flowName + "'");
        sb.append(",runName:'" + title + "'");
        sb.append(",flowType:'" + flowType + "'");
        sb.append(",formId:" + formId);
        sb.append(",prcsName:'" + (String)prcsNameMap.get(sFlowPrcsI) + "'");
        sb.append(",beginUser:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append("},");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getSign(Connection conn , YHPerson user , int maxCount , String sortId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "SELECT FLOW_RUN_PRCS.PRCS_ID " 
      + " ,FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,PRCS_FLAG " 
      + " ,FLOW_PRCS " 
      + " ,FLOW_NAME " 
      + " ,RUN_NAME " 
      +  ",FLOW_TYPE"
      + " ,PERSON.USER_NAME"
      + "  from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" FLOW_RUN_PRCS, "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN, "+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE, PERSON WHERE " 
      + "  FLOW_RUN_PRCS.RUN_ID=FLOW_RUN.RUN_ID  " 
      + "  and PERSON.SEQ_ID = FLOW_RUN.BEGIN_USER  " 
      + " and FLOW_RUN.FLOW_ID=FLOW_TYPE.Seq_ID  " 
      + " and FLOW_RUN.DEL_FLAG=0  " 
      + " and FLOW_RUN_PRCS.USER_ID=" + user.getSeqId() ;
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
     query += "  and OP_FLAG='0' order by FLOW_RUN_PRCS.PRCS_FLAG,PRCS_TIME desc ";
    int count = 0 ;
    Map prcsNameMap = new HashMap();
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
     
      while (rs.next()) {
        count ++ ;
        if(count > maxCount )
           break;
        int prcsId = rs.getInt("PRCS_ID");
        int runId = rs.getInt("RUN_ID");
        int flowId = rs.getInt("FLOW_ID");
        String  prcsFlag = rs.getString("PRCS_FLAG");
        int flowPrcs = rs.getInt("FLOW_PRCS");
        String flowName = rs.getString("FLOW_NAME");
        String runName = rs.getString("RUN_NAME");
        String userName = rs.getString("USER_NAME");
        
        String flowType = rs.getString("FLOW_TYPE");
        String sFlowPrcsI = String.valueOf(flowPrcs);
        if ("1".equals(flowType)) {
          if (!prcsNameMap.containsKey(sFlowPrcsI)) {
            String query3 = "select PRCS_NAME from "+ YHWorkFlowConst.FLOW_PROCESS +" WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs;
            Statement stm3 = null;
            ResultSet rs3 = null;
            try {
              stm3 = conn.createStatement();
              rs3 = stm3.executeQuery(query3);
              if (rs3.next()) {
                String tmp = rs3.getString("PRCS_NAME");
                prcsNameMap.put(sFlowPrcsI, tmp);
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm3, rs3, null); 
            }
          }
        } else {
          prcsNameMap.put(sFlowPrcsI, "");
        }
        
        
        
        YHFlowRunUtility wf = new YHFlowRunUtility();
        YHDocFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "标题");
        String title = "";
        if (rd6 != null) {
          title = rd6.getItemData();
        }
        sb.append("{");
        sb.append("runId:" + runId);
        sb.append(",prcsId:" + prcsId);
        sb.append(",flowId:" + flowId);
        sb.append(",prcsFlag:'" + prcsFlag + "'");
        sb.append(",flowPrcs:'" + flowPrcs + "'");
        sb.append(",flowName:'" + flowName + "'");
        sb.append(",runName:'" + title + "'");
        sb.append(",prcsName:'" + (String)prcsNameMap.get(sFlowPrcsI) + "'");
        sb.append(",beginUser:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append("},");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getFocusWork(Connection conn, YHPerson user, int maxCount , String sortId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    
    String query = "SELECT FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,FLOW_RUN.RUN_NAME " 
      + " ,FLOW_TYPE.FLOW_NAME " 
      + " ,FLOW_RUN.FOCUS_USER "
      + " ,PERSON.USER_NAME"
      + "  FROM "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN, "+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE,PERSON  WHERE  " 
      + " FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID  "
      + "  and PERSON.SEQ_ID = FLOW_RUN.BEGIN_USER  " 
      + " AND DEL_FLAG=0  ";
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
     query +="  ORDER BY RUN_ID DESC";
    int count = 0 ;
    Map prcsNameMap = new HashMap();
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      while (rs.next()) {
        String focus = rs.getString("FOCUS_USER");
        if (YHWorkFlowUtility.findId(focus, String.valueOf(user.getSeqId()))) {
          count ++ ;
          if(count > maxCount )
             break;
          int runId = rs.getInt("RUN_ID");
          int flowId = rs.getInt("FLOW_ID");
          String flowName = rs.getString("FLOW_NAME");
          String runName = rs.getString("RUN_NAME");
          String userName = rs.getString("USER_NAME");
          YHFlowRunUtility wf = new YHFlowRunUtility();
          YHDocFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "标题");
          String title = "";
          if (rd6 != null) {
            title = rd6.getItemData();
          }
          sb.append("{");
          sb.append("runId:" + runId);
          sb.append(",flowId:" + flowId);
          sb.append(",flowName:'" + flowName + "'");
          sb.append(",runName:'" + title + "'");
          sb.append(",beginUser:\"" + YHUtility.encodeSpecial(userName) + "\"");
          sb.append("},");
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
}
