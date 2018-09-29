package yh.subsys.portal.guoyan.module.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.portal.guoyan.module.logic.YHPortalGridModuleResLogic;

public class YHPortalGridModuleRes {
  /**
   * 加载列表数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadGridData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPortalGridModuleResLogic lal = new YHPortalGridModuleResLogic();
      String data = lal.loadGridDataLogic(dbConn, request.getParameterMap()).toString();
      PrintWriter pw = response.getWriter();
      pw.write(data);
      pw.flush();
    }catch(Exception ex) {
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
  public String loadOneData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String newsId = request.getParameter("newsId");
      YHPortalGridModuleResLogic lal = new YHPortalGridModuleResLogic();
      String data = lal.loadOneData(dbConn,Integer.valueOf(newsId)).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
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
  public String loadDataPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String pageSize = request.getParameter("pageSize");
      String pageIndex = request.getParameter("pageIndex");
      String newsType = request.getParameter("newsType");

      YHPortalGridModuleResLogic lal = new YHPortalGridModuleResLogic();
      String data = lal.loadDataPage(dbConn, Integer.valueOf(pageSize), Integer.valueOf(pageIndex), newsType).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
