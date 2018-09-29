package yh.core.funcs.doc.logic;

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
  /**
   * 设置权限
   * @param flowId
   * @param action
   * @param privStr
   * @param conn
   * @throws Exception
   */
  public void setPriv (int flowId , String action , String privStr , Connection conn) throws Exception {
    String updateSql = "update "+ YHWorkFlowConst.FLOW_TYPE +" SET " ;
    if ("MANAGE".equals(action)) {
      updateSql += " MANAGE_USER='" + privStr + "' "  ;
    } else if ("MANAGE_DEPT".equals(action)) {
      updateSql += " MANAGE_USER_DEPT='" + privStr + "' "  ;
    } else if ("QUERY".equals(action)) {
      updateSql += " QUERY_USER='" + privStr + "' "  ;
    } else if ("QUERY_DEPT".equals(action)) {
      updateSql += " QUERY_USER_DEPT='" + privStr + "' "  ;
    } else if ("EDIT".equals(action)) {
      updateSql += " EDIT_PRIV='" + privStr + "' "  ;
    }
    updateSql += " where SEQ_ID=" + flowId;
    this.updatePriv(updateSql, conn);
  }
  /**
   * 设置评论权限
   * @param flowId
   * @param priv
   * @param conn
   * @throws Exception
   */
  public void setCommentPriv (int flowId , int priv , Connection conn) throws Exception {
    String sql = "update "+ YHWorkFlowConst.FLOW_TYPE +" SET  COMMENT_PRIV ='" + priv + "' where SEQ_ID=" + flowId;
    this.updatePriv(sql, conn);
  }
  /**
   * 更新数据库
   * @param sql
   * @param conn
   * @throws Exception
   */
  public void updatePriv (String sql , Connection conn) throws Exception {
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null,  null);
    }
  }
  /**
   * 取得权限信息
   * @param flowId
   * @param conn
   * @return 
   * @throws Exception 
   */
  public String getPriv(int flowId , Connection conn) throws Exception {
    YHORM orm = new YHORM();
    YHDocFlowType flowType = (YHDocFlowType) orm.loadObjSingle(conn, YHDocFlowType.class, flowId);
    StringBuffer sb = new StringBuffer();
    String manage = this.getPrivJson(flowType.getManageUser(), conn);
    sb.append("{MANAGE:" + manage);
    String manageDept = this.getPrivJson(flowType.getManageUserDept(), conn);
    sb.append(",MANAGE_DEPT:" + manageDept);
    String query = this.getPrivJson(flowType.getQueryUser(), conn);
    sb.append(",QUERY:" + query);
    String queryDept = this.getPrivJson(flowType.getQueryUserDept(), conn);
    sb.append(",QUERY_DEPT:" + queryDept);
    String edit = this.getPrivJson(flowType.getEditPriv(), conn);
    sb.append(",EDIT:" + edit);
    String sComment = flowType.getCommentPriv();
    int comment = 0 ;
    int commentPriv1 = 0 ;
    int commentPriv2 = 0 ;
    if (sComment != null) {
      comment  =  Integer.parseInt(sComment);
    }
    if (comment ==  1) {
      commentPriv1 = 1;
    } else if (comment == 2) {
      commentPriv2 = 1;
    } else if (comment == 3) {
      commentPriv1 = 1;
      commentPriv2 = 1;
    }
    sb.append(",COMMENT:{");
    sb.append("commentPriv1:'"  + commentPriv1 + "'");
    sb.append(",commentPriv2:'"  + commentPriv2 + "'");
    sb.append("}}");
    return sb.toString(); 
  }
  public String getPrivJson(String privStr ,Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String privUser = "";
    String privDept = "";
    String role = "";
    String userName =  "";
    String deptName = "";
    String roleName = "";
    if (privStr != null) {
      String[] privArray = privStr.split("\\|");
      if (privArray.length == 1) { 
        privUser = privArray[0];
      } else if (privArray.length == 2) {
        privUser =  privArray[0];
        privDept =  privArray[1];
      } else if (privArray.length == 3) {
        privUser =  privArray[0];
        privDept =  privArray[1];
        role = privArray[2];
      }
      YHPersonLogic userLogic = new YHPersonLogic();
      userName = userLogic.getNameBySeqIdStr(privUser, conn);
      YHDeptLogic  deptLogic = new YHDeptLogic();
      deptName = deptLogic.getNameByIdStr(privDept, conn);
      YHUserPrivLogic  roleLogic = new YHUserPrivLogic();
      roleName = roleLogic.getNameByIdStr(role, conn);
    }
    sb.append("{");
    sb.append("privUser:'" + privUser + "'" );
    sb.append(",privUserName:'" + userName + "'");
    sb.append(",privDept:'" + privDept + "'" );
    sb.append(",privDeptName:'" + deptName + "'");
    sb.append(",role:'" + role + "'" );
    sb.append(",roleDesc:'" + roleName + "'");
    sb.append("}");
    return sb.toString();
  }
  public String getTypePriv (Connection conn) throws Exception {
    String query = "select TYPE_PRIV from oa_msg2_priv";
    String typePriv = "";
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      if(rs2.next()){
        typePriv = rs2.getString("TYPE_PRIV");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    return typePriv;
  }
  public void remindUser (Connection conn , String toId , String content , String contextPath , int userId , String sortId, String skin, String flag ) throws Exception { 
    YHSmsBack sb = new YHSmsBack();
    sb.setSmsType("7");
    sb.setContent(content);
    sb.setFromId(userId);
    sb.setToId(toId);
    if ("1".equals(flag)) {
      sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/index.jsp?skin="+ skin +"&sortId=" + sortId);
    } else {
      sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/index.jsp?skin="+ skin +"&sortId=" + sortId);
    }
    YHSmsUtil.smsBack(conn, sb);
  }
  public void endWorkFlow(int runId , YHPerson user  , Connection conn ) throws Exception {
    //自由流程删除后续步骤
    String nowPrcs = "select prcs_id , flow_prcs from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where run_ID = " + runId + " and prcs_id =  (select max(prcs_id)  from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where run_ID = " + runId + ")";
    int prcsId = 0 ;
    int flowPrcs = 0 ;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(nowPrcs);
      if(rs2.next()){
        prcsId = rs2.getInt("prcs_id");
        flowPrcs = rs2.getInt("flow_prcs");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    
    String del = "delete from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE RUN_ID="+runId+" and PRCS_FLAG=5";
    this.executeUpdate(del, conn, null);
    String update = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set "  
      + " DELIVER_TIME=? "  
      + " WHERE  "  
      + "  RUN_ID =" + runId  
      + " AND PRCS_FLAG<>'4'";
    Timestamp time =  new  Timestamp(new Date().getTime());
    this.executeUpdate(update, conn, time);
    update = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set DELIVER_TIME=? WHERE RUN_ID="+ runId +" and DELIVER_TIME is null";
    this.executeUpdate(update, conn , time);
    update = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set PRCS_TIME=? WHERE RUN_ID="+runId+" and PRCS_TIME is null";
    this.executeUpdate(update, conn, time);
    //--- 结束本流程 ---
    String update2 = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set PRCS_FLAG='4' WHERE RUN_ID=" + runId;
    PreparedStatement stm3 = null;
    try {
      stm3 = conn.prepareStatement(update2);
      stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
    //处理子流程
  //--------更新当前流程（被结束的）的结束时间-------------
    update = "update "+ YHWorkFlowConst.FLOW_RUN +" set END_TIME=? WHERE RUN_ID =" + runId;
    this.executeUpdate(update, conn, time);
    //--- 流程日志 ---
    String content = user.getUserName() + "强制结束流程";
    YHFlowRunLogLogic log = new YHFlowRunLogLogic();
    log.runLog(runId, prcsId, flowPrcs, user.getSeqId(), 1, content, "", conn);
  }
  /**
   * true-刚刚创建，只有第一步；false-已经开始流转，不只有第一步
   * @param runId
   * @param conn
   * @return flag
   * @throws Exception
   */
  public boolean getFlag(int runId , Connection conn) throws Exception {
    String query = "select 1 from  "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE  RUN_ID="+ runId +" AND  PRCS_ID>1";
    boolean flag = true;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){//说明自己是最后一个
        flag = false;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return flag;
  }
  public boolean delWorkFlow(int runId , int userId , Connection conn) throws Exception {
    String update = "update "+ YHWorkFlowConst.FLOW_RUN +" set DEL_FLAG=1 where RUN_ID=" + runId; 
    PreparedStatement stm2 = null;
    int i = 0 ;
    try {
      stm2 = conn.prepareStatement(update);
      i = stm2.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, null, null); 
    }
    //工作流日志
    if (i == 1) {
      String content = "删除此工作";
      YHFlowRunLogLogic log = new YHFlowRunLogLogic();
      log.runLog(runId, 0, 0, userId, 3, content, "", conn);
      return true;
    } else {
      return false;
    }
  }
  public boolean restore(int runId , int userId, Connection conn) throws Exception {
    //把最后一步的状态改为“2-办理中”
    
    String query = "select SEQ_ID from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where " 
      + " RUN_ID=" + runId 
      + " and OP_FLAG='1' "
      + " ORDER BY PRCS_ID DESC,PRCS_TIME DESC";
    int seqId = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){//说明自己是最后一个
        seqId = rs.getInt("SEQ_ID");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    String update = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set PRCS_FLAG='2' where SEQ_ID=" + seqId;
    PreparedStatement stm2 = null;
    int i = 0 ;
    try {
      stm2 = conn.prepareStatement(update);
      i = stm2.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, null, null); 
    }
   
    //取消结束时间
    update = "update "+ YHWorkFlowConst.FLOW_RUN +" set END_TIME=NULL where RUN_ID=" + runId;
    PreparedStatement stm3 = null;
    int j = 0 ;
    try {
      stm3 = conn.prepareStatement(update);
      j = stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
    if (i == 1 && j == 1) {
      String content = "恢复执行此工作";
      YHFlowRunLogLogic log = new YHFlowRunLogLogic();
      log.runLog(runId, 0, 0, userId, 1, content, "", conn);
      return true;
    } else {
      return false;
    }
  }
  public String getCommentPriv(int flowId , YHPerson user , Connection conn) throws Exception {
    boolean hasPriv = false;
    String query = "SELECT MANAGE_USER,QUERY_USER,COMMENT_PRIV,QUERY_USER_DEPT, MANAGE_USER_DEPT from "+ YHWorkFlowConst.FLOW_TYPE +" WHERE SEQ_ID=" + flowId; 
    String manageUser = "";
    String queryUser = "";
    String commentPriv = "";
    String manageUserDept = "";
    String queryUserDept = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        manageUser = rs.getString("MANAGE_USER");
        if (manageUser == null) {
          manageUser = "";
        }
        queryUser = rs.getString("QUERY_USER");
        if (queryUser == null) {
          queryUser = "";
        }
        manageUserDept = rs.getString("MANAGE_USER_DEPT");
        if (manageUserDept == null) {
          manageUserDept = "";
        }
        queryUserDept = rs.getString("QUERY_USER_DEPT");
        if (queryUserDept == null) {
          queryUserDept = "";
        }
        commentPriv = rs.getString("COMMENT_PRIV");
        if (commentPriv == null) {
          commentPriv = "";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
    
    boolean mUserPriv = pu.checkPriv(user, manageUser) ;
    boolean qUserPriv = pu.checkPriv(user, queryUser) ;
    boolean qUserDeptPriv = pu.checkPriv(user, queryUserDept) ;
    boolean mUserDeptPriv = pu.checkPriv(user, manageUserDept) ;
    
    if (("1".equals(commentPriv) && (mUserPriv || qUserPriv || qUserDeptPriv|| mUserDeptPriv ))
        || ("2".equals(commentPriv) && (qUserPriv || qUserDeptPriv))
        || ("3".equals(commentPriv) && (mUserPriv || mUserDeptPriv))) {
      hasPriv = true;
    } 
    String result = "hasPriv:" + hasPriv;
    return result ;
  }
  public int  getMaxPrcsId(int runId  , Connection conn) throws Exception {
    String query = "SELECT PRCS_ID from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE RUN_ID=" +runId + " AND PRCS_FLAG<>5 ORDER BY PRCS_ID DESC"; 
    int prcsId = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        prcsId = rs.getInt("PRCS_ID");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return prcsId ;
  }
  public String  getSmsRemind(int userId , Connection conn) throws Exception {
    YHConfigLogic logic = new YHConfigLogic();
    StringBuffer sb = new StringBuffer();
    String paraValue = logic.getSysPar("SMS_REMIND", conn);
    String[] remindArray = paraValue.split("\\|");
    String smsRemind = "";
    String sms2remind = "";
    if (remindArray.length == 1) {
      smsRemind = remindArray[0];
    } else if (remindArray.length  >= 2) {
      smsRemind = remindArray[0];
      sms2remind = remindArray[1];
    }
    sb.append("smsRemind:'"  + smsRemind + "', sms2Remind:'" + sms2remind + "'");
    String query = "select TYPE_PRIV,SMS2_REMIND_PRIV from oa_msg2_priv";
    String typePriv = "";
    String sms2RemindPriv = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        typePriv = rs.getString("TYPE_PRIV");
        sms2RemindPriv = rs.getString("SMS2_REMIND_PRIV");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    //检查该模块是否允许手机提醒
    boolean sms2Priv = false ;
    if (YHWorkFlowUtility.findId(typePriv, "7") 
        && YHWorkFlowUtility.findId(sms2RemindPriv , String.valueOf(userId))) {
      sms2Priv = true;
    }
    sb.append(",sms2Priv:" + sms2Priv);
    return sb.toString();
  }
  public int getFlowId(int runId , Connection conn) throws Exception {
    int flowId = 0 ;
    String query = "select FLOW_ID from "+ YHWorkFlowConst.FLOW_RUN +" where run_id=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        flowId = rs.getInt("FLOW_ID");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return flowId; 
  }
  public void saveComment(int runId , int prcsId , int userId , String userName   , String comment , String smsRemind , String contextPath , Connection conn) throws Exception {
    Timestamp time =  new  Timestamp(new Date().getTime());
    
    String insert = "INSERT INTO "+ YHWorkFlowConst.FLOW_RUN_FEEDBACK +" (RUN_ID,PRCS_ID,USER_ID,CONTENT,ATTACHMENT_ID,ATTACHMENT_NAME,EDIT_TIME,FEED_FLAG)  VALUES "
     + "("+runId +","+prcsId+","+userId+",?,'','',?,'1')";
    PreparedStatement stm2 = null;
    try {
      stm2 = conn.prepareStatement(insert);
      stm2.setString(1, comment);
      stm2.setTimestamp(2, time);
      stm2.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, null, null); 
    }
    if ("on".equals(smsRemind)) {
      //-------------短信通知当前步骤经办人----------
      String query = "SELECT USER_ID from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where RUN_ID="+ runId +" AND USER_ID<>"+userId +" AND PRCS_ID=" + prcsId;
      String userStr = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        while (rs.next()){
          userStr += rs.getString("USER_ID") + ",";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      if (!"".equals(userStr)) {
        
        String content = userName + "已经对您所经办的工作[流水号：" + runId + "]作出点评";
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("7");
        sb.setContent(content);
        sb.setFromId(userId);
        sb.setToId(userStr);
        int flowId = this.getFlowId(runId, conn);
        sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/print/index.jsp?runId="+runId+"&flowId="+ flowId + "&openFlag=1&width=800&height=600");
        YHSmsUtil.smsBack(conn, sb);
      }
    }
  }
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
       sb.setRemindUrl( YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/print/index.jsp?runId="+runId+"&flowId="+ flowId + "&openFlag=1&width=800&height=600");
       YHSmsUtil.smsBack(conn, sb);
    }
    this.updateFocus(focusUser, runId, conn);
  }
  public void calFocus(String focusUser , int userId , int runId , Connection conn) throws Exception {
    String[] aStr = focusUser.split(",");
    String newStr = "";
    for (String tmp : aStr) {
      if (!"".equals(tmp)) {
        int id = Integer.parseInt(tmp);
        if (id != userId) {
          newStr += id + ",";
        }
      }
    }
    this.updateFocus(newStr, runId, conn);
  }
  public void executeUpdate(String sql , Connection conn , Timestamp time ) throws Exception {
    PreparedStatement stm3 = null;
    try {
      stm3 = conn.prepareStatement(sql);
      if (time != null) {
        stm3.setTimestamp(1, time);
      }
      stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
  }
}

