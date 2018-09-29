package yh.core.funcs.setdescktop.userinfo.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;  
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.setdescktop.setports.data.YHPort;
import yh.core.funcs.setdescktop.shortcut.logic.YHShortcutLogic;
import yh.core.funcs.setdescktop.theme.logic.YHThemeLogic;
import yh.core.funcs.setdescktop.userinfo.logic.YHUserinfoLogic;
import yh.core.funcs.system.data.YHSysFunction;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.menu.data.YHSysMenu;

public class YHUserinfoAct {
  
  public String getOnStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHUserinfoLogic logic = new YHUserinfoLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String onStatus = user.getOnStatus();
      String sex = user.getSex();
      if (YHUtility.isNullorEmpty(sex)) {
        sex = "1";
      }
      
      if (YHUtility.isNullorEmpty(onStatus)){
        onStatus = "1";
      }
      
      PrintWriter pw = response.getWriter();
      String data = "{status:\"" + onStatus + "\",sex:\"" + sex + "\"}";
      pw.println(data);
      pw.flush();
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String updateUserParam(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHUserinfoLogic logic = new YHUserinfoLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String key = request.getParameter("name");
      String value = request.getParameter("value");
      
      if (YHUtility.isNullorEmpty(key) || YHUtility.isNullorEmpty(value)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "传递参数不正确");
        return "/core/inc/rtjson.jsp";
      }
      
      Map<String, String> map = new HashMap<String, String>();
      map.put(key, value);
      logic.addUserParam(dbConn, map, user);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getMyStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String myStatus = user.getMyStatus();
      
      if (myStatus == null){
        myStatus = "";
      }
      
      PrintWriter pw = response.getWriter();
      pw.println(myStatus);
      pw.flush();
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String modifyOnStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String onStatus = request.getParameter("onStatus");
    Connection dbConn = null;
    
    try {
      YHUserinfoLogic logic = new YHUserinfoLogic();
      if (onStatus == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递状态值!");
        return "/core/inc/rtjson.jsp";
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      if ("1".equals(logic.getSecOnStatus(dbConn))) {
        logic.modifyOnStatus(dbConn, user.getSeqId(), onStatus);
      }
      logic.modifyStatusUserOnline(dbConn, user.getSeqId(), onStatus);
      user.setOnStatus(onStatus);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String modifyMyStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String myStatus = request.getParameter("myStatus");
    Connection dbConn = null;
    try {
      YHUserinfoLogic logic = new YHUserinfoLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      if (myStatus != null) {
        myStatus = YHUtility.encodeSpecial(myStatus);
      }
      
      logic.modifyMyStatus(dbConn, user.getSeqId(), myStatus);
      user.setMyStatus(myStatus);
      
      YHMsgPusher.pushMyStatus(String.valueOf(user.getSeqId()), myStatus);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
      
      //精灵更新组织机构使用
      YHMsgPusher.updateOrg(dbConn);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      YHUserinfoLogic logic = new YHUserinfoLogic();
      Map<String, String> map = logic.queryInfo(dbConn, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      request.setAttribute(YHActionKeys.RET_DATA, toJson(map));
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryCardInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String userId = request.getParameter("userId");
    if (!YHUtility.isNumber(userId)) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "id错误");
      return "/core/inc/rtjson.jsp";
    }
      
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
        YHPersonLogic pLogic = new YHPersonLogic();
        YHPerson user = pLogic.getPersonById(Integer.parseInt(userId), dbConn);
        
        YHUserinfoLogic uLogic = new YHUserinfoLogic();
      
      Map<String, String> map = uLogic.queryCardInfo(dbConn, user);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      request.setAttribute(YHActionKeys.RET_DATA, toJson(map));
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateBackground(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHUserinfoLogic logic = new YHUserinfoLogic();
      String background = request.getParameter("background");
      if (background == null) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "参数传递错误!");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map<String, String> map = YHFOM.json2Map(user.getParamSet());
      map.put("desktopBg", background);
      
      logic.addUserParam(dbConn, map, user);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public static String toJson(Map<String,String> m) throws Exception{
    StringBuffer sb = new StringBuffer("{");
    for (Iterator<Entry<String,String>> it = m.entrySet().iterator(); it.hasNext();){
      Entry<String,String> e = it.next();
      sb.append("\"");
      sb.append(e.getKey());
      sb.append("\":\"");
      String value = e.getValue();
      if (value == null) {
        value = "";
      }
      sb.append(value.trim());
      sb.append("\",");
    }
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}