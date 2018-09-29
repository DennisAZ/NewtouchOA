package yh.core.funcs.system.extuser.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.system.accesscontrol.logic.YHAccesscontrolLogic;
import yh.core.funcs.system.extuser.data.YHExtUser;
import yh.core.funcs.system.extuser.logic.YHExtUserLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHExtUserAct {
  private static Logger log = Logger.getLogger(YHExtUserAct.class);
  public String addExtUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String password = request.getParameter("password");
      String useFlagstr = request.getParameter("useFlagstr");
      String moduleSmsStr = request.getParameter("moduleSmsStr");
      String moduleWorkflowStr = request.getParameter("moduleWorkflowStr");
      String postfix = request.getParameter("postfix");
      String remark = request.getParameter("remark");
      String sumStr = "";
      YHExtUserLogic dl = new YHExtUserLogic();
      if(dl.existsTableNo(dbConn, userId)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "错误  用户名"+userId+"以存在，请重新填写！");
        return "/core/inc/rtjson.jsp";
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      if(moduleSmsStr.equals("1") && moduleWorkflowStr.equals("4")){
        sumStr = moduleSmsStr+","+moduleWorkflowStr+",";
      }else if(moduleSmsStr.equals("1")&&moduleWorkflowStr.equals("0")){
        sumStr = moduleSmsStr;
      }else if(moduleSmsStr.equals("0")&&moduleWorkflowStr.equals("4")){
        sumStr = moduleWorkflowStr;
      }else if(moduleSmsStr.equals("0")&&moduleWorkflowStr.equals("0")){
        sumStr = "";
      }
      
      Map m =new HashMap();
      m.put("userId", userId);
      m.put("password", password);
      m.put("useFlag", useFlagstr);
      m.put("authModule", sumStr);
      m.put("postfix", postfix);
      m.put("remark", remark);
      m.put("sysUser", "0");
      YHORM t = new YHORM();
      
      t.saveSingle(dbConn, "extUser", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getExtUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHExtUserLogic extLogic = new YHExtUserLogic();
      ArrayList<YHExtUser> extList = new ArrayList<YHExtUser>();
      extList = extLogic.getExtUser(dbConn);
      request.setAttribute("extList", extList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/extuser/manage.jsp";
  }
  
  public String getCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHExtUserLogic extLogic = new YHExtUserLogic();
      long sum = 0;
      sum = extLogic.existsCount(dbConn, 0);
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("sum:\"" + sum + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute("extListSum", sum);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
    //return "/core/funcs/system/extuser/manage.jsp";
  }
  
  public String getEditExtUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHUserPriv> perList = null;
      String[] filters = new String[]{"SEQ_ID="+seqId};
      List funcList = new ArrayList();
      funcList.add("extUser");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("EXT_USER");
      for(Map m : list) {
        
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userId:\"" + m.get("userId") + "\"");
        sb.append(",authModule:\"" + m.get("authModule") + "\"");
        sb.append(",useFlag:\"" + m.get("useFlag") + "\"");
        sb.append(",postfix:\"" + (m.get("postfix") == null ? "" : YHUtility.encodeSpecial((String)m.get("postfix"))) + "\"");
        sb.append(",remark:\"" + (m.get("remark") == null ? "" : YHUtility.encodeSpecial((String)m.get("remark"))) + "\"");
        sb.append("}");
      }       
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateExtUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAccesscontrolLogic orgLogic = new YHAccesscontrolLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String userId = request.getParameter("userId");
      String password = request.getParameter("password");
      String useFlagstr = request.getParameter("useFlagstr");
      String remark = request.getParameter("remark");
      String moduleSmsStr = request.getParameter("moduleSmsStr");
      String moduleWorkflowStr = request.getParameter("moduleWorkflowStr");
      String postfix = request.getParameter("postfix");
      String sumStr = "";
      if(moduleSmsStr.equals("1") && moduleWorkflowStr.equals("4")){
        sumStr = moduleSmsStr+","+moduleWorkflowStr+",";
      }else if(moduleSmsStr.equals("1")&&moduleWorkflowStr.equals("0")){
        sumStr = moduleSmsStr;
      }else if(moduleSmsStr.equals("0")&&moduleWorkflowStr.equals("4")){
        sumStr = moduleWorkflowStr;
      }else if(moduleSmsStr.equals("0")&&moduleWorkflowStr.equals("0")){
        sumStr = "";
      }
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("userId", userId);
      m.put("password", password);
      m.put("useFlag", useFlagstr);
      m.put("authModule", sumStr);
      m.put("postfix", postfix);
      m.put("remark", remark);
      t.updateSingle(dbConn, "extUser", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteExtUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("idStrs"));
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      //t.deleteSingle(dbConn, "extUser", m);
      YHExtUserLogic extLogic = new YHExtUserLogic();
      extLogic.deleteAll(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
