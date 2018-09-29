package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHMyWorkLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHMyWorkAct {
  private static Logger log = Logger.getLogger(YHMyWorkAct.class);
 
  public String getMyWorkList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String showLenStr = request.getParameter("showLength");
    String flowIdStr = request.getParameter("flowId");
    String pageIndexStr = request.getParameter("pageIndex");
    String typeStr = request.getParameter("typeStr");
    String sSortId = request.getParameter("sortId");
    String sort = request.getParameter("sort");
    if (YHUtility.isNullorEmpty(sort)) {
      sort  = "DESC";
    }
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String str =  "";
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      
      String filedList = "";
      if (!YHUtility.isNullorEmpty(flowIdStr)) {
        filedList = YHWorkFlowUtility.getOutOfTail(myWorkLogic.getFildList(dbConn, flowIdStr));
      }
      
      if("3".equals(typeStr)){
        String opFlag = request.getParameter("opFlag");
        str = myWorkLogic.getEndWorkList1( loginUser, Integer.parseInt(pageIndexStr),
        flowIdStr, Integer.parseInt(showLenStr),opFlag  , sSortId ,dbConn , filedList  , sort);
      }else{
        str = myWorkLogic.getMyWorkList( loginUser, Integer.parseInt(pageIndexStr),
        flowIdStr, Integer.parseInt(showLenStr),typeStr , sSortId , dbConn , filedList , sort);
      }
      log.debug(str);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,filedList);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        String message = YHWorkFlowUtility.Message("用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getHeader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      String filedList = "";
      if (!YHUtility.isNullorEmpty(flowIdStr)) {
        filedList = YHWorkFlowUtility.getOutOfTail(myWorkLogic.getFildList(dbConn, flowIdStr));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,filedList);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 桌面模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
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
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
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
  /**
   * 桌面模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
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
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
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
  /**
   * 桌面模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
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
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
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
  public String getWorkMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      String str =  myWorkLogic.getWorkMsg(Integer.parseInt(flowIdStr), Integer.parseInt(runIdStr), dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        String message = YHWorkFlowUtility.Message("用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getPrcsList1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      Map map =  myWorkLogic.getPrcsListMsg1(Integer.parseInt(flowIdStr), Integer.parseInt(runIdStr) , dbConn);
      request.setAttribute("map" , map);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        String message = YHWorkFlowUtility.Message("用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }
      throw ex;
    }
    return "/core/funcs/workflow/flowrun/list/flowview/viewgraph2.jsp?flowId=" + flowIdStr + "&runId=" + runIdStr;
  }
  public String getPrcsList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      String str =  myWorkLogic.getPrcsListMsg(Integer.parseInt(flowIdStr), Integer.parseInt(runIdStr) , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        String message = YHWorkFlowUtility.Message("用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getLogList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      String str =  myWorkLogic.getLogList(Integer.parseInt(flowIdStr), Integer.parseInt(runIdStr) , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!");
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delRun(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runIdStr = request.getParameter("runId");
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      myWorkLogic.delRun(Integer.parseInt(runIdStr), loginUser, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据!");
    } catch (Exception ex) {
      if(loginUser == null){
        String contextPath = request.getContextPath();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户未登录，请重新<a href='"+contextPath+"/login.jsp'>登录</a>!");
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      }
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String callBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson loginUser = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic myWorkLogic = new YHMyWorkLogic();
      String runIdStr = request.getParameter("runId");
      String prcsIdStr = request.getParameter("prcsId");
      String flowPrcsStr = request.getParameter("flowPrcs");
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      String msg = myWorkLogic.callBack(runId, prcsId, flowPrcs, loginUser, dbConn);
      if (msg == null ) {
        msg = "收回操作成功";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,msg);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
 
  /**
   * 是否有未接收的工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String hasWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String fFlowId = request.getParameter("flowId");  
    String sSortId = request.getParameter("sortId");
    int flowId = 0 ;
    
    if (YHUtility.isInteger(fFlowId)) {
      flowId = Integer.parseInt(fFlowId);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHMyWorkLogic logic = new YHMyWorkLogic();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"");
      request.setAttribute(YHActionKeys.RET_DATA, String.valueOf(logic.hasWork(dbConn, loginUser.getSeqId() , sSortId , flowId)));
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
