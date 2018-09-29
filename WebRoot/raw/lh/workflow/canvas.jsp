<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
%>
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title>流程设计</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
vml\:* { FONT-SIZE: 12px; BEHAVIOR: url(#VMLRender) }
</STYLE>
<link rel="stylesheet" href = "/style.css">
<script language="JavaScript" src="flowdesigner.js"></script>

</HEAD>
<BODY  style="height:600px" onload="createVml()"  onmousedown="DoRightClick();" oncontextmenu="nocontextmenu();">

</BODY>
</HTML>