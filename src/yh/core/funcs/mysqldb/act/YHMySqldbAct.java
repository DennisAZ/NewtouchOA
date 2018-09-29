package yh.core.funcs.mysqldb.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipOutputStream;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.mysqldb.data.YHMySqlTabInfo;
import yh.core.funcs.mysqldb.logic.YHMySqlDBLogic;
import yh.core.funcs.office.ntko.logic.YHNtkoLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;

public class YHMySqldbAct {
/**
 * 
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String listTableInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      List<YHMySqlTabInfo> data = mydbl.getTableInfo(dbConn);
      request.setAttribute("tabInfo", data);
    } catch (Exception ex) {
      throw ex;
    }
    return "/core/funcs/mysqldb/list.jsp";
  }
 /**
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String importSql(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      ArrayList<String> data = mydbl.importSql(dbConn, fileForm.getInputStream("sql_file"));
      request.setAttribute("warring", data);
    } catch (Exception ex) {
      throw ex;
    }
    return "/core/funcs/mysqldb/importInfo.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doaction(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tables = request.getParameter("tables");
      String action = request.getParameter("action");
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      StringBuffer data = mydbl.getActionMrsg(dbConn, action, tables);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"取出数据失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDbNames(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      StringBuffer data = mydbl.getDataBaseNames(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"取出数据失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String backUp(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String[] dbName = request.getParameterValues("dbName");
      String backUpPath = request.getParameter("backUpDir");
      String dbNames = "";
      if(dbName != null){
        for (int i = 0; i < dbName.length; i++) {
          if(!"".equals(dbNames)){
            dbNames += ",";
          }
          dbNames += dbName[i];
        }
      }
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      StringBuffer data = mydbl.backup(dbConn, dbNames, backUpPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"取出数据失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getBackUpTask(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      StringBuffer data = mydbl.getBackUpTask(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"取出数据失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateOfficeTask(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      mydbl.updateOfficeTask(dbConn, Integer.valueOf(seqId), request.getParameterMap());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"热备份定时设置成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"热备份定时设置失败：" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String export(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    OutputStream ops = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tables = request.getParameter("tables");
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      String fileName = URLEncoder.encode( "1.zip","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      ZipOutputStream zos = new ZipOutputStream(ops);
      zos.setEncoding("GBK");
      mydbl.exportMysqlTable(dbConn, tables,zos);
      zos.flush();
      zos.close();
      ops.flush();
    } catch (Exception ex) {
      throw ex;
    }
    return null;
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String userRepair(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      mydbl.clearOnLineUser(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"在线人数已修正！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"在线人数修正失败：" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String userRepairById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIds = request.getParameter("userIds");
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      if(userIds.trim().endsWith(",")){
        userIds = userIds.trim().substring(0, userIds.trim().length() - 1);
      }
      mydbl.clearOnLineUser(dbConn,userIds);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"强制离线操作完成！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"强制离线操作失败：" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String checkDb(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHMySqlDBLogic mydbl = new YHMySqlDBLogic();
      boolean result = mydbl.checkDb();
      String data = "{isMysql:" + result + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,"数据库检查失败：" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
