package yh.core.funcs.doc.receive.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
public class YHDocRegisterLogic{
  /*
   * 本部门的，和自己外来发文登记权限
   */
  public String getSendMesage2(YHPerson user, Connection conn, Map request , String webroot, String isSign) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer resualt = new StringBuffer();
    String sql = "";
    try {
      String fromDeptName = request.get("fromDeptName") != null ? ((String[])request.get("fromDeptName"))[0] : null;
      String sendDocNo = request.get("sendDocNo") != null ? ((String[])request.get("sendDocNo"))[0] : null;
      String title = request.get("title") != null ? ((String[])request.get("title"))[0] : null;
      String endTime = request.get("endTime") != null ? ((String[])request.get("endTime"))[0] : null;
      String startTime = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
      
      String deptIds = YHWorkFlowUtility.getInStr(user.getDeptIdOther());
      deptIds +=  "'" +user.getDeptId() + "'";
      
      YHDocUtility docUtility = new YHDocUtility();
      if (docUtility.usingEsb() && docUtility.haveEsbRecRight(user, conn)) {
        YHEsbClientConfig config = YHEsbClientConfig.builder(webroot + YHEsbConst.CONFIG_PATH) ;
        YHDeptTreeLogic logic = new YHDeptTreeLogic();
        YHExtDept dept = logic.getDeptByEsbUser(conn, config.getUserId());
        if (!YHUtility.isNullorEmpty(deptIds)) {
          deptIds += ",'" ;
        }
        deptIds +=  dept.getDeptId() + "'";
      }
      if (YHUtility.isNullorEmpty(deptIds)) {
        sql += " AND 1<>1 ";
      } else {
        sql += " AND TO_DEPT IN (" + deptIds + ") ";
      }
      
      if (YHUtility.isNullorEmpty(isSign)) {
        isSign = "1";
      }
      if (!YHUtility.isNullorEmpty(title)) {
        sql += " and TITLE like '%" + YHDBUtility.escapeLike(title) + "%'";
      }
      if (!YHUtility.isNullorEmpty(sendDocNo)) {
        sql += " and oa_officialdoc_fl_run.DOC like '%" + YHDBUtility.escapeLike(sendDocNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(fromDeptName)) {
        sql += " and (oa_department.DEPT_NAME like '%" + YHDBUtility.escapeLike(fromDeptName) + "%' OR SEND_UNIT like '%" + YHDBUtility.escapeLike(fromDeptName) + "%')";
      }
      if(startTime != null && !"".equals(startTime)){
        startTime +=  " 00:00:00";
        String dbDateF = YHDBUtility.getDateFilter("SIGN_TIME", startTime, " >= ");
        sql += " and " + dbDateF;
      }
      if(endTime != null && !"".equals(endTime)){
        endTime +=  " 23:59:59";
        String dbDateF = YHDBUtility.getDateFilter("SIGN_TIME", endTime, " <= ");
        sql += " and " + dbDateF;
      }
      
      sql += " AND STATUS = '" + isSign + "'";
      sql = "select"
        + " TITLE"
        + " , SEND_DOC_NO"
        + ", DOC_NAME"
        + ", DOC_ID"
        + " ,SEND_UNIT"
        + " ,SEND_TIME " 
        + " ,SIGN_TIME " 
        + " ,STATUS "
        + " ,IS_OUT "
        + " , oa_officialdoc_send.SEQ_ID"
        + " , DEPT_ID"
        + " from oa_officialdoc_send left outer join oa_department ON oa_department.SEQ_ID = oa_officialdoc_send.DEPT_ID   where  IS_CANCEL='0' " +  sql;
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      YHDeptTreeLogic logic2 = new YHDeptTreeLogic();
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        int isOut = YHUtility.cast2Long(record.getValueByName("isOut")).intValue();
        String fromDept = (String)record.getValueByName("fromDeptId");
        String deptName = "";
        if (isOut == 1) {
          deptName = logic2.getDeptName(conn, fromDept);
          record.updateField("fromDept", deptName);
        } 
      }
      resualt.append(pageDataList.toJson());
    } catch (Exception ex) {
      throw ex;
    }
    return resualt.toString();
  }

  public String getRecReg(Connection conn, String rec_seqId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer().append("{");
    Statement stm = null;
    ResultSet rs = null;
    String query = "select"
      + " TITLE"
      + " , SEND_DOC_NO"
      + ", DOC_NAME"
      + ", DOC_ID"
      + ", DEPT_ID"
      + ", IS_OUT"
      + " , IS_CANCEL"
      + ", SEND_UNIT"
      + ", SEND_TIME"
      + ", oa_officialdoc_run.ATTACHMENT_NAME "
      + " , oa_officialdoc_run.ATTACHMENT_ID"
      + " from oa_officialdoc_send LEFT OUTER JOIN oa_officialdoc_run ON oa_officialdoc_run.RUN_ID = oa_officialdoc_send.RUN_ID  where  oa_officialdoc_send.seq_id = " + rec_seqId;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        sb.append("title:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("TITLE"))) + "\"");
        sb.append(",recDoc:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("SEND_DOC_NO"))) + "\"");
        sb.append(",recDocName:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("DOC_NAME"))) + "\"");
        sb.append(",recDocId:\"" + rs.getString("DOC_ID") + "\"");
        sb.append(",fromDeptId:\"" + rs.getString("DEPT_ID") + "\"");
        int isOut = rs.getInt("IS_OUT");
        String deptName = "";
        YHDeptTreeLogic logic2 = new YHDeptTreeLogic();
        if (isOut == 1) {
          deptName = logic2.getDeptName(conn, rs.getString("DEPT_ID"));
        } else {
          sb.append(",attachmentId:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("ATTACHMENT_ID"))) + "\"");
          sb.append(",attachmentName:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("ATTACHMENT_NAME"))) + "\"");
          deptName = YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("SEND_UNIT")));
        }
        sb.append(",fromDeptName:\"" + deptName + "\"");
        sb.append(",sendTime:\"" + YHUtility.getDateTimeStr(rs.getTimestamp("SEND_TIME")) + "\"");
        sb.append(",isCancel:\"" + rs.getString("IS_CANCEL") + "\"");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    sb.append("}");
    return sb.toString();
  }

  public int getMaxOrderNo(Connection conn, String type) throws Exception {
    // TODO Auto-generated method stub
    Statement stm = null;
    ResultSet rs = null;
    int result = 0 ;
    String query = "select max(REC_NO) from oa_officialdoc_rec_register   where REC_TYPE=  '" + type + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return result;
  }

  public void register(Connection conn, String recId, String recType,
      String recNo, String fromDeptName, String fromDeptId,
      String secretsLevel, String sendDocNo, String title, String copies,
      String recDocId, String recDocName, String attachmentId,
      String attachmentName , int userId) throws Exception {
    // TODO Auto-generated method stub
    String update = "insert into oa_officialdoc_rec_register (REC_ID, FROM_DEPT_ID, FROM_DEPT_NAME, REC_DOC_NAME, REC_DOC_ID, REC_TYPE, REC_NO, TITLE, REGISTER_USER, ATTACHMENT_ID, ATTACHMENT_NAME, COPIES, SEND_DOC_NO, SECRETS_LEVEL,REGISTER_TIME)" 
      + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement stm = null; 
    try { 
      Timestamp time =  new  Timestamp(new Date().getTime());
      stm = conn.prepareStatement(update);
      if (YHUtility.isNullorEmpty(recId)) {
        recId = "0";
      }
      stm.setInt(1, Integer.parseInt(recId));
      stm.setString(2, fromDeptId);
      stm.setString(3, fromDeptName);
      stm.setString(4, recDocName);
      stm.setString(5, recDocId);
      stm.setString(6, recType);
      stm.setInt(7,  Integer.parseInt(recNo));
      stm.setString(8, title);
      stm.setInt(9, userId);
      stm.setString(10, attachmentId);
      stm.setString(11, attachmentName);
      stm.setInt(12, Integer.parseInt( copies));
      stm.setString(13, sendDocNo);
      stm.setString(14, secretsLevel);
      stm.setTimestamp(15 , time);
      stm.executeUpdate();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, null, null); 
    } 
  }

  public void update(Connection conn, String seqId, String recId,
      String recType, String recNo, String fromDeptName, String fromDeptId,
      String secretsLevel, String sendDocNo, String title, String copies,
      String recDocId, String recDocName, String attachmentId,
      String attachmentName) throws Exception {
    // TODO Auto-generated method stub
    String update = "update oa_officialdoc_rec_register set  FROM_DEPT_NAME=?, REC_NO=?, TITLE=?, ATTACHMENT_ID=?, ATTACHMENT_NAME=?, COPIES=?, SEND_DOC_NO=?, SECRETS_LEVEL=?, REC_TYPE=?" 
      + " where seq_Id= " + seqId;
    PreparedStatement stm = null; 
    try { 
      stm = conn.prepareStatement(update);
      stm.setString(1, fromDeptName);
      
      stm.setInt(2,  Integer.parseInt(recNo));
      stm.setString(3, title);
      stm.setString(4, attachmentId);
      stm.setString(5, attachmentName);
      stm.setInt(6, Integer.parseInt( copies));
      stm.setString(7, sendDocNo);
      stm.setString(8, secretsLevel);
      stm.setString(9, recType);
      stm.executeUpdate();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, null, null); 
    }
  }
  public void updateStatus(Connection dbConn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String update = "UPDATE oa_officialdoc_send SET STATUS = '2'  WHERE SEQ_ID = " + seqId ;
    PreparedStatement stm = null; 
    try { 
      stm = dbConn.prepareStatement(update);
      stm.executeUpdate();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, null, null); 
    } 
  }

  public String getRegList(YHPerson user, Connection conn, Map request,
      String realPath) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer resualt = new StringBuffer();
    String sql = "";
    try {
      String type = ((String[])request.get("type"))[0];
      String fromDeptName = request.get("fromDeptName") != null ? ((String[])request.get("fromDeptName"))[0] : null;
      String sendDocNo = request.get("sendDocNo") != null ? ((String[])request.get("sendDocNo"))[0] : null;
      String title = request.get("title") != null ? ((String[])request.get("title"))[0] : null;
      String endTime = request.get("endTime") != null ? ((String[])request.get("endTime"))[0] : null;
      String startTime = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
      String secretsLevel = request.get("secretsLevel") != null ? ((String[])request.get("secretsLevel"))[0] : null;
      String recType = request.get("recType") != null ? ((String[])request.get("recType"))[0] : null;
      
      sql = "select"
        + " TITLE"
        + " , SEND_DOC_NO"
        + ", REC_DOC_NAME"
        + ", REC_DOC_ID"
        + " ,FROM_DEPT_NAME "
        + ", SECRETS_LEVEL"
        + " ,REC_TYPE " 
        + " ,REGISTER_TIME "
        + " , 1"
        + " ,oa_officialdoc_rec_register.SEQ_ID "
        + " , oa_officialdoc_rec_register.RUN_ID"
        + ", 1"
        + ", REC_NO"
        + " , oa_officialdoc_run.FLOW_ID"
        + " from oa_officialdoc_rec_register left outer join oa_officialdoc_run ON oa_officialdoc_rec_register.RUN_ID = oa_officialdoc_run.RUN_ID   where register_user = '" + user.getSeqId() + "'";
      if ("1".equals(type)) {
        sql += " and (oa_officialdoc_rec_register.RUN_ID = 0 or  oa_officialdoc_rec_register.RUN_ID IS NULL)";
      } else if ("2".equals(type)) {
        sql += " and (oa_officialdoc_rec_register.RUN_ID <> 0 or oa_officialdoc_rec_register.RUN_ID IS not NULL) and  oa_officialdoc_run.END_TIME IS  NULL";
      } else if ("3".equals(type)) {
        sql += " and oa_officialdoc_run.END_TIME IS NOT NULL";
      } 
      if (!YHUtility.isNullorEmpty(recType)) {
        sql += " and REC_TYPE like '%" + YHDBUtility.escapeLike(recType) + "%'";
      }
      if (!YHUtility.isNullorEmpty(secretsLevel)) {
        sql += " and SECRETS_LEVEL like '%" + YHDBUtility.escapeLike(secretsLevel) + "%'";
      }
      if (!YHUtility.isNullorEmpty(title)) {
        sql += " and TITLE like '%" + YHDBUtility.escapeLike(title) + "%'";
      }
      if (!YHUtility.isNullorEmpty(fromDeptName)) {
        sql += " and FROM_DEPT_NAME like '%" + YHDBUtility.escapeLike(fromDeptName) + "%'";
      }
      if (!YHUtility.isNullorEmpty(sendDocNo)) {
        sql += " and SEND_DOC_NO like '%" + YHDBUtility.escapeLike(sendDocNo) + "%'";
      }
      if(startTime != null && !"".equals(startTime)){
        startTime +=  " 00:00:00";
        String dbDateF = YHDBUtility.getDateFilter("REGISTER_TIME", startTime, " >= ");
        sql += " and " + dbDateF;
      }
      if(endTime != null && !"".equals(endTime)){
        endTime +=  " 23:59:59";
        String dbDateF = YHDBUtility.getDateFilter("REGISTER_TIME", endTime, " <= ");
        sql += " and " + dbDateF;
      }
      sql += " order by REGISTER_TIME desc";
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        int runId = YHUtility.cast2Long(record.getValueByName("runId")).intValue();
        int flowId = YHUtility.cast2Long(record.getValueByName("flowId")).intValue();
        if (runId == 0) {
          record.updateField("status", "未办理");
        } else {
          record.updateField("status", this.getPrcsName(conn, runId , flowId));
        }
        record.updateField("runEnd", this.isRunEnd(conn, runId));
      }
      resualt.append(pageDataList.toJson());
    } catch (Exception ex) {
      throw ex;
    }
    return resualt.toString();
  }
  public String getPrcsName(Connection conn , int runId , int flowId) throws Exception {
    String prcsName = "";
    String flowType = "1";
    Statement stm = null;
    ResultSet rs = null;
    String query = "select flow_type from oa_officialdoc_fl_type, oa_officialdoc_run  where FLOW_ID = oa_officialdoc_fl_type.seq_id AND  run_Id=  '" + runId + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        flowType = rs.getString("flow_type");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
    Statement stm2 = null;
    ResultSet rs2 = null;
    int prcsId = 0 ;
    List<Integer> flowPrcs = new ArrayList();
    String query2 = "select PRCS_ID, FLOW_PRCS from oa_officialdoc_fl_run_prcs  where run_Id=  '" + runId + "' AND PRCS_ID = (SELECT MAX(PRCS_ID) FROM DOC_FLOW_RUN_PRCS WHERE run_Id=  '" + runId + "')";
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query2);
      while (rs2.next()) {
        prcsId = rs2.getInt("PRCS_ID");
        flowPrcs.add(rs2.getInt("FLOW_PRCS"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
    if ("2".equals(flowType)) {
      return "自由流程：第" + prcsId + "步"; 
    } else {
      return this.getPrcsName(flowPrcs, flowId, conn);
    }
  }
  public String getPrcsName(List<Integer> flowPrcs , int flowId , Connection conn) throws Exception {
    Statement stm2 = null;
    ResultSet rs2 = null;
    String name = "";
    String str = "";
    for (Integer i : flowPrcs) {
      str += "'" + i + "',";
    }
    if (str.endsWith("," ))
      str = str.substring(0, str.length() - 1);
    String query2 = "select PRCS_NAME from oa_officialdoc_fl_process  where FLOW_SEQ_ID=  '" + flowId + "' AND PRCS_ID IN ("+str+")";
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query2);
      while (rs2.next()) {
        name += rs2.getString("PRCS_NAME") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
    if (name.endsWith(",")) 
      name = name.substring(0, name.length() - 1);
    if (flowPrcs.size() > 1) {
      return "并发：" + name;
    }
    return name;
  }
  public String isRunEnd(Connection conn , int runId) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String query = "select 1 from oa_officialdoc_run  where run_Id=  '" + runId + "' AND END_TIME IS NOT NULL";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        return "1";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return "0";
  }
  public String[] getAttach(Connection conn , String seqId ) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String[] result = new String[2];
    String query = "select attachment_name , attachment_id , REC_DOC_NAME  , REC_DOC_ID from oa_officialdoc_rec_register   where SEQ_ID=  '" + seqId + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        result[1] = rs.getString(1);
        result[0] = rs.getString(2);
        
          if (YHUtility.isNullorEmpty(result[1]) || result[1].endsWith("*")) {
            result[1] += YHUtility.null2Empty(rs.getString(3));
          } else {
            result[1] += "*" + YHUtility.null2Empty(rs.getString(3));
          }
          if (YHUtility.isNullorEmpty(result[0]) ||result[0].endsWith(",")) {
            result[0] += YHUtility.null2Empty(rs.getString(4));
          } else {
            result[0] += "," + YHUtility.null2Empty(rs.getString(4));
          }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    
    return result;
  }
  public void delRun(Connection conn, int runId) throws Exception {
    String sql = "update oa_officialdoc_rec_register set RUN_ID = null  where RUN_ID='" + runId + "'";
    PreparedStatement ps = null;
    try{
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (SQLException e){      
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  public String getRecRegBySeqId(Connection conn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String query = "select * from oa_officialdoc_rec_register where SEQ_ID=  '" + seqId + "'";
    StringBuffer sb = new StringBuffer().append("{");
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        sb.append("title:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("TITLE"))) + "\"");
        sb.append(",sendDocNo:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("SEND_DOC_NO"))) + "\"");
        sb.append(",copies:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("COPIES"))) + "\"");
        sb.append(",recNo:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("REC_NO"))) + "\"");
        sb.append(",recDocName:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("REC_DOC_NAME"))) + "\"");
        sb.append(",recDocId:\"" + rs.getString("REC_DOC_ID") + "\"");
        sb.append(",attachmentName:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("ATTACHMENT_NAME"))) + "\"");
        sb.append(",attachmentId:\"" + rs.getString("ATTACHMENT_ID") + "\"");
        sb.append(",recType:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("REC_TYPE"))) + "\"");
        sb.append(",secretsLevel:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("SECRETS_LEVEL"))) + "\"");
        sb.append(",fromDeptName:\"" + YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("FROM_DEPT_NAME"))) + "\"");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    sb.append("}");
    return sb.toString();
  }
}
