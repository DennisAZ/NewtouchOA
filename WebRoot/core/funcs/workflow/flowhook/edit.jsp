<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String hid = request.getParameter("hid");

%>
<html>
<head>
<title>编辑引擎</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript">

</script>

</head>



<body onload="doInit()">
<form method="post" id="hookForm" name="hookForm">
<input type="hidden" value="0" name="openeddata" id="openeddata">
<input type="hidden" value="0" name="openedplugin" id="openedplugin">

<input type="hidden" value="0" name="system" id="system">
<input type="hidden" value="<%=hid %>" name="hid" id="hid">
<div id="contentDiv"></div>
</form>
</body>
</html>