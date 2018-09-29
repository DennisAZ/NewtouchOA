package yh.core.funcs.workflow.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowWorkControlLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHMyWorkControlAct {
  private static Logger log = Logger.getLogger(YHMyWorkControlAct.class);
  // 工作监控 ACT
  public String getMyManagerWork1(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sSortId =  request.getParameter("sortId");
      YHFlowWorkControlLogic myWorkLogic = new YHFlowWorkControlLogic();
      StringBuffer result = myWorkLogic.getFlowRunManager(dbConn, request.getParameterMap(), loginUser ,  sSortId);
      PrintWriter pw = response.getWriter();
      pw.println( result.toString());
      pw.flush();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
//工作监控 ACT
  public String getMyManagerWork(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sSortId =  request.getParameter("sortId");
      YHFlowWorkControlLogic myWorkLogic = new YHFlowWorkControlLogic();
      StringBuffer result = myWorkLogic.getFlowRunManager1(dbConn, request.getParameterMap(), loginUser , sSortId);
      PrintWriter pw = response.getWriter();
      pw.println( result.toString());
      pw.flush();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
  
}
