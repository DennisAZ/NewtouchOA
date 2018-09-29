package yh.core.funcs.doc.send.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.ByteArrayBuffer;

import yh.core.esb.client.data.YHDocSendMessage;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHEsbMessage;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.logic.YHEsbClientUtility;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.send.data.YHDocFlowRun;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.mobilesms.data.YHSms2;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.user.api.core.db.YHDbconnWrap;

public class YHDocSendLogic {
  /**
   * 发送公文到部门
   * @param deptId
   * @param sendRunId
   * @param conn
   * @param webroot
   * @param loginUser
   * @param remind
   * @param attachmentId
   * @param attachmentName
   * @throws Exception
   */
  public void sendDocToDept(String deptId 
      , int sendRunId
      , Connection conn 
      , String webroot
      , int loginUser
      ,  boolean remind
      , boolean remind2
  ) throws Exception {
    YHFlowRunLogic logic = new YHFlowRunLogic();
    YHDocRun run = logic.getFlowRunByRunId(sendRunId, conn);
    int beginUser = run.getBeginUser();
    YHDocUtility util = new YHDocUtility();
    Map sendDept =  util.getDeptByUser(beginUser, conn);
    YHDocFlowRun fr = util.getSendDocByRunId(sendRunId , conn); 
    YHFlowRunUtility flowRunLogic = new YHFlowRunUtility();
    YHDocFlowRunData data = flowRunLogic.getFlowRunData(conn, run.getFlowId(), sendRunId, YHDocConst.getProp(webroot, YHDocConst.DOC_TITLE));
    String title = "";
    if (data != null) {
      title = data.getItemData();
    }
    YHDocFlowRunData data2 = flowRunLogic.getFlowRunData(conn, run.getFlowId(), sendRunId, YHDocConst.getProp(webroot, YHDocConst.DOC_SEND_UNIT));
    String sendUnit = "";
    if (data2 != null) {
      sendUnit = data2.getItemData();
    } 
    if (YHUtility.isNullorEmpty(sendUnit)) {
      sendUnit = sendDept.get("DEPT_NAME").toString();
    }
    //Date date = new Date();
    String[] depts = deptId.split(",");
    for (String de : depts) {
      Timestamp time =  new  Timestamp(new Date().getTime());
      this.sendEnvelope(conn, sendDept.get("DEPT_ID").toString(), de, 0, sendRunId, time , title , fr.getDoc() , fr.getDocName(), fr.getDocId() , sendUnit);
      //this.sendDocToDept(de, sendDept, title, sendRunId, attachmentName, attachmentId , fr.getDoc(), date, conn, loginUser);
    }
    //提醒
    if (remind || remind2) {
      String users = this.getRoleByDepts(conn, depts);
      if (remind) {
        this.receiveDocRemind(conn, title, fr.getDoc(), loginUser, users);
      }
      if (remind2) {
        this.receiveDocRemind2(conn, title, fr.getDoc(), loginUser, users);
      }
      
    }
  }
  /**
   * 根据部门取得有该部门权限的人员
   * @param conn
   * @param depts 以逗号侵害的部门
   * @return
   * @throws Exception 
   */
  public String getRoleByDepts(Connection conn , String[] depts) throws Exception {
    /* 取得有全局权限的人员*/
    String role = this.getRoleByDept(conn, "-1");
    String users = this.getUserByRole(role, conn);
    for (String de : depts) {
      if (!"".equals(de)) {
        //取得有该部门权限的人员
        String user2 = this.getRoleByDept(conn, de);
        String[] tmps = user2.split(",");
        for (String tmp : tmps) {
          if (!YHWorkFlowUtility.findId(users, tmp )) {
            users += tmp + ",";
          }
        }
      }
    }
    users = YHWorkFlowUtility.getOutOfTail(users);
    return users;
  }
  /**
   * 取得有收外部公文权限的人员
   * @param conn
   * @return
   * @throws Exception 
   */
  public String getEsbDocRole(Connection conn) throws Exception {
    String deptIds = "";
    String privIds ="";
    String userIds = "";
    String query = "select USER_ID , DEPT_ID, USER_PRIV from oa_esb_rec_person";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        deptIds =YHUtility.null2Empty(rs.getString("DEPT_ID"));
        privIds =YHUtility.null2Empty(rs.getString("USER_PRIV"));
        userIds = YHUtility.null2Empty(rs.getString("USER_ID"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return this.getUsersByRole(conn, deptIds, privIds, userIds);
  }
  
  public String getUsersByRole(Connection conn , String deptId , String userPriv , String userId) throws Exception {
    String query = "select SEQ_ID from PERSON  where not_login = '0'";
    String con = "";
    if (!"0".equals(deptId) && !"ALL_DEPT".equals(deptId)) {
      if (!YHUtility.isNullorEmpty(userId)) {
        con += " SEQ_ID IN (" + YHWorkFlowUtility.getOutOfTail(userId) + ")";
      }
      if (!YHUtility.isNullorEmpty(userPriv)) {
        con += " or USER_PRIV IN (" + YHWorkFlowUtility.getOutOfTail(userPriv) + ")";
      }
      if (!YHUtility.isNullorEmpty(deptId)) {
        con += " or DEPT_ID IN (" + YHWorkFlowUtility.getOutOfTail(deptId) + ")";
      }
    }
    if (!"".equals(con)) {
      query += " and (" + con + ") ";
    }
    Statement stm = null; 
    ResultSet rs = null; 
    String userIds = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      while (rs.next()){ 
        userIds += rs.getString("SEQ_ID") + ",";
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return userIds;
  }
  /**
   * 根据部门取得有该部门权限的人员
   * @param conn
   * @param deptId 单个部门
   * @return
   * @throws Exception
   */
  public String getRoleByDept(Connection conn , String deptId) throws Exception {
    String query = "select * from oa_officialdoc_recv_priv where dept_id = '"+deptId+"'";
    Statement stm = null; 
    ResultSet rs = null; 
    String role = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        role = YHUtility.null2Empty(rs.getString("USER_ID"));
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return role;
  }
  /**
   * 收到来文提醒
   * @param conn
   * @param title 公文标题
   * @param doc 公文文号
   * @param loginUser 发送人
   * @param remindUser 提醒人
   * @throws Exception
   */
  public void receiveDocRemind(Connection conn , String title , String doc , int loginUser , String remindUser) throws Exception {
    String content = "收到来文：" + title + "，对方文号："+ doc +"，请签收！";
    String url = "/core/funcs/doc/receive/sign/sign.jsp";
    if (!YHUtility.isNullorEmpty(remindUser)) {
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType(YHDocConst.remindType);
      sb.setContent(content);
      sb.setFromId(loginUser);
      sb.setToId(remindUser);
      sb.setRemindUrl(url);
      YHSmsUtil.smsBack(conn, sb);
    }
  }
  /**
   * 收到来文提醒
   * @param conn
   * @param title 公文标题
   * @param doc 公文文号
   * @param loginUser 发送人
   * @param remindUser 提醒人
   * @throws Exception
   */
  public void receiveDocRemind2(Connection conn , String title , String doc , int loginUser , String remindUser) throws Exception {
    String content = "收到来文：" + title + "，对方文号："+ doc +"，请签收！";
    if (!YHUtility.isNullorEmpty(remindUser)) {
      YHMobileSms2Logic logic = new YHMobileSms2Logic();
      logic.remindByMobileSms(conn, remindUser, loginUser, content, new Date());
    }
  }
  public void cancel(Connection conn , String seqId , String status , int userId) throws Exception{
    String url = "";
    String update = "update oa_officialdoc_send SET IS_CANCEL = '1' WHERE SEQ_ID = '" + seqId + "'";
    YHWorkFlowUtility.updateTableBySql(update, conn);
    String toId = "";
    String content = "";
    if ("0".equals(status)) {
    } else if ("1".equals(status)) {
      String[] r = this.getRegisterMsg(conn, seqId);
      toId = r[0];
      content = "你签收的公文："+r[1]+"，已经收回，请不用办理！";
    } else if ("2".equals(status)) {
      String[] r = this.getRegisterMsg(conn, seqId);
      toId = r[0];
      content = "你登记的公文："+r[1]+"，已经收回，请停止办理！";
    }
    if (!YHUtility.isNullorEmpty(toId)) {
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType(YHDocConst.remindType);
      sb.setContent(content);
      sb.setFromId(userId);
      sb.setToId(toId);
      sb.setRemindUrl(url);
      YHSmsUtil.smsBack(conn, sb);
    }
  }
  /**
   * 是否需要发送
   * @param conn
   * @param runId
   * @param flowId
   * @param flowPrcs
   * @return
   * @throws Exception
   */
  public boolean hasSend(Connection conn , int  runId  , int  flowId  ,int flowPrcs) throws Exception {
    //不需要发送
    if (!this.isNeedSend(conn, flowId, flowPrcs)) {
      return false;
    }
    Statement stm = null;
    ResultSet rs = null;
    String query = "select"
      + " 1"
      + " from  oa_officialdoc_send  where RUN_ID  = " + runId  + " AND IS_CANCEL = '0'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      //不需要再发送
      if (rs.next()) {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return true;
  }
  /**
   * 是否需要归档
   * @param conn
   * @param runId
   * @param flowId
   * @param flowPrcs
   * @return
   * @throws Exception
   */
  public boolean hasRoll(Connection conn , int  runId  , int  flowId  ,int flowPrcs) throws Exception {
    //不需要归档
    if (!this.isNeedRoll(conn, flowId, flowPrcs)) {
      return false;
    }
    Statement stm = null;
    ResultSet rs = null;
    String query = "select"
      + " 1"
      + " from  oa_officialdoc_run  where RUN_ID  = " + runId  + " AND EXTEND = '1'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      //不需要再归档
      if (rs.next()) {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return true;
  }
  private boolean isNeedRoll(Connection conn, int flowId, int flowPrcs) throws Exception {
    // TODO Auto-generated method stub
    Statement stm = null;
    ResultSet rs = null;
    String query2 = "select"
      + " 1"
      + " from  oa_officialdoc_fl_process  where FLOW_SEQ_ID  = " + flowId + " AND PRCS_ID = '" + flowPrcs + "' and EXTEND='1'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query2);
      if (rs.next()) {
        return true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return false;
  }
  public boolean isNeedSend(Connection conn , int  flowId  ,int flowPrcs) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String query = "select"
      + " FLOW_TYPE"
      + " from  oa_officialdoc_fl_type  where SEQ_ID  = " + flowId;
    String flowType = "1";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        flowType = rs.getString(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    if ("2".equals(flowType)) {
      return false;
    }
    String query2 = "select"
      + " DOC_SEND_FLAG"
      + " from  oa_officialdoc_fl_process  where FLOW_SEQ_ID  = " + flowId + " AND PRCS_ID <= '" + flowPrcs + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query2);
      while (rs.next()) {
        if ("1".equals(rs.getString(1)))  {
          return true;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return false;
  }
  public void resend(Connection conn , String seqId , String status , int userId) throws Exception{
    String url = "";
    String update = "update oa_officialdoc_send SET IS_CANCEL = '0' WHERE SEQ_ID = '" + seqId + "'";
    YHWorkFlowUtility.updateTableBySql(update, conn);
    String toId = "";
    String content = "";
    if ("0".equals(status)) {
    } else if ("1".equals(status)) {
      String[] r = this.getRegisterMsg(conn, seqId);
      toId = r[0];
      content = "你签收的公文："+r[1]+"，已经重新发送给你，请登记！";
      url =  "/core/funcs/doc/receive/register/docReg.jsp?rec_seqId=" + seqId;
    } else if ("2".equals(status)) {
      String[] r = this.getRegisterMsg(conn, seqId);
      toId = r[0];
      content = "你登记的公文："+r[1]+"，已经重新发送给你，请继续办理！";
    }
    if (!YHUtility.isNullorEmpty(toId)) {
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType(YHDocConst.remindType);
      sb.setContent(content);
      sb.setFromId(userId);
      sb.setToId(toId);
      sb.setRemindUrl(url);
      YHSmsUtil.smsBack(conn, sb);
    }
  }
  public String getRegisterUser(Connection conn , String seqId) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String r = "";
    String query = "select"
      + " REGISTER_USER"
      + " from  oa_officialdoc_rec_register  where oa_officialdoc_rec_register.REC_ID  = " + seqId;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        return String.valueOf(rs.getInt("REGISTER_USER"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return r;
  }
  public String[] getRegisterMsg(Connection conn , String seqId) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String[] r =new String[2];
    String query = "select"
      + " REGISTER_USER"
      + ", TITLE"
      + " from  oa_officialdoc_rec_register  where oa_officialdoc_rec_register.REC_ID  = " + seqId;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        r[0]=  String.valueOf(rs.getInt("REGISTER_USER"));
        r[1]=  rs.getString("TITLE");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return r;
  }
  /**
   * 
   * @param deptId 发送到那个部门
   * @param sendDept 发送部门
   * @param title 公文标题
   * @param sendRunId 发文流水号
   * @param attachmentName 公文文件名
   * @param attachmentId 公文文件id
   * @param sendNo 公文文号
   * @param sendTime 发送时间
   * @param conn 
   * @param webroot
   * @param loginUser
   * @throws Exception
   */
  public void sendDocToDept(String deptId 
      , String sendDept
      , String title
      , int sendRunId
      , String attachmentName
      , String attachmentId
      , String sendNo
      , Date sendTime
      , Connection conn 
      , int loginUser
  ) throws Exception {
    //设置值
    String docType = "1";
    String insertSql = "insert into oa_officialdoc_recv " 
      + "( DOC_NO "
      +", RES_DATE " 
      + ", FROMUNITS " 
      + ", OPPDOC_NO " 
      + ", TITLE " 
      + ", DOC_TYPE " 
      + ", STATUS " 
      + ", ATTACHNAME " 
      + ", ATTACHID " 
      + ", SEND_RUN_ID" 
      + ",CREATE_USER_ID"
      + ",SPONSOR"
      + ", SEND_STATUS " 
      + ", REC_DOC_ID"
      + ", REC_DOC_NAME"
      + ") values ('',? , ? , ? , ? , ? ,? ,? ,? , ? , ? , ? , ?  , ? , ?)" ;
    PreparedStatement ps = null;
    try {
      if (!YHUtility.isNullorEmpty(attachmentName) && !attachmentName.endsWith("*")) {
        attachmentName  += "*";
      }
      if (!YHUtility.isNullorEmpty(attachmentId) && !attachmentId.endsWith(",")) {
        attachmentId  += ",";
      }
      ps = conn.prepareStatement(insertSql);
      ps.setTimestamp(1,new Timestamp(sendTime.getTime()));
      ps.setString(2 , sendDept);
      ps.setString(3 , sendNo);
      ps.setString(4 , title);
      ps.setString(5 , docType);
      ps.setInt(6 , 0);
      ps.setString(7, "");
      ps.setString(8, "");
      ps.setInt(9, sendRunId);
      ps.setInt(10, loginUser);
      ps.setString(11, deptId);
      ps.setInt(12, 2);//收文状态 2未登记 0-已登记未签收
      ps.setString(13, attachmentId);
      ps.setString(14, attachmentName);
      ps.execute();
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  public String getUserByRole(String roles , Connection conn) throws Exception {
    String users = "";
    String[] role = roles.split(",");
    for (String roleId : role) {
      String sql  = "select SEQ_ID  FROM PERSON WHERE (USER_PRIV='" + roleId + "' or  USER_PRIV_OTHER LIKE '%" + YHUtility.encodeLike(String.valueOf(roleId)) + "%' " + YHDBUtility.escapeLike() + ")  AND NOT_LOGIN <> '1' ";
      Statement stm = null; 
      ResultSet rs = null; 
      try { 
        stm = conn.createStatement(); 
        rs = stm.executeQuery(sql); 
        if (rs.next()){ 
          int userId = rs.getInt("SEQ_ID");
          if (!YHWorkFlowUtility.findId(users, userId + "")) {
            users += userId + ",";
          }
        } 
      } catch(Exception ex) { 
        throw ex; 
      } finally { 
        YHDBUtility.close(stm, rs, null); 
      } 
    }
    return users;
  }
  /**
   * 修改发送标志
   * @param runId 公文号
   * @param innerDept 内部部门
   * @param outerDept 外部门
   * @param conn 
   * @throws Exception
   */
  public void setSendFlag(int runId , String innerDept, String outerDept, Connection conn) throws Exception {
    String query = "update  oa_officialdoc_fl_run set SEND_FLAG ='1'" +
    		//" , INNER_DEPT = '"+innerDept+"',OUTER_DEPT='"+ outerDept +"' " +
    				" where RUN_ID=" + runId;
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(query);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null);
    }
  }
  /**
   * 从esb上收到公文 ,处理
   * @param dsm
   * @param bb
   */
    public void receiveFormEsb(YHDocSendMessage dsm, ByteArrayBuffer bb) {
      YHDbconnWrap dbUtil = new YHDbconnWrap();
      Connection conn = null;
      try {      
        conn = dbUtil.getSysDbConn();
        int loginUser = 1;
        YHEsbClientConfig config = YHEsbClientConfig.builder(YHSysProps.getRootPath() + File.separator + "webroot"+File.separator + "yh" + YHEsbConst.CONFIG_PATH) ;
        YHDeptTreeLogic logic = new YHDeptTreeLogic();
        YHExtDept dept = logic.getDeptByEsbUser(conn, config.getUserId());
        String tmp[] = YHEsbClientUtility.getNewAttachPath(dsm.getDocName(), YHWorkFlowConst.MODULE);
        String attachementId = tmp[0];
        String path = tmp[1];
        YHEsbClientUtility.writeFile(new File(path), bb.toByteArray());
        this.sendEnvelope(conn, dsm.getFromDept(), dept.getDeptId(), 1, dsm.getRunId(), new  Timestamp(dsm.getSendTime().getTime()), dsm.getTitle() , dsm.getDoc() , dsm.getDocName() , attachementId , "");
        //this.sendDocToDept(dept.getDeptId(), dsm.getFromDeptName(), dsm.getTitle(), dsm.getRunId(), dsm.getDocName(), attachementId, dsm.getDoc(), dsm.getSendTime(), conn, loginUser);
        String users = this.getEsbDocRole(conn);
        this.receiveDocRemind(conn, dsm.getTitle(), dsm.getDoc(), loginUser, users);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        dbUtil.closeAllDbConns();
      }
    }
/**
 * 发送公文通过esb
 * @param deptId2
 * @param runId
 * @param conn
 * @param webroot
 * @param seqId
 * @param remind
 * @param attachmentId
 * @param attachmentName
 * @throws Exception
 */
  public void sendDocToEsbDept(String deptId2, int runId, Connection conn,
      String webroot, int seqId, boolean remind) throws Exception {
    // TODO Auto-generated method stub
    YHDocUtility util = new YHDocUtility();
    YHDocFlowRun fr = util.getSendDocByRunId(runId , conn); 
    String attachmentId = fr.getDocId();
    String attachmentName = fr.getDocName();
    YHDeptTreeLogic logic = new YHDeptTreeLogic();
    String esbUser = logic.getEsbUsers(conn, deptId2);
    YHEsbClientConfig config = YHEsbClientConfig.builder(webroot + YHEsbConst.CONFIG_PATH) ;
    YHExtDept dept = logic.getDeptByEsbUser(conn, config.getUserId());
    String message = attachmentId +".zip";
    String filePath = config.getCachePath() + File.separator + message;
    YHFlowRunUtility flowRunLogic = new YHFlowRunUtility();
    YHFlowRunLogic logic2 = new YHFlowRunLogic();
    YHDocRun run = logic2.getFlowRunByRunId(runId, conn);
    
    YHDocFlowRunData data = flowRunLogic.getFlowRunData(conn, run.getFlowId(), runId, YHDocConst.getProp(webroot, YHDocConst.DOC_TITLE));
    String title = "";
    if (data != null) {
      title = YHUtility.null2Empty(data.getItemData());
    }
    String[] depts = deptId2.split(",");
    Timestamp time =  new  Timestamp(new Date().getTime());
    for (String d : depts) {
      if (!d.equals(dept.getDeptId())) 
        this.sendEnvelope(conn, dept.getDeptId(), d, 1, runId, time, title , fr.getDoc() , attachmentName , attachmentId , "");
    }
    YHDocSendMessage dsm = new YHDocSendMessage(runId, dept.getDeptId(), dept.getDeptName(), time , attachmentName ,fr.getDoc() , title);
    InputStream isb = new ByteArrayInputStream(dsm.toXml().getBytes("UTF-8"));
    Map map = new HashMap();
    map.put(YHEsbMessage.KEY_MESSAGE_FILE, isb);
    String file = YHEsbClientUtility.getAttachPath(attachmentId, attachmentName, YHWorkFlowConst.MODULE);
    InputStream in = new FileInputStream(new File(file));
    map.put(attachmentName, in);
    this.createFile(map, filePath);
    YHWSCaller caller = new YHWSCaller();
    caller.setWS_PATH(config.getWS_PATH());
    String[] esbs = esbUser.split(",");
    
    for (String esb : esbs) {
      caller.send(filePath, esb, "doc",config.getToken());
    }
  }
  public void createFile(Map<String , InputStream> map , String path ) throws Exception {
    FileOutputStream out = new FileOutputStream(new File(path));
    org.apache.tools.zip.ZipOutputStream zipout = new org.apache.tools.zip.ZipOutputStream(out);
    zipout.setEncoding("GBK");
    Set<String> key = map.keySet();
    for (String tmp : key) {
      InputStream in = map.get(tmp);
      YHEsbClientUtility.output(in, zipout, tmp);
    }
    zipout.flush();
    zipout.close();
    out.close();
  }
  /**
   * 发送
   * @throws Exception 
   */
  public void sendEnvelope(Connection conn , String fromDept , String toDept , int isOut , int runId ,  Timestamp time  , String title , String sendDocNo ,  String docName,  String docId , String sendUnit) throws Exception {
    String insertInto = "insert into oa_officialdoc_send (TITLE , DEPT_ID , TO_DEPT , IS_OUT , RUN_ID , STATUS , IS_CANCEL , SEND_TIME , SEND_DOC_NO , DOC_NAME , DOC_ID , SEND_UNIT) " 
      + " values (?,'"+fromDept+"','"+toDept+"','"+isOut+"','"+runId+"',0, 0 , ? , ? , ? ,? , ? )";
    PreparedStatement stm = null; 
    try { 
      stm = conn.prepareStatement(insertInto);
      stm.setString(1, title);
      stm.setTimestamp(2, time);
      stm.setString(3, sendDocNo);
      stm.setString(4, docName);
      stm.setString(5, docId);
      stm.setString(6, sendUnit);
      stm.executeUpdate();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, null, null); 
    } 
  }
}
