<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String attachmentName = request.getParameter("attachmentName");
String attachmentId = request.getParameter("attachmentId");
String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript">
var attachmentName = "<%=attachmentName %>";
var attachmentId = "<%=attachmentId %>";
function createDisp(){
  var param = "attachmentName=" + encodeURIComponent(attachmentName) + "&attachmentId=" + attachmentId + "&module=workFlow";
  var url = "http://<%=host %>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?" + param;
  var obj = $("HWPostil1");
  obj.LoadFile(url);
  return;
}
</script>
</head>
<body>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
createDisp();
</SCRIPT>
 <OBJECT id=HWPostil1 style="WIDTH:800px;HEIGHT:600px" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/core/cntrls/HWPostil.cab#version=3,0,7,0' >"
 </OBJECT>
</body>
</html>