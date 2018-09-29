<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
String flowId = request.getParameter("flowId");

%>
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
vml\:* { FONT-SIZE: 12px; BEHAVIOR: url(#VMLRender) }
</STYLE>
<link rel="stylesheet" href = "/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script language="JavaScript" src="set_main.js"></script>
<script language="JavaScript">
var flowId = '<%=flowId%>';
var contextPath = '<%=contextPath%>';
</script>
</HEAD>
<BODY  style="height:600px" onload="createVml()"  onmousedown="DoRightClick();" oncontextmenu="nocontextmenu();">

</BODY>
</HTML>