package yh.rad.dsdef.act;


import java.io.PrintWriter;
import java.sql.Connection;

import javax.print.DocFlavor.STRING;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.rad.dsdef.logic.YHDsDefLogic;
import yh.rad.dsdef.logic.YHDsDefLogic2Db;

/**
 * 用于生成数据字典的物理结构
 * @author Think
 *
 */
public class YHDsDef2DBAct {

  /**
   *生成物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toPhysicsDb(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNo = request.getParameter("tableNo");
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      ddl.createPhyics(dbConn, tableNo);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构创建成功!");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构创建失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *生成物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String tabIsExist(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableName = request.getParameter("tableName");
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      boolean isExist = ddl.isExist(dbConn, tableName);
      String data = "0";
      if(isExist){
        data = "1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构创建失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *生成物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String dropTab(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableName = request.getParameter("tableName");
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      ddl.dropTabLogic(dbConn, tableName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构删除成功!");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构删除失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 抽取表的物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPhysicsDbInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String tableName = request.getParameter("tableName");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      String data = ddl.getPhysicsDbInfo(dbConn,tableName).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构删除失败!");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 抽取表的物理结构
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPhysicsDbInfo2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String tableName = request.getParameter("tableName");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      String data = ddl.getPhysicsDbInfo2(dbConn,tableName);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
      pw.close();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "物理结构删除失败!");
      throw ex;
    }
    return null;
  }
  
  public String isExistForTab(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String tableName = request.getParameter("tableName");
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDsDefLogic2Db ddl = new YHDsDefLogic2Db();
      String isExist  = ddl.isExistForTab(dbConn, tableName);
      String data = "{\"isExist\":\"" + isExist + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
