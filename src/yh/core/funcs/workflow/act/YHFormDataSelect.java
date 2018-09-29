package yh.core.funcs.workflow.act;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHPluginLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;

public class YHFormDataSelect {
  private static Logger log = Logger.getLogger(YHFormDataSelect.class);
  
  public String getDataConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String contextRealPath = request.getSession().getServletContext().getRealPath("/");
      String path = contextRealPath + "/core/funcs/workflow/flowform/editor/plugins/NDataSelect/config.properties";
      Properties p = new Properties();
      p.load(new InputStreamReader(new FileInputStream(new File(path)) , "UTF-8"));
      StringBuffer sb = new StringBuffer();
      Set<Object> keys = p.keySet();
      sb.append("{");
      int count = 0 ;
      for (Object o : keys) {
        String s = (String)o;
        String value = p.getProperty(s);
        sb.append(s + ":" + value + ",");
        count++;
      }
    
      
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("},selfDef:{");
    //自定义数据源
      count = 0 ;
      String query = "select d_name,d_desc from oa_db_source order by seq_id";
      Statement stm2 = null;
      ResultSet rs2 = null;
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          String dName = rs2.getString("d_name");
          String dDesc = rs2.getString("d_desc");
          sb.append("\"DATA_" + dName + "\":\""  + dDesc + "\",");
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
      sb.append("}");
      log.debug(sb.toString());
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
      String tableName  = "data_" + dataSrc;
      String query  = "SHOW FULL FIELDS FROM " + tableName;
      Statement stm2 = null;
      ResultSet rs2 = null;
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      try {
        stm2 = dbConn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          String dName = rs2.getString("Field");
          String dDesc = rs2.getString("Comment");
          sb.append("\"" + dName + "\":\""  + dDesc + "\",");
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
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString() );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String selectData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sql = request.getParameter("sqlId");
      String findStr = request.getParameter("findStr");
      YHPluginLogic logic = new YHPluginLogic();
      StringBuffer result = logic.getSelectData(dbConn, request.getParameterMap(), findStr, sql);
      PrintWriter pw = response.getWriter();
      pw.println(result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String selectFlowData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowId = request.getParameter("flowId");
      String findStr = request.getParameter("findStr");
      String field = request.getParameter("field");
      YHPluginLogic logic = new YHPluginLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer result = logic.getSelectFlowData(dbConn, request.getParameterMap(), findStr, flowId , field , loginUser);
      PrintWriter pw = response.getWriter();
      pw.println(result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}
