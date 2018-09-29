package yh.core.funcs.workflow.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowOverTimeLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHWorkOverTimeAct {
  private static Logger log = Logger
      .getLogger("yh.core.funcs.workflow.act.YHWorkQueryAct");

  public String getWorkOverTimeList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowList");
    if ("".equals(flowId) || flowId == null || flowId.equals("undefined") || flowId =="undefined") {
      flowId = "0";
    }
    String flowStatus = request.getParameter("flowStatus");
    String starttime = request.getParameter("statrTime");
    String endtime = request.getParameter("endTime");
    String sortId = request.getParameter("sortId");
    if (sortId == null) {
      sortId = "";
    }
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);

      YHFlowOverTimeLogic myWorkLogic = new YHFlowOverTimeLogic();
      String result = myWorkLogic.getWorkOverTimeList(Integer.parseInt(flowId),flowStatus, starttime, endtime, dbConn,  request.getParameterMap(),loginUser , sortId);
      PrintWriter pw = response.getWriter();
      pw.println(result);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 工作超时统计重写的方法
   */
  public String getOverTimeTotal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    int flag = 0;
    String sFlag = request.getParameter("flag");
    if (YHUtility.isInteger(sFlag)) {
      flag = Integer.parseInt(sFlag);
    }
    String flowquery = request.getParameter("flowquery");
    String bumenquery = request.getParameter("STATCS_MANNER_QUERY"); 
    String starttime = request.getParameter("statrTime");
    String endtime = request.getParameter("endTime");
    String user = request.getParameter("user"); 
    String dept = request.getParameter("dept");
    String role = request.getParameter("role");
    YHPerson loginUser = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      StringBuffer sb = new StringBuffer();
      YHFlowOverTimeLogic myWorkLogic = new YHFlowOverTimeLogic();
      Map map = myWorkLogic.getOverTimeTotal(flowquery, bumenquery, starttime, endtime, user, dept, role, conn, loginUser);
      if (flag != 2) {
        request.setAttribute("flowData", map);
      } else {
        response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
        InputStream is = null;
        try{
          String fileName = URLEncoder.encode("超时统计结果.csv","UTF-8");
          fileName = fileName.replaceAll("\\+", "%20");
          response.setHeader("Cache-control","private");
          response.setContentType("application/vnd.ms-excel");
          response.setHeader("Cache-Control","maxage=3600");
          response.setHeader("Pragma","public");
          response.setHeader("Accept-Ranges","bytes");
          response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
          ArrayList<YHDbRecord > dbL = myWorkLogic.covertToExportCsvData(map);
          YHCSVUtil.CVSWrite(response.getWriter(), dbL);
        } catch (Exception ex) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
          throw ex;
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    //返回到柱状图页面
    if (flag == 1) {
      return "/core/funcs/workflow/flowrun/overtime/columnChart.jsp";
    } else if (flag == 2) {
      return null;
    } else {
      return "/core/funcs/workflow/flowrun/overtime/overouttime.jsp";
    }
  }
  public String viewDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sFlowId = request.getParameter("flowId");
      int flowId= 0 ;
      if (sFlowId != null && !"".equals(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      String sUserId = request.getParameter("userId");
      int userId = 0 ;
      if (sUserId != null && !"".equals(sUserId)) {
        userId = Integer.parseInt(sUserId);
      }
      String prcsDate1Query = request.getParameter("prcsDate1Query");
      String prcsDate2Query = request.getParameter("prcsDate2Query");
      YHFlowOverTimeLogic logic = new YHFlowOverTimeLogic();
      String data = logic.viewDetail(dbConn, request.getParameterMap(), flowId, userId, prcsDate1Query, prcsDate2Query);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String exportCsv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowId = request.getParameter("flowList");
    if ("".equals(flowId) || flowId == null || flowId.equals("undefined") || flowId =="undefined") {
      flowId = "0";
    }
    String flowStatus = request.getParameter("flowStatus");
    String starttime = request.getParameter("statrTime");
    String endtime = request.getParameter("endTime");
    String sortId = request.getParameter("sortId");
    if (sortId == null) {
      sortId = "";
    }
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    YHPerson loginUser = null;
    InputStream is = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String fileName = URLEncoder.encode("超时工作记录.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHFlowOverTimeLogic myWorkLogic = new YHFlowOverTimeLogic();
      ArrayList<YHDbRecord > dbL = myWorkLogic.getOverTimeList(Integer.parseInt(flowId),flowStatus, starttime, endtime, conn,  request.getParameterMap(),loginUser , sortId);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      throw ex;
    }
    return null;
  }
}
