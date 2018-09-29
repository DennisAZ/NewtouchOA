<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="yh.core.global.YHActionKeys"%>
<%
  String returnState = (String)request.getAttribute(
      YHActionKeys.RET_STATE);
  String rtMsrg = (String)request.getAttribute(
    YHActionKeys.RET_MSRG);  
  String retData = (String)request.getAttribute(
      YHActionKeys.RET_DATA);
  if (retData == null) {
    retData = "";
  }
  retData = retData.replace("\\", "/").replaceAll("\"", "\\\\\"");
  String fileForm = (String)request.getAttribute("fileForm");
  if (YHUtility.isNullorEmpty(fileForm)) {
    fileForm = "fileForm";
  }
%>
<script>
var returnState = "<%=returnState%>";
var fileForm = "<%=fileForm%>";
/**
 * 处理页面加载
 */
function doInit() {
  if (returnState == 0) {
    parent.currFormFile = fileForm;
    parent.removeAllFile();
  }
  parent.handleSingleUpload(returnState, "<%=rtMsrg %>", "<%=retData%>");
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body onload="doInit()">
</body>
</html>