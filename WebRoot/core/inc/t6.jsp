<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.core.servlet.YHServletUtility" %>
<%@ page import="yh.core.global.YHActionKeys" %>
<%@ page import="yh.core.global.YHConst" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%@ page import="yh.core.global.YHSysPropKeys" %>
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
  
  String stylePath = contextPath + "/uifrm/styles/style" + styleIndex;
  String imgPath = stylePath + "/img";
  String jsPath = contextPath + "/uifrm/js";
  String cssPath = stylePath + "/css";
  String fullContextPath = YHServletUtility.getWebAppDir(this.getServletConfig().getServletContext());
  String ssoGPower = YHSysProps.getString("ssourl.gpower");
  String isOnlineEval = YHSysProps.getString("IS_ONLINE_EVAL");
  String signFileServiceUrl  = YHSysProps.getString("signFileServiceUrl");//主题标引服务地址
  int maxUploadSize = YHSysProps.getInt("maxUploadFileSize");
%>
<script type="text/javascript">
var contextPath = "<%=contextPath %>";
var imgPath = "<%=imgPath %>";
var jsPath = "<%=jsPath%>";
var ssoUrlGPower = "<%=ssoGPower%>";
var limitUploadFiles = "<%=limitUploadFiles%>"
var signFileServiceUrl = "<%=signFileServiceUrl%>";
var isOnlineEval = "<%=isOnlineEval%>";
var useSearchFunc = "<%=useSearchFunc%>";
var maxUploadSize = <%=maxUploadSize%>;
var isDev = "<%=isDev%>";
</script>