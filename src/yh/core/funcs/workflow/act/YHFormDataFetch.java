package yh.core.funcs.workflow.act;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.logic.YHFormVersionLogic;
import yh.core.funcs.workflow.logic.YHWorkflowSave2DataTableLogic;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFormDataFetch {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFormDataFetch");
  public String getDataConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String query = "select FORM_SEQ_ID,FLOW_NAME,FLOW_TYPE.SEQ_ID AS flowId from oa_fl_type as FLOW_TYPE order by SEQ_ID";
      Statement stm2 = null;
      ResultSet rs2 = null;
      StringBuffer sb = new StringBuffer();
      sb.append("[");
      int count = 0 ;
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          int formId = rs2.getInt("FORM_SEQ_ID");
          int flowId = rs2.getInt("flowId");
          String dDesc = rs2.getString("FLOW_NAME");
          if (formId != 0 ) {
            sb.append("{formId:" + formId);
            sb.append(",flowName:\"" + dDesc + "\"");
            sb.append(",flowId:" + flowId );
            sb.append("},");
            count++;
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString() );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String dataSrc = request.getParameter("dataSrc");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int count = 0 ;
      String query = "select TITLE , ITEM_ID from oa_fl_form_item where form_id = '" + dataSrc + "'";
      Statement stm2 = null;
      ResultSet rs2 = null;
      StringBuffer sb = new StringBuffer();
      sb.append("[");
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          String dName = rs2.getString("ITEM_ID");
          String dDesc = rs2.getString("TITLE");
          sb.append("{");
          sb.append("itemId:\"" + dName + "\"" );
          sb.append(",title:\"" + dDesc + "\"" );
          sb.append("},");
          count++;
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString() );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDataByFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String dataSrc = request.getParameter("dataSrc");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int count = 0 ;
      String query = "select TITLE , ITEM_ID from oa_fl_form_item , oa_fl_type as FLOW_TYPE  where  oa_fl_form_item.form_id = FLOW_TYPE.FORM_SEQ_ID AND FLOW_TYPE.SEQ_ID = '" + dataSrc + "'";
      Statement stm2 = null;
      ResultSet rs2 = null;
      StringBuffer sb = new StringBuffer();
      sb.append("[");
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          String dName = rs2.getString("ITEM_ID");
          String dDesc = rs2.getString("TITLE");
          sb.append("{");
          sb.append("itemId:\"" + dName + "\"" );
          sb.append(",title:\"" + dDesc + "\"" );
          sb.append("},");
          count++;
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString() );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String dataFetch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer();
      String runId = request.getParameter("runId");
      String message = "";
      if (YHUtility.isNullorEmpty(runId) 
          || !YHUtility.isInteger(runId)) {
        message = "error:请输入正确的流水号!";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
        return "/core/inc/rtjson.jsp";
      }
      String dataField = YHUtility.null2Empty(request.getParameter("dataField"));
      String[] dataArray = dataField.split("`");
      String itemId = "";
      for (String s : dataArray) {
        if (!"".equals(s)) {
          itemId += s + ",";
        }
      }
      if (itemId.endsWith(",")) {
        itemId = itemId.substring(0 , itemId.length() - 1);
      }
      String sql = "select FLOW_ID from oa_fl_run where RUN_ID=" + runId;
      Statement stm2 = null;
      ResultSet rs2 = null;
      int flowId = 0 ;
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(sql);
        if (rs2.next()){
          flowId = rs2.getInt("FLOW_ID");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      String dataSrc = request.getParameter("dataSrc");
      sql = "select * from oa_fl_type where FORM_SEQ_ID='" + dataSrc + "' and SEQ_ID = '" + flowId + "'";
      Statement stm3 = null;
      ResultSet rs3 = null;
      
      try {
        stm3= dbConn.createStatement();
        rs3 = stm3.executeQuery(sql);
        if (!rs3.next()){
          message = "error:无法从未建立映射关系的表单提取数据!";
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, message);
          return "/core/inc/rtjson.jsp";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      String itemStr = YHUtility.null2Empty(request.getParameter("itemStr"));
      String itemArray[] = itemStr.split(",");//当前表单回填字段
      String itemIdArray[] = itemId.split(",");//目标表单ITEM_ID
      Map mapArray = new HashMap();
      for (int i = 0 ;i < itemArray.length ;i++) {
        String s = itemArray[i];
        if (!"".equals(s) && i <= itemIdArray.length) {
          mapArray.put(itemIdArray[i], s);
        }
      }
      sb.append("{");
      int count = 0 ;
      if (!YHWorkFlowUtility.isSave2DataTable()){
        String sql2="select ITEM_ID,ITEM_DATA from oa_fl_run_data WHERE RUN_ID=" +runId + " and ITEM_ID in ("+itemId+")";
        Statement stm4 = null;
        ResultSet rs4 = null;
        
        try {
          stm4= dbConn.createStatement();
          rs4 = stm4.executeQuery(sql2);
          while (rs4.next()){
            String itemData = YHUtility.null2Empty(rs4.getString("ITEM_DATA"));
            String itemId2 = rs4.getString("ITEM_ID");
            itemData = YHUtility.encodeSpecial(itemData);
            itemData = itemData.replace("\r\n", "<br>");
            itemData = itemData.replace("\n", "<br>");
            itemData = itemData.replace("\r", "<br>");
            sb.append("\"" + (String)mapArray.get(itemId2) + "\":\"" + itemData + "\",");
            count++;
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null); 
        }
      }else {
        YHFormVersionLogic lo = new YHFormVersionLogic();
        YHFlowRunUtility logic = new YHFlowRunUtility();
        
        int versionNo = lo.getVersionNo(dbConn, Integer.parseInt(runId));
        int formId = logic.getFormId(dbConn, flowId);
        int formSeqId = lo.getFormSeqId(dbConn, versionNo, formId);
        String tableName = YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE+ flowId  + "_" + formSeqId;
        
        String query ="select * from " + tableName + " where RUN_ID = "+ runId;
        Statement stm1 = null; 
        ResultSet rs1 = null; 
        try { 
          stm1 = dbConn.createStatement(); 
          rs1 = stm1.executeQuery(query); 
          if (rs1.next()){ 
            Set<String> keySet = mapArray.keySet();
            for (String id : keySet) {
              String key = "DATA_" + id;
              String itemData = rs1.getString(key);
              itemData = YHUtility.encodeSpecial(itemData);
              itemData = itemData.replace("\r\n", "<br>");
              itemData = itemData.replace("\n", "<br>");
              itemData = itemData.replace("\r", "<br>");
              sb.append("\"" + (String)mapArray.get(id) + "\":\"" + itemData + "\",");
              count++;
            }
          } 
        } catch(Exception ex) { 
          throw ex; 
        } finally { 
          YHDBUtility.close(stm1, rs1, null); 
        }
        
      }
      if (count > 0 ) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString() );
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
