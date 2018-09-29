package yh.core.funcs.system.accesscontrol.act;

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
import yh.core.funcs.system.accesscontrol.data.YHIpRule;
import yh.core.funcs.system.accesscontrol.logic.YHAccesscontrolLogic;
import yh.core.funcs.system.accesscontrol.logic.YHIpRuleLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHIpRuleAct {
  private static Logger log = Logger.getLogger(YHIpRuleAct.class);
  
  public String updateIpRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAccesscontrolLogic orgLogic = new YHAccesscontrolLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String secOcMark = request.getParameter("secOcMark");
      String beginIp = request.getParameter("beginIp");
      String endIp = request.getParameter("endIp");
      String type = request.getParameter("type");
      String remark = request.getParameter("remark");
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("type", secOcMark);
      m.put("beginIp", beginIp);
      m.put("endIp", endIp);
      m.put("type", type);
      m.put("remark", remark);
      t.updateSingle(dbConn, "ipRule", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addIpRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String secOcMark = request.getParameter("secOcMark");
      String beginIp = request.getParameter("beginIp");
      String endIp = request.getParameter("endIp");
      String remark = request.getParameter("remark");
      /* 没必要转特殊字符
      if(!YHUtility.isNullorEmpty(remark)){
        remark = remark.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
      }*/
      Map m =new HashMap();
      m.put("type", secOcMark);
      m.put("beginIp", beginIp);
      m.put("endIp", endIp);
      m.put("remark", remark);
      YHORM t = new YHORM();
      t.saveSingle(dbConn, "ipRule", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getIpRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHIpRuleLogic ruleLogic = new YHIpRuleLogic();
      ArrayList<YHIpRule> ruleList = new ArrayList<YHIpRule>();
      ruleList = ruleLogic.getIpRule(dbConn);
      request.setAttribute("ruleList", ruleList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/accesscontrol/ip/index.jsp";
  }
  public String getEditIpRule(HttpServletRequest request,
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
      funcList.add("ipRule");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_IPRULE");
      for(Map m : list) {
        String remark = (String) m.get("remark");
        remark = YHUtility.encodeSpecial(remark);
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",beginIp:\"" + m.get("beginIp") + "\"");
        sb.append(",endIp:\"" + m.get("endIp") + "\"");
        sb.append(",type:\"" + m.get("type") + "\"");
        sb.append(",remark:\"" + remark + "\"");
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
  
  public String deleteIpRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      Map m =new HashMap();
      m.put("seqId", seqId);
      YHORM t = new YHORM();
      t.deleteSingle(dbConn, "ipRule", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteAllIpRule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      Map m =new HashMap();
      YHIpRuleLogic ipLogin = new YHIpRuleLogic();
      ipLogin.deleteAll(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
