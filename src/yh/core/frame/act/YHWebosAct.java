package yh.core.frame.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.frame.logic.YHWebosLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.logic.YHPortalLogic;
import yh.core.funcs.setdescktop.syspara.logic.YHSysparaLogic;
import yh.core.funcs.setdescktop.userinfo.logic.YHUserinfoLogic;
import yh.core.funcs.system.interfaces.data.YHInterFaceCont;
import yh.core.funcs.system.interfaces.logic.YHInterFacesLogic;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHRegistUtility;
import yh.core.util.form.YHFOM;
import yh.oa.tools.StaticData;

public class YHWebosAct {
  private static Logger log = Logger.getLogger(YHWebosAct.class);
  
  public String queryInitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      //门户的id
      String idStr = request.getParameter("id");
      int id = -1;
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        
      }
      
      if ("personal".equals(idStr)) {
        id = -2;
      }
      
      YHSysparaLogic sysParaLogic = new YHSysparaLogic();
      YHUserinfoLogic userInfoLogic = new YHUserinfoLogic();
      YHSystemLogic systemLogic = new YHSystemLogic();
      YHPortalLogic portalLogic = new YHPortalLogic();
      YHWebosLogic webosLogic = new YHWebosLogic();
      
      String portal = portalLogic.listPorts(dbConn, user, id);
      String title = systemLogic.getIETitle(dbConn);
      if (YHUtility.isNullorEmpty(title)) {
        title = StaticData.SOFTTITLE;
      }
      Map<String, String> map = userInfoLogic.queryInfo(dbConn, user);
      Map<String, String> otherPara = getOtherPara(request);
      Map<String, String> smsPara = getSmsPara();
      
      trimStringMap(map);
      trimStringMap(otherPara);
      trimStringMap(smsPara);
      
      int count = sysParaLogic.queryUserCount(dbConn);
      String onlineRefStr = YHSysProps.getString("$ONLINE_REF_SEC");
      if (onlineRefStr == null || "".equals(onlineRefStr.trim())) {
        onlineRefStr = "3600";
      }
      
      String funcId = sysParaLogic.queryFuncId(dbConn, "控制面板");
      otherPara.put("controlId", funcId);
      StringBuffer sb = new StringBuffer("{");
      sb.append("\"userInfo\":");
      sb.append(YHFOM.toJson(map).toString());
      sb.append(",\"background\":\"");
      sb.append(map.get("desktopBg"));
      sb.append("\",\"portal\":");
      sb.append(portal);
      sb.append(",\"browserTitle\":\"");
      sb.append(YHUtility.encodeSpecial(title));
      sb.append("\",\"onlineAmount\": {\"amount\":");
      sb.append(count);
      sb.append(",\"onlineRefStr\":");
      sb.append(onlineRefStr);
      sb.append("},\"smsPara\":");
      sb.append(YHFOM.toJson(smsPara));
      sb.append(",\"otherPara\":");
      sb.append(YHFOM.toJson(otherPara));
      sb.append(",\"bannerInfo\":");
      sb.append(YHFOM.toJson(webosLogic.getBannerInfo(dbConn)));
      sb.append(",\"logoutMsg\":");
      String logoutMsg = sysParaLogic.queryLogoutText(dbConn);
      sb.append("\"" + YHUtility.encodeSpecial(logoutMsg) + "\"");
      sb.append(",\"regist\":");
      sb.append(YHFOM.toJson(registInfo()));
      sb.append("}");
      
      
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public Map<String, String> getSmsPara() {
    Map<String, String> map = new HashMap<String, String>();
    String smsRef = YHSysProps.getString("$SMS_REF_SEC");
    if (smsRef == null || "".equals(smsRef.trim())) {
      smsRef = "30";
    }
    String smsCallCount = YHSysProps.getString("$SMS_REF_MAX");
    if (smsCallCount == null || "".equals(smsCallCount.trim())) {
      smsCallCount = "3";
    }
    String smsInterval = YHSysProps.getString("$SMS_CALLSOUND_INTERVAL");
    if (smsInterval == null || "".equals(smsInterval.trim())) {
      smsInterval = "3";
    }
    map.put("smsRef", smsRef);
    map.put("smsCallCount", smsCallCount);
    map.put("smsInterval", smsInterval);
    return map;
  }
  
  public Map<String, String> getOtherPara(HttpServletRequest request) {
    Map<String, String> map = new HashMap<String, String>();
    String sessionToken = (String)request.getSession().getAttribute("sessionToken");
    String statusRefStr = YHSysProps.getString("$STATUS_REF_SEC");
    if (statusRefStr == null || "".equals(statusRefStr.trim())) {
      statusRefStr = "3600";
    }
    YHSysparaLogic logic = new YHSysparaLogic();
    int remainDays = YHRegistUtility.remainDays();
    map.put("sesstionToken", sessionToken);
    map.put("statusRefStr", statusRefStr);
    map.put("remainDays", String.valueOf(remainDays));
    return map;
  }
  
  private Map<String, String> registInfo() {
    Map<String, String> map = new HashMap<String, String>();
    String hasReg = "1";
    if (!YHRegistUtility.hasRegisted()) {
      hasReg = "0";
      int remainDays = YHRegistUtility.remainDays();
      map.put("remainDays", String.valueOf(remainDays));
      try {
        map.put("machineCode", YHRegistUtility.getMchineCode());
      } catch (Exception e) {
        log.debug("获取机器码异常：" + e.getMessage());
      }
    }
    map.put("hasRegisted", hasReg);
    return map;
  }
  
  private void trimStringMap(Map<String, String> map) {
    for (String s : map.keySet()) {
      String value = map.get(s);
      if (value != null) {
        map.put(s, map.get(s).trim());
      }
    }
  }
  
  public String queryHeaderImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String defaultPath = "/core/frame/webos/styles/style1/images/logo.png";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHInterFacesLogic logic = new YHInterFacesLogic();
      String guid = logic.queryWebOSLOGO(dbConn);
      
      File dir = new File(YHInterFaceCont.ATTA_PATH + File.separator + YHInterFaceCont.MODULE + File.separator + guid);
      File[] files = dir.listFiles();
      if (files == null || files.length <= 0) {
        return defaultPath;
      }
      
      File file = files[0];
      if (!file.exists()) {
        return defaultPath;
      }
      
      FileInputStream fis = new FileInputStream(file);
      response.setContentType("image/");
      OutputStream out = response.getOutputStream();
      
      byte[] b = new byte[1024];  
      int i = 0;  
      
      while((i = fis.read(b)) > 0) {  
      out.write(b, 0, i);
      }
      
      out.flush();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch (FileNotFoundException ex) {
      return defaultPath;
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "";
  }
  
  
  public String listWallpappers(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String s = File.separator;
      String path = request.getSession().getServletContext().getRealPath(s) + "core" + s + "frame" + s + "webos" + s + "styles" + s + "wallpapers";
      
      StringBuffer sb = new StringBuffer("[");
      
      File dir = new File(path);
      File[] files = dir.listFiles();
      
      if (files != null) {
        for (File f : files) {
          if (f.getName().endsWith(".jpg") 
              || f.getName().endsWith(".JPG")
              || f.getName().endsWith(".png")
              || f.getName().endsWith(".PNG")
              || f.getName().endsWith(".jpeg")
              || f.getName().endsWith(".JPEG")
              || f.getName().endsWith(".gif")
              || f.getName().endsWith(".GIF")) {
            sb.append("\"");
            sb.append(f.getName());
            sb.append("\",");
          }
        }
        if (sb.charAt(sb.length() - 1) == ',') {
          sb.deleteCharAt(sb.length() - 1);
        }
      }
      
      sb.append("]");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}