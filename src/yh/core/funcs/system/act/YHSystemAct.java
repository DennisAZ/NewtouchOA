package yh.core.funcs.system.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oa.spring.util.OpenAccount;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.psit.struts.entity.LimUser;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHSecureKey;
import yh.core.funcs.person.data.YHUserOnline;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHSecureCardLogic;
import yh.core.funcs.system.act.adapter.YHLoginAdapter;
import yh.core.funcs.system.act.filters.YHBindIpValidator;
import yh.core.funcs.system.act.filters.YHExistUserValidator;
import yh.core.funcs.system.act.filters.YHForbidLoginValidator;
import yh.core.funcs.system.act.filters.YHInitialPwValidator;
import yh.core.funcs.system.act.filters.YHIpRuleValidator;
import yh.core.funcs.system.act.filters.YHPasswordValidator;
import yh.core.funcs.system.act.filters.YHPwExpiredValidator;
import yh.core.funcs.system.act.filters.YHRepeatLoginValidator;
import yh.core.funcs.system.act.filters.YHRetryLoginValidator;
import yh.core.funcs.system.act.filters.YHUsbkeyValidator;
import yh.core.funcs.system.act.filters.YHVerificationCodeValidator;
import yh.core.funcs.system.data.YHLoginUsers;
import yh.core.funcs.system.data.YHMenu;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.funcs.system.security.data.YHSecurity;
import yh.core.funcs.system.security.logic.YHSecurityLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.oa.tools.StaticData;

public class YHSystemAct {
  private static String sp = System.getProperty("file.separator");
  private static Logger log = Logger.getLogger(YHSystemAct.class);
  //定义菜单图片的文件夹路径常量
  public final static String IMAGE_PATH = "/core/styles/imgs/menuIcon/";
  public final static String WEBOS_IMAGE_PATH = YHSysProps.getString(YHSysPropKeys.ROOT_DIR) + sp +
                    YHSysProps.getString(YHSysPropKeys.WEB_ROOT_DIR) + sp +
                    YHSysProps.getString(YHSysPropKeys.JSP_ROOT_DIR) + sp +
                    "core" + sp +
                		"frame" + sp +
                		"webos" + sp +
                		"styles" + sp +
                		"icons" + sp;
  public final static String DEFAULT_PATH = "/core/funcs/";
//  public final static String HOME_CLASSIC = "/core/frame/2/index.jsp";
  public final static String HOME_CLASSIC = "/yhindex.jsp";
  public final static String HOME_WEBOS = "/core/frame/webos/index.jsp";
  public final static String HOME_TDOA = "/core/frame/5/index.jsp";
  
  /**
   * 系统登录方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String prepareLoginIn(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSystemLogic logic = new YHSystemLogic();
      Map<String,String> map = logic.getSysPara(dbConn);
      
      int loginKey = 0;
      int keyUser = 0;
      int verificationCode = 0;
      try {
        loginKey = Integer.parseInt(map.get("LOGIN_KEY"));
      } catch (Exception e) {
        
      }
      
      try {
        keyUser = Integer.parseInt(map.get("SEC_KEY_USER"));
      } catch (Exception e) {
        
      }
      
      try {
        verificationCode = Integer.parseInt(map.get("VERIFICATION_CODE"));
      } catch (Exception e) {
        
      }
      
      String title = logic.getIETitle(dbConn);
      
      if (YHUtility.isNullorEmpty(title)) {
        title = StaticData.SOFTTITLE;
      }
      
      request.setAttribute("ieTitle", title);
      request.setAttribute("secKeyUser", String.valueOf(keyUser));
      request.setAttribute("useUsbKey", String.valueOf(loginKey));
      request.setAttribute("verificationCode", String.valueOf(verificationCode));
    } catch (Exception ex) {
      request.setAttribute("secKeyUser", "0");
      request.setAttribute("useUsbKey", "0");
      request.setAttribute("verificationCode", "0");
    }
    return "/login.jsp";
  }
  
  /**
   * 系统登录方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String prepareLoginInIM(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    prepareLoginIn(request, response);
    return "/core/frame/ispirit/login.jsp";
  }
  
  /**
   * 系统登录方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String prepareLoginInWebos(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    prepareLoginIn(request, response);
    return "/login2.jsp";
  }
  
  /**
   * 系统登录方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doLoginIn(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSystemLogic logic = new YHSystemLogic();
      Map<String,String> map = logic.getSysPara(dbConn);
      String keyUser = YHUtility.null2Empty(request.getParameter("KEY_USER"));
      String caUser = YHUtility.null2Empty(request.getParameter("CA_USER"));
         System.out.println(map);
      /**
       * OA公文交换单点登录---》不需要校验密码处理-----syl
       */
      String GW_JH_TYPE = request.getParameter("GW_JH_TYPE") == null ? "" :request.getParameter("GW_JH_TYPE");
      /**
       * 结束----修改下面密码验证代码处理
       */
      
      
      String userName = request.getParameter("userName");
      if (YHUtility.isNullorEmpty(userName) && !YHUtility.isNullorEmpty(keyUser)) {
        userName = keyUser;
      }
      
      YHPerson person = null;
      if (YHUtility.isNullorEmpty(userName)) {
        userName = "";
      }
      else {
        person = logic.queryPerson(dbConn, userName);
      }
      
      String pwd = request.getParameter("pwd");
      //是否开启动态密码卡验证
      YHSecurityLogic orgLogic = new YHSecurityLogic();
      YHSecurity security  = orgLogic.getSecritySecureKey(dbConn);
      //该用户是否绑定动态密码卡
      YHSecureCardLogic secureCardLogic = new YHSecureCardLogic();
      YHSecureKey secureKey = secureCardLogic.getKeyInfo(dbConn , person);
      
      if("1".equals(security.getParaValue()) && secureKey != null) {
        if(request.getParameter("pwd").length() > 6){
          pwd = request.getParameter("pwd").substring(0, request.getParameter("pwd").length() - 6);
        }
      }
      else{
        pwd = request.getParameter("pwd");
      }
//      if (pwd == null) {
//        pwd = null;
//      }
      
      String useingKey = "";
      if ("1".equals(map.get("LOGIN_KEY")) && person != null) {
        useingKey = YHUtility.null2Empty(person.getUseingKey());
      }
      
      if (!"1".equals(useingKey.trim())) {
        userName = request.getParameter("userName");
        if (userName == null) {
          userName = "";
        }
      }
     
      if (YHUtility.isNullorEmpty(userName)) {
        userName = "";
      }
      else {
        //如果用户禁止usbkey登陆,用户名必须从form表单中取
        person = logic.queryPerson(dbConn, userName);
      }
      //登录验证
      YHLoginAdapter loginAdapter = new YHLoginAdapter(request, person);
    /*  //验证软件是否过期
      if (!loginAdapter.validate(new YHSoftwareExpiredValidator())){
        return "/core/inc/rtjson.jsp";
      }*/
      //验证用户是否存在
      if (!loginAdapter.validate(new YHExistUserValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      //验证Ip规则
      if (!loginAdapter.validate(new YHIpRuleValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      //验证用户绑定Ip
      if (!loginAdapter.validate(new YHBindIpValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      //验证用户是否禁止登陆
      if (!loginAdapter.validate(new YHForbidLoginValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      //验证用户是否重复登陆
      if (!loginAdapter.validate(new YHRepeatLoginValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      if("1".equals(useingKey.trim()) && "1".equals(map.get("LOGIN_KEY"))){
        
        //当用户使用Usbkey登陆时,进行usbkey验证
        if (!loginAdapter.validate(new YHUsbkeyValidator())){
          return "/core/inc/rtjson.jsp";
        }
      }
      else if(caUser != null && "1".equals(caUser.trim())){
        //如果是ca用户则放行，默认其合法
      }
      else {
        if (!loginAdapter.validate(new YHVerificationCodeValidator())){
          return "/core/inc/rtjson.jsp";
        }
        
        
        
        
        /**
         * OA公文交换单点登录---》不需要校验密码处理-----syl
         */
       //当用户正常登陆时,验证密码是否正确---原来的
      /*  if (!loginAdapter.validate(new YHRetryLoginValidator())){
          return "/core/inc/rtjson.jsp";
        }
        
        if (!loginAdapter.validate(new YHPasswordValidator())){
          return "/core/inc/rtjson.jsp";
        }*/
       if(GW_JH_TYPE.equals("1")){
          
        }else{
          if (!loginAdapter.validate(new YHRetryLoginValidator())){
            return "/core/inc/rtjson.jsp";
          }
          
          if (!loginAdapter.validate(new YHPasswordValidator())){
            return "/core/inc/rtjson.jsp";
          }
        }
      }
        /**
         * end
         */
        
      
      //调用登陆成功的处理      this.loginSuccess(dbConn, person, request, response);
      
      try {
        Cookie cookie = reportSSO(person, pwd, request);
        if (cookie != null) { 
          response.addCookie(cookie);
        }
        
        Cookie cookieOA = reportOA(person, pwd, request);
        if (cookieOA != null) { 
          response.addCookie(cookieOA);
        }
      }catch(Exception ex) {
      }

      //验证密码是否过期,是否需要修改      if (!loginAdapter.validate(new YHPwExpiredValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      //验证时候需要修改初始密码
      if (!loginAdapter.validate(new YHInitialPwValidator())){
        return "/core/inc/rtjson.jsp";
      }
      
      String menuType = person.getMenuType();
      String saveUserName = map.get("SEC_USER_MEM");
      
      if (menuType == null || "".equals(menuType.trim())) {
        menuType = "1";
      }
      
      if (saveUserName == null || "".equals(saveUserName.trim())) {
        saveUserName = "1";
      }
      
      Map<String, String> rtMap = new HashMap<String, String>();
      rtMap.put("saveUserName", saveUserName);
      rtMap.put("menuType", menuType);
      rtMap.put("userName", person.getUserName());
      rtMap.put("deptId", String.valueOf(person.getDeptId()));
      rtMap.put("seqId", String.valueOf(person.getSeqId()));
      String classic = (String) YHFOM.json2Map(person.getParamSet()).get("classicHome");
      if ("1".equals(classic)) {
        rtMap.put("homeAddress", HOME_CLASSIC);
      }
      else if ("0".equals(classic)) {
        rtMap.put("homeAddress", HOME_WEBOS);
      }
      else if("2".equals(classic)) {
    	  rtMap.put("homeAddress", HOME_TDOA);
      }
      else {
        String style = map.get("DEFAULT_INTERFACE_STYLE");
        if ("1".equals(style)) {
          rtMap.put("homeAddress", HOME_WEBOS);
        }
        else if("2".equals(style)) {
        	rtMap.put("homeAddress", HOME_TDOA);
        }
        else {
          rtMap.put("homeAddress", HOME_CLASSIC);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(rtMap).toString());
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 系统登录方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doLoginIn2(HttpServletRequest request,
      HttpServletResponse response,String userName,String pwd) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSystemLogic logic = new YHSystemLogic();
      Map<String,String> map = logic.getSysPara(dbConn);
      /**
       * 结束----修改下面密码验证代码处理
       */
      YHPerson person = null;
        person = logic.queryPerson(dbConn, userName);
      //调用登陆成功的处理
      this.loginSuccess(dbConn, person, request, response);
      
      try {
        Cookie cookie = reportSSO(person, pwd, request);
        if (cookie != null) { 
          response.addCookie(cookie);
        }
        
        Cookie cookieOA = reportOA(person, pwd, request);
        if (cookieOA != null) { 
          response.addCookie(cookieOA);
        }
      }catch(Exception ex) {
      }
      String menuType = person.getMenuType();
      String saveUserName = map.get("SEC_USER_MEM");
      
      if (menuType == null || "".equals(menuType.trim())) {
        menuType = "1";
      }
      
      if (saveUserName == null || "".equals(saveUserName.trim())) {
        saveUserName = "1";
      }
      
      Map<String, String> rtMap = new HashMap<String, String>();
      rtMap.put("saveUserName", saveUserName);
      rtMap.put("menuType", menuType);
      rtMap.put("userName", person.getUserName());
      rtMap.put("deptId", String.valueOf(person.getDeptId()));
      rtMap.put("seqId", String.valueOf(person.getSeqId()));
      String classic = (String) YHFOM.json2Map(person.getParamSet()).get("classicHome");
      if ("1".equals(classic)) {
        rtMap.put("homeAddress", HOME_CLASSIC);
      }
      else if ("0".equals(classic)) {
        rtMap.put("homeAddress", HOME_WEBOS);
      }
      else if("2".equals(classic)) {
    	  rtMap.put("homeAddress", HOME_TDOA);
      }
      else {
        String style = map.get("DEFAULT_INTERFACE_STYLE");
        if ("1".equals(style)) {
          rtMap.put("homeAddress", HOME_WEBOS);
        }
        else if("2".equals(style)) {
        	rtMap.put("homeAddress", HOME_TDOA);
        }
        else {
          rtMap.put("homeAddress", HOME_CLASSIC);
        }
      }
    } catch (Exception ex) {
      throw ex;
    }
    return null;
  }
  
  /**
   * 登录成功的处理   * @param conn
   * @param person
   * @param request
   * @throws Exception
   */
  private void loginSuccess(Connection conn, YHPerson person, HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    //获取用户当前的session,如果不存在就生成一个新的session
    HttpSession session = request.getSession(true);
    YHSystemLogic logic = new YHSystemLogic();
    logic.updateLastVisitInfo(conn, person.getSeqId(), request.getRemoteAddr());
    //记录登陆的时间
    person.setLastVisitTime(new Date());
    
    //判断用户是否已经登录
    if (session.getAttribute("LOGIN_USER") == null){
      //添加登陆成功的系统日志      YHSysLogLogic.addSysLog(conn, YHLogConst.LOGIN, "登录成功", person.getSeqId(), request.getRemoteAddr());
      
      setUserInfoInSession(person, session, request.getRemoteAddr(), request);
      this.addOnline(conn, person, String.valueOf(session.getAttribute("sessionToken")));
    }else {
      YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
      //如果是新用户登录时,销毁原有的session
      if (loginPerson.getSeqId() != person.getSeqId()){
        //销毁session
        session.invalidate();
        //重新调用登录成功的处理
        loginSuccess(conn, person, request, response);
      }
    }
    
    //登录后检查今天是否需要提醒短信，
    //如果有则提醒，并且修改最后一次提醒时间为今天(周期性事物)
    try {
      Class<?> classObj = Class.forName("yh.core.funcs.calendar.act.YHAffairAct");
      Class<?>[] paramTypeArray = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
      Method methodObj = classObj.getMethod("selectAffairRemindByToday", paramTypeArray);
      methodObj.invoke(classObj.newInstance(), new Object[]{request, response});
      
    } catch (ClassNotFoundException e){
      
    } catch (Exception e){
      
    }
  }
  
  /**
   * 单点登录到报表系统   * @param person
   * @param originalPw
   * @param request
   * @return
   */
  private Cookie reportSSO(YHPerson person, String originalPw, HttpServletRequest request) {
    String port = YHSysProps.getProp("REPORT_SSO_PORT");
    //获取不到REPORT_SSO_PORT参数则不执行单点登录
    if (YHUtility.isNullorEmpty(port)) {
      return null;
    }
    String url = request.getRequestURL().toString();
    if (YHUtility.isNullorEmpty(url)) {
      return null;
    }
    String host = url.substring(0, url.indexOf("/yh/yh/"));
    if (YHUtility.isNullorEmpty(host)) {
      return null;
    }
    //避免http://的冒号
    int tmpIndex = host.indexOf(":", 5);
    if (tmpIndex > 0) {
      host = host.substring(0, tmpIndex);
    }
    
    return simpleSSO(person, originalPw, port, "/logincheck.php");
  }
  
  /**
   * 单点登录到OA系统
   * @param person
   * @param originalPw
   * @param request
   * @return
   */
  private Cookie reportOA(YHPerson person, String originalPw, HttpServletRequest request) {
    String port = YHSysProps.getProp("OA_SSO_PORT");
    //获取不到REPORT_SSO_PORT参数则不执行单点登录
    if (YHUtility.isNullorEmpty(port)) {
      return null;
    }
    String url = request.getRequestURL().toString();
    if (YHUtility.isNullorEmpty(url)) {
      return null;
    }
    String host = url.substring(0, url.indexOf("/yh/yh/"));
    if (YHUtility.isNullorEmpty(host)) {
      return null;
    }
    //避免http://的冒号
    int tmpIndex = host.indexOf(":", 5);
    if (tmpIndex > 0) {
      host = host.substring(0, tmpIndex);
    }
    
    return simpleSSO(person, originalPw, port, "/logincheck.php");
  }
  
  private Cookie simpleSSO(YHPerson person, String originalPw, String port, String loginUrl) {
    HttpClient httpclient = new HttpClient();
    String ssoUrl = "http://127.0.0.1" + ":" + port + loginUrl;
    PostMethod method = new PostMethod(ssoUrl);
    method.addParameter("USERNAME", person.getUserId());
    method.addParameter("PASSWORD", originalPw);
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
        new DefaultHttpMethodRetryHandler()); 
    method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, 
        "gbk");
    try {
      httpclient.executeMethod(method);
      int statusCode = httpclient.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK) {
        return null;
      }
      for (org.apache.commons.httpclient.Cookie c : httpclient.getState().getCookies()) {
        if ("PHPSESSID".equals(c.getName())) {
          Cookie cookie = new Cookie(c.getName(), c.getValue());
          cookie.setPath("/");
          return cookie;
        }
      }
    } catch (HttpException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return null;
  }
  
  /**
   * 在session中设置用户信息   * @param person
   * @param session
   */
  public void setUserInfoInSession(YHPerson person, HttpSession session, String ip, HttpServletRequest request) throws Exception {
    
    String sessionToken = YHGuid.getRawGuid();
    session.setAttribute("LOGIN_USER", person);
    session.setAttribute("sessionToken", sessionToken);
    session.setAttribute("LOGIN_IP", ip);
    session.setAttribute("STYLE_INDEX", getStyleIndex(request));
    /*crm session 使用*/
    LimUser lmu=new LimUser();
    lmu.setPerson(person);
    session.setAttribute("CUR_USER", lmu);
    
    String lockSecStr = YHSysProps.getString("$OFFLINE_TIME_MIN");
    Long lockSec = null;
    try {
      lockSec = Long.valueOf(Integer.parseInt(lockSecStr) * 60 * 1000);
    } catch (Exception e) {
      lockSec = Long.valueOf(0);
    }
    session.setAttribute("OFFLINE_TIME_MIN", lockSec);
  }

  /**
   * 退出登录   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String doLogout(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null){
      session.invalidate();
    }
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取ietitle
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getIeTitle(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHSystemLogic logic = new YHSystemLogic();
      String title = logic.getIETitle(dbConn);
      if (YHUtility.isNullorEmpty(title)) {
        title = StaticData.SOFTTITLE;
      }
      
      title = "\"" + title + "\"";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, title);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取系统菜单(未完成应用)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listMenu(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    Connection dbConn = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      List<String> menuSet = YHSystemAct.listUserMenu(dbConn, person);
      YHSystemLogic logic = new YHSystemLogic();
      //1.一次展开一个一级菜单      int type = logic.queryMenuExpandType(dbConn);
      
      StringBuffer sb = new StringBuffer("{expandType:");
      sb.append(type);
      sb.append(",menu:[");
      String menuStr = menuSet.toString();
      
      for (String id : menuSet){
        if (id.length() == 2){
          YHMenu menu = logic.queryMenu(dbConn, Integer.parseInt(id));
          if (menu == null){
            continue;
          }
          //判断是否有下级节点          if (!menuStr.matches(".*[ ,\\[]" + menu.getId() + "\\d+.*")){
            menu.setLeaf(1);
          }
          String menuExpand = person.getMenuExpand();
          
          if (menuExpand != null && id.equals(menuExpand.trim())){
            menu.setExpand(1);
          }
          sb.append(YHFOM.toJson(menu));
          sb.append(",");
          
        }
      }
      
      if (sb.charAt(sb.length() - 1) == ','){
        sb.deleteCharAt(sb.length() - 1);
      }
      
      sb.append("]}");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 惰性加载菜单   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String lazyLoadMenu(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    
    String parent = request.getParameter("parent");
    
    Connection dbConn = null;
    try {
      //在获取二级三级菜单时,没有传递一级菜单id返回异常信息
      if (parent == null || "".equals(parent)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递上级菜单id");
        return "/core/inc/rtjson.jsp";
      }
      
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      //获取当前用户有权限查看的菜单List
      List<String> menuSet = YHSystemAct.listUserMenu(dbConn, person);
      if (!menuSet.contains(parent)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "该目录无访问权限");
        return "/core/inc/rtjson.jsp";
      }
      
      StringBuffer sb = lazyLoadJson(menuSet, dbConn, parent, request.getContextPath(), request);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 返回所有用户有权限的菜单List
   * 其中包括用户辅助角色有权限的菜单
   * @param dbConn
   * @param person
   * @return
   * @throws Exception
   */
  public static List<String> listUserMenu(Connection dbConn, YHPerson person) throws Exception{
    String privOther = "" ; 
    if (person  != null) {
      privOther = person.getUserPrivOther();
    }
    
    YHORM t = new YHORM();
    
    List<YHUserPriv> priv = new ArrayList<YHUserPriv>();
    
    if (privOther != null && !"".equals(privOther.trim())){
      privOther = privOther.trim();
      for (String s : privOther.split(",")){
        s = s.trim();
        if("".equals(s.trim())){
          continue;
        }
        Map<String,Integer> query = new HashMap<String,Integer>();
        query.put("SEQ_ID", Integer.parseInt(s));

        YHUserPriv up = (YHUserPriv) t.loadObjSingle(dbConn, YHUserPriv.class,
            query);
        
        if (up != null){
          priv.add(up);
        }
        
      }
    }
    
    Map<String,String> query = new HashMap<String,String>();
    
    String userPriv = "" ; 
    if (person  != null) {
      userPriv = person.getUserPriv();
    }
    
    if (userPriv == null || "".equals(userPriv.trim())){
      userPriv = "";
    }
    
    query.put("SEQ_ID", userPriv.trim());

    YHUserPriv up = (YHUserPriv) t.loadObjSingle(dbConn, YHUserPriv.class, query);

    if (up != null){
      priv.add(up);
    }
    
    //用户主角色/辅助角色的菜单去除重复
    HashSet<String> menuSet = new HashSet<String>();
    
    for (YHUserPriv p : priv){
      menuSet.addAll(Arrays.asList(YHUtility.null2Empty(p.getFuncIdStr()).split(",")));
    }
    
    HashSet<String> addSet = new HashSet<String>(menuSet);
    
    for (String s : menuSet){
      s = s.trim();
      if (s.length() > 4){
        addSet.add(s.substring(0, 4));
      }
      else if (s.length() > 2){
        addSet.add(s.substring(0, 2));
      }
    }
    
    List<String> list = new ArrayList<String>(addSet);
    //根据菜单的排序字段把菜单排序
    Collections.sort(list, new Comparator<String>(){
      public int compare(String arg0, String arg1) {
        if (YHUtility.isNullorEmpty(arg0)) {
          arg0 = "";
        }
        if (YHUtility.isNullorEmpty(arg1)) {
          arg1 = "";
        }
        return arg0.compareTo(arg1);
      }
      
    });
    
    return list;
  }
  
  /**
   * 生成菜单json数据的递归函数
   * @param menuSet
   * @param dbConn
   * @param parent
   * @param contextPath
   * @return
   * @throws Exception
   */
  private StringBuffer lazyLoadJson(List<String> menuSet, Connection dbConn,String parent, String contextPath, HttpServletRequest request) throws Exception{
    
    String menuStr = menuSet.toString();
    StringBuffer sb = new StringBuffer("[");
    String iconFolder = request.getParameter("iconFolder");
    
    for (String id : menuSet){
      if (id.matches(parent + "\\d{2}")){
        YHSystemLogic logic = new YHSystemLogic();
        YHMenu menu = logic.queryFunc(dbConn, Integer.parseInt(id));
        
        if (menu == null){
          continue;
        }
        if(OpenAccount.isOpenAccount()){
            if(!menu.getUrl().equals(oa.spring.util.StaticDataUtil.INIT_PAGE)){
            	 if (YHUtility.isNullorEmpty(iconFolder)) {
                     parseMenuIcon(menu);
                   }
                   else {
                     parseMenuIcon(menu, iconFolder);
                   }
                   //判断是否有下级节点
                   if (!menuStr.matches(".*[ ,\\[]" + menu.getId() + "\\d+.*")){
                     //没有下级菜单的情况,设置菜单为叶子节点
                     menu.setLeaf(1);
                     menu.setUrl(YHSystemAct.parseMenuUrl(menu.getUrl(), contextPath, request));
                     sb.append(YHFOM.toJson(menu));
                   }
                   else{
                     sb.append(YHFOM.toJson(menu));
                     sb.deleteCharAt(sb.length() - 1);
                     sb.append(",children:");
                     
                     //当存在下级节点时再次调用此函数,将下级菜单添加到children中

                     sb.append(lazyLoadJson(menuSet, dbConn, id, contextPath, request));
                     sb.append("}");
                   }
                   sb.append(",");
            }
        }
        else{
        	 if (YHUtility.isNullorEmpty(iconFolder)) {
                 parseMenuIcon(menu);
               }
               else {
                 parseMenuIcon(menu, iconFolder);
               }
               //判断是否有下级节点
               if (!menuStr.matches(".*[ ,\\[]" + menu.getId() + "\\d+.*")){
                 //没有下级菜单的情况,设置菜单为叶子节点
                 menu.setLeaf(1);
                 menu.setUrl(YHSystemAct.parseMenuUrl(menu.getUrl(), contextPath, request));
                 sb.append(YHFOM.toJson(menu));
               }
               else{
                 sb.append(YHFOM.toJson(menu));
                 sb.deleteCharAt(sb.length() - 1);
                 sb.append(",children:");
                 
                 //当存在下级节点时再次调用此函数,将下级菜单添加到children中

                 sb.append(lazyLoadJson(menuSet, dbConn, id, contextPath, request));
                 sb.append("}");
               }
               sb.append(",");
        }
      }
    }
    
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    
    sb.append("]");
    return sb;
  }
  
  /**
   * 设置用户在线
   * @param conn
   * @param person
   * @param sessionToken
   * @throws Exception
   */
  private void addOnline(Connection conn, YHPerson person, String sessionToken) throws Exception{
    YHUserOnline online = new YHUserOnline();
    
    online.setSessionToken(sessionToken);
    online.setLoginTime(new Date());
    online.setUserId(person.getSeqId());
    YHSystemLogic logic = new YHSystemLogic();
    Map<String,String> map = logic.getSysPara(conn);
    int state = logic.queryUserOnline(conn, person.getSeqId());
    if (state > 0){
      online.setUserState(String.valueOf(state));
      person.setOnStatus(String.valueOf(state));
    }
    else if ("0".equals(map.get("SEC_ON_STATUS"))){
      online.setUserState("1");
      person.setOnStatus("1");
    }
    else{
      if (person.getOnStatus() == null) {
        person.setOnStatus("1");
      }
      online.setUserState(person.getOnStatus());
    }
    
    logic.addOnline(conn, online);
  }
  
  /**
   * 登陆通元网站
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loginOtherSys(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    
    try {
      
      String bindOtherServer = YHSysProps.getString("BIND_USERS_OTHERS");
      
      if (bindOtherServer == null || !"1".equals(bindOtherServer)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "系统参数未设置绑定其他系统!");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      YHLoginUsers loginUsers = new YHLoginUsers();
      HttpSession session = request.getSession();
      YHSystemLogic logic = new YHSystemLogic();
      String userId = logic.queryBindId(dbConn, person.getSeqId());
      if (userId == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户未绑定其他系统账号!");
        return "/core/inc/rtjson.jsp";
      }
      
      String sessionToken = String.valueOf(session.getAttribute("sessionToken"));
      loginUsers.setSessionToken(sessionToken);
      loginUsers.setRoleId("");
      loginUsers.setUserId(userId);
      
      loginUsers.setLoginTime(new Date());
      
      try {
        logic.addLoginUser(dbConn, loginUsers);
      } catch (Exception e) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "其他系统集成用户登录失败!");
        return "/core/inc/rtjson.jsp";
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + sessionToken + "\"");
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getLoginBg(HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSystemLogic logic = new YHSystemLogic();
      String path = logic.getLoginBg(dbConn);
      String sp = System.getProperty("file.separator");
      path = YHSysProps.getAttachPath() + sp + "system" + sp + path;
      File file = new File(path);
      if (file.exists() && file.isFile()) {
        FileInputStream fis = new FileInputStream(file);
        response.setContentType("image/" + path.replaceAll(".*\\.", ""));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];  
        int i = 0;  
        while((i = fis.read(b)) > 0) {
        out.write(b, 0, i);  
        }
        out.flush();
      } else {
        String template = YHSystemAct.queryTemplate(request);
        return template = "/core/templates/" + template + "/img/login_bg.jpg";
      }
    } catch (Exception ex) {
      String template = YHSystemAct.queryTemplate(request);
      return template = "/core/templates/" + template + "/img/login_bg.jpg";
    }
    return "";
  }
  
  public static int getStyleIndex (HttpServletRequest request) {
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      
      int styleIndex = YHSystemLogic.getStyleIndex(dbConn);

      if (styleIndex == 0){
        if (person != null) {
          styleIndex = Integer.parseInt(person.getTheme());
          
          if (styleIndex == 0){
            styleIndex = Integer.parseInt(person.getTheme());
          }
        }
        else {
          styleIndex = 1;
        }
      }
      return styleIndex;
    } catch (Exception e) {
      return 1;
    }
  }
  
  public static String queryTemplate(HttpServletRequest request) {
    Connection dbConn = null;
    String template = "default";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      template = YHSystemLogic.queryTemplate(dbConn);
      if (template == null || "".equals(template.trim())) {
        template = "default";
      }
    } catch (Exception e) {
      
    }
    return template;
  }
  
  /**
   * 解析目录url的格式,比如http:// | ftp:// | javascript: 等的情况处理
   * @param url
   * @param contextPath
   * @return
   */
  public static String parseMenuUrl(String url, String contextPath, HttpServletRequest request) {
    
    if (url.startsWith("/")) {
      return contextPath + url;
    }
    else if (url.toLowerCase().startsWith("javascript:")) {
      return url;
    }
    else if (url.contains("://")) {
      return url;
    }
    else if (url.startsWith("@1/")) {
      //erp菜单使用
      return "/yherp" + url.replaceFirst("@1", "");
    }
    else if (url.startsWith("@2/")) {
      //报表的路径问题
      String u = request.getRequestURL().toString();
      if (YHUtility.isNullorEmpty(u)) {
        return url;
      }
      String host = u.substring(0, u.indexOf("/yh/yh/"));
      if (YHUtility.isNullorEmpty(host)) {
        return url;
      }
      String port = YHSysProps.getProp("REPORT_SSO_PORT");
      if (YHUtility.isNullorEmpty(port)) {
        return url;
      }
      int tmpInt = host.indexOf(":", 5);
      if (tmpInt > 0) {
        host = host.substring(0, tmpInt);
      }
      return host + ":" + port + "/" + url.replaceFirst("@2/", "");
    }
    else if (url.startsWith("@3/")) {
      //报表的路径问题      
      String u = request.getRequestURL().toString();
      if (YHUtility.isNullorEmpty(u)) {
        return url;
      }
      String host = u.substring(0, u.indexOf("/yh/yh/"));
      if (YHUtility.isNullorEmpty(host)) {
        return url;
      }
      String port = YHSysProps.getProp("OA_SSO_PORT");
      if (YHUtility.isNullorEmpty(port)) {
        return url;
      }
      int tmpInt = host.indexOf(":", 5);
      if (tmpInt > 0) {
        host = host.substring(0, tmpInt);
      }
      return host + ":" + port + "/" + url.replaceFirst("@3/", "");
    }
    else {
      return contextPath + YHSystemAct.DEFAULT_PATH + url;
    }
  }
  
  /**
   * 解析目录url的格式,比如http:// | ftp:// | javascript: 等的情况处理
   * @param url
   * @param contextPath
   * @return
   */
  public static String parseMenuUrl(String url, String contextPath) {
    return parseMenuUrl(url, contextPath, null);
  }
  
  /**
   * 当菜单的图标不存在时,赋给一个默认的图标
   * @param menu
   */
  public static void parseMenuIcon(YHMenu menu) {
    parseMenuIcon(menu, IMAGE_PATH);
  }
  
  public static void parseMenuIcon(YHMenu menu, String folder) {
    if (menu != null) {
      String iconSrc = menu.getIcon();
      if (folder.startsWith("/")) {
        folder = YHSysProps.getWebPath() + folder;
      }
      File icon = new File(folder + iconSrc);
      if (!icon.exists() || !icon.isFile()) {
        menu.setIcon("default.png");
      }
    }
  }
}
