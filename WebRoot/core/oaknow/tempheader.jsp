<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.core.servlet.YHServletUtility" %>
<%@ page import="yh.core.global.YHActionKeys" %>
<%@ page import="yh.core.global.YHConst" %>

<%!
  private Logger log = Logger.getLogger("yzq." + this.getClass().getName());
%>
<%
	String contextPath = request.getContextPath();
  if (contextPath.equals("")) {
    contextPath = "/yh";
  }
	int styleIndex = 1;
	String stylePath = contextPath + "/core/styles/style" + styleIndex;
	String imgPath = stylePath + "/img";
	String cssPath = stylePath + "/css";
	String fullContextPath = YHServletUtility.getWebAppDir(this.getServletConfig().getServletContext());
	
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

</script>