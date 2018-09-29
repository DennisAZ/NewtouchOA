package yh.core.funcs.doc.logic;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;

public class YHFlowRunAssistLogic {
  public void addPrcsUser(Connection conn , int prcsId , int runId  , int flowId , int flowPrcs , int toId , String sortId , String skin, int userId , String contextPath) throws Exception {
    String query = "select TOP_FLAG,PARENT FROM "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE RUN_ID='"+ runId +"' AND PRCS_ID='"+ prcsId +"'";
    if (flowPrcs > 0) {
      query += " and FLOW_PRCS='" + flowPrcs + "'";
    }
    Statement stm2 = null;
    ResultSet rs2 = null;
    String topFlag = "0";
    Clob parent = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      if (rs2.next()) {
        topFlag = rs2.getString("TOP_FLAG");
        parent = rs2.getClob("PARENT");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    String insert = "insert into "+ YHWorkFlowConst.FLOW_RUN_PRCS +" (RUN_ID,PRCS_ID,FLOW_PRCS,USER_ID,PRCS_FLAG,OP_FLAG,TOP_FLAG,PARENT) values ('"+runId+"','"+ prcsId +"','"+ flowPrcs +"','"+toId+"',1,0,'"+ topFlag +"',?)";
    
    PreparedStatement stm3 = null;
    int result = 0 ;
    try {
      stm3 = conn.prepareStatement(insert);
      stm3.setClob(1, parent);
      result = stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
    if (result != 0) {
      //工作流日志
      YHWorkFlowUtility fu = new YHWorkFlowUtility();
      String userName  = fu.getUserNameById(toId, conn);
      String content = "增加经办人：" + userName ;
      YHFlowRunLogLogic log = new YHFlowRunLogLogic();
      log.runLog(runId, prcsId, flowPrcs, userId , 7, content, "", conn);
      
      //内部短信提醒
      YHFlowRunUtility ru = new YHFlowRunUtility();
      String runName = ru.getRunNameById(runId, conn);
      content = "您有新的待办工作待您处理:" + runName;
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("7");
      sb.setContent(content);
      sb.setFromId(userId);
      sb.setToId(String.valueOf(toId));
      sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/inputform/index.jsp?skin="+ skin +"&sortId="+sortId +"&runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs);
      YHSmsUtil.smsBack(conn, sb);
    }
  }

  public String getOutline(int flowId, int runId, int userId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query = "select p.run_id,p.flow_prcs,r.run_name from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" p , "+ YHWorkFlowConst.FLOW_RUN +" r  where r.run_id = p.run_id and r.flow_id = " + flowId 
      + " and p.prcs_id=1 "
      + " and p.user_id=" + userId
      + " and r.END_TIME is null and r.DEL_FLAG='0'"
      + " and p.PRCS_FLAG='2'";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int runId2 = rs.getInt("run_id");
        if (runId2 != runId) {
          sb.append("{name:\"");
          String runName = rs.getString("run_name");
          sb.append("<div title='"+ runName +"' style='padding-top:5px;margin-left:5px;height:16px'>");
          if (runName.length() > 18) {
            runName = runName.substring(0 , 18) + "...";
          }
          sb.append(runName);
          sb.append("<div>\"");
          sb.append(",runId:" + runId2);
          sb.append(",action:useOutline");
          sb.append("},");
          count++;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    } else {
      sb.append("{name:\"");
      sb.append("<div style='padding-top:5px;margin-left:5px;height:16px'>");
      sb.append("无草稿");
      sb.append("<div>\"");
      sb.append("}");
    }
    sb.append("]");
    return sb.toString();
  }
  
}
