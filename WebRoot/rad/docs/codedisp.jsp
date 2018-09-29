<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String pageUrl = request.getParameter("page");
%>
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var pageUrl = "<%=pageUrl%>";
function doInit() {
  var rtText = getTextRs(contextPath + "/yh/rad/docs/act/YHCodeTrnsAct/trnsCode.act?page=" + pageUrl);
  $("codeDisp").innerHTML = rtText;
}
</script>
</head>

<body onload="doInit();">
<br/>
<input value="返回" onclick="history.go(-1)" type="button" class="SmallButton"/>
<div id="codeDisp">
</div>
</body>
</html>