package yh.core.funcs.doc.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.doc.logic.YHWorkLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHWorkLogAct {
  private static Logger log = Logger.getLogger(YHWorkLogAct.class);
  public String getWorkLogList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHWorkLogLogic  wll = new YHWorkLogLogic();
        StringBuffer result = wll.getWorklogListLogic(dbConn, request.getParameterMap());
        PrintWriter pw = response.getWriter();
        pw.println( result.toString());
        pw.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      }
      return null;
    }
  /**
   * 删除日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteLog(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHWorkLogLogic  wll = new YHWorkLogLogic();
        String seqId = request.getParameter("seqId");
        int result = wll.deleteLog(dbConn, seqId);
        request.setAttribute(YHActionKeys.RET_DATA,"\"" + result + "\"");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      } catch (Exception ex) {
        ex.printStackTrace();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  /**
   * 删除日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteLogBySearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHWorkLogLogic  wll = new YHWorkLogLogic();
        StringBuffer seqIds = wll.getAlldeleteSeqId(dbConn, request.getParameterMap());
        int result = 0;
        String seqIdStr = seqIds.toString();
        String[] seqIdArray = seqIdStr.split("\\*");
        for (int i = 0; i < seqIdArray.length; i++) {
          if(!"".equals(seqIdArray[i])){
            result += wll.deleteLog(dbConn, seqIdArray[i]);
          }
        }
        request.setAttribute(YHActionKeys.RET_DATA,"\"" + result + "\"");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       } catch (Exception ex) {
        ex.printStackTrace();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  /**
   * 删除日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPrcsName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        String flowId = request.getParameter("flowId");
        String flowPrcs = request.getParameter("flowPrcs");
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHWorkLogLogic  wll = new YHWorkLogLogic();
         String result = wll.getPrcsName(dbConn, flowId, flowPrcs);
        request.setAttribute(YHActionKeys.RET_DATA,"\"" + result + "\"");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      } catch (Exception ex) {
        ex.printStackTrace();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  /**
   * 删除日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String export(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String fileName = URLEncoder.encode("工作流日志.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHWorkLogLogic  wll = new YHWorkLogLogic();
      ArrayList<YHDbRecord > dbL = wll.toExportData(conn, request.getParameterMap());
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  
}
