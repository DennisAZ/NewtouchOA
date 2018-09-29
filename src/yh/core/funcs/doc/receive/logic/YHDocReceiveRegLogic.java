package yh.core.funcs.doc.receive.logic;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHDocReceiveRegLogic{
  /**
   * 取得登记记录
   * @param conn
   * @param request
   * @param user
   * @param type
   * @return
   * @throws Exception
   */
  public StringBuffer getRegList( Connection conn, Map request,YHPerson user , String type , String webroot)
  throws Exception {
    StringBuffer resualt = new StringBuffer();
    String sql = "";
    try {
      sql = " select " 
        + " SEQ_ID " 
        + " , TITLE " 
        + " , FROMUNITS " 
        + " , OPPDOC_NO " 
        + " , RES_DATE " 
        + " , CREATE_USER_ID " 
        + " , REC_DOC_NAME, REC_DOC_ID " 
        + " , SEND_STATUS " 
        + " , SEND_RUN_ID "
        + ", sponsor"
        + " from oa_officialdoc_recv " 
        + " WHERE  1=1 ";
      if (!YHUtility.isNullorEmpty(type)) {
        sql += " AND SEND_STATUS = '" + type +"' ";
      }
      
      YHDocUtility docUtility = new YHDocUtility();
      if (!docUtility.haveAllRight(user, conn)) {
        String deptIds = YHWorkFlowUtility.getOutOfTail(docUtility.deptRight(user.getSeqId(), conn));
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
          sql += " AND SPONSOR IN (" + deptIds + ") ";
        }
      }
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        String sponsor = (String) record.getValueByName("fromDept");
        if (!YHUtility.isNullorEmpty(sponsor)) {
          String deptName = this.getDeptName(conn, sponsor);
          if (!YHUtility.isNullorEmpty(deptName)) {
            record.updateField("fromUnits", deptName);
          }
        }
      }
      resualt.append(pageDataList.toJson());
    } catch (Exception ex) {
      throw ex;
    }
    return resualt;
  }
  public String getDeptName(Connection conn , String deptId) throws Exception {
    String deptName = "";
    String query = "select DEPT_NAME FROM oa_department where seq_id = '" + deptId + "'";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        deptName = rs.getString("DEPT_NAME");
        return deptName;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    query = "select DEPT_NAME FROM oa_dept_ext where DEPT_ID = '" + deptId + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        deptName = rs.getString("DEPT_NAME");
        return deptName;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return deptName;
  }
  /**
   * 取得收文记录
   * @param conn
   * @param request
   * @param user
   * @param type
   * @return
   * @throws Exception
   */
  public String getRecReg( Connection conn, int seqId)
  throws Exception {
    String r = "";
    String sql = " select " 
      + " SEQ_ID " 
       + " , DOC_NO"
       + ", DOC_TYPE"
      + " , FROMUNITS " 
      + " , OPPDOC_NO " 
      + " , TITLE " 
      + " , ATTACHNAME, ATTACHID " 
      + " , SPONSOR"
      + ", REC_DOC_ID"
      + ", REC_DOC_NAME"
      + " from oa_officialdoc_recv " 
      + " WHERE  SEQ_ID =  " + seqId;
    Statement stm = null; 
    ResultSet rs = null; 
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(sql); 
      if (rs.next()){ 
        String docNo = rs.getString("DOC_NO");
        String DOC_TYPE = rs.getString("DOC_TYPE");
        String fromUnits = rs.getString("FROMUNITS");
        String attachIds = rs.getString("ATTACHID");
        String attachNames = rs.getString("ATTACHNAME");
        String sponsor = rs.getString("SPONSOR");
        String title = rs.getString("TITLE");
        String oppDocNo = rs.getString("OPPDOC_NO");
        String recDocId = rs.getString("REC_DOC_ID");
        String recDocName = rs.getString("REC_DOC_NAME");
        
        r = "{";
        r += "docType:\"" + YHUtility.null2Empty(DOC_TYPE) + "\"";
        r += ",docNo:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(docNo)) + "\"";
        r += ",fromUnits:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(fromUnits)) + "\"";
        r += ",oppDocNo:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(oppDocNo)) + "\"";
        r += ",title:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(title)) + "\"";
        r += ",sponsor:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(sponsor)) + "\"";
        r += ",attachNames:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(attachNames)) + "\"";
        r += ",attachIds:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(attachIds)) + "\"";
        r += ",recDocId:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(recDocId)) + "\"";
        r += ",recDocName:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(recDocName)) + "\"";
        r += "}";
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return r;
  }
  /**
   * 更新收文
   * @param conn
   * @param doc
   * @throws Exception
   */
  public void updateDocReceive(Connection conn, YHDocReceive doc) throws Exception{
    String sql = " update oa_officialdoc_recv set DOC_NO = ? " 
      + " , FROMUNITS  = ? " 
      + " , OPPDOC_NO = ? " 
      + " , TITLE = ? " 
      + " , COPIES = ? " 
      + " , CONF_LEVEL = ? " 
      + " , INSTRUCT = ? " 
      + " , SPONSOR = ? " 
      + " , RECE_USER_ID = ? " 
      + " , DOC_TYPE = ? " 
      + " , STATUS = ? " 
      + " , SEND_STATUS = ? " 
      + " ,ATTACHNAME = ?,ATTACHID = ? where SEQ_ID = " + doc.getSeq_id();
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, doc.getDocNo());
      ps.setString(2, doc.getFromUnits());
      ps.setString(3, doc.getOppdocNo());
      ps.setString(4, doc.getTitle());
      ps.setInt(5, doc.getCopies());
      ps.setInt(6, doc.getConfLevel());
      ps.setString(7, doc.getInstruct());
      ps.setString(8, doc.getSponsor());
      ps.setInt(9, doc.getUserId());
      ps.setInt(10, doc.getDocType());
      ps.setInt(11, 0);
      ps.setInt(12, 0);
      ps.setString(13, doc.getAttachNames());
      ps.setString(14, doc.getAttachIds());
      ps.execute();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
}
