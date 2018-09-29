<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.core.servlet.YHServletUtility" %>
<%@ page import="yh.core.global.YHActionKeys" %>
<%@ page import="yh.core.global.YHConst" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%@ page import="yh.core.global.YHSysPropKeys" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%!
  private Logger log = Logger.getLogger("yzq." + this.getClass().getName());
%>
<%
  String contextPath = request.getContextPath();
  if (contextPath.equals("")) {
    contextPath = "/yh";
  }
  String limitUploadFiles  = YHSysProps.getProp("limitUploadFiles");
  String useSearchFunc = YHSysProps.getProp("useSearchFunc");
  String isDev = YHSysProps.getProp("isDevelopContext"); 
  if (YHUtility.isNullorEmpty(isDev)) { 
    isDev = "0"; 
  }
  //获取主题的索引号
  int styleIndex = 1;
  Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
  if (styleInSession != null) {
    styleIndex = styleInSession;
  }
  
  
  String stylePath = contextPath + "/core/styles/style" + styleIndex;
  String imgPath = stylePath + "/img";
  String cssPath = stylePath + "/css";
  
  String oaStyle = (String)request.getSession().getAttribute("OA_STYLE");
  if(YHUtility.isNullorEmpty(oaStyle)){
    oaStyle = "1";
  }
  String cssPathOA = contextPath + "/core/frame/5/styles/style"+oaStyle+"/css";
  String fullContextPath = YHServletUtility.getWebAppDir(this.getServletConfig().getServletContext());
  String ssoGPower = YHSysProps.getString("ssourl.gpower");
  String isOnlineEval = YHSysProps.getString("IS_ONLINE_EVAL");
  String signFileServiceUrl  = YHSysProps.getString("signFileServiceUrl");//主题标引服务地址
  int maxUploadSize = YHSysProps.getInt("maxUploadFileSize");
%>
<script type="text/javascript">
/** 常量定义 **/
var TDJSCONST = {
  YES: 1,
  NO: 0
};
/** 变量定义 **/
var contextPath = "<%=contextPath %>";
var imgPath = "<%=imgPath %>";
var ssoUrlGPower = "<%=ssoGPower%>";
var limitUploadFiles = "<%=limitUploadFiles%>"
var signFileServiceUrl = "<%=signFileServiceUrl%>";
var isOnlineEval = "<%=isOnlineEval%>";
var useSearchFunc = "<%=useSearchFunc%>";
var maxUploadSize = <%=maxUploadSize%>;
var isDev = "<%=isDev%>";
var ostheme = "<%=oaStyle %>";
</script>