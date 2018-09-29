package yh.core.funcs.system.censorwords.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.system.censorwords.data.YHCensorModule;
import yh.core.funcs.system.censorwords.logic.YHCensorModuleLogic;
import yh.core.funcs.system.censorwords.logic.YHCensorWordsLogic;
import yh.core.funcs.system.extuser.logic.YHExtUserLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHCensorModuleAct {
  private static Logger log = Logger.getLogger(YHCensorModuleAct.class);
  public String insertCensorModule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String useFlag = request.getParameter("useFlag"); 
      String smsRemind = request.getParameter("smsRemind"); 
      String sms2Remind = request.getParameter("sms2Remind");
      String checkUser = request.getParameter("checkUser");
      String bannedHint = request.getParameter("bannedHint");
      String modHint = request.getParameter("modHint");
      String filterHint = request.getParameter("filterHint");
      String moduleCode = request.getParameter("moduleCode");
      Map m =new HashMap();
      
      m.put("checkUser", checkUser);
      m.put("bannedHint", bannedHint);
      m.put("modHint", modHint);
      m.put("moduleCode", moduleCode);
      m.put("filterHint", filterHint);
      m.put("useFlag", useFlag);
      m.put("smsRemind", smsRemind);
      m.put("sms2Remind", sms2Remind);
      YHORM t = new YHORM();
      
      YHCensorModuleLogic cwLogic = new YHCensorModuleLogic();
      if(cwLogic.existsCensorModule(dbConn, moduleCode)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "错误  该模块已经存在！");
        return "/core/inc/rtjson.jsp";
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }

      t.saveSingle(dbConn, "censorModule", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSingleWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
    
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("replacement", replacement);
      orgLogic.updateSingleWords(dbConn, find, replacement);
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorModule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      YHCensorModuleLogic extLogic = new YHCensorModuleLogic();
      ArrayList<YHCensorModule> moduleList = new ArrayList<YHCensorModule>();
      
      moduleList = extLogic.getCensorModule(dbConn);
      //System.out.println(moduleList.size()+"UTUGIUHIOJ");
      request.setAttribute("moduleList", moduleList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/censorwords/module/index.jsp?userName="+userName;
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
  
  public String getCensorModuleId(HttpServletRequest request,
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
      funcList.add("censorModule");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_EXAMINE_MODULE");
      for(Map m : list) {
        String bannedHint = (String) m.get("bannedHint");
        if(!YHUtility.isNullorEmpty(bannedHint)){
          bannedHint = YHUtility.encodeSpecial(bannedHint);
        }
        String modHint = (String) m.get("modHint");
        if(!YHUtility.isNullorEmpty(modHint)){
          //modHint = modHint.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
          modHint = YHUtility.encodeSpecial(modHint);
        }
        String filterHint = (String) m.get("filterHint");
        if(!YHUtility.isNullorEmpty(filterHint)){
          //filterHint = filterHint.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
          filterHint = YHUtility.encodeSpecial(filterHint);
        }
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",moduleCode:\"" + m.get("moduleCode") + "\"");
        sb.append(",useFlag:\"" + m.get("useFlag") + "\"");
        sb.append(",checkUser:\"" + m.get("checkUser") + "\"");
        sb.append(",smsRemind:\"" + m.get("smsRemind") + "\"");
        sb.append(",sms2Remind:\"" + m.get("sms2Remind") + "\"");
        sb.append(",bannedHint:\"" + (m.get("bannedHint") == null ? "" : bannedHint) + "\"");
        sb.append(",modHint:\"" + (m.get("modHint") == null ? "" : modHint) + "\"");
        sb.append(",filterHint:\"" + (m.get("filterHint") == null ? "" : filterHint) + "\"");
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
  
  public String updateCensorModule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String moduleCode = request.getParameter("moduleCode");
      String useFlag = request.getParameter("useFlag");
      String smsRemind = request.getParameter("smsRemind");
      String sms2Remind = request.getParameter("sms2Remind");
      String checkUser = request.getParameter("checkUser");
      String bannedHint = request.getParameter("bannedHint");
      //bannedHint = YHUtility.encodeSpecial(bannedHint);
      String modHint = request.getParameter("modHint");
      //modHint = YHUtility.encodeSpecial(modHint);
      String filterHint = request.getParameter("filterHint");
      //filterHint = YHUtility.encodeSpecial(filterHint);
      String moduleCodeOld = request.getParameter("moduleCodeOld");
    
      if(!moduleCode.equals(moduleCodeOld)){
        YHCensorModuleLogic cwLogic = new YHCensorModuleLogic();
        if(cwLogic.existsCensorModule(dbConn, moduleCode)){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "错误  该模块已经存在！");
          return "/core/inc/rtjson.jsp";
        }else{
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
        }
      }
      
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("moduleCode", moduleCode);
      m.put("useFlag", useFlag);
      m.put("smsRemind", smsRemind);
      m.put("sms2Remind", sms2Remind);
      m.put("checkUser", checkUser);
      m.put("bannedHint", bannedHint);
      m.put("modHint", modHint);
      m.put("filterHint", filterHint);
      t.updateSingle(dbConn, "censorModule", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteModuleWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("idStrs");
      YHCensorModuleLogic wordsLogic = new YHCensorModuleLogic();
      wordsLogic.deleteAll(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteAllCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic wordsLogic = new YHCensorWordsLogic();
      if(userId == 196){//管理员seqId
        wordsLogic.deleteAllWords(dbConn);
      }else{
        wordsLogic.deleteAllFast(dbConn, userId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
