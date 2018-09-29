package yh.core.funcs.doc.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.logic.YHWorkDestroyLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
/**
 * 工作流销毁
 * @author Think
 *
 */
public class YHWorkDestroyAct {
  private static Logger log = Logger.getLogger(YHWorkDestroyAct.class);
  // 工作监控 ACT
  public String getWorkList(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
      StringBuffer result = myWorkLogic.getWorkListLogic(dbConn,loginUser, request.getParameterMap());
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
   * 得到所有流程名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        
        YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
        String sortId = request.getParameter("sortId");
        String data = myWorkLogic.getFlow(dbConn  , sortId);
        request.setAttribute(YHActionKeys.RET_DATA, data);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  /**
   * 直接销毁工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String destroyBysearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        
        YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
        StringBuffer runIds = myWorkLogic.getAlldeleteRunId(dbConn,loginUser, request.getParameterMap());
        int data =  myWorkLogic.destroyFlowWork(dbConn, "workFlow", runIds.toString(), loginUser.getSeqId(),request.getRemoteAddr());
        request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
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
   * 查询后销毁工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String destroy(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        String runIds = request.getParameter("runId");
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
        int data =   myWorkLogic.destroyFlowWork(dbConn, "workFlow", runIds, loginUser.getSeqId(),request.getRemoteAddr());
        request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
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
   * 查询后还原工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String recover(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        String runIds = request.getParameter("runId");
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
        int data =  myWorkLogic.recoverWork(dbConn, runIds, "workFlow", loginUser.getSeqId());
        request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
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
   * 直接还原工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String recoverBysearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      YHPerson loginUser = null;
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        
        YHWorkDestroyLogic myWorkLogic = new YHWorkDestroyLogic();
        StringBuffer runIds = myWorkLogic.getAlldeleteRunId(dbConn, loginUser,request.getParameterMap());
        int data =  myWorkLogic.recoverWork(dbConn, runIds.toString(), "workFlow", loginUser.getSeqId());
        request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      } catch (Exception ex) {
        ex.printStackTrace();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
}
