package yh.core.funcs.doc.flowrunRec.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.doc.data.YHDocFlowRunLog;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowManageLogic {
  public String getFocusUser(int runId , Connection conn) throws Exception {
    String query = "select FOCUS_USER from "+ YHWorkFlowConst.FLOW_RUN +" where RUN_ID=" + runId;
    String focusUser = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        focusUser = rs.getString("FOCUS_USER");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return focusUser ;
  }
  public void focus(YHPerson u , String focusUser , int runId , String contextPath , Connection conn) throws Exception {
    if (focusUser == null) {
      focusUser = "";
    }
    focusUser += u.getSeqId() + ",";
    String query = "SELECT USER_ID from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where RUN_ID=" + runId;
    String userIdStr = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()){
        int userId = rs.getInt("USER_ID");
        if (userId != u.getSeqId()) {
          userIdStr += userId + ",";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if (!"".equals(userIdStr)) {
       query =  "SELECT RUN_NAME,FLOW_ID from "+ YHWorkFlowConst.FLOW_RUN +" where RUN_ID=" + runId;
       String runName = "";
       int flowId = 0 ;
       Statement stm2 = null;
       ResultSet rs2 = null;
       try {
         stm2= conn.createStatement();
         rs2 = stm2.executeQuery(query);
         if  (rs2.next()){
           flowId = rs2.getInt("FLOW_ID");
           runName = rs2.getString("RUN_NAME");
         }
       } catch(Exception ex) {
         throw ex;
       } finally {
         YHDBUtility.close(stm2, rs2, null); 
       }
       String content = "您所经办的工作[" + runName + "]已经被" + u.getUserName() + " 关注";
       YHSmsBack sb = new YHSmsBack();
       sb.setSmsType("7");
       sb.setContent(content);
       sb.setFromId(u.getSeqId());
       sb.setToId(userIdStr);
       sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/print/index.jsp?runId="+runId+"&flowId="+ flowId + "&openFlag=1&width=800&height=600");
       YHSmsUtil.smsBack(conn, sb);
    }
    this.updateFocus(focusUser, runId, conn);
  }
  public void updateFocus(String focusUser , int runId , Connection conn) throws Exception {
    String query = "update "+ YHWorkFlowConst.FLOW_RUN +" set FOCUS_USER='"+focusUser+"' where RUN_ID=" + runId;
    PreparedStatement stm2 = null;
    try {
      stm2 = conn.prepareStatement(query);
      stm2.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, null, null); 
    }
  }
}

