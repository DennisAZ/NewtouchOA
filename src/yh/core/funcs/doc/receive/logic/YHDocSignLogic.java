package yh.core.funcs.doc.receive.logic;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.receive.data.YHDocReceive;
import yh.core.funcs.doc.receive.logic.YHDocReceiveLogic;
import yh.core.funcs.doc.receive.logic.YHDocSmsLogic;
import yh.core.funcs.doc.send.logic.YHDocLogic;
import yh.core.funcs.doc.util.YHDocUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
public class YHDocSignLogic{
  public String getSendMesage(YHPerson user, Connection conn, Map request , String webroot, String isSign) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer resualt = new StringBuffer();
    String sql = "";
    try {
      String fromDeptName = request.get("fromDeptName") != null ? ((String[])request.get("fromDeptName"))[0] : null;
      String sendDocNo = request.get("sendDocNo") != null ? ((String[])request.get("sendDocNo"))[0] : null;
      String title = request.get("title") != null ? ((String[])request.get("title"))[0] : null;
      String endTime = request.get("endTime") != null ? ((String[])request.get("endTime"))[0] : null;
      String startTime = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
      
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
          sql += " AND TO_DEPT IN (" + deptIds + ") ";
        }
      }
      if (YHUtility.isNullorEmpty(isSign)) {
        isSign = "0";
      } 
      if ("0".equals(isSign)) {
        sql += " AND STATUS = '" + isSign + "'";
      } else {
        sql += " AND STATUS in  ('1','2') ";
      }
      if (!YHUtility.isNullorEmpty(title)) {
        sql += " and TITLE like '%" + YHDBUtility.escapeLike(title) + "%'";
      }
      if (!YHUtility.isNullorEmpty(sendDocNo)) {
        sql += " and oa_officialdoc_fl_run.DOC like '%" + YHDBUtility.escapeLike(sendDocNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(fromDeptName)) {
        sql += " and (oa_department.DEPT_NAME like '%" + YHDBUtility.escapeLike(fromDeptName) + "%' OR oa_dept_ext .DEPT_NAME like '%" + YHDBUtility.escapeLike(fromDeptName) + "%' OR SEND_UNIT like '%" + YHDBUtility.escapeLike(fromDeptName) + "%')";
      }
      if(startTime != null && !"".equals(startTime)){
        startTime +=  " 00:00:00";
        String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", startTime, " >= ");
        sql += " and " + dbDateF;
      }
      if(endTime != null && !"".equals(endTime)){
        endTime +=  " 23:59:59";
        String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", endTime, " <= ");
        sql += " and " + dbDateF;
      }
      
      sql = "select"
        + " TITLE"
        + " , SEND_DOC_NO"
        
        + ", DOC_NAME"
        + ", DOC_ID"
        
        + " ,oa_officialdoc_send.SEND_UNIT"
        + " ,SEND_TIME " 
        + " ,SIGN_TIME " 
        + " ,STATUS "
        + " ,IS_OUT "
        + " , oa_officialdoc_send.SEQ_ID" 
        + " ,oa_officialdoc_send.DEPT_ID"
        + " from oa_officialdoc_send left outer join oa_department ON oa_department.SEQ_ID = oa_officialdoc_send.DEPT_ID  left outer join oa_dept_ext ON oa_dept_ext.DEPT_ID = oa_officialdoc_send.DEPT_ID  where  IS_CANCEL='0' " +  sql;
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      YHDeptTreeLogic logic2 = new YHDeptTreeLogic();
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        int isOut = YHUtility.cast2Long(record.getValueByName("isOut")).intValue();
        String fromDept = (String)record.getValueByName("fromDeptId");
        if (isOut == 1) {
          fromDept = logic2.getDeptName(conn, fromDept);
          record.updateField("fromDept", fromDept);
        } 
        //else {
          
          //deptName = logic.getDeptNameById(conn, Integer.parseInt(fromDept));
        //}
        
      }
      resualt.append(pageDataList.toJson());
    } catch (Exception ex) {
      throw ex;
    }
    return resualt.toString();
  }
  public void sign(Connection dbConn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    Timestamp time =  new  Timestamp(new Date().getTime());
    String update = "UPDATE oa_officialdoc_send SET STATUS = '1' , SIGN_TIME=?  WHERE SEQ_ID = " + seqId ;
    PreparedStatement stm = null; 
    try { 
      stm = dbConn.prepareStatement(update);
      stm.setTimestamp(1, time);
      stm.executeUpdate();
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, null, null); 
    } 
  }
  public boolean isOut(Connection conn, String seqId) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String query = "select 1 from oa_officialdoc_send where seq_id = '" + seqId + "' and is_out = '1'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
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
  public void comeback(Connection conn, String seqId) throws Exception {
    // TODO Auto-generated method stub
    //if (!isOut(conn , seqId)) {
      String update = "UPDATE oa_officialdoc_send SET IS_CANCEL = '1' WHERE SEQ_ID = " + seqId ;
      YHWorkFlowUtility.updateTableBySql(update, conn);
    //}
  }
  public String getSendMesageDesktop(YHPerson user, Connection conn,
      Map request, String webroot) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    String sql = "";
    Statement stm =null;
    ResultSet rs = null;//结果集


    int showLen = 10;
    int pageIndex = 1;
    try {
      
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
          sql += " AND TO_DEPT IN (" + deptIds + ") ";
        }
      }
      sql += " AND STATUS = '0'";
      sql = "select"
        + " oa_officialdoc_send.SEQ_ID,TITLE"
        + " , SEND_DOC_NO"
        + ", DOC_NAME"
        + ", DOC_ID"
        + " ,oa_officialdoc_send.SEND_UNIT"
        + " ,SEND_TIME " 
        + " ,SIGN_TIME " 
        + " ,STATUS "
        + " ,IS_OUT "
        + " , oa_officialdoc_send.SEQ_ID" 
        + " ,oa_officialdoc_send.DEPT_ID"
        + " from oa_officialdoc_send left outer join oa_department ON oa_department.SEQ_ID = oa_officialdoc_send.DEPT_ID  left outer join oa_dept_ext ON oa_dept_ext.DEPT_ID = oa_officialdoc_send.DEPT_ID   where  IS_CANCEL='0' " +  sql;
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(sql);
      sb.append("{");
      sb.append("listData:[");
      int count = 0 ;
      YHDeptTreeLogic logic2 = new YHDeptTreeLogic();
      for (int i = 0; i < showLen && rs.next(); i++) { 
        sb.append("{");
        sb.append("title:\"" +   YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("TITLE"))) + "\"");
        int isOut = rs.getInt("IS_OUT");
        String fromDept = rs.getString("DEPT_ID");
        if (isOut == 1) {
          fromDept = logic2.getDeptName(conn, fromDept);
        } else {
          fromDept = rs.getString("SEND_UNIT");
        }
        sb.append(",sendDocNo:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("SEND_DOC_NO")))+ "\"");
        sb.append(",sendUnit:\"" +  YHUtility.encodeSpecial(YHUtility.null2Empty(fromDept)) + "\"");
        sb.append(",seqId:" + rs.getInt("SEQ_ID"));
        sb.append("},");
        count++ ;
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    //结束索引
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);;
    }
    sb.append("}");
    return sb.toString();
  }
}
