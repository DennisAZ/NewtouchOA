<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId %>";
function doInit() {
  var content  = opener.$("docContent-" + seqId).value;
  $('content').update(content);
}
</script>
</head>
<body onload="doInit()">
<table width="90%" cellspacing="0" cellpadding="3" bordercolor="#b8d1e2" border="1" align="center" class="TableList" style="border-collapse: collapse;"><tr class="TableHeader"><td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> 正文</div></td></tr>
<tr><td align="left" id="content"></td></tr>
</table>
</body>
</html>