<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="yh.core.global.YHSysProps" %>
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
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
oval {behavior: url(#default#VML);position:absolute}
</STYLE>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function createVml(){
  var ss = "<oval style=\"left:200px;top:0px;width:100px;height:50px\" fillcolor=\"blue\" stroked=\"f\"></oval>";
  var oval = document.createElement(ss);
 document.body.appendChild(oval);
}
</script>
</HEAD>
<BODY  style="height:600px" >
  <oval style="left:300px;top:60px;width:100px;height:50px" fillcolor="blue" stroked="f"></oval>
</BODY>
</HTML>