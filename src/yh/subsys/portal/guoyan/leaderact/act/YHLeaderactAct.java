package yh.subsys.portal.guoyan.leaderact.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.portal.guoyan.leaderact.logic.YHLeaderactLogic;
/**
 * 领导活动
 * @author Think
 *
 */
public class YHLeaderactAct {
  /**
   * 取的领导活动数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listDataLimit(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHLeaderactLogic lal = new YHLeaderactLogic();
      String limitstr = request.getParameter("pageSize");
      String data = lal.loadNew2(dbConn, Integer.valueOf(limitstr)).toString();
      PrintWriter pw = response.getWriter();
      pw.write(data);
      pw.flush();
    }catch(Exception ex) {
      
      throw ex;
    }
    return null;
  }
  
  /**
   * 取的领导活动数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listDataPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHLeaderactLogic lal = new YHLeaderactLogic();
      String pageSize = request.getParameter("pageSize");
      String pageIndex = request.getParameter("pageIndex");
      System.out.println(request.getParameterMap());
      String data = lal.loadNew(dbConn, Integer.valueOf(pageSize),Integer.valueOf(pageIndex)).toString();
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
   * 取的领导活动数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOneNews(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHLeaderactLogic lal = new YHLeaderactLogic();
      String newId = request.getParameter("newId");
      String data = lal.loadOneNew(dbConn, Integer.valueOf(newId)).toString();
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
