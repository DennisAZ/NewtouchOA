package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.logic.YHConfigLogic;
import yh.core.funcs.workflow.logic.YHFlowTimerLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHFlowTimerAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFlowTimerAct");
  public String saveTimer(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String flowId = request.getParameter("flowId");
      String type = request.getParameter("TYPE");
      String privUser = request.getParameter("privUser");
      
      
      String dateVar = "REMIND_DATE" + type;
      String timeVar = "REMIND_TIME" + type;
      
      String remindDateS = request.getParameter(dateVar);
      String remindTimeS = YHUtility.null2Empty(request.getParameter(timeVar));
      
      
      if ("1".equals(type)) {
        remindDateS = request.getParameter("REMIND_TIME1");
        if (!YHUtility.isNullorEmpty(remindDateS)) {
          remindTimeS = remindDateS.split(" ")[1];
          remindDateS = remindDateS.split(" ")[0];
        }
      }
      
      if ("5".equals(type)) {
        String mon = YHUtility.null2Empty(request.getParameter("REMIND_DATE5_MON")) ;
        String day =  YHUtility.null2Empty(request.getParameter("REMIND_DATE5_DAY")) ;
        remindDateS = mon + "-" + day;
      }
      
      
      YHFlowTimerLogic logic = new YHFlowTimerLogic();
      logic.saveTimer(dbConn, seqId, flowId, type, privUser, remindDateS, remindTimeS);
      
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getTimers(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowId = request.getParameter("flowId");

      YHFlowTimerLogic logic = new YHFlowTimerLogic();
      String str = logic.getTimers(dbConn, flowId);
      
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getTimer(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");

      YHFlowTimerLogic logic = new YHFlowTimerLogic();
      String str = logic.getTimer(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delTimer(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHFlowTimerLogic logic = new YHFlowTimerLogic();
      logic.delTimer(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功！");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
