<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String flowIdStr = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function onresizedd() {
  var obj = $("docIframe");
  var h = document.viewport.getDimensions().height - 20;
  obj.style.height = h + "px";
  obj.style.width = "100%";
}
</script>
</head>
<body onload="onresizedd()" onresize="onresizedd()">
<iframe style="margin:0px;padding:0px"  frameborder=0 name="docIframe" id="docIframe" width="100%" height="100%" src="<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowProcessAct/getProcessList1.act?flowId=<%=flowIdStr %>&type=1"></iframe>
</body>
</html>