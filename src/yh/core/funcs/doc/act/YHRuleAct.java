package yh.core.funcs.doc.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowRunFeedback;
import yh.core.funcs.doc.logic.YHFeedbackLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHRuleLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHRuleAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHRuleAct");
  public String addRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sUserId = request.getParameter("userId");
      int userId = loginUser.getSeqId();
      if (sUserId != null && !"".equals(sUserId)) {
        userId = Integer.parseInt(sUserId);
      }
      String sortId = request.getParameter("sortId");
      String checkAll = request.getParameter("checkAll");
      String alwaysOn = request.getParameter("alwaysOn");
      String beginDate = request.getParameter("beginDate");
      if ("".equals(beginDate)) {
        beginDate = null;
      }
      String endDate = request.getParameter("endDate");
      if ("".equals(endDate)) {
        endDate = null;
      }
      int toId = Integer.parseInt(request.getParameter("toId"));
      if ("on".equals(alwaysOn)) {
        beginDate = null;
        endDate = null;
      } 
      YHRuleLogic logic = new YHRuleLogic();
      if ("on".equals(checkAll)) {
        String flowIdStr = request.getParameter("flowIdStr");
        String[] flowIds = flowIdStr.split(",");
        for (String sFlowId : flowIds) {
          int flowId = 0 ;
          if (sFlowId != null && !"".equals(sFlowId)) {
            flowId = Integer.parseInt(sFlowId);
            //添加规则
            logic.addRule(userId, toId, flowId, beginDate, endDate, dbConn);
          }
        }
      } else {
        int flowId = Integer.parseInt(request.getParameter("flowId"));
        logic.addRule(userId, toId, flowId, beginDate, endDate, dbConn);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String loadRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sUserId = request.getParameter("queryUserId");
      int userId = loginUser.getSeqId();
      if (sUserId != null && !"".equals(sUserId)) {
        userId = Integer.parseInt(sUserId);
      }
      String sortId = request.getParameter("sortId");
      if (sortId == null) {
        sortId = "";
      }
      String ruleState = request.getParameter("ruleState");
      YHRuleLogic logic = new YHRuleLogic();
      String result = logic.loadRule(userId, Integer.parseInt(ruleState), dbConn , sortId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "取得成功!");
      request.setAttribute(YHActionKeys.RET_DATA, result);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String openOrClose(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int ruleId = Integer.parseInt(request.getParameter("ruleId"));
      String sIsOpened = request.getParameter("isOpened");
      boolean isOpened = Boolean.valueOf(sIsOpened);
      
      YHRuleLogic logic = new YHRuleLogic();
      logic.openOrClose(ruleId, isOpened, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String ruleId = request.getParameter("ruleId");
      YHRuleLogic logic = new YHRuleLogic();
      logic.delRule(ruleId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String closeOrOpenAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String ruleIds = request.getParameter("ruleIds");
      String sIsOpened = request.getParameter("isOpen");
      boolean isOpen = Boolean.valueOf(sIsOpened);
      YHRuleLogic logic = new YHRuleLogic();
      if (ruleIds != null) {
        logic.closeOrOpenAll(ruleIds , isOpen , dbConn);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String ruleIds = request.getParameter("ruleIds");
      YHRuleLogic logic = new YHRuleLogic();
      logic.delAll(ruleIds , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getRuleById(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int ruleId = Integer.parseInt(request.getParameter("ruleId"));
      YHRuleLogic logic = new YHRuleLogic();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
      request.setAttribute(YHActionKeys.RET_DATA, logic.getRuleById(ruleId, dbConn));
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int ruleId = Integer.parseInt(request.getParameter("ruleId"));
      int userId = Integer.parseInt(request.getParameter("userId"));
      if (loginUser.getSeqId() == userId || loginUser.isAdminRole()) {
        int toId = Integer.parseInt(request.getParameter("toId"));
        int flowId = Integer.parseInt(request.getParameter("flowId"));
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        YHRuleLogic logic = new YHRuleLogic();
        logic.updateRule(ruleId , userId, toId, flowId, beginDate, endDate, dbConn);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "操作成功!");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String queryUserId = request.getParameter("queryUserId");
      String sHasDelegate = request.getParameter("type");
      String sortId = request.getParameter("sortId");
      if (sortId == null) {
        sortId = "";
      }
      boolean hasDelegate = true;
      if (sHasDelegate != null 
          && !"".equals(sHasDelegate)) {
        hasDelegate = Boolean.valueOf(sHasDelegate);
      }
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int otherUser = loginUser.getSeqId();
      if (queryUserId != null && !"".equals(queryUserId)) {
        otherUser =  Integer.parseInt(queryUserId);
      }
      YHRuleLogic logic = new YHRuleLogic();
      Map map =  request.getParameterMap();
      String data = logic.getList(dbConn , otherUser , map , hasDelegate , sortId);
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
}
