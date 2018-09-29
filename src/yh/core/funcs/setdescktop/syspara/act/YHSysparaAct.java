package yh.core.funcs.setdescktop.syspara.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.act.YHImageUtil;
import yh.core.funcs.portal.logic.YHPortalLogic;
import yh.core.funcs.setdescktop.syspara.logic.YHSysparaLogic;
import yh.core.funcs.setdescktop.userinfo.logic.YHUserinfoLogic;
import yh.core.funcs.system.interfaces.data.YHInterFaceCont;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHRegistUtility;
import yh.core.util.form.YHFOM;
import yh.oa.tools.StaticData;

public class YHSysparaAct {
  
  public String queryLogoutText(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      String data = logic.queryLogoutText(dbConn);
      data = "\"" + data.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "") + "\"";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryStatusText(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      String text = logic.queryStatusText(dbConn);
      String marquee = logic.queryStatusMarquee(dbConn);
      
      if (text == null) {
        text = "";
      }
      
      if (marquee == null) {
        marquee = "";
      }
      
      StringBuffer sb = new StringBuffer("{\"TEXT\":\"");
      sb.append(text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r\n", "<br>"));
      sb.append("\",\"MARQUEE\":\"");
      sb.append(marquee);
      sb.append("\"}");
      
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String queryHeaderImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    
    int styleIndex = 1;
    Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
    if (styleInSession != null) {
      styleIndex = styleInSession;
    }
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      Map<String,String> map = logic.queryHeaderImg(dbConn);
      
      String path = YHInterFaceCont.ATTA_PATH + File.separator + "system" + File.separator  + map.get("id") + File.separator + map.get("name");
      String newPath = path.replaceAll("\\..*", map.get("width") + "-" + map.get("height") + ".jpg");
      //map.put("path", path.replace("\\", "\\\\"));
      //String data = this.toJson(map);
      
      YHImageUtil iu = new YHImageUtil();
      
      if (!new File(path).exists()) {
        return "/core/styles/style" + styleIndex + "/img/banner/logo_bg.jpg";
      }
      if (!new File(newPath).exists()) {
        try {
          iu.saveImageAsUser(path, newPath, Integer.parseInt(map.get("width")), Integer.parseInt(map.get("height")));
        } catch (NumberFormatException e) {
          iu.saveImageAsUser(path, newPath, 300, 50);
        }
      }
      
      FileInputStream fis = new FileInputStream(newPath);
      response.setContentType("image/" + map.get("name").replaceAll(".*\\.", ""));
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
      return "/core/styles/style" + styleIndex + "/img/banner/logo_bg.jpg";
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "";
  }
  
  public String queryUserCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSysparaLogic logic = new YHSysparaLogic();
      int count = logic.queryUserCount(dbConn);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, count + "");
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询天气的方法,解决ajax跨域的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryWeather(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String id = request.getParameter("cityId");
      if (YHUtility.isNullorEmpty(id)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "ID错误");
        return "/core/inc/rtjson.jsp";
      }
      String weatherPath = "http://m.weather.com.cn/data/" + id + ".html";
      HttpClient client = new HttpClient();
      GetMethod getMethod = new GetMethod(weatherPath);
      //设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
      getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
              new DefaultHttpMethodRetryHandler()); 
      //执行getMethod
      int statusCode = client.executeMethod(getMethod);
      if (statusCode != HttpStatus.SC_OK) {
        //System.err.println("Method failed: " + getMethod.getStatusLine());
      }
      byte[] responseBody = getMethod.getResponseBody();
      getMethod.releaseConnection();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, new String(responseBody, "utf-8"));
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  private Map<String, String> registInfo() {
    Map<String, String> map = new HashMap<String, String>();
    String hasReg = "1";
    if (!YHRegistUtility.hasRegisted()) {
      hasReg = "0";
      int remainDays = YHRegistUtility.remainDays();
      map.put("remainDays", String.valueOf(remainDays));
    }
    map.put("hasRegisted", hasReg);
    return map;
  }
  
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
      
      String portal = portalLogic.listPorts(dbConn, user, id);
      String title = systemLogic.getIETitle(dbConn);
      if (YHUtility.isNullorEmpty(title)) {
        title =StaticData.SOFTTITLE;
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
      sb.append(",\"background\":");
      sb.append(map.get("desktopBg"));
      sb.append(",\"portal\":");
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
  
  private String toJson(Map<String,String> m) throws Exception {
    StringBuffer sb = new StringBuffer("{");
    for (Iterator<Entry<String,String>> it = m.entrySet().iterator(); it.hasNext();){
      Entry<String,String> e = it.next();
      sb.append(e.getKey());
      sb.append(":\"");
      sb.append(e.getValue());
      sb.append("\",");
    }
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
  
  private void trimStringMap(Map<String, String> map) {
    for (String s : map.keySet()) {
      String value = map.get(s);
      if (value != null) {
        map.put(s, map.get(s).trim());
      }
    }
  }
}