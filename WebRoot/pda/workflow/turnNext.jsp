<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.workflow.data.YHPdaFlowProcess" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
Map<String,String> op = (Map) request.getAttribute("op");
Map<String,String> other = (Map) request.getAttribute("other");
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
String flowName = (String) request.getAttribute("flowName");
String flowType = (String) request.getAttribute("flowType");
String runName = (String) request.getAttribute("runName");
String beginUserName = (String) request.getAttribute("beginUserName");
String parentRun = (String) request.getAttribute("parentRun");
int flowId = (Integer)request.getAttribute("flowId");
int runId = (Integer)request.getAttribute("runId");
int prcsId = (Integer)request.getAttribute("prcsId");
int flowPrcs = (Integer)request.getAttribute("flowPrcs");
String prcsIdNext = (String)request.getAttribute("prcsIdNext");
%>
<!doctype html>
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/list.css" />
</head>
<body>
<div id="list_top">
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/search.act?P=<%= loginPerson.getSeqId()%>"></a></div>
  <span class="list_top_center">工作转交</span>
  <div class="list_top_right"><a class="ButtonA" href="javascript:document.form1.submit();">提交</a></div>
</div>
<div id="list_main" class="list_main">
工作名称/文号：<%=runName %><br>
发起人：<%=beginUserName %><br>
请选择办理人员：<br>
主办人：
<form action="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/turnSubmit.act"  method="post" name="form1">
<select name="prcsUser_<%=prcsIdNext %>">
<%
Set<String> set = op.keySet(); 
if (set != null) {
for (String s:set) {
  String userName = (String) op.get(s);
%>
<option value="<%=s %>"><%=userName %></option>
<%
}}
%>
</select><br>
经办人：
<%
Set<String> set2 = other.keySet(); 
int i = 0;
if (set2 != null) {
for (String s:set2) {
  String userName = (String) other.get(s);
%>
<input type="checkbox" value="<%=s %>" name="prcsOpUser_<%=prcsIdNext %>"><%=userName %>&nbsp;
<%
i++;
}  
}
%>
<input type="hidden" name="P" value="<%=loginPerson.getSeqId() %>">
<input type="hidden" name="flowId" value="<%=flowId %>">
<input type="hidden" name="runId" value="<%=runId %>">
<input type="hidden" name="prcsId" value="<%=prcsId %>">
<input type="hidden" name="flowPrcs" value="<%=flowPrcs %>">
<input type="hidden" name="prcsIdNext" value="<%=prcsIdNext %>">
</form>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>