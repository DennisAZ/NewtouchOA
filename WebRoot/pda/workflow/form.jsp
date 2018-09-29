<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
int flowId = Integer.parseInt(request.getParameter("flowId"));
int runId = Integer.parseInt(request.getParameter("runId"));
int prcsId = Integer.parseInt(request.getParameter("prcsId"));
int flowPrcs = Integer.parseInt(request.getParameter("flowPrcs"));
String js = (String) request.getAttribute("js");
String css = (String) request.getAttribute("css");
String form = (String) request.getAttribute("form");
String opFlag = (String) request.getAttribute("opFlag");
String feedback = (String) request.getAttribute("feedback");

String runName = (String) request.getAttribute("runName");
String attachmentId = (String) request.getAttribute("attachmentId") == null ? "" : (String) request.getAttribute("attachmentId");
String attachmentName = (String) request.getAttribute("attachmentName") == null ? "" : (String) request.getAttribute("attachmentName");
Date beginTime = (Date) request.getAttribute("beginTime");

String attachmentIds[] = attachmentId.split(",");
String attachmentNames[] = attachmentName.replace("*",",").split(",");
%>
<!doctype html>
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/list.css" />
<style>
table,td {
  border-collapse : collapse;
  border : 1px solid black;
  
}
<%= css%>
</style>
</head>
<body>
<div id="list_top">
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%= loginPerson.getSeqId()%>"></a></div>
  <span class="list_top_center">工作流 </span>
</div>
<div id="list_main" class="list_main">
<b>名称/文号</b>:<%=runName %><br>
<b>流水号</b>:<%=runId %><br>
<b>流程开始</b>:<%=beginTime.toString().substring(0,19) %><br>
<b>附件</b>:
<%
for(int i = 0; i < attachmentNames.length; i++){
%>
  <a href="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?module=workflow&attachmentId=<%=attachmentIds[i] %>&attachmentName=<%=attachmentNames[i] %>"><%=attachmentNames[i] %></a>&nbsp;&nbsp;&nbsp;&nbsp;
<%
}
%>
<br>
<hr>
<div id="form" style="margin-top:5px;margic-bottom:5px;padding-bottom:5px"><%=form %></div>
<hr>
<%
if("1".equals(opFlag)){
%>
<a class="ButtonA" href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/turn.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">转交</a>&nbsp;
<%
}
if(!"1".equals(feedback)){
%>
<a class="ButtonA" href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/sign.act?P=<%=loginPerson.getSeqId() %>&flowId=<%=flowId %>&runId=<%=runId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>">会签</a>
<%
}
%>
</div>
<div id="list_bottom">
    <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>