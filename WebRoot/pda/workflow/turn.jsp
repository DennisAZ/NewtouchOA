<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.workflow.data.YHPdaFlowProcess" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
String flowName = (String) request.getAttribute("flowName");
String flowType = (String) request.getAttribute("flowType");
String runName = (String) request.getAttribute("runName");
String beginUserName = (String) request.getAttribute("beginUserName");
String parentRun = (String) request.getAttribute("parentRun");
List flowProcesses = (List) request.getAttribute("flowProcesses");
int flowId = Integer.parseInt(request.getParameter("flowId"));
int runId = Integer.parseInt(request.getParameter("runId"));
int prcsId = Integer.parseInt(request.getParameter("prcsId"));
int flowPrcs = Integer.parseInt(request.getParameter("flowPrcs"));
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
  <div class="list_top_right"><a class="ButtonA" href="javascript:document.form1.submit();">继续</a></div>
</div>
<div id="list_main" class="list_main">
工作名称/文号：<%=runName %><br>
发起人：<%=beginUserName %><br>
请选择下一步骤：<br>
<form action="<%=contextPath %>/yh/pda/workflow/act/YHPdaWorkflowAct/turnNext.act" method="post" name="form1">
<%
int count = flowProcesses.size();
for(int i = 0 ; i < count ; i++){
  YHPdaFlowProcess flowProcess = (YHPdaFlowProcess)flowProcesses.get(i);
  int prcsIdNext = flowProcess.getPrcsId();
  String prcsName = flowProcess.getPrcsName();
  String prcsIn = flowProcess.getPrcsIn();
  String prcsInSet = flowProcess.getPrcsInSet();
  String conditionDesc = flowProcess.getConditionDesc();
  String userLock = flowProcess.getUserLock();
  String topDefault = flowProcess.getTopDefault();
  int childFlow = flowProcess.getChildFlow();
  int autoBaseUser = flowProcess.getAutoBaseUser();
%>
<input type="radio" name="prcsIdNext" id="prcs_<%=prcsIdNext %>" value="<%=prcsIdNext %>" <%if(i==0){%> checked<%} %>><label for="prcs_<%=prcsIdNext %>"><%=prcsName %></label><br />
<%
}
%>
<input type="hidden" name="P" value="<%=loginPerson.getSeqId() %>">
<input type="hidden" name="flowId" value="<%=flowId %>">
<input type="hidden" name="runId" value="<%=runId %>">
<input type="hidden" name="prcsId" value="<%=prcsId %>">
<input type="hidden" name="flowPrcs" value="<%=flowPrcs %>">
</form>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>