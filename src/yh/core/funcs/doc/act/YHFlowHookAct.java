package yh.core.funcs.doc.act;

import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.logic.YHFlowHookLogic;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
public class YHFlowHookAct {
  private YHFlowHookLogic logic=new YHFlowHookLogic();
  
  public String addHookAct(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String fId = request.getParameter("fId"); 
      Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    
      dbConn = requestDbConn.getSysDbConn();
      this.logic.addHookLogic(dbConn,request.getParameterMap(),person);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);     
   
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getHookListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String realPath = request.getRealPath("/");
      String data = this.logic.getHookJsonLogic(dbConn, request.getParameterMap(), person ,realPath);
      
      PrintWriter pw = response.getWriter();
      //System.out.println(data);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String getFlowNameAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String flowId=request.getParameter("flowId");
      String data = this.logic.getFlowNameLogic(dbConn, flowId);
      data="{flowName:'"+data+"'}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteHookAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId=request.getParameter("seqId");
      this.logic.deleteHookLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getHook(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String hid = request.getParameter("hid"); 
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowHookLogic logic = new YHFlowHookLogic();
      String realPath = request.getRealPath("/");
      String str = logic.getHook(dbConn, Integer.parseInt(hid) , realPath);
      request.setAttribute(YHActionKeys.RET_DATA, str);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateHook(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String hid = request.getParameter("hid"); 
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if (!YHUtility.isNullorEmpty(hid)) {
        String hname = request.getParameter("hname");
        String hdesc = request.getParameter("hdesc");
        String status = request.getParameter("status");
        String openeddata = request.getParameter("openeddata");
        String openedplugin = request.getParameter("openedplugin");
        
        String system = request.getParameter("system");
        String query = "update "+ YHWorkFlowConst.FLOW_HOOK +" as FLOW_HOOK set hname='"+ hname +"',hdesc='"+ hdesc +"',status='"+status+"'";
        if ("1".equals(openeddata)) {
          String mapIn = request.getParameter("mapIn");
          String flowId = request.getParameter("flows");
          query += ",map='"+mapIn+"',flow_id='"+flowId+"'";
        }
        if ("1".equals(openedplugin)) {
          String conditionSet = request.getParameter("prcsInSet");
          String condition = request.getParameter("prcsIn").replaceAll("\r\n", ""); 
          condition = condition.replace("'", "''");
          conditionSet = conditionSet.replace("]AND", "] AND");
          conditionSet = conditionSet.replace("]OR", "] OR");
          conditionSet = conditionSet.replace("AND[", "AND [");
          conditionSet = conditionSet.replace("OR[", "OR [");
          query += ",condition_set='"+conditionSet+"',FLOW_HOOK.condition='"+condition+"'";
          if ("0".equals(system)) {
            String plugin = request.getParameter("plugin");
            query += ",plugin='"+plugin+"' ";
          }
        }
        query += "  where SEQ_ID='"+hid+"'";
        YHWorkFlowUtility.updateTableBySql(query, dbConn);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
