package yh.core.funcs.workflow.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.logic.YHConfigLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHConfigAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHConfigAct");
  public String getParam(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHConfigLogic logic = new YHConfigLogic();
      String sealForm = logic.getSysPar("SEAL_FROM", dbConn);
      if (sealForm == null  || "".equals(sealForm)) {
        sealForm = "1";
      }
      String flowRemindAfter = logic.getSysPar("FLOW_REMIND_AFTER", dbConn);
      String unitAfter = "d";
      if (flowRemindAfter != null  && !"".equals(flowRemindAfter)) {
        unitAfter =  flowRemindAfter.substring(flowRemindAfter.length() - 1);
        if (flowRemindAfter.length() == 1) {
          flowRemindAfter = "";
        } else {
          flowRemindAfter =  flowRemindAfter.substring(0 , flowRemindAfter.length() - 1);
        }
      } else {
        flowRemindAfter = "";
      }
      String flowRemindBefore = logic.getSysPar("FLOW_REMIND_BEFORE", dbConn);
      String unitBefore = "m";
      if (flowRemindBefore != null && !"".equals(flowRemindBefore)) {
        unitBefore =  flowRemindBefore.substring(flowRemindBefore.length() - 1);
        if (flowRemindBefore.length() == 1) {
          flowRemindBefore = "";
        } else {
          flowRemindBefore =  flowRemindBefore.substring(0 , flowRemindBefore.length() - 1);
        }
      } else {
        flowRemindBefore = "";
      }
      String flowAction = logic.getSysPar("FLOW_ACTION", dbConn);
      
      String mobile = logic.getSysPar("FLOW_MOBILE_REMIND", dbConn);
      request.setAttribute("flowMobileRemind", mobile);
      request.setAttribute("flowRemindBefore", flowRemindBefore);
      request.setAttribute("unitBefore", unitBefore);
      request.setAttribute("flowRemindAfter", flowRemindAfter);
      request.setAttribute("unitAfter", unitAfter);
      request.setAttribute("sealForm", sealForm);
      request.setAttribute("flowAction", flowAction);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/workflow/config/index.jsp";
  }
  public String saveConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sealForm = request.getParameter("sealForm");
      String flowRemindBefore = request.getParameter("flowRemindBefore");
      if (flowRemindBefore == null) {
        flowRemindBefore = "";
      }
      String unitBefore = request.getParameter("unitBefore");
      flowRemindBefore = flowRemindBefore + unitBefore;
      String flowRemindAfter = request.getParameter("flowRemindAfter");
      if (flowRemindAfter == null) {
        flowRemindAfter = "";
      }
      String flowMobileRemind = request.getParameter("flowMobileRemind");
      if (flowMobileRemind == null) {
        flowMobileRemind = "";
      }
      if ("on".equals(flowMobileRemind)) {
        flowMobileRemind = "1";
      }
      String unitAfter = request.getParameter("unitAfter");
      flowRemindAfter = flowRemindAfter + unitAfter;
      StringBuffer flowAction=new StringBuffer("");
      String []flowActionArray=request.getParameterValues("FLOW_ACTION");
      if(flowActionArray!=null && flowActionArray.length>0){
    	  for(int i=0;i<flowActionArray.length;i++){
    		  if(i==flowActionArray.length-1){
    			  flowAction.append(flowActionArray[i]);
    		  }else{
    			  flowAction.append(flowActionArray[i]);
    			  flowAction.append(",");
    		  }
    	  }
      }
      YHConfigLogic logic = new YHConfigLogic();
      logic.updateSysPar("FLOW_MOBILE_REMIND", flowMobileRemind, dbConn);
      logic.updateSysPar("SEAL_FROM", sealForm, dbConn);
      logic.updateSysPar("FLOW_REMIND_AFTER", flowRemindAfter, dbConn);
      logic.updateSysPar("FLOW_REMIND_BEFORE", flowRemindBefore, dbConn);
      logic.updateSysPar("FLOW_ACTION",flowAction.toString(), dbConn);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
