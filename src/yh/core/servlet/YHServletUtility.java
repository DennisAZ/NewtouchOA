package yh.core.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.global.YHBeanKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class YHServletUtility {
  private static List<String> needNoCheckUriList = new ArrayList<String>();
  private static List<String> gbkCodeUriList = new ArrayList<String>();
  
  /**
   * 设置不需要检查的URI列表
   * @param uriStr
   */
  public static void setNeedNoCheckUriList(String uriStr) {
    if (YHUtility.isNullorEmpty(uriStr)) {
      return;
    }
    String[] uriList = uriStr.split(",");
    for (int i = 0; i < uriList.length; i++) {
      String uri = uriList[i].trim();
      if (!needNoCheckUriList.contains(uri)) {
        needNoCheckUriList.add(uri);
      }
    }
  }
  
  /**
   * 设置需要GBK编码的URI列表
   * @param uriStr
   */
  public static void setNeedGbkCodeUriList(String uriStr) {
    if (YHUtility.isNullorEmpty(uriStr)) {
      return;
    }
    String[] uriList = uriStr.split(",");
    for (int i = 0; i < uriList.length; i++) {
      String uri = uriList[i].trim();
      if (!gbkCodeUriList.contains(uri)) {
        gbkCodeUriList.add(uri);
      }
    }
  }
  
  /**
   * 设置不需要检查的URI列表
   * @param uriStr
   */
  public static void resetNeedNoCheckUriList(String uriStr) {
    needNoCheckUriList.clear();
    setNeedNoCheckUriList(uriStr);
  }
  
  /**
   * 设置需要GBK编码的URI列表
   * @param uriStr
   */
  public static void resetNeedGBKCodeUriList(String uriStr) {
    gbkCodeUriList.clear();
    setNeedGbkCodeUriList(uriStr);
  }
  
  /**
   * 传递请求
   * @param toUrl                        传递的URL地址
   * @param request                      请求
   * @param response                     回复
   * @throws ServletException
   * @throws IOException
   */
  public static void forward(String toUrl, HttpServletRequest request,
      HttpServletResponse response) throws ServletException,
      IOException {
    RequestDispatcher requestDispatcher =
        request.getRequestDispatcher(toUrl);
    requestDispatcher.forward(request, response);
  }
  
  /**
   * 
   * @param requestUri
   * @return
   */
  public static boolean isLoginAction(HttpServletRequest request) {
    if (request == null) {
      return false;
    }
    String qUri = request.getRequestURI();
    String contextPath = request.getContextPath();
    if (contextPath.equals("")) {
      contextPath = "/yh";
    }
    
    if (qUri.endsWith("/")) {
      qUri += "index.jsp";
    }
    if (!qUri.startsWith(contextPath)) {
      qUri = contextPath + qUri;
    }
    if (qUri.equals(contextPath + "/login.jsp")
        || qUri.equals(contextPath + "/index.jsp")
        || qUri.equals(contextPath + "/logincheck.jsp")
        || qUri.equals(contextPath + "/core/inc/sessionerror.jsp")
        || qUri.equals(contextPath + "/core/inc/pagenevbar.jsp")
        || qUri.startsWith(contextPath + "/yh/core/funcs/system/act/YHSystemAct/")
        || qUri.startsWith(contextPath + "/yh/core/funcs/system/info/act/YHInfoAct/")
        || qUri.startsWith(contextPath + "/core/funcs/system/info/")) {
      
      return true;
    }
    if (!YHUtility.isNullorEmpty(YHSysProps.getString("NEED_NOT_CHECKED_URI"))) {
      if (needNoCheckUriList.size() < 1) {
        setNeedNoCheckUriList(YHSysProps.getString("NEED_NOT_CHECKED_URI"));
      }
      for (int i = 0; i < needNoCheckUriList.size(); i++) {
        String uri = needNoCheckUriList.get(i);
        if (qUri.startsWith(contextPath + uri)) {
          return true;
        }
      }
    }
   
    return false;
  }
  
  /**
   * 是否需要GBK编码
   * @param request
   * @return
   */
  public static boolean isGbkCode(HttpServletRequest request) {
    if (request == null) {
      return false;
    }
    
    if (gbkCodeUriList.size() > 0 || !YHUtility.isNullorEmpty(YHSysProps.getString("NEED_GBKCODE_URI"))) {
      if (gbkCodeUriList.size() < 1) {
        setNeedGbkCodeUriList(YHSysProps.getString("NEED_GBKCODE_URI"));
      }
      
      String qUri = request.getRequestURI();
      String contextPath = request.getContextPath();
      if (contextPath.equals("")) {
        contextPath = "/yh";
      }
      
      if (qUri.endsWith("/")) {
        qUri += "index.jsp";
      }
      if (!qUri.startsWith(contextPath)) {
        qUri = contextPath + qUri;
      }
      for (int i = 0; i < gbkCodeUriList.size(); i++) {
        String uri = gbkCodeUriList.get(i);
        if (qUri.startsWith(contextPath + uri)) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * 验证Session的有效性
   * @param session        用户上下文会话
   * @param key            检查参数的键名
   * @return               true=在上下文会话中存在以key为名称的对象
   *                       false=在上下文会话中不存在以key为名称的对象
   */
  public static boolean isValidSession(HttpSession session, String key) {
    if (session == null || key == null) {
      return false;
    }
    if (session.getAttribute(key) == null) {
      return false;
    }
    return true;
  }

  
  /**
   * 取得cookie的值
   * @param request
   * @param cookieName
   * @param defaultValue
   * @return
   */
  public static String getCookieValue(HttpServletRequest request,
      String cookieName) throws Exception {   
    return getCookieValue(request, cookieName, "");
  }
  
  /**
   * 取得cookie的值
   * @param request
   * @param cookieName
   * @param defaultValue
   * @return
   */
  public static String getCookieValue(HttpServletRequest request,
      String cookieName,
      String defaultValue) throws Exception {

    Cookie[] cookies = request.getCookies();    
    return getCookieValue(cookies, cookieName, defaultValue);
  }
  
  /**
   * 取得cookie的值
   * @param cookies
   * @param cookieName
   * @param defaultValue
   * @return
   */
  public static String getCookieValue(Cookie[] cookies,
      String cookieName,
      String defaultValue) throws Exception {
    
    if (cookies == null || cookieName == null) {
      return null;
    }
    
    for (int i = 0; i < cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (cookieName.equals(cookie.getName())) {
        return URLDecoder.decode(cookie.getValue(), "GBK");
      }
    }
    return defaultValue;
  }
  
  /**
   * 添加Cookie
   * @param name
   * @param value
   * @param leavSeconds
   * @param response
   */
  public static void addCookie(String name,
      String value,
      int leafSeconds,
      HttpServletResponse response) throws Exception {
    
    Cookie cookie = new Cookie(name, URLEncoder.encode(value, "GBK"));
    cookie.setMaxAge(leafSeconds);
    response.addCookie(cookie);
  }
  
  /**
   * 取得参数哈希表
   * @param request
   * @return
   */
  public static Map<String, String> getParamMap(HttpServletRequest request) {
    Map<String, String> rtMap = new HashMap<String, String>();
    Map<String, String[]> paramMap = request.getParameterMap();
    
    Iterator<String> iKeys = paramMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      String[] value = paramMap.get(key);
      if (value != null && value.length > 0) {
        rtMap.put(key, value[0]);
      }else {
        rtMap.put(key, "");
      }
    }
    
    return rtMap;
  }
  
  /**
   * 取得请求中的参数，构成Url参数串
   * @param request     Http请求对象
   * @return
   */
  public static String getUrlParams(HttpServletRequest request) throws Exception {    
    return getUrlParams(request, null);
  }
  
  /**
   * 取得请求中的参数，构成Url参数串
   * @param request     Http请求对象
   * @return
   */
  public static String getUrlParams(HttpServletRequest request,
      ArrayList exKeyList) throws Exception {
    
    if (exKeyList == null) {
      exKeyList = new ArrayList();
    }
    
    StringBuffer params = new StringBuffer();
    Enumeration eParamName = request.getParameterNames();
    while (eParamName.hasMoreElements()) {
      String paramName = (String)eParamName.nextElement();
      if (exKeyList.contains(paramName)) {
        continue;
      }
      String paramValue = YHUtility.iso88591ToGbk(
          request.getParameter(paramName));
      params.append(paramName);
      params.append("=");
      params.append(paramValue);
      params.append("&");
    }
    if (params.length() > 1) {
      params.delete(params.length() - 1, params.length());
    }
    return params.toString();
  }
  
  /**
   * 创建一个实例
   * @param className
   * @return
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public static Object applicationInstance(String className)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
  
    return (applicationClass(className).newInstance());
  
  }
  
  /**
   * 创建一个类
   * @param className
   * @return
   * @throws ClassNotFoundException
   */
  public static Class applicationClass(String className) throws ClassNotFoundException {

      // Look up the class loader to be used
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        classLoader = YHServletUtility.class.getClassLoader();
      }

      // Attempt to load the specified class
      return (classLoader.loadClass(className));

  }
  
  /**
   * 以消息参数为消息名称取得本地化的消息
   * @param servletContext
   * @param request
   * @param rscId
   * @param msrg
   * @return 如果在i18文件中如果存在，那么则返回本地化消息，否则返回原来的消息
   */
  public static String getLocaleMsrg(
      ServletContext servletContext,
      HttpServletRequest request,
      String rscId,
      String msrg) {

//    if (YHUtility.isNullorEmpty(msrg)) {
//      return "";
//    }
//    MessageResources messages = null;
//    if (YHUtility.isNullorEmpty(rscId)) {
//      messages = (MessageResources)servletContext.getAttribute("org.apache.struts.action.MESSAGE");
//    }else {
//      messages = (MessageResources)servletContext.getAttribute(rscId);
//    }
//    Locale locale = RequestUtils.getUserLocale(request, null);
//    if (messages == null) {
//      return msrg;
//    }
//    String localeMsrg = messages.getMessage(locale, msrg);
//    if (localeMsrg != null) {
//      return localeMsrg;
//    }
//    if (YHUtility.isNullorEmpty(rscId)) {
//      messages = (MessageResources)servletContext.getAttribute("opts");
//      if (messages != null) {
//        localeMsrg = messages.getMessage(locale, msrg);
//      }
//    }
//    if (localeMsrg == null) {
//      localeMsrg = msrg;
//    }
//    return localeMsrg;
    return "";
  }
  
  /**
   * 取得Web应用的安装目录
   * @param context
   * @return
   */
  public static String getWebAppDir(ServletContext context) {
    return context.getRealPath("/");
  }
  
  /**
   * 取得Web应用所在的盘符
   * @param context
   * @return
   */
  public static String getWebAppDiskPart(ServletContext context) {
    return getWebAppDir(context).substring(0, 3);
  }
  
  /**
   * 取得参数
   * @param request
   * @param paramName
   * @param defalutValue
   * @return
   */
  public static String getParam(HttpServletRequest request, String paramName, String defalutValue) {
    String paramValue = null;
    //新传递的参数优先级最高，目的是防止参数覆盖
    Map paramMapInner = (Map)request.getAttribute(YHBeanKeys.PARAM_MAP_INNER);
    if (paramMapInner != null) {
      paramValue = (String)paramMapInner.get(paramName);
      if (!YHUtility.isNullorEmpty(paramValue)) {
        return paramValue;
      }
    }
    paramValue = request.getParameter(paramName);
    if (!YHUtility.isNullorEmpty(paramValue)) {
      return paramValue;
    }        
    paramValue = (String)request.getAttribute(paramName);
    if (!YHUtility.isNullorEmpty(paramValue)) {
      return paramValue;
    }
    return defalutValue;
  }
  /**
   * 取得参数
   * @param request
   * @param paramName
   * @return
   */
  public static String getParam(HttpServletRequest request, String paramName) {
    return getParam(request, paramName, "");
  }
}
