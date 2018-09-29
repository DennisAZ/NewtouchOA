package yh.core.funcs.workflow.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHMyDocLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHMyDocAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHMyDocAct");
  public String getMyWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson user = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String str =  "";
      String sortId = request.getParameter("sortId");
      YHMyDocLogic myWorkLogic = new YHMyDocLogic();
      str = myWorkLogic.getMyWork(dbConn, user, 10  , sortId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getSign(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson user = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String str =  "";
      String sortId = request.getParameter("sortId");
      YHMyDocLogic myWorkLogic = new YHMyDocLogic();
      str = myWorkLogic.getSign(dbConn, user, 10 , sortId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getFocusWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson user = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String str =  "";
      String sortId = request.getParameter("sortId");
      YHMyDocLogic myWorkLogic = new YHMyDocLogic();
      str = myWorkLogic.getFocusWork(dbConn, user, 10, sortId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
