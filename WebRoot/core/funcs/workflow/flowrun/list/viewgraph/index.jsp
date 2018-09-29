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
String flowId = request.getParameter("flowId");

%>
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
oval {FONT-SIZE: 12px;behavior: url(#default#VML);}
shadow {FONT-SIZE: 12px;behavior: url(#default#VML);}
textbox {FONT-SIZE: 12px;behavior: url(#default#VML);}
roundrect {FONT-SIZE: 12px;behavior: url(#default#VML);}
shapetype {FONT-SIZE: 12px;behavior: url(#default#VML);}
stroke {FONT-SIZE: 12px;behavior: url(#default#VML);}
path {FONT-SIZE: 12px;behavior: url(#default#VML);}
shape {FONT-SIZE: 12px;behavior: url(#default#VML);}
line {FONT-SIZE: 12px;behavior: url(#default#VML);}
</STYLE>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script language="JavaScript" src="flowdesigner.js"></script>
<script language="JavaScript">
var flowId = '<%=flowId%>';
var contextPath = '<%=contextPath%>';
function myinit(){
	window.location.href=contextPath+'/strawberry/demo/strawberry.jsp?flowId='+flowId;
}
</script>
</HEAD>
<BODY  style="height:600px" onload="createVml()"  onmousedown="DoRightClick();" oncontextmenu="nocontextmenu();">

</BODY>
</HTML>