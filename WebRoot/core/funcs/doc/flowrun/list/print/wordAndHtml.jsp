<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.net.URLEncoder" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%

String ext = (String)request.getAttribute("ext");
String runName = (String)request.getAttribute("runName");
runName = URLEncoder.encode(runName, "UTF-8");
if ("doc".equals(ext)) {
  response.setHeader("Cache-control","private");
  response.setHeader("Accept-Ranges","bytes");
  response.setContentType("application/msword");
  response.setHeader("Content-Disposition", "attachment;filename=" + runName + ".doc");
} else  {
  response.setHeader("Cache-control","private");
  response.setHeader("Accept-Ranges","bytes");
  response.setContentType("application/octet-stream");
  response.setHeader("Content-Disposition", "attachment;filename=" + runName + ".html");
}
String form = (String)request.getAttribute("form");
String attachment = (String)request.getAttribute("attachment");
String prcs = (String)request.getAttribute("prcs");
String feedback = (String)request.getAttribute("feedback");
%>
<html>
<head>
<title>表单打印</title>

</head>
<body>
<% if (form != null ) { %>
<div id="form" style="margin-top:5px;margic-bottom:5px">
<%=form %>
</div>
<%} %>
<% if (attachment != null ) { %>
<div id="attachment">
<table width='100%'>
<tr class=TableHeader><td colspan=3>公共附件</td></tr>
<tbody id="attachmentsList">
<%=attachment %>
</tbody>
</table>
</div>
<%} %>
<% if (feedback != null ) { %>
<div id="feedBack">
<table width='100%'>
<tr class=TableHeader><td colspan=3>会签与点评</td></tr>
<tbody id="feedbackList">
<%=feedback %>
</tbody>
</table>
</div>
<%} %>
<% if (prcs != null ) { %>
<div id="prcss">
<table width='100%'>
<tr class=TableHeader><td colspan=3>流程图</td></tr>
<tbody id="listTbody">
<%=prcs %>
</tbody>
</table>
</div>
<%} %>
</body>
</html>